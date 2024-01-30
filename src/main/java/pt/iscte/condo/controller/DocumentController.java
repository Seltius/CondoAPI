package pt.iscte.condo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.iscte.condo.controller.request.DocumentRequest;
import pt.iscte.condo.controller.response.DocumentResponse;
import pt.iscte.condo.controller.response.FileResponse;
import pt.iscte.condo.service.DocumentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/documents")
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadDocument(@RequestBody DocumentRequest request) {
        documentService.uploadDocument(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileResponse> getDocument(@PathVariable Integer id) {
        return ResponseEntity.ok(documentService.getDocument(id));
    }

    @GetMapping()
    public ResponseEntity<List<DocumentResponse>> getDocuments() {
        return ResponseEntity.ok(documentService.getDocuments());
    }

}
