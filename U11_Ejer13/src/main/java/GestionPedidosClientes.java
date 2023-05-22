import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class GestionPedidosClientes {

	public static void pedidosClientes(ArrayList<String> numerosClientes) throws SQLException, IOException, DocumentException {
	
		//Creamos objetos para hacer la consulta a la BD
        Connection connection = ConectarBd.getConnection();
        
		for (String numeroCliente : numerosClientes) {
			String sql = "SELECT * FROM `pedidos` WHERE `numeroCliente` = ? ";
			PreparedStatement ps = connection.prepareStatement(
	        		sql, 
	        		ResultSet.TYPE_SCROLL_SENSITIVE, 
	                ResultSet.CONCUR_UPDATABLE);
			System.out.println("Cliente Nº: " + numeroCliente.toString());
			ps.setString(1, numeroCliente.toString());
	        ResultSet rs = ps.executeQuery();
	        
	      //Numero de Filas de la consulta
	        rs.last();
	        int numeroFilas = rs.getRow();
	        rs.beforeFirst();
	        
	        //System.out.println(numeroFilas);
	        
	        if (numeroFilas>0) {
	        	//System.out.println("Tiene Pedidos");
					//System.out.println(rs.getString("numeroPedido"));
					String Consultasql = "SELECT P.NumeroPedido, P.FechaPedido, P.Estado, SUM(D.CantidadPedida * D.PrecioUnidad) AS ImporteTotal " +
					"FROM clientes C "+
					"JOIN pedidos P ON C.numeroCliente = P.numeroCliente "+
					"JOIN detallespedidos D ON P.numeroPedido = D.numeroPedido "+
					"WHERE C.numeroCliente = "+numeroCliente.toString()+
					" GROUP BY P.numeroPedido, P.fechaPedido, P.estado;";
					
					crearPDF(numeroCliente.toString()+".pdf",Consultasql);
			} else {
				//System.out.println("No tiene Pedidos");
				BufferedWriter bw = new BufferedWriter(new FileWriter("incidenciasPedidos.txt",true));
				
				bw.write(numeroCliente.toString()+ " No tiene Pedidos");
				bw.newLine();
				bw.close();
				System.out.println("Añadido al documento de incidencias");
			} 
	        
		}
	
	}
	
	
	
	// CREAR PDF
	private static void crearPDF(String archivo, String sql) throws SQLException, IOException, DocumentException {
		
		//Creamos objetos para hacer la consulta a la BD
        Connection conex = ConectarBd.getConnection();
        //sql = "Select * from "+sql+";";
        PreparedStatement ps = conex.prepareStatement(sql, 
        		ResultSet.TYPE_SCROLL_SENSITIVE, 
                ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = ps.executeQuery();
        
        //Nombre de la tabla de la consulta
        String nombreTabla = rs.getMetaData().getTableName(1);
        
        //Numero de Filas de la consulta
        rs.last();
        int numeroFilas = rs.getRow();
        rs.beforeFirst();
        
		// Numero de columnas de la consulta
		int numeroColumnas = rs.getMetaData().getColumnCount();
        
		
        
        if (numeroFilas>0) {
			
        	 //Creamos un documento pdf
            Document documento = new Document();
            //creamos un archivo binario
            OutputStream ficheroPdf = Files.newOutputStream(Paths.get(archivo));
            
            //Asociar documento con FileOutputStream e indicar espacio entre líneas
            PdfWriter.getInstance(documento, ficheroPdf).setInitialLeading(20);
            documento.open();
            
            //fuentes
            Font fontArialNormal = new Font(FontFactory.getFont("arial", 8, Font.NORMAL, BaseColor.BLACK));  
            Font fontArialNegrita = new Font(FontFactory.getFont("arial", 8, Font.BOLD, BaseColor.BLACK));  
        	
            // CABECERA DEL DOCUMENTO PDF
            
            //añadir tabla cabecera
            PdfPTable tableCab = new PdfPTable(1); //1 columna
            
            //nueva frase y añadimos a tabla
            Phrase texto = new Phrase("Listado de " + nombreTabla +" del usuario:", fontArialNegrita);            
            tableCab.addCell(texto);
            texto = new Phrase("Fecha: " + LocalDate.now().toString(), fontArialNegrita);
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
            	texto = new Phrase(rs.getMetaData().getColumnName(i).toString(), fontArialNegrita);
                tablaDetalle.addCell(texto);
			}
            
            rs.beforeFirst();
            while (rs.next()) {
            	for (int i = 1; i <= numeroColumnas; i++) {
            		texto = new Phrase(rs.getString(i), fontArialNormal);
                    tablaDetalle.addCell(texto);
				}
			}
            
            documento.add(tablaDetalle);
            documento.close();
            System.out.println("Documento Productos.pdf generado");
            
		}else {
			System.out.println("No hay filas que mostrar");
		}
	}
	
}
