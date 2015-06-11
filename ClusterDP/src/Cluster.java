import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Vector;

class  Tclass
{
	Vector<Integer> val = new Vector<Integer>();
}
class score implements Comparable<score>
{
	Integer k;
	Double sc;
	score(int k, double sc)
	{
		this.k=k;
		this.sc=sc;
	}
	score(score s)
	{
		this.k = s.k;
		this.sc = s.sc;
	}
	
	public String toString()
	{
		String s = "("+k+","+sc+")";
		return s;
	}
	public int compareTo(score a) {
		// TODO Auto-generated method stub
		return this.sc.compareTo(a.sc);
	}
}

public class Cluster {

	//double p[] = {11,12,13,13, 40, 67,68,68,69,69,  80, 111,112,113,114,115,  1000,1001,1002,1003 };
	//score[] scArray = new score[1000];
	Vector<score> scArray = new Vector<score>();
	Vector<Double> p = new Vector<Double>();
	Vector<Double> origP = new Vector<Double>();
	//double p[] = {11,67,111,1000};
	double F[][][]; 
	Tclass T[][][]; 
	
	void readFile()
	{
		String str1;
		double d1;
		BufferedReader br1 = null;
		try {
			br1 = new BufferedReader(new FileReader("C:\\Users\\tanmay\\Desktop\\pitches_clusterDP.txt"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try 
		{
			while( (str1 = br1.readLine()) != null)
			{
				d1=Double.parseDouble(str1);
				p.add(d1);	
				origP.add(d1);
			}
		} 
		catch (NumberFormatException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		T = new Tclass[p.size()+10][p.size()+10][36];
		F = new double[p.size()+10][p.size()+10][36];
		Collections.sort(p);
	}
	
	double evaluate(int start, int end) 
	{
		double product = 1.0, err = 0, gm;

		if(start!=end)
		{
			for(int i=start; i<=end; i++)
			{
				product *= p.get(i);
			}
			gm = Math.pow(product, 1.0/(end-start+1));   
			for (int i = start; i <= end; i++) {
				if(gm >p.get(i))
					err += Math.pow(Math.abs((gm/p.get(i))),2);
				else
					err += Math.pow(Math.abs((p.get(i)/gm)),2);
			}
			//System.out.println("err "+err);
			return err;
		}
		else
		{
			return 1;
		}

	}

	double fun(int i,int j,int k)
	{
		T[i][j][k] = new Tclass();
		double min = Double.MAX_VALUE, t1, t2;

		if((j-i+1) >= k)
		{

			double temp;
			if(k == 1)
			{
				if(F[i][j][k] == 0)
				{
					temp = evaluate(i,j);
					F[i][j][k] = temp;
				}
				//T[i][j][k].val.add(-1);
				return F[i][j][k];
			}

			int minT = 0;
			for (int t = i; t < j; t++) 
			{
				if(F[i][t][1] == 0)
					F[i][t][1] = fun(i,t,1);
				if(F[t+1][j][k-1] == 0)
					F[t+1][j][k-1] = fun(t+1,j,k-1);
				double currSc = F[i][t][1] + F[t+1][j][k-1];

				//System.out.println("currSc"+currSc);
				if(currSc < min)
				{
					min = currSc;
					minT = t;

				}
			} // end of for t
			T[i][j][k].val.add(minT);

			for(int x123=0;x123<T[minT+1][j][k-1].val.size();x123++)
			{
				int temp1 = T[minT+1][j][k-1].val.get(x123);
				T[i][j][k].val.add(temp1);
			}

		} // end of if
		F[i][j][k] = min;

		return min;
	}

	void start()
	{
		
		Scanner in = new Scanner(System.in);
		double sc;
		double min = Double.MAX_VALUE;
		int minI = 0;
		readFile();
		for (int i = 1; i <= 15; i++) 
		{ 
			System.out.println("k="+i);
			sc = fun(0,p.size()-1, i);
			//int isc=(int)(sc+0.5);
			double isc = sc;
			System.out.println("intsc="+isc);
			for (int j = 0; j < T[0][p.size()-1][i].val.size(); j++) 
			{
				System.out.print(T[0][p.size()-1][i].val.get(j)+"  ");
			}
			System.out.println();
			if(isc < min) 
			{
				//System.out.println("min updated");
				min = isc;
				minI = i;
			}
			//in.nextInt();
			scArray.add(new score(i,isc));			
		} 

		System.out.println(min);
		System.out.println("no. clusters are  "+minI);
		sort(scArray, min, minI);
		System.out.println("Enter no. of clusters ");
		int bestK=in.nextInt();
		NoteAssignment nA = new NoteAssignment(p, origP, T[0][p.size()-1][bestK].val , bestK);
		
		
		/*
		System.out.println("F matrix ");
		for (int i = 0; i < p.size(); i++) {
			for (int j = 0; j < p.size(); j++) {
				for (int k = 1; k < 4; k++) {
					System.out.print(F[i][j][k] +" ");
				}
				System.out.print("		");
			}
			System.out.println();
		}
		*/


		
		
	}
	void sort(Vector<score> scArray, double min, int mink)
	{	
		System.out.println(min+"  "+mink);
		Vector<score> higher = new Vector<score>();                     
		for (int i = 0; i <  scArray.size(); i++) 
		{
			System.out.println("here "+i);
			if(scArray.get(i).sc>min)
			{
				higher.add(new score(scArray.get(i)));
			}
		}
		Collections.sort(higher);
		
		for (int i = 0; i < higher.size(); i++) {
			System.out.println(higher.get(i));
		}
		
		
	}
	
	
}
