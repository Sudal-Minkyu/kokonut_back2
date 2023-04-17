package com.app.kokonut.refactor.addressBook;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressBookRepository extends JpaRepository<AddressBook, Long>, JpaSpecificationExecutor<AddressBook>, AddressBookRepositoryCustom {

}