package ecommerce.http.services.order;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.http.entities.Client;
import ecommerce.http.entities.Order;
import ecommerce.http.entities.OrderItem;
import ecommerce.http.entities.ProductSku;
import ecommerce.http.enums.OrderStatus;

import ecommerce.http.exceptions.BadRequestException;
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

        Boolean userHasNecessaryAmount = userWalletAmount.compareTo(orderTotal) > 0;

        OrderStatus orderStatus =
                userHasNecessaryAmount ? OrderStatus.APPROVED : OrderStatus.PENDING;

        order.setStatus(orderStatus);

        Order performOrderCreation = this.orderRepository.save(order);

        this.orderItemRepository.saveAll(allOrderItems);

        if (!userHasNecessaryAmount) {
            throw new PaymentRequiredException(
                    "Insufficient amount. Order status will be pending.");
        }

        if (performOrderCreation != null) {
            this.walletService.withdrawValue(orderTotal, orderClient.getWallet().getId());
        }

        return performOrderCreation;
    }

}
