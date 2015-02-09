
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

public class Notations 
{
	Vector<Double> pitches = new Vector<Double>();
	Vector<String> notes = new Vector<String>();
	double octave[] = new double[12];
	Vector<Integer> Q = new Vector<Integer>();
	
	public Notations(Vector<Double> temp) 
	{
		pitches = temp;
		//play pa =new play(Screen1.path);		
	}
	
	Vector<Integer> extractNotations()
	{
		double fundamental = Collections.min(pitches);
		System.out.println("fundamental is "+fundamental);
		double factor = 1.059463094;
		for (int i = 0; i < 12; i++) 
		{
			octave[i] = fundamental * Math.pow(factor, i);
			System.out.println("octave[i] is "+ octave[i]);
		}
		boolean flag;
		for (int i = 0; i < pitches.size(); i++)
		{
			flag = false;
			for (int j = 1; j <= 3; j++) 
			{
				for (int k = 0; k < 12; k++) 
				{
					if( Math.abs(pitches.get(i) - octave[k]*j) < 0.03*octave[k]*j)// 
					{
						int temp=(k+1)+((j-1)*12);
						System.out.println(" "+temp+" \t ");
						Q.add(temp);
						flag = true;
						break;
					}
				}
				if(flag) break;
			}
		}
		return Q;	
	}
}
