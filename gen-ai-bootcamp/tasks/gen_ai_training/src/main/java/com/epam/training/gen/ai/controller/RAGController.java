package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.service.RAGService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * This class has the APIs for RAG operations
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class RAGController {

    private final RAGService ragService;

    @GetMapping("/docAssistant")
    public Mono<ResponseEntity<String>> ragResponse(@RequestParam String text){
    return ragService.getRagResponse(text).map(ResponseEntity::ok);
    }
}
