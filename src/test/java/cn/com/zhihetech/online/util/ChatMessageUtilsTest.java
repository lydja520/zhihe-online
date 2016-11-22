package cn.com.zhihetech.online.util;

import cn.com.zhihetech.online.commons.AppConfig;
import cn.com.zhihetech.online.commons.Constant;
import cn.com.zhihetech.online.commons.HTTPMethod;
import cn.com.zhihetech.online.commons.Roles;
import cn.com.zhihetech.online.service.IChatMessageService;
import cn.com.zhihetech.online.vo.ClientSecretCredential;
import cn.com.zhihetech.online.vo.Credential;
import cn.com.zhihetech.online.vo.EndPoints;
import com.fasterxml.jackson.databind.node.ObjectNode;
import junit.framework.TestCase;
import org.glassfish.jersey.client.JerseyWebTarget;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Administrator on 2016/3/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class ChatMessageUtilsTest extends TestCase {

    @Resource(name = "chatMessageService")
    private IChatMessageService chatMessageService;

    @Test
    public void testChatRoom() throws Exception {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        Credential credential = new ClientSecretCredential(AppConfig.EasemobConfig.APP_CLIENT_ID, AppConfig.EasemobConfig.APP_CLIENT_SECRET, Roles.USER_ROLE_APPADMIN);
        JerseyWebTarget webTarget = EndPoints.CHATROOMS_TARGET.resolveTemplate("org_name", AppConfig.EasemobConfig.EM_APP_KEY.split("#")[0]).resolveTemplate("app_name", AppConfig.EasemobConfig.EM_APP_KEY.split("#")[1]);
        ObjectNode objectNode = JerseyUtils.sendRequest(webTarget, null, credential, HTTPMethod.METHOD_GET, null);
        System.out.println(objectNode);
    }

    @Test
    public void testDeleteChatRoom() throws IOException {
        ObjectNode objectNode = EMChatUtils.deleteChatRoom("193071503963587000");
        System.out.println(objectNode);
    }

    @Test
    public void testExportChatMessages() {
        EMChatUtils chatUtils = new EMChatUtils();
        ChatMessageResult result = chatUtils.exportChatMessages(null, System.currentTimeMillis(), 200, null);
        this.chatMessageService.executeImportMessages(result.getEntities());
        //System.out.println(result);
    }
}