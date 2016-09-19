package uk.ac.gla.cvr.hackathon2016;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;


import com.mysql.jdbc.AbandonedConnectionCleanupThread;

@WebListener
@ApplicationPath("/")
public class Hackathon2016Application extends ResourceConfig implements ServletContextListener {

	public static Logger logger = Logger.getLogger("uk.ac.gla.cvr.hackathon2016");
	
	public Hackathon2016Application() {
		super();
    	registerInstances(new Hackathon2016RequestHandler());
    	registerInstances(new Hackathon2016ExceptionHandler());
	}


	
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		String jdbcUrl;
		String username;
		String password;
		try {
			Context ctx = new InitialContext();
			jdbcUrl = (String) ctx.lookup("java:comp/env/hackathon2016.jdbcUrl");
	        username = (String) ctx.lookup("java:comp/env/hackathon2016.username");
	        password = (String) ctx.lookup("java:comp/env/hackathon2016.password");
		} catch (NamingException e) {
			throw new RuntimeException("JNDI error. Please ensure the correct webapp config file exists in $CATALINA_BASE/conf/[enginename]/[hostname]/ or elsewhere", e);
		}
		Hackathon2016Database.setJdbcUrl(jdbcUrl);
		Hackathon2016Database.setUsername(username);
		Hackathon2016Database.setPassword(password);
		
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		cleanupMySQL();
	}
    
	private void cleanupMySQL() {
		Enumeration<Driver> drivers = DriverManager.getDrivers();
        Driver d = null;
        while(drivers.hasMoreElements()) {
            try {
                d = drivers.nextElement();
                DriverManager.deregisterDriver(d);
                logger.warning(String.format("Driver %s deregistered", d));
            } catch (SQLException ex) {
                logger.warning(String.format("Error deregistering driver %s: %s", d, ex.getMessage()));
            }
        }
        try {
            AbandonedConnectionCleanupThread.shutdown();
        } catch (InterruptedException e) {
            logger.warning("SEVERE problem cleaning up: " + e.getMessage());
            e.printStackTrace();
        }
     }
    
    
}