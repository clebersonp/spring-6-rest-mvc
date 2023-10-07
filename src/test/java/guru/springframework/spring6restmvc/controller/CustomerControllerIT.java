package guru.springframework.spring6restmvc.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.mappers.CustomerMapper;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class CustomerControllerIT {

  @Autowired
  CustomerRepository customerRepository;

  @Autowired
  CustomerController customerController;

  @Autowired
  CustomerMapper customerMapper;

  @Rollback
  @Transactional
  @Test
  void testListAllEmptyList() {
    customerRepository.deleteAll();
    List<CustomerDTO> dtos = customerController.listAllCustomers();

    assertThat(dtos.size()).isEqualTo(0);
  }

  @Test
  void testListAll() {
    List<CustomerDTO> dtos = customerController.listAllCustomers();

    assertThat(dtos.size()).isEqualTo(3);
  }

  @Test
  void testGetByIdNotFound() {
    assertThrows(NotFoundException.class, () -> {
      customerController.getCustomerById(UUID.randomUUID());
    });
  }

  @Test
  void testGetById() {
    Customer customer = customerRepository.findAll().get(0);
    CustomerDTO customerDTO = customerController.getCustomerById(customer.getId());
    assertThat(customerDTO).isNotNull();
  }

  @Rollback
  @Transactional
  @Test
  void testSaveNewCustomer() {
    LocalDateTime now = LocalDateTime.now();
    CustomerDTO newCustomer = CustomerDTO.builder().name("NEW CUSTOMER").createdDate(now).updateDate(now).build();

    ResponseEntity<?> responseEntity = this.customerController.handlePost(newCustomer);

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
    assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

    String[] pathFragment = responseEntity.getHeaders().getLocation().getPath().split("/");
    UUID savedUUID = UUID.fromString(pathFragment[pathFragment.length - 1]);

    Optional<Customer> customerOpt = this.customerRepository.findById(savedUUID);

    assertThat(customerOpt).isPresent();
  }

  @Rollback
  @Transactional
  @Test
  void testUpdateByIdFound() {
    // prepared the data
    Customer customer = this.customerRepository.findAll().get(0);
    CustomerDTO customerDTO = this.customerMapper.customerToCustomerDto(customer);
    customerDTO.setId(null);
    customerDTO.setVersion(null);
    final String nameUpdated = "NAME UPDATED";
    customerDTO.setName(nameUpdated);

    // perform the operation
    ResponseEntity<?> responseEntity = this.customerController.updateCustomerByID(customer.getId(), customerDTO);

    // check the result
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

    Optional<Customer> customerUpdatedOpt = this.customerRepository.findById(customer.getId());

    assertThat(customerUpdatedOpt).isPresent().get()
        .matches(customerUpdated -> customerUpdated.getName().equalsIgnoreCase(nameUpdated),
            "value for name equal to ( %s ) but was ( %s )".formatted(nameUpdated, customerUpdatedOpt.get().getName()));
  }

  @Rollback
  @Transactional
  @Test
  void testUpdatedByIdNotFound() {
    assertThrows(NotFoundException.class, () -> {
      this.customerController.updateCustomerByID(UUID.randomUUID(), CustomerDTO.builder().build());
    });
  }

  @Rollback
  @Transactional
  @Test
  void testDeleteByIdFound() {
    Customer customer = this.customerRepository.findAll().get(0);

    ResponseEntity<?> responseEntity = this.customerController.deleteCustomerById(customer.getId());

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

    assertThat(this.customerRepository.findById(customer.getId())).isEmpty();
  }

  @Rollback
  @Transactional
  @Test
  void testDeleteByIdNotFound() {
    assertThrows(NotFoundException.class, () -> {
      this.customerController.deleteCustomerById(UUID.randomUUID());
    });
  }

  @Rollback
  @Transactional
  @Test
  void testPatchByIdFound() {
    Customer customer = this.customerRepository.findAll().get(0);
    CustomerDTO customerDTO = this.customerMapper.customerToCustomerDto(customer);
    final String nameUpdated = "NAME UPDATED";
    customerDTO.setName(nameUpdated);
    customerDTO.setId(null);
    customerDTO.setVersion(null);

    ResponseEntity<?> responseEntity = this.customerController.patchCustomerById(customer.getId(), customerDTO);

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

    Optional<Customer> customerUpdatedOpt = this.customerRepository.findById(customer.getId());

    assertThat(customerUpdatedOpt).isPresent();
    assertThat(customerUpdatedOpt.get().getName()).isEqualTo(nameUpdated);

  }
}










