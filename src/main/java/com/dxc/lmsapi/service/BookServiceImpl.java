package com.dxc.lmsapi.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dxc.lmsapi.entity.Book;
import com.dxc.lmsapi.exception.LibraryException;
import com.dxc.lmsapi.repository.BookRepository;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepository bookRepo;

	@Transactional
	@Override
	public Book add(Book book) throws LibraryException {
		if (book != null) {
			if (bookRepo.existsById(book.getBcode())) {
				throw new LibraryException("A book with the bcode " + book.getBcode() + " already exists!");
			}

			bookRepo.save(book);
		}
		return book;
	}

	@Transactional
	@Override
	public Book update(Book book) throws LibraryException {
		if (book != null) {
			if (!bookRepo.existsById(book.getBcode())) {
				throw new LibraryException("No Such Book Found To Update!");
			}

			bookRepo.save(book);
		}
		return book;
	}

	@Transactional
	@Override
	public boolean deleteBook(int bcode) throws LibraryException {
		boolean deleted = false;
		if (!bookRepo.existsById(bcode)) {
			throw new LibraryException("No Such Book Found To Delete!");
		}

		bookRepo.deleteById(bcode);
		deleted = true;

		return deleted;
	}

	@Override
	public Book getByBcode(int bcode) {
		return bookRepo.findById(bcode).orElse(null);
	}

	@Override
	public List<Book> getAllBooks() {
		return bookRepo.findAll();
	}

	@Override
	public Book findByBname(String bname) {
		return bookRepo.findByBname(bname);
	}

	@Override
	public List<Book> findInPriceRange(double lowerBound, double upperBound) {
		return bookRepo.getAllInPriceRange(lowerBound, upperBound);
	}

	@Override
	public List<Book> findByPackageDate(LocalDate packageDate) {

		return bookRepo.findAllByPackageDate(packageDate);
	}
}
