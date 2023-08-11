package com.app.kokonut.awskmshistory;

/**
 * @author Woody
 * Date : 2023-08-11
 * Time :
 * Remark : AwsKmsHistory Sql 쿼리호출
 */
public interface AwsKmsHistoryRepositoryCustom {

    Long findByMonthKmsPrice(String cpCode, String akhYyyymm);

}