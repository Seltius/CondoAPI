package pt.iscte.condo.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.iscte.condo.controller.dto.Topic;
import pt.iscte.condo.repository.entities.Apartment;
import pt.iscte.condo.repository.entities.MeetingTopic;
import pt.iscte.condo.service.dto.FractionInfo;

import java.util.List;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface SummarizeMapper {

    List<Topic> mapTopicEntityList2Topic(List<MeetingTopic> meetingTopic);

    @Mapping(source = "user.name", target = "fractionOwnerName")
    FractionInfo mapApartmentEntity2FractionInfo(Apartment apartments);

    default List<FractionInfo> mapApartmentEntity2Apartment(List<Apartment> apartments) {
        return apartments.stream()
                .map(this::mapApartmentEntity2FractionInfo)
                .collect(Collectors.toList());
    }

}
