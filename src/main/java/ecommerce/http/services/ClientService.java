package ecommerce.http.services;

import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ecommerce.http.entities.Client;
import ecommerce.http.exceptions.BadRequestException;
import ecommerce.http.exceptions.ConflictException;
import ecommerce.http.repositories.ClientRepository;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    private final ClientRepository studentRepository;

    public ClientService(ClientRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Client register(Client client) {
        if (client == null) {
            throw new BadRequestException("Invalid client informations.");
        }

        Optional<Client> doesClientExists = this.studentRepository.findByEmail(client.getEmail());

        if (doesClientExists.isPresent()) {
            throw new ConflictException("User e-mail already exists.");
        }

        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder(6);

        String hashedPassword = bcrypt.encode(client.getPassword());

        client.setPassword(hashedPassword);

        Client createdClient = this.studentRepository.save(client);

        return createdClient;
    }
}
