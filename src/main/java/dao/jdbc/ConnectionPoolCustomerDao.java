package dao.jdbc;

import dto.Customer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class ConnectionPoolCustomerDao {

   private DataSource datasource;

   ConnectionPoolCustomerDao() {
//      datasource = new HikariDataSource(config);
   }


   public Customer get(Integer id) throws SQLException {
      try (Connection connection = datasource.getConnection()) {
         PreparedStatement statement = connection.prepareStatement("select * from customers where id=?");

         statement.setInt(1, id);

         ResultSet resultSet = statement.executeQuery();
         if (resultSet.next()) {
            return new Customer(resultSet.getInt("id"),
                  resultSet.getString("first_name"),
                  resultSet.getString("last_name"));
         }
         return null;
      }
   }
}
