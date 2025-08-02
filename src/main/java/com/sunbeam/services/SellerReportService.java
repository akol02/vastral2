package com.sunbeam.services;

import com.sunbeam.entities.Seller;
import com.sunbeam.entities.SellerReport;

public interface SellerReportService {
    SellerReport getSellerReport(Seller seller);
    SellerReport updateSellerReport(SellerReport sellerReport);
}