import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.mysql.fabric.xmlrpc.base.Array;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

class Pair implements Comparable<Pair>
{
	Integer id;
	Integer val;

	public int compareTo(Pair a) {
		// TODO Auto-generated method stub
		return this.val.compareTo(a.val);
	}
	public String toString()
	{
		String s = "("+id+","+val+")";
		return s;
	}
}

public class SearchResults 
{
	ConcurrentHashMap<Integer,Integer> scoresForAllSongs;
	ConcurrentHashMap<Integer,Integer> matchCountForAllSongs;
	int ACTUAL_DB_SIZE=0;
	SearchResults(ConcurrentHashMap<Integer,Integer> scoresForAllSongs, ConcurrentHashMap<Integer,Integer> matchCountForAllSongs,int size)
	{
		this.scoresForAllSongs = scoresForAllSongs;
		this.matchCountForAllSongs = matchCountForAllSongs;
		ACTUAL_DB_SIZE=size;
		sortAndDisplayResult();
	}
	void sortAndDisplayResult()
	{
		Pair[] editPair = new Pair[ACTUAL_DB_SIZE];
		Pair[] matchPair = new Pair[ACTUAL_DB_SIZE];

		Integer[] key = new Integer[ACTUAL_DB_SIZE];
		key =  scoresForAllSongs.keySet().toArray((key));
		for (int i = 0; i < ACTUAL_DB_SIZE; i++) {
			editPair[i] = new Pair();
			editPair[i].id = key[i];
			editPair[i].val = scoresForAllSongs.get(key[i]);		
		}
		Arrays.sort(editPair);
		key =  matchCountForAllSongs.keySet().toArray((key));
		for (int i = 0; i < ACTUAL_DB_SIZE; i++) {
			matchPair[i] = new Pair();
			matchPair[i].id = key[i];
			matchPair[i].val = matchCountForAllSongs.get(key[i]);
		}
		Arrays.sort(matchPair);

		/*System.out.println("\n");
		System.out.println("editPair : ");
		for (int i = 0; i < ACTUAL_DB_SIZE; i++) {
			System.out.print(editPair[i]+"  ");
		}
		System.out.println("\nmatchPair : ");
		for (int i = 0; i < ACTUAL_DB_SIZE; i++) {
			System.out.print(matchPair[i]+"  ");
		}
		System.out.println("\n");*/

		Vector<Pair> topEditPair = new Vector<Pair>();
		Vector<Pair> topMatchPair = new Vector<Pair>();

		select(editPair,topEditPair);
		select(matchPair,topMatchPair);

		/*System.out.println("top edit pair");
		for (int i = 0; i < topEditPair.size(); i++) 
		{
			System.out.print(topEditPair.get(i)+"  ");
		}
		System.out.println("top match pair");
		for (int i = 0; i < topMatchPair.size(); i++) 
		{
			System.out.print(topMatchPair.get(i)+"  ");
		}*/

		Vector<Integer> searchList = new Vector<Integer>();
		
		makeFinalOrdering(topEditPair, topMatchPair, searchList);
		
		//System.out.println("searchList is :"+searchList);
		
		try {
			
				fetchResult(searchList);
		}
		catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	/* int calculateconfidence(int sc,int cnt)
	{
		double confidence=30;
		//System.out.println("sc "+sc);
		if(sc<mymain.noteLength)
		{
			confidence+=((double)sc/mymain.noteLength*25);
			confidence+=((double)cnt/mymain.noteLength*10);
		}
		else
			confidence=75;
		confidence+=(sc*2);
		if(confidence > 100) confidence=90;
		if(mymain.noteLength<6)
			return (int)(confidence/3);
		return (int)confidence;
	}
	*/
	
	void fetchResult(Vector<Integer> searchList) throws SQLException, ClassNotFoundException
	{
		/*BufferedWriter br; 
		try {
			br = new BufferedWriter(new FileWriter("..\\Cluster_DP_output.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		Class.forName("com.mysql.jdbc.Driver");
		String url = "jdbc:mysql://localhost:3306/project";
		String pwd = "abcde";
		String name = "dbuser";
		Connection conn = (Connection) DriverManager.getConnection(url, name, pwd);
		//System.out.println("got conn");
		Statement stmt = (Statement) conn.createStatement();
		String orderedSongList[] = new String[searchList.size()];
		int orderedSongconfidence[] = new int[searchList.size()];
		
		//System.out.println("\nRanked list :\n");
		String query="";
		int i;
		int currmax=Integer.MIN_VALUE;
		for ( i=0;i<searchList.size();i++) 
		{
			query=query+searchList.get(i)+" ; ";
			ResultSet rs = stmt.executeQuery("select name from songlist where id = "+query);
			rs.first();
			Object dbtime;
			dbtime=rs.getString(1);
			System.out.println(dbtime.toString() +"score -->"+scoresForAllSongs.get(searchList.get(i)));
			if(currmax<scoresForAllSongs.get(searchList.get(i)))
			{
				currmax=scoresForAllSongs.get(searchList.get(i));
			}
			orderedSongList[i] = dbtime.toString();
			/* orderedSongconfidence[i]=calculateconfidence(scoresForAllSongs.get(searchList.get(i)),matchCountForAllSongs.get(searchList.get(i)));
			System.out.println(orderedSongconfidence[i]); */
			query="";
		}
		NoteAssignment.maxvec.add(currmax);
		System.out.println();
		conn.close();
	}
	
	void select(Pair[] arr, Vector<Pair> top)
	{
		int cnt = 0;
		for(int i=arr.length-1; i>=0; i--)
		{
			if(cnt < 5)
			{
				top.add(arr[i]);
				cnt++;
			}
			else
			{
				while(i>0 && arr[i-1].val == arr[i].val)
				{
					top.add(arr[i]);
					i--;
					cnt++;
				}
				top.add(arr[i]);
				break;
			}

		}

	}

	void makeFinalOrdering(Vector<Pair>topEditPair, Vector<Pair>topMatchPair, Vector<Integer>searchList)
	{
		
		boolean editMark[] = new boolean[topEditPair.size()];
		boolean matchMark[] = new boolean[topMatchPair.size()];

		for(int i=0;i<topEditPair.size();i++)
		{
			for(int j=0;j<topMatchPair.size();j++) 
			{
				if(topEditPair.get(i).id == topMatchPair.get(j).id)
				{
					searchList.add(topEditPair.get(i).id);
					editMark[i]=true;
					matchMark[j]=true;
					break;
				}
			}
		}
		for(int i=0;i<topEditPair.size();i++)
		{
			if(!editMark[i])
			{
				searchList.add(topEditPair.get(i).id);
			}
		}
		for(int i=0;i<topMatchPair.size();i++)
		{
			if(!matchMark[i])
			{
				searchList.add(topMatchPair.get(i).id);
			}
		}
	}




}
