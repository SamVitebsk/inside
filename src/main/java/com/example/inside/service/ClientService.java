package com.example.inside.service;

import com.example.inside.entity.Client;
import com.example.inside.exception.UserNameExistsException;
import com.example.inside.exception.UserNotFoundException;
import com.example.inside.repository.ClientRepository;
import com.example.inside.resources.AuthRequest;
import com.example.inside.resources.AuthResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

import static org.apache.commons.codec.digest.DigestUtils.sha3_256Hex;

@Service
@RequiredArgsConstructor
public class ClientService {
    private static final String PREFIX = "Bearer_";
    private static final String SECRET_KEY = "secret";

    private final ClientRepository clientRepository;

    public AuthResponse create(AuthRequest authRequest) {
        var clientOptional = clientRepository.findByName(authRequest.getName());
        if (clientOptional.isPresent()) {
            throw new UserNameExistsException(String.format("Пользователь и именем %s уже зарегистрирован", authRequest.getName()));
        }

        var client = new Client().setName(authRequest.getName())
                                    .setPassword(sha3_256Hex(authRequest.getPassword()));
        clientRepository.save(client);

        return new AuthResponse().setToken(createToken(client.getName()));
    }

    public AuthResponse auth(AuthRequest authRequest) {
        Client client = clientRepository.findByNameAndPassword(authRequest.getName(), sha3_256Hex(authRequest.getPassword()))
                                        .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        return new AuthResponse().setToken(createToken(client.getName()));
    }

    public Boolean compareUsernames(String token, String name) {
        String userName = Jwts.parser()
                              .setSigningKey(SECRET_KEY)
                              .parseClaimsJws(token.substring(7))
                              .getBody()
                              .getSubject();

        return name.equals(userName);
    }

    private String createToken(String subject) {
        return PREFIX + Jwts.builder()
                            .setSubject(subject)
                            .setIssuedAt(new Date(System.currentTimeMillis()))
                            .setExpiration(new Date(System.currentTimeMillis() + Integer.MAX_VALUE))
                            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                            .compact();
    }
}
