# E-commerce system - In progress

Java-based application built using the Spring framework. The system is designed to cater to both clients and administrators, providing a range of features to facilitate smooth e-commerce operations in a basic way.

## Application Rules

### Clients

- [x] It must be possible to register.
- [x] It must be possible to login.
- [x] It must be possible to update password (forgot password).
- [x] It must be possible to check profile informations.
- [x] It must be possible to update profile dynamically.
- [x] It must be possible to buy an product.
- [ ] It must be possible to request a refund from an order. - Doing 1
- [x] Each client has your own wallet.
- [x] Perform Wallet transactions with products and orders.
- [x] Manage your own orders.
- [ ] Manage your owns coupons.
- [ ] Send user email after a purchase.
- [x] All entities validations.

### Admin (Shop)

- [x] It must be possible to perform CRUD operations in a product.
- [x] It must be possible to insert a new SKU to an product.
- [x] It must be possible to perform CRUD operations in a category.
- [x] It must be possible to filter products with dynamic queries such as active, category, name, color, sizes, gender etc.
- [x] List all orders already made.
- [ ] Give an determined amount to an user wallet.
- [ ] Give users coupons to use.
- [x] Aprove/refuse orders.
- [ ] Refund an purchase. - Doing 2
- [x] All entities validations.

## Technologies

- Java
- Spring Boot
- Postgres
- Docker
- Hibernate