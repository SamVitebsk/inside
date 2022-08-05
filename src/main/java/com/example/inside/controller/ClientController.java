package com.example.inside.controller;

import com.example.inside.resources.AuthRequest;
import com.example.inside.resources.AuthResponse;
import com.example.inside.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @PostMapping("/registration")
    public ResponseEntity<AuthResponse> registration(@Valid @RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(clientService.create(authRequest));
    }

    @PostMapping("/authorization")
    public ResponseEntity<AuthResponse> authorization(@Valid @RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(clientService.auth(authRequest));
    }
}
