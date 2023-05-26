package com.app.kokonutuser.dtos;
import lombok.Data;
import java.util.List;

/**
 * @author Woody
 * Date : 2023-05-20
 * Time :
 * Remark :
 */
@Data
public class KokonutSearchDto {

    private int pageNum; // 페이지번호

    private List<String> searchTables; // 조회하는 테이블번호

    private List<String> searchCodes; // 조회할 컬럼 고유코드

    private List<String> searchTexts; // 조회할 텍스트

    private List<String> searchSecuriys; // 조회하는 암호화여부

}