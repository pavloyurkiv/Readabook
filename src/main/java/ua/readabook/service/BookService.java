package ua.readabook.service;

import org.springframework.data.domain.Pageable;
import ua.readabook.domain.BookDTO;
import ua.readabook.domain.filter.SimpleFilter;

import java.util.List;

public interface BookService {

    void saveBook(BookDTO book);

    List<BookDTO> findAllBooks();

    BookDTO findBookById(Long id);

    void deleteBookById(Long id);

    List<BookDTO> findBookByName(String name);

    List<BookDTO> findBooksByPage(Pageable pageable);
    
    void addImageToBook(String image, Long id);

    List<BookDTO> findAll(SimpleFilter filter);
    
    Boolean existsByName(String name);
}
