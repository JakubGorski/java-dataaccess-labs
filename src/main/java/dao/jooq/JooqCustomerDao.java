package dao.jooq;

import dto.Customer;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static dao.jdbc.ConnectionProvider.createConnection;
import static org.jooq.model.Tables.CUSTOMERS;

class JooqCustomerDao {

   List<Customer> findAll() throws SQLException {
      try (Connection connection = createConnection()) {

         return dsl(connection).
                     select().
                     from(CUSTOMERS).
                     fetch().
                     stream().map(this::map).
                     collect(Collectors.toList());
      }
   }

   List<String> findNames() throws SQLException {
      try (Connection connection = createConnection()) {
         return dsl(connection).
                                     select(CUSTOMERS.FIRST_NAME.concat(" ").concat(CUSTOMERS.LAST_NAME).as("CUSTOMER_NAME")).
                                     from(CUSTOMERS).
                                     fetch().
                                     stream()
                               .map(row -> row.get("CUSTOMER_NAME", String.class)).collect(Collectors.toList());
      }
   }

   private DSLContext dsl(Connection connection) {
      return DSL.using(connection, SQLDialect.POSTGRES);
   }

   private Customer map(Record row) {
      return new Customer(row.get(CUSTOMERS.FIRST_NAME), row.get(CUSTOMERS.LAST_NAME));
   }
}
