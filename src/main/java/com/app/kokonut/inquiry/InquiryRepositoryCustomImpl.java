package com.app.kokonut.inquiry;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

/**
 * @author Woody
 * Date : 2023-01-19
 * Time :
 * Remark :
 */
@Repository
public class InquiryRepositoryCustomImpl extends QuerydslRepositorySupport implements InquiryRepositoryCustom {

    public InquiryRepositoryCustomImpl() {
        super(Inquiry.class);
    }

}
