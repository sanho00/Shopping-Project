package com.shopping.service;

import com.shopping.domain.*;
import com.shopping.repository.ItemRepository;
import com.shopping.repository.SaleItemRepository;
import com.shopping.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;
    private final SaleItemRepository saleItemRepository;
    private final ItemRepository itemRepository;
    private final UserPageService userPageService;

    // 회원가입 하면 판매자 당 판매 내역 1개 생성
    public void createSale(Member member) {
        Sale sale = Sale.createSale(member);
        saleRepository.save(sale);
    }

    // id 에 해당하는 판매 아이템 찾기
    public List<SaleItem> findSellerSaleItems(int sellerId) {
        return saleItemRepository.findSaleItemBySellerId(sellerId);
    }

    // 판매자 id 에 해당하는 sale 찾기
    public Sale findSaleById(int sellerId) {
        return saleRepository.findBySellerId(sellerId);
    }

    // 판매 내역에 저장 (장바구니 전체 주문)
    @Transactional
    public SaleItem addSale(int itemId, int sellerId, CartItem cartItem) {
        Member seller = userPageService.findMember(sellerId);
        Sale sale = saleRepository.findBySellerId(sellerId);

        sale.setTotalCount(sale.getTotalCount() + cartItem.getCount());
        saleRepository.save(sale);

        SaleItem saleItem = SaleItem.createSaleItem(itemId, sale, seller, cartItem);
        saleItemRepository.save(saleItem);

        return saleItem;
    }

    // 판매 내역에 저장 (상품 개별 주문)
    @Transactional
    public SaleItem addSale(int sellerId, Item item, int count) {
        Member seller = userPageService.findMember(sellerId);
        Sale sale = saleRepository.findBySellerId(sellerId);

        sale.setTotalCount(sale.getTotalCount() + count);
        saleRepository.save(sale);

        SaleItem saleItem = SaleItem.createSaleItem(item.getId(), sale, seller, item, count);
        saleItemRepository.save(saleItem);

        return saleItem;
    }
}
