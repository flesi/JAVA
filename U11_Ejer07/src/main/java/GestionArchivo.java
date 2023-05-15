import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GestionArchivo {

	public static void crear(String categoria) throws SQLException, IOException {
		
		ResultSet rsProductos = selectProductos(categoria);
		
		rsProductos.last();
		int nFilas = rsProductos.getRow();
		rsProductos.first();
		
		
		if (nFilas>0) {
			BufferedWriter writer = new BufferedWriter(new FileWriter("Productos_Categoria_"+categoria+".txt"));
		
			String producto;
			while (rsProductos.next()) {
				System.out.println(rsProductos.getString("codigoProducto") + ";" + 
									rsProductos.getString("nombreProducto") + ";" +
 									rsProductos.getString("categoriaProducto") + ";" +
									rsProductos.getString("precioVenta") + ";" +
 									rsProductos.getString("precioCompra"));
				
				producto = (rsProductos.getString("codigoProducto") + ";" + 
						rsProductos.getString("nombreProducto") + ";" +
							rsProductos.getString("categoriaProducto") + ";" +
						rsProductos.getString("precioVenta") + ";" +
							rsProductos.getString("precioCompra"));
				
				
				writer.write(producto);
				writer.newLine();
				
				//System.out.println(rsProductos.getString("codigoProducto" "nombreProducto" "categoriaProducto" "precioVenta" "precioCompra"));
			}
			writer.close();
		}
			
		
	}
	
	
	public static ResultSet selectProductos(String categoria) throws SQLException {

		String sqlSelect = "SELECT codigoProducto, nombreProducto, categoriaProducto, precioVenta, precioCompra FROM `productos` where categoriaProducto = ?";

		PreparedStatement ps = ConectarBd.getConnection().prepareStatement(
				sqlSelect,
				ResultSet.TYPE_SCROLL_SENSITIVE,  
				ResultSet.CONCUR_READ_ONLY);
		
		ps.setString(1, categoria);

		return ps.executeQuery();
	}

}
