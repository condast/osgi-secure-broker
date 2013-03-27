package org.eclipselabs.osgi.ds.broker.meeting;

import org.eclipselabs.osgi.ds.broker.util.StringStyler;

interface IExchangeListener {

	public enum Notifications{
		DATA_EXCHANGE,
		DATA_RECEIVED;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}

	public void notifyChange( ExchangeEvent<?> event );
}
