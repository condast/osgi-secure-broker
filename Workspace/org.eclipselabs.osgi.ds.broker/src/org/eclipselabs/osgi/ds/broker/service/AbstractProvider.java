package org.eclipselabs.osgi.ds.broker.service;

import org.eclipselabs.osgi.ds.broker.meeting.AbstractAttendee;

/**
 * The provider changes the order of the annotations, so that this remains intuitive:
 * T = authentication, U is request, V is response
 * @author Kees Pieters
 * @company Condast
 * @website www.condast.com
 *
 * @param <T> The token that is used to authenticate attendees within an assembly
 * @param <U> The received request. For a provider this is optional
 * @param <V> The data that is provided
 */
public abstract class AbstractProvider<T, U, V extends Object> extends
		AbstractAttendee<T,V,U> {

	protected AbstractProvider(IPalaver<T> palaver) {
		this( palaver, false );
	}

	protected AbstractProvider(IPalaver<T> palaver, boolean claimAttention ) {
		super(palaver);
		palaver.setClaimAttention( claimAttention );
	}

	protected AbstractProvider(String identifier, IPalaver<T> palaver, boolean claimAttention ) {
		super(identifier, palaver);
		palaver.setClaimAttention( claimAttention );
	}
	
	@Override
	protected void setIdentifier(String identifier) {
		super.setIdentifier(identifier);
	}

	/**
	 * A provider does not necessarily have to respond to a request. This is only required for a petitioner. 
	 * That's why this method is empty by default. 
	 */
	@Override
	protected void onDataReceived( U datum){}

	
	protected void provide( V data ){
		super.exchangeData(data);
	}
}