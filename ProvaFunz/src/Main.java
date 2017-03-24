import java.util.ArrayList;
import java.util.List;

public class Main {

	public Main()
	{
		List<Float> ls = new ArrayList<Float>();
		ls.add(1.f);
		ls.add(2.f);
		ls.add(2.f);
		ls.add(3.f);

		List<Float> ls2 = Main.removeHigher(ls, 2.f);
		for(Float f : ls2 )
			System.out.println(f);
		
	}
	
	public static void main(String[] args) {
		
		Main prova = new Main();
		
		
	}

	public static <E extends Comparable<E> >  List<E> removeHigher(List<E> lst, E low)
	{
		List<E> tmp = new ArrayList<E>();
		for(int i = 0 ; i < lst.size(); i++)
		{
			System.out.println("REMOVE HIGHER["+lst.get(i)+","+low+"] CMP = "+lst.get(i).compareTo(low));
			if( lst.get(i).compareTo(low) <= 0 )
				tmp.add(lst.get(i));
		}
		return tmp ;
	}
}
