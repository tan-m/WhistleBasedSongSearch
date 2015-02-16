/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.mysql.jdbc.Statement;
/**
 *
 * @author vaibhav
 */
public class Search {

	
	// match = 1, mismatch = -1, indel = -1
	
	private java.sql.Connection conn = null;
	String pwd = "abcde";
	String name = "dbuser";
	ResultSet rs;
	Statement stmt = null;	
	void connecttodb() throws Exception 
	{
		
		Class.forName("com.mysql.jdbc.Driver");
		String url = "jdbc:mysql://localhost:3306/project";
		conn = DriverManager.getConnection(url, name, pwd);
		System.out.println("got conn");
		stmt = (Statement) conn.createStatement();
		rs = stmt.executeQuery("select * from note");
		System.out.println(" curr size of "+rs.getFetchSize());
		//conn.close();
		//rs.get
		//rs.first();
		//Object temp=rs.getString(3);
		//System.out.println(rs.getString(1));
		//System.out.println("temp is "+temp.toString());
	
	}
	
	
	int MAX_DB_SIZE = 100;
	
	public void startSearch(Vector<Integer> userQ) throws SQLException 
	{
		SearchThread[] st = new SearchThread[MAX_DB_SIZE];
		//Search search = new Search();
		  
		Vector<Integer> val = new Vector<Integer>();
		ConcurrentHashMap<Integer,Integer> scoresForAllSongs = new ConcurrentHashMap<Integer,Integer>();
		ConcurrentHashMap<Integer,Integer> matchCountForAllSongs = new ConcurrentHashMap<Integer,Integer>();
		
		Vector<Vector<Integer>> song = new Vector<Vector<Integer>>();
		Object dbtime;
		int c=0;
		int curid=-1;
		int previd=1;
		int threadCnt=0;
		try 
		{
			String strLine;
			String temp;
			
			while(rs.next())
			{
					dbtime=rs.getString(1);
					temp=(String)dbtime;
					curid=Integer.parseInt(temp);
					//System.out.println("curr is"+curid);
					if(curid==previd)
					{
						
					}
					else
					{
						Vector<Integer> editSc = new Vector<Integer>();
						Vector<Integer> matchCnt = new Vector<Integer>();
						st[threadCnt++] = new SearchThread((previd),userQ,song, scoresForAllSongs, matchCountForAllSongs );
						song=null;
						song = new Vector<Vector<Integer>>(70);
						
					}
					dbtime=rs.getString(3);
					strLine =(String)dbtime; 
				String [] s = strLine.split(" ");
				for (int i = 0; i < s.length; i++) 
				{
					val.add(Integer.parseInt(s[i]));
				}
				//System.out.println("val is"+val);
				song.add(val);
				//System.out.println(++c + ")   " + score(userQ, val)+" "+val.get(0)+" "+val.get(1)+" "+val.get(2));
				//val.clear();
				val=new Vector<Integer>();
				previd=curid;
			}
			Vector<Integer> editSc = new Vector<Integer>();
			Vector<Integer> matchCnt = new Vector<Integer>();
			st[threadCnt++] = new SearchThread(curid,userQ,song, scoresForAllSongs, matchCountForAllSongs);
		} 
		catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int ACTUAL_DB_SIZE=threadCnt;
		System.out.println("main thread waiting for"+threadCnt);
		int i=0;	//----------here 1
		//System.out.println(ACTUAL_DB_SIZE);
		while(i<ACTUAL_DB_SIZE)
		{
		
			try 
			{
				st[i].t.join();
			} 
			catch (InterruptedException e) 
			{
				 //TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
		}
		System.out.println("main starts");
		System.out.println("scores For All Songs ---->\n\n ");
		System.out.println(scoresForAllSongs);
		System.out.println("MatchCnt For All Songs ---->\n\n ");
		System.out.println(matchCountForAllSongs);
		conn.close();
		SearchResults sr = new SearchResults(scoresForAllSongs,matchCountForAllSongs,ACTUAL_DB_SIZE); 
		
		
	}
} 