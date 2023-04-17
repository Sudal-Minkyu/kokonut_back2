package com.app.kokonut.bizMessage.friendtalkMessage;

import com.app.kokonut.bizMessage.friendtalkMessage.dto.FriendtalkMessageInfoListDto;
import com.app.kokonut.bizMessage.friendtalkMessage.dto.FriendtalkMessageListDto;
import com.app.kokonut.bizMessage.friendtalkMessage.dto.FriendtalkMessageSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Woody
 * Date : 2022-12-20
 * Time :
 * Remark : 기존의 코코넛 프로젝트의 FriendtalkMessage Sql 쿼리호출
 */
public interface FriendtalkMessageRepositoryCustom {

    Page<FriendtalkMessageListDto> findByFriendtalkMessagePage(FriendtalkMessageSearchDto friendtalkMessageSearchDto, Long companyId, Pageable pageable);

    List<FriendtalkMessageInfoListDto> findByFriendtalkMessageInfoList(Long companyId, String state);

}