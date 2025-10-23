package org.microservicesrevision.auth.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.microservicesrevision.auth.dto.UserDetailsDTO;
import org.microservicesrevision.auth.repo.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByName(username)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getName(),
                        user.getPassword(),  // stored hash (use encoder later)
                        user.getRoles().stream()
                                .map(role -> new SimpleGrantedAuthority(role.getRole().getRoleName()))
                                .collect(Collectors.toList())
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public UserDetailsDTO loadUserDetails(String username) {
        UserDetails userDetails = loadUserByUsername(username);
        UserDetailsDTO dto = new UserDetailsDTO();
        dto.setUsername(userDetails.getUsername());
        dto.setPassword(userDetails.getPassword());
        dto.setAuthorities(userDetails.getAuthorities().stream()
                .map(Object::toString)
                .collect(Collectors.toList()));
        return dto;
    }
}
