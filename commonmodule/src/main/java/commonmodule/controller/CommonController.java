package commonmodule.controller;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import commonmodule.dto.ChatRequestDTO;
import commonmodule.dto.ChatResponseDTO;
import commonmodule.service.FlightCommonService;
import commonmodule.service.HotelCommonService;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/common")
public class CommonController {

    private static final Logger log = LoggerFactory.getLogger(CommonController.class);
    private final FlightCommonService flightCommonService;
    private final HotelCommonService hotelCommonService;

    public CommonController(FlightCommonService flightCommonService, HotelCommonService hotelCommonService) {
        this.flightCommonService = flightCommonService;
        this.hotelCommonService = hotelCommonService;
    }

    //For Flight
    @GetMapping("/chat")
    public ResponseEntity<String> chatGet(@RequestParam("message") String message) {
        if (message == null || message.trim().isEmpty()) {
            log.warn("Received empty message in GET request");
            return ResponseEntity.badRequest().body("Message is empty.");
        }
        log.info("Received GET chat request: {}", message);
        String response = flightCommonService.processUserInput(message);
        log.info("GET chat response: {}", response);
        return ResponseEntity.ok(response);
    }

    //For Hotel
    @PostMapping("/chat")
    public ResponseEntity<ChatResponseDTO> chatPost(@Valid @RequestBody ChatRequestDTO request) {
        log.info("Received POST chat request: {}", request.getMessage());
        try {
            ChatResponseDTO response = hotelCommonService.processUserInput(request);
            log.info("POST chat response: {}", response.getResponse());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error processing POST chat request: {}", request.getMessage(), e);
            ChatResponseDTO errorResponse = new ChatResponseDTO();
            errorResponse.setResponse("Oops! Something went wrong.");
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}