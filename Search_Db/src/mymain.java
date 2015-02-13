import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Vector;


public class mymain {

	
	 
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		int start=(int) System.currentTimeMillis();
		
		String str1;
		double d1;
		Vector<Double> finalResult = new Vector<Double> ();
		BufferedReader br1 = null;
		try {
			br1 = new BufferedReader(new FileReader("C:\\Users\\tanmay\\Desktop\\pitch.txt"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try 
		{
			while( (str1 = br1.readLine()) != null)
			{
				d1=Double.parseDouble(str1);
				finalResult.add(d1);	
			}
		} 
		catch (NumberFormatException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Notations n= new Notations(finalResult);	
		Vector<Integer> Q =n.extractNotations();
		System.out.println("notations are -- >"+Q);
		Search sch=new Search();
		try 
		{
			sch.connecttodb();
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try 
		{
			sch.startSearch(Q);
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		/*Search sh = new Search();
		//Integer[] qarr = {4, 3, 1, 6, };
		//Integer[] dbarr = { 6, 8, 10, 4,3,1, 6,8,10,14};
		
		Integer[] qarr = {7, 1, 20, 20, 1, 3, 1 };
		Integer[] dbarr = {7, 3, 1, 20, 7, 3, 21};
		
		Vector<Integer> q = new Vector<Integer>(Arrays.asList(qarr));
		Vector<Integer> db = new Vector<Integer>(Arrays.asList(dbarr));
		System.out.println(sh.score(q, db));
		*/
		
		int end=(int) System.currentTimeMillis();
		System.out.println("exec time "+(end-start));
	}

}
