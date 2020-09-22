package org.galatea.starter.domain.rpsy.bookrepo;

import org.galatea.starter.domain.Book;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface BookRepository extends MongoRepository<Book, Integer> {


}
