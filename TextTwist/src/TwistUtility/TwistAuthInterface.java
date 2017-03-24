package TwistUtility;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TwistAuthInterface extends Remote {

	public final static String REMOTE_OBJECT_NAME="TwistAuth";
			
	/*
	 * from String to SHA1 hash
	 * return hashed string of "toHash"
	 */
	public String hashString(String toHash) throws RemoteException;
	
	/*
	 * log-in user given username and password
	 * return auth token or null is incorrect data
	 */
	public String login(String user, String psw) throws RemoteException;
	
	/*
	 * keep-alive connection, to avoid auto log-out
	 * return true if connection stile alive, false otherwise
	 */
	public boolean keepAlive(String token) throws RemoteException;
	
	/*
	 * Check if user is connected
	 * return true if user is still connected, false otherwise
	 */
	public boolean isConnected(String token) throws RemoteException;
	
	/*
	 * Log-out from current session
	 * return true if user is correctly disconnected
	 */
	public boolean logout(String token) throws RemoteException;
	
	/*
	 * Sign-up for new user
	 * return false if invalid data or if user already exist, 
	 * 	true if correctly signed up
	 */
	public boolean signUp(String user, String pwd) throws RemoteException;
	
	/*
	 * Set file for user data
	 */
	public void setUserFile(TwistJsonConf data) throws RemoteException;

	/*
	 * Get current user by token
	 */
	public String getCurrentUser(String token) throws RemoteException;
	
	
}
