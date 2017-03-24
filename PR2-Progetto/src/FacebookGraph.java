/* Gaspare Ferraro - matricola 520549
 * 
 *  Overview: Tipo modificabile di grafo di una rete sociale, 
 *  	con nodi generici e archi che estendono amicizia.
 *  
 *  AF = (vertices, links)
 *  IR = vertices != null && links != null &&
 *  	 forall i. vertices.get(i) != null && links.get(vertices.get(i)) != null &&
 *  	 forall i,j. links.get(vertices.get(i)).get(j).connect(vertices.get(i)) &&
 *  				vertices.contains( links.get(vertices.get(i)).get(j).getFrom() ) &&
 *  				vertices.contains( links.get(vertices.get(i)).get(j).getTo() ) 
 */

import java.util.Map;
import java.util.Queue;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

public class FacebookGraph<E,N extends Amicizia<E>> implements Graph<E,Amicizia<E>> 
{
	private Vector<E> vertices;
	private Map<E,Vector<Amicizia<E>>> links;
	
	public FacebookGraph()
	{
		this.vertices = new Vector<E>();
		this.links = new HashMap<E,Vector<Amicizia<E>>>();
	}

	/* Aggiunge il nodo toAdd ai vertici*/
	@Override
	public void addNode(E toAdd) 
	{
		if(toAdd == null)
			throw new NullPointerException("[addNode] nodo null");
		
		if(!vertices.contains(toAdd))
		{
			vertices.add(toAdd);
			links.put(toAdd, new Vector<Amicizia<E>>());
		}
	}
	/*
	MODIFIES: this
	EFFECTS: modifica vertices aggiugendo toAdd
 		se toAdd == null lancia una NullPointerException (unchecked)	
    */
 	
	/* Aggiunge l'arco toAdd ai link*/
	@Override
	public void addEdge(Amicizia<E> toAdd) 
	{
		if(toAdd == null)
			throw new NullPointerException("[addEdge] arco null");
		
		if(!vertices.contains(toAdd.getFrom()))
			throw new IllegalArgumentException();
		if(!vertices.contains(toAdd.getTo()))
			throw new IllegalArgumentException();
		
		links.get(toAdd.getFrom()).add(toAdd);
		
		if(!toAdd.isDirect())
			links.get(toAdd.getTo()).add(toAdd);
	}
	/*
	MODIFIES: this
	EFFECTS: modifica links.get(toAdd.from()) aggiugendo toAdd e,
			se l'arco non è diretto, links.get(toAdd.from()) aggiungendo toAdd
 		se toRemove == null lancia una NullPointerException (unchecked)
 		se toRemove.from ∉ Vertices || toRemove.to ∉ Vertices lancia una IllegalArgumentException (unchecked)	
    */

	/* Aggiunge l'arco tra v1 e v2*/
	public void addEdge(E v1, E v2)
	{
		this.addEdge(new Amicizia<E>(v1,v2));
	}
	/*
	MODIFIES: this
	EFFECTS: Crea un nuovo arco amicizia tra V1 e V2 e lo passa
		alla funzione addEdge(Amicizia<E>)
	*/
	
	/* Rimuove, se presente, il nodo toRemove e i suoi link incidenti*/
	@Override
	public void removeNode(E toRemove) 
	{
		if(toRemove == null)
			throw new NullPointerException("[removeNode] nodo null");
		
		if(!vertices.contains(toRemove))
			throw new IllegalArgumentException();
		vertices.remove(toRemove);
		Vector<Amicizia<E>> daRimuovere = links.get(toRemove);
		for(Amicizia<E> link : daRimuovere)
		{
			if(!link.isDirect())
				links.get(link.getTo()).remove(link);
		}
		links.remove(toRemove);
	
	}
	/*
	MODIFIES: this
	EFFECTS: modifica vertices rimuovendo toRemove,
			 modifica links rimuovendo tutti gli archi che contengono toRemove
			 modifica links rimuovendo l'elemento con chiave toRemove.
		se toRemove == null lancia una NullPointerException (unchecked)
		se toRemove ∉ Vertices lancia una IllegalArgumentException (unchecked)
    */
	
	/* Rimuove, se presente, l'arco toRemove dall'insieme Nodes */
	@Override
	public void removeEdge(Amicizia<E> toRemove) 
	{
		if(toRemove == null)
			throw new NullPointerException("[toRemove] arco null");
		
		if(!vertices.contains(toRemove.getFrom()))
			throw new IllegalArgumentException();
		
		if(!vertices.contains(toRemove.getTo()))
			throw new IllegalArgumentException();
		
		if(!links.get(toRemove.getFrom()).contains(toRemove))
			throw new IllegalArgumentException();
				
		if(toRemove.isDirect())
		{
			if(!links.get(toRemove.getTo()).contains(toRemove))
				throw new IllegalArgumentException();
			
			links.get(toRemove.getTo()).contains(toRemove);			
		}

		links.get(toRemove.getFrom()).remove(toRemove);
	}
	/*
    MODIFIES: this
    EFFECTS: modifica links.get(toRemove.getFrom()) rimuovendo toRemove,
    		se l'arco non è diretto lo rimuove pure da links.get(toRemove.getTo())
    	se toRemove == null lancia una NullPointerException (unchecked)
    	se toRemove ∉ Links lancia una IllegalArgumentException (unchecked)
    */
	
	/*Crea l'arco tra V1 e V2 e lo rimuove*/
	public void removeEdge(E v1, E v2)
	{
		this.removeEdge(new Amicizia<E>(v1,v2));
	}
	/*
    MODIFIES: this
    EFFECTS: Crea un arco tra V1 e V2 e lo passa alla removeEdge(Amicizia<E>)
    */
	
	/*Stampa il contenuto del grafo */
	@Override
	public String toString()
	{
		String out = "[FacebookGraph]\n";
		for(E persona : vertices)
		{
			out += "- "+persona+ " ha "+links.get(persona).size()+" amici\n";
			for(Amicizia<E> amici : links.get(persona))
			{
				out += "\t- "+amici.toString() + "\n" ;
			}
		}
		return out;
	}
	/*
    EFFECTS: Crea una stringa elencati tutti i vertici e i rispettivi archi 
    */
	
	/*Restituisce la distanza tra due nodi*/
	public Integer distance(E v1, E v2) {
		if(v1 == null || v2 == null)
			throw new NullPointerException();
		if(!vertices.contains(v1) || !vertices.contains(v2))
			throw new IllegalArgumentException();
		
		// Eseguo una BFS del grafo, poichè la rete non è pesata
		Integer out = -1 ;
		Map<E,Boolean> visitati = new HashMap<E,Boolean>();
		Map<E,Integer> distanza = new HashMap<E,Integer>();
		Queue<E> q = new LinkedList<E>();
		visitati.put(v1, true);
		distanza.put(v1, 0);
		q.add(v1);
		while(!q.isEmpty())
		{
			E attuale = q.poll();
			visitati.put(attuale, true);
			for(Amicizia<E> am : links.get(attuale))
			{
				E to = am.getFrom();
				if(to.equals(attuale))
					to = am.getTo();
				
				if(!visitati.containsKey(to))
				{
					q.add(to);
					distanza.put(to, distanza.get(attuale)+1);
				}
			}
		}
		if(visitati.containsKey(v2) && visitati.get(v2) == true)
			out = distanza.get(v2);
		return out;
	}
	/*
	 EFFECTS: Calcola la distanza tra v1 e v2,
	 	restituisce -1 se v1 e v2 non sono connessi
	 */
	
	/*Restituisce il diametro della rete*/
	public Integer diameter() {
		Integer r = 0 ;
		for(E v1 : vertices)
		{
			for(E v2 : vertices)
			{
				if(!v1.equals(v2))
				{
					r = Math.max(r, distance(v1,v2));
				}
			}
		}
		return r;
	}
	/*
	 EFFECTS: Per ogni coppia di vertici distinti calcola la distanza e ne trova il massimo valore
	 */
	
}
