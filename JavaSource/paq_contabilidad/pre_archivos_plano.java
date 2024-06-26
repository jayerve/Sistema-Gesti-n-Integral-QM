package paq_contabilidad;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Check;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_general.ejb.ServicioGeneral;
import paq_sistema.aplicacion.Pantalla;

public class pre_archivos_plano extends Pantalla {
	
	private Tabla tab_tabla1 = new Tabla();
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
	private SeleccionTabla sel_transaccion= new SeleccionTabla();
	private SeleccionCalendario sec_importar=new SeleccionCalendario();
	private String strTipoCedula="NA";
	
	@EJB
	private ServicioGeneral ser_general = (ServicioGeneral ) utilitario.instanciarEJB(ServicioGeneral.class);
	@EJB
    private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	
	public pre_archivos_plano() {
		
		bar_botones.limpiar();
		
		bar_botones.agregarComponente(new Etiqueta("Fecha Inicio :"));
		cal_fecha_inicial.setValue(utilitario.obtenerFechaInicioAnio());
		bar_botones.agregarComponente(cal_fecha_inicial);
		
		bar_botones.agregarComponente(new Etiqueta("Fecha Corte :"));
		cal_fecha_final.setValue(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		bar_botones.agregarComponente(cal_fecha_final);
		
		Boton bot_gast = new Boton();
		bot_gast.setValue("Ver Gasto");
		bot_gast.setMetodo("ced_gasto");
		bar_botones.agregarBoton(bot_gast);
		
		Boton bot_ingreso = new Boton();
		bot_ingreso.setValue("Ver Ingreso");
		bot_ingreso.setMetodo("ced_ingreso");
		bar_botones.agregarBoton(bot_ingreso);
		
		Boton bot_gast_ing= new Boton();
		bot_gast_ing.setValue("Ver Gasto/Ingreso");
		bot_gast_ing.setMetodo("ced_gasto_ingreso");
		bar_botones.agregarBoton(bot_gast_ing);
		
		Boton bot_generar1 = new Boton();
		bot_generar1.setValue("Generar Archivo Inicial");
		bot_generar1.setMetodo("generar_archivoI");
		bot_generar1.setAjax(false);
		bar_botones.agregarBoton(bot_generar1);
		
		Boton bot_generar = new Boton();
		bot_generar.setValue("Generar Archivo Cédulas");
		bot_generar.setMetodo("generar_archivo");
		bot_generar.setAjax(false);
		bar_botones.agregarBoton(bot_generar);
		
		Boton bot_generarCedula_bde = new Boton();
		bot_generarCedula_bde.setValue("Generar Archivo Cédulas BDE");
		bot_generarCedula_bde.setMetodo("generar_archivo_cedulas_bde");
		bot_generarCedula_bde.setAjax(false);
		bar_botones.agregarBoton(bot_generarCedula_bde);

		Boton bot_generar_trans = new Boton();
		bot_generar_trans.setValue("Generar Archivo Transfer.");
		bot_generar_trans.setMetodo("generar_archivo_trans");
		//bot_generar_trans.setAjax(false);
		bar_botones.agregarBoton(bot_generar_trans);
		
		tab_tabla1.setId("tab_tabla1");
		tab_tabla1.setSql(ser_contabilidad.getCedGastoIngreso(ser_general.getEjecucionPresupuestaria("1900-01-01","1900-01-01"),ser_general.getEjecucionPresupuestariaIngreso("1900-01-01","1900-01-01"),"T"));
		tab_tabla1.setColumnaSuma("inicial,reforma,codificado,compromiso,devengado,pago,cobrado,saldo_comp,saldo_dev,saldo_x_dev");
		tab_tabla1.setRows(30);
		tab_tabla1.setLectura(true);
		tab_tabla1.dibujar();
		
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_tabla1);
		
		Division div_tabla = new Division();
		div_tabla.dividir1(pat_panel);
		agregarComponente(div_tabla);
		
		sel_transaccion.setId("sel_transaccion");
		sel_transaccion.setTitle("TRANSFERENCIAS");
		sel_transaccion.setSeleccionTabla(ser_contabilidad.getTransferencias("1900-01-01", "1900-01-01"), "codigo");
		sel_transaccion.getTab_seleccion().ejecutarSql();
		sel_transaccion.getBot_aceptar().setMetodo("generar_archivo_trans");
		sel_transaccion.getBot_aceptar().setAjax(false);
		agregarComponente(sel_transaccion);
		
		sec_importar.setTitle("SELECCION DE FECHAS");
		sec_importar.setFooter("Seleccione un Rango de fechas para Buscar Transferencias");
		sec_importar.setFecha1(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		sec_importar.setFecha2(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		sec_importar.getBot_aceptar().setMetodo("generar_archivo_trans");
		agregarComponente(sec_importar);
		strTipoCedula="NA";
	}
	
	public void ced_ingreso(){
		
		String fecha_inicial=cal_fecha_inicial.getFecha();
		String fecha_final=cal_fecha_final.getFecha();
		strTipoCedula="I";
		
		tab_tabla1.setSql(ser_contabilidad.getCedGastoIngreso(ser_general.getEjecucionPresupuestaria(fecha_inicial,fecha_final),ser_general.getEjecucionPresupuestariaIngreso(fecha_inicial,fecha_final),"I"));
		tab_tabla1.ejecutarSql();
		utilitario.addUpdate("tab_tabla1");
		
	}
	
	public void ced_gasto(){
		
		String fecha_inicial=cal_fecha_inicial.getFecha();
		String fecha_final=cal_fecha_final.getFecha();
		strTipoCedula="G";
		
		tab_tabla1.setSql(ser_contabilidad.getCedGastoIngreso(ser_general.getEjecucionPresupuestaria(fecha_inicial,fecha_final),ser_general.getEjecucionPresupuestariaIngreso(fecha_inicial,fecha_final),"G"));
		tab_tabla1.ejecutarSql();
		utilitario.addUpdate("tab_tabla1");
		
	}
	
	public void ced_gasto_ingreso(){
		
		String fecha_inicial=cal_fecha_inicial.getFecha();
		String fecha_final=cal_fecha_final.getFecha();
		strTipoCedula="NA";
		
		tab_tabla1.setSql(ser_contabilidad.getCedGastoIngreso(ser_general.getEjecucionPresupuestaria(fecha_inicial,fecha_final),ser_general.getEjecucionPresupuestariaIngreso(fecha_inicial,fecha_final),"T"));
		tab_tabla1.ejecutarSql();
		utilitario.addUpdate("tab_tabla1");
		
	}
	
	public void generar_archivoI(){

		if(tab_tabla1.isEmpty()){
			utilitario.agregarMensajeInfo("No se puede generar el Archivo", "No existen registros para la generación del archivo");
			return;
		}

		generar_archivo_plano("CEDULA","CEDULA_GASTO_INGRESO_INICIAL_",cal_fecha_final.getFecha(),tab_tabla1, true);
		
	}

	public void generar_archivo(){
		
		if(tab_tabla1.isEmpty()){
			utilitario.agregarMensajeInfo("No se puede generar el Archivo", "No existen registros para la generación del archivo");
			return;
		}
		
		generar_archivo_plano("CEDULA","CEDULA_GASTO_INGRESO_",cal_fecha_final.getFecha(),tab_tabla1, false);
		
	}
	
	public void generar_archivo_cedulas_bde(){
		
		if(tab_tabla1.isEmpty()){
			utilitario.agregarMensajeInfo("No se puede generar el Archivo", "No existen registros para la generación del archivo");
			return;
		}
		
		if(strTipoCedula.contains("NA"))
		{
			utilitario.agregarMensajeInfo("No se puede generar el Archivo", "Primero genere la cédula presionando los botones Ver Gasto/Ingreso individualmente.");
			return;
		}
		
		if(strTipoCedula.contains("I"))		
			generar_archivo_plano_cedula_bde("CEDULA_INGRESO_BDE_",cal_fecha_final.getFecha(),tab_tabla1, true);
		
		if(strTipoCedula.contains("G"))	
			generar_archivo_plano_cedula_bde("CEDULA_GASTO_BDE_",cal_fecha_final.getFecha(),tab_tabla1, false);
	}


	public void generar_archivo_trans()
	{

		 if (sec_importar.isVisible()){
			 
			 sec_importar.cerrar();
			 sel_transaccion.setTitle("GENERACION DEL ARCHIVO PLANO DE TRANSFERENCIAS");
			 sel_transaccion.setSql(ser_contabilidad.getTransferencias(sec_importar.getFecha1String(), sec_importar.getFecha2String()));
			 sel_transaccion.getTab_seleccion().ejecutarSql();
			 sel_transaccion.seleccionarTodas();
			 sel_transaccion.dibujar();
		
		 }
		 else if(sel_transaccion.isVisible()){
			 sel_transaccion.cerrar();
			 
			 if(sel_transaccion.getSeleccionados() !=null){
				 generar_archivo_plano("TRANSFERENCIA","TRANSFERENCIA_",sec_importar.getFecha2String(),sel_transaccion.getTab_seleccion(), false);	
				 
				}
				else{
					utilitario.agregarMensajeError("Selección", "Seleccione un registro para continuar");
				}
				
		 }
		 else {
			 sec_importar.dibujar();
			}
		
	}
	
	private void generar_archivo_plano(String tipo, String nombre, String fecha_final, Tabla tabla, boolean esInicial)
	{
		
		try {
			ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
			String fileName= nombre+utilitario.getNombreMes(utilitario.getMes(fecha_final))+utilitario.getAnio(fecha_final);
			String path= extContext.getRealPath("/");	
			String[] partida;
			
			File archivotxt=new File(path+fileName.concat(".txt"));
			BufferedWriter escribir=new BufferedWriter(new FileWriter(archivotxt)); 
			StringBuilder str_detalle=new StringBuilder();
			
			for(int i=0;i<tabla.getTotalFilas();i++){
				
				str_detalle=new StringBuilder();
				                                 //str_detalle.append(utilitario.getFormatoNumero(tabla.getValor(i, "periodo"),1)).append("|");
				str_detalle.append(pckUtilidades.Utilitario.padLeft(utilitario.getFormatoNumero(tabla.getValor(i, "periodo"),1),2)).append("|");
				
				if(tipo.equals("CEDULA"))
				{
					//str_detalle.append(utilitario.getFormatoNumero(tab_tabla1.getValor(i, "periodo"),1)).append("|");
					str_detalle.append(tabla.getValor(i, "tipo_cedula")).append("|");
					
					partida=tabla.getValor(i, "codigo_clasificador_prcla").split(Pattern.quote("."));
	
					str_detalle.append(partida[0]).append("|");
					str_detalle.append(partida[1]).append("|");
					str_detalle.append(partida[2]).append("|");
					
					if(tabla.getValor(i, "tipo_cedula").equals("I")) //Ingreso
					{
						str_detalle.append(tabla.getValor(i, "inicial"));
						if(!esInicial)
						{
							str_detalle.append("|");
							str_detalle.append(tabla.getValor(i, "reforma")).append("|");
							str_detalle.append(tabla.getValor(i, "codificado")).append("|");
							str_detalle.append(tabla.getValor(i, "devengado")).append("|");
							str_detalle.append(tabla.getValor(i, "cobrado")).append("|");
							str_detalle.append(tabla.getValor(i, "saldo_x_dev"));
						}
					}
					
					if(tabla.getValor(i, "tipo_cedula").equals("G")) //Gasto
					{
						str_detalle.append(tabla.getValor(i, "funcion")).append("|");
						str_detalle.append("99999999").append("|"); //ORIENTACION DEL GASTO DEFAULT 99999999
						str_detalle.append(tabla.getValor(i, "inicial"));
						if(!esInicial)
						{
							str_detalle.append("|");
							str_detalle.append(tabla.getValor(i, "reforma")).append("|");
							str_detalle.append(tabla.getValor(i, "codificado")).append("|");
							str_detalle.append(tabla.getValor(i, "compromiso")).append("|");
							str_detalle.append(tabla.getValor(i, "devengado")).append("|");
							str_detalle.append(tabla.getValor(i, "pago")).append("|");
							str_detalle.append(tabla.getValor(i, "saldo_comp")).append("|");
							str_detalle.append(tabla.getValor(i, "saldo_dev"));
						}
					}

				}
				
				if(tipo.equals("TRANSFERENCIA"))
				{
					partida=tabla.getValor(i, "cuenta").split(Pattern.quote("."));
					
					str_detalle.append(partida[0]).append("|");
					str_detalle.append(partida[1]).append("|");
					str_detalle.append(partida[2]).append("|");
					
					str_detalle.append(tabla.getValor(i, "ruc_recep")).append("|");
					str_detalle.append(tabla.getValor(i, "ruc_otorgante")).append("|");
					str_detalle.append(tabla.getValor(i, "deudor")).append("|");
					str_detalle.append(tabla.getValor(i, "acreedor")).append("|");
					str_detalle.append(tabla.getValor(i, "cuenta_monetaria"));
					
				}
				
				escribir.write(str_detalle.toString());	
				escribir.newLine();
			}
			escribir.close();
			
			utilitario.crearArchivo(new File[]{archivotxt},fileName.concat(".txt"));	
			//utilitario.crearArchivo(archivotxt.getPath());

		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("generar_archivo error: "+ e);
		}	
		
	}
	
	private void generar_archivo_plano_cedula_bde(String nombre, String fecha_final, Tabla tabla, boolean esIngreso)
	{
		
		try {
			ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
			String fileName= nombre+utilitario.getNombreMes(utilitario.getMes(fecha_final))+utilitario.getAnio(fecha_final);
			String path= extContext.getRealPath("/");	
			String[] partida;
			
			File archivotxt=new File(path+fileName.concat(".txt"));
			BufferedWriter escribir=new BufferedWriter(new FileWriter(archivotxt)); 
			StringBuilder str_detalle=new StringBuilder();
			
			for(int i=0;i<tabla.getTotalFilas();i++){
				
				str_detalle=new StringBuilder();
				
				partida=tabla.getValor(i, "codigo_clasificador_prcla").split(Pattern.quote("."));
				
				if(tabla.getValor(i, "tipo_cedula").equals("I")) //Ingreso
				{	
					str_detalle.append("0000");
					str_detalle.append("0000");
					str_detalle.append(partida[0]);
					str_detalle.append(partida[1]);
					str_detalle.append(partida[2]);
					str_detalle.append("0").append("\t");
					
					str_detalle.append(pckUtilidades.Utilitario.padLeft(utilitario.getFormatoNumero(tabla.getValor(i, "periodo"),1),2)).append("\t");	
					str_detalle.append(tabla.getValor(i, "descripcion_clasificador_prcla")).append("\t");
					str_detalle.append(tabla.getValor(i, "inicial")).append("\t");						
					str_detalle.append(tabla.getValor(i, "reforma")).append("\t");
					str_detalle.append(tabla.getValor(i, "codificado")).append("\t");
					str_detalle.append(tabla.getValor(i, "compromiso")).append("\t");
					str_detalle.append(tabla.getValor(i, "devengado")).append("\t");
					str_detalle.append(tabla.getValor(i, "cobrado"));
			
				}
				
				if(tabla.getValor(i, "tipo_cedula").equals("G")) //Gasto
				{
					str_detalle.append("0000");
					str_detalle.append("0000");
					str_detalle.append("0");
					str_detalle.append("0");
					str_detalle.append(partida[0]);
					str_detalle.append(partida[1]);
					str_detalle.append(partida[2]);
					str_detalle.append("0").append("\t");
					
					str_detalle.append(pckUtilidades.Utilitario.padLeft(utilitario.getFormatoNumero(tabla.getValor(i, "periodo"),1),2)).append("\t");
					str_detalle.append(tabla.getValor(i, "descripcion_clasificador_prcla")).append("\t");

					str_detalle.append(tabla.getValor(i, "inicial")).append("\t");
					str_detalle.append(tabla.getValor(i, "reforma")).append("\t");
					str_detalle.append(tabla.getValor(i, "codificado")).append("\t");
					str_detalle.append(tabla.getValor(i, "compromiso")).append("\t");
					str_detalle.append(tabla.getValor(i, "devengado")).append("\t");
					str_detalle.append(tabla.getValor(i, "pago"));
					
				}


				escribir.write(str_detalle.toString());	
				escribir.newLine();
			}
			escribir.close();
			
			utilitario.crearArchivo(new File[]{archivotxt},fileName.concat(".txt"));	
			//utilitario.crearArchivo(archivotxt.getPath());

		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("generar_archivo error: "+ e);
		}	
		
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		
	}

	public Tabla getTab_tabla1() {
		return tab_tabla1;
	}

	public void setTab_tabla1(Tabla tab_tabla1) {
		this.tab_tabla1 = tab_tabla1;
	}

	public Calendario getCal_fecha_final() {
		return cal_fecha_final;
	}

	public void setCal_fecha_final(Calendario cal_fecha_final) {
		this.cal_fecha_final = cal_fecha_final;
	}

	public SeleccionTabla getSel_transaccion() {
		return sel_transaccion;
	}

	public void setSel_transaccion(SeleccionTabla sel_transaccion) {
		this.sel_transaccion = sel_transaccion;
	}

	public SeleccionCalendario getSec_importar() {
		return sec_importar;
	}

	public void setSec_importar(SeleccionCalendario sec_importar) {
		this.sec_importar = sec_importar;
	}
	
	

}
