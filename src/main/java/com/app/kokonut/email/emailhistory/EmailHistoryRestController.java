package com.app.kokonut.email.emailhistory;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Api(tags = "")
@Validated
@RestController
@RequestMapping("/v2/api/EmailHistory")
public class EmailHistoryRestController {


    private final EmailHistoryService emailHistoryService;

    @Autowired
    public EmailHistoryRestController(EmailHistoryService emailHistoryService) {
        this.emailHistoryService = emailHistoryService;
    }
}
