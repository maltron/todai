/*
 * 
 * Copyright 2014 Mauricio "Maltron" Leal <maltron@gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package net.nortlam.todai.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.MongoSocketException;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import java.io.Serializable;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * In charge of connecting to MongoDatabase 
 * 
 * @author Mauricio "Maltron" Leal <maltron at gmail dot com>
 */
public class MongoConnection implements Serializable {

    public static final String NAME = "MongoConnection";
    private static final Logger LOG = Logger.getLogger(MongoConnection.class.getName());

    // OpenShift's MongoDB Command:
    // oc new-app mongodb-ephemeral -p MONGODB_DATABASE=todai -p MONGODB_USER=todai -p MONGODB_PASSWORD=todai
    private static final String DEFAULT_USERNAME = "todai";
    private static final String DEFAULT_PASSWORD = "todai";
    private static final String DEFAULT_MONGODB_HOST = "mongodb";
    private static final String DEFAULT_MONGODB_DATABASE = "todai";
    private static final int DEFAULT_MONGODB_PORT = 27017;

    private String database;
    private MongoClient client;

    public MongoConnection() {
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
        
//        // Once connected, create some importante indexes for some Collections
//        LOG.log(Level.FINE, ">>> init() Creating Indexes on Collection Schedule: Name");
//        client.getDatabase(database)
//                .getCollection(ServiceSchedule.COLLECTION_NAME)
//                .createIndex(Indexes.ascending(Schedule.TAG_NAME), 
//                                                new IndexOptions().unique(true));
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
    
    public MongoDatabase getDatabase() {
        return client.getDatabase(database);
    }
}
