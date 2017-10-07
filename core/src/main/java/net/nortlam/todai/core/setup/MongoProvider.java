package net.nortlam.todai.core.setup;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
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
    
    private MongoClient client;
    
    @PostConstruct
    private void init() {
        LOG.log(Level.INFO, ">>> init() Connecting to Database");
        MongoClientURI clientURI = new MongoClientURI(connectionString());
        client = new MongoClient(clientURI);
    }
    
    private String connectionString() {
        String username = System.getenv("DB_USERNAME");
        String password = System.getenv("DB_PASSWORD");
        String service = System.getenv("DB_SERVICE");
        String database = System.getenv("DB_DATABASE");
        
        if(username == null || password == null || service == null
                || database == null) {
            LOG.log(Level.SEVERE, "### One of the Connection String is NULL"+
                    " Username: {0} Password: {1} Service: {2} Database {3}",
                    new Object[] {username, password, service, database});
        }
        
        return String.format("mongodb://%s:%s@%s:27017/%s", 
                                         username, password, service, database);
    }
    
    @Lock(LockType.READ)
    public MongoDatabase getDatabase() {
        return client.getDatabase(System.getenv("DB_DATABASE"));
    }
}
