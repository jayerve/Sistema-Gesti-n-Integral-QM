package paq_juridico;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.OutputLink;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_sistema.aplicacion.Pantalla;
import paq_tesoreria.ejb.ServicioTesoreria;

public class pre_coactiva extends Pantalla {

	private Tabla tab_coactiva = new Tabla();
	private Tabla tab_coactiva_archivo = new Tabla();
	private SeleccionTabla sel_titulos = new SeleccionTabla();
	
	private Confirmar con_guardar_titulo=new Confirmar();
	private Combo com_anio=new Combo();
	
	///reporte
	private Map p_parametros = new HashMap();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();
	private Map map_parametros = new HashMap();
	
	private String str_tituloSeleccionado="";
	
	@EJB
	private ServicioTesoreria ser_tesoreria = (ServicioTesoreria ) utilitario.instanciarEJB(ServicioTesoreria.class);
	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion ) utilitario.instanciarEJB(ServicioFacturacion.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);

	public pre_coactiva() {
		
		bar_botones.getBot_insertar().setRendered(false);
		rep_reporte.setId("rep_reporte"); //id
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");//ejecuta el metodo al aceptar reporte
		agregarComponente(rep_reporte);//agrega el componente a la pantalla
		bar_botones.agregarReporte();//aparece el boton de reportes en la barra de botones
		self_reporte.setId("self_reporte"); //id
		agregarComponente(self_reporte);
		
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
		
		//Boton Actualizar Titulo de credito
		Boton bot_actualizar_titulo=new Boton();
		bot_actualizar_titulo.setIcon("ui-icon-document");
		bot_actualizar_titulo.setValue("Nuevo Proceso");
		bot_actualizar_titulo.setMetodo("agregar_titulo");
		bar_botones.agregarBoton(bot_actualizar_titulo);
		
		Boton bot_cargar_req = new Boton();
		bot_cargar_req.setValue("Subir Anexo");
		bot_cargar_req.setMetodo("insertarRequisito");
		bot_cargar_req.setIcon("ui-icon-plusthick");
		bar_botones.agregarBoton(bot_cargar_req);
		
		con_guardar_titulo.setId("con_guardar_titulo");
		con_guardar_titulo.setMessage("¿Esta Seguro de Iniciar un nuevo proceso COACTIVO?");
		con_guardar_titulo.setTitle("Confirmación");
		con_guardar_titulo.getBot_aceptar().setMetodo("agregar_titulo");
		agregarComponente(con_guardar_titulo);
		
		//Boton portal remates
		OutputLink bot_portal_remates=new OutputLink();
		bot_portal_remates.setId("bot_portal_remates");
		bot_portal_remates.setTarget("_blank");
		bot_portal_remates.setValue(utilitario.getVariable("p_web_portal_remates"));
		bot_portal_remates.setStyle("display: inline-block; margin: 0 0 -8px 0;");
		bot_portal_remates.getChildren().add(new Etiqueta("Portal de Remates"));
		
		bar_botones.agregarComponente(bot_portal_remates);
		

		// TODO Auto-generated constructor stub
		tab_coactiva.setId("tab_coactiva");
		tab_coactiva.setTabla("jur_coactiva", "ide_jucoa", 1);
		tab_coactiva.setHeader("PROCESO COACTIVO");
		tab_coactiva.getColumna("fecha_jucoa").setValorDefecto(utilitario.getFechaActual());
		tab_coactiva.getColumna("ide_juepr").setCombo("jur_etapa_procesal", "ide_juepr", "detalle_juepr", "");
		//tab_coactiva.getColumna("ide_recli").setCombo(ser_facturacion.getClientes("0,1"));
		tab_coactiva.getColumna("ide_recli").setLectura(true);
		tab_coactiva.getColumna("ide_facon").setCombo(ser_tesoreria.getSqlTitulosCreditoCoactiva("-1"));
		tab_coactiva.getColumna("ide_facon").setAutoCompletar();
		tab_coactiva.getColumna("ide_facon").setLectura(true);
		tab_coactiva.getColumna("activo_jucoa").setValorDefecto("true");
		tab_coactiva.getColumna("ide_juepr").setValorDefecto("1");
		tab_coactiva.getColumna("dias_vencidos_jucoa").setValorDefecto("91");
		tab_coactiva.getColumna("cuantia_jucoa").setValorDefecto("0.00");
		tab_coactiva.getColumna("aplica_convenio_pago_jucoa").setValorDefecto("false");
		tab_coactiva.setCondicion("ide_geani=-1"); 
		tab_coactiva.setTipoFormulario(true);
		tab_coactiva.getGrid().setColumns(4);
		tab_coactiva.agregarRelacion(tab_coactiva_archivo);
		tab_coactiva.dibujar();
 
		PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_coactiva);
        
        tab_coactiva_archivo.setId("tab_coactiva_archivo");
        tab_coactiva_archivo.setTabla("jur_coactiva_archivo", "ide_jucar", 2);
        tab_coactiva_archivo.setHeader("EXPEDIENTE");
        tab_coactiva_archivo.getColumna("fecha_jucar").setValorDefecto(utilitario.getFechaActual());
        tab_coactiva_archivo.getColumna("activo_jucar").setValorDefecto("true");
        tab_coactiva_archivo.getColumna("adjunto_jucar").setUpload("coactivas");
        tab_coactiva_archivo.getColumna("activo_jucar").setLectura(true);
        tab_coactiva_archivo.dibujar();
        
		PanelTabla pat_archivo= new PanelTabla();
		pat_archivo.setPanelTabla(tab_coactiva_archivo);
        
        Division div_division = new Division();

        div_division.setId("div_division");
        div_division.dividir2(pat_panel,pat_archivo,"60%","H");
        agregarComponente(div_division);
        
        inicializaTitulos();

	}
	
	public void inicializaTitulos(){
		sel_titulos.setId("sel_titulos");
		sel_titulos.setTitle("TITULOS DE CREDITO");
		sel_titulos.setSeleccionTabla(ser_tesoreria.getSqlTitulosCreditoCoactiva("0"), "ide_facon");
		sel_titulos.getTab_seleccion().getColumna("nro_titulo_facon").setFiltro(true);
		sel_titulos.getTab_seleccion().getColumna("ruc_comercial_recli").setFiltroContenido();
		sel_titulos.getTab_seleccion().getColumna("ruc_comercial_recli").setLongitud(30);
		sel_titulos.getTab_seleccion().getColumna("razon_social_recli").setFiltroContenido();
		sel_titulos.getTab_seleccion().getColumna("razon_social_recli").setLongitud(100);
		sel_titulos.getTab_seleccion().getColumna("nombre_comercial_recli").setFiltroContenido();
		sel_titulos.getTab_seleccion().getColumna("nombre_comercial_recli").setLongitud(100);
		sel_titulos.getTab_seleccion().getColumna("descripcion").setFiltroContenido();
		sel_titulos.getTab_seleccion().getColumna("descripcion").setLongitud(100);
		
		sel_titulos.setRadio();
		sel_titulos.getTab_seleccion().ejecutarSql();
		sel_titulos.getBot_aceptar().setMetodo("agregar_titulo");
		agregarComponente(sel_titulos);
	}
	
	
	public void seleccionaElAnio (){
		if(com_anio.getValue()!=null){
			tab_coactiva.setCondicion("ide_geani="+com_anio.getValue());
			tab_coactiva.ejecutarSql();
			tab_coactiva_archivo.ejecutarValorForanea(tab_coactiva.getValorSeleccionado());
		}
		else{
			utilitario.agregarMensajeInfo("Selecione un año", "");
		}
	}
	
	public void insertarRequisito()
	{
		if(com_anio.getValue()==null){
			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar un Año");
			return;
		}
		
		if(tab_coactiva.getTotalFilas()<1)
		{
			utilitario.agregarNotificacionInfo("No se puede insertar", "Seleccione un Requisito antes de continuar...");
			return;
		}
		
		tab_coactiva_archivo.insertar();
		
	}

	public void agregar_titulo(){
		
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un año", "");
			return;
		}

		if(sel_titulos.isVisible())
		{
			str_tituloSeleccionado=sel_titulos.getValorSeleccionado();
			if(str_tituloSeleccionado!=null){
				sel_titulos.cerrar();
				con_guardar_titulo.dibujar();
				utilitario.addUpdate("con_guardar_titulo");
			}
			else
			{
				utilitario.agregarMensajeInfo("Selecione un Titulo de Credito", "");
			}
		}
		else if (con_guardar_titulo.isVisible()){
			
			TablaGenerica cliente = utilitario.consultar("select ide_facon, ide_recli from fac_convenio where ide_facon="+str_tituloSeleccionado);
			//tab_coactiva.modificar(tab_coactiva.getFilaActual());
			tab_coactiva.insertar();
			tab_coactiva.setValor("ide_geani",com_anio.getValue()+"");
			tab_coactiva.setValor("ide_recli", cliente.getValor("ide_recli"));
			tab_coactiva.setValor("ide_facon", str_tituloSeleccionado);
			//utilitario.addUpdate("tab_coactiva");
			tab_coactiva.guardar();
			con_guardar_titulo.cerrar();
			guardarPantalla();
		}
		else
		{
			//set_actualizar_cliente.getTab_seleccion().setSql(ser_facturacion.getClientes("0,1"));
			sel_titulos.setSql(ser_tesoreria.getSqlTitulosCreditoCoactiva(com_anio.getValue()+""));
			sel_titulos.getTab_seleccion().ejecutarSql();
			sel_titulos.dibujar();
		}
	}

	
	
	
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		
		//if(com_anio.getValue()==null){
			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar un año");
		//	return;
		
		//}
		
		//if (tab_coactiva.isFocus()) {
		//	tab_coactiva.insertar();
		//	tab_coactiva.setValor("ide_geani",com_anio.getValue()+"");
		//}

	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		
		if(pckUtilidades.CConversion.CInt(tab_coactiva.getValor("dias_vencidos_jucoa"))<90)
		{
			utilitario.agregarMensajeError("Guardar", "Para registrar debe de tener al menos 90 dias de vencimiento.");
			return;
		}
		
		if (tab_coactiva.guardar()) {
			tab_coactiva_archivo.guardar();
			guardarPantalla();
		}

	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		if (tab_coactiva.isFocus()) {
			tab_coactiva.eliminar();

		}
	}

	public Tabla getTab_coactiva() {
		return tab_coactiva;
	}

	public void setTab_coactiva(Tabla tab_coactiva) {
		this.tab_coactiva = tab_coactiva;
	}

	

	public SeleccionTabla getSel_titulos() {
		return sel_titulos;
	}

	public void setSel_titulos(SeleccionTabla sel_titulos) {
		this.sel_titulos = sel_titulos;
	}

	public Confirmar getCon_guardar_titulo() {
		return con_guardar_titulo;
	}

	public void setCon_guardar_titulo(Confirmar con_guardar_titulo) {
		this.con_guardar_titulo = con_guardar_titulo;
	}

	public Tabla getTab_coactiva_archivo() {
		return tab_coactiva_archivo;
	}

	public void setTab_coactiva_archivo(Tabla tab_coactiva_archivo) {
		this.tab_coactiva_archivo = tab_coactiva_archivo;
	}
	
	

}
