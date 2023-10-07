package guru.springframework.spring6restmvc.bootstrap;

import static org.assertj.core.api.Assertions.assertThat;

import guru.springframework.spring6restmvc.repositories.BeerRepository;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * @author cleberson
 */
@DataJpaTest
class BootstrapDataTest {

  @Autowired
  BeerRepository beerRepository;

  @Autowired
  CustomerRepository customerRepository;

  BootstrapData bootstrapData;

  @BeforeEach
  void beforeEach() throws Exception {
    this.bootstrapData = new BootstrapData(beerRepository, customerRepository);
    this.bootstrapData.run((String) null);
  }

  @Test
  void testRun() {
    this.assertLoadBeerData();
    this.assertLoadCustomerData();
  }

  void assertLoadBeerData() {
    assertThat(this.beerRepository.count()).isGreaterThan(0);
  }

  void assertLoadCustomerData() {
    assertThat(this.customerRepository.count()).isGreaterThan(0);
  }
}