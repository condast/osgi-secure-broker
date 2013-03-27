package org.eclipse.equinox.ds.broker.server;

import org.eclipselabs.osgi.ds.broker.service.AbstractAttendeeProviderComponent;

public class AttendeeComponent extends AbstractAttendeeProviderComponent {

	@Override
	protected void initialise() {
		super.addAttendee( SimpleServer.getInstance() );
		SimpleServer.getInstance().start();
	}

	@Override
	protected void finalise() {
		SimpleServer.getInstance().stop();
		super.finalise();
	}

	
}
