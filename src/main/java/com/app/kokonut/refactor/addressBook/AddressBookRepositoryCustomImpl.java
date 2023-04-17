package com.app.kokonut.refactor.addressBook;

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
public class AddressBookRepositoryCustomImpl extends QuerydslRepositorySupport implements AddressBookRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public AddressBookRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(AddressBook.class);
        this.jpaResultMapper = jpaResultMapper;
    }

}
