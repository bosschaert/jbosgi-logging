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
package org.jboss.osgi.logging;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

/**
 * A combined ServiceTracker/LogService that bundles can use to access the registered LogService.
 *
 * If a LogService is registered, the LogServiceTracker delegates to that LogService. If there is no LogService registered, the
 * LogServiceTracker delegates to the {@link SystemLogService}
 *
 *
 * @author thomas.diesler@jboss.com
 * @since 11-Apr-2009
 */
public class LogServiceTracker extends ServiceTracker<LogService,LogService> implements LogService {
    private LogService log;

    public LogServiceTracker(BundleContext context) {
        super(context, LogService.class, null);
        log = new SystemLogService(context);
        open();
    }

    @Override
    public LogService addingService(ServiceReference<LogService> reference) {
        log = super.addingService(reference);
        return log;
    }

    @Override
    public void removedService(ServiceReference<LogService> reference, LogService service) {
        super.removedService(reference, service);
        log = new SystemLogService(context);
    }

    public void log(int level, String message) {
        log.log(level, message);
    }

    public void log(int level, String message, Throwable exception) {
        log.log(level, message, exception);
    }

    @SuppressWarnings("rawtypes")
    public void log(ServiceReference sr, int level, String message) {
        log.log(sr, level, message);
    }

    @SuppressWarnings("rawtypes")
    public void log(ServiceReference sr, int level, String message, Throwable exception) {
        log.log(sr, level, message, exception);
    }
}