package paq_adquisicion;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;

public class pre_solicitud_compra extends Pantalla {

	private Tabla tab_solicitud_compra= new Tabla();
	private Dialogo dia_presupuesto = new Dialogo();
	private	Radio lis_activo=new Radio();
	private SeleccionTabla set_tipo_compra=new SeleccionTabla();
	private SeleccionTabla set_adjudicador=new SeleccionTabla();
	private SeleccionTabla set_proveedor=new SeleccionTabla();
	private SeleccionTabla set_cotizado=new SeleccionTabla();

	public static String par_modulo_adquisicion;
	public static String par_estado_modulo_compra;
	public static String par_adquisicion;

	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina ) utilitario.instanciarEJB(ServicioNomina.class);
	@EJB
	private ServicioBodega ser_bodega = (ServicioBodega ) utilitario.instanciarEJB(ServicioBodega.class);

	public pre_solicitud_compra() {
		par_modulo_adquisicion =utilitario.getVariable("p_modulo_adquisicion");
		par_estado_modulo_compra =utilitario.getVariable("p_estado_modulo_compra");
		par_adquisicion=utilitario.getVariable("p_cotizacion_adquisicion");

		// TODO Auto-generated constructor stub
		tab_solicitud_compra.setId("tab_solicitud_compra");
		tab_solicitud_compra.setTabla("adq_solicitud_compra", "ide_adsoc", 1);
		tab_solicitud_compra.getColumna("ide_coest").setCombo("cont_estado","ide_coest", "detalle_coest","");
		tab_solicitud_compra.getColumna("ide_coest").setLectura(true);
		tab_solicitud_compra.getColumna("ide_coest").setAutoCompletar();
		tab_solicitud_compra.getColumna("ide_copag").setCombo("cont_parametros_general","ide_copag", "detalle_copag", "");
		tab_solicitud_compra.getColumna("ide_copag").setAutoCompletar();
		tab_solicitud_compra.getColumna("ide_copag").setLectura(true);
		tab_solicitud_compra.getColumna("ide_geedp").setCombo(ser_nomina.servicioEmpleadoContrato("true,false"));
		tab_solicitud_compra.getColumna("ide_geedp").setAutoCompletar();
		tab_solicitud_compra.getColumna("ide_geedp").setLectura(true);
		tab_solicitud_compra.getColumna("ide_tepro").setCombo(ser_bodega.getProveedor("true,false"));
		tab_solicitud_compra.getColumna("ide_tepro").setAutoCompletar();
		tab_solicitud_compra.getColumna("ide_tepro").setCombo(ser_bodega.getProveedor("true,false"));
		tab_solicitud_compra.getColumna("ide_tepro").setAutoCompletar();
		tab_solicitud_compra.getColumna("ide_tepro").setLectura(true);
		tab_solicitud_compra.getColumna("fecha_proforma2_adsoc").setVisible(false);
		tab_solicitud_compra.getColumna("valor_proforma2_adsoc").setVisible(false);
		tab_solicitud_compra.getColumna("factura_proforma2_adsoc").setVisible(false);
		tab_solicitud_compra.getColumna("oferente2_adsoc").setVisible(false);
		tab_solicitud_compra.getColumna("fecha_proforma1_adsoc").setVisible(false);
		tab_solicitud_compra.getColumna("factura_proforma1_adsoc").setVisible(false);
		tab_solicitud_compra.getColumna("valor_proforma1_adsoc").setVisible(false);
		tab_solicitud_compra.getColumna("proforma_proveedor_adsoc").setVisible(false);
		tab_solicitud_compra.getColumna("fecha_proforma_proveedor_adsoc").setVisible(false);
		tab_solicitud_compra.getColumna("oferente1_adsoc").setVisible(false);
		tab_solicitud_compra.setTipoFormulario(true);
		tab_solicitud_compra.getGrid().setColumns(4);
		tab_solicitud_compra.dibujar();

		PanelTabla pat_compra= new PanelTabla();
		pat_compra.setPanelTabla(tab_solicitud_compra);
		agregarComponente(pat_compra);
		// Inicializo componente dialogo para seleccionar si proviene de una certificacion presupuestaria

		List lista = new ArrayList();
		Object fila1[] = {
				"0", "Seleccionar Certificación Presupuestaria"
		};
		Object fila2[] = {
				"1", "Continuar Registro de Compra Normalmente"
		};
		lista.add(fila1);
		lista.add(fila2);

		lis_activo.setRadio(lista);
		lis_activo.setVertical();
		dia_presupuesto.setId("dia_presupuesto");
		dia_presupuesto.setTitle("SELECCIONE UNA OPCION");
		dia_presupuesto.getBot_aceptar().setMetodo("aceptaPresupuesto");
		dia_presupuesto.setDialogo(lis_activo);
		dia_presupuesto.setHeight("40%");
		dia_presupuesto.setWidth("40%");
		dia_presupuesto.setDynamic(false);
		agregarComponente(dia_presupuesto);
		//Inicializamos sel tabla para seleccionar el tipo de compra que va a realizar

		set_tipo_compra.setId("set_tipo_compra");
		set_tipo_compra.setTitle("SELECCIONE EL TIPO DE COMPRA QUE DESEA REALIZAR");
		set_tipo_compra.setSeleccionTabla(ser_contabilidad.getModuloParametros("true",par_modulo_adquisicion),"ide_copag");
		set_tipo_compra.getBot_aceptar().setMetodo("aceptarCompra");
		set_tipo_compra.setRadio();
		agregarComponente(set_tipo_compra);

		//Inicializamos sel tabla para seleccionar el adjudicador

		set_adjudicador.setId("set_adjudicador");
		set_adjudicador.setTitle("SELECCIONE EL ADJUDICADOR");
		set_adjudicador.setSeleccionTabla(ser_nomina.servicioEmpleadoContrato("true"),"ide_geedp");
		set_adjudicador.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
		set_adjudicador.getTab_seleccion().getColumna("NOMBRES_APELLIDOS").setFiltro(true);
		set_adjudicador.getBot_aceptar().setMetodo("aceptarCompra");
		set_adjudicador.setRadio();
		agregarComponente(set_adjudicador);

		//Inicializamos sel tabla para seleccionar el proveeedor

		set_proveedor.setId("set_proveedor");
		set_proveedor.setTitle("SELECCIONE EL PROVEEDOR");
		set_proveedor.setSeleccionTabla(ser_bodega.getProveedor("true"),"ide_tepro");
		set_proveedor.getTab_seleccion().getColumna("nombre_tepro").setFiltro(true);
		set_proveedor.getTab_seleccion().getColumna("ruc_tepro").setFiltro(true);
		set_proveedor.getBot_aceptar().setMetodo("aceptarCompra");
		set_proveedor.setRadio();
		agregarComponente(set_proveedor);

		Boton bot_cotizacion = new Boton();
		bot_cotizacion.setValue("Agregar Cotizacion");
		bot_cotizacion.setTitle("COTIZACION");
		bot_cotizacion.setIcon("ui-icon-person");
		bot_cotizacion.setMetodo("aceptarCotizacion");
		bar_botones.agregarBoton(bot_cotizacion);
		
		set_cotizado.setId("set_cotizado");
		set_cotizado.setSeleccionTabla(ser_contabilidad.getModuloEstados("true", par_adquisicion),"ide_coest");  
				set_cotizado.getBot_aceptar().setMetodo("aceptarCotizacion");
		set_cotizado.setRadio();
		agregarComponente(set_cotizado);

		Boton bot_certificacion = new Boton();
		bot_certificacion.setValue("Agregar Certificación Presupuestaria");
		bot_certificacion.setTitle("CERTIFICACION PRESUPUESTARIA");
		bot_certificacion.setIcon("ui-icon-person");
		bot_certificacion.setMetodo("importarCertificacion");
		bar_botones.agregarBoton(bot_certificacion);

		Boton bot_adjudicado = new Boton();
		bot_adjudicado.setValue("Agregar Adjudicado");
		bot_adjudicado.setTitle("ADJUDICADO");
		bot_adjudicado.setIcon("ui-icon-person");
		bot_adjudicado.setMetodo("importarAdjudicado");
		bar_botones.agregarBoton(bot_adjudicado);

		Boton bot_solicitud = new Boton();
		bot_solicitud.setValue("Agregar Solicitud");
		bot_solicitud.setTitle("SOLICITUD PAGO");
		bot_solicitud.setIcon("ui-icon-person");
		bot_solicitud.setMetodo("importarSolicitud");
		bar_botones.agregarBoton(bot_solicitud);




	}

	public void aceptarCotizacion(){
		tab_solicitud_compra.setValor("ide_coest",par_adquisicion);
		set_cotizado.getTab_seleccion().setSql(ser_contabilidad.getModuloEstados("true", par_adquisicion));
		set_cotizado.getTab_seleccion().ejecutarSql();
		set_cotizado.dibujar();
		utilitario.addUpdate("tab_solicitud_compra");
	}
	
	public void aceptaPresupuesto(){

		if(lis_activo.getValue() == null){
			utilitario.agregarMensajeInfo("Certificación / Continuar", "Debe Seleccionar una Opción");
			return;
		}
		else {
			if (lis_activo.getValue().equals("0")){
				// fata definor aun los campos faltantes programar certificacio presupuestari un sel tabla
				utilitario.agregarMensajeInfo("Certificación Presupuestaria", "No existe Certificaciones Presupuestarias.");
			}
			if (lis_activo.getValue().equals("1")){
				aceptarCompra();
			}
		}
	}

	public void aceptarCompra(){
		if(dia_presupuesto.isVisible()){
			tab_solicitud_compra.insertar();

			set_tipo_compra.getTab_seleccion().setSql(ser_contabilidad.getModuloParametros("true", par_modulo_adquisicion));
			set_tipo_compra.getTab_seleccion().ejecutarSql();
			dia_presupuesto.cerrar();
			set_tipo_compra.dibujar();
		}
		else if(set_tipo_compra.isVisible()){
			if (set_tipo_compra.getValorSeleccionado()!=null){

				tab_solicitud_compra.setValor("ide_copag",set_tipo_compra.getValorSeleccionado());

				set_adjudicador.getTab_seleccion().setSql(ser_nomina.servicioEmpleadoContrato("true"));
				set_adjudicador.getTab_seleccion().ejecutarSql();
				set_tipo_compra.cerrar();
				set_adjudicador.dibujar();
			}
			else {
				utilitario.agregarMensajeInfo("SELECCIONE OPCION", "Seleccione un registro");
			}
		}   
		else  if(set_adjudicador.isVisible()){
			if (set_adjudicador.getValorSeleccionado()!=null){
				tab_solicitud_compra.setValor("ide_geedp",set_adjudicador.getValorSeleccionado());
				set_proveedor.getTab_seleccion().setSql(ser_bodega.getProveedor("true"));
				set_proveedor.getTab_seleccion().ejecutarSql();
				set_adjudicador.cerrar();
				set_proveedor.dibujar();
			}
			else {
				utilitario.agregarMensajeInfo("SELECCIONE OPCION", "Seleccione un registro");
			}
		}
		else if(set_proveedor.isVisible()){
			if (set_proveedor.getValorSeleccionado()!=null){
				set_proveedor.cerrar();
				tab_solicitud_compra.setValor("ide_tepro",set_proveedor.getValorSeleccionado());
				utilitario.addUpdate("tab_solicitud_compra");

			}
			else {
				utilitario.agregarMensajeInfo("SELECCIONE OPCION", "Seleccione un registro");
			}
		}

	}
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		dia_presupuesto.dibujar();
		tab_solicitud_compra.insertar();

		tab_solicitud_compra.setValor("ide_coest",par_estado_modulo_compra);
		set_tipo_compra.getTab_seleccion().setSql(ser_contabilidad.getModuloParametros("true", par_estado_modulo_compra));
		set_tipo_compra.getTab_seleccion().ejecutarSql();
		set_tipo_compra.dibujar();
		utilitario.addUpdate("tab_solicitud_compra");

	}

	@Override
	public void guardar() {
		if(tab_solicitud_compra.guardar()){
			guardarPantalla();
		}

	}

	@Override
	public void eliminar() {
		utilitario.getTablaisFocus().eliminar();

	}

	public Tabla getTab_solicitud_compra() {
		return tab_solicitud_compra;
	}

	public void setTab_solicitud_compra(Tabla tab_solicitud_compra) {
		this.tab_solicitud_compra = tab_solicitud_compra;
	}
	public Dialogo getDia_presupuesto() {
		return dia_presupuesto;
	}

	public void setDia_presupuesto(Dialogo dia_presupuesto) {
		this.dia_presupuesto = dia_presupuesto;
	}

	public SeleccionTabla getSet_tipo_compra() {
		return set_tipo_compra;
	}

	public void setSet_tipo_compra(SeleccionTabla set_tipo_compra) {
		this.set_tipo_compra = set_tipo_compra;
	}

	public SeleccionTabla getSet_adjudicador() {
		return set_adjudicador;
	}

	public void setSet_adjudicador(SeleccionTabla set_adjudicador) {
		this.set_adjudicador = set_adjudicador;
	}

	public SeleccionTabla getSet_proveedor() {
		return set_proveedor;
	}

	public void setSet_proveedor(SeleccionTabla set_proveedor) {
		this.set_proveedor = set_proveedor;
	}

	public SeleccionTabla getSet_cotizado() {
		return set_cotizado;
	}

	public void setSet_cotizado(SeleccionTabla set_cotizado) {
		this.set_cotizado = set_cotizado;
	}




}
