package com.app.kokonut.awskmshistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AwsKmsHistoryRepository extends JpaRepository<AwsKmsHistory, Long>, JpaSpecificationExecutor<AwsKmsHistory>, AwsKmsHistoryRepositoryCustom {

    Optional<AwsKmsHistory> findAwsKmsHistoryByCpCodeAndAkhYyyymm(String cpCode, String akhYyyymm);

}