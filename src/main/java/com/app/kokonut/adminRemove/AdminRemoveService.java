package com.app.kokonut.adminRemove;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminRemoveService {

    private final AdminRemoveRepository adminRemoveRepository;

    @Autowired
    public AdminRemoveService(AdminRemoveRepository adminRemoveRepository){
        this.adminRemoveRepository = adminRemoveRepository;
    }

}
