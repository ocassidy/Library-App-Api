package com.library.api.services;

import com.library.api.entities.UserEntity;
import com.library.api.mappers.UserMapper;
import com.library.api.models.ApiResponse;
import com.library.api.models.user.UserModel;
import com.library.api.models.user.UserLoanDetails;
import com.library.api.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserModel getUser(String username) {
        Optional<UserEntity> userEntity = userRepository.findByUsername(username);
        return userMapper.mapEntityToModel(userEntity.get());
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public Page<UserModel> findPaginated(int page, int size) {
        PageRequest pageReq = PageRequest.of(page, size);
        Page<UserEntity> resultPage = userRepository.findAll(pageReq);
        return userMapper.mapEntitiesToModels(resultPage);
    }

    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username: " + username)
                );
    }

    public UserEntity getUserByEmail(String email) {
        return userRepository.findByUsername(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email: " + email)
                );
    }

    public ApiResponse updateUser(UserModel userModel) {
        UserEntity userEntity = getUserByUsername(userModel.getUsername());
        userEntity.setAddress1(userModel.getAddress1());
        userEntity.setAddress2(userModel.getAddress2());
        userEntity.setCity(userModel.getCity());
        userEntity.setContactNumber(userModel.getContactNumber());
        userEntity.setGender(userModel.getGender());

        userRepository.save(userEntity);
        return new ApiResponse(true, "User updated successfully");
    }

    public Page<UserLoanDetails> getActiveUserLoans(String username, int page, int size) {
        PageRequest pageReq = PageRequest.of(page, size);
        return userRepository.getActiveUserLoanDetails(username, pageReq);
    }

    public Page<UserLoanDetails> getInactiveUserLoans(String username, int page, int size) {
        PageRequest pageReq = PageRequest.of(page, size);
        return userRepository.getInactiveUserLoanDetails(username, pageReq);
    }
}
