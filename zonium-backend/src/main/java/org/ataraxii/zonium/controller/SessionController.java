package org.ataraxii.zonium.controller;

import lombok.RequiredArgsConstructor;
import org.ataraxii.zonium.dto.session.RequestSessionDto;
import org.ataraxii.zonium.dto.session.ResponseSessionDto;
import org.ataraxii.zonium.service.BrowserSessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class SessionController {

    private final BrowserSessionService sessionService;

    @PostMapping("/session")
    public ResponseEntity<ResponseSessionDto> createSession(@RequestBody RequestSessionDto dto) {
        ResponseSessionDto response = sessionService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/session")
    public ResponseEntity<List<ResponseSessionDto>> findAllSession() {
        List<ResponseSessionDto> response = sessionService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/session/{id}")
    public ResponseEntity<ResponseSessionDto> findSessionById(@PathVariable Long id) {
        ResponseSessionDto response = sessionService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/session/{id}")
    public ResponseEntity<ResponseSessionDto> updateSession(
            @PathVariable Long id,
            @RequestBody RequestSessionDto dto) {
        ResponseSessionDto response = sessionService.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/session/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        sessionService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
