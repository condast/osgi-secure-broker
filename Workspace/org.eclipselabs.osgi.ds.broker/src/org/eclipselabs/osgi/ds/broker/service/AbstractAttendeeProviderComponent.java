package org.eclipselabs.osgi.ds.broker.service;

import org.eclipselabs.osgi.ds.broker.IAttendeeService;
import org.eclipselabs.osgi.ds.broker.service.IAttendee;

public abstract class AbstractAttendeeProviderComponent {

	private IAttendeeService service;
	
	public AbstractAttendeeProviderComponent() {
	}
	
	protected final boolean addAttendee( IAttendee<?,?,?> attendee ){
		if( service == null )
			return false;
		return service.addAttendee(attendee);
	}

	protected final boolean removeAttendee(IAttendee<?,?,?> attendee ){
		if( service == null )
			return false;
		return service.removeAttendee(attendee);
	}
	
	protected abstract void initialise();

	public final void setAttendeeService( IAttendeeService service ){
		this.service = service;
		this.initialise();
	}

	protected void finalise(){};
	
	public final void unsetAttendeeService( IAttendeeService service ){
		this.finalise();
		this.service.dispose();
		this.service = null;
	}

}
