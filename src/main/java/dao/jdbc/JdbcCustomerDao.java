package dao.jdbc;

import dto.Customer;

import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

import static dao.jdbc.ConnectionProvider.createConnection;

class JdbcCustomerDao {

   List<Customer> findAll() throws SQLException {
      try (Connection connection = createConnection();
           Statement statement = connection.createStatement();
           ResultSet resultSet = statement
                 .executeQuery("select c.id as customer_id, first_name, last_name from customers c")
      ) {
         List<Customer> results = new LinkedList<>();

         while (resultSet.next()) {

            results.add(new Customer(resultSet.getInt("customer_id"),
                  resultSet.getString("first_name"),
                  resultSet.getString("last_name")));
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
         return new Customer(resultSet.getInt("id"),
               resultSet.getString("first_name"),
               resultSet.getString("last_name"), null, null);
      }
      return null;
   }

}
