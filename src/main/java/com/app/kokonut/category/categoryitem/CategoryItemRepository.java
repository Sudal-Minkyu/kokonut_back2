package com.app.kokonut.category.categoryitem;

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
public interface CategoryItemRepository extends JpaRepository<CategoryItem, Long>, JpaSpecificationExecutor<CategoryItem>, CategoryItemRepositoryCustom {
    boolean existsByCddName(String cddName);
}