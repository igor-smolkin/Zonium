package org.ataraxii.zonium.controller;

import lombok.RequiredArgsConstructor;
import org.ataraxii.zonium.dto.fingerprint.RequestFingerprintDto;
import org.ataraxii.zonium.dto.session.ResponseSessionDto;
import org.ataraxii.zonium.service.FingerprintService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class FingerprintController {

    private final FingerprintService fingerprintService;

    @PostMapping("/fingerprint/{id}")
    public ResponseEntity<ResponseSessionDto> updateFingerprint(
            @PathVariable Long id,
            @RequestBody RequestFingerprintDto dto) {
        ResponseSessionDto response = fingerprintService.update(id, dto);
        return ResponseEntity.ok(response);
    }
}
