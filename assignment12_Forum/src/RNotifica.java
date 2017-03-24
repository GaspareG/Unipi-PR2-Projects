import java.rmi.RemoteException;

public class RNotifica implements Notifica{

	private String name;;
	
	public RNotifica(String name)
	{
		this.name = name;
	}
	
	@Override
	public String getName() throws RemoteException {
		return name;
	}

	@Override
	public void sendNotify(String forum, String msg) throws RemoteException {
		System.out.println("*NOTIFICA NUOVO MESSAGGIO* ["+forum+"] "+ msg);
	}

}
