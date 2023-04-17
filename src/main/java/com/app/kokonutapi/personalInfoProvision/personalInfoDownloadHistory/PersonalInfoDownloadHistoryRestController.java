package com.app.kokonutapi.personalInfoProvision.personalInfoDownloadHistory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Woody
 * Date : 2023-01-17
 * Remark :
 */
@Slf4j
@RestController
@RequestMapping("/v3/api/personalInfoDownloadHistory")
public class PersonalInfoDownloadHistoryRestController {

    private final PersonalInfoDownloadHistoryService personalInfoDownloadHistoryService;

    @Autowired
    public PersonalInfoDownloadHistoryRestController(PersonalInfoDownloadHistoryService personalInfoDownloadHistoryService){
        this.personalInfoDownloadHistoryService = personalInfoDownloadHistoryService;
    }

}
