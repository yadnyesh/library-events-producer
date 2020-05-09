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
				.bookId(null)
				.bookAuthor(null)
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
	
	
}
