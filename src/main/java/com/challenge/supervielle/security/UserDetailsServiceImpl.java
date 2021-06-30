package com.challenge.supervielle.security;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder encoder;

    @SneakyThrows
    public UserDetails loadUserByUsername(String usuario) throws UsernameNotFoundException {

        //TODO no esta implementado el servicio de usuarios. Se coloca usuario generico a modo de ejemplo para crear un token

        List<String> roles = new ArrayList();
        roles.add("supervielle");
        User user = userBuilder("supervielle", "supervielle", roles);
        return user;
    }

    private User userBuilder(String usuario, String clave, List<String> roles){

        boolean enable = true;
        boolean accountNonExpired = true;
        boolean credentianNonExpired = true;
        boolean accounNonLocked = true;

        List<GrantedAuthority> authorities = new ArrayList<>();

        for (String rol : roles){
            authorities.add(new SimpleGrantedAuthority("ROLE_" + rol));
        }

        return new User(usuario, clave, enable, accountNonExpired, credentianNonExpired, accounNonLocked, authorities);

    }
}
