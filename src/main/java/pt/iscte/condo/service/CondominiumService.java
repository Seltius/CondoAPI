package pt.iscte.condo.service;

import pt.iscte.condo.controller.dto.request.CreateCondominiumRequest;
import pt.iscte.condo.controller.dto.request.InviteCondominiumMemberRequest;
import pt.iscte.condo.controller.dto.request.UpdateCondominiumRequest;
import pt.iscte.condo.controller.dto.response.GetCondominiumResponse;

public interface CondominiumService {

    void createCondominium(CreateCondominiumRequest request);

    void deleteCondominium(Integer id);

    void updateCondominium(UpdateCondominiumRequest request);

    GetCondominiumResponse getCondominium(Integer id);

    void inviteCondominiumMember(InviteCondominiumMemberRequest request);

}
