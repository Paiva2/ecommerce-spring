package ecommerce.http.services.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ecommerce.http.entities.Client;
import ecommerce.http.exceptions.BadRequestException;
import ecommerce.http.exceptions.ConflictException;
import ecommerce.http.exceptions.ForbiddenException;
import ecommerce.http.exceptions.NotAllowedException;
import ecommerce.http.exceptions.NotFoundException;
import ecommerce.http.repositories.ClientRepository;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import java.beans.PropertyDescriptor;

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

    // TODO: ADD SECRET QUESTION AND ANSWER VALIDATION B4
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

    public Client editProfile(Client clientProfileUpdate, UUID clientId) throws Exception {
        if (clientProfileUpdate == null) {
            throw new BadRequestException("Invalid client profile update.");
        }

        String clientOldPassword = clientProfileUpdate.getPassword();
        String clientOldPrivateQuestion = clientProfileUpdate.getPrivateQuestion();
        String clientOldPrivateAnswer = clientProfileUpdate.getPrivateAnswer();

        String clientNewPassword = clientProfileUpdate.getNewPassword();
        String clientNewPrivateQuestion = clientProfileUpdate.getNewPrivateQuestion();
        String clientNewPrivateAnswer = clientProfileUpdate.getNewPrivateAnswer();

        if (clientOldPassword != null && clientOldPassword.length() < 6
                || clientNewPassword != null && clientNewPassword.length() < 6) {
            throw new BadRequestException("Password must have at least 6 characters");
        }

        Optional<Client> getCurrentProfile = this.repository.findById(clientId);

        if (getCurrentProfile.isEmpty()) {
            throw new NotFoundException("Client not found.");
        }

        if (clientOldPassword != null || clientNewPassword != null) {
            if (this.checkOldAndNew(clientOldPassword, clientNewPassword, "password")) {
                Boolean doesPasswordsMatches = this.bcrypt.matches(clientOldPassword,
                        getCurrentProfile.get().getPassword());

                if (!doesPasswordsMatches) {
                    throw new ForbiddenException("Password's don't match.");
                }

                String hashedNewPassword = this.bcrypt.encode(clientNewPassword);

                clientProfileUpdate.setPassword(hashedNewPassword);
            }
        }

        if (clientOldPrivateQuestion != null || clientNewPrivateQuestion != null) {
            if (this.checkOldAndNew(clientOldPrivateQuestion, clientNewPrivateQuestion, "private question")) {
                Boolean doesOldPrivateQuestionMatch = getCurrentProfile.get().getPrivateQuestion()
                        .equals(clientOldPrivateQuestion);

                if (!doesOldPrivateQuestionMatch) {
                    throw new ForbiddenException("Old private question doesn't match.");
                }

                clientProfileUpdate.setPrivateQuestion(clientNewPrivateQuestion);
            }
        }

        if (clientOldPrivateAnswer != null || clientNewPrivateAnswer != null) {
            if (this.checkOldAndNew(clientOldPrivateAnswer, clientNewPrivateAnswer, "private answer")) {
                Boolean doesOldPrivateAnswerMatch = getCurrentProfile.get().getPrivateAnswer()
                        .equals(clientOldPrivateAnswer);

                if (!doesOldPrivateAnswerMatch) {
                    throw new ForbiddenException("Old private answer doesn't match.");
                }

                clientProfileUpdate.setPrivateAnswer(clientNewPrivateAnswer);
            }
        }

        if (clientProfileUpdate.getEmail() != null) {
            Optional<Client> doesEmailAlreadyExists = this.repository.findByEmail(clientProfileUpdate.getEmail());

            if (doesEmailAlreadyExists.isPresent() && doesEmailAlreadyExists.get().getId() != clientId) {
                throw new ConflictException("E-mail already exists.");
            }
        }

        BeanWrapper sourceProfile = new BeanWrapperImpl(getCurrentProfile.get());
        BeanWrapper updatedProfile = new BeanWrapperImpl(clientProfileUpdate);

        PropertyDescriptor[] fieldsToUpdate = updatedProfile.getPropertyDescriptors();

        List<String> updatableFields = List.of("email",
                "name",
                "password",
                "privateQuestion",
                "privateAnswer");

        for (PropertyDescriptor field : fieldsToUpdate) {
            String fieldName = field.getName();
            Object fieldValue = updatedProfile.getPropertyValue(fieldName);

            if (fieldValue != null && updatableFields.contains(fieldName)) {
                sourceProfile.setPropertyValue(fieldName, fieldValue);
            }
        }

        Client performUpdate = this.repository.save(getCurrentProfile.get());

        return performUpdate;
    }

    protected Boolean checkOldAndNew(String oldProp, String newProp, String propName) {
        if (oldProp != null && newProp == null) {
            throw new BadRequestException("Invalid new " + propName);
        } else if (oldProp == null && newProp != null) {
            throw new BadRequestException("Invalid old " + propName);
        }

        return true;
    }

}
