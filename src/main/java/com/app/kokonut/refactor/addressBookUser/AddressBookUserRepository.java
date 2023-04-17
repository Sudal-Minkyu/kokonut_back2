package com.app.kokonut.refactor.addressBookUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressBookUserRepository extends JpaRepository<AddressBookUser, Long>, JpaSpecificationExecutor<AddressBookUser>, AddressBookUserRepositoryCustom {

}