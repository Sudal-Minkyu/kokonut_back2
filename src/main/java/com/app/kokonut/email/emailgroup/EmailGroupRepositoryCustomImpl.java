package com.app.kokonut.email.emailgroup;

import lombok.extern.slf4j.Slf4j;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class EmailGroupRepositoryCustomImpl extends QuerydslRepositorySupport implements EmailGroupRepositoryCustom {

    @Autowired
    JpaResultMapper jpaResultMapper;

    public EmailGroupRepositoryCustomImpl() {
        super(EmailGroup.class);
    }

}