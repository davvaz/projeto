package pt.unl.fct.di.apdc.projetoindividual.resources;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.appengine.repackaged.org.apache.commons.codec.digest.DigestUtils;
import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.PathElement;
import com.google.cloud.datastore.Transaction;
import com.google.gson.Gson;

import pt.unl.fct.di.apdc.projetoindividual.util.AuthToken;
//import pt.unl.fct.di.apdc.projectoindividual.data.Database;
import pt.unl.fct.di.apdc.projetoindividual.util.RegisterData;
import pt.unl.fct.di.apdc.projetoindividual.util.RequestData;



@Path("/users")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class UserResource {

	private static final Logger LOG = Logger.getLogger(UserResource.class.getName());
	
	
	private static final DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
	public String timestamp;
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	
	private final Gson g = new Gson();
	
	public UserResource() {
		
	}
	
//	@GET
//	@Path("/test")
//	public Response test() {
//		return Response.ok("{ }").build();
//		
//	}
//	
////	
//	@POST
//	@Path("/user2")
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Response doRegister2(RegisterData data) {
//		LOG.fine("Attempt to register user " +
//				 data.username);
//				 
//				 // Checks input data
//				 
//				 if(! data.validRegistration()) { 
//					 return Response.status(Status.BAD_REQUEST).entity("Missing or wrong parameter.").build(); 
//				}
//				  
//				  //Creates an entity user from the data. The key is the username 
////				 	Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.username); 
////				 	Entity user = datastore.get(userKey);
//				
////				  if(user != null) { 
////					  return Response.status(Status.BAD_REQUEST).entity("User already exists.").build(); 
////				}
////				  user = Entity.newBuilder(userKey) 
////						  .set("user_name", data.name)
////						  .set("user_pwd", DigestUtils.sha512Hex(data.password)) 
////						  .set("user_email",data.email) 
////						  .set("user_creation_time", Timestamp.now()) .build();
////				  datastore.add(user); //se houver algum problema vai dar uma excecao, deviamos
//				  LOG.info("User registered " + data.username); 
//				  return  Response.ok(" {} ").build(); 
//	}
//	
	
//	@POST
//	@Path("/user")
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Response doRegister(RegisterData data) {
//		
//		LOG.warning("Attempt to register user " + data.username);
//		LOG.fine("Attempt to register user " + data.username);
//		
//		
//
//		String response = data.registerUser(data.username, data.email, data.password, data.confirmation, 
//				data.profile, data.phoneNumber, data.mobileNumber, data.address, data.extraAddress, data.location);
//		if(!response.isEmpty()) {
//			
//			return Response.status(Status.BAD_REQUEST).entity(response).build();
//			
//		}else
//			return Response.ok("{ }").build();
//
//	}
//	
//	@POST
//	@Path("/user3")
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Response doRegister3(RegisterData data ) {
//	
//		Database db = new Database();
//		LOG.warning("Attempt to register user " + data.username);
//		LOG.fine("Attempt to register user " + data.username);
//		
//		String response = db.registerUser(data);
//
////		String response = data.registerUser(data.username, data.email, data.password, data.confirmation, 
////				data.profile, data.phoneNumber, data.mobileNumber, data.address, data.extraAddress, data.location);
//		if(!response.isEmpty()) {
//			
//			return Response.status(Status.BAD_REQUEST).entity(response).build();
//			
//		}else
//			return Response.ok("{ }").build();
//	}
	
	
	@POST
	@Path("/insert") //register
	@Consumes(MediaType.APPLICATION_JSON)
	public Response doRegister(RegisterData data) {
		LOG.warning("Attempt to register user " + data.getUsername());
		
		
		// Checks input data
		
		if(! data.validRegistration()) {
			return Response.status(Status.BAD_REQUEST).entity("Missing or wrong parameter.").build();
		}
		
		//Creates an entity user from the data. The key is the username
	
		Transaction txn = datastore.newTransaction();

		try {
			Key userKey = datastore.newKeyFactory()
					.setKind("User")
					.newKey(data.getUsername());		
			Entity user = txn.get(userKey);

			if(user != null) {
				txn.rollback();
				return Response.status(Status.BAD_REQUEST).entity("User already exists.").build();	
			}else {
				
			//Entity user = datastore.get(userKey);	
				user = Entity.newBuilder(userKey)
						.set("user_email", data.getEmail())
						.set("user_pwd", DigestUtils.sha512Hex(data.getPassword()))
						.set("user_profile", data.getProfile())
						.set("user_phone_number", data.getPhoneNumber())
						.set("user_mobile_number", data.getMobileNumber())
						.set("user_address", data.getAddress())
						.set("user_extra_address", data.getExtraAddress())
						.set("location", data.getLocation())
						.set("user_role", "USER")
						.set("user_state", "ENABLED")
						.set("user_creation_time", Timestamp.now())
						.build();
				txn.add(user);
				LOG.warning("User registered " + data.getUsername());
				txn.commit();
				
				return Response.ok(" {} ").build();
			}
			
			
		}catch(Exception e) {
			txn.rollback();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}finally{
			if(txn.isActive()) {
				txn.rollback();
				return Response.status(Status.INTERNAL_SERVER_ERROR).build();
			}
			
		}

	}
	
	
	@POST
	@Path("/delete")
	public Response doDelete(RequestData data) {
		LOG.warning("Attempt to delete user " + data.userToDelete );
		
		Transaction txn = datastore.newTransaction();
		
		try {
			Key userKey = datastore.newKeyFactory()
					.setKind("User")
					.newKey(data.token.getUsername());
			Entity userEntity = txn.get(userKey);
			
			Key tokenKey = datastore.newKeyFactory()
					.addAncestor(PathElement.of("User", data.token.getUsername()))
					.setKind("Token")
					.newKey(data.token.getTokenID());
			Entity tokenEntity = txn.get(tokenKey);
			
			Key userDelKey = datastore.newKeyFactory()
					.setKind("User")
					.newKey(data.userToDelete);
			Entity userDelEntity = txn.get(userDelKey);
			
			if(tokenEntity == null || System.currentTimeMillis() > data.token.getExpirationData()) {
				txn.rollback();
				LOG.warning("Token Authentication Failed");
				return Response.status(Status.FORBIDDEN).build();
			}
			
			if(userDelEntity == null) {
				txn.rollback();
				LOG.warning("No user to delete");
				return Response.status(Status.BAD_REQUEST).entity("No such user.").build();
			}
			
			if(userEntity.getString("user_role").equals("USER") && data.token.getUsername().equals(data.userToDelete)) {
				
				if(userEntity.getString("user_state").equals("DELETED")) {
					txn.rollback();
					LOG.warning("No such user, already deleted");
					return Response.status(Status.FORBIDDEN).build();
				}//added recently
				
				Entity deletedUser = Entity.newBuilder(userDelEntity).set("user_state", "DELETED").build();
				
				txn.put(deletedUser);
				LOG.warning("Self User deleted: " + data.userToDelete);
				txn.commit();
				return Response.ok(" {} ").build(); 
			}
			if((userEntity.getString("user_role").equals("GBO") || userEntity.getString("user_role").equals("GA") ) 
					&& userDelEntity.getString("user_role").equals("USER")) {
				
				if(userDelEntity.getString("user_state").equals("DELETED")) {
					txn.rollback();
					LOG.warning("No such user, already deleted");
					return Response.status(Status.FORBIDDEN).build();
				}//added recently
				
				Entity deletedUser = Entity.newBuilder(userDelEntity).set("user_state", "DELETED").build();
				
				txn.put(deletedUser);
				LOG.warning("Another user deleted: " + data.userToDelete);
				txn.commit();
				return Response.ok(" {} ").build(); 
			}
			
			//return Response.status(Status.BAD_REQUEST).entity("Failed user login.").build();
			
		}catch(Exception e) {
			txn.rollback();
			LOG.warning("exception "+ e.toString());
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
		}finally {
			if(txn.isActive()) {
				txn.rollback();
				LOG.warning("entered finally");
				return Response.status(Status.INTERNAL_SERVER_ERROR).build();
			}
			
		}
		
		LOG.warning("Failed delete attempt for username: " + data.userToDelete);
		//return Response.status(Status.FORBIDDEN).build();
		return Response.status(Status.BAD_REQUEST).entity("ups").build();
	}
	
	
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response doUpdate(RequestData data) {
		LOG.warning("Attempt to update user: "+ data.getUserData().getUsername());
		
		Transaction txn = datastore.newTransaction();
		Key userKey = datastore.newKeyFactory()
				.setKind("User")
				.newKey(data.token.getUsername());
		Key tokenKey = datastore.newKeyFactory()
				.addAncestor(PathElement.of("User", data.token.getUsername()))
				.setKind("Token")
				.newKey(data.token.getTokenID());
		try {
			
			Entity userEntity = txn.get(userKey);
			Entity tokenEntity = txn.get(tokenKey);
			
			if(tokenEntity == null || System.currentTimeMillis() > data.token.getExpirationData()) {
				txn.rollback();
				LOG.warning("Token Authentication Failed");
				return Response.status(Status.FORBIDDEN).build();
			}
			
			
			if(userEntity.getString("user_role").equals("USER") && (!userEntity.getString("user_state").equals("DELETED") 
					|| !userEntity.getString("user_state").equals("DISABLED")) ) {
				
				String email= data.getUserData().validateEmail() ? data.getUserData().getEmail() : userEntity.getString("user_email");
				
//				String password = data.getUserData().validatePassword() ? data.getUserData().getPassword() : userEntity.getString("user_password");
				
//				String password = data.getUserData().validatePassword() ? DigestUtils.sha512Hex(data.getUserData().getPassword()) : userEntity.getString("user_pwd");
				
				String profile = data.getUserData().getProfile()!=null && !data.getUserData().getProfile().isEmpty() 
						? data.getUserData().getProfile() : userEntity.getString("user_profile");
				
				String phoneNumber = data.getUserData().getPhoneNumber() != null && !data.getUserData().getPhoneNumber().isEmpty()
						? data.getUserData().getPhoneNumber() : userEntity.getString("user_phone_number");
				
				String mobileNumber = data.getUserData().getMobileNumber() != null && !data.getUserData().getMobileNumber().isEmpty()
						? data.getUserData().getMobileNumber() : userEntity.getString("user_mobile_number");
				
				String address = data.getUserData().getAddress() != null && !data.getUserData().getAddress().isEmpty()
						? data.getUserData().getAddress() : userEntity.getString("user_address");
				
				String extraAddress = data.getUserData().getExtraAddress() != null && !data.getUserData().getExtraAddress().isEmpty()
						? data.getUserData().getExtraAddress() : userEntity.getString("user_extra_address");
				
				String location = data.getUserData().getLocation() != null &&  !data.getUserData().getLocation().isEmpty()
						? data.getUserData().getLocation() : userEntity.getString("user_location");
				
				userEntity = Entity.newBuilder(datastore.get(userKey))
						.set("user_email", email)
//						.set("user_pwd", userEntity.getString("user_pwd"))
//						.set("user_pwd", password)
						.set("user_profile", profile)
						.set("user_phone_number", phoneNumber)
						.set("user_mobile_number", mobileNumber)
						.set("user_address", address)
						.set("user_extra_address", extraAddress)
						.set("location", location)
//						.set("user_role", userEntity.getString("user_role"))
//						.set("user_state", userEntity.getString("user_state"))
//						.set("user_creation_time", userEntity.getTimestamp("user_creation_time"))
						.build();
										
				txn.update(userEntity);
				LOG.warning("Self User updated: " + data.getUserData().getUsername());
				txn.commit();
//				return 
				return Response.ok(" {} ").build(); 
				
			}
			
			
			
		}catch(Exception e) {
			txn.rollback();
			LOG.warning("exception "+ e.toString());
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
		}finally {
			if(txn.isActive()) {
				txn.rollback();
				LOG.warning("entered finally");
				return Response.status(Status.INTERNAL_SERVER_ERROR).build();
			}
		}
		LOG.warning("Failed update attempt for username: " + data.getUserData().getUsername());
		return Response.status(Status.BAD_REQUEST).entity("ups").build();
		
	}
	
	@POST
	@Path("/role/change")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response doRoleChange(RequestData data) {
		LOG.warning("Attempt to change role on user: "+ data.getUserToChange());
		
		Transaction txn = datastore.newTransaction();
		
		try {
			Key userKey = datastore.newKeyFactory()
					.setKind("User")
					.newKey(data.token.getUsername());
			Entity userEntity = txn.get(userKey);
			
			Key tokenKey = datastore.newKeyFactory()
					.addAncestor(PathElement.of("User", data.token.getUsername()))
					.setKind("Token")
					.newKey(data.token.getTokenID());
			Entity tokenEntity = txn.get(tokenKey);
			
			Key updatedUserKey = datastore.newKeyFactory()
					.setKind("User")
					.newKey(data.getUserToChange());
			Entity updatedUserEntity = txn.get(updatedUserKey);
			
			if(tokenEntity == null || System.currentTimeMillis() > data.token.getExpirationData()) {
				txn.rollback();
				LOG.warning("Token Authentication Failed");
				return Response.status(Status.FORBIDDEN).build();
			}
			
			if(userEntity.getString("user_role").equals("SU")) {
				if(data.getAttribute().equals("GA") && !updatedUserEntity.getString("user_role").equals("GA")) {
					Entity updatedUser = Entity.newBuilder(updatedUserEntity)
							.set("user_role","GA").build();
					
					txn.put(updatedUser);
					LOG.warning("User role changed to GA");
					txn.commit();
					return Response.ok(" {} ").build(); 
				}
				if(data.getAttribute().equals("GBO") && !updatedUserEntity.getString("user_role").equals("GBO")) {
					Entity updatedUser = Entity.newBuilder(updatedUserEntity)
							.set("user_role","GBO").build();
					
					txn.put(updatedUser);
					LOG.warning("User role changed to GBO");
					txn.commit();
					return Response.ok(" {} ").build();
				}
					
			}
			if(userEntity.getString("user_role").equals("GA")) {
				if(updatedUserEntity.getString("user_role").equals("USER")) {
					Entity updatedUser = Entity.newBuilder(updatedUserEntity)
							.set("user_role", "GBO").build();
					txn.put(updatedUser);
					LOG.warning("User role changed to GBO");
					txn.commit();
					return Response.ok(" {} ").build();
					
				}
			}
			
			
		}catch(Exception e) {
			txn.rollback();
			LOG.warning("exception "+ e.toString());
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
		}finally {
			if(txn.isActive()) {
				txn.rollback();
				LOG.warning("entered finally");
				return Response.status(Status.INTERNAL_SERVER_ERROR).build();
			}
		}
		LOG.warning("Failed role update attempt for username: " + data.getUserToChange());
		return Response.status(Status.BAD_REQUEST).entity("ups").build();
		
	}
	
	
	@POST
	@Path("/state/change")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response doStateChange(RequestData data) {
		LOG.warning("Attempt to change role on user: "+ data.getUserToChange());
		
		Transaction txn = datastore.newTransaction();
		Key userKey = datastore.newKeyFactory()
				.setKind("User")
				.newKey(data.token.getUsername());
		Entity userEntity = txn.get(userKey);
		
		Key tokenKey = datastore.newKeyFactory()
				.addAncestor(PathElement.of("User", data.token.getUsername()))
				.setKind("Token")
				.newKey(data.token.getTokenID());
		Entity tokenEntity = txn.get(tokenKey);
		
		Key updatedUserKey = datastore.newKeyFactory()
				.setKind("User")
				.newKey(data.getUserToChange());
		Entity updatedUserEntity = txn.get(updatedUserKey);
		
		try {
			
			if(tokenEntity == null || System.currentTimeMillis() > data.token.getExpirationData()) {
				txn.rollback();
				LOG.warning("Token Authentication Failed");
				return Response.status(Status.FORBIDDEN).build();
			}
			
			if(userEntity.getString("user_role").equals("SU")) {
				if(data.getAttribute().equals("ENABLED") && !updatedUserEntity.getString("user_state").equals("ENABLED")) {
					
					Entity updatedUser = Entity.newBuilder(updatedUserEntity)
							.set("user_state","ENABLED").build();
					
					txn.put(updatedUser);
					LOG.warning("User state changed to ENABLED");
					txn.commit();
					return Response.ok(" {} ").build();
				}
				if(data.getAttribute().equals("DISABLED") && !updatedUserEntity.getString("user_state").equals("DISABLED")) {
					Entity updatedUser = Entity.newBuilder(updatedUserEntity)
							.set("user_state","DISABLED").build();
					
					txn.put(updatedUser);
					LOG.warning("User state changed to DISABLED");
					txn.commit();
					return Response.ok(" {} ").build();
				}
			}
			if(userEntity.getString("user_role").equals("GA")) {
				if(updatedUserEntity.getString("user_role").equals("USER") || updatedUserEntity.getString("user_role").equals("GBO")) {
					
					if(data.getAttribute().equals("ENABLED") && !updatedUserEntity.getString("user_state").equals("ENABLED")) {
						
						Entity updatedUser = Entity.newBuilder(updatedUserEntity)
								.set("user_state","ENABLED").build();
						
						txn.put(updatedUser);
						LOG.warning("User state changed to ENABLED");
						txn.commit();
						return Response.ok(" {} ").build();
					}
					if(data.getAttribute().equals("DISABLED") && !updatedUserEntity.getString("user_state").equals("DISABLED")) {
						Entity updatedUser = Entity.newBuilder(updatedUserEntity)
								.set("user_state","DISABLED").build();
						
						txn.put(updatedUser);
						LOG.warning("User state changed to DISABLED");
						txn.commit();
						return Response.ok(" {} ").build();
					}
				}
			}
			if(userEntity.getString("user_role").equals("GBO")) {
				if(updatedUserEntity.getString("user_role").equals("USER")) {
					
					if(data.getAttribute().equals("ENABLED") && !updatedUserEntity.getString("user_state").equals("ENABLED")) {
						
						Entity updatedUser = Entity.newBuilder(updatedUserEntity)
								.set("user_state","ENABLED").build();
						
						txn.put(updatedUser);
						LOG.warning("User state changed to ENABLED");
						txn.commit();
						return Response.ok(" {} ").build();
					}
					if(data.getAttribute().equals("DISABLED") && !updatedUserEntity.getString("user_state").equals("DISABLED")) {
						Entity updatedUser = Entity.newBuilder(updatedUserEntity)
								.set("user_state","DISABLED").build();
						
						txn.put(updatedUser);
						LOG.warning("User state changed to DISABLED");
						txn.commit();
						return Response.ok(" {} ").build();
					}
				}
			}
			
			
		}catch(Exception e) {
			txn.rollback();
			LOG.warning("exception "+ e.toString());
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
		}finally {
			if(txn.isActive()) {
				txn.rollback();
				LOG.warning("entered finally");
				return Response.status(Status.INTERNAL_SERVER_ERROR).build();
			}
		}
		LOG.warning("Failed role update attempt for username: " + data.getUserToChange());
		return Response.status(Status.BAD_REQUEST).entity("ups").build();
	}
	
}
