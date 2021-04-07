package pt.unl.fct.di.apdc.projetoindividual.util;

import java.text.DateFormat;
import java.util.regex.Pattern;

public class RegisterData {
	
	public String username;
	public String email;
	public String password;
	public String confirmation;
	public String profile;
	public String phoneNumber;
	public String mobileNumber;
	public String address;
	public String extraAddress;
	public String location;
	private String role;
	
	private String state;
	
	public RegisterData() {	
	}
	
	public RegisterData(String username, String email, String password, 
			String confirmation, String profile, String phoneNumber, 
			String mobileNumber, String address, String extraAddress,
			String location) {
		
		this.username = username;
		this.email = email;
		this.password = password;
		this.profile = profile;
		this.phoneNumber = phoneNumber;
		this.mobileNumber = mobileNumber;
		this.address = address;
		this.extraAddress = extraAddress;
		this.location = location;
		this.role = "USER";
		this.state = "ENABLED";
	}
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	private boolean isValid(String text) {
		return text != null && !text.isEmpty();
	}
	
	public boolean ValidRegistration() {
		return this.isValid(this.username)
				&& this.validatePassword(this.password)
				&& this.password.equals(this.confirmation)
				&& this.validateEmail(this.email);
		
	}
	
	public boolean validatePassword(String password) {
		Pattern pattern = Pattern.compile("^(?=[a-z])(?=.[A-Z])(?=.\\d)(?=.[\\W])[^]{8.}$");
		//maiusculas, minusculas, pelo menos 1 maiuscula, pelo menos 1 numero, pelo menos 8 chars
		return isValid(password) && pattern.matcher(password).matches();
	}
	
	
	public boolean validateEmail(String email) {
		Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		
		return isValid(email) && pattern.matcher(email).matches();
	}


}
