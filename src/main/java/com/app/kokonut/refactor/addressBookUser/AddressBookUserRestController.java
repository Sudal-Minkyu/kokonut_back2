package com.app.kokonut.refactor.addressBookUser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v2/api/AddressBookUser")
public class AddressBookUserRestController {

    private final AddressBookUserService addressBookUserService;

    @Autowired
    public AddressBookUserRestController(AddressBookUserService addressBookUserService){
        this.addressBookUserService = addressBookUserService;
    }


}
