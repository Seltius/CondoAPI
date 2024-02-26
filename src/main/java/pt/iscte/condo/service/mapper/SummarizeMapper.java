package pt.iscte.condo.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.iscte.condo.controller.dto.Topic;
import pt.iscte.condo.repository.entities.Apartment;
import pt.iscte.condo.repository.entities.MeetingTopic;
import pt.iscte.condo.service.dto.FractionInfo;

import java.util.List;


@Mapper(componentModel = "spring")
public interface SummarizeMapper {

    List<Topic> mapTopicEntityList2Topic(List<MeetingTopic> meetingTopic);

    @Mapping(source = "apartments.user.name", target = "fractionOwnerName")
    List<FractionInfo> mapApartmentEntity2Apartment(List<Apartment> apartments);

}
