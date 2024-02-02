package ecommerce.http.services.client;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ecommerce.http.repositories.ClientRepository;
import ecommerce.http.repositories.OrderRepository;
import ecommerce.http.services.wallet.WalletService;

import ecommerce.http.entities.Client;
import ecommerce.http.entities.ClientWallet;
import ecommerce.http.entities.Order;

import ecommerce.http.enums.OrderStatus;

import ecommerce.http.exceptions.BadRequestException;
import ecommerce.http.exceptions.ConflictException;
import ecommerce.http.exceptions.ForbiddenException;
import ecommerce.http.exceptions.NotAllowedException;
import ecommerce.http.exceptions.NotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import java.beans.PropertyDescriptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    @Autowired
    private final ClientRepository repository;

    @Autowired
    private final WalletService walletService;

    @Autowired
    private final OrderRepository orderRepository;

    private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder(6);

    public ClientService(ClientRepository repository, WalletService walletService,
            OrderRepository orderRepository) {
        this.repository = repository;
        this.walletService = walletService;
        this.orderRepository = orderRepository;
    }

    public Client auth(Client client) {
        if (client == null) {
            throw new BadRequestException("Invalid client informations.");
        }

        Optional<Client> getClient = this.repository.findByEmail(client.getEmail());

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

        Optional<Client> doesClientExists = this.repository.findByEmail(client.getEmail());

        if (doesClientExists.isPresent()) {
            throw new ConflictException("User e-mail already exists.");
        }

        String hashedPassword = bcrypt.encode(client.getPassword());

        client.setPassword(hashedPassword);
        ClientWallet userWallet = this.walletService.generateWallet(client);
        client.setWallet(userWallet);

        Client createdClient = this.repository.save(client);

        return createdClient;
    }

    public void forgotPassword(Client client) {
        if (client == null) {
            throw new BadRequestException("Invalid client informations.");
        }

        if (client.getPrivateAnswer() == null) {
            throw new BadRequestException("Invalid private answer");
        }

        Optional<Client> doesClientExists = this.repository.findByEmail(client.getEmail());

        if (doesClientExists.isEmpty()) {
            throw new NotFoundException("User not found.");
        }

        Boolean doesPrivateAnswerMatches =
                doesClientExists.get().getPrivateAnswer().equals(client.getPrivateAnswer());

        if (!doesPrivateAnswerMatches) {
            throw new ForbiddenException("Invalid private answer");
        }

        String hashNewPassword = bcrypt.encode(client.getPassword());

        client.setPassword(hashNewPassword);

        int userUpdated = this.repository.forgotPassword(hashNewPassword, client.getEmail());

        if (userUpdated < 1) {
            throw new NotFoundException("User not found.");
        }
    }

    public Map<String, Object> getProfile(String clientId) {
        if (clientId == null) {
            throw new BadRequestException("Client id can't be empty.");
        }

        UUID parseId = UUID.fromString(clientId);

        Optional<Client> getClient = this.repository.findById(parseId);

        if (!getClient.isPresent()) {
            throw new NotFoundException("User not found.");
        }

        Client retrieveClient = getClient.get();

        Map<String, Object> clientMap = new LinkedHashMap<>();

        clientMap.put("id", retrieveClient.getId().toString());
        clientMap.put("name", retrieveClient.getName());
        clientMap.put("email", retrieveClient.getEmail());
        clientMap.put("role", retrieveClient.getRole().toString());
        clientMap.put("wallet", retrieveClient.getWallet());

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
            if (this.checkOldAndNew(clientOldPrivateQuestion, clientNewPrivateQuestion,
                    "private question")) {
                Boolean doesOldPrivateQuestionMatch = getCurrentProfile.get().getPrivateQuestion()
                        .equals(clientOldPrivateQuestion);

                if (!doesOldPrivateQuestionMatch) {
                    throw new ForbiddenException("Old private question doesn't match.");
                }

                clientProfileUpdate.setPrivateQuestion(clientNewPrivateQuestion);
            }
        }

        if (clientOldPrivateAnswer != null || clientNewPrivateAnswer != null) {
            if (this.checkOldAndNew(clientOldPrivateAnswer, clientNewPrivateAnswer,
                    "private answer")) {
                Boolean doesOldPrivateAnswerMatch =
                        getCurrentProfile.get().getPrivateAnswer().equals(clientOldPrivateAnswer);

                if (!doesOldPrivateAnswerMatch) {
                    throw new ForbiddenException("Old private answer doesn't match.");
                }

                clientProfileUpdate.setPrivateAnswer(clientNewPrivateAnswer);
            }
        }

        if (clientProfileUpdate.getEmail() != null) {
            Optional<Client> doesEmailAlreadyExists =
                    this.repository.findByEmail(clientProfileUpdate.getEmail());

            if (doesEmailAlreadyExists.isPresent()
                    && doesEmailAlreadyExists.get().getId() != clientId) {
                throw new ConflictException("E-mail already exists.");
            }
        }

        BeanWrapper sourceProfile = new BeanWrapperImpl(getCurrentProfile.get());
        BeanWrapper updatedProfile = new BeanWrapperImpl(clientProfileUpdate);

        PropertyDescriptor[] fieldsToUpdate = updatedProfile.getPropertyDescriptors();

        List<String> updatableFields =
                List.of("email", "name", "password", "privateQuestion", "privateAnswer");

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

    public Page<Set<Order>> getAllOrders(UUID clientId, Integer page, Integer perPage,
            OrderStatus status) {
        if (clientId == null) {
            throw new BadRequestException("Invalid client id.");
        }

        if (page < 1) {
            page = 1;
        }

        if (perPage < 5) {
            perPage = 5;
        }

        Optional<Client> getClient = this.repository.findById(clientId);

        if (getClient.isEmpty()) {
            throw new NotFoundException("User not found.");
        }

        PageRequest pageable = PageRequest.of(page - 1, perPage);

        Page<Set<Order>> clientOrders;

        if (status == null) {
            clientOrders =
                    this.orderRepository.getOrdersByUserId(getClient.get().getId(), pageable);
        } else {
            clientOrders = this.orderRepository.getOrdersByUserIdAndStatus(getClient.get().getId(),
                    pageable, status);
        }
        return clientOrders;
    }

    public void requestRefund(UUID orderId) {
        if (orderId == null) {
            throw new BadRequestException("Invalid order id.");
        }

        Optional<Order> doesOrderExists = this.orderRepository.findById(orderId);

        if (doesOrderExists.isEmpty()) {
            throw new NotFoundException("Order not found.");
        }

        Order order = doesOrderExists.get();

        if (!order.getStatus().equals(OrderStatus.FINISHED)
                || order.getStatus().equals(OrderStatus.PENDING_REFUND)) {
            throw new NotAllowedException(
                    "You can only request refund on orders that are already finished.");
        }

        order.setStatus(OrderStatus.PENDING_REFUND);

        this.orderRepository.save(order);
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
