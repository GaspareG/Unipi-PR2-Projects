package TwistClient;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.HashMap;

import javax.swing.DefaultListModel;

import TwistUtility.TwistInviteInterface;

public class TwistInvite implements TwistInviteInterface, Serializable {

	private static final long serialVersionUID = 1L;
	
	private DefaultListModel<String> toAdd ;
	private HashMap<String,String> listaInviti;
	
	public TwistInvite()
	{
		
	}
	
	public void setPanel(DefaultListModel<String> modelInviti) throws RemoteException
	{
		this.toAdd = modelInviti;
	}
	
	public void setLista(HashMap<String,String> inviti) throws RemoteException
	{
		this.listaInviti = inviti;
	}
	
	@Override
	public void receiveInvite(String username, String inviteToken) throws RemoteException {
		this.listaInviti.put(username, inviteToken);
		this.toAdd.addElement(username);
	}
		
}
