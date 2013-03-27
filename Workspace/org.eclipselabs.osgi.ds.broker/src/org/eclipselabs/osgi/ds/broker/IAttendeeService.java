package org.eclipselabs.osgi.ds.broker;

import org.eclipselabs.osgi.ds.broker.service.IAttendee;

public interface IAttendeeService {

	public boolean addAttendee( IAttendee<?,?,?> attendee );

	public boolean removeAttendee( IAttendee<?,?,?> attendee );
	
	/**
	 * dispose actions prior to removing the service
	*/
	public void dispose();

}
