package pt.unl.fct.di.apdc.projectoindividual.data;

import com.google.appengine.repackaged.org.apache.commons.codec.digest.DigestUtils;
import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Transaction;

import pt.unl.fct.di.apdc.projetoindividual.util.RegisterData;

public class Database {
	
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	
	public Database() {
		
	}
	
	
//	public String registerUser(RegisterData userData) {
////		RegisterData userData = userData;
//		userData = new RegisterData(userData.getUsername(), userData.getEmail(), userData.getPassword(), 
//				userData.getConfirmation(), userData.getProfile(), userData.getPhoneNumber(), 
//				userData.getMobileNumber(), userData.getAddress(), userData.getExtraAddress(), userData.getLocation());
//				
//		
//		if(!userData.validRegistration()) {
//			return "Missing or wrong parameter.";
//		}
//		
//		Transaction txn = datastore.newTransaction();
//		
//		
//		try {
//			Key userKey = datastore.newKeyFactory().setKind("User").newKey(userData.getUsername());		
//			Entity user = txn.get(userKey);
//
//			if(user != null) {
//				txn.rollback();
////				new Exception("User already exists.");
//				return ("User already exists.");
//			}else {
//				user = Entity.newBuilder(userKey)
//						.set("user_email", userData.getEmail())
//						.set("user_pwd", DigestUtils.sha512Hex(userData.getPassword()))
//						.set("user_profile", userData.getProfile())
//						.set("user_phone_number", userData.getPhoneNumber())
//						.set("user_mobile_number", userData.getMobileNumber())
//						.set("user_address", userData.getAddress())
//						.set("user_extra_address", userData.getExtraAddress())
//						.set("location", userData.getLocation())
//						.set("user_role", userData.getRole())
//						.set("user_state", userData.getState())
////						.set("user_profile", user.profile)
////						.set("user_p", null)
//						.set("user_creation_time", Timestamp.now())
//						.build();
//				txn.add(user);
//				txn.commit();
//			}
//		}catch(Exception e) {
//			txn.rollback();
//			return e.toString();
//			
//		}finally {
//			if(txn.isActive()) {
//				txn.rollback();
////				return "Internal Server Error";
//			}
//		}
//		return "";
//	}
	

}
