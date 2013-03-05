CREATE DATABASE `automation`;

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `harness`
-- ----------------------------
DROP TABLE IF EXISTS `harness`;
CREATE TABLE `harness` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `testName` varchar(255) DEFAULT NULL,
  `passed` int(11) DEFAULT NULL,
  `failed` int(11) DEFAULT NULL,
  `exceptions` int(11) DEFAULT NULL,
  `batchId` varchar(255) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `module` varchar(200) DEFAULT NULL,
  `feature` varchar(200) DEFAULT NULL,
  `subfeature` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

