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
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import net.nortlam.todai.core.setup.MongoProvider;
import net.nortlam.todai.entity.Schedule;
import net.nortlam.todai.exception.AlreadyExistsException;
import net.nortlam.todai.exception.NoContentException;
import net.nortlam.todai.exception.NotFoundException;
import org.bson.Document;

/**
 * A ser of services necessary to validate information on API's call
 * 
 * @author Mauricio "Maltron" Leal <maltron at gmail dot com>
 */
@Stateless
public class ServiceSchedule implements Serializable {

    private static final Logger LOG = Logger.getLogger(ServiceSchedule.class.getName());
    
    public static final String COLLECTION_NAME = "schedule";
    
    @EJB
    private MongoProvider provider;

    public ServiceSchedule() {
    }
    
    /**
     * Return a list of all Schedules available in the database */
    public Collection<Schedule> fetchAll() throws NoContentException {
        Collection<Schedule> result = new ArrayList<>();
        for(Document document: getCollection().find()) {
            result.add(new Schedule(document));
        }
        
        if(result.isEmpty()) throw new NoContentException();
        
        return result;
    }
    
    public Schedule findByName(String name) throws NotFoundException {
        Document document = getCollection().find(Filters.eq(
                                                Schedule.TAG_NAME, name)).first();
        if(document == null) throw new NotFoundException("Schedule "+name+" was not found");
        return new Schedule(document);
    }

    /**
     * Check if this name already exists in the Database */
    public boolean alreadyExist(String name) {
        Document document = getCollection().find(
                                    Filters.eq(Schedule.TAG_NAME, name)).first();
        return document != null;
    }
    
    /**
     * Return a instance of the collection */
    private MongoCollection<Document> getCollection() {
        return provider.getDatabase().getCollection(COLLECTION_NAME);
    }
}
