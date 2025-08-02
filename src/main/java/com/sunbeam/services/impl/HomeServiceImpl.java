package com.sunbeam.services.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.sunbeam.daos.DealRepository;
import com.sunbeam.entities.Deal;
import com.sunbeam.entities.Home;
import com.sunbeam.entities.HomeCategory;
import com.sunbeam.models.HomeCategorySection;
import com.sunbeam.services.HomeService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class HomeServiceImpl implements HomeService {

    private final DealRepository dealRepository;

    @Override
    public Home creatHomePageData(List<HomeCategory> allCategories) {
        Home home = new Home();
        home.setGrid(filterBySection(allCategories, HomeCategorySection.GRID));
        home.setShopByCategories(filterBySection(allCategories, HomeCategorySection.SHOP_BY_CATEGORIES));
        home.setElectricCategories(filterBySection(allCategories, HomeCategorySection.ELECTRIC_CATEGORIES));
        home.setDealCategories(filterBySection(allCategories, HomeCategorySection.DEALS));
        home.setDeals(dealRepository.findAll());
        return home;
    }
    
    private List<HomeCategory> filterBySection(List<HomeCategory> categories, HomeCategorySection section) {
        return categories.stream()
                .filter(category -> section.equals(category.getSection()))
                .collect(Collectors.toList());
    }
}