package org.mytodoapp.todo.security.model;

import lombok.RequiredArgsConstructor;
import org.mytodoapp.todo.user.entity.User;
import org.mytodoapp.todo.user.repo.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
         User user = userRepo.findByEmail(email)
                .orElseThrow( () -> new UsernameNotFoundException("User not found: " + email));

         return new CustomUserDetails(user);
    }

}
