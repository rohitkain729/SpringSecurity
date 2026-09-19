package com.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.model.ERole;
import com.app.model.Roles;
import com.app.model.User;

public interface RoleRepository extends JpaRepository<Roles, Integer>{

	Optional<Roles> findByName(ERole name);
}
