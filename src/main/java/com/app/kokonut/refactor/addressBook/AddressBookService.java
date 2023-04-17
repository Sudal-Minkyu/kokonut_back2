package com.app.kokonut.refactor.addressBook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressBookService {

    private final AddressBookRepository addressBookRepository;

    @Autowired
    public AddressBookService(AddressBookRepository addressBookRepository){
        this.addressBookRepository = addressBookRepository;
    }

}
