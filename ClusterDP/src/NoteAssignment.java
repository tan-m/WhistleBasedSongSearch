import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;


class Bucket
{
	Vector<Double> elements = new Vector<Double>() ;
	double centroid;
	int note;
}

public class NoteAssignment {
	
	Vector<Double> sortedP,  p,modefromM;
	Vector<Integer> t;
	int k;
	int len=0;
	Vector<Bucket> map = new Vector<Bucket>(); 
	Vector<Integer> initialResult;
	public static Vector<Integer> maxvec=new Vector<Integer>() ;
	
	public NoteAssignment(Vector<Double> sortedP, Vector<Double> p, Vector<Integer> t, int k)
	{
		this.sortedP = sortedP;
		this.p = p;
		this.t = t;
		this.k = k;
		modefromM = new Vector<Double>();
		builtHashMap();
		
	}
	void readFile()
	{
		String str1;
		double d1;
		BufferedReader br1 = null;
		try {
			br1 = new BufferedReader(new FileReader("C:\\Users\\tanmay\\Desktop\\energy_clusterDP.txt"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try 
		{
			while( (str1 = br1.readLine()) != null)
			{
				d1=Double.parseDouble(str1);
				modefromM.add(d1);	
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
		System.out.println("modes from M");
		System.out.println(modefromM);
		System.out.println("size of mode is "+modefromM.size());
	}
	double getcentroid(Vector<Double> vec)  
	{
		double gm,product = 0;
		for (Double d1 : vec) 
		{
			product *= d1;
		}
		gm = Math.pow(product, 1.0/vec.size()); 
		return gm;
	}
	
	void builtHashMap()
	{
		int end,start = 0;
		int count=1;
		t.add(p.size()-1);
		for(int i=0;i<t.size();i++) 
		{
			
			map.add(new Bucket());
			end = t.get(i);
			for (int j = start; j <= end; j++) {
				if(!map.get(i).elements.contains(sortedP.get(j)))
						map.get(i).elements.add(sortedP.get(j));
			}
			start = end+1;
			map.get(i).note=count++;
			map.get(i).centroid=getcentroid(map.get(i).elements);
			System.out.println(" ");
			System.out.print(map.get(i).elements);
			System.out.print("  "+map.get(i).note);
		}
		readFile();
		findnotes();
		
		int seq[] = new int[k-1];
		len=0;
		subsequence(12,k-1,seq);
		
		System.out.println(maxvec);
		System.out.println("global max is "+Collections.max(maxvec));
		
	}
	void findnotes()
	{
		initialResult = new Vector<Integer>(); 
		for (int i = 0; i <modefromM.size() ; i++) 
		{
			double temp = modefromM.get(i);
			for (int j = 0; j < map.size(); j++) 
			{
				if(map.get(j).elements.contains(temp))
				{
					initialResult.add(map.get(j).note);
				}
			}		
		}
		System.out.println("notes--->");
		System.out.println(initialResult);
	}
	
	void useCurrentSeq(int s[])
	{
		int seq[] = new int[k];
		seq[0] = 1;
		for (int i = 0; i < s.length; i++) {
			seq[i+1] = s[i];
		}
		Vector<Integer> finalResult = new Vector<Integer>();
		
		// initialResult = the notes played in the user query by assigning first k natural numbers 
		// finalResult = notes played in the user query by assigning numbers in ascending order
		// for each in note in initialResult check what it is to be replaced by looking into the generated sequence 
		// and put it into finalResult
		
		for (int i = 0; i < initialResult.size(); i++) {
			int temp = seq[initialResult.get(i)-1];
			finalResult.add(temp);
		}
		
		System.out.println("finalResult is" + finalResult);
		
		SearchingDatabase sdb = new SearchingDatabase(finalResult);
		
		
	}
	
	void subsequence(int n, int k, int seq[])
	{
		if(len == k)
		{
			for (int i = 0; i < seq.length; i++) {
				System.out.print(seq[i]+"  ");
			}
			System.out.println();
			useCurrentSeq(seq);
			System.out.println();
			return;
		}
		
		int i = (len == 0)? 2 : seq[len-1] + 1;
		
		len++;
		
		while (i<=n)
	    {
	        seq[len-1] = i;
	        subsequence(n, k, seq);
	        i++;
	    }
		
		len--;
		
	}
	/*void subsequence(int n,int k,Vector<Integer> seq)
	{
		if(seq.size()==k)
		{
			System.out.println(seq);
			return ;
		}
		int i= seq.get(seq.size()-1);
		i++;
		while(i<n)
		{
			seq.add(i);
			subsequence(n,k,seq);
			i++;
		}
	}*/
	
	
}
