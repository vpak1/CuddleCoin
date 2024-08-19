package ca.sheridancollege.pakv.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sheridancollege.pakv.beans.Transaction;
import ca.sheridancollege.pakv.beans.User;
import ca.sheridancollege.pakv.database.DatabaseAccess;

@Controller
public class BudgetController {
	@Autowired
	@Lazy
	private DatabaseAccess da;


	@GetMapping("/secure/transaction")
	public String transaction(Model model) {
		model.addAttribute("tr", new Transaction());
		model.addAttribute("trList", da.getTrList());
		return "/secure/transaction";
	}

	@GetMapping("/")
	public String index() {
		return "index";
	}
	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/register")
	public String getRegister() {
		return "register";
	}

	@PostMapping("/register")
	public String postRegister(@RequestParam String username, @RequestParam String password) {
		da.addUser(username, password);
		Long userId = da.findUserAccount(username).getUserId();
		da.addRole(username, userId, Long.valueOf(1));
		return "login";
	}

	@GetMapping("/permission-denied")
	public String permissionDenied() {
		return "/error/permission-denied";
	}

	@GetMapping("/admin/users")
	public String users(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("usList", da.getUsList());
		return "/admin/users";
	}

	@GetMapping("/admin/user/deleteUsById/{userId}")
	public String deleteUsById(Model model, @PathVariable Long userId) {
		da.deleteUsById(userId);
		da.deleteRoleById(userId);
		model.addAttribute("user", new User());
		model.addAttribute("usList", da.getUsList());
		return "redirect:/admin/users";
	}
	@PostMapping("/secure/transaction/insertTr")
    public String insertTr(Model model, @ModelAttribute Transaction tr) {
        da.insertTr(tr);
        model.addAttribute("tr", new Transaction());
        model.addAttribute("trList", da.getTrList());
        return "redirect:/secure/transaction";
    }

    @GetMapping("/secure/transaction/editTrById/{id}")
    public String editTrById(Model model, @PathVariable Long id) {
        Transaction tr = da.getTrListById(id).get(0);
        da.deleteTrById(id);
        model.addAttribute("tr", tr);
        model.addAttribute("trList", da.getTrList());
        return "redirect:/secure/transaction";
    }

    @GetMapping("/secure/transaction/deleteTrById/{id}")
    public String deleteTrById(Model model, @PathVariable Long id) {
        da.deleteTrById(id);
        model.addAttribute("tr", new Transaction());
        model.addAttribute("trList", da.getTrList());
        return "redirect:/secure/transaction";
    }

    @GetMapping("/secure/topCategories")
    public String topCategories(Model model) {
        List<String> categories = da.getTopCategories();
        model.addAttribute("categories", categories);
        return "/secure/topCategory";
    }
    
}