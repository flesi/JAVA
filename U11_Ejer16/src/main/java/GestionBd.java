import java.awt.Font;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.log.SysoCounter;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class GestionBd {
	
	public void crearPDF(String categoria) throws SQLException, IOException, DocumentException  {
		Connection conexionBd = ConectarBd.getConnection();
		String sql = "SELECT codigoProducto, nombreProducto, vendedor,categoriaProducto, unidadesStock FROM productos WHERE categoriaProducto = ?;";
		PreparedStatement ps = conexionBd.prepareStatement(
				sql,
				ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
	
		ps.setString(1, categoria);
		
		ResultSet rs = ps.executeQuery();
		
		// Numero de filas de la consulta
		rs.last();
		int nFilas = rs.getRow();
		rs.last();
		
		// Numero de columnas de la consulta
		int numeroColumnas = rs.getMetaData().getColumnCount();
		
		if (nFilas > 0) {
			 //Creamos un documento pdf
            Document documento = new Document();
            //creamos un archivo binario
            OutputStream ficheroPdf = Files.newOutputStream(Paths.get(categoria+".pdf"));
            
            //Asociar documento con FileOutputStream e indicar espacio entre líneas
            PdfWriter.getInstance(documento, ficheroPdf).setInitialLeading(20);
            documento.open();
            
            //fuentes
//            Font fontArialNormal = new Font(FontFactory.getFont("arial", 8, Font.BOLD, BaseColor.BLACK));  
//            Font fontArialNegrita = new Font(FontFactory.getFont("arial", 8, Font.BOLD, BaseColor.BLACK));
            
          //añadir tabla cabecera
            PdfPTable tableCab = new PdfPTable(1); //1 columna
            
            //nueva frase y añadimos a tabla
            Phrase texto = new Phrase("Listado de " + categoria +":");            
            tableCab.addCell(texto);
            texto = new Phrase("Fecha: " + LocalDate.now().toString());
            tableCab.addCell(texto);
        	
            //Añadimos tableCab al documento
            documento.add(tableCab);
            
             //Añadimos dos lineas en blanco
            documento.add(new Phrase(" "));
            documento.add(new Phrase(" "));
            
            PdfPTable tablaDetalle = new PdfPTable(numeroColumnas);  
            tablaDetalle.setHeaderRows(1);
            
            for (int i = 1; i <= numeroColumnas; i++) {
            	//cabeceras de la tabla
            	texto = new Phrase(rs.getMetaData().getColumnName(i).toString());
                tablaDetalle.addCell(texto);
			}
            
            rs.beforeFirst();
            while (rs.next()) {
            	for (int i = 1; i <= numeroColumnas; i++) {
            		texto = new Phrase(rs.getString(i));
                    tablaDetalle.addCell(texto);
				}
			}
            
            documento.add(tablaDetalle);
            documento.close();
            System.out.println("Documento "+categoria+".pdf generado");
            
		}
	}
	
	public void getProductosCategoria(String categoria) throws SQLException {
		Connection conexionBd = ConectarBd.getConnection();
		String sql = "SELECT codigoProducto, nombreProducto, vendedor,categoriaProducto, unidadesStock FROM productos WHERE categoriaProducto = ?;";
		PreparedStatement ps = conexionBd.prepareStatement(
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
				System.out.println(
						rs.getString("codigoProducto") + 
						rs.getString("nombreProducto") +
						rs.getString("vendedor") + 
						rs.getString("categoriaProducto") +
						rs.getString("unidadesStock")
						);
			}
		}
		
	}

	
	public void getCategorias() throws SQLException, IOException, DocumentException {
		
		Connection conexionBd = ConectarBd.getConnection();  
		String sql = "SELECT categoria FROM categoriasproductos";
		PreparedStatement ps = conexionBd.prepareStatement(
				sql,
				ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		
		ResultSet rs = ps.executeQuery();
		
		rs.last();
		int nFilas = rs.getRow();
		rs.beforeFirst();
		
		if (nFilas > 0) {
			while (rs.next()) {
				System.out.println("*******************************");
				System.out.println("CATEGORIA: " + rs.getString(1));
				getProductosCategoria(rs.getString(1));
				crearPDF(rs.getString(1));
			}
		}
		
	}
}
