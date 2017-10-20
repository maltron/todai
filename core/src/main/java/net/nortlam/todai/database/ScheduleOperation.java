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

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import java.io.Serializable;
import java.util.logging.Logger;
import net.nortlam.todai.entity.Schedule;
import net.nortlam.todai.exception.NotFoundException;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

/**
 * Several operations on Schedule Collection to easy to handle all
 * the necessary data into a single place, so other classes doesn't need
 * have several imports
 * 
 * @author Mauricio "Maltron" Leal <maltron at gmail dot com>
 */
public class ScheduleOperation implements Serializable {

    public static final String COLLECTION_NAME = "schedule";
    private static final Logger LOG = Logger.getLogger(ScheduleOperation.class.getName());
    private Connection connection;

    public ScheduleOperation(Connection connection) {
        this.connection = connection;
    }
    
    /**
     * Look for a particular Schedule's ID and if it's not false */
    public Document availableSchedule(String ID) throws NotFoundException {
        Document search = new Document()
                .append(Schedule.TAG_ID, new ObjectId(ID))
                .append(Schedule.TAG_IS_LOCK, false);
        
        Document found = getCollection().find(search).first();
        if(found == null) throw new NotFoundException();
        
        return found;
    }
    
    /**
     * Lock a particular Schedule, so other instances won't 
     * @return Instance with a lock that it can use  */
    public Schedule lockit(String ID) throws NotFoundException {
        // Look for a particular Schedule which it's *NOT* lock
        Document document = availableSchedule(ID);
        
        // Now, lock it
        document.replace(Schedule.TAG_IS_LOCK, true);
        UpdateResult updateResult = getCollection().updateOne(document,
                                                new Document("$set", document));
        
        return new Schedule(document);
    }
    
    /**
     * Updates a status to Successful */
    public Schedule successful(Schedule schedule) {
        
    }
    
    /**
     * Look for a particular Schedule based on his ID */
    public Schedule findByID(Schedule schedule) throws NotFoundException {
        return findByID(schedule.getID());
    }
    
    /**
     * Look for a particular Schedule based on his ID */
    public Schedule findByID(String ID) throws NotFoundException {
        Bson filter = new Document().append(Schedule.TAG_ID, new ObjectId(ID));
        Document found = getCollection().find(filter).first();
        if(found == null) throw new NotFoundException("Schedule ID:"+ID+" was not found");
        
        return new Schedule(found);
    }
    
    /**
     * Returns a instance of the collection needed for any operation */
    private MongoCollection<Document> getCollection() {
        return connection.getMongo().getDatabase().getCollection(COLLECTION_NAME);
    }

}
