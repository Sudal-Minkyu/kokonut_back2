package com.app.kokonut.refactor.addressBookUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressBookUserService {

    private final AddressBookUserRepository addressBookUserRepository;

    @Autowired
    public AddressBookUserService(AddressBookUserRepository addressBookUserRepository){
        this.addressBookUserRepository = addressBookUserRepository;
    }

}
