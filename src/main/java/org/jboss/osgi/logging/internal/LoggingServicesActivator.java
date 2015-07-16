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

import org.jboss.osgi.logging.LogServiceTracker;
import org.jboss.osgi.logging.LoggingService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogReaderService;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The Log Services Activator
 *
 * @author thomas.diesler@jboss.com
 * @author David Bosschaert
 * @since 23-Jan-2009
 */
public class LoggingServicesActivator implements BundleActivator {
    private LogServiceTracker logServiceTracker;
    private ServiceTracker<LogReaderService,LogReaderService> logReaderTracker;

    public void start(BundleContext context) {
        logServiceTracker = new LogServiceTracker(context);

        // Track LogReaderService and add/remove LogListener
        logReaderTracker = new LogReaderServiceServiceTracker(context);
        logReaderTracker.open();

        // Register the logging marker service
        context.registerService(LoggingService.class, new LoggingService() {
        }, null);
    }

    public void stop(BundleContext context) {
        if (logServiceTracker != null)
            logServiceTracker.close();

        if (logReaderTracker != null)
            logReaderTracker.close();
    }

    static class LogReaderServiceServiceTracker extends ServiceTracker<LogReaderService,LogReaderService> {
        public LogReaderServiceServiceTracker(BundleContext context) {
            super(context, LogReaderService.class, null);
        }

        @Override
        public LogReaderService addingService(ServiceReference<LogReaderService> reference) {
            LogReaderService logReader = super.addingService(reference);
            logReader.addLogListener(new LogListenerBridge());
            return logReader;
        }
    }
}