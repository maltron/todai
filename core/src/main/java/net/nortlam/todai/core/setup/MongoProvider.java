package net.nortlam.todai.core.setup;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.MongoSocketException;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class MongoProvider {

    private static final Logger LOG = Logger.getLogger(MongoProvider.class.getName());
    
    // OpenShift's MongoDB Command:
    // oc new-app mongodb-ephemeral -p MONGODB_DATABASE=todai -p MONGODB_USER=todai -p MONGODB_PASSWORD=todai
    private static final String DEFAULT_USERNAME = "todai";
    private static final String DEFAULT_PASSWORD = "todai";
    private static final String DEFAULT_MONGODB_HOST = "mongodb";
    private static final String DEFAULT_MONGODB_DATABASE = "todai";
    private static final int DEFAULT_MONGODB_PORT = 27017;
    
    private String database;
    private MongoClient client;
    
    @PostConstruct
    private void init() {
        String mongoHost = System.getenv("MONGODB_HOST");
        if(mongoHost == null) {
            LOG.log(Level.WARNING, "### MongoDB's Host was not set. "
                    + "Assuming default value: {0}", DEFAULT_MONGODB_HOST);
            mongoHost = DEFAULT_MONGODB_HOST;
        }
        
        int mongoPort = DEFAULT_MONGODB_PORT; 
        String mongoPortString = System.getenv("MONGODB_PORT");
        if(mongoPortString == null) {
            LOG.log(Level.WARNING, "### MongoDB's Port was not set. "
                    + "Assuming default value: {0}", DEFAULT_MONGODB_PORT);
            mongoPort = DEFAULT_MONGODB_PORT;
        } else {
            try {
                mongoPort = Integer.parseInt(mongoPortString);
            } catch(NumberFormatException ex) {
                LOG.log(Level.SEVERE, "### NUMBER FORMAT EXCEPTION: "
                        + "Unable to parse MongoDB's port: {0}", mongoPortString);
            }
        }
        
        try {
            LOG.log(Level.INFO, ">>> init() Connecting to Database Host:{0} Port:{1}",
                    new Object[] {mongoHost, mongoPort});
            ServerAddress mongoAddress = new ServerAddress(mongoHost, mongoPort);
            client = new MongoClient(mongoAddress, Arrays.asList(credential()));
        } catch(MongoSocketException ex) {
            LOG.log(Level.SEVERE, "### MONGO SOCKET EXCEPTION:{0}", ex.getMessage());
        }
    }
    
    /**
     * Return a Connection information regarding MongoDB's */
    private MongoCredential credential() {
        String username = System.getenv("TODAI_USERNAME");
        if(username == null) {
            LOG.log(Level.WARNING, "### MongoDB's Username was not set. "
                    + "Assuming default value: {0}", DEFAULT_USERNAME);
            username = DEFAULT_USERNAME;
        }
        
        String password = System.getenv("TODAI_PASSWORD");
        if(password == null) {
            LOG.log(Level.WARNING, "### MongoDB's Password was not set. "
                    + "Assuming default value: {0}", DEFAULT_PASSWORD);
            password = DEFAULT_PASSWORD;
        }
        
        database = System.getenv("TODAI_DATABASE");
        if(database == null) {
            LOG.log(Level.WARNING, "### MongoDB's Database was not set. "
                    + "Assuming default value: {0}", DEFAULT_MONGODB_DATABASE);
            database = DEFAULT_MONGODB_DATABASE;
        }
        
        LOG.log(Level.INFO, ">>> credential() Username:{0} Password:{1} Database:{2}",
                new Object[] {username, password, database});
        return MongoCredential.createCredential(username, database, 
                                                        password.toCharArray());
    }
    
    @Lock(LockType.READ)
    public MongoDatabase getDatabase() {
        return client.getDatabase(database);
    }
}
