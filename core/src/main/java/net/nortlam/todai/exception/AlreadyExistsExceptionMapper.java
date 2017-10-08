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
package net.nortlam.todai.exception;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Indicate that some data was already found in the Database
 * @return: Code 409: CONFLICT
 * @author Mauricio "Maltron" Leal <maltron at gmail dot com>
 */
@Provider
public class AlreadyExistsExceptionMapper implements ExceptionMapper<AlreadyExistsException> {

    private static final Logger LOG = Logger.getLogger(AlreadyExistsExceptionMapper.class.getName());

    @Override
    public Response toResponse(AlreadyExistsException ex) {
        LOG.log(Level.WARNING, "### ALREADY EXISTS EXCEPTION:{0}", ex.getMessage());
        return Response.status(Response.Status.CONFLICT).build();
    }
}
