# E-commerce system

Java-based application built using the Spring framework. The system is designed to cater to both clients and administrators, providing a range of features to facilitate smooth e-commerce operations in a basic way.

## Application Rules

### Clients

- [x] It must be possible to register.
- [x] It must be possible to login.
- [x] It must be possible to update password (forgot password).
- [x] It must be possible to check profile informations.
- [x] It must be possible to update profile dynamically.
- [x] It must be possible to buy an product.
- [x] It must be possible to buy an product using an discount coupon.
- [x] It must be possible to request a refund from an order.
- [x] Each client has your own wallet.
- [x] Perform Wallet transactions with products and orders.
- [x] Manage your own orders.
- [x] Manage your owns coupons.
- [x] Send user email after a purchase, register, refund, or order cancelled (e-mail microservice).
- [x] All entities validations.

### Admin (Shop)

- [x] It must be possible to perform CRUD operations in a product.
- [x] It must be possible to insert a new SKU to an product.
- [x] It must be possible to perform CRUD operations in a category.
- [x] It must be possible to filter products with dynamic queries such as active, category, name, color, sizes, gender etc.
- [x] It must be possible to perform CRUD operations in coupons.
- [x] List all orders already made.
- [x] Give an determined amount to an user wallet.
- [x] Give users coupons to use.
- [x] Aprove/refuse orders.
- [x] Refund an purchase.
- [x] All entities validations.

## Technologies

- Java 17
- Spring Boot
- Postgres
- Docker
- Hibernate
- RabbitMQ
- Microservice for e-mail handling (https://github.com/Paiva2/mail-service)
