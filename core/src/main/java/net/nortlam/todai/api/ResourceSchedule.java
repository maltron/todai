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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.nortlam.todai.entity.Schedule;
import net.nortlam.todai.exception.NoContentException;
import net.nortlam.todai.exception.NotFoundException;

/**
 * Add some Scheduling Time
 * 
 * @author Mauricio "Maltron" Leal <maltron at gmail dot com>
 */
@Path("/v1/schedule")
public class ResourceSchedule {

    private static final Logger LOG = Logger.getLogger(ResourceSchedule.class.getName());
    
    private static final SimpleDateFormat DATE_FORMAT =
                        new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    
    @EJB
    private ServiceSchedule service;
    
    @Context
    private ServletContext context;

    public ResourceSchedule() {
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() throws NoContentException {
        GenericEntity<Collection<Schedule>> all = 
                    new GenericEntity<Collection<Schedule>>(service.fetchAll()){};
        return Response.ok(all, MediaType.APPLICATION_JSON).build();
    }
    
    @GET
    @Path("/name/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByName(@PathParam("name") String name) throws NotFoundException {
        Schedule schedule = service.findByName(name);
        return Response.ok(schedule, MediaType.APPLICATION_JSON).build();
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
    
    private static Date nextMinute(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setLenient(true);
        
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE)+1);
        
        return calendar.getTime();
    }
    
    private static String stringDate(Date date) {
        return DATE_FORMAT.format(date);
    }
}
