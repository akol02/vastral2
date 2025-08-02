package com.sunbeam.services;

import java.util.List;
import com.sunbeam.entities.Home;
import com.sunbeam.entities.HomeCategory;

public interface HomeService {
    Home creatHomePageData(List<HomeCategory> categories);
}