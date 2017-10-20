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

import com.mongodb.MongoException;
import com.mongodb.MongoWriteConcernException;
import com.mongodb.MongoWriteException;
import org.bson.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;

import net.nortlam.todai.database.MongoConnection;
import net.nortlam.todai.entity.Schedule;
import net.nortlam.todai.exception.AlreadyExistsException;
import net.nortlam.todai.exception.InvalidException;
import net.nortlam.todai.exception.NoContentException;
import net.nortlam.todai.scheduling.TodaiJob;
import net.nortlam.todai.validation.ValidateSchedule;

import org.quartz.CronTrigger;
import static org.quartz.JobBuilder.newJob;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * A ser of services necessary to validate information on API's call
 * 
 * @author Mauricio "Maltron" Leal <maltron at gmail dot com>
 */
@Stateless
public class ServiceSchedule implements Serializable {

    private static final Logger LOG = Logger.getLogger(ServiceSchedule.class.getName());
    
    public static final String COLLECTION_NAME = "schedule";
    public static final String DEFAULT_QUARTZ_GROUP = "todai";
    
//    @EJB
//    private MongoProvider provider;

    public ServiceSchedule() {
    }
    
    /**
     * Return a list of all Schedules available in the database */
    public Collection<Schedule> fetchAll(MongoConnection connection) throws NoContentException {
        Collection<Schedule> result = new ArrayList<>();
        for(Document document: connection.getDatabase()
                                        .getCollection(COLLECTION_NAME).find()) {
            result.add(new Schedule(document));
        }
        
        if(result.isEmpty()) throw new NoContentException();
        
        return result;
    }
    
    /**
     * Create a new Schedule information (assuming it was previously validated)
     */
    public String add(MongoConnection connection, 
                    SchedulerFactory factory, Schedule schedule) throws MongoWriteException, 
                                    MongoWriteConcernException, MongoException,
                  InvalidException, AlreadyExistsException, SchedulerException {
        // Step #1: Information validation 
        // Is there any schedule at all ?
        if(schedule == null) {
            LOG.log(Level.WARNING, "### ServiceSchedule.add() Schedule is NULL");
            throw new InvalidException("schedule instance is null");
        }
        
        // Is this a valid Name ?
        ValidateSchedule validate = new ValidateSchedule();
        
        // Step #2: Add a information about this particular Job into the
        //         the database
        Document document = schedule.toDocument();
        connection.getDatabase().getCollection(COLLECTION_NAME)
                .insertOne(document);

        // Step #3: Quartz's Scheduler creation
        Scheduler scheduler = factory.getScheduler();
        JobDetail jobDetail = newJob(TodaiJob.class)
                .usingJobData(Schedule.TAG_ID, document.getObjectId(Schedule.TAG_ID).toString())
                .usingJobData(Schedule.TAG_CRON_EXPRESSION, schedule.getCronExpression())
                .usingJobData(Schedule.TAG_DELETE_ON_SUCCESSFUL, schedule.isDeleteOnsuccessful())
                .usingJobData(Schedule.TAG_URL, schedule.getUrl())
                .usingJobData(Schedule.TAG_STATUS, schedule.getStatus().toString())
//                .usingJobData(MongoProvider.NAME, provider.getDatabase())
                .build();
        
        CronTrigger trigger = newTrigger()
                .withSchedule(schedule.getCronExpressionBuilder())
                // FUTURE: Add TimeZone Capabilities
//                .inTimeZone(TimeZone.getTimeZone("America/Los_Angeles"))
                .build();
        
        scheduler.scheduleJob(jobDetail, trigger);
        
        return document.getObjectId(Schedule.TAG_ID).toString();
    }
    
//    /**
//     * Return a instance of the collection */
//    private MongoCollection<Document> getCollection() {
//        return provider.getDatabase().getCollection(COLLECTION_NAME);
//    }
    
//    /**
//     * Return a instance of Quartz's Scheduler Factory, previously stored
//     * by {@link QuartzInitializerListener} */
//    private SchedulerFactory getSchedulerFactory(ServletContext context) {
//        return (SchedulerFactory)context.getAttribute(
//                QuartzInitializerServlet.QUARTZ_FACTORY_KEY);
//    }
}
