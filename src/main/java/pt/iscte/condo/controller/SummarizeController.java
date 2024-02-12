package pt.iscte.condo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.iscte.condo.controller.dto.request.DocumentRequest;
import pt.iscte.condo.service.SummarizeService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/summarize")
public class SummarizeController {

    private final SummarizeService summarizeService;

    @PostMapping()
    public ResponseEntity<Map<String, String>> summarize(@RequestBody DocumentRequest request) {
        return ResponseEntity.ok(summarizeService.summarizeDocument(request));
    }

}
