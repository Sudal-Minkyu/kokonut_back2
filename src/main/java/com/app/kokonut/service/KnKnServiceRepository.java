package com.app.kokonut.service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Joy
 * Date : 2023-01-09
 * Time :
 * Remark : ServiceRepository
 */
public interface KnKnServiceRepository extends JpaRepository<KnService, Long>, JpaSpecificationExecutor<KnService>, KnServiceRepositoryCustom {

}