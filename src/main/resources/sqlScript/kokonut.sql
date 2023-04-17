/*
SQLyog Professional v12.09 (64 bit)
MySQL - 5.7.26-log : Database - kokonut
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`kokonut` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `kokonut`;

/* 고유번호 기록 테이블 */
CREATE TABLE `kn_autonum` (
  `auto_key` varchar(50) NOT NULL DEFAULT '' COMMENT '번호채번유형',
  `prefix` varchar(6) DEFAULT NULL COMMENT '번호채벌 앞 prefix',
  `key_decimal` int(11) DEFAULT NULL COMMENT 'seq 자리수(1,6)까지만 일단 넣자',
  `remark` varchar(100) DEFAULT NULL COMMENT '간단한키설명',
  `modify_id` varchar(50) DEFAULT NULL,
  `modify_dt` datetime DEFAULT NULL,
  PRIMARY KEY (`auto_key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci COMMENT='번호채번테이블';

/* 고유번호 로그 기록 테이블 */
CREATE TABLE `kn_autonum_log` (
  `auto_key` varchar(50) NOT NULL DEFAULT '' COMMENT '키값',
  `prefix` varchar(20) NOT NULL DEFAULT '' COMMENT '번호채번앞자리문자',
  `last_seq` int(11) DEFAULT NULL COMMENT '마지막채번된 seq',
  `last_key` varchar(20) DEFAULT NULL COMMENT '마지막 채번된 전체 번호',
  `modify_id` varchar(20) DEFAULT NULL,
  `modify_dt` datetime DEFAULT NULL,
  PRIMARY KEY (`auto_key`,`prefix`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci COMMENT='번호채번세부테이블';

/*Table structure for table `activity` */

DROP TABLE IF EXISTS `activity`;

CREATE TABLE `activity` (
  `IDX` int(11) NOT NULL AUTO_INCREMENT COMMENT '주키',
  `TYPE` int(11) DEFAULT NULL COMMENT '활동 종류(1:고객정보처리,2:관리자활동,3:회원DB관리이력)',
  `ACTIVITY` varchar(128) DEFAULT NULL COMMENT '활동',
  `MONTH` int(11) DEFAULT NULL COMMENT '활동내역(1:로그인,2:회원정보변경,3:회원정보삭제,4:관리자추가,5:관리자권한변경,6:열람이력다운로드,7:활동이력다운로드,8:고객정보 열람,9:고객정보 다운로드,10:고객정보 처리,11:회원정보DB관리 변경,12:회원DB 항목 관리 변경,13:회원 관리 등록,14:정보제공 목록,15:정보 파기 관리,16:테이블 생성,17:전체DB다운로드,18:회원 관리 변경)',
  `REGDATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일',
  `MODIFY_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '수정일',
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

/*Data for the table `activity` */

insert  into `activity`(`IDX`,`TYPE`,`ACTIVITY`,`MONTH`,`REGDATE`,`MODIFY_DATE`) values (1,2,'로그인',36,'2022-06-29 10:13:25','2022-07-06 13:49:48'),(2,2,'회원정보변경',36,'2022-06-29 10:13:30','2022-07-06 13:49:48'),(3,2,'회원정보삭제',36,'2022-06-29 10:13:34','2022-07-06 13:49:48'),(4,2,'관리자추가',36,'2022-06-29 10:13:37','2022-07-06 13:49:48'),(5,2,'관리자권한변경',36,'2022-06-29 10:13:42','2022-07-06 13:49:48'),(6,2,'열람이력다운로드',36,'2022-06-29 10:13:45','2022-07-06 13:49:49'),(7,2,'활동이력다운로드',36,'2022-06-29 10:13:50','2022-07-06 13:49:49'),(8,1,'고객정보 열람',36,'2022-06-29 10:14:13','2022-07-06 13:49:50'),(9,1,'고객정보 다운로드',36,'2022-06-29 10:14:17','2022-07-06 13:49:50'),(10,1,'고객정보 처리',36,'2022-06-29 10:14:20','2022-07-06 13:49:50'),(11,2,'회원정보DB관리 변경',36,'2022-06-29 10:14:30','2022-07-06 13:49:49'),(12,2,'회원DB 항목 관리 변경',36,'2022-06-29 10:14:33','2022-07-06 13:49:49'),(13,2,'회원 관리 등록',36,'2022-06-29 10:14:37','2022-07-06 13:49:49'),(14,2,'정보제공 목록',36,'2022-06-29 10:14:41','2022-07-06 13:49:49'),(15,3,'정보 파기 관리',36,'2022-06-29 10:14:45','2022-07-06 13:49:49'),(16,2,'테이블 생성',36,'2022-06-29 10:14:51','2022-07-06 13:49:49'),(17,2,'전체DB다운로드',36,'2022-06-29 10:14:55','2022-07-06 13:49:49'),(18,2,'회원 관리 변경',36,'2022-06-29 10:14:56','2022-07-06 13:49:49');

/*Table structure for table `activity_history` */

DROP TABLE IF EXISTS `activity_history`;

CREATE TABLE `activity_history` (
  `IDX` int(11) NOT NULL AUTO_INCREMENT COMMENT '키',
  `COMPANY_IDX` int(11) DEFAULT NULL COMMENT '회사(Company) 키',
  `TYPE` int(11) DEFAULT NULL COMMENT '1:고객정보처리,2:관리자활동,3:회원DB관리이력',
  `ADMIN_IDX` int(11) DEFAULT NULL COMMENT '관리자키',
  `ACTIVITY` int(11) DEFAULT NULL COMMENT '활동내역(1:로그인,2:회원정보변경,3:회원정보삭제,4:관리자추가,5:관리자권한변경,6:처리이력다운로드,7:활동이력다운로드,8:고객정보 열람,9:고객정보 다운로드,10:고객정보 처리,11:회원정보DB관리 변경,12:회원DB 항목 관리 변경,13:회원 관리 등록,14:정보제공 목록,15:정보 파기 관리,16:테이블 생성,17:전체DB다운로드,18:회원 관리 변경)',
  `ACTIVITY_IDX` int(11) DEFAULT NULL COMMENT 'activity IDX',
  `ACTIVITY_DETAIL` varchar(256) DEFAULT NULL COMMENT '활동 상세 내역',
  `REASON` varchar(256) DEFAULT NULL COMMENT '사유',
  `IP_ADDR` varchar(64) DEFAULT NULL COMMENT '접속IP주소',
  `REGDATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '활동일시',
  `STATE` int(11) DEFAULT NULL COMMENT '0:비정상,1:정상',
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `activity_history` */

/*Table structure for table `address_book` */

DROP TABLE IF EXISTS `address_book`;

CREATE TABLE `address_book` (
  `IDX` int(11) NOT NULL AUTO_INCREMENT,
  `COMPANY_IDX` int(11) DEFAULT NULL COMMENT '회사 키(주소록 보는 권한이 개인이면 삭제해도 되는 컬럼)',
  `ADMIN_IDX` int(11) DEFAULT NULL COMMENT '관리자 키',
  `IP` varchar(64) DEFAULT NULL COMMENT 'IP',
  `REGDATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
  `EXP_DATE` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '만료일시',
  `MODIFY_DATE` timestamp NULL DEFAULT '0000-00-00 00:00:00' COMMENT '수정일자',
  `IS_SENDED` char(1) DEFAULT NULL COMMENT '발송여부(Y/N)',
  `SEND_DATE` timestamp NULL DEFAULT NULL COMMENT '발송일',
  `USE` varchar(256) DEFAULT NULL COMMENT '주소록 용도',
  `PURPOSE` varchar(16) DEFAULT NULL COMMENT '발송목적(NOTICE: 주요공지, AD:광고/홍보)',
  `FILE_GROUP_ID` varchar(256) DEFAULT NULL COMMENT '파일 그룹 아이디',
  `TARGET` varchar(16) DEFAULT NULL COMMENT '발송대상(ALL: 전체회원, SELECTED: 선택회원)',
  `TYPE` varchar(16) DEFAULT NULL COMMENT '메시지종류(EMAIL: 이메일, alimTalk ALIMTALK: 알림톡)',
  `SENDER_EMAIL` varchar(256) DEFAULT NULL COMMENT '발신자 이메일',
  `TITLE` varchar(256) DEFAULT NULL COMMENT '메시지 제목',
  `CONTENT` text COMMENT '메시지 내용',
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `address_book` */

/*Table structure for table `address_book_user` */

DROP TABLE IF EXISTS `address_book_user`;

CREATE TABLE `address_book_user` (
  `IDX` int(11) NOT NULL AUTO_INCREMENT COMMENT '키',
  `ADDRESS_BOOK_IDX` int(11) DEFAULT NULL COMMENT '주소록 키',
  `ID` varchar(256) DEFAULT NULL COMMENT '아이디',
  `REGDATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `address_book_user` */

/*Table structure for table `admin` */

DROP TABLE IF EXISTS `admin`;

CREATE TABLE `admin` (
  `IDX` int(11) NOT NULL AUTO_INCREMENT COMMENT '키',
  `COMPANY_IDX` int(11) DEFAULT NULL COMMENT 'COMPANY IDX',
  `MASTER_IDX` int(11) DEFAULT NULL COMMENT '마스터IDX(마스터는 0):관리자로 등록한 마스터의 키',
  `USER_TYPE` int(11) DEFAULT NULL COMMENT '회원타입(1:사업자,2:개인)',
  `EMAIL` varchar(128) DEFAULT NULL COMMENT '이메일',
  `PASSWORD` varchar(256) DEFAULT NULL COMMENT '비밀번호',
  `PWD_CHANGE_DATE` timestamp NULL DEFAULT NULL COMMENT '비밀번호 변경 일자',
  `PWD_ERROR_COUNT` int(11) DEFAULT '0' COMMENT '비밀번호오류횟수',
  `NAME` varchar(128) DEFAULT NULL COMMENT '이름(대표자명)',
  `PHONE_NUMBER` varchar(64) DEFAULT NULL COMMENT '휴대폰번호',
  `DEPARTMENT` varchar(128) DEFAULT NULL COMMENT '부서',
  `STATE` int(11) DEFAULT '1' COMMENT '0:정지(권한해제),1:사용,2:로그인제한(비번5회오류),3:탈퇴, 4:휴면계정',
  `DORMANT_DATE` timestamp NULL DEFAULT NULL COMMENT '휴면계정 전환일',
  `EXPECTED_DELETE_DATE` timestamp NULL DEFAULT NULL COMMENT '계정삭제예정일',
  `REASON` varchar(256) DEFAULT NULL COMMENT '권한해제 사유',
  `IP` varchar(64) DEFAULT NULL COMMENT '최근 접속 IP',
  `APPROVAL_STATE` int(11) DEFAULT '1' COMMENT '승인상태(1:승인대기, 2:승인완료, 3:승인보류)',
  `APPROVAL_DATE` timestamp NULL DEFAULT NULL COMMENT '관리자승인일시,반려일시',
  `APPROVAL_RETURN_REASON` varchar(512) DEFAULT NULL COMMENT '관리자 반려 사유',
  `APPROVAL_NAME` varchar(64) DEFAULT NULL COMMENT '승인자(반려자)',
  `WITHDRAWAL_REASON_TYPE` int(11) DEFAULT NULL COMMENT '탈퇴사유선택(1:계정변경, 2:서비스이용불만,3:사용하지않음,4:기타)',
  `WITHDRAWAL_REASON` varchar(256) DEFAULT NULL COMMENT '탈퇴사유',
  `WITHDRAWAL_DATE` timestamp NULL DEFAULT NULL COMMENT '탈퇴일시',
  `LAST_LOGIN_DATE` timestamp NULL DEFAULT NULL COMMENT '최근접속일시(휴면계정전환에 필요)',
  `IS_EMAIL_AUTH` char(1) DEFAULT 'N' COMMENT '이메일인증여부',
  `EMAIL_AUTH_NUMBER` varchar(256) DEFAULT NULL COMMENT '이메일인증번호',
  `PWD_AUTH_NUMBER` varchar(256) DEFAULT NULL COMMENT '비밀번호 설정 시 인증번호',
  `AUTH_START_DATE` timestamp NULL DEFAULT NULL COMMENT '인증시작시간',
  `AUTH_END_DATE` timestamp NULL DEFAULT NULL COMMENT '인증종료시간',
  `OTP_KEY` varchar(128) DEFAULT NULL COMMENT '구글 OTP에 사용될 KEY',
  `IS_LOGIN_AUTH` char(1) DEFAULT 'N' COMMENT 'GOOGLE인증여부',
  `ROLE_NAME` varchar(32) DEFAULT NULL COMMENT '권한(시스템관리자:ROLE_SYSTEM, 마스터관리자:ROLE_MASTER, 일반관리자 : ROLE_ADMIN)',
  `REGDATE` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
  `MODIFIER_IDX` int(11) DEFAULT NULL COMMENT '수정자',
  `MODIFIER_NAME` varchar(64) DEFAULT NULL COMMENT '수정자이름',
  `MODIFY_DATE` timestamp NULL DEFAULT '0000-00-00 00:00:00' COMMENT '수정일시',
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `admin` */

insert  into `admin`(`IDX`,`COMPANY_IDX`,`MASTER_IDX`,`ADMIN_LEVEL_IDX`,`USER_TYPE`,`EMAIL`,`PASSWORD`,`PWD_CHANGE_DATE`,`PWD_ERROR_COUNT`,`NAME`,`PHONE_NUMBER`,`DEPARTMENT`,`STATE`,`DORMANT_DATE`,`EXPECTED_DELETE_DATE`,`REASON`,`IP`,`APPROVAL_STATE`,`APPROVAL_DATE`,`APPROVAL_RETURN_REASON`,`APPROVAL_NAME`,`WITHDRAWAL_REASON_TYPE`,`WITHDRAWAL_REASON`,`WITHDRAWAL_DATE`,`LAST_LOGIN_DATE`,`IS_EMAIL_AUTH`,`EMAIL_AUTH_NUMBER`,`PWD_AUTH_NUMBER`,`AUTH_START_DATE`,`AUTH_END_DATE`,`OTP_KEY`,`IS_LOGIN_AUTH`,`ROLE_NAME`,`REGDATE`,`MODIFIER_IDX`,`MODIFIER_NAME`,`MODIFY_DATE`) values (1,1,0,0,NULL,'kokonut_admin@kokonut.me','d31def3355cd26f480eb7f93697dab89d87606fa9560194be143e25a0e28f798',NULL,0,'시스템관리자','01064396533',NULL,1,NULL,NULL,NULL,'10.0.8.227',2,'0000-00-00 00:00:00',NULL,NULL,NULL,NULL,NULL,'2022-07-19 12:56:01','Y',NULL,NULL,NULL,NULL,'GIMG6YAXBEIL5DNG','N','ROLE_SYSTEM','2021-06-15 16:12:20',1,'시스템관리자','2022-01-13 16:26:40');

/*Table structure for table `admin_level` */

DROP TABLE IF EXISTS `admin_level`;

CREATE TABLE `admin_level` (
  `IDX` int(11) NOT NULL AUTO_INCREMENT COMMENT '키',
  `LEVEL` varchar(128) DEFAULT NULL COMMENT '관리자 등급',
  `MENU_1` int(11) DEFAULT '0' COMMENT 'API 메뉴얼 관리(0:사용안함,1:사용)',
  `MENU_2` int(11) DEFAULT '0' COMMENT '회원 관리(0:사용안함,1:사용)',
  `MENU_3` int(11) DEFAULT '0' COMMENT '관리자 관리(0:사용안함,1:사용)',
  `MENU_4` int(11) DEFAULT '0' COMMENT '메일 발송 관리(0:사용안함,1:사용)',
  `MENU_5` int(11) DEFAULT '0' COMMENT '카카오톡 메세지 관리(0:사용안함,1:사용)',
  `MENU_6` int(11) DEFAULT '0' COMMENT '개인정보처리방침 관리(0:사용안함,1:사용)',
  `MENU_7` int(11) DEFAULT '0' COMMENT '설정(0:사용안함,1:사용)',
  `MENU_8` int(11) DEFAULT '0' COMMENT '회원정보제공(0:사용안함,1:사용)',
  `MENU_9` int(11) DEFAULT '0' COMMENT '사용안함(0:사용안함,1:사용)',
  `MENU_10` int(11) DEFAULT '0' COMMENT '사용안함(0:사용안함,1:사용)',
  `MENU_11` int(11) DEFAULT '0' COMMENT '사용안함(0:사용안함,1:사용)',
  `MENU_12` int(11) DEFAULT '0' COMMENT '사용안함(0:사용안함,1:사용)',
  `BASIC_LIMIT` int(11) DEFAULT '0' COMMENT 'BASIC등급제한(사용안함)',
  `STANDARD_LIMIT` int(11) DEFAULT '0' COMMENT 'STANDARD등급제한(사용안함)',
  `PREMIUM_LIMIT` int(11) DEFAULT '0' COMMENT 'PREMIUM등급제한(사용안함)',
  `REGDATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
  `MODIFY_DATE` timestamp NULL DEFAULT '0000-00-00 00:00:00' COMMENT '수정일시',
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Data for the table `admin_level` */

insert  into `admin_level`(`IDX`,`LEVEL`,`MENU_1`,`MENU_2`,`MENU_3`,`MENU_4`,`MENU_5`,`MENU_6`,`MENU_7`,`MENU_8`,`MENU_9`,`MENU_10`,`MENU_11`,`MENU_12`,`BASIC_LIMIT`,`STANDARD_LIMIT`,`PREMIUM_LIMIT`,`REGDATE`,`MODIFY_DATE`) values (1,'사업자',1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,'2021-12-13 11:18:47','2022-01-07 13:26:02'),(2,'최고 관리자',1,1,1,1,1,1,1,1,1,1,1,1,99999,99999,99999,'2022-01-13 11:18:17','2022-07-12 10:18:23'),(3,'개인정보 관리자',0,1,1,1,1,1,0,1,1,1,1,1,99999,99999,99999,'2022-01-13 11:18:35','2022-07-18 15:26:08'),(4,'일반 관리자',0,0,0,1,1,0,0,1,1,1,0,0,99999,99999,99999,'2022-01-13 11:18:46','2022-07-18 15:26:20'),(5,'제3자',1,1,0,0,0,0,0,0,0,0,0,0,99999,99999,99999,'2022-01-13 11:18:55','2022-07-18 15:25:19');

/*Table structure for table `admin_remove` */

DROP TABLE IF EXISTS `admin_remove`;

CREATE TABLE `admin_remove` (
  `ADMIN_IDX` int(11) NOT NULL COMMENT 'ADMIN IDX',
  `EMAIL` varchar(128) DEFAULT NULL COMMENT '이메일',
  `REASON` int(11) DEFAULT NULL COMMENT '탈퇴 사유(1:계정변경, 2:서비스 이용 불만, 3:사용하지 않음, 4:기타)',
  `REASON_DETAIL` varchar(256) DEFAULT NULL COMMENT '탈퇴 사유',
  `REGDATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '탈퇴일시',
  PRIMARY KEY (`ADMIN_IDX`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `admin_remove` */

/*Table structure for table `admin_setting` */

DROP TABLE IF EXISTS `admin_setting`;

CREATE TABLE `admin_setting` (
  `IS_OPERATION` char(1) NOT NULL DEFAULT 'N' COMMENT '스케줄러 작동여부',
  `LOGIN_SESSION_TIME` int(11) NOT NULL DEFAULT '10' COMMENT '로그인 세션 시간'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `admin_setting` */

insert  into `admin_setting`(`IS_OPERATION`,`LOGIN_SESSION_TIME`) values ('N',10);

/*Table structure for table `alimtalk_message` */

DROP TABLE IF EXISTS `alimtalk_message`;

CREATE TABLE `alimtalk_message` (
  `IDX` int(11) NOT NULL AUTO_INCREMENT COMMENT '키',
  `COMPANY_IDX` int(11) DEFAULT NULL COMMENT '회사 키',
  `ADMIN_IDX` int(11) DEFAULT NULL COMMENT '보낸사람(관리자 키)',
  `CHANNEL_ID` varchar(128) NOT NULL COMMENT '채널ID',
  `TEMPLATE_CODE` varchar(128) DEFAULT NULL COMMENT '템플릿 코드',
  `REQUEST_ID` varchar(256) NOT NULL COMMENT '요청ID(예약발송시 reserveId로 사용)',
  `TRANSMIT_TYPE` varchar(32) DEFAULT 'immediate' COMMENT '발송타입-즉시발송(immediate),예약발송(reservation)',
  `RESERVATION_DATE` timestamp NULL DEFAULT '0000-00-00 00:00:00' COMMENT '예약발송일시',
  `STATUS` varchar(32) DEFAULT 'init' COMMENT '발송상태(init-초기상태,[메시지발송요청조회]success-성공,processing-발송중,reserved-예약중,scheduled-스케줄중,fail-실패 [예약메시지]ready-발송 대기,processing-발송 요청중,canceled-발송 취소,fail-발송 요청 실패,done-발송 요청 성공,stale-발송 요청 실패 (시간 초과))',
  `REGIDX` int(11) NOT NULL COMMENT '등록자',
  `REGDATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
  `MODIFY_DATE` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '업데이트일시',
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `alimtalk_message` */

/*Table structure for table `alimtalk_message_recipient` */

DROP TABLE IF EXISTS `alimtalk_message_recipient`;

CREATE TABLE `alimtalk_message_recipient` (
  `IDX` int(11) NOT NULL AUTO_INCREMENT,
  `ALIMTALK_MESSAGE_IDX` int(11) DEFAULT NULL,
  `EMAIL` varchar(512) DEFAULT NULL,
  `NAME` varchar(128) DEFAULT NULL,
  `PHONE_NUMBER` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `alimtalk_message_recipient` */

/*Table structure for table `alimtalk_template` */

DROP TABLE IF EXISTS `alimtalk_template`;

CREATE TABLE `alimtalk_template` (
  `IDX` int(11) NOT NULL AUTO_INCREMENT COMMENT '키',
  `COMPANY_IDX` int(11) DEFAULT NULL COMMENT '회사 키',
  `CHANNEL_ID` varchar(128) NOT NULL COMMENT '채널ID',
  `TEMPLATE_CODE` varchar(128) DEFAULT NULL COMMENT '템플릿 코드',
  `TEMPLATE_NAME` varchar(256) DEFAULT NULL COMMENT '템플릿 이름',
  `MESSAGE_TYPE` varchar(64) NOT NULL COMMENT '메세지 유형(BA:기본형, EX:부가정보형, AD:광고 추가형, MI:복합형)',
  `EXTRA_CONTENT` text COMMENT '부가 정보 내용',
  `AD_CONTENT` text COMMENT '광고 추가 내용',
  `EMPHASIZE_TYPE` varchar(64) NOT NULL COMMENT '알림톡 강조표기 유형(NONE:기본형, TEXT:강조표기형)',
  `EMPHASIZE_TITLE` varchar(256) DEFAULT NULL COMMENT '알림톡 강조표시 제목',
  `EMPHASIZE_SUB_TITLE` varchar(256) DEFAULT NULL COMMENT '알림톡 강조표시 부제목',
  `SECURITY_FLAG` int(1) DEFAULT NULL COMMENT '보안 설정 여부(0:사용안함,1:사용)',
  `REGDATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
  `MODIFY_DATE` timestamp NULL DEFAULT '0000-00-00 00:00:00' COMMENT '수정일시',
  `STATUS` varchar(128) DEFAULT 'ACCEPT' COMMENT '상태: ACCEPT - 수락 REGISTER - 등록 INSPECT - 검수 중 COMPLETE - 완료 REJECT - 반려',
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `alimtalk_template` */

/*Table structure for table `api_key` */

DROP TABLE IF EXISTS `api_key`;

CREATE TABLE `api_key` (
  `IDX` int(11) NOT NULL AUTO_INCREMENT COMMENT '키',
  `COMPANY_IDX` int(11) DEFAULT NULL COMMENT '회사(Company) 키',
  `ADMIN_IDX` int(11) DEFAULT NULL COMMENT '등록자',
  `REGISTER_NAME` varchar(128) DEFAULT NULL COMMENT '등록자 이름',
  `KEY` varchar(256) NOT NULL COMMENT 'API KEY',
  `REGDATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
  `TYPE` int(11) DEFAULT NULL COMMENT '타입(1:일반,2:테스트)',
  `NOTE` varchar(200) DEFAULT NULL COMMENT '설명',
  `VALIDITY_START` timestamp NULL DEFAULT NULL COMMENT '유효기한 시작일자',
  `VALIDITY_END` timestamp NULL DEFAULT NULL COMMENT '유효기한 종료일자',
  `USE_ACCUMULATE` int(11) DEFAULT NULL COMMENT '테스트기간 누적데이터 지속사용여부(0:일괄삭제,1:지속사용)',
  `STATE` int(11) DEFAULT NULL COMMENT '발급상태(1:신규,2:재발급)',
  `USE_YN` char(1) DEFAULT 'Y' COMMENT '사용여부',
  `REASON` varchar(256) DEFAULT NULL COMMENT '해제사유',
  `MODIFIER_IDX` int(11) DEFAULT NULL COMMENT '수정자',
  `MODIFIER_NAME` varchar(64) DEFAULT NULL COMMENT '수정자 이름',
  `MODIFY_DATE` timestamp NULL DEFAULT NULL COMMENT '수정일자',
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `api_key` */

/*Table structure for table `aws_kms_history` */

DROP TABLE IF EXISTS `aws_kms_history`;

CREATE TABLE `aws_kms_history` (
  `IDX` int(11) NOT NULL AUTO_INCREMENT COMMENT '키',
  `REGDATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '호출 날짜',
  `TYPE` varchar(16) DEFAULT NULL COMMENT '호출 타입',
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `aws_kms_history` */

/*Table structure for table `collect_information` */

DROP TABLE IF EXISTS `collect_information`;

CREATE TABLE `collect_information` (
  `IDX` int(11) NOT NULL AUTO_INCREMENT COMMENT '주키',
  `COMPANY_IDX` int(11) DEFAULT NULL COMMENT '회사 IDX',
  `ADMIN_IDX` int(11) DEFAULT NULL COMMENT '등록자',
  `TITLE` varchar(256) DEFAULT NULL COMMENT '제목',
  `CONTENT` text COMMENT '내용',
  `REGISTER_NAME` varchar(128) DEFAULT NULL COMMENT '등록자 이름',
  `REGDATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일',
  `MODIFIER_IDX` int(11) DEFAULT NULL COMMENT '수정자',
  `MODIFIER_NAME` varchar(128) DEFAULT NULL COMMENT '수정자 이름',
  `MODIFY_DATE` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '수정자',
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `collect_information` */

/*Table structure for table `company` */

DROP TABLE IF EXISTS `company`;

CREATE TABLE `company` (
  `IDX` int(11) NOT NULL AUTO_INCREMENT COMMENT '주키',
  `SERVICE_IDX` int(11) DEFAULT NULL COMMENT '서비스',
  `COMPANY_NAME` varchar(128) DEFAULT NULL COMMENT '회사명',
  `REPRESENTATIVE` varchar(64) DEFAULT NULL COMMENT '대표자명',
  `BUSINESS_NUMBER` varchar(10) DEFAULT NULL COMMENT '사업자등록번호',
  `BUSINESS_TYPE` varchar(128) DEFAULT NULL COMMENT '업태/업종',
  `COMPANY_TEL` varchar(11) DEFAULT NULL COMMENT '사업장 전화번호',
  `COMPANY_ADDRESS_NUMBER` varchar(64) DEFAULT NULL COMMENT '우편번호(주소)',
  `COMPANY_ADDRESS` varchar(128) DEFAULT NULL COMMENT '주소',
  `COMPANY_ADDRESS_DETAIL` varchar(256) DEFAULT NULL COMMENT '상세주소',
  `SERVICE` varchar(16) DEFAULT NULL COMMENT '상품(PREMIUM, STANDARD, BASIC)',
  `PAY_DAY` int(11) DEFAULT NULL COMMENT '결제일(5일,10일 등 일자)',
  `PAY_DATE` timestamp NULL DEFAULT NULL COMMENT '결제등록일',
  `IS_AUTO_PAY` int(11) DEFAULT NULL COMMENT '자동결제(1:자동결제안함, 2:첫결제신청, 3: 해제, 4:첫결제 이후 재결제, 6:강제해제)',
  `BILLING_KEY` varchar(128) DEFAULT NULL COMMENT '카드(빌링키)와 1:1로 대응하는 값',
  `STOP_SERVICE_PRICE` int(11) DEFAULT NULL COMMENT '서비스 결제 X 강제 해지시 결제 안한 금액',
  `NOT_AUTO_PAY_DATE` timestamp NULL DEFAULT NULL COMMENT '자동결제 해지일시',
  `VALID_START` timestamp NULL DEFAULT NULL COMMENT '회원권 시작일',
  `VALID_END` timestamp NULL DEFAULT NULL COMMENT '회원권 종료일',
  `PAY_REQUEST_UID` varchar(256) DEFAULT NULL COMMENT '결제요청UID',
  `FILE_GROUP_ID` varchar(256) DEFAULT NULL COMMENT '사업자등록증사본',
  `ADMIN_IDX` int(11) DEFAULT NULL COMMENT '등록자',
  `REGDATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일',
  `MODIFIER_IDX` int(11) DEFAULT NULL COMMENT '수정자',
  `MODIFIER_NAME` varchar(128) DEFAULT NULL COMMENT '수정자 이름',
  `MODIFY_DATE` timestamp NULL DEFAULT NULL COMMENT '수정일자',
  `ENCRYPT_TEXT` varchar(256) DEFAULT NULL COMMENT '암호화한 키',
  `DATA_KEY` varchar(256) DEFAULT NULL COMMENT '복호화에 사용할 데이터 키',
  `DORMANT_ACCUMULATE` int(11) DEFAULT '0' COMMENT '누적 휴면회원 수',
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `company` */

insert  into `company`(`IDX`,`SERVICE_IDX`,`COMPANY_NAME`,`REPRESENTATIVE`,`BUSINESS_NUMBER`,`BUSINESS_TYPE`,`COMPANY_TEL`,`COMPANY_ADDRESS_NUMBER`,`COMPANY_ADDRESS`,`COMPANY_ADDRESS_DETAIL`,`SERVICE`,`PAY_DAY`,`PAY_DATE`,`CARD_NAME`,`CARD_CODE`,`CARD_NUMBER`,`EXPIRY`,`BIRTH`,`PWD_2DIGIT`,`CUSTOMER_UID`,`IS_AUTO_PAY`,`STOP_SERVICE_PRICE`,`NOT_AUTO_PAY_DATE`,`VALID_START`,`VALID_END`,`PAY_REQUEST_UID`,`FILE_GROUP_ID`,`ADMIN_IDX`,`REGDATE`,`MODIFIER_IDX`,`MODIFIER_NAME`,`MODIFY_DATE`,`ENCRYPT_TEXT`,`DATA_KEY`,`DORMANT_ACCUMULATE`) values (1,2,'(주)2월대개봉','곽호림','3488101536','서비스','01064396533','06247','서울 강남구 역삼로 165','TIPS타운 3층',NULL,5,'2022-07-14 15:11:22','BC카드',361,'919c0ae22961be714ee837082aa52958eb678d1d745f07ea9443f54d34fad007','464be25f48108ecbecfef1b41d04cc06','d6833c1de301659b9f1d2a80908c36d2','eb1827541f4b311a52301a24d8a3252c','custom_6d9ee6d6-9f6a-4cb1-9952-649b49f9f502',2,NULL,NULL,'2022-07-14 15:11:22','2022-08-05 23:59:59','4_1657779080774','FATT_1657700666371',1,'2022-07-13 17:24:26',1,'시스템관리자','2022-07-14 15:11:22','0p7IwOJKe/FOdHAXWyjamic0mOLUfH5e8ZjroNo89Cc=','AQIDAHgJGloEIm1LwL4cPWLvM58HeuMmUWjfnn29PPq5/oTojwGJbwkVrf/IeTstHr/X8EVCAAAAfjB8BgkqhkiG9w0BBwagbzBtAgEAMGgGCSqGSIb3DQEHATAeBglghkgBZQMEAS4wEQQMdESXFy+J1l9ZTL9EAgEQgDt0M0nfTla0Q8W71s2lBOhkhl3YuBMjVvAk60e+lq3JD0nwwcrc3zD+2uwgfaF7XtwXzDw3lHNOfJuu/w==',0);

/*Table structure for table `download_history` */

DROP TABLE IF EXISTS `download_history`;

CREATE TABLE `download_history` (
  `IDX` int(11) NOT NULL AUTO_INCREMENT COMMENT '키',
  `FILE_NAME` varchar(256) DEFAULT NULL COMMENT '다운로드한 파일 이름',
  `REASON` varchar(256) DEFAULT NULL COMMENT '다운로드 사유',
  `ADMIN_IDX` int(11) DEFAULT NULL COMMENT '다운로드한 사람',
  `REGIST_DATE` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '다운로드 일시',
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `download_history` */

/*Table structure for table `email` */

DROP TABLE IF EXISTS `email`;

CREATE TABLE `email` (
  `IDX` int(11) NOT NULL AUTO_INCREMENT COMMENT '키',
  `SENDER_ADMIN_IDX` int(11) NOT NULL COMMENT '보내는 관리자 키(시스템 관리자 고정)',
  `RECEIVER_TYPE` char(1) NOT NULL COMMENT '받는사람 타입(I:개별,G:그룹)',
  `RECEIVER_ADMIN_IDX_LIST` varchar(1024) DEFAULT NULL COMMENT '받는 관리자 키(문자열, 구분자: '','')',
  `EMAIL_GROUP_IDX` int(11) DEFAULT NULL COMMENT '받는 그룹 키',
  `TITLE` varchar(64) NOT NULL COMMENT '제목',
  `CONTENTS` text NOT NULL COMMENT '내용',
  `REGDATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '등록일',
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `email` */

/*Table structure for table `email_group` */

DROP TABLE IF EXISTS `email_group`;

CREATE TABLE `email_group` (
  `IDX` int(11) NOT NULL AUTO_INCREMENT COMMENT '키',
  `ADMIN_IDX_LIST` varchar(1024) NOT NULL COMMENT '관리자 키(문자열, 구분자: '','')',
  `NAME` varchar(64) NOT NULL COMMENT '그룹명',
  `DESC` varchar(2048) NOT NULL COMMENT '그룹설명',
  `REGDATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '등록일',
  `USE_YN` char(1) NOT NULL DEFAULT 'Y' COMMENT '사용여부',
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `email_group` */

/*Table structure for table `email_history` */

DROP TABLE IF EXISTS `email_history`;

CREATE TABLE `email_history` (
  `IDX` int(11) NOT NULL AUTO_INCREMENT COMMENT '키',
  `FROM` varchar(256) NOT NULL COMMENT '보내는 사람 이메일',
  `FROM_NAME` varchar(64) DEFAULT NULL COMMENT '보내는 사람 이름',
  `TO` varchar(256) NOT NULL COMMENT '받는 사람 이메일',
  `TO_NAME` varchar(64) DEFAULT NULL COMMENT '받는 사람 이름',
  `TITLE` varchar(512) NOT NULL COMMENT '제목',
  `CONTENTS` text NOT NULL COMMENT '내용',
  `REGDATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '발송일시',
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `email_history` */

/*Table structure for table `faq` */

DROP TABLE IF EXISTS `faq`;

CREATE TABLE `faq` (
  `IDX` int(11) NOT NULL AUTO_INCREMENT COMMENT '키',
  `ADMIN_IDX` int(11) DEFAULT NULL COMMENT '최종 등록한 사람의 IDX를 저장',
  `QUESTION` varchar(256) DEFAULT NULL COMMENT '질문',
  `ANSWER` text COMMENT '답변',
  `TYPE` int(11) DEFAULT '0' COMMENT '분류(0:기타,1:회원정보,2:사업자정보,3:kokonut서비스,4:결제)',
  `REGISTER_NAME` varchar(64) DEFAULT NULL COMMENT '작성정보 작성자',
  `REGDATE` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자',
  `REGIST_START_DATE` timestamp NULL DEFAULT NULL COMMENT '게시시작일자',
  `REGIST_END_DATE` timestamp NULL DEFAULT NULL COMMENT '게시종료일자',
  `MODIFIER_IDX` int(11) DEFAULT NULL COMMENT '수정자',
  `MODIFIER_NAME` varchar(64) DEFAULT NULL COMMENT '수정정보 수정자',
  `MODIFY_DATE` timestamp NULL DEFAULT NULL COMMENT '수정일자',
  `VIEW_COUNT` int(11) DEFAULT '0' COMMENT '조회수',
  `STATE` int(11) DEFAULT '1' COMMENT '0:게시중지,1:게시중,2:게시대기',
  `STOP_DATE` timestamp NULL DEFAULT NULL COMMENT '게시중지 일자',
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `faq` */

/*Table structure for table `file` */

DROP TABLE IF EXISTS `file`;

CREATE TABLE `file` (
  `IDX` int(11) NOT NULL AUTO_INCREMENT COMMENT '주키',
  `FILE_GROUP_ID` varchar(256) DEFAULT NULL COMMENT '파일그룹아이디',
  `FILE_SIZE` int(11) DEFAULT '0' COMMENT '파일사이즈',
  `REAL_FILE_NAME` varchar(256) DEFAULT NULL COMMENT '실제파일이름',
  `SAVE_FILE_NAME` varchar(256) DEFAULT NULL COMMENT '저장시파일이름',
  `FILE_PATH` varchar(512) DEFAULT NULL COMMENT '파일저장경로',
  `ADMIN_IDX` int(11) DEFAULT NULL COMMENT '등록자',
  `REGDATE` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일',
  `COMMENT` varchar(128) DEFAULT NULL COMMENT '코멘트',
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `file` */

/*Table structure for table `friendtalk_message` */

DROP TABLE IF EXISTS `friendtalk_message`;

CREATE TABLE `friendtalk_message` (
  `IDX` int(11) NOT NULL AUTO_INCREMENT COMMENT '키',
  `COMPANY_IDX` int(11) DEFAULT NULL COMMENT '회사 키',
  `ADMIN_IDX` int(11) DEFAULT NULL COMMENT '보낸사람(관리자 키)',
  `CHANNEL_ID` varchar(128) NOT NULL COMMENT '채널ID',
  `REQUEST_ID` varchar(256) NOT NULL COMMENT '요청ID(예약발송시 reserveId로 사용)',
  `TRANSMIT_TYPE` varchar(32) DEFAULT 'immediate' COMMENT '발송타입-즉시발송(immediate),예약발송(reservation)',
  `RESERVATION_DATE` timestamp NULL DEFAULT '0000-00-00 00:00:00' COMMENT '예약발송일시',
  `STATUS` varchar(32) DEFAULT 'init' COMMENT '발송상태(init-초기상태,[메시지발송요청조회]success-성공,processing-발송중,reserved-예약중,scheduled-스케줄중,fail-실패 [예약메시지]ready-발송 대기,processing-발송 요청중,canceled-발송 취소,fail-발송 요청 실패,done-발송 요청 성공,stale-발송 요청 실패 (시간 초과))',
  `REGIDX` int(11) NOT NULL COMMENT '등록자',
  `REGDATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
  `MODIFY_DATE` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '업데이트일시',
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `friendtalk_message` */

/*Table structure for table `friendtalk_message_recipient` */

DROP TABLE IF EXISTS `friendtalk_message_recipient`;

CREATE TABLE `friendtalk_message_recipient` (
  `IDX` int(11) NOT NULL AUTO_INCREMENT,
  `FRIENDTALK_MESSAGE_IDX` int(11) DEFAULT NULL,
  `EMAIL` varchar(512) DEFAULT NULL,
  `NAME` varchar(128) DEFAULT NULL,
  `PHONE_NUMBER` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `friendtalk_message_recipient` */

/*Table structure for table `kakao_channel` */

DROP TABLE IF EXISTS `kakao_channel`;

CREATE TABLE `kakao_channel` (
  `IDX` int(11) NOT NULL AUTO_INCREMENT COMMENT '키',
  `COMPANY_IDX` int(11) DEFAULT NULL COMMENT '회사 키',
  `CHANNEL_ID` varchar(128) NOT NULL COMMENT '채널 ID',
  `CHANNEL_NAME` varchar(128) DEFAULT NULL COMMENT '채널 이름',
  `STATUS` varchar(32) DEFAULT NULL COMMENT '카카오톡 채널 상태(ACTIVE - 정상, DELETED - 삭제, DELETING_PERMANENTLY - 영구 삭제 중, PERMANENTLY_DELETED - 영구 삭제, PENDING_DELETE - 삭제 지연 중, BLOCKED - 차단(반려))',
  `REGDATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `kakao_channel` */

/*Table structure for table `notice` */

DROP TABLE IF EXISTS `notice`;

CREATE TABLE `notice` (
  `IDX` int(11) NOT NULL AUTO_INCREMENT COMMENT '키',
  `ADMIN_IDX` int(11) DEFAULT NULL COMMENT '최종 등록한 사람의 IDX를 저장',
  `IS_NOTICE` int(11) DEFAULT '0' COMMENT '상단공지여부(0:일반,1:상단공지)',
  `TITLE` varchar(256) DEFAULT NULL COMMENT '제목',
  `CONTENT` text COMMENT '내용',
  `VIEW_COUNT` int(11) DEFAULT '0' COMMENT '조회수(사용여부확인필요)',
  `FILE_GROUP_ID` varchar(128) DEFAULT NULL COMMENT '첨부파일 아이디',
  `REGISTER_NAME` varchar(64) DEFAULT NULL COMMENT '작성정보 작성자',
  `REGIST_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '게시일자',
  `REGDATE` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자',
  `MODIFIER_IDX` int(11) DEFAULT NULL COMMENT '수정자',
  `MODIFIER_NAME` varchar(64) DEFAULT NULL COMMENT '수정정보 수정자',
  `MODIFY_DATE` timestamp NULL DEFAULT NULL COMMENT '수정일자',
  `STATE` int(11) DEFAULT '2' COMMENT '0:게시중지,1:게시중,2:게시대기',
  `STOP_DATE` timestamp NULL DEFAULT NULL COMMENT '게시중지 일자',
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `notice` */

/*Table structure for table `payment` */

DROP TABLE IF EXISTS `payment`;

CREATE TABLE `payment` (
  `IDX` int(11) NOT NULL AUTO_INCREMENT COMMENT '키',
  `COMPANY_IDX` int(11) DEFAULT NULL COMMENT 'COMPANY IDX',
  `ADMIN_IDX` int(11) DEFAULT NULL COMMENT '결제자 키(관리자)',
  `PAY_REQUEST_UID` varchar(256) DEFAULT NULL COMMENT '결제UID',
  `MERCHANT_UID` varchar(512) DEFAULT NULL COMMENT '상점거래ID',
  `IMP_UID` varchar(64) DEFAULT NULL COMMENT '결제번호',
  `PG_TID` varchar(512) DEFAULT NULL COMMENT '고유거래번호',
  `SERVICE` varchar(16) DEFAULT NULL COMMENT '상품(PREMIUM, STANDARD, PREMIUM)',
  `VALID_START` timestamp NULL DEFAULT NULL COMMENT '요금부과기간 시작일',
  `VALID_END` timestamp NULL DEFAULT NULL COMMENT '요금부과기간 종료일',
  `USER_COUNT` int(11) DEFAULT NULL COMMENT '기준 회원수',
  `AMOUNT` int(11) DEFAULT NULL COMMENT '결제금액',
  `STATE` int(11) DEFAULT NULL COMMENT '상태(0:결제오류,1:결제완료)',
  `CARD_NAME` varchar(256) DEFAULT NULL COMMENT '카드이름',
  `CARD_NUMBER` varchar(256) DEFAULT NULL COMMENT '카드번호',
  `PAY_METHOD` varchar(512) DEFAULT NULL COMMENT '결제방법(AUTO_CARD:자동결제, FEE_CALCULATE:요금정산, FAIL : 결제실패, CARD : 해지할 때 결제, WITHDRAWAL_CANCEL : 해지 당시에 결제할 때)',
  `RECEIPT_URL` varchar(1024) DEFAULT NULL COMMENT '거래전표 URL',
  `IS_APPLY_REFUND` char(1) DEFAULT 'N' COMMENT '환불신청상태',
  `REFUND_APPLY_DATE` timestamp NULL DEFAULT NULL COMMENT '환불신청날짜',
  `REFUND_STATE` char(1) DEFAULT 'N' COMMENT '환불상태',
  `REFUND_REASON` varchar(512) DEFAULT NULL COMMENT '환불사유',
  `REFUND_DATE` timestamp NULL DEFAULT NULL COMMENT '환불날짜',
  `REGDATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '결제일시',
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `payment` */

/*Table structure for table `personal_info_download_history` */

DROP TABLE IF EXISTS `personal_info_download_history`;

CREATE TABLE `personal_info_download_history` (
  `IDX` int(11) NOT NULL AUTO_INCREMENT COMMENT '키',
  `NUMBER` varchar(32) DEFAULT NULL COMMENT 'personal_info_provision 고유번호',
  `REGDATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일',
  `RETENTION_DATE` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '보유기간 만료일',
  `EMAIL` varchar(256) NOT NULL COMMENT '이메일',
  `FILE_NAME` varchar(128) NOT NULL COMMENT '파일명',
  `AGREE_YN` char(1) NOT NULL DEFAULT 'N' COMMENT '주의사항 동의여부 (Y/N)',
  `DESTRUCTION_FILE_GROUP_ID` varchar(256) DEFAULT NULL COMMENT '정보제공 파기 파일그룹 아이디',
  `DESTRUCTION_AGREE_YN` char(1) NOT NULL DEFAULT 'N' COMMENT '정보제공 파기 주의사항 동의여부 (Y/N)',
  `DESTRUCTION_DATE` timestamp NULL DEFAULT NULL COMMENT '정보제공 파기 최근 등록일',
  `DESTRUNCTION_REGISTER_NAME` varchar(128) DEFAULT NULL COMMENT '정보제공 파기 등록자',
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `personal_info_download_history` */

/*Table structure for table `personal_info_provision` */

DROP TABLE IF EXISTS `personal_info_provision`;

CREATE TABLE `personal_info_provision` (
  `IDX` int(11) NOT NULL AUTO_INCREMENT COMMENT '키',
  `COMPANY_IDX` int(11) NOT NULL COMMENT '기업 키',
  `ADMIN_IDX` int(11) NOT NULL COMMENT '관리자 키 (=등록자)',
  `NUMBER` varchar(32) NOT NULL COMMENT '관리번호',
  `REASON` int(11) NOT NULL COMMENT '필요사유 (1: 서비스운영, 2: 이벤트/프로모션, 3: 제휴, 4: 광고/홍보)',
  `TYPE` int(11) NOT NULL COMMENT '항목유형 (1: 회원정보 전체 항목, 2: 개인 식별 정보를 포함한 일부 항목, 3: 개인 식별 정보를 포함하지 않는 일부 항목)',
  `RECIPIENT_TYPE` int(11) NOT NULL COMMENT '받는사람 유형 (1: 내부직원, 2: 제3자, 3: 본인, 4: 위수탁)',
  `AGREE_YN` char(1) NOT NULL DEFAULT 'N' COMMENT '정보제공 동의여부 (Y/N)',
  `AGREE_TYPE` int(1) DEFAULT NULL COMMENT '정보제공 동의유형 (1: 고정필드, 2: 별도수집) (AGREE_YN 이 ''Y''인 경우에만 저장)',
  `REGDATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일',
  `PURPOSE` varchar(256) NOT NULL COMMENT '목적',
  `TAG` varchar(256) NOT NULL COMMENT '태그',
  `START_DATE` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '제공 시작일',
  `EXP_DATE` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '제공 만료일 (=제공 시작일+제공 기간)',
  `PERIOD` int(11) NOT NULL COMMENT '제공 기간 (일)',
  `RETENTION_PERIOD` varchar(16) NOT NULL COMMENT '보유기간 (사용후 즉시 삭제: IMMEDIATELY, 한달: MONTH, 일년: YEAR)',
  `COLUMNS` varchar(512) NOT NULL COMMENT '제공 항목 (컬럼 목록, 구분자: '','')',
  `RECIPIENT_EMAIL` varchar(256) NOT NULL COMMENT '받는사람 이메일',
  `TARGETS` varchar(2048) NOT NULL COMMENT '제공 대상 (키 목록, 구분자: '','')',
  `TARGET_STATUS` varchar(16) NOT NULL COMMENT '제공 대상 상태 (전체: ALL, 선택완료: SELETED)',
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `personal_info_provision` */

/*Table structure for table `personal_info_provision_agree` */

DROP TABLE IF EXISTS `personal_info_provision_agree`;

CREATE TABLE `personal_info_provision_agree` (
  `IDX` int(11) NOT NULL AUTO_INCREMENT COMMENT '키',
  `NUMBER` varchar(32) NOT NULL COMMENT 'personal_info_provision 관리번호',
  `AGREE_DATE` date DEFAULT NULL COMMENT '동의 일',
  `AGREE_TIME` time DEFAULT NULL COMMENT '동의 시간',
  `REGDATE` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '등록일',
  `ID` varchar(128) NOT NULL COMMENT '대상 아이디',
  `FILE_GROUP_ID` varchar(256) DEFAULT NULL COMMENT '파일그룹 아이디',
  `AGREE_YN` char(1) NOT NULL DEFAULT 'N' COMMENT '주의사항 동의여부 (Y/N)',
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `personal_info_provision_agree` */

/*Table structure for table `personal_info_provision_history` */

DROP TABLE IF EXISTS `personal_info_provision_history`;

CREATE TABLE `personal_info_provision_history` (
  `IDX` int(11) NOT NULL AUTO_INCREMENT COMMENT '키',
  `NUMBER` varchar(32) NOT NULL COMMENT 'personal_info_provision 고유번호',
  `REGDATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일',
  `COMPANY_IDX` int(11) NOT NULL COMMENT '회사 키',
  `ADMIN_IDX` int(11) DEFAULT NULL COMMENT '관리자 키 (=수정자)',
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `personal_info_provision_history` */

/*Table structure for table `policy` */

DROP TABLE IF EXISTS `policy`;

CREATE TABLE `policy` (
  `IDX` int(11) NOT NULL AUTO_INCREMENT COMMENT '키',
  `ADMIN_IDX` int(11) DEFAULT NULL COMMENT '정책 등록자',
  `TYPE` int(11) DEFAULT NULL COMMENT '정책(1:서비스이용약관,2:개인정보취급방침)',
  `EFFECTIVE_DATE` timestamp NULL DEFAULT '0000-00-00 00:00:00' COMMENT '시행일자',
  `HTML` text COMMENT 'HTML코드(기획상에는 파일로 되어있어 FILE_GROUP_ID로 대체될 수 있다.)',
  `REGISTER_NAME` varchar(64) DEFAULT NULL COMMENT '작성정보 작성자',
  `REGIST_DATE` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '게시일시',
  `REGDATE` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
  `MODIFIER_IDX` int(11) DEFAULT NULL COMMENT '작성정보 수정자',
  `MODIFIER_NAME` varchar(64) DEFAULT NULL COMMENT '작성정보 수정자 이름',
  `MODIFY_DATE` timestamp NULL DEFAULT '0000-00-00 00:00:00' COMMENT '작성정보 수정일시',
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `policy` */

insert  into `policy`(`IDX`,`ADMIN_IDX`,`TYPE`,`EFFECTIVE_DATE`,`HTML`,`REGISTER_NAME`,`REGIST_DATE`,`REGDATE`,`MODIFIER_IDX`,`MODIFIER_NAME`,`MODIFY_DATE`) values (1,1,1,'2022-05-03 00:00:00','<p><strong>코코넛 서비스 이용약관</strong></p><p>&nbsp;</p><p><strong>제1장 총칙</strong></p><p>&nbsp;</p><p><strong>제1조(목적)</strong>&nbsp;</p><p>이 약관은&nbsp;(주)2월대개봉(이하 “회사”)이 제공하는 코코넛 서비스(이하 “서비스”)의 이용과 관련하여,&nbsp;회사와 서비스를 이용하고자 하는 기업(이하 “고객”)&nbsp;간에&nbsp;서비스 이용에 관한 권리와 의무 및 책임,&nbsp;기타 제반 사항을 규정하는 것을 목적으로 합니다.</p><p>&nbsp;</p><p><strong>제2조(정의)</strong>&nbsp;이 계약서에서 사용하는 용어의 정의는 아래와 같습니다.</p><ol><li>회사 :&nbsp;코코넛 서비스를 제공하는 사업자를 말합니다.</li><li>고객 :&nbsp;회사와 서비스 이용계약을 체결한 자로서,&nbsp;회사가 제공하는 서비스를 이용하는 법인 또는 개인사업자 등을 말합니다.&nbsp;</li><li>이용 신청자 :&nbsp;서비스를 이용하기 위해 서비스 이용을 신청한 법인 또는 개인사업자 등을 말합니다.</li><li>아이디(ID) :&nbsp;고객의 식별과 서비스 이용을 위하여 고객에게 제공되는 문자와 숫자의 조합을 말합니다.</li><li>비밀번호(PASSWORD) :&nbsp;고객의 개인정보 및 권익 보호를 위하여 고객에게 제공되는 문자와 숫자의 조합으로 고객의 아이디(ID)와 함께 고객의 신원확인에 사용됩니다.</li><li>코코넛 서비스 :&nbsp;회사에서 제공하는 클라우드 기반의 개인정보 보호 및 관리 서비스로서 개인정보의 암호화,&nbsp;네트워크 보안 등 개인정보의 안전한 관리 기능,&nbsp;개인정보취급자의 접속기록 관리 등 개인정보 규제준수 기능을 제공하는 서비스 및 고객 정보를 기반으로 한 문자 또는 이메일 전송,&nbsp;통계정보 제공 등 개인정보 관리 및 활용에 필요한 부가 기능 등을 제공하는 서비스를 말합니다.</li><li>API(Application Programming Interface) :&nbsp;고객이 서비스를 이용할 수 있도록 지원하는 인터페이스를 뜻하며, API&nbsp;설명서를 통해 구체적인 기능을 확인할 수 있습니다.</li><li>서비스 계약기간 :&nbsp;코코넛 서비스 개통일부터 서비스가 해지되기까지의 기간을 말합니다.</li><li>고객 정보 :&nbsp;고객이 회사가 관리하는 정보통신자원에 저장하는 정보로서 고객이 소유 또는 관리하는 정보를 말합니다.</li><li>지체없이 :&nbsp;특별한 사유가 없는 한, 5&nbsp;영업일 이내를 말합니다.</li></ol><p>*&nbsp;본 조에서 규정되지 않은 용어는 관련 법령과 일반적인 용례에 따릅니다.</p><p>&nbsp;</p><p><strong>제3조(이용약관의 효력과 변경)</strong></p><ol><li>회사는 이 약관의 내용을 고객이 쉽게 확인할 수 있도록 서비스 초기 화면에 게시합니다.</li><li>회사는 ‘약관의 규제에 관한 법률’(이하 약관법), ‘클라우드컴퓨팅 발전 및 이용자 보호에 관한 법률’(이하 클라우드컴퓨팅법), ‘정보통신망 이용촉진 및 정보보호 등에 관한 법률’(이하 정보통신망법),&nbsp;개인정보보호법 등 관련 법령을 위배하지 않는 범위에서 이 약관을 개정할 수 있습니다.</li><li>회사가 약관을 개정할 경우에는 시행 일자 및 개정 사유를 명시하여 함께 서비스 초기 화면 내 공지사항에 시행 일자 7일 전부터 시행 일자 전일까지 게시하고,&nbsp;고객들에게 전자우편으로 통지합니다.</li><li>회사가 전항에 따라 공지 및 통지를 통해 공지 기간 내에 의사 표시를 하지 않으면 의사 표시가 표명된 것으로 본다는 뜻을 명확하게 공지 또는 통지하였음에도 고객이 명시적으로 의사 표시를 하지 아니한 경우 고객이 개정 약관에 동의한 것으로 간주합니다.</li><li>고객이 개정된 약관 내용에 동의하지 않는 경우 고객은 이용계약을 해지할 수 있습니다.&nbsp;계약 해지의 의사를 표시하지 아니한 경우에는 변경에 동의한 것으로 간주합니다.</li><li>고객은 약관의 변경에 대해 주의 의무를 다하여야 하며,&nbsp;변경된 약관으로 인한 고객의 피해는 회사가 책임지지 아니합니다.</li><li>이 약관의 효력은 제2조 제8항에 따른 ‘서비스 계약기간’으로 규정합니다.&nbsp;단,&nbsp;채권 또는 채무 관계가 있을 경우에는 채권,&nbsp;채무의 완료일까지 규정합니다.</li></ol><p>&nbsp;</p><p><strong>제4조(약관의 해석)</strong>&nbsp;</p><p>이 약관에서 정하지 아니한 사항과 약관의 해석에 관하여는 약관법,&nbsp;클라우드컴퓨팅법,&nbsp;개인정보보호법,&nbsp;정보통신망법, 기타 관계 법령 또는 상관례에 따릅니다.</p><p>&nbsp;</p><p><strong>제2장 이용계약의 체결</strong></p><p>&nbsp;</p><p><strong>제5조(서비스의 종류 및 변경)</strong></p><ol><li>회사는 고객에게 다음과 같은 서비스를 제공합니다.<ol><li>개인정보의 안전한 관리와 개인정보 규제 준수를 위한 개인정보보호 및 관리 서비스</li><li>고객 정보를 기반으로 한 문자 또는 이메일 전송,&nbsp;통계정보 제공 등 개인정보 관리 및 활용에 필요한 부가서비스</li><li>API&nbsp;연동에 필요한 제반 서비스</li></ol></li><li>그 외 개인정보 관련 서비스</li><li>회사는 서비스 종류,&nbsp;서비스 내용,&nbsp;가격 및 기타 서비스 관련 사항을 회사가 운영하는 웹사이트를 통하여 게시하며 고객은 해당 웹사이트를 기준으로 서비스 이용신청을 해야 합니다.</li><li>회사는 서비스 종류의 신설이나 변경은 전항에서 기술한 웹사이트를 통하여 게시하고,&nbsp;이미 고객이 이용 중인 서비스의 변경사항은 관련 서비스 초기 화면에 게시하거나 고객의 전자우편 주소를 통해서 통지하여 고객이 7일 이내에 이의를 제기하지 않으면 고객이 변경사항에 대해서 동의한 것으로 간주합니다.</li></ol><p>&nbsp;</p><p><strong>제6조(이용신청의 승낙과 제한)</strong></p><ol><li>이용계약은, 서비스의 이용을 신청하는 자(이하&nbsp;‘이용신청자’)가 서비스 이용약관의 내용에 동의하고,&nbsp;서비스 가입 신청을 한 뒤,&nbsp;회사가 이 신청에 대해 승낙함으로써 체결됩니다.&nbsp;회사는 필요한 경우에 고객에게 구비서류를 요구할 수 있습니다.</li><li>이용신청자는 이용신청 시&nbsp;담당자의 실명,&nbsp;사업자의 실제 상호 등 실제&nbsp;정보를 입력하여야 하며,&nbsp;이를 위반한 자는&nbsp;약관에서 정한 권리를 주장할 수 없고,&nbsp;회사는 이를 이유로 계약을 해제하거나 해지할 수 있습니다.</li><li>이용신청자가 이용신청 시 제공한 개인정보는 관계 법령 및 회사의 개인정보보호정책에 따라 보호됩니다.</li><li>회사는 다음 각 호의 사유가 있을 때에는 이용신청자의 이용신청을 승낙하지 않을 수 있습니다.<ol><li>제2항을 위반하여 실제와 다른 정보 또는 허위정보로 서비스 이용을 신청한 경우</li><li>회사의 다른 이용요금을 체납하는 등 이용신청자가&nbsp;회사와 체결한 계약의 중대한 내용을 위반한 사실이 있는 경우</li><li>‘정보통신망법’, ‘저작권법’, ‘개인정보보호법’&nbsp;등&nbsp;관계 법령에서 금지하는 위법행위를 할 목적으로 이용신청을 하는 경우</li><li>이용신청자가 이 계약에 의하여 이전에 고객의 자격을 상실한 사실이 있는 경우</li><li>타 고객의 서비스 이용에 지장이 있을 것으로 판단되는 경우</li><li>그 밖에 제1호에서 제5호까지에 준하는 사유로서 승낙하는 것이 상당히 부적절하다고 판단되는 경우</li></ol></li><li>회사는 다음 각 호의 어느 하나에 해당하는 경우에는 그 사유가 해소될 때까지 승낙을&nbsp;유보할 수 있습니다.<ol><li>회사의 설비에 여유가 없거나 기술적 장애가 있는 경우</li><li>서비스 장애가 있는 경우</li><li>그 밖에 제1호 또는 제2호에 준하는 사유로서 이용신청의 승낙이 곤란한 경우</li></ol></li><li>고객은 이용신청 시 기재한 사항이 변경되었을 경우 그 내용을 회사에게 지체없이 알려야 합니다.</li></ol><p>&nbsp;</p><p><strong>제7조(서비스의 개통)</strong></p><ol><li>이용계약이 성립되면 회사는 고객의 신청 내용대로 계정을 개설하고 계정 아이디 등 서비스 개통에 필요한 사항을 전자우편으로 통보합니다.</li><li>서비스 이용을 위해 회사와 고객 간의 연동 테스트 후 실 시스템에서 고객사용 API Key가 고객에게 발급된 날짜가 서비스 개통일이 됩니다.</li></ol><p>&nbsp;</p><p><strong>제3장 개인정보 처리 업무의 위탁</strong></p><p>&nbsp;</p><p><strong>제8조(위탁업무의 목적 및 범위)</strong></p><p>회사는 이 약관이 정하는 바에 따라 고객에게 서비스를 제공할 목적으로 고객으로부터 다음 개인정보 처리 업무를 위탁 받아 수행합니다.</p><ol><li>이 약관에 따른 서비스 제공</li><li>개인정보보호법,&nbsp;정보통신망법 등 관련 법령의 위반 및 코코넛 서비스의 원활한 운영에 지장을 주는 행위에 대한 제재</li><li>접속 및 이용 기록 등의 분석을 통한 보안 서비스 제공,&nbsp;코코넛 서비스 개선</li><li>기타 고객과의 협의를 통해 정한 업무</li></ol><p>&nbsp;</p><p><strong>제9조(위탁업무의 기간)</strong></p><p>위탁업무의 기간은 제2조 제8항에 따른 ‘서비스 계약기간’과 동일합니다.</p><p>&nbsp;</p><p><strong>제10조(위탁업무의 재위탁)</strong></p><ol><li>회사는 고객의 동의를 받아 다음과 같이 개인정보 처리업무를 재위탁할 수 있습니다.&nbsp;재위탁 필수 사항에 대해 고객이 동의하지 않는 경우에는 코코넛 서비스를 제공할 수 없습니다.</li></ol><figure class=\"table\"><table><tbody><tr><td><strong>재위탁 업무</strong></td><td><strong>필수/선택</strong></td><td><strong>수탁업체</strong></td><td><strong>재위탁기간</strong></td></tr><tr><td>본인 인증</td><td>필수</td><td>나이스평가정보</td><td rowspan=\"5\">코코넛 서비스 계약 또는 재위탁 계약의 만료 및 해지시까지</td></tr><tr><td>대금 결제</td><td>필수</td><td>아임포트,&nbsp;나이스페이먼츠</td></tr><tr><td>개인정보 저장</td><td>필수</td><td>AWS</td></tr><tr><td>카카오 알림톡</td><td>선택</td><td><p>카카오,</p><p>네이버 클라우드 플랫폼</p></td></tr><tr><td>메일링 서비스</td><td>선택</td><td>네이버 클라우드 플랫폼</td></tr></tbody></table></figure><ol><li>회사가 제1항 외의 다른 개인정보 처리업무를 재위탁할 경우에 회사는 해당 사실을 계약 체결 14일 이전에 고객에게 통보하고 동의를 받아야 합니다.</li></ol><p>&nbsp;</p><p><strong>제11조(개인정보의 안전성 확보조치)</strong></p><p>회사는 ‘개인정보보호법’&nbsp;법령 및 ‘개인정보의 안전성 확보조치 기준’, ‘개인정보의 기술적∙관리적 보호조치 기준’(이상 개인정보보호위원회 고시)에 따라 개인정보의 안전성 확보에 필요한 기술적∙관리적 조치를 취합니다.</p><p>&nbsp;</p><p><strong>제12조(개인정보의 처리 제한)</strong></p><p>회사는 계약 기간은 물론 계약 종료 후에도 위탁업무 수행 목적 범위를 넘어 개인정보를 이용하거나 이를 제3자에게 제공 또는 누설하지 않습니다.&nbsp;다만,&nbsp;회사는 고객 정보를 포함한 개인정보를 익명으로 처리한 후에는 계약 종료 후에도 이를 보유할 수 있으며,&nbsp;통계 작성 및 과학적 연구를 위해 필요한 범위 내에서 위 익명 정보를 이용할 수 있습니다.</p><p>&nbsp;</p><p><strong>제13조(수탁자에 대한 관리∙감독 등)</strong></p><ol><li>고객은 회사에 대하여 다음 각 호의 사항을 감독할 수 있으며,&nbsp;회사는 특별한 사유가 없는 한 이를 응하여야 합니다.<br>1.&nbsp;개인정보의 처리 현황<br>2.&nbsp;개인정보의 접근 또는 접속현황<br>3.&nbsp;개인정보 접근 또는 접속 대상자<br>4.&nbsp;목적 외 이용∙제공 및 재위탁 금지 준수여부<br>5.&nbsp;암호화 등 안전성 확보조치 이행여부<br>6.&nbsp;그 밖에 개인정보의 보호를 위하여 필요한 사항</li><li>고객은 회사에 대하여 제1항 각 호의 사항에 대한 실태 점검 결과를 요구할 수 있으며,&nbsp;회사는 특별한 사유가 없는 한 이행하여야 합니다.</li><li>고객은 처리위탁으로 인하여 정보주체의 개인정보가 분실∙도난∙유출∙변조 또는 훼손되지 아니하도록 회사를 교육할 수 있습니다.&nbsp;다만 회사가 자체 교육을 진행하고 그에 대한 증빙을 고객에게 제공하는 등의 방식으로 이를 갈음할 수 있습니다.</li></ol><p>&nbsp;</p><p><strong>제14조(정보주체의 권리보장)</strong></p><p>회사는 정보주체의 개인정보 열람,&nbsp;정정∙삭제,&nbsp;처리 정지 요청 등에 대응하기 위한 연락처 등 민원 창구를 마련해야 합니다.</p><p>&nbsp;</p><p><strong>제15조(개인정보의 파기)</strong></p><ol><li>회사는 제9조의 위탁업무 기간이 종료되면 위탁업무와 관련하여 보유하고 있는 개인정보는 고객이 다운로드할 수 있도록 일정 기간 지원한 뒤,&nbsp;이를 완전히 파기합니다.&nbsp;다만,&nbsp;제12조 단서에서 정하는 익명 정보는 예외로 합니다.</li><li>제1항에 따라 회사가 개인정보를 파기한 경우 지체없이 고객에게 그 결과를 통보합니다.</li></ol><p>&nbsp;</p><p><strong>제4장 계약 당사자의 의무</strong></p><p>&nbsp;</p><p><strong>제16조(회사의 의무)&nbsp;</strong></p><ol><li>회사는 ‘클라우드컴퓨팅법’, ‘정보통신망법’&nbsp;등 관련 법령을 준수하고,&nbsp;고객이 서비스를 원활히 이용할 수 있도록 서비스를 안정적이고,&nbsp;지속적으로 제공하여야 합니다.</li><li>회사는 안정적인 서비스 제공을 위하여 정기적인 운영 점검을 실시할 수 있고,&nbsp;이를 사전에 고객에게 통지하여야 합니다.</li><li>회사는 장애로 인하여 정상적인 서비스가 어려운 경우에 이를 신속하게 수리 및 복구하고,&nbsp;신속한 처리가 곤란한 경우에는 그 사유와 일정을 고객에게 통지하여야 합니다.&nbsp;</li><li>회사는 고객 정보를 안전하게 관리하여야 합니다.&nbsp;개인정보의 보호 및 사용에 대해서는 관련 법규 및 회사의 개인정보처리방침이 적용됩니다.&nbsp;다만,&nbsp;회사의 공식 사이트 이외의 링크된 사이트에서는 회사의 개인정보처리방침이 적용되지 않습니다.</li><li>회사는 고객이 서비스의 이용현황 및 대금 내역을 알기 쉽게 확인할 수 있도록 조치하여야 합니다.</li></ol><p>&nbsp;</p><p><strong>제17조(고객의 의무)&nbsp;</strong></p><ol><li>고객은 서비스를 이용하는 과정에서 개인정보보호법, 정보통신망법,&nbsp;저작권법 등 관련 법령을 위반하거나 선량한 풍속,&nbsp;기타 사회질서에 반하는 행위를 하지 말아야 합니다.</li><li>고객은 서비스 계약에 필요한 기업정보나 개인정보 등을 회사에 허위로 제공하여서는 안 되며,&nbsp;정보 변경 시 지체없이 회사에 통보하여 갱신하여야 합니다.</li><li>고객은 아이디 및 비밀번호 등 서비스 접속 정보에 대한 관리책임이 있으며,&nbsp;관리책임 위반으로 인해 발생하는 문제에 대한 책임은 고객에게 있습니다.&nbsp;만약 접속 정보가 도용되거나 제3자가 사용하고 있음을 인지한 경우에 이를 즉시 회사에 통지하고 회사의 안내에 따라야 합니다.</li><li>제2항 및 제3항의 경우 해당 고객이 회사에 그 사실을 통지하지 않거나 통지한 경우에도 회사의 안내에 따르지 않아 발생한 불이익에 대하여 회사는 책임지지 않습니다.</li><li>고객은 회사가 정한 서비스 이용요금을 지정된 일자에 납부하여야 합니다.</li><li>고객은 회사의 서비스 제공 목적 외의 용도로 서비스를 이용하여서는 안 됩니다.</li><li>고객은 회사와 타 고객의 서비스 운영에 방해가 되는 행위를 하지 않아야 합니다.</li><li>고객은 스팸 또는 불법 스팸 등 정보통신망법상의 광고성 정보 전송 시 의무 사항 및 이 약관을 준수하여야 하며,&nbsp;이를 위반하여 발생하는 모든 민·형사 상의 책임은 고객이 부담합니다.</li><li>고객은 서면 등을 통한 회사의 명시적 동의가 없는 한 서비스의 이용권한,&nbsp;기타 이용계약상의 지위를 타인에게 양도,&nbsp;증여할 수 없으며 이를 담보로 제공할 수 없습니다.</li><li>고객은 이 약관 제23조 제1항에 의거하여 고객의 귀책 사유로 인해 회사로부터 서비스 이용제한을 적용 받을 경우,&nbsp;해당일로부터 1개월 이내에 그 사유를 해소하여야 합니다.&nbsp;</li></ol><p>&nbsp;</p><p><strong>제5장 서비스의 이용</strong></p><p>&nbsp;</p><p><strong>제18조(서비스의 제공 및 변경)&nbsp;</strong></p><ol><li>회사는 제6조 제1항의 이용신청을 승낙한 경우에 해당 고객에 서비스 계약기간 동안 서비스를 제공합니다.&nbsp;</li><li>회사는 회사의 업무상 또는 기술상 특별한 지장이 없는 한 연중무휴, 1일 24시간 서비스 제공을 원칙으로 합니다.&nbsp;다만&nbsp;제22조 제1항의 사유로 서비스를 일시 중단할 수 있습니다.</li><li>회사는 고객이 요청하는 경우,&nbsp;고객이 수신자로부터 명시적인 사전 동의를 받는 것을 전제로 수신자에 대하여 고객의 명의로 광고성 정보를 전송할 수 있습니다.&nbsp;또한 회사는 고객이 요청하는 경우,&nbsp;고객이 정보통신망법 제50조 제3항에 근거하여 수신자로부터 별도의 사전 동의를 받는 것을 전제로 오후 9시부터 그 다음 날 오전 8시까지의 시간에 수신자에 대하여 고객의 명의로 광고성 정보를 전송할 수 있습니다.</li></ol><p>&nbsp;</p><p><strong>제19조(서비스 이용요금)&nbsp;</strong></p><ol><li>서비스 이용요금은 고객이 이용하는 개인정보 규모에 따라 다르며,&nbsp;요금의 종류,&nbsp;단가,&nbsp;과금 방식,&nbsp;할인율 등 세부 서비스 요금표는 별지에 기재하거나 웹사이트에 게시합니다.</li><li>서비스 이용요금은 매월 1일부터 말일까지 이용 시 서비스 요금표에 따른 이용요금이 산정됩니다.&nbsp;다만 해당 월의 이용기간이 1달이 되지 않은 경우에는 일할 계산하여 산정됩니다.&nbsp;다만 부가서비스 등 월 정액으로 정의되어 있지 않은 요금은 별도의 서비스 요금표에 따라 산정됩니다.</li><li>일회적 또는 일시적으로 발생하는 서비스 이용요금은 회사가 고객과 약정한 지급 시기에 그 금액을 합산하여 청구하고,&nbsp;고객은 이의가 없으면 이를 지급하여야 합니다.&nbsp;</li><li>서비스 이용요금의 감면 또는 할인은 회사와 고객이 협의하여 그 조건,&nbsp;방법 및 내용을 정할 수 있습니다.</li></ol><p>&nbsp;</p><p><strong>제20조(이용요금의 청구와 납부)</strong></p><ol><li>회사는 고객이 서비스를 이용한 달의 다음 달 5영업일 이내 지급청구서를 발송하거나 웹사이트에 게시하여야 합니다.</li><li>고객은 지급청구서의 내용에 이의가 없으면 청구서에 기재된 납부기일까지 청구된 요금을 지급하여야 합니다.&nbsp;다만 고객의 책임 없는 사유로 서비스를 이용하지 못한 경우에는 그 기간 동안의 이용요금 지급의무를 면합니다.<ul><li>통상적으로 납부기일은 서비스를 이용한 달의 다음 달 25일입니다.</li></ul></li><li>고객은 청구된 이용요금에 이의가 있으면 청구서가 도달한 날로부터&nbsp;14일&nbsp;이내에 이의를 신청할 수 있고,&nbsp;회사는 이의신청을 접수한 날로부터&nbsp;14일 이내에 그 처리결과를 고객에게 통지하여야 합니다.&nbsp;다만,&nbsp;부득이한 사유로 상당한 기간 내에 그 결과를 알릴 수 없는 경우에는 그 사유 및 처리기한을 다시 정하여 고객에게 알려야 합니다.</li></ol><p>&nbsp;</p><p><strong>제21조(이용요금의 정산∙반환 및 체납 시 처리)&nbsp;</strong></p><ol><li>회사는 고객이 이용요금을 과·오납한 때에는 이를 반환하거나 다음 달 이용금액에서 정산합니다.</li><li>고객이 서비스의 중대한 오류로 인하여 서비스 이용계약의 목적을 달성할 수 없는 때에는 이미 요금이 납부된 서비스라도 회사에 이용요금의 반환을 청구할 수 있습니다.&nbsp;</li><li>고객이 이용요금을 체납한 때에는 회사는 서비스 제공을 중단하거나 납부기일 다음 날부터 매일 체납금액의&nbsp;100분의&nbsp;2의 범위 내에서 가산금을 징수할 수 있습니다.&nbsp;</li></ol><p>&nbsp;</p><p><strong>제22조(서비스 제공의 중단)&nbsp;</strong></p><ol><li>회사는 다음 각 호의 어느 하나에 해당하는 경우에 서비스 제공을 중단할 수 있으며,&nbsp;그 사유가 해소되면 지체없이 서비스 제공을 재개하여야 합니다.<ol><li>서비스의 점검·개선,&nbsp;설비의 증설·보수·점검,&nbsp;시설의 관리 및 운용 등의 사유로 부득이하게 서비스를 제공할 수 없는 경우</li><li>해킹 등 전자적 침해사고나 통신사고 등 예상하지 못한 서비스의 불안전성에 대응하기 위하여 필요한 경우</li><li>천재지변,&nbsp;국가비상사태,&nbsp;기간통신사업자 또는&nbsp;IaaS(Infrastructure as a Service)&nbsp;클라우드 사업자가 제공하는 인프라의 불안정 또는 중단 등 회사가 통제할 수 없는 요인으로 인하여 정상적인 서비스 제공이 불가능한 경우</li><li>기타 제1호~제3호와 유사한 이유로 부득이하게 서비스를 제공하지 못하는 경우</li></ol></li><li>회사는 제1항 제1호의 경우에는 서비스 제공을&nbsp;중단하기 5영업일 전까지 그 사실을 고객에게 통지하여야 하며, 회사가 운영하는 홈페이지를 통하여 게시하는 것으로 대신할 수 있습니다.&nbsp;다만,&nbsp;제2호 및 제3호의 경우에는 사전 통지 없이 서비스를 중단할 수 있으나,&nbsp;중단 후에는 지체없이 그 사실을 고객에게 통지하여야 합니다.</li><li>회사는 회사가 통제할 수 없는 사유로 인한 서비스 중단의 경우(시스템관리자의 고의,&nbsp;과실이 없는 디스크 장애,&nbsp;시스템 다운 등)에 사전통지가 불가능하며 서비스 유관업체(기간 통신 사업자,&nbsp;이동통신사업자 등)의 고의,&nbsp;과실로 인한 시스템 중지 등의 경우에는 별도의 통지를 하지 않습니다.</li><li>회사는 서비스의 전부 및 일부를 휴지 또는 폐지하고자 할 때에는 그 휴지 또는 폐지 예정일의 30일전까지 그 내용을 고객에게 전자우편,&nbsp;웹사이트 등을 이용하여 공지합니다.</li></ol><p>&nbsp;</p><p><strong>제6장 서비스의 이용제한 및 계약의 해제</strong>·<strong>해지</strong></p><p>&nbsp;</p><p><strong>제23조(서비스의 이용제한)&nbsp;</strong></p><ol><li>회사는 고객의 서비스 이용 내용이 다음 각 호의 하나에 해당할 경우 서비스 이용을 일시적으로 제한할 수 있습니다.<ol><li>서비스 이용의 폭주 등 불가항력으로 인하여 서비스 이용에 지장이 있는 경우</li><li>회사가 제공하는 서비스를 직접 혹은 간접으로 이용하는 시스템 운영의 심각한 장애나 보안 위험을 초래한 경우</li><li>비밀번호 관리 소홀 등 고객의 귀책사유로 고객 정보 보호에 문제가 발생할 수 있어 이에 대해 신속한 대응이 필요한 경우</li><li>고객이 이용요금,&nbsp;가산금,&nbsp;회사와의 계약에 따른 다른 서비스의 비용,&nbsp;그 밖의 채무(계약에 의해 지급이 필요하게 된 것)를 납기에 납부하지 않은 경우</li><li>고객이 서비스 또는 회사와 체결한 다른 서비스를 이용할 때 제17조 제1항 내지 제9항을 위반한 경우</li><li>서비스에서 제공하는 정보를 업무상의 목적이 아닌 다른 목적으로 사용한 경우</li><li>고객이&nbsp;제3자에게 서비스를 임의로 제공하는 경우</li><li>서비스를 이용하여 얻은 정보를 회사의 사전 승낙 없이 복제 또는 유통시키거나 상업적으로 이용하는 경우</li><li>다른 고객이 보유한 정보의 유출,&nbsp;비밀번호 도용 등의 부정한 행위를 한 경우</li><li>서비스를 범죄에 이용한다고 객관적으로 판단되는 경우</li><li>이 약관을 포함하여 기타 회사가 정한 제반 규정, 관련 법령 또는 이용 조건을 위반하는 행위를 하는 경우</li></ol></li><li>회사가 제1항 각 호에 따라 서비스를 제한한 경우에는 특별한 사유가 없으면 고객이 그 기간 동안의 이용요금을 납부하여야 합니다.</li><li>회사는 서비스 이용 제한을 할 경우에는 그 사유 및 기간 등을 통지한 후에 서비스 이용을 제한하여야 합니다.&nbsp;다만,&nbsp;시스템 운영에 중대한 장애가 초래되거나 사안이 긴급한 경우에는 먼저 서비스를 정지한 후 이를 사후에 통지할 수 있습니다.</li><li>회사는 서비스 이용 제한된 고객이 서비스 이용 제한일부터 1개월 이내에 그 사유를 해소할 경우에는 소정의 절차에 따라 서비스 이용 제한을 지체없이 해제합니다.</li></ol><p>&nbsp;</p><p><strong>제24조(고객의 계약 해제∙해지 및 탈퇴절차)&nbsp;</strong></p><ol><li>고객은 다음 각 호의 어느 하나에 해당하는 사유를 인지한 날 이후 14일 이내에 제3항 소정의 탈퇴 절차를 통해 사유를 통지하고,&nbsp;이 계약을 해제할 수 있습니다.<ol><li>이 계약에서 약정한 서비스가 처음부터 제공되지 않는 경우</li><li>제공되는 서비스가 계약 내용과 현저한 차이가 있는 경우</li><li>서비스의 결함으로 정상적인 이용이 불가능한 경우</li></ol></li><li>고객은 다음 각 호의 어느 하나에 해당하는 경우,&nbsp;제3항 소정의 탈퇴 절차를 통해 사유를 통지하고,&nbsp;이 계약을 해지할 수 있습니다.<ol><li>회사가 서비스 제공 중에 파산 등의 사유로 계약상의 의무를 이행할 수 없거나 그 의무의 이행이 현저히 곤란하게 된 경우</li><li>회사가 계약에 따른 서비스제공을 다하지 않는 경우</li><li>고객이 서비스 사용을 종료하는 경우</li></ol></li><li>고객의 탈퇴 절차는 다음과 같은 순서로 이루어집니다.<ol><li>고객이 제1항 각호 또는 제2항 각호에 해당하는 사유를 회사에 통지</li><li>회사는 고객이 통지한 사유를 지체없이 확인 및 검토</li><li>회사는 고객이 통지한 사유가 타당하다고 판단하면 탈퇴 승인</li><li>회사는 고객에게 자신의 고객 정보를 다운로드할 수 있는 기간(3일)&nbsp;부여</li><li>회사는 제5장의 조항 등에 근거하여 고객과 이용요금 정산</li></ol></li></ol><p>&nbsp;</p><p><strong>제25조(회사의 계약 해제 및 해지</strong>)</p><ol><li>회사는 다음 각 호의 어느 하나에 해당하는 경우에 계약을 해제할 수 있습니다.<ol><li>회사가 서비스를 개시하여도 고객이 계약의 목적을 달성할 수 없는 경우</li><li>계약체결 후 서비스가 제공되기 전에 고객이 파산 등의 사유로 계약상의 의무를 이행할 수 없거나 그 의무의 이행이 현저히 곤란하게 된 경우</li></ol></li><li>회사는 다음 각 호의 어느 하나에 해당하는 경우에 계약을 해지할 수 있습니다.<ol><li>고객이 회사의 동의 없이 계약상의 권리 및 의무를 제3자에게 처분한 경우</li><li>고객이 이용요금,&nbsp;가산금,&nbsp;회사와의 계약에 따른 다른 서비스의 비용,&nbsp;그 밖의 채무(계약에 의해 지급이 필요하게 된 것)를 납기에 납부하지 않은 경우</li><li>회사가 사업의 종료에 따라 서비스를 종료하는 경우</li></ol></li><li>회사가 제2항에 따라 계약을 해지하고자 하는 때에는 고객에게 30일 전까지 그 사유를 통지하여야 합니다.&nbsp;다만,&nbsp;고객의 책임 있는 사유로 통지를 할 수 없는 때에는 사전통지와 이의신청의 기회제공을 면합니다.</li><li>회사는 고객이 다음 각호에 해당할 경우에 사전 통지 없이 이용계약을 해지할 수 있습니다.&nbsp;회사는 해지 후 그 사실을 고객에게 지체없이 통지하여야 합니다.<ol><li>서비스 이용 신청 시 기재한 담당자 정보 또는 기업정보가 허위로 판명된 경우</li><li>서비스 운영을 고의로 방해하여 회사에 손해를 입힌 경우</li><li>이 약관 제23조에 의거하여 이용 제한된 후 1개월 이내에 이용제한 사유를 해소하지 않는 경우</li><li>당해 연도에 2회 이상 이용제한을 당한 경우</li><li>고객 정보를 고의 또는 과실로 유출한 경우</li><li>회사의 서비스 제공 목적 외의 용도로 서비스를 이용하거나,&nbsp;제3자에게 임의로 해당 서비스를 제공한 경우</li><li>객관적으로 범죄에 이용된다고 판단되는 경우</li></ol></li><li>회사가 계약을 해지하는 경우에는 고객에게 해지 사유,&nbsp;해지일을 통지합니다.</li><li>회사는 고객의 귀책사유로 계약이 해지된 고객이 다시 이용신청을 하는 경우 이용 승낙을 거부할 수 있습니다.</li></ol><p>&nbsp;</p><p><strong>제7장 고객 정보의 보호</strong></p><p>&nbsp;</p><p><strong>제26조(고객 정보의 보호와 관리)</strong>&nbsp;</p><p>회사는 개인정보보호법,&nbsp;정보통신망법 등 관련 법령이 정하는 바에 따라 고객 정보를 보호합니다.</p><p>&nbsp;</p><p><strong>제27조(고객 정보의 처리)&nbsp;</strong></p><p>회사는 계약이 해제·해지되거나 종료되면 고객 정보를 고객에게 반환하여야 하고,&nbsp;그 반환이 사실상 불가능한 경우에는 고객 정보를 파기하여야 합니다.&nbsp;다만,&nbsp;제12조 단서에서 정하는 익명 정보는 예외로 합니다.</p><p>&nbsp;</p><p>&nbsp;</p><p><strong>제8장 손해배상 등</strong></p><p>&nbsp;</p><p><strong>제28조(손해배상)</strong>&nbsp;</p><ol><li>회사의 고의 또는 과실로 고객이 서비스를 이용하지 못하는 경우에 회사와 고객은 고객의 최종 납입 이용요금 범위 내에서 상호 협의한 후 배상할 수 있습니다.</li><li>회사는 개인정보보호법령이 정하고 있는 손해배상책임의 이행을 위하여 개인정보보호 손해배상 책임보험에 가입할 수 있습니다.&nbsp;회사는 원칙적으로 해당 책임보험을 통하여 고객의 손해를 배상하며,&nbsp;그 외 고객의 특별한 사정으로 인한 손해는 회사가 그 사정을 알았거나 알 수 있었을 때에 한하여 배상의 책임이 있습니다.</li></ol><p>&nbsp;</p><p><strong>제29조(손해배상의 청구)</strong></p><ol><li>고객의 손해배상의 청구는 회사에 청구사유,&nbsp;청구금액 및 산출근거를 기재하여 전자우편 및 우편을 통해 서면으로 신청할 수 있으며,&nbsp;그 사유가 발생한 날로부터 3개월 이내에 신청한 경우에만 유효합니다.</li><li>고객이 제17조에서 규정한 의무사항을 위반하여 회사에 손해를 끼친 경우에 회사는 해당 고객에 대해 손해배상을 청구할 수 있습니다.</li></ol><p>&nbsp;</p><p><strong>제30조(면책)</strong>&nbsp;</p><ol><li>회사는 다음 각 호의 사유로 인하여 발생한 손해에 대하여는 책임을 지지 않습니다.<ol><li>제23조&nbsp;제1항 각 호의 사유로 서비스 점검이 불가피하여 같은 조&nbsp;제3항에서 정한 절차에 따라 사전에 알린 경우로서 회사에게 고의 또는 과실이 없는 경우</li><li>천재지변,&nbsp;전쟁·내란·폭동 등&nbsp;비상사태,&nbsp;현재의 기술수준으로는 해결이 불가능한 기술적 결함 그 밖에&nbsp;불가항력에 의하여 서비스를 제공할 수 없는 경우</li><li>IaaS(Infrastructure as a Service)&nbsp;클라우드 사업자의 인프라 제공 중단 등으로 인하여&nbsp;정상적인 서비스 제공이 불가능한 경우</li><li>전기통신사업자 또는 IaaS&nbsp;클라우드 사업자가 서비스를 중지하거나 정상적으로 제공하지 아니하여 고객에게 손해가 발생한데 대하여 회사에게 고의 또는 과실이 없는 경우</li><li>고객의 고의 또는 과실 등 고객의 귀책사유로 인한 서비스의 중단,&nbsp;장애 및 계약 해지의 경우</li><li>고객의 컴퓨터 오류 또는 전자우편&nbsp;주소의&nbsp;부정확한 기재 등으로 고객에게 발생한 손해에 대하여&nbsp;회사에게 고의 또는 과실이&nbsp;없는 경우</li></ol></li><li>회사는 고객이 회사의 서비스 제공으로부터 기대되는 이익을 얻지 못하였거나 서비스로부터 수반되는 잠재가치 및 서비스 자료에 대한 취사선택 또는 이용으로 발생하는 손해 등에 대해서는 책임을 지지 않습니다.</li><li>회사는 고객 상호간 또는 고객과 제3자 상호간에 지적재산권 침해로 발생한 분쟁으로 인한 손해에 대하여 개입할 의무가 없으며,&nbsp;배상책임 또한 지지 않습니다.</li></ol><p>&nbsp;</p><p><strong>제31조(고객에 대한 통지)</strong>&nbsp;</p><ol><li>회사는 다음 각 호의 어느 하나에 해당하는 사유가 발생한 경우에는 고객이 미리 지정한 전화,&nbsp;문자메시지,&nbsp;우편(전자우편 포함)의 발신,&nbsp;서비스 화면 게시 또는 이와 유사한 방법으로 고객에게 알려야 합니다.<ol><li>정보통신망법 제2조 제7호에 따른 침해사고가 발생한 때</li><li>고객 정보가 유출된 때</li><li>사전예고 없이 클라우드컴퓨팅법 시행령 제16조에서 정한 기간 이상 서비스 중단이 발생한 때</li><li>서비스의 종료</li><li>그 밖에 고객의 서비스 이용에 중대한 영향을 미치는 사항</li></ol></li><li>회사는 제1항 제1호에서 제3호까지의 사유가 발생한 경우에 지체없이 다음 각 호의 사항을 해당 고객에게 알려야 합니다.&nbsp;다만,&nbsp;파악하지 못한 사항이 있는&nbsp;경우에는 나머지 사항을 먼저 알리고,&nbsp;확인되면 이를 지체없이 해당 고객에게 알려야 합니다.<ol><li>발생 내용</li><li>발생 원인</li><li>회사의 피해 확산 방지 조치 현황</li><li>고객의 피해예방 또는 확산방지방법</li><li>담당부서 및 연락처</li></ol></li></ol><p>&nbsp;</p><p><strong>제32조(고객의 법적 지위 승계)</strong></p><p>상속,&nbsp;합병,&nbsp;분할,&nbsp;영업양수 등으로 고객 법적 지위의 승계 사유가 발생한 때에는 그 사유가 발생한 날로부터 30일 이내에 사업자등록증사본(개인은 회사가 지정한 본인 인증 절차)과 지위승계를 입증할 수 있는 관련서류를 첨부하여 회사가 지정한 절차에 따라 신청하여야 합니다.</p><p>&nbsp;</p><p><strong>제33조(양도 등의 제한)</strong></p><p>고객이 서비스를 제공받는 권리는 제32조의 규정에 의하여 승계하는 경우를 제외하고는 이를 양도하거나 증여 등을 할 수 없으며 또한 담보로 제공할 수 없습니다.</p><p>&nbsp;</p><p><strong>제34조(준거법 및&nbsp;관할법원)&nbsp;</strong></p><ol><li>이 계약의 성립,&nbsp;효력,&nbsp;해석 및 이행과 관련하여서는 대한민국법이 적용됩니다.</li><li>회사와 고객&nbsp;간에 발생한 분쟁으로 소송이 제기되는 경우에는 민사소송법이 정한 법원을 관할법원으로 합니다.</li><li>당사자 일방이 외국사업자인 경우에는 대한민국 법원이 국제재판관할권을 가집니다.</li></ol><p>&nbsp;</p><p>(시행일)&nbsp;이 약관은 2022년 6월 1일부터 시행합니다.</p><p>&nbsp;</p>','시스템관리자','2022-05-03 09:00:00','2022-05-03 15:33:13',1,'시스템관리자','2022-05-04 13:10:39'),(2,1,2,'2022-05-04 00:00:00','<p><strong>코코넛 서비스 개인정보처리방침</strong></p><p>&nbsp;</p><p>(주)2월대개봉(이하회사)은&nbsp;코코넛서비스(이하&nbsp;서비스)를&nbsp;이용하는&nbsp;고객의개인정보를&nbsp;중시하며, 개인정보보호법&nbsp;등&nbsp;관련법규를&nbsp;준수하기&nbsp;위해노력하고&nbsp;있습니다.</p><p>&nbsp;</p><p>회사는 “개인정보&nbsp;처리방침 -&nbsp;코코넛&nbsp;서비스(고객사관리자용)”(이하&nbsp;개인정보처리방침)을&nbsp;통해고객사&nbsp;관리자&nbsp;또는고객사로부터&nbsp;승인받아&nbsp;고객사의개인&nbsp;회원의&nbsp;개인정보를제공받는&nbsp;다른&nbsp;기업의개인정보&nbsp;담당자(이하이용자)의&nbsp;개인정보를어떠한&nbsp;용도와&nbsp;방식으로이용하고&nbsp;있으며, 개인정보보호를&nbsp;위해&nbsp;어떠한조치를&nbsp;취하고&nbsp;있는지알려드립니다.</p><p>&nbsp;</p><p>‘개인정보’란&nbsp;생존하는&nbsp;개인에관한&nbsp;정보로서&nbsp;개인을식별할&nbsp;수&nbsp;있는정보(다른&nbsp;정보와용이하게&nbsp;결합하여&nbsp;식별할수&nbsp;있는&nbsp;것을포함)를&nbsp;말합니다.</p><p>&nbsp;</p><p>회사는개인정보&nbsp;처리방침의&nbsp;지속적인관리를&nbsp;위하여&nbsp;개인정보처리방침을&nbsp;개정하는&nbsp;데필요한&nbsp;절차를&nbsp;정하고있습니다.&nbsp;그리고&nbsp;개인정보처리방침을&nbsp;개정하는&nbsp;경우버전번호를&nbsp;부여하여&nbsp;개정된사항을&nbsp;쉽게&nbsp;알아볼수&nbsp;있도록&nbsp;하고있습니다.</p><h3><br><strong>1.&nbsp;개인정보의 처리 목적</strong></h3><p>회사는개인정보를&nbsp;다음&nbsp;목적을위해&nbsp;처리합니다.</p><ul><li>코코넛&nbsp;서비스&nbsp;제공:&nbsp;가입&nbsp;의사&nbsp;확인,&nbsp;본인&nbsp;인증,&nbsp;회원&nbsp;관리,&nbsp;코코넛&nbsp;서비스&nbsp;제공,&nbsp;코코넛&nbsp;서비스와&nbsp;관련된&nbsp;각종&nbsp;고지&nbsp;및&nbsp;통지</li><li>코코넛&nbsp;서비스&nbsp;운영:&nbsp;관련&nbsp;법령&nbsp;및&nbsp;코코넛&nbsp;서비스&nbsp;이용&nbsp;약관의&nbsp;위반,&nbsp;코코넛&nbsp;서비스의&nbsp;원활한&nbsp;운영에&nbsp;지장을&nbsp;주는&nbsp;행위에&nbsp;대한&nbsp;제재</li><li>보안&nbsp;및&nbsp;개선: &nbsp;접속&nbsp;및&nbsp;이용&nbsp;기록&nbsp;분석을&nbsp;통한&nbsp;보안&nbsp;서비스&nbsp;제공,&nbsp;코코넛&nbsp;서비스&nbsp;기능&nbsp;개선</li></ul><h3><br><strong>2.&nbsp;수집하는&nbsp;개인정보의&nbsp;항목&nbsp;및&nbsp;수집&nbsp;방법,&nbsp;보유&nbsp;기간</strong></h3><p><strong>1.&nbsp;서비스&nbsp;가입&nbsp;시&nbsp;수집하는&nbsp;개인정보</strong></p><figure class=\"table\"><table><thead><tr><th><strong>처리&nbsp;목적</strong></th><th><strong>수집&nbsp;항목</strong></th><th><strong>수집&nbsp;방법</strong></th><th><strong>처리&nbsp;및&nbsp;보유&nbsp;기간</strong></th></tr></thead><tbody><tr><td>서비스&nbsp;제공&nbsp;및&nbsp;운영</td><td>이메일&nbsp;주소, &nbsp;이름,&nbsp;휴대전화&nbsp;번호, &nbsp;비밀번호</td><td>이용자의&nbsp;입력</td><td>탈퇴&nbsp;또는&nbsp;계약&nbsp;종료&nbsp;시까지</td></tr></tbody></table></figure><p>&nbsp;</p><p><strong>2.&nbsp;서비스&nbsp;이용&nbsp;중&nbsp;생성,&nbsp;수집하는&nbsp;정보</strong></p><figure class=\"table\"><table><thead><tr><th><strong>처리&nbsp;목적</strong></th><th><strong>수집&nbsp;항목</strong></th><th><strong>수집&nbsp;방법</strong></th><th><strong>처리&nbsp;및&nbsp;보유&nbsp;기간</strong></th></tr></thead><tbody><tr><td>서비스&nbsp;제공&nbsp;및&nbsp;운영, &nbsp;법규&nbsp;준수</td><td>IP&nbsp;주소,&nbsp;기기&nbsp;정보, &nbsp;서비스&nbsp;접속&nbsp;및&nbsp;이용&nbsp;기록</td><td>서비스&nbsp;이용&nbsp;시&nbsp;생성,&nbsp; &nbsp;수집</td><td>탈퇴&nbsp;또는&nbsp;계약&nbsp;종료&nbsp;시까지</td></tr></tbody></table></figure><p>&nbsp;</p><ul><li>기기&nbsp;정보를&nbsp;수집하는&nbsp;경우에는&nbsp;일방향&nbsp;암호화(Hash)를&nbsp;통해&nbsp;기기를&nbsp;식별할&nbsp;수&nbsp;없는&nbsp;방법으로&nbsp;변환하여&nbsp;보관합니다.</li></ul><p>&nbsp;</p><p><strong>3.&nbsp;법령에&nbsp;따른&nbsp;개인정보의&nbsp;보유기간</strong></p><figure class=\"table\"><table><thead><tr><th><strong>수집&nbsp;항목</strong></th><th><strong>근거법</strong></th><th><strong>보유&nbsp;기간</strong></th></tr></thead><tbody><tr><td>인터넷&nbsp;접속&nbsp;로그</td><td>통신비밀보호법&nbsp;제15조의2,&nbsp;시행령&nbsp;제41조</td><td>3개월</td></tr><tr><td>개인정보취급자의&nbsp;서비스&nbsp;접속&nbsp;및&nbsp;이용&nbsp;기록</td><td>개인정보보호법&nbsp;제29조,&nbsp;개인정보의&nbsp;안전성&nbsp;확보조치&nbsp;기준&nbsp;고시&nbsp;제8조</td><td>2년</td></tr></tbody></table></figure><p>&nbsp;</p><p><strong>4.&nbsp;개인정보를&nbsp;자동으로&nbsp;수집하는&nbsp;장치의&nbsp;설치운영&nbsp;및&nbsp;그&nbsp;거부에&nbsp;관한&nbsp;사항</strong></p><p>①&nbsp;회사는&nbsp;이용자에게개별적인&nbsp;맞춤서비스를&nbsp;제공하기위해&nbsp;이용&nbsp;정보를저장하고&nbsp;수시로&nbsp;불러오는 ‘쿠키(cookie)’를사용할&nbsp;수&nbsp;있습니다.&nbsp;<br>②&nbsp;쿠키는&nbsp;웹사이트를운영하는데&nbsp;이용되는&nbsp;웹서버가이용자의&nbsp;웹브라우저에게&nbsp;보내는소량의&nbsp;정보이며&nbsp;웹브라우저가실행되는&nbsp;이용자&nbsp;기기의저장장치에&nbsp;저장되기도&nbsp;합니다.</p><ul><li>쿠키의 사용목적:&nbsp;이용자가 방문한 각 서비스와 웹 사이트들에 대한 방문 및 이용형태,&nbsp;인기 검색어,&nbsp;보안접속 여부 등을 파악하여 이용자에게 최적화된 정보 제공 또는 서비스 개선을 위해 사용됩니다.</li></ul><p>③&nbsp;이용자는&nbsp;다음과같은&nbsp;방법으로&nbsp;쿠키사용을&nbsp;거부할&nbsp;수있습니다.</p><ul><li>크롬(Chrome): &nbsp;상단 맨 오른쪽의 더보기 &gt;&nbsp;설정 &gt; &nbsp;개인정보 및 보안 &gt;&nbsp;쿠키 및 기타 사이트 데이터 &gt;&nbsp;모든 사이트 데이터 및 권한 보기에서 \'쿠키를 사용할 수 없는 사이트\'&nbsp;오른쪽의 \'추가\'를 클릭하고, \"[*.]<a href=\"http://abc.com/\">abc.com</a><i>\"을 입력합니다.</i>[*.]google.com은&nbsp;<a href=\"http://drive.google.com/\">drive.google.com</a>, &nbsp;<a href=\"http://calendar.google.com/\">calendar.google.com</a>, abc.google.com을 모두 포함합니다.&nbsp;상세한 사항은 &nbsp;<a href=\"https://support.google.com/chrome/answer/95647?hl=ko&amp;co=GENIE.Platform%3DDesktop#zippy=%2C%EC%82%AC%EC%9D%B4%ED%8A%B8%EC%97%90%EC%84%9C-%EC%BF%A0%ED%82%A4-%EC%82%AD%EC%A0%9C%ED%95%98%EA%B8%B0%2C%EC%BF%A0%ED%82%A4-%ED%97%88%EC%9A%A9-%EB%98%90%EB%8A%94-%EC%B0%A8%EB%8B%A8%ED%95%98%EA%B8%B0%2C%ED%8A%B9%EC%A0%95-%EC%82%AC%EC%9D%B4%ED%8A%B8\">구글의&nbsp;쿠키&nbsp;차단&nbsp;도움말</a>을 참고하세요.</li><li>인터넷 익스플로러(Internet Explorer):&nbsp;상단에서 도구 &gt;&nbsp;인터넷 옵션 &gt;&nbsp;개인정보 &gt;&nbsp;고급에서 ‘현재 사이트의 쿠키’&nbsp;아래 ‘차단’을 선택합니다.</li><li>마이크로소프트 엣지(Edge): &nbsp;상단 맨 오른쪽의 더보기 &gt;&nbsp;설정 &gt; &nbsp;쿠키 및 사이트 권한 &gt;&nbsp;쿠키 및 저장된 데이터에서 ‘차단’&nbsp;오른쪽의 ‘추가’를 클릭하고, \"[*.]<a href=\"http://abc.com/\">abc.com</a><i>\"을 입력합니다.&nbsp;</i>[*.]google.com은&nbsp;<a href=\"http://drive.google.com/\">drive.google.com</a>, &nbsp;<a href=\"http://calendar.google.com/\">calendar.google.com</a>, abc.google.com을 모두 포함합니다.</li></ul><h3><br><strong>3.&nbsp;개인정보의&nbsp;파기에&nbsp;관한사항</strong></h3><p>회사는개인정보&nbsp;보유기간의&nbsp;경과,&nbsp;처리목적&nbsp;달성&nbsp;등개인정보가&nbsp;불필요하게&nbsp;되었을때에는&nbsp;지체없이&nbsp;해당개인정보를&nbsp;파기합니다.</p><p>&nbsp;</p><p>개인정보보유기간이&nbsp;경과하거나&nbsp;처리목적이달성되었음에도&nbsp;불구하고&nbsp;다른법령에&nbsp;따라&nbsp;개인정보를계속&nbsp;보존하여야&nbsp;하는경우에는,&nbsp;해당&nbsp;개인정보를별도의&nbsp;데이터베이스(DB)로옮기거나&nbsp;보관장소를&nbsp;달리하여보존합니다.&nbsp;이때, DB로&nbsp;옮겨진&nbsp;개인정보는해당&nbsp;법령에서&nbsp;허용된목적&nbsp;이외의&nbsp;다른목적으로&nbsp;이용되지&nbsp;않습니다.</p><p>&nbsp;</p><p>회사의개인정보&nbsp;파기절차&nbsp;및방법은&nbsp;다음과&nbsp;같습니다.</p><p>&nbsp;</p><p><strong>1.&nbsp;파기&nbsp;절차</strong>&nbsp;<br>회사는&nbsp;탈퇴&nbsp;또는계약&nbsp;종로의&nbsp;사유로보유기간이&nbsp;경과한&nbsp;개인정보는자동으로&nbsp;지체없이&nbsp;해당개인정보를&nbsp;파기합니다. 개인정보보호책임자는&nbsp;정기적으로&nbsp;파기된개인정보&nbsp;현황을&nbsp;확인합니다.</p><p>&nbsp;</p><p><strong>2.&nbsp;파기&nbsp;방법</strong>&nbsp;<br>회사는&nbsp;전자적&nbsp;파일형태로&nbsp;기록·저장된개인정보는&nbsp;기록을&nbsp;재생할수&nbsp;없도록&nbsp;파기하며,&nbsp;종이&nbsp;문서에&nbsp;기록·저장된&nbsp;개인정보는&nbsp;분쇄기로분쇄하거나&nbsp;소각하여&nbsp;파기합니다.</p><p>&nbsp;</p><h3><br><strong>4.&nbsp;미이용자의&nbsp;개인정보&nbsp;파기&nbsp;등에&nbsp;관한&nbsp;조치</strong></h3><p>①&nbsp;회사는 1년&nbsp;동안&nbsp;서비스를이용하지&nbsp;않은&nbsp;이용자의정보를&nbsp;파기합니다. 다만,&nbsp;이용자가&nbsp;사전에지정된&nbsp;기간&nbsp;동안로그인하지&nbsp;않으면&nbsp;고객사관리자에게&nbsp;통지할&nbsp;수있습니다.</p><p>②&nbsp;회사는 1달&nbsp;동안&nbsp;서비스를이용하지&nbsp;않은&nbsp;이용자는코코넛&nbsp;서비스&nbsp;개인회원에&nbsp;대한&nbsp;접근권한을&nbsp;회수하고, 이용자와&nbsp;고객사&nbsp;관리자에게통지합니다.&nbsp;고객사&nbsp;관리자는권한을&nbsp;다시&nbsp;부여할수&nbsp;있습니다.</p><p>③&nbsp;개인정보의&nbsp;파기를원하지&nbsp;않으시는&nbsp;경우,&nbsp;기간&nbsp;만료&nbsp;전서비스에&nbsp;로그인하시면&nbsp;됩니다.</p><h3>&nbsp;</h3><h3><strong>5.&nbsp;개인정보&nbsp;처리&nbsp;업무의&nbsp;위탁에&nbsp;관한&nbsp;사항</strong></h3><figure class=\"table\"><table><thead><tr><th><strong>위탁&nbsp;업무</strong></th><th><strong>필수/선택</strong></th><th><strong>수탁&nbsp;업체</strong></th><th><strong>위탁&nbsp;기간</strong></th></tr></thead><tbody><tr><td>본인 인증</td><td>필수</td><td>나이스평가정보</td><td rowspan=\"5\">코코넛&nbsp;서비스&nbsp;계약&nbsp;또는&nbsp;재위탁&nbsp;계약의&nbsp;만료&nbsp;및&nbsp;해지시까지</td></tr><tr><td>대금 결제</td><td>필수</td><td>아임포트,&nbsp;나이스페이먼츠</td></tr><tr><td>개인정보 저장</td><td>필수</td><td>Amazon Web Services, Inc.</td></tr><tr><td>카카오 알림톡</td><td>선택</td><td><p>카카오,</p><p>네이버 클라우드 플랫폼</p></td></tr><tr><td>메일링 서비스</td><td>선택</td><td>네이버 클라우드 플랫폼</td></tr></tbody></table></figure><h3><br>6.&nbsp;정보주체의&nbsp;권리·의무및&nbsp;그&nbsp;행사방법에관한&nbsp;사항</h3><p>①&nbsp;이용자는&nbsp;계약기간&nbsp;내에&nbsp;언제든지등록되어&nbsp;있는&nbsp;자신의개인정보를&nbsp;조회하거나&nbsp;수정할수&nbsp;있으며&nbsp;자신의개인정보에&nbsp;대해&nbsp;정보삭제및&nbsp;처리정지&nbsp;요구등의&nbsp;권리를&nbsp;행사할수&nbsp;있습니다.</p><p>②&nbsp;온라인으로&nbsp;제공되지않는&nbsp;정보주체의&nbsp;권리행사는&nbsp;개인정보보호&nbsp;담당부서(<a href=\"mailto:privacy@kokonut.me\">privacy@kokonut.me</a>)로전자우편을&nbsp;보내주시면, 회사는&nbsp;이에&nbsp;대해지체없이&nbsp;조치하겠습니다.</p><p>③&nbsp;권리&nbsp;행사는정보주체의&nbsp;위임을&nbsp;받은자&nbsp;등&nbsp;대리인을통하여&nbsp;할&nbsp;수도있습니다.&nbsp;이&nbsp;경우 “개인정보&nbsp;처리&nbsp;방법에관한&nbsp;고시(제2020-7호)”&nbsp;별지&nbsp;제11호&nbsp;서식에&nbsp;따른위임장을&nbsp;제출하여야&nbsp;합니다.</p><p>④&nbsp;회사는&nbsp;정보주체의권리에&nbsp;따른&nbsp;열람·정정·삭제·처리정지의&nbsp;요구&nbsp;시요구를&nbsp;한&nbsp;자가본인이거나&nbsp;정당한&nbsp;대리인인지를확인합니다.</p><p>&nbsp;</p><h3><strong>7.&nbsp;개인정보의&nbsp;안전성&nbsp;확보&nbsp;조치에&nbsp;관한&nbsp;사항</strong></h3><p>회사는개인정보의&nbsp;안전성&nbsp;확보를위해&nbsp;다음과&nbsp;같은조치를&nbsp;취하고&nbsp;있습니다.</p><ul><li><strong>관리적 보호 조치</strong> :&nbsp;내부관리계획 수립·시행,&nbsp;지속적인 직원 교육</li><li><strong>기술적 보호 조치</strong> :&nbsp;접근권한 관리,&nbsp;네트워크 보안,&nbsp;개인정보 암호화,&nbsp;보안 프로그램 운영, 2단계 인증,&nbsp;접속기록 보호&nbsp;등</li></ul><h3><br><strong>8. 개인정보보호책임자에관한&nbsp;사항</strong></h3><p>정보주체는개인정보&nbsp;보호&nbsp;관련문의,&nbsp;불만처리, 피해구제&nbsp;등에&nbsp;관한사항을&nbsp;개인정보보호&nbsp;책임자에게문의하실&nbsp;수&nbsp;있습니다.&nbsp;개인정보보호&nbsp;책임자는&nbsp;이용자의문의에&nbsp;대해&nbsp;지체없이답변&nbsp;및&nbsp;처리해드릴&nbsp;것입니다.</p><figure class=\"table\"><table><thead><tr><th><strong>성명</strong></th><th><strong>직책</strong></th><th><strong>연락처</strong></th><th><strong>담당부서</strong></th></tr></thead><tbody><tr><td>곽호림</td><td>CEO</td><td><a href=\"mailto:privacy@kokonut.me\">privacy@kokonut.me</a></td><td>정보보안팀</td></tr></tbody></table></figure><p>&nbsp;</p><h3><strong>9.&nbsp;개인정보&nbsp;처리방침의&nbsp;변경에&nbsp;관한&nbsp;사항</strong></h3><p>개인정보처리방침은&nbsp;시행일로부터&nbsp;적용되며,&nbsp;법령&nbsp;및&nbsp;방침에따른&nbsp;변경내용의&nbsp;추가,&nbsp;삭제&nbsp;및&nbsp;정정이있는&nbsp;경우에는&nbsp;변경사항의시행 7일&nbsp;전부터홈페이지&nbsp;또는&nbsp;이메일등&nbsp;개별&nbsp;통지방법을&nbsp;통해&nbsp;고지할것입니다.</p><ul><li>현 개인정보 처리방침 시행 일자: 2022. 6. 1</li></ul><h3>&nbsp;</h3><h3>10. 개인정보의&nbsp;권익침해에&nbsp;대한구제방법</h3><p>이용자는개인정보침해로&nbsp;인한&nbsp;구제를받기&nbsp;위하여&nbsp;개인정보분쟁조정위원회,&nbsp;한국인터넷진흥원&nbsp;개인정보침해신고센터&nbsp;등에&nbsp;분쟁해결이나&nbsp;상담등을&nbsp;신청할&nbsp;수있습니다.&nbsp;이&nbsp;밖에기타&nbsp;개인정보침해의&nbsp;신고,&nbsp;상담에&nbsp;대하여는&nbsp;아래의기관에&nbsp;문의하시기&nbsp;바랍니다.</p><ul><li>개인정보분쟁조정위원회 : (국번없이) 1833-6972 (<a href=\"http://www.kopico.go.kr\">www.kopico.go.kr</a>)</li><li>개인정보침해신고센터 : (국번없이) 118 (<a href=\"http://privacy.kisa.or.kr\">privacy.kisa.or.kr</a>)</li><li>대검찰청 : (국번없이) 1301 (<a href=\"http://www.spo.go.kr\">www.spo.go.kr</a>)</li><li>경찰청 : (국번없이) 182 (<a href=\"http://ecrm.cyber.go.kr\">ecrm.cyber.go.kr</a>)</li></ul><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p>','시스템관리자','2022-05-04 09:00:00','2022-05-04 13:13:55',1,'시스템관리자','2022-05-04 13:18:13');

/*Table structure for table `privacy_email` */

DROP TABLE IF EXISTS `privacy_email`;

CREATE TABLE `privacy_email` (
  `IDX` int(11) NOT NULL AUTO_INCREMENT COMMENT '키',
  `SENDER_EMAIL` varchar(350) NOT NULL COMMENT '발신자 이메일',
  `TITLE` varchar(600) NOT NULL COMMENT '제목',
  `CONTENTS` text NOT NULL COMMENT '내용',
  `REGDATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '등록일',
  `COMPANY_IDX` int(11) DEFAULT NULL COMMENT '기업 키(기업별 이메일인 경우)',
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `privacy_email` */

/*Table structure for table `privacy_email_history` */

DROP TABLE IF EXISTS `privacy_email_history`;

CREATE TABLE `privacy_email_history` (
  `IDX` int(11) NOT NULL AUTO_INCREMENT COMMENT '키',
  `PRIVACY_EMAIL_IDX` int(11) NOT NULL COMMENT 'privacy_email 키',
  `RECEIVER_EMAIL` varchar(256) NOT NULL COMMENT '받는 사람 이메일',
  `SEND_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '발송일',
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `privacy_email_history` */

/*Table structure for table `qna` */

DROP TABLE IF EXISTS `qna`;

CREATE TABLE `qna` (
  `IDX` int(11) NOT NULL AUTO_INCREMENT COMMENT '키',
  `COMPANY_IDX` int(11) DEFAULT NULL COMMENT 'COMPANY IDX',
  `ADMIN_IDX` int(11) DEFAULT NULL COMMENT '질문자(사용자 키)',
  `TITLE` varchar(256) DEFAULT NULL COMMENT '제목',
  `CONTENT` text COMMENT '문의내용',
  `FILE_GROUP_ID` varchar(128) DEFAULT NULL COMMENT '첨부파일 아이디',
  `TYPE` int(11) DEFAULT '0' COMMENT '분류(0:기타,1:회원정보,2:사업자정보,3:Kokonut서비스,4:결제)',
  `REGDATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '질문등록일시',
  `STATE` int(11) DEFAULT '0' COMMENT '상태(0:답변대기,1:답변완료)',
  `ANS_IDX` int(11) DEFAULT NULL COMMENT '질문 답변자',
  `ANSWER` text COMMENT '답변 내용',
  `ANSWER_DATE` timestamp NULL DEFAULT NULL COMMENT '답변일',
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `qna` */

/*Table structure for table `revised_document` */

DROP TABLE IF EXISTS `revised_document`;

CREATE TABLE `revised_document` (
  `IDX` int(11) NOT NULL AUTO_INCREMENT COMMENT '키',
  `COMPANY_IDX` int(11) DEFAULT NULL COMMENT '회사(Company) 키',
  `ENFORCE_START_DATE` timestamp NULL DEFAULT NULL COMMENT '시행시작일자',
  `ENFORCE_END_DATE` timestamp NULL DEFAULT NULL COMMENT '시행종료일자',
  `FILE_GROUP_ID` varchar(256) DEFAULT NULL COMMENT '파일그룹아이디',
  `ADMIN_IDX` int(11) DEFAULT NULL COMMENT '등록자',
  `REGISTER_NAME` varchar(128) DEFAULT NULL COMMENT '등록자이름',
  `REGDATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자',
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `revised_document` */

/* DROP TABLE IF EXISTS `kn_service`; */
DROP TABLE IF EXISTS `kn_service`;
CREATE TABLE `kn_service` (
  `idx` int(11) NOT NULL AUTO_INCREMENT COMMENT '주키',
  `ks_service` varchar(64) DEFAULT NULL COMMENT '서비스 이름',
  `ks_price` int(11) DEFAULT NULL COMMENT '서비스 금액',
  `ks_per_price` int(11) DEFAULT NULL COMMENT '평균 회원 1명당 금액',
  `reg_date` timestamp NOT NULL DEFAULT current_timestamp() COMMENT '등록일',
  `modify_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE current_timestamp() COMMENT '수정일',
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;
INSERT INTO `kn_service` (`idx`, `ks_service`, `ks_price`, `ks_per_price`, `reg_date`, `modify_date`)
VALUES
    (1, 'BASIC', 0, 0, '2021-12-02 13:19:52', '2022-04-14 16:52:09'),
    (2, 'STANDARD', 0, 20, '2021-12-02 13:20:53', '2022-05-16 15:26:38');

select * from `kn_service`;
commit;

/*Table structure for table `knSetting` */

DROP TABLE IF EXISTS `knSetting`;

CREATE TABLE `knSetting` (
  `IDX` int(11) NOT NULL AUTO_INCREMENT COMMENT '키',
  `COMPANY_IDX` int(11) DEFAULT NULL COMMENT '회사(Company) 키',
  `OVERSEAS_BLOCK` int(11) DEFAULT '0' COMMENT '해외로그인차단(0:차단안함,1:차단)',
  `DORMANT_ACCOUNT` int(11) DEFAULT '0' COMMENT '휴면회원 전환 시(0:다른DB로 정보이관\r\n, 1:이관 없이 회원정보 삭제)',
  `REGDATE` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
  `MODIFY_DATE` timestamp NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `knSetting` */

/*Table structure for table `shedlock` */

DROP TABLE IF EXISTS `shedlock`;

CREATE TABLE `shedlock` (
  `name` varchar(64) NOT NULL COMMENT '스케줄잠금이름',
  `lock_until` timestamp(3) NULL DEFAULT NULL COMMENT '잠금기간',
  `locked_at` timestamp(3) NULL DEFAULT NULL COMMENT '잠금일시',
  `locked_by` varchar(255) DEFAULT NULL COMMENT '잠금신청자',
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='스케줄잠금';

/*Data for the table `shedlock` */

/*Table structure for table `statistics_day` */

DROP TABLE IF EXISTS `statistics_day`;

CREATE TABLE `statistics_day` (
  `IDX` int(11) NOT NULL AUTO_INCREMENT,
  `COMPANY_IDX` int(11) DEFAULT NULL COMMENT 'COMPANY IDX',
  `DATE` date DEFAULT NULL COMMENT '날짜(일자로 기록)',
  `ALL_MEMBER` int(11) DEFAULT '0' COMMENT '일평균 회원 수',
  `NEW_MEMBER` int(11) DEFAULT '0' COMMENT '개인회원(신규가입총합은 더해서 표현)',
  `DORMANT` int(11) DEFAULT '0' COMMENT '휴면계정전환',
  `WITHDRAWAL` int(11) DEFAULT '0' COMMENT '회원탈퇴',
  `PERSONAL_HISTORY` int(11) DEFAULT '0' COMMENT '개인정보 열람 이력',
  `ADMIN_HISTORY` int(11) DEFAULT '0' COMMENT '관리자 열람 이력',
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `statistics_day` */

/*Table structure for table `statistics_day_system` */

DROP TABLE IF EXISTS `statistics_day_system`;

CREATE TABLE `statistics_day_system` (
  `IDX` int(11) NOT NULL AUTO_INCREMENT,
  `DATE` date DEFAULT NULL COMMENT '날짜(일자로 기록)',
  `NEW_MEMBER` int(11) DEFAULT '0' COMMENT '신규회원',
  `NEW_MASTER_MEMBER` int(11) DEFAULT '0' COMMENT '신규사업자회원',
  `NEW_ADMIN_MEMBER` int(11) DEFAULT '0' COMMENT '신규개인회원',
  `DORMANT` int(11) DEFAULT '0' COMMENT '휴면계정전환',
  `WITHDRAWAL` int(11) DEFAULT '0' COMMENT '회원탈퇴,회원탈퇴해지(이탈총합은 더해서 표현)',
  `BASIC` int(11) DEFAULT '0' COMMENT '신규 서비스 BASIC',
  `STANDARD` int(11) DEFAULT '0' COMMENT '신규 서비스 STANDARD',
  `PREMIUM` int(11) DEFAULT '0' COMMENT '신규 서비스 PREMIUM',
  `AUTO_CANCEL` int(11) DEFAULT '0' COMMENT '자동결제해지',
  `WITHDRAWAL_CANCEL` int(11) DEFAULT '0' COMMENT '회원탈퇴해지',
  `BASIC_AMOUNT` int(11) DEFAULT '0' COMMENT 'BASIC 결제금액',
  `STANDARD_USER` int(11) DEFAULT '0' COMMENT 'STANDARD 이용자',
  `STANDARD_AMOUNT` int(11) DEFAULT '0' COMMENT 'STANDARD 결제금액',
  `PREMIUM_AMOUNT` int(11) DEFAULT '0' COMMENT 'PREMIUM 결제금액',
  `PERSONAL_HISTORY` int(11) DEFAULT '0' COMMENT '개인정보열람이력',
  `ADMIN_HISTORY` int(11) DEFAULT '0' COMMENT '관리자열람이력',
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `statistics_day_system` */

/*Table structure for table `total_db_download` */

DROP TABLE IF EXISTS `total_db_download`;

CREATE TABLE `total_db_download` (
  `IDX` int(11) NOT NULL AUTO_INCREMENT COMMENT '키',
  `ADMIN_IDX` int(11) DEFAULT NULL COMMENT '요청자',
  `REASON` varchar(512) DEFAULT NULL COMMENT '요청사유',
  `APPLY_DATE` datetime NULL DEFAULT NULL COMMENT '요청일자',
  `LINK` varchar(512) DEFAULT NULL COMMENT '다운로드 링크',
  `STATE` int(11) DEFAULT '1' COMMENT '상태(1:다운로드요청, 2:다운로드승인(다운로드대기), 3:다운로드완료, 4:반려)',
  `RETURN_REASON` varchar(512) DEFAULT NULL COMMENT '반려사유',
  `LIMIT` int(11) DEFAULT NULL COMMENT '횟수제한',
  `LIMIT_DATE_START` datetime NULL DEFAULT NULL COMMENT '기간제한 시작일자',
  `LIMIT_DATE_END` datetime NULL DEFAULT NULL COMMENT '기간제한 종료일자',
  `DOWNLOAD_DATE` datetime NULL DEFAULT NULL COMMENT '다운로드 일자',
  `REGISTER_IDX` int(11) DEFAULT NULL COMMENT '다운로드정보 등록자',
  `REGIST_DATE` datetime NULL DEFAULT NULL COMMENT '다운로드정보 등록일시',
  `MODIFIER_IDX` int(11) DEFAULT NULL COMMENT '다운로드정보 수정자',
  `MODIFY_DATE` datetime NULL DEFAULT NULL COMMENT '다운로드정보 수정일시',
  `IP_ADDR` varchar(64) DEFAULT NULL COMMENT 'IP주소(다운로드정보에 표현)',
  `REGDATE` datetime NULL DEFAULT NULL COMMENT '요청일시',
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `company_file`;

CREATE TABLE IF NOT EXISTS `company_file` (
  `IDX` int(11) NOT NULL AUTO_INCREMENT,
  `COMPANY_IDX` int(11) NOT NULL,
  `CF_PATH` varchar(255) DEFAULT NULL COMMENT 'S3 파일 경로',
  `CF_FILENAME` varchar(255) DEFAULT NULL COMMENT 'S3 파일 명',
  `CF_ORIGINAL_FILENAME` varchar(255) DEFAULT NULL COMMENT '원래 파일명',
  `CF_VOLUME` bigint(20) DEFAULT NULL COMMENT '용량',
  `REGIDX` int(11) DEFAULT NULL,
  `REGDATE` datetime DEFAULT NULL,
  `MODIFYIDX` int(11) DEFAULT NULL,
  `MODIFYDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='사업자등록증 이미지 파일테이블';

CREATE TABLE `revised_document_file` (
 `IDX` int(11) NOT NULL AUTO_INCREMENT,
 `REVISED_DOCUMENT_IDX` int(11) NOT NULL,
 `CF_PATH` varchar(255) DEFAULT NULL COMMENT 'S3 파일 경로',
 `CF_FILENAME` varchar(255) DEFAULT NULL COMMENT 'S3 파일 명',
 `CF_ORIGINAL_FILENAME` varchar(255) DEFAULT NULL COMMENT '원래 파일명',
 `CF_VOLUME` bigint(20) DEFAULT NULL COMMENT '용량',
 `REGIDX` int(11) DEFAULT NULL,
 `REGDATE` datetime DEFAULT NULL,
 `MODIFYIDX` int(11) DEFAULT NULL,
 `MODIFYDATE` datetime DEFAULT NULL,
 PRIMARY KEY (`IDX`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci COMMENT='개인정보 처리방침 개정문서 파일 테이블';

/*Data for the table `total_db_download` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
