package dao.jpa;

import dto.Deal;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


public class JpaDealDaoTest {

   private EntityManagerFactory entityManagerFactory;
   private EntityManager entityManager;
   private JpaDealDao dealDao;

   @Before
   public void setUp() {
      entityManagerFactory = Persistence.createEntityManagerFactory("persistence");
      entityManager = entityManagerFactory.createEntityManager();

      dealDao = new JpaDealDao(entityManager);
   }

   @After
   public void tearDown() {
      entityManager.close();
      entityManagerFactory.close();
   }

   /*
   Task 9 - add pagination support to dealDao.find method. The page size is 3 records, pageNumber begins at 1.
    */
   @Test
   public void allDealsPaginated() throws SQLException {
      final List<Deal> deals = dealDao.find(null, null, null, 2);

      assertThat(deals).hasSize(3);
      assertThat(deals.stream().map(Deal::getId).collect(Collectors.toList())).isEqualTo(Arrays.asList(10004, 10005, 10006));

   }

}



