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
package net.nortlam.todai.api;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import net.nortlam.todai.core.setup.MongoProvider;
import net.nortlam.todai.entity.Schedule;
import net.nortlam.todai.exception.AlreadyExistsException;
import net.nortlam.todai.exception.NotFoundException;
import org.bson.Document;

/**
 * A ser of services necessary to validate information on API's call
 * 
 * @author Mauricio "Maltron" Leal <maltron at gmail dot com>
 */
@Stateless
public class Service implements Serializable {

    private static final Logger LOG = Logger.getLogger(Service.class.getName());
    
    public static final String COLLECTION_NAME = "schedule";
    
    @EJB
    private MongoProvider provider;

    public Service() {
    }
    
    /**
     * Returns a Collection of the entire Database */
    public MongoCollection<Document> getEntries() {
        return getCollection();
    }
    
    public Document findByName(String name) throws NotFoundException {
        Document document = getCollection().find(Filters.eq(
                                                Schedule.TAG_NAME, name)).first();
        if(document == null) throw new NotFoundException("Schedule "+name+
                                                                    " was not found");
        
        return document;
    }
    
    /**
     * Check if this name already exist */
    public boolean alreadyExistsName(String name) throws AlreadyExistsException {
        Document document = getCollection().find(Filters.eq(Schedule.TAG_NAME, name)).first();
        if(document != null) throw new AlreadyExistsException(name+" already exists");
        
        return false;
    }
    
    /**
     * Return a instance of the collection */
    private MongoCollection<Document> getCollection() {
        return provider.getDatabase().getCollection(COLLECTION_NAME);
    }
}
