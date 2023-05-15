import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Scanner;

public class GestionClassicModels {

	
	public void loginUsuario() throws NoSuchAlgorithmException {
		Scanner sc = new Scanner(System.in);
		System.out.print("Usuario: ");
		String user = sc.next();
		
		System.out.print("Contraseña: ");
		String pass = sc.next();

		SHA256 sha256 = new SHA256();
		pass = sha256.sha256(pass);
		
		
		
	}
	
	
public boolean comprobarUsuario(String user, String clave) throws SQLException {
		
		ResultSet selectUsers;
		selectUsers = selectUsuarios(user,clave);
		
		selectUsers.last();
		int nFilas = selectUsers.getRow();
		selectUsers.beforeFirst();
		
		if (nFilas>0) {
			while (selectUsers.next()) {
				if (selectUsers.getString("email").equals(user)) {
					System.out.println("Existe!");
					if (selectUsers.getString("clave").equals(clave)) {
						return true;
						
					} else {
						System.out.println("Clave Erronea");
					}
				} else {
					//System.out.println("Error");
				}
				
			}	
		}
		return false;
	}
	
	
	public static ResultSet selectUsuarios(String user, String clave) throws SQLException {

		String sqlSelect = "SELECT numeroUsuario, email, clave, tipoUsuario FROM usuarios";

		PreparedStatement ps = ConectarBd.getConnection().prepareStatement(
				sqlSelect,
				ResultSet.TYPE_SCROLL_SENSITIVE,  
				ResultSet.CONCUR_READ_ONLY);
		
		//ps.setString(1, user);

		return ps.executeQuery();
	}
	
	//*******************************************************************
	
	//AÑADIR ELEMENTO
	public void addElemento(String tabla) {
		
	}
	
	
	
	
	
	
	
	//*****************************************************************
	//MUESTRA TODAS LAS TABLAS DE LA BASE DE DATOS
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
				System.out.print(rs.getString(1) + " | ");
			}
		}
		System.out.println("");
		System.out.print("Selecciona una tabla para mostrar: ");
		Scanner sc = new Scanner(System.in);
		String tabla = sc.next();
		mostrarTablas(tabla);
	}
	
	
	
	
	//MUESTRA LOS ELEMENTOS DENTRO DE LAS TABLAS
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
		
}
