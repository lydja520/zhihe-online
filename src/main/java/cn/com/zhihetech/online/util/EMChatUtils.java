package cn.com.zhihetech.online.util;

import cn.com.zhihetech.online.bean.ChatMessage;
import cn.com.zhihetech.online.bean.MessageBody;
import cn.com.zhihetech.online.bean.MessagePayload;
import cn.com.zhihetech.online.commons.AppConfig;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.HTTPMethod;
import cn.com.zhihetech.online.commons.Roles;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.vo.ClientSecretCredential;
import cn.com.zhihetech.online.vo.Credential;
import cn.com.zhihetech.online.vo.EndPoints;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.sun.istack.internal.NotNull;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeType;
import org.glassfish.jersey.client.JerseyWebTarget;

import java.io.IOException;
import java.util.*;

/**
 * Created by ShenYunjie on 2016/4/26.
 */
public class EMChatUtils {

    private final static Credential credential = new ClientSecretCredential(AppConfig.EasemobConfig.APP_CLIENT_ID, AppConfig.EasemobConfig.APP_CLIENT_SECRET, Roles.USER_ROLE_APPADMIN);
    private final static String ORG_NAME = AppConfig.EasemobConfig.EM_APP_KEY.split("#")[0];
    private final static String APP_NAME = AppConfig.EasemobConfig.EM_APP_KEY.split("#")[1];
    private final static JsonNodeFactory factory = new JsonNodeFactory(false);

    public EMChatUtils() {
    }

    /**
     * 分页按游标导出指定时间段内的消息
     *
     * @param startTimestamp //开始时间戳
     * @param endTimestamp   //结束时间戳
     * @param limit          //每页条数
     * @param cursor         //游标用于分页
     * @return MessagesResult
     */
    public ChatMessageResult exportChatMessages(Long startTimestamp, Long endTimestamp, Integer limit, String cursor) {
        StringBuilder ql = new StringBuilder("select *");
        if (startTimestamp != null && endTimestamp != null) {
            ql.append(" ").append(" where timestamp > ").append(startTimestamp).append(" and timestamp < ").append(endTimestamp);
        } else if (startTimestamp != null) {
            ql.append(" ").append(" where timestamp > ").append(startTimestamp);
        } else if (endTimestamp != null) {
            ql.append(" ").append(" where timestamp < ").append(endTimestamp);
        }
        limit = limit == null ? 200 : limit; //默认20条
        ObjectNode queryNode = factory.objectNode()
                .put("ql", ql.toString())
                .put("limit", String.valueOf(limit));
        if (!cn.com.zhihetech.util.common.StringUtils.isEmpty(cursor)) {
            queryNode.put("cursor", cursor);
        }
        ObjectNode objectNode = getChatMessages(queryNode);
        if (objectNode == null) {
            return null;
        }
        return createMessageResultWithObjectNode(objectNode);
    }

    protected ChatMessageResult createMessageResultWithObjectNode(ObjectNode objectNode) {
        ChatMessageResult messageResult = new ChatMessageResult();
        messageResult.setCount(objectNode.get("count").asLong());
        JsonNode cursorNode = objectNode.get("cursor");
        if (cursorNode != null) {
            messageResult.setCursor(cursorNode.toString());
        }
        messageResult.setStatusCode(objectNode.get("statusCode").asInt());
        messageResult.setTimestamp(objectNode.get("timestamp").asLong());
        messageResult.setDuration(objectNode.get("duration").asLong());
        if (messageResult.getCount() <= 0) {
            return messageResult;
        }
        ArrayNode arrayNode = objectNode.withArray("entities");
        List<ChatMessage> messages = new ArrayList<>(arrayNode.size());
        for (JsonNode jsonNode : arrayNode) {
            ChatMessage message = JSONObject.parseObject(jsonNode.toString(), ChatMessage.class);
            MessagePayload messagePayload = new MessagePayload();
            JsonNode payloadNode = jsonNode.get("payload");
            if (payloadNode.getNodeType() == JsonNodeType.OBJECT) {
                JsonNode extNode = payloadNode.get("ext");
                if (extNode != null) {
                    Map tmp = JSONObject.parseObject(extNode.toString(), HashMap.class);
                    Map<String, String> ext = new HashMap<>();
                    for (Object key : tmp.keySet()) {
                        String _key = String.valueOf(key);
                        ext.put(_key, String.valueOf(tmp.get(key)));
                    }
                    messagePayload.setExt(ext);
                }
                ArrayNode bodies = (ArrayNode) payloadNode.get("bodies");
                if (bodies != null && bodies.size() > 0) {
                    List<MessageBody> bodyList = new ArrayList<>();
                    for (JsonNode bodyNode : bodies) {
                        MessageBody body = JSONObject.parseObject(bodyNode.toString(), MessageBody.class);
                        bodyList.add(body);
                    }
                    messagePayload.setBodies(bodyList);
                }
                message.setPayload(messagePayload);
            }
            messages.add(message);
        }
        messageResult.setEntities(messages);
        return messageResult;
    }

    /**
     * 获取聊天消息
     *
     * @param queryStrNode
     */
    protected ObjectNode getChatMessages(ObjectNode queryStrNode) {
        String APPKEY =AppConfig.EasemobConfig.EM_APP_KEY;
        // 通过app的client_id和client_secret来获取app管理员token

        ObjectNode objectNode = factory.objectNode();
        // check appKey format
        if (!JerseyUtils.match("^(?!-)[0-9a-zA-Z\\-]+#[0-9a-zA-Z]+", APPKEY)) {
            objectNode.put("message", "Bad format of Appkey");
            return objectNode;
        }
        try {
            JerseyWebTarget webTarget = EndPoints.CHATMESSAGES_TARGET.resolveTemplate("org_name", ORG_NAME)
                    .resolveTemplate("app_name", APP_NAME);
            if (null != queryStrNode && null != queryStrNode.get("ql") && !StringUtils.isEmpty(queryStrNode.get("ql").asText())) {
                webTarget = webTarget.queryParam("ql", queryStrNode.get("ql").asText());
            }
            if (null != queryStrNode && null != queryStrNode.get("limit") && !StringUtils.isEmpty(queryStrNode.get("limit").asText())) {
                webTarget = webTarget.queryParam("limit", queryStrNode.get("limit").asText());
            }
            if (null != queryStrNode && null != queryStrNode.get("cursor") && !StringUtils.isEmpty(queryStrNode.get("cursor").asText())) {
                webTarget = webTarget.queryParam("cursor", queryStrNode.get("cursor").asText());
            }
            objectNode = JerseyUtils.sendRequest(webTarget, null, credential, HTTPMethod.METHOD_GET, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return objectNode;
    }

    /**
     * 创建聊天室
     *
     * @param name        聊天室名称
     * @param description 聊天室描述
     * @param owner       聊天室创建者
     * @param members     聊天室成员
     * @return
     */
    public static ObjectNode createChatRoom(@NotNull String name, String description,
                                            @NotNull String owner, List<String> members) throws IOException {
        members = members == null ? new ArrayList<>() : members;
        if (members.isEmpty()) {
            members.add(owner);
        }
        ArrayNode memberArray = JsonNodeFactory.instance.arrayNode();
        for (String member : members) {
            memberArray.add(member);
        }
        ObjectNode dataObjectNode = JsonNodeFactory.instance.objectNode();
        dataObjectNode.put("name", name)
                .put("description", description)
                .put("owner", owner)
                .put("members", memberArray);
        JerseyWebTarget webTarget = EndPoints.CHATROOMS_TARGET.resolveTemplate("org_name", ORG_NAME).resolveTemplate("app_name", APP_NAME);
        ObjectNode objectNode = JerseyUtils.sendRequest(webTarget, dataObjectNode, credential, HTTPMethod.METHOD_POST, null);
        return objectNode;
    }

    /**
     * 删除指定聊天室
     *
     * @param chatRoomId
     * @return
     */
    public static ObjectNode deleteChatRoom(String chatRoomId) throws IOException {
        if (StringUtils.isEmpty(chatRoomId)) {
            throw new SystemException("delete chatRoom is not able null");
        }
        JerseyWebTarget webTarget = EndPoints.CHATROOMS_TARGET.resolveTemplate("org_name", ORG_NAME)
                .resolveTemplate("app_name", APP_NAME).path(chatRoomId);
        ObjectNode objectNode = null;
        objectNode = JerseyUtils.sendRequest(webTarget, null, credential, HTTPMethod.METHOD_DELETE, null);
        return objectNode;
    }

    /**
     * 添加指定用户到指定的聊天室
     *
     * @param roomId 聊天室 ID
     * @param userId 用户环信账号
     * @return
     * @throws IOException
     */
    private ObjectNode addUserToChatRoom(String roomId, String userId) throws IOException {

        ObjectNode objectNode = null;

        if (cn.com.zhihetech.util.common.StringUtils.isEmpty(roomId)) {
            return objectNode;
        }
        if (cn.com.zhihetech.util.common.StringUtils.isEmpty(userId)) {
            return objectNode;
        }

        JerseyWebTarget webTarget = EndPoints.CHATROOMS_USERS_TARGET.resolveTemplate("org_name", ORG_NAME)
                .resolveTemplate("app_name", APP_NAME).resolveTemplate("chatroomid", roomId).path(userId);
        objectNode = JerseyUtils.sendRequest(webTarget, null, credential, HTTPMethod.METHOD_POST, null);

        return objectNode;
    }
}
