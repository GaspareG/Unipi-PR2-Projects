package TwistClient;

import java.rmi.RemoteException;

import TwistUtility.TwistAuthInterface;

public class TwistAuthKeepAlive extends Thread {
	
	private TwistAuthInterface auth ;
	private Integer refreshSec ;
	private String token ;
	public TwistAuthKeepAlive( TwistAuthInterface auth, Integer refreshSec, String stoken )
	{
		this.auth = auth ;
		this.refreshSec = refreshSec ;
		this.token = stoken ;
	}
	
	@Override
	public void run() {
		
		try {
			while( auth.isConnected(token) && !Thread.currentThread().isInterrupted() )
			{
				auth.keepAlive(token);
				Thread.sleep(refreshSec*1000);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			
		}
		
	}
	
	
}
