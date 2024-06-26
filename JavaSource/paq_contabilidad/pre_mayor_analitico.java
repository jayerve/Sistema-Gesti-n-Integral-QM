package paq_contabilidad;

import javax.ejb.EJB;



import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;

public class pre_mayor_analitico extends Pantalla{

	private Tabla tab_tabla1=new Tabla();
	private SeleccionTabla set_cuenta_contable = new SeleccionTabla();
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
	private Combo com_anio=new Combo();

	
	public static String par_tipo_asiento_inicial;

	
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);

	public pre_mayor_analitico() {
		par_tipo_asiento_inicial =utilitario.getVariable("p_tipo_asiento_inicial");

		bar_botones.limpiar();
		
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setValue(utilitario.getVariable("p_anio_vigente"));
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
		
		bar_botones.agregarComponente(new Etiqueta("Fecha Inicial :"));
		//cal_fecha_inicial.setFechaActual();
		cal_fecha_inicial.setValue(utilitario.obtenerFechaInicioAnio());
		bar_botones.agregarComponente(cal_fecha_inicial);

		bar_botones.agregarComponente(new Etiqueta("Fecha Final :"));
		//cal_fecha_final.setFechaActual();
		cal_fecha_final.setValue(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		bar_botones.agregarComponente(cal_fecha_final);

		// boton limpiar
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarBoton(bot_limpiar);
		//Boton actualizar		
		Boton bot_actualizar=new Boton();
		bot_actualizar.setIcon("ui-icon-person");
		bot_actualizar.setValue("Generar Mayor Analitico");
		bot_actualizar.setMetodo("generarMayorAnalitico");
		bar_botones.agregarBoton(bot_actualizar);	
				
		tab_tabla1.setId("tab_tabla1");  
		tab_tabla1.setSql(ser_contabilidad.getMayorAnalitico("1900-01-01", "1900-01-01", "-1","-1"));	
		tab_tabla1.setCampoPrimaria("ide_comov");
		tab_tabla1.setHeader("MAYOR ANALITICO");
		tab_tabla1.getColumna("nro_comprobante_comov").setFiltro(true);
		tab_tabla1.getColumna("detalle_comov").setFiltro(true);
		tab_tabla1.getColumna("estado").setFiltro(true);
		tab_tabla1.getColumna("cue_codigo_cocac").setFiltro(true);
		tab_tabla1.getColumna("cue_descripcion_cocac").setFiltro(true);
		tab_tabla1.getColumna("nro_comprobante_comov").setNombreVisual("NRO. COMPROBANTE");
		tab_tabla1.getColumna("detalle_comov").setNombreVisual("DETALLE DEL ASIENTO");
		tab_tabla1.getColumna("estado").setNombreVisual("ESTADO");
		tab_tabla1.getColumna("cue_codigo_cocac").setNombreVisual("CODIGO DE LA CUENTA");
		tab_tabla1.getColumna("cue_descripcion_cocac").setNombreVisual("NOMBRE DE LA CUENTA");
		tab_tabla1.getColumna("detalle_gemes").setNombreVisual("MES");
		tab_tabla1.getColumna("IDE_COMOV").setNombreVisual("NRO. ASIENTO");
		tab_tabla1.getColumna("MOV_FECHA_COMOV").setNombreVisual("FECHA ASIENTO");
		tab_tabla1.getColumna("DEBE_CODEM").setNombreVisual("DEBE");
		tab_tabla1.getColumna("HABER_CODEM").setNombreVisual("HABER");

		tab_tabla1.getColumna("cue_codigo_cocac").setLongitud(20);
		tab_tabla1.getColumna("detalle_gemes").setLongitud(20);
		tab_tabla1.getColumna("estado").setLongitud(20);
		tab_tabla1.setColumnaSuma("debe_codem,haber_codem,devengado_prmen,cobrado_prmen,pagado_prmen");

		tab_tabla1.setRows(20);
		tab_tabla1.setLectura(true);
		tab_tabla1.dibujar();
		PanelTabla pat_panel1=new PanelTabla();
		pat_panel1.setPanelTabla(tab_tabla1);
		pat_panel1.getMenuTabla().getItem_formato().setDisabled(true);

		Division div1 = new Division();
		div1.dividir1(pat_panel1);
		agregarComponente(div1);
		
		
				
		set_cuenta_contable.setId("set_cuenta_contable");
		set_cuenta_contable.setSeleccionTabla(ser_contabilidad.getCatalogoCuentaAnio("true,false", "-1"),"ide_cocac");
		set_cuenta_contable.getBot_aceptar().setMetodo("imprimirMayorAnalitico");
		set_cuenta_contable.getTab_seleccion().getColumna("cue_codigo_cocac").setNombreVisual("CODIGO DE CUENTA");
		set_cuenta_contable.getTab_seleccion().getColumna("cue_descripcion_cocac").setNombreVisual("NOMBRE DE CUENTA");
		set_cuenta_contable.getTab_seleccion().getColumna("detalle_geani").setNombreVisual("AÑO");

		set_cuenta_contable.getTab_seleccion().getColumna("cue_codigo_cocac").setFiltro(true);
		set_cuenta_contable.getTab_seleccion().getColumna("cue_descripcion_cocac").setFiltro(true);

		agregarComponente(set_cuenta_contable);
				
			
		
	}
	public void generarMayorAnalitico(){
		if (com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un año","");
			return;

		}
		if (cal_fecha_inicial.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar una fecha","");
			return;

		}
		if (cal_fecha_final.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar una fecha","");
			return;

		}
		set_cuenta_contable.getTab_seleccion().setSql(ser_contabilidad.getCatalogoCuentaAnio("true", com_anio.getValue().toString()));
		set_cuenta_contable.getTab_seleccion().ejecutarSql();
		set_cuenta_contable.dibujar();	
	}
	
	public void imprimirMayorAnalitico(){
		String seleccionados = set_cuenta_contable.getSeleccionados();
		System.out.println("imprimirMayorAnalitico "+seleccionados);
		if (seleccionados!=null && seleccionados.length()>0){
			tab_tabla1.setSql(ser_contabilidad.getMayorAnalitico(cal_fecha_inicial.getFecha(), cal_fecha_final.getFecha(), seleccionados,com_anio.getValue()+""));
			tab_tabla1.ejecutarSql();
			set_cuenta_contable.cerrar();
		}
		else
			utilitario.agregarMensajeError("Seleccione un Registro", "Debe seleccionar una cuenta contable para continuar.");
	}
	/**
	 * limpia toda la pantalla incluyendo el autocompletar
	 */
	public void limpiar() {
		tab_tabla1.limpiar();	
		utilitario.addUpdate("tab_tabla1");// limpia y refresca el autocompletar
	}
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_tabla1.insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_tabla1.guardar();
		guardarPantalla();		
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_tabla1.eliminar();
	}

	

	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}
	public Tabla getTab_tabla1() {
		return tab_tabla1;
	}
	public void setTab_tabla1(Tabla tab_tabla1) {
		this.tab_tabla1 = tab_tabla1;
	}
	public SeleccionTabla getSet_cuenta_contable() {
		return set_cuenta_contable;
	}
	public void setSet_cuenta_contable(SeleccionTabla set_cuenta_contable) {
		this.set_cuenta_contable = set_cuenta_contable;
	}
	
	
	


}
