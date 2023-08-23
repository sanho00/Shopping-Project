package com.shopping.controller;

import com.shopping.config.auth.PrincipalDetails;
import com.shopping.domain.Cart;
import com.shopping.domain.CartItem;
import com.shopping.domain.Item;
import com.shopping.domain.Member;
import com.shopping.service.CartService;
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
    private final CartService cartService;

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
            int sellerId = principalDetails.getMember().getId();
            List<Item> items = itemService.allItemView();
            model.addAttribute("items", items);
            model.addAttribute("member", userPageService.findMember(sellerId));

            return "/list";
        } else {
            // 구매자
            int userId = principalDetails.getMember().getId();
            List<Item> items = itemService.allItemView();
            model.addAttribute("items", items);
            model.addAttribute("member", userPageService.findMember(userId));

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
    public String itemAdd(Item item, MultipartFile imgFile,
                          @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {
        if (principalDetails.getMember().getRole().equals("ROLE_SELLER")) {
            // 판매자
            item.setSeller(principalDetails.getMember());
            itemService.saveItem(item, imgFile);

            return "/seller/addForm";
        } else {
            return "redirect:/list";
        }
    }

    // 상품 상세 페이지
    @GetMapping("/item/view/{itemId}")
    public String itemView(Model model, @PathVariable("id") Integer id, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        if (principalDetails.getMember().getRole().equals("ROLE_SELLER")) {
            // 판매자
            Member member = principalDetails.getMember();

            model.addAttribute("item", itemService.itemView(id));
            model.addAttribute("member", member);

            return "/item/itemView";
        } else {
            // 구매자
            Member member = principalDetails.getMember();

            // 페이지에 접속한 유저
            Member loginMember = userPageService.findMember(member.getId());

            int cartCount = 0;
            Cart memberCart = cartService.findMemberCart(loginMember.getId());
            List<CartItem> cartItems = cartService.allMemberCartView(memberCart);

            for (CartItem cartItem : cartItems) {
                cartCount += cartItem.getCount();
            }

            model.addAttribute("cartCount", cartCount);
            model.addAttribute("item", itemService.itemView(id));
            model.addAttribute("member", member);

            return "/item/itemView";
        }
    }

    @GetMapping("/item/view/nonlogin/{id}")
    public String nonLoginItemView(Model model, @PathVariable("id") Integer id) {
        // 로그인 안 한 유저
        model.addAttribute("item", itemService.itemView(id));
        return "/item/itemView";
    }

    // 상품 수정 페이지
    @GetMapping("/item/modify/{itemId}")
    public String itemModifyForm(Model model, @PathVariable("id") Integer id,
                                 @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails.getMember().getRole().equals("ROLE_SELLER")) {
            // 판매자
            Member member = itemService.itemView(id).getSeller();
            if (member.getId() == principalDetails.getMember().getId()) {
                model.addAttribute("item", itemService.itemView(id));
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
    public String itemModify(Item item, @PathVariable("id") Integer id,
                             @AuthenticationPrincipal PrincipalDetails principalDetails, MultipartFile imgFile) throws IOException {
        if (principalDetails.getMember().getRole().equals("ROLE_SELLER")) {
            // 판매자
            Member member = itemService.itemView(id).getSeller();

            if (member.getId() == principalDetails.getMember().getId()) {
                itemService.itemModify(item, id, imgFile);
                return "redirect:/list";
            } else {
                return "redirect:/list";
            }
        } else {
            return "redirect:/list";
        }
    }

    @GetMapping("/item/delete/{itemId}")
    public String itemDelete(@PathVariable("id") Integer id) {
        itemService.itemDelete(id);
        return "/list";
    }
}
