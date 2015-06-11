import java.sql.SQLException;
import java.util.Vector;


public class SearchingDatabase 
{
	Search s;
	SearchingDatabase(Vector<Integer> userQ)
	{
		s=new Search();
		try {
			s.connecttodb();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			s.startSearch(userQ);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	

}
