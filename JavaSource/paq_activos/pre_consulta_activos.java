package paq_activos;

import javax.ejb.EJB;

import paq_general.ejb.ServicioGeneral;
import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;

public class pre_consulta_activos extends Pantalla{
	
	private Tabla tab_activos = new Tabla();
	private SeleccionTabla sel_certificacion= new SeleccionTabla();
	private SeleccionTabla sel_compromiso= new SeleccionTabla();
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
	
	@EJB
	private ServicioGeneral ser_general = (ServicioGeneral ) utilitario.instanciarEJB(ServicioGeneral.class);

	public pre_consulta_activos(){

		bar_botones.limpiar();

		bar_botones.agregarComponente(new Etiqueta("Fecha Inicio :"));
		cal_fecha_inicial.setValue(utilitario.obtenerFechaInicioAnio());
		bar_botones.agregarComponente(cal_fecha_inicial);

		bar_botones.agregarComponente(new Etiqueta("Fecha Corte :"));
		cal_fecha_final.setValue(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
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
    			
		tab_activos.setId("tab_activos");
		tab_activos.setSql(ser_general.getEjecucionPresupuestaria("1900-01-01","1900-01-01"));
		//tab_activos.getColumna("ide_prpoa").setVisible(false);
		tab_activos.getColumna("periodo").setNombreVisual("PERIODO");
		tab_activos.getColumna("detalle_geani").setNombreVisual("AÑO");
		tab_activos.getColumna("detalle_geani").setLongitud(15);
		tab_activos.getColumna("cod_prog").setNombreVisual("COD. PROGRAMA");
		tab_activos.getColumna("detalle_programa").setNombreVisual("PROGRAMA");
		tab_activos.getColumna("cod_pry").setNombreVisual("COD. PROYECTO");
		tab_activos.getColumna("detalle_proyecto").setNombreVisual("PROYECTO");
		//tab_activos.getColumna("cod_prod").setNombreVisual("COD. PRODUCTO"); //cod_prod_mc
		tab_activos.getColumna("cod_prod_mc").setNombreVisual("COD. PRODUCTO"); 
		tab_activos.getColumna("cod_prod").setVisible(false);
		tab_activos.getColumna("detalle_producto").setNombreVisual("PRODUCTO");
		tab_activos.getColumna("codigo_obra").setNombreVisual("COD. OBRA"); 
		
		tab_activos.getColumna("codigo_fuente_prfuf").setNombreVisual("CODIGO FUENTE");
		tab_activos.getColumna("detalle_programa").setFiltro(true);
		tab_activos.getColumna("detalle_proyecto").setFiltro(true);
		tab_activos.getColumna("detalle_producto").setFiltro(true);
		tab_activos.getColumna("detalle_subactividad").setFiltro(true);
		tab_activos.getColumna("ide_prpoa").setFiltro(true);
		tab_activos.getColumna("codigo_subactividad").setFiltro(true);
		tab_activos.getColumna("fuente").setFiltro(true);
		tab_activos.getColumna("codigo_clasificador_prcla").setFiltro(true);
		tab_activos.getColumna("codigo_clasificador_prcla").setNombreVisual("PARTIDA");
		tab_activos.getColumna("descripcion_clasificador_prcla").setNombreVisual("DESCRIPCION PARTIDA");
		tab_activos.getColumna("presupuesto_inicial_prpoa").setNombreVisual("ASIGNACION INICIAL");
		tab_activos.getColumna("reforma_prpoa").setNombreVisual("REFORMAS");
		tab_activos.getColumna("presupuesto_codificado_prpoa").setNombreVisual("CODIFICADO");
		tab_activos.setColumnaSuma("presupuesto_inicial_prpoa,reforma_prpoa,presupuesto_codificado_prpoa,certificado,comprometido,devengado,pagado");

		tab_activos.setRows(30);
		tab_activos.setLectura(true);
		tab_activos.dibujar();
		
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_activos);
		
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
		
	}
	
	public void cargaEjecucion(){
		
		String fecha_inicial=cal_fecha_inicial.getFecha();
		String fecha_final=cal_fecha_final.getFecha();
		
		tab_activos.setSql(ser_general.getEjecucionPresupuestaria(fecha_inicial,fecha_final));
		tab_activos.ejecutarSql();
		utilitario.addUpdate("tab_activos");
	}
	
	public void limpiar(){
		
		tab_activos.limpiar();
		
		tab_activos.setSql(ser_general.getEjecucionPresupuestaria("1900-01-01","1900-01-01"));
		tab_activos.ejecutarSql();
		utilitario.addUpdate("tab_activos");
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

	public Tabla gettab_activos() {
		return tab_activos;
	}

	public void settab_activos(Tabla tab_activos) {
		this.tab_activos = tab_activos;
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

	

}
