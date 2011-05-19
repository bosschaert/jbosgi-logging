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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

/**
 * A basic LogService that writes to System.out
 *
 * @author thomas.diesler@jboss.com
 * @since 11-Apr-2009
 */
public class SystemLogService implements LogService {
    private BundleContext context;

    public SystemLogService(BundleContext context) {
        this.context = context;
    }

    public void log(int level, String message) {
        logInternal(null, level, message, null);
    }

    public void log(int level, String message, Throwable exception) {
        logInternal(null, level, message, exception);
    }

    public void log(ServiceReference sr, int level, String message) {
        logInternal(sr, level, message, null);
    }

    public void log(ServiceReference sr, int level, String message, Throwable exception) {
        logInternal(sr, level, message, exception);
    }

    private void logInternal(ServiceReference sref, int level, String message, Throwable exception) {
        Bundle bundle;
        try {
            bundle = context.getBundle();
        } catch (IllegalStateException ex) {
            // If this BundleContext is no longer valid.
            return;
        }

        long time = System.currentTimeMillis();
        String bndStr = bundle.getSymbolicName();

        String srefStr = null;
        if (sref != null && sref.getBundle() != null)
            srefStr = sref.getBundle().getSymbolicName();

        String t = new SimpleDateFormat("dd-MMM-yyyy HH:mm.ss.SSS").format(new Date(time));
        String l = " " + logLevel(level);
        String s = srefStr != null ? ",sref=" + srefStr : "";
        String b = ",bnd=" + bndStr;
        String m = ",msg=" + message;
        String e = exception != null ? ",ex=" + exception : "";

        System.out.println("[" + t + l + b + s + m + e + "]");

        if (exception != null)
            exception.printStackTrace(System.out);
    }

    private String logLevel(int level) {
        String logLevel;
        switch (level) {
        case LogService.LOG_DEBUG:
            logLevel = "DEBUG";
            break;
        case LogService.LOG_INFO:
            logLevel = "INFO";
            break;
        case LogService.LOG_WARNING:
            logLevel = "WARN";
            break;
        case LogService.LOG_ERROR:
            logLevel = "ERROR";
            break;
        default:
            logLevel = "Level=" + level;
        }
        return logLevel;
    }
}