package ecommerce.http.services.order;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import ecommerce.http.entities.Client;
import ecommerce.http.entities.Order;
import ecommerce.http.entities.OrderItem;
import ecommerce.http.entities.ProductSku;
import ecommerce.http.enums.OrderStatus;

import ecommerce.http.exceptions.BadRequestException;
import ecommerce.http.exceptions.ConflictException;
import ecommerce.http.exceptions.NotAllowedException;
import ecommerce.http.exceptions.NotFoundException;
import ecommerce.http.exceptions.PaymentRequiredException;

import ecommerce.http.repositories.ClientRepository;
import ecommerce.http.repositories.OrderItemRepository;
import ecommerce.http.repositories.OrderRepository;
import ecommerce.http.repositories.ProductSkuRepository;
import ecommerce.http.services.orderItem.OrderItemService;
import ecommerce.http.services.wallet.WalletService;

@Service
public class OrderService {
    @Autowired
    private final OrderRepository orderRepository;

    @Autowired
    private final ClientRepository clientRepository;

    @Autowired
    private final OrderItemRepository orderItemRepository;

    @Autowired
    private final OrderItemService orderItemService;

    @Autowired
    private final ProductSkuRepository productSkuRepository;

    @Autowired
    private final WalletService walletService;

    public OrderService(OrderRepository orderRepository, ClientRepository clientRepository,
            OrderItemRepository orderItemRepository, OrderItemService orderItemService,
            ProductSkuRepository productSkuRepository, WalletService walletService) {
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderItemService = orderItemService;
        this.productSkuRepository = productSkuRepository;
        this.walletService = walletService;
    }

    @Transactional(noRollbackFor = PaymentRequiredException.class)
    public Order newOrder(Order order, String clientId) {
        if (order == null) {
            throw new BadRequestException("Invalid order.");
        }

        if (clientId == null) {
            throw new BadRequestException("Invalid client id.");
        }

        Optional<Client> doesClientExists =
                this.clientRepository.findById(UUID.fromString(clientId));

        if (doesClientExists.isEmpty()) {
            throw new NotFoundException("Client not found.");
        }

        Client orderClient = doesClientExists.get();

        order.setClient(orderClient);

        Set<OrderItem> allOrderItems = order.getItems();
        Set<ProductSku> orderSkuList = new HashSet<>();

        allOrderItems.forEach(orderItem -> {
            this.orderItemService.newOrderItem(order, orderItem, orderSkuList);
        });

        this.productSkuRepository.saveAll(orderSkuList);

        BigDecimal userWalletAmount =
                new BigDecimal(orderClient.getWallet().getAmount().toString());

        BigDecimal orderTotal = new BigDecimal(order.getTotal().toString());

        Boolean userHasNecessaryAmount = userWalletAmount.compareTo(orderTotal) >= 0;

        OrderStatus orderStatus =
                userHasNecessaryAmount ? OrderStatus.FINISHED : OrderStatus.PENDING;

        order.setStatus(orderStatus);

        Order performOrderCreation = this.orderRepository.save(order);

        this.orderItemRepository.saveAll(allOrderItems);

        if (!userHasNecessaryAmount) {
            throw new PaymentRequiredException(
                    "Insufficient amount. Order status will be pending.");
        }

        if (performOrderCreation != null) {
            this.walletService.handleAmount(orderTotal, orderClient.getWallet().getId(),
                    "subtract");
        }

        return performOrderCreation;
    }

    @Transactional
    public void handleApproveOrder(UUID orderId) {
        if (orderId == null) {
            throw new BadRequestException("Invalid order id.");
        }

        Optional<Order> getOrder = this.orderRepository.findById(orderId);

        if (getOrder.isEmpty()) {
            throw new NotFoundException("Order not found.");
        }

        Order orderToHandle = getOrder.get();

        OrderStatus orderCurrentStatus = orderToHandle.getStatus();

        if (orderCurrentStatus.equals(OrderStatus.FINISHED)) {
            throw new ConflictException("Order is already finished.");
        }

        Optional<Client> getOrderOwner =
                this.clientRepository.findById(orderToHandle.getClient().getId());

        if (getOrderOwner.isEmpty()) {
            throw new NotFoundException("Order owner not found.");
        }

        if (orderCurrentStatus.equals(OrderStatus.CANCELLED)) {
            Set<ProductSku> orderItems = new HashSet<>();

            orderToHandle.getItems().forEach(item -> {
                ProductSku orderItemUpdated =
                        this.orderItemService.handleOrderItemQuantity(item, "subtract");

                orderItems.add(orderItemUpdated);
            });

            this.productSkuRepository.saveAll(orderItems);
        }

        orderToHandle.setStatus(OrderStatus.FINISHED);

        this.orderRepository.save(orderToHandle);

        this.walletService.handleAmount(orderToHandle.getTotal(),
                getOrderOwner.get().getWallet().getId(), "subtract");
    }

    @Transactional
    public void denyPendingOrder(UUID orderId) {
        if (orderId == null) {
            throw new BadRequestException("Invalid order id.");
        }

        Optional<Order> getOrder = this.orderRepository.findById(orderId);

        if (getOrder.isEmpty()) {
            throw new NotFoundException("Order not found");
        }

        Order orderToHandle = getOrder.get();

        if (orderToHandle.getStatus().equals(OrderStatus.CANCELLED)) {
            throw new ConflictException("Order is already cancelled.");
        }

        if (orderToHandle.getStatus().equals(OrderStatus.FINISHED)) {
            throw new ConflictException("Order is already finished.");
        }

        Set<ProductSku> itemsList = new HashSet<>();

        orderToHandle.getItems().forEach(orderItem -> {
            ProductSku orderItemUpdated =
                    this.orderItemService.handleOrderItemQuantity(orderItem, "increase");

            itemsList.add(orderItemUpdated);
        });

        orderToHandle.setStatus(OrderStatus.CANCELLED);

        this.productSkuRepository.saveAll(itemsList);
    }

    public Page<Order> ListAllOrders(Integer page, Integer perPage, OrderStatus status) {
        if (page < 1) {
            page = 1;
        }

        if (perPage < 5) {
            perPage = 5;
        }

        PageRequest pageable = PageRequest.of((page - 1), perPage);

        Page<Order> orders = null;

        if (status != null) {
            orders = this.orderRepository.findByStatus(status, pageable);
        } else {
            orders = this.orderRepository.findAll(pageable);
        }

        return orders;
    }

    @Transactional
    public void handlePendingRefund(UUID orderId, Boolean willApprove) {
        if (orderId == null) {
            throw new BadRequestException("Invalid order id.");
        }

        if (willApprove == null) {
            throw new BadRequestException("You must provide an valid action to pending refund.");
        }

        Optional<Order> doesOrderExists = this.orderRepository.findById(orderId);

        if (doesOrderExists.isEmpty()) {
            throw new NotFoundException("Order not found.");
        }

        Order order = doesOrderExists.get();

        if (!order.getStatus().equals(OrderStatus.PENDING_REFUND)) {
            throw new NotAllowedException(
                    "You can only refund orders that are pending for refund.");
        }

        BigDecimal orderTotal = order.getTotal();

        Optional<Client> getOrderClient = this.clientRepository.findById(order.getClient().getId());

        if (getOrderClient.isEmpty()) {
            throw new NotFoundException("Order owner not found.");
        }

        UUID clientWalletId = getOrderClient.get().getWallet().getId();

        OrderStatus defineStatus = willApprove ? OrderStatus.REFUNDED : OrderStatus.DENIED_REFUND;

        if (willApprove) {
            this.walletService.handleAmount(orderTotal, clientWalletId, "insert");

            Set<ProductSku> orderItemsSkus = new HashSet<>();

            order.getItems().forEach(item -> {
                ProductSku itemUpdated =
                        this.orderItemService.handleOrderItemQuantity(item, "increase");

                orderItemsSkus.add(itemUpdated);
            });

            this.productSkuRepository.saveAll(orderItemsSkus);
        }

        order.setStatus(defineStatus);

        this.orderRepository.save(order);
    }
}
