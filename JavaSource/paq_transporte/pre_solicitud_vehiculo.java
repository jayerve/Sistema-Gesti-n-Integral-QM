package paq_transporte;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.DateSelectEvent;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;
import paq_transporte.ejb.ServicioTransporte;
import pckEntidades.EnvioMail;

/**
*
* @author Alex Becerra
*/ 
public class pre_solicitud_vehiculo extends Pantalla {

	private Tabla tab_solicitud = new Tabla();
	private Tabla tab_solicitud_ruta = new Tabla();
	private Tabla tab_solicitud_ocupante = new Tabla();
	private Tabla tab_solicitud_archivo = new Tabla();
	private Combo com_anio=new Combo();
	private Combo com_sucursales=new Combo();
	private SeleccionTabla set_pantalla_sucursal = new SeleccionTabla();
	private SeleccionTabla set_pantalla_conductor = new SeleccionTabla();
	List lista = new ArrayList();

	//private String p_responsable_transporte = utilitario.getVariable("p_responsable_transporte");
	
	@EJB
	private ServicioTransporte ser_transporte = (ServicioTransporte) utilitario.instanciarEJB(ServicioTransporte.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);

	Date fecha_inicial = new Date();
	Date fecha_final = new Date();
	
	private String ide_vetes;

	public pre_solicitud_vehiculo() {
		
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("AÑO:"));
		bar_botones.agregarComponente(com_anio);
		
		com_sucursales.setCombo("SELECT IDE_SUCU,NOM_SUCU FROM SIS_SUCURSAL where IDE_SUCU in (1,10) order by NOM_SUCU");
    	com_sucursales.setMetodo("cambioSucursal");    
    	com_sucursales.setValue("1");
    	com_sucursales.setStyle("width: 100px; margin: 0 0 -8px 0;");
    	bar_botones.agregarComponente(new Etiqueta("Sucursales :"));
    	bar_botones.agregarComponente(com_sucursales);

		ide_vetes="";
		fecha_inicial.getDate();
		fecha_final.getDate();

		Tabulador tab_tabulador = new Tabulador();
		tab_tabulador.setId("tab_tabulador");

		tab_solicitud.setId("tab_solicitud");
		tab_solicitud.setTipoFormulario(true);
		tab_solicitud.getGrid().setColumns(4);
		tab_solicitud.setTabla("veh_solicitud", "ide_vesol", 1);
		tab_solicitud.getColumna("activo_vesol").setValorDefecto("true");
		tab_solicitud.getColumna("activo_vesol").setNombreVisual("ACTIVO");
		tab_solicitud.getColumna("activo_vesol").setOrden(2);

		tab_solicitud.getColumna("departamento_vesol");
		tab_solicitud.getColumna("departamento_vesol").setNombreVisual("ÁREA O DEPARTAMENTO");
		tab_solicitud.getColumna("departamento_vesol").setOrden(3);
		tab_solicitud.getColumna("fecha_solicitud_vesol");
		tab_solicitud.getColumna("fecha_solicitud_vesol").setNombreVisual("Fecha de Ingreso");
		tab_solicitud.getColumna("fecha_solicitud_vesol").setValorDefecto(utilitario.getFechaActual());
		tab_solicitud.getColumna("fecha_solicitud_vesol").setLectura(false);
		tab_solicitud.getColumna("fecha_solicitud_vesol").setOrden(4);
		tab_solicitud.getColumna("detalle_vesol").setVisible(false);

		tab_solicitud.getColumna("motivo_vesol");
		tab_solicitud.getColumna("motivo_vesol").setOrden(5);
		tab_solicitud.getColumna("motivo_vesol").setNombreVisual("Motivo de la salida");
		tab_solicitud.getColumna("fecha_salida_vesol");
		tab_solicitud.getColumna("fecha_salida_vesol").setOrden(6);
		tab_solicitud.getColumna("fecha_salida_vesol").setNombreVisual("Fecha de Salida");
		tab_solicitud.getColumna("fecha_salida_vesol").setValorDefecto(utilitario.getFechaActual());
		tab_solicitud.getColumna("fecha_regreso_vesol");
		tab_solicitud.getColumna("fecha_regreso_vesol").setOrden(7);
		tab_solicitud.getColumna("fecha_regreso_vesol").setNombreVisual("Fecha de Retorno");
		tab_solicitud.getColumna("fecha_regreso_vesol").setValorDefecto(utilitario.getFechaActual());
		tab_solicitud.getColumna("fecha_regreso_vesol").setMetodoChange("calculaDiasComision");
		// tab_solicitud.getColumna("fecha_regreso_vesol").setMetodoChange("disponibilidadVehiculo");

		tab_solicitud.getColumna("hora_salida_vesol");
		tab_solicitud.getColumna("hora_salida_vesol").setOrden(9);
		tab_solicitud.getColumna("hora_salida_vesol").setNombreVisual("Hora de salida");

		tab_solicitud.getColumna("hora_retorno_vesol");
		tab_solicitud.getColumna("hora_retorno_vesol").setOrden(10);
		tab_solicitud.getColumna("hora_retorno_vesol").setNombreVisual("Hora de retorno en la Oficina");
		tab_solicitud.getColumna("hora_retorno_vesol").setMetodoChange("calculaHorasComision");

		tab_solicitud.getColumna("dias_solicitado_vesol");
		tab_solicitud.getColumna("dias_solicitado_vesol").setOrden(8);
		tab_solicitud.getColumna("dias_solicitado_vesol").setNombreVisual("Número de días (dur.)");
		tab_solicitud.getColumna("dias_solicitado_vesol").setVisible(true);
		// tab_solicitud.getColumna("dias_solicitado_vesol").setMetodoChange(
		// "disponibilidadVehiculo");

		tab_solicitud.getColumna("tiempo_solicitado_vesol");
		tab_solicitud.getColumna("tiempo_solicitado_vesol").setOrden(11);
		tab_solicitud.getColumna("tiempo_solicitado_vesol").setNombreVisual("Número de horas (dur.)");
		tab_solicitud.getColumna("tiempo_solicitado_vesol").setVisible(true);

		tab_solicitud.getColumna("ext_contacto_vesol");
		tab_solicitud.getColumna("ext_contacto_vesol").setOrden(12);
		tab_solicitud.getColumna("ext_contacto_vesol").setNombreVisual("Num. de ext. de contacto");
		tab_solicitud.getColumna("prioridad").setVisible(false);
		tab_solicitud.getColumna("ide_vecon").setCombo(ser_transporte.getSqlConductor("true,false"));
		//tab_solicitud.getColumna("ide_vecon").setVisible(false);
		tab_solicitud.getColumna("ide_vecon").setAutoCompletar();
		tab_solicitud.getColumna("ide_vecon").setNombreVisual("Conductor");
		tab_solicitud.getColumna("ide_vetes").setCombo("veh_solicitud_estado", "ide_vetes", "detalle_vetes", "");
		tab_solicitud.getColumna("ide_vetes").setOrden(13);
		tab_solicitud.getColumna("ide_vetes").setValorDefecto("1");
		tab_solicitud.getColumna("ide_vetes").setNombreVisual("Estado de la Solicitud");

		tab_solicitud.getColumna("ocupantes_vesol");
		tab_solicitud.getColumna("ocupantes_vesol").setOrden(14);
		tab_solicitud.getColumna("ocupantes_vesol").setNombreVisual("Ocupantes de la Comisión");

		tab_solicitud.getColumna("ide_gtemp");
		tab_solicitud.getColumna("ide_gtemp").setOrden(15);
		
		tab_solicitud.getColumna("ide_gtemp").setNombreVisual("Empleado Solicitado");
		tab_solicitud.getColumna("ide_gtemp").setCombo(ser_transporte.getSqlEmpleado());
		tab_solicitud.getColumna("ide_gtemp").setAutoCompletar();
		tab_solicitud.getColumna("gen_ide_gtemp");
		tab_solicitud.getColumna("gen_ide_gtemp").setOrden(16);
		tab_solicitud.getColumna("gen_ide_gtemp").setNombreVisual("A nombre de quien es la comisión");
		tab_solicitud.getColumna("gen_ide_gtemp").setCombo(ser_transporte.getSqlEmpleado());
		tab_solicitud.getColumna("gen_ide_gtemp").setAutoCompletar();

		Object fila1[] = { "1", "DISPONIBILIDAD" };
		Object fila2[] = { "0", "CAPACIDAD" };

		lista.add(fila1);
		lista.add(fila2);
		tab_solicitud.getColumna("tipo_calculo_vesol").setRadio(lista, "1");
		tab_solicitud.getColumna("tipo_calculo_vesol").setOrden(17);
		tab_solicitud.getColumna("tipo_calculo_vesol").setRadioVertical(true);
		tab_solicitud.getColumna("tipo_calculo_vesol").setNombreVisual("Verifica Vehículo");

		tab_solicitud.getColumna("ide_veveh").setCombo("veh_vehiculo", "ide_veveh", "PLACA_veveh", "");
		tab_solicitud.getColumna("ide_veveh").setOrden(18);
		tab_solicitud.getColumna("ide_veveh").setNombreVisual("Vehículo Disponible");
		tab_solicitud.getColumna("ide_veveh").setLectura(true);
		// tab_solicitud.getColumna("ide_veveh").setLongitud(20);
		// tab_solicitud.getColumna("ide_veveh").setCombo(
		// getSqlVehiculosDisponibles(
		// tab_solicitud.getValor("fecha_salida_vesol"),
		// tab_solicitud.getValor("fecha_regreso_vesol")));
		// tab_solicitud.getColumna("ide_veveh").setMetodoChange("disponibilidadVehiculo");
		// tab_solicitud.getColumna("ide_veveh").setAutoCompletar();

		// realizar el reporte de los vehiculos disponibles y vehiculos con la
		// capacidad
		// tab_solicitud.getColumna("gen_ide_gtemp").setCombo(ser_transporte.getSqlEmpleado());
		// tab_solicitud.getColumna("gen_ide_gtemp").setAutoCompletar();

		tab_solicitud.getColumna("observaciones_vesol");
		tab_solicitud.getColumna("observaciones_vesol").setOrden(19);
		tab_solicitud.getColumna("observaciones_vesol").setNombreVisual("Observaciones");
		tab_solicitud.getColumna("gen_ide_geedp");
		tab_solicitud.getColumna("gen_ide_geedp").setOrden(20);
		tab_solicitud.getColumna("gen_ide_geedp").setNombreVisual("Jefe de Área");
		tab_solicitud.getColumna("gen_ide_geedp").setVisible(false);
		
		tab_solicitud.getColumna("ide_vetip").setCombo("veh_tipo_vehiculo", "ide_vetip", "detalle_vetip", "");
		tab_solicitud.getColumna("ide_vetip").setAutoCompletar();
		tab_solicitud.getColumna("ide_vetip").setNombreVisual("TIPO VEHICULO");
		tab_solicitud.getColumna("ide_vetip").setLectura(true);
		tab_solicitud.getColumna("ide_vetip").setOrden(22);			
		
		tab_solicitud.getColumna("ide_geedp").setCombo(ser_nomina.servicioEmpleadoContrato("true,false"));
		tab_solicitud.getColumna("ide_geedp").setAutoCompletar();
		
		tab_solicitud.getColumna("aprobado_vesol").setValorDefecto("false");
		tab_solicitud.getColumna("aprobado_vesol").setLectura(true);
		
		tab_solicitud.setCondicion("ide_vesol=-1");
		tab_solicitud.agregarRelacion(tab_solicitud_ruta);
		tab_solicitud.agregarRelacion(tab_solicitud_ocupante);
		tab_solicitud.agregarRelacion(tab_solicitud_archivo);
		tab_solicitud.dibujar();

		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_solicitud);

		tab_solicitud_ruta.setId("tab_solicitud_ruta");
		tab_solicitud_ruta.setIdCompleto("tab_tabulador:tab_solicitud_ruta");
		tab_solicitud_ruta.setTabla("veh_solicitud_ruta", "ide_vesru", 2);
		tab_solicitud_ruta.getColumna("ide_vesru").setNombreVisual("CÓDIGO");
		tab_solicitud_ruta.getColumna("ide_vesru").setOrden(1);				

		tab_solicitud_ruta.getColumna("ide_salida_verut").setCombo("veh_ruta", "ide_verut", "detalle_verut", "");
		tab_solicitud_ruta.getColumna("ide_salida_verut").setNombreVisual("PUNTO DE LA RUTA ORIGEN");
		tab_solicitud_ruta.getColumna("ide_salida_verut").setOrden(2);				

		tab_solicitud_ruta.getColumna("detalle_salida_vesru").setNombreVisual("OBSERVACIÓN ORIGEN");
		tab_solicitud_ruta.getColumna("detalle_salida_vesru").setOrden(3);	
		
		
		tab_solicitud_ruta.getColumna("ide_verut").setCombo("veh_ruta", "ide_verut", "detalle_verut", "");
		tab_solicitud_ruta.getColumna("ide_verut").setNombreVisual("PUNTO DE LA RUTA DESTINO");
		tab_solicitud_ruta.getColumna("ide_verut").setOrden(4);				
		tab_solicitud_ruta.getColumna("detalle_vesru").setNombreVisual("OBSERVACIÓN DESTINO");
		tab_solicitud_ruta.getColumna("detalle_vesru").setOrden(5);				
		
		tab_solicitud_ruta.getColumna("activo_vesru").setValorDefecto("true");
		tab_solicitud_ruta.getColumna("activo_vesru").setVisible(false);
		tab_solicitud_ruta.getColumna("activo_vesru").setOrden(6);				
		tab_solicitud_ruta.setLectura(true);
		
		tab_solicitud_ruta.dibujar();

		PanelTabla pat_panel2 = new PanelTabla();
		pat_panel2.setPanelTabla(tab_solicitud_ruta);
		
		
		tab_solicitud_ocupante.setId("tab_solicitud_ocupante");
		tab_solicitud_ocupante.setIdCompleto("tab_tabulador:tab_solicitud_ocupante");
		tab_solicitud_ocupante.setTabla("veh_solicitud_ocupante", "ide_vesoc", 3);
		tab_solicitud_ocupante.getColumna("IDE_GTEMP").setCombo(ser_nomina.servicioEmpleadosActivos("true,false"));
		tab_solicitud_ocupante.getColumna("activo_vesoc").setValorDefecto("true");
		tab_solicitud_ocupante.setLectura(true);
		tab_solicitud_ocupante.dibujar();

		PanelTabla pat_panel3 = new PanelTabla();
		pat_panel3.setPanelTabla(tab_solicitud_ocupante);

		tab_solicitud_archivo.setId("tab_solicitud_archivo");
		tab_solicitud_archivo.setIdCompleto("tab_tabulador:tab_solicitud_archivo");
		tab_solicitud_archivo.setTabla("veh_solicitud_archivo", "ide_vesoa", 4);
		tab_solicitud_archivo.getColumna("archivo_vesoa").setUpload("agendamiento");
		tab_solicitud_archivo.setLectura(true);
		tab_solicitud_archivo.dibujar();

		PanelTabla pat_panel4 = new PanelTabla();
		pat_panel4.setPanelTabla(tab_solicitud_archivo);

		tab_tabulador.agregarTab("RUTA", pat_panel2);
		tab_tabulador.agregarTab("OCUPANTES", pat_panel3);
		tab_tabulador.agregarTab("JUSTIFICACIÓN", pat_panel4);

		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(pat_panel, tab_tabulador, "70%", "H");
		agregarComponente(div_division);

		// BOTON VERIFICAR VEHICULO
		Boton bot_agregarVehiculo = new Boton();
		bot_agregarVehiculo.setValue("Asignar Vehículo");
		bot_agregarVehiculo.setIcon("ui-icon-person");
		bot_agregarVehiculo.setMetodo("agregarVehiculo");
		bar_botones.agregarBoton(bot_agregarVehiculo);
		
		Boton bot_agregarConductor = new Boton();
		bot_agregarConductor.setValue("Asignar Conductor");
		bot_agregarConductor.setIcon("ui-icon-person");
		bot_agregarConductor.setMetodo("aceptarConductor");
		bar_botones.agregarBoton(bot_agregarConductor);
		
		Boton bot_pendientes = new Boton();
		bot_pendientes.setValue("Pendientes");
		bot_pendientes.setIcon("ui-icon-alert");
		bot_pendientes.setMetodo("pendiente");
		bar_botones.agregarBoton(bot_pendientes);
		
		Boton bot_todos = new Boton();
		bot_todos.setValue("Todos");
		bot_todos.setIcon("ui-icon-refresh");
		bot_todos.setMetodo("todos");
		bar_botones.agregarBoton(bot_todos);

		set_pantalla_sucursal.setId("set_pantalla_sucursal");
		set_pantalla_sucursal.setTitle("SELECCIONE UN VEHÍCULO");
		set_pantalla_sucursal.getBot_aceptar().setMetodo("aceptarVehiculo");
		set_pantalla_sucursal.setSeleccionTabla(ser_transporte.getVehiculoCapacidad(""), "ide_veveh");

		set_pantalla_sucursal.getTab_seleccion().getColumna("placa_veveh").setFiltro(true);
		set_pantalla_sucursal.getTab_seleccion().getColumna("ruta").setNombreVisual("RUTA");
		set_pantalla_sucursal.getTab_seleccion().getColumna("ruta").setLongitud(30);
		set_pantalla_sucursal.getTab_seleccion().getColumna("conductor").setLongitud(40);
		set_pantalla_sucursal.setRadio();
		set_pantalla_sucursal.getTab_seleccion().ejecutarSql();
		agregarComponente(set_pantalla_sucursal);
		
		set_pantalla_conductor.setId("set_pantalla_conductor");
		set_pantalla_conductor.setTitle("SELECCIONE UN CONDUCTOR");
		set_pantalla_conductor.getBot_aceptar().setMetodo("aceptarConductor");
		set_pantalla_conductor.setSeleccionTabla(ser_transporte.getSqlConductor("true"), "IDE_VECON");
		set_pantalla_conductor.getTab_seleccion().getColumna("NOMBRES_APELLIDOS").setFiltroContenido();
		set_pantalla_conductor.setRadio();
		set_pantalla_conductor.getTab_seleccion().ejecutarSql();
		agregarComponente(set_pantalla_conductor);

	}
	
	public void seleccionaElAnio (){
		
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Año", "");
			return;			
		}
		
		if(com_sucursales.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione una Sucursal", "");
			return;			
		}
		
		tab_solicitud.setCondicion(" coalesce(aprobado_vesol,false)=true and extract(year from fecha_solicitud_vesol)=(SELECT cast(detalle_geani as int) as anio FROM gen_anio where ide_geani="+com_anio.getValue()+") and ide_sucu="+com_sucursales.getValue());
		tab_solicitud.ejecutarSql();
		tab_solicitud_ruta.ejecutarValorForanea(tab_solicitud.getValorSeleccionado());
		tab_solicitud_ocupante.ejecutarValorForanea(tab_solicitud.getValorSeleccionado());
		tab_solicitud_archivo.ejecutarValorForanea(tab_solicitud.getValorSeleccionado());
	}
	
	public void cambioSucursal(){
		
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Año", "");
			return;			
		}
		
		if(com_sucursales.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione una Sucursal", "");
			return;			
		}

		tab_solicitud.setCondicion(" coalesce(aprobado_vesol,false)=true and extract(year from fecha_solicitud_vesol)=(SELECT cast(detalle_geani as int) as anio FROM gen_anio where ide_geani="+com_anio.getValue()+") and ide_sucu="+com_sucursales.getValue());
		tab_solicitud.ejecutarSql();
		tab_solicitud_ruta.ejecutarValorForanea(tab_solicitud.getValorSeleccionado());
		tab_solicitud_ocupante.ejecutarValorForanea(tab_solicitud.getValorSeleccionado());
		tab_solicitud_archivo.ejecutarValorForanea(tab_solicitud.getValorSeleccionado());  	
    }
	
	public void pendiente(){
		
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Año", "");
			return;			
		}
		
		if(com_sucursales.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione una Sucursal", "");
			return;			
		}
		
		tab_solicitud.setCondicion(" ide_vetes=1 and coalesce(aprobado_vesol,false)=true and extract(year from fecha_solicitud_vesol)=(SELECT cast(detalle_geani as int) as anio FROM gen_anio where ide_geani="+com_anio.getValue()+") and ide_sucu="+com_sucursales.getValue());
		tab_solicitud.ejecutarSql();
		tab_solicitud_ruta.ejecutarValorForanea(tab_solicitud.getValorSeleccionado());
		tab_solicitud_ocupante.ejecutarValorForanea(tab_solicitud.getValorSeleccionado());
		tab_solicitud_archivo.ejecutarValorForanea(tab_solicitud.getValorSeleccionado());
	}
	
	public void todos(){
		
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Año", "");
			return;			
		}
		
		if(com_sucursales.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione una Sucursal", "");
			return;			
		}
		
		tab_solicitud.setCondicion(" extract(year from fecha_solicitud_vesol)=(SELECT cast(detalle_geani as int) as anio FROM gen_anio where ide_geani="+com_anio.getValue()+") and ide_sucu="+com_sucursales.getValue());
		tab_solicitud.ejecutarSql();
		tab_solicitud_ruta.ejecutarValorForanea(tab_solicitud.getValorSeleccionado());
		tab_solicitud_ocupante.ejecutarValorForanea(tab_solicitud.getValorSeleccionado());
		tab_solicitud_archivo.ejecutarValorForanea(tab_solicitud.getValorSeleccionado());
	}
	
	public void aceptarConductor()
	{
		if(set_pantalla_conductor.isVisible())
		{
			String str_seleccionado = set_pantalla_conductor.getValorSeleccionado();
			if (str_seleccionado != null) {
				set_pantalla_conductor.cerrar();
				tab_solicitud.setValor("ide_vecon", str_seleccionado);
				tab_solicitud.modificar(tab_solicitud.getFilaActual());
				tab_solicitud.guardar();
				guardarPantalla();
				utilitario.addUpdate("tab_solicitud");
			} else {
				utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
			}
		}
		else
			set_pantalla_conductor.dibujar();
	}

	public void aceptarVehiculo() {
		String str_seleccionado = set_pantalla_sucursal.getValorSeleccionado();
		if (str_seleccionado != null) {
			// Inserto la sucursal en la tabla
			//if (tab_solicitud.isFilaInsertada() == false) {
				// Controla que si ya esta insertada no vuelva a insertar
				// tab_solicitud.insertar();
			//}

			// if(validarCapacidad(str_seleccionado)){

			set_pantalla_sucursal.cerrar();
			tab_solicitud.setValor("ide_veveh", str_seleccionado);
			TablaGenerica tabV = utilitario.consultar("select * from veh_vehiculo where ide_veveh="+str_seleccionado);
			if(pckUtilidades.CConversion.CInt(tabV.getValor("ide_vecon"))>0)
				tab_solicitud.setValor("ide_vecon", tabV.getValor("ide_vecon"));
			
			tab_solicitud.modificar(tab_solicitud.getFilaActual());
			tab_solicitud.guardar();
			guardarPantalla();
			utilitario.addUpdate("tab_solicitud");

			// }else{
			// utilitario.agregarMensajeInfo(
			// "El vehículo seleccionado no dispone de capacidad", "");
			// }

		} else {
			utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
		}
	}

	public boolean validarCapacidad(String str_ide_veveh) {
		int pasajero = 0;
		boolean a;

		TablaGenerica tab = utilitario.consultar(ser_transporte.getVehiculoDisponibilidad(tab_solicitud.getValor("fecha_salida_vesol"), str_ide_veveh));

		pasajero = pckUtilidades.CConversion.CInt(tab_solicitud.getValor("ocupantes_vesol")) + pckUtilidades.CConversion.CInt(tab.getValor("capacidad_restante"));

		if (pasajero >= 0) {
			a = true;
		} else {
			a = false;
		}

		return a;
	}

	public void agregarVehiculo() {
		// Hace aparecer el componente

		try {
			if (!tab_solicitud.getValor("tipo_calculo_vesol").isEmpty() || tab_solicitud.getValor("tipo_calculo_vesol") != null) {

				if (tab_solicitud.getValor("tipo_calculo_vesol").equals("1")) {

					set_pantalla_sucursal.getTab_seleccion().setSql(ser_transporte.getVehiculoDisponible(tab_solicitud.getValor("fecha_salida_vesol"), tab_solicitud.getValor("fecha_regreso_vesol")));
					set_pantalla_sucursal.getTab_seleccion().getColumna("ide_veveh").setFiltro(true);
					set_pantalla_sucursal.getTab_seleccion().getColumna("placa_veveh").setFiltro(true);
					set_pantalla_sucursal.setRadio();
					set_pantalla_sucursal.getTab_seleccion().ejecutarSql();
					set_pantalla_sucursal.dibujar();
				} else {
					set_pantalla_sucursal.getTab_seleccion().setSql(ser_transporte.getVehiculoCapacidad(tab_solicitud.getValor("fecha_salida_vesol")));
					set_pantalla_sucursal.getTab_seleccion().getColumna("ide_veveh").setFiltro(true);
					set_pantalla_sucursal.getTab_seleccion().getColumna("placa_veveh").setFiltro(true);
					set_pantalla_sucursal.setRadio();
					set_pantalla_sucursal.getTab_seleccion().ejecutarSql();
					set_pantalla_sucursal.dibujar();
				}

			} else {
				utilitario.agregarMensajeInfo("No se puede Asignar el Vehículo", "Seleccione el tipo de asignación (Disponibilidad o Capacidad");

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			utilitario.agregarMensajeInfo("No se puede Asignar el Vehículo", "Seleccione el tipo de asignación (Disponibilidad o Capacidad");
		}
	}

	public void calculaDiasComision(DateSelectEvent evt) {
		tab_solicitud.modificar(evt);

		int numeroDias = 0;

		numeroDias = ser_transporte.getCalcularDias(tab_solicitud.getValor("fecha_salida_vesol"), tab_solicitud.getValor("fecha_regreso_vesol"));

		// System.out.println("tiempo dias "+numeroDias+"");

		if (numeroDias > 0) {

			tab_solicitud.setValor("dias_solicitado_vesol", +numeroDias + "");
			utilitario.addUpdateTabla(tab_solicitud, "dias_solicitado_vesol", "");
			// tab_solicitud.setValor("fecha_salida_vesol", tab_solicitud
			// .getColumna("fecha_salida_vesol").toString());
			// tab_solicitud.setValor("fecha_regreso_vesol", tab_solicitud
			// .getColumna("fecha_regreso_vesol").toString());
			// utilitario.addUpdateTabla(tab_solicitud, "ide_veveh", "");
		} else {
			utilitario.agregarMensajeInfo("No se puede calcular", "La fecha de salida no puede ser mayor a la fecha de retorno");
		}

		// utilitario.addUpdateTabla(tab_solicitud, "ide_veveh", "");
		// disponibilidadVehiculo(null);
	}

	public void calculaHorasComision(AjaxBehaviorEvent evt) {
		tab_solicitud.modificar(evt);

		double tiempoSolicitado = 0;

		tiempoSolicitado = ser_transporte.getHoras(tab_solicitud.getValor("hora_salida_vesol"), tab_solicitud.getValor("hora_retorno_vesol"));

		// System.out.println("tiempo solicitado " + tiempoSolicitado + "");

		if (tiempoSolicitado > 0) {
			tab_solicitud.setValor("tiempo_solicitado_vesol", +redondearDecimales(tiempoSolicitado, 2) + "");
			utilitario.addUpdateTabla(tab_solicitud, "tiempo_solicitado_vesol", "");
		} else {
			utilitario.agregarMensajeInfo("No se puede calcular", "La hora de salida no puede ser mayor a la hora de retorno");
		}
	}

	public static double redondearDecimales(double valorInicial, int numeroDecimales) {
		double parteEntera, resultado;
		resultado = valorInicial;
		parteEntera = Math.floor(resultado);
		resultado = (resultado - parteEntera) * Math.pow(10, numeroDecimales);
		resultado = Math.round(resultado);
		resultado = (resultado / Math.pow(10, numeroDecimales)) + parteEntera;
		return resultado;
	}

	/*public String verVehiculoDisponible(AjaxBehaviorEvent evt) {
		tab_solicitud.modificar(evt);
		String sql = "";
		if (tab_solicitud.getValor("fecha_salida_vesol") != null && !tab_solicitud.getValor("fecha_salida_vesol").isEmpty() && tab_solicitud.getValor("fecha_regreso_vesol") != null && !tab_solicitud.getValor("fecha_regreso_vesol").isEmpty()) {

			sql = ser_transporte.getVehiculoDisponible(tab_solicitud.getValor("fecha_salida_vesol"), tab_solicitud.getValor("fecha_regreso_vesol"));
			// utilitario.addUpdateTabla(tab_solicitud, "ide_veveh", "");
		} else {

			sql = ser_transporte.getVehiculoDisponible(tab_solicitud.getValor("2001-01-01"), tab_solicitud.getValor("2020-01-01"));

			// utilitario.addUpdateTabla(tab_solicitud, "ide_veveh", "");
		}

		// String sql =
		// ser_transporte.getVehiculoDisponible(tab_solicitud.getValor("fecha_salida_vesol"),
		// tab_solicitud.getValor("fecha_regreso_vesol"));
		return sql;
	}

	public void disponibilidadVehiculo(AjaxBehaviorEvent evt) {

		tab_solicitud.modificar(evt);

		tab_solicitud.setValor("fecha_salida_vesol", tab_solicitud.getColumna("fecha_salida_vesol").toString());
		tab_solicitud.setValor("fecha_regreso_vesol", tab_solicitud.getColumna("fecha_regreso_vesol").toString());

		// tab_solicitud.getColumna("ide_veveh").setCombo(ser_transporte.getVehiculoDisponible(utilitario.DeDateAString(fecha_inicial),
		// utilitario.DeDateAString(fecha_final)));

		utilitario.addUpdateTabla(tab_solicitud, "ide_veveh", "");

	}

	public String getSqlVehiculosDisponibles(String fechaInicial, String fechaFinal) {

		String str_sql = "";

		if (fechaInicial != null && !fechaInicial.isEmpty() && fechaFinal != null && !fechaFinal.isEmpty()) {

			str_sql = " select veh.ide_veveh, veh.placa_veveh from veh_vehiculo veh where veh.ide_veveh not in ( select sol.ide_veveh " + " from veh_solicitud sol	where sol.activo_vesol ='true' and sol.ide_vetes in(2) "
					+ " and sol.fecha_salida_vesol between '" + utilitario.DeStringADate(fechaInicial) + "' and '" + utilitario.DeStringADate(fechaFinal) + "'" + " or sol.fecha_regreso_vesol  between '" + utilitario.DeStringADate(fechaInicial) + "' and '"
					+ utilitario.DeStringADate(fechaFinal) + "' " + "  and sol.ide_veveh is not null and veh.activo_veveh = true ); ";

			// System.out.println("el sql" + str_sql);
		} else {

			Date d1 = new Date();
			Date d2 = new Date();

			d1.getDate();
			d2.getDate();

			System.out.println("d1" + d1 + "  d2" + d2);
			fechaInicial = utilitario.DeDateAString(d1);
			fechaInicial = utilitario.DeDateAString(d2);

			str_sql = " select veh.ide_veveh, veh.placa_veveh from veh_vehiculo veh where veh.ide_veveh not in ( select sol.ide_veveh " + " from veh_solicitud sol	where sol.activo_vesol ='true' and sol.ide_vetes in(2) "
					+ " and sol.fecha_salida_vesol between '" + d1 + "' and '" + d2 + "'" + " or sol.fecha_regreso_vesol  between '" + d1 + "' and '" + d2 + "' " + " and sol.ide_veveh is not null and veh.activo_veveh = true ); ";
			// System.out.println("el segundo sql" + str_sql);
		}
		return str_sql;
	}*/
	
	private void validarEstadoSolicitud()
	{
		ide_vetes=""+pckUtilidades.CConversion.CInt(tab_solicitud.getValor("ide_vetes"));
		
		if(pckUtilidades.CConversion.CInt(tab_solicitud.getValor("ide_vetes"))==1)
		{
			utilitario.agregarMensajeInfo("Por Aprobar", "Recuerde que esta solicitud esta pendiente de aprobación/anulación");
		}
	}
	
	@Override
	public void actualizar() {
		// TODO Auto-generated method stub
		super.actualizar();

		validarEstadoSolicitud();
	}

	/**
	 * metodo para el boton Inicio del navegador de paginas, muestra el primer registro de la tabla 
	 * 
	 */
	@Override
	public void inicio() {
		// TODO Auto-generated method stub
		super.inicio();
		validarEstadoSolicitud();
	}


	/**
	 * metodo para el boton Fin del navegador de paginas, muestra el ultimo registro de la tabla 
	 * 
	 */
	@Override
	public void fin() {
		// TODO Auto-generated method stub
		super.fin();
		validarEstadoSolicitud();
	}


	/**
	 * metodo para el boton Siguiente del navegador de paginas, muestra un registro posterior del registro actual de la tabla 
	 * 
	 */
	@Override
	public void siguiente() {
		// TODO Auto-generated method stub
		super.siguiente();
		validarEstadoSolicitud();
	}


	/**
	 * metodo para el boton Atras del navegador de paginas, muestra un registro anterior del registro actual de la tabla 
	 * 
	 */
	@Override
	public void atras() {
		// TODO Auto-generated method stub
		super.atras();
		validarEstadoSolicitud();
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		// tab_solicitud.insertar();
		// // tab_solicitud_ruta.insertar();
		utilitario.getTablaisFocus().insertar();

	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		
		ser_contabilidad.limpiarAcceso("veh_solicitud");
		ser_contabilidad.limpiarAcceso("veh_solicitud_ruta");
		if (tab_solicitud.guardar()) {
			if (tab_solicitud_ruta.guardar()) {
				System.out.println("guardar: ide_vetes="+ide_vetes+" tab_solicitud.getValor(ide_vetes)="+tab_solicitud.getValor("ide_vetes"));
				if(!ide_vetes.equals(tab_solicitud.getValor("ide_vetes")))
					enviarCorreo();
			}
		}
		guardarPantalla();
		validarEstadoSolicitud();
	}

	public void enviarCorreo() {
		
		TablaGenerica tab_correo_envio = utilitario.consultar("select co.ide_corr,smtp_corr, puerto_corr, usuario_corr, correo_corr,clave_corr,plantilla_cpla from sis_correo co " + "left join sis_correo_plantilla cop on cop.ide_corr=co.ide_corr "
				+ "where activo_cpla = true and activo_corr = true and co.ide_corr=3");

		EnvioMail envMail = new EnvioMail(tab_correo_envio.getValor("smtp_corr"), tab_correo_envio.getValor("puerto_corr"), tab_correo_envio.getValor("correo_corr"), tab_correo_envio.getValor("usuario_corr"), tab_correo_envio.getValor("clave_corr"));

		TablaGenerica tab_correo_plantilla = utilitario.consultar("select ide_cpla,co.ide_corr,smtp_corr, puerto_corr, usuario_corr, correo_corr,clave_corr,plantilla_cpla from sis_correo co "
				+ "left join sis_correo_plantilla cop on cop.ide_corr=co.ide_corr " + "where activo_cpla = true and activo_corr = true and co.ide_corr=3 order by 1;");

		TablaGenerica tab_correo_vehiculo = utilitario.consultar("select v.ide_veveh, v.placa_veveh, c.ide_vecon, emp.ide_gtemp, " + " EMP.APELLIDO_PATERNO_GTEMP ||' '|| (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' "
				+ " else EMP.APELLIDO_MATERNO_GTEMP end) ||' '|| EMP.PRIMER_NOMBRE_GTEMP ||' '|| " + " (case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS conductor, email_vecon " + " from veh_vehiculo v "
				+ " left join veh_conductor c on v.ide_vecon=c.ide_vecon " + " left join gth_empleado emp on emp.ide_gtemp=c.ide_gtemp" 
				+ " where v.activo_veveh=true and c.activo_vecon=true " + " and v.ide_veveh=" + tab_solicitud.getValor("ide_veveh") + "");

		//pckUtilidades.Utilitario util = new pckUtilidades.Utilitario();

		TablaGenerica tab_empleado = utilitario.consultar(ser_nomina.servicioEmpleadoCorreo(tab_solicitud.getValor("ide_gtemp")));
		TablaGenerica tab_jefe = ser_nomina.ideEmpleadoContrato(tab_solicitud.getValor("ide_geedp"),"true,false");
		
		TablaGenerica tab_solicitud_estado = utilitario.consultar("select ide_vetes, detalle_vetes from veh_solicitud_estado where ide_vetes=" + tab_solicitud.getValor("ide_vetes") + " ;");
		TablaGenerica tab_solicitud_ruta = utilitario.consultar("SELECT ide_vesru, detalle_vesru,detalle_salida_vesru FROM veh_solicitud_ruta where ide_vesol=" + tab_solicitud.getValor("ide_vesol") + " ;");

		String str_asunto = "AGENDAMIENTO VEHICULAR";
		String str_mensaje = tab_correo_plantilla.getValor(0, "plantilla_cpla");
		String str_fecha = utilitario.getFechaLarga(tab_solicitud.getValor("fecha_salida_vesol"));
		String str_hora = utilitario.DeDateAStringHora(utilitario.getHoraCalendario(tab_solicitud.getValor("hora_salida_vesol")));
		String str_estado = pckUtilidades.CConversion.CStr(tab_solicitud_estado.getValor("detalle_vetes"));
		String str_placa = pckUtilidades.CConversion.CStr(tab_correo_vehiculo.getValor("placa_veveh"));
		String str_conductor = pckUtilidades.CConversion.CStr(tab_correo_vehiculo.getValor("conductor"));
		String str_rutaDestino = pckUtilidades.CConversion.CStr(tab_solicitud_ruta.getValor("detalle_vesru"));
		String str_rutaOrigen = pckUtilidades.CConversion.CStr(tab_solicitud_ruta.getValor("detalle_salida_vesru"));
		String str_obs = pckUtilidades.CConversion.CStr(tab_solicitud.getValor("observaciones_vesol"));
		String str_aprobado = pckUtilidades.CConversion.CBol(tab_solicitud.getValor("aprobado_vesol"))?"SI":"NO";
		String str_jefeAprobador = pckUtilidades.CConversion.CStr(tab_jefe.getValor("PRIMER_NOMBRE_GTEMP"))+" "+pckUtilidades.CConversion.CStr(tab_jefe.getValor("APELLIDO_PATERNO_GTEMP"));
		String str_motivo=pckUtilidades.CConversion.CStr(tab_solicitud.getValor("motivo_vesol"));
		str_mensaje = str_mensaje.replaceAll("@FECHA", str_fecha);
		str_mensaje = str_mensaje.replaceAll("@HORA", str_hora);
		str_mensaje = str_mensaje.replaceAll("@ESTADO", str_estado);
		str_mensaje = str_mensaje.replaceAll("@PLACA", str_placa);
		str_mensaje = str_mensaje.replaceAll("@CONDUCTOR", str_conductor);
		str_mensaje = str_mensaje.replaceAll("@OBSERVACION", str_obs);
		str_mensaje = str_mensaje.replaceAll("@SALIDA", str_rutaOrigen);
		str_mensaje = str_mensaje.replaceAll("@DESTINO", str_rutaDestino);
		str_mensaje = str_mensaje.replaceAll("@APROBADO", str_aprobado);
		str_mensaje = str_mensaje.replaceAll("@JEFEAPROBADOR", str_jefeAprobador);
		str_mensaje=str_mensaje.replaceAll("@MOTIVO", str_motivo);
		String str_funcionario = "";

		try {

			System.out.println("Ingresa a correo solicitante");
			str_funcionario = tab_empleado.getValor("nombres");
			str_mensaje = str_mensaje.replaceAll("@FUNCIONARIO", str_funcionario);
			String str_mail = tab_empleado.getValor("detalle_gtcor");
			System.out.println("enviarCorreo agendamiento sol: "+str_mail);
			//str_mail = "alex.becerra@emgirs.gob.ec";	
			
			envMail.setAsunto(str_asunto);
			envMail.setCuerpoHtml(str_mensaje);
			envMail.setPara(str_mail);
			if(pckUtilidades.consumoServiciosCore.enviarMail(envMail).getRespuesta())
			{
				utilitario.agregarMensaje("Correo de notificación","Enviado exitosamente a : " + str_mail);
			}
			else
				utilitario.agregarMensajeError("Correo de notificación","Error al enviar la notificación al correo: " + str_mail);

			if (pckUtilidades.CConversion.CInt(tab_solicitud.getValor("gen_ide_gtemp")) != pckUtilidades.CConversion.CInt(tab_solicitud.getValor("ide_gtemp"))) {
				TablaGenerica tab_emp_solicita = utilitario.consultar(ser_nomina.servicioEmpleadoCorreo(tab_solicitud.getValor("gen_ide_gtemp") + ""));
				str_mail = tab_emp_solicita.getValor("detalle_gtcor");
				System.out.println("enviarCorreo agendamiento sol: "+str_mail);
				//str_mail = "alex.becerra@emgirs.gob.ec";	
				str_funcionario = tab_emp_solicita.getValor("nombres");
				str_mensaje = str_mensaje.replaceAll("@FUNCIONARIO", str_funcionario);
				//util.EnviaMailInterno(envMail, str_mail, str_asunto, str_mensaje1, null);
				envMail.setAsunto(str_asunto);
				envMail.setCuerpoHtml(str_mensaje);
				envMail.setPara(str_mail);
				if(pckUtilidades.consumoServiciosCore.enviarMail(envMail).getRespuesta())
				{
					utilitario.agregarMensaje("Correo de notificación","Enviado exitosamente a : " + str_mail);
				}
				else
					utilitario.agregarMensajeError("Correo de notificación","Error al enviar la notificación al correo: " + str_mail);
			}

		} catch (Exception e) {
			System.out.println("Error al enviar el correo al solicitante: " + e.getMessage());
		}

		// correo del responsable de transporte
		if (tab_solicitud.getValor("ide_veveh") != null) {
			try {
				System.out.println("Ingresa a correo conductor");
				str_asunto = "NUEVA SOLICITUD DE AGENDAMIENTO VEHICULAR";
				str_mensaje = tab_correo_plantilla.getValor(2, "plantilla_cpla");
				str_mensaje = str_mensaje.replaceAll("@FECHA", str_fecha);
				str_mensaje = str_mensaje.replaceAll("@HORA", str_hora);
				str_mensaje = str_mensaje.replaceAll("@PLACA", str_placa);
				str_mensaje = str_mensaje.replaceAll("@CONDUCTOR", str_conductor);
				str_mensaje = str_mensaje.replaceAll("@ESTADO", str_estado);
				str_mensaje = str_mensaje.replaceAll("@FUNCIONARIO", str_funcionario);
				str_mensaje = str_mensaje.replaceAll("@SALIDA", str_rutaOrigen);
				str_mensaje = str_mensaje.replaceAll("@DESTINO", str_rutaDestino);

				String str_mail = tab_correo_vehiculo.getValor("email_vecon");
				System.out.println("enviarCorreo agendamiento conductor: "+str_mail);
				//str_mail = "alexbec0000@gmail.com";	
				System.out.println("Correo enviado a: "+str_conductor);
				//util.EnviaMailInterno(envMail, str_mail, str_asunto, str_mensaje, null);
				envMail.setAsunto(str_asunto);
				envMail.setCuerpoHtml(str_mensaje);
				envMail.setPara(str_mail);
				if(pckUtilidades.consumoServiciosCore.enviarMail(envMail).getRespuesta())
				{
					utilitario.agregarMensaje("Correo de notificación","Enviado exitosamente a : " + str_mail);
				}
				else
					utilitario.agregarMensajeError("Correo de notificación","Error al enviar la notificación al correo: " + str_mail);
			} catch (Exception ex) {
				System.err.println("Correo no Enviado conductor " + ex.getMessage());
			}
		}

	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub

	}

	public Tabla getTab_solicitud() {
		return tab_solicitud;
	}

	public void setTab_solicitud(Tabla tab_solicitud) {
		this.tab_solicitud = tab_solicitud;
	}

	public Tabla getTab_solicitud_ruta() {
		return tab_solicitud_ruta;
	}

	public void setTab_solicitud_ruta(Tabla tab_solicitud_ruta) {
		this.tab_solicitud_ruta = tab_solicitud_ruta;
	}

	public SeleccionTabla getSet_pantalla_sucursal() {
		return set_pantalla_sucursal;
	}

	public void setSet_pantalla_sucursal(SeleccionTabla set_pantalla_sucursal) {
		this.set_pantalla_sucursal = set_pantalla_sucursal;
	}

	public List getLista() {
		return lista;
	}

	public void setLista(List lista) {
		this.lista = lista;
	}

	public ServicioTransporte getSer_transporte() {
		return ser_transporte;
	}

	public void setSer_transporte(ServicioTransporte ser_transporte) {
		this.ser_transporte = ser_transporte;
	}

	public ServicioContabilidad getSer_contabilidad() {
		return ser_contabilidad;
	}

	public void setSer_contabilidad(ServicioContabilidad ser_contabilidad) {
		this.ser_contabilidad = ser_contabilidad;
	}

	public Date getFecha_inicial() {
		return fecha_inicial;
	}

	public void setFecha_inicial(Date fecha_inicial) {
		this.fecha_inicial = fecha_inicial;
	}

	public Date getFecha_final() {
		return fecha_final;
	}

	public void setFecha_final(Date fecha_final) {
		this.fecha_final = fecha_final;
	}

	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}

	public Tabla getTab_solicitud_ocupante() {
		return tab_solicitud_ocupante;
	}

	public void setTab_solicitud_ocupante(Tabla tab_solicitud_ocupante) {
		this.tab_solicitud_ocupante = tab_solicitud_ocupante;
	}

	public Tabla getTab_solicitud_archivo() {
		return tab_solicitud_archivo;
	}

	public void setTab_solicitud_archivo(Tabla tab_solicitud_archivo) {
		this.tab_solicitud_archivo = tab_solicitud_archivo;
	}

	public SeleccionTabla getSet_pantalla_conductor() {
		return set_pantalla_conductor;
	}

	public void setSet_pantalla_conductor(SeleccionTabla set_pantalla_conductor) {
		this.set_pantalla_conductor = set_pantalla_conductor;
	}
	
	

}
