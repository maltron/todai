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
import java.util.logging.Logger;
import org.bson.types.ObjectId;

/**
 * This is the most basic information that it will be stored into the Database
 * 
 * @author Mauricio "Maltron" Leal <maltron at gmail dot com>
 */
public class Schedule implements Serializable {

    private static final Logger LOG = Logger.getLogger(Schedule.class.getName());
    
    private ObjectId objectID;
    
    public static final String TAG_NAME = "name";
    private String name;
    
    public static final String TAG_CRON_EXPRESSION = "cronExpression";
    private String cronExpression;
    
    public static final String TAG_DELETE_ON_SUCCESSFUL = "deleteOnsucessful";
    private boolean deleteOnsuccessful;

    public Schedule() {
    }

}
