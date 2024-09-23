package com.project.bakerymanagementsystem.repository;

import com.project.bakerymanagementsystem.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByProduct_Name(String productName);
    List<Item> findAllByProduct_ProductId(long id);


}
