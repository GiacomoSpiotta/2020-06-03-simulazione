package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Arco;
import it.polito.tdp.PremierLeague.model.Player;

public class PremierLeagueDAO {
	
	public List<Player> listAllPlayers(){
		String sql = "SELECT * FROM Players";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				result.add(player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Player> listAllPlayerswGoal(double minGoal){
		String sql = "SELECT actions.PlayerID, AVG(actions.Goals)*100000 AS goal "
				+ "FROM actions "
				+ "GROUP BY actions.PlayerID";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				int goal = res.getInt("goal") ;
				int minIntGoal = (int)(minGoal*100000);
				
				if(goal > minIntGoal) {
					for(Player p : this.listAllPlayers()) {
						if(p.getPlayerID() == res.getInt("PlayerID")) {
							Player player = p ;
							result.add(player);
						}
					}
				}
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public int checkArco(Player p1, Player p2) {
		String sql = "SELECT a1.TimePlayed AS TimePlayed1, a2.TimePlayed AS TimePlayed2 "
				+ "FROM actions a1, actions a2 "
				+ "WHERE a1.PlayerID = ? AND a2.PlayerID = ? AND "
				+ "		a1.MatchID = a2.MatchID AND a1.`Starts` = 1 AND "
				+ "		a2.`Starts` = 1";
		int result = 0 ;
		int time1 = 0 ;
		int time2 = 0 ;
		Connection conn = DBConnect.getConnection();
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, p1.getPlayerID());
			st.setInt(2, p2.getPlayerID());
			ResultSet res = st.executeQuery();
			while (res.next()) {
				time1 += res.getInt("TimePlayed1") ;
				time2 += res.getInt("TimePlayed2") ;
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return 0;
		}
		result = time1-time2 ;
		return result ;
	}
	
	public List<Arco> listAllArchi(){
		String sql = "SELECT a1.PlayerID as PlayerId1, a2.PlayerID as PlayerId2, SUM(a1.TimePlayed - a2.TimePlayed) AS result "
				+ "FROM actions a1, actions a2 "
				+ "WHERE a1.MatchID = a2.MatchID AND a1.`Starts` = 1 AND "
				+ "		a2.`Starts` = 1 AND a1.PlayerID != a2.PlayerID AND a1.TeamID != a2.TeamID "
				+ "GROUP BY a1.PlayerID, a2.PlayerID ";
		List<Arco> result = new ArrayList<Arco>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				if(res.getInt("result") != 0) {
					Arco a = new Arco(res.getInt("PlayerId1"), res.getInt("PlayerId2"), res.getInt("result")) ;
					result.add(a) ;
				}
				
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
