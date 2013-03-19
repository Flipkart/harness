CREATE DATABASE `automation`;

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

use `automation`;

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

-- ----------------------------
--  Table structure for `perf`
-- ----------------------------
DROP TABLE IF EXISTS `perf`;
CREATE TABLE `perf` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `testName` varchar(255) DEFAULT NULL,
  `samples` int(11) DEFAULT NULL,
  `success` int(11) DEFAULT NULL,
  `failures` int(11) DEFAULT NULL,
  `minResponseTime` int(11) DEFAULT NULL,
  `maxResponseTime` int(11) DEFAULT NULL,
  `avgResponseTime` int(11) DEFAULT NULL,
  `median` int(11) DEFAULT NULL,
  `ninetiethPercentile` int(11) DEFAULT NULL,
  `throughput` int(11) DEFAULT NULL,
  `batchId` varchar(255) NOT NULL DEFAULT '',
  `type` varchar(30) DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `module` varchar(200) DEFAULT NULL,
  `feature` varchar(200) DEFAULT NULL,
  `subfeature` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`,`batchId`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

