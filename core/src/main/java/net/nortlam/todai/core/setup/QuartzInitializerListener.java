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
package net.nortlam.todai.core.setup;

import net.nortlam.todai.database.MongoConnection;
import java.lang.reflect.Constructor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import net.nortlam.todai.database.Connection;
import net.nortlam.todai.scheduling.TodaiJob;
import org.quartz.Job;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.ee.servlet.QuartzInitializerServlet;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;

/**
 * Servlet Listener responsible to initiliaze Quartz Factory, which it's going
 * to be used later on in each JAX-RS Resource
 * 
 * @author Mauricio "Maltron" Leal <maltron at gmail dot com>
 */
public class QuartzInitializerListener 
        implements ServletContextListener, JobFactory, Connection {

    private static final Logger LOG = Logger.getLogger(QuartzInitializerListener.class.getName());

    // Quartz'z Scheduler used to schedule services
    private Scheduler scheduler = null;
    private MongoConnection mongoConnection;
    
    public QuartzInitializerListener() {
    }

    @Override
    public void contextInitialized(ServletContextEvent context) {
        LOG.log(Level.INFO, ">>> QuartzInitializerListener.contextInitialized() +++++++++++++++++++++");
        ServletContext servletContext = context.getServletContext();
        try {
            // Create an instance of MongoConnection
            LOG.log(Level.INFO, ">>> contetInitialized() Creating an instance of MongoConnection");
            this.mongoConnection = new MongoConnection();
            servletContext.setAttribute(MongoConnection.NAME, mongoConnection);

            // Create the factory and add into ServletContext
            SchedulerFactory factory = new StdSchedulerFactory();
            // Initialize the Scheduler
            scheduler = factory.getScheduler();
            // It will instance a Job with a MongoConnection in it
            scheduler.setJobFactory(this); 
            
            LOG.log(Level.FINE, ">>> contextInitialized() Starting Scheduler");
            scheduler.start();
            
            LOG.log(Level.INFO, ">>> contextInitialized() Adding SchedulerFactory "
                    + "into ServletContext as a Key:{0}", QuartzInitializerServlet.QUARTZ_FACTORY_KEY);
            servletContext.setAttribute(QuartzInitializerServlet.QUARTZ_FACTORY_KEY, factory);
            
        } catch(SchedulerException ex) {
            LOG.log(Level.SEVERE, "### contextInitialized() SCHEDULER EXCEPTION:{0}",
                    ex.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent context) {
        LOG.log(Level.INFO, ">>> QuartzInitializerListener.contextDestroyed()   ---------------------");
    }

    @Override
    public MongoConnection getMongo() {
        return mongoConnection;
    }

    // JOB FACTORY JOB FACTORY JOB FACTORY JOB FACTORY JOB FACTORY JOB FACTORY 
    //   JOB FACTORY JOB FACTORY JOB FACTORY JOB FACTORY JOB FACTORY JOB FACTORY 
    @Override
    public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException {
        Job result = null;
        try {
            Class<? extends Job> jobClass = bundle.getJobDetail().getJobClass();
            if(TodaiJob.class.equals(jobClass)) {
                Constructor<? extends Job> constructor = (Constructor<? extends Job>)
                        jobClass.getConstructors()[1];
                constructor.setAccessible(true);
                result = constructor.newInstance(this);
            }
            
        } catch(Exception ex) {
            LOG.log(Level.SEVERE, "### QuartzInitializerServlet.newJob() "
                    + "EXCEPTION:{0}", ex.getMessage());
        }
        
        return result;
    }
    
    
    
}
