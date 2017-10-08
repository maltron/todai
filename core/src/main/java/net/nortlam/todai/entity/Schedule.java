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

/**
 * This is the most basic information that it will be stored into the Database
 * 
 * @author Mauricio "Maltron" Leal <maltron at gmail dot com>
 */
@XmlRootElement(name="schedule")
@XmlAccessorType(XmlAccessType.FIELD)
public class Schedule implements Serializable {

    private static final Logger LOG = Logger.getLogger(Schedule.class.getName());
    
    public static final String TAG_NAME = "name";
    @XmlElement(name = TAG_NAME, nillable = false, required = true, type = String.class)
    private String name;
    
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

    public Schedule() {
    }
    
    public Schedule(Document document) {
        this.name = document.getString(TAG_NAME);
        this.cronExpression = document.getString(TAG_CRON_EXPRESSION);
        this.deleteOnsuccessful = document.getBoolean(TAG_DELETE_ON_SUCCESSFUL, 
                                                DEFAULT_DELETE_ON_SUCCESSFUL);
        this.url = document.getString(TAG_URL);
    }

    public Schedule(String name, String cronExpression, String url) {
        this.name = name;
        this.cronExpression = cronExpression;
        this.url = url;
    }

    public Schedule(String name, String cronExpression, String url, boolean deleteOnsuccessful) {
        this.name = name;
        this.cronExpression = cronExpression;
        this.deleteOnsuccessful = deleteOnsuccessful;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCronExpression() {
        return cronExpression;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.name);
        hash = 47 * hash + Objects.hashCode(this.cronExpression);
        hash = 47 * hash + (this.deleteOnsuccessful ? 1 : 0);
        hash = 47 * hash + Objects.hashCode(this.url);
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
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.cronExpression, other.cronExpression)) {
            return false;
        }
        if (!Objects.equals(this.url, other.url)) {
            return false;
        }
        return true;
    }
    
    public JsonObject toJSON() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add(TAG_NAME, this.name);
        builder.add(TAG_CRON_EXPRESSION, this.cronExpression);
        builder.add(TAG_URL, this.url);
        builder.add(TAG_DELETE_ON_SUCCESSFUL, this.deleteOnsuccessful);
        
        return builder.build();
    }
}
