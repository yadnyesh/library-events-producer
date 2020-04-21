package io.yadnyesh.libraryeventsproducer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.yadnyesh.libraryeventsproducer.domain.LibraryEvent;
import io.yadnyesh.libraryeventsproducer.producer.LibraryEventsProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@Slf4j
public class LibraryEventsController {
    
    @Autowired
    LibraryEventsProducer libraryEventsProducer;

    @PostMapping("/v1/libraryevent")
    public ResponseEntity<LibraryEvent> postLibraryEvent(@RequestBody LibraryEvent libraryEvent) throws JsonProcessingException {
        log.info("before sendLibraryEvent");
        libraryEventsProducer.sendLibraryEvent(libraryEvent);
        log.info("after afterLibraryEvent");
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }
}
