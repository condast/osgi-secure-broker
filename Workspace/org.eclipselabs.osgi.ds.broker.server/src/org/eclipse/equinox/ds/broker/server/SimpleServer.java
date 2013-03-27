package org.eclipse.equinox.ds.broker.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.eclipselabs.osgi.ds.broker.server.encryption.Encryption;
import org.eclipselabs.osgi.ds.broker.server.encryption.IEncryption;
import org.eclipselabs.osgi.ds.broker.service.AbstractPalaver;
import org.eclipselabs.osgi.ds.broker.service.AbstractProvider;


public class SimpleServer extends AbstractProvider<String, String, String> implements Runnable {

	private static final String S_MSG_SERVING = "SERVER: Sending service to clients";
	private static final String S_HELLO_WORLD = "SERVER: Server says 'Hello World' ";

	public static int S_TIME_OUT = 10000; //Serve every second
	
	private static SimpleServer attendee = new SimpleServer();;
	
	private ExecutorService executor;
	private boolean started;
	
	private SimpleServer() {
		super( new Palaver());
		executor = Executors.newSingleThreadExecutor();
	}
	
	public static SimpleServer getInstance(){
		return attendee;
	}

	public void start(){
		this.started = true;
		this.executor.execute(this);
	}

	public void stop(){
		this.started = false;
		this.executor.shutdown();
	}

	@Override
	public void run() {
		Logger logger = Logger.getLogger( this.getClass().getName() );
		while( started ){
			logger.info( "" );
			logger.info( S_MSG_SERVING );
			super.exchangeData( S_HELLO_WORLD );
			try{
				Thread.sleep( S_TIME_OUT );
			}
			catch( InterruptedException e ){
				logger.info("Server interrupted");
			}
		}
		
	}
}

class Palaver extends AbstractPalaver<String>{

	private static final String S_INTRODUCTION = "Introducing Myself";
	private static final String S_ENCRYPTION_KEY = "org.key.ds.myKey"; //Must be 16 chars for AES or ARCFOUR
	
	protected Palaver() {
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