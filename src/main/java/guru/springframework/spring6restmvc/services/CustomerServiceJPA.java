package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.mappers.CustomerMapper;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * Created by jt, Spring Framework Guru.
 */
@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJPA implements CustomerService {

  private final CustomerRepository customerRepository;
  private final CustomerMapper customerMapper;

  @Override
  public Optional<CustomerDTO> getCustomerById(UUID uuid) {
    return this.customerRepository.findById(uuid).map(this.customerMapper::customerToCustomerDto);
  }

  @Override
  public List<CustomerDTO> getAllCustomers() {
    return this.customerRepository.findAll().stream().map(this.customerMapper::customerToCustomerDto).toList();
  }

  @Override
  public CustomerDTO saveNewCustomer(CustomerDTO customer) {
    return null;
  }

  @Override
  public void updateCustomerById(UUID customerId, CustomerDTO customer) {

  }

  @Override
  public void deleteCustomerById(UUID customerId) {

  }

  @Override
  public void patchCustomerById(UUID customerId, CustomerDTO customer) {

  }
}
