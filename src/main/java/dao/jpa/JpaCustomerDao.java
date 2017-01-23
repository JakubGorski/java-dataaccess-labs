package dao.jpa;

import dto.Account;
import dto.Customer;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.sql.SQLException;
import java.util.List;

class JpaCustomerDao {

   private EntityManager entityManager;

   JpaCustomerDao(EntityManager entityManager) {
      this.entityManager = entityManager;
   }

   List<Customer> findAll() throws SQLException {
      TypedQuery<Customer> query = entityManager.createQuery("from Customer", Customer.class);
      return query.getResultList();
   }

   Customer get(Integer id) {
      return entityManager.find(Customer.class, id);
   }

   List<Customer> find(String firstName, String lastName) {
      final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
      final CriteriaQuery<Customer> query = builder.createQuery(Customer.class);

      return entityManager
            .createQuery(query)
            .getResultList();
   }

   public Integer create(String firstName, String lastName, String accountName) {

      final Customer entity = new Customer(firstName, lastName);
      entity.addAccount(new Account(accountName));

      entityManager.getTransaction().begin();

      entityManager.persist(entity);

      entityManager.getTransaction().commit();

      return entity.getId();
   }


}
