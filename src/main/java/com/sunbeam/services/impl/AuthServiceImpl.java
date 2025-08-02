package com.sunbeam.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.sunbeam.config.JwtProvider;
import com.sunbeam.daos.*;
import com.sunbeam.entities.*;
import com.sunbeam.models.UserRole;
import com.sunbeam.request.LoginRequest;
import com.sunbeam.request.SignupRequest;
import com.sunbeam.response.AuthResponse;
import com.sunbeam.services.AuthService;
import com.sunbeam.services.EmailService;
import com.sunbeam.utils.OtpUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final JwtProvider jwtProvider;
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;
    private final CustomerUserServiceImpl customUserService;
    private final SellerRepository sellerRepository;

    @Override
    public void sentLoginOtp(String email, UserRole role) throws Exception {
        if (role.equals(UserRole.ROLE_SELLER)) {
            Seller seller = sellerRepository.findByEmail(email);
            if (seller == null) {
                throw new Exception("Seller not found with email: " + email);
            }
        } else {
            User user = userRepository.findByEmail(email);
            if (user == null) {
                // For signup, user might not exist, which is fine. OTP is for email verification.
            }
        }

        VerificationCode existingCode = verificationCodeRepository.findByEmail(email);
        if (existingCode != null) {
            verificationCodeRepository.delete(existingCode);
        }

        String otp = OtpUtil.generateOtp();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(email);
        verificationCodeRepository.save(verificationCode);

        String subject = "Vastral: Login/Signup OTP";
        String text = "Your Login/Signup OTP is: " + otp;
        emailService.sendVerificationOtpEmail(email, subject, text);
    }

    @Override
    public String createUser(SignupRequest req) throws Exception {
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(req.getEmail());
        if (verificationCode == null || !verificationCode.getOtp().equals(req.getOtp())) {
            throw new Exception("Wrong or expired OTP.");
        }

        User user = userRepository.findByEmail(req.getEmail());
        if (user != null) {
            throw new Exception("User with this email already exists.");
        }

        User createdUser = new User();
        createdUser.setEmail(req.getEmail());
        createdUser.setFullName(req.getFullName());
        createdUser.setRole(UserRole.ROLE_CUSTOMER);
        createdUser.setPassword(passwordEncoder.encode(req.getOtp())); // Using OTP as initial password
        user = userRepository.save(createdUser);

        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);
        
        verificationCodeRepository.delete(verificationCode); // OTP is used, so delete it

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(UserRole.ROLE_CUSTOMER.toString()));
        Authentication authentication = new UsernamePasswordAuthenticationToken(req.getEmail(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtProvider.generateToken(authentication);
    }

    @Override
    public AuthResponse signing(LoginRequest req) throws Exception {
        Authentication authentication = authenticate(req.getEmail(), req.getOtp());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roleName = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();
        
        AuthResponse authResponse = new AuthResponse(token, "Login Success", UserRole.valueOf(roleName));
        return authResponse;
    }

    private Authentication authenticate(String username, String otp) throws Exception {
        UserDetails userDetails = customUserService.loadUserByUsername(username);

        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username.");
        }
        
        String emailForOtp = username.startsWith("seller_") ? username.substring(7) : username;
        
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(emailForOtp);
        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            throw new Exception("Wrong or expired OTP.");
        }
        
        verificationCodeRepository.delete(verificationCode); // OTP is used, so delete it

        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
    }
}