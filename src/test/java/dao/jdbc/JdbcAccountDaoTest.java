package dao.jdbc;

import dto.Account;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JdbcAccountDaoTest {

   @Test
   public void getAccountByName() throws Exception {
      final JdbcAccountDao accountDao = new JdbcAccountDao();

      final List<Account> accounts = accountDao.getAccountByName("CFD");

      assertThat(accounts).isNotNull();
      assertThat(accounts.size()).isEqualTo(2);

      assertThat(accounts).allSatisfy("CFD"::equals);
   }

   /*
   TODO: Task 4 - getAccountByName() is sql injection vulnerable, fix the issue. Hint: PreparedStatement
   */
   @Test
   public void sqlInjection() throws Exception {
      final JdbcAccountDao accountDao = new JdbcAccountDao();

      final List<Account> accounts = accountDao.getAccountByName("CFD' OR '1' = '1");

      assertThat(accounts).isNotNull();
      assertThat(accounts.size()).isEqualTo(0);
   }

}