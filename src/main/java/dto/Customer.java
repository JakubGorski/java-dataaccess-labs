package dto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity()
@Table(name = "customers")
public class Customer {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer id;

   @Column(name = "first_name")
   private String firstName;
   @Column(name = "last_name")
   private String lastName;

   @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
   @JoinColumn(name = "customer_id")
   private List<Account> accounts;


   private byte[] picture;



   protected Customer() {
   }

   public Customer(Integer id, String firstName, String lastName, List<Account> accounts, byte[] picture) {
      this.id = id;
      this.firstName = firstName;
      this.lastName = lastName;
      this.accounts = accounts;
      this.picture = picture;
   }

   public Customer(Integer id, String firstName, String lastName) {
      this(id, firstName, lastName, new ArrayList<>(), null);
   }

   public Customer(String firstName, String lastName) {
      this(null, firstName, lastName);
   }

   public Integer getId() {
      return id;
   }

   public String getFirstName() {
      return firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public List<Account> getAccounts() {
      return accounts;
   }

   public byte[] getPicture() {
      return picture;
   }

   public void addAccount(Account account) {
      account.setCustomer(this);
      getAccounts().add(account);
   }


   @Override
   public String toString() {
      return "Customer{" +
            "id=" + id +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", accounts=" + accounts +
            '}';
   }
}
