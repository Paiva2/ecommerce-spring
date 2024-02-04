package ecommerce.http.controllers.client;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import ecommerce.http.dtos.client.AuthClientDto;
import ecommerce.http.dtos.client.ForgotPasswordClientDto;
import ecommerce.http.dtos.client.RegisterClientDto;
import ecommerce.http.dtos.client.UpdateProfileDto;
import ecommerce.http.entities.Client;
import ecommerce.http.entities.Coupon;
import ecommerce.http.entities.Order;
import ecommerce.http.enums.OrderStatus;
import ecommerce.http.services.client.ClientService;
import ecommerce.http.services.jwt.JwtService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {
    @Autowired
    private final ClientService clientService;

    @Autowired
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

        return ResponseEntity.status(201)
                .body(Collections.singletonMap("message", "Registerd successfuly."));
    }

    @PatchMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(
            @RequestBody @Valid ForgotPasswordClientDto forgotPasswordClientDto) {

        clientService.forgotPassword(forgotPasswordClientDto.toClient());

        return ResponseEntity.ok()
                .body(Collections.singletonMap("message", "Password successfully updated."));
    }

    @GetMapping("/profile")
    public ResponseEntity<Map<String, Map<String, Object>>> profile(
            @RequestHeader("Authorization") String headers) {
        String headerToken = headers.replaceAll("Bearer", "");

        String parseToken = jwtService.validateToken(headerToken);

        Map<String, Object> getClientProfile = this.clientService.getProfile(parseToken);

        return ResponseEntity.ok().body(Collections.singletonMap("profile", getClientProfile));
    }

    @PatchMapping("/profile")
    public ResponseEntity<Map<String, Client>> updateProfile(
            @RequestBody @Valid UpdateProfileDto updateprofileDto,
            @RequestHeader("Authorization") String headers) throws Exception {
        String headerToken = headers.replaceAll("Bearer", "");

        String parseToken = jwtService.validateToken(headerToken);

        Client update = this.clientService.editProfile(updateprofileDto.toClient(),
                UUID.fromString(parseToken));

        return ResponseEntity.ok().body(Collections.singletonMap("message", update));
    }

    @GetMapping("/orders")
    public ResponseEntity<Page<Set<Order>>> getOrders(
            @RequestHeader("Authorization") String authToken,
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "perPage", required = false, defaultValue = "5") Integer perPage,
            @RequestParam(name = "status", required = false) OrderStatus status) {
        String userId = this.jwtService.validateToken(authToken);

        Page<Set<Order>> orders =
                this.clientService.getAllOrders(UUID.fromString(userId), page, perPage, status);

        return ResponseEntity.ok().body(orders);
    }

    @PatchMapping("/refund/{orderId}")
    public ResponseEntity<Object> requestAnOrderRefund(
            @Valid @PathVariable(name = "orderId", required = true) UUID orderId) {
        this.clientService.requestRefund(orderId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/coupons")
    public ResponseEntity<Map<String, Set<Coupon>>> listAllCoupons(
            @RequestHeader("Authorization") String authToken) {
        String tokenParsed = this.jwtService.validateToken(authToken);

        Set<Coupon> coupons = this.clientService.listCoupons(UUID.fromString(tokenParsed));

        Map<String, Set<Coupon>> response = new HashMap<>();

        response.put("coupons", coupons);

        return ResponseEntity.ok().body(response);
    }
}
