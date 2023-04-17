package com.app.kokonut.adminRemove;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v2/api/AdminRemove")
public class AdminRemoveRestController {

    private final AdminRemoveService adminRemoveService;

    @Autowired
    public AdminRemoveRestController(AdminRemoveService adminRemoveService){
        this.adminRemoveService = adminRemoveService;
    }

}
