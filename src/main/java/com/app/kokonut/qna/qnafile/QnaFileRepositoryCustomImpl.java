package com.app.kokonut.qna.qnafile;

import com.app.kokonut.qna.qnafile.dto.QnaFileListDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

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
