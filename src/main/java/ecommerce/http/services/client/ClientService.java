package ecommerce.http.services.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ecommerce.http.entities.Client;
import ecommerce.http.exceptions.BadRequestException;
import ecommerce.http.exceptions.ConflictException;
import ecommerce.http.exceptions.NotAllowedException;
import ecommerce.http.exceptions.NotFoundException;
import ecommerce.http.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    @Autowired
    private final ClientRepository repository;

    private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder(6);

    public ClientService(ClientRepository repository) {
        this.repository = repository;
    }

    public Client auth(Client client) {
        if (client == null) {
            throw new BadRequestException("Invalid client informations.");
        }

        Optional<Client> getClient = this.repository.findByEmail(client.getEmail());

        if (!getClient.isPresent()) {
            throw new ConflictException("User not found.");
        }

        boolean doesPasswordsMatches = this.bcrypt.matches(client.getPassword(), getClient.get().getPassword());

        if (!doesPasswordsMatches) {
            throw new NotAllowedException("Invalid credentials.");
        }

        return getClient.get();
    }

    public Client register(Client client) {
        if (client == null) {
            throw new BadRequestException("Invalid client informations.");
        }

        Optional<Client> doesClientExists = this.repository.findByEmail(client.getEmail());

        if (doesClientExists.isPresent()) {
            throw new ConflictException("User e-mail already exists.");
        }

        String hashedPassword = bcrypt.encode(client.getPassword());

        client.setPassword(hashedPassword);

        Client createdClient = this.repository.save(client);

        return createdClient;
    }

    public void forgotPassword(Client client) {
        if (client == null) {
            throw new BadRequestException("Invalid client informations.");
        }

        String hashNewPassword = bcrypt.encode(client.getPassword());

        client.setPassword(hashNewPassword);

        int userUpdated = this.repository.forgotPassword(hashNewPassword, client.getEmail());

        if (userUpdated < 1) {
            throw new NotFoundException("User not found.");
        }
    }

    public Map<String, String> getProfile(String clientId) {
        if (clientId == null) {
            throw new BadRequestException("Client id can't be empty.");
        }

        UUID parseId = UUID.fromString(clientId);

        Optional<Client> getClient = this.repository.findById(parseId);

        if (!getClient.isPresent()) {
            throw new NotFoundException("User not found.");
        }

        Client retrieveClient = getClient.get();

        Map<String, String> clientMap = new HashMap<>();

        clientMap.put("id", retrieveClient.getId().toString());
        clientMap.put("name", retrieveClient.getName());
        clientMap.put("email", retrieveClient.getEmail());
        clientMap.put("role", retrieveClient.getRole().toString());

        return clientMap;
    }
}
