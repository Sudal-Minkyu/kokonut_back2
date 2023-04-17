package com.app.kokonut.qnaFile;

import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.qna.dtos.QnaDetailDto;
import com.app.kokonut.qna.dtos.QnaListDto;
import com.app.kokonut.qna.dtos.QnaSchedulerDto;
import com.app.kokonut.qnaFile.dto.QnaFileListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-01
 * Time :
 * Remark :
 */
public interface QnaFileRepositoryCustom {

    List<QnaFileListDto> findByQnaFileList(Long qnaId);

}
