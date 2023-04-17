package com.app.kokonut.bizMessage.friendtalkMessage.friendtalkMessageRecipient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FriendtalkMessageRecipientRepository extends JpaRepository<FriendtalkMessageRecipient, Long>, JpaSpecificationExecutor<FriendtalkMessageRecipient> {

}