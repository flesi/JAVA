import java.sql.SQLException;
import java.util.Scanner;

public class main {

	public static void main(String[] args) {

		GestionUsuarios gestionUsuarios = new GestionUsuarios();
		
		System.out.println("Bienvenido a ClassicModels");
		
		Scanner sc = new Scanner(System.in);
		System.out.print("Usuario: ");
		String user = sc.next();

		System.out.print("Clave: ");
		String clave = sc.next();		
		
		try {
			
			gestionUsuarios.Login(user,clave);
			
			
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
	}

}
