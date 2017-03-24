package assignment5_wordLookup;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class ParallelWordLookup {

	private BufferedReader buffer ;
	private int nThread ;
	private String parola ;
	private int result ;
	private ExecutorService executor ;
	private ReentrantLock exLock ;
	private ReentrantLock resLock ;
	
	public ParallelWordLookup(BufferedReader buffer, int nThread, String parola) throws InterruptedException 
	{
		
		this.buffer = buffer ;
		this.nThread = nThread ;
		this.parola = parola ;
		this.result = -1 ;
		this.executor = Executors.newFixedThreadPool(this.nThread);
		this.exLock = new ReentrantLock();
		this.resLock = new ReentrantLock();
		
		System.out.println("["+assignment5.getOrario()+"] [ParallelWordLookup] Start searching for word '"+this.parola+"' in file...");
		///////////////////////////////
		String line = null ;
		int nlinea = 0 ;
		System.out.println("["+assignment5.getOrario()+"] [ParallelWordLookup] Start reading lines...");
		do
		{
			try {
				//System.out.println("["+assignment5.getOrario()+"] [ParallelWordLookup] linea ["+(++nlinea)+"]");
				line = this.buffer.readLine();
				LineScanner ls = new LineScanner(line,this,++nlinea);
				Thread lineT = new Thread(ls);
				this.exLock.lock();
				if(!this.executor.isShutdown())
					this.executor.execute(lineT);
				this.exLock.unlock();	
			} catch (IOException e) {
				e.printStackTrace();
			}
		} while(line != null && !this.executor.isShutdown() );
		System.out.println("["+assignment5.getOrario()+"] [ParallelWordLookup] Finish reading lines...");
		this.executor.shutdown();	
		///////////////////////////////
		this.executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		System.out.println("["+assignment5.getOrario()+"] [ParallelWordLookup] Finish scanning... word '"+parola+"'"+(this.result!=-1?" ":" not ")+"found!");

	}

	public ReentrantLock getExLock()
	{
		return this.exLock ;
	}
	

	public int getResult()
	{
		int res = -1 ;
		this.resLock.lock();
		res = this.result ;
		this.resLock.unlock();
		return res;
	}
	public void setResult(int nline)
	{
		// nel caso più thread attivi trovino l'occorrenza
		// scelgo il numero di linea minore
		this.resLock.lock();
		if(this.result == -1 || (this.result != -1 && this.result > nline))
			this.result = nline ;
		this.resLock.unlock();
	}
	
	public String getWord()
	{
		return this.parola;
	}
	
	public ExecutorService getExecutor()
	{
		return this.executor ;
	}
}
