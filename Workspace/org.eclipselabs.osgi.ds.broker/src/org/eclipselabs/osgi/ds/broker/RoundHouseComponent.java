package org.eclipselabs.osgi.ds.broker;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.eclipselabs.osgi.ds.broker.meeting.RoundHouse;
import org.eclipselabs.osgi.ds.broker.service.IAttendee;

public class RoundHouseComponent<T extends Object> implements IAttendeeService, CommandProvider
{
	private RoundHouse roundhouse;
	
	public void activate(){
		roundhouse = RoundHouse.getInstance();
	}
	
	public void deactivate(){
		roundhouse.dispose();
		roundhouse = null;
	}
	
	@Override
	public boolean addAttendee( IAttendee<?,?,?> attendee ){
		return roundhouse.addAttendee(attendee);
	}

	@Override
	public boolean removeAttendee( IAttendee<?,?,?> attendee ){
		return roundhouse.removeAttendee(attendee);
	}

	public void _attendees( CommandInterpreter ci ){
		ci.println( roundhouse.printAttendees() );
	}

	public void _matched( CommandInterpreter ci ){
		ci.println( roundhouse.printMatched() );
	}

	public void _waiting( CommandInterpreter ci ){
		ci.println( roundhouse.printWaiting() );
	}

	public void _roundhouse( CommandInterpreter ci ){
		ci.println( "For further help, type in the provided commands:\n" + getHelp());
	}

	@Override
	public String getHelp() {
		StringBuffer buffer = new StringBuffer();
		buffer.append( "\troundhouse - General Help\n" );
		buffer.append( "\tattendees - A list of attendees at the roundhouse.\n" );
		buffer.append( "\tmatched - A list of clients that have been matched.\n" );
		buffer.append( "\twaiting - A list of clients that are waiting to be matched." );
		return buffer.toString();
	}
	
	/**
	 * dispose actions prior to removing the service
	*/
	public void dispose(){
		this.roundhouse.dispose();
	}
}
