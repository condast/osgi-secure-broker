package org.eclipselabs.osgi.ds.broker.client.and.provider;

import java.util.logging.Logger;

import org.eclipselabs.osgi.ds.broker.client.and.provider.encryption.Encryption;
import org.eclipselabs.osgi.ds.broker.client.and.provider.encryption.IEncryption;
import org.eclipselabs.osgi.ds.broker.service.AbstractListener;
import org.eclipselabs.osgi.ds.broker.service.AbstractPalaver;
import org.eclipselabs.osgi.ds.broker.service.ParlezEvent;


public class SimpleClient extends AbstractListener<String, String, String>{

	private static SimpleClient attendee = new SimpleClient();;
	
	private SimpleClient() {
		super( new ClientPalaver());
	}
	
	public static SimpleClient getInstance(){
		return attendee;
	}

	@Override
	protected void onDataReceived(ParlezEvent<String> event) {
		Logger logger = Logger.getLogger( this.getClass().getName() );
		logger.info("\tCLIENT: Message received from " + event.getData() );
	}
}

class ClientPalaver extends AbstractPalaver<String>{

	private static final String S_INTRODUCTION = "Introducing Myself";
	private static final String S_ENCRYPTION_KEY = "org.key.ds.myKey"; //Must be 16 chars for AES or ARCFOUR
	
	protected ClientPalaver() {
		super( S_INTRODUCTION );
	}

	@Override
	public String giveToken() {
		try {
			IEncryption encrypt = new Encryption( IEncryption.Algorithms.AES, S_ENCRYPTION_KEY );
			return encrypt.encryptData( S_INTRODUCTION );
		} catch( Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean confirm(Object token) {
		try {
			if(!( token instanceof String ))
				return false;
			IEncryption encrypt = new Encryption( IEncryption.Algorithms.AES, S_ENCRYPTION_KEY );
			String unscramble = encrypt.decryptData(( String )token );
			return ( unscramble.equals( S_INTRODUCTION ));
		} catch( Exception e) {
			e.printStackTrace();
		}
		return false;
	}	
}