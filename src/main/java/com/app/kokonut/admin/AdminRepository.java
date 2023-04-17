package com.app.kokonut.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long>, JpaSpecificationExecutor<Admin>, AdminRepositoryCustom {

    Optional<Admin> findByKnEmail(String knEmail);

    boolean existsByKnEmail(String knEmail);

    boolean existsByKnPhoneNumber(String knPhoneNumber);

    boolean existsByKnEmailAuthNumber(String knEmailAuthNumber);

    Optional<Admin> findAdminByKnNameAndKnPhoneNumber(String knName, String knPhoneNumber);
}