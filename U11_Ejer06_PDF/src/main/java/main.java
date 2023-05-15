import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfWriter;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String ciudad = "Barcelona";
		
		try {
			GestionPdf.crear("ClientesDe"+ ciudad +".pdf", ciudad);
		} catch (SQLException | DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
