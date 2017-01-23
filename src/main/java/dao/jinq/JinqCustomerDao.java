package dao.jinq;

import dto.Customer;
import org.jinq.jpa.JPAJinqStream;
import org.jinq.jpa.JinqJPAStreamProvider;

import javax.persistence.EntityManager;
import java.sql.SQLException;
import java.util.List;

import static java.util.stream.Collectors.toList;

class JinqCustomerDao {

   private final JinqJPAStreamProvider streams;
   private final EntityManager em;

   JinqCustomerDao(EntityManager em) {
      this.em = em;
      streams = new JinqJPAStreamProvider(em.getMetamodel());
   }

   List<Customer> findAll() throws SQLException {
      return streamCustomer().collect(toList());
   }

   List<Customer> findByFirstName(String firstName) {

      return streamCustomer()
            .where(customer -> true)
            .collect(toList());
   }

   private JPAJinqStream<Customer> streamCustomer() {
      return streams.streamAll(em, Customer.class);
   }
}
