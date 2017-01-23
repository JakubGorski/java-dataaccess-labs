package dao.jpa;

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

public class JpaCustomerDaoTest {

   private JpaCustomerDao customerDao;

   private EntityManager entityManager;

   private EntityManagerFactory entityManagerFactory;

   @Before
   public void setUp() {
      entityManagerFactory = Persistence.createEntityManagerFactory("persistence");
      entityManager = entityManagerFactory.createEntityManager();

      customerDao = new JpaCustomerDao(entityManager);
   }

   @After
   public void tearDown() {
      if (entityManager.isOpen()) {
         entityManager.close();
      }
      entityManagerFactory.close();
   }

   /*
    TODO: Task 6 - customerDao.findAll() results in 5 queries (n+1). Please optimize the hql query to execute just one. Hint: JOIN FETCH
    */
   /*
    TODO: Task 7 - Use proper annotation in Customer class to execute only 2 queries. Hint: @Fetch
    */
   @Test
   public void returnAllCustomers() throws SQLException {
      List<Customer> customers = customerDao.findAll();

      assertThat(customers.size()).isGreaterThan(3);
   }

   /*
    TODO: Task 8 - Implement 'create' method that creates the customer with a account . Hint: @OneToMany.cascade
    */
   @Test
   public void addCustomer() throws SQLException {
      Integer id = customerDao.create("Jan", "Kowalski", "SpreadBet");

      assertThat(id).isNotNull();

      final Customer customer = customerDao.get(id);

      assertThat(customer).isNotNull();
      assertThat(customer.getFirstName()).isEqualTo("Jan");
      assertThat(customer.getLastName()).isEqualTo("Kowalski");
      assertThat(customer.getAccounts()).isNotNull();
      assertThat(customer.getAccounts().size()).isEqualTo(1);
      assertThat(customer.getAccounts().get(0)).isNotNull();
      assertThat(customer.getAccounts().get(0).getName()).isEqualTo("SpreadBet");
      assertThat(customer.getAccounts().get(0).getCustomer()).isSameAs(customer);

      entityManager.getTransaction().begin();
      entityManager.remove(customer);

      entityManager.getTransaction().commit();
   }

   /*
    TODO: Task 9 - Modify the customer's JPA mapping to avoid "lazy initialization exception". Hint: @OneToMany.fetch = EAGER
    */
   @Test
   public void customerById() throws SQLException {
      Customer customer = customerDao.get(1002);
      entityManager.close(); // simulates detaching the object from active session
      assertThat(customer.getLastName()).isEqualTo("Brown");
      assertThat(customer.getFirstName()).isEqualTo("Richard");

      customer.getAccounts().forEach(System.out::println);
   }

   /*
    TODO: Task 10 - Implement find method that accepts first name and last name. If a parameter is null (last or first name), means that all values is accepted". Hint: See JpaDealDao.find
    */
   @Test
   public void conditionalFind() throws SQLException {
      List<Customer> customers = customerDao.find(null, null);

      assertThat(customers).hasSize(5);

      customers = customerDao.find("James", null);

      assertThat(customers).hasSize(2);

      customers = customerDao.find(null, "Bond");

      assertThat(customers).hasSize(2);

      customers = customerDao.find("James", "Bond");

      assertThat(customers).hasSize(1);

      assertThat(customers.get(0).getFirstName()).isEqualTo("James");
      assertThat(customers.get(0).getLastName()).isEqualTo("Bond");
   }

}
