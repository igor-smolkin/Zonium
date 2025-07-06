package org.ataraxii.zonium.controller;

import lombok.RequiredArgsConstructor;
import org.ataraxii.zonium.dto.proxy.RequestProxyDto;
import org.ataraxii.zonium.dto.proxy.ResponseProxyDto;
import org.ataraxii.zonium.security.SecurityUtil;
import org.ataraxii.zonium.service.ProxyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class ProxyController {

    private final ProxyService proxyService;
    private final SecurityUtil securityUtil;

    @PostMapping("/proxy")
    public ResponseEntity<ResponseProxyDto> createProxy(@RequestBody RequestProxyDto dto) {
        UUID userId = securityUtil.getCurrentUserId();
        ResponseProxyDto response = proxyService.create(dto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/proxy")
    public ResponseEntity<List<ResponseProxyDto>> findAllProxy() {
        UUID userId = securityUtil.getCurrentUserId();
        List<ResponseProxyDto> response = proxyService.findAll(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/proxy/{id}")
    public ResponseEntity<ResponseProxyDto> findProxyById(@PathVariable Long id) {
        UUID userId = securityUtil.getCurrentUserId();
        ResponseProxyDto response = proxyService.findById(id, userId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/proxy/{id}")
    public ResponseEntity<ResponseProxyDto> updateProxy(
            @PathVariable Long id,
            @RequestBody RequestProxyDto dto) {
        UUID userId = securityUtil.getCurrentUserId();
        ResponseProxyDto response = proxyService.update(id, dto, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/proxy/{id}")
    public ResponseEntity<Void> deleteProxy(@PathVariable Long id) {
        UUID userId = securityUtil.getCurrentUserId();
        proxyService.delete(id, userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
