package com.app.kokonut.refactor.addressBookUser;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAddressBookUser is a Querydsl query type for AddressBookUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAddressBookUser extends EntityPathBase<AddressBookUser> {

    private static final long serialVersionUID = -935147623L;

    public static final QAddressBookUser addressBookUser = new QAddressBookUser("addressBookUser");

    public final NumberPath<Long> abuId = createNumber("abuId", Long.class);

    public final StringPath abuUserId = createString("abuUserId");

    public final NumberPath<Long> addressBookIdx = createNumber("addressBookIdx", Long.class);

    public final DateTimePath<java.time.LocalDateTime> insert_date = createDateTime("insert_date", java.time.LocalDateTime.class);

    public final StringPath insert_email = createString("insert_email");

    public final DateTimePath<java.time.LocalDateTime> modify_date = createDateTime("modify_date", java.time.LocalDateTime.class);

    public final StringPath modify_email = createString("modify_email");

    public QAddressBookUser(String variable) {
        super(AddressBookUser.class, forVariable(variable));
    }

    public QAddressBookUser(Path<? extends AddressBookUser> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAddressBookUser(PathMetadata metadata) {
        super(AddressBookUser.class, metadata);
    }

}

