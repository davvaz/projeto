package pt.unl.fct.di.apdc.projetoindividual.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.PathElement;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery;
import com.google.cloud.datastore.Transaction;
import com.google.gson.Gson;

import pt.unl.fct.di.apdc.projetoindividual.util.AuthToken;
import pt.unl.fct.di.apdc.projetoindividual.util.LoginData;


@Path("/authentication")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class AuthenticationResource {

	
	private static final Logger LOG = Logger.getLogger(AuthenticationResource.class.getName());
	
	private final Gson g = new Gson();
	
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	
	public AuthenticationResource() {
		
	}
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response doLogin(LoginData data) {
		LOG.warning("WARNING: Login atempt by user: " + data.getUsername());
		
		Key userKey = datastore.newKeyFactory()
				.setKind("User")
				.newKey(data.getUsername());
		
		
		Transaction txn = datastore.newTransaction();
		
		try {
			Entity user = txn.get(userKey);
			
			if(user == null) {
				LOG.warning("Failed login attempt");
				return Response.status(Status.FORBIDDEN).entity("Failed login attempt").build();
			}
			
			if(user.getString("user_state").equals("DELETED") || user.getString("user_state").equals("DISABLED")) {
				LOG.warning("Failed login attempt");
				return Response.status(Status.FORBIDDEN).entity("Failed login attempt").build();
			}
			
			String hashedPWD = user.getString("user_pwd");
			if(hashedPWD.equals(DigestUtils.sha512Hex(data.getPassword()))) {
				AuthToken token = new AuthToken(data.getUsername(),user.getString("user_role"));
				Key tokenKey = datastore.newKeyFactory()
						.addAncestor(PathElement.of("User", data.getUsername()))
						.setKind("Token")
						.newKey(token.getTokenID());
				Entity tokenEntity = Entity.newBuilder(tokenKey)
						.set("token_ID", token.getTokenID())
						.set("token_username",token.getUsername())
						.set("token_creationData",token.getCreationData())
						.set("token_expirationData", token.getExpirationData())
						.set("token_role", token.getRole())
						.build();
				txn.put(tokenEntity);
				LOG.warning("WARNING: User '" + data.getUsername() + "'logged in successfully.");
				txn.commit();
				return Response.ok(g.toJson(token)).build();
				
			}
			else {
				txn.rollback();
				LOG.warning("Wrong password for username: " + data.getUsername());
				return Response.status(Status.FORBIDDEN).entity("Wrong password or username").build();
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
	}
	
	@POST
	@Path("/logout")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response doLogout(AuthToken token) {
		
		Transaction txn = datastore.newTransaction();
		
		Key tokenKey = datastore.newKeyFactory()
				.addAncestor(PathElement.of("User", token.getUsername()))
				.setKind("Token")
				.newKey(token.getTokenID());
		Entity tokenEntity = txn.get(tokenKey);
		
		try {
			
			if(tokenEntity == null || System.currentTimeMillis() > token.getExpirationData()) {
				txn.rollback();
				LOG.warning("Token Authentication Failed");
				return Response.status(Status.FORBIDDEN).build();
			}
			
			Query<Entity> query = Query.newEntityQueryBuilder()
					.setKind("Token")
					.setFilter(
							StructuredQuery.PropertyFilter.eq("token_username", token.getUsername()))
					.build();
			
			QueryResults<Entity> tokens = datastore.run(query);
			
			List<Key> tokenKeysList = new ArrayList<>();
			
			tokens.forEachRemaining(userTokens -> {
				tokenKeysList.add(userTokens.getKey());
			});
			
			for(Key tokenIDkey : tokenKeysList) {
				
				LOG.warning("deleted token: " + tokenIDkey.toString());
				txn.delete(tokenIDkey);
					
			}
			txn.commit();
			Response.ok(" {} ").build();
			return Response.temporaryRedirect(new URI("https://webapp-310017.ey.r.appspot.com/logout/logout.html")).build();
//			return Response.status(200).location(new URI("https://webapp-310017.ey.r.appspot.com/logout/logout.html")).build();
			
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
	}

	
}
