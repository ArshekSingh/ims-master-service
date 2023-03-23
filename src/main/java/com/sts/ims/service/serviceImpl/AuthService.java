package com.sts.ims.service.serviceImpl;

import com.sts.ims.constant.Constant;
import com.sts.ims.entity.Token;
import com.sts.ims.entity.User;
import com.sts.ims.repository.TokenRepository;
import com.sts.ims.repository.UserRepository;
import com.sts.ims.request.LoginRequest;
import com.sts.ims.request.RegisterRequest;
import com.sts.ims.response.LoginResponse;
import com.sts.ims.response.Response;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements Constant {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public Response register(RegisterRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isPresent()) {
            return new Response("User already exist with this email", HttpStatus.CONFLICT);
        }
        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        saveUserToken(user, jwtToken);
        return new Response(SUCCESS, HttpStatus.CREATED);
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder().user(user).token(jwtToken).expired(false).revoked(false).build();
        tokenRepository.save(token);
    }

    public Response login(LoginRequest request) {
        if (isValidUser(request)) {
            Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
            if (!optionalUser.isPresent()) {
                return new Response(NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            User user = optionalUser.get();
            String jwtToken = jwtService.generateToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken);
            return new Response(SUCCESS, LoginResponse.builder().token(jwtToken).build(), HttpStatus.OK);
        }
        return new Response(INVALID_USERNAME_PASSWORD, HttpStatus.BAD_REQUEST);
    }

    private boolean isValidUser(LoginRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            return true;
        } catch (Exception e) {
            log.error("Exception occurred while login for user {}", request);
            return false;
        }
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (!validUserTokens.isEmpty()) {
            validUserTokens.forEach(token -> {
                token.setExpired(true);
                token.setRevoked(true);
            });
            tokenRepository.saveAll(validUserTokens);
        }
    }
}
