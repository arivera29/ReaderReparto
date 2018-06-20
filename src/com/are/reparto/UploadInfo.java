package com.are.reparto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

public class UploadInfo {

    private static ArrayList<Configuracion> configuracion;
    private static String[] fields;
    private static db conexion = null;

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        String cFields = "NIC,SIMBOLO_VARIABLE,FECHA_VENCIMIENTO,FECHA_PAGO_OPORTUNO,FECHA_EMISION,NUMERO_FACTURA,INDICATIVO_SUMINISTRO,CIIU,TIPO_COBRO,SECUENCIAL_RECIBO,NIS,TIPO_SERVICIO,ORDEN_LECTURA,MEDIDOR,LECT_ANTERIOR,LECT_ACTUAL,FECHA_LECTURA_ACTUAL,CONSUMO_FACTURADO,VALOR_FACTURA,GRUPO_FAMILIAR,PRECIO_UNITARIO_CU,IND_VISUSALIZACION_BARCODI,IMPORTE_TOTAL_DEUDA,CANTIDAD_FACTURAS_ADEUDADAS,DESC_ANOMALIA_LECTURA,IMPORTE_TOTAL_FACTURA,SUBTOTAL_FACTURA,DESCRIPCION_TARIFA,CONSUMO_PROMEDIO,DIAS_FACTURADOS,CLAVE_ORDEN_FACTURA,PROPIEDAD_EQUIPO_MEDIDA,UNICOM,RUTA,ITINERARIO";
        fields = cFields.split(",");

        readConfiguracion();

        if (configuracion.size() != fields.length) {
            System.out.println("Longitud de campos y configuración es diferente. Fields: " + fields.length + " Configuracion: " + configuracion.size());
            Utilidades.AgregarLog("Longitud de campos y configuración es diferente. Fields: " + fields.length + " Configuracion: " + configuracion.size());
            return;
        }

        if (configuracion.size() > 0) {
            System.out.println("Campos a leer: " + configuracion.size());
            Utilidades.AgregarLog("Campos a leer: " + configuracion.size());
            String ruta = "C:\\REPARTO\\";
            File directorio = new File(ruta);
            Utilidades.AgregarLog("Descomprimiendo archivos ruta:" + ruta);
            DescomprimirArchivosCarpeta(directorio);
            Utilidades.AgregarLog("Procesando archivos de reparto en ruta:" + ruta);
            ProcesarDirectorio(directorio);

        } else {
            System.out.println("No se ha cargado la configuracion");
            Utilidades.AgregarLog("No se ha cargado la configuracion");
        }

    }

    public static boolean GuardarRegistro(String[] fila, String filename) {
        boolean resultado = false;
        File f = new File(filename);
        String sql = "";
        try {
            sql = "INSERT INTO reparto (";

            for (int x = 0; x < fields.length; x++) {
                sql += fields[x] + ",";

            }
            sql += "FECHA_CARGA, USUARIO_CARGA, FILE_ORIGEN) VALUES (";

            for (int x = 0; x < fields.length; x++) {
                sql += "?,";

            }
            sql += "SYSDATETIME(), 'reader',?)";
            System.out.println("Guardando registro.");
            Utilidades.AgregarLog("Guardando registro.");
            java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
            for (int x = 0; x < fila.length; x++) {
                if (fields[x].equals("IMPORTE_TOTAL_DEUDA")
                        || fields[x].equals("IMPORTE_TOTAL_FACTURA")
                        || fields[x].equals("SUBTOTAL_FACTURA")
                        || fields[x].equals("CONSUMO_PROMEDIO")
                        || fields[x].equals("CONSUMO_PROMEDIO")) {
                    fila[x] = fila[x].replace(".", "");
                    fila[x] = fila[x].replace(",", ".");
                }
                pst.setString(x+1, fila[x].trim());
            }
            pst.setString(fila.length+1, filename);

            if (conexion.Update(pst) > 0) {
                System.out.println("Registro guardado correctamente");
                Utilidades.AgregarLog("Registro guardado correctamente");
                conexion.Commit();
                resultado = true;
            }

        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getErrorCode() + ", " + ex.getMessage());
            Utilidades.AgregarLog("Error: " + ex.getErrorCode() + ", " + ex.getMessage());
        }

        return resultado;
    }

    public static void readConfiguracion() {
        configuracion = new ArrayList<Configuracion>();
        String rutaFileConfiguracion = "C:\\LECTURAS\\configuracion.txt";
        String cadena;
        FileReader f;
        try {
            f = new FileReader(rutaFileConfiguracion);
            BufferedReader b = new BufferedReader(f);
            while ((cadena = b.readLine()) != null) {
                String[] fila = cadena.split("	");
                if (fila.length == 3) {
                    Configuracion c = new Configuracion();
                    c.setNombre(fila[0]);
                    c.setPosInicial(Integer.parseInt(fila[1]));
                    c.setPosFinal(Integer.parseInt(fila[2]));
                    configuracion.add(c);
                }

            }
            b.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Error: " + e.getMessage());
            Utilidades.AgregarLog("Error: " + e.getMessage());
        }
    }

    private static void EscribirArchivo(String fila) {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter("C:\\LECTURAS\\SALIDA.TXT", true);
            String finLinea = "\r\n";
            pw = new PrintWriter(fichero);
            pw.append(fila + finLinea);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fichero != null) {
                try {
                    fichero.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public static void ProcesarDirectorio(File directorio) {
        System.out.println("Procesando Directorio: " + directorio.getPath());
        Utilidades.AgregarLog("Procesando Directorio: " + directorio.getPath());
        File[] ficheros = directorio.listFiles();
        for (File fichero : ficheros) {
            if (fichero.isDirectory()) {
                ProcesarDirectorio(fichero);
            } else {
                System.out.println("Validando nombre archivo: " + fichero.getName().substring(0, 7));
                Utilidades.AgregarLog("Validando nombre archivo: " + fichero.getName().substring(0, 7));
                if (!getExtension(fichero.getName()).equals(".Z")) {
                    System.out.println("Procesando archivo: " + fichero.getPath());
                    Utilidades.AgregarLog("Procesando archivo: " + fichero.getPath());
                    ProcesarArchivo(fichero.getPath());
                
                }else {
                    System.out.println("Archivo: " + fichero.getPath() + " DESCARTADO...");
                    Utilidades.AgregarLog("Archivo: " + fichero.getPath() + " DESCARTADO...");
                }
                /*
                if (fichero.getName().substring(0, 7).equals("FNORMAL")) {
                    System.out.println("Validando Extension archivo: " + getExtension(fichero.getName()));
                    Utilidades.AgregarLog("Validando Extension archivo: " + getExtension(fichero.getName()));
                    if (!getExtension(fichero.getName()).equals(".Z")) {
                        // Procesa archivo no comprimido
                        System.out.println("Procesando archivo: " + fichero.getPath());
                        Utilidades.AgregarLog("Procesando archivo: " + fichero.getPath());
                        ProcesarArchivo(fichero.getPath());
                    } else {
                        // Descomprimir y procesar.
                        System.out.println("Descomprimir archivo: " + fichero.getPath());
                        Utilidades.AgregarLog("Descomprimir archivo: " + fichero.getPath());

                    }
                } else {
                    System.out.println("Archivo: " + fichero.getPath() + " DESCARTADO...");
                    Utilidades.AgregarLog("Archivo: " + fichero.getPath() + " DESCARTADO...");
                }
                        */
            }
        }

    }

    public static void ProcesarArchivo(String filename) {
        String cadena;
        FileReader f;

        try {
            f = new FileReader(filename);
            BufferedReader b = new BufferedReader(f);
            int cont = 0;
            conexion = new db();
            while ((cadena = b.readLine()) != null) {
                cont++;
                System.out.println("Longitud de la cadena: " + cadena.length() + " Fila: " + cont);
                Utilidades.AgregarLog("Longitud de la cadena: " + cadena.length() + " Fila: " + cont);
                if (cadena.length() > 8000) {
                    if (cadena.substring(0, 1).equals("1")) {
                        String[] fila = new String[fields.length];
                        for (int x = 0; x < configuracion.size(); x++) {
                            Configuracion c = configuracion.get(x);
                            //System.out.println("Leyendo campo: " + c.getNombre() + " PosIni: " + c.getPosInicial() + " PosFin: " + c.getPosFinal());
                            String campo = cadena.substring(c.getPosInicial() - 1, c.getPosFinal()-1);
                            //System.out.println(fields[x] + "=" + campo);
                            //Utilidades.AgregarLog(fields[x] + "=" + campo);
                            fila[x] = campo;
                        }
                        GuardarRegistro(fila, filename);
                    }
                }

            }
            b.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Error: " + e.getMessage());
            Utilidades.AgregarLog("Error: " + e.getMessage());
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getErrorCode() + ", " + ex.getMessage());
            Utilidades.AgregarLog("Error: " + ex.getErrorCode() + ", " + ex.getMessage());
        } finally {
            if (conexion != null) {
                try {
                    conexion.Close();
                } catch (SQLException ex) {
                    System.out.println("Error: " + ex.getErrorCode() + ", " + ex.getMessage());
                    Utilidades.AgregarLog("Error: " + ex.getErrorCode() + ", " + ex.getMessage());
                }
            }
        }

    }

    public static String getExtension(String name) {
        String extension = "";
        if (name.lastIndexOf('.') > 0) {
            // get last index for '.' char
            int lastIndex = name.lastIndexOf('.');
            // get extension
            extension = name.substring(lastIndex);
        }
        return extension;
    }

    public static void DescomprimirArchivosCarpeta(File directorio) {
        System.out.println("Procesando Directorio: " + directorio.getPath());
        Utilidades.AgregarLog("Procesando Directorio: " + directorio.getPath());
        File[] ficheros = directorio.listFiles();
        for (File fichero : ficheros) {
            if (fichero.isDirectory()) {
                DescomprimirArchivosCarpeta(fichero);
            } else {
                if (getExtension(fichero.getName()).equals(".Z")) {
                    // Procesa archivo no comprimido
                    System.out.println("Descomprimiendo archivo: " + fichero.getPath());
                    Utilidades.AgregarLog("Descomprimiendo archivo: " + fichero.getPath());
                    Utilidades.DescomprimirArchivo(fichero.getPath(), fichero.getParent());

                }
            }
        }
    }

}
