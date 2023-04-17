package com.app.kokonut.refactor.privacyEmailHistory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrivacyEmailHistoryService {

    @Autowired
    private PrivacyEmailHistoryRepository privacyEmailHistoryRepository;

}
