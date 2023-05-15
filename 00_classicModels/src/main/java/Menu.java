import java.util.Scanner;

public class Menu {
	
	
	public void menuEmpleado() {
		
	}
	
	
	public void menuGestionTablas() {
		int opc;
		
		GestionBd gestionBd = new GestionBd();
			System.out.println("*****************************");
			System.out.println(	"0. Cambiar Tabla \n"+
								"1. Mostrar Elementos \n" +
								"2. Mostrar Info Tabla \n" +
							 	"3. Añadir Elemento \n" + 
								"4.  \n" +
								"5. Buscar Elemento \n" +
								"6. Filtrar Elementos \n" +
								"7. Ordenar Elementos \n" +
								"8. Imprimir PDF \n" +
								"9. Salir"
					);
	}

}
