package com.sunbeam.services;

import com.sunbeam.entities.User;
import com.sunbeam.exceptions.UserException;

public interface UserService {
    User findUserByJwtToken(String jwt) throws UserException;
    User findUserByEmail(String email) throws UserException;
    User findUserProfileByJwt(String jwt) throws UserException;
}