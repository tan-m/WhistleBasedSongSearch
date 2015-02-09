import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;



public class SwarToNumbers {

	

	void closeFiles(BufferedReader ip,BufferedWriter op) throws IOException
	{
		op.close();
		ip.close();
	}
	
	int switchRagaTracks(String s)
	{
		int number=0;
		switch(s)
		{
		case "'S" :
			number = 1;
			break;
		case "'r" :
			number = 2;
			break;
		case "'R" :
			number = 3;
			break;
		case "'g" :
			number = 4;
			break;
		case "'G" :
			number = 5;
			break;
		case "'m" :
			number = 6;
			break;
		case "'M" :
			number = 7;
			break;
		case "'P" :
			number = 8;
			break;
		case "'d" :
			number = 9;
			break;
		case "'D" :
			number = 10;
			break;
		case "'n" :
			number = 11;
			break;
		case "'N" :
			number = 12;
			break;
		case "S" :
			number = 13;
			break;
		case "r" :
			number = 14;
			break;
		case "R" :
			number = 15;
			break;
		case "g" :
			number = 16;
			break;
		case "G" :
			number = 17;
			break;
		case "m" :
			number = 18;
			break;
		case "M" :
			number = 19;
			break;
		case "P" :
			number = 20;
			break;
		case "d" :
			number = 21;
			break;
		case "D" :
			number = 22;
			break;
		case "n" :
			number = 23;
			break;
		case "N" :
			number = 24;
			break;
			
		case "S'" :
			number = 25;
			break;
		case "r'" :
			number = 26;
			break;
		case "R'" :
			number = 27;
			break;
		case "g'" :
			number = 28;
			break;
		case "G'" :
			number = 29;
			break;
		case "m'" :
			number = 30;
			break;
		case "M'" :
			number = 31;
			break;
		case "P'" :
			number = 32;
			break;
		case "d'" :
			number = 33;
			break;
		case "D'" :
			number = 34;
			break;
		case "n'" :
			number = 35;
			break;
		case "N'" :
			number = 36;
			break;
		default :
			System.out.println("default reached : "+ s);
			number = 1000;
			break;
		}
		return number;
	}
	int switchWestern(String s)
	{
		int n=0;
		switch(s)
		{
			case "c":
				n = 1;
				break;
			case "c#":
				n = 2;
				break;
			case "d":
				n = 3;
				break;
			case "d#":
				n = 4;
				break;
			case "e":
				n = 5;
				break;
			case "f":
				n = 6;
				break;
			case "f#":
				n = 7;
				break;
			case "g":
				n = 8;
				break;
			case "g#":
				n = 9;
				break;
			case "a":
				n = 10;
				break;
			case "a#":
				n = 11;
				break;
			case "b":
				n = 12;
				break;
				
			case "C":
				n = 13;
				break;
			case "C#":
				n = 14;
				break;
			case "D":
				n = 15;
				break;
			case "D#":
				n = 16;
				break;
			case "E":
				n = 17;
				break;
			case "F":
				n = 18;
				break;
			case "F#":
				n = 19;
				break;
			case "G":
				n = 20;
				break;
			case "G#":
				n = 21;
				break;
			case "A":
				n = 22;
				break;
			case "A#":
				n = 23;
				break;
			case "B":
				n = 24;
				break;
				
			
			case "C.":
				n = 25;
				break;
			case "C#.":
				n = 26;
				break;
			case "D.":
				n = 27;
				break;
			case "D#.":
				n = 28;
				break;
			case "E.":
				n = 29;
				break;
			case "F.":
				n = 30;
				break;
			case "F#.":
				n = 31;
				break;
			case "G.":
				n = 32;
				break;
			case "G#.":
				n = 33;
				break;
			case "A.":
				n = 34;
				break;
			case "A#.":
				n = 35;
				break;
			case "B.":
				n = 36;
				break;
				
			default :
				System.out.println("default reached : "+ s);
				n = 1000;
				break;
		}
		return n;
	}
	void convertTracks(BufferedReader ip,BufferedWriter op) throws IOException
	{
		String[] split;
		String line="";
		while(true)
		{
			line = ip.readLine();
			if(line == null) break;
			split = line.split("\\s+");
			int number=0;
			for (int i = 0; i < split.length; i++) {
				number = switchWestern(split[i]);
				if(number != 1000)
					op.write(number + " ");
				else 
					op.write(split[i] + " ");

			}
		}
	}


	void start()
	{
		BufferedReader ip=null;
		BufferedWriter op=null;
		try 
		{
			ip = new BufferedReader(new FileReader(new
					File("F:\\Whistle Based Song Search\\swar\\YeDosti.txt")));
		}
		catch (FileNotFoundException e) 
		{
			System.out.println("input file not found");
			e.printStackTrace();
		}
		try {
			op = new BufferedWriter(new 
					FileWriter("C:\\Users\\tanmay\\Desktop\\songsNote.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try 
		{
			convertTracks(ip,op);
			closeFiles(ip,op);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			// ‘ 
		}
		
		System.out.println("Done!");
	}

}
