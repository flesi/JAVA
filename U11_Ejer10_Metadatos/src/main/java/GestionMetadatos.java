import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GestionMetadatos {
	
	
	
	public void mostrarNombreColumnas(String tabla) throws SQLException {
		ConectarBd conectarBd = new ConectarBd();

		DatabaseMetaData metadata = ConectarBd.getConnection().getMetaData();
		
		ResultSet columnas = metadata.getColumns(null, null, tabla, null);

		System.out.println("Listado de campos de la tabla: " + tabla);
		while (columnas.next()) {
			System.out.println(columnas.getString(4));
		}
	}
	
	

}
