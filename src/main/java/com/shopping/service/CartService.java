package com.shopping.service;

import com.shopping.domain.Cart;
import com.shopping.domain.CartItem;
import com.shopping.domain.Item;
import com.shopping.domain.Member;
import com.shopping.repository.CartItemRepository;
import com.shopping.repository.CartRepository;
import com.shopping.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;
    private final CartItemRepository cartItemRepository;

    // 회원 가입 하면 회원당 카트 하나 생성
    public void createCart(Member member) {
        Cart cart = Cart.createCart(member);
        cartRepository.save(cart);
    }

    // 장바구니 담기
    @Transactional
    public void addCart(Member member, Item newItem, int amount) {
        // 유저 ID 로 장바구니 찾기
        Cart cart = cartRepository.findCartByMemberId(member.getId());

        if (cart == null) {
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        Item item = itemRepository.findItemById(newItem.getId());
        CartItem cartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        // 상품이 장바구니에 없다면 카트 상품 생성 후 추가
        if (cartItem == null) {
            cartItem = CartItem.createCartItem(cart, item, amount);
            cartItemRepository.save(cartItem);
        } else {
            CartItem update = cartItem;
            update.setCart(cartItem.getCart());
            update.setItem(cartItem.getItem());
            update.addCount(amount);
            update.setCount(update.getCount());

            cartItemRepository.save(update);
        }

        // 카트 상품 총 개수 증가
        cartItem.setCount(cart.getCount() + amount);
    }

    // 유저 ID 로 해당 유저 장바구니 찾기
    public Cart findMemberCart(int memberId) {
        return cartRepository.findCartByMemberId(memberId);
    }

    // 해당 유저가 담은 상품만 반환
    // 유저의 카트 ID 와 카트 상품의 카트 ID 가 같아야 함
    public List<CartItem> allMemberCartView(Cart memberCart) {
        int memberCartId = memberCart.getId();

        // ID 에 해당하는 유저가 담은 상품들 넣어둘 곳
        List<CartItem> MemberCartItems = new ArrayList<>();

        // 유저 상관 없이 카트에 있는 상품 모두 불러오기
        List<CartItem> CartItems = cartItemRepository.findAll();

        for (CartItem cartItem : CartItems) {
            if (cartItem.getCart().getId() == memberCartId) {
                MemberCartItems.add(cartItem);
            }
        }
        return MemberCartItems;
    }

    // 카트 상품 리스트 중 해당하는 상품 ID 의 상품만 반환
    public List<CartItem> findCartItemByItemId(int id) {
        List<CartItem> cartItems = cartItemRepository.findCartItemByItemId(id);
        return cartItems;
    }

    // 카트 상품 리스트 중 해당하는 상품 ID 상품만 반환
    public CartItem findCartItemById(int id) {
        CartItem cartItem = cartItemRepository.findCartItemById(id);
        return cartItem;
    }

    // 장바구니 상품 개별 삭제
    public void cartItemDelete(int id) {
        cartItemRepository.deleteById(id);
    }

    // 장바구니 상품 전체 삭제
    public void allCartItemDelete(int id) {
        List<CartItem> cartItems = cartItemRepository.findAll();

        for (CartItem cartItem : cartItems) {
            if (cartItem.getCart().getMember().getId() == id) {
                cartItem.getCart().setCount(0);
                cartItemRepository.deleteById(cartItem.getId());
            }
        }
    }
}
