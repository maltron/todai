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
package net.nortlam.todai.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.quartz.CronScheduleBuilder;

import static org.quartz.CronScheduleBuilder.cronSchedule;

/**
 * This is the most basic information that it will be stored into the Database
 * 
 * @author Mauricio "Maltron" Leal <maltron at gmail dot com>
 */
@XmlRootElement(name="schedule")
@XmlAccessorType(XmlAccessType.FIELD)
public class Schedule implements Serializable {

    private static final Logger LOG = Logger.getLogger(Schedule.class.getName());
    
    public static final String TAG_ID = "_id";
    @XmlElement(name = TAG_ID, nillable = true, required = false, type = String.class)
    private String ID;
    
    public static final String TAG_CRON_EXPRESSION = "cronExpression";
    @XmlElement(name = TAG_CRON_EXPRESSION, nillable = false, required = true, type = String.class)
    private String cronExpression;
    
    public static final String TAG_DELETE_ON_SUCCESSFUL = "deleteOnsucessful";
    public static final boolean DEFAULT_DELETE_ON_SUCCESSFUL = true;
    @XmlElement(name = TAG_DELETE_ON_SUCCESSFUL, nillable = true, required = false, type = Boolean.class)
    private boolean deleteOnsuccessful;
    
    public static final String TAG_URL = "url";
    @XmlElement(name = TAG_URL, nillable = false, required = true, type = String.class)
    private String url;
    
    public static final Status DEFAULT_STATUS = Status.SCHEDULED;
    public static final String TAG_STATUS = "status";
    @XmlElement(name = TAG_STATUS, nillable = true, required = false, type = Status.class)
    private Status status;
    
    public static final boolean DEFAULT_IS_LOCK = false;
    public static final String TAG_IS_LOCK = "isLock";
    @XmlElement(name = TAG_IS_LOCK, nillable = true, required = false, type = Boolean.class)
    private boolean isLock;

    public Schedule() {
        this.deleteOnsuccessful = DEFAULT_DELETE_ON_SUCCESSFUL;
        this.status = DEFAULT_STATUS;
        this.isLock = DEFAULT_IS_LOCK;
    }
    
    public Schedule(Document document) {
        this(document.getString(TAG_CRON_EXPRESSION),
        document.getString(TAG_URL),
        document.getBoolean(TAG_DELETE_ON_SUCCESSFUL, DEFAULT_DELETE_ON_SUCCESSFUL),
        Status.valueOf(document.getString(TAG_STATUS)),
        document.getBoolean(TAG_IS_LOCK, DEFAULT_IS_LOCK));
        
        ObjectId id = document.getObjectId(TAG_ID);
        if(id != null) this.ID = id.toString();
    }

    public Schedule(String cronExpression, String url) {
        this(cronExpression, url, DEFAULT_DELETE_ON_SUCCESSFUL, 
                                                DEFAULT_STATUS, DEFAULT_IS_LOCK);
    }

    public Schedule(String cronExpression, String url, boolean deleteOnsuccessful) {
        this(cronExpression, url, deleteOnsuccessful, 
                                                    DEFAULT_STATUS, DEFAULT_IS_LOCK);
    }

    public Schedule(String cronExpression, String url, boolean deleteOnsuccessful, 
                                                    Status status, boolean isLock) {
        this.cronExpression = cronExpression;
        this.deleteOnsuccessful = deleteOnsuccessful;
        this.url = url;
        this.status = status;
        this.isLock = isLock;
    }
    
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
    
    public String getCronExpression() {
        return cronExpression;
    }
    
    public CronScheduleBuilder getCronExpressionBuilder() {
        return cronSchedule(getCronExpression());
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public boolean isDeleteOnsuccessful() {
        return deleteOnsuccessful;
    }

    public void setDeleteOnsuccessful(boolean deleteOnsuccessful) {
        this.deleteOnsuccessful = deleteOnsuccessful;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isLock() {
        return isLock;
    }

    public void setLock(boolean isLock) {
        this.isLock = isLock;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.ID);
        hash = 23 * hash + Objects.hashCode(this.cronExpression);
        hash = 23 * hash + (this.deleteOnsuccessful ? 1 : 0);
        hash = 23 * hash + (this.isLock ? 1 : 0);
        hash = 23 * hash + Objects.hashCode(this.url);
        hash = 23 * hash + Objects.hashCode(this.status);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Schedule other = (Schedule) obj;
        if (this.deleteOnsuccessful != other.deleteOnsuccessful) {
            return false;
        }
        if (this.isLock != other.isLock) {
            return false;
        }
        if (!Objects.equals(this.ID, other.ID)) {
            return false;
        }
        if (!Objects.equals(this.cronExpression, other.cronExpression)) {
            return false;
        }
        if (!Objects.equals(this.url, other.url)) {
            return false;
        }
        if (this.status != other.status) {
            return false;
        }
        return true;
    }
    
    public JsonObject toJSON() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add(TAG_ID, this.ID);
        builder.add(TAG_CRON_EXPRESSION, this.cronExpression);
        builder.add(TAG_URL, this.url);
        builder.add(TAG_DELETE_ON_SUCCESSFUL, this.deleteOnsuccessful);
        if(this.status != null)
            builder.add(TAG_STATUS, this.status.toString());
        
        builder.add(TAG_IS_LOCK, this.isLock);
        
        return builder.build();
    }
    
    /**
     * Return a instance of Mongo's Document instance */
    public Document toDocument() {
        // Start with all the mandatory fields
        Document document = new Document();
        if(this.ID != null) 
            document.append(TAG_ID, new ObjectId(this.ID));
        document.append(TAG_CRON_EXPRESSION, this.cronExpression);
        document.append(TAG_URL, this.url);
        document.append(TAG_DELETE_ON_SUCCESSFUL, this.deleteOnsuccessful);
        if(this.status != null)
            document.append(TAG_STATUS, this.status.toString());
        document.append(TAG_IS_LOCK, this.isLock);
        
        return document;
    }
}
