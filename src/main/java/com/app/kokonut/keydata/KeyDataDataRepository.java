package com.app.kokonut.keydata;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Woody
 * Date : 2023-01-04
 * Time :
 * Remark :
 */
@Repository
public interface KeyDataDataRepository extends JpaRepository<KeyData, String>, KeyDataRepositoryCustom {

}