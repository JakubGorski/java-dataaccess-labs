package dao.jooq;

import dto.Deal;
import org.jooq.Record2;
import org.jooq.Record4;
import org.jooq.Result;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class JooqDealDaoTest {

   private JooqDealDao dealDao;

   @Before
   public void setUp() {
      dealDao = new JooqDealDao();
   }

   @Test
   public void allDeals() throws SQLException {
      final List<Deal> deals = dealDao.find(null, null, null);

      assertThat(deals).hasSize(17);
   }

   /*
   TODO: Task 16 - Implement totalBalanceReport using the window function:
    select deals.id, deals.open_price, deals.close_price,
               ((deals.close_price - deals.open_price)
               + coalesce(sum((deals.close_price - deals.open_price)) over (partition by deals.account_id
               order by deals.open_timestamp asc, deals.id asc rows between unbounded preceding and 1 preceding), ?)) as "Total balance"
    from deals where deals.account_id = ?
    order by deals.open_timestamp asc, deals.id asc
    */
   @Test
   public void totalBalanceReport() throws SQLException {
      final Result<Record4<Integer, BigDecimal, BigDecimal, BigDecimal>> records = dealDao.totalBalanceReport();

      assertBalance(records.get(0), 10008, "18.12", "9.30", "-8.82");
      assertBalance(records.get(1), 10009, "3.12", "5.33", "-6.61");
      assertBalance(records.get(2), 10010, "4.12", "13.31", "2.58");
      assertBalance(records.get(3), 10011, "5.12", "8.16", "5.62");
   }

   /*
    TODO: Task 17 - Implement customerTradeBestReport that returns pairs of customer's last name and best deal value
     */
   @Test
   public void bestTradeReport() throws SQLException {
      final Result<Record2<String, BigDecimal>> pairs = dealDao.customerTradeBestReport();


      assertThat(pairs.get(0).value1()).isEqualTo("Brown");
      assertThat(pairs.get(0).value2()).isEqualTo(new BigDecimal("9.19"));

      assertThat(pairs.get(1).value1()).isEqualTo("King");
      assertThat(pairs.get(1).value2()).isEqualTo(new BigDecimal("3.26"));

      assertThat(pairs.get(2).value1()).isEqualTo("Williams");
      assertThat(pairs.get(2).value2()).isEqualTo(new BigDecimal("1.52"));

   }

   private void assertBalance(Record4<Integer, BigDecimal, BigDecimal, BigDecimal> records, int id, String openPrice, String closePrice, String totalBalance) {
      assertThat(records.value1()).isEqualTo(id);
      assertThat(records.value2()).isEqualTo(new BigDecimal(openPrice));
      assertThat(records.value3()).isEqualTo(new BigDecimal(closePrice));
      assertThat(records.value4()).isEqualTo(new BigDecimal(totalBalance));
   }


}



