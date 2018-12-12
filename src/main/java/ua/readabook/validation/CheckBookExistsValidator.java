package ua.readabook.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.readabook.repository.BookRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class CheckBookExistsValidator implements ConstraintValidator<CheckBookExists, String> {

    @Autowired private BookRepository bookRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (bookRepository.findByName(value) == null) {
            return true;
        }
        return false;
    }
}
