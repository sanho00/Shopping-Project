package com.shopping;

import com.shopping.constant.ItemSellStatus;
import com.shopping.domain.Item;
import com.shopping.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
}
