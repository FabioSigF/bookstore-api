package com.bookstore.jpa.services;

import com.bookstore.jpa.dtos.BookRecordDto;
import com.bookstore.jpa.models.AuthorModel;
import com.bookstore.jpa.models.BookModel;
import com.bookstore.jpa.models.ReviewModel;
import com.bookstore.jpa.repositories.AuthorRepository;
import com.bookstore.jpa.repositories.BookRepository;
import com.bookstore.jpa.repositories.PublisherRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;


    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, PublisherRepository publisherRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
    }

    public List<BookModel> getAllBooks(){
        return bookRepository.findAll();
    }

    @Transactional
    public BookModel saveBook(BookRecordDto bookRecordDto) {
        BookModel book = new BookModel();
        book.setName(bookRecordDto.title());
        //Busca publisher na base de dados e retorna para o setPublisher
        book.setPublisher(publisherRepository.findById(bookRecordDto.publisherId()).get());
        //Busca autores do livro pelo dto, recebe uma lista de Author, transforma em Stream e retorna em forma de Collection
        book.setAuthors(new HashSet<>(authorRepository.findAllById(bookRecordDto.authorIds())));

        ReviewModel reviewModel = new ReviewModel();
        reviewModel.setComment(bookRecordDto.reviewComment());
        reviewModel.setBook(book);

        book.setReview(reviewModel);

        return bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(UUID id) {
        bookRepository.deleteById(id);
    }
}
