package org.eternity.food.service.shop;

import org.eternity.food.domain.shop.Menu;
import org.eternity.food.domain.shop.MenuRepository;
import org.eternity.food.domain.shop.ShopRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShopService {
    private ShopRepository shopRepository;
    private MenuRepository menuRepository;

    public ShopService(ShopRepository shopRepository, MenuRepository menuRepository) {
        this.shopRepository = shopRepository;
        this.menuRepository = menuRepository;
    }

    @Transactional(readOnly = true)
    public void getMenuBoard(Long shopId) {
//        Shop shop = shopRepository.findById(shopId).get();
        Menu menu = menuRepository.findById(1L).get();
        System.out.println(menu.getName());
//        System.out.println(menu.getName());
//        menu.getGroups().forEach(g -> System.out.println(g.getOptions().get(0).getName()));
    }
}
