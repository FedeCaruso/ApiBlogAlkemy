package com.alkemy.apiBlog.services;

import com.alkemy.apiBlog.exceptions.WebException;
import com.alkemy.apiBlog.models.BlogUser;
import com.alkemy.apiBlog.repositories.BlogUserRepository;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *
 * @author Fede
 */
@Service
public class BlogUserService implements UserDetailsService {

    @Autowired
    private BlogUserRepository blogUserRepository;

    @Transactional
    public BlogUser save(String username, String password, String password2) throws WebException {
        BlogUser blogUser = new BlogUser();

        if (username == null | username.isEmpty()) {
            throw new WebException("El username no puede estar vacio");
        }
        if (findByUsername(username) != null) {
            throw new WebException("El username que queres usar ya existe.");
        }
        if (password == null | password2 == null | password.isEmpty() | password2.isEmpty()) {
            throw new WebException("La contraseña no puede estar vacía");
        }
        if (!password.equals(password2)) {
            throw new WebException("Las contraseñas deben ser iguales.");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        blogUser.setUsername(username);
        blogUser.setPassword(encoder.encode(password));

        return blogUserRepository.save(blogUser);
    }

    public BlogUser findByUsername(String username) {
        return blogUserRepository.findByUsername(username);
    }

    public List<BlogUser> listAll() {
        return blogUserRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            BlogUser blogUser = blogUserRepository.findByUsername(username);
            User user;
            List<GrantedAuthority> authorities = new ArrayList<>();
//            authorities.add(new SimpleGrantedAuthority("ROLE_"+blogUser.getRol()));
            
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("blogUsersession", blogUser);
            
            return new User(username, blogUser.getPassword(), authorities);
        } catch (Exception e) {
            throw new UnsupportedOperationException("El blogUser no existe.");
        }
    }
}
