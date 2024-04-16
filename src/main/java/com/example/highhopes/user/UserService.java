package com.example.highhopes.user;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public List<User> listAll() {
        return userRepository.findAll();
    }


    public User add(User user) {
        return userRepository.save(user);
    }


    public Boolean deleteById(long id) {
        userRepository.deleteById(id);
        return true;
    }


    public User update(User user) {
        if (userRepository.existsById(user.getId())) {
            userRepository.save(user);
            return user;
        } else {
            return null;
        }
    }

    public User getById(long id) {
        return userRepository.findById(id).orElse(null);
    }
}
