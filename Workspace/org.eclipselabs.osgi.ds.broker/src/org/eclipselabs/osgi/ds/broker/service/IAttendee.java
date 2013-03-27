package org.eclipselabs.osgi.ds.broker.service;

import java.util.Collection;

public interface IAttendee<T,U,V extends Object> {

	/**
	 * Get the identifier for this attendee
	 * @return
	 */
	public String getIdentifier();
	
	/**
	 * The palaver contains data for enlisting in assemblies at the roundhouse
	 * @return
	 */
	public Collection<IPalaver<T>> getPalavers();
	
	/**
	 * Returns true if the attendee is attending
	 * @return
	 */
	public boolean isAttending();

	/**
	 * If true, the attendee is attending the palaver;
	 * @param choice
	 */
	public void setAttending( boolean choice );

	/**
	 * Returns true if the attendee is matched to another
	 * @return
	 */
	public boolean isMatched();

	/**
	 * If true, the attendee is matched to another;
	 * @param choice
	 */
	public void setMatched( boolean choice );
}