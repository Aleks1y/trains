package com.example.trains.service;

import com.example.trains.entity.RegisteredUser;
import com.example.trains.exception.RegisteredUserNotFoundException;
import com.example.trains.repository.RegisteredUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	RegisteredUserRepository registeredUserRepository;
	
	@Override
	public UserDetails loadUserByUsername(String mail) throws RegisteredUserNotFoundException{
		RegisteredUser registeredUser = registeredUserRepository.findByMail(mail);
		if (registeredUser == null){
			throw new RegisteredUserNotFoundException("Не найден пользователь с email: " + mail);
		}
		return registeredUser;
	}

}
