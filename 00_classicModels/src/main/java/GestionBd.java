import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class GestionBd {

	//*********************************
	//AÑADIR UN ELEMENTO A UNA TABLA
	//************************************
	public void addElementoTabla(String tabla) throws SQLException {
		String sqlSelect = "SELECT * FROM "+tabla;
		PreparedStatement ps = ConectarBd.getConnection().prepareStatement(
				sqlSelect,
				ResultSet.TYPE_SCROLL_SENSITIVE,  
				ResultSet.CONCUR_READ_ONLY);
		
		ResultSet rs = ps.executeQuery();
		
		// Conocer numero de filas de la consulta
		rs.last();
		int nFilas = rs.getRow();
		rs.beforeFirst();
		
		// Conocer el numero de columnas de la consulta
		int nColumnas = rs.getMetaData().getColumnCount();
		
		String[] tipoColumnas;
		tipoColumnas = new String[nColumnas];
		String[] nombreColumnas;
		nombreColumnas = new String[nColumnas];
		
		String[] nuevoElemento = new String[nColumnas];
		
		Scanner sc = new Scanner(System.in);
		for (int i = 1; i <= nColumnas; i++) {	
			if (rs.getMetaData().getColumnTypeName(i).equals("VARCHAR")) {
				tipoColumnas[i-1] = "String"; 
				nombreColumnas[i-1] = rs.getMetaData().getColumnName(i);
				
				System.out.print(tipoColumnas[i-1] + " | " + nombreColumnas[i-1]);
				
				System.out.println("");
				System.out.print(rs.getMetaData().getColumnName(i)+": ");
				
				nuevoElemento[i-1] = sc.next();
				if (isPrimaria(tabla,nombreColumnas[i-1])) {
					do {
						System.out.println("Esa clave primaria ya existe, introduce otra");
						System.out.print(rs.getMetaData().getColumnName(i)+": ");
						nuevoElemento[i-1] = sc.next();
						
					} while (existData(tabla,nombreColumnas[i-1],nuevoElemento[i-1]));
				} else if (isForanea(tabla,nombreColumnas[i-1])) {
					do {
						System.out.println("Esa clave foranea no existe, introduce otra");
						System.out.print(rs.getMetaData().getColumnName(i)+": ");
						nuevoElemento[i-1] = sc.next();
					} while (!existData(tabla,nombreColumnas[i-1],nuevoElemento[i-1]));
				}
				
				
			} else if (rs.getMetaData().getColumnTypeName(i).equals("INT")) {
				tipoColumnas[i-1] = "int"; 
				nombreColumnas[i-1] = rs.getMetaData().getColumnName(i);
			
				System.out.print(tipoColumnas[i-1] + " | " + nombreColumnas[i-1]);
				
				System.out.println("");
				System.out.print(rs.getMetaData().getColumnName(i)+": ");
				nuevoElemento[i-1] = sc.next();				
			
				//COMPRUEBA SI ES PRIMARIA O FORANEA
				if (isPrimaria(tabla,nombreColumnas[i-1])) {
					do {
						System.out.println("Esa clave primaria ya existe, introduce otra");
						System.out.print(rs.getMetaData().getColumnName(i)+": ");
						nuevoElemento[i-1] = sc.next();
						
					} while (existData(tabla,nombreColumnas[i-1],nuevoElemento[i-1]));
				} else if (isForanea(tabla,nombreColumnas[i-1])) {
					do {
						System.out.println("Esa clave foranea no existe, introduce otra");
						System.out.print(rs.getMetaData().getColumnName(i)+": ");
						nuevoElemento[i-1] = sc.next();
					} while (!existData(tabla,nombreColumnas[i-1],nuevoElemento[i-1]));
				}
				
				//COMO ES UN NUMERO CONVERTIMOS LA CADENA A INT
				int numero = Integer.parseInt(nuevoElemento[i-1]);
				
			} else if (rs.getMetaData().getColumnTypeName(i).equals("DECIMAL")) {
				tipoColumnas[i-1] = "double"; 
				nombreColumnas[i-1] = rs.getMetaData().getColumnName(i);
			
				System.out.print(tipoColumnas[i-1] + " | " + nombreColumnas[i-1]);
				
				System.out.println("");
				System.out.print(rs.getMetaData().getColumnName(i)+": ");
				nuevoElemento[i-1] = sc.next();
				
				//COMPRUEBA SI ES PRIMARIA O FORANEA
				if (isPrimaria(tabla,nombreColumnas[i-1])) {
					do {
						System.out.println("Esa clave primaria ya existe, introduce otra");
						System.out.print(rs.getMetaData().getColumnName(i)+": ");
						nuevoElemento[i-1] = sc.next();
						
					} while (existData(tabla,nombreColumnas[i-1],nuevoElemento[i-1]));
						//COMPRUEBA SI LA CLAVE ES FORANEA
				} else if (isForanea(tabla,nombreColumnas[i-1])) {
					do {
						System.out.println("Esa clave foranea no existe, introduce otra");
						System.out.print(rs.getMetaData().getColumnName(i)+": ");
						nuevoElemento[i-1] = sc.next();
						
					} while (!existData(tabla,nombreColumnas[i-1],nuevoElemento[i-1]));
				}
				
				
				//COMO ES DOUBLE CONVERTIMOS LA CADENA A DOUBLE
				double numero = Double.parseDouble(nuevoElemento[i-1]);
			}
		}

				
		// CONSTRUIR CONSULTA DINAMICA  STRINGBUILDER
		// NOMBRES DE TABLA
        StringBuilder queryBuilder = new StringBuilder("INSERT INTO " + tabla + " ");
        boolean isFirstColumn = true;
       
		for (int i = 1; i <= nColumnas; i++) {
			String columnName = nombreColumnas[i-1];
			
            if (isFirstColumn) {
                isFirstColumn = false;
                queryBuilder.append("(");
            } else {
                queryBuilder.append(", ");
            }

            queryBuilder.append(columnName);
            if (i  == nColumnas) {
				queryBuilder.append(") ");
			} 
		}
		
		//VALORES PARA AÑADIR EN LA TABLA

        StringBuilder valuesBuilder = new StringBuilder("VALUES ");
        isFirstColumn = true;
       
		for (int i = 1; i <= nColumnas; i++) {
			String elementName = nuevoElemento[i-1];
			
            if (isFirstColumn) {
                isFirstColumn = false;
                valuesBuilder.append("(");
            } else {
                valuesBuilder.append(", ");
            }

            
            //Si el elemtno es un String le añadimos comillas antes y despues
            if (tipoColumnas[i-1].toString().equals("String")) {
            	valuesBuilder.append("'");
			}
            valuesBuilder.append(elementName);
            if (tipoColumnas[i-1].toString().equals("String")) {
            	valuesBuilder.append("'");
			}
            if (i  == nColumnas) {
				valuesBuilder.append(");");
			} 
		}
		
		
		System.out.println("CONSULTA QUE SE REALIZA");
		
		String consulta = queryBuilder.toString() + valuesBuilder.toString();
		System.out.println(consulta);

		PreparedStatement ps2 = ConectarBd.getConnection().prepareStatement(consulta);
		ps2.executeUpdate(consulta);		
		
		//System.out.println(queryBuilder.toString());
		
//		for (int i = 1; i < nColumnas; i++) {
//			System.out.println(tipoColumnas[i-1] + " | " + nombreColumnas[i-1]);
//			
//		}

		
		System.out.println("");
		System.out.println("***************************************************************");
		

	}
	
	
	
	
	
	
	
	
	//******************************** 
	//CONOCER CLAVE PRIMARIA
	//********************************
	public boolean isPrimaria(String tabla, String nombreColumna) throws SQLException {
		String sql = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE TABLE_NAME = '"+tabla+"' AND CONSTRAINT_NAME = 'PRIMARY';"; 
		//System.out.println(sql);
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
				for (int i = 1; i <= nColumnas; i++) {
					//System.out.print(rs.getMetaData().getColumnName(i) + " | ");
					//System.out.println(rs.getString(i));
					if (rs.getString(i).equals(nombreColumna)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	
	//*************************
	//CONOCER CLAVE FORANEA
	//*************************
	public boolean isForanea(String tabla, String nombreColumna) throws SQLException {
		String sql = "SELECT CONSTRAINT_NAME, COLUMN_NAME, REFERENCED_TABLE_NAME, REFERENCED_COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE TABLE_NAME = '"+tabla+"' AND REFERENCED_TABLE_NAME IS NOT NULL;";
		//System.out.println(sql);
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
				for (int i = 1; i <= nColumnas; i++) {
					//System.out.print(rs.getMetaData().getColumnName(i) + " | ");
					//System.out.println(rs.getString(i));
					if (rs.getString(i).equals(nombreColumna)) {
						return true;
					}
				}
			}
		}
		return false;
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
	
	
	//*****************************************************************
	//MUESTRA EL NOMBRE Y EL TIPO DE DATO DE UNA TABLA
	//*****************************************************************
	public void mostrarInfoTabla(String tabla) throws SQLException {
		String sqlSelect = "SELECT * FROM "+tabla;

		PreparedStatement ps = ConectarBd.getConnection().prepareStatement(
				sqlSelect,
				ResultSet.TYPE_SCROLL_SENSITIVE,  
				ResultSet.CONCUR_READ_ONLY);
		
		
		ResultSet rs = ps.executeQuery();
		
		// Conocer numero de filas de la consulta
		rs.last();
		int nFilas = rs.getRow();
		rs.beforeFirst();
		
		// Conocer el numero de columnas
		int nColumnas = rs.getMetaData().getColumnCount();
		
		String[] tipoColumnas;
		tipoColumnas = new String[nColumnas];
		String[] nombreColumnas;
		nombreColumnas = new String[nColumnas];		
		
		for (int i = 1; i <= nColumnas; i++) {
			
			if (rs.getMetaData().getColumnTypeName(i).equals("VARCHAR") || rs.getMetaData().getColumnTypeName(i).equals("TEXT")) {
				tipoColumnas[i-1] = "String"; 
				nombreColumnas[i-1] = rs.getMetaData().getColumnName(i);
				
				System.out.print(tipoColumnas[i-1] + " | " + nombreColumnas[i-1]);
				
			} else if (rs.getMetaData().getColumnTypeName(i).equals("INT")) {
				tipoColumnas[i-1] = "int"; 
				nombreColumnas[i-1] = rs.getMetaData().getColumnName(i);
			
				System.out.print(tipoColumnas[i-1] + " | " + nombreColumnas[i-1]);
				
			
			} else if (rs.getMetaData().getColumnTypeName(i).equals("DECIMAL")) {
				tipoColumnas[i-1] = "double"; 
				nombreColumnas[i-1] = rs.getMetaData().getColumnName(i);
			
				System.out.print(tipoColumnas[i-1] + " | " + nombreColumnas[i-1]);
				
			} else if (rs.getMetaData().getColumnTypeName(i).equals("DATE")) {
				tipoColumnas[i-1] = "LocalDate";
				
				nombreColumnas[i-1] = rs.getMetaData().getColumnName(i);
				
				System.out.print(tipoColumnas[i-1] + " | " + nombreColumnas[i-1]);
			}
			//COMPRUEBA SI LA CLAVE ES PRIMARIA o FORANEA
			if (isPrimaria(tabla,nombreColumnas[i-1])) {
				System.out.print(" *P");
				//System.out.println("");
			} else if (isForanea(tabla,nombreColumnas[i-1])) {
				System.out.print(" *F");
				//System.out.println("");
			} {
				System.out.println("");
			}
			
		}

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
				System.out.print(rs.getString(1) + " | ");
			}
		}
//		System.out.println("");
//		System.out.print("Selecciona una tabla para mostrar: ");
//		Scanner sc = new Scanner(System.in);
//		String tabla = sc.next();
//		mostrarTablas(tabla);
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
	
	
}
