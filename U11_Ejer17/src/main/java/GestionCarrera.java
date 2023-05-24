import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GestionCarrera {
	
	
	public static void mejoresCategoria(String categoria) throws SQLException {
		Connection con = ConectarBd.getConnection();
		
		String sql = "SELECT dorsal, nombre, apellidos, categoria, tiempo FROM resultados WHERE categoria= ? ORDER BY tiempo ASC LIMIT 3";
		
		PreparedStatement ps = con.prepareStatement(
				sql,
				ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		
		ps.setString(1, categoria);
		
		ResultSet rs = ps.executeQuery();
		
		rs.last();
		int nFilas = rs.getRow();
		rs.beforeFirst();
		
		if (nFilas > 0) {
			while (rs.next()) {
				System.out.println(rs.getString(1) +"|" 
			+  rs.getString(2)+"|" 
			+  rs.getString(3)+"|" 
			+  rs.getString(4)+"|" 
			+  rs.getString(5));
			}
		}
		
	}
	
	
	
	public static void mostrarCategorias() throws SQLException {
		Connection con = ConectarBd.getConnection();
		
		String sql = "SELECT distinct categoria FROM resultados ORDER BY categoria ASC";
		
		PreparedStatement ps = con.prepareStatement(
				sql,
				ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		
		ResultSet rs = ps.executeQuery();
		
		rs.last();
		int nFilas = rs.getRow();
		rs.beforeFirst();
						
		if (nFilas > 0) {
			while (rs.next()) {
				System.out.println(rs.getString(1));
			
			}
		}
		
	}

}
