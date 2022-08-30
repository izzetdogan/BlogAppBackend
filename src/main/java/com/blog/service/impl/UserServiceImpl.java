package com.blog.service.impl;

import com.blog.config.AppEnv;
import com.blog.dto.UserDto;
import com.blog.exceptions.ResourceNotFoundException;
import com.blog.model.Role;
import com.blog.model.User;
import com.blog.repository.RoleRepository;
import com.blog.repository.UserRepository;
import com.blog.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;



@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto registerNewUser(UserDto userDto) {
        User user =  modelMapper.map(userDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role role =roleRepository.findById(AppEnv.NORMAL_USER).get();

        user.getRoles().add(role);
        User newUser = userRepository.save(user);
        return modelMapper.map(user,UserDto.class);
    }

    @Override
    public UserDto createUser(UserDto userDto){
        User user = dtoToUser(userDto);
        User savedUser = userRepository.save(user);
        return userToDto(savedUser);
    }
    @Override
    public UserDto updateUser(UserDto userDto, Integer userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setAbout(userDto.getAbout());
        user.setPassword(userDto.getPassword());

        User updateUser = userRepository.save(user);
        UserDto newUserDto = userToDto(updateUser);
        return  newUserDto;

    }
    @Override
    public UserDto getUserById(Integer userId){
        User  user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        return this.userToDto(user);

    }
    @Override
    public List<UserDto> getAllUsers(){

        List<User> users = userRepository.findAll();
        List<UserDto> usersDto = users.stream().map(user -> userToDto(user)).collect(Collectors.toList());
        return usersDto;
    }
    @Override
    public void deleteUser(Integer userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        this.userRepository.delete(user);
    }

    private User dtoToUser(UserDto userDto){
        User user = modelMapper.map(userDto, User.class);
//        user.setId(userDto.getId());
//        user.setName(userDto.getName());
//        user.setAbout(userDto.getName());
//        user.setPassword(userDto.getPassword());
 //       user.setEmail(userDto.getEmail());
        return user;
    }

    public UserDto userToDto(User user){
        UserDto userDto = modelMapper.map(user, UserDto.class);
//        userDto.setId(user.getId());
 //       userDto.setName(user.getName());
 //       userDto.setPassword(user.getPassword());
 //       userDto.setAbout(user.getAbout());
 //       userDto.setEmail(user.getEmail());
        return userDto;
    }
}
