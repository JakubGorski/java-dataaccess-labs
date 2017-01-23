package dao.jdbc;

import dto.Account;
import dto.Customer;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

import static dao.jdbc.ConnectionProvider.createConnection;

class JdbcCustomerDao {

   List<Customer> findAll() throws SQLException {
      try (Connection connection = createConnection();
           Statement statement = connection.createStatement();
           ResultSet resultSet = statement
                 .executeQuery("select c.id as customer_id, first_name, last_name, a.id as account_id, name as account_name" +
                       " from customers c left join accounts a on c.id = a.customer_id order by last_name")
      ) {
         List<Customer> results = new LinkedList<>();

         Integer customerId = null;
         Customer customer = null;
         while (resultSet.next()) {
            final Integer currentCustomerId = resultSet.getInt("customer_id");

            if (!currentCustomerId.equals(customerId)) {
               customer = new Customer(currentCustomerId,
                     resultSet.getString("first_name"),
                     resultSet.getString("last_name"));
               customerId = currentCustomerId;
               results.add(customer);
            }

            final int accountId = resultSet.getInt("account_id");

            if (!resultSet.wasNull()) {
               customer.getAccounts().add(new Account(accountId, resultSet.getString("account_name"), customer));
            }

         }
         return results;
      }
   }

   Customer get(Integer id) throws SQLException, IOException {
      Connection connection = createConnection();
      PreparedStatement statement = connection.prepareStatement("select * from customers where id=?");

      statement.setInt(1, id);

      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
         byte[] picture = null;
         try (final InputStream is = resultSet.getBinaryStream("picture")) {
            if (!resultSet.wasNull()) {
               picture = IOUtils.toByteArray(is);
            }
         }

         return new Customer(resultSet.getInt("id"),
               resultSet.getString("first_name"),
               resultSet.getString("last_name"), null, picture);
      }
      return null;

   }

}
