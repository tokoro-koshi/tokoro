package com.tokorokoshi.tokoro.modules.tags;

import com.tokorokoshi.tokoro.modules.ai.AiClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AiTagService {
    private final AiClientService clientService;

    @Autowired
    public AiTagService(AiClientService clientService) {
        this.clientService = clientService;
    }
}
