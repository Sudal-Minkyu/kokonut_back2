package com.app.kokonut.category.categorydefault;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author Woody
 * Date : 2023-04-20
 * Time :
 * Remark :
 */
@Repository
public interface CategoryDefaultRepository extends JpaRepository<CategoryDefault, Long>, JpaSpecificationExecutor<CategoryDefault>, CategoryDefaultRepositoryCustom {

}