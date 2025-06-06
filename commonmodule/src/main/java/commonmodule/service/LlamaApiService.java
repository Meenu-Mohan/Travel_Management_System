package commonmodule.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ai.chat.client.ChatClient;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class LlamaApiService {

	private final ChatClient chatClient;
    private static final Logger logger = LogManager.getLogger(LlamaApiService.class);	
	public LlamaApiService(ChatClient.Builder chatClientBuilder) {
		this.chatClient = chatClientBuilder.build();

	}

	public String processInput(String userInput) {
		try {

			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/json");

			// Flight Module related prompts

			String checkAvailability = "When user input anything related to check or find available flights (then intent is only 'check_availability'"
					+ "then entities(noOFTickets(by default 1 if not specified),"
					+ "depAirport(corresponding departure airport code),"
					+ "arrAirport(corresponding arrival airport code),"
					+ "depatureDate(dateformat'YYYY-MM-DD' & if not specified today's date by default),"
					+ "arrivalDate(dateformat'YYYY-MM-DD' & if not specified today's date by default)";
			
			String airportCode = "For depAirport and arrAirport it should be IATA Code. If user input is not IATA Airport Code convert to IATA Airport Code";

			String viewTicketPrompt = "When user input anything related to view or show or booking details of tickets along with reservationId"
					+ "then(intent='view_ticket',action='flight', entities (reservationId(integer))";

			String bookPrompt = "When user inputs is anything related to booking or registration or reservation of flight(intent = 'book_flight', action = 'flight')"
					+ "then entities(flightId(String), numberOfTickets(default 1), reservedClass(default ECONOMY).";

			String passengerPrompt = "when user inputs anything related to passenger details includes sessionId, passengerName, age, phoneNumber, emailAddress,"
					+ " dateofBirth, passportNumber (intent='passenger_details', action='flight')"
					+ "then entities(passengerName(string),age(integer),phoneNumber(long),emailAddress(string),"
					+ "dateOfBirth(dateformat 'YYYY-MM-DD' only),passportNumber(string)";

			String casualPrompt = "When user input is anything which is not related to flight and hotel"
					+ "then (intent = 'casual' action='chat' then entities(response(String) which is answer for the user input)";
			
			String jsonFormatExample = "{\"intent\": \"view_ticket\", \"action\": \"flight\", \"entities\": {\"reservationId\": 37}}";

			String task = "Extract json from this input: '" + userInput + "'. "
					+ "Return JSON where 'action' can be (flight or chat)'intent' can only be ('check_availability', 'book_flight', 'view_ticket', 'passenger_details' or 'casual').No other intent possible."
					+ " intent('check_availability', 'book_flight', 'view_ticket', 'passenger_details') belongs to action 'flight'. intent ('casual') belongs to action('chat')."
					+ "and 'entities'" + checkAvailability + airportCode + bookPrompt + passengerPrompt + viewTicketPrompt
					+ casualPrompt +  "Default values are used when user input for corresponding entities are empty."
							+ "Ensure the output is valid JSON only no explain needed."
					+ "Return ONLY one-line valid JSON with properly quoted keys and comma-separated values. "
					+ "Ensure no missing commas or extra characters. JSON should contain: 'intent', 'action', 'entities' (map), and optional 'response'. "
					+ "Do NOT return explanations, only JSON.There should be only one json. Example for json format: " + jsonFormatExample;

			// Send message to LLaMA model
			String llamaResponse = chatClient.prompt().user(task).call().content();
			return llamaResponse != null ? extractJsonFromResponse(llamaResponse)
					: "{\"intent\":\"error\",\"response\":\"No response from LLAMA.\"}";
		} catch (Exception e) {
			return "{\"intent\":\"error\",\"response\":\"Error connecting to LLAMA API: " + e.getMessage() + "\"}";
		}
	}

	private String extractJsonFromResponse(String response) {
		try {
			int start = response.indexOf("{\"");
			if (start == -1) {
				return "{\"intent\":\"error\",\"response\":\"No valid JSON found in response: " + response + "\"}";
			}
			int end = response.lastIndexOf("}");
			if (end == -1 || end <= start) {
				return "{\"intent\":\"error\",\"response\":\"Invalid JSON structure in response: " + response + "\"}";
			}
			return response.substring(start, end + 1);
		} catch (Exception e) {
			return "{\"intent\":\"error\",\"response\":\"Failed to extract JSON: " + e.getMessage() + "\"}";
		}
	}
}