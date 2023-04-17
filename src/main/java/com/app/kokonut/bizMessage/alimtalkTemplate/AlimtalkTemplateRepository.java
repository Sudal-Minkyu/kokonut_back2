package com.app.kokonut.bizMessage.alimtalkTemplate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Repository
public interface AlimtalkTemplateRepository extends JpaRepository<AlimtalkTemplate, Long>, JpaSpecificationExecutor<AlimtalkTemplate>, AlimtalkTemplateRepositoryCustom {

    @Transactional
    @Modifying
    @Query("delete from AlimtalkTemplate a where a.kcChannelId = :kcChannelId")
    void findByAlimtalkTemplateDelete(String kcChannelId);

    @Query("select a from AlimtalkTemplate a where a.atTemplateCode = :atTemplateCode and a.kcChannelId = :kcChannelId")
    Optional<AlimtalkTemplate> findByAlimtalkTemplate(String atTemplateCode, String kcChannelId);

}