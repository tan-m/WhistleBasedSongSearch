
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

public class Notations 
{
	Vector<Double> pitches = new Vector<Double>();
	Vector<String> notes = new Vector<String>();
	double octave[] = new double[36];
	

	public Notations(Vector<Double> temp) 
	{
		pitches = temp;
		
	}
	Vector<Integer> extractNotations()
	{
		double mid = Double.MAX_VALUE; // minimum
		double low;
		double high;
		double secondMin=Double.MAX_VALUE;
		double minErrFundamental = Double.MAX_VALUE;
		double bestFundamental = 0;
		double errforQ;
		Vector<Integer> Q= new Vector<Integer>();
		for (Double i : pitches) 
		{
			if(i < mid)
			{
				secondMin = mid;
				mid = i;
			}
			else if(i < secondMin && i!=mid)
			{
				secondMin = i;
			}
		}
		
		if(mid==0)
		{
			//System.out.println("error");
			System.exit(0);
		}
		
		high = mid + ((secondMin - mid)*0.75);
		low = mid - ((secondMin - mid)*0.75);
		
		/*System.out.println("2min"+secondMin);
		System.out.println("low"+low);
		System.out.println("high"+high);
		System.out.println("mid"+mid); */
		
		double incr = Math.ceil(mid/100.0);
		incr=1;
		for(double f = low; f<=high; f+=incr)
		{
			errforQ = shiftFundamental(f,Q,false);
			if(errforQ < minErrFundamental)
			{
				minErrFundamental = errforQ;
				bestFundamental = f;
			}
		}
		
		// call shiftFundamental with low explicitly
		
		errforQ = shiftFundamental(mid,Q,false);
		if(errforQ < minErrFundamental)
		{
			bestFundamental = mid;
		}
				//loop end
		shiftFundamental(bestFundamental,Q,true);
		//shiftFundamental(mid,Q,true);
		return Q;
		
	} 

	double shiftFundamental(double fundamental,Vector<Integer> Q,boolean flag)
	{
		double errforQ=0;
		//System.out.println("fundamental is "+fundamental);
		double factor = 1.059463094;
		for (int i = 0; i < 36; i++) 
		{
			octave[i] = fundamental * Math.pow(factor, i);
			if(flag);	//System.out.println(i+"  "+octave[i]);
		}
		double minerror;
		int note = 0;
		for (int i = 0; i < pitches.size(); i++)
		{
			minerror =Double.MAX_VALUE;
			for (int j = 0; j < 36; j++) 
			{
				double temp=Math.abs(octave[j]-pitches.get(i));
				if(temp < minerror)
				{
					minerror=temp;
					note=j+1;
				}
			}
			errforQ+=minerror; //error
			if(flag) Q.add(note);
		}
		return errforQ;	
	}
}
