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

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Mauricio "Maltron" Leal <maltron at gmail dot com>
 */
@XmlEnum
@XmlType(name = "status")
public enum Status {
    
    @XmlEnumValue(value = "Scheduled")
    SCHEDULED("Scheduled"), 
    
    @XmlEnumValue(value = "Successful")
    SUCCESSFUL("Successful"), 
    
    @XmlEnumValue(value = "Failed")
    FAILED("Failed");
    
    private String value;
    
    private Status(String value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return value;
    }
}
