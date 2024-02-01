package ecommerce.http.controllers.order;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ecommerce.http.dtos.order.NewOrderDto;
import ecommerce.http.dtos.order.utils.FormatNewOrderDto;
import ecommerce.http.entities.Order;
import ecommerce.http.enums.OrderStatus;
import ecommerce.http.services.jwt.JwtService;
import ecommerce.http.services.order.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtService jwtService;

    public OrderController(OrderService orderService, JwtService jwtService) {
        this.orderService = orderService;
        this.jwtService = jwtService;
    }

    @PostMapping("/new")
    public ResponseEntity<Map<String, String>> newOrder(
            @RequestBody @Valid Map<String, Set<NewOrderDto>> newOrderDto,
            @RequestHeader("Authorization") String authToken) {
        String userToken = jwtService.validateToken(authToken);

        Set<NewOrderDto> dtoItems = newOrderDto.get("order");

        FormatNewOrderDto formatOrderDto = new FormatNewOrderDto(dtoItems);

        formatOrderDto.insertItemsOnOrder();

        this.orderService.newOrder(formatOrderDto.getOrder(), userToken);

        return ResponseEntity.status(201)
                .body(Collections.singletonMap("message", "Order created successfully!"));
    }

    @PatchMapping("/approve/{orderId}")
    public ResponseEntity<Object> approveOrder(
            @Valid @PathVariable(name = "orderId", required = true) UUID orderId) {

        this.orderService.handleApproveOrder(orderId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/deny/{orderId}")
    public ResponseEntity<Object> denyOrder(
            @Valid @PathVariable(name = "orderId", required = true) UUID orderId) {

        this.orderService.denyPendingOrder(orderId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/list")
    public ResponseEntity<Page<Order>> listAll(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "perPage", required = false, defaultValue = "5") Integer perPage,
            @Valid @RequestParam(name = "status", required = false) OrderStatus status) {

        Page<Order> ordersList = this.orderService.ListAllOrders(page, perPage, status);

        return ResponseEntity.ok().body(ordersList);
    }
}
