package com.app.kokonut.refactor.addressBookUser;

import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

/**
 * @author Woody
 * Date : 2022-11-29
 * Time :
 * Remark : AddressBookRepositoryCustom 쿼리문 선언부
 */
@Repository
public class AddressBookUserRepositoryCustomImpl extends QuerydslRepositorySupport implements AddressBookUserRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public AddressBookUserRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(AddressBookUser.class);
        this.jpaResultMapper = jpaResultMapper;
    }

}
