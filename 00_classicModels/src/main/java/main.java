import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import com.itextpdf.text.DocumentException;

public class main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Bienvenido a Classic Models");

		GestionClassicModels gestion = new GestionClassicModels();
		GestionBd gestionBd = new GestionBd();
		
		
		//String pass = "Pedro1234"; //5773a02052b08a9af8885dc6b46e6c60b52d9cbcf1cb0111671637a2f3f95345
		SHA256 sha256 = new SHA256();
		//pass = sha256.sha256(pass);
		
		try {
			
		//	gestion.comprobarUsuario("cschmitt@terra.es", pass);
		
		//gestionBd.mostrarTablas("clientes");
		//gestionBd.mostrarTablas();
		
		//System.out.println(gestionBd.isPrimaria("clientes", "numeroCliente"));
		//System.out.println(gestionBd.isForanea("clientes","empleadoGestorVentas"));
		
		//gestionBd.mostrarInfoTabla("pagos");
		//gestionBd.addElementoTabla("clientes");
		
		//gestionBd.existData("clientes", "empleadoGestorVentas", "4444");
		
		//System.out.println(gestionBd.existData("empleados", "numeroEmpleado","1002"));
			
		crearPDF crearPDF = new crearPDF();	
			
		//crearPDF.crearPDF("pruebaOficinas.pdf","Select * from oficinas");
			
			System.out.print("Usuario: ");
			String user = sc.next();
			
			System.out.print("Pass: ");
			String pass = sha256.sha256(sc.next());
		
			
			if (gestion.comprobarUsuario(user, pass)) {
			
				String tabla=null;
				Menu menu = new Menu();
				
				int opc = 0;	
				menu.menuGestionTablas();
				System.out.println("");
				do {
					System.out.println("**********************************");
					System.out.println("[Tabla Seleccionada: " + tabla + "]");
					switch (opc) {
					case 0:
						gestionBd.mostrarTablas();
						System.out.println("");
						System.out.print("Selecciona una tabla: ");
						tabla = sc.next();
						break;
					case 1:
						gestionBd.mostrarTablas(tabla);
						break;
					case 2:
						gestionBd.mostrarInfoTabla(tabla);
						break;
					case 3:
						gestionBd.addElementoTabla(tabla);
					case 4:
						System.out.println("No configurado aun");
						break;
					case 5:
						System.out.println("No configurado aun");
						break;
					case 6:
						System.out.println("No configurado aun");
						break;
					case 7:
						System.out.println("No configurado aun");
						break;
					case 8:
						crearPDF.crearPDF(tabla+".pdf", tabla);
						break;
					case 9:
						break;
					default:
						System.out.println("OPCION NO VALIDA");
						break;
						
					}
					System.out.println("[Tabla Seleccionada: " + tabla + "]");
					menu.menuGestionTablas();
					System.out.print("Selecciona otra opcion: ");
					opc = sc.nextInt();
				} while (opc!=7);
				
			} else {
				System.out.println("Usuario o Clave Erroneos");
			}
			
			
				
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
