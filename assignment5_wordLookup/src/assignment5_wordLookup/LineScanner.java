package assignment5_wordLookup;

import java.util.concurrent.ExecutorService;

public class LineScanner implements Runnable {

	private String line ;
	private String parola ;
	private int lineNumber ;
	private ExecutorService executor ;
	private ParallelWordLookup pwl ;
	
	public LineScanner(String line, ParallelWordLookup pwl, int lineNumber) {
		this.line = line ;
		this.parola = pwl.getWord() ;
		this.executor = pwl.getExecutor() ;
		this.pwl = pwl ;
		this.lineNumber = lineNumber ;
	}

	@Override
	public void run() {
		//System.out.println("["+assignment5.getOrario()+"] ["+Thread.currentThread().getName()+"] Analyze... [" + this.line + "]");
		if(this.line == null) return ;
		boolean res = this.line.indexOf(this.parola) != -1;
		if(res)
		{
			this.pwl.getExLock().lock();
			this.executor.shutdown();
			this.pwl.getExLock().unlock();
			this.pwl.setResult(this.lineNumber);
		}
		
	}

}
