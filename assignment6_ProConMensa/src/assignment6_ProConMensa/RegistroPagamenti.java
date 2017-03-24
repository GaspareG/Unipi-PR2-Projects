package assignment6_ProConMensa;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class RegistroPagamenti {

	private BufferedReader buff ;
	private Map<String,Integer> registro ;
	
	public RegistroPagamenti(String string) throws IOException {
		this.buff = new BufferedReader(new InputStreamReader(new FileInputStream(string),"UTF-8"));
		this.registro = new HashMap<String,Integer>();
		
		String line;
		while ((line = buff.readLine()) != null) {
			String part[] = line.split(",");
			if(part.length == 5)
			{
				String nome = part[1] ;
				String cognome = part[2] ;
				Integer piatti = new Integer(part[4]) ;
				String nomecognome = nome + " " + cognome ;
				if(!registro.containsKey(nomecognome)) registro.put(nomecognome, new Integer(0));
				
				registro.put(nomecognome, registro.get(nomecognome)+piatti);
				
			}
		}
		
	}

	public void stampaPagamenti() {
		System.out.println("[REGISTRO PAGAMENTI]");
		Set<Entry<String, Integer>> et = registro.entrySet() ;
		for(Entry<String, Integer> i : et)
		{
			System.out.println("Lavoratore ["+i.getKey()+"] Pagamento["+i.getValue().intValue()+"$]");
		}
	}

}
