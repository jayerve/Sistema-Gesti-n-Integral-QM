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

public class pre_transacciones_reciprocas extends Pantalla {
	
	private Tabla tab_trx_recip = new Tabla();
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
	
	@EJB
	private ServicioGeneral ser_general = (ServicioGeneral ) utilitario.instanciarEJB(ServicioGeneral.class);
	@EJB
    private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	
	public pre_transacciones_reciprocas() {
		
		bar_botones.limpiar();
		
		bar_botones.agregarComponente(new Etiqueta("Fecha Inicio :"));
		cal_fecha_inicial.setValue(utilitario.obtenerFechaInicioAnio());
		bar_botones.agregarComponente(cal_fecha_inicial);
		
		bar_botones.agregarComponente(new Etiqueta("Fecha Corte :"));
		cal_fecha_final.setValue(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		bar_botones.agregarComponente(cal_fecha_final);
		
		Boton bot_gast = new Boton();
		bot_gast.setValue("Ver Transacciones");
		bot_gast.setMetodo("trx_reciprocas");
		
		bar_botones.agregarBoton(bot_gast);	
		
		Boton bot_generar = new Boton();
		bot_generar.setValue("Generar Archivo Transacciones");
		bot_generar.setMetodo("generar_archivo");
		bot_generar.setAjax(false);
		bar_botones.agregarBoton(bot_generar);
				
		tab_trx_recip.setId("tab_trx_recip");
		tab_trx_recip.setSql(ser_contabilidad.getTrxReciprocas("1900-01-01","1900-01-01"));
		tab_trx_recip.setColumnaSuma("flujo_deudor,flujo_acreedor,flujo_deudor2,flujo_acreedor2");
		tab_trx_recip.setRows(30);
		tab_trx_recip.setLectura(true);
		tab_trx_recip.dibujar();
		
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_trx_recip);
		
		Division div_tabla = new Division();
		div_tabla.dividir1(pat_panel);
		agregarComponente(div_tabla);
		
	}
	
	
	public void trx_reciprocas(){
		
		String fecha_inicial=cal_fecha_inicial.getFecha();
		String fecha_final=cal_fecha_final.getFecha();
		
		tab_trx_recip.setSql(ser_contabilidad.getTrxReciprocas(fecha_inicial,fecha_final));
		tab_trx_recip.ejecutarSql();
		utilitario.addUpdate("tab_trx_recip");
		
	}
	
	public void generar_archivo(){
		
		if(tab_trx_recip.isEmpty()){
			utilitario.agregarMensajeInfo("No se puede generar el Archivo", "No existen registros para la generación del archivo");
			return;
		}
		
		generar_archivo_plano("TRANSACCIONES_RECIPROCAS_",cal_fecha_final.getFecha(),tab_trx_recip);
		
	}
	
	private void generar_archivo_plano(String nombre, String fecha_final, Tabla tabla)
	{
		
		try {
			ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
			String fileName= nombre+utilitario.getNombreMes(utilitario.getMes(fecha_final))+utilitario.getAnio(fecha_final);
			String path= extContext.getRealPath("/");
			String strCta_mayorPagarCobrar="";
			String strPartida="";
			String strCta_mayorGastoIngreso="";
			String[] cta_mayorPagarCobrar;
			String[] partida;
			String[] cta_mayorGastoIngreso;
			
			File archivotxt=new File(path+fileName.concat(".txt"));
			BufferedWriter escribir=new BufferedWriter(new FileWriter(archivotxt)); 
			StringBuilder str_detalle=new StringBuilder();
			
			for(int i=0;i<tabla.getTotalFilas();i++){
				
				str_detalle=new StringBuilder();
	
				str_detalle.append(pckUtilidades.Utilitario.padLeft(utilitario.getFormatoNumero(tabla.getValor(i, "periodo"),1),2)).append("|");
				str_detalle.append(pckUtilidades.CConversion.CStr(tabla.getValor(i, "rucEntidad"))).append("|");

				strCta_mayorPagarCobrar=pckUtilidades.CConversion.CStr(tabla.getValor(i, "cta_mayor_pagar_cobrar"));
				
				if(strCta_mayorPagarCobrar.length()>4)
				{
					cta_mayorPagarCobrar=strCta_mayorPagarCobrar.split(Pattern.quote("."));
					
					str_detalle.append(cta_mayorPagarCobrar[0]).append("|");
					str_detalle.append(cta_mayorPagarCobrar[1]).append("|");
					if(cta_mayorPagarCobrar.length>2)
						str_detalle.append(cta_mayorPagarCobrar[2]).append("|");
					else
						str_detalle.append("00").append("|");
				}
				else
				{
					str_detalle.append("").append("|");
					str_detalle.append("").append("|");
					str_detalle.append("").append("|");
				}
				
				str_detalle.append(formateaCero(tabla.getValor(i, "flujo_deudor"))).append("|");
				str_detalle.append(formateaCero(tabla.getValor(i, "flujo_acreedor"))).append("|");
				str_detalle.append(tabla.getValor(i, "ruc")).append("|");
				str_detalle.append(tabla.getValor(i, "nombre_tepro")).append("|");
				
				strPartida=pckUtilidades.CConversion.CStr(tabla.getValor(i, "codigo_clasificador_prcla"));
				if(strPartida.length()>4)
				{
					partida=strPartida.split(Pattern.quote("."));
					str_detalle.append(partida[0]).append("|");
					str_detalle.append(partida[1]).append("|");
					str_detalle.append(partida[2]).append("|");
				}
				else
				{
					str_detalle.append("").append("|");
					str_detalle.append("").append("|");
					str_detalle.append("").append("|");
				}
				
				strCta_mayorGastoIngreso=pckUtilidades.CConversion.CStr(tabla.getValor(i, "cta_mayor_gasto_i"));
				if(strCta_mayorGastoIngreso.length()>4)
				{
					cta_mayorGastoIngreso=strCta_mayorGastoIngreso.split(Pattern.quote("."));
					
					str_detalle.append(cta_mayorGastoIngreso[0]).append("|");
					str_detalle.append(cta_mayorGastoIngreso[1]).append("|");
					if(strCta_mayorGastoIngreso.contains("111.06"))
						str_detalle.append("00").append("|");
					else
						if(cta_mayorGastoIngreso.length>2)						
							str_detalle.append(cta_mayorGastoIngreso[2]).append("|");
						else
							str_detalle.append("00").append("|");
				}
				else
				{
					str_detalle.append("").append("|");
					str_detalle.append("").append("|");
					str_detalle.append("").append("|");
				}
				
				str_detalle.append(formateaCero(tabla.getValor(i, "flujo_deudor2"))).append("|");
				str_detalle.append(formateaCero(tabla.getValor(i, "flujo_acreedor2"))).append("|");
				str_detalle.append(tabla.getValor(i, "nro_transaccion")).append("|");
				str_detalle.append(tabla.getValor(i, "nro_referenci")).append("|");
				str_detalle.append(tabla.getValor(i, "fecha_apro")).append("|");
				str_detalle.append(tabla.getValor(i, "fecha_venc"));
				
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
				
				escribir.write(str_spi_encr.toString());	
				escribir.newLine();
			}
			escribir.close();
			
			utilitario.crearArchivo(new File[]{archivotxt},fileName.concat(".txt"));	


		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("generar_archivo error: "+ e);
		}	
		
	}
	
	private String formateaCero(String valor)
	{
		if(pckUtilidades.CConversion.CDbl(valor)>0)
			return valor;
		else
			return "0.00";
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


	public Tabla getTab_trx_recip() {
		return tab_trx_recip;
	}


	public void setTab_trx_recip(Tabla tab_trx_recip) {
		this.tab_trx_recip = tab_trx_recip;
	}


	public Calendario getCal_fecha_inicial() {
		return cal_fecha_inicial;
	}


	public void setCal_fecha_inicial(Calendario cal_fecha_inicial) {
		this.cal_fecha_inicial = cal_fecha_inicial;
	}


	public Calendario getCal_fecha_final() {
		return cal_fecha_final;
	}


	public void setCal_fecha_final(Calendario cal_fecha_final) {
		this.cal_fecha_final = cal_fecha_final;
	}



}
