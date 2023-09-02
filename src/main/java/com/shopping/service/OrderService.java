package com.shopping.service;

import com.shopping.domain.*;
import com.shopping.repository.OrderItemRepository;
import com.shopping.repository.OrderRepository;
import com.shopping.repository.SaleItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final SaleItemRepository saleItemRepository;
    private final UserPageService userPageService;
    private final ItemService itemService;

    // 회원 가입 하면 회원 당 주문 1개 생성
    public void createOrder(Member member) {
        Order order = Order.createOrder(member);
        orderRepository.save(order);
    }

    // id 에 해당하는 주문 아이템 찾음
    public List<OrderItem> findMemberOrderItems(int memberId) {
        return orderItemRepository.findOrderItemByMemberId(memberId);
    }

    // OrderItem 1개 찾기
    public OrderItem findOrderItem(int orderItemId) {
        return orderItemRepository.findOrderItemById(orderItemId);
    }

    // 장바구니 상품 주문
    @Transactional
    public OrderItem addCartOrder(int itemId, int memberId, CartItem cartItem, SaleItem saleItem) {
        Member member = userPageService.findMember(memberId);
        OrderItem orderItem = OrderItem.createOrderItem(itemId, member, cartItem, saleItem);
        orderItemRepository.save(orderItem);
        return orderItem;
    }

    // 주문하면 order 만들기
    @Transactional
    public void addOrder(Member member, List<OrderItem> orderItemList) {
        Order memberOrder = Order.createOrder(member, orderItemList);
        orderRepository.save(memberOrder);
    }

    // 단일 상품 주문
    @Transactional
    public void addOneItemOrder(int memberId, Item item, int count, SaleItem saleItem) {
        Member member = userPageService.findMember(memberId);
        Order memberOrder = Order.createOrder(member);
        OrderItem orderItem = OrderItem.createOrderItem(item.getId(), member, item, count, memberOrder, saleItem);
        orderItemRepository.save(orderItem);
        orderRepository.save(memberOrder);
    }

    // 주문 취소 가능
    @Transactional
    public void orderCancle(Member member, OrderItem cancleItem) {
        Item item = itemService.itemView(cancleItem.getItemId());

        // 판매자의 판매내역 totalCount 감소
        cancleItem.getSaleItem().getSale().setTotalCount(cancleItem.getSaleItem().getSale().getTotalCount() - cancleItem.getItemCount());

        // 해당 item 재고 다시 증가
        item.setStock(item.getStock() + cancleItem.getItemCount());

        // 해당 item 판매량 감소
        item.setCount(item.getCount() - cancleItem.getItemCount());

        // 해당 orderItem 주문 상태 1로 변경
        cancleItem.setIsCancle(cancleItem.getIsCancle() + 1);

        // saleItem 찾아서 상태 1로 변경
        cancleItem.getSaleItem().setIsCancle(cancleItem.getSaleItem().getIsCancle() + 1);

        orderItemRepository.save(cancleItem);
        saleItemRepository.save(cancleItem.getSaleItem());
    }
}
