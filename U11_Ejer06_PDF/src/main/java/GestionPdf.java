
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


public class GestionPdf {

	public static void crear (String nombreArchivo, String localidad) throws SQLException, DocumentException, IOException {
		
		//ResultSet registros = GestionPdf.selectProductos(precioInicio, precioFin);
		ResultSet registros = GestionPdf.selectClientes(localidad);
		
		//averiguamos el nºº de filas del resultSet
        //nos vamos al último elem. y obtenemos su nº de fila
        //ese nº nos indica tb cuántos elementos hay
        registros.last();
        int numeroFilas = registros.getRow();
        registros.beforeFirst();
        
        if (numeroFilas>0){	
            //Creamos un documento pdf
            Document documento = new Document();
            //creamos un archivo binario
            OutputStream ficheroPdf = (OutputStream) Files.newOutputStream(Paths.get(nombreArchivo));
            
            //Asociar documento con FileOutputStream e indicar espacio entre líneas
            PdfWriter.getInstance(documento, ficheroPdf).setInitialLeading(20);
            documento.open();
            
            //fuentes
            Font fontArialNormal = new Font(FontFactory.getFont("arial", 8, Font.NORMAL, BaseColor.BLACK));  
            Font fontArialNegrita = new Font(FontFactory.getFont("arial", 8, Font.BOLD, BaseColor.BLACK));  
            
            //añadir tabla cabecera
            PdfPTable tableCab = new PdfPTable(1); //1 columna
            
            //nueva frase y añadimos a tabla
            Phrase texto = new Phrase("Listado clientes de " + localidad);
            
            tableCab.addCell(texto);
            texto = new Phrase("Fecha: " + LocalDate.now().toString(), fontArialNegrita);
            tableCab.addCell(texto);
            
            //add tabla a documento
            documento.add(tableCab);
            
             //lineas blanco
            documento.add(new Phrase(" "));
            documento.add(new Phrase(" "));
            
            PdfPTable tablaDetalle = new PdfPTable(5);  //5 columnas
            
            tablaDetalle.setHeaderRows(1); //repetir cabecera de tabla en todas pags.
            //cabeceras de la tabla
            texto = new Phrase("numeroCliente", fontArialNegrita);
            tablaDetalle.addCell(texto);
            texto = new Phrase("nombre", fontArialNegrita);
            tablaDetalle.addCell(texto);
            texto = new Phrase("telefono", fontArialNegrita);
            tablaDetalle.addCell(texto);
            texto = new Phrase("ciudad", fontArialNegrita);
            tablaDetalle.addCell(texto);
            texto = new Phrase("pais", fontArialNegrita);
            tablaDetalle.addCell(texto);
            
            DecimalFormat formato = new DecimalFormat("##,###,###.00");
            
            //registros.firegistrost();
            while (registros.next()){
                texto = new Phrase(registros.getString("numeroCliente"), fontArialNormal);
                tablaDetalle.addCell(texto);
                texto = new Phrase(registros.getString("nombre"), fontArialNormal);
                tablaDetalle.addCell(texto);
                texto = new Phrase(registros.getString("telefono"), fontArialNormal);
                tablaDetalle.addCell(texto);
                texto = new Phrase(registros.getString("ciudad"), fontArialNormal);
                tablaDetalle.addCell(texto);
                texto = new Phrase(registros.getString("pais"), fontArialNormal);
                tablaDetalle.addCell(texto);
                
                
            }
            //añadir la tabla al documento
            documento.add(tablaDetalle);
            
            documento.close();
            
            System.out.printf("Documento %s generado", nombreArchivo);
        }
        else
            System.out.println("No hay filas que mostrar.");
		
	}

	
	public static ResultSet selectClientes(String localidad) throws SQLException {

		String sqlSelect = "SELECT numeroCliente, nombre, telefono, ciudad, pais FROM clientes WHERE ciudad= ?";
		
//		String sqlSelect = "select codigoProducto, nombreProducto, vendedor, precioVenta "
//				+ " from productos where precioVenta between ? and ?"
//				+ " order by 4 ASC";

		PreparedStatement ps = ConectarBd.getConnection().prepareStatement(
				sqlSelect,
				ResultSet.TYPE_SCROLL_SENSITIVE,  
				ResultSet.CONCUR_READ_ONLY);

		ps.setString(1, localidad);

		return ps.executeQuery();
	}
	
	
	//***************************************
	public static ResultSet selectProductos(double precio1, double precio2) throws SQLException {

		if (precio1 > precio2) {
			double aux = precio2;
			precio2 = precio1;
			precio1 = aux;
		}

		String sqlSelect = "select codigoProducto, nombreProducto, vendedor, precioVenta "
				+ " from productos where precioVenta between ? and ?"
				+ " order by 4 ASC";

		PreparedStatement ps = ConectarBd.getConnection().prepareStatement(
				sqlSelect,
				ResultSet.TYPE_SCROLL_SENSITIVE,  
				ResultSet.CONCUR_READ_ONLY);

		ps.setDouble(1, precio1);
		ps.setDouble(2, precio2);

		return ps.executeQuery();
	}

}