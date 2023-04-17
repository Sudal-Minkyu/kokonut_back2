package com.app.kokonut.adminRemove;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRemoveRepository extends JpaRepository<AdminRemove, Long>, JpaSpecificationExecutor<AdminRemove>, AdminRemoveRepositoryCustom {

}