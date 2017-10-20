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
import java.util.Collection;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.nortlam.todai.database.MongoConnection;
import net.nortlam.todai.entity.Schedule;
import net.nortlam.todai.exception.AlreadyExistsException;
import net.nortlam.todai.exception.InvalidException;
import net.nortlam.todai.exception.NoContentException;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.ee.servlet.QuartzInitializerServlet;

/**
 * Add some Scheduling Time
 * 
 * @author Mauricio "Maltron" Leal <maltron at gmail dot com>
 */
@Path("/v1/schedule")
public class ResourceSchedule {

    private static final Logger LOG = Logger.getLogger(ResourceSchedule.class.getName());
    
    @EJB
    private ServiceSchedule service;
    
    @Context
    private ServletContext context;

    public ResourceSchedule() {
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() throws NoContentException {
        MongoConnection connection = (MongoConnection)
                                    context.getAttribute(MongoConnection.NAME);
        GenericEntity<Collection<Schedule>> all = 
                    new GenericEntity<Collection<Schedule>>(service.fetchAll(connection)){};
        return Response.ok(all, MediaType.APPLICATION_JSON).build();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(Schedule schedule) throws MongoWriteException, 
                                MongoWriteConcernException, MongoException,
                  InvalidException, AlreadyExistsException, SchedulerException {
        MongoConnection connection = (MongoConnection)
                                    context.getAttribute(MongoConnection.NAME);
        SchedulerFactory factory = (SchedulerFactory)
                context.getAttribute(QuartzInitializerServlet.QUARTZ_FACTORY_KEY);
        
        String newlySchedule = service.add(connection, factory, schedule);
        return Response.ok(newlySchedule, MediaType.APPLICATION_JSON).build();
    }
    
//    /**
//     * Doing a small test into creating something into Scheduler */
//    public Response add() throws SchedulerException {
//        LOG.log(Level.INFO, ">>> Operation.add()");
//        
//        SchedulerFactory factory = (SchedulerFactory)context.getAttribute(
//                                    QuartzInitializerServlet.QUARTZ_FACTORY_KEY);
//        Scheduler scheduler = factory.getScheduler();
//        
//        Date date = new Date();
//        Date nextMinute = nextMinute(date);
//        
//        JobDetail jobDetail = JobBuilder.newJob(TestJob.class).withIdentity("name1", "group1").build();
//        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1")
//                .startAt(nextMinute).build();
//        
//        LOG.log(Level.INFO, ">>> Operation.add() Start at {0} and triggering at {1}",
//                new Object[] { stringDate(date), stringDate(nextMinute) });
//        scheduler.scheduleJob(jobDetail, trigger);
//        
//        LOG.log(Level.INFO, ">>> Operatoin.add() Returning 200 OK");
//        return Response.ok().build();
//    }
}