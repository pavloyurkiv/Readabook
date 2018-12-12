package ua.readabook.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class BookDTO {
    private Long id;


    @NotNull(message = "Field 'Name' can't be null")
    @Size(min = 2, max = 200, message = "'Name' length should be between 2 and 200")
    private String name;

    @NotNull(message = "Field 'Author' can't be null")
    @Size(min = 2, max = 200, message = "'Author' length should be between 2 and 200")
    private String author;

    private String image;

    @Size(min = 1)
    private String annotation;

    @NotNull(message = "Field 'link' can't be null")
    private String link;

    @NotNull(message = "Field 'category' can't be null")
    private CategoryDTO category;

    @NotNull(message = "Field 'lang' can't be null")
    private LangDTO lang;
}

