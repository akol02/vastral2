package com.sunbeam.services;

import java.util.List;
import com.sunbeam.entities.Deal;

public interface DealService {
    Deal createDeal(Deal deal);
    List<Deal> getDeals();
    Deal updateDeal(Deal deal, Long id) throws Exception;
    void deleteDeal(Long id) throws Exception;
}