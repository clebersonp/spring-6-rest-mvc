package guru.springframework.spring6restmvc.controller;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6restmvc.model.Customer;
import guru.springframework.spring6restmvc.services.CustomerService;
import guru.springframework.spring6restmvc.services.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

  @MockBean
  CustomerService customerService;

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  CustomerServiceImpl customerServiceImpl;

  @BeforeEach
  void setUp() {
    customerServiceImpl = new CustomerServiceImpl();
  }

  @Test
  void testCreatCustomer() throws Exception {
    Customer customer = customerServiceImpl.getAllCustomers().get(0);
    customer.setId(null);
    customer.setVersion(null);

    given(customerService.saveNewCustomer(any(Customer.class)))
        .willReturn(customerServiceImpl.getAllCustomers().get(1));

    mockMvc.perform(post("/api/v1/customer").contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(customer)))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"));
  }

  @Test
  void listAllCustomers() throws Exception {
    given(customerService.getAllCustomers()).willReturn(customerServiceImpl.getAllCustomers());

    mockMvc.perform(get("/api/v1/customer")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()", is(3)));
  }

  @Test
  void getCustomerById() throws Exception {
    Customer customer = customerServiceImpl.getAllCustomers().get(0);

    given(customerService.getCustomerById(customer.getId())).willReturn(customer);

    mockMvc.perform(get("/api/v1/customer/" + customer.getId())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.name", is(customer.getName())));

  }

  @Test
  void updateCustomerById() throws Exception {
    Customer customer = this.customerServiceImpl.getAllCustomers().get(0);

    willDoNothing().given(this.customerService).updateCustomerById(customer.getId(), customer);

    this.mockMvc.perform(
            put("/api/v1/customer/%s".formatted(customer.getId().toString()))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(customer)))
        .andExpect(status().isNoContent());

    verify(this.customerService, times(1)).updateCustomerById(customer.getId(), customer);
  }
}










