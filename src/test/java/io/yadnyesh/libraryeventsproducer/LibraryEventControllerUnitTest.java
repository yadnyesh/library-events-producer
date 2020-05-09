package io.yadnyesh.libraryeventsproducer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.yadnyesh.libraryeventsproducer.controller.LibraryEventsController;
import io.yadnyesh.libraryeventsproducer.domain.Book;
import io.yadnyesh.libraryeventsproducer.domain.LibraryEvent;
import io.yadnyesh.libraryeventsproducer.producer.LibraryEventsProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LibraryEventsController.class)
@AutoConfigureMockMvc
public class LibraryEventControllerUnitTest {
	
	@Autowired
	MockMvc mockMvc;
	
	ObjectMapper objectMapper = new ObjectMapper();
	
	@MockBean
	LibraryEventsProducer libraryEventProducer;
	
	@Test
	void postLibraryEvent() throws Exception {
		
		Book book = Book.builder()
				.bookId(1)
				.bookAuthor("Yadnyesh")
				.bookName("Kafka using Spring Boot")
				.build();
		
		LibraryEvent libraryEvent = LibraryEvent.builder()
				.libraryEventId(null)
				.book(book)
				.build();
		
		
		String json = objectMapper.writeValueAsString(libraryEvent);
		doNothing().when(libraryEventProducer).sendLibraryEvent(isA(LibraryEvent.class));
		
		String expectedErrorMessage = "book.bookAuthor - must not be blank, book.bookId - must not be null";
		
		mockMvc.perform(post("/v1/libraryevent")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
	}
	
	@Test
	void postLibraryEvent_4xx() throws Exception {
		
		Book book = Book.builder()
				.bookId(null)
				.bookAuthor(null)
				.bookName("Kafka using Spring Boot")
				.build();
		
		LibraryEvent libraryEvent = LibraryEvent.builder()
				.libraryEventId(null)
				.book(book)
				.build();
		
		
		String json = objectMapper.writeValueAsString(libraryEvent);
		when(libraryEventProducer.sendLibraryEventSpecifyTopicName(isA(LibraryEvent.class))).thenReturn(null);
		
		String expectedErrorMessage = "book.bookAuthor - must not be blank, book.bookId - must not be null";
		
		mockMvc.perform(post("/v1/libraryevent")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError())
				.andExpect(content().string(expectedErrorMessage));
	}
	
	@Test
	void updateLibraryEvent() throws Exception {
		
		//given
		Book book = new Book().builder()
				.bookId(123)
				.bookAuthor("Dilip")
				.bookName("Kafka Using Spring Boot")
				.build();
		
		LibraryEvent libraryEvent = LibraryEvent.builder()
				.libraryEventId(123)
				.book(book)
				.build();
		String json = objectMapper.writeValueAsString(libraryEvent);
		when(libraryEventProducer.sendLibraryEventSpecifyTopicName(isA(LibraryEvent.class))).thenReturn(null);
		
		//expect
		mockMvc.perform(
				put("/v1/libraryevent")
						.content(json)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		
	}
	
	@Test
	void updateLibraryEvent_withNullLibraryEventId() throws Exception {
		
		//given
		Book book = new Book().builder()
				.bookId(123)
				.bookAuthor("Dilip")
				.bookName("Kafka Using Spring Boot")
				.build();
		
		LibraryEvent libraryEvent = LibraryEvent.builder()
				.libraryEventId(null)
				.book(book)
				.build();
		String json = objectMapper.writeValueAsString(libraryEvent);
		when(libraryEventProducer.sendLibraryEventSpecifyTopicName(isA(LibraryEvent.class))).thenReturn(null);
		
		//expect
		mockMvc.perform(
				put("/v1/libraryevent")
						.content(json)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError())
				.andExpect(content().string("Please pass the LibraryEventId"));
		
	}
	
}
