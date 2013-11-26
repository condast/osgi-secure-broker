package org.eclipselabs.osgi.ds.broker.service;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipselabs.osgi.ds.broker.meeting.AbstractAttendee;
import org.eclipselabs.osgi.ds.broker.service.IParlezListener.Notifications;

public abstract class AbstractListener<T, U, V extends Object> extends AbstractAttendee<T,U,V> {

	private Collection<IParlezListener> listeners;
	
	protected AbstractListener(IPalaver<T> palaver) {
		super(palaver);
		listeners = new ArrayList<IParlezListener>();
		palaver.setClaimAttention(false );
	}

	protected AbstractListener(String identifier, IPalaver<T> palaver) {
		super(identifier, palaver);
		listeners = new ArrayList<IParlezListener>();
		palaver.setClaimAttention(false );
	}

	
	public void addParlezListener( IParlezListener listener ){
		this.listeners.add( listener );
	}

	public void removeParlezListener( IParlezListener listener ){
		this.listeners.remove( listener );
	}
	
	protected abstract void onDataReceived( ParlezEvent<V> event );
	
	protected final void onDataReceived(V datum) {
		ParlezEvent<V> event = new ParlezEvent<V>( this, Notifications.DATA_RECEIVED, datum ); 
		this.onDataReceived(event);
		this.notifyChange( event );
	}
	
	protected void notifyChange( ParlezEvent<?> event ){
		for( IParlezListener listener: listeners ){
			if( !listener.equals( event.getSource() ))
				listener.notifyChange( event );
		}
	}
}