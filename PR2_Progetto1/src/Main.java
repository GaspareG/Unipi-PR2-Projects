import java.util.Map;

public class Main {

	public static void main(String[] args) {

		Persona p1 = new Persona("Paolino","Paperino");
		Persona p2 = new Persona("Topolino","Topolini");
		Persona p3 = new Persona("Gamba","DiLegno");
		Persona p4 = new Persona("Gastone","Paperone");
		Persona p5 = new Persona("Topolina","Topolini");
		Persona p6 = new Persona("Paperoga","Papero");
		
		MyGraph<Persona> facebook = new MyGraph<Persona>();
		
		facebook.addNode(p1);
		facebook.addNode(p2);
		facebook.addNode(p3);
		facebook.addNode(p4);
		facebook.addNode(p5);
		facebook.addNode(p6);

		facebook.addEdge(p1, p2);
		facebook.addEdge(p2, p3);
		facebook.addEdge(p3, p4);
		facebook.addEdge(p4, p5);
		facebook.addEdge(p5, p6);

		facebook.addEdge(p2, p4);
		facebook.addEdge(p2, p6);
		facebook.addEdge(p4, p6);

		facebook.addEdge(p1, p3);
		facebook.addEdge(p1, p5);
		facebook.addEdge(p3, p5);

		System.out.println(facebook.connected());

		Map<Persona,Integer> tempDist = facebook.getAllDistance(p1);
		for(Persona p : tempDist.keySet() )
		{
			System.out.println(p + " " + tempDist.get(p));
		}
		
		facebook.addNode(new Persona("a","b"));
		
		System.out.println(facebook.connected());
		
	}

}
