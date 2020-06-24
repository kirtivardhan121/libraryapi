package com.dxc.lmsapi;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.dxc.lmsapi.entity.Book;
import com.dxc.lmsapi.repository.BookRepository;

@DataJpaTest
public class BookRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	BookRepository bookRepository;
	
	private Book[] testData;
	
	@BeforeEach
	public void fillTestData() {
		testData = new Book[] {
				new Book(101, "TheHound", 999, LocalDate.now()),
				new Book(102, "TheRing", 650, LocalDate.now()),
				new Book(103, "Game Of Thrones", 550, LocalDate.now()),
				new Book(104, "Bharat ek Khoj", 799, LocalDate.now()),
				new Book(105, "I had adream", 700, LocalDate.now()),
		};
		for (Book book : testData) {
			entityManager.persist(book);
		}
		entityManager.flush();
 	}
	
	@AfterEach
	public void clearDatabase() {
		
		for(Book book : testData) {
			entityManager.remove(book);
		}
		entityManager.flush();
	}
	
	@Test
	public void findByBnameTest() {
		for(Book book : testData) {
			Assertions.assertEquals(book, bookRepository.findByBname(book.getBname()));
		}
	}
	
	@Test
	public void findByInameTestWithNonExistingDate() {
		Assertions.assertNull(bookRepository.findByBname("@#1234"));
	}
	
	@Test
	public void findAllByPublishedDateTest() {
		Book[] actualData = bookRepository.findAllByPackageDate(LocalDate.now()).toArray(new Book[] {});
		for (int i = 0; i < actualData.length; i++) {
			Assertions.assertEquals(testData[i], actualData[i]);
		}
	}

	@Test
	public void findAllByPublishedDateTestWithNonExisitngDate() {
		List<Book> actualData = bookRepository.findAllByPackageDate(LocalDate.now().plusDays(2));
		Assertions.assertEquals(0, actualData.size());
	}

	@Test
	public void getAllInPriceRangeTest() {
		Book[] actualData = bookRepository.getAllInPriceRange(100, 500).toArray(new Book[] {});
		Book[] expectedData = new Book[] { testData[1], testData[2] };

		for (int i = 0; i < actualData.length; i++) {
			Assertions.assertEquals(expectedData[i], actualData[i]);
		}

	}
}
