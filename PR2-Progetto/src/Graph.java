/* Gaspare Ferraro - matricola 520549
 * 
 * Overview: Generica rappresentazione del TDA Grafo modificabile
 * 		con vertici di tipo E non null 
 * 		e archi non null che collegano solo vertici presenti nel grafo
 * 			di tipo N che implementa Edge<E>
 * 
 * Typical Element: (Vertices Link)
 * 		Vertices = V_0, ..., V_(n-1) con V_i oggetto di E e != 
 * 		Links = N_0, ..., N_(m-1) con N_i oggetto di N
 * 			e forall i. ( (N_i).from ∈ Vertices && (N_i).to ∈ Vertices )
 */
 
public interface Graph<E, N extends Edge<E>> {

	/* Aggiunge il nodo toAdd ai vertici*/
 	public void addNode(E toAdd);
    /*
	MODIFIES: this
	EFFECTS: Vertices = Vertices U toAdd
 		se toAdd == null lancia una NullPointerException (unchecked)	
    */
 	
	/* Aggiunge l'arco toAdd ai link*/
	public void addEdge(N toAdd);
    /*
	MODIFIES: this
	EFFECTS: Links = Links U toAdd
 		se toRemove == null lancia una NullPointerException (unchecked)
 		se toRemove.from ∉ Vertices || toRemove.to ∉ Vertices lancia una IllegalArgumentException (unchecked)	
    */
	
	/* Rimuove, se presente, il nodo toRemove e i suoi link incidenti*/
	public void removeNode(E toRemove);
    /*
	MODIFIES: this
	EFFECTS: Vertices = Vertices \ toRemove
		Links = Links \ Incidenti con
 		Incidenti = { L | L ∈ Link && L.(toRemove) }
		se toRemove == null lancia una NullPointerException (unchecked)
		se toRemove ∉ Vertices lancia una IllegalArgumentException (unchecked)
    */
	
	/* Rimuove, se presente, l'arco toRemove dall'insieme Nodes */
	public void removeEdge(N toRemove);
    /*
    MODIFIES: this
    EFFECTS: Links = Links \ toRemove
    	se toRemove == null lancia una NullPointerException (unchecked)
    	se toRemove ∉ Links lancia una IllegalArgumentException (unchecked)
    */
}
