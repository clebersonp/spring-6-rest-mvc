package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.Customer;
import guru.springframework.spring6restmvc.services.CustomerService;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Created by jt, Spring Framework Guru.
 */
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
@RestController
public class CustomerController {

  private final CustomerService customerService;

  @RequestMapping(method = RequestMethod.GET)
  public List<Customer> listAllCustomers() {
    return customerService.getAllCustomers();
  }

  @RequestMapping(value = "{customerId}", method = RequestMethod.GET)
  public Customer getCustomerById(@PathVariable("customerId") UUID id) {
    return customerService.getCustomerById(id);
  }

  @PostMapping
  public ResponseEntity<?> handlePost(@RequestBody Customer customer) {
    Customer savedCustomer = this.customerService.saveNewCustomer(customer);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest().pathSegment("{customerId}")
        .build(savedCustomer.getId());
    return ResponseEntity.created(location).build();
  }

}
