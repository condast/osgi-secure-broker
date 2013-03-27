package org.eclipselabs.osgi.ds.broker.client.and.provider;

import org.eclipselabs.osgi.ds.broker.service.AbstractAttendeeProviderComponent;

public class AttendeeComponent extends AbstractAttendeeProviderComponent {

	@Override
	protected void initialise() {
		super.addAttendee( SimpleClient.getInstance() );
		super.addAttendee( SimpleProvider.getInstance() );
	}	
}
