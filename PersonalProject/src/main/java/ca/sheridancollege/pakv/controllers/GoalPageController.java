package ca.sheridancollege.pakv.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import ca.sheridancollege.pakv.beans.Goal;

@Controller
public class GoalPageController {
	final String REST_URL = "http://localhost:8080/secure/api/v1/goal";

	@GetMapping("/secure/goal")
	public String goal(Model model, RestTemplate restTemplate) {	
		ResponseEntity<Goal[]> responseEntity = restTemplate.getForEntity(REST_URL, Goal[].class);
		model.addAttribute("glList", responseEntity.getBody());
		return "/secure/goal";
	}

	@GetMapping(value = "/getGoal/{id}", produces = "application/json")
	@ResponseBody
	public Goal getGoal(@PathVariable int id, RestTemplate restTemplate) {
		ResponseEntity<Goal> responseEntity = restTemplate.getForEntity(REST_URL + "/" + id, Goal.class);
		return responseEntity.getBody();
	}
}
