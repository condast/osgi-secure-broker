package org.eclipselabs.osgi.ds.broker.petitioner;

import java.util.logging.Logger;

import org.eclipselabs.osgi.ds.broker.petitioner.encryption.Encryption;
import org.eclipselabs.osgi.ds.broker.petitioner.encryption.IEncryption;
import org.eclipselabs.osgi.ds.broker.service.AbstractPalaver;
import org.eclipselabs.osgi.ds.broker.service.AbstractPetitioner;
import org.eclipselabs.osgi.ds.broker.service.ParlezEvent;


public class SimplePetitioner extends AbstractPetitioner<String, String, String>{

	private static final String S_HELLO_WORLD = "SERVER: Server says 'Hello World' ";
	private static final String S_PETITION = "PETITIONER: Please provide me a service";

	private static SimplePetitioner attendee = new SimplePetitioner();;
	
	private SimplePetitioner() {
		super( new ServerPalaver());
		super.addPalaver(new PetitionPalaver());
	}
	
	public static SimplePetitioner getInstance(){
		return attendee;
	}

	@Override
	protected void onDataReceived(ParlezEvent<String> event) {
		Logger logger = Logger.getLogger( this.getClass().getName() );
		//Only petition after a trigger from the server
		if( event.getData().equals( S_HELLO_WORLD )){
			String msg = "\tPETITIONER: Message received from " + event.getData() + "\n\t" + S_PETITION;
			logger.info( msg );
			super.petition( S_PETITION );	
		}else{
			logger.info( "\tPETITIONER: Service provided from " + event.getData());
		}
		
	}
}

class ServerPalaver extends AbstractPalaver<String>{

	private static final String S_INTRODUCTION = "Introducing Myself";
	private static final String S_ENCRYPTION_KEY = "org.key.ds.myKey"; //Must be 16 chars for AES or ARCFOUR
	
	protected ServerPalaver() {
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