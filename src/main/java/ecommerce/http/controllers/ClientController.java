package ecommerce.http.controllers;

import java.util.Collections;
import java.util.Map;
import ecommerce.http.dtos.AuthClientDto;
import ecommerce.http.dtos.ForgotPasswordClientDto;
import ecommerce.http.dtos.RegisterClientDto;
import ecommerce.http.entities.Client;
import ecommerce.http.services.ClientService;
import ecommerce.http.services.JwtService;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {
    private final ClientService clientService;
    private final JwtService jwtService;

    public ClientController(ClientService clientService, JwtService jwtService) {
        this.clientService = clientService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> authClient(
            @RequestBody @Valid AuthClientDto authClientDto) {

        Client authorizedClient = clientService.auth(authClientDto.toClient());

        String clientToken = jwtService.generateToken(authorizedClient);

        return ResponseEntity.ok().body(Collections.singletonMap("secret_token", clientToken));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Map<String, String>> registerClient(
            @RequestBody @Valid RegisterClientDto registerClientDto) {

        clientService.register(registerClientDto.toClient());

        return ResponseEntity.status(201).body(Collections.singletonMap("message", "Registerd successfuly."))
    }

    @PatchMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(
            @RequestBody @Valid ForgotPasswordClientDto forgotPasswordClientDto) {

        clientService.forgotPassword(forgotPasswordClientDto.toClient());

        return ResponseEntity.ok()
                .body(Collections.singletonMap("message", "Password successfully updated."));
    }

    @GetMapping("/profile")
    public ResponseEntity<Map<String, Map<String, String>>> profile(
            @RequestHeader("Authorization") String headers) {
        String headerToken = headers.replaceAll("Bearer", "");

        String parseToken = jwtService.validateToken(headerToken);

        Map<String, String> getClientProfile = this.clientService.getProfile(parseToken);

        return ResponseEntity.ok().body(Collections.singletonMap("profile", getClientProfile));
    }

}
