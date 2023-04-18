package com.app.kokonut.bizMessage.kakaoChannel;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QKakaoChannel is a Querydsl query type for KakaoChannel
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QKakaoChannel extends EntityPathBase<KakaoChannel> {

    private static final long serialVersionUID = 317320279L;

    public static final QKakaoChannel kakaoChannel = new QKakaoChannel("kakaoChannel");

    public final NumberPath<Long> companyId = createNumber("companyId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> insert_date = createDateTime("insert_date", java.time.LocalDateTime.class);

    public final StringPath insert_email = createString("insert_email");

    public final StringPath kcChannelId = createString("kcChannelId");

    public final StringPath kcChannelName = createString("kcChannelName");

    public final NumberPath<Long> kcId = createNumber("kcId", Long.class);

    public final StringPath kcStatus = createString("kcStatus");

    public final DateTimePath<java.time.LocalDateTime> modify_date = createDateTime("modify_date", java.time.LocalDateTime.class);

    public final StringPath modify_email = createString("modify_email");

    public QKakaoChannel(String variable) {
        super(KakaoChannel.class, forVariable(variable));
    }

    public QKakaoChannel(Path<? extends KakaoChannel> path) {
        super(path.getType(), path.getMetadata());
    }

    public QKakaoChannel(PathMetadata metadata) {
        super(KakaoChannel.class, metadata);
    }

}

