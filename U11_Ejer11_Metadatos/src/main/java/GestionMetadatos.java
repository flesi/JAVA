import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GestionMetadatos {
	
	
	
	public void mostrarNombreTablas(String bd) throws SQLException {
		ConectarBd conectarBd = new ConectarBd();

		DatabaseMetaData metadata = ConectarBd.getConnection().getMetaData();
		
		ResultSet tablas = metadata.getTables(bd, null, null, null);
		
		System.out.println("Listado de tablas de la base de datos "+ bd +": ");
		while (tablas.next()) {
			System.out.println(tablas.getString(3));
		}
		
	}

}
