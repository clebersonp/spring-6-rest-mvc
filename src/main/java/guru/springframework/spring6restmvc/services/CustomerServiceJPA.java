package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.mappers.CustomerMapper;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    return Optional.ofNullable(customerMapper.customerToCustomerDto(customerRepository.findById(uuid).orElse(null)));
  }

  @Override
  public List<CustomerDTO> getAllCustomers() {
    return customerRepository.findAll().stream().map(customerMapper::customerToCustomerDto)
        .collect(Collectors.toList());
  }

  @Override
  public CustomerDTO saveNewCustomer(CustomerDTO customer) {
    return this.customerMapper.customerToCustomerDto(
        this.customerRepository.save(this.customerMapper.customerDtoToCustomer(customer)));
  }

  @Override
  public Optional<CustomerDTO> updateCustomerById(UUID customerId, CustomerDTO customer) {
    AtomicReference<Optional<CustomerDTO>> atomicReference = new AtomicReference<>();
    this.getCustomerById(customerId).ifPresentOrElse(customerDTO -> {
      customerDTO.setName(customer.getName());
      customerDTO.setUpdateDate(LocalDateTime.now());
      atomicReference.set(Optional.of(this.customerMapper.customerToCustomerDto(
          this.customerRepository.save(this.customerMapper.customerDtoToCustomer(customerDTO)))));
    }, () -> atomicReference.set(Optional.empty()));
    return atomicReference.get();
  }

  @Override
  public boolean deleteCustomerById(UUID customerId) {
    if (this.customerRepository.existsById(customerId)) {
      this.customerRepository.deleteById(customerId);
      return true;
    }
    return false;
  }

  @Override
  public Optional<CustomerDTO> patchCustomerById(UUID customerId, CustomerDTO customer) {
    AtomicReference<Optional<CustomerDTO>> atomicReference = new AtomicReference<>();
    this.getCustomerById(customerId).ifPresentOrElse(customerDTO -> {
      if (StringUtils.hasText(customer.getName())) {
        customerDTO.setName(customer.getName());
      }
      customerDTO.setUpdateDate(LocalDateTime.now());
      atomicReference.set(Optional.of(this.customerMapper.customerToCustomerDto(
          this.customerRepository.save(this.customerMapper.customerDtoToCustomer(customerDTO)))));
    }, () -> atomicReference.set(Optional.empty()));
    return atomicReference.get();
  }
}
