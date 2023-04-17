package com.app.kokonut.bizMessage.friendtalkMessage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendtalkMessageRepository extends JpaRepository<FriendtalkMessage, Long>, JpaSpecificationExecutor<FriendtalkMessage>, FriendtalkMessageRepositoryCustom {

}