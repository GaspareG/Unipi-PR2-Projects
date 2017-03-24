package TwistUtility;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

import javax.swing.DefaultListModel;

public interface TwistInviteInterface extends Remote, Serializable {

	public final long serialVersionUID = 1L;
	
	public final static String REMOTE_OBJECT_NAME="TwistInvite";
	
	public void receiveInvite(String username, String inviteToken) throws RemoteException;

	public void setPanel(DefaultListModel<String> modelInviti) throws RemoteException;
	
	public void setLista(HashMap<String,String> inviti) throws RemoteException;

}
