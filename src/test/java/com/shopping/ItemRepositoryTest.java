package com.shopping;

import com.shopping.constant.ItemSellStatus;
import com.shopping.domain.Item;
import com.shopping.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    void createItemTest() {
            Item item = new Item();

            item.setItemName("테스트 상품");
            item.setPrice(10000);
            item.setItemDetail("테스트 상품 상세 설명");
            item.setItmeSellStatus(ItemSellStatus.SELL);

            Item savedItem = itemRepository.save(item);
            System.out.println(savedItem.toString());
    }


    void createItemList() {
        for (int i=1; i<=10; i++) {
            Item item = new Item();
            item.setItemName("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);

            Item savedItem = itemRepository.save(item);
            System.out.println(savedItem.toString());
        }
    }

    @Test
    void findByItemNameTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemName("테스트 상품");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

//    @Test
//    void findByItemDetailTest() {
//        this.createItemList();
//        List<Item> itemList = this.itemRepository.findByItemDetail("테스트 상품 상세 설명");
//        for (Item item : itemList) {
//            System.out.println(item.toString());
//        }
//    }
}
