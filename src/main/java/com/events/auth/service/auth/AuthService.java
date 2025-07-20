package com.events.auth.service.auth;

import com.events.auth.dto.LoginRequest;
import com.events.auth.dto.LoginResponse;
import com.events.auth.dto.RegisterRequest;
import com.events.auth.exception.EmailAlreadyExistException;
import com.events.auth.exception.UserNotFoundException;
import com.events.auth.service.email.IEmailService;
import com.events.auth.service.jwt.JwtService;
import com.events.auth.service.user.UserDetailsServiceImp;
import com.events.auth.utils.JwtUtils;
import com.events.user.entity.User;
import com.events.user.entity.role.Role;
import com.events.user.repository.IUserRepository;

import com.events.user.service.IUserService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final IUserService userService;
    private final IEmailService emailService;
    private final UserDetailsServiceImp userDetailsService;

    public String register(RegisterRequest request){

        if(userService.findByUsername(request.email()).isPresent()){
            throw new EmailAlreadyExistException(request.email());
        }

        String token = jwtService.generateToken(request.email());

        User user = User.builder()
                .fullName(request.fullName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .verificationToken(token)
                .isVerified(false)
                .build();

        userRepository.save(user);

        emailService.sendVerificationEmail(request.email(), token);

        return "Verification email sent to " + request.email() + ". Please check your inbox.";
    }

    @Override
    public String verifyEmail(String token) {
        return "";
    }

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = userRepository
                .findByEmail(request.email()).orElseThrow(() -> new UserNotFoundException(request.email()));

        String jwt = jwtService.generateToken(user);

        return LoginResponse.builder().token(jwt).build();
    }
}
