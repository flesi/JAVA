import java.sql.SQLException;

public class main {

	public static void main(String[] args) {


		GestionMetadatos gestionMetadatos = new GestionMetadatos();
		
		try {
			gestionMetadatos.mostrarNombreTablas("ClassicModels");
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
