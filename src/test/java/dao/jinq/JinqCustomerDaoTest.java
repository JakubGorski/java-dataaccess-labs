package dao.jinq;

import dto.Customer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JinqCustomerDaoTest {

   private JinqCustomerDao customerDao;
   private EntityManagerFactory entityManagerFactory;
   private EntityManager entityManager;

   @Before
   public void setUp() {
      entityManagerFactory = Persistence.createEntityManagerFactory("persistence");
      entityManager = entityManagerFactory.createEntityManager();

      customerDao = new JinqCustomerDao(entityManager);
   }

   @After
   public void tearDown() {
      entityManager.close();
      entityManagerFactory.close();
   }

   /*
   TODO: Task 14 - implement case insensitive findByFirstName
    */
   @Test
   public void customersWithFirstNameJamesLowerCase() throws SQLException {
      List<Customer> customers = customerDao.findByFirstName("james");

      assertThat(customers).hasSize(2);
      assertThat(customers.stream().map(Customer::getFirstName)).allSatisfy("james"::equalsIgnoreCase);
   }

   @Test
   public void returnAllCustomers() throws SQLException {
      List<Customer> customers = customerDao.findAll();

      assertThat(customers.size()).isGreaterThan(3);
   }

}
