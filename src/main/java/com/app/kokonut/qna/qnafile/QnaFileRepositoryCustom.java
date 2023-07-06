package com.app.kokonut.qna.qnafile;

import com.app.kokonut.qna.qnafile.dto.QnaFileListDto;

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
