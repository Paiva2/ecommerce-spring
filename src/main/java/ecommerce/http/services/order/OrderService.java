package ecommerce.http.services.order;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.http.entities.Client;
import ecommerce.http.entities.ClientWallet;
import ecommerce.http.entities.Order;
import ecommerce.http.entities.OrderItem;
import ecommerce.http.entities.Product;
import ecommerce.http.entities.ProductSku;

import ecommerce.http.enums.OrderStatus;

import ecommerce.http.exceptions.BadRequestException;
import ecommerce.http.exceptions.ConflictException;
import ecommerce.http.exceptions.NotFoundException;
import ecommerce.http.exceptions.PaymentRequiredException;

import ecommerce.http.repositories.ClientRepository;
import ecommerce.http.repositories.OrderItemRepository;
import ecommerce.http.repositories.OrderRepository;
import ecommerce.http.repositories.ProductRepository;
import ecommerce.http.repositories.ProductSkuRepository;
import ecommerce.http.repositories.WalletRepository;

@Service
public class OrderService {
    @Autowired
    private final OrderRepository orderRepository;

    @Autowired
    private final WalletRepository walletRepository;

    @Autowired
    private final ClientRepository clientRepository;

    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    private final ProductSkuRepository productSkuRepository;

    @Autowired
    private final OrderItemRepository orderItemRepository;

    public OrderService(OrderRepository orderRepository, ClientRepository clientRepository,
            ProductRepository productRepository, WalletRepository walletRepository,
            OrderItemRepository orderItemRepository, ProductSkuRepository productSkuRepository) {
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
        this.walletRepository = walletRepository;
        this.orderItemRepository = orderItemRepository;
        this.productSkuRepository = productSkuRepository;
    }

    @Transactional
    public Order newOrder(Order order, String clientId) {
        if (order == null) {
            throw new BadRequestException("Invalid order.");
        }

        Optional<Client> doesClientExists =
                this.clientRepository.findById(UUID.fromString(clientId));

        if (doesClientExists.isEmpty()) {
            throw new NotFoundException("Client not found.");
        }

        Client orderClient = doesClientExists.get();
        order.setClient(orderClient);

        Set<OrderItem> allOrderItems = order.getItems();

        // TODO: move this logic to an orderItemService
        allOrderItems.forEach(orderItem -> {
            Product doesProductExists = this.productRepository
                    .findByIdAndSku(orderItem.getProductId(), orderItem.getSkuId()).orElse(null);

            if (doesProductExists == null) {
                throw new NotFoundException("Product and sku, not found.");
            }

            Product orderProduct = doesProductExists;

            BigDecimal orderQuantity = new BigDecimal(orderItem.getQuantity());

            ProductSku orderSku = orderProduct.getSkus().iterator().next();

            if (Integer.valueOf(orderQuantity.toString()) > orderSku.getQuantity()) {
                throw new ConflictException(
                        "Quantity of product " + orderProduct.getName() + " isn't available.");
            }

            orderItem.setProductName(orderProduct.getName());
            orderItem.setWasOnSale(orderSku.getIsOnSale());
            orderItem.setSkuColor(orderSku.getColor());
            orderItem.setSkuGender(orderSku.getGender().toString());
            orderItem.setSkuSize(orderSku.getSize());
            orderItem.setOrder(order);

            BigDecimal getItemValue =
                    orderSku.getIsOnSale() ? orderSku.getPriceOnSale() : orderSku.getPrice();

            BigDecimal itemValueTotal = getItemValue.multiply(orderQuantity);

            order.incrementTotal(itemValueTotal);

            orderSku.subtractQuantity(orderItem.getQuantity());

            productSkuRepository.save(orderSku);
        });

        BigDecimal userWalletAmount =
                new BigDecimal(orderClient.getWallet().getAmount().toString());

        BigDecimal orderTotal = new BigDecimal(order.getTotal().toString());

        if (userWalletAmount.compareTo(orderTotal) < 0) {
            throw new PaymentRequiredException("No amount for this order found on Client wallet.");
        }

        order.setStatus(OrderStatus.APPROVED); // TODO: do not aprove immediately

        Order performOrderCreation = this.orderRepository.save(order);

        this.orderItemRepository.saveAll(allOrderItems);

        if (performOrderCreation != null) {
            Optional<ClientWallet> clientWallet =
                    this.walletRepository.findById(doesClientExists.get().getWallet().getId());

            if (clientWallet.isEmpty()) {
                throw new NotFoundException("Client wallet not found.");
            }

            clientWallet.get().withdraw(order.getTotal());

            this.walletRepository.save(clientWallet.get());
        }

        return performOrderCreation;
    }
}
