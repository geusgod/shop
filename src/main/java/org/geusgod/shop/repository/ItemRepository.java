package org.geusgod.shop.repository;

import org.geusgod.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item> {

    List<Item> findByItemNm(String itemNm);

    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);

    List<Item> findByPriceLessThan(Integer price);

    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    @Query("SELECT i FROM Item i WHERE i.itemDetail LIKE %:itemDetail% ORDER BY i.price DESC")
    List<Item> findByItemDetailByQuery(@Param("itemDetail") String itemDetail);

    @Query(value = "SELECT * FROM shop_item i WHERE i.item_detail LIKE %:itemDetail% ORDER BY i.price DESC", nativeQuery = true)
    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);
}
