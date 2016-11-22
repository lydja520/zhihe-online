package cn.com.zhihetech.online.bean;

import cn.com.zhihetech.online.commons.Constant;
import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by YangDaiChun on 2015/11/12.
 */
@Entity
@Table(name = "t_merchant")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Merchant extends SerializableAndCloneable {

    private String merchantId;
    private ImgInfo coverImg;   //商家封面图片
    private ImgInfo headerImg;  //商家主页顶部图片
    private District district;  //商家所在商圈
    private String merchName;
    private String merchTell;   //企业联系电话
    private String address; //详细地址
    private String alipayCode;  //支付宝账号
    private int merchOrder; //商家排列顺序
    private Date createDate;    //入驻时间
    private String orgCode; //组织机构代码
    private String licenseCode; //工商执照注册码
    private String taxRegCode;  //税务登记证号
    private int emplyCount;  //企业规模（员工人数）
    private String merchantDetails;  //商家简介
    private Set<GoodsAttributeSet> categories;  //经营商品类别
    private long updateDate;  //最近上新商品时间

    /*运营者（联系人）相关*/
    private String contactName; //企业联系人姓名
    private String contactPartAndPositon;   //联系人部门与职位
    private String contactMobileNO;   //联系人手机号码(必须为手机号码)
    private String contactEmail;   //联系人电子邮箱
    private String contactID;   //联系人身份证号

    /*提交材料相关*/
    private ImgInfo opraterIDPhoto;   //运营者手持身份证照片
    private ImgInfo orgPhoto;   //组织机构代码证原件照片
    private ImgInfo busLicePhoto;   //工商营业执照原件照片
    private ImgInfo applyLetterPhoto;   //加盖公章的申请认证公函（与商家纠纷事件裁定等）照片

    /*审核情况*/
    private int examinState = Constant.EXAMINE_STATE_NOT_SUBMIT;    //审核状态（默认为未提交审核）
    private String examinMsg;   //审核信息
    private String invitcode;   //受邀请码

    private boolean permit; //启用
    private String permitMsg = "无"; //禁用原因

    /*商品有关*/
    private List<Goods> recommendGoodses;  //推荐商品
    private long goodsNum; //该商家有多少商品
    private boolean isActivating;  //该商家现在是否有活动正在进行

    /*特色街区，购物中心，特色店，商家应该为其中一种，且只能为一种*/
    private StoreType storeType;
    private FeaturedBlock featuredBlock;
    private ShoppingCenter shoppingCenter;

    public Merchant(String merchantId) {
        super();
        this.merchantId = merchantId;
    }

    public Merchant() {
    }

    public enum StoreType {
        shoppingCenter, featuredBlock, featuredStore, vipStore;
    }

    /**
     * 商家评分
     */
    private float score = 5f;

    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid2")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "merch_id", length = 36)
    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    @ManyToOne
    @JoinColumn(name = "cover_img", nullable = false)
    public ImgInfo getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(ImgInfo coverImg) {
        this.coverImg = coverImg;
    }

    @ManyToOne
    @JoinColumn(name = "hearder_img", nullable = false)
    public ImgInfo getHeaderImg() {
        return headerImg;
    }

    public void setHeaderImg(ImgInfo headerImg) {
        this.headerImg = headerImg;
    }

    @ManyToOne
    @JoinColumn(name = "district_id", nullable = true)
    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    @Column(name = "merch_name", length = 100, nullable = false)
    public String getMerchName() {
        return merchName;
    }

    public void setMerchName(String merchName) {
        this.merchName = merchName;
    }

    @Column(name = "merch_tell", length = 20, nullable = false)
    public String getMerchTell() {
        return merchTell;
    }

    public void setMerchTell(String merchTell) {
        this.merchTell = merchTell;
    }

    @Column(name = "merch_address", length = 300, nullable = false)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "alipay_code", length = 100, nullable = true)
    public String getAlipayCode() {
        return alipayCode;
    }

    public void setAlipayCode(String alipayCode) {
        this.alipayCode = alipayCode;
    }

    @Column(name = "merch_order", nullable = true)
    public int getMerchOrder() {
        return merchOrder;
    }

    public void setMerchOrder(int merchOrder) {
        this.merchOrder = merchOrder;
    }

    @Column(name = "permit", nullable = false)
    public boolean isPermit() {
        return permit;
    }

    public void setPermit(boolean permit) {
        this.permit = permit;
    }

    @JSONField(format = Constant.DEFAULT_DATE_TIME_FORMAT)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", updatable = false)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "examin_state")
    public int getExaminState() {
        return examinState;
    }

    public void setExaminState(int examinState) {
        this.examinState = examinState;
    }

    @Column(name = "org_code", length = 50, nullable = true)
    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    @Column(name = "licen_code", length = 100, nullable = true)
    public String getLicenseCode() {
        return licenseCode;
    }

    public void setLicenseCode(String licenseCode) {
        this.licenseCode = licenseCode;
    }

    @Column(name = "tax_reg_code", length = 100, nullable = true)
    public String getTaxRegCode() {
        return taxRegCode;
    }

    public void setTaxRegCode(String taxRegCode) {
        this.taxRegCode = taxRegCode;
    }

    @Column(name = "emp_count", nullable = false)
    public int getEmplyCount() {
        return emplyCount;
    }

    public void setEmplyCount(int emplyCount) {
        this.emplyCount = emplyCount;
    }

    @Column(name = "contact_name", length = 50, nullable = false)
    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    @Column(name = "part_position", length = 100, nullable = false)
    public String getContactPartAndPositon() {
        return contactPartAndPositon;
    }

    public void setContactPartAndPositon(String contactPartAndPositon) {
        this.contactPartAndPositon = contactPartAndPositon;
    }

    @Column(name = "mobile_no", length = 20, nullable = false)
    public String getContactMobileNO() {
        return contactMobileNO;
    }

    public void setContactMobileNO(String contactMobileNO) {
        this.contactMobileNO = contactMobileNO;
    }

    @Column(name = "email", length = 50, nullable = true)
    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    @Column(name = "id_no", length = 20, nullable = true)
    public String getContactID() {
        return contactID;
    }

    public void setContactID(String contactID) {
        this.contactID = contactID;
    }

    @ManyToOne
    @JoinColumn(name = "op_id_photo", nullable = true)
    public ImgInfo getOpraterIDPhoto() {
        return opraterIDPhoto;
    }

    public void setOpraterIDPhoto(ImgInfo opraterIDPhoto) {
        this.opraterIDPhoto = opraterIDPhoto;
    }

    @ManyToOne
    @JoinColumn(name = "org_photo", nullable = true)
    public ImgInfo getOrgPhoto() {
        return orgPhoto;
    }

    public void setOrgPhoto(ImgInfo orgPhoto) {
        this.orgPhoto = orgPhoto;
    }

    @ManyToOne
    @JoinColumn(name = "bus_lice_photo", nullable = true)
    public ImgInfo getBusLicePhoto() {
        return busLicePhoto;
    }

    public void setBusLicePhoto(ImgInfo busLicePhoto) {
        this.busLicePhoto = busLicePhoto;
    }

    @ManyToOne
    @JoinColumn(name = "aplley_letter_photo", nullable = true)
    public ImgInfo getApplyLetterPhoto() {
        return applyLetterPhoto;
    }

    public void setApplyLetterPhoto(ImgInfo applyLetterPhoto) {
        this.applyLetterPhoto = applyLetterPhoto;
    }

    @Column(name = "invit_code", length = 50)
    public String getInvitcode() {
        return invitcode;
    }

    public void setInvitcode(String invitcode) {
        this.invitcode = invitcode;
    }

    @Column(name = "examin_msg", length = 500)
    public String getExaminMsg() {
        return examinMsg;
    }

    public void setExaminMsg(String examinMsg) {
        this.examinMsg = examinMsg;
    }

    @Transient
    public List<Goods> getRecommendGoodses() {
        return recommendGoodses;
    }

    public void setRecommendGoodses(List<Goods> recommendGoodses) {
        this.recommendGoodses = recommendGoodses;
    }

    @Transient
    public long getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(long goodsNum) {
        this.goodsNum = goodsNum;
    }

    @Transient
    public boolean getIsActivating() {
        return isActivating;
    }

    public void setIsActivating(boolean isActivating) {
        this.isActivating = isActivating;
    }


    @JSONField(serialize = false)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "merchant_category", joinColumns = {@JoinColumn(name = "merchant_id", nullable = false)}, inverseJoinColumns = {@JoinColumn(name = "category_id", nullable = false)})
    public Set<GoodsAttributeSet> getCategories() {
        return categories;
    }

    public void setCategories(Set<GoodsAttributeSet> categories) {
        this.categories = categories;
    }

    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name = "details", nullable = true)
    public String getMerchantDetails() {
        return merchantDetails;
    }

    public void setMerchantDetails(String merchantDetails) {
        this.merchantDetails = merchantDetails;
    }

    @ManyToOne
    @JoinColumn(name = "shopping_center_id")
    public ShoppingCenter getShoppingCenter() {
        return shoppingCenter;
    }

    public void setShoppingCenter(ShoppingCenter shoppingCenter) {
        this.shoppingCenter = shoppingCenter;
    }

    @ManyToOne
    @JoinColumn(name = "featured_block_id")
    public FeaturedBlock getFeaturedBlock() {
        return featuredBlock;
    }

    public void setFeaturedBlock(FeaturedBlock featuredBlock) {
        this.featuredBlock = featuredBlock;
    }

    @Transient
    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "store_type", nullable = false)
    public StoreType getStoreType() {
        return storeType;
    }

    public void setStoreType(StoreType storeType) {
        this.storeType = storeType;
    }

    @Column(name = "permit_msg", nullable = false, length = 255)
    public String getPermitMsg() {
        return permitMsg;
    }

    public void setPermitMsg(String permitMsg) {
        this.permitMsg = permitMsg;
    }

    @Column(name = "update_date", nullable = true)
    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }
}
