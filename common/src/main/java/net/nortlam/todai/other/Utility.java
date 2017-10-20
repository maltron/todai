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
package net.nortlam.todai.other;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Some utilities methods to help
 * 
 * @author Mauricio "Maltron" Leal <maltron at gmail dot com>
 */
public class Utility implements Serializable {

    private static final Logger LOG = Logger.getLogger(Utility.class.getName());
    
    // DEFAULT FORMAT FOR SHOWING DATES
    public static final SimpleDateFormat DATE_FORMAT = 
            new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    
    
    /**
     * Returning a easy way to read Dates in a String format */
    public static String format(Date date) {
        return DATE_FORMAT.format(date);
    }
}
