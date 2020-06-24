package com.dxc.lmsapi;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

import com.dxc.lmsapi.api.BookApi;
import com.dxc.lmsapi.entity.Book;
import com.dxc.lmsapi.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;


@SpringJUnitConfig
@WebMvcTest(BookApi.class)
public class BookApiUnitTest {

	  @Autowired
	    private MockMvc mvcClient;

	    @MockBean
	    private BookService bookService;

	    private List<Book> testData;

	    private static final String API_URL = "/books";
	    
	    @BeforeEach
	    public void fillTestData() {
	        testData = new ArrayList<>();
	        testData.add(new Book(101, "Arthashastra", 200, LocalDate.now()));
	        testData.add(new Book(102, "Broken_Wings", 350, LocalDate.now()));
	        testData.add(new Book(103, "Ram_Charit_Manas", 500, LocalDate.now()));
	        testData.add(new Book(104, "Life_Of_Pie", 400, LocalDate.now())); 
	    }
	    
	    private static final ObjectMapper makeMapper() {
	        ObjectMapper mapper = new ObjectMapper();
	        mapper.registerModule(new ParameterNamesModule());
	        mapper.registerModule(new Jdk8Module());
	        mapper.registerModule(new JavaTimeModule());
	        return mapper;
	    }
	    
	    @AfterEach
	    public void clearDatabase() {
	        testData = null;
	    }

	    @Test
	    public void getAllBooksTest()  {
	        Mockito.when(bookService.getAllBooks()).thenReturn(testData);

	        try {
	            mvcClient.perform(get(API_URL))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$", hasSize(4)))
	                .andDo(print());
	        } catch (Exception e) {
	            Assertions.fail(e.getMessage());
	        }
	    }
	    
	    @Test
	    public void getBookByIdTest() {
	        Book testRec = testData.get(0);
	        
	        Mockito.when(bookService.getByBcode(testRec.getBcode())).thenReturn(testRec);
	    
	        try {
	            mvcClient.perform(get(API_URL + "/" + testRec.getBcode()))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.bname", is(testRec.getBname())))
	                .andExpect(jsonPath("$.price", is(testRec.getPrice())))
	                .andDo(print());
	            
	        } catch (Exception e) {
	            Assertions.fail(e.getMessage());
	        }        
	    }
	    
	    @Test
	    public void getBookByIdTestNonExsiting() {
	        
	        Mockito.when(bookService.getByBcode(8888)).thenReturn(null);
	    
	        try {
	            mvcClient.perform(get(API_URL + "/8888"))
	                .andExpect(status().isNotFound())
	                .andDo(print());
	        } catch (Exception e) {
	            Assertions.fail(e.getMessage());
	        }        
	    }
	    
	    @Test
	    public void geBookByTitleTest(){
	    Book testRec = testData.get(0);
	        
	        Mockito.when(bookService.findByBname(testRec.getBname())).thenReturn(testRec);
	    
	        try {
	            mvcClient.perform(get(API_URL + "/" + testRec.getBname()))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.bcode", is(testRec.getBcode())))
	                .andExpect(jsonPath("$.price", is(testRec.getPrice())))
	                .andDo(print());
	            
	        } catch (Exception e) {
	            Assertions.fail(e.getMessage());
	        }    
	    }
	    
	    @Test
	    public void getBookByPublishedDateTest(){
	        Book testRec = testData.get(0);
	        
	        Mockito.when(bookService.findByPackageDate(testRec.getPackageDate())).thenReturn(testData);
	    
	        try {
				mvcClient.perform(get(API_URL + "/" + testRec.getPackageDate())).andExpect(status().isOk())
						.andExpect(jsonPath("$", hasSize(4))).andDo(print());
			} catch (Exception e) {
				Assertions.fail(e.getMessage());
			}   
	    }
	    
	    @Test
	    public void getBookByPriceRangeTest() {
	        
	        Mockito.when(bookService.findInPriceRange(100, 500)).thenReturn(testData);
	    
	        try {
	            mvcClient.perform(get(API_URL + "/priceBetween/100/and/500"))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$", hasSize(4)))
	                .andDo(print());
	        } catch (Exception e) {
	            Assertions.fail(e.getMessage());
	        }
	    }
	    
	    @Test
	    public void createBookTest() {
	        Book testRec = testData.get(0);
	       
	        try {
	            Mockito.when(bookService.add(testRec)).thenReturn(testRec);
	           
	            mvcClient.perform(post(API_URL)
	                    .contentType(MediaType.APPLICATION_JSON)
	                    .content(makeMapper().writeValueAsString(testRec))
	                    ).andExpect(status().isOk()).andDo(print());
	        } catch (Exception e) {
	            Assertions.fail(e.getMessage());
	        }
	   
	    }
	   
	    @Test
	    public void updateBookTest() {
	        Book testRec = testData.get(0);
	       
	        try {
	            Mockito.when(bookService.update(testRec)).thenReturn(testRec);
	            System.out.println((new ObjectMapper()).writeValueAsString(testRec));
	            mvcClient.perform(put(API_URL)
	                    .contentType(MediaType.APPLICATION_JSON)
	                    .content(makeMapper().writeValueAsString(testRec))
	                    ).andExpect(status().isOk()).andDo(print());
	        } catch (Exception e) {
	            Assertions.fail(e.getMessage());
	        }
	   
	    }
	    @Test
	    public void deleteBooksByIdTest() {
	        
	        try {
	            Mockito.when(bookService.deleteBook(testData.get(0).getBcode())).thenReturn(true);
	            
	            mvcClient.perform(delete(API_URL +"/"+testData.get(0).getBcode()))
	            .andExpect(status().isOk())
	            .andDo(print());
	        } catch (Exception e) {
	            Assertions.fail(e.getMessage());
	        }
	    }
}
