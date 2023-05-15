import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import com.itextpdf.text.DocumentException;

public class main {

	public static void main(String[] args) {

		GestionPDF gestionPDF = new GestionPDF();
		
		try {
			// CREA UN PDF DE UNA CONSULTA SQL
			//*****************************************************	
			gestionPDF.crearPDF("Pruebasql.pdf", "SELECT * FROM CLIENTES;");
			
			
			//CREA UN PDF DESDE UN ARRAY PASADO COMO PARAMETRO
			//*************************************************
			String[] miArray = {
					"id;nombre;apellido;", // CABECERA DE LA TABLA PDF
					"01;Pedro;Ramos;",
					"02;Mateo;Gonzalez;",
					"03;Rocio;Llera",
					"04;Pedro;Garcia",
			};
			
			//gestionPDF.crearPDF("miArray.pdf", miArray);
			
			
			//CREA UN PDF DESDE UN ARRAY LIST
			//************************************
			ArrayList<String> miArrayList = new ArrayList<String>();
			miArrayList.add("id;nombre;apellido;ciudad");
			miArrayList.add("01;Pedro;Garcia;Zafra");
			miArrayList.add("02;Manuel;Gutierrez;Sevilla");
			
			//gestionPDF.crearPDF("miArrayList.pdf", miArrayList);
			
			
			//CREA UN PDF DESDE UN ARCHIVO
			
			
			
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
