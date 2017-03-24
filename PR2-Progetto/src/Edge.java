/* Gaspare Ferraro - matricola 520549
 * 
 * Overview: Generica rappresentazione di un arco di un grafo,
 * 		con nodi in entrata e in uscita di tipo E non null e non modificabili
 *  	con peso di tipo numerico non null e non modificabile
 *  	con diretto di tipo booleano non modificabile
 *  
 *  Typical Element: (From, To, Direct, Weight)
 *  	con From e To di tipo E, non null
 *  	con Direct di tipo boolean
 *  	con Weight di tipo Number, non null 
 */

public interface Edge<E> {

	/* Controlla se l'arco è diretto */
	public boolean isDirect();
	/*
	EFFECTS: restituisce Direct
	*/
	
	/* Controlla l'arco ha il nodo connected come from o to */
	public boolean connect(E connected);
	/*
	EFFECTS: restituisce true se From.equalsTo(connected) oppure To.equalsTo(connected), false altrimenti
	*/
	
	/* Restituisce il nodo in entrata */
	public E getFrom();
	/*
	EFFECTS: Restituisce From
	*/
	
	/* Restituisce il nodo in uscita*/
	public E getTo();
	/*
	EFFECTS: Restituisce To
	*/
	
	/* Restituisce il peso corrispondente dell'arco  */
	public Number getWeight();
	/*
	EFFECTS: Restituisce Weight
	*/
	
}
