package pt.unl.fct.di.apdc.projetoindividual.resources;

import java.util.logging.Logger;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.PathElement;
import com.google.cloud.datastore.Transaction;
import com.google.gson.Gson;

import pt.unl.fct.di.apdc.projetoindividual.util.AuthToken;

@Path("/delete")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class DeleteResource {

	private static final Logger LOG = Logger.getLogger(DeleteResource.class.getName());
	
	private final Gson g = new Gson();
	
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	
	public DeleteResource() {
		
	}
	
	@POST
	@Path("/user")
	public Response doDelete2(AuthToken token, @PathParam("username") String userToDelete) {
		LOG.warning("Attempt to delete user " + userToDelete );
		
		Transaction txn = datastore.newTransaction();
		
		try {
			Key userKey = datastore.newKeyFactory()
					.setKind("User")
					.newKey(token.getUsername());
			Entity userEntity = txn.get(userKey);
			
			Key tokenKey = datastore.newKeyFactory()
					.addAncestor(PathElement.of("User", token.getUsername()))
					.setKind("Token")
					.newKey(token.getTokenID());
			Entity tokenEntity = txn.get(tokenKey);
			
			Key userDelKey = datastore.newKeyFactory()
					.setKind("User")
					.newKey(userToDelete);
			Entity userDelEntity = txn.get(userDelKey);
			
			if(tokenEntity == null || token.expirationData > System.currentTimeMillis()) {
				txn.rollback();
				LOG.warning("Token Authentication Failed");
				return Response.status(Status.FORBIDDEN).build();
			}
			
			if(userDelEntity == null) {
				txn.rollback();
				LOG.warning("No user to delete");
				return Response.status(Status.BAD_REQUEST).entity("No such user.").build();
			}
			
			if(userEntity.getString("role").equals("USER") && token.getUsername().equals(userToDelete)) {
				
				Entity deletedUser = Entity.newBuilder(userDelEntity).set("user_state", "DELETED").build();
				
				txn.put(deletedUser);
				LOG.warning("Self User deleted: " + userToDelete);
				txn.commit();
				return Response.ok(" {} ").build(); 
			}
			if((userEntity.getString("role").equals("GBO") || userEntity.getString("role").equals("GA") ) 
					&& userDelEntity.getString("role").equals("USER")) {
				
				Entity deletedUser = Entity.newBuilder(userDelEntity).set("user_state", "DELETED").build();
				
				txn.put(deletedUser);
				LOG.warning("Another user deleted: " + userToDelete);
				txn.commit();
				return Response.ok(" {} ").build(); 
			}
			
			//return Response.status(Status.BAD_REQUEST).entity("Failed user login.").build();
			
		}catch(Exception e) {
			txn.rollback();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}finally {
			if(txn.isActive()) {
				txn.rollback();
				return Response.status(Status.INTERNAL_SERVER_ERROR).build();
			}
			
		}
		
		LOG.warning("Failed delete attempt for username: " + userToDelete);
		//return Response.status(Status.FORBIDDEN).build();
		return Response.status(Status.BAD_REQUEST).entity("ups").build();
	}
	
	
	@POST
	@Path("/user/{username}")
	public Response doDelete(AuthToken token, @PathParam("username") String userToDelete) {
		LOG.warning("Attempt to delete user " + userToDelete );
		
		Transaction txn = datastore.newTransaction();
		
		try {
			Key userKey = datastore.newKeyFactory()
					.setKind("User")
					.newKey(token.getUsername());
			Entity userEntity = txn.get(userKey);
			
			Key tokenKey = datastore.newKeyFactory()
					.addAncestor(PathElement.of("User", token.getUsername()))
					.setKind("Token")
					.newKey(token.getTokenID());
			Entity tokenEntity = txn.get(tokenKey);
			
			Key userDelKey = datastore.newKeyFactory()
					.setKind("User")
					.newKey(userToDelete);
			Entity userDelEntity = txn.get(userDelKey);
			
			if(tokenEntity == null || token.expirationData > System.currentTimeMillis()) {
				txn.rollback();
				LOG.warning("Token Authentication Failed");
				return Response.status(Status.FORBIDDEN).build();
			}
			
			if(userDelEntity == null) {
				txn.rollback();
				LOG.warning("No user to delete");
				return Response.status(Status.BAD_REQUEST).entity("No such user.").build();
			}
			
			if(userEntity.getString("role").equals("USER") && token.getUsername().equals(userToDelete)) {
				
				Entity deletedUser = Entity.newBuilder(userDelEntity).set("user_state", "DELETED").build();
				
				txn.put(deletedUser);
				LOG.warning("Self User deleted: " + userToDelete);
				txn.commit();
				return Response.ok(" {} ").build(); 
			}
			if((userEntity.getString("role").equals("GBO") || userEntity.getString("role").equals("GA") ) 
					&& userDelEntity.getString("role").equals("USER")) {
				
				Entity deletedUser = Entity.newBuilder(userDelEntity).set("user_state", "DELETED").build();
				
				txn.put(deletedUser);
				LOG.warning("Another user deleted: " + userToDelete);
				txn.commit();
				return Response.ok(" {} ").build(); 
			}
			
			//return Response.status(Status.BAD_REQUEST).entity("Failed user login.").build();
			
		}catch(Exception e) {
			txn.rollback();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}finally {
			if(txn.isActive()) {
				txn.rollback();
				return Response.status(Status.INTERNAL_SERVER_ERROR).build();
			}
			
		}
		
		LOG.warning("Failed delete attempt for username: " + userToDelete);
		//return Response.status(Status.FORBIDDEN).build();
		return Response.status(Status.BAD_REQUEST).entity("ups").build();
	}
	
}
