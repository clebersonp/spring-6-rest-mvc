package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.Customer;
import guru.springframework.spring6restmvc.services.CustomerService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jt, Spring Framework Guru.
 */
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
@RestController
public class CustomerController {

  private final CustomerService customerService;

  @PutMapping("{customerId}")
  public ResponseEntity updateCustomerByID(@PathVariable("customerId") UUID customerId,
      @RequestBody Customer customer) {

    customerService.updateCustomerById(customerId, customer);

    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @PostMapping
  public ResponseEntity handlePost(@RequestBody Customer customer) {
    Customer savedCustomer = customerService.saveNewCustomer(customer);

    HttpHeaders headers = new HttpHeaders();
    headers.add("Location", "/api/v1/customer/" + savedCustomer.getId().toString());

    return new ResponseEntity(headers, HttpStatus.CREATED);
  }

  @RequestMapping(method = RequestMethod.GET)
  public List<Customer> listAllCustomers() {
    return customerService.getAllCustomers();
  }

  @RequestMapping(value = "{customerId}", method = RequestMethod.GET)
  public Customer getCustomerById(@PathVariable("customerId") UUID id) {
    return customerService.getCustomerById(id);
  }

  @DeleteMapping("{customerId}")
  public ResponseEntity<?> deleteById(@PathVariable("customerId") UUID customerId) {
    this.customerService.deleteById(customerId);
    return ResponseEntity.noContent().build();

  }

}
