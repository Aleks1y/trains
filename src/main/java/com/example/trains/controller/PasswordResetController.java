package com.example.trains.controller;

import com.example.trains.DTO.PasswordRecoveryDTO;
import com.example.trains.DTO.PasswordResetDTO;
import com.example.trains.exception.ElementNotFoundException;
import com.example.trains.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/password")
@CrossOrigin( origins = "*", maxAge = 3500)
public class PasswordResetController {
    @Autowired
    PasswordResetService passwordResetService;

    @GetMapping("/reset")
    public ResponseEntity<?> sendMessageWithCode(@RequestBody PasswordResetDTO request) {
        try {
            passwordResetService.sendMailToPasswordRecovery(request.getMail());
        }
        catch (ElementNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("Письмо c кодом отправлено");
    }

    @PostMapping("/recovery")
    public ResponseEntity<?> recoverPassword(@RequestBody PasswordRecoveryDTO request) {
        try {
            passwordResetService.recoverPassword(request);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("Пароль обновлен");
    }
}
