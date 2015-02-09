import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class NoiseRemoval {
	
	Vector<Double> finalResult = new Vector<Double> ();
	Vector<Integer> addlist = new Vector<Integer> ();
	ConcurrentHashMap<Integer,Integer> framePitch = new ConcurrentHashMap<Integer,Integer>();
	ConcurrentHashMap<Integer,Integer> framePower = new ConcurrentHashMap<Integer,Integer>();
	HashMap<Integer,Integer> pitchCount = new HashMap<Integer,Integer>();
	HashMap<Integer,Integer> ref_pitch = new HashMap<Integer,Integer>();
	
	HashMap<Integer,Integer> framePitch1 = new HashMap<Integer,Integer>();
	HashMap<Integer,Integer> framePower1 = new HashMap<Integer,Integer>();
	
	private Object lock = new Object();
	String file;
	static final int frameSize=2048;
	static int numChannels;
	
	double comp(int t)
	{
		return t*0.05;
	}
	
	int pitchForSlice(int start, int end)
	{
		HashMap<Integer,Integer> successive = new HashMap<Integer,Integer>();
		
		int prev1, curr = 0, cnt=0;
		prev1 = framePitch1.get(start);
		for (int i = start+1; i < end; i++) 
		{
			curr = framePitch1.get(i);
			if(curr == prev1) 
				cnt++;
			else
			{
				
				if(successive.containsKey(prev1))
				{
					int t = successive.get(prev1);
					if(t < cnt)
						successive.put(prev1,cnt);
				}
				else
				{
					successive.put(prev1, cnt);
				}
				cnt = 0;
			}
			prev1 = curr;
				
		}
		
		int sd=0,max=cnt,ret=prev1;
		for(Integer k : successive.keySet())
		{
			sd=successive.get(k);
			if(sd>max)
			{
				max=sd;
				ret=k;
			}
					
		}
		
		System.out.println("successive is--------"+successive);
		System.out.println("Return value "+ret);
		return ret;
	}
	
	void read()
	{
		try
		{
			// Create a buffer of 100 frames
			double[] buffer = null,dbuffer = null; 

			String str1,str2;
			String strA1[] = new String[2]; 
			String strA2[] = new String[2];
			double d1,d2;
			int a,b;
			
			int l = 0,ans1=0,ans2 = 0;
			BufferedReader br1 = new BufferedReader(new FileReader("C:\\Users\\HP\\Desktop\\coeffs.txt"));
			BufferedReader br2 = new BufferedReader(new FileReader("C:\\Users\\HP\\Desktop\\power.txt"));
			while( (str1 = br1.readLine()) != null && (str2 = br2.readLine()) != null )
			{
				d1=Double.parseDouble(str1);
				d2=Double.parseDouble(str2);
				
				
				framePitch.put(l,(int)d1);
				framePower.put(l,(int)d2);
				l++;
			}
			
			for (int i = 0; i < framePitch.size(); i++) {
				
				//t1=framePitch.get(i);
				int t2 = framePower.get(i);
				//if(t2 > (avg*0.8))
				{
					int t1 = framePitch.get(i);
					System.out.format("\n pitch is %d \t\t power is  %d",t1,t2);
				}
				
				
			}
			
		
			// keeping count of occurrence a particular pitch in framePitch 
			int t;
			Double avgpower=(double) 0;
			for (int i = 0; i < framePitch.size(); i++) 
			{
				int temp1 = (int) Math.round(framePitch.get(i));
				if(pitchCount.containsKey(temp1))
				{
					t = pitchCount.get(temp1);
					t++;
					pitchCount.put(temp1,t);
					
				}
				else
				{
					pitchCount.put(temp1, 1);
				}
				avgpower+=framePower.get(i);
			}
			avgpower/=framePower.size();
			
			System.out.println("pitchCount------------->");
			System.out.println(pitchCount);
			
			Integer [] arr = new Integer[pitchCount.size()]; 
			arr =  pitchCount.keySet().toArray((arr));
			HashMap<Integer,Integer> roundoff = new HashMap<Integer,Integer>();
			
			
			// making count of similar pitches same 
			for (int i = 0; i < arr.length; i++) 
			{
				if(!pitchCount.containsKey(arr[i])) continue;
				int sum=arr[i], deno=1, cnt=pitchCount.get(arr[i]);
				addlist.add(arr[i]);
				for (int j = i+1; j < arr.length; j++) 
				{
					
					if(Math.abs(arr[i]-arr[j])*1.0 < comp(arr[i]))
					{
						
						if(pitchCount.containsKey(arr[j]))
						{
							addlist.add(arr[j]);
							cnt+=pitchCount.get(arr[j]);
							pitchCount.remove(arr[j]);
						}
						
					}
					
				}
				if(cnt>5)
				{
					System.out.println("addlist is "+addlist+"\t\t cnt is"+cnt);
					
				}
				int t_avg=0;
				for(Integer k : addlist)
				{
					t_avg += k;
					ref_pitch.put(k,cnt);
				}
				t_avg /= addlist.size();
				
				for(Integer k : addlist)
				{
					roundoff.put(k,t_avg);
				}
				
				addlist.removeAllElements();
				
			}
			
			System.out.println("Before  Ref_pitch------>");
			System.out.println(ref_pitch);
			
			Vector<Integer> remove = new Vector<Integer> ();
			
			// removing entries with small count from ref_pitch
			for(Integer i : ref_pitch.keySet() )
			{
				if(ref_pitch.get(i) < 5 ) 
					remove.add(i);
			}
			for (Integer i : remove) 
			{
				ref_pitch.remove(i);
			}
			remove = null;
			
			System.out.println("Ref_pitch------>");
			System.out.println(ref_pitch);
			
			// second pass on framePitch
			System.out.println("size of framepitch is "+framePitch.size());
			for (int i = 0, j=0; i < framePitch.size(); i++) 
			{
				if(ref_pitch.containsKey((int)Math.round(framePitch.get(i))))
				{
					framePitch1.put(j, (int) Math.round(framePitch.get(i)));
					framePower1.put(j, (int) Math.round(framePower.get(i)));
					j++;
					//System.out.println("here");
				}
			}
			
			System.out.println("\n\n Filter--------------------> ");
			
					
			

			//roundoff 
			
			for (int i = 0; i < framePitch1.size(); i++) 
			{
				int key = framePitch1.get(i);
				int val = roundoff.get(key);
				framePitch1.put(i, val);
				
			}
			
			System.out.println("\n\n\nafter round off");
			for (int i = 0; i <framePitch1.size(); i++) 
			{
				
				System.out.println("pitch   "+framePitch1.get(i)+"\t power   "+framePower1.get(i));
			}
			
			System.out.println("avgpower is "+avgpower);
			int avgp=avgpower.intValue();
			System.out.println("avgpower is "+avgp);
			
			int start,max,end;
			for (int i = 0; i < framePower1.size(); i++) 
			{
				if(framePower1.get(i)<avgp)
				{
					continue;
				}
				start=i;
				end=start;
				while(i < framePower1.size() && framePower1.get(i)>=avgp)
				{
					System.out.println("In block pitch is "+framePitch1.get(i));
					i++;
					end++;
				}
				i = end;
				System.out.println("calling "+start+" "+end);
				if(Math.abs(start-end)>2.0)
				{
					finalResult.add((double) pitchForSlice(start,end));
				}
			}
			
			System.out.println("finalResult is   "+finalResult);
			Notations n= new Notations(finalResult);	
			Vector<Integer> Q =n.extractNotations();
			System.out.println("notations are -- >"+Q);
			Search sch=new Search();
			sch.connecttodb();
			sch.startSearch(Q);
			
			// Testing only
			/*Vector<Integer> temp = new Vector<Integer>();
			temp.add(1);temp.add(2);temp.add(3);temp.add(4);temp.add(5);
			System.out.println("temp is  "+temp);
			sch.startSearch(temp);*/
		}
		catch (Exception e)
		{
			System.err.println(e);
		}
	}

	/*public static void main(String[] args) {
		// TODO Auto-generated method stub
		NoiseRemoval nr = new NoiseRemoval();
		nr.read();
		
		/*
		Search sh = new Search();
		Integer[] qarr = {1, 3, 6, 6, 9, 9, 11, 13, 13, 13, 13, 11, 9, 6, 6, 6, 3, 3, 1};
		Integer[] dbarr = {3,5,7,8,10,12,14,15,15,14,12,10,8,7,5,3};
		Vector<Integer> q = new Vector<Integer>(Arrays.asList(qarr));
		Vector<Integer> db = new Vector<Integer>(Arrays.asList(dbarr));
		System.out.println(sh.score(q, db));
		
	}*/

}
