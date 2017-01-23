package dao.jdbc;

import dto.Account;
import dto.Customer;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class JdbcCustomerDaoTest {

   private JdbcCustomerDao customerDao;

   @Before
   public void setUp() {
      customerDao = new JdbcCustomerDao();
   }


   /**
    TODO: Task 1 - Why this test fails? Fix the issue. Hint: you may need to restart db after this test case execution
    */
   @Test
   public void returnAllCustomersManyTimes() throws SQLException, IOException {
      for (int i = 0; i < 200; i++) {
         Customer customer = customerDao.get(1001);
         assertThat(customer).isNotNull();
      }
   }

   /**
    TODO: Task 2 - Use in ConnectionPoolCustomerDao the connection pool. Set maximum 10 connections, minimum idle 5.

    What will happen if you do not close the connection when using connection pool?
    */
   @Test
   public void returnAllCustomersManyTimesWithConnectionPool() throws SQLException {

      final ConnectionPoolCustomerDao customerDao = new ConnectionPoolCustomerDao();
      for (int i = 0; i < 200; i++) {
         Customer customer = customerDao.get(1001);
         assertThat(customer).isNotNull();
      }
   }

   /**
    TODO: Task 3 Using JDBC return all clients including accounts sorted by last name. Please make sure that only one query is used.
    */
   @Test
   public void returnOrderedCustomers() throws SQLException {
      List<Customer> customers = customerDao.findAll();

      assertThat(customers.size()).isEqualTo(5);

      assertCustomer(customers.get(0), "James", "Bond");
      assertCustomer(customers.get(1), "Larry", "Bond");
      assertCustomer(customers.get(2), "Richard", "Brown", "SpreadBet", "CFD");
      assertCustomer(customers.get(3), "Jenny", "King", "CFD", "SpreadBet", "StockBroking");
      assertCustomer(customers.get(4), "James", "Williams", "SpreadBet");

   }

   /*
    TODO: Task 5 - modify getCustomerById to return content of  "customers.picture". Hint: resultSet.getBinaryStream
    */
   @Test
   public void getCustomerById() throws SQLException, IOException {
      Customer customer = customerDao.get(1001);

      final byte[] picture = customer.getPicture();

      assertThat(picture).isNotNull();

      assertThat(md5(picture)).isEqualTo("2b14365bda5b8e45b81f0e757aa9b8be");
   }

   private void assertCustomer(Customer customer, String expectedFirstName, String expectedLastName, String... expectedAccountNames) {
      assertThat(customer).isNotNull();
      assertThat(customer.getFirstName()).isEqualTo(expectedFirstName);
      assertThat(customer.getLastName()).isEqualTo(expectedLastName);
      assertThat(customer.getAccounts()).isNotNull();
      assertThat(customer.getAccounts().size()).isEqualTo(expectedAccountNames.length);

      customer.getAccounts().forEach(account -> {
         assertThat(account).isNotNull();
         assertThat(account.getId()).isNotNull();
         assertThat(account.getCustomer()).isSameAs(customer);
      });

      final Set<String> accountNames = customer.getAccounts().stream().map(Account::getName).collect(Collectors.toSet());

      assertThat(accountNames).isEqualTo(new HashSet<>(Arrays.asList(expectedAccountNames)));
   }

   private String md5(byte[] picture) {
      return new BigInteger(1, DigestUtils.md5(picture)).toString(16);
   }
}
