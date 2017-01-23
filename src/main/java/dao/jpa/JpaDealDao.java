package dao.jpa;

import dto.Deal;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

class JpaDealDao {

   private EntityManager entityManager;

   JpaDealDao(EntityManager entityManager) {
      this.entityManager = entityManager;
   }

   List<Deal> find(String instrumentName, String customerLastName, String accountName, int pageNumber) {
      final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
      final CriteriaQuery<Deal> query = builder.createQuery(Deal.class);

      final Root<Deal> deal = query.from(Deal.class);

      List<Predicate> criteria = new ArrayList<>();
      if (customerLastName != null) {
         criteria.add(builder.equal(deal
               .get("account")
               .get("customer")
               .get("lastName"), customerLastName));
      }
      if (instrumentName != null) {
         criteria.add(builder.equal(deal
               .get("instrument")
               .get("name"), instrumentName));
      }
      if (accountName != null) {
         criteria.add(builder.equal(deal
               .get("account")
               .get("name"), accountName));
      }

      if (!criteria.isEmpty()) {
         query
               .select(deal)
               .
                     where(builder.and(
                           criteria
                                 .stream()
                                 .toArray(Predicate[]::new))
                     );
      }

      query.orderBy(builder.asc(deal.get("id")));

      return entityManager
            .createQuery(query)
            .setFirstResult((pageNumber - 1) * 3)
            .setMaxResults(3)
            .getResultList();

   }
}
