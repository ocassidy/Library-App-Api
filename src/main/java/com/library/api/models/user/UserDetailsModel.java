package com.library.api.models.user;

import com.library.api.entities.RoleEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsModel {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Set<RoleEntity> roles;
}
