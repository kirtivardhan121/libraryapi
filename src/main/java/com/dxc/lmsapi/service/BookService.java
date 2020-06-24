package com.dxc.lmsapi.service;

import java.time.LocalDate;
import java.util.List;

import com.dxc.lmsapi.entity.Book;
import com.dxc.lmsapi.exception.LibraryException;

public interface BookService {

	Book add(Book book) throws LibraryException;

	boolean deleteBook(int bcode) throws LibraryException;

	Book update(Book book) throws LibraryException;

	List<Book> getAllBooks();

	Book getByBcode(int bcode);

	Book findByBname(String bname);

	List<Book> findInPriceRange(double lowerBound, double upperBound);

	List<Book> findByPackageDate(LocalDate packageDate);
}
