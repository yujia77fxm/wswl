/*
 Navicat Premium Data Transfer

 Source Server         : enab
 Source Server Type    : MySQL
 Source Server Version : 50616
 Source Host           : 106.14.117.83:3306
 Source Schema         : eanble

 Target Server Type    : MySQL
 Target Server Version : 50616
 File Encoding         : 65001

 Date: 14/06/2020 20:48:06
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tranction
-- ----------------------------
DROP TABLE IF EXISTS `tranction`;
CREATE TABLE `tranction` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `base58_check` varchar(128) NOT NULL,
  `from_address` varchar(128) NOT NULL,
  `to_address` varchar(128) NOT NULL,
  `amount` bigint(20) DEFAULT NULL,
  `is_success` bit(1) DEFAULT b'0',
  `is_confirmed` bit(1) DEFAULT b'0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `tranction_id` varchar(128) NOT NULL,
  `tokenid` varchar(32) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10000006 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of tranction
-- ----------------------------
BEGIN;
INSERT INTO `tranction` VALUES (10000000, 'base58Check', '41517bbbbecfad07303c8e59b53b071b8f7a8ab456', '41d257753d8eb868dc1708f7c3d42d16bddb279217', 1088, b'1', b'0', '2020-06-14 13:58:34', '', '');
INSERT INTO `tranction` VALUES (10000001, 'base58Check', '41517bbbbecfad07303c8e59b53b071b8f7a8ab456', '41d257753d8eb868dc1708f7c3d42d16bddb279217', 101, b'1', b'0', '2020-06-14 14:05:32', '', '');
INSERT INTO `tranction` VALUES (10000002, 'base58Check', '41517bbbbecfad07303c8e59b53b071b8f7a8ab456', '41d257753d8eb868dc1708f7c3d42d16bddb279217', 101, b'1', b'0', '2020-06-14 14:05:34', '', '');
INSERT INTO `tranction` VALUES (10000003, 'base58Check', '41517bbbbecfad07303c8e59b53b071b8f7a8ab456', '41d257753d8eb868dc1708f7c3d42d16bddb279217', 88, b'1', b'0', '2020-06-14 14:05:39', '', '');
INSERT INTO `tranction` VALUES (10000004, 'base58Check', 'TCzvRmtUww6dykCmxeFC6LXbdJvfZ7ikGb', 'TRdoRP2U8HxpERpaFqBa2daQXWhHkB8cz1', 1, b'1', b'0', '2020-06-14 16:49:24', '', '');
INSERT INTO `tranction` VALUES (10000005, 'base58Check', 'TCzvRmtUww6dykCmxeFC6LXbdJvfZ7ikGb', 'TRdoRP2U8HxpERpaFqBa2daQXWhHkB8cz1', 1, b'1', b'0', '2020-06-14 17:23:24', '', '');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
