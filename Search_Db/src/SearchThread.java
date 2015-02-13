import java.util.Arrays;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class SearchThread implements Runnable 
{
	Thread t;
	Vector<Integer> q;
	ConcurrentHashMap<Integer,Integer> finalscore  = new ConcurrentHashMap<Integer,Integer>();
	ConcurrentHashMap<Integer,Double> avgScore  = new ConcurrentHashMap<Integer,Double>();
	Vector<Vector<Integer>> db;
	Vector<Integer> matchCnt;
	Vector<Integer> editSc;
	int id;
	int[][] F;
	int [][] FforScore;
	int i,j;
	int match, delete, insert, d;
	int avg = 0,cnt=0;
	int maxscore=Integer.MIN_VALUE;
	SearchThread(int id,Vector<Integer> q, Vector<Vector<Integer>> db,ConcurrentHashMap finalscore,ConcurrentHashMap avgScore,Vector<Integer> editSc,Vector<Integer> matchCnt)
	{
		this.id=id;
		this.db=db;
		this.q=q;
		this.finalscore=finalscore;
		this.avgScore=avgScore;
		this.editSc=editSc;
		this.matchCnt=matchCnt;
		F = new int[100][100];
		FforScore = new int[100][100];
		t = new Thread(this);
		t.start();
	}
	int delSc()
	{
		return -1;
	}
	int simSc(int a, int b)
	{
		if(a == b)
		{
			return 1;
		}
		return -1;
	}


	int align(int F[][],Vector<Integer> q, Vector<Integer> db)
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
		//System.out.println(AlignmentB);
		//System.out.println(AlignmentA);
		String[] qarr = AlignmentA.split(" ");
		String[] dbarr =AlignmentB.split(" ");
		for (int j2 = 0; j2 < qarr.length; j2++) {
			if(qarr[j2].equals(dbarr[j2]) && (!qarr[j2].equals("-")))
			{
				match++;
			}
		}
		//System.out.println("match is ->"+match);
		return match;
	}

	public int needlemanWunsch(int F[][],Vector<Integer> curr,Vector<Integer> q, int flag) // flag = 0 the it is for score
	{
		d = delSc();

		for(int i=0;i<=q.size();i++)
		{
			F[i][0] = d * i;
		}
		for(int i=0;i<=curr.size();i++)
		{
			if(flag == 0) 
				F[0][i] = 0; // score only
			else
				F[0][i] = d * i; 
		}
		for(i=1;i<=q.size();i++)
		{
			for(j=1;j<=curr.size();j++)
			{
				match = F[i-1][j-1] + simSc(q.get(i-1),curr.get(j-1));
				delete = F[i-1][j] + d;
				insert = F[i][j-1] + d;
				F[i][j] = Math.max(delete, Math.max(match,insert));                    
			}
		}


		int max=0;


		if(flag == 0) // this is for no end gap penalty
		{
			max = F[q.size()][0];
			for (int i = 0; i <= curr.size(); i++) 
			{
				if(F[q.size()][i] > max)
					max = F[q.size()][i];
			}
			editSc.add(max);
		}
		else 
		{
			max=F[q.size()][curr.size()];
			// calling the align function 
			matchCnt.add(align(F,q,curr));
		}	

		/*synchronized(lock)
		{
			System.out.println("\n for thread "+id);
			for(i=0;i<=q.size();i++)
	        {
	            for(j=0;j<=db.size();j++)
	            {
	                   System.out.print(F[i][j]+"  ");
	            }
	            System.out.println();
	        }
		}*/
		return max;
	}

	public void run() 
	{
		Object lock = null;
		lock = new Object();
		int max=0;
		// with gap penalty
		//System.out.println("db is "+db);
		synchronized(lock)
		{
			for (int ver = 1; ver < db.size(); ver++) 
			{

				Vector<Integer> curr = new Vector<Integer>();
				curr=(Vector)db.get(ver-1).clone();

				needlemanWunsch(FforScore,curr,q,0);
				needlemanWunsch(F,curr,q,1);

				//System.out.println("Score of song# "+id+" ver# "+ver+"  ="+maxscore);
				
			} // end of for ver loop

			//System.out.println(id+"   :  "+maxscore);
			
			
			System.out.println("details of #"+id);
			System.out.println(editSc+ "\n\n"+ matchCnt);
			
			
			
			int maxEditSc=Integer.MIN_VALUE, maxMatchCnt=Integer.MIN_VALUE, temp = 0;
			for(i=0;i<editSc.size();i++)
			{
				if(maxEditSc < editSc.get(i))
				{
					maxEditSc = editSc.get(i);
					temp = matchCnt.get(i);
				}
				if(maxMatchCnt < matchCnt.get(i))
					maxMatchCnt = matchCnt.get(i);
			}
			
			System.out.println("song id #"+id+" maxEditSc = "+maxEditSc+" maxMatchCnt = "+maxMatchCnt+" temp = "+temp);
			System.out.println("\n\n");

		}
	}
}
