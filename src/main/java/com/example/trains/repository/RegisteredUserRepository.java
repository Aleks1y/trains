package com.example.trains.repository;

import com.example.trains.entity.RegisteredUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisteredUserRepository extends JpaRepository<RegisteredUser, Long>{
	RegisteredUser findByMail(String mail);
	Boolean existsByMail(String mail);
}
