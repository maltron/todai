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
 * NotExistException JAX-RS Mapper
 * @return Code 404: Resource doesn't exist
 * 
 * @author Mauricio "Maltron" Leal <maltron at gmail dot com>
 */
@Provider
public class NotExistExceptionMapper implements ExceptionMapper<NotExistException> {

    private static final Logger LOG = Logger.getLogger(NotExistExceptionMapper.class.getName());

    @Override
    public Response toResponse(NotExistException ex) {
        LOG.log(Level.WARNING, "### NOT EXIST EXCEPTION:{0}", ex.getMessage());
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
