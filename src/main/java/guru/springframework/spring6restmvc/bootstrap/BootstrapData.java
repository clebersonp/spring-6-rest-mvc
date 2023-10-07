package guru.springframework.spring6restmvc.bootstrap;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author cleberson
 */
@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

  private final BeerRepository beerRepository;
  private final CustomerRepository customerRepository;

  @Override
  public void run(String... args) throws Exception {
    this.loadBeerData();
    this.loadCustomerData();
  }

  private void loadBeerData() {
    if (this.beerRepository.count() == 0) {
      Beer beer1 = Beer.builder()
          .beerName("Galaxy Cat")
          .beerStyle(BeerStyle.PALE_ALE)
          .upc("12356")
          .price(new BigDecimal("12.99"))
          .quantityOnHand(122)
          .createdDate(LocalDateTime.now())
          .updateDate(LocalDateTime.now())
          .build();

      Beer beer2 = Beer.builder()
          .beerName("Crank")
          .beerStyle(BeerStyle.PALE_ALE)
          .upc("12356222")
          .price(new BigDecimal("11.99"))
          .quantityOnHand(392)
          .createdDate(LocalDateTime.now())
          .updateDate(LocalDateTime.now())
          .build();

      Beer beer3 = Beer.builder()
          .beerName("Sunshine City")
          .beerStyle(BeerStyle.IPA)
          .upc("12356")
          .price(new BigDecimal("13.99"))
          .quantityOnHand(144)
          .createdDate(LocalDateTime.now())
          .updateDate(LocalDateTime.now())
          .build();

      this.beerRepository.saveAll(Arrays.asList(beer1, beer2, beer3));
    }
  }

  private void loadCustomerData() {
    if (this.customerRepository.count() == 0) {
      Customer customer1 = Customer.builder()
          .name("Customer 1")
          .createdDate(LocalDateTime.now())
          .updateDate(LocalDateTime.now())
          .build();

      Customer customer2 = Customer.builder()
          .name("Customer 2")
          .createdDate(LocalDateTime.now())
          .updateDate(LocalDateTime.now())
          .build();

      Customer customer3 = Customer.builder()
          .name("Customer 3")
          .createdDate(LocalDateTime.now())
          .updateDate(LocalDateTime.now())
          .build();

      this.customerRepository.saveAll(Arrays.asList(customer1, customer2, customer3));
    }
  }
}
