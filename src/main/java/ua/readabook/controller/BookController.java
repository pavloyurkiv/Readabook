package ua.readabook.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ua.readabook.domain.BookDTO;
import ua.readabook.domain.ErrorDTO;
import ua.readabook.domain.filter.SimpleFilter;
import ua.readabook.service.FileStorageService;
import ua.readabook.service.BookService;

@RestController
@RequestMapping("books")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping
    public ResponseEntity<?> createBook(@Valid @RequestBody BookDTO book, BindingResult br) {

        if (br.hasErrors()) {
            System.out.println("Validation error");

            String errMsg = br.getFieldErrors()
                        .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .findFirst().get().toString();
            ErrorDTO error = new ErrorDTO(errMsg);
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        System.out.println(book.getName());
        bookService.saveBook(book);
        return new ResponseEntity<Void>(HttpStatus.CREATED); // 201
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> getBooks() {
        List<BookDTO> books = bookService.findAllBooks();
        return new ResponseEntity<List<BookDTO>>(books, HttpStatus.OK);
    }

    @GetMapping("{bookId}")
    public ResponseEntity<BookDTO> getBookById(
            @PathVariable("bookId") Long id
    ) {
        BookDTO book = bookService.findBookById(id);
        return new ResponseEntity<BookDTO>(book, HttpStatus.OK);
    }

    @DeleteMapping("{bookId}")
    public ResponseEntity<?> deleteBook(
            @PathVariable("bookId") Long id
    ) {
        bookService.deleteBookById(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

//    @GetMapping("search")
//    public ResponseEntity<?> getBookByName(
//            @RequestParam("name") String name,
//            @RequestParam("min") BigDecimal min,
//            @RequestParam("max") BigDecimal max
//    ) {
//        System.out.println(name + " " + min + " " + max);
//        SimpleFilter filter = new SimpleFilter();
//        filter.setSearch(name);
////        filter.setMin(min);
////        filter.setMax(max);
//
//        List<BookDTO> bookDTOs = bookService.findAll(filter);
//        return new ResponseEntity<List<BookDTO>>(bookDTOs, HttpStatus.OK);
//    }

     @GetMapping("search")
     public ResponseEntity<?> getBookByName(
        @RequestParam("name") String name
     ) {
    List<BookDTO> bookDTOs = bookService.findBookByName(name);
    return new ResponseEntity<List<BookDTO>>(bookDTOs, HttpStatus.OK);
    }

    @GetMapping("pages")
    public ResponseEntity<?> getBooksByPage(
            @PageableDefault Pageable pageable
    ) {
        List<BookDTO> bookDTOS = bookService.findBooksByPage(pageable);

        return new ResponseEntity<>(bookDTOS, HttpStatus.OK);
    }

    @PostMapping("{bookId}/image")
    public ResponseEntity<?> uploadImage(
            @PathVariable("bookId") Long id,
            @RequestParam("file") MultipartFile file
            ) {
        System.out.println(file.getOriginalFilename());
        fileStorageService.storeFile(file);
        bookService.addImageToBook(file.getOriginalFilename(), id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("image")
    public ResponseEntity<?> getFile(
            @RequestParam("fileName") String fileName,
            HttpServletRequest request) {
    	
        Resource resource = fileStorageService.loadFile(fileName);

        String contentType = null;

        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline: filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }



    @GetMapping("check")
    public ResponseEntity<?> checkBookName(
    		@RequestParam("name") String name
    		) {
    	return new ResponseEntity<>(
    			bookService.existsByName(name),
    			HttpStatus.OK
    		);    	
    }
}
