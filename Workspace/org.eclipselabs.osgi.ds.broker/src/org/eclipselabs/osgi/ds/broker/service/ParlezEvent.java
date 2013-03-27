package org.eclipselabs.osgi.ds.broker.service;

import java.util.EventObject;

import org.eclipselabs.osgi.ds.broker.service.IParlezListener.Notifications;

public class ParlezEvent<U extends Object> extends EventObject {
	private static final long serialVersionUID = 1L;

	private Notifications notification;
	private U data;
	
	public ParlezEvent(Object arg0, Notifications notification ) {
		super(arg0 );
		this.notification = notification;
	}

	public ParlezEvent(Object arg0, Notifications notification, U data) {
		this( arg0, notification );
		this.data = data;
	}

	public Notifications getNotification() {
		return this.notification;
	}

	public U getData() {
		return this.data;
	}
}
