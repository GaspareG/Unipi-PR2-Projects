package TwistServer;

import TwistUtility.TwistAuthInterface;
import TwistUtility.TwistInviteInterface;
import TwistUtility.TwistJsonConf;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.security.MessageDigest ;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class TwistAuth implements TwistAuthInterface {

	private TwistJsonConf data;
	private TwistOnline manager; 
	private HashMap<String, TwistInviteInterface> callBackMap;
	private Registry registry ;
	
	public TwistAuth( Registry registry )
	{
		this.registry = registry ;
	}
	
	@Override
	public String hashString(String toHash) throws RemoteException {
        MessageDigest mDigest = null;
		try {
			mDigest = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
        byte[] result = mDigest.digest(toHash.getBytes());
        StringBuffer out = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            out.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        return out.toString();
	}

	@Override
	public String login(String user, String psw) throws RemoteException {
		
		if( user == null || psw == null ) throw new NullPointerException();
		
		String hashed = this.hashString(psw);
		String toCheck = data.getString(user);
		if( !this.manager.isOnline(user) && toCheck != null && hashed.equals(toCheck) )
		{
			System.out.println("[TwistServer] Login " + user);
			TwistInviteInterface callback;
			try {
				callback = (TwistInviteInterface) registry.lookup(TwistInviteInterface.REMOTE_OBJECT_NAME + "_" + user);
				callBackMap.put(user, callback);
			} catch (NotBoundException e) {
				e.printStackTrace();
			}
			return this.manager.addNewUser(user);
		}
		return null;
	}

	@Override
	public boolean keepAlive(String token) throws RemoteException {
		this.manager.refreshUser(token);
		return this.isConnected(token);
	}

	@Override
	public boolean isConnected(String token) throws RemoteException{
		return this.manager.isOnline( this.manager.getUsernameByToken(token) );
	}

	@Override
	public boolean logout(String token)  throws RemoteException {
		System.out.println("[TwistServer] Logout " + this.manager.getUsernameByToken(token));
		this.manager.removeUser( this.manager.getUsernameByToken(token) );
		return false;
	}

	@Override
	public boolean signUp(String user, String pwd) throws RemoteException {

		if( data.getString(user) == null )
		{
			String hashedPwd = this.hashString(pwd);
			data.setString(user, hashedPwd);
			System.out.println("[TwistServer] Signup " + user);
			return true ;
		}
		return false;
	}

	@Override
	public void setUserFile(TwistJsonConf data) throws RemoteException {
		if( data == null ) throw new NullPointerException("Empty user data file");
		
		this.data = data ;
	}

	public void setOnlineManager(TwistOnline onlineManager) throws RemoteException {
		this.manager = onlineManager ;
	}
	
	public void setCallBackMap(HashMap<String, TwistInviteInterface> callBackMap) throws RemoteException {
		this.callBackMap = callBackMap;
	}

	@Override
	public String getCurrentUser(String token) throws RemoteException {
		return this.manager.getUsernameByToken(token);
	}

}
