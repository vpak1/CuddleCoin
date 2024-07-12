package ca.sheridancollege.pakv.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import ca.sheridancollege.pakv.beans.Transaction;
import ca.sheridancollege.pakv.database.DatabaseAccess;

@Controller
public class BudgetController {
	@Autowired
    private DatabaseAccess da;
	@GetMapping("/")
    public String index() {
        return "index";
    }
	@GetMapping("/transaction")
	public String transaction(Model model) {
        model.addAttribute("tr", new Transaction());
        model.addAttribute("trList", da.getTrList());
        return "transaction";
    }
	@PostMapping("/transaction/insertTr")
    public String insertTr(Model model, @ModelAttribute Transaction tr) {
	    da.insertTr(tr);
	    model.addAttribute("tr", new Transaction());
	    model.addAttribute("trList", da.getTrList());
//	    I had a problem with css when using a different path, so I searched up it in the internet and it suggested to add redirect:/
	    return "redirect:/transaction";
    }
	@GetMapping("/transaction/editTrById/{id}")
    public String editTrById(Model model, @PathVariable Long id) {
		Transaction tr = da.getTrListById(id).get(0);
		da.deleteTrById(id);
		model.addAttribute("tr", tr);
		model.addAttribute("trList", da.getTrList());
		return "redirect:/transaction";
    }

    @GetMapping("/transaction/deleteTrById/{id}")
    public String deleteTrById(Model model, @PathVariable Long id) {
        da.deleteTrById(id);
        model.addAttribute("tr", new Transaction());
        model.addAttribute("trList", da.getTrList());
        return "redirect:/transaction";
    }
    @GetMapping("/topCategories")
    public String topCategories(Model model) {
    	List<String> categories = da.getTopCategories();
        model.addAttribute("categories", categories);
        return "topCategory";
    }
}
