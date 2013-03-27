package org.eclipselabs.osgi.ds.broker.service;

public interface IPalaver<T extends Object> {
	
	/**
	 * The first check; only attendees with equal introductions get matched
	 * @return
	 */
	public String getIntroduction();
	
	/**
	 * Give a token that allows other attendants to see if you belong to their
	 * congregation
	 * @return
	 */
	public T giveToken();

	/**
	 * Within this group, a token needs to be passed to confirm the tie
	 * between the attendants
	 * @return
	 */
	public boolean confirm( Object token );
	
	/**
	 * If true, the attendee claims the crowd with which it gets matched.
	 * If other attendees claim the same crowd, then their attendance is not
	 * accepted.
	 * @return
	 */
	public boolean claimAttention();
	
	/**
	 * set the flag for claiming attention
	 * @param choice
	 */
	public void setClaimAttention( boolean choice );
}