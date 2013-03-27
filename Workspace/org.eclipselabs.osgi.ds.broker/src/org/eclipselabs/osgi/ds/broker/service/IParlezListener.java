package org.eclipselabs.osgi.ds.broker.service;

import org.eclipselabs.osgi.ds.broker.util.StringStyler;

public interface IParlezListener {
	
	public enum Notifications{
		ATTENDING,
		LEAVING,
		DATA_EXCHANGE,
		DATA_RECEIVED,
		DATA_UPDATED;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}

	public void notifyChange( ParlezEvent<?> event );
}
