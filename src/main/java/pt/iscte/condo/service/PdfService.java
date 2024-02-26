package pt.iscte.condo.service;

import pt.iscte.condo.controller.dto.Topic;
import pt.iscte.condo.repository.entities.Condominium;
import pt.iscte.condo.repository.entities.Meeting;
import pt.iscte.condo.service.dto.FractionInfo;

import java.util.List;
import java.util.Map;

public interface PdfService {

    byte[] generateMinute(List<Topic> topics, List<FractionInfo> fractionInfos, Map<String, String> aiSummarizedTopics, Condominium condominium, Meeting meeting) throws Exception;

}
