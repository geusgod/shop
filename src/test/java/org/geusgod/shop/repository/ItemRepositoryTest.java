package org.geusgod.shop.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.geusgod.shop.entity.Item;
import org.geusgod.shop.entity.ItemSellStatus;
import org.geusgod.shop.entity.QItem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-db.properties")
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;


    @AfterEach
    public void clearData() {
        itemRepository.deleteAll();
    }

    public void createDataTest() {

        List<Item> arrayList = new ArrayList<>();

        IntStream.range(1, 10).forEach(i -> {
            String itemNm = "상품명"+ i;
            int price = 50000 + i;
            int stockNumber = 100;
            String itemDetail = "상품설명"+ i;
            ItemSellStatus itemSellStatus = ItemSellStatus.SELL;
            LocalDateTime regTime = LocalDateTime.now();

            arrayList.add(Item.builder()
                    .itemNm(itemNm)
                    .price(price)
                    .stockNumber(stockNumber)
                    .itemDetail(itemDetail)
                    .itemSellStatus(itemSellStatus)
                    .regTime(regTime)
                    .build());
        });

        IntStream.range(11, 20).forEach(i -> {
            String itemNm = "상품명"+ i;
            int price = 60000 + i;
            int stockNumber = 100;
            String itemDetail = "상품설명"+ i;
            ItemSellStatus itemSellStatus = ItemSellStatus.SOLD_OUT;
            LocalDateTime regTime = LocalDateTime.now();

            arrayList.add(Item.builder()
                    .itemNm(itemNm)
                    .price(price)
                    .stockNumber(stockNumber)
                    .itemDetail(itemDetail)
                    .itemSellStatus(itemSellStatus)
                    .regTime(regTime)
                    .build());
        });

        itemRepository.saveAll(arrayList);
    }

    @Test
    @DisplayName("Item Jpa 기본 테스트")
    public void itemJpaTest() {
        //given
        this.createDataTest();

        //when
        List<Item> itemList = itemRepository.findAll();

        //then
        Item item = itemList.get(0);

        assertThat(item.getItemNm()).isEqualTo("상품명1");
        assertThat(item.getPrice()).isEqualTo(50001);
    }


    @Test
    @DisplayName("Item 쿼리메소드 상품명 검색 테스트")
    public void itemJpaFindItemNmTest() {
        //given
        this.createDataTest();

        //when
        List<Item> itemList = itemRepository.findByItemNm("상품명1");

        //then
        Item item = itemList.get(0);

        assertThat(item.getItemNm()).isEqualTo("상품명1");
        assertThat(item.getPrice()).isEqualTo(50001);
    }

    @Test
    @DisplayName("Item 쿼리메소드 상품명+상품설명 검색 테스트")
    public void itemJpaFindItemNmOrItemDetailTest() {
        //given
        this.createDataTest();

        //when
        List<Item> itemList = itemRepository.findByItemNmOrItemDetail("상품명1", "상품설명3");

        //then
        Item item1 = itemList.get(0);
        assertThat(item1.getItemNm()).isEqualTo("상품명1");

        Item item3 = itemList.get(1);
        assertThat(item3.getItemDetail()).isEqualTo("상품설명3");
    }

    @Test
    @DisplayName("Item 쿼리메소드 가격 검색 테스트")
    public void itemJpaFindPriceLessThanTest() {
        //given
        this.createDataTest();

        //when
        List<Item> itemList = itemRepository.findByPriceLessThan(50002);

        //then
        Item item1 = itemList.get(0);
        assertThat(item1.getPrice()).isEqualTo(50001);

    }

    @Test
    @DisplayName("Item 쿼리메소드 가격 검색 및 정렬 테스트")
    public void itemJpaFindPriceLessThanOrderByPriceDescTest() {
        //given
        this.createDataTest();

        //when
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(50004);

        //then
        Item item1 = itemList.get(0);
        assertThat(item1.getItemNm()).isEqualTo("상품명3");

    }


    @Test
    @DisplayName("Item @Query 어노테이션 상품 설명 검색 테스트")
    public void itemJpaFindByItemDetailByQueryTest() {
        //given
        this.createDataTest();

        //when
        List<Item> itemList = itemRepository.findByItemDetailByQuery("상품설명4");

        //then
        Item item1 = itemList.get(0);
        assertThat(item1.getItemNm()).isEqualTo("상품명4");

    }

    @Test
    @DisplayName("Item @Query 어노테이션 상품 설명 검색 테스트")
    public void itemJpaFindByItemDetailByNativeTest() {
        //given
        this.createDataTest();

        //when
        List<Item> itemList = itemRepository.findByItemDetailByNative("상품설명4");

        //then
        Item item1 = itemList.get(0);
        assertThat(item1.getItemNm()).isEqualTo("상품명4");

    }


    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("Querydsl 조회 테스트")
    public void queryDslTest() {

        this.createDataTest();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qItem = QItem.item;
        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%" + "상품설명" + "%"))
                .orderBy(qItem.price.desc());

        List<Item> itemList = query.fetch();
        for(Item item : itemList) {
            System.out.println(item.toString());
        }

    }

    @Test
    @DisplayName("상품 Querydsl 조회 테스트 2")
    public void queryDslTest2(){

        this.createDataTest();

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QItem item = QItem.item;
        String itemDetail = "상품설명";
        int price = 10003;
        String itemSellStat = "SELL";

        booleanBuilder.and(item.itemDetail.like("%" + itemDetail + "%"));
        booleanBuilder.and(item.price.gt(price));
        System.out.println(ItemSellStatus.SELL);
        if(StringUtils.equals(itemSellStat, ItemSellStatus.SELL)){
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        Pageable pageable = PageRequest.of(0, 5);
        Page<Item> itemPagingResult = itemRepository.findAll(booleanBuilder, pageable);
        System.out.println("total elements : " + itemPagingResult. getTotalElements ());

        List<Item> resultItemList = itemPagingResult.getContent();
        for(Item resultItem: resultItemList){
            System.out.println(resultItem.toString());
        }
    }

}