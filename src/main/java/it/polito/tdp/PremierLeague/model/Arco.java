package it.polito.tdp.PremierLeague.model;

public class Arco {
	
	int player1ID ;
	int player2ID ;
	int peso ;
	
	public Arco(int player1id, int player2id, int peso) {
		player1ID = player1id;
		player2ID = player2id;
		this.peso = peso;
	}

	public int getPlayer1ID() {
		return player1ID;
	}

	public int getPlayer2ID() {
		return player2ID;
	}

	public int getPeso() {
		return peso;
	}

}
