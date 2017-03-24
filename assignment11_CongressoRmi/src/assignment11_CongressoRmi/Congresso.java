package assignment11_CongressoRmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

public interface Congresso extends Remote {
	
	public final static String REMOTE_OBJECT_NAME="CONGRESSO";
			
	// Numero di giorni del congresso
	public Integer getDays()
			throws RemoteException;
	
	// Numero di sessioni in un giorno
	public Integer getSessionPerDay()
			throws RemoteException;
	
	// Aggiunta di uno speaker ad una determinata sessione
	public void addSpeakerSession(int day, int session, Speaker toAdd)
			throws RemoteException;

	// Nome di una singola sessione
	public String getSessionName(int day, int session)
			throws RemoteException;
	
	// Speaker di una singola sessione
	public Vector<Speaker> getSessionSpeaker(int day, int session)
			throws RemoteException;

	// Programma di una singola sessione
	public String getSessionSchedule(int day, int session)
			throws RemoteException;
	
	// Programma di un intero giorno
	public String getDaySchedule(int day)
			throws RemoteException;

	// Programma dell'intero congresso
	public String getCongressSchedule()
			throws RemoteException;
	
}


/*

	Giorno 1
		S1: Nome S1
			[speaker1,speaker2,speaker3,speaker4,speaker5]
		S2: Nome S2
			[speaker1,speaker2,speaker3,speaker4,speaker5]
		...
		S12: Nome S12
			[speaker1,speaker2,speaker3,speaker4,speaker5]

	Giorno 2
		...
	Giorno 3
		...
*/