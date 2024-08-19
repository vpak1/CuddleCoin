package ca.sheridancollege.pakv.beans;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Transaction {
	Long id;
	double amount;
	String description;
	String type;
	String category;
	double budget;
	private LocalDate date;
	private final String[] TYPES = {"Expense", "Income"};
	private final String[] CATEGORIES = {"Groceries", "Shopping", "Entertainment", "Rent", "Takeout", "Salary"};
}



