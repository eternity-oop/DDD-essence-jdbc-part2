package org.eternity.food.service.shop;

import org.eternity.food.domain.shop.Menu;
import org.eternity.food.domain.shop.MenuRepository;
import org.eternity.food.domain.shop.Option;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Transactional
    public void changeOptionGroupName(Long menuId, Long optionGroupId, String name) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new IllegalArgumentException());
        menu.changeOptionGroupName(optionGroupId, name);
        menuRepository.save(menu);
    }

    @Transactional
    public void changeOptionName(Long menuId, Long optionGroupId, Option target, String optionName) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new IllegalArgumentException());
        menu.changeOptionName(optionGroupId, target, optionName);
        menuRepository.save(menu);
    }
}
