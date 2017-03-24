/* Gaspare Ferraro - matricola 520549
 * 
 * Overview: Tipo di arco, non orientato e non pesato,
 * con i dati non modificabili e diversi da null di tipo E
 * 
 * AF = { V1 V2 }
 * IR = V1 != null && V2 != null
 */
public class Amicizia<E> implements Edge<E> {
	private E V1, V2;
		
	public Amicizia(E from, E to)
	{
		this.V1 = from;
		this.V2 = to;
	}
	
	/*Restituisce false, visto che non è orientato*/
	@Override
	public boolean isDirect() {
		return false;
	}
    /*
    EFFECTS: restituisce false
    */

	/*Restituisce true se connected è uno dei vertici*/
	@Override
	public boolean connect(E connected) {
		return V1.equals(connected) || V2.equals(connected);
	}
    /*
    EFFECTS: restituisce true se uno dei due nodi è uguale a connected
    */

	/*Restituisce il primo nodo*/
	@Override
	public E getFrom() {
		return V1;
	}
    /*
    EFFECTS: restituisce V1
    */

	/*Restituisce il secondo nodo*/
	@Override
	public E getTo() {
		return V2;
	}
    /*
    EFFECTS: restituisce V2
    */

	/*Restituisce 1, visto che non è pesato*/
	@Override
	public Number getWeight() {
		return 1;
	}
    /*
    EFFECTS: restituisce il valore numerico 1
    */

	/*Restituisce una stringa corrispondente all'oggetto*/
	@Override
	public String toString() {
		return "Amicizia [V1=" + V1 + ", V2=" + V2 + "]";
	}
    /*
    EFFECTS: restituisce una stringa formata dallo stato dell'arco
    */

	/*Restituisce il confronto con un altro oggetto*/
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Amicizia other = (Amicizia) obj;
		if (V1 == null) {
			if (other.V1 != null)
				return false;
		} else if (!V1.equals(other.V1))
			return false;
		if (V2 == null) {
			if (other.V2 != null)
				return false;
		} else if (!V2.equals(other.V2))
			return false;
		return true;
	}
    /*
    EFFECTS: restituisce true se obj non è null, è dello stesso tipo dell'arco, 
    	ed entrambi i nodi non sono null e sono uguali ai nodi dell'arco attuale, false altrimenti
    */

}