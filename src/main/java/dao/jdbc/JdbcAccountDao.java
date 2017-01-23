package dao.jdbc;

import dto.Account;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

import static dao.jdbc.ConnectionProvider.createConnection;

class JdbcAccountDao {


   List<Account> getAccountByName(String name) throws SQLException {

      try (Connection connection = createConnection();
           PreparedStatement statement = connection.prepareStatement("select * from accounts where name = ?")
      ) {
         statement.setString(1, name);
         try (ResultSet resultSet = statement.executeQuery()) {

            List<Account> results = new LinkedList<>();

            while (resultSet.next()) {
               results.add(new Account(resultSet.getInt("id"), resultSet.getString("name"), null));
            }
            return results;
         }
      }
   }

}
