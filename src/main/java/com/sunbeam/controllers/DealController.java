// package com.sunbeam.controllers;

// import com.sunbeam.entities.Deal;
// import com.sunbeam.response.ApiResponse;
// import com.sunbeam.services.DealService;
// import lombok.RequiredArgsConstructor;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
// import java.util.List;

// @RestController
// @RequiredArgsConstructor
// @RequestMapping("/admin/deals")
// public class DealController {

//     private final DealService dealService;

//     @PostMapping
//     public ResponseEntity<Deal> createDeals(@RequestBody Deal deals) {
//         Deal createdDeals = dealService.createDeal(deals);
//         return new ResponseEntity<>(createdDeals, HttpStatus.ACCEPTED);
//     }

//     @GetMapping
//     public ResponseEntity<List<Deal>> getDeals() {
//         List<Deal> deals = dealService.getDeals();
//         return new ResponseEntity<>(deals, HttpStatus.ACCEPTED);
//     }

//     @PatchMapping("/{id}")
//     public ResponseEntity<Deal> updateDeal(@PathVariable Long id, @RequestBody Deal deal) throws Exception {
//         Deal updatedDeal = dealService.updateDeal(deal, id);
//         return ResponseEntity.ok(updatedDeal);
//     }

//     @DeleteMapping("/{id}")
//     public ResponseEntity<ApiResponse> deleteDeals(@PathVariable Long id) throws Exception {
//         dealService.deleteDeal(id);
//         ApiResponse apiResponse = new ApiResponse("Deal deleted", true);
//         return new ResponseEntity<>(apiResponse, HttpStatus.ACCEPTED);
//     }
// }

package com.sunbeam.controllers;

import com.sunbeam.entities.Deal;
import com.sunbeam.entities.HomeCategory; // Import HomeCategory
import com.sunbeam.response.ApiResponse;
import com.sunbeam.services.DealService;
import com.sunbeam.services.HomeCategoryService; // Import HomeCategoryService
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin") // Group all admin endpoints under /admin
public class DealController {

    private final DealService dealService;
    private final HomeCategoryService homeCategoryService; // Inject the service

    // --- Home Category Endpoints ---

    @PostMapping("/home-categories")
    public ResponseEntity<HomeCategory> createHomeCategory(@RequestBody HomeCategory category) {
        HomeCategory createdCategory = homeCategoryService.createCategory(category);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @GetMapping("/home-categories")
    public ResponseEntity<List<HomeCategory>> getAllHomeCategories() {
        List<HomeCategory> categories = homeCategoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // --- Deal Endpoints ---

    @PostMapping("/deals")
    public ResponseEntity<Deal> createDeals(@RequestBody Deal deals) {
        Deal createdDeals = dealService.createDeal(deals);
        return new ResponseEntity<>(createdDeals, HttpStatus.CREATED);
    }

    @GetMapping("/deals")
    public ResponseEntity<List<Deal>> getDeals() {
        List<Deal> deals = dealService.getDeals();
        return new ResponseEntity<>(deals, HttpStatus.OK);
    }

    @PatchMapping("/deals/{id}")
    public ResponseEntity<Deal> updateDeal(@PathVariable Long id, @RequestBody Deal deal) throws Exception {
        Deal updatedDeal = dealService.updateDeal(deal, id);
        return ResponseEntity.ok(updatedDeal);
    }

    @DeleteMapping("/deals/{id}")
    public ResponseEntity<ApiResponse> deleteDeals(@PathVariable Long id) throws Exception {
        dealService.deleteDeal(id);
        ApiResponse apiResponse = new ApiResponse("Deal deleted", true);
        return new ResponseEntity<>(apiResponse, HttpStatus.ACCEPTED);
    }
}