package com.shopping.controller;

import com.shopping.domain.Item;
import com.shopping.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ShopController {

    private final ItemService itemService;

    // 메인
    @GetMapping("/")
    public String home(Model model) {
        List<Item> itemList = itemService.allItemView();
        model.addAttribute("itemList", itemList);
        return "/list";
    }

}
