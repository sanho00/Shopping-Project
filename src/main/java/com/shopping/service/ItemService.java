package com.shopping.service;

import com.shopping.domain.Item;
import com.shopping.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;

    // 상품 등록
    public void saveItem(Item item, MultipartFile imgFile) throws IOException {

        String oriImgName = imgFile.getOriginalFilename();
        String imgName = "";

        String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files/";

        // UUID 이용하여 파일명 새로 생성
        UUID uuid = UUID.randomUUID();

        String savedFileName = uuid + "_" + oriImgName; // 파일명에서 imgName 으로

        imgName = savedFileName;

        File saveFile = new File(projectPath, imgName);
        imgFile.transferTo(saveFile);

        item.setImgName(imgName);
        item.setImgPath("/files/" + imgName);

        itemRepository.save(item);
    }

    // 개별 상품 조회
    public Item itemView(Integer id) {
        return itemRepository.findItemById(id);
    };

    // 전체 상품 조회
    public List<Item> allItemView() {
        return itemRepository.findAll();
    }

    // 상품 수정
    @Transactional
    public void itemModify(Item item, Integer id, MultipartFile imgFile) throws IOException {

        String projectPath = System.getProperty("users.dir") + "/src/main/resources/static/files/";
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + imgFile.getOriginalFilename();
        File saveFile = new File(projectPath, fileName);
        imgFile.transferTo(saveFile);

        Item modify = itemRepository.findItemById(id);
        modify.setItemName(item.getItemName());
        modify.setItemDetail(item.getItemDetail());
        modify.setPrice(item.getPrice());
        modify.setItemSellStatus(item.getItemSellStatus());
        modify.setImgName(fileName);
        modify.setImgPath("/files/" + fileName);
        itemRepository.save(modify);
    }

    // 상품 삭제
    public void itemDelete(Integer id) {
        itemRepository.deleteById(id);
    }
}
