package paq_general;

import javax.ejb.EJB;

import paq_general.ejb.ServicioGeneral;
import paq_presupuesto.ejb.ServicioPresupuesto;
import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;

public class pre_ejecucion_presupuestaria extends Pantalla{
	
	private Tabla tab_ejecucionP = new Tabla();
	private SeleccionTabla sel_certificacion= new SeleccionTabla();
	private SeleccionTabla sel_compromiso= new SeleccionTabla();
	private SeleccionTabla sel_eje_proyecto= new SeleccionTabla();
	private SeleccionTabla sel_eje_area= new SeleccionTabla();
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
	
	@EJB
	private ServicioGeneral ser_general = (ServicioGeneral ) utilitario.instanciarEJB(ServicioGeneral.class);
	@EJB
    private ServicioPresupuesto ser_presupuesto=(ServicioPresupuesto)utilitario.instanciarEJB(ServicioPresupuesto.class);
	
	public pre_ejecucion_presupuestaria(){

		bar_botones.limpiar();
		
		bar_botones.agregarComponente(new Etiqueta("Fecha Inicio :"));
		cal_fecha_inicial.setValue(utilitario.obtenerFechaInicioAnio());
		bar_botones.agregarComponente(cal_fecha_inicial);

		bar_botones.agregarComponente(new Etiqueta("Fecha Corte :"));
		cal_fecha_final.setFechaActual();
		bar_botones.agregarComponente(cal_fecha_final);
		
		Boton bot_actualiza = new Boton();
		bot_actualiza.setId("bot_actualiza");
		bot_actualiza.setTitle("ACTUALIZAR");
		bot_actualiza.setValue("ACTUALIZAR");
		bot_actualiza.setMetodo("cargaEjecucion");
		bar_botones.agregarBoton(bot_actualiza);
		
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setTitle("Limpiar");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarBoton(bot_limpiar);
		
		Boton bot_validarCert = new Boton();
		bot_validarCert.setValue("Validar Certificaciones");
		bot_validarCert.setMetodo("validaCert");
		bar_botones.agregarBoton(bot_validarCert);
		
		Boton bot_validarComp = new Boton();
		bot_validarComp.setValue("Validar Compromisos");
		bot_validarComp.setMetodo("validaComp");
		bar_botones.agregarBoton(bot_validarComp);
		
		Boton bot_ejecPry = new Boton();
		bot_ejecPry.setValue("Ejecución Proyecto");
		bot_ejecPry.setMetodo("ejecucionPry");
		bar_botones.agregarBoton(bot_ejecPry);
		
		Boton bot_ejecArea = new Boton();
		bot_ejecArea.setValue("Ejecución Area");
		bot_ejecArea.setMetodo("ejecucion_Area");
		bar_botones.agregarBoton(bot_ejecArea);
    			
		Boton bot_excel=new Boton();
	    bot_excel.setValue("Certificaciones EXCEL");
	    bot_excel.setIcon("ui-icon-calculator");
	    bot_excel.setAjax(false);
	    bot_excel.setMetodo("exportarExcelCERT");
	    bar_botones.agregarBoton(bot_excel);
	    
	    Boton bot_excelComp=new Boton();
	    bot_excelComp.setValue("Compromisos EXCEL");
	    bot_excelComp.setIcon("ui-icon-calculator");
	    bot_excelComp.setAjax(false);
	    bot_excelComp.setMetodo("exportarExcelCOMP");
	    bar_botones.agregarBoton(bot_excelComp);
    			
		tab_ejecucionP.setId("tab_ejecucionP");
		tab_ejecucionP.setSql(ser_general.getEjecucionPresupuestaria("1900-01-01","1900-01-01"));
		//tab_ejecucionP.getColumna("ide_prpoa").setVisible(false);
		tab_ejecucionP.getColumna("periodo").setNombreVisual("PERIODO");
		tab_ejecucionP.getColumna("detalle_geani").setNombreVisual("AÑO");
		tab_ejecucionP.getColumna("detalle_geani").setLongitud(15);
		tab_ejecucionP.getColumna("cod_prog").setNombreVisual("COD. PROGRAMA");
		tab_ejecucionP.getColumna("detalle_programa").setNombreVisual("PROGRAMA");
		tab_ejecucionP.getColumna("cod_pry").setNombreVisual("COD. PROYECTO");
		tab_ejecucionP.getColumna("detalle_proyecto").setNombreVisual("PROYECTO");
		//tab_ejecucionP.getColumna("cod_prod").setNombreVisual("COD. PRODUCTO"); //cod_prod_mc
		tab_ejecucionP.getColumna("cod_prod_mc").setNombreVisual("COD. PRODUCTO"); 
		tab_ejecucionP.getColumna("cod_prod").setVisible(false);
		tab_ejecucionP.getColumna("detalle_producto").setNombreVisual("PRODUCTO");
		tab_ejecucionP.getColumna("codigo_obra").setNombreVisual("COD. OBRA"); 
		
		tab_ejecucionP.getColumna("codigo_fuente_prfuf").setNombreVisual("CODIGO FUENTE");
		tab_ejecucionP.getColumna("detalle_programa").setFiltroContenido();
		tab_ejecucionP.getColumna("detalle_proyecto").setFiltroContenido();
		//tab_ejecucionP.getColumna("detalle_producto").setFiltro(true);
		tab_ejecucionP.getColumna("detalle_producto").setFiltroContenido();
		tab_ejecucionP.getColumna("detalle_subactividad").setFiltroContenido();
		tab_ejecucionP.getColumna("ide_prpoa").setFiltroContenido();
		tab_ejecucionP.getColumna("codigo_subactividad").setFiltroContenido();
		tab_ejecucionP.getColumna("fuente").setFiltroContenido();
		tab_ejecucionP.getColumna("codigo_clasificador_prcla").setFiltroContenido();
		tab_ejecucionP.getColumna("codigo_clasificador_prcla").setNombreVisual("PARTIDA");
		tab_ejecucionP.getColumna("descripcion_clasificador_prcla").setNombreVisual("DESCRIPCION PARTIDA");
		tab_ejecucionP.getColumna("presupuesto_inicial_prpoa").setNombreVisual("ASIGNACION INICIAL");
		tab_ejecucionP.getColumna("reforma_prpoa").setNombreVisual("REFORMAS");
		tab_ejecucionP.getColumna("presupuesto_codificado_prpoa").setNombreVisual("CODIFICADO");
		tab_ejecucionP.getColumna("tipo_gestion").setFiltroContenido();
		tab_ejecucionP.getColumna("area").setFiltroContenido();
		
		tab_ejecucionP.setColumnaSuma("presupuesto_inicial_prpoa,reforma_prpoa,presupuesto_codificado_prpoa,certificado,comprometido,devengado,pagado,credito_iva");

		tab_ejecucionP.setRows(30);
		tab_ejecucionP.setLectura(true);
		tab_ejecucionP.dibujar();
		
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_ejecucionP);
		
		Division div_tabla = new Division();
		div_tabla.dividir1(pat_panel);
		agregarComponente(div_tabla);
		
		sel_certificacion.setId("sel_certificacion");
		sel_certificacion.setTitle("VALIDAR CERTIFICACIONES");
		sel_certificacion.setSeleccionTabla(validarCertificaciones(), "ide_prcer");
		sel_certificacion.getTab_seleccion().ejecutarSql();
		sel_certificacion.getBot_aceptar().setMetodo("validaCert");
		agregarComponente(sel_certificacion);
		
		sel_compromiso.setId("sel_compromiso");
		sel_compromiso.setTitle("VALIDAR COMPROMISOS");
		sel_compromiso.setSeleccionTabla(validarCompromisos(), "ide_prtra");
		sel_compromiso.getTab_seleccion().ejecutarSql();
		sel_compromiso.getBot_aceptar().setMetodo("validaComp");
		agregarComponente(sel_compromiso);
		
		sel_eje_proyecto.setId("sel_eje_proyecto");
		sel_eje_proyecto.setTitle("EJECUCIÓN POR PROYECTO");
		sel_eje_proyecto.setSeleccionTabla(ejecucionProyecto(), "codigo");
		sel_eje_proyecto.getTab_seleccion().ejecutarSql();
		sel_eje_proyecto.setRadio();
		sel_eje_proyecto.getTab_seleccion().setColumnaSuma("codificado,certificado,saldo_certificar,comprometido,saldo_comprometido,devengado,saldo_por_devengar");
		sel_eje_proyecto.getBot_aceptar().setMetodo("ejecucionPry");
		agregarComponente(sel_eje_proyecto);
		
		sel_eje_area.setId("sel_eje_area");
		sel_eje_area.setTitle("EJECUCIÓN POR AREA");
		sel_eje_area.setSeleccionTabla(ejecucionArea(), "codigo");
		sel_eje_area.getTab_seleccion().ejecutarSql();
		sel_eje_area.setRadio();
		sel_eje_area.getTab_seleccion().setColumnaSuma("codificado,certificado,saldo_certificar,comprometido,saldo_comprometido,devengado,saldo_por_devengar");
		sel_eje_area.getBot_aceptar().setMetodo("ejecucion_Area");
		agregarComponente(sel_eje_area);
		
	}
	
	public void cargaEjecucion(){
		
		String fecha_inicial=cal_fecha_inicial.getFecha();
		String fecha_final=cal_fecha_final.getFecha();
		
		tab_ejecucionP.setSql(ser_general.getEjecucionPresupuestaria(fecha_inicial,fecha_final));
		tab_ejecucionP.ejecutarSql();
		utilitario.addUpdate("tab_ejecucionP");
		
		for (int i=0;i<tab_ejecucionP.getTotalFilas();i++)
		{	
			if(pckUtilidades.CConversion.CDbl_2(tab_ejecucionP.getValor(i,"saldo_por_devengar")) <0)
			{
				utilitario.agregarNotificacionInfo("MENSAJE - IMPORTANTE","LA EJECUCIÓN PRESUPUESTARIA ESTA DESCUADRADA, REVISAR DEVENGADOS");
				break;
			}	
		}

	}
	
	public void limpiar(){
		
		tab_ejecucionP.limpiar();
		
		tab_ejecucionP.setSql(ser_general.getEjecucionPresupuestaria("1900-01-01","1900-01-01"));
		tab_ejecucionP.ejecutarSql();
		utilitario.addUpdate("tab_ejecucionP");
	}

	public void validaCert()
	{

		if(sel_certificacion.isVisible()){
			sel_certificacion.cerrar();
		}
		else 
		{
			sel_certificacion.setSql(validarCertificaciones());
			sel_certificacion.getTab_seleccion().ejecutarSql();
			sel_certificacion.dibujar();
		}
		
	}
	
	public void validaComp()
	{

		if(sel_compromiso.isVisible()){
			sel_compromiso.cerrar();
		}
		else 
		{
			sel_compromiso.setSql(validarCompromisos());
			sel_compromiso.getTab_seleccion().ejecutarSql();
			sel_compromiso.dibujar();
		}
		
	}
	
	public void ejecucionPry()
	{

		if(sel_eje_proyecto.isVisible()){
			sel_eje_proyecto.cerrar();
		}
		else 
		{
			sel_eje_proyecto.setSql(ejecucionProyecto());
			sel_eje_proyecto.getTab_seleccion().ejecutarSql();
			sel_eje_proyecto.dibujar();
		}
		
	}
	
	public void ejecucion_Area()
	{

		if(sel_eje_area.isVisible()){
			sel_eje_area.cerrar();
		}
		else 
		{
			sel_eje_area.setSql(ejecucionArea());
			sel_eje_area.getTab_seleccion().ejecutarSql();
			sel_eje_area.dibujar();
		}
		
	}
	
	private String validarCertificaciones()
	{
		String sql="select *, valor_certificacion_prcer-certificadoT as Diferencia from ("
		+" select cert.ide_prcer,nro_certificacion_prcer,fecha_prcer,coalesce(valor_certificacion_prcer,0) as valor_certificacion_prcer"
		+" ,valor_liberado_prcer, sum(coalesce(valor_certificado_prpoc,0)) as certificadoT from pre_certificacion cert"
		+" left join pre_poa_certificacion dcert on dcert.ide_prcer=cert.ide_prcer"
		//+" where extract(month from fecha_prcer)<=10"
		+" group by cert.ide_prcer,nro_certificacion_prcer,fecha_prcer,valor_certificacion_prcer,valor_liberado_prcer"
		+" order by 1)a order by 7";
		
		return sql;
	}
	
	private String validarCompromisos()
	{
		String sql="select *, total_compromiso_prtra-comprometidoT as Diferencia from ("
				+" select comp.ide_prtra,comp.ide_prtra as numCompromiso,fecha_tramite_prtra,coalesce(total_compromiso_prtra,0) as total_compromiso_prtra"
				+" ,valor_liberado_prtra, sum(coalesce(comprometido_prpot,0)) as comprometidoT from pre_tramite comp"
				+" left join pre_poa_tramite dcomp on dcomp.ide_prtra=comp.ide_prtra"
				//+" where extract(month from fecha_tramite_prtra)<=10"
				+" group by comp.ide_prtra,fecha_tramite_prtra,total_compromiso_prtra,valor_liberado_prtra"
				+" order by 1)a order by 7";
		
		return sql;
	}
	
	private String ejecucionProyecto()
	{
		String fecha_inicial=cal_fecha_inicial.getFecha();
		String fecha_final=cal_fecha_final.getFecha();
		
		String sql="select row_number() over(order by detalle_proyecto) as codigo, detalle_proyecto, sum(presupuesto_codificado_prpoa) as codificado, sum(certificado) as certificado"
				+ ", sum(saldo_certificar) as saldo_certificar, sum(comprometido) as comprometido, sum(saldo_comprometido) as saldo_comprometido"
				+ ", sum(devengado) as devengado, sum(saldo_por_devengar) as saldo_por_devengar"
				+ ", cast(round((coalesce(sum(devengado),0)/coalesce(sum(presupuesto_codificado_prpoa),0))*100,2) as character(4)) || ' %' as ejecucion from (";

			sql+=ser_general.getEjecucionPresupuestaria(fecha_inicial,fecha_final);
					
			sql+=" ) epry "
			   +" group by detalle_proyecto having sum(presupuesto_codificado_prpoa)>0 "
			   +" order by detalle_proyecto ";
		
		return sql;
	}
	
	private String ejecucionArea()
	{
		String fecha_inicial=cal_fecha_inicial.getFecha();
		String fecha_final=cal_fecha_final.getFecha();
		
		String sql="select row_number() over(order by area) as codigo, area, sum(presupuesto_codificado_prpoa) as codificado, sum(certificado) as certificado"
				+ ", sum(saldo_certificar) as saldo_certificar, sum(comprometido) as comprometido, sum(saldo_comprometido) as saldo_comprometido"
				+ ", sum(devengado) as devengado, sum(saldo_por_devengar) as saldo_por_devengar"
				+ ", cast(round((coalesce(sum(devengado),0)/coalesce(sum(presupuesto_codificado_prpoa),0))*100,2) as character(4)) || ' %' as ejecucion from (";

			sql+=ser_general.getEjecucionPresupuestaria(fecha_inicial,fecha_final);
					
			sql+=" ) ear "
			   +" group by area having sum(presupuesto_codificado_prpoa)>0 "
			   +" order by area ";
		
		return sql;
	}

	public void exportarExcelCERT()
	{
		String fecha_inicial=cal_fecha_inicial.getFecha();		
		String fecha_final=cal_fecha_final.getFecha();	
		TablaGenerica tab_anio = utilitario.consultar("select ide_geani as codigo, ide_geani from gen_anio where detalle_geani = cast(extract(year from DATE '"+fecha_inicial+"') as varchar)");
		
		if(tab_anio.getTotalFilas()>0)
		{
	      Tabla tab_tablaXls = new Tabla();
	      tab_tablaXls.setSql(ser_presupuesto.getRptCertificaciones(tab_anio.getValor("ide_geani"),fecha_inicial,fecha_final));
	      tab_tablaXls.ejecutarSql();
	      tab_tablaXls.exportarXLS();
		}
    }
	
	public void exportarExcelCOMP()
	{
		String fecha_inicial=cal_fecha_inicial.getFecha();		
		String fecha_final=cal_fecha_final.getFecha();	
		TablaGenerica tab_anio = utilitario.consultar("select ide_geani as codigo, ide_geani from gen_anio where detalle_geani = cast(extract(year from DATE '"+fecha_inicial+"') as varchar)");
		
		if(tab_anio.getTotalFilas()>0)
		{
	      Tabla tab_tablaXls = new Tabla();
	      tab_tablaXls.setSql(ser_presupuesto.getRptCompromisos(tab_anio.getValor("ide_geani"),fecha_inicial,fecha_final));
	      tab_tablaXls.ejecutarSql();
	      tab_tablaXls.exportarXLS();
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

	public Tabla getTab_ejecucionP() {
		return tab_ejecucionP;
	}

	public void setTab_ejecucionP(Tabla tab_ejecucionP) {
		this.tab_ejecucionP = tab_ejecucionP;
	}

	public SeleccionTabla getSel_certificacion() {
		return sel_certificacion;
	}

	public void setSel_certificacion(SeleccionTabla sel_certificacion) {
		this.sel_certificacion = sel_certificacion;
	}

	public SeleccionTabla getSel_compromiso() {
		return sel_compromiso;
	}

	public void setSel_compromiso(SeleccionTabla sel_compromiso) {
		this.sel_compromiso = sel_compromiso;
	}

	public SeleccionTabla getSel_eje_proyecto() {
		return sel_eje_proyecto;
	}

	public void setSel_eje_proyecto(SeleccionTabla sel_eje_proyecto) {
		this.sel_eje_proyecto = sel_eje_proyecto;
	}

	public SeleccionTabla getSel_eje_area() {
		return sel_eje_area;
	}

	public void setSel_eje_area(SeleccionTabla sel_eje_area) {
		this.sel_eje_area = sel_eje_area;
	}

	

}
