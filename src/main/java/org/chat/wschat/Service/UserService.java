package org.chat.wschat.Service;

import org.chat.wschat.Repository.UserRepository;
import org.chat.wschat.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found! " + username));
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),new ArrayList<>());
    }

    public User resgisterUser(String username, String email, String password){
        if(userRepository.existsByUsername(username)){
            throw new IllegalArgumentException("Username already exists!");
        }
        if(userRepository.existsByEmail(email)){
            throw new IllegalArgumentException("Email already exists!");
        }
        User newUser = User.builder().username(username).email(email).password(passwordEncoder.encode(password)).build();
        return userRepository.save(newUser);
    }

    public User findByUserName(String username){
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found! " + username)
        );
    }
}
