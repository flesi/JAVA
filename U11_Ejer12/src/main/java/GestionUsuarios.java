import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class GestionUsuarios {

	
	public void Login(String user, String pass) throws SQLException {
	
		//Creamos objetos para hacer la consulta a la BD
        Connection conex = ConectarBd.getConnection();
        String sql = "SELECT * FROM `usuarios` WHERE email=? AND clave=?";
        PreparedStatement ps = conex.prepareStatement(sql, 
        		ResultSet.TYPE_SCROLL_SENSITIVE, 
                ResultSet.CONCUR_UPDATABLE);
        
        ps.setString(1, user);
        ps.setString(2, sha256(pass));
        
        ResultSet rs = ps.executeQuery();
        
        if (!rs.next() ) {
            System.out.println("Usuario no encontrado");
            
        }else {
        	if (rs.getString("tipoUsuario").equals("admin")) {
    			System.out.println("Eres Admin");
    			mostrarMenu(true);
    		} else {
    			mostrarMenu(false);
    		}
		}   
	}
	
	public void mostrarMenu(boolean esAdmin) throws SQLException {
		if (esAdmin) {
			System.out.println("1. Mostrar Tablas");
			System.out.println("2. Crear Usuarios");
			System.out.print("Selecciona una opcion: ");
			Scanner sc = new Scanner(System.in);
			int opc = sc.nextInt();
			
			do {
				switch (opc) {
				case 1:
					mostrarTablas();
					break;
				case 2: 
					crearUsuario();
					break;
				case 3:
					System.out.println("Hasta pronto!");
					break;
				default:
					System.out.println("Opcion no valida");
					break;
				}
			} while (opc!=3);
			
		}else {
			mostrarTablas();
		}
	}
	
	//CREAR USUARIO
	public void crearUsuario() throws SQLException {
		
		Scanner sc = new Scanner(System.in);
		System.out.print("ID Usuario: ");
		String numeroUsuario = sc.next();
		while (existData("usuarios", "numeroUsuario", numeroUsuario)) {
			System.out.print("Ese id ya existe introduce otro: ");
			numeroUsuario = sc.next();
		}
		System.out.println(numeroUsuario);
		System.out.print("Email: ");
		String email = sc.next();
		System.out.print("Clave: ");
		String clave = sha256(sc.next());
		System.out.print("Tipo Usuario: ");
		String tipoUsuario = sc.next();
		
		String consulta = "INSERT INTO usuarios (numeroUsuario, email, clave, tipoUsuario) VALUES ('"+numeroUsuario+"','"+email+"','"+clave+"','"+tipoUsuario+"');";
		
		PreparedStatement ps2 = ConectarBd.getConnection().prepareStatement(consulta);
		ps2.executeUpdate(consulta);
		System.out.println("**************");
		System.out.println("Usuario Creado");
		System.out.println("**************");
		
		mostrarMenu(true);
	}
	
	//**********************************************
	//MUESTRA TODAS LAS TABLAS DE LA BASE DE DATOS
	//*********************************************	
	public void mostrarTablas() throws SQLException {

		String sqlSelect = "show tables";

		PreparedStatement ps = ConectarBd.getConnection().prepareStatement(
				sqlSelect,
				ResultSet.TYPE_SCROLL_SENSITIVE,  
				ResultSet.CONCUR_READ_ONLY);
		
		
		ResultSet rs = ps.executeQuery();
		
		rs.last();
		int nFilas = rs.getRow();
		rs.beforeFirst();
		
		if (nFilas>0) {
			while (rs.next()) {
				if (!rs.getString(1).equalsIgnoreCase("Usuarios")) {
					System.out.print(rs.getString(1) + " | ");
				}
				
			}
		}
		System.out.println("");
		System.out.print("Selecciona una tabla para mostrar: ");
		Scanner sc = new Scanner(System.in);
		String tabla = sc.next();
		mostrarTablas(tabla);
	}

	//******************************************	
	//MUESTRA TODOS LOS ELEMENTOS DENTRO DE UNA TABLAS
	//********************************************
	public void mostrarTablas(String tabla) throws SQLException {

		String sqlSelect = "SELECT * FROM "+tabla;

		PreparedStatement ps = ConectarBd.getConnection().prepareStatement(
				sqlSelect,
				ResultSet.TYPE_SCROLL_SENSITIVE,  
				ResultSet.CONCUR_READ_ONLY);
		
		
		ResultSet rs = ps.executeQuery();
		
		rs.last();
		int nFilas = rs.getRow();
		rs.beforeFirst();
		
		int nColumnas = rs.getMetaData().getColumnCount();
		
		for (int i = 1; i <= nColumnas; i++) {
			System.out.print(rs.getMetaData().getColumnName(i) + " | ");
		}
		System.out.println("");
		System.out.println("***************************************************************");
			
		if (nFilas>0) {
			while (rs.next()) {
				
				for (int i = 1; i <= nColumnas; i++) {
					System.out.print(rs.getString(i) + " | ");
					//System.out.print("->"+rs.getMetaData().getColumnTypeName(i)+"<-  | ");
				}
				System.out.println("");
			}
		}
		
	}
	
	//*************************************************************
		//SABER SI EXISTE UN DATO DENTRO DE UNA COLUMNA DE UNA TABLA
		//************************************************************
		public boolean existData(String nombreTabla, String nombreColumna, String dato) throws SQLException {
			
			String sql = "Select "+nombreColumna+" FROM "+nombreTabla+" WHERE "+nombreColumna+" IS NOT NULL;";
			PreparedStatement ps = ConectarBd.getConnection().prepareStatement(
					sql,
					ResultSet.TYPE_SCROLL_SENSITIVE,  
					ResultSet.CONCUR_READ_ONLY);
			
			ResultSet rs = ps.executeQuery();
			
			// Conocer el numero de columnas
			int nColumnas = rs.getMetaData().getColumnCount();
			//Conocer numero de Filas
			rs.last();
			int nFilas = rs.getRow();
			rs.beforeFirst();
			
			if (nFilas>0) {
				while (rs.next()) {
					if (rs.getString(1).toString().equals(dato)) {
						return true;
					}
				}
			}
			
			
			return false;
		}
	//*********************
		public static String sha256(String input) {
	        try {
	            MessageDigest digest = MessageDigest.getInstance("SHA-256");
	            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

	            // Convertir el arreglo de bytes a representación hexadecimal
	            StringBuilder hexString = new StringBuilder();
	            for (byte b : hash) {
	                String hex = Integer.toHexString(0xff & b);
	                if (hex.length() == 1)
	                    hexString.append('0');
	                hexString.append(hex);
	            }
	            return hexString.toString();
	        } catch (NoSuchAlgorithmException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	
	
	
}
