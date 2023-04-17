package com.app.kokonut.refactor.addressBook;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v2/api/AddressBook")
public class AddressBookRestController {

    private final AddressBookService addressBookService;

    @Autowired
    public AddressBookRestController(AddressBookService addressBookService){
        this.addressBookService = addressBookService;
    }





}
