package paq_bodega;

import javax.ejb.EJB;

import paq_adquisicion.ejb.ServicioAdquisicion;
import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_gestion.ejb.ServicioGestion;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;

public class pre_egreso_material_solicitud extends Pantalla {
	private Tabla  tab_concepto_egreso=new Tabla();
	private Tabla  tab_egreso=new Tabla();
	private Combo com_anio=new Combo();
	private Etiqueta eti_total= new Etiqueta();

	private SeleccionTabla set_inventario=new SeleccionTabla();
	private SeleccionTabla set_solicitud=new SeleccionTabla();


	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	@EJB
	private ServicioGestion ser_gestion = (ServicioGestion) utilitario.instanciarEJB(ServicioGestion.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioBodega ser_Bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);
	@EJB
	private ServicioAdquisicion ser_Adquisicion=(ServicioAdquisicion) utilitario.instanciarEJB(ServicioAdquisicion.class);


	public pre_egreso_material_solicitud(){

		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));		
		com_anio.setMetodo("seleccionaElAnio");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);

		tab_concepto_egreso.setId("tab_concepto_egreso");
		tab_concepto_egreso.setHeader("EGRESO DE MATERIALES");
		tab_concepto_egreso.setTabla("bodt_concepto_egreso","ide_bocoe", 1);
		tab_concepto_egreso.getColumna("ide_geani").setVisible(false);
		tab_concepto_egreso.setCondicion("ide_geani = -1 and not ide_adsoc is null ");
		tab_concepto_egreso.getColumna("IDE_GETIP").setCombo("gen_tipo_persona","ide_getip","detalle_getip","");
		tab_concepto_egreso.getColumna("IDE_GEARE").setCombo("gen_area","ide_geare", "detalle_geare", "");
		tab_concepto_egreso.getColumna("IDE_BODES").setCombo("bodt_destino","ide_bodes","detalle_bodes", "");
		tab_concepto_egreso.getColumna("IDE_GEEDP").setCombo(ser_nomina.servicioEmpleadoContrato("true"));
		tab_concepto_egreso.getColumna("gen_ide_geedp").setCombo(ser_nomina.servicioEmpleadoContrato("true"));
		tab_concepto_egreso.getColumna("gen_ide_geedp2").setCombo(ser_nomina.servicioEmpleadoContrato("true"));
		tab_concepto_egreso.getColumna("fecha_egreso_bocoe").setValorDefecto(utilitario.getFechaActual());
		tab_concepto_egreso.getColumna("ide_adsoc").setCombo(ser_Adquisicion.getComprasCombo("true,false"));
		tab_concepto_egreso.getColumna("ide_adsoc").setAutoCompletar();
		tab_concepto_egreso.getColumna("ide_adsoc").setLectura(true);
		tab_concepto_egreso.getColumna("activo_bocoe").setValorDefecto("true");
		tab_concepto_egreso.getColumna("activo_bocoe").setLectura(true);

		tab_concepto_egreso.agregarRelacion(tab_egreso);
		tab_concepto_egreso.setTipoFormulario(true);
		tab_concepto_egreso.getGrid().setColumns(4);	
		tab_concepto_egreso.dibujar();
		PanelTabla pat_concepto_egreso=new PanelTabla();
		pat_concepto_egreso.setPanelTabla(tab_concepto_egreso);


		tab_egreso.setId("tab_egreso");
		tab_egreso.setHeader("DETALLE EGRESO DE MATERIALES");
		tab_egreso.setTabla("bodt_egreso", "ide_boegr", 2);	
		tab_egreso.getColumna("ide_bobod").setCombo(ser_Bodega.getMaterialBodegaCompras());
		tab_egreso.getColumna("ide_boinv").setVisible(false);
		tab_egreso.getColumna("costo_egreso_boegr").setVisible(false);
		tab_egreso.getColumna("total_egreso_boegr").setVisible(false);
		tab_egreso.getColumna("existencias_boegr").setVisible(false);
		tab_egreso.getColumna("costo_anterior_boegr").setVisible(false);
		tab_egreso.getColumna("fecha_ingreso_articulo_boegr").setVisible(false);
		tab_egreso.getColumna("fecha_egreso_boegr").setLectura(true);
		tab_egreso.getColumna("cantidad_egreso_boegr").setLectura(true);
		tab_egreso.getColumna("documento_egreso_boegr").setLectura(true);
		tab_egreso.getColumna("activo_boegr").setValorDefecto("true");

		tab_egreso.getColumna("activo_boegr").setLectura(true);
		tab_egreso.getColumna("ide_bobod").setLectura(true);
		tab_egreso.dibujar();
		PanelTabla pat_egreso=new PanelTabla();
		pat_egreso.setPanelTabla(tab_egreso);
		pat_egreso.getMenuTabla().getItem_insertar().setDisabled(true);
		


		Division div_division=new Division();
		div_division.dividir2(pat_concepto_egreso,pat_egreso, "50%", "h");
		agregarComponente(div_division);

		Boton bot_material = new Boton();
		bot_material.setValue("Buscar Solicitud Compra Bodega");
		bot_material.setTitle("Solicitud Compra");
		bot_material.setIcon("ui-icon-person");
		bot_material.setMetodo("importarSolicitud");
		bar_botones.agregarBoton(bot_material);
		
		set_solicitud.setId("set_solicitud");
		set_solicitud.setSeleccionTabla(ser_Bodega.getEgresoSolicitud(),"ide_adsoc");
		set_solicitud.getBot_aceptar().setMetodo("aceptarSolicitud");
		set_solicitud.getTab_seleccion().getColumna("numero_factura").setFiltro(true);
		set_solicitud.getTab_seleccion().getColumna("ingreso_bodega").setFiltro(true);
		set_solicitud.getTab_seleccion().getColumna("ingreso_bodega").setFiltro(true);
		set_solicitud.getTab_seleccion().getColumna("detalle_compra").setFiltro(true);
		set_solicitud.getTab_seleccion().getColumna("numero_solicitud_compra").setFiltro(true);		
		set_solicitud.getTab_seleccion().ejecutarSql();
		set_solicitud.setRadio();
		agregarComponente(set_solicitud);


	}

	public void seleccionaElAnio (){
		if(com_anio.getValue()!=null){
			tab_concepto_egreso.setCondicion("ide_geani="+com_anio.getValue()+" and not ide_adsoc is null ");
			tab_concepto_egreso.ejecutarSql();
			tab_egreso.ejecutarValorForanea(tab_concepto_egreso.getValorSeleccionado());


		}
		else{
			tab_egreso.ejecutarSql();
			tab_egreso.ejecutarValorForanea(tab_concepto_egreso.getValorSeleccionado());
			utilitario.agregarMensajeInfo("Selecione un Año", "");

		}
	}

	public void importarSolicitud(){
        if(com_anio.getValue()==null){
           	utilitario.agregarMensajeInfo("No Existe Año", "Debe seleccionar un año");
            return;
        	
        }
		if(tab_concepto_egreso.getValor("ide_bocoe")==null||tab_concepto_egreso.getValor("ide_bocoe").isEmpty()){
        	utilitario.agregarMensajeInfo("No existen registros", "Debe existir un concepto de egreso, para proceder a registar los detalles del egreso");
            return;
        }
		set_solicitud.getTab_seleccion().setSql(ser_Bodega.getEgresoSolicitud());
		set_solicitud.getTab_seleccion().ejecutarSql();
		set_solicitud.dibujar();

	}

	public  void aceptarSolicitud(){
		
		String str_seleccionados=set_solicitud.getValorSeleccionado();
		if(str_seleccionados!=null){
			//Inserto los empleados seleccionados en la tabla de participantes 
			TablaGenerica tab_solititud_selecionada = ser_Bodega.getEgresoSolicitudBodega(str_seleccionados);		
			
			//set_empleado.setSeleccionTabla(ser_nomina.servicioEmpleadoContrato("true"),"ide_geedp");
			
			System.out.println(" tabla generica"+tab_solititud_selecionada.getSql());
			tab_concepto_egreso.setValor("ide_adsoc", tab_solititud_selecionada.getValor("ide_adsoc"));
			for(int i=0;i<tab_solititud_selecionada.getTotalFilas();i++){
				System.out.println(" entwr e afor 0"+i);
				tab_egreso.insertar();
				tab_egreso.setValor("fecha_egreso_boegr", tab_concepto_egreso.getValor("fecha_egreso_bocoe"));
				tab_egreso.setValor("ide_bobod", tab_solititud_selecionada.getValor(i, "ide_bobod"));			
				tab_egreso.setValor("cantidad_egreso_boegr", tab_solititud_selecionada.getValor(i, "cantidad_ingreso_bobod"));			
				tab_egreso.setValor("documento_egreso_boegr", tab_concepto_egreso.getValor("numero_egreso_bocoe"));			
				tab_egreso.setValor("activo_boegr", "true");			
				
			}
			set_solicitud.cerrar();
			utilitario.addUpdateTabla(tab_egreso, "fecha_egreso_boegr,ide_bobod,cantidad_egreso_boegr,documento_egreso_boegr", "tab_concepto_egreso");			
			tab_concepto_egreso.modificar(tab_concepto_egreso.getFilaActual());
			utilitario.addUpdate("tab_concepto_egreso");	
			//utilitario.addUpdateTabla(tab_concepto_egreso, "ide_adsoc", "tab_concepto_egreso");			

			tab_egreso.guardar();
		    tab_concepto_egreso.guardar();
		    guardarPantalla();
		}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar almenos un registro", "");
		}
	}
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if(com_anio.getValue()==null){
			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar un año");
			return;

		}
		if (tab_concepto_egreso.isFocus()) {
			tab_concepto_egreso.insertar();
			tab_concepto_egreso.setValor("ide_geani", com_anio.getValue()+"");

		}

		else if (tab_egreso.isFocus()) {
			utilitario.agregarMensaje("No se Puede Insertar", "Seleccione una Solicitud de Compra");
		}	
	}



	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if (tab_concepto_egreso.guardar()){
			tab_egreso.guardar();
		}
		guardarPantalla();

	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		if(tab_concepto_egreso.isFocus()){
			tab_concepto_egreso.eliminar();
		}
		else if(tab_egreso.isFocus()){
			tab_egreso.eliminar();
		}	

	}
	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}

	public SeleccionTabla getSet_inventario() {
		return set_inventario;
	}

	public void setSet_inventario(SeleccionTabla set_inventario) {
		this.set_inventario = set_inventario;
	}

	public Tabla getTab_concepto_egreso() {
		return tab_concepto_egreso;
	}

	public void setTab_concepto_egreso(Tabla tab_concepto_egreso) {
		this.tab_concepto_egreso = tab_concepto_egreso;
	}

	public Tabla getTab_egreso() {
		return tab_egreso;
	}

	public void setTab_egreso(Tabla tab_egreso) {
		this.tab_egreso = tab_egreso;
	}

	public SeleccionTabla getSet_solicitud() {
		return set_solicitud;
	}

	public void setSet_solicitud(SeleccionTabla set_solicitud) {
		this.set_solicitud = set_solicitud;
	}


}
