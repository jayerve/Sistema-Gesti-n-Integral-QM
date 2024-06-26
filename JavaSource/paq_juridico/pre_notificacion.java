package paq_juridico;

import java.util.List;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJB;

import org.primefaces.event.SelectEvent;

import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.BotonesCombo;
import framework.componentes.Calendario;
import framework.componentes.Check;
import framework.componentes.Confirmar;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.ItemMenu;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import framework.componentes.VisualizarPDF;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_juridico.ejb.ServicioJuridico;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;
import paq_tesoreria.ejb.ServicioTesoreria;

public class pre_notificacion extends Pantalla{
	
	private Tabla tab_comprobantes_vencidos = new Tabla();
	private SeleccionTabla sel_comprobantes= new SeleccionTabla();

	private VisualizarPDF vpdf_pago = new VisualizarPDF();
	public static String par_sec_notificacion;

	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion ) utilitario.instanciarEJB(ServicioFacturacion.class);
	@EJB
	private ServicioJuridico ser_juridico = (ServicioJuridico ) utilitario.instanciarEJB(ServicioJuridico.class);
	@EJB
    private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);

	public pre_notificacion(){

		bar_botones.limpiar();
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarBoton(bot_limpiar);
		
		par_sec_notificacion =utilitario.getVariable("p_modulo_secuencialnotificacion");

		Boton bot_actualiza = new Boton();
		bot_actualiza.setId("bot_actualiza");
		bot_actualiza.setTitle("ACTUALIZAR");
		bot_actualiza.setValue("ACTUALIZAR");
		bot_actualiza.setMetodo("cargaCliente");
		bar_botones.agregarBoton(bot_actualiza);
		
		ItemMenu itm_notificacion1 = new ItemMenu();
		itm_notificacion1.setValue("Primera Notificación");
		itm_notificacion1.setMetodo("primera");
		itm_notificacion1.setIcon("ui-icon-print");
		
		ItemMenu itm_notificacion2 = new ItemMenu();
		itm_notificacion2.setValue("Segunda Notificación");
		itm_notificacion2.setMetodo("segunda");
		itm_notificacion2.setIcon("ui-icon-print");
		
		ItemMenu itm_notificacion3 = new ItemMenu();
		itm_notificacion3.setValue("Tercera Notificación");
		itm_notificacion3.setMetodo("tercera");
		itm_notificacion3.setIcon("ui-icon-print");
		
		ItemMenu itm_ver_comprobantes = new ItemMenu();
		itm_ver_comprobantes.setValue("Ver Comprobantes");
		itm_ver_comprobantes.setMetodo("verComprobantes");
		itm_ver_comprobantes.setIcon("ui-icon-search");

		tab_comprobantes_vencidos.setId("tab_comprobantes_vencidos");
		tab_comprobantes_vencidos.setSql(ser_juridico.getNotificacionClientes());
		tab_comprobantes_vencidos.getColumna("ruc_comercial_recli").setFiltroContenido();
		tab_comprobantes_vencidos.getColumna("ruc_comercial_recli").setLongitud(25);
		tab_comprobantes_vencidos.getColumna("razon_social_recli").setFiltroContenido();
		tab_comprobantes_vencidos.getColumna("nro_1").setLongitud(10);
		tab_comprobantes_vencidos.getColumna("notificacion_1").setLongitud(30);
		tab_comprobantes_vencidos.getColumna("nro_2").setLongitud(10);
		tab_comprobantes_vencidos.getColumna("notificacion_2").setLongitud(30);
		tab_comprobantes_vencidos.getColumna("nro_3").setLongitud(10);
		tab_comprobantes_vencidos.getColumna("notificacion_3").setLongitud(30);
		tab_comprobantes_vencidos.setColumnaSuma("saldo,interes,total");
		tab_comprobantes_vencidos.setRows(25);
		tab_comprobantes_vencidos.setLectura(true);
		tab_comprobantes_vencidos.dibujar();
		
		
		PanelTabla pat_panel=new PanelTabla();
		pat_panel.setPanelTabla(tab_comprobantes_vencidos);
		pat_panel.getMenuTabla().getChildren().add(itm_ver_comprobantes);
		pat_panel.getMenuTabla().getChildren().add(itm_notificacion1);
		pat_panel.getMenuTabla().getChildren().add(itm_notificacion2);
		pat_panel.getMenuTabla().getChildren().add(itm_notificacion3);
		
		Division div_tabla = new Division();
		div_tabla.dividir1(pat_panel);
		agregarComponente(div_tabla);
		
		//REPORTE
        vpdf_pago.setId("vpdf_pago");
        vpdf_pago.setTitle("Notificación Extrajudicial");
        agregarComponente(vpdf_pago);
		
        //Comprobantes
        sel_comprobantes.setId("sel_comprobantes");
        sel_comprobantes.setSeleccionTabla(ser_juridico.getNotificacionClientesComprobantes("-1"),"codigo");
        sel_comprobantes.setTitle("Comprobantes Impagos.");
        sel_comprobantes.getBoc_seleccion_inversa().setRendered(false);
        sel_comprobantes.setRadio();
        sel_comprobantes.getTab_seleccion().setColumnaSuma("saldo,interes,total");
        //sel_comprobantes.getTab_seleccion().getColumna("detalle_prcer").setFiltroContenido();
        sel_comprobantes.getBot_aceptar().setRendered(false);
		agregarComponente(sel_comprobantes);

	}
	
	
	public void cargaCliente(){
		tab_comprobantes_vencidos.limpiar();
		tab_comprobantes_vencidos.setSql(ser_juridico.getNotificacionClientes());
		tab_comprobantes_vencidos.dibujar();
		tab_comprobantes_vencidos.ejecutarSql();
		utilitario.addUpdate("tab_comprobantes_vencidos");
	}
	
	public void verComprobantes() {
		sel_comprobantes.getTab_seleccion().setSql(ser_juridico.getNotificacionClientesComprobantes(tab_comprobantes_vencidos.getValor("ruc_comercial_recli")));
		sel_comprobantes.getTab_seleccion().ejecutarSql();
		sel_comprobantes.dibujar();
	}
	
	public void limpiar(){
		
		tab_comprobantes_vencidos.limpiar();
		utilitario.addUpdate("tab_comprobantes_vencidos");
	}
	
	public boolean validar_notificacion(String ruc, int tipo)
	{
		TablaGenerica tab_notificacion = utilitario.consultar("SELECT ide_junot, ruc_junot, tipo_notificacion_junot FROM public.jur_notificacion where ruc_junot like '"+ruc+"' and tipo_notificacion_junot="+tipo);
		
		if(tab_notificacion.getTotalFilas()>0)
			return true;
		
		return false;
	}
	
 	public void primera()
	{
 		String ruc=tab_comprobantes_vencidos.getValor("ruc_comercial_recli");
 		
 		if(validar_notificacion(ruc,1))
 		{
 			utilitario.agregarMensajeInfo("Ya se notificó al cliente", "");
 			return;
 		}
 		
		TablaGenerica tab_deuda = utilitario.consultar(ser_facturacion.getDeudaCliente(0,0,ruc));
		
		double total = pckUtilidades.CConversion.CDbl(tab_deuda.getValor("total"));
		
		if(total>0)
		{
			Locale locale=new Locale("es","ES");	
			
			String notificacion_nro=utilitario.getAnio(utilitario.getFechaActual())+ "-"+pckUtilidades.Utilitario.padLeft(ser_contabilidad.numeroSecuencial(par_sec_notificacion),4);
			String notificacion="PRIMERA NOTIFICACIÓN";

			//AQUI ABRE EL REPORTE
	        Map p_parametros = new HashMap();
	        p_parametros.put("titulo", "EMGIRS - EP");
	        p_parametros.put("p_fecha_larga", utilitario.DateStringFormat(utilitario.DeDateAString(utilitario.getDate())).toLowerCase());
	        p_parametros.put("p_notificacion_nro", notificacion_nro);
	        p_parametros.put("p_notificacion", notificacion);
	        p_parametros.put("p_jurisdiccion", "aplicará la correspondiente jurisdicción coactiva");
	        p_parametros.put("p_ruc", ruc);
	        p_parametros.put("p_total", total);
	        p_parametros.put("p_total_letras", utilitario.getLetrasDolarNumero(utilitario.getFormatoNumero(total,2)));
	        p_parametros.put("REPORT_LOCALE", locale);

	        vpdf_pago.setVisualizarPDF("rep_juridico/rep_notificacion_extrajudicial.jasper", p_parametros);
	        vpdf_pago.dibujar();
	        utilitario.addUpdate("vpdf_pago");
	        
	        registrar(ruc,notificacion_nro,notificacion,total,1);
		}
		else
			utilitario.agregarMensajeInfo("Cliente no posee valores pendientes.", "");
	
	}
	
	public void segunda()
	{
		String ruc=tab_comprobantes_vencidos.getValor("ruc_comercial_recli");
 		
 		if(validar_notificacion(ruc,2))
 		{
 			utilitario.agregarMensajeInfo("Ya se notificó al cliente", "");
 			return;
 		}
 		
		TablaGenerica tab_deuda = utilitario.consultar(ser_facturacion.getDeudaCliente(0,0,ruc));
		
		double total = pckUtilidades.CConversion.CDbl(tab_deuda.getValor("total"));
		
		if(total>0)
		{
			Locale locale=new Locale("es","ES");	
			
			String notificacion_nro=utilitario.getAnio(utilitario.getFechaActual())+ "-"+pckUtilidades.Utilitario.padLeft(ser_contabilidad.numeroSecuencial(par_sec_notificacion),4);
			String notificacion="SEGUNDA NOTIFICACIÓN";

			//AQUI ABRE EL REPORTE
	        Map p_parametros = new HashMap();
	        p_parametros.put("titulo", "EMGIRS - EP");
	        p_parametros.put("p_fecha_larga", utilitario.DateStringFormat(utilitario.DeDateAString(utilitario.getDate())).toLowerCase());
	        p_parametros.put("p_notificacion_nro", notificacion_nro);
	        p_parametros.put("p_notificacion", notificacion);
	        p_parametros.put("p_jurisdiccion", "aplicará la correspondiente jurisdicción coactiva");
	        p_parametros.put("p_ruc", ruc);
	        p_parametros.put("p_total", total);
	        p_parametros.put("p_total_letras", utilitario.getLetrasDolarNumero(utilitario.getFormatoNumero(total,2)));
	        p_parametros.put("REPORT_LOCALE", locale);

	        vpdf_pago.setVisualizarPDF("rep_juridico/rep_notificacion_extrajudicial.jasper", p_parametros);
	        vpdf_pago.dibujar();
	        utilitario.addUpdate("vpdf_pago");
	        registrar(ruc,notificacion_nro,notificacion,total,2);
	        
		}
		else
			utilitario.agregarMensajeInfo("Cliente no posee valores pendientes.", "");
	
	}
	
	public void tercera()
	{
		String ruc=tab_comprobantes_vencidos.getValor("ruc_comercial_recli");
 		
 		if(validar_notificacion(ruc,3))
 		{
 			utilitario.agregarMensajeInfo("Ya se notificó al cliente", "");
 			return;
 		}
 		
		TablaGenerica tab_deuda = utilitario.consultar(ser_facturacion.getDeudaCliente(0,0,ruc));
		
		double total = pckUtilidades.CConversion.CDbl(tab_deuda.getValor("total"));
		
		if(total>0)
		{
			Locale locale=new Locale("es","ES");	
			
			String notificacion_nro=utilitario.getAnio(utilitario.getFechaActual())+ "-"+pckUtilidades.Utilitario.padLeft(ser_contabilidad.numeroSecuencial(par_sec_notificacion),4);
			String notificacion="TERCERA NOTIFICACIÓN";

			//AQUI ABRE EL REPORTE
	        Map p_parametros = new HashMap();
	        p_parametros.put("titulo", "EMGIRS - EP");
	        p_parametros.put("p_fecha_larga", utilitario.DateStringFormat(utilitario.DeDateAString(utilitario.getDate())).toLowerCase());
	        p_parametros.put("p_notificacion_nro", notificacion_nro);
	        p_parametros.put("p_notificacion", notificacion);
	        p_parametros.put("p_jurisdiccion", "aplicará la correspondiente jurisdicción coactiva");
	        p_parametros.put("p_ruc", ruc);
	        p_parametros.put("p_total", total);
	        p_parametros.put("p_total_letras", utilitario.getLetrasDolarNumero(utilitario.getFormatoNumero(total,2)));
	        p_parametros.put("REPORT_LOCALE", locale);

	        vpdf_pago.setVisualizarPDF("rep_juridico/rep_notificacion_extrajudicial.jasper", p_parametros);
	        vpdf_pago.dibujar();
	        utilitario.addUpdate("vpdf_pago");
	        registrar(ruc,notificacion_nro,notificacion,total,3);
	        
		}
		else
			utilitario.agregarMensajeInfo("Cliente no posee valores pendientes.", "");
	
	}

	public void registrar(String ruc, String notificacion_nro, String notificacion, double total, int tipo)
	{
		TablaGenerica tab_maximo =utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("jur_notificacion", "ide_junot"));
		String maximo_id=tab_maximo.getValor("codigo");
		
		String str_insert_notificacion="insert into jur_notificacion (ide_junot,ruc_junot,tipo_notificacion_junot,notificacion_junot,nro_notificacion_junot,total_junot,activo_junot,usuario_ingre,fecha_ingre,hora_ingre) ";
		str_insert_notificacion +=" values ("+maximo_id+",'"
											+ruc+"',"+tipo+",'"
											+notificacion+"','"+notificacion_nro+"',"+total+",true,"
				                           +"'"+ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("nick_usua")+"',now(),now())";
		
		utilitario.getConexion().ejecutarSql(str_insert_notificacion);
		System.out.println("inserto la notificacion "+str_insert_notificacion);
        
        utilitario.agregarMensaje("Guardando secuencial primera notificacion ", "Nro: "+ser_contabilidad.numeroSecuencial(par_sec_notificacion));
		ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_sec_notificacion), par_sec_notificacion);
		
		tab_comprobantes_vencidos.ejecutarSql();
		utilitario.addUpdate("tab_comprobantes_vencidos");
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

	

	public Tabla getTab_comprobantes_vencidos() {
		return tab_comprobantes_vencidos;
	}

	public void setTab_comprobantes_vencidos(Tabla tab_comprobantes_vencidos) {
		this.tab_comprobantes_vencidos = tab_comprobantes_vencidos;
	}

	public VisualizarPDF getVpdf_pago() {
		return vpdf_pago;
	}

	public void setVpdf_pago(VisualizarPDF vpdf_pago) {
		this.vpdf_pago = vpdf_pago;
	}


	public SeleccionTabla getSel_comprobantes() {
		return sel_comprobantes;
	}


	public void setSel_comprobantes(SeleccionTabla sel_comprobantes) {
		this.sel_comprobantes = sel_comprobantes;
	}



}
