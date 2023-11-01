package com.app.kokonut.test;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PhototestDto {

    private List<String> codeList;

    private List<String> dataList;

    private List<MultipartFile> multipartFile;

}
