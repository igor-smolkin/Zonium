package org.ataraxii.zonium.controller;

import lombok.RequiredArgsConstructor;
import org.ataraxii.zonium.dto.proxy.RequestProxyDto;
import org.ataraxii.zonium.dto.proxy.ResponseProxyDto;
import org.ataraxii.zonium.service.ProxyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class ProxyController {

    private final ProxyService proxyService;

    @PostMapping("/proxy")
    public ResponseEntity<ResponseProxyDto> createProxy(@RequestBody RequestProxyDto dto) {
        ResponseProxyDto response = proxyService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/proxy")
    public ResponseEntity<List<ResponseProxyDto>> findAllProxy() {
        List<ResponseProxyDto> response = proxyService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/proxy/{id}")
    public ResponseEntity<ResponseProxyDto> findProxyById(@PathVariable Long id) {
        ResponseProxyDto response = proxyService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/proxy/{id}")
    public ResponseEntity<ResponseProxyDto> updateProxy(
            @PathVariable Long id,
            @RequestBody RequestProxyDto dto) {
        ResponseProxyDto response = proxyService.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/proxy/{id}")
    public ResponseEntity<Void> deleteProxy(@PathVariable Long id) {
        proxyService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
