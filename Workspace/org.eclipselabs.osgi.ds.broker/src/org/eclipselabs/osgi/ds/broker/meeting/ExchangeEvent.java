package org.eclipselabs.osgi.ds.broker.meeting;

import java.util.EventObject;

import org.eclipselabs.osgi.ds.broker.meeting.IExchangeListener.Notifications;

class ExchangeEvent<U extends Object> extends EventObject {
	private static final long serialVersionUID = 1L;

	private Notifications notification;
	private U data;
	
	ExchangeEvent(Object arg0, Notifications notification ) {
		super(arg0);
		this.notification = notification;
	}

	ExchangeEvent(Object arg0, Notifications notification, U data) {
		this( arg0, notification );
		this.data = data;
	}

	Notifications getNotification() {
		return notification;
	}

	U getData() {
		return data;
	}
}
