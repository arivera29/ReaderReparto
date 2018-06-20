/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.reparto;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Aimer
 */
public class Utilidades {
    
    public static void AgregarLog(String log) {
		FileWriter fichero = null;
		java.util.Date f1 = new Date();
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String fileLog = "REPARTO_" + df.format(f1) + ".txt";
		try {
			fichero = new FileWriter(fileLog,true);
		} catch (IOException e1) {
		}
		
		if (fichero != null) {
			PrintWriter pw = null;
	        try
	        {
	        	String tab = "	";
	            pw = new PrintWriter(fichero);
	            java.util.Date fecha = new Date();
	    		df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	    		pw.append(df.format(fecha) + tab + log + "\r\n" );
	    		fichero.close();
	
	        } catch (Exception e) {
	        }
	        
		}
	}
    
    public static boolean DescomprimirArchivo(String pathOrigen, String dirDestino) {
		boolean result = false;
		//String comando = "C:\\Program Files\\7-Zip\\7z.exe X -yo"
		//		+ dirDestino + " " + pathOrigen;
                
                String comando = "7z.exe X -y -o"
				+ dirDestino + " " + pathOrigen;
                        
		String salida = null;
		boolean descomprimido = false;
		System.out.println("Comando: " + comando);
		AgregarLog("Comando: " + comando);
		try {

			Process proceso = Runtime.getRuntime().exec(comando);
			InputStreamReader entrada = new InputStreamReader(proceso.getInputStream());
			BufferedReader stdInput = new BufferedReader(entrada);

			// Si el comando tiene una salida la mostramos
			if ((salida = stdInput.readLine()) != null) {
				System.out.println("Comando ejecutado Correctamente");
				AgregarLog("Comando ejecutado Correctamente");
				while ((salida = stdInput.readLine()) != null) {
					System.out.println(salida);
					AgregarLog(salida);
					if (salida.contains("Everything is Ok")) {
						descomprimido = true;
					}
				}

				if (descomprimido) {
					System.out.println("Archivo descomprimido");
					AgregarLog("Archivo descomprimido");
					/*File file = new File(pathOrigen);
					if (file.exists()) {
						//file.delete();
						System.out.println("Archivo comprimido Eliminado "+ file.getPath());
						AgregarLog("Archivo comprimido Eliminado "+ file.getPath());
					}
					*/
					result = true;
				}

			} else {
				System.out.println("No se ha producido ninguna salida");
				AgregarLog("No se ha producido ninguna salida");
			}
			
			entrada.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	
    
}
