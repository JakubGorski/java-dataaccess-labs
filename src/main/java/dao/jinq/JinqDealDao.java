package dao.jinq;

import dto.Deal;
import org.jinq.jpa.JPAJinqStream;
import org.jinq.jpa.JinqJPAStreamProvider;
import org.jinq.tuples.Pair;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

import static java.util.stream.Collectors.toList;

class JinqDealDao {

   private final EntityManager em;
   private final JinqJPAStreamProvider streams;

   JinqDealDao(EntityManager entityManager) {
      this.em = entityManager;
      this.streams = new JinqJPAStreamProvider(entityManager.getMetamodel());
   }

   List<Deal> find(String instrumentName, String customerLastName, String accountName) {

      JPAJinqStream<Deal> deals = deals();

      if (instrumentName != null) {
         deals = deals.where(deal -> deal.getInstrument().getName().equals(instrumentName));
      }

      if (customerLastName != null) {
         deals = deals.where(deal -> deal.getAccount().getCustomer().getLastName().equals(customerLastName));
      }

      if (accountName != null) {
         deals = deals.where(deal -> deal.getAccount().getName().equals(accountName));
      }

      return deals.collect(toList());
   }

   Pair<BigDecimal, BigDecimal> findBestAndWorstDeal() {
      return deals().aggregate(
            streams1 -> streams1.max(deal -> deal.getClosePrice().subtract(deal.getOpenPrice())),
            streams1 -> streams1.min(deal -> deal.getClosePrice().subtract(deal.getOpenPrice())));
   }

   List<Pair<String, Long>> customerTradeCountReport() {
      return deals().
                          group(deal -> deal.getAccount().getCustomer().getLastName(),
                                (name, deal) -> deal.count()
                          ).collect(toList());
   }


   List<Pair<String, BigDecimal>> customerTradeBestReport() {
      return deals().
                          group(deal -> deal.getAccount().getCustomer().getLastName(),
                                (name, deal) -> deal.max(d -> d.getClosePrice().subtract(d.getOpenPrice()))
                          ).collect(toList());
   }

   private JPAJinqStream<Deal> deals() {
      return streams.streamAll(em, Deal.class);
   }
}


