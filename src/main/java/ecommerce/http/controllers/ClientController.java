package ecommerce.http.controllers;

import java.util.Collections;
import java.util.Map;
import ecommerce.http.dtos.AuthClientDto;
import ecommerce.http.dtos.RegisterClientDto;
import ecommerce.http.entities.Client;
import ecommerce.http.services.ClientService;
import ecommerce.http.services.JwtService;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {
    private ClientService clientService;
    private JwtService jwtService;

    public ClientController(ClientService clientService, JwtService jwtService) {
        this.clientService = clientService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public Map<String, String> authClient(@RequestBody @Valid AuthClientDto authClientDto) {

        Client authorizedClient = clientService.auth(authClientDto.toClient());

        String clientToken = jwtService.generateToken(authorizedClient);

        return Collections.singletonMap("secret_token", clientToken);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> registerClient(
            @RequestBody @Valid RegisterClientDto registerClientDto) {

        clientService.register(registerClientDto.toClient());

        return Collections.singletonMap("message", "Registerd successfuly.");
    }

}
