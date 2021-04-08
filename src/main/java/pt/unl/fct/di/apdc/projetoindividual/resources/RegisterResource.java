package pt.unl.fct.di.apdc.projetoindividual.resources;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
import com.google.cloud.datastore.Transaction;
import com.google.gson.Gson;

import pt.unl.fct.di.apdc.projectoindividual.data.Database;
import pt.unl.fct.di.apdc.projetoindividual.util.RegisterData;



@Path("/register")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class RegisterResource {

	private static final Logger LOG = Logger.getLogger(RegisterResource.class.getName());
	
	
	private static final DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
	public String timestamp;
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	
	private final Gson g = new Gson();
	
	public RegisterResource() {
		
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
//	
	
	@POST
	@Path("/user")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response doRegister(RegisterData data) {
		LOG.fine("Attempt to register user " + data.username);
		
		
		// Checks input data
		
		if(! data.validRegistration()) {
			return Response.status(Status.BAD_REQUEST).entity("Missing or wrong parameter.").build();
		}
		
		//Creates an entity user from the data. The key is the username
		
		Transaction txn = datastore.newTransaction();

		try {
			Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.username);		
			Entity user = txn.get(userKey);

			if(user != null) {
				txn.rollback();
				return Response.status(Status.BAD_REQUEST).entity("User already exists.").build();	
			}else {
				
			//Entity user = datastore.get(userKey);	
				user = Entity.newBuilder(userKey)
						.set("user_email", data.email)
						.set("user_pwd", DigestUtils.sha512Hex(data.password))
						.set("user_profile", data.profile)
						.set("user_phone_number", data.phoneNumber)
						.set("user_mobile_number", data.mobileNumber)
						.set("user_address", data.address)
						.set("user_extra_address", data.extraAddress)
						.set("location", data.location)
						.set("user_role", "USER")
						.set("user_state", "ENABLED")
						.set("user_creation_time", Timestamp.now())
						.build();
				txn.add(user);
				LOG.info("User registered " + data.username);
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
}
