package io.yadnyesh.libraryeventsproducer.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.yadnyesh.libraryeventsproducer.domain.LibraryEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
@Slf4j
public class LibraryEventsProducer {
	
	@Autowired
	KafkaTemplate<Integer, String> kafkaTemplate;
	
	@Autowired
	ObjectMapper objectMapper;
	
	public void sendLibraryEvent(LibraryEvent libraryEvent) throws JsonProcessingException {
		Integer key = libraryEvent.getLibraryEventId();
		String value = objectMapper.writeValueAsString(libraryEvent);
		ListenableFuture<SendResult<Integer, String>> listenableFuture = kafkaTemplate.sendDefault(key, value);
		listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
			@Override
			public void onFailure(Throwable e) {
				handleFailure(e);
			}
			
			@Override
			public void onSuccess(SendResult<Integer, String> result) {
				handleSuccess(key, value, result);
			}
		});
	}
	
	private void handleFailure(Throwable e) {
		log.error("Error Sending message and the exception is {}", e.getMessage());
		try {
			throw e;
		} catch(Throwable throwable) {
			log.error(throwable.getMessage());
		}
	}
	
	private void handleSuccess(Integer key, String value, SendResult<Integer, String> result) {
		log.info("Message sent successfully for the key: {} and the value is: {}, partition is {}", key, value, result.getRecordMetadata().partition());
	}
	
}
