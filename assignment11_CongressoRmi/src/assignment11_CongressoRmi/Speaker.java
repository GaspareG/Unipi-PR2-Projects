package assignment11_CongressoRmi;

import java.io.Serializable;

// Speaker, immutabile, niente lock quindi
public class Speaker implements Serializable {
	private String name;
	private String surname;
	
	private static final long serialVersionUID = 1L;
	
	public Speaker(String n, String s)
	{
		this.name = n;
		this.surname = s;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getSurname()
	{
		return this.surname;
	}
	
	public String toString(){
		StringBuilder sb= new StringBuilder();
		sb.append("Speaker[name=");
		sb.append(this.getName());
		sb.append(",surname=");
		sb.append(this.getSurname());
		sb.append("]");
		return sb.toString();
	}
	
	public Speaker copy()
	{
		return new Speaker(this.getName(),this.getSurname());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((surname == null) ? 0 : surname.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Speaker other = (Speaker) obj;
		if (name == null) {
			if (other.getName() != null)
				return false;
		} else if (!name.equals(other.getName()))
			return false;
		if (surname == null) {
			if (other.getSurname() != null)
				return false;
		} else if (!surname.equals(other.getSurname()))
			return false;
		return true;
	}
	
	
}
