/*
SQLyog Professional v12.09 (64 bit)
MySQL - 10.6.7-MariaDB-log : Database - kokonut_user
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`kokonut_user` /*!40100 DEFAULT CHARACTER SET utf8mb3 */;
CREATE DATABASE `kokonut_remove` /*!40100 DEFAULT CHARACTER SET utf8mb3 */;
CREATE DATABASE `kokonut_dormant` /*!40100 DEFAULT CHARACTER SET utf8mb3 */;

USE `kokonut_user`;

/*Table structure for table `common` */

DROP TABLE IF EXISTS `common`;

CREATE TABLE `common` (
  `IDX` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '인덱스(기본적용,수정불가)',
  `ID` varchar(128) NOT NULL DEFAULT 'kokonut_Default' COMMENT '아이디(기본적용,수정불가)',
  `PASSWORD` varchar(256) NOT NULL DEFAULT 'kokonut_Default' COMMENT '비밀번호(암호화,기본적용,수정불가)',
  `PERSONAL_INFO_AGREE` varchar(1) NOT NULL DEFAULT 'Y' COMMENT '개인정보 동의(기본적용,수정불가)',
  `OFFER_INFO_AGREE` varchar(1) DEFAULT 'Y' COMMENT '제3자제공 동의(기본적용,수정불가)',
  `REGDATE` datetime NOT NULL COMMENT '등록 일시(기본적용,수정불가)',
  `SEND_MAIL_DATE` datetime DEFAULT NULL COMMENT '이용내역보낸 날짜(기본적용,수정불가)',
  `LAST_LOGIN_DATE` datetime DEFAULT NULL COMMENT '최종 로그인 일시(기본적용,수정불가)',
  `MODIFY_DATE` datetime DEFAULT NULL COMMENT '수정 일시(기본적용,수정불가)',
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

/*2월대개봉 사업자번호 테이블*/
DROP TABLE IF EXISTS `3488101536`;

CREATE TABLE `3488101536` (
    `IDX` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '인덱스(기본적용,수정불가)',
    `ID` varchar(128) NOT NULL DEFAULT 'kokonut_Default' COMMENT '아이디(기본적용,수정불가)',
    `PASSWORD` varchar(256) NOT NULL DEFAULT 'kokonut_Default' COMMENT '비밀번호(암호화,기본적용,수정불가)',
    `PERSONAL_INFO_AGREE` varchar(1) NOT NULL DEFAULT 'Y' COMMENT '개인정보 동의(기본적용,수정불가)',
    `OFFER_INFO_AGREE` varchar(1) DEFAULT 'Y' COMMENT '제3자제공 동의(기본적용,수정불가)',
    `REGDATE` datetime NOT NULL COMMENT '등록 일시(기본적용,수정불가)',
    `SEND_MAIL_DATE` datetime DEFAULT NULL COMMENT '이용내역보낸 날짜(기본적용,수정불가)',
    `LAST_LOGIN_DATE` datetime DEFAULT NULL COMMENT '최종 로그인 일시(기본적용,수정불가)',
    `MODIFY_DATE` datetime DEFAULT NULL COMMENT '수정 일시(기본적용,수정불가)',
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb3;



/*Data for the table `common` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
