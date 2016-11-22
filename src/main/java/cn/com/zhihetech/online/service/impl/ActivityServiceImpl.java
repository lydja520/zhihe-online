package cn.com.zhihetech.online.service.impl;

import cn.com.zhihetech.online.bean.*;
import cn.com.zhihetech.online.commons.*;
import cn.com.zhihetech.online.components.ActivityStartAndEndSchedule;
import cn.com.zhihetech.online.components.ActivityStartSmsReminder;
import cn.com.zhihetech.online.dao.*;
import cn.com.zhihetech.online.exception.SystemException;
import cn.com.zhihetech.online.service.*;
import cn.com.zhihetech.online.util.EMChatUtils;
import cn.com.zhihetech.online.vo.*;
import cn.com.zhihetech.util.common.GeneratedNRandom;
import cn.com.zhihetech.util.common.StringUtils;
import cn.com.zhihetech.util.hibernate.GeneralQueryParams;
import cn.com.zhihetech.util.hibernate.IQueryParams;
import cn.com.zhihetech.util.hibernate.commons.PageData;
import cn.com.zhihetech.util.hibernate.commons.Pager;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.glassfish.jersey.client.JerseyWebTarget;
import org.quartz.*;
import org.springframework.stereotype.Service;
import cn.com.zhihetech.online.util.JerseyUtils;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import javax.annotation.Resource;
import javax.ws.rs.QueryParam;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;

/**
 * Created by ShenYunjie on 2015/12/7.
 */
@Service("activityService")
public class ActivityServiceImpl implements IActivityService {

    private static final JsonNodeFactory factory = new JsonNodeFactory(false);
    private static final String APPKEY = AppConfig.EasemobConfig.EM_APP_KEY;
    private static final String QUQRTZ_ACTIVITY_JOB_START_GROUP = "activityJobStartGroup";
    private static final String QUQRTZ_ACTIVITY_JOB_OVER_GROUP = "activityJobOverGroup";

    // 通过app的client_id和client_secret来获取app管理员token
    private static Credential credential = new ClientSecretCredential(AppConfig.EasemobConfig.APP_CLIENT_ID, AppConfig.EasemobConfig.APP_CLIENT_SECRET, Roles.USER_ROLE_APPADMIN);

    @Resource(name = "activityDao")
    private IActivityDao activityDao;
    @Resource(name = "adminService")
    private IAdminService adminService;
    @Resource(name = "merchantAllianceDao")
    private IMerchantAllianceDao merchantAllianceDao;
    @Resource(name = "activityFansDao")
    private IActivityFansDao activityFansDao;
    @Resource(name = "activityInvitationService")
    private IActivityInvitationService activityInvitationService;
    @Resource(name = "activityGoodsDao")
    private IActivityGoodsDao activityGoodsDao;
    @Resource(name = "goodsDao")
    private IGoodsDao goodsDao;
    @Resource(name = "activityOrderDetailDao")
    private IActivityOrderDetailDao activityOrderDetailDao;
    @Resource(name = "goodsAttributeSetDao")
    private IGoodsAttributeSetDao goodsAttributeSetDao;
    @Resource(name = "goodsService")
    private IGoodsService goodsService;
    @Resource(name = "skuDao")
    private ISkuDao skuDao;
    @Resource(name = "schedulerFactory")
    private Scheduler scheduler;


    /**
     * 根据ID获取持久化对象
     *
     * @param id 对象ID
     * @return
     */
    @Override
    public Activity getById(String id) {
        return this.activityDao.findEntityById(id);
    }

    /**
     * 删除持久化对象
     *
     * @param activity 需要删除的持久化对象
     */
    @Override
    public void delete(Activity activity) {
        this.activityDao.deleteEntity(activity);
    }

    /**
     * 添加一个对象到数据库
     *
     * @param activity 需要持久化的对象
     * @return
     */
    @Override
    public Activity add(Activity activity) {
        return this.activityDao.saveEntity(activity);
    }

    /**
     * 更新一个持久化对象
     *
     * @param activity
     */
    @Override
    public void update(Activity activity) {
        if (!isEditAble(activity)) {
            throw new SystemException("当前活动信息不可更改");
        }
        this.activityDao.updateEntity(activity);
    }

    /**
     * 根据分页参数和查询条件查询持久化对象
     *
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return
     */
    @Override
    public List<Activity> getAllByParams(Pager pager, IQueryParams queryParams) {
        return this.activityDao.getEntities(pager, queryParams);
    }

    /**
     * 根据分页参数和查询条件查询持久化对象
     *
     * @param pager       分页参数
     * @param queryParams 查询条件
     * @return 经过分页的数据
     */
    @Override
    public PageData<Activity> getPageData(Pager pager, IQueryParams queryParams) {
        return this.activityDao.getPageData(pager, queryParams);
    }

    @Override
    public Activity saveOrUpdate(Activity activity) {
        if (!isEditAble(activity)) {
            throw new SystemException("当前活动信息不可更改");
        }
        return this.activityDao.saveOrUpdate(activity);
    }

    @Override
    public PageData<Activity> getPageDataByAdmin(Pager pager, GeneralQueryParams queryParams, Admin currentAdmin) {
        if (queryParams == null) {
            queryParams = new GeneralQueryParams();
        }
        Merchant merchant = this.adminService.getMerchant(currentAdmin);
        queryParams.andEqual("activityPromoter", merchant);
        return this.activityDao.getPageData(pager, queryParams);
    }

    //TODO:当添加新的活动时，添加一个和活动对应的活动订单，同时把活动发起者添加成一个活动订单详细-活动支出
    @Override
    public void saveByAdmin(Activity activity, Admin admin) {
        activity.setCreateDate(new Date());
        Merchant merchant = this.adminService.getById(admin.getAdminId()).getMerchant();
        if (merchant == null) {
            throw new SystemException("当前账号异常,请联系管理员");
        }
        activity.setActivityPromoter(merchant);
        activity.setActivityBudget(0f);
        this.activityDao.saveEntity(activity);
        MerchantAlliance merchantAlliance = new MerchantAlliance();
        merchantAlliance.setCreateDate(new Date());
        merchantAlliance.setActivity(activity);
        merchantAlliance.setAllianceDesc(activity.getActivitDesc());
        merchantAlliance.setAllianceName(activity.getActivitName());
        merchantAlliance.setMerchant(activity.getActivityPromoter());
        merchantAlliance.setAllianceState(MerchantAlliance.ALLIANCE_AGREED_STATE);   //活动发起人加入商家联盟默认为确认加入
        merchantAlliance.setPromoted(true); //活动发起人加入商家联盟默认为活动发起人
        merchantAlliance.setActivityBudget(0f);
        this.merchantAllianceDao.saveEntity(merchantAlliance);

        ActivityOrderDetail activityOrderDetail = new ActivityOrderDetail();
        activityOrderDetail.setActivityOrder(null);
        activityOrderDetail.setOrderType(ActivityOrderDetail.OrderType.activityCost);
        activityOrderDetail.setOrderTypeId(merchantAlliance.getAllianceId());
        activityOrderDetail.setPayState(false);
        activityOrderDetail.setMoney(Constant.MERCHNT_ACTIVITY_BUDGET);
        activityOrderDetail.setMerchantId(merchantAlliance.getMerchant().getMerchantId());
        activityOrderDetail.setActivityId(activity.getActivitId());
        this.activityOrderDetailDao.saveEntity(activityOrderDetail);
    }

    @Override
    public boolean isEditAble(Activity activity) {
        if (StringUtils.isEmpty(activity.getActivitId())) {
            return true;    //如果活动为新增则可编辑
        }
        int currentState = getCurrentSateById(activity.getActivitId());
        if (currentState == Constant.ACTIVITY_STATE_UNSBUMIT || currentState == Constant.ACTIVITY_STATE_EXAMINED_NOT) {
            return true;    //如果未提交或审核未通过则可以编辑
        }
        return false;
    }

    @Override
    public int getCurrentSateById(String activitId) {
        IQueryParams queryParams = new GeneralQueryParams().andEqual("activitId", activitId);
        List<Object[]> objectList = this.activityDao.getProperties(new String[]{"currentState", "activitId"}, null, queryParams);
        if (objectList.size() > 0) {
            Object[] objects = objectList.get(0);
            return (int) objects[0];
        }
        throw new SystemException("活动不存在!");
    }

    /**
     * 活动审核，如果活动审核通过，就创建一个活动聊天室，
     * 并把参与活动的商家和邀请的会员都加进来
     *
     * @param activityId
     * @param currentState
     * @param examinMsg
     */
    @Override
    public ResponseMessage updateActivityExaminState(String activityId, String currentState, String examinMsg) {
        ResponseMessage responseMessage = null;
        Activity activity = this.activityDao.findEntityById(activityId);
        if (activity.getCurrentState() != Constant.ACTIVITY_STATE_SBUMITED) {
            responseMessage.setCode(ResponseStatusCode.SUCCESS_CODE);
            responseMessage.setSuccess(false);
            responseMessage.setMsg("该活动状态不支持审核操作，请刷新网页后再试！");
            responseMessage.setAttribute(null);
            return responseMessage;
        }
        activity.setCurrentState(Integer.parseInt(currentState));
        activity.setExaminMsg(examinMsg);
        if (Integer.parseInt(currentState) == Constant.ACTIVITY_STATE_EXAMINED_NOT) {
            this.activityDao.updateEntity(activity);
            return responseMessage;
        }
        ObjectNode newChatRoomNode = JsonNodeFactory.instance.objectNode();
        String owner = (activity.getActivityPromoter().getMerchantId()).replaceAll("-", "");
        newChatRoomNode.put("name", activity.getActivitName());
        newChatRoomNode.put("description", "活动聊天室");
        newChatRoomNode.put("owner", owner);
        ArrayNode arrayNode = factory.arrayNode();
        IQueryParams queryParams = new GeneralQueryParams().andEqual("activity.activitId", activityId);
        List<ActivityFans> chatUsers = this.activityFansDao.getEntities(queryParams);
        if (chatUsers.size() > 0) {
            for (ActivityFans activityFans : chatUsers) {
                if (StringUtils.isEmpty(activityFans.getFans().getChatUserName())) {
                    continue;
                }
                arrayNode.add(activityFans.getFans().getChatUserName());
            }
        }
        queryParams = new GeneralQueryParams().andEqual("activity.activitId", activityId);
        List<MerchantAlliance> merchantAlliances = this.merchantAllianceDao.getEntities(queryParams);
        if (merchantAlliances.size() != 0) {
            for (MerchantAlliance merchantAlliance : merchantAlliances) {
                String memberId = merchantAlliance.getMerchant().getMerchantId().replaceAll("-", "");
                arrayNode.add(memberId);
            }
        }
        newChatRoomNode.put("members", arrayNode);
        ObjectNode createChatRoom = this.createChatRoom(newChatRoomNode);
        if (createChatRoom == null) {
            throw new SystemException("系统出错，请与管理员联系！");
        }
        startActivityScheduleJob(activity, chatUsers, merchantAlliances);
        Object idObj = createChatRoom.get("data").get("id");
        String chatRoomId = idObj.toString().replaceAll("\"", "");
        if (createChatRoom != null && chatRoomId != null) {
            activity.setChatRoomId(chatRoomId);
            this.activityDao.updateEntity(activity);
            return responseMessage;
        } else {
            throw new SystemException("创建聊天室失败!");
        }
    }

    /**
     * quartz创建定时活动任务
     *
     * @param activity
     */
    private void startActivityScheduleJob(Activity activity, List<ActivityFans> activityFanses, List<MerchantAlliance> merchantAlliances) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ss mm HH dd MM yyyy");
        Date activityStartTime = activity.getBeginDate();
        Date activityOverTime = activity.getEndDate();

        List<ActivityScheduleJob> activityScheduleJobs = new LinkedList<>();
        //这里获取任务信息数据
        ActivityScheduleJob activityScheduleJob = new ActivityScheduleJob();
        activityScheduleJob.setJobName(activity.getActivitId());
        activityScheduleJob.setJobGroup(this.QUQRTZ_ACTIVITY_JOB_START_GROUP);
        activityScheduleJob.setCronExpression(activityStartTime);
        activityScheduleJob.setStartFlag(true);
        activityScheduleJob.setActivity(activity);
        activityScheduleJobs.add(activityScheduleJob);

        ActivityScheduleJob activityScheduleJob1 = new ActivityScheduleJob();
        activityScheduleJob1.setJobName(activity.getActivitId());
        activityScheduleJob1.setJobGroup(this.QUQRTZ_ACTIVITY_JOB_OVER_GROUP);
        activityScheduleJob1.setCronExpression(activityOverTime);
        activityScheduleJob1.setStartFlag(false);
        activityScheduleJob1.setActivity(activity);
        activityScheduleJobs.add(activityScheduleJob1);

        for (ActivityScheduleJob job : activityScheduleJobs) {
            TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());
            SimpleTrigger trigger = null;
            try {
                trigger = (SimpleTrigger) scheduler.getTrigger(triggerKey);
            } catch (SchedulerException e) {
                throw new SystemException("quartz trigger 获取失败", e);
            }

            if (null == trigger) {
                JobDetail jobDetail = JobBuilder.newJob(ActivityStartAndEndSchedule.class).withIdentity(job.getJobName(), job.getJobGroup()).build();
                jobDetail.getJobDataMap().put("activityScheduleJob", job);
                //按新的SimpleTriggle Expression表达式构建一个新的trigger
                trigger = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup()).startAt(job.getCronExpression()).forJob(jobDetail).build();
                try {
                    scheduler.scheduleJob(jobDetail, trigger);
                } catch (SchedulerException e) {
                    throw new SystemException("quartz 任务创建失败", e);
                }
            } else {
                //按新的SimpleTriggle Expression表达式重新构建trigger
                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).startAt(job.getCronExpression()).build();
                //按新的trigger重新设置job执行
                try {
                    scheduler.rescheduleJob(triggerKey, trigger);
                } catch (SchedulerException e) {
                    throw new SystemException("quartz 任务创建失败", e);
                }
            }
        }

        List<String> phoneNumbers = new ArrayList<>();
        for (ActivityFans activityFans : activityFanses) {
            phoneNumbers.add(activityFans.getFans().getUserPhone());
        }
        for (MerchantAlliance merchantAlliance : merchantAlliances) {
            phoneNumbers.add(merchantAlliance.getMerchant().getContactMobileNO());
        }
        ActivityStartReminderJob activityStartReminderJob = new ActivityStartReminderJob();
        activityStartReminderJob.setPhoneNumbers(phoneNumbers);
        activityStartReminderJob.setStartDate(activityStartTime);

        Calendar cal = Calendar.getInstance();
        cal.setTime(activityStartTime);
        cal.add(Calendar.MINUTE, -30);
        Date reminderDate = cal.getTime();
        JobDetail jobDetail = JobBuilder.newJob(ActivityStartSmsReminder.class).withIdentity(activity.getActivitId(), "activityStartReminderGroup").build();
        SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger()
                .withIdentity(activity.getActivitId(), "activityStartReminderGroup")
                .startAt(reminderDate).forJob(jobDetail).build();
        jobDetail.getJobDataMap().put("activityStartReminderJob", activityStartReminderJob);
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new SystemException("活动开始提醒任务创建失败", e);
        }
    }

    /**
     * 创建聊天室
     *
     * @param dataObjectNode
     * @return
     */
    private ObjectNode createChatRoom(ObjectNode dataObjectNode) {
        ObjectNode objectNode = null;
        // check appKey format
        if (!JerseyUtils.match("^(?!-)[0-9a-zA-Z\\-]+#[0-9a-zA-Z]+", APPKEY)) {
            return null;
        }

        // check properties that must be provided
        if (!dataObjectNode.has("name")) {
            return null;
        }
        if (!dataObjectNode.has("description")) {
            return null;
        }
        if (!dataObjectNode.has("owner")) {
            return null;
        }
        if (dataObjectNode.has("members") && (!dataObjectNode.path("members").isArray() || 0 == dataObjectNode.path("members").size())) {
            return null;
        }
        try {
            JerseyWebTarget webTarget = EndPoints.CHATROOMS_TARGET.resolveTemplate("org_name", APPKEY.split("#")[0]).resolveTemplate("app_name", APPKEY.split("#")[1]);
            objectNode = JerseyUtils.sendRequest(webTarget, dataObjectNode, credential, HTTPMethod.METHOD_POST, null);
        } catch (Exception e) {
            return null;
        }
        return objectNode;
    }

    /**
     * 提交活动申请
     *
     * @param activityId
     */
    @Override
    public void updateSubmitState(String activityId) {

        /**
         * 判断是否还有未提交的商家(如果已同意参加的商家还未提交活动则不允许提交申请）
         */
        IQueryParams queryParams = new GeneralQueryParams().andEqual("activity.activitId", activityId)
                .andEqual("allianceState", MerchantAlliance.ALLIANCE_AGREED_STATE);
        List<Object> objects = this.merchantAllianceDao.getProperty("allianceId", null, queryParams);
        if (objects != null && objects.size() > 0) {
            throw new SystemException("此活动中还有商家未提价活动，待全部商家提交完成后再提交申请！");
        }

        this.activityInvitationService.deleteActivityInvalidInvitation(activityId);//删除活动的无效邀请和商家联盟


        queryParams = new GeneralQueryParams().andEqual("activitId", activityId);
        Map<String, Object> paramAndValue = new HashMap<>();
        paramAndValue.put("currentState", Constant.ACTIVITY_STATE_SBUMITED);
        this.activityDao.executeUpdate(paramAndValue, queryParams);
    }

    /**
     * 将指定用户加入指定的活动
     *
     * @param activityId
     * @param userId
     * @return
     */
    @Override
    public ResponseMessage addSingleUserToActivityChatRoom(String activityId, String userId) {
        ResponseMessage responseMessage = new ResponseMessage(200, true, null);
        Activity activity = this.activityDao.findEntityById(activityId);
        if (activity.getCurrentState() != Constant.ACTIVITY_STATE_STARTED) {
            responseMessage.setCode(ResponseStatusCode.FORBID_ADD);
            responseMessage.setSuccess(true);
            responseMessage.setMsg("sorry!当前活动状态不允许加入");
        }

        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("activity", activity).andEqual("fans.userId", userId);
        List<ActivityFans> activityFanses = this.activityFansDao.getEntities(null, queryParams);
        if (activityFanses.size() != 0) {   //如果此用户已参与了此次活动则直接返回参加成功
            return responseMessage;
        }

        ActivityFans activityFans = new ActivityFans();
        User user = new User();
        user.setUserId(userId);
        activityFans.setFans(user);
        activityFans.setActivity(activity);
        activityFans.setJoinDate(new Date());
        this.activityFansDao.saveEntity(activityFans);
        return responseMessage;
    }

    /**
     * 获取聊天室详情
     *
     * @param activityId
     * @return
     */
    public ResponseMessage getChatRoomInfo(String activityId) {
        Activity activity = this.activityDao.findEntityById(activityId);
        Object chatRoomDetails = getChatRoomDetail(activity.getChatRoomId());
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setCode(ResponseStatusCode.SUCCESS_CODE);
        responseMessage.setMsg("获取成功");
        responseMessage.setSuccess(true);
        responseMessage.setAttribute(chatRoomDetails);
        return responseMessage;
    }

    @Override
    public void executeUpdate(Map<String, Object> paramAndValue, IQueryParams queryParams) {
        this.activityDao.executeUpdate(paramAndValue, queryParams);
    }

    /**
     * 获取已审核通过或已开始的活动
     *
     * @param pager
     * @param queryParams
     * @return
     */
    @Override
    public PageData<Activity> getExaminedAndStartedActivities(Pager pager, IQueryParams queryParams) {
        if (queryParams == null) {
            queryParams = new GeneralQueryParams();
        }
        queryParams.andEqual("currentState", Constant.ACTIVITY_STATE_EXAMINED_OK)
                .orEqual("currentState", Constant.ACTIVITY_STATE_STARTED);

        return this.getPageData(pager, queryParams);
    }

    public ObjectNode getChatRoomDetail(String roomId) {
        ObjectNode objectNode = factory.objectNode();

        if (!JerseyUtils.match("^(?!-)[0-9a-zA-Z\\-]+#[0-9a-zA-Z]+", APPKEY)) {
            return null;
        }

        if (StringUtils.isEmpty(roomId)) {
            return null;
        }

        JerseyWebTarget webTarget = EndPoints.CHATROOMS_TARGET.resolveTemplate("org_name", APPKEY.split("#")[0])
                .resolveTemplate("app_name", APPKEY.split("#")[1]).path(roomId);
        try {
            objectNode = JerseyUtils.sendRequest(webTarget, null, credential, HTTPMethod.METHOD_GET, null);
        } catch (IOException e) {
            return null;
        }

        return objectNode;
    }

    /*=================新增方法=====================*/

    /**
     * 活动开始执行
     *
     * @param activitId
     */
    @Override
    public void executeActivityStarted(String activitId) {
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("activitId", activitId);
        HashMap<String, Object> values = new HashMap<>();
        values.put("currentState", Constant.ACTIVITY_STATE_STARTED);
        this.activityDao.executeUpdate(values, queryParams);
        onActivityStartedInitGoodsSaleVolume(activitId);
    }

    /**
     * 活动开始后将活动商品对应的商品销量加上活动商品的数量，预防商品购买超出库存
     *
     * @param activitId
     */
    protected void onActivityStartedInitGoodsSaleVolume(String activitId) {
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("activity.activitId", activitId);
        List<ActivityGoods> ads = this.activityGoodsDao.getEntities(queryParams);
        if (ads == null || ads.size() <= 0) {
            return;
        }
        for (ActivityGoods ad : ads) {
            if (ad.getAgCount() <= 0) {
                continue;
            }
/*            Goods goods = ad.getGoods();
            long volume = goods.getCurrentStock() > ad.getAgCount() ? ad.getAgCount() : goods.getCurrentStock();
            this.goodsDao.executeAddGoodsVolume(goods.getGoodsId(), volume);
            if (volume != ad.getAgCount()) {
                ad.setAgCount((int) volume);
                this.activityGoodsDao.updateEntity(ad);
            }*/

            Sku sku = ad.getSku();
            long volume = sku.getCurrentStock() > ad.getAgCount() ? ad.getAgCount() : sku.getCurrentStock();
            this.skuDao.addSkuVolumeBySkuAndCount(sku.getSkuId(), (long) ad.getAgCount());
            this.goodsService.executeUpdateGoodsTransientInfo(ad.getGoods().getGoodsId());
            if (volume != ad.getAgCount()) {
                ad.setAgCount((int) volume);
                this.activityGoodsDao.updateEntity(ad);
            }
        }
    }


    /**
     * 活动结束执行
     *
     * @param activitId
     */
    @Override
    public void executeActivityOver(String activitId) throws IOException {
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("activitId", activitId);
        HashMap<String, Object> values = new HashMap<>();
        values.put("currentState", Constant.ACTIVITY_STATE_FNISHED);
        this.activityDao.executeUpdate(values, queryParams);
        onActivityOverResetGoodsSaleVolume(activitId);
        List<Object> objects = this.activityDao.getProperty("chatRoomId", null, queryParams);
        if (objects != null || objects.size() > 0) {
            EMChatUtils.deleteChatRoom(String.valueOf(objects.get(0)));
        }
    }

    @Override
    public void executeActivity(Activity activity, List<String> attributIds) {
        IQueryParams queryParam = new GeneralQueryParams();
        if (attributIds != null && !attributIds.isEmpty()) {
            ArrayList<Object> params = new ArrayList<>();
            for (String tmp : attributIds) {
                params.add(tmp);
            }
            queryParam.andIn("goodsAttSetId", params);
            List<GoodsAttributeSet> attributs = this.goodsAttributeSetDao.getEntities(null, queryParam);
            Set<GoodsAttributeSet> attibuteSets = new HashSet<>(attributs);
            activity.setAttributeSets(attibuteSets);
        }
        this.update(activity);
    }

    @Override
    public PageData<Activity> getActivitiesByGoodsAttSetId(String goodsAttSetId, Pager pager) {
        return this.activityDao.getActivitiesByGoodsAttributeSetId(goodsAttSetId, pager);
    }

    /**
     * 活动结束后重置活动商品对应商品的销量，目前为添加活动商品时对应的商品销量增加为活动商品的数量，此方法为当活动结束后将对应商品
     * 的销量复原
     *
     * @param activityId
     */
    protected void onActivityOverResetGoodsSaleVolume(String activityId) {
        IQueryParams queryParams = new GeneralQueryParams()
                .andEqual("activity.activitId", activityId);
        List<ActivityGoods> ads = this.activityGoodsDao.getEntities(queryParams);
        if (ads == null || ads.size() <= 0) {
            return;
        }
        for (ActivityGoods ad : ads) {
            if (ad.getAgCount() <= 0) {
                continue;
            }
            Sku sku = ad.getSku();
            long volume = sku.getVolume() <= ad.getAgCount() ? 0 : sku.getVolume() - ad.getAgCount();
            sku.setVolume(volume);  //将商品销量减去活动商品剩余数量才为其商品真正销量
            this.skuDao.updateEntity(sku);
            this.goodsService.executeUpdateGoodsTransientInfo(ad.getGoods().getGoodsId());
        }
    }
}
