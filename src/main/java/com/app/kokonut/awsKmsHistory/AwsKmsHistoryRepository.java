package com.app.kokonut.awsKmsHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AwsKmsHistoryRepository extends JpaRepository<AwsKmsHistory, Long>, JpaSpecificationExecutor<AwsKmsHistory> {

}