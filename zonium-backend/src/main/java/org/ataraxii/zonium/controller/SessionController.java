package org.ataraxii.zonium.controller;

import lombok.RequiredArgsConstructor;
import org.ataraxii.zonium.dto.session.RequestSessionDto;
import org.ataraxii.zonium.dto.session.ResponseSessionDto;
import org.ataraxii.zonium.security.SecurityUtil;
import org.ataraxii.zonium.service.BrowserSessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class SessionController {

    private final BrowserSessionService sessionService;
    private final SecurityUtil securityUtil;

    @PostMapping("/session")
    public ResponseEntity<ResponseSessionDto> createSession(@RequestBody RequestSessionDto dto) {
        UUID userId = securityUtil.getCurrentUserId();
        ResponseSessionDto response = sessionService.create(dto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/session")
    public ResponseEntity<List<ResponseSessionDto>> findAllSession() {
        UUID userId = securityUtil.getCurrentUserId();
        List<ResponseSessionDto> response = sessionService.findAll(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/session/{id}")
    public ResponseEntity<ResponseSessionDto> findSessionById(@PathVariable Long id) {
        UUID userId = securityUtil.getCurrentUserId();
        ResponseSessionDto response = sessionService.findById(id, userId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/session/{id}")
    public ResponseEntity<ResponseSessionDto> updateSession(
            @PathVariable Long id,
            @RequestBody RequestSessionDto dto) {
        UUID userId = securityUtil.getCurrentUserId();
        ResponseSessionDto response = sessionService.update(id, dto, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/session/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        UUID userId = securityUtil.getCurrentUserId();
        sessionService.delete(id, userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
