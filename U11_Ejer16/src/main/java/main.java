import java.io.IOException;
import java.sql.SQLException;

import com.itextpdf.text.DocumentException;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GestionBd gestionBd = new GestionBd();
		
		try {
			gestionBd.getCategorias();
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
