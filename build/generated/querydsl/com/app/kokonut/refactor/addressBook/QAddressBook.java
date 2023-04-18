package com.app.kokonut.refactor.addressBook;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAddressBook is a Querydsl query type for AddressBook
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAddressBook extends EntityPathBase<AddressBook> {

    private static final long serialVersionUID = 851796291L;

    public static final QAddressBook addressBook = new QAddressBook("addressBook");

    public final StringPath abContent = createString("abContent");

    public final DateTimePath<java.time.LocalDateTime> abExpDate = createDateTime("abExpDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> abId = createNumber("abId", Long.class);

    public final StringPath abIpAddr = createString("abIpAddr");

    public final StringPath abIsSended = createString("abIsSended");

    public final StringPath abPurpose = createString("abPurpose");

    public final DateTimePath<java.time.LocalDateTime> abSendDate = createDateTime("abSendDate", java.time.LocalDateTime.class);

    public final StringPath abSenderEmail = createString("abSenderEmail");

    public final StringPath abTarget = createString("abTarget");

    public final StringPath abTitle = createString("abTitle");

    public final StringPath abType = createString("abType");

    public final StringPath abUse = createString("abUse");

    public final NumberPath<Long> adminId = createNumber("adminId", Long.class);

    public final NumberPath<Integer> companyId = createNumber("companyId", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> insert_date = createDateTime("insert_date", java.time.LocalDateTime.class);

    public final StringPath insert_email = createString("insert_email");

    public final DateTimePath<java.time.LocalDateTime> modify_date = createDateTime("modify_date", java.time.LocalDateTime.class);

    public final StringPath modify_email = createString("modify_email");

    public QAddressBook(String variable) {
        super(AddressBook.class, forVariable(variable));
    }

    public QAddressBook(Path<? extends AddressBook> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAddressBook(PathMetadata metadata) {
        super(AddressBook.class, metadata);
    }

}

