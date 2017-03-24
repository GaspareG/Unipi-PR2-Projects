import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class RForum implements Forum 
{
	private Map<String,Vector<String>> forums ;
	private Map<String,Vector<Notifica>> callbacks ;
	
	public RForum()
	{
		forums = new HashMap<String,Vector<String>>();
		callbacks = new HashMap<String,Vector<Notifica>>();
	}
	
	@Override
	public void addForum(String forumName) throws RemoteException {
		if(forumName == null || forumName.length() == 0)
			throw new RemoteException("Empty forum name!");

		if(forums.containsKey(forumName))
			throw new RemoteException("Forum already exists!");
		
		forums.put(forumName, new Vector<String>());
		callbacks.put(forumName, new Vector<Notifica>());
		
	}

	@Override
	public void addMessage(String forumName, String message) throws RemoteException {
		if(!forums.containsKey(forumName))
			throw new RemoteException("Forum not exists!");
		if(message == null)
			throw new RemoteException("Empty message!");

		forums.get(forumName).add(message);
		for(Notifica n : this.callbacks.get(forumName))
			n.sendNotify(forumName, message);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Vector<String> getMessage(String forumName) throws RemoteException {
		if(!forums.containsKey(forumName))
			throw new RemoteException("Forum not exists!");
		return (Vector<String>) this.forums.get(forumName).clone();
	}

	@Override
	public void subForum(String forumName, Notifica not) throws RemoteException {
		if(!forums.containsKey(forumName))
			throw new RemoteException("Forum not exists!");
		
		this.callbacks.get(forumName).add(not);
		
	}

}
