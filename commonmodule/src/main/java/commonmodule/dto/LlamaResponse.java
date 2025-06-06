//LlamaResponse.java
package commonmodule.dto;

import java.util.Map;

public class LlamaResponse {
	private String intent;
	private String action;
	private Map<String, String> entities;
	private String response;

	public String getIntent() {
		return intent;
	}

	public void setIntent(String intent) {
		this.intent = intent;
	}

	public Map<String, String> getEntities() {
		return entities != null ? entities : new java.util.HashMap<>();
	}

	public void setEntities(Map<String, String> entities) {
		this.entities = entities;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
}