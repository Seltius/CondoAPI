package pt.iscte.condo.service.mapper;

import org.mapstruct.Mapper;
import pt.iscte.condo.controller.dto.request.CreateCondominiumRequest;
import pt.iscte.condo.controller.dto.request.UpdateCondominiumRequest;
import pt.iscte.condo.controller.dto.response.GetCondominiumResponse;
import pt.iscte.condo.repository.entities.Condominium;



@Mapper(componentModel = "spring")
public interface CondominiumMapper {

    GetCondominiumResponse toDto(Condominium condominium);

    Condominium toEntity(CreateCondominiumRequest dto);

    void updateEntity(UpdateCondominiumRequest dto, Condominium entity);


}
