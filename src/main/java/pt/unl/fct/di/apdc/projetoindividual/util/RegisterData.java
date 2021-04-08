package pt.unl.fct.di.apdc.projetoindividual.util;

import java.text.DateFormat;
import java.util.regex.Pattern;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.appengine.repackaged.org.apache.commons.codec.digest.DigestUtils;
import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Transaction;

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
	
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	
	public RegisterData() {	
	}
	
	public RegisterData(String username, String email, String password, 
			String confirmation, String profile, String phoneNumber, 
			String mobileNumber, String address, String extraAddress,
			String location) {
		
		this.username = username;
		this.email = email;
		this.password = password;
		this.confirmation = confirmation;
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
	
	public boolean validRegistration() {
		return this.isValid(this.username)
				&& this.validatePassword(this.password)
				&& this.password.equals(this.confirmation)
				&& this.validateEmail(this.email);
		
	}
	
	public boolean validatePassword(String pass) {
//		Pattern pattern = Pattern.compile("^(?=[a-z])(?=.[A-Z])(?=.\\d)(?=.[\\W])[^]{8.}$");
		//Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\S+$).{8,}$");
		
		String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}";
		//pelo menos 1 minuscula, pelo menos 1 maiuscula, pelo menos 8 chars, pelo menos 1 digito e sem espacos brancos
		
//		Pattern pattern = Pattern.compile("^[_A-Za-z0-9-]");
//		maiusculas, minusculas, pelo menos 1 maiuscula, pelo menos 1 numero, pelo menos 8 chars
//		return isValid(password) && pattern.matcher(password).matches();
//		return true;
//		return pattern.matcher(password).matches();
		return isValid(pass) && pass.matches(pattern);
		
	}
	
	
	public boolean validateEmail(String mail) {
		Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		
		return isValid(mail) && pattern.matcher(mail).matches();
//		return true;
	}

//	public String registerUser(String username, String email, String password, 
//			String confirmation, String profile, String phoneNumber, 
//			String mobileNumber, String address, String extraAddress,
//			String location) {
//		
//		RegisterData user = new RegisterData(username, email, password, 
//				confirmation, profile, phoneNumber, 
//				mobileNumber, address, extraAddress, location);
//				
//		
//		if(!validRegistration()) {
//			return "Invalid";
//		}
//		
//		Transaction txn = datastore.newTransaction();
//		
//		
//		try {
//			Key userKey = datastore.newKeyFactory().setKind("User").newKey(user.username);		
//			Entity entityUser = txn.get(userKey);
//
//			if(entityUser != null) {
//				txn.rollback();
//				new Exception("User already exists.");
//			}else {
//				entityUser = Entity.newBuilder(userKey)
//						.set("user_email", user.email)
//						.set("user_pwd", DigestUtils.sha512Hex(user.password))
////						.set("user_profile", user.profile)
////						.set("user_p", null)
//						.set("user_creation_time", Timestamp.now())
//						.build();
//				txn.add(entityUser);
//				txn.commit();
//			}
//		}catch(Exception e) {
//			txn.rollback();
//			return e.toString();
//			
//		}finally {
//			if(txn.isActive()) {
//				txn.rollback();
//			}
//		}
//		return "";
//	}

	
	
}
