package com.shopping.service;

import com.shopping.domain.Item;
import com.shopping.domain.UploadFile;
import com.shopping.dto.ItemFormDto;
import com.shopping.file.FileStore;
import com.shopping.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final FileStore fileStore;

    // 상품 등록
    public void saveItem(@ModelAttribute ItemFormDto formDto) throws IOException {
        // 스토리지에 파일 저장
        UploadFile attachFile = fileStore.storeFile(formDto.getAttachFile());
//        List<UploadFile> storeImgFiles = fileStore.storeFile(formDto.getImageFiles());

        // DB에 저장
        Item item = new Item();
        item.setItemName(formDto.getItemName());
        item.setItemDetail(formDto.getItemDetail());
        item.setQuantity(formDto.getQuantity());
        item.setPrice(formDto.getPrice());
        item.setAttachFile(attachFile);
        //item.setImageFiles(storeImgFiles);
        item.setItemSellStatus(formDto.getItemSellStatus());

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
    public void itemModify(Item item, Long itemId) throws IOException {
        UploadFile attachFile = fileStore.storeFile((MultipartFile) item.getAttachFile());
        //List<UploadFile> storeImgFiles = fileStore.storeFiles(item.getImageFiles());

        Item modify = itemRepository.findByItemId(itemId);
        modify.setItemName(item.getItemName());
        modify.setItemDetail(item.getItemDetail());
        modify.setPrice(item.getPrice());
        modify.setItemSellStatus(item.getItemSellStatus());
        itemRepository.save(modify);
    }

    // 상품 삭제
    public void itemDelete(Long itemId) {
        itemRepository.deleteById(itemId);
    }
}
