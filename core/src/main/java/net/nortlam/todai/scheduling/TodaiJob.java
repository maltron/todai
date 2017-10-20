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
package net.nortlam.todai.scheduling;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.UriBuilder;
import net.nortlam.todai.database.Connection;
import net.nortlam.todai.database.ScheduleOperation;
import net.nortlam.todai.entity.Schedule;
import net.nortlam.todai.exception.NotFoundException;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * This represents a Quartz's Job, in which it's going to (basically) Submit 
 * a URL and get the result in return
 * 
 * @author Mauricio "Maltron" Leal <maltron at gmail dot com>
 */
public class TodaiJob implements Job {

    private static final Logger LOG = Logger.getLogger(TodaiJob.class.getName());
    
    private Connection connection;

    public TodaiJob() {
    }
    
    public TodaiJob(Connection connection) {
        this.connection = connection;
        LOG.log(Level.INFO, ">>> TodaiJob with MongoConnection in it");
    }
    
    // JOB JOB JOB JOB JOB JOB JOB JOB JOB JOB JOB JOB JOB JOB JOB JOB JOB 
    //   JOB JOB JOB JOB JOB JOB JOB JOB JOB JOB JOB JOB JOB JOB JOB JOB JOB 
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // Fetch data created 
        JobDataMap data = context.getJobDetail().getJobDataMap();
        String ID = data.getString(Schedule.TAG_ID);
        
        // Look for this particular Schedule in the database
        ScheduleOperation ops = new ScheduleOperation(connection);
        try {
            Schedule schedule = ops.lockit(ID);
            
            // Submit a URL
            Client client = ClientBuilder.newClient();
            UriBuilder builder = UriBuilder.fromUri(schedule.getUrl());
            Response response = client.target(builder).request().get();
            if(response.)
            
        } catch(NotFoundException ex) {
            // There wasn't any Schedule available to use
            LOG.log(Level.SEVERE, "### MONGO EXCEPTION:{0}", ex.getMessage());
        }
        
        String url = data.getString(Schedule.TAG_URL);
        boolean deleteOnSucessfull = data.getBooleanValue(Schedule.TAG_DELETE_ON_SUCCESSFUL);
        
        LOG.log(Level.INFO, ">>> TodaiJob.execute()");
        UriBuilder builder = UriBuilder.fromUri(url);
        
        // Submit URL
        Client client = ClientBuilder.newClient();
        Response response = client.target(builder).request().get();
        if(response.getStatusInfo().getFamily() == Family.SUCCESSFUL) {
            
        }
        
        
        
        
//        Client client = ClientBuilder.newClient();
//        WebTarget target = client.target("sdsd").request().post(entity, responseType)
        
    }
    

}
