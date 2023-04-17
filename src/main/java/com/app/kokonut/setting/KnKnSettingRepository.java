package com.app.kokonut.setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author Joy
 * Date : 2023-01-05
 * Time :
 * Remark : SettingRepository 관리자 환경설정
 */
@Repository
public interface KnKnSettingRepository extends JpaRepository<KnSetting, Long>, JpaSpecificationExecutor<KnSetting>, KnSettingRepositoryCustom {

}