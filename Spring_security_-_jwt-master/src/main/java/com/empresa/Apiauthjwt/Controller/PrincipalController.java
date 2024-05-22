package com.empresa.Apiauthjwt.Controller;

import com.empresa.Apiauthjwt.Controller.Request.CreateUserDTO;
import com.empresa.Apiauthjwt.Entity.ERole;
import com.empresa.Apiauthjwt.Entity.RoleEntity;
import com.empresa.Apiauthjwt.Entity.UserEntity;
import com.empresa.Apiauthjwt.Repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.PasswordAuthentication;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class PrincipalController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/hello")
    public String hello(){

        return "Hello World Not Secured";
    }

    @GetMapping("/helloSecured")
    public String helloSecured(){

        return "Hello World Secured";
    }

    @PostMapping("/createUser")
    public ResponseEntity<?>createUser(@Valid @RequestBody CreateUserDTO createUserDTO){

        Set<RoleEntity> roles =createUserDTO.getRoles().stream()
                .map(role -> RoleEntity.builder()
                        .name(ERole.valueOf(role))
                        .build())
                .collect(Collectors.toSet());

        UserEntity userEntity = UserEntity.builder()
                .username(createUserDTO.getUsername())
                .password(passwordEncoder.encode(createUserDTO.getPassword()))
                .email(createUserDTO.getEmail())
                .roles(roles)
                .build();
        userRepository.save(userEntity);
        return ResponseEntity.ok(userEntity);
    }

    @DeleteMapping("/deleteUser")
    public String deleteUser(@RequestParam String id){
        userRepository.deleteById(Long.parseLong(id));
        return "User deleted ".concat(id);
    }
}
