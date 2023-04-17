package com.app.kokonut.bizMessage.alimtalkTemplate;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAlimtalkTemplate is a Querydsl query type for AlimtalkTemplate
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAlimtalkTemplate extends EntityPathBase<AlimtalkTemplate> {

    private static final long serialVersionUID = 1737472247L;

    public static final QAlimtalkTemplate alimtalkTemplate = new QAlimtalkTemplate("alimtalkTemplate");

    public final StringPath atAdContent = createString("atAdContent");

    public final StringPath atEmphasizeSubTitle = createString("atEmphasizeSubTitle");

    public final StringPath atEmphasizeTitle = createString("atEmphasizeTitle");

    public final StringPath atEmphasizeType = createString("atEmphasizeType");

    public final StringPath atExtraContent = createString("atExtraContent");

    public final NumberPath<Long> atId = createNumber("atId", Long.class);

    public final StringPath atMessageType = createString("atMessageType");

    public final NumberPath<Integer> atSecurityFlag = createNumber("atSecurityFlag", Integer.class);

    public final StringPath atStatus = createString("atStatus");

    public final StringPath atTemplateCode = createString("atTemplateCode");

    public final StringPath atTemplateName = createString("atTemplateName");

    public final DateTimePath<java.time.LocalDateTime> insert_date = createDateTime("insert_date", java.time.LocalDateTime.class);

    public final StringPath insert_email = createString("insert_email");

    public final StringPath kcChannelId = createString("kcChannelId");

    public final DateTimePath<java.time.LocalDateTime> modify_date = createDateTime("modify_date", java.time.LocalDateTime.class);

    public final StringPath modify_email = createString("modify_email");

    public QAlimtalkTemplate(String variable) {
        super(AlimtalkTemplate.class, forVariable(variable));
    }

    public QAlimtalkTemplate(Path<? extends AlimtalkTemplate> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAlimtalkTemplate(PathMetadata metadata) {
        super(AlimtalkTemplate.class, metadata);
    }

}

