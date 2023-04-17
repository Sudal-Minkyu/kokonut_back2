package com.app.kokonut.bizMessage.kakaoChannel;

import com.app.kokonut.bizMessage.kakaoChannel.dto.KakaoChannelByChannelIdListDto;
import com.app.kokonut.bizMessage.kakaoChannel.dto.KakaoChannelListDto;
import com.app.kokonut.bizMessage.kakaoChannel.dto.KakaoChannelSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Woody
 * Date : 2022-12-15
 * Time :
 * Remark : 기존의 코코넛 프로젝트의 KakaoChannel Sql 쿼리호출
 */
public interface KakaoChannelRepositoryCustom {

    // 카카오 채널 리스트 호출 -> 기존의 코코넛 메서트 : SelectKakaoTalkChannelList
    Page<KakaoChannelListDto> findByKakaoChannelPage(KakaoChannelSearchDto kakaoChannelSearchDto, Long companyId, Pageable pageable);

    // 카카오 채널ID 리스트 조회
    List<KakaoChannelByChannelIdListDto> findByKakaoChannelIdList(Long companyId);

}
