package com.shopping.controller;

import com.shopping.config.auth.PrincipalDetails;
import com.shopping.domain.Item;
import com.shopping.domain.Member;
import com.shopping.dto.ItemDto;
import com.shopping.dto.ItemFormDto;
import com.shopping.service.ItemService;
import com.shopping.service.UserPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final UserPageService userPageService;

    // 메인
    @GetMapping("/")
    public String home(Model model) {
        List<Item> items = itemService.allItemView();
        model.addAttribute("items", items);
        return "/list";
    }

    @GetMapping("/list")
    public String mainPage(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails.getMember().getRole().equals("ROLE_SELLER")) {
            // 판매자
            List<Item> items = itemService.allItemView();
            model.addAttribute("items", items);
            model.addAttribute("member", principalDetails.getMember());

            return "/list";
        } else {
            // 구매자
            List<Item> items = itemService.allItemView();
            model.addAttribute("items", items);
            model.addAttribute("member", principalDetails.getMember());

            return "/list";
        }
    }


    // 상품 등록 페이지
    @GetMapping("/item/new")
    public String itemAddForm(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        if (principalDetails.getMember().getRole().equals("ROLE_SELLER")) {
            // 판매자
            model.addAttribute("member", principalDetails.getMember());
            return "/seller/addForm";
        } else {
            // 일반 회원
            return "redirect:/list";
        }
    }

    // 상품 등록
    @PostMapping("/item/new")
    public String itemAdd(ItemFormDto itemFormDto, Model model,
                          @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {
        if (principalDetails.getMember().getRole().equals("ROLE_SELLER")) {
            // 판매자
            itemFormDto.setSeller(principalDetails.getMember());
            itemService.saveItem(itemFormDto);

            return "/seller/addForm";
        } else {
            return "redirect:/list";
        }
    }

    // 상품 상세 페이지
    @GetMapping("/item/{itemId}")
    public String itemView(Model model, @PathVariable("itemId") Long itemId) {
        model.addAttribute("item", itemService.itemView(itemId));
        return "/item/itemView";
    }

    // 상품 수정 페이지
    @GetMapping("/item/modify/{itemId}")
    public String itemModifyForm(Model model, @PathVariable("itemId") Long itemId,
                                 @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails.getMember().getRole().equals("ROLE_SELLER")) {
            // 판매자
            Member member = itemService.itemView(itemId).getSeller();
            if (member.getId() == principalDetails.getMember().getId()) {
                model.addAttribute("item", itemService.itemView(itemId));
                model.addAttribute("member", principalDetails.getMember());
                return "/seller/itemModify";
            } else {
                return "redirect:/list";
            }
        } else {
            // 일반 회원
            return "redirect:/list";
        }
    }

    @PostMapping("/item/modify/{itemId}")
    public String itemModify(Item item, @PathVariable("itemId") Long itemId,
                             @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {
        if (principalDetails.getMember().getRole().equals("ROLE_SELLER")) {
            // 판매자
            Member member = itemService.itemView(itemId).getSeller();

            if (member.getId() == principalDetails.getMember().getId()) {
                itemService.itemModify(item, itemId);
                return "redirect:/list";
            } else {
                return "redirect:/list";
            }
        } else {
            return "redirect:/list";
        }
    }

    @GetMapping("/item/delete/{itemId}")
    public String itemDelete(@PathVariable("itemId") Long itemId) {
        itemService.itemDelete(itemId);
        return "/list";
    }
}
