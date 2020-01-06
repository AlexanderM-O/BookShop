package ru.cource.model.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import ru.cource.model.domain.Book;
import ru.cource.model.service.BookShopServiceImpl;
import ru.cource.model.service.BookShopServiceInterface;
/**
 * A Validator for {@link Book}.
 * 
 * A book is valid if:
 * 1.Has unique name 
 * 2.Has valid author string i.e. there is no duplicates and we could parse it
 * 								
 */
@Component
public class BookValidator implements Validator {
	private Book book;

	@Autowired
	private BookShopServiceInterface service;

	@Override
	public boolean supports(Class<?> clazz) {
		return Book.class.equals(clazz);
	}

	private void baseValidate(Object target, Errors errors) {
		book = (Book) target;
		if (book.getAuthorError() != null) {
			errors.rejectValue("authors", "", book.getAuthorError());
		}
	}

	/**
	 * valid {@link Book} in create methods
	 * @param target new book
	 * @param errors
	 *
	 */
	@Override
	public void validate(Object target, Errors errors) {
		baseValidate(target, errors);
		if (service.getBookByName(book.getName()) != null) {
			// if book exist in data base hence book has name and we wont have 2 errors at
			// the same time
			errors.rejectValue("name", "", "Book is alreary exist");
		}
	}

	/**
	 * valid {@link Book} in update methods:should allow new book has the same name as old book due to we replace it
	 * @param target new book
	 * @param errors
	 * @param oldBook book which we are replacing
	 */
	public void validate(Object target, Errors errors, Book oldBook) {
		baseValidate(target, errors);
		if (book.getName().equals(oldBook.getName()))
			return;
		// book have different names should check if is it try to replace another book
		if (service.getBookByName(book.getName()) != null) {
			errors.rejectValue("name", "", "Cannot replace book");
		}
	}

}
