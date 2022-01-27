package com.leightek.cache.service;

import com.leightek.cache.model.Customer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@CacheConfig(cacheNames = {"addresses"})
public class CustomerDataService {

    private static final Log LOG = LogFactory.getLog(CustomerDataService.class);

    @Cacheable(value = "address", key = "#customer.name")
    public String getAddress(final Customer customer) {
        LOG.info("Inside getAddress()");
        return customer.getAddress();
    }
}
