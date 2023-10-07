package guru.springframework.spring6restmvc.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author cleberson
 */
@SpringBootTest
class CustomerControllerIT {

  @Autowired
  CustomerController customerController;

  @Autowired
  CustomerRepository customerRepository;

  @Rollback
  @Transactional
  @Test
  void testEmptyList() {
    this.customerRepository.deleteAll();
    List<CustomerDTO> allCustomers = this.customerController.listAllCustomers();

    assertThat(allCustomers).isEmpty();
  }

  @Test
  void testListAllCustomers() {
    List<CustomerDTO> allCustomers = this.customerController.listAllCustomers();

    assertThat(allCustomers.size()).isGreaterThan(0);
  }

  @Test
  void testGetCustomerById() {
    Customer customer = this.customerRepository.findAll().get(0);
    CustomerDTO customerDTO = this.customerController.getCustomerById(customer.getId());

    assertThat(customerDTO).isNotNull();
  }

  @Test
  void testGetCustomerByIdNotFound() {
    assertThrows(NotFoundException.class, () -> {
      this.customerController.getCustomerById(UUID.randomUUID());
    });
  }
}