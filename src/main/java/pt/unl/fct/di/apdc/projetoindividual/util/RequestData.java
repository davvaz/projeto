package pt.unl.fct.di.apdc.projetoindividual.util;

public class RequestData {

	public AuthToken token;
	public String userToDelete;
	public RegisterData userData;
	public String userToChange;
	public String attribute;
	
	public RequestData() {
		
	}
	
	public RequestData(AuthToken token, String userToDelete) {
		this.token = token;
		this.userToDelete = userToDelete;
		//delete
	}
	
	
	public RequestData(AuthToken token, RegisterData userData) {
		this.token = token;
		this.userData = userData;
		//update
	}
	
	
	public RequestData(AuthToken token, String userToChange, String attribute) {
		this.token = token;
		this.userToChange = userToChange;
		this.attribute = attribute;
		//role and state
	}

	public String getUserToChange() {
		return userToChange;
	}

	public void setUserToChange(String userToChange) {
		this.userToChange = userToChange;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String roleToChange) {
		this.attribute = roleToChange;
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
