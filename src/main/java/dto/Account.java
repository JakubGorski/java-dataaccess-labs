package dto;

import javax.persistence.*;

@Entity
@Table(name = "accounts")
public class Account {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer id;

   @Column
   private String name;

   @ManyToOne
   private Customer customer;

   protected Account() {
   }

   public Account(String name) {
      this.name = name;
   }

   public Account(Integer id, String name, Customer customer) {
      this.id = id;
      this.name = name;
      this.customer = customer;
   }

   public Integer getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   public Customer getCustomer() {
      return customer;
   }

   public void setCustomer(Customer customer) {
      this.customer = customer;
   }

   @Override
   public String toString() {
      return "Account{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
   }
}
