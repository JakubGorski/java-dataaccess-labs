package dao.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dto.Customer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class ConnectionPoolCustomerDao {

   private final DataSource datasource;

   ConnectionPoolCustomerDao() {
      HikariConfig config = new HikariConfig();
      config.setJdbcUrl("jdbc:postgresql://localhost:5432/dealing");
      config.setUsername("postgres");
      config.setPassword("password1");

      config.setMaximumPoolSize(10);
      config.setMinimumIdle(5);
      config.setConnectionTestQuery("SELECT 1");

      datasource = new HikariDataSource(config);
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
