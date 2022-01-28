package com.leightek.cache.repositories;

import com.leightek.cache.model.SimpleBook;
import com.leightek.cache.repositorties.SimpleBookRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.AopTestUtils;

import java.util.UUID;

import static java.util.Optional.of;
import static org.mockito.Mockito.*;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class SimpleBookCachingIntegrationTest {

    private static final SimpleBook DOE = new SimpleBook(UUID.randomUUID(), "Doe");
    private static final SimpleBook FOUNDATION = new SimpleBook(UUID.randomUUID(), "Foundation");

    private SimpleBookRepository mock;

    @Autowired
    private SimpleBookRepository simpleBookRepository;

    @EnableCaching
    @Configuration
    public static class CachingTestConfig {

        @Bean
        public SimpleBookRepository simpleBookRepositoryMockImplementation() {
            return mock(SimpleBookRepository.class);
        }

        @Bean
        public CacheManager cacheManager() {
            return new ConcurrentMapCacheManager("simpleBooks");
        }
    }

    @Before
    public void setup() {
        mock = AopTestUtils.getTargetObject(simpleBookRepository);

        reset(mock);

        when(mock.findFirstByTitle(eq("Foundation"))).thenReturn(of(FOUNDATION));
        when(mock.findFirstByTitle(eq("Doe"))).thenReturn(of(DOE)).thenThrow(new RuntimeException(
                "Book should be cached!"));
    }

    @Test
    public void givenCacheSimpleBook_whenFindByTitle_thenRepositoryShouldNotBeHit() {
        Assert.assertEquals(of(DOE), simpleBookRepository.findFirstByTitle("Doe"));
        verify(mock).findFirstByTitle("Doe");

        Assert.assertEquals(of(DOE), simpleBookRepository.findFirstByTitle("Doe"));
        Assert.assertEquals(of(DOE), simpleBookRepository.findFirstByTitle("Doe"));

        verifyNoMoreInteractions(mock);
    }

    @Test
    public void givenNotCachedSimpleBook_whenFindByTitle_thenRepositoryShouldBeHit() {
        Assert.assertEquals(of(FOUNDATION), simpleBookRepository.findFirstByTitle("Foundation"));
        Assert.assertEquals(of(FOUNDATION), simpleBookRepository.findFirstByTitle("Foundation"));
        Assert.assertEquals(of(FOUNDATION), simpleBookRepository.findFirstByTitle("Foundation"));

        verify(mock, times(3)).findFirstByTitle("Foundation");
    }
}
