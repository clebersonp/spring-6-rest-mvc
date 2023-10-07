package guru.springframework.spring6restmvc.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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
  @Captor
  ArgumentCaptor<UUID> uuidArgumentCaptor;
  @Captor
  ArgumentCaptor<Customer> customerArgumentCaptor;

  @BeforeEach
  void setUp() {
    customerServiceImpl = new CustomerServiceImpl();
  }

  @Test
  void testDeleteCustomer() throws Exception {
    Customer customer = customerServiceImpl.getAllCustomers().get(0);

    mockMvc.perform(delete("/api/v1/customer/" + customer.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

    verify(customerService).deleteCustomerById(uuidArgumentCaptor.capture());

    assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
  }

  @Test
  void testUpdateCustomer() throws Exception {
    Customer customer = customerServiceImpl.getAllCustomers().get(0);

    mockMvc.perform(put("/api/v1/customer/" + customer.getId())
            .content(objectMapper.writeValueAsString(customer))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

    verify(customerService).updateCustomerById(uuidArgumentCaptor.capture(), any(Customer.class));

    assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
  }

  @Test
  void testCreateCustomer() throws Exception {
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
  void testPatchById() throws Exception {
    Customer customer = this.customerServiceImpl.getAllCustomers().get(0);

    Map<String, Object> customerMap = new HashMap<>();
    customerMap.put("name", "NEW NAME");

    this.mockMvc.perform(patch("/api/v1/customer/%s".formatted(customer.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(customerMap)))
        .andExpect(status().isNoContent());

    verify(this.customerService).patchCustomerById(this.uuidArgumentCaptor.capture(),
        this.customerArgumentCaptor.capture());

    assertThat(customer.getId()).isEqualTo(this.uuidArgumentCaptor.getValue());
    assertThat(customerMap.get("name")).isEqualTo(this.customerArgumentCaptor.getValue().getName());
  }
}










