package edu.espe.springlab.web.controller;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/version")
public class VersionController {

    @Value("${app.version:1.0.0-dev}")
    private String version;

    @Value("${app.build.number:local}")
    private String buildNumber;

    @Value("${app.build.commit:unknown}")
    private String commitSha;

    @GetMapping
    public ResponseEntity<Map<String, String>> getVersion() {
        Map<String, String> versionInfo = new HashMap<>();
        versionInfo.put("version", version);
        versionInfo.put("buildNumber", buildNumber);
        versionInfo.put("commitSha", commitSha);
        versionInfo.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.ok(versionInfo);
    }
}