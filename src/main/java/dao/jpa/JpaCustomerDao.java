package dao.jpa;

import dto.Account;
import dto.Customer;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class JpaCustomerDao {

   private EntityManager entityManager;

   JpaCustomerDao(EntityManager entityManager) {
      this.entityManager = entityManager;
   }

   List<Customer> findAll() throws SQLException {
      TypedQuery<Customer> query = entityManager.createQuery("from Customer c JOIN FETCH c.accounts", Customer.class);
      return query.getResultList();
   }

   public Customer get(Integer id) {
      return entityManager.find(Customer.class, id);
   }

   List<Customer> findByFirstName(String firstName) {
      TypedQuery<Customer> query = entityManager.createQuery("from Customer where firstName=:firstName", Customer.class);
      query.setParameter("firstName", firstName);

      return query.getResultList();
   }


   List<Customer> find(String firstName, String lastName) {
      final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
      final CriteriaQuery<Customer> query = builder.createQuery(Customer.class);

      final Root<Customer> customer = query.from(Customer.class);

      List<Predicate> criteria = new ArrayList<>();
      if (firstName != null) {
         criteria.add(builder.equal(customer
               .get("firstName"), firstName));
      }
      if (lastName != null) {
         criteria.add(builder.equal(customer
               .get("lastName"), lastName));
      }

      if (!criteria.isEmpty()) {
         query
               .select(customer)
               .
                     where(builder.and(
                           criteria
                                 .stream()
                                 .toArray(Predicate[]::new))
                     );
      }

      query.orderBy(builder.asc(customer.get("id")));

      return entityManager
            .createQuery(query)
            .getResultList();
   }

    Integer create(String firstName, String lastName, String accountName) {

      final Customer entity = new Customer(firstName, lastName);
      entity.addAccount(new Account(accountName));

      entityManager.getTransaction().begin();

      entityManager.persist(entity);

      entityManager.getTransaction().commit();

      return entity.getId();
   }


}
