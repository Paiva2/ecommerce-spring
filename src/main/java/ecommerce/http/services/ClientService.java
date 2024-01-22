package ecommerce.http.services;

import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ecommerce.http.entities.Client;
import ecommerce.http.enums.UserRole;
import ecommerce.http.exceptions.BadRequestException;
import ecommerce.http.exceptions.ConflictException;
import ecommerce.http.exceptions.NotAllowedException;
import ecommerce.http.exceptions.NotFoundException;
import ecommerce.http.repositories.ClientRepository;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    private final ClientRepository studentRepository;
    private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder(6);

    public ClientService(ClientRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Client auth(Client client) {
        if (client == null) {
            throw new BadRequestException("Invalid client informations.");
        }

        Optional<Client> getClient = this.studentRepository.findByEmail(client.getEmail());

        if (!getClient.isPresent()) {
            throw new ConflictException("User not found.");
        }

        boolean doesPasswordsMatches =
                this.bcrypt.matches(client.getPassword(), getClient.get().getPassword());

        if (!doesPasswordsMatches) {
            throw new NotAllowedException("Invalid credentials.");
        }

        return getClient.get();
    }

    public Client register(Client client) {
        if (client == null) {
            throw new BadRequestException("Invalid client informations.");
        }

        Optional<Client> doesClientExists = this.studentRepository.findByEmail(client.getEmail());

        if (doesClientExists.isPresent()) {
            throw new ConflictException("User e-mail already exists.");
        }

        String hashedPassword = bcrypt.encode(client.getPassword());

        client.setPassword(hashedPassword);
        client.setRole(UserRole.USER);

        Client createdClient = this.studentRepository.save(client);

        return createdClient;
    }

    public void forgotPassword(Client client) {
        if (client == null) {
            throw new BadRequestException("Invalid client informations.");
        }

        String hashNewPassword = bcrypt.encode(client.getPassword());

        client.setPassword(hashNewPassword);

        int userUpdated = this.studentRepository.forgotPassword(hashNewPassword, client.getEmail());

        if (userUpdated < 1) {
            throw new NotFoundException("User not found.");
        }
    }
}
