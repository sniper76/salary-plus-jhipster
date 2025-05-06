package com.salary.plus.web.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HealthCheckResource {

    @GetMapping("/health")
    public ResponseEntity<Void> getHealth() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
