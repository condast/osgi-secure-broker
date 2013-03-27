package org.eclipselabs.osgi.ds.broker.petitioner;

import org.eclipselabs.osgi.ds.broker.petitioner.SimplePetitioner;
import org.eclipselabs.osgi.ds.broker.service.AbstractAttendeeProviderComponent;

public class AttendeeComponent extends AbstractAttendeeProviderComponent {

	@Override
	protected void initialise() {
		super.addAttendee( SimplePetitioner.getInstance() );
	}	
}
