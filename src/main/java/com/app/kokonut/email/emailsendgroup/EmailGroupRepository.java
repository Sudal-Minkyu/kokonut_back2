package com.app.kokonut.email.emailsendgroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EmailGroupRepository extends JpaRepository<EmailSendGroup, Long>, JpaSpecificationExecutor<EmailSendGroup>, EmailGroupRepositoryCustom {

}