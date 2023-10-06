package guru.springframework.spring6restmvc.controller;


import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import guru.springframework.spring6restmvc.model.Customer;
import guru.springframework.spring6restmvc.services.CustomerService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author cleberson
 */
@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

  private static List<Customer> customers;
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private CustomerService customerService;

  @BeforeAll
  static void setUp() {
    loadCustomer();
  }

  private static void loadCustomer() {
    customers = new ArrayList<>();
    Customer john = Customer.builder().id(UUID.randomUUID()).name("John").version(1)
        .createdDate(LocalDateTime.now().minusDays(10)).updateDate(LocalDateTime.now().minusDays(5)).build();
    Customer mat = Customer.builder().id(UUID.randomUUID()).name("Mat").version(1)
        .createdDate(LocalDateTime.now().minusDays(8)).updateDate(LocalDateTime.now().minusDays(3)).build();
    customers.add(john);
    customers.add(mat);
  }

  @Test
  void listAllCustomers() throws Exception {
    given(this.customerService.getAllCustomers()).willReturn(customers);
    this.mockMvc.perform(
            get("/api/v1/customer").accept(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()", is(2)))
        .andExpect(jsonPath("$.[0].id", is(customers.get(0).getId().toString())));
  }

  @Test
  void getCustomerById() throws Exception {
    Customer customer = customers.get(0);
    given(this.customerService.getCustomerById(customer.getId())).willReturn(customer);
    this.mockMvc.perform(
            get("/api/v1/customer/%s".formatted(customer.getId()))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is(customer.getId().toString())))
        .andExpect(jsonPath("$.name", is(customer.getName())));
  }
}