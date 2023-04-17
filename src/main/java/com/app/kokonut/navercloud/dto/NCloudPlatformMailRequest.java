package com.app.kokonut.navercloud.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NCloudPlatformMailRequest {
	
//	HTTP 상태코드	설명
//	201	발송 요청 성공
//	400	인증 실패, 잘못된 요청
//	500	서버 오류
	
//	HTTP Status Code	리턴 코드	응답 메시지
//	400	77101	로그인 정보 오류
//	400	77102	BAD_REQUEST
//	400	77103	리소스가 존재하지 않음
//	403	77201	권한 없음
//	403	77202	Email 상품 사용신청 하지 않음
//	405	77001	METHOD_NOT_ALLOWED
//	415	77002	UNSUPPORTED_MEDIA_TYPE
//	500	77301	기본 프로젝트가 존재하지 않음
//	500	77302	외부 시스템 API 연동 오류
//	500	77303	그외 INTERNAL_SERVER_ERROR
														//* - 필수값!!!!!
	String senderAddress;								//*발송자 Email 주소
	String senderName;									//발송자 이름
//	int templateSid;									//템플릿 ID
	String title;										//*제목
	String body;										//*Email 본문
	boolean individual = true;							//개인별 발송 or 일반 발송여부 default: true;
	boolean advertising;								//광고메일여부
//	Object parameters;									//Map 형태의 Object
//	Long reservationUtc;								//예약발송일시, reservateionUtc 값 우선
//	List<String> attachFileds;							//첨부파일 ID 목록
	List<RecipientForRequest> recipients;				//*수신자 목록, recipientGroupFilter 값이 입력되지 않으면 필수
//	List<RecipientGroupFilter> recipientGroupFilter;	//수신자 그룹 조합 발송 조건
	boolean useBasicUnsubscribeMsg = false;				//광고 메일의 경우 기본 수신 거부 문구 사용 여부
	String unsubscribeMessage;							//*사용자 정의 수신 거부 문구, 수신 거부 문구는 기본적으로 body의 끝에 추가되며 본문의 원하는 위치에 #{UNSUBSCRIBE_MESSAGE}를 추가하면 해당 태그의 위치에 수신거부 문구가 들어가게 됨
	List<AttachFile> attachFiles; // 첨부파일 목록

	public String getSenderAddress() {
		return senderAddress;
	}
	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
//	public int getTemplateSid() {
//		return templateSid;
//	}
//	public void setTemplateSid(int templateSid) {
//		this.templateSid = templateSid;
//	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public boolean isIndividual() {
		return individual;
	}
	public void setIndividual(boolean individual) {
		this.individual = individual;
	}
	public boolean isAdvertising() {
		return advertising;
	}
	public void setAdvertising(boolean advertising) {
		this.advertising = advertising;
	}
//	public Object getParameters() {
//		return parameters;
//	}
//	public void setParameters(Object parameters) {
//		this.parameters = parameters;
//	}
//	public Long getReservationUtc() {
//		return reservationUtc;
//	}
//	public void setReservationUtc(Long reservationUtc) {
//		this.reservationUtc = reservationUtc;
//	}
//	public List<String> getAttachFileds() {
//		return attachFileds;
//	}
//	public void setAttachFileds(List<String> attachFileds) {
//		this.attachFileds = attachFileds;
//	}
	public List<RecipientForRequest> getRecipients() {
		return recipients;
	}
	public void setRecipients(List<RecipientForRequest> recipients) {
		this.recipients = recipients;
	}
//	public List<RecipientGroupFilter> getRecipientGroupFilter() {
//		return recipientGroupFilter;
//	}
//	public void setRecipientGroupFilter(List<RecipientGroupFilter> recipientGroupFilter) {
//		this.recipientGroupFilter = recipientGroupFilter;
//	}
	public boolean isUseBasicUnsubscribeMsg() {
		return useBasicUnsubscribeMsg;
	}
	public void setUseBasicUnsubscribeMsg(boolean useBasicUnsubscribeMsg) {
		this.useBasicUnsubscribeMsg = useBasicUnsubscribeMsg;
	}
	public String getUnsubscribeMessage() {
		return unsubscribeMessage;
	}
	public void setUnsubscribeMessage(String unsubscribeMessage) {
		this.unsubscribeMessage = unsubscribeMessage;
	}
	
}
