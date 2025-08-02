package com.sunbeam.services.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.sunbeam.daos.SellerRepository;
import com.sunbeam.daos.UserRepository;
import com.sunbeam.entities.Seller;
import com.sunbeam.entities.User;
import com.sunbeam.models.UserRole;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerUserServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private static final String SELLER_PREFIX = "seller_";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.startsWith(SELLER_PREFIX)) {
            String actualUsername = username.substring(SELLER_PREFIX.length());
            Seller seller = sellerRepository.findByEmail(actualUsername);
            if (seller != null) {
                return buildUserDetails(seller.getEmail(), seller.getPassword(), seller.getRole(), true);
            }
        } else {
            User user = userRepository.findByEmail(username);
            if (user != null) {
                return buildUserDetails(user.getEmail(), user.getPassword(), user.getRole(), false);
            }
        }
        throw new UsernameNotFoundException("User or Seller not found with email: " + username);
    }

    private UserDetails buildUserDetails(String email, String password, UserRole role, boolean isSeller) {
        if (role == null) {
            role = UserRole.ROLE_CUSTOMER; // Default Role
        }

        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(role.toString()));
        
        String username = isSeller ? SELLER_PREFIX + email : email;

        return new org.springframework.security.core.userdetails.User(username, password, authorityList);
    }
}