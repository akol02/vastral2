package com.sunbeam.services.impl;

import com.sunbeam.daos.DealRepository;
import com.sunbeam.daos.HomeCategoryRepository;
import com.sunbeam.entities.Deal;
import com.sunbeam.entities.HomeCategory;
import com.sunbeam.services.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DealServiceImpl implements DealService {
    private final DealRepository dealRepository;
    private final HomeCategoryRepository homeCategoryRepository;

    @Override
    public Deal createDeal(Deal deal) {
        HomeCategory category = homeCategoryRepository
                .findById(deal.getCategory().getId()).orElse(null);
        if (category == null) {
            throw new IllegalArgumentException("Category for the deal must exist.");
        }
        Deal newDeal = new Deal();
        newDeal.setCategory(category);
        newDeal.setDiscount(deal.getDiscount());
        return dealRepository.save(newDeal);
    }

    @Override
    public List<Deal> getDeals() {
        return dealRepository.findAll();
    }

    @Override
    public Deal updateDeal(Deal deal, Long id) throws Exception {
        Deal existingDeal = dealRepository.findById(id)
                .orElseThrow(() -> new Exception("Deal not found with id: " + id));
        
        if (deal.getCategory() != null && deal.getCategory().getId() != null) {
             HomeCategory category = homeCategoryRepository.findById(deal.getCategory().getId())
                .orElseThrow(() -> new Exception("Category not found"));
             existingDeal.setCategory(category);
        }

        if (deal.getDiscount() != null) {
            existingDeal.setDiscount(deal.getDiscount());
        }
        return dealRepository.save(existingDeal);
    }

    @Override
    public void deleteDeal(Long id) throws Exception {
        if (!dealRepository.existsById(id)) {
            throw new Exception("Deal not found with id: " + id);
        }
        dealRepository.deleteById(id);
    }
}