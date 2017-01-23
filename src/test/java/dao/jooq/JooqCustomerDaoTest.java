package dao.jooq;

import dto.Customer;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class JooqCustomerDaoTest {

   private JooqCustomerDao customerDao;

   @Before
   public void setUp() {
      customerDao = new JooqCustomerDao();
   }

   @Test
   public void    returnAllCustomers() throws SQLException {
      List<Customer> customers = customerDao.findAll();

      assertThat(customers.size()).isGreaterThan(3);
   }

   /*
   TODO: Task 15 - implement findNames that returns list of "FirstName LastName". Use sql concat.
   */
   @Test
   public void returnAllCustomersNames() throws SQLException {
      List<String> names = customerDao.findNames();

      assertThat(names.size()).isGreaterThan(3);
      assertThat(names).isEqualTo(asList("Richard Brown", "Jenny King", "James Bond", "Larry Bond", "James Williams"));
   }

}
