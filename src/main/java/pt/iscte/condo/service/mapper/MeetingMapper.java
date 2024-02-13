package pt.iscte.condo.service.mapper;

import org.mapstruct.Mapper;
import pt.iscte.condo.controller.dto.Topic;
import pt.iscte.condo.repository.entities.MeetingTopic;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MeetingMapper {

    List<MeetingTopic> topics2MeetingTopics(List<Topic> topics);

}
