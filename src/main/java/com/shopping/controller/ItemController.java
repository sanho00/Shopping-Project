package com.shopping.controller;

import com.shopping.domain.Item;
import com.shopping.dto.ItemDto;
import com.shopping.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    // 상품 등록 페이지
    @GetMapping("/admin/new")
    public String itemAddForm() {
        return "/item/addForm";
    }

    // 상품 등록
    @PostMapping("/admin/new")
    public String itemAdd(Item item) {
        itemService.saveItem(item);
        return "/list";
    }

    // 상품 상세 페이지
    @GetMapping("/item/{itemId}")
    public String itemView(Model model, @PathVariable("itemId") Long itemId) {
        model.addAttribute("item", itemService.itemView(itemId));
        return "/item/itemView";
    }

    // 상품 수정 페이지
    @GetMapping("/admin/modify/{itemId}")
    public String itemModifyForm(Model model, @PathVariable("itemId") Long itemId) {
        model.addAttribute("item", itemService.itemView(itemId));
        return "/item/itemModify";
    }

    @PostMapping("/admin/modify/{itemId}")
    public String itemModify(Item item, @PathVariable("itemId") Long itemId) {
        itemService.itemModify(item, itemId);
        return "redirect:/item/{itemId}";
    }

    @GetMapping("/admin/{itemId}/delete")
    public String itemDelete(@PathVariable("itemId") Long itemId) {
        itemService.itemDelete(itemId);
        return "/list";
    }
}
