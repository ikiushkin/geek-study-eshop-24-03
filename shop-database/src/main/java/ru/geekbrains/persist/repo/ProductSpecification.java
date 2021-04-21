package ru.geekbrains.persist.repo;

import org.springframework.data.jpa.domain.Specification;
import ru.geekbrains.persist.model.Product;

import javax.persistence.criteria.JoinType;

public final class ProductSpecification {

    public static Specification<Product> byId(long id) {
        return (root, query, builder) -> builder.equal(root.get("id"), id);
    }

    public static Specification<Product> byCategory(long categoryId) {
        return (root, query, builder) -> builder.equal(root.get("category").get("id"), categoryId);
    }

    public static Specification<Product> fetchPictures() {
        return (root, query, builder) -> {
            // Don't do fetch in case of count() query for pagination
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("pictures", JoinType.LEFT);
                root.fetch("brand", JoinType.LEFT);
                query.distinct(true);
            }
            return builder.isTrue(builder.literal(true));
        };
    }
}
