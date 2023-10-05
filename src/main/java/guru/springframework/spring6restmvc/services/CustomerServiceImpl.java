package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.Customer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 * Created by jt, Spring Framework Guru.
 */
@Service
public class CustomerServiceImpl implements CustomerService {

  private Map<UUID, Customer> customerMap;

  public CustomerServiceImpl() {
    Customer customer1 = Customer.builder().id(UUID.randomUUID()).name("Customer 1").version(1)
        .createdDate(LocalDateTime.now()).updateDate(LocalDateTime.now()).build();

    Customer customer2 = Customer.builder().id(UUID.randomUUID()).name("Customer 2").version(1)
        .createdDate(LocalDateTime.now()).updateDate(LocalDateTime.now()).build();

    Customer customer3 = Customer.builder().id(UUID.randomUUID()).name("Customer 3").version(1)
        .createdDate(LocalDateTime.now()).updateDate(LocalDateTime.now()).build();

    customerMap = new HashMap<>();
    customerMap.put(customer1.getId(), customer1);
    customerMap.put(customer2.getId(), customer2);
    customerMap.put(customer3.getId(), customer3);
  }

  @Override
  public Customer getCustomerById(UUID uuid) {
    return customerMap.get(uuid);
  }

  @Override
  public List<Customer> getAllCustomers() {
    return new ArrayList<>(customerMap.values());
  }

  @Override
  public Customer saveNewCustomer(Customer customer) {
    Customer savedCustomer = Customer.builder().id(UUID.randomUUID()).createdDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now()).version(1).name(customer.getName()).build();
    this.customerMap.put(savedCustomer.getId(), savedCustomer);
    return savedCustomer;
  }
}











