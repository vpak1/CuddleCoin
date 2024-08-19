package ca.sheridancollege.pakv.beans;


import org.springframework.lang.NonNull;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor

public class User {
	private Long userId;
	@NonNull
	private String email;
	@NonNull
	private String encryptedPassword;
	@NonNull
	private Boolean enabled;
	@NonNull
	private Boolean roleId;
}
