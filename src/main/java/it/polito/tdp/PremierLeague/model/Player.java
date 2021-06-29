package it.polito.tdp.PremierLeague.model;

import java.util.LinkedList;
import java.util.List;

public class Player {
	Integer playerID ;
	String name ;
	Integer battuti ;
	double pesoSconfitti ;
	Integer pesoTitolarita;
	List<Player> sconfitti ;
	
	public Player(Integer playerID, String name) {
		super();
		this.playerID = playerID ;
		this.name = name ;
		this.battuti = 0 ;
		pesoSconfitti = 0;
		pesoTitolarita = 0;
		sconfitti = new LinkedList<>() ;
	}
	
	public List<Player> getSconfitti() {
		return sconfitti;
	}
	public void setSconfitti(Player sconfitti) {
		this.sconfitti.add(sconfitti) ;
	}
	public Integer getPlayerID() {
		return playerID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getBattuti() {
		return battuti;
	}

	public void setBattuti(Integer battuti) {
		this.battuti = battuti;
	}

	public double getPesoSconfitti() {
		return pesoSconfitti;
	}

	public void setPesoSconfitti(double pesoSconfitti) {
		this.pesoSconfitti = pesoSconfitti;
	}

	public Integer getPesoTitolarita() {
		return pesoTitolarita;
	}

	public void setPesoTitolarita(Integer pesoTitolarita) {
		this.pesoTitolarita = pesoTitolarita;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((playerID == null) ? 0 : playerID.hashCode());
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
		Player other = (Player) obj;
		if (playerID == null) {
			if (other.playerID != null)
				return false;
		} else if (!playerID.equals(other.playerID))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return playerID + " - " + name + " - " + pesoTitolarita;
	}
	
}
