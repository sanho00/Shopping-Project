package com.shopping.service;

import com.shopping.domain.Item;
import com.shopping.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;

    // 상품 등록
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    // 개별 상품 조회
    public Item itemView(Long itemId) {
        return itemRepository.findByItemId(itemId);
    }

    // 전체 상품 조회
    public List<Item> allItemView() {
        return itemRepository.findAll();
    }

    // 상품 수정
    public void itemModify(Item item, Long itemId) {
        Item modify = itemRepository.findByItemId(itemId);
        modify.setItemName(item.getItemName());
        modify.setItemDetail(item.getItemDetail());
        modify.setPrice(item.getPrice());
        modify.setItmeSellStatus(item.getItmeSellStatus());
        itemRepository.save(modify);
    }

    // 상품 삭제
    public void itemDelete(Long itemId) {
        itemRepository.deleteById(itemId);
    }
}
