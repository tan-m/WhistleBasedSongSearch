/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package search;
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

	/**
	 * @param args the command line arguments
	 */
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
		//rs.get
		//rs.first();
		//Object temp=rs.getString(3);
		//System.out.println(rs.getString(1));
		//System.out.println("temp is "+temp.toString());
	
	}
	
	
	
	public final  int MAX_DB_SIZE=100;
	
	int[][] F = new int[100][100];
	int i,j;
	int delSc()
	{
		return -1;
	}
	
	void align(int F[][],Vector<Integer> q, Vector<Integer> db)
	{
		String AlignmentA = ""; // query
		String AlignmentB = ""; // db
		int i = q.size();
		int j = db.size();
		while (i >0 || j > 0)
		{
			if (i > 0 && j > 0 && F[i][j] == (F[i-1][j-1] + simSc(q.get(i-1),db.get(j-1))))
			{
				AlignmentA = q.get(i-1).toString()+" "  + AlignmentA;
				AlignmentB = db.get(j-1).toString()+" "  + AlignmentB;
				i = i-1;
				j = j-1;
			}
			else if (i > 0 && F[i][j] == (F[i-1][j] + delSc()))
			{
				AlignmentA = q.get(i-1).toString() +" "+ AlignmentA;
				AlignmentB = "-" +" " + AlignmentB;
				i= i-1;
			}
			else if (j > 0 && F[i][j] == F[i][j-1] + delSc())
			{
				AlignmentA 	= "-"+" "  + AlignmentA;
				AlignmentB 	= db.get(j-1).toString()+" "  + AlignmentB;
				j=j-1;
			}
		}
		int match=0;
		System.out.println("align :");
		System.out.println(AlignmentB);
		System.out.println(AlignmentA);
		String[] qarr = AlignmentA.split(" ");
		String[] dbarr =AlignmentB.split(" ");
		for (int j2 = 0; j2 < qarr.length; j2++) {
			if(qarr[j2].equals(dbarr[j2]) && (!qarr[j2].equals("-")))
			{
				match++;
			}
		}
		System.out.println("match is ->"+match);
	}
	
	int simSc(int a, int b)
	{
		
		if(a == b)
			return 1;
		//return (Math.abs(a-b)*-1);
		return -1;
	}
	// needleman wunch algo
	//       db
	//     -------------
	//     |
	//  q  |
	//     |
	int score(Vector<Integer> q, Vector<Integer> db)
	{
		int match, delete, insert, d;
		d = delSc();
		for(int i=0;i<=q.size();i++)
		{
			F[i][0] = d * i;
		}
		for(int i=0;i<=db.size();i++)
		{
			F[0][i] = d * i; //0;
		}
		for(i=1;i<=q.size();i++)
		{
			for(j=1;j<=db.size();j++)
			{
				match = F[i-1][j-1] + simSc(q.get(i-1),db.get(j-1));
				delete = F[i-1][j] + d;
				insert = F[i][j-1] + d;
				F[i][j] = Math.max(delete, Math.max(match,insert));                    
			}
		}
		System.out.println("\n");
		for(i=0;i<=q.size();i++)
        {
            for(j=0;j<=db.size();j++)
            {
                   System.out.print(F[i][j]+"  ");
            }
            System.out.println();
        }
		int max = F[q.size()][0];
		for (int i = 0; i <= db.size(); i++) 
		{
			if(F[q.size()][i] > max)
				max = F[q.size()][i];
		}
		align(F,q,db);
		
		//return max;
		return F[q.size()][db.size()];
	}
	
	public void startSearch(Vector<Integer> userQ) throws SQLException 
	{
		SearchThread[] st = new SearchThread[MAX_DB_SIZE];
		//Search search = new Search();
		ConcurrentHashMap<Integer,Integer> finalScore = new ConcurrentHashMap<Integer,Integer>(); 
		ConcurrentHashMap<Integer,Double> avgScore = new ConcurrentHashMap<Integer,Double>(); 
		Vector<Integer> val = new Vector<Integer>();
		//Vector<Integer>[] song = new Vector<Integer>[70];
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
			//BufferedReader br = new BufferedReader(new FileReader(new File("C:\\Users\\HP\\Desktop\\songsNoteShifted.txt")));
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
						st[threadCnt++] = new SearchThread((previd),userQ,song,finalScore,avgScore,editSc,matchCnt);
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
			st[threadCnt++] = new SearchThread(curid,userQ,song,finalScore,avgScore,editSc,matchCnt);
		} 
		catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int ACTUAL_DB_SIZE=threadCnt;
		int i=1;
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
		System.out.println("finalscore and avgscore is ---->\n\n ");
		//System.out.println("1) song name - SaReGaMaPa	   	"+finalScore.get(1) );
		//System.out.println("2) song name - Twinkle twinkle		"+finalScore.get(2) );
		/*for (int index = 1; index <=finalScore.size(); index++) 
		{
			System.out.println(finalScore.get(index) + "	"+	avgScore.get(index));
		}*/
		
	}
} 