package com.app.kokonut.navercloud.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttachFile {

    private String fileId;
    private String fileName;
    private Integer fileSize;

}
