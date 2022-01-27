package com.leightek.cache.service;

import com.leightek.cache.config.CachingConfig;
import com.leightek.cache.model.Customer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CachingConfig.class}, loader = AnnotationConfigContextLoader.class)
public class SpringCachingIntegrationTest {

    @Autowired
    private CustomerDataService service;

    @Test
    public void whenGettingAddress_thenCorrect() {
        final Customer customer = new Customer("John", "1000 Bay Street, Toronto");
        String address = service.getAddress(customer);
        Assert.assertNotNull(address);

        address = service.getAddress(customer);
        Assert.assertNotNull(address);
    }
}
