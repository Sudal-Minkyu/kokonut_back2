package com.app.kokonut.keydata;

import com.app.kokonut.keydata.dtos.KeyDataDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

/**
 * @author Woody
 * Date : 2023-01-04
 * Time :
 * Remark :
 */
@Repository
public class KeyDataRepositoryCustomImpl extends QuerydslRepositorySupport implements KeyDataRepositoryCustom {

    public KeyDataRepositoryCustomImpl() {
        super(KeyData.class);
    }

    // kdKeyValue 조회
    @Override
    public KeyDataDto findByKeyValue(String kdKeyName) {

        QKeyData keyData = QKeyData.keyData;

        JPQLQuery<KeyDataDto> query = from(keyData)
                .where(keyData.kdKeyName.eq(kdKeyName))
                .select(Projections.constructor(KeyDataDto.class,
                        keyData.kdKeyValue
                ));

        return query.fetchOne();
    }

    // AWS S3 keyData 조회
    @Override
    public String findByAWSKey(String kdKeyName) {

        QKeyData keyData = QKeyData.keyData;

        JPQLQuery<String> query = from(keyData)
                .where(keyData.kdKeyGroup.eq("aws_s3").and(keyData.kdKeyName.eq(kdKeyName)))
                .select(Projections.constructor(String.class,
                        keyData.kdKeyValue
                ));

        return query.fetchOne();
    }

    // KMS keyData 조회
    @Override
    public String findByKMSKey(String kdKeyName) {

        QKeyData keyData = QKeyData.keyData;

        JPQLQuery<String> query = from(keyData)
                .where(keyData.kdKeyGroup.eq("kms").and(keyData.kdKeyName.eq(kdKeyName)))
                .select(Projections.constructor(String.class,
                        keyData.kdKeyValue
                ));

        return query.fetchOne();
    }

    // NCLOUD keyData 조회
    @Override
    public String findByNCLOUDKey(String kdKeyName) {

        QKeyData keyData = QKeyData.keyData;

        JPQLQuery<String> query = from(keyData)
                .where(keyData.kdKeyGroup.eq("ncloud").and(keyData.kdKeyName.eq(kdKeyName)))
                .select(Projections.constructor(String.class,
                        keyData.kdKeyValue
                ));

        return query.fetchOne();
    }

    // NICE keyData 조회
    @Override
    public String findByNICEKey(String kdKeyName) {

        QKeyData keyData = QKeyData.keyData;

        JPQLQuery<String> query = from(keyData)
                .where(keyData.kdKeyGroup.eq("nice").and(keyData.kdKeyName.eq(kdKeyName)))
                .select(Projections.constructor(String.class,
                        keyData.kdKeyValue
                ));

        return query.fetchOne();
    }

    // MAIL keyData 조회
    @Override
    public String findByMAILKey(String kdKeyName) {
        QKeyData keyData = QKeyData.keyData;

        JPQLQuery<String> query = from(keyData)
                .where(keyData.kdKeyGroup.eq("mail").and(keyData.kdKeyName.eq(kdKeyName)))
                .select(Projections.constructor(String.class,
                        keyData.kdKeyValue
                ));

        return query.fetchOne();
    }

    // OTP keyData 조회
    @Override
    public String findByOTPKey(String kdKeyName) {
        QKeyData keyData = QKeyData.keyData;

        JPQLQuery<String> query = from(keyData)
                .where(keyData.kdKeyGroup.eq("otp").and(keyData.kdKeyName.eq(kdKeyName)))
                .select(Projections.constructor(String.class,
                        keyData.kdKeyValue
                ));

        return query.fetchOne();
    }

}
