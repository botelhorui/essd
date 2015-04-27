package pt.tecnico.bubbledocs.service.dto;

import pt.tecnico.bubbledocs.domain.User;

public class UserDTO {
	private String username;
	private String password;
	private String name;
	private String email;

	public UserDTO(User user) {
		username = user.getUsername();
		password = user.getPassword();
		name = user.getName();
		email = user.getEmail();
	}
	
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

}
