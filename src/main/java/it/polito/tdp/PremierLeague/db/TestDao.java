package it.polito.tdp.PremierLeague.db;

public class TestDao {

	public static void main(String[] args) {
		TestDao testDao = new TestDao();
		testDao.run();
	}
	
	public void run() {
		PremierLeagueDAO dao = new PremierLeagueDAO();
		System.out.println("Players:");
		System.out.println(dao.listAllPlayerswGoal(0.5));
		int i = dao.checkArco(dao.listAllPlayerswGoal(0.5).get(5), dao.listAllPlayerswGoal(0.5).get(7)) ;
		System.out.println(i);
		//System.out.println("Actions:");
		//System.out.println(dao.listAllActions());
	}

}
