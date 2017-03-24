
public class Item {
	private String nome ;
	private Float prezzo;
	private String negozio;
	public Item(String nome, Float prezzo, String negozio) {
		super();
		this.nome = nome;
		this.prezzo = prezzo;
		this.negozio = negozio;
	}
	public String getNome() {
		return nome;
	}
	public Float getPrezzo() {
		return prezzo;
	}
	public String getNegozio() {
		return negozio;
	}
	
}
