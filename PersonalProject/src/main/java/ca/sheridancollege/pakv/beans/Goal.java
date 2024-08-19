package ca.sheridancollege.pakv.beans;

import org.springframework.lang.NonNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Goal {
	Long id;
	@NonNull
	String goalName;
	@NonNull
	Double goalAmount;
	@NonNull
	Double currentAmount;
}
