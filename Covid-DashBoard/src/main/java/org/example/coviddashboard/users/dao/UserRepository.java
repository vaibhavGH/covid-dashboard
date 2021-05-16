package org.example.coviddashboard.users.dao;

import org.example.coviddashboard.users.beans.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findUserByUsername(String username);

    Boolean existsByUsername(String username);
}
