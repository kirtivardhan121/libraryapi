package com.dxc.lmsapi.api;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dxc.lmsapi.entity.Book;
import com.dxc.lmsapi.exception.LibraryException;
import com.dxc.lmsapi.service.BookService;

@RestController
@RequestMapping("/books")
public class BookApi {

	@Autowired
	private BookService bookService;

	@GetMapping
	public ResponseEntity<List<Book>> getAllBooks() {

		return new ResponseEntity<List<Book>>(bookService.getAllBooks(), HttpStatus.OK);
	}

	@GetMapping("/{bookId:[0-9]{1,5}}")
	public ResponseEntity<Book> getBookById(@PathVariable("bookId") int bcode) {
		ResponseEntity<Book> response = null;

		Book book = bookService.getByBcode(bcode);

		if (book == null) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			response = new ResponseEntity<>(book, HttpStatus.OK);

		}
		return response;
	}

	@PostMapping
	public ResponseEntity<Book> createBook(@Valid @RequestBody Book book, BindingResult result)
			throws LibraryException {
		ResponseEntity<Book> response = null;

		if (result.hasErrors()) {
			StringBuilder errMsg = new StringBuilder();
			for (FieldError err : result.getFieldErrors()) {
				errMsg.append(err.getDefaultMessage() + ",");
			}
			throw new LibraryException(errMsg.toString());
		} else {
			bookService.add(book);
			response = new ResponseEntity<>(book, HttpStatus.OK);

		}

		return response;
	}

	@PutMapping
	public ResponseEntity<Book> updateBook(@Valid @RequestBody Book book, BindingResult result)
			throws LibraryException {
		ResponseEntity<Book> response = null;

		if (result.hasErrors()) {

			StringBuilder errMsg = new StringBuilder();
			for (FieldError err : result.getFieldErrors()) {
				errMsg.append(err.getDefaultMessage() + ",");
			}
			throw new LibraryException(errMsg.toString());
		} else {
			bookService.update(book);
			response = new ResponseEntity<>(book, HttpStatus.OK);

		}

		return response;
	}

	@DeleteMapping("/{bookId}")
	public ResponseEntity<Void> deleteById(@PathVariable("bookId") int bcode) throws LibraryException {

		bookService.deleteBook(bcode);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/{bname:[A-Za-z]{4,20}}")
	public ResponseEntity<Book> getBookByBname(@PathVariable("bname") String bname) throws LibraryException {

		ResponseEntity<Book> response = null;

		Book book = bookService.findByBname(bname);

		if (book == null) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			response = new ResponseEntity<>(book, HttpStatus.OK);
		}
		return response;
	}

	@GetMapping("/{packageDate:[0-9]{4}-[0-9]{2}-[0-9]{2}}")
	public ResponseEntity<List<Book>> getBookByPackageDate(

			@PathVariable("packageDate") @DateTimeFormat(iso = ISO.DATE) LocalDate packageDate)
			throws LibraryException {

		ResponseEntity<List<Book>> response = null;

		List<Book> books = bookService.findByPackageDate(packageDate);

		if (books == null || books.size() == 0) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			response = new ResponseEntity<>(books, HttpStatus.OK);
		}
		return response;
	}

	@GetMapping("/priceBetween/{lbound}/and/{ubound}")
	public ResponseEntity<List<Book>> getBookByPriceRange(@PathVariable("lbound") double lbound,
			@PathVariable("ubound") double ubound) throws LibraryException {

		ResponseEntity<List<Book>> response = null;

		List<Book> items = bookService.findInPriceRange(lbound, ubound);

		if (items == null || items.size() == 0) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			response = new ResponseEntity<>(items, HttpStatus.OK);
		}
		return response;
	}
}
