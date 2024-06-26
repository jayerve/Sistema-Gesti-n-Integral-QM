/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_facturacion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import paq_facturacion.ejb.ServicioFacturacion;
import paq_sistema.aplicacion.Pantalla;
import paq_tesoreria.ejb.ServicioTesoreria;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Encriptar;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.Tabla;

/**
 *
 * @author AWBECERRA
 */
public class pre_archivo_banco extends Pantalla {

    
    private Tabla tab_tabla = new Tabla();

	@EJB
	private ServicioTesoreria ser_tesoreria = (ServicioTesoreria ) utilitario.instanciarEJB(ServicioTesoreria.class);
	
    public pre_archivo_banco() { 
    	
    	bar_botones.limpiar();
		
		Boton bot_filtrar = new Boton();
		bot_filtrar.setValue("Actualizar Consulta");
		bot_filtrar.setMetodo("seleccionaOpcion");
		bot_filtrar.setIcon("ui-icon-refresh");
		bar_botones.agregarBoton(bot_filtrar);
		
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarBoton(bot_limpiar);
	
		Boton bot_generar2= new Boton();
		bot_generar2.setValue("Generar Archivo Transferencia Pichincha");
		bot_generar2.setMetodo("generarArchivoPic");
		bot_generar2.setAjax(false);
		bar_botones.agregarBoton(bot_generar2);
		
        tab_tabla.setId("tab_tabla");
        tab_tabla.setSql(ser_tesoreria.getSqlDeudaClientesArchivo("-1"));
        tab_tabla.getColumna("tipo_id").setLongitud(10);
        tab_tabla.getColumna("doc_identidad").setLongitud(30);
        tab_tabla.getColumna("razon_social_recli").setNombreVisual("CLIENTE");
        tab_tabla.getColumna("detalle_bogrm").setNombreVisual("SERVICIO");
        tab_tabla.setColumnaSuma("valor,interes");
        tab_tabla.setLectura(true);
        tab_tabla.setRows(30);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
    }
    
    public void seleccionaOpcion (){
				
		tab_tabla.setSql(ser_tesoreria.getSqlDeudaClientesArchivo("1"));
		tab_tabla.ejecutarSql();
				
		utilitario.addUpdate("tab_tabla");
	}
   
	
	public void generarArchivoPic(){		
		if(tab_tabla.getValor("codigo")==null){
			utilitario.agregarMensajeInfo("No se puede generar el Archivo", "No existen registros para la generación del archivo");
			return;
		}
		
		String strDeuda="0.00";	
		
		//Crear objeto FileWriter que sera el que nos ayude a escribir sobre archivo
		try {
			//StringBuilder str_spi=new StringBuilder();
			String str_nombre_archivo="Banco_Pichincha";
			//String str_localidad=utilitario.getVariable("p_fac_localidad_archivo_banco");
			//String str_transaccion =utilitario.getVariable("p_fac_transaccion_archivo_banco");
			String str_codservicio="CO";
			String str_referencia="FACTURAS E INTERESES";
			String str_formapago=utilitario.getVariable("p_fac_formapago_archivo_banco")+"C";
			String str_moneda=utilitario.getVariable("p_fac_moneda_archivo_banco");
				
			
			ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
			String fileName= str_nombre_archivo+utilitario.getFechaActual(); //utilitario.getNombreMes(utilitario.getMes(str_fecha_hora))+utilitario.getAnio(str_fecha_hora);
			String path= extContext.getRealPath("/");			

			File archivotxt=new File(path+fileName.concat(".txt"));
			BufferedWriter escribir=new BufferedWriter(new FileWriter(archivotxt)); 
	
			tab_tabla.ejecutarSql();			
			for(int i=0;i<tab_tabla.getTotalFilas();i++){
				
				strDeuda = pckUtilidades.CConversion.CStr(pckUtilidades.CConversion.CDbl_2(pckUtilidades.CConversion.CDbl_2(tab_tabla.getValor(i, "valor")) 
									+ pckUtilidades.CConversion.CDbl_2(tab_tabla.getValor(i, "interes"))));
				String[] array = strDeuda.split(Pattern.quote("."));
				
				StringBuilder str_detalle=new StringBuilder();
				str_detalle.append(str_codservicio).append("\t");
				str_detalle.append(tab_tabla.getValor(i, "doc_identidad")).append("\t");
				str_detalle.append(str_moneda).append("\t");
				str_detalle.append(array[0] + pckUtilidades.Utilitario.padRight(array[1], "0", 2) ).append("\t");

				str_detalle.append(str_formapago).append("\t").append("\t").append("\t");
				str_detalle.append(str_referencia).append("\t");
				str_detalle.append(tab_tabla.getValor(i, "tipo_id")).append("\t");
				str_detalle.append(tab_tabla.getValor(i, "doc_identidad")).append("\t");		
				str_detalle.append(tab_tabla.getValor(i, "razon_social_recli")).append("\t");

				String str_spi_encr="";
				try {
					 str_spi_encr=str_detalle.toString();
					str_spi_encr=str_spi_encr.replaceAll("Á", "A");
					str_spi_encr=str_spi_encr.replaceAll("É", "E");
					str_spi_encr=str_spi_encr.replaceAll("Í", "I");
					str_spi_encr=str_spi_encr.replaceAll("Ó", "O");
					str_spi_encr=str_spi_encr.replaceAll("Ú", "U");
					str_spi_encr=str_spi_encr.replaceAll("á", "a");
					str_spi_encr=str_spi_encr.replaceAll("é", "e");
					str_spi_encr=str_spi_encr.replaceAll("í", "i");
					str_spi_encr=str_spi_encr.replaceAll("ó", "o");
					str_spi_encr=str_spi_encr.replaceAll("ú", "u");
					str_spi_encr=str_spi_encr.replaceAll("Ñ", "N");
					str_spi_encr=str_spi_encr.replaceAll("ñ", "n");
				} catch (Exception e) {
					// TODO: handle exception
				}				
				//str_spi.append(str_spi_encr);
				escribir.write(str_spi_encr.toString());
				escribir.newLine();

			}
			escribir.close();
			String str_spi_encr="";
			BufferedReader entrada;
			try {
			entrada = new BufferedReader( new FileReader( archivotxt ) );
			String linea;
			while(entrada.ready()){
			linea = entrada.readLine();
			if(str_spi_encr.isEmpty()==false){
				str_spi_encr+="\r\n";
			}
			str_spi_encr+=linea;
			}
			}catch (IOException e) {
			e.printStackTrace();
			}
			
			
			//System.out.println("str spi "+str_spi_encr.toString());
			utilitario.crearArchivo(new File[]{archivotxt}, fileName.concat(".txt"));						
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("No Generado: strDeuda " + strDeuda);
			utilitario.agregarMensajeError("No Generado", "Error..."+e.getMessage());
			utilitario.crearError("No Generado", "xxss", e);
		}
	}
	/**
	 * limpia toda la pantalla incluyendo el autocompletar
	 */
	public void limpiar() {
		tab_tabla.limpiar();	
		utilitario.addUpdate("tab_tabla");// limpia y refresca el autocompletar
	}

    @Override
    public void insertar() {
        tab_tabla.insertar();
    }

    @Override
    public void guardar() {
        tab_tabla.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        tab_tabla.eliminar();
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

    
}
