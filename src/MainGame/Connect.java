package MainGame;



import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
public class Connect {

	private static Connection con;
	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver OK");
			con=DriverManager.getConnection(YOUR_DB_LINK,USERNAME,PASSWORD);
			System.out.println("Connection OK");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static Connection getConnection() {
		return con;
	}
  }

