package pt.iscte.condo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.iscte.condo.controller.dto.request.CreateCondominiumRequest;
import pt.iscte.condo.controller.dto.request.InviteCondominiumMemberRequest;
import pt.iscte.condo.controller.dto.request.UpdateCondominiumRequest;
import pt.iscte.condo.controller.dto.response.GetCondominiumResponse;
import pt.iscte.condo.service.CondominiumService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/condominium")
public class CondominiumController {

    private final CondominiumService condominiumService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody @Valid CreateCondominiumRequest request) {
        condominiumService.createCondominium(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
        public ResponseEntity<?> delete(@PathVariable Integer id) {
        condominiumService.deleteCondominium(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody @Valid UpdateCondominiumRequest request) {
        condominiumService.updateCondominium(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<GetCondominiumResponse> get(@PathVariable Integer id) {
        return ResponseEntity.ok(condominiumService.getCondominium(id));
    }

    @PostMapping("/invite")
    public ResponseEntity<?> invite(@RequestBody @Valid InviteCondominiumMemberRequest request) {
        condominiumService.inviteCondominiumMember(request);
        return ResponseEntity.ok().build();
    }

}
