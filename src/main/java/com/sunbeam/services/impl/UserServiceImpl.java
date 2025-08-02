package com.sunbeam.services.impl;

import org.springframework.stereotype.Service;
import com.sunbeam.config.JwtProvider;
import com.sunbeam.daos.UserRepository;
import com.sunbeam.entities.User;
import com.sunbeam.exceptions.UserException;
import com.sunbeam.services.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    public User findUserByJwtToken(String jwt) throws UserException {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        return findUserByEmail(email);
    }

    @Override
    public User findUserByEmail(String email) throws UserException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserException("User not found with email: " + email);
        }
        return user;
    }

    @Override
    public User findUserProfileByJwt(String jwt) throws UserException {
        return findUserByJwtToken(jwt);
    }
}