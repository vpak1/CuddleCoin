package ca.sheridancollege.pakv.database;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.pakv.beans.Goal;
import ca.sheridancollege.pakv.beans.Transaction;
import ca.sheridancollege.pakv.beans.User;

@Repository
public class DatabaseAccess {
	@Autowired
	protected NamedParameterJdbcTemplate jdbc;
	@Autowired
	private PasswordEncoder passwordEncoder;

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
		List<Transaction> transactions = jdbc.query(query, namedParameters,
				new BeanPropertyRowMapper<Transaction>(Transaction.class));
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

	public User findUserAccount(String email) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT * FROM sec_user where email = :email";
		namedParameters.addValue("email", email);
		try {
			return jdbc.queryForObject(query, namedParameters, new BeanPropertyRowMapper<>(User.class));
		} catch (EmptyResultDataAccessException erdae) {
			return null;
		}
	}

	public List<String> getRolesById(Long userId) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT sec_role.roleName " + "FROM user_role, sec_role "
				+ "WHERE user_role.roleId = sec_role.roleId " + "AND userId = :userId";
		namedParameters.addValue("userId", userId);
		return jdbc.queryForList(query, namedParameters, String.class);
	}

	public void addUser(String email, String password) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "INSERT INTO sec_user " + "(email, encryptedPassword, enabled) "
				+ "VALUES (:email, :encryptedPassword, 1)";
		namedParameters.addValue("email", email);
		namedParameters.addValue("encryptedPassword", passwordEncoder.encode(password));
		jdbc.update(query, namedParameters);
	}

	public void addRole(String email, Long userId, Long roleId) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "INSERT INTO user_role (userId, roleId) " + "VALUES (:userId, :roleId)";
		namedParameters.addValue("userId", userId);
		namedParameters.addValue("roleId", roleId);
		jdbc.update(query, namedParameters);
	}

	public List<User> getUsList() {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT * FROM sec_user";
		return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<User>(User.class));
	}

	public void deleteUsById(Long userId) {
		deleteRoleById(userId);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "DELETE FROM sec_user WHERE userId = :userId";
		namedParameters.addValue("userId", userId);
		int usersDeleted = jdbc.update(query, namedParameters);
		if (usersDeleted > 0) {
			System.out.println("Deleted user " + userId + " from the database.");
		} else {
			System.out.println("No user found with userId " + userId);
		}
	}

	public void deleteRoleById(Long userId) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "DELETE FROM user_role WHERE userId = :userId";
		namedParameters.addValue("userId", userId);
		int userRolesDeleted = jdbc.update(query, namedParameters);
		if (userRolesDeleted > 0) {
			System.out.println("Deleted roles for user " + userId + " from the database.");
		} else {
			System.out.println("No roles found for user with userId " + userId);
		}
	}

	public Goal findById(Long id) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT * FROM financial_goals WHERE id=:id";
		namedParameters.addValue("id", id);
		return jdbc.queryForObject(query, namedParameters, new BeanPropertyRowMapper<Goal>(Goal.class));
	}

	public Long save(Goal goal) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
		String query = "INSERT INTO financial_goals (goalName, goalAmount, currentAmount) VALUES (:goalName, :goalAmount, :currentAmount)";
		namedParameters.addValue("goalName", goal.getGoalName());
		namedParameters.addValue("goalAmount", goal.getGoalAmount());
		namedParameters.addValue("currentAmount", goal.getCurrentAmount());

		jdbc.update(query, namedParameters, generatedKeyHolder);
		return (Long) generatedKeyHolder.getKey();
	}

	public void deleteAll() {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "DELETE FROM financial_goals";
		jdbc.update(query, namedParameters);
	}

	public void saveAll(List<Goal> glList) {
		for (Goal gl : glList) {
			save(gl);
		}
	}

	public Long count() {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT count(*) FROM financial_goals";
		return jdbc.queryForObject(query, namedParameters, Long.TYPE);
	}

	public List<Goal> findAll() {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT * FROM financial_goals";
		return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<Goal>(Goal.class));
	}

}
