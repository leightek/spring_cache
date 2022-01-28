package com.leightek.cache.repositorties;

import com.leightek.cache.model.SimpleBook;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;
import java.util.UUID;

@ContextConfiguration
public interface SimpleBookRepository extends CrudRepository<SimpleBook, UUID> {

    @Cacheable(value = "simpleBooks", unless = "#a0 == 'Foundation'")
    Optional<SimpleBook> findFirstByTitle(String title);
}
