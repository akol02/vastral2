package com.sunbeam.services.impl;

import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.sunbeam.config.JwtProvider;
import com.sunbeam.daos.AddressRepository;
import com.sunbeam.daos.SellerRepository;
import com.sunbeam.entities.Address;
import com.sunbeam.entities.Seller;
import com.sunbeam.exceptions.SellerException;
import com.sunbeam.models.AccountStatus;
import com.sunbeam.models.UserRole;
import com.sunbeam.services.SellerService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;

    @Override
    public Seller getSellerProfile(String jwt) throws Exception {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        return getSellerByEmail(email);
    }

    @Override
    public Seller createSeller(Seller seller) throws Exception {
        if (sellerRepository.findByEmail(seller.getEmail()) != null) {
            throw new Exception("Seller with this email already exists.");
        }
        
        Address savedAddress = addressRepository.save(seller.getPickupAddress());
        
        Seller newSeller = new Seller();
        newSeller.setEmail(seller.getEmail());
        newSeller.setPassword(passwordEncoder.encode(seller.getPassword())); // Password must be encoded
        newSeller.setSellerName(seller.getSellerName());
        newSeller.setPickupAddress(savedAddress);
        newSeller.setGSTIN(seller.getGSTIN());
        newSeller.setRole(UserRole.ROLE_SELLER);
        newSeller.setMobile(seller.getMobile());
        newSeller.setBankDetails(seller.getBankDetails());
        newSeller.setBusinessDetails(seller.getBusinessDetails());
        
        return sellerRepository.save(newSeller);
    }

    @Override
    public Seller getSellerById(Long id) throws SellerException {
        return sellerRepository.findById(id)
                .orElseThrow(() -> new SellerException("Seller not found with id: " + id));
    }

    @Override
    public Seller getSellerByEmail(String email) throws Exception {
        Seller seller = sellerRepository.findByEmail(email);
        if (seller == null) {
            throw new Exception("Seller not found with email: " + email);
        }
        return seller;
    }

    @Override
    public List<Seller> getAllSellers(AccountStatus status) {
        if (status != null) {
            return sellerRepository.findByAccountStatus(status);
        }
        return sellerRepository.findAll();
    }

    @Override
    public Seller updateSeller(Long id, Seller seller) throws Exception {
        Seller existingSeller = getSellerById(id);
        if (seller.getSellerName() != null) existingSeller.setSellerName(seller.getSellerName());
        if (seller.getMobile() != null) existingSeller.setMobile(seller.getMobile());
        // Handle other updates...
        return sellerRepository.save(existingSeller);
    }

    @Override
    public void deleteSeller(Long id) throws Exception {
        if (!sellerRepository.existsById(id)) {
            throw new SellerException("Seller not found with id: " + id);
        }
        sellerRepository.deleteById(id);
    }

    @Override
    public Seller verifyEmail(String email, String otp) throws Exception {
        Seller seller = getSellerByEmail(email);
        seller.setEmailVerified(true);
        seller.setAccountStatus(AccountStatus.ACTIVE); // Activate account upon verification
        return sellerRepository.save(seller);
    }

    @Override
    public Seller updateSellerAccountStatus(Long id, AccountStatus status) throws Exception {
        Seller seller = getSellerById(id);
        seller.setAccountStatus(status);
        return sellerRepository.save(seller);
    }
}