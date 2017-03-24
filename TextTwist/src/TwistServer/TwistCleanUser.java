package TwistServer;

public class TwistCleanUser extends Thread {

	private TwistOnline manager ;
	private Integer timeout ;
	
	public TwistCleanUser(TwistOnline onlineManager, Integer LoginTimeoutSec) {
		this.manager = onlineManager ;
		this.timeout = LoginTimeoutSec ;
	}
	
	@Override
	public void run() {
		
		while( !Thread.currentThread().isInterrupted() )
		{
			manager.clean();
			try {
				Thread.sleep(timeout*1000);
			} catch (InterruptedException e) {
				
			}
		}
		
	}
	

}
