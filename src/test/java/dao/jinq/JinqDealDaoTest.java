package dao.jinq;

import dto.Deal;
import org.jinq.tuples.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class JinqDealDaoTest {

   private JinqDealDao dealDao;
   private EntityManagerFactory entityManagerFactory;
   private EntityManager entityManager;

   @Before
   public void setUp() throws NoSuchMethodException {
      entityManagerFactory = Persistence.createEntityManagerFactory("persistence");
      entityManager = entityManagerFactory.createEntityManager();

      dealDao = new JinqDealDao(entityManager);
   }

   @After
   public void tearDown() {
      entityManager.close();
      entityManagerFactory.close();
   }

   @Test
   public void allDeals() throws SQLException {
      final List<Deal> deals = dealDao.find(null, null, null);

      assertThat(deals).hasSize(17);
   }

   /*
   TODO: Task 11 - this method generates quite complicated query. Can you simplify it?
    */
   @Test
   public void complexSearch() throws SQLException {
      final List<Deal> deals = dealDao.find("USD/EUR", "King", "CFD");

      assertThat(deals).hasSize(1);
      final Deal deal = deals.get(0);
      assertThat(deal.getAccount().getCustomer().getLastName()).isEqualTo("King");
      assertThat(deal.getAccount().getName()).isEqualTo("CFD");
      assertThat(deal.getInstrument().getName()).isEqualTo("USD/EUR");
   }

   /*
   TODO: Task 12 - implement findBestAndWorstDeal() that returns the best and worst deal value. Hint: aggregate method
   */
   @Test
   public void findBestAndAverageDealDeal() throws SQLException {
      final Pair<BigDecimal, BigDecimal> deals = dealDao.findBestAndWorstDeal();

      assertThat(deals).isNotNull();
      assertThat(deals.getOne()).isEqualTo(new BigDecimal("9.19"));
      assertThat(deals.getTwo()).isEqualTo(new BigDecimal("-9.88"));
   }

   /*
    TODO: Task 13 - implement method that returns report that contains the customer and best trade for the customer
    */
   @Test
   public void bestTradeReport() {
      final List<Pair<String, BigDecimal>> pairs = dealDao.customerTradeBestReport();

      assertTrade(pairs.get(0), "Williams", "1.52");
      assertTrade(pairs.get(1), "Brown", "9.19");
      assertTrade(pairs.get(2), "King", "3.26");
   }

   @Test
   public void customerTradeCountReport() {
      final List<Pair<String, Long>> pairs = dealDao.customerTradeCountReport();

      pairs.forEach(pair -> System.out.println(pair.getOne() + " - " + pair.getTwo()));

      assertTrade(pairs.get(0), "Williams", 3);
      assertTrade(pairs.get(1), "Brown", 8);
      assertTrade(pairs.get(2), "King", 6);

   }

   private void assertTrade(Pair<String, BigDecimal> row, String expectedLastName, String expectedBestPrice) {
      assertThat(row.getOne()).isEqualTo(expectedLastName);
      assertThat(row.getTwo()).isEqualTo(new BigDecimal(expectedBestPrice));
   }

   private void assertTrade(Pair<String, Long> row, String expectedLastName, long expectedCount) {
      assertThat(row.getOne()).isEqualTo(expectedLastName);
      assertThat(row.getTwo()).isEqualTo(expectedCount);
   }


}



