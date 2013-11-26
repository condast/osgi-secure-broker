package org.eclipselabs.osgi.ds.broker.service;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipselabs.osgi.ds.broker.service.IParlezListener.Notifications;

public abstract class AbstractPetitioner<T, U, V extends Object> extends
		AbstractListener<T, U, V> {

	private Collection<V> collection;
	
	protected AbstractPetitioner(IPalaver<T> palaver) {
		super(palaver);
		palaver.setClaimAttention(false);
		collection = new ArrayList<V>();
	}

	protected AbstractPetitioner( String identifier, IPalaver<T> palaver) {
		super(identifier, palaver);
		palaver.setClaimAttention(false);
		collection = new ArrayList<V>();
	}

	@Override
	protected void setIdentifier(String identifier) {
		super.setIdentifier(identifier);
	}

	/**
	 * Get the collection
	 * @return
	 */
	protected Collection<V> getCollection(){
		return collection;
	}

	
	@Override
	public void setMatched(boolean choice) {
		super.setMatched(choice);
	}

	/**
	 * Petition the given data and wait for the received 
	 * @param data
	 */
	public void petition( U data ){
		super.exchangeData(data);
	}

	@Override
	protected void onDataReceived(ParlezEvent<V> event) {
		collection.add( event.getData());
	}

	@Override
	protected final void notifyChange(ParlezEvent<?> event) {
		ParlezEvent<Collection<V>> event2 = new ParlezEvent<Collection<V>>( this, Notifications.DATA_RECEIVED, collection ); 
		super.notifyChange(event2);
	}
}
