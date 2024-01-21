package ecommerce.http.controllers;

import java.util.Collections;
import java.util.Map;
import ecommerce.http.dtos.RegisterClientDto;
import ecommerce.http.entities.Client;
import ecommerce.http.services.ClientService;
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

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> registerClient(
            @RequestBody @Valid RegisterClientDto registerClientDto) {

        clientService.register(registerClientDto.transformToClient());

        return Collections.singletonMap("message", "Registerd successfuly.");
    }

}
