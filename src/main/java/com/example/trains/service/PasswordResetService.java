package com.example.trains.service;

import com.example.trains.DTO.PasswordRecoveryDTO;
import com.example.trains.entity.RegisteredUser;
import com.example.trains.exception.ElementNotFoundException;
import com.example.trains.repository.RegisteredUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PasswordResetService {
    @Autowired
    RegisteredUserRepository registeredUserRepository;
    @Autowired
    MailSenderService mailSenderService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public void sendMailToPasswordRecovery(String mail) throws ElementNotFoundException {
        RegisteredUser user = registeredUserRepository.findByMail(mail);
        if(user == null) {
            throw new ElementNotFoundException("Пользователь не найден " + mail);
        }
        String code = UUID.randomUUID().toString();
        user.setRecoveryCode(passwordEncoder.encode(code));
        registeredUserRepository.save(user);
        String message = "Здравствуйте, " + user.getPassenger().getFIO() + " \n Код подтверждения: " + code;

        mailSenderService.send(user.getMail(), "Восстановление пароля", message);
    }

    public void recoverPassword(PasswordRecoveryDTO request) throws Exception {
        RegisteredUser user = registeredUserRepository.findByMail(request.getMail());
        if(user == null) {
            throw new ElementNotFoundException("Пользователь не найден");
        }
        if(!passwordEncoder.matches(request.getRecoveryCode(), user.getRecoveryCode())) {
            throw new Exception("Неверный код подтверждения");
        }
        if(!request.getNewPassword().equals(request.getNewPasswordConfirm())) {
            throw new Exception("Пароли не совпадают");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        registeredUserRepository.save(user);
    }
}
