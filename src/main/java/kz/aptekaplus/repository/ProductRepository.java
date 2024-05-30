package kz.aptekaplus.repository;

import kz.aptekaplus.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    List<Product> findByNameStartingWith(String temp);

    @Query("SELECT p FROM Product p WHERE p.subCategory.id = :subCategoryId")
    List<Product> findBySubCategoryId(@Param("subCategoryId") UUID subCategoryId);

    List<Product> findByNameContaining(String temp);
}
