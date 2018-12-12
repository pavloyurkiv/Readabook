package ua.readabook.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ua.readabook.domain.BookDTO;
import ua.readabook.domain.filter.SimpleFilter;
import ua.readabook.entity.BookEntity;
import ua.readabook.exception.AlreadyExistsException;
import ua.readabook.exception.ResourceNotFoundException;
import ua.readabook.repository.BookRepository;
import ua.readabook.service.BookService;
import ua.readabook.utils.ObjectMapperUtils;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.List;

//@Component
@Log4j2
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapperUtils modelMapper;

    @Override
    public void saveBook(BookDTO bookDTO) {

        BookEntity bookEntity =
                bookRepository.findByName(bookDTO.getName());

        if(bookEntity != null) {
            throw new AlreadyExistsException("Book with name [" + bookDTO.getName() + "] already exists");
        }


        BookEntity book = modelMapper.map(bookDTO, BookEntity.class);
        bookRepository.save(book);
    }

    @Override
    public List<BookDTO> findAllBooks() {
        List<BookEntity> bookEntities = bookRepository.findAll();
        List<BookDTO> bookDTOS = modelMapper.mapAll(bookEntities, BookDTO.class);
        return bookDTOS;
    }

    @Override
    public BookDTO findBookById(Long id) {
        BookEntity bookEntity =
                bookRepository.findById(id)
                        .orElseThrow(
                                () -> new ResourceNotFoundException("Book with id[" + id + "] not found")
                        );

        BookDTO bookDTO = modelMapper.map(bookEntity, BookDTO.class);
        return bookDTO;
    }

    @Override
    public void deleteBookById(Long id) {
        BookEntity book = bookRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Could not delete book with id[" + id + "]. Not found")
                );
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDTO> findBookByName(String name) {
        List<BookEntity> books = bookRepository.findByNameLike("%" + name + "%");

        if (books == null) {
            throw new ResourceNotFoundException("Book with name[" + name + "] not found");
        }
        return modelMapper.mapAll(books, BookDTO.class);
    }

    @Override
    public List<BookDTO> findBooksByPage(Pageable pageable) {
        String mName = "findBooksByPage";
        log.debug("Begin: " + mName);
        int pageSize = pageable.getPageSize();
        if(pageable.getPageSize() > 50) {
            log.debug("Entered value > 50. Use default");
            pageSize = 50;
        }
        PageRequest request = PageRequest.of(pageable.getPageNumber(), // 0
                                          pageSize, //pageable.getPageSize(), // 10
                                          pageable.getSort()); // id
        Page<BookEntity> bookEntityPage = bookRepository.findAll(request);

        List<BookDTO> bookDtos =
                modelMapper.mapAll(bookEntityPage.getContent(), BookDTO.class);
        log.debug("Finish: " + mName);
        return bookDtos;
    }
    
    @Override
	public void addImageToBook(String image, Long id) {
		BookEntity bookEntity = bookRepository.findById(id)
				.orElseThrow(
						() -> new ResourceNotFoundException("Book with id [" + id + "] not found")
				);
		bookEntity.setImage(image);
		bookRepository.save(bookEntity);
	}

	@Override
	public Boolean existsByName(String name) {
		return bookRepository.existsByName(name);
	}

    @Override
    public List<BookDTO> findAll(SimpleFilter filter) {
        return modelMapper.mapAll(
                bookRepository.findAll(getSpecification(filter)),
                BookDTO.class
        );
    }

    private Specification<BookEntity> getSpecification(SimpleFilter filter) {
        return new Specification<BookEntity>() {

            private static final long serialVersionUID = 2520387034841361491L;

            @Override
            public Predicate toPredicate(Root<BookEntity> root, CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {



                Expression<String> bookNameExp = root.get("name");
                Predicate bookNamePredicate =
                        criteriaBuilder.like(bookNameExp, "%" + filter.getSearch() + "%");

//                Expression<BigDecimal> priceExp = root.get("price");
//                Predicate pricePredicate =
//                        criteriaBuilder.between(priceExp, filter.getMin(), filter.getMax());

//                return criteriaBuilder.and(bookNamePredicate, pricePredicate);
                return criteriaBuilder.and(bookNamePredicate);
            }
        };

    }

}
