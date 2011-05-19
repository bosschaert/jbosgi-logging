/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.osgi.logging.internal;

import org.jboss.logging.Logger;
import org.osgi.framework.Bundle;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;
import org.osgi.service.log.LogService;

/**
 * A LogListener that logs LogEntrys to jboss-logging.
 *
 * @author thomas.diesler@jboss.com
 * @since 04-Mar-2009
 */
public class LogListenerBridge implements LogListener {
    public void logged(LogEntry entry) {
        Bundle bundle = entry.getBundle();
        int level = entry.getLevel();
        Throwable throwable = entry.getException();

        String loggerName = bundle.getSymbolicName();
        Logger log = Logger.getLogger(loggerName);

        String message = entry.getMessage();

        if (level == LogService.LOG_DEBUG)
            log.debug(message, throwable);

        else if (level == LogService.LOG_INFO) {
            // Ignore INFO message of type 'BundleEvent STARTING'
            String[] splitMessage = message.split("[\\s]");
            if (throwable != null || splitMessage.length != 2 || splitMessage[0].endsWith("Event") == false) {
                log.info(message, throwable);
            }
        }

        else if (level == LogService.LOG_WARNING)
            log.warn(message, throwable);

        else if (level == LogService.LOG_ERROR)
            log.error(message, throwable);
    }
}