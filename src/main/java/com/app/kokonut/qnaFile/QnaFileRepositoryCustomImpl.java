package com.app.kokonut.qnaFile;

import com.app.kokonut.admin.QAdmin;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.qna.QQna;
import com.app.kokonut.qna.Qna;
import com.app.kokonut.qna.QnaRepositoryCustom;
import com.app.kokonut.qna.dtos.QnaDetailDto;
import com.app.kokonut.qna.dtos.QnaListDto;
import com.app.kokonut.qna.dtos.QnaSchedulerDto;
import com.app.kokonut.qnaFile.dto.QnaFileListDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import io.swagger.annotations.ApiModelProperty;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author joy
 * Date : 2022-12-27
 * Time :
 * Remark : QnaRepositoryCustom 쿼리문 선언부
 */
@Repository
public class QnaFileRepositoryCustomImpl extends QuerydslRepositorySupport implements QnaFileRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public QnaFileRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(QnaFile.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    @Override
    public List<QnaFileListDto> findByQnaFileList(Long qnaId) {
        QQnaFile qnaFile = QQnaFile.qnaFile;

        JPQLQuery<QnaFileListDto> query = from(qnaFile)
                .select(Projections.constructor(QnaFileListDto.class,
                        qnaFile.qfId,
                        qnaFile.qfBucket,
                        qnaFile.qfFilename,
                        qnaFile.qfOriginalFilename,
                        qnaFile.qfVolume
                ));

        query.where(qnaFile.qnaId.eq(qnaId));

        return query.fetch();
    }

}
