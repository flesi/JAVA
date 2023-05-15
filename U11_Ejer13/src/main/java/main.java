import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import com.itextpdf.text.DocumentException;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GestionPedidosClientes GPC = new GestionPedidosClientes();
		
		ArrayList<String> listaClientes = new ArrayList<String>();
		
		listaClientes.add("181");
		listaClientes.add("182");
		listaClientes.add("128");
		listaClientes.add("123");
		listaClientes.add("124");
		listaClientes.add("125");
		listaClientes.add("99993");
		
		try {
			try {
				
				GPC.pedidosClientes(listaClientes);
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
