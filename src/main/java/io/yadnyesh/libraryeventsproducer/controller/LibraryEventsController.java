package io.yadnyesh.libraryeventsproducer.controller;

import io.yadnyesh.libraryeventsproducer.domain.LibraryEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@Slf4j
public class LibraryEventsController {

    @PostMapping("/v1/libraryevent")
    public ResponseEntity<LibraryEvent> postLibraryEvent(@RequestBody LibraryEvent libraryEvent) {
        log.info("Called the post method");
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }
}
