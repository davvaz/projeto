package pt.unl.fct.di.apdc.projetoindividual.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.google.appengine.repackaged.org.apache.commons.codec.digest.DigestUtils;
import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Transaction;


@WebListener
public class StartupListener implements ServletContextListener {

	public StartupListener() {
		
	}
	
	
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
		Transaction txn = datastore.newTransaction();
		
		Key userKey = datastore.newKeyFactory()
				.setKind("User")
				.newKey("SUPERUSER");		
		Entity user = txn.get(userKey);
		
		user = Entity.newBuilder(userKey)
		.set("user_email", "superuser@mail.com")
		.set("user_pwd", DigestUtils.sha512Hex("UltimatePwd123"))
		.set("user_profile", "PRIVATE")
		.set("user_phone_number", "219090909")
		.set("user_mobile_number", "909090909")
		.set("user_address", "super user address")
		.set("user_extra_address", "super user extra address")
		.set("location", "super user location")
		.set("user_role", "SU")
		.set("user_state", "ENABLED")
		.set("user_creation_time", Timestamp.now())
		.build();
		
		txn.add(user);
		txn.commit();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub

	}

}
