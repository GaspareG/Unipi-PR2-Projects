package TwistServer;

import java.util.UUID;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class TwistOnline 
{

	// Timeout in seconds
	private Integer timeout ;
	
	// Username -> token
	private Map<String,String> onlineUser ;
	// Username -> last keep-alive time
	private Map<String,Long> lastKeepAlive ;
	
	public TwistOnline(Integer timeout)
	{
		this.timeout = timeout ;
		this.onlineUser = new HashMap<String,String>();
		this.lastKeepAlive = new HashMap<String,Long>();
	}
	
	public String addNewUser(String username)
	{
		String token = null ;
		if( !this.isOnline(username) )
		{
			token = UUID.randomUUID().toString();
			this.onlineUser.put(username, token);
			this.lastKeepAlive.put(username, (long) 0);
			this.refreshUser(username);
		}
		return token ;
	}
	
	public void removeUser(String username)
	{
		if( isOnline(username) )
		{
			this.onlineUser.remove(username);
			this.lastKeepAlive.remove(username);
		}
	}
	
	public String getUsernameByToken(String token)
	{
		String out = null ;
		String usr[] = this.getOnlineUsers();
		for(int i=0; i<usr.length; i++)
		{
			String tkn = this.onlineUser.get(usr[i]);
			if( tkn.equals(token) )
			{
				out = usr[i];
				break;
			}
		}
		return out ;
	}
	
	public void refreshUser(String token)
	{
		String username = this.getUsernameByToken(token);
		if( username != null && this.isOnline(username) )
		{
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			this.lastKeepAlive.put(username, timestamp.getTime());
		}
	}
	
	public boolean isOnline(String username)
	{
		return this.onlineUser.containsKey(username) ;
	}
	
	public String[] getOnlineUsers()
	{
		String out[] = this.onlineUser.keySet().toArray(new String[this.onlineUser.size()]);
		return out ;
	}
	
	public void clean()
	{
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Long current = timestamp.getTime();
		
		String online[] = this.getOnlineUsers();
		for(int i=0; i<online.length; i++)
		{
			if( (current - this.lastKeepAlive.get(online[i])) > timeout*1000 )
			{
				this.removeUser(online[i]);
			}
		}
	}
}
