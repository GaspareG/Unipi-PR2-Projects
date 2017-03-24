package assignment11_CongressoRmi;

import java.rmi.RemoteException;
import java.util.Vector;

public class RCongresso implements Congresso {

	private Integer nday, nsessionperday, nmaxspeakerpersession;
	private Vector<Speaker> sessionSpeaker[][] ;
	private String sessionName[][] ;
	
	@SuppressWarnings("unchecked")
	public RCongresso(Integer day, Integer sessionperday, Integer maxspeakerpersession)
	{
		if(day == null || sessionperday == null)
			throw new IllegalArgumentException("Day or Session per day can't be null!");
		
		this.nday = day ;
		this.nsessionperday = sessionperday ;
		this.nmaxspeakerpersession = maxspeakerpersession ;
		this.sessionSpeaker = new Vector[nday][nsessionperday];
		this.sessionName = new String[nday][nsessionperday];
		for(int i=0; i<nday; i++)
			for(int j=0; j<nsessionperday; j++)
			{
				this.sessionSpeaker[i][j] = new Vector<Speaker>();
				this.sessionName[i][j] = null;
			}		
	}
	public void setSessionName(int day, int session, String sname)
	{
		if(day >= nday || session >= nsessionperday)
			throw new IllegalArgumentException("Day or Session not valid!");
		
		if(sessionName[day][session] == null)
			sessionName[day][session] = sname;
		else
			throw new IllegalArgumentException("Specified session already has a name!");			
	}
	
	@Override
	public Integer getDays() throws RemoteException {
		return this.nday;
	}

	@Override
	public Integer getSessionPerDay() throws RemoteException {
		return this.nsessionperday;
	}

	@Override
	public void addSpeakerSession(int day, int session, Speaker toAdd) throws RemoteException {
		if(day >= nday || session >= nsessionperday)
			throw new IllegalArgumentException("Day or Session not valid!");
		
		if(this.sessionSpeaker[day][session].size() >= this.nmaxspeakerpersession)
			throw new IllegalArgumentException("Already reached max speaker for this session!");
		
		if(this.sessionSpeaker[day][session].contains(toAdd))
			throw new IllegalArgumentException("This speaker is already sign-up for the session");
		
		this.sessionSpeaker[day][session].addElement(toAdd);
	}

	@Override
	public String getSessionName(int day, int session) throws RemoteException {
		if(day >= nday || session >= nsessionperday)
			throw new IllegalArgumentException("Day or Session not valid!");
		return this.sessionName[day][session];
	}

	@SuppressWarnings("unchecked")
	@Override
	public Vector<Speaker> getSessionSpeaker(int day, int session) throws RemoteException {
		if(day >= nday || session >= nsessionperday)
			throw new IllegalArgumentException("Day or Session not valid!");
		
		return (Vector<Speaker>) this.sessionSpeaker[day][session].clone();
	}

	@Override
	public String getSessionSchedule(int day, int session) throws RemoteException {
		if(day >= nday || session >= nsessionperday)
			throw new IllegalArgumentException("Day or Session not valid!");
		
		StringBuilder sb= new StringBuilder();
		sb.append("Session name: ");
		sb.append(this.getSessionName(day, session));
		sb.append("\nSpeakers:\n");
		Vector<Speaker> sp = this.getSessionSpeaker(day, session);
		for(Speaker s : sp)
			sb.append("\t" + s.toString() + "\n");
		return sb.toString();
	}

	@Override
	public String getDaySchedule(int day) throws RemoteException {
		if(day >= nday)
			throw new IllegalArgumentException("Day not valid!");

		StringBuilder sb= new StringBuilder();
		sb.append("Day "+day+" schedule:\n");
		for(int i=0; i<this.nsessionperday; i++)
		{
			sb.append("- Session "+i+":\n");
			sb.append(this.getSessionSchedule(day, i));
		}
		return sb.toString();
	}

	@Override
	public String getCongressSchedule() throws RemoteException {

		StringBuilder sb= new StringBuilder();
		sb.append("Congress Schedule:\n");
		for(int i=0; i<this.nday; i++)
		{
			sb.append("==================================\n");
			sb.append("# Day "+i+":\n");
			sb.append(this.getDaySchedule(i));
			sb.append("==================================\n");
			sb.append("\n");
		}
		return sb.toString();
	}
	
	
}
