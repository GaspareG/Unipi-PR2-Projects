/* Gaspare Ferraro - matricola 520549
 * 
 * 
 * 
 * 
 */
public interface Graph<E> {
	
 	public void addNode(E toAdd);
	
	public void addEdge(E from, E to);
	
	public void removeNode(E toRemove);
	
	public void removeEdge(E from, E to);

}
