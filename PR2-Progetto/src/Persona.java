/* Gaspare Ferraro - matricola 520549
 * 
 *  Overview: Tipo non modificabile che rappresenta una persona
 *  
 *  AF = Nome Cognome Data
 *  IR = Nome != null && Cognome != null && Data != null
 */

public class Persona {

	private String nome;
	private String cognome;
	private String data;
	public Persona(String nome, String cognome, String data) {
		if(nome == null || cognome == null || data == null)
			throw new NullPointerException();
		this.nome = nome;
		this.cognome = cognome;
		this.data = data;
	}
	
	public String getNome() {
		return nome;
	}
	public String getCognome() {
		return cognome;
	}
	public String getData() {
		return data;
	}

	@Override
	public String toString() {
		return "Persona [nome=" + nome + ", cognome=" + cognome + ", data=" + data + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Persona other = (Persona) obj;
		if (cognome == null) {
			if (other.cognome != null)
				return false;
		} else if (!cognome.equals(other.cognome))
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

}
