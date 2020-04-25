package com.library.api.mappers;

import com.library.api.entities.UserEntity;
import com.library.api.models.user.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    public UserModel mapEntityToModel(UserEntity userEntity) {
        return UserModel.builder()
                .address1(userEntity.getAddress1())
                .address2(userEntity.getAddress2())
                .city(userEntity.getCity())
                .contactNumber(userEntity.getContactNumber())
                .email(userEntity.getEmail())
                .gender(userEntity.getGender())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .registrationDate(userEntity.getRegistrationDate())
                .roles(userEntity.getRoles())
                .username(userEntity.getUsername())
                .id(userEntity.getId())
                .build();

    }

    public UserEntity mapModelToEntity(UserModel userModel) {
        return UserEntity.builder()
                .id(userModel.getId())
                .address1(userModel.getAddress1())
                .address2(userModel.getAddress2())
                .city(userModel.getCity())
                .contactNumber(userModel.getContactNumber())
                .email(userModel.getEmail())
                .gender(userModel.getGender())
                .firstName(userModel.getFirstName())
                .lastName(userModel.getLastName())
                .username(userModel.getUsername())
                .build();

    }

    public Page<UserModel> mapEntitiesToModels(Page<UserEntity> userEntities) {
        return userEntities.map(this::mapEntityToModel);
    }
}
