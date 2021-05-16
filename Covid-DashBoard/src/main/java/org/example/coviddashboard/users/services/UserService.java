package org.example.coviddashboard.users.services;

import org.example.coviddashboard.users.beans.User;
import org.example.coviddashboard.users.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> findUserByUsername(String username)
    {
        if(username == null || username.isEmpty())
            throw new IllegalArgumentException("Invalid Username");

        return userRepository.findUserByUsername(username);
    }

    public boolean existsByUsername(String username)
    {
        if(username == null || username.isEmpty())
            throw new IllegalArgumentException("Invalid Username");

        return userRepository.existsByUsername(username);
    }

    public User save(User user) {
        Objects.requireNonNull(user);

        if(user.getUsername() == null || user.getUsername().isEmpty())
            throw new IllegalArgumentException("Invalid Username");

        if(user.getPassword() == null || user.getPassword().isEmpty())
            throw new IllegalArgumentException("Invalid Username");

        return userRepository.save(user);
    }
}
