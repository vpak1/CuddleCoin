package ca.sheridancollege.pakv.database;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.pakv.beans.Transaction;

@Repository
public class DatabaseAccess {
    @Autowired
    protected NamedParameterJdbcTemplate jdbc;
    
    public void insertTr(Transaction tr) {
        String query = "INSERT INTO budget (type, date, amount, category, description, budget) VALUES (:type, :date, :amount, :category, :description, :budget)";
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("type", tr.getType());
        namedParameters.addValue("date", tr.getDate());
        namedParameters.addValue("amount", tr.getAmount());
        namedParameters.addValue("category", tr.getCategory());
        namedParameters.addValue("description", tr.getDescription());
        
        double updatedBudget = updateBudget(tr);
        tr.setBudget(updatedBudget);
        namedParameters.addValue("budget", tr.getBudget());
        int rowsAffected = jdbc.update(query, namedParameters);
        if (rowsAffected > 0) {
            System.out.println("Transaction inserted into the database");
        }
    }
    public List<Transaction> getTrList() {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        String query = "SELECT * FROM budget";
        return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<Transaction>(Transaction.class));
    }
    public void deleteTrById(Long id) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        String query = "DELETE FROM budget WHERE id = :id";
        namedParameters.addValue("id", id);
        if (jdbc.update(query, namedParameters) > 0) {
            System.out.println("Deleted transaction " + id + " from the database.");
        }
    }
    public List<Transaction> getTrListById(Long id) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        String query = "SELECT * FROM budget WHERE id = :id";
        namedParameters.addValue("id", id);
        return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<Transaction>(Transaction.class));
    }
//    updating budget based on income and expenses
    private double updateBudget(Transaction tr) {
        double currentBudget = calculateBudget();        
        if ("Income".equals(tr.getType())) {
            currentBudget += tr.getAmount();
        } else if ("Expense".equals(tr.getType())) {
            currentBudget -= tr.getAmount();
        }
        
        return currentBudget;
    }
    public double calculateBudget() {
    	MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    	String query = "SELECT * FROM budget ";
    	double totalIncome = 0.0;
        double totalExpense = 0.0;
    	List<Transaction> transactions= jdbc.query(query, namedParameters, new BeanPropertyRowMapper<Transaction>(Transaction.class));
    	for (Transaction transaction : transactions) {
            if ("Income".equals(transaction.getType())) {
                totalIncome += transaction.getAmount();
            } else if ("Expense".equals(transaction.getType())) {
                totalExpense += transaction.getAmount();
            }
        }
        
        return totalIncome - totalExpense;
    }
//    top 5 categories are determined based on how many time category name appeared
    public List<String> getTopCategories() {
    	MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        String query = "SELECT category FROM budget WHERE type = 'Expense' GROUP BY category ORDER BY COUNT(*) DESC LIMIT 5";
        return jdbc.queryForList(query, namedParameters, String.class);
    }
    
}
