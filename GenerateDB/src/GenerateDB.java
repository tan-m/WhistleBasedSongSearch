import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.io.FileOutputStream;

import com.mysql.jdbc.Statement;


public class GenerateDB {

	/**
	 * @param args 
	 */
	private java.sql.Connection conn = null;
	String pwd = "abcde";
	String name = "dbuser";
	protected ResultSet rs;
	Statement stmt = null;	
	void connecttodb() throws Exception 
	{
		Class.forName("com.mysql.jdbc.Driver");
		String url = "jdbc:mysql://localhost:3306/project";
		conn = DriverManager.getConnection(url, name, pwd);
		System.out.println("got conn");
		stmt = (Statement) conn.createStatement();
		
	}
	
	void generate() throws IOException, SQLException
    {
		FileInputStream fstream = new FileInputStream("C:\\Users\\tanmay\\Desktop\\songsNote.txt");
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		int id;
		int subid=1;
		String strLine;
		int val[] = new int[1090];
		int val1[] = new int[1090];
		String notation = new String("");
		int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
		ResultSet rst =this.stmt.executeQuery("select max(id) from note");
		rst.first();
		
		Object temp=rst.getString(1);
		System.out.println(rst.getString(1));
		System.out.println("temp is "+temp);
		
		if(rst.getString(1) == null)
			id = 0;
		else
			id=Integer.parseInt(temp.toString());
		id++;
		
		System.out.println("id --- > "+id);
		
		while ((strLine = br.readLine()) != null)
		{
			
			String [] s = strLine.split(" ");
			for (int i = 0; i < s.length; i++) {
				val[i] = Integer.parseInt( s[i] );
				if(val[i] > max) max = val[i];
				if(val[i] < min) min = val[i];
			}
			for (int i = 0; i < max; i++) {
				for (int j = 0; j < s.length; j++) {
					val1[j] = val[j] - i;
					notation=notation.concat(val1[j]+" ");

				}
				System.out.println("insert -->  "+notation);
				this.stmt.executeUpdate("insert into note values('"
						+ id + "','" + subid
						+ "','" + notation + 
						 "')");
				System.out.println();
				subid++;
				notation = notation.replaceAll("(.*)","");
				//op.newLine();
			}
			//op.newLine();
			//op.newLine();
			for (int i = 1; i <= (36 - min); i++) {
				for (int j = 0; j < s.length; j++) {
					val1[j] = val[j] + i;
					notation=notation.concat(val1[j]+" ");
					//op.write(val1[j]+" ");

				}
				System.out.println();
				System.out.println("insert -->  "+notation);
				this.stmt.executeUpdate("insert into note values('"
						+ id + "','" + subid
						+ "','" + notation + 
						 "')");
				System.out.println();
				subid++;
				notation = notation.replaceAll("(.*)","");
				//op.newLine();
			}
			subid=1;
			id++;

		}
		br.close();


	}
	public static void main(String[] args) {
			
		// TODO Auto-generated method stub
		GenerateDB g = new GenerateDB();
		
		try {
			g.connecttodb();
			g.generate();
			g.conn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}

}
