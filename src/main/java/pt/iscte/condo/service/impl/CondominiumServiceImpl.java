package pt.iscte.condo.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pt.iscte.condo.controller.dto.request.CreateCondominiumRequest;
import pt.iscte.condo.controller.dto.request.InviteCondominiumMemberRequest;
import pt.iscte.condo.controller.dto.request.UpdateCondominiumRequest;
import pt.iscte.condo.controller.dto.response.GetCondominiumResponse;
import pt.iscte.condo.enums.InvitationStatus;
import pt.iscte.condo.repository.CondominiumInvitationRepository;
import pt.iscte.condo.repository.CondominiumRepository;
import pt.iscte.condo.repository.UserRepository;
import pt.iscte.condo.repository.entities.Condominium;
import pt.iscte.condo.repository.entities.CondominiumInvitation;
import pt.iscte.condo.repository.entities.User;
import pt.iscte.condo.service.CondominiumService;
import pt.iscte.condo.service.mapper.CondominiumMapper;
import pt.iscte.condo.utils.UserUtils;


@Service
@RequiredArgsConstructor
public class CondominiumServiceImpl implements CondominiumService {

    private final CondominiumRepository condominiumRepository;
    private final UserRepository userRepository;
    private final CondominiumInvitationRepository invitationRepository;

    private final CondominiumMapper condominiumMapper;

    private final UserUtils userUtils;

    @Override
    public void createCondominium(CreateCondominiumRequest request) {
        //todo verify if the user is role admin
        condominiumRepository.save(condominiumMapper.toEntity(request));
    }

    @Override
    public void deleteCondominium(Integer id) {
        //todo verify if condominium exists
        //todo verify if the user role is admin of the condominium
        condominiumRepository.deleteById(id);
    }

    @Override
    public void updateCondominium(UpdateCondominiumRequest request) {
        //todo verify if condominium exists
        //todo verify if the user role is admin of the condominium

        Condominium condominium = condominiumRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("Condominium not found"));

        //todo refactor
        condominium.setName(request.getName());
        condominium.setAddress(request.getAddress());
        condominium.setAddress(request.getParish());
        condominium.setCounty(request.getCounty());

        condominiumRepository.save(condominium);
    }

    @Override
    public GetCondominiumResponse getCondominium(Integer id) {

        Condominium condominium = condominiumRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Condominium not found"));

        return condominiumMapper.toDto(condominium);
    }

    @Override
    public void inviteCondominiumMember(InviteCondominiumMemberRequest request) {
        String email = request.getEmail();
        User inviter = userUtils.getUserByBearer();
        Condominium condominium = inviter.getCondominium();

        User invitee = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        CondominiumInvitation invitation = new CondominiumInvitation();
        invitation.setCondominium(condominium);
        invitation.setInviter(inviter);
        invitation.setInvitee(invitee);
        invitation.setStatus(InvitationStatus.PENDING);

        invitationRepository.save(invitation);

        // TODO: send email to invitee
        //TODO: create endpoint to accept invitation or refuse invitation

    }

}