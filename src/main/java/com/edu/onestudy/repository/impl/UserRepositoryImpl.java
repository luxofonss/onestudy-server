package com.edu.onestudy.repository.impl;

import com.edu.onestudy.dto.user.BasicUserDto;
import com.edu.onestudy.entity.User;
import com.edu.onestudy.entity.UserCredential;
import com.edu.onestudy.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository repository;

    private final JpaUserCredentialRepository userCredentialRepository;

    public UserRepositoryImpl(JpaUserRepository repository, JpaUserCredentialRepository userCredentialRepository) {
        this.repository = repository;
        this.userCredentialRepository = userCredentialRepository;
    }

    @Override
    public Optional<User> getUserById(String userId) {
        return repository.findById(UUID.fromString(userId));
    }

    @Override
    public Optional<User> getByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public Optional<User> getByUsernameForAuth(String username) {
        Optional<User> user = repository.findByUsername(username);
        if (user.isPresent()) {
            UserCredential credentials = userCredentialRepository.findByUserId(user.get().getId());
            user.get().setCredentials(credentials);
            return user;
        }
        return Optional.empty();
    }

    @Override
    public User persist(User user) {
        User userRes = repository.save(user);
        UserCredential credentials = user.getCredentials();
        credentials.setUserId(userRes.getId());
        userCredentialRepository.save(credentials);
        return userRes;
    }

    @Override
    public List<User> getByUserIdList(List<UUID> studentIds) {
        return repository.findAllById(studentIds);
    }

    @Override
    public List<BasicUserDto> basicSearch(String keyword) {
        return List.of();
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return repository.findByEmail(email);
    }
}
