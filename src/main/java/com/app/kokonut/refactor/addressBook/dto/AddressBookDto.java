package com.app.kokonut.refactor.addressBook.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Woody
 * Date : 2022-11-29
 * Time :
 * Remark : AddressBook 단일 조회 Dto
 * 사용 메서드 :
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressBookDto implements Serializable {

    /**
     * 회사 키(주소록 보는 권한이 개인이면 삭제해도 되는 컬럼)
     */
    private Long companyId;

    /**
     * 관리자 키
     */
    private Long adminId;

    /**
     * IP
     */
    private String ip;

    /**
     * 등록일시
     */
    private Date regdate;

    /**
     * 만료일시
     */
    private Date expDate;

    /**
     * 수정일자
     */
    private Date modifyDate;

    /**
     * 발송여부(Y/N)
     */
    private String sended;

    /**
     * 발송일
     */
    private Date sendDate;

    /**
     * 주소록 용도
     */
    private String use;

    /**
     * 발송목적(NOTICE: 주요공지, AD:광고/홍보)
     */
    private String purpose;

    /**
     * 파일 그룹 아이디
     */
    private String fileGroupId;

    /**
     * 발송대상(ALL: 전체회원, SELECTED: 선택회원)
     */
    private String target;

    /**
     * 메시지종류(EMAIL: 이메일, alimTalk ALIMTALK: 알림톡)
     */
    private String type;

    /**
     * 발신자 이메일
     */
    private String senderEmail;

    /**
     * 메시지 제목
     */
    private String title;

    /**
     * 메시지 내용
     */
    private String content;

}
