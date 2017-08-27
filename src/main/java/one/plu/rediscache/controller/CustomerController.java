package one.plu.rediscache.controller;

import one.plu.rediscache.CacheExpire;
import one.plu.rediscache.domain.Customer;
import one.plu.rediscache.repo.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@CacheConfig(keyGenerator = "keyGenerator")
public class CustomerController {

    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

    CustomerRepository customerRepository;

    @Autowired
    public void setCustomerRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping("/customers/{customerId}")
    @Cacheable(cacheNames = "customer")
    @CacheExpire(expireAfter = 10)
    public Customer findCustomer(@PathVariable Long customerId) {
        log.info("cache not working");
        return customerRepository.findOne(customerId);
    }

    @GetMapping(value = "/customers")
    @Cacheable(cacheNames = "customers")
    public Iterable<Customer> findAll() {
        log.info("cache not working");
        return customerRepository.findAll();
    }
}
