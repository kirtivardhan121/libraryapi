package com.dxc.lmsapi;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.dxc.lmsapi.entity.Book;
import com.dxc.lmsapi.exception.LibraryException;
import com.dxc.lmsapi.repository.BookRepository;
import com.dxc.lmsapi.service.BookService;
import com.dxc.lmsapi.service.BookServiceImpl;

@SpringJUnitConfig
public class BookServiceImplUnitTest {

	@MockBean
	private BookRepository bookRepository;

	@TestConfiguration
	static class BookServiceImplTestContextConfiguration {

		@Bean
		public BookService bookService() {
			return new BookServiceImpl();
		}
	}

	@Autowired
	private BookService bookService;

	private Book[] testData;

	@BeforeEach
	public void fillTestData() {
		testData = new Book[] { new Book(101, "TheHound", 999, LocalDate.now()),
				new Book(102, "TheRing", 650, LocalDate.now()), new Book(103, "Game Of Thrones", 550, LocalDate.now()),
				new Book(104, "Bharat ek Khoj", 799, LocalDate.now()),
				new Book(105, "I had adream", 700, LocalDate.now()), };

	}

	@AfterEach
	public void clearDatabase() {
		testData = null;
	}

	@Test
	public void addTest() {

		Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenReturn(null);

		Mockito.when(bookRepository.existsById(testData[0].getBcode())).thenReturn(false);

		try {
			Book actual = bookService.add(testData[0]);
			Assertions.assertEquals(testData[0], actual);
		} catch (LibraryException e) {
			Assertions.fail(e.getMessage());
		}
	}

	@Test
	public void addExistingBookTest() {
		Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenReturn(null);

		Mockito.when(bookRepository.existsById(testData[0].getBcode())).thenReturn(true);

		Assertions.assertThrows(LibraryException.class, () -> {
			bookService.add(testData[0]);
		});

	}

	@Test
	public void updateExistingBookTest() {

		Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenReturn(null);

		Mockito.when(bookRepository.existsById(testData[0].getBcode())).thenReturn(true);

		try {
			Book actual = bookService.update(testData[0]);
			Assertions.assertEquals(testData[0], actual);
		} catch (LibraryException e) {
			Assertions.fail(e.getMessage());
		}
	}

	@Test
	public void updateNonExistingBookTest() {
		Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenReturn(null);

		Mockito.when(bookRepository.existsById(testData[0].getBcode())).thenReturn(false);

		Assertions.assertThrows(LibraryException.class, () -> {
			bookService.update(testData[0]);
		});

	}

	@Test
	public void deleteByIdExistingRecordTest() {
		Mockito.when(bookRepository.existsById(Mockito.isA(Integer.class))).thenReturn(true);

		Mockito.doNothing().when(bookRepository).deleteById(Mockito.isA(Integer.class));

		try {
			Assertions.assertTrue(bookService.deleteBook(testData[0].getBcode()));
		} catch (LibraryException e) {
			Assertions.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void deleteByIdNonExistingRecordTest() {
		Mockito.when(bookRepository.existsById(Mockito.isA(Integer.class))).thenReturn(false);

		Mockito.doNothing().when(bookRepository).deleteById(Mockito.isA(Integer.class));

		Assertions.assertThrows(LibraryException.class, () -> {
			bookService.deleteBook(testData[0].getBcode());
		});

	}

	@Test
	public void getByIdExisitngRecordTest() {
		Mockito.when(bookRepository.findById(testData[0].getBcode())).thenReturn(Optional.of(testData[0]));
		Assertions.assertEquals(testData[0], bookService.getByBcode(testData[0].getBcode()));
	}

	@Test
	public void getByIdNonExisitngRecordTest() {
		Mockito.when(bookRepository.findById(testData[0].getBcode())).thenReturn(Optional.empty());
		Assertions.assertNull(bookService.getByBcode(testData[0].getBcode()));
	}

	@Test
	public void getAllBooksWhenDataExists() {
		List<Book> expected = Arrays.asList(testData);

		Mockito.when(bookRepository.findAll()).thenReturn(expected);

		Assertions.assertEquals(expected, bookService.getAllBooks());
	}

	@Test
	public void getAllBooksWhenNoDataExists() {
		List<Book> expected = new ArrayList<>();

		Mockito.when(bookRepository.findAll()).thenReturn(expected);

		Assertions.assertEquals(expected, bookService.getAllBooks());
	}
}
