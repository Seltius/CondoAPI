package pt.iscte.condo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.iscte.condo.service.OpenAIService;

@Service
@RequiredArgsConstructor
public class OpenAIServiceImpl implements OpenAIService {

    @Override
    public String startAssistant(String transcript) {
        return null;
    }

    @Override
    public String runAssistant(String threadId, String question) {
        return null;
    }
}
