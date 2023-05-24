import java.sql.SQLException;
import java.util.Scanner;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GestionCarrera gestionCarrera = new GestionCarrera();
		
		
		try {
			String categoria = null;
			do {
				
				//MOSTRAMOS LAS CATEGORIAS
				System.out.println();
				System.out.println("*****Categorias*****");
				gestionCarrera.mostrarCategorias();
				
				System.out.println("");
				
				Scanner sc = new Scanner(System.in);
				System.out.println("S Para Salir");
				System.out.print("Selecciona una categoria: ");
				categoria = sc.next();
				
				gestionCarrera.mejoresCategoria(categoria);
				
				
				
			} while (!categoria.equalsIgnoreCase("S"));
			
				
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
