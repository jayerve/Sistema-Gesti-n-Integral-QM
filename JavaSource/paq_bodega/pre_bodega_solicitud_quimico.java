package paq_bodega;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.SelectEvent;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_bodega.ejb.ServicioBodega;
import paq_bodega.ejb.ServicioCorreoBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;
import pckEntidades.EnvioMail;

public class pre_bodega_solicitud_quimico  extends Pantalla{
	
	private final String ESTILO_ETIQUETA = "font-size:15px;font-weight: bold;text-decoration: underline;color:red";
	
	private Tabla tab_tabla = new Tabla();
	private Tabla tab_tabla_detalle = new Tabla();
	
	private Combo com_anio = new Combo();
	private Combo com_bodega = new Combo();
	private String  enProceso= "EN PROCESO";
	private String  procesada= "PROCESADA";
	private String  tipoSolicitudQuimico= "QUIMICO";
	private String reactorCircular="CIRCULAR";
	private String reactorReactangular="RECTANGULAR";
	
	private Boolean esEditable=false;
	
	private Confirmar con_guardar=new Confirmar();
	
	private Confirmar con_guardar_solicitud=new Confirmar();
	
	private  SeleccionTabla set_solicitudes_procesadas= new SeleccionTabla();
	
	
	private Map p_parametros = new HashMap();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();
	
	private SeleccionTabla set_catalogo = new SeleccionTabla();
	
	private SeleccionTabla set_solicitante = new SeleccionTabla();
	private SeleccionTabla set_jefe_solicitante = new SeleccionTabla();
	
	
	//pckUtilidades.Utilitario util= new pckUtilidades.Utilitario();
	
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	

	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario
			.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioCorreoBodega ser_bodega_correo = (ServicioCorreoBodega) utilitario.instanciarEJB(ServicioCorreoBodega.class);
	
	@EJB
	private ServicioBodega ser_bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);
	
	@EJB
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);

	Boton editar_solicitud = new Boton();
	
	public pre_bodega_solicitud_quimico()  {
		
		
		// REACTOR 
		
	
		
		System.out.println("pre_bodega_solicitud");
		
		///reporte
		rep_reporte.setId("rep_reporte"); //id
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");//ejecuta el metodo al aceptar reporte
		agregarComponente(rep_reporte);//agrega el componente a la pantalla
		bar_botones.agregarReporte();//aparece el boton de reportes en la barra de botones
		self_reporte.setId("self_reporte"); //id
		agregarComponente(self_reporte);
		
		tab_tabla.setId("tab_tabla");
		tab_tabla_detalle.setHeader("SOLICITUD DE ITEMS");
		tab_tabla.setCondicion("ide_solicitud=-1");
		tab_tabla.setCampoOrden("ide_solicitud desc");
		tab_tabla.setTabla("solicitud_items", "ide_solicitud", 1);
		tab_tabla.getGrid().setColumns(4);
		
		
		tab_tabla.getColumna("ide_solicitud").setLectura(true);
		tab_tabla.getColumna("ide_solicitud").setNombreVisual("NÚMERO DE SOLICITUD");

		tab_tabla.getColumna("estado_solicitud").setLectura(true);
		tab_tabla.getColumna("estado_solicitud").setNombreVisual("ESTADO DE SOLICITUD");
		tab_tabla.getColumna("estado_solicitud").setLongitud(60);
		
		
		tab_tabla.getColumna("fecha_solicitud").setLectura(true);
		tab_tabla.getColumna("fecha_solicitud").setValorDefecto(utilitario.getFechaActual());
		tab_tabla.getColumna("fecha_solicitud").setNombreVisual("FECHA DE SOLICITUD");
		
		tab_tabla.getColumna("ide_gtemp_solicitante").setCombo(ser_nomina.servicioEmpleadosActivos("true,false"));
		tab_tabla.getColumna("ide_gtemp_solicitante").setAutoCompletar();
		tab_tabla.getColumna("ide_gtemp_solicitante").setLectura(true);
		tab_tabla.getColumna("ide_gtemp_solicitante").setNombreVisual("EMPLEADO SOLICITANTE");

		tab_tabla.getColumna("ide_gtemp_aprobador").setCombo(ser_nomina.servicioEmpleadosActivos("true,false"));
		tab_tabla.getColumna("ide_gtemp_aprobador").setAutoCompletar();
		tab_tabla.getColumna("ide_gtemp_aprobador").setLectura(true);
		tab_tabla.getColumna("ide_gtemp_aprobador").setRequerida(true);
		tab_tabla.getColumna("ide_gtemp_aprobador").setNombreVisual("EMPLEADO APROBADOR");
		
		tab_tabla.getColumna("ide_gtemp_despachador").setCombo(ser_nomina.servicioEmpleadosActivos("true,false"));
		tab_tabla.getColumna("ide_gtemp_despachador").setAutoCompletar();
		tab_tabla.getColumna("ide_gtemp_despachador").setLectura(true);
		tab_tabla.getColumna("ide_gtemp_despachador").setNombreVisual("EMPLEADO DESPACHADOR");
		
		
		tab_tabla.getColumna("fecha_aprobacion").setLectura(true);
		tab_tabla.getColumna("fecha_aprobacion").setNombreVisual("FECHA DE APROBACIÓN");
		
		
		tab_tabla.getColumna("fecha_despacho").setLectura(true);
		tab_tabla.getColumna("fecha_despacho").setNombreVisual("FECHA DE DESPACHO");
		
		
		tab_tabla.getColumna("tipo").setNombreVisual("TIPO");
		tab_tabla.getColumna("tipo").setLectura(true);
		
		tab_tabla.getColumna("aprobacion_aprobador").setVisible(false);
		tab_tabla.getColumna("rechazo_aprobador").setVisible(false);
		tab_tabla.getColumna("aprobacion_bodega").setVisible(false);
		tab_tabla.getColumna("rechazo_bodega").setVisible(false);
		tab_tabla.getColumna("observacion").setVisible(false);
		
		
		tab_tabla.getColumna("ide_boubi").setVisible(false);
		tab_tabla.getColumna("ide_geani").setVisible(false);
	
		
		
		
		//_______________DETALLE SOLICITUD____________________________________________________________________
		
		tab_tabla_detalle.setId("tab_tabla_detalle");
		tab_tabla_detalle.setHeader("DETALLE DE LA SOLICITUD");
		tab_tabla_detalle.setTabla("bodt_solicitud_detalle_quimico", "ide_bosdq", 2);
		tab_tabla.agregarRelacion(tab_tabla_detalle);
		
		
		tab_tabla_detalle.getColumna("ide_bocam").setCombo(ser_bodega.getCatalogoExistencias());
		tab_tabla_detalle.getColumna("ide_bocam").setRequerida(true);
		tab_tabla_detalle.getColumna("ide_bocam").setFiltroContenido();
		tab_tabla_detalle.getColumna("ide_bocam").setMetodoChange("cargarCatalogo");
		tab_tabla_detalle.getColumna("ide_bocam").setLectura(true);
		tab_tabla_detalle.getColumna("ide_bocam").setNombreVisual("FAMILIA");
		tab_tabla_detalle.getColumna("ide_bocam").setLongitud(40);
		
		
		tab_tabla_detalle.getColumna("ide_bosdq").setVisible(true);
		tab_tabla_detalle.getColumna("ide_bosdq").setNombreVisual("ID");
		tab_tabla_detalle.getColumna("ide_bosdq").setOrden(1);
		
		tab_tabla_detalle.getColumna("cantidad_disponible").setVisible(true);
		tab_tabla_detalle.getColumna("cantidad_disponible").setNombreVisual("CANTIDAD DISPONIBLE");
		tab_tabla_detalle.getColumna("cantidad_disponible").setLongitud(40);
		tab_tabla_detalle.getColumna("cantidad_disponible").setLectura(true);;

		
		
		tab_tabla_detalle.getColumna("dosificacion").setVisible(true);
		tab_tabla_detalle.getColumna("dosificacion").setNombreVisual("DOSIFICACION FORMULACION");
		tab_tabla_detalle.getColumna("dosificacion").setLongitud(40);
		tab_tabla_detalle.getColumna("dosificacion").setOrden(2);
		tab_tabla_detalle.getColumna("dosificacion").setMetodoChange("calcular");
		
		tab_tabla_detalle.getColumna("batch").setVisible(true);
		tab_tabla_detalle.getColumna("batch").setNombreVisual("CANTIDAD BATCH");
		tab_tabla_detalle.getColumna("batch").setLongitud(30);
		tab_tabla_detalle.getColumna("batch").setOrden(3);
		tab_tabla_detalle.getColumna("batch").setMetodoChange("calcular");
		
		
		tab_tabla_detalle.getColumna("cantidad_reactor_rectangular").setVisible(true);
		tab_tabla_detalle.getColumna("cantidad_reactor_rectangular").setNombreVisual("CANTIDAD REACTOR RECTANGULAR");
		tab_tabla_detalle.getColumna("cantidad_reactor_rectangular").setLongitud(45);
		tab_tabla_detalle.getColumna("cantidad_reactor_rectangular").setOrden(4);
		tab_tabla_detalle.getColumna("cantidad_reactor_rectangular").setLectura(true);
		
		
		tab_tabla_detalle.getColumna("cantidad_reactor_circular").setVisible(true);
		tab_tabla_detalle.getColumna("cantidad_reactor_circular").setNombreVisual("CANTIDAD REACTOR CIRCULAR");
		tab_tabla_detalle.getColumna("cantidad_reactor_circular").setLongitud(45);
		tab_tabla_detalle.getColumna("cantidad_reactor_circular").setOrden(5);
		tab_tabla_detalle.getColumna("cantidad_reactor_circular").setLectura(true);
		
		
		
		tab_tabla_detalle.getColumna("cantidad_reactor_total").setVisible(true);
		tab_tabla_detalle.getColumna("cantidad_reactor_total").setNombreVisual("CANTIDAD TOTAL");
		tab_tabla_detalle.getColumna("cantidad_reactor_total").setLongitud(30);
		tab_tabla_detalle.getColumna("cantidad_reactor_total").setEstilo(ESTILO_ETIQUETA);
		tab_tabla_detalle.getColumna("cantidad_reactor_total").setOrden(6);
		tab_tabla_detalle.getColumna("cantidad_reactor_total").setLectura(true);
		
		tab_tabla_detalle.getColumna("observacion").setVisible(true);
		tab_tabla_detalle.getColumna("observacion").setNombreVisual("OBSERVACIÓN");
		tab_tabla_detalle.getColumna("observacion").setLongitud(40);
		tab_tabla_detalle.getColumna("observacion").setOrden(7);
		tab_tabla_detalle.getColumna("observacion").setLectura(false);


		tab_tabla_detalle.getColumna("cantidad_solicitada").setVisible(false);
		//tab_tabla_detalle.getColumna("cantidad_disponible").setVisible(false);
		//tab_tabla_detalle.getColumna("ide_solrea").setVisible(false);
		//tab_tabla_detalle.getColumna("ide_solcir").setVisible(false);



		
		con_guardar.setId("con_guardar");
		agregarComponente(con_guardar);
		
		
		con_guardar_solicitud.setId("con_guardar_solicitud");
		agregarComponente(con_guardar_solicitud);
		
		
		

		tab_tabla.setTipoFormulario(true);
		tab_tabla.dibujar();
		tab_tabla_detalle.dibujar();
		
		
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_tabla);

		PanelTabla pat_panel_detalle = new PanelTabla();
		pat_panel_detalle.setPanelTabla(tab_tabla_detalle);
		
		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(pat_panel, pat_panel_detalle, "50%", "H");
		agregarComponente(div_division);
		
		//____________________BOTONES_______________________________________________________________________________

		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true", "true,false"));
		com_anio.setMetodo("seleccionaParametros");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");

		com_bodega.setCombo(ser_bodega.getBodegas());
		com_bodega.setMetodo("seleccionaParametros");
		com_bodega.setStyle("width: 100px; margin: 0 0 -8px 0;");

		bar_botones.agregarComponente(new Etiqueta("AÑO:"));
		bar_botones.agregarComponente(com_anio);
		bar_botones.agregarComponente(new Etiqueta("BODEGA:"));
		bar_botones.agregarComponente(com_bodega);
		
		Boton bot_factura = new Boton();
		bot_factura.setValue("Agregar Item");
		bot_factura.setTitle("AGREGAR ITEM");
		bot_factura.setIcon("ui-icon-note");
		bot_factura.setMetodo("importarItem");
		
		
		editar_solicitud.setValue("Editar Solicitud");
		editar_solicitud.setTitle("Editar SOLICITUD");
		editar_solicitud.setIcon("ui-icon-person");
		editar_solicitud.setMetodo("editarSolicitud");


		Boton bot_jefe_solicitante = new Boton();
		bot_jefe_solicitante.setValue("Jefe Aprobador");
		bot_jefe_solicitante.setTitle("JEFE APROBADOR");
		bot_jefe_solicitante.setIcon("ui-icon-person");
		bot_jefe_solicitante.setMetodo("importarJefeSolicitante");
		
		
		bar_botones.agregarBoton(bot_factura);
		//bar_botones.agregarBoton(bot_certificacion);
		bar_botones.agregarBoton(bot_jefe_solicitante);
		bar_botones.agregarBoton(editar_solicitud);
		
		
		//________________________MODAL__________________________________________________________________________
		set_catalogo.setId("set_catalogo");
		set_catalogo.setSeleccionTabla(ser_bodega.getSqlInventarioActualPorBodegaConSaldoQuimico("-1", "-1"), "ide_bocam");

		set_catalogo.setTitle("Seleccione un item");
		set_catalogo.getBot_aceptar().setMetodo("aceptarItem");
		set_catalogo.getTab_seleccion().getColumna("descripcion_bocam").setFiltroContenido();

		set_catalogo.getTab_seleccion().getColumna("IDE_BOINV").setFiltroContenido();
		
	
		set_catalogo.setCheck();
		agregarComponente(set_catalogo);
		
		
		
		
		set_solicitante.setId("set_solicitante");
		set_solicitante.setSeleccionTabla(ser_bodega.getEmpleadosActivos(), "ide_gtemp");
		set_solicitante.getTab_seleccion().getColumna("nombre").setFiltroContenido();

		set_solicitante.setTitle("Seleccione el usuario para la solicitud");
		set_solicitante.getBot_aceptar().setMetodo("aceptarSolicitante");
		set_solicitante.setRadio();
		agregarComponente(set_solicitante);

		set_jefe_solicitante.setId("set_jefe_solicitante");
		set_jefe_solicitante.setSeleccionTabla(ser_nomina.servicioJefesInmediatos(), "ide_gtemp");
		set_jefe_solicitante.getTab_seleccion().getColumna("NOMBRES_APELLIDOS").setFiltro(true);
		set_jefe_solicitante.getTab_seleccion().getColumna("NOMBRES_APELLIDOS").setNombreVisual("APELLIDOS Y NOMBRES");

		set_jefe_solicitante.setTitle("Seleccione el jefe Aprobador");
		set_jefe_solicitante.getBot_aceptar().setMetodo("aceptarJefeSolicitante");
		set_jefe_solicitante.setRadio();
		agregarComponente(set_jefe_solicitante);
		
		prepararSelectorSolicitudesProcesadas();
		
	}
	
public void calcular(AjaxBehaviorEvent event) {
		tab_tabla_detalle.modificar(event);
		try {
			double dosificacion = Double.parseDouble(tab_tabla_detalle.getValor("dosificacion").toString());
			
			
			double batch = Double.parseDouble(tab_tabla_detalle.getValor("batch").toString());
			
	
			TablaGenerica tab_reactorCircular=utilitario.consultar("SELECT * from bodt_solicitudes_reactor  where nombre in ("+ "'"+ reactorCircular + "'"+")");
			TablaGenerica tab_reactorRectangular=utilitario.consultar("SELECT * from bodt_solicitudes_reactor  where nombre in ("+ "'"+ reactorReactangular + "'"+")");
			
			double valorReactorCircular= Double.parseDouble(tab_reactorCircular.getValor("valor").toString());
			double valorReactorRectangular= Double.parseDouble(tab_reactorRectangular.getValor("valor").toString());

			System.out.println("VALOR DEL REACTOR CIR "+ valorReactorCircular);
			System.out.println("VALOR DEL REACTOR REC "+ valorReactorRectangular);
			
			double valorCantidadQuimicoCircular= ((dosificacion*valorReactorCircular)/1000)*batch;
			double valorCantidadQuimicoRectangular= ((dosificacion*valorReactorRectangular)/1000)*batch;
			String auxQC=utilitario.getFormatoNumero(valorCantidadQuimicoCircular,0);
			String auxQR=utilitario.getFormatoNumero(valorCantidadQuimicoRectangular,0);
			double auxSumCT=Double.parseDouble(auxQC)+Double.parseDouble(auxQR);
			
			tab_tabla_detalle.setValor("cantidad_reactor_circular", utilitario.getFormatoNumero(valorCantidadQuimicoCircular, 1));
			tab_tabla_detalle.setValor("cantidad_reactor_rectangular", utilitario.getFormatoNumero(valorCantidadQuimicoRectangular, 1));
			
			tab_tabla_detalle.setValor("cantidad_reactor_total", utilitario.getFormatoNumero(auxSumCT, 1));
			
			tab_tabla_detalle.setValor("cantidad_solicitada", utilitario.getFormatoNumero(valorCantidadQuimicoCircular+valorCantidadQuimicoRectangular, 1));
			
		

		} catch (NumberFormatException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		utilitario.addUpdateTabla(tab_tabla_detalle, "cantidad_solicitada", "");

		utilitario.addUpdate("tab_tabla_detalle");
	}
	
public void prepararSelectorSolicitudesProcesadas() {
	
	set_solicitudes_procesadas.setId("set_solicitudes_procesadas");
	set_solicitudes_procesadas.setTitle("SELECCIONE UNA SOLICITUD EN ESTADO PROCESADA");
	set_solicitudes_procesadas.setSeleccionTabla("Select ide_solicitud, ide_solicitud as id, estado_solicitud, fecha_solicitud, observacion from  solicitud_items  where  estado_solicitud="+ "'"+ procesada + "'" + " and tipo="+ "'"+ tipoSolicitudQuimico + "'", "ide_solicitud");
	set_solicitudes_procesadas.getTab_seleccion().getColumna("id").setFiltro(true);
	set_solicitudes_procesadas.getTab_seleccion().getColumna("id").setNombreVisual("IDE SOLICITUD");
	
	set_solicitudes_procesadas.getTab_seleccion().getColumna("estado_solicitud");
	set_solicitudes_procesadas.getTab_seleccion().getColumna("estado_solicitud").setLongitud(50);
	set_solicitudes_procesadas.getTab_seleccion().getColumna("estado_solicitud").setNombreVisual("ESTADO SOLICITUD");

	set_solicitudes_procesadas.getTab_seleccion().getColumna("fecha_solicitud");
	set_solicitudes_procesadas.getTab_seleccion().getColumna("fecha_solicitud").setLongitud(50);
	set_solicitudes_procesadas.getTab_seleccion().getColumna("fecha_solicitud").setNombreVisual("FECHA SOLICITUD");
	
	
	set_solicitudes_procesadas.getTab_seleccion().getColumna("observacion");
	set_solicitudes_procesadas.getTab_seleccion().getColumna("observacion").setLongitud(70);
	set_solicitudes_procesadas.getTab_seleccion().getColumna("observacion").setNombreVisual("OBSERVACION");
	
	
	set_solicitudes_procesadas.getBot_aceptar().setMetodo("aceptarSolicitudProcesada");
	agregarComponente(set_solicitudes_procesadas);		

}

public  void aceptarSolicitudProcesada(){
	String str_seleccionado = set_solicitudes_procesadas.getSeleccionados();
	try {
		if (str_seleccionado != null) {
			try {
				//tab_tabla.setValor("ide_solicitud",str_seleccionado);						
			    set_solicitudes_procesadas.cerrar();	
				tab_tabla.setCondicion("ide_solicitud=" + str_seleccionado);
				
				tab_tabla.ejecutarSql();
				tab_tabla_detalle.ejecutarValorForanea(tab_tabla.getValorSeleccionado());
				//tab_tabla.modificar(tab_tabla.getFilaActual());
				
				utilitario.addUpdate("tab_tabla");
				utilitario.addUpdate("tab_tabla_detalle");
				esEditable=true;
				
				editar_solicitud.setDisabled(true);
				utilitario.addUpdate("editar_solicitud");
				
			} catch (Exception e) {
				System.out.println("ERROR"+e);
			}

		    
			//guardarPantalla();
		
		} else {
			utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
		}
		
	} catch (Exception e) {
		System.out.println("EXEPTION"+ e);
	}

}


public void editarSolicitud() {
	set_solicitudes_procesadas.setId("set_solicitudes_procesadas");
	set_solicitudes_procesadas.dibujar();
	set_solicitudes_procesadas.redibujar();
}




	public void importarItem() {

		if (tab_tabla.getFilaSeleccionada() != null) {
			String ide_geani = com_anio.getValue().toString();
			String ide_boubi = com_bodega.getValue().toString();
			System.out.println("pre_egreso_existencia:importarItem ide_geani: " + ide_geani + " ide_boubi: " + ide_boubi);
			
			set_catalogo.getTab_seleccion().setSql(ser_bodega.getSqlInventarioActualPorBodegaConSaldo(ide_geani, ide_boubi));
			set_catalogo.getTab_seleccion().ejecutarSql();
			set_catalogo.dibujar();
		} else {
			utilitario.agregarMensaje("Debe seleccionar una solicitud existente o crear una nueva solicitud", "");
		}
	}
	

	public void importarJefeSolicitante() {

		if (tab_tabla.getFilaSeleccionada() != null) {
			set_jefe_solicitante.getTab_seleccion().setSql(ser_nomina.servicioJefesInmediatos());
			set_jefe_solicitante.getTab_seleccion().ejecutarSql();
			set_jefe_solicitante.dibujar();
		} else {
			utilitario.agregarMensaje("Debe seleccionar un egreso existente o crear un nuevo egreso", "");
		}
	}
	
	public void seleccionaParametros() {
		if (com_anio.getValue() != null && com_bodega.getValue() != null) {
			tab_tabla.setCondicion("ide_geani=" + com_anio.getValue() + " AND ide_boubi=" + com_bodega.getValue()+ " and estado_solicitud="+ "'"+ procesada + "' "+ " and tipo="+ "'"+ tipoSolicitudQuimico + "'");
			tab_tabla.ejecutarSql();
			tab_tabla_detalle.ejecutarValorForanea(tab_tabla.getValorSeleccionado());
		} else {
			utilitario.agregarMensajeInfo("Selecione un año y bodega", "");
		}
	}
	
	public void aceptarItem() {
		System.out.println("pre_egreso_existencia:aceptarItem");

		String str_seleccionados = set_catalogo.getTab_seleccion().getFilasSeleccionadas();

		String ide_bocam = str_seleccionados; 
		String ide_boubi = com_bodega.getValue().toString();
		String ide_geani = com_anio.getValue().toString();

        
		System.out.println("ELEMENTO SLECCIONADO " +str_seleccionados);
	
		
		if (str_seleccionados != null) {
		   TablaGenerica tg_catalogo = ser_bodega.getTablaGenericaCatalogo(ide_bocam, ide_boubi, ide_geani);
						Double subtotal = (double) 0;
			Double valor_iva = (double) 0;
			Double total = (double) 0;

			System.out.println("pre_egreso_existencia:totalFilas:" + String.valueOf(tg_catalogo.getTotalFilas()));

			for (int i = 0; i < tg_catalogo.getTotalFilas(); i++) {
				tab_tabla_detalle.insertar();
				tab_tabla_detalle.modificar(tab_tabla_detalle.getFilaActual());
				//tab_tabla_detalle.setValor("ide_solicitud_detalle", tab_tabla.getValor("ide_ingeg"));
				tab_tabla_detalle.setValor("cantidad_disponible",utilitario.getFormatoNumero(tg_catalogo.getValor(i, "cantidad_saldo_boinv"), 3));
				
				
				tab_tabla_detalle.setValor("ide_bocam", tg_catalogo.getValor(i, "ide_bocam"));
			
			}

			utilitario.addUpdate("tab_tabla_detalle");

			set_catalogo.cerrar();
		} else {
			utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
		}

	}
	
	public void aceptarSolicitante() {

		String str_seleccionado = set_solicitante.getValorSeleccionado();
		if (str_seleccionado != null) {
			tab_tabla.setValor("ide_gtemp_solicitante", str_seleccionado);
			tab_tabla.modificar(tab_tabla.getFilaActual());

			System.out.println("str_seleccionado: " + str_seleccionado);
			System.out.println("tab_tabla.getFilaActual(): " + tab_tabla.getFilaActual());
		} else {
			System.out.println("str_seleccionado null: " + str_seleccionado);
		}
		set_solicitante.cerrar();
		utilitario.addUpdate("tab_tabla");

	}
	
	public void aceptarJefeSolicitante() {

		String str_seleccionado = set_jefe_solicitante.getValorSeleccionado();
		if (str_seleccionado != null) {
			tab_tabla.setValor("ide_gtemp_aprobador", str_seleccionado);
			tab_tabla.modificar(tab_tabla.getFilaActual());

			System.out.println("str_seleccionado: " + str_seleccionado);
			System.out.println("tab_tabla.getFilaActual(): " + tab_tabla.getFilaActual());
		} else {
			System.out.println("str_seleccionado null: " + str_seleccionado);
		}
		set_jefe_solicitante.cerrar();
		utilitario.addUpdate("tab_tabla");

	}
	
	public boolean verificacionEdita() {
		if (com_anio.getValue() == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return false;
		}
		if (esEditable) {
			return true;
		} else {
			utilitario.agregarMensaje("Prohibición", "La presente pantalla no permite la modificación de los registros de las solicitudes.");
			return false;
		}
	}

	@Override
	public void eliminar() {
		if(esEditable){
			con_guardar.setMessage("¿Esta Seguro de eliminar el item? ");
			con_guardar.setTitle("CONFIRMACION DE ELIMINACIÓN DE ITEM");
			con_guardar.getBot_aceptar().setMetodo("aceptarElimincacionItem");
			con_guardar.dibujar();
			utilitario.addUpdate("con_guardar");
		}else{
			utilitario.agregarMensaje("No puede eliminar el elemento, elija Editar Solicitud ", "");
			
		}

	}
	
	public void aceptarElimincacionItem(){		
		tab_tabla_detalle.isFocus();
		System.out.println("pre_egreso_existencia:eliminar");
		if (com_anio.getValue() == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		utilitario.addUpdate("tab_tabla_detalle");
		utilitario.getTablaisFocus().eliminar();
		utilitario.addUpdate("tab_tabla_detalle");
		utilitario.addUpdate("tab_tabla");
		utilitario.agregarMensaje("Elemento eliminado", "");
		guardarPantalla();
		con_guardar.cerrar();	
		
	}
	
	

	@Override
	public void insertar() {
		if (com_anio.getValue() == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		if (com_bodega.getValue() == null) {
			utilitario.agregarMensajeInfo("Debe seleccionar una Bodega", "");
			return;
		}


	    
		esEditable=true;
		if (tab_tabla.isFocus()) {
			tab_tabla.isFocus();
			tab_tabla.insertar();
			String ide_gtempxx = ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
			
			System.out.println(	"IDE  SOLICITANTE "+ ide_gtempxx );
			tab_tabla.setValor("ide_gtemp_solicitante", ide_gtempxx);
			tab_tabla.setValor("fecha_solicitud", utilitario.getFechaActual());
			tab_tabla.setValor("estado_solicitud",enProceso);
			 tab_tabla.setValor("tipo", tipoSolicitudQuimico);
			
			
		}
		
	}
	
	/*FUNCION NECESARIA PARA QUE PINTE EL REPORte
	 * Crear un Jasper duplicado de uno existente 
	 * cambiar de nombre 
	 * agregar el reporte en Opciones
	 * agregar el reporte en el perfil
	 * */
	public void abrirListaReportes() {
		rep_reporte.dibujar();
	}
	
	public void aceptarReporte() {
		
		System.out.println("ENTRA A ACEPTAR REPORTE");
		
		if (rep_reporte.getReporteSelecionado().equals("Solicitud procesada")) {
			if (rep_reporte.isVisible()) {
				p_parametros=new HashMap();	
				rep_reporte.cerrar();
				System.out.println("SOLICITUD EN PROCESO No. " + tab_tabla.getValor("ide_solicitud"));
				p_parametros.put("titulo", "SOLICITUD EN PROCESO No. " + tab_tabla.getValor("ide_solicitud"));
				p_parametros.put("id_solicitud", pckUtilidades.CConversion.CInt(tab_tabla.getValor("ide_solicitud")));
				self_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				self_reporte.dibujar();
			}
		}
	}
	
	
	public Boolean validarSaldo() {

		if (tab_tabla_detalle.getTotalFilas() <= 0) {
			utilitario.agregarMensajeError("No hay items en la solicitud", "");
			return false;
		}

		HashMap<String, Double> cantidadesPorEgresar = new HashMap<String, Double>();

		for (int i = 0; i < tab_tabla_detalle.getTotalFilas(); i++) {
			try {

				String ide_bocam = tab_tabla_detalle.getValor(i, "ide_bocam");
				double saldo_disponible_inged = Double
						.parseDouble(tab_tabla_detalle.getValor(i, "cantidad_disponible").toString());
				double cantidad = Double.parseDouble(tab_tabla_detalle.getValor(i, "cantidad_reactor_total").toString());

				if (cantidadesPorEgresar.containsKey(ide_bocam)) {
					double cantidadTotal = cantidadesPorEgresar.get(ide_bocam) + cantidad;
					if (cantidadTotal > saldo_disponible_inged) {
						return false;
					}

					cantidadesPorEgresar.put(ide_bocam, cantidadTotal);

				} else {
					cantidadesPorEgresar.put(ide_bocam, cantidad);
				}

				if (cantidad > saldo_disponible_inged) {
					return false;
				}
				if (cantidad <= 0) {
					return false;
				}
			} catch (NumberFormatException e) {
				System.out.println(e.getMessage());
				return false;
			}
		}

		return true;
	}
	
	@Override
	public void guardar() {
		
		if(esEditable){
			con_guardar_solicitud.setMessage("¿Esta Seguro de enviar la Solicitud? ");
			con_guardar_solicitud.setTitle("CONFIRMACION DE ENVÍO SOLICITUD");
			con_guardar_solicitud.getBot_aceptar().setMetodo("confirmacionGuardarSolicitud");
			con_guardar_solicitud.dibujar();
			utilitario.addUpdate("con_guardar_solicitud");
			
			
		}else{
			utilitario.agregarMensaje("No puede guardar nuevamente, elija Editar Solicitud ", "");	
		}
	} 
	
public void confirmacionGuardarSolicitud(){		
		
	if (verificacionEdita()) {
		int numFilDA = tab_tabla.getTotalFilas();
		int numFilCU = tab_tabla_detalle.getTotalFilas();
		if (numFilCU <= 0) {
			utilitario.agregarMensajeError("Detalle de Activos", "La solicitud debe tener al menos un activo");
			return;
		}
		
		if (validarSaldo()) {
			System.out.println("validarSaldo true");

				System.out.println(tab_tabla_detalle.getFilas());
				    
				 tab_tabla.setValor("ide_geani",com_anio.getValue().toString());
			     tab_tabla.setValor("ide_boubi",com_bodega.getValue().toString());
				 tab_tabla.setValor("estado_solicitud",procesada);
			     tab_tabla.setValor("tipo", tipoSolicitudQuimico);
			     try {
						if (tab_tabla.guardar()) {
							if (tab_tabla_detalle.guardar()) {
								guardarPantalla();
								ser_bodega_correo.enviarCorreoSolicitud(tab_tabla.getValor("ide_gtemp_solicitante"),tab_tabla.getValor("ide_solicitud"),1,tab_tabla.getValor("estado_solicitud"));
								ser_bodega_correo.enviarCorreoSolicitud(tab_tabla.getValor("ide_gtemp_aprobador"),tab_tabla.getValor("ide_solicitud"),2,tab_tabla.getValor("estado_solicitud"));
							}
						}
						
					    editar_solicitud.setDisabled(false);
						utilitario.addUpdate("editar_solicitud");
						
					    esEditable=false;
					    
						tab_tabla.setCondicion("ide_geani=" + com_anio.getValue() + " AND estado_solicitud="+ "'"+ procesada + "'" + " and tipo="+ "'"+ tipoSolicitudQuimico + "'" );
						tab_tabla.ejecutarSql();
						tab_tabla_detalle.ejecutarValorForanea(tab_tabla.getValorSeleccionado());
					    utilitario.addUpdate("tab_tabla");
					    utilitario.addUpdate("tab_tabla_detalle");
						
					} catch (Exception e) {
						System.out.println("ERROR"+e);
					}
		
				


		} else {
			utilitario.agregarMensaje("No dispone del stock necesario", "");
		}
		
	}
	
		con_guardar_solicitud.cerrar();	
	}


	

	public SeleccionTabla getSet_solicitante() {
		return set_solicitante;
	}


	public void setSet_solicitante(SeleccionTabla set_solicitante) {
		this.set_solicitante = set_solicitante;
	}


	public SeleccionTabla getSet_jefe_solicitante() {
		return set_jefe_solicitante;
	}


	public void setSet_jefe_solicitante(SeleccionTabla set_jefe_solicitante) {
		this.set_jefe_solicitante = set_jefe_solicitante;
	}


	public SeleccionTabla getSet_catalogo() {
		return set_catalogo;
	}


	public void setSet_catalogo(SeleccionTabla set_catalogo) {
		this.set_catalogo = set_catalogo;
	}


	
	public Reporte getRep_reporte() {
		return rep_reporte;
	}
	
	

	public Tabla getTab_tabla() {
		return tab_tabla;
	}

	public void setTab_tabla(Tabla tab_tabla) {
		this.tab_tabla = tab_tabla;
	}

	public Tabla getTab_tabla_detalle() {
		return tab_tabla_detalle;
	}

	public void setTab_tabla_detalle(Tabla tab_tabla_detalle) {
		this.tab_tabla_detalle = tab_tabla_detalle;
	}

	public SeleccionFormatoReporte getSelf_reporte() {
		return self_reporte;
	}


	public void setSelf_reporte(SeleccionFormatoReporte self_reporte) {
		this.self_reporte = self_reporte;
	}


	public void setRep_reporte(Reporte rep_reporte) {
		this.rep_reporte = rep_reporte;
	}

	public SeleccionFormatoReporte getSef_reporte() {
		return self_reporte;
	}

	public void setSef_reporte(SeleccionFormatoReporte sef_reporte) {
		this.self_reporte = sef_reporte;
	}
	
	
	public Confirmar getCon_guardar() {
		return con_guardar;
	}

	public void setCon_guardar(Confirmar con_guardar) {
		this.con_guardar = con_guardar;
	}

	public Confirmar getCon_guardar_solicitud() {
		return con_guardar_solicitud;
	}

	public void setCon_guardar_solicitud(Confirmar con_guardar_solicitud) {
		this.con_guardar_solicitud = con_guardar_solicitud;
	}

	public SeleccionTabla getSet_solicitudes_procesadas() {
		return set_solicitudes_procesadas;
	}

	public void setSet_solicitudes_procesadas(
			SeleccionTabla set_solicitudes_procesadas) {
		this.set_solicitudes_procesadas = set_solicitudes_procesadas;
	}
	
   
	

}

