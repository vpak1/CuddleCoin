package ca.sheridancollege.pakv.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.sheridancollege.pakv.beans.Goal;
import ca.sheridancollege.pakv.database.DatabaseAccess;

@RestController
@RequestMapping("/secure/api/v1/goal")
public class GoalController {
	@Autowired
	private DatabaseAccess da;
	@GetMapping(produces = "application/json")
	public List<Goal> getGoalCollection() {
	return da.findAll();
	}
	@GetMapping(value = "/{id}", produces = "application/json")
	public Goal getIndividualGoal(@PathVariable Long id) {
	return da.findById(id);
	}
	@PostMapping(consumes = "application/json", produces = "application/json")
	public String postGoal(@RequestBody Goal goal) {
	return "http://localhost:8080/secure/api/v1/goal" + da.save(goal);
	}
	@PutMapping(consumes = "application/json", produces = "application/json")
	public String putGoalCollection(@RequestBody List<Goal> glList) {
	da.deleteAll();
	da.saveAll(glList);
	return "Total Records: " + da.count();
	}
	@DeleteMapping
	public void deletedGoalCollection() {
	da.deleteAll();
	}
}
