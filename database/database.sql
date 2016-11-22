/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : zhihe_online

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2016-05-04 17:13:37
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for activity_fans
-- ----------------------------
DROP TABLE IF EXISTS `activity_fans`;
CREATE TABLE `activity_fans` (
  `fans_id` varchar(36) NOT NULL,
  `join_date` datetime NOT NULL,
  `activity` varchar(36) NOT NULL,
  `user` varchar(36) NOT NULL,
  `invitation_merchant` varchar(36) NOT NULL,
  PRIMARY KEY (`fans_id`),
  KEY `FK_emf6uxr6qneqa6a9vapn8ipii` (`activity`),
  KEY `FK_3dgf8o6is014ne8syevu316k5` (`user`),
  KEY `FK_46ihii8okxp2vbrq59ag1m2pp` (`invitation_merchant`),
  CONSTRAINT `FK_3dgf8o6is014ne8syevu316k5` FOREIGN KEY (`user`) REFERENCES `t_user` (`user_id`),
  CONSTRAINT `FK_46ihii8okxp2vbrq59ag1m2pp` FOREIGN KEY (`invitation_merchant`) REFERENCES `t_merchant` (`merch_id`),
  CONSTRAINT `FK_emf6uxr6qneqa6a9vapn8ipii` FOREIGN KEY (`activity`) REFERENCES `t_common_activitie` (`acitivit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of activity_fans
-- ----------------------------

-- ----------------------------
-- Table structure for admin_role
-- ----------------------------
DROP TABLE IF EXISTS `admin_role`;
CREATE TABLE `admin_role` (
  `admin_id` varchar(36) NOT NULL,
  `role_id` varchar(36) NOT NULL,
  PRIMARY KEY (`admin_id`,`role_id`),
  KEY `FK_ea71xkita8ex1nejqqg50shu6` (`role_id`),
  CONSTRAINT `FK_ea71xkita8ex1nejqqg50shu6` FOREIGN KEY (`role_id`) REFERENCES `t_role` (`role_id`),
  CONSTRAINT `FK_k62kncyowxmp31xh5nobs7xcw` FOREIGN KEY (`admin_id`) REFERENCES `t_admin` (`admin_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of admin_role
-- ----------------------------
INSERT INTO `admin_role` VALUES ('28f45deb-7976-4695-b1bb-0c83d7530645', '4028b881515b9dfd01515c1270460000');

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu` (
  `role_id` varchar(36) NOT NULL,
  `menu_id` varchar(36) NOT NULL,
  PRIMARY KEY (`role_id`,`menu_id`),
  CONSTRAINT `FK_r6o1lqlask5jahtkqv3w8sbeh` FOREIGN KEY (`role_id`) REFERENCES `t_role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of role_menu
-- ----------------------------
INSERT INTO `role_menu` VALUES ('2db6dc78-229f-4267-892b-79fd9e8a4d4d', '02565683-c437-4991-b12c-d1c84a27747b');
INSERT INTO `role_menu` VALUES ('2db6dc78-229f-4267-892b-79fd9e8a4d4d', '057ed845-b798-4bc4-bb2c-2f83039ad7e1');
INSERT INTO `role_menu` VALUES ('2db6dc78-229f-4267-892b-79fd9e8a4d4d', '09670f33-98c3-4e4a-b164-51248d98dc39');
INSERT INTO `role_menu` VALUES ('2db6dc78-229f-4267-892b-79fd9e8a4d4d', '0aa6b1dd-9a4a-4509-a9ff-c2492af1ca75');
INSERT INTO `role_menu` VALUES ('2db6dc78-229f-4267-892b-79fd9e8a4d4d', '1f8c114f-62c5-4bfb-8905-412670d927d2');
INSERT INTO `role_menu` VALUES ('2db6dc78-229f-4267-892b-79fd9e8a4d4d', '27cfb5d0-a39d-43db-89d0-9ebc0ce572ca');
INSERT INTO `role_menu` VALUES ('2db6dc78-229f-4267-892b-79fd9e8a4d4d', '390d6f19-1dba-4359-99df-9ece93f1bd7e');
INSERT INTO `role_menu` VALUES ('2db6dc78-229f-4267-892b-79fd9e8a4d4d', '5d6bc89c-bc17-4da0-9e0b-321b1499f983');
INSERT INTO `role_menu` VALUES ('2db6dc78-229f-4267-892b-79fd9e8a4d4d', '705911cb-b20d-4920-a496-fb4a113af677');
INSERT INTO `role_menu` VALUES ('2db6dc78-229f-4267-892b-79fd9e8a4d4d', '7c61d5dc-b115-4215-abab-448ece70f435');
INSERT INTO `role_menu` VALUES ('2db6dc78-229f-4267-892b-79fd9e8a4d4d', '8eb32a0d-63f7-4715-907d-5599ecea0b56');
INSERT INTO `role_menu` VALUES ('2db6dc78-229f-4267-892b-79fd9e8a4d4d', '921da0a8-3464-4432-a77f-a6cd057ac7d8');
INSERT INTO `role_menu` VALUES ('2db6dc78-229f-4267-892b-79fd9e8a4d4d', 'a1db766e-29c0-4bad-9562-337207af3e5f');
INSERT INTO `role_menu` VALUES ('2db6dc78-229f-4267-892b-79fd9e8a4d4d', 'a7f29903-7ade-4f45-8273-648855cad590');
INSERT INTO `role_menu` VALUES ('2db6dc78-229f-4267-892b-79fd9e8a4d4d', 'adf3001e-d09f-48ab-aae0-b3dfe29d642e');
INSERT INTO `role_menu` VALUES ('2db6dc78-229f-4267-892b-79fd9e8a4d4d', 'b70c0ff6-e200-4223-9fcf-092472218fa1');
INSERT INTO `role_menu` VALUES ('2db6dc78-229f-4267-892b-79fd9e8a4d4d', 'd5ed3b7e-6513-45ab-8792-e907205a20ba');
INSERT INTO `role_menu` VALUES ('2db6dc78-229f-4267-892b-79fd9e8a4d4d', 'fb478663-ae86-4e9d-9379-4c3df68875a7');
INSERT INTO `role_menu` VALUES ('4028b881515b9dfd01515c1270460000', '1ccf6578-99b2-4029-bb3b-4cca0ceba2fa');
INSERT INTO `role_menu` VALUES ('4028b881515b9dfd01515c1270460000', '264e4a91-65c4-40f6-a57a-63f5bbfea93b');
INSERT INTO `role_menu` VALUES ('4028b881515b9dfd01515c1270460000', '334920a2-e03c-4f75-bbd1-44339b35cb28');
INSERT INTO `role_menu` VALUES ('4028b881515b9dfd01515c1270460000', '37f84657-e721-4923-b4a0-7cb3daf652cd');
INSERT INTO `role_menu` VALUES ('4028b881515b9dfd01515c1270460000', '3e104661-aef8-42fa-ae33-4928feed4124');
INSERT INTO `role_menu` VALUES ('4028b881515b9dfd01515c1270460000', '474b784a-5bca-4659-89ee-d9cb53792482');
INSERT INTO `role_menu` VALUES ('4028b881515b9dfd01515c1270460000', '50993853-4f3b-4856-a475-5fd9c7233daf');
INSERT INTO `role_menu` VALUES ('4028b881515b9dfd01515c1270460000', '56c6cf3d-11cc-4926-855e-c2a1d35981f6');
INSERT INTO `role_menu` VALUES ('4028b881515b9dfd01515c1270460000', '57edc3e7-426a-4e6e-94d5-4dbd53f205ef');
INSERT INTO `role_menu` VALUES ('4028b881515b9dfd01515c1270460000', '5bddafc9-2171-4f0c-9233-e2fa62b3c8c4');
INSERT INTO `role_menu` VALUES ('4028b881515b9dfd01515c1270460000', '62bc956f-307c-493c-8a39-55ce3f5660bf');
INSERT INTO `role_menu` VALUES ('4028b881515b9dfd01515c1270460000', '7ab241f9-d19b-4bc4-9720-07eee9fa6dc7');
INSERT INTO `role_menu` VALUES ('4028b881515b9dfd01515c1270460000', '7fad92b1-4fe3-454d-a1f3-1c9beae11f03');
INSERT INTO `role_menu` VALUES ('4028b881515b9dfd01515c1270460000', '881ede8f-3fe5-4347-97bf-03f3d84de229');
INSERT INTO `role_menu` VALUES ('4028b881515b9dfd01515c1270460000', '8b7dcd72-f931-4b63-b1ae-95731a5eaaf7');
INSERT INTO `role_menu` VALUES ('4028b881515b9dfd01515c1270460000', '8e153903-9d3b-4762-8344-4e64522fba5f');
INSERT INTO `role_menu` VALUES ('4028b881515b9dfd01515c1270460000', '9458e927-062a-443f-920f-48a5db35a6cc');
INSERT INTO `role_menu` VALUES ('4028b881515b9dfd01515c1270460000', 'a4893705-8e13-4487-ad44-bd88c85f695e');
INSERT INTO `role_menu` VALUES ('4028b881515b9dfd01515c1270460000', 'affb3519-756d-479d-8c86-efe9e5328f68');
INSERT INTO `role_menu` VALUES ('4028b881515b9dfd01515c1270460000', 'd0213554-5330-405c-95cc-5ab89b9e656e');
INSERT INTO `role_menu` VALUES ('4028b881515b9dfd01515c1270460000', 'e465876b-950c-4515-b86e-00f7ff961684');
INSERT INTO `role_menu` VALUES ('4028b881515b9dfd01515c1270460000', 'ec412909-9f6c-4ad3-9cd8-4bef3f66abd4');
INSERT INTO `role_menu` VALUES ('4028b881515b9dfd01515c1270460000', 'fa9d9bb6-c5f6-4b79-9a65-1ac056f5a8c1');

-- ----------------------------
-- Table structure for t_activity_category
-- ----------------------------
DROP TABLE IF EXISTS `t_activity_category`;
CREATE TABLE `t_activity_category` (
  `category_id` varchar(36) NOT NULL,
  `cate_desc` longtext,
  `cate_name` varchar(50) NOT NULL,
  `cate_type` int(11) NOT NULL,
  `create_date` datetime NOT NULL,
  `official` bit(1) NOT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_activity_category
-- ----------------------------

-- ----------------------------
-- Table structure for t_activity_goods
-- ----------------------------
DROP TABLE IF EXISTS `t_activity_goods`;
CREATE TABLE `t_activity_goods` (
  `ag_id` varchar(36) NOT NULL,
  `activityGoods_desc` varchar(255) DEFAULT NULL,
  `activity_price` float DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  `activity_id` varchar(36) NOT NULL,
  `goods_id` varchar(36) NOT NULL,
  `merchant_id` varchar(36) NOT NULL,
  PRIMARY KEY (`ag_id`),
  KEY `FK_frguxaopkh38godyo6sqlm9nd` (`activity_id`),
  KEY `FK_4e5p518rct93ei8pll1ij8kh5` (`goods_id`),
  KEY `FK_d1fo483e7fmbpah2e6ta27lup` (`merchant_id`),
  CONSTRAINT `FK_4e5p518rct93ei8pll1ij8kh5` FOREIGN KEY (`goods_id`) REFERENCES `t_goods` (`goods_id`),
  CONSTRAINT `FK_d1fo483e7fmbpah2e6ta27lup` FOREIGN KEY (`merchant_id`) REFERENCES `t_merchant` (`merch_id`),
  CONSTRAINT `FK_frguxaopkh38godyo6sqlm9nd` FOREIGN KEY (`activity_id`) REFERENCES `t_common_activitie` (`acitivit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_activity_goods
-- ----------------------------

-- ----------------------------
-- Table structure for t_admin
-- ----------------------------
DROP TABLE IF EXISTS `t_admin`;
CREATE TABLE `t_admin` (
  `admin_id` varchar(36) NOT NULL,
  `admin_code` varchar(30) NOT NULL,
  `admin_pwd` varchar(50) NOT NULL,
  `sys_admin` bit(1) DEFAULT NULL,
  `permit` bit(1) DEFAULT NULL,
  `super_admin` bit(1) NOT NULL,
  `merchant_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`admin_id`),
  UNIQUE KEY `UK_ptk0spn4he6m23ew0gyggw5ye` (`admin_code`),
  KEY `FK_fm684crflh1as6n60471q4mpc` (`merchant_id`),
  CONSTRAINT `FK_fm684crflh1as6n60471q4mpc` FOREIGN KEY (`merchant_id`) REFERENCES `t_merchant` (`merch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_admin
-- ----------------------------
INSERT INTO `t_admin` VALUES ('28f45deb-7976-4695-b1bb-0c83d7530645', 'admin', 'e10adc3949ba59abbe56e057f20f883e', '\0', '', '', null);

-- ----------------------------
-- Table structure for t_app_home_img
-- ----------------------------
DROP TABLE IF EXISTS `t_app_home_img`;
CREATE TABLE `t_app_home_img` (
  `home_img_id` varchar(36) NOT NULL,
  `img_name` varchar(100) DEFAULT NULL,
  `img_id` varchar(36) NOT NULL,
  PRIMARY KEY (`home_img_id`),
  KEY `FK_heug6btyobo23g2pxvqyp2vkr` (`img_id`),
  CONSTRAINT `FK_heug6btyobo23g2pxvqyp2vkr` FOREIGN KEY (`img_id`) REFERENCES `t_img_info` (`img_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_app_home_img
-- ----------------------------

-- ----------------------------
-- Table structure for t_app_version
-- ----------------------------
DROP TABLE IF EXISTS `t_app_version`;
CREATE TABLE `t_app_version` (
  `version_id` varchar(36) NOT NULL,
  `version_code` int(11) NOT NULL,
  `version_disc` longtext NOT NULL,
  `version_name` varchar(50) NOT NULL,
  `version_url` varchar(100) NOT NULL,
  PRIMARY KEY (`version_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_app_version
-- ----------------------------

-- ----------------------------
-- Table structure for t_area
-- ----------------------------
DROP TABLE IF EXISTS `t_area`;
CREATE TABLE `t_area` (
  `area_id` varchar(36) NOT NULL,
  `area_name` longtext,
  `is_root` bit(1) DEFAULT NULL,
  `parent_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`area_id`),
  KEY `FK_528sc5w4u8ld4cfqkwt2hqq2c` (`parent_id`),
  CONSTRAINT `FK_528sc5w4u8ld4cfqkwt2hqq2c` FOREIGN KEY (`parent_id`) REFERENCES `t_area` (`area_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_area
-- ----------------------------

-- ----------------------------
-- Table structure for t_banner
-- ----------------------------
DROP TABLE IF EXISTS `t_banner`;
CREATE TABLE `t_banner` (
  `banner_id` varchar(36) NOT NULL,
  `banner_order` int(11) NOT NULL,
  `banner_type` int(11) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `view_target` varchar(100) DEFAULT NULL,
  `target_title` varchar(50) DEFAULT NULL,
  `view_type` int(11) NOT NULL,
  `imginfo_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`banner_id`),
  KEY `FK_do957dupucykeev82oxxv12nq` (`imginfo_id`),
  CONSTRAINT `FK_do957dupucykeev82oxxv12nq` FOREIGN KEY (`imginfo_id`) REFERENCES `t_img_info` (`img_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_banner
-- ----------------------------

-- ----------------------------
-- Table structure for t_big_brand
-- ----------------------------
DROP TABLE IF EXISTS `t_big_brand`;
CREATE TABLE `t_big_brand` (
  `banner_id` varchar(36) NOT NULL,
  `brand_order` int(11) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `merch_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`banner_id`),
  KEY `FK_j29ha4a34286dny7dwdijudtw` (`merch_id`),
  CONSTRAINT `FK_j29ha4a34286dny7dwdijudtw` FOREIGN KEY (`merch_id`) REFERENCES `t_merchant` (`merch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_big_brand
-- ----------------------------

-- ----------------------------
-- Table structure for t_chat_message
-- ----------------------------
DROP TABLE IF EXISTS `t_chat_message`;
CREATE TABLE `t_chat_message` (
  `chat_message_id` varchar(36) NOT NULL,
  `chat_type` varchar(15) NOT NULL,
  `create_date` datetime NOT NULL,
  `em_msg_id` varchar(50) NOT NULL,
  `message_from` varchar(50) NOT NULL,
  `time_stamp` bigint(20) NOT NULL,
  `em_msg_to` varchar(50) NOT NULL,
  `type` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`chat_message_id`),
  UNIQUE KEY `UK_anv9y98m5nbc8xhq3ng8jdui` (`em_msg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_chat_message
-- ----------------------------

-- ----------------------------
-- Table structure for t_common_activitie
-- ----------------------------
DROP TABLE IF EXISTS `t_common_activitie`;
CREATE TABLE `t_common_activitie` (
  `activity_flag` varchar(31) NOT NULL,
  `acitivit_id` varchar(36) NOT NULL,
  `activit_desc` longtext,
  `detail` longtext NOT NULL,
  `activit_name` varchar(200) NOT NULL,
  `begin_date` datetime NOT NULL,
  `chat_room_id` varchar(255) DEFAULT NULL,
  `contact_tell` varchar(20) NOT NULL,
  `contacter` varchar(50) NOT NULL,
  `create_date` datetime NOT NULL,
  `state` int(11) DEFAULT NULL,
  `end_date` datetime NOT NULL,
  `examin_msg` varchar(255) DEFAULT NULL,
  `promoter` varchar(36) NOT NULL,
  `attrute_set` varchar(255) DEFAULT NULL,
  `category` varchar(36) NOT NULL,
  `cover_img` varchar(36) DEFAULT NULL,
  `reception_room` varchar(36) NOT NULL,
  PRIMARY KEY (`acitivit_id`),
  KEY `FK_7qp4mawt16xrlpebxnp8e2hy5` (`promoter`),
  KEY `FK_h7agl0k662cjtu2tck57t23mq` (`category`),
  KEY `FK_attqen1ewdv17o23qq0kckt7t` (`cover_img`),
  KEY `FK_ekk7bm3g368uixa4o2yf30fqk` (`reception_room`),
  CONSTRAINT `FK_7qp4mawt16xrlpebxnp8e2hy5` FOREIGN KEY (`promoter`) REFERENCES `t_merchant` (`merch_id`),
  CONSTRAINT `FK_attqen1ewdv17o23qq0kckt7t` FOREIGN KEY (`cover_img`) REFERENCES `t_img_info` (`img_id`),
  CONSTRAINT `FK_ekk7bm3g368uixa4o2yf30fqk` FOREIGN KEY (`reception_room`) REFERENCES `t_reception_room` (`room_id`),
  CONSTRAINT `FK_h7agl0k662cjtu2tck57t23mq` FOREIGN KEY (`category`) REFERENCES `t_activity_category` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_common_activitie
-- ----------------------------

-- ----------------------------
-- Table structure for t_complex
-- ----------------------------
DROP TABLE IF EXISTS `t_complex`;
CREATE TABLE `t_complex` (
  `id` varchar(36) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `district_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_o5nvwuehw24kem6vbdlyxmc2j` (`district_id`),
  CONSTRAINT `FK_o5nvwuehw24kem6vbdlyxmc2j` FOREIGN KEY (`district_id`) REFERENCES `t_district` (`district_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_complex
-- ----------------------------

-- ----------------------------
-- Table structure for t_coupon
-- ----------------------------
DROP TABLE IF EXISTS `t_coupon`;
CREATE TABLE `t_coupon` (
  `coupon_id` varchar(36) NOT NULL,
  `coupon_msg` longtext NOT NULL,
  `coupon_name` varchar(50) NOT NULL,
  `coupon_type` int(11) NOT NULL,
  `create_date` datetime NOT NULL,
  `deleted` bit(1) NOT NULL,
  `end_validity` datetime DEFAULT NULL,
  `face_value` float NOT NULL,
  `start_validity` datetime DEFAULT NULL,
  `total` int(11) NOT NULL,
  `total_received` int(11) NOT NULL,
  `activity` varchar(36) DEFAULT NULL,
  `merchant` varchar(36) NOT NULL,
  PRIMARY KEY (`coupon_id`),
  KEY `FK_i1s0xbfwo0p67593exnehg5re` (`activity`),
  KEY `FK_qh8pmklbucqx45xbp7towi88a` (`merchant`),
  CONSTRAINT `FK_i1s0xbfwo0p67593exnehg5re` FOREIGN KEY (`activity`) REFERENCES `t_common_activitie` (`acitivit_id`),
  CONSTRAINT `FK_qh8pmklbucqx45xbp7towi88a` FOREIGN KEY (`merchant`) REFERENCES `t_merchant` (`merch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_coupon
-- ----------------------------

-- ----------------------------
-- Table structure for t_coupon_item
-- ----------------------------
DROP TABLE IF EXISTS `t_coupon_item`;
CREATE TABLE `t_coupon_item` (
  `coupon_item_id` varchar(36) NOT NULL,
  `begin_validity` datetime DEFAULT NULL,
  `coupon_code` varchar(255) DEFAULT NULL,
  `coupon_name` varchar(36) DEFAULT NULL,
  `coupon_type` int(11) NOT NULL,
  `face_value` float DEFAULT NULL,
  `received_date` datetime DEFAULT NULL,
  `use_date` datetime DEFAULT NULL,
  `is_use` bit(1) NOT NULL,
  `validity` datetime NOT NULL,
  `coupon_id` varchar(36) NOT NULL,
  `user_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`coupon_item_id`),
  KEY `FK_jvb0dssyeqw5vxgoylwl1kugu` (`coupon_id`),
  KEY `FK_3tvyby5yb7gg5xihk6f12wmxm` (`user_id`),
  CONSTRAINT `FK_3tvyby5yb7gg5xihk6f12wmxm` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`user_id`),
  CONSTRAINT `FK_jvb0dssyeqw5vxgoylwl1kugu` FOREIGN KEY (`coupon_id`) REFERENCES `t_coupon` (`coupon_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_coupon_item
-- ----------------------------

-- ----------------------------
-- Table structure for t_district
-- ----------------------------
DROP TABLE IF EXISTS `t_district`;
CREATE TABLE `t_district` (
  `district_id` varchar(36) NOT NULL,
  `district_desc` longtext,
  `distract_name` varchar(100) NOT NULL,
  `area_id` varchar(36) NOT NULL,
  PRIMARY KEY (`district_id`),
  KEY `FK_1606pm6at2ts3lvq1g3ijpjp1` (`area_id`),
  CONSTRAINT `FK_1606pm6at2ts3lvq1g3ijpjp1` FOREIGN KEY (`area_id`) REFERENCES `t_area` (`area_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_district
-- ----------------------------

-- ----------------------------
-- Table structure for t_featured_block
-- ----------------------------
DROP TABLE IF EXISTS `t_featured_block`;
CREATE TABLE `t_featured_block` (
  `fb_id` varchar(36) NOT NULL,
  `fb_name` varchar(100) NOT NULL,
  `fb_order` int(11) DEFAULT NULL,
  `permit` bit(1) DEFAULT NULL,
  `area_id` varchar(36) DEFAULT NULL,
  `imgInfo_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`fb_id`),
  KEY `FK_em5w8nex2y8q1b97tcnodfb4w` (`area_id`),
  KEY `FK_oe1k16a7g06uonv1d4asci23w` (`imgInfo_id`),
  CONSTRAINT `FK_em5w8nex2y8q1b97tcnodfb4w` FOREIGN KEY (`area_id`) REFERENCES `t_area` (`area_id`),
  CONSTRAINT `FK_oe1k16a7g06uonv1d4asci23w` FOREIGN KEY (`imgInfo_id`) REFERENCES `t_img_info` (`img_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_featured_block
-- ----------------------------

-- ----------------------------
-- Table structure for t_focus_goods
-- ----------------------------
DROP TABLE IF EXISTS `t_focus_goods`;
CREATE TABLE `t_focus_goods` (
  `focus_id` varchar(36) NOT NULL,
  `focus_date` datetime DEFAULT NULL,
  `goods_id` varchar(36) NOT NULL,
  `user_id` varchar(36) NOT NULL,
  PRIMARY KEY (`focus_id`),
  KEY `FK_97ldk1wuc142foavc5jfiffju` (`goods_id`),
  KEY `FK_ocgt355so49o57jlk7r87qoxg` (`user_id`),
  CONSTRAINT `FK_97ldk1wuc142foavc5jfiffju` FOREIGN KEY (`goods_id`) REFERENCES `t_goods` (`goods_id`),
  CONSTRAINT `FK_ocgt355so49o57jlk7r87qoxg` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_focus_goods
-- ----------------------------

-- ----------------------------
-- Table structure for t_focus_merchant
-- ----------------------------
DROP TABLE IF EXISTS `t_focus_merchant`;
CREATE TABLE `t_focus_merchant` (
  `focus_id` varchar(36) NOT NULL,
  `focus_date` datetime DEFAULT NULL,
  `merchant_id` varchar(36) NOT NULL,
  `user_id` varchar(36) NOT NULL,
  PRIMARY KEY (`focus_id`),
  KEY `FK_sg72yi9wdmlwslevaar5vf2wk` (`merchant_id`),
  KEY `FK_ss13cyn0345e48n83tr1nwylf` (`user_id`),
  CONSTRAINT `FK_sg72yi9wdmlwslevaar5vf2wk` FOREIGN KEY (`merchant_id`) REFERENCES `t_merchant` (`merch_id`),
  CONSTRAINT `FK_ss13cyn0345e48n83tr1nwylf` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_focus_merchant
-- ----------------------------

-- ----------------------------
-- Table structure for t_goods
-- ----------------------------
DROP TABLE IF EXISTS `t_goods`;
CREATE TABLE `t_goods` (
  `goods_id` varchar(36) NOT NULL,
  `carriage` float DEFAULT NULL,
  `carriage_method` varchar(255) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `deleteState` bit(1) DEFAULT NULL,
  `examin_msg` longtext,
  `examin_state` int(11) DEFAULT NULL,
  `goods_desc` longtext NOT NULL,
  `goods_name` longtext NOT NULL,
  `is_activityGoods` bit(1) DEFAULT NULL,
  `is_pick` bit(1) DEFAULT NULL,
  `onsale` bit(1) DEFAULT NULL,
  `goods_price` double DEFAULT NULL,
  `goods_stock` bigint(20) DEFAULT NULL,
  `goods_volume` bigint(20) DEFAULT NULL,
  `cover_img` varchar(36) DEFAULT NULL,
  `goods_att_set` varchar(255) DEFAULT NULL,
  `merch_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`goods_id`),
  KEY `FK_qtmyq4p2effj4q7kdmqahvp9r` (`cover_img`),
  KEY `FK_o0yysjjycehjguk8eraf8omnp` (`merch_id`),
  CONSTRAINT `FK_o0yysjjycehjguk8eraf8omnp` FOREIGN KEY (`merch_id`) REFERENCES `t_merchant` (`merch_id`),
  CONSTRAINT `FK_qtmyq4p2effj4q7kdmqahvp9r` FOREIGN KEY (`cover_img`) REFERENCES `t_img_info` (`img_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_goods
-- ----------------------------

-- ----------------------------
-- Table structure for t_goods_banner
-- ----------------------------
DROP TABLE IF EXISTS `t_goods_banner`;
CREATE TABLE `t_goods_banner` (
  `banner_id` varchar(36) NOT NULL,
  `banner_order` int(11) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `goods_id` varchar(36) DEFAULT NULL,
  `img_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`banner_id`),
  KEY `FK_7hhs8f07foxwlvejmy5av8etn` (`goods_id`),
  KEY `FK_dh2byygecoxmet32siw5bfuj4` (`img_id`),
  CONSTRAINT `FK_7hhs8f07foxwlvejmy5av8etn` FOREIGN KEY (`goods_id`) REFERENCES `t_goods` (`goods_id`),
  CONSTRAINT `FK_dh2byygecoxmet32siw5bfuj4` FOREIGN KEY (`img_id`) REFERENCES `t_img_info` (`img_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_goods_banner
-- ----------------------------

-- ----------------------------
-- Table structure for t_goods_browse
-- ----------------------------
DROP TABLE IF EXISTS `t_goods_browse`;
CREATE TABLE `t_goods_browse` (
  `browse_id` varchar(36) NOT NULL,
  `browse_date` datetime DEFAULT NULL,
  `goods_id` varchar(36) NOT NULL,
  `user_id` varchar(36) NOT NULL,
  PRIMARY KEY (`browse_id`),
  KEY `FK_3r67h2hg1u5bxqaiwcx6bifdc` (`goods_id`),
  KEY `FK_m54drgyvsqcnn64kqaoj4bsd9` (`user_id`),
  CONSTRAINT `FK_3r67h2hg1u5bxqaiwcx6bifdc` FOREIGN KEY (`goods_id`) REFERENCES `t_goods` (`goods_id`),
  CONSTRAINT `FK_m54drgyvsqcnn64kqaoj4bsd9` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_goods_browse
-- ----------------------------

-- ----------------------------
-- Table structure for t_goods_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_goods_detail`;
CREATE TABLE `t_goods_detail` (
  `id` varchar(36) NOT NULL,
  `detail_order` int(11) DEFAULT NULL,
  `view_target` varchar(100) DEFAULT NULL,
  `view_type` int(11) DEFAULT NULL,
  `goods_id` varchar(36) DEFAULT NULL,
  `imginfo_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_mo5xbnrexhdtoyfdyy8vhpu3` (`goods_id`),
  KEY `FK_nkusx95ntoxclfbjvv5t69ep8` (`imginfo_id`),
  CONSTRAINT `FK_mo5xbnrexhdtoyfdyy8vhpu3` FOREIGN KEY (`goods_id`) REFERENCES `t_goods` (`goods_id`),
  CONSTRAINT `FK_nkusx95ntoxclfbjvv5t69ep8` FOREIGN KEY (`imginfo_id`) REFERENCES `t_img_info` (`img_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_goods_detail
-- ----------------------------

-- ----------------------------
-- Table structure for t_goods_score
-- ----------------------------
DROP TABLE IF EXISTS `t_goods_score`;
CREATE TABLE `t_goods_score` (
  `id` varchar(36) NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `evaluate` varchar(255) DEFAULT NULL,
  `score` float DEFAULT NULL,
  `goods_id` varchar(36) DEFAULT NULL,
  `order_id` varchar(36) DEFAULT NULL,
  `user_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_hv5gk3vy3tkd0d63tpoo6g9aa` (`goods_id`),
  KEY `FK_tgre4k2v1f797flf0at66ocv1` (`order_id`),
  KEY `FK_966hdyx4h6y66q6au07rfecqo` (`user_id`),
  CONSTRAINT `FK_966hdyx4h6y66q6au07rfecqo` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`user_id`),
  CONSTRAINT `FK_hv5gk3vy3tkd0d63tpoo6g9aa` FOREIGN KEY (`goods_id`) REFERENCES `t_goods` (`goods_id`),
  CONSTRAINT `FK_tgre4k2v1f797flf0at66ocv1` FOREIGN KEY (`order_id`) REFERENCES `t_order` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_goods_score
-- ----------------------------

-- ----------------------------
-- Table structure for t_hobby_tag
-- ----------------------------
DROP TABLE IF EXISTS `t_hobby_tag`;
CREATE TABLE `t_hobby_tag` (
  `tag_id` varchar(36) NOT NULL,
  `tag_desc` longtext,
  `tag_name` varchar(200) NOT NULL,
  `parent_tag` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`tag_id`),
  KEY `FK_gvk2tulh60rrce0ij0if1ovqf` (`parent_tag`),
  CONSTRAINT `FK_gvk2tulh60rrce0ij0if1ovqf` FOREIGN KEY (`parent_tag`) REFERENCES `t_hobby_tag` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_hobby_tag
-- ----------------------------

-- ----------------------------
-- Table structure for t_img_info
-- ----------------------------
DROP TABLE IF EXISTS `t_img_info`;
CREATE TABLE `t_img_info` (
  `img_id` varchar(36) NOT NULL,
  `img_bucket` varchar(50) NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `img_domain` varchar(50) NOT NULL,
  `img_height` int(11) DEFAULT NULL,
  `img_key` varchar(50) NOT NULL,
  `img_width` int(11) DEFAULT NULL,
  PRIMARY KEY (`img_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_img_info
-- ----------------------------

-- ----------------------------
-- Table structure for t_integral
-- ----------------------------
DROP TABLE IF EXISTS `t_integral`;
CREATE TABLE `t_integral` (
  `integral_id` varchar(36) NOT NULL,
  `aecc_way` longtext,
  `create_date` datetime DEFAULT NULL,
  `integral_type` int(11) DEFAULT NULL,
  `integral_value` int(11) DEFAULT NULL,
  `user_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`integral_id`),
  KEY `FK_t0l03sje10efwv0bu51l74s5x` (`user_id`),
  CONSTRAINT `FK_t0l03sje10efwv0bu51l74s5x` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_integral
-- ----------------------------

-- ----------------------------
-- Table structure for t_login_journal
-- ----------------------------
DROP TABLE IF EXISTS `t_login_journal`;
CREATE TABLE `t_login_journal` (
  `journal_id` varchar(36) NOT NULL,
  `app_version_code` float DEFAULT NULL,
  `app_version_name` varchar(255) DEFAULT NULL,
  `fail_reason` longtext,
  `latitude` float DEFAULT NULL,
  `login_date` datetime DEFAULT NULL,
  `login_ip` varchar(20) DEFAULT NULL,
  `login_type` varchar(255) NOT NULL,
  `longitude` float DEFAULT NULL,
  `mobile_name` varchar(50) DEFAULT NULL,
  `os_name` varchar(50) DEFAULT NULL,
  `os_version` varchar(50) DEFAULT NULL,
  `login_success` bit(1) NOT NULL,
  `user_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`journal_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_login_journal
-- ----------------------------

-- ----------------------------
-- Table structure for t_lucky_draw
-- ----------------------------
DROP TABLE IF EXISTS `t_lucky_draw`;
CREATE TABLE `t_lucky_draw` (
  `ld_id` varchar(36) NOT NULL,
  `ld_amount` int(11) DEFAULT NULL,
  `del_state` bit(1) DEFAULT NULL,
  `ld_desc` longtext NOT NULL,
  `ld_order` int(11) NOT NULL,
  `ld_luck_state` bit(1) DEFAULT NULL,
  `ld_percentage` float DEFAULT NULL,
  `ld_title` varchar(255) NOT NULL,
  `lda_id` varchar(36) DEFAULT NULL,
  `merchant_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`ld_id`),
  KEY `FK_55hygpu5pxx7lpfyy5ryttbkk` (`lda_id`),
  KEY `FK_3qjwum0pnnyvpwkan54sq68xx` (`merchant_id`),
  CONSTRAINT `FK_3qjwum0pnnyvpwkan54sq68xx` FOREIGN KEY (`merchant_id`) REFERENCES `t_merchant` (`merch_id`),
  CONSTRAINT `FK_55hygpu5pxx7lpfyy5ryttbkk` FOREIGN KEY (`lda_id`) REFERENCES `t_lucky_draw_activity` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_lucky_draw
-- ----------------------------

-- ----------------------------
-- Table structure for t_lucky_draw_activity
-- ----------------------------
DROP TABLE IF EXISTS `t_lucky_draw_activity`;
CREATE TABLE `t_lucky_draw_activity` (
  `activity_id` varchar(36) NOT NULL,
  `activity_name` varchar(100) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `content_desc` varchar(255) DEFAULT NULL,
  `permit` bit(1) DEFAULT NULL,
  `submit_state` bit(1) DEFAULT NULL,
  PRIMARY KEY (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_lucky_draw_activity
-- ----------------------------

-- ----------------------------
-- Table structure for t_lucky_draw_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_lucky_draw_detail`;
CREATE TABLE `t_lucky_draw_detail` (
  `ldd_id` varchar(36) NOT NULL,
  `draw_date` datetime DEFAULT NULL,
  `lucky_draw_code` varchar(36) DEFAULT NULL,
  `use_state` bit(1) DEFAULT NULL,
  `who_number` bigint(20) DEFAULT NULL,
  `lucky_draw_id` varchar(36) NOT NULL,
  `user_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`ldd_id`),
  KEY `FK_f7w9jbniepxfsc93fjh44dhf` (`lucky_draw_id`),
  KEY `FK_h39r90yuvmcx960fjgniooqjc` (`user_id`),
  CONSTRAINT `FK_f7w9jbniepxfsc93fjh44dhf` FOREIGN KEY (`lucky_draw_id`) REFERENCES `t_lucky_draw` (`ld_id`),
  CONSTRAINT `FK_h39r90yuvmcx960fjgniooqjc` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_lucky_draw_detail
-- ----------------------------

-- ----------------------------
-- Table structure for t_lucky_draw_record
-- ----------------------------
DROP TABLE IF EXISTS `t_lucky_draw_record`;
CREATE TABLE `t_lucky_draw_record` (
  `lucky_draw_record_id` varchar(36) NOT NULL,
  `draw_date` datetime DEFAULT NULL,
  `personNumber` bigint(20) DEFAULT NULL,
  `lda_id` varchar(36) DEFAULT NULL,
  `userId` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`lucky_draw_record_id`),
  KEY `FK_hvqo46wo02tilokamirkw3mje` (`lda_id`),
  KEY `FK_r6jlhk3hssemcr42un4sokd9h` (`userId`),
  CONSTRAINT `FK_hvqo46wo02tilokamirkw3mje` FOREIGN KEY (`lda_id`) REFERENCES `t_lucky_draw_activity` (`activity_id`),
  CONSTRAINT `FK_r6jlhk3hssemcr42un4sokd9h` FOREIGN KEY (`userId`) REFERENCES `t_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_lucky_draw_record
-- ----------------------------

-- ----------------------------
-- Table structure for t_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu` (
  `menu_id` varchar(36) NOT NULL,
  `is_root` bit(1) DEFAULT NULL,
  `menu_desc` longtext,
  `menu_name` varchar(50) DEFAULT NULL,
  `menu_order` int(11) DEFAULT NULL,
  `menu_url` varchar(200) DEFAULT NULL,
  `permit` bit(1) DEFAULT NULL,
  `parent_id` varchar(36) DEFAULT NULL,
  `only_admin` bit(1) DEFAULT NULL,
  `menu_img` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`menu_id`),
  KEY `FK_q3vj2cm34vra053rte5tr0c0h` (`parent_id`) USING BTREE,
  CONSTRAINT `t_menu_ibfk_1` FOREIGN KEY (`parent_id`) REFERENCES `t_menu` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_menu
-- ----------------------------
INSERT INTO `t_menu` VALUES ('00e0c9af-ba8c-4996-ac24-9930edba7ba2', '\0', '', '综合体管理', '8', '/admin/api/complex', '\0', '436a22cf-c0ed-4379-90b2-0da12f35c7ac', null, 'icon-creditcards');
INSERT INTO `t_menu` VALUES ('02565683-c437-4991-b12c-d1c84a27747b', '\0', '待退款', '待退款', '96', '/admin/order/refund', '', 'f3a41912-ac2c-441d-b156-4c03c884d359', null, 'icon-plugin');
INSERT INTO `t_menu` VALUES ('057ed845-b798-4bc4-bb2c-2f83039ad7e1', '\0', '', '退款成功', '97', '/admin/order/refundOk', '', 'f3a41912-ac2c-441d-b156-4c03c884d359', null, 'icon-creditcards');
INSERT INTO `t_menu` VALUES ('09670f33-98c3-4e4a-b164-51248d98dc39', '\0', '', '所有商品', '83', '/admin/goods', '', 'ee0be142-fb2d-457b-89ae-3d79f812cfcd', null, 'icon-application-form-edit');
INSERT INTO `t_menu` VALUES ('0aa6b1dd-9a4a-4509-a9ff-c2492af1ca75', '\0', '用户关注或收藏了该商家，都会在这里显示', '关注我的会员', '111', '/admin/focusMerchant', '', '1fc5cee9-cc95-4374-a483-5aa7a0015c82', null, 'icon-users-group');
INSERT INTO `t_menu` VALUES ('19a80faf-6b86-4e96-8851-5328c423c6c1', '\0', '挚合会客厅，商家活动对应一个会客厅', '会客厅管理', '36', '/admin/receptionRoom/index', '', '421f2aca-8878-48be-9d0b-1ecd533c9054', null, 'icon-user');
INSERT INTO `t_menu` VALUES ('1ccf6578-99b2-4029-bb3b-4cca0ceba2fa', '\0', '', '待审核商家', '51', '/admin/examinMerchant', '', '4182c872-0539-4075-bd0e-51962eeb5c07', null, 'icon-bricks');
INSERT INTO `t_menu` VALUES ('1f8c114f-62c5-4bfb-8905-412670d927d2', '\0', '', '已签收和已评价', '95', '/admin/order/alreadyFinish', '', 'f3a41912-ac2c-441d-b156-4c03c884d359', null, 'icon-edit2');
INSERT INTO `t_menu` VALUES ('1fc5cee9-cc95-4374-a483-5aa7a0015c82', '', '管理关注了该商户的用户', '会员管理', '110', '/focusMerchant', '', null, null, 'icon-users-group');
INSERT INTO `t_menu` VALUES ('264e4a91-65c4-40f6-a57a-63f5bbfea93b', '\0', '设定购物中心', '购物中心管理', '24', '/admin/shoppingCenter', '', '421f2aca-8878-48be-9d0b-1ecd533c9054', null, 'icon-creditcards');
INSERT INTO `t_menu` VALUES ('27cfb5d0-a39d-43db-89d0-9ebc0ce572ca', '\0', '', '所有商品评价', '122', '/admin/allGoodsEvaluate', '', 'a4f25c04-1109-402f-bbc6-290f5e0a0e8b', null, 'icon-edit2');
INSERT INTO `t_menu` VALUES ('2a3d1ff6-1627-4686-bb1e-72af92cf92c7', '', '', '活动专区', '100', '/activity', '', null, null, 'redpack');
INSERT INTO `t_menu` VALUES ('334920a2-e03c-4f75-bbd1-44339b35cb28', '\0', '', 'APP轮播图管理', '23', '/admin/banner', '', '421f2aca-8878-48be-9d0b-1ecd533c9054', null, 'icon-photo');
INSERT INTO `t_menu` VALUES ('37f84657-e721-4923-b4a0-7cb3daf652cd', '\0', '', '短信验证码管理', '9', '/admin/messager', '', '436a22cf-c0ed-4379-90b2-0da12f35c7ac', null, 'icon-messager');
INSERT INTO `t_menu` VALUES ('390d6f19-1dba-4359-99df-9ece93f1bd7e', '\0', '', '我参与的活动', '102', '/admin/merchActivity', '', '2a3d1ff6-1627-4686-bb1e-72af92cf92c7', null, 'icon-alliance');
INSERT INTO `t_menu` VALUES ('3e104661-aef8-42fa-ae33-4928feed4124', '\0', '', '商品类别管理', '5', '/admin/goodsAttSet', '', '436a22cf-c0ed-4379-90b2-0da12f35c7ac', null, 'icon-plugin');
INSERT INTO `t_menu` VALUES ('4182c872-0539-4075-bd0e-51962eeb5c07', '', '', '商家管理', '50', '/merchant', '', null, null, 'icon-bricks');
INSERT INTO `t_menu` VALUES ('421f2aca-8878-48be-9d0b-1ecd533c9054', '', '', 'APP管理', '20', '/app', '', null, null, 'icon-application-cascade');
INSERT INTO `t_menu` VALUES ('436a22cf-c0ed-4379-90b2-0da12f35c7ac', '', '', '系统设置', '0', '/admin', '', null, null, 'icon-creditcards');
INSERT INTO `t_menu` VALUES ('474b784a-5bca-4659-89ee-d9cb53792482', '\0', '', 'APP导航管理', '22', '/admin/navigation', '', '421f2aca-8878-48be-9d0b-1ecd533c9054', null, 'icon-application-form-edit');
INSERT INTO `t_menu` VALUES ('4d23a325-1f92-43d5-8151-750ba3582625', '', '', '财务管理', '130', '/financialManager', '', null, null, 'icon-chart-bar');
INSERT INTO `t_menu` VALUES ('50993853-4f3b-4856-a475-5fd9c7233daf', '\0', '', '云图片管理', '10', '/admin/imgInfo', '', '436a22cf-c0ed-4379-90b2-0da12f35c7ac', null, 'icon-photo');
INSERT INTO `t_menu` VALUES ('56c6cf3d-11cc-4926-855e-c2a1d35981f6', '\0', '用于新闻发布会维护', '新闻发布会', '7', '/admin/realsemeet/index', '', 'ab5d8c6f-7b62-4161-b3c0-25042a8be887', null, 'icon-report');
INSERT INTO `t_menu` VALUES ('57edc3e7-426a-4e6e-94d5-4dbd53f205ef', '\0', '商家提交的，等待审核的活动', '待审核的活动', '72', '/admin/waitExaminActivity', '', 'f16a1e54-a124-4287-9a1c-6ee33958201f', null, 'icon-categ');
INSERT INTO `t_menu` VALUES ('5bddafc9-2171-4f0c-9233-e2fa62b3c8c4', '\0', '', '抽奖活动', '31', '/admim/luckyDrawActivity', '', 'ab5d8c6f-7b62-4161-b3c0-25042a8be887', null, 'icon-bricks');
INSERT INTO `t_menu` VALUES ('5d6bc89c-bc17-4da0-9e0b-321b1499f983', '\0', '', '我发起的活动', '101', '/admin/applyActivity', '', '2a3d1ff6-1627-4686-bb1e-72af92cf92c7', null, 'redpack');
INSERT INTO `t_menu` VALUES ('62bc956f-307c-493c-8a39-55ce3f5660bf', '\0', '', '用户登录日志', '62', '/admin/journal', '', 'acca1a8e-de12-4c28-a67a-72f0f8daee59', null, 'icon-discount');
INSERT INTO `t_menu` VALUES ('705911cb-b20d-4920-a496-fb4a113af677', '\0', '', '添加商品', '81', '/admin/goods/addView', '', 'ee0be142-fb2d-457b-89ae-3d79f812cfcd', null, 'icon-application-form-edit');
INSERT INTO `t_menu` VALUES ('7ab241f9-d19b-4bc4-9720-07eee9fa6dc7', '\0', '', '角色管理', '2', '/admin/role', '', '436a22cf-c0ed-4379-90b2-0da12f35c7ac', null, 'icon-users-group');
INSERT INTO `t_menu` VALUES ('7c61d5dc-b115-4215-abab-448ece70f435', '\0', '', '订单评价', '121', '/admin/orderEvaluate', '', 'a4f25c04-1109-402f-bbc6-290f5e0a0e8b', null, 'icon-edit2');
INSERT INTO `t_menu` VALUES ('7fad92b1-4fe3-454d-a1f3-1c9beae11f03', '\0', '', '管理员维护', '3', '/admin/admin', '', '436a22cf-c0ed-4379-90b2-0da12f35c7ac', null, 'icon-user');
INSERT INTO `t_menu` VALUES ('881ede8f-3fe5-4347-97bf-03f3d84de229', '\0', '', '活动类别管理', '71', '/admin/activityCategory', '', 'f16a1e54-a124-4287-9a1c-6ee33958201f', null, 'icon-categ');
INSERT INTO `t_menu` VALUES ('8b7dcd72-f931-4b63-b1ae-95731a5eaaf7', '\0', '', '用户钱包提现', '42', '/admin/userBilling', '', 'cce46f8b-d732-4d4b-a161-d04ae64e8123', null, 'icon-discount');
INSERT INTO `t_menu` VALUES ('8e153903-9d3b-4762-8344-4e64522fba5f', '\0', '', '用户信息', '61', '/admin/user', '', 'acca1a8e-de12-4c28-a67a-72f0f8daee59', null, 'icon-users');
INSERT INTO `t_menu` VALUES ('8eb32a0d-63f7-4715-907d-5599ecea0b56', '\0', '', '所有订单', '91', '/admin/order', '', 'f3a41912-ac2c-441d-b156-4c03c884d359', null, 'icon-calender');
INSERT INTO `t_menu` VALUES ('921da0a8-3464-4432-a77f-a6cd057ac7d8', '\0', '', '每日收益', '131', '/admin/financialManager', '', '4d23a325-1f92-43d5-8151-750ba3582625', null, 'icon-categ');
INSERT INTO `t_menu` VALUES ('9458e927-062a-443f-920f-48a5db35a6cc', '\0', '设定特色街区', '特色街区管理', '25', '/admin/featuredBlock', '', '421f2aca-8878-48be-9d0b-1ecd533c9054', null, 'icon-categ');
INSERT INTO `t_menu` VALUES ('a1db766e-29c0-4bad-9562-337207af3e5f', '\0', '商家推荐商品', '商品推荐', '84', '/admin/merchant/recommend', '', 'ee0be142-fb2d-457b-89ae-3d79f812cfcd', null, 'icon-application-form-edit');
INSERT INTO `t_menu` VALUES ('a4893705-8e13-4487-ad44-bd88c85f695e', '\0', '', '所有活动', '73', '/admin/activity', '', 'f16a1e54-a124-4287-9a1c-6ee33958201f', null, 'icon-categ');
INSERT INTO `t_menu` VALUES ('a4f25c04-1109-402f-bbc6-290f5e0a0e8b', '', '', '评价管理', '120', '/evaluateManager', '', null, null, 'icon-calender-edit');
INSERT INTO `t_menu` VALUES ('a7f29903-7ade-4f45-8273-648855cad590', '\0', '', '待支付', '92', '/admin/order/waitPay', '', 'f3a41912-ac2c-441d-b156-4c03c884d359', null, 'icon-chart-bar');
INSERT INTO `t_menu` VALUES ('ab5d8c6f-7b62-4161-b3c0-25042a8be887', '', '', 'APP新闻发布会', '30', '/newsPublish', '', null, null, 'icon-categ');
INSERT INTO `t_menu` VALUES ('acca1a8e-de12-4c28-a67a-72f0f8daee59', '', '管理用户', '用户管理', '60', '/user', '', null, null, 'icon-users');
INSERT INTO `t_menu` VALUES ('adf3001e-d09f-48ab-aae0-b3dfe29d642e', '\0', '', '待发货', '93', '/admin/order/waitDispatcher', '', 'f3a41912-ac2c-441d-b156-4c03c884d359', null, 'icon-categ');
INSERT INTO `t_menu` VALUES ('affb3519-756d-479d-8c86-efe9e5328f68', '\0', '', '菜单（权限）管理', '1', '/admin/menu', '', '436a22cf-c0ed-4379-90b2-0da12f35c7ac', null, 'icon-menu');
INSERT INTO `t_menu` VALUES ('b4631b4e-89da-4418-a431-0991012e51e0', '\0', '', '爱好标签维护', '11', '/admin/hobbyTag', '', '436a22cf-c0ed-4379-90b2-0da12f35c7ac', null, 'icon-plugin');
INSERT INTO `t_menu` VALUES ('b70c0ff6-e200-4223-9fcf-092472218fa1', '\0', '', '正在售的商品', '82', '/admin/onsalGoods', '', 'ee0be142-fb2d-457b-89ae-3d79f812cfcd', null, 'icon-application-form-edit');
INSERT INTO `t_menu` VALUES ('b8970c22-175d-462a-ac11-401968cadc67', '\0', '', '商家账单结算', '41', '/admin/merchantBilling', '', 'cce46f8b-d732-4d4b-a161-d04ae64e8123', null, 'icon-calender');
INSERT INTO `t_menu` VALUES ('cce46f8b-d732-4d4b-a161-d04ae64e8123', '', '', '账单结算', '40', '/billSettlement', '', null, null, 'icon-chart-curve');
INSERT INTO `t_menu` VALUES ('d0213554-5330-405c-95cc-5ab89b9e656e', '\0', '', '商品sku属性管理', '6', '/admin/skuAttribute', '', '436a22cf-c0ed-4379-90b2-0da12f35c7ac', null, 'icon-discount');
INSERT INTO `t_menu` VALUES ('d0a69268-269a-40f9-b197-b9b340ead88d', '\0', '', '商品评价', '123', '/admin/goodsEvaluate', '', 'a4f25c04-1109-402f-bbc6-290f5e0a0e8b', null, 'icon-plugin');
INSERT INTO `t_menu` VALUES ('d5ed3b7e-6513-45ab-8792-e907205a20ba', '\0', '', '待签收', '94', '/admin/order/waitDeliver', '', 'f3a41912-ac2c-441d-b156-4c03c884d359', null, 'icon-chart-curve');
INSERT INTO `t_menu` VALUES ('e465876b-950c-4515-b86e-00f7ff961684', '\0', '', '区域管理', '7', '/admin/area', '', '436a22cf-c0ed-4379-90b2-0da12f35c7ac', null, 'icon-edit2');
INSERT INTO `t_menu` VALUES ('ec412909-9f6c-4ad3-9cd8-4bef3f66abd4', '\0', '', '商家管理', '52', '/admin/merchant', '', '4182c872-0539-4075-bd0e-51962eeb5c07', null, 'icon-edit2');
INSERT INTO `t_menu` VALUES ('ee0be142-fb2d-457b-89ae-3d79f812cfcd', '', '', '商品管理', '80', '/goods', '', null, null, 'icon-application-form-edit');
INSERT INTO `t_menu` VALUES ('f16a1e54-a124-4287-9a1c-6ee33958201f', '', '活动类别管理，审核商家提交的活动', '活动管理', '70', '/activity', '', null, null, 'icon-categ');
INSERT INTO `t_menu` VALUES ('f3a41912-ac2c-441d-b156-4c03c884d359', '', '', '订单管理', '90', '/order', '', null, null, 'icon-chart-bar');
INSERT INTO `t_menu` VALUES ('f59b4243-2511-4518-ad79-442c559fa5a5', '\0', '', '商圈管理', '12', '/admin/district', '\0', '436a22cf-c0ed-4379-90b2-0da12f35c7ac', null, 'icon-note');
INSERT INTO `t_menu` VALUES ('fa9d9bb6-c5f6-4b79-9a65-1ac056f5a8c1', '\0', '', 'APP封面图', '21', '/admin/appHomeImg', '', '421f2aca-8878-48be-9d0b-1ecd533c9054', null, 'icon-discount');
INSERT INTO `t_menu` VALUES ('fb478663-ae86-4e9d-9379-4c3df68875a7', '\0', '', '商家排名', '124', '/admin/merchantRank', '', 'a4f25c04-1109-402f-bbc6-290f5e0a0e8b', null, 'icon-chart-curve');

-- ----------------------------
-- Table structure for t_merchant
-- ----------------------------
DROP TABLE IF EXISTS `t_merchant`;
CREATE TABLE `t_merchant` (
  `merch_id` varchar(36) NOT NULL,
  `merch_address` longtext NOT NULL,
  `alipay_code` varchar(100) DEFAULT NULL,
  `email` varchar(50) NOT NULL,
  `id_no` varchar(20) NOT NULL,
  `mobile_no` varchar(20) NOT NULL,
  `contact_name` varchar(50) NOT NULL,
  `part_position` varchar(100) NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `emp_count` int(11) DEFAULT NULL,
  `examin_msg` longtext,
  `examin_state` int(11) DEFAULT NULL,
  `invit_code` varchar(50) DEFAULT NULL,
  `licen_code` varchar(100) DEFAULT NULL,
  `merch_name` varchar(100) NOT NULL,
  `merch_order` int(11) DEFAULT NULL,
  `merch_tell` varchar(20) NOT NULL,
  `details` varchar(255) DEFAULT NULL,
  `org_code` varchar(50) DEFAULT NULL,
  `permit` bit(1) DEFAULT NULL,
  `tax_reg_code` varchar(100) DEFAULT NULL,
  `aplley_letter_photo` varchar(36) DEFAULT NULL,
  `bus_lice_photo` varchar(36) DEFAULT NULL,
  `cover_img` varchar(36) DEFAULT NULL,
  `district_id` varchar(36) DEFAULT NULL,
  `featured_block_id` varchar(36) DEFAULT NULL,
  `hearder_img` varchar(36) DEFAULT NULL,
  `op_id_photo` varchar(36) DEFAULT NULL,
  `org_photo` varchar(36) DEFAULT NULL,
  `shopping_center_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`merch_id`),
  KEY `FK_7pk6f340yumsfjr9c2765x0l9` (`aplley_letter_photo`),
  KEY `FK_k4lfr0cix1gyaf8hqa40rh4s9` (`bus_lice_photo`),
  KEY `FK_asnb8cf4rk6k1g80tk3udqwg2` (`cover_img`),
  KEY `FK_oflmqxoyjh8pvmmh0lmdihaou` (`district_id`),
  KEY `FK_rkbmnx94cj87dl1un8gehxwo7` (`featured_block_id`),
  KEY `FK_4xcr02qh5kvc7o44lmu7pnfwf` (`hearder_img`),
  KEY `FK_j1p97srs9ah9o4mwkoq2gicmu` (`op_id_photo`),
  KEY `FK_ogvi8epu8tsx6t01atxym0udc` (`org_photo`),
  KEY `FK_dw2s77sx4pemgjm8p8o906994` (`shopping_center_id`),
  CONSTRAINT `FK_4xcr02qh5kvc7o44lmu7pnfwf` FOREIGN KEY (`hearder_img`) REFERENCES `t_img_info` (`img_id`),
  CONSTRAINT `FK_7pk6f340yumsfjr9c2765x0l9` FOREIGN KEY (`aplley_letter_photo`) REFERENCES `t_img_info` (`img_id`),
  CONSTRAINT `FK_asnb8cf4rk6k1g80tk3udqwg2` FOREIGN KEY (`cover_img`) REFERENCES `t_img_info` (`img_id`),
  CONSTRAINT `FK_dw2s77sx4pemgjm8p8o906994` FOREIGN KEY (`shopping_center_id`) REFERENCES `t_shopping_center` (`sc_id`),
  CONSTRAINT `FK_j1p97srs9ah9o4mwkoq2gicmu` FOREIGN KEY (`op_id_photo`) REFERENCES `t_img_info` (`img_id`),
  CONSTRAINT `FK_k4lfr0cix1gyaf8hqa40rh4s9` FOREIGN KEY (`bus_lice_photo`) REFERENCES `t_img_info` (`img_id`),
  CONSTRAINT `FK_oflmqxoyjh8pvmmh0lmdihaou` FOREIGN KEY (`district_id`) REFERENCES `t_district` (`district_id`),
  CONSTRAINT `FK_ogvi8epu8tsx6t01atxym0udc` FOREIGN KEY (`org_photo`) REFERENCES `t_img_info` (`img_id`),
  CONSTRAINT `FK_rkbmnx94cj87dl1un8gehxwo7` FOREIGN KEY (`featured_block_id`) REFERENCES `t_featured_block` (`fb_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_merchant
-- ----------------------------

-- ----------------------------
-- Table structure for t_merchant_adv
-- ----------------------------
DROP TABLE IF EXISTS `t_merchant_adv`;
CREATE TABLE `t_merchant_adv` (
  `adv_id` varchar(36) NOT NULL,
  `adv_order` int(11) NOT NULL,
  `begin_date_time` datetime DEFAULT NULL,
  `create_date_time` datetime NOT NULL,
  `end_date_time` datetime DEFAULT NULL,
  `adv_location` varchar(255) NOT NULL,
  `adv_img` varchar(36) NOT NULL,
  `adv_merchant` varchar(36) NOT NULL,
  PRIMARY KEY (`adv_id`),
  KEY `FK_b9cu4xdychm98olsf2d089b43` (`adv_img`),
  KEY `FK_ag3rb1o8gkjnm4muynd1ksk81` (`adv_merchant`),
  CONSTRAINT `FK_ag3rb1o8gkjnm4muynd1ksk81` FOREIGN KEY (`adv_merchant`) REFERENCES `t_merchant` (`merch_id`),
  CONSTRAINT `FK_b9cu4xdychm98olsf2d089b43` FOREIGN KEY (`adv_img`) REFERENCES `t_img_info` (`img_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_merchant_adv
-- ----------------------------

-- ----------------------------
-- Table structure for t_merchant_alliance
-- ----------------------------
DROP TABLE IF EXISTS `t_merchant_alliance`;
CREATE TABLE `t_merchant_alliance` (
  `alliance_id` varchar(36) NOT NULL,
  `alliance_desc` varchar(200) DEFAULT NULL,
  `alliance_name` varchar(100) DEFAULT NULL,
  `alliance_state` int(11) NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `promoted` bit(1) DEFAULT NULL,
  `activity_id` varchar(36) NOT NULL,
  `merchant_id` varchar(36) NOT NULL,
  PRIMARY KEY (`alliance_id`),
  KEY `FK_l1bnogq52e81k44pd505x51ao` (`activity_id`),
  KEY `FK_mn2og0eq0wvwlbn7okcamqoyc` (`merchant_id`),
  CONSTRAINT `FK_l1bnogq52e81k44pd505x51ao` FOREIGN KEY (`activity_id`) REFERENCES `t_common_activitie` (`acitivit_id`),
  CONSTRAINT `FK_mn2og0eq0wvwlbn7okcamqoyc` FOREIGN KEY (`merchant_id`) REFERENCES `t_merchant` (`merch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_merchant_alliance
-- ----------------------------

-- ----------------------------
-- Table structure for t_merchant_bill
-- ----------------------------
DROP TABLE IF EXISTS `t_merchant_bill`;
CREATE TABLE `t_merchant_bill` (
  `bill_id` varchar(36) NOT NULL,
  `alipay_transaction_code` varchar(50) DEFAULT NULL,
  `amount` double NOT NULL,
  `bill_code` varchar(50) NOT NULL,
  `create_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `handle_date` datetime DEFAULT NULL,
  `handle_state` bit(1) NOT NULL,
  `msg` varchar(255) DEFAULT NULL,
  `order_amount` bigint(20) NOT NULL,
  `poundage` double NOT NULL,
  `real_amount` double NOT NULL,
  `start_date` datetime NOT NULL,
  `merchant_id` varchar(36) NOT NULL,
  `proof_img` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`bill_id`),
  KEY `FK_ct0yteur2fqd5x63g0nfy7ne8` (`merchant_id`),
  KEY `FK_64fv8ixy4i0yvn8norh9kcyfr` (`proof_img`),
  CONSTRAINT `FK_64fv8ixy4i0yvn8norh9kcyfr` FOREIGN KEY (`proof_img`) REFERENCES `t_img_info` (`img_id`),
  CONSTRAINT `FK_ct0yteur2fqd5x63g0nfy7ne8` FOREIGN KEY (`merchant_id`) REFERENCES `t_merchant` (`merch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_merchant_bill
-- ----------------------------

-- ----------------------------
-- Table structure for t_merchant_browse
-- ----------------------------
DROP TABLE IF EXISTS `t_merchant_browse`;
CREATE TABLE `t_merchant_browse` (
  `browse_id` varchar(36) NOT NULL,
  `browse_date` datetime DEFAULT NULL,
  `merchant_id` varchar(36) NOT NULL,
  `user_id` varchar(36) NOT NULL,
  PRIMARY KEY (`browse_id`),
  KEY `FK_qmiq3d52002r3k3kb411bpeea` (`merchant_id`),
  KEY `FK_2jhrf7swa23m7kfk3e0ek36kf` (`user_id`),
  CONSTRAINT `FK_2jhrf7swa23m7kfk3e0ek36kf` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`user_id`),
  CONSTRAINT `FK_qmiq3d52002r3k3kb411bpeea` FOREIGN KEY (`merchant_id`) REFERENCES `t_merchant` (`merch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_merchant_browse
-- ----------------------------

-- ----------------------------
-- Table structure for t_merchant_display
-- ----------------------------
DROP TABLE IF EXISTS `t_merchant_display`;
CREATE TABLE `t_merchant_display` (
  `show_id` varchar(36) NOT NULL,
  `show_desc` longtext,
  `img_id` varchar(36) NOT NULL,
  `merchant_id` varchar(36) NOT NULL,
  PRIMARY KEY (`show_id`),
  KEY `FK_ixjvxvdh9lhbxi2je2f0kx2ja` (`img_id`),
  KEY `FK_ecb7ayt7lfpqgbww4cj3o97b2` (`merchant_id`),
  CONSTRAINT `FK_ecb7ayt7lfpqgbww4cj3o97b2` FOREIGN KEY (`merchant_id`) REFERENCES `t_merchant` (`merch_id`),
  CONSTRAINT `FK_ixjvxvdh9lhbxi2je2f0kx2ja` FOREIGN KEY (`img_id`) REFERENCES `t_img_info` (`img_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_merchant_display
-- ----------------------------

-- ----------------------------
-- Table structure for t_merchant_score
-- ----------------------------
DROP TABLE IF EXISTS `t_merchant_score`;
CREATE TABLE `t_merchant_score` (
  `id` varchar(36) NOT NULL,
  `score` float DEFAULT NULL,
  `merchant_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_r286lc3k5xso0kiogop8yvy5d` (`merchant_id`),
  CONSTRAINT `FK_r286lc3k5xso0kiogop8yvy5d` FOREIGN KEY (`merchant_id`) REFERENCES `t_merchant` (`merch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_merchant_score
-- ----------------------------

-- ----------------------------
-- Table structure for t_messager
-- ----------------------------
DROP TABLE IF EXISTS `t_messager`;
CREATE TABLE `t_messager` (
  `id` varchar(36) NOT NULL,
  `phone_number` varchar(36) NOT NULL,
  `security_state` int(11) NOT NULL,
  `security_code` varchar(36) DEFAULT NULL,
  `security_msg` longtext,
  `send_date` datetime DEFAULT NULL,
  `validity` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_messager
-- ----------------------------

-- ----------------------------
-- Table structure for t_message_body
-- ----------------------------
DROP TABLE IF EXISTS `t_message_body`;
CREATE TABLE `t_message_body` (
  `body_id` varchar(36) NOT NULL,
  `loc_addr` longtext,
  `file_name` longtext,
  `loc_lat` float DEFAULT NULL,
  `body_length` float DEFAULT NULL,
  `loc_lng` float DEFAULT NULL,
  `body_msg` longtext,
  `body_secret` longtext,
  `img_thumb_url` varchar(200) DEFAULT NULL,
  `thumb_secret` varchar(100) DEFAULT NULL,
  `msg_type` varchar(255) NOT NULL,
  `body_url` longtext,
  `message_id` varchar(36) NOT NULL,
  PRIMARY KEY (`body_id`),
  KEY `FK_gfeqfu7sfdjktu9r38octxv87` (`message_id`),
  CONSTRAINT `FK_gfeqfu7sfdjktu9r38octxv87` FOREIGN KEY (`message_id`) REFERENCES `t_chat_message` (`chat_message_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_message_body
-- ----------------------------

-- ----------------------------
-- Table structure for t_message_ext
-- ----------------------------
DROP TABLE IF EXISTS `t_message_ext`;
CREATE TABLE `t_message_ext` (
  `ext_id` varchar(36) NOT NULL,
  `ext_key` varchar(100) NOT NULL,
  `ext_value` longtext,
  `message_id` varchar(36) NOT NULL,
  PRIMARY KEY (`ext_id`),
  KEY `FK_fco1h3djgfvt0bayos30m4ukn` (`message_id`),
  CONSTRAINT `FK_fco1h3djgfvt0bayos30m4ukn` FOREIGN KEY (`message_id`) REFERENCES `t_chat_message` (`chat_message_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_message_ext
-- ----------------------------

-- ----------------------------
-- Table structure for t_navigation
-- ----------------------------
DROP TABLE IF EXISTS `t_navigation`;
CREATE TABLE `t_navigation` (
  `nav_id` varchar(36) NOT NULL,
  `nav_desc` longtext,
  `nava_name` varchar(50) NOT NULL,
  `nav_order` int(11) DEFAULT NULL,
  `nav_permit` bit(1) DEFAULT NULL,
  `nav_target` longtext,
  `view_url` varchar(100) DEFAULT NULL,
  `nav_img` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`nav_id`),
  KEY `FK_3qlub1d1v7aplt8r3inp1wggt` (`nav_img`),
  CONSTRAINT `FK_3qlub1d1v7aplt8r3inp1wggt` FOREIGN KEY (`nav_img`) REFERENCES `t_img_info` (`img_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_navigation
-- ----------------------------

-- ----------------------------
-- Table structure for t_notification
-- ----------------------------
DROP TABLE IF EXISTS `t_notification`;
CREATE TABLE `t_notification` (
  `n_id` varchar(36) NOT NULL,
  `n_desc` varchar(255) DEFAULT NULL,
  `n_link` varchar(255) DEFAULT NULL,
  `n_name` varchar(255) DEFAULT NULL,
  `read_state` bit(1) DEFAULT NULL,
  PRIMARY KEY (`n_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_notification
-- ----------------------------

-- ----------------------------
-- Table structure for t_order
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order` (
  `order_id` varchar(36) NOT NULL,
  `apply_date` datetime DEFAULT NULL,
  `cancel_order_date` datetime DEFAULT NULL,
  `carriage` float DEFAULT NULL,
  `carriage_num` varchar(100) DEFAULT NULL,
  `confirm_refund_time` datetime DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `delete_state` bit(1) DEFAULT NULL,
  `deliver_date` datetime DEFAULT NULL,
  `dispatcher_date` datetime DEFAULT NULL,
  `evaluate_date` datetime DEFAULT NULL,
  `order_code` varchar(50) NOT NULL,
  `order_name` longtext NOT NULL,
  `order_state` int(11) NOT NULL,
  `order_total` float NOT NULL,
  `pay_date` datetime DEFAULT NULL,
  `pay_type` varchar(255) DEFAULT NULL,
  `pingPP_id` varchar(36) DEFAULT NULL,
  `pingPP_order_no` varchar(255) DEFAULT NULL,
  `receiver_add` longtext NOT NULL,
  `receiver_name` varchar(50) NOT NULL,
  `receiver_phone` varchar(20) NOT NULL,
  `refund` float DEFAULT NULL,
  `refund_ok_date` datetime DEFAULT NULL,
  `user_msg` longtext,
  `ag_id` varchar(36) DEFAULT NULL,
  `img_info_id` varchar(36) DEFAULT NULL,
  `merchant_id` varchar(36) DEFAULT NULL,
  `user_id` varchar(36) NOT NULL,
  PRIMARY KEY (`order_id`),
  KEY `FK_mcosrnqnor2khyb5whqrvqwvy` (`ag_id`),
  KEY `FK_39mvxwfa5d07kedhs82a6k993` (`img_info_id`),
  KEY `FK_nxdo7asi4j98gxbst9ty5tg1d` (`merchant_id`),
  KEY `FK_75ffb6s55q5aibdhqs2unu49q` (`user_id`),
  CONSTRAINT `FK_39mvxwfa5d07kedhs82a6k993` FOREIGN KEY (`img_info_id`) REFERENCES `t_img_info` (`img_id`),
  CONSTRAINT `FK_75ffb6s55q5aibdhqs2unu49q` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`user_id`),
  CONSTRAINT `FK_mcosrnqnor2khyb5whqrvqwvy` FOREIGN KEY (`ag_id`) REFERENCES `t_activity_goods` (`ag_id`),
  CONSTRAINT `FK_nxdo7asi4j98gxbst9ty5tg1d` FOREIGN KEY (`merchant_id`) REFERENCES `t_merchant` (`merch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_order
-- ----------------------------

-- ----------------------------
-- Table structure for t_order_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_order_detail`;
CREATE TABLE `t_order_detail` (
  `detial_id` varchar(36) NOT NULL,
  `count` bigint(20) DEFAULT NULL,
  `price` float DEFAULT NULL,
  `goods_id` varchar(36) DEFAULT NULL,
  `order_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`detial_id`),
  KEY `FK_jmg3lyyqxywmca8t9mnf4c3c9` (`goods_id`),
  KEY `FK_h6eiw5nu5imxb4n1bkumtkbcj` (`order_id`),
  CONSTRAINT `FK_h6eiw5nu5imxb4n1bkumtkbcj` FOREIGN KEY (`order_id`) REFERENCES `t_order` (`order_id`),
  CONSTRAINT `FK_jmg3lyyqxywmca8t9mnf4c3c9` FOREIGN KEY (`goods_id`) REFERENCES `t_goods` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_order_detail
-- ----------------------------

-- ----------------------------
-- Table structure for t_received_goods_address
-- ----------------------------
DROP TABLE IF EXISTS `t_received_goods_address`;
CREATE TABLE `t_received_goods_address` (
  `addressId` varchar(36) NOT NULL,
  `is_default` bit(1) NOT NULL,
  `receiver_address` longtext NOT NULL,
  `receiver_name` varchar(50) NOT NULL,
  `receiver_phone` varchar(20) NOT NULL,
  `user_id` varchar(36) NOT NULL,
  PRIMARY KEY (`addressId`),
  KEY `FK_5ytqs03dcmioq8e17rl7n7c12` (`user_id`),
  CONSTRAINT `FK_5ytqs03dcmioq8e17rl7n7c12` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_received_goods_address
-- ----------------------------

-- ----------------------------
-- Table structure for t_reception_room
-- ----------------------------
DROP TABLE IF EXISTS `t_reception_room`;
CREATE TABLE `t_reception_room` (
  `room_id` varchar(36) NOT NULL,
  `deleted` bit(1) DEFAULT NULL,
  `room_name` varchar(50) NOT NULL,
  `template_path` varchar(100) NOT NULL,
  `attribute_id` varchar(255) NOT NULL,
  `cover_img` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`room_id`),
  KEY `FK_8u2gl1r40pu7c9d9s2a1irowp` (`cover_img`),
  CONSTRAINT `FK_8u2gl1r40pu7c9d9s2a1irowp` FOREIGN KEY (`cover_img`) REFERENCES `t_img_info` (`img_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_reception_room
-- ----------------------------

-- ----------------------------
-- Table structure for t_recommend
-- ----------------------------
DROP TABLE IF EXISTS `t_recommend`;
CREATE TABLE `t_recommend` (
  `recommend_id` varchar(36) NOT NULL,
  `create_date` datetime NOT NULL,
  `order_index` int(11) NOT NULL,
  `reason` longtext,
  `goods_id` varchar(36) NOT NULL,
  `merchant_id` varchar(36) NOT NULL,
  PRIMARY KEY (`recommend_id`),
  UNIQUE KEY `UK_rnrfduguotww71pla37w0vjau` (`goods_id`),
  KEY `FK_tao91itk0qpxlt2lpvxk8gmi2` (`merchant_id`),
  CONSTRAINT `FK_rnrfduguotww71pla37w0vjau` FOREIGN KEY (`goods_id`) REFERENCES `t_goods` (`goods_id`),
  CONSTRAINT `FK_tao91itk0qpxlt2lpvxk8gmi2` FOREIGN KEY (`merchant_id`) REFERENCES `t_merchant` (`merch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_recommend
-- ----------------------------

-- ----------------------------
-- Table structure for t_redenvelop_item
-- ----------------------------
DROP TABLE IF EXISTS `t_redenvelop_item`;
CREATE TABLE `t_redenvelop_item` (
  `item_id` varchar(36) NOT NULL,
  `amount_money` float NOT NULL,
  `extract_state` bit(1) NOT NULL,
  `received` bit(1) NOT NULL,
  `received_date` datetime DEFAULT NULL,
  `envelop_id` varchar(36) NOT NULL,
  `user_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`item_id`),
  KEY `FK_o0yf57e4vsxwokk0tv3w3flps` (`envelop_id`),
  KEY `FK_q2238y11eekqlp6swdxr13d8u` (`user_id`),
  CONSTRAINT `FK_o0yf57e4vsxwokk0tv3w3flps` FOREIGN KEY (`envelop_id`) REFERENCES `t_red_envelop` (`envelop_id`),
  CONSTRAINT `FK_q2238y11eekqlp6swdxr13d8u` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_redenvelop_item
-- ----------------------------

-- ----------------------------
-- Table structure for t_red_envelop
-- ----------------------------
DROP TABLE IF EXISTS `t_red_envelop`;
CREATE TABLE `t_red_envelop` (
  `envelop_id` varchar(36) NOT NULL,
  `create_date` datetime NOT NULL,
  `envelop_msg` varchar(100) DEFAULT NULL,
  `numbers` int(11) NOT NULL,
  `sended_date` datetime DEFAULT NULL,
  `sended` bit(1) NOT NULL,
  `total_money` float NOT NULL,
  `activity_id` varchar(36) NOT NULL,
  `merchant_id` varchar(36) NOT NULL,
  PRIMARY KEY (`envelop_id`),
  KEY `FK_4un58t7cnbj524lnwv0pfxeya` (`activity_id`),
  KEY `FK_jd6y4l6la3y3me8rxeudcv2cy` (`merchant_id`),
  CONSTRAINT `FK_4un58t7cnbj524lnwv0pfxeya` FOREIGN KEY (`activity_id`) REFERENCES `t_common_activitie` (`acitivit_id`),
  CONSTRAINT `FK_jd6y4l6la3y3me8rxeudcv2cy` FOREIGN KEY (`merchant_id`) REFERENCES `t_merchant` (`merch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_red_envelop
-- ----------------------------

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `role_id` varchar(36) NOT NULL,
  `role_desc` longtext,
  `role_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES ('2db6dc78-229f-4267-892b-79fd9e8a4d4d', '', '普通商户');
INSERT INTO `t_role` VALUES ('4028b881515b9dfd01515c1270460000', '', '系统管理员');

-- ----------------------------
-- Table structure for t_shopping_cart
-- ----------------------------
DROP TABLE IF EXISTS `t_shopping_cart`;
CREATE TABLE `t_shopping_cart` (
  `shopping_cart_id` varchar(36) NOT NULL,
  `amount` int(11) DEFAULT NULL,
  `focus_date` datetime DEFAULT NULL,
  `goods_id` varchar(36) NOT NULL,
  `user_id` varchar(36) NOT NULL,
  PRIMARY KEY (`shopping_cart_id`),
  KEY `FK_gpyddfcmw0j4n1thnodpij9y1` (`goods_id`),
  KEY `FK_o0us5gk8humstagdo934u3f8x` (`user_id`),
  CONSTRAINT `FK_gpyddfcmw0j4n1thnodpij9y1` FOREIGN KEY (`goods_id`) REFERENCES `t_goods` (`goods_id`),
  CONSTRAINT `FK_o0us5gk8humstagdo934u3f8x` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_shopping_cart
-- ----------------------------

-- ----------------------------
-- Table structure for t_shopping_center
-- ----------------------------
DROP TABLE IF EXISTS `t_shopping_center`;
CREATE TABLE `t_shopping_center` (
  `sc_id` varchar(36) NOT NULL,
  `permit` bit(1) DEFAULT NULL,
  `sc_name` varchar(100) NOT NULL,
  `sc_order` int(11) DEFAULT NULL,
  `imgInfo_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`sc_id`),
  KEY `FK_dapq3pibhlvjq8imkd6ek8o48` (`imgInfo_id`),
  CONSTRAINT `FK_dapq3pibhlvjq8imkd6ek8o48` FOREIGN KEY (`imgInfo_id`) REFERENCES `t_img_info` (`img_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_shopping_center
-- ----------------------------

-- ----------------------------
-- Table structure for t_system_config
-- ----------------------------
DROP TABLE IF EXISTS `t_system_config`;
CREATE TABLE `t_system_config` (
  `config_id` varchar(36) NOT NULL,
  `config_desc` varchar(200) DEFAULT NULL,
  `confit_name` varchar(50) DEFAULT NULL,
  `config_type` int(11) NOT NULL,
  `config_value` varchar(200) NOT NULL,
  PRIMARY KEY (`config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_system_config
-- ----------------------------

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `user_id` varchar(36) NOT NULL,
  `user_birthday` date DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `income` varchar(100) DEFAULT NULL,
  `invit_code` varchar(20) DEFAULT NULL,
  `occupation` varchar(100) DEFAULT NULL,
  `permit` bit(1) DEFAULT NULL,
  `user_pwd` varchar(40) NOT NULL,
  `user_sex` bit(1) DEFAULT NULL,
  `user_name` varchar(50) DEFAULT NULL,
  `user_phone` varchar(20) DEFAULT NULL,
  `area_id` varchar(36) DEFAULT NULL,
  `portrait` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  KEY `FK_5rsn23du0bfal1nxfc4fd1ibq` (`area_id`),
  KEY `FK_agugslrdm7wt2gv8e7lhexxoj` (`portrait`),
  CONSTRAINT `FK_5rsn23du0bfal1nxfc4fd1ibq` FOREIGN KEY (`area_id`) REFERENCES `t_area` (`area_id`),
  CONSTRAINT `FK_agugslrdm7wt2gv8e7lhexxoj` FOREIGN KEY (`portrait`) REFERENCES `t_img_info` (`img_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_user
-- ----------------------------

-- ----------------------------
-- Table structure for t_user_withdraw
-- ----------------------------
DROP TABLE IF EXISTS `t_user_withdraw`;
CREATE TABLE `t_user_withdraw` (
  `uwd_id` varchar(36) NOT NULL,
  `alipay_code` varchar(100) NOT NULL,
  `alipay_order_num` varchar(255) DEFAULT NULL,
  `apply_date` datetime DEFAULT NULL,
  `money` float DEFAULT NULL,
  `opeator` varchar(100) DEFAULT NULL,
  `poundage` float DEFAULT NULL,
  `real_money` float DEFAULT NULL,
  `withdraw_date` datetime DEFAULT NULL,
  `withdraw_state` int(11) DEFAULT NULL,
  `user_id` varchar(36) NOT NULL,
  PRIMARY KEY (`uwd_id`),
  KEY `FK_jiuenq0hk9wjp4tu13ir1j6ov` (`user_id`),
  CONSTRAINT `FK_jiuenq0hk9wjp4tu13ir1j6ov` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_user_withdraw
-- ----------------------------

-- ----------------------------
-- Table structure for t_wallet
-- ----------------------------
DROP TABLE IF EXISTS `t_wallet`;
CREATE TABLE `t_wallet` (
  `id` varchar(36) NOT NULL,
  `total_money` float DEFAULT NULL,
  `user_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_e4leqv2evy8spay8756h5ml6m` (`user_id`),
  CONSTRAINT `FK_e4leqv2evy8spay8756h5ml6m` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_wallet
-- ----------------------------
