package com.example.trains.controller;

import com.example.trains.entity.RegisteredUser;
import com.example.trains.DTO.LoginRequestDTO;
import com.example.trains.repository.RegisteredUserRepository;
import com.example.trains.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin( origins = "*", maxAge = 3500)
@RestController
@RequestMapping("/login")
public class AuthenticationController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RegisteredUserRepository registeredUserRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping()
    public ResponseEntity<?> authenticate(@RequestBody LoginRequestDTO request) {
        try {
            RegisteredUser user = registeredUserRepository.findByMail(request.getMail());
            if(user == null) {
                throw new UsernameNotFoundException("Пользователь не найден");
            }
            if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new Exception("Неверный пароль");
            }
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            request.getMail(),
                            request.getPassword()));

            String token = jwtUtils.generateJwtToken(authentication);

            Map<Object, Object> response = new HashMap<>();
            response.put("mail", request.getMail());
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}