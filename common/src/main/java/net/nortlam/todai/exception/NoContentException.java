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

import java.util.logging.Logger;

/**
 * No Content was found
 * 
 * @author Mauricio "Maltron" Leal <maltron at gmail dot com>
 */
public class NoContentException extends Exception {

    /**
     * Creates a new instance of <code>NoContentException</code> without detail
     * message.
     */
    public NoContentException() {
        super();
    }

    /**
     * Constructs an instance of <code>NoContentException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NoContentException(String msg) {
        super(msg);
    }
    private static final Logger LOG = Logger.getLogger(NoContentException.class.getName());

    public NoContentException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoContentException(Throwable cause) {
        super(cause);
    }

    public NoContentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
