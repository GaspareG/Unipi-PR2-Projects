/* Gaspare Ferraro - matricola 520549
 * 
 * 
 * 
 * 
 * 
 */
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Vector;
import java.util.AbstractMap.SimpleEntry;

public class MyGraph<E> implements Graph<E> 
{
	private Vector<E> node ;
	private Map<E,Vector<E>> edge ;
	
	public MyGraph()
	{
		node = new Vector<E>();
		edge = new HashMap<E,Vector<E>>();
	}
	
	@Override
	public void addNode(E toAdd) 
	{
		if(toAdd == null) throw new NullPointerException();
		if(!node.contains(toAdd))
		{
			node.add(toAdd);
			edge.put(toAdd, new Vector<E>());
		}
	}

	@Override
	public void addEdge(E from, E to) 
	{
		// Controllo la validità di from e to all'interno del grafo
		if(from == null || to == null) throw new NullPointerException();
		if(!isNode(from) || !isNode(to)) return;
		if(areFriends(from,to)) return;
		// Aggiungo le amicizie (from,to) e (to,from)
		edge.get(from).add(to);
		edge.get(to).add(from);		
	}
	
	public boolean areFriends(E from, E to)
	{
		if(from == null || to == null) throw new NullPointerException();
		if(!isNode(from) || !isNode(to)) return false ;
		return edge.get(from).contains(to);
	}
	
	public boolean isNode(E toCheck)
	{
		return this.node.contains(toCheck);
	}
	
	@Override
	public void removeNode(E toRemove) 
	{
		
	}

	@Override
	public void removeEdge(E from, E to) 
	{
		
	}
	
	public int getDistance(E from, E to)
	{
		// Controllo NullPointer
		if(from == null || to == null) throw new NullPointerException();
		// From o To non sono nodi della rete
		if(!isNode(from) || !isNode(to)) return -1 ;
		// Ottengo tutte le distanze da from
		Map<E,Integer> dists = getAllDistance(from);
		// Se è possibile raggiungere to da from restituisco la distanza
		if(dists.containsKey(to)) return dists.get(to);
		// Altrimenti from non è connesso a to
		return -1 ;
	}
	
	public Map<E,Integer> getAllDistance(E from)
	{
		// Coda delle entrate
		Queue<Entry<E,Integer>> Q = new LinkedList<Entry<E,Integer>>();
		// Mappa delle distanze
		Map<E,Integer> distance = new HashMap<E,Integer>();
		// Aggiungo il nodo iniziale
		Q.add(new SimpleEntry<E,Integer>(from,new Integer(0)));
		// Finchè ho nodi da analizzare
		while(!Q.isEmpty())
		{
			Entry<E,Integer> att = Q.poll();
			System.out.println("Dentro a " + att.getKey() + " " + att.getValue());
			// Se sono già entrato in att lo salto
			if(distance.containsKey(att.getKey())) continue ;
			// Inserisco la distanza, la prima che trovo
			distance.put(att.getKey(), att.getValue());
			// Aggiungo alla coda tutti i nodi raggiungibili da att
			for(E i : edge.get(att.getKey()))
				Q.add(new SimpleEntry<E,Integer>(i, new Integer(att.getValue().intValue()+1)));
		}
		return distance ;
	}

	public boolean connected()
	{
		boolean ret = true ;
		int size = node.size();
		for(E n : node)
		{
			Map<E,Integer> tempDist = getAllDistance(n);
			if(tempDist.size() != size)
				ret = false ;
		}
		return ret ;
	}
}
