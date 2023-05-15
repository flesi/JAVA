import java.io.IOException;
import java.sql.SQLException;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		GestionArchivo gestionArchivo = new GestionArchivo();
		
		try {
			gestionArchivo.crear("Ships");
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
