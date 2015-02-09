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
	int id;
	int[][] F;
	int i,j;
	int match, delete, insert, d;
	int avg = 0,cnt=0;
	int maxscore=Integer.MIN_VALUE;
	SearchThread(int id,Vector<Integer> q, Vector<Vector<Integer>> db,ConcurrentHashMap finalscore,ConcurrentHashMap avgScore)
	{
		this.id=id;
		this.db=db;
		this.q=q;
		this.finalscore=finalscore;
		this.avgScore=avgScore;
		F = new int[100][100];
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
			return 1;
		return -1;
	}
	public void run() 
	{
		Object lock = null;
		lock = new Object();
		// with gap penalty
		//System.out.println("db is "+db);
		synchronized(lock)
		{
		for (int ver = 1; ver < db.size(); ver++) 
		{
		
			Vector<Integer> curr = new Vector<Integer>();
			curr=(Vector)db.get(ver-1).clone();
			//System.out.println("curr is " + curr);
			d = delSc();
			for(int i=0;i<=q.size();i++)
			{
				F[i][0] = d * i;
			}
			for(int i=0;i<=curr.size();i++)
			{
				F[0][i] = d * i; // here
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
			int max = F[q.size()][0];
			for (int i = 0; i <= curr.size(); i++) 
			{
				if(F[q.size()][i] > max)
					max = F[q.size()][i];
			}
			
				max=F[q.size()][curr.size()];
				//System.out.println("Score of song# "+id+" ver# "+ver+"  ="+maxscore);
				if(maxscore<max)
				{
					
					maxscore=max;
				}
				avg+=max;
				cnt++;
		}
			//System.out.println(id+"   :  "+maxscore);
			finalscore.put(id,maxscore);
			avgScore.put(id,((double)avg/cnt));
			System.out.println("score of #"+id+"  highest score "+maxscore+" avg is "+((double)avg/cnt));
			
		}
	}
}
