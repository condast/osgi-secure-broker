package org.eclipselabs.osgi.ds.broker.meeting;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipselabs.osgi.ds.broker.meeting.IExchangeListener.Notifications;
import org.eclipselabs.osgi.ds.broker.service.IAttendee;
import org.eclipselabs.osgi.ds.broker.service.IPalaver;

public abstract class AbstractAttendee<T,U,V extends Object> implements IAttendee<T,U,V>, Comparable<IAttendee<T,U,V>> {

	private String identifier;
	private Collection<IPalaver<T>> palavers;
	
	private Collection<IExchangeListener> listeners;
	
	private boolean attending;
	private boolean matched;
	
	public AbstractAttendee( IPalaver<T> palaver ) {
		this.palavers = new ArrayList<IPalaver<T>>();
		this.palavers.add(palaver);
		this.identifier = this.getClass().getName();
		this.attending = false;
		this.matched = false;
		listeners = new ArrayList<IExchangeListener>();
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}

	protected void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	protected void addPalaver( IPalaver<T> palaver ){
		this.palavers.add( palaver );
	}

	protected void removePalaver( IPalaver<T> palaver ){
		this.palavers.remove( palaver );
	}

	@Override
	public Collection<IPalaver<T>> getPalavers() {
		return this.palavers;
	}

	void addExchangeListener( IExchangeListener listener ){
		this.listeners.add( listener );
	}

	void removeExchangeListener( IExchangeListener listener ){
		this.listeners.remove( listener );
	}

	/**
	 * Returns true if the attendee is allowed to attend
	 * @param choice
	 */
	@Override
	public void setAttending( boolean choice ){
		this.attending = choice;
	}
	
	@Override
	public boolean isAttending(){
		return this.attending;
	}

	public boolean isMatched(){
		return matched;
	}
	
	public void setMatched( boolean choice ){
		this.matched = choice;
	}
	
	boolean addReceivedDatum(V datum) {
		this.onDataReceived(datum);
		this.notifyChange( new ExchangeEvent<V>( this, Notifications.DATA_RECEIVED, datum ));
		return true;
	}

	/**
	 * Respond to receiving a datum
	 * @param datum
	 */
	protected abstract void onDataReceived( V datum );	
	
	protected void exchangeData(U data) {
		  this.notifyChange( new ExchangeEvent<U>( this, Notifications.DATA_EXCHANGE, data ));
	}

	private final void notifyChange( ExchangeEvent<?> event ){
		for( IExchangeListener listener: listeners ){
			if( !listener.equals( event.getSource() ))
				listener.notifyChange( event );
		}
	}
	
	@Override
	public int compareTo(IAttendee<T,U,V> o) {
		return this.identifier.compareTo( o.getIdentifier());
	}

	/**
	 * Returns the first palaver that matches with the given attendee, or null if none are found
	 * @param attendee
	 * @return
	 */
	public IPalaver<T> getMatchingPalaver( IAttendee<?,?,?> attendee ){
		for( IPalaver<?> secundary: attendee.getPalavers() ){
			if(( secundary == null ) || ( isNull( secundary.getIntroduction() )))
				continue;
			for( IPalaver<T> primary: this.getPalavers() ){
				if(( primary == null ) || ( isNull( primary.getIntroduction() )))
					continue;
				if( !primary.getIntroduction().equals( secundary.getIntroduction()))
					continue;
				if( primary.confirm( secundary.giveToken() ))
					return primary;
			}
		}
		return null;
	}

	/**
	 * Returns true if all the conditions are met for a palaver
	 * @param second
	 * @return
	 */
	public static boolean checkPalaver( IAttendee<?,?,?> first , IAttendee<?,?,?> second ){
		for( IPalaver<?> secundary: second.getPalavers() ){
			if(( secundary == null ) || ( isNull( secundary.getIntroduction() )))
				continue;
			for( IPalaver<?> primary: first.getPalavers() ){
				if(( primary == null ) || ( isNull( primary.getIntroduction() )))
					continue;
				if( !primary.getIntroduction().equals( secundary.getIntroduction()))
					continue;
				if( primary.confirm( secundary.giveToken() ))
					return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns true if there is a conflict in the claiming of attention
	 * @param first
	 * @param second
	 * @return
	 */
	public static boolean hasAttentionConflict( IAttendee<?,?,?> first , IAttendee<?,?,?> second ){
		for( IPalaver<?> secundary: second.getPalavers() ){
			if(( secundary == null ) || ( isNull( secundary.getIntroduction() )))
				continue;
			for( IPalaver<?> primary: first.getPalavers() ){
				if(( primary == null ) || ( isNull( primary.getIntroduction() )))
					continue;
				if( !primary.getIntroduction().equals( secundary.getIntroduction()))
					continue;
				if( !primary.confirm( secundary.giveToken() ))
					continue;
				if( primary.claimAttention() && secundary.claimAttention() )
					return true;
			}
		}
		return false;
	}
	
	public static final boolean isNull( String str ){
		return ( str == null ) || ( str.trim().length() == 0 ); 
	}	
}