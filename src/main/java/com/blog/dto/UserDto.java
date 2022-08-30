package com.blog.dto;

import com.blog.model.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    private int id;

    @NotEmpty
    @Size(min=4, message = "Username must be min of 4 characetres")
    private  String name;

    @NotNull
    @Size(min=5 , message = "Password must be min of 5")
    private  String password;

    @Email(message = "Email address is not valid")
    private  String email;

    @NotNull
    private  String about;

    private Set<RoleDto> roles = new HashSet<>();
}
