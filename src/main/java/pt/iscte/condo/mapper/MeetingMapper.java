package pt.iscte.condo.mapper;

import org.mapstruct.Mapper;
import pt.iscte.condo.controller.dto.Topic;
import pt.iscte.condo.domain.MeetingTopic;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class MeetingMapper {

    public abstract List<MeetingTopic> topics2MeetingTopics(List<Topic> topics);

}
