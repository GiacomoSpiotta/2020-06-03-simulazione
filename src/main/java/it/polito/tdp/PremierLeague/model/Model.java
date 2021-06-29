package it.polito.tdp.PremierLeague.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private PremierLeagueDAO dao ;
	private SimpleDirectedWeightedGraph<Player, DefaultWeightedEdge> grafo;
	private Player topPlayer;
	private List<Player> topPlayerTeam;
	private int battuti ;
	private List<Player> playerSconfitti;
	
	public Model(){
		dao = new PremierLeagueDAO() ;
		battuti = 0;
		playerSconfitti = new LinkedList<Player>() ;
	}
	
	public void creaGrafo(double minGoal) {
		grafo = new SimpleDirectedWeightedGraph<Player, DefaultWeightedEdge>(DefaultWeightedEdge.class) ;
		
		//Aggiungo i vertici
		Graphs.addAllVertices(this.grafo, dao.listAllPlayerswGoal(minGoal)) ;
		//int result = dao.checkArco(dao.listAllPlayerswGoal(minGoal).get(5), dao.listAllPlayerswGoal(minGoal).get(7)) ;
		//Graphs.addEdgeWithVertices(this.grafo, dao.listAllPlayerswGoal(minGoal).get(5), dao.listAllPlayerswGoal(minGoal).get(7), result) ;
		//Aggiungo archi
		for(Arco a : dao.listAllArchi()) {
				if(a.player1ID != a.player2ID && this.getPlayer(a.player1ID) != null && this.getPlayer(a.player2ID) != null) {
					if(a.peso > 0) {
						Graphs.addEdgeWithVertices(this.grafo, this.getPlayer(a.player1ID), this.getPlayer(a.player2ID), a.getPeso()) ;
					}else {
						Graphs.addEdgeWithVertices(this.grafo, this.getPlayer(a.player2ID), this.getPlayer(a.player1ID), -a.getPeso()) ;
					}
				}
		}
	}
	
	public int getNVertici() {
		if(this.grafo != null) {
			return this.grafo.vertexSet().size() ;
		}
		return 0 ;
	}
	
	public int getNArchi() {
		if(grafo != null)
			return grafo.edgeSet().size() ;
		
		return 0;
	}
	
	public Set<Player> getVertici(){
		return this.grafo.vertexSet() ;
	}
	
	public Player getPlayer(int ID) {
		Player player = null;
		for(Player p : this.getVertici()) {
			if(p.getPlayerID() == ID) {
				player = p ;
			}
		}
		return player ;
	}
	
	public void calcolaTopPlayer() {
		for(Player p : this.getVertici()) {
			p.setBattuti(this.grafo.outgoingEdgesOf(p).size());
			if(p.getBattuti() > this.battuti) {
				battuti = p.getBattuti() ;
				topPlayer = p ;
			}
		}
		List<DefaultWeightedEdge> archiUscenti = new LinkedList<>(this.grafo.outgoingEdgesOf(topPlayer)) ;
		for(DefaultWeightedEdge dwe : archiUscenti) {
			Graphs.getOppositeVertex(this.grafo, dwe, topPlayer).setPesoSconfitti(this.grafo.getEdgeWeight(dwe));
			playerSconfitti.add(Graphs.getOppositeVertex(this.grafo, dwe, topPlayer)) ;
		}
		Collections.sort(this.playerSconfitti, new ComparatorePlayer());
	}

	public Player getTopPlayer() {
		return topPlayer;
	}
	public int getBattuti() {
		return battuti;
	}
	public List<Player> getPlayerSconfitti() {
		return playerSconfitti;
	}
	class ComparatorePlayer implements Comparator<Player> {
		
		@Override 
		public int compare(Player a1, Player a2) {
			return (int)(a2.getPesoSconfitti()-a1.getPesoSconfitti());
		}
	}
	
	
	public void getPesoTitolarita() {
		for(Player p : this.getVertici()) {
			int peso = 0 ;
			for(DefaultWeightedEdge dwe : this.grafo.outgoingEdgesOf(p)) {
				peso += this.grafo.getEdgeWeight(dwe) ;
				p.setSconfitti(Graphs.getOppositeVertex(this.grafo, dwe, p));
			}
			for(DefaultWeightedEdge dwe : this.grafo.incomingEdgesOf(p)) {
				peso -= this.grafo.getEdgeWeight(dwe) ;
			}
			p.setPesoTitolarita(peso);
		}
	}
	
	public void cerca(int numeroTopPlayer) {
		this.getPesoTitolarita();
		topPlayerTeam = new LinkedList<Player>() ;
		List<Player> parziale = new LinkedList<Player>() ;
		List<Player> tutti = new LinkedList<Player>(this.getVertici());
		this.cercaTopPlayer(numeroTopPlayer, parziale, tutti) ;
	}

	private void cercaTopPlayer(int numeroTopPlayer, List<Player> parziale, List<Player> tutti) {
		//Condizione Finale
		if(parziale.size() == numeroTopPlayer) {
			if(this.getPesoTotale(parziale) > this.getPesoTotale(topPlayerTeam)) {
				topPlayerTeam = new LinkedList<Player>(parziale) ;
			}
			return ;
		}
		
		/*List<Player> banditi = new LinkedList<>() ;
		for(Player p : parziale) {
			for(Player pp : p.getSconfitti()) {
				banditi.add(pp) ;
			}
		}*/
		
		for(Player p : tutti) {
			if(!parziale.contains(p)) {
				tutti.remove(p) ;
				parziale.add(p) ;
				this.cercaTopPlayer(numeroTopPlayer, parziale, tutti);
				parziale.remove(p) ;
			}
		}
		
		
	}
	
	public int getPesoTotale(List<Player> parziale) {
		int peso = 0 ;
		if(parziale.size() != 0) {
			for(Player p : parziale) {
				peso += p.getPesoTitolarita() ;
			}
		}
		return peso ;
	}

	public List<Player> getTopPlayerTeam() {
		return topPlayerTeam;
	}
}
