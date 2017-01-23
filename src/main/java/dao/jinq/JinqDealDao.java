package dao.jinq;

import dto.Deal;
import org.jinq.jpa.JPAJinqStream;
import org.jinq.jpa.JinqJPAStreamProvider;
import org.jinq.tuples.Pair;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

class JinqDealDao {

   private final EntityManager em;
   private final JinqJPAStreamProvider streams;

   JinqDealDao(EntityManager entityManager) {
      this.em = entityManager;
      this.streams = new JinqJPAStreamProvider(entityManager.getMetamodel());
   }

   List<Deal> find(String instrumentName, String customerLastName, String accountName) {
      return deals().where(deal ->
            (instrumentName == null || instrumentName.equals(deal.getInstrument().getName())
                  && (customerLastName == null || customerLastName.equals(deal.getAccount().getCustomer().getLastName()))
                  && (accountName == null || deal.getAccount().getName().equals(accountName))
      )).collect(Collectors.toList());
   }

   Pair<BigDecimal, BigDecimal> findBestAndWorstDeal() {
      return null;
   }

   List<Pair<String, BigDecimal>> customerTradeBestReport() {
      return null;
   }


   List<Pair<String, Long>> customerTradeCountReport() {
      return deals().
                          group(deal -> deal.getAccount().getCustomer().getLastName(),
                                (name, deal) -> deal.count()
                          ).collect(toList());
   }

   private JPAJinqStream<Deal> deals() {
      return streams.streamAll(em, Deal.class);
   }
}


