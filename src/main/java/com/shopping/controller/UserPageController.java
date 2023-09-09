package com.shopping.controller;

import com.shopping.config.auth.PrincipalDetails;
import com.shopping.domain.*;
import com.shopping.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class UserPageController {

    private final UserPageService userPageService;
    private final CartService cartService;
    private final ItemService itemService;
    private final OrderService orderService;
    private final SaleService saleService;

    // 유저 페이지 접속
    @GetMapping("/member/{id}")
    public String userPage(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails.getMember().getId() == id) {
            model.addAttribute("member", userPageService.findMember(id));
            return "/user/userPage";
        } else {
            return "redirect:/list";
        }
    }

    // 회원 정보 수정
    @GetMapping("/member/modify/{id}")
    public String userModify(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails.getMember().getId() == id) {
            model.addAttribute("member", userPageService.findMember(id));
            return "/userModify";
        } else {
            return "redirect:/list";
        }
    }

    // 회원 정보 수정 (POST)
    @PostMapping("/member/modify/{id}")
    public String userUpdate(@PathVariable("id") Integer id, Member member) {
        userPageService.memberModify(member);
        return "redirct:/member/{id}";
    }

    // 장바구니 접속
    @GetMapping("/member/cart/{id}")
    public String userCartPage(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails.getMember().getId() == id) {
            Member member = userPageService.findMember(id);
            Cart memberCart = member.getCart();

            // 장바구니의 아이템 모두 가져옴
            List<CartItem> cartItemList = cartService.allMemberCartView(memberCart);

            // 장바구니 상품들 총 가격
            int totalPrice = 0;
            for (CartItem cartItem : cartItemList) {
                totalPrice += cartItem.getCount() * cartItem.getItem().getPrice();
            }

            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("totalCount", memberCart.getCount());
            model.addAttribute("cartItems",  cartItemList);
            model.addAttribute("member", userPageService.findMember(id));

            return "/user/cart";
        } else {
            return "redirect:/list";
        }
    }

    // 장바구니에 물건 넣기
    @PostMapping("/member/cart/{id}/{itemId}")
    public String addCartItem(@PathVariable("id") Integer id, @PathVariable("itemId") Integer itemId, int amount) {
        Member member = userPageService.findMember(id);
        Item item = itemService.itemView(itemId);
        cartService.addCart(member, item, amount);

        return "redirect:/item/view/{itemId}";
    }

    // 장바구니 물건 삭제
    @GetMapping("/member/cart/{id}/{cartItemId}/delete")
    public String deleteCartItem(@PathVariable("id") Integer id, @PathVariable("cartItemId") Integer itemId,
                                 Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails.getMember().getId() == id) {
            CartItem cartItem = cartService.findCartItemById(itemId);

            // 해당 유저 장바구니 찾기
            Cart memberCart = cartService.findMemberCart(id);

            // 장바구니 전체 수량 감소
            memberCart.setCount(memberCart.getCount() - cartItem.getCount());

            // 장바구니 물건 삭제
            cartService.cartItemDelete(itemId);

            List<CartItem> cartItemList = cartService.allMemberCartView(memberCart);

            int totalPrice = 0;
            for (CartItem cartItems : cartItemList) {
                totalPrice += cartItems.getCount() * cartItems.getItem().getPrice();
            }

            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("totalCount", memberCart.getCount());
            model.addAttribute("cartItems", cartItemList);
            model.addAttribute("member", userPageService.findMember(id));

            return "/user/cart";
        } else {
            return "redirect:/list";
        }
    }

    // 주문 내역 조회
    @GetMapping("/member/orderList/{id}")
    public String orderList(@PathVariable("id") Integer id, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        if (principalDetails.getMember().getId() == id) {
            List<OrderItem> orderItemList = orderService.findMemberOrderItems(id);

            // 총 주문 개수
            int totalCount = 0;
            for (OrderItem orderItem : orderItemList) {
                if (orderItem.getIsCancle() != 1) {
                    totalCount += orderItem.getItemCount();
                }
            }

                model.addAttribute("totalCount", totalCount);
                model.addAttribute("orderItems", orderItemList);
                model.addAttribute("member", userPageService.findMember(id));

                return "user/userOrderList";
        } else {
            return "redirect:/list";
        }
    }

    // 장바구니 상품 전체 주문
    @Transactional
    @PostMapping("/member/cart/checkout/{id}")
    public String cartCheckout(@PathVariable("id") Integer id, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        if (principalDetails.getMember().getId() == id) {
            Member member = userPageService.findMember(id);
            Cart memberCart = cartService.findMemberCart(member.getId());
            List<CartItem> memberCartItems = cartService.allMemberCartView(memberCart);

            // 최종 결제 금액
            int totalPrice = 0;
            for (CartItem cartItem : memberCartItems) {
                // 장바구니 안에 있는 상품의 재고가 없거나 재고보다 많이 주문할 경우
                if (cartItem.getItem().getStock() == 0 || cartItem.getItem().getStock() < cartItem.getCount()) {
                    return "redirect:/list";
                }
                totalPrice += cartItem.getCount() * cartItem.getItem().getPrice();
            }

            List<OrderItem> orderItemList = new ArrayList<>();

            for (CartItem cartItem : memberCartItems) {
                // 각 상품에 대한 판매자
                Member seller = cartItem.getItem().getSeller();

                // 재고 감소
                cartItem.getItem().setStock(cartItem.getItem().getStock() - cartItem.getCount());

                // 상품 개별로 판매 개수 증가
                cartItem.getItem().setCount(cartItem.getItem().getCount() + cartItem.getCount());

                SaleItem saleItem = saleService.addSale(cartItem.getItem().getId(), seller.getId(), cartItem);
                OrderItem orderItem = orderService.addCartOrder(cartItem.getItem().getId(), member.getId(), cartItem, saleItem);

                orderItemList.add(orderItem);
            }
            orderService.addOrder(member, orderItemList);
            cartService.allCartItemDelete(id);

            model.addAttribute("cartItems", memberCartItems);
            model.addAttribute("member", userPageService.findMember(id));

            return "redirect:/member/cart/{id}";
        } else {
            return "redirect:/list";
        }
    }

    // 상품 개별 주문
    @Transactional
    @PostMapping("/member/{id}/checkout/{itemId}")
    public String checkout(@PathVariable("id") Integer id, @PathVariable("itemid") Integer itemId,
                           @AuthenticationPrincipal PrincipalDetails principalDetails, Model model, int count) {
        if (principalDetails.getMember().getId() == id) {
            Member member = userPageService.findMember(id);
            Item item = itemService.itemView(itemId);

            if (item.getStock() == 0 || item.getStock() < count) {
                return "redirect:/list";
            }

            item.setStock(item.getStock() - count);
            item.setCount(item.getCount() + count);

            SaleItem saleItem = saleService.addSale(item.getSeller().getId(), item, count);
            orderService.addOneItemOrder(member.getId(), item, count, saleItem);

            return "redirect:/member/orderList/{id}";
        } else {
            return "redirect:/list";
        }
    }

    @PostMapping("/member/{id}/checkout/cancel/{orderItemId}")
    public String cancelOrder(@PathVariable("id") Integer id, @PathVariable("orderItemId") Integer orderItemId,
                              Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails.getMember().getId() == id) {
            OrderItem cancelItem = orderService.findOrderItem(orderItemId); // 취소할 상품 찾기
            Member member = userPageService.findMember(id); // 취소하는 유저 찾기

            // 주문 내역 총 개수에서 취소 상품 개수 줄어듦
            List<OrderItem> orderItemList = orderService.findMemberOrderItems(id);
            int totalCount = 0;
            for (OrderItem orderItem : orderItemList) {
                totalCount += orderItem.getItemCount();
            }
            totalCount = totalCount - cancelItem.getItemCount();

            orderService.orderCancle(member, cancelItem);

            model.addAttribute("totalCount", totalCount);
            model.addAttribute("orderItems", orderItemList);
            model.addAttribute("member", member);

            return "redirect:/member/orderList/{id}";
        } else {
            return "redirect:/list";
        }
    }

}
