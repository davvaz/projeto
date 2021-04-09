package pt.unl.fct.di.apdc.projetoindividual.util;

public class RequestData {

	public AuthToken token;
	public String userToDelete;
	public RegisterData userData;
	public String userToChange;
	public String roleToChange;
	
	public RequestData() {
		
	}
	
	public RequestData(AuthToken token, String userToDelete) {
		this.token = token;
		this.userToDelete = userToDelete;
	}
	
	
	public RequestData(AuthToken token, RegisterData userData) {
		this.token = token;
		this.userData = userData;
	}
	
	
	public RequestData(AuthToken token, String userToChange, String roleToChange) {
		this.token = token;
		this.userToChange = userToChange;
		this.roleToChange = roleToChange;
	}

	public String getUserToChange() {
		return userToChange;
	}

	public void setUserToChange(String userToChange) {
		this.userToChange = userToChange;
	}

	public String getRoleToChange() {
		return roleToChange;
	}

	public void setRoleToChange(String roleToChange) {
		this.roleToChange = roleToChange;
	}

	public AuthToken getToken() {
		return token;
	}

	public void setToken(AuthToken token) {
		this.token = token;
	}

	public String getUserToDelete() {
		return userToDelete;
	}

	public void setUserToDelete(String userToDelete) {
		this.userToDelete = userToDelete;
	}

	public RegisterData getUserData() {
		return userData;
	}

	public void setUserData(RegisterData userData) {
		this.userData = userData;
	}
	
	
	
}
