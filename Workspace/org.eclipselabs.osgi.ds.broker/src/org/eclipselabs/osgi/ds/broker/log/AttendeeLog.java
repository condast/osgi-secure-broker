package org.eclipselabs.osgi.ds.broker.log;


import org.eclipse.core.runtime.IStatus;
import org.eclipselabs.osgi.ds.broker.Activator;

public class AttendeeLog {

	/**
	 * Log an info message
	 * @param message
	 */
	public static void logInfo( String message ){
		log( IStatus.INFO, message );
	}

	/**
	 * Log an info message
	 * @param message
	 */
	public static void logWarning( String message ){
		log( IStatus.WARNING, message );
		System.out.println( "WARNING: " + message );
	}

	/**
	 * Log an error message
	 * @param message
	 */
	public static void logError( String message, Throwable exception ){
		log( IStatus.ERROR, message, exception );
	}

	/**
	 * Log an error message
	 * @param message
	 */
	public static void logError( Throwable exception ){
		logError( "An unexpected excpetion was thrown", exception );
		exception.printStackTrace();
	}

	public static void log( int severity, String message ){
		
		Activator.getDefault().getLog().log( severity, message );
	}

	public static void log( int severity, String message, Throwable exception ){
		
		Activator.getDefault().getLog().log( severity, message, exception);
	}
}
