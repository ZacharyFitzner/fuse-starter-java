package org.galatea.starter.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString

@Document
public class Book {

  @Id
  private int id;
  private String bookName;
  private String authorName;

  public Book(Integer id, String bookName, String authorName){
    this.id = id;
    this.bookName = bookName;
    this.authorName = authorName;
  }

}
