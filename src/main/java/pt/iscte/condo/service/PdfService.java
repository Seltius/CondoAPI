package pt.iscte.condo.service;

import java.util.Map;

public interface PdfService {

    byte[] generateMinute(Map<String, String> data) throws Exception;

}
