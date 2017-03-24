package assignment3_laboratorioInformatica;

public interface Utente {


	public String getNome();
	public void setNome(String n);
	
	public int getComputer();
	public void setComputer(int pc);
	
	public void assegnaComputers(int tempo,Computer[] pcs);
}
