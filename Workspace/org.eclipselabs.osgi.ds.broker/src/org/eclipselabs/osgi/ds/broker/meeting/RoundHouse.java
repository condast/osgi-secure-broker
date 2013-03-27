package org.eclipselabs.osgi.ds.broker.meeting;

import java.util.Collection;
import java.util.TreeSet;

import org.eclipselabs.osgi.ds.broker.IAttendeeService;
import org.eclipselabs.osgi.ds.broker.log.AttendeeLog;
import org.eclipselabs.osgi.ds.broker.service.IAttendee;
import org.eclipselabs.osgi.ds.broker.service.IPalaver;

public class RoundHouse implements IAttendeeService{

	public static final String S_ROUND_HOUSE_EMPTY = "The Round House is currently empty";
	public static final String S_NO_REGISTERED_ATTENDEES = "There are currently no registered attendees in the Round House";
	public static final String S_ALL_REGISTERED_ATTENDEES = "All the attendees in the Round House are registered";
	public static final String S_NO_MATCHES = " is not matched";
	public static final String S_MATCHED = " is matched with:\n";
	
	private static RoundHouse roundHouse = new RoundHouse();
	
	private Collection<Match> attendees;
	
	private RoundHouse() {
		attendees = new TreeSet<Match>();
	}
	
	/**
	 * Get an instance of the round house
	 * @return
	 */
	public static RoundHouse getInstance(){
		return roundHouse;
	}

	public boolean isEmpty(){
		return this.attendees.isEmpty();
	}
	
	/**
	 * Add a new attendee
	 * @param attendee
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean addAttendee( IAttendee<?,?,?> attendee ){
		if(!( attendee instanceof AbstractAttendee ))
			return false;
		
		//First check for attention conflicts
		for( Match match: attendees ){
			if( match.isAttending(attendee))
				return false;
			if( match.hasAttentionConflict( attendee )){
				AttendeeLog.logWarning( attendee.getIdentifier() + " has an attention conflict with " + match.first.getIdentifier() + ". Please check the Palaver.");
				return false;
			}
		}
		//Then see which current congregations the attendee can join
		for( Match match: attendees ){
			if( match.hasMatch( attendee ) || !match.checkPalaver( attendee ))
				continue;
			boolean retval = match.addAttendee( attendee );
			IPalaver<?> palaver = match.first.getMatchingPalaver(attendee);
			if(( palaver != null ) && (palaver.claimAttention() ))
				return retval;
		}
		//Last create new congregations with secundary attendees
		boolean retval = true;
		Collection<IAttendee<?,?,?>> temp = new TreeSet<IAttendee<?,?,?>>();
		for( Match match: attendees ){
			temp.addAll( match.getAssembly(attendee));
		}
		Match match = new Match( (AbstractAttendee<Object, Object, Object>) attendee );
		attendees.add( match );
		retval &= match.putAssembly( temp );
		if( retval )
			match.first.setAttending(true);
		return retval;
	}

	/**
	 * Remove an attendee
	 * @param attendee
	 * @return
	 */
	public boolean removeAttendee( IAttendee<?,?,?> attendee ){
		boolean retval = false;
		attendee.setMatched(false);
		for( Match match: attendees ){
			if( !match.isAttending(attendee))
				continue;
			if( match.first.equals( attendee ))
				return attendees.remove(match);
			
			retval |= match.removeAttendee(attendee);
			IPalaver<?> palaver = match.first.getMatchingPalaver(attendee);
			if(( palaver != null ) && ( palaver.claimAttention() ))
				return retval;
		}
		//Update attending
		for( Match match: attendees ){
			if( match.isAttending(attendee)){
				attendee.setAttending(true );
				return retval;
			}
		}
		attendee.setAttending(false);
		return retval;
	}
	
	/**
	 * Clear the roundhouse
	 */
	public void dispose(){
		for( Match match: attendees ){
			match.dispose();
		}
		attendees.clear();
	}

	/**
	 * Get a string that represents all the attendees
	 * @return
	 */
	public String printAttendees(){
		StringBuffer buffer = new StringBuffer();
		if( attendees.isEmpty() )
			return S_ROUND_HOUSE_EMPTY;
		
		for( Match match: attendees ){
			buffer.append( match.first.getIdentifier() + "\n" );
		}
		return buffer.toString();
	}

	/**
	 * Get a string that represents a list of assemblies
	 * @return
	 */
	public String printMatched(){
		StringBuffer buffer = new StringBuffer();
		if( attendees.isEmpty() )
			return S_ROUND_HOUSE_EMPTY;
		
		boolean result = false;
		for( Match match: attendees ){
			if( !match.isEmpty() ){
			  buffer.append( match.printMatched() + "\n" );
			  result = true;
			}
		}
		return ( result )? buffer.toString(): S_NO_REGISTERED_ATTENDEES;
	}

	/**
	 * Get a string that represents a list of assemblies
	 * @return
	 */
	public String printWaiting(){
		StringBuffer buffer = new StringBuffer();
		if( attendees.isEmpty() )
			return S_ROUND_HOUSE_EMPTY;
		
		boolean result = false;
		for( Match match: attendees ){
			if(( match.isEmpty() ) && ( !match.first.isMatched() )){
			  buffer.append( match.first.getIdentifier() + "\n" );
			  result = true;
			}
		}
		return ( result )? buffer.toString(): S_ALL_REGISTERED_ATTENDEES;
	}

} 

class Match implements Comparable<Match>, IExchangeListener{
	
	AbstractAttendee<Object,Object,Object> first;
	private Collection<IAttendee<Object,Object,Object>> assembly;
	
	Match( AbstractAttendee<Object,Object,Object> first ){
		assembly = new TreeSet<IAttendee<Object,Object,Object>>();
		this.first = first;
		first.addExchangeListener(this);
	}

	boolean checkPalaver( IAttendee<?,?,?> attendee ){
		return AbstractAttendee.checkPalaver( first, attendee);
	}

	public boolean isEmpty(){
		return this.assembly.isEmpty();
	}

	public void clear(){
		this.assembly.clear();
	}

	public void dispose(){
		for( IAttendee<?,?,?> attendee: this.assembly )
			attendee.setAttending(false);
		this.assembly.clear();
		this.first.removeExchangeListener(this);
		this.first.setAttending( false );
	}
	
	public boolean isDisposed(){
		return !this.first.isAttending();
	}

	/**
	 * Returns true if there is an attention conflict between the attendees
	 * @param second
	 * @return
	 */
	boolean hasAttentionConflict( IAttendee<?,?,?> second ){
		if( AbstractAttendee.hasAttentionConflict( first, second))
			return true;
		if( second == null )
			return false;
		for( IAttendee<?,?,?> attendee: this.assembly ){
			if( AbstractAttendee.hasAttentionConflict( attendee, second))
				return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	boolean addAttendee( IAttendee<?,?,?> attendee ){
		if( !checkPalaver( attendee ))
			return false;
		boolean retval = this.assembly.add((IAttendee<Object, Object, Object>) attendee );
		if( retval ){
			attendee.setAttending( true );
			((AbstractAttendee<?, ?, ?>) attendee).addExchangeListener(this);
		}
		attendee.setMatched( true );
		if( !first.isMatched() )
			first.setMatched(true);
		return retval;
	}
	
	boolean removeAttendee( IAttendee<?,?,?> attendee ){
		boolean retval = this.assembly.remove( attendee );
		if(!retval )
			return false;
		((AbstractAttendee<?, ?, ?>) attendee).removeExchangeListener(this);
		first.setMatched( !assembly.isEmpty());
		return retval;
	}
	
	/**
	 * Return true if there is a match
	 * @return
	 */
	boolean hasMatch( IAttendee<?,?,?> attendee ){
		return assembly.contains( attendee );
	}
	
	boolean isAttending( IAttendee<?,?,?> attendee ){
		if( first.equals( attendee ))
			return true;
		return this.assembly.contains( attendee ); 
	}
	
	/**
	 * Get an assembly that is assigned to the given attendee
	 * @param attendee
	 * @return
	 */
	Collection<IAttendee<?,?,?>> getAssembly( IAttendee<?,?,?> attendee ){
		Collection<IAttendee<?,?,?>> results = new TreeSet<IAttendee<?,?,?>>();
		for( IAttendee<?,?,?> secundary: this.assembly ){
			if( !AbstractAttendee.checkPalaver( attendee, secundary ))
				continue;
			if( !AbstractAttendee.hasAttentionConflict( attendee, secundary ))
				return results;
			results.add( secundary );
		}
		return results;
	}
	
	@SuppressWarnings("unchecked")
	boolean putAssembly( Collection<IAttendee<?,?,?>> attendees ){
		if( attendees == null )
			return false;
		if( attendees.isEmpty() )
			return true;
		attendees.remove(this.first );
		if( attendees.isEmpty() )
			return true;		
		return assembly.addAll((Collection<? extends IAttendee<Object, Object,Object>>) attendees );
	}

	@Override
	public void notifyChange( ExchangeEvent<?> event ) {
		if(( event.getData() == null ) || ( !event.getNotification().equals( Notifications.DATA_EXCHANGE )))
			return;
		if(!( event.getSource().equals( this.first ))){
			first.addReceivedDatum(( Object )event.getData());
			return;
		}
			
		for( IAttendee<Object,Object,Object> secundary: this.assembly ){
			if(!(secundary.equals(event.getSource() )))
			  ((AbstractAttendee<Object, Object, Object>) secundary).addReceivedDatum(( Object )event.getData() );
		}
		return;
	}

	@Override
	public int compareTo(Match arg0) {
		return this.first.getIdentifier().compareTo( arg0.first.getIdentifier());
	}
	
	/**
	 * Get a string that represents a list of assemblies
	 * @return
	 */
	String printMatched(){
		StringBuffer buffer = new StringBuffer();
		if( assembly.isEmpty() )
			return first.getIdentifier() + RoundHouse.S_NO_MATCHES;
		
		buffer.append( first.getIdentifier() + RoundHouse.S_MATCHED );
		for( IAttendee<?,?,?> attendee: assembly ){
			  buffer.append( "\t" + attendee.getIdentifier() + "\n" );
		}
		return buffer.toString();
	}
}