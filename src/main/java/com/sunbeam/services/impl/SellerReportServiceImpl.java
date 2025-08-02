package com.sunbeam.services.impl;

import org.springframework.stereotype.Service;
import com.sunbeam.daos.SellerReportRepository;
import com.sunbeam.entities.Seller;
import com.sunbeam.entities.SellerReport;
import com.sunbeam.services.SellerReportService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellerReportServiceImpl implements SellerReportService {

    private final SellerReportRepository sellerReportRepository;

    @Override
    public SellerReport getSellerReport(Seller seller) {
        SellerReport report = sellerReportRepository.findBySellerId(seller.getId());
        if (report == null) {
            report = new SellerReport();
            report.setSeller(seller);
            return sellerReportRepository.save(report);
        }
        return report;
    }

    @Override
    public SellerReport updateSellerReport(SellerReport sellerReport) {
        return sellerReportRepository.save(sellerReport);
    }
}