package org.eclipselabs.osgi.ds.broker.client.and.provider;

import java.util.logging.Logger;

import org.eclipselabs.osgi.ds.broker.client.and.provider.encryption.Encryption;
import org.eclipselabs.osgi.ds.broker.client.and.provider.encryption.IEncryption;
import org.eclipselabs.osgi.ds.broker.service.AbstractPalaver;
import org.eclipselabs.osgi.ds.broker.service.AbstractProvider;


public class SimpleProvider extends AbstractProvider<String, String, String>{

	public static String S_PETITION = "PETITIONER: Please provide me a service";

	public static String S_PROVIDE = "PROVIDER: This is what I can provide for you!";

	private static SimpleProvider attendee = new SimpleProvider();;
	
	private SimpleProvider() {
		super( new PetitionPalaver(), false );
	}
	
	public static SimpleProvider getInstance(){
		return attendee;
	}

	
	@Override
	protected void onDataReceived(String data) {
		Logger logger = Logger.getLogger( this.getClass().getName() );
		String msg = "\tPROVIDER: Petition received from " + data + 
				"\n\tPROVIDER: Providing a service: " + S_PROVIDE;
		logger.info(msg );
		super.provide( S_PROVIDE );	
	}
}

class PetitionPalaver extends AbstractPalaver<String>{

	private static final String S_INTRODUCTION = "Introducing Myself as Petitioner";
	private static final String S_ENCRYPTION_KEY = "org.key.ds.snKey"; //Must be 16 chars for AES or ARCFOUR
	
	protected PetitionPalaver() {
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