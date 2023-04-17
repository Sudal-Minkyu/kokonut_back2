package com.app.kokonutapi.personalInfoProvision.personalInfoDownloadHistory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Woody
 * Date : 2023-01-17
 * Remark :
 */
@Service
public class PersonalInfoDownloadHistoryService {

    private PersonalInfoDownloadHistoryRepository personalInfoDownloadHistoryRepository;

    @Autowired
    public PersonalInfoDownloadHistoryService(PersonalInfoDownloadHistoryRepository personalInfoDownloadHistoryRepository){
        this.personalInfoDownloadHistoryRepository = personalInfoDownloadHistoryRepository;
    }


}
