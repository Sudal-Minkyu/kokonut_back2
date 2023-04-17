package com.app.kokonut.bizMessage.alimtalkMessage.alimtalkMessageRecipient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AlimtalkMessageRecipientRepository extends JpaRepository<AlimtalkMessageRecipient, Long>, JpaSpecificationExecutor<AlimtalkMessageRecipient> {

}