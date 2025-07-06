package org.ataraxii.zonium.controller;

import lombok.RequiredArgsConstructor;
import org.ataraxii.zonium.dto.fingerprint.RequestFingerprintDto;
import org.ataraxii.zonium.dto.session.ResponseSessionDto;
import org.ataraxii.zonium.security.SecurityUtil;
import org.ataraxii.zonium.service.FingerprintService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class FingerprintController {

    private final FingerprintService fingerprintService;
    private final SecurityUtil securityUtil;

    @PostMapping("/fingerprint/{id}")
    public ResponseEntity<ResponseSessionDto> updateFingerprint(
            @PathVariable Long id,
            @RequestBody RequestFingerprintDto dto) {
        UUID userId = securityUtil.getCurrentUserId();
        ResponseSessionDto response = fingerprintService.update(id, dto, userId);
        return ResponseEntity.ok(response);
    }
}
