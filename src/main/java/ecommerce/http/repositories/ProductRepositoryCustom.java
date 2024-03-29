package ecommerce.http.repositories;

import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import ecommerce.http.entities.Category;
import ecommerce.http.entities.Product;
import ecommerce.http.entities.ProductSku;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.ArrayList;

@Component
public class ProductRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    public ProductRepositoryCustom() {}

    public Page<Product> dynamicQuery(String productName, String color, String size,
            String categoryName, Boolean active, Integer page, Integer perPage) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);

        Join<Product, ProductSku> joinSkus = null;
        Join<Product, Category> joinCategories = null;

        if (productName != null || categoryName != null || color != null || size != null) {
            joinSkus = root.join("skus");
            joinCategories = root.join("category");
        }

        List<Predicate> predicates = new ArrayList<>();

        if (productName != null) {
            String[] nameToFind = productName.split(" ");

            if (nameToFind.length - 1 > 0) {
                predicates.add(criteriaBuilder.equal(root.get("name"), productName));
            } else {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + nameToFind[0] + "%"));
            }
        }

        if (categoryName != null) {
            predicates.add(criteriaBuilder.equal(joinCategories.get("name"), categoryName));
        }

        if (color != null) {
            predicates.add(criteriaBuilder.equal(joinSkus.get("color"), color));
        }

        if (size != null) {
            predicates.add(criteriaBuilder.equal(joinSkus.get("size"), size));
        }

        if (active != null) {
            predicates.add(criteriaBuilder.equal(root.get("active"), active));
        }

        criteriaQuery.where(predicates.toArray(new Predicate[] {}));

        List<Product> queryResult = entityManager.createQuery(criteriaQuery)
                .setFirstResult((page - 1) * perPage).setMaxResults(perPage).getResultList();

        Pageable pageable = PageRequest.of(page, perPage);

        Page<Product> resultPagination = new PageImpl<>(queryResult, pageable, queryResult.size());

        return resultPagination;
    }

}
