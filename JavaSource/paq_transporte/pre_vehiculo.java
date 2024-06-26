package paq_transporte;

import java.util.Date;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.DateSelectEvent;

import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_gestion.ejb.ServicioGestion;
import paq_sistema.aplicacion.Pantalla;
import paq_transporte.ejb.ServicioTransporte;

public class pre_vehiculo extends Pantalla{
	
	private Tabla tab_vehiculo=new Tabla();
	public static String str_ide_activo_fijo_vehiculo = "108"; // tipo de asistencia
	@EJB
	private ServicioTransporte ser_transporte = (ServicioTransporte) utilitario.instanciarEJB(ServicioTransporte.class);
	
	public pre_vehiculo() {
		
		tab_vehiculo.setId("tab_vehiculo");
		tab_vehiculo.setTipoFormulario(true);  
		tab_vehiculo.getGrid().setColumns(4); 
		tab_vehiculo.setTabla("veh_vehiculo", "ide_veveh", 1);
		tab_vehiculo.getColumna("ide_veveh").setNombreVisual("CÓDIGO");
		tab_vehiculo.getColumna("ide_veveh").setOrden(1);
		tab_vehiculo.getColumna("activo_veveh");
		tab_vehiculo.getColumna("activo_veveh").setNombreVisual("ACTIVO");
		tab_vehiculo.getColumna("activo_veveh").setOrden(2);
		tab_vehiculo.getColumna("activo_veveh").setValorDefecto("true");
		tab_vehiculo.getColumna("ide_afact");
		tab_vehiculo.getColumna("ide_afact").setOrden(3);
		tab_vehiculo.getColumna("ide_afact").setNombreVisual("ACTIVO FIJO");;
//		tab_vehiculo.getColumna("ide_afact").setCombo("afi_activo", "ide_afact", "serie_afact", " ide_afnoa = "+str_ide_activo_fijo_vehiculo+"");
		tab_vehiculo.getColumna("ide_afact").setCombo(ser_transporte.getActivoFijoVehiculo());
		tab_vehiculo.getColumna("ide_afact").setAutoCompletar();
		tab_vehiculo.getColumna("placa_veveh");
		tab_vehiculo.getColumna("placa_veveh").setOrden(4);
		tab_vehiculo.getColumna("placa_veveh").setNombreVisual("Placa");
		tab_vehiculo.getColumna("motor_veveh");
		tab_vehiculo.getColumna("motor_veveh").setOrden(5);
		tab_vehiculo.getColumna("motor_veveh").setNombreVisual("Número de Motor");
		tab_vehiculo.getColumna("chasis_veveh");
		tab_vehiculo.getColumna("chasis_veveh").setOrden(6);
		tab_vehiculo.getColumna("chasis_veveh").setNombreVisual("Número de chasis");
		tab_vehiculo.getColumna("cilindraje_veveh");
		tab_vehiculo.getColumna("cilindraje_veveh").setOrden(7);
		tab_vehiculo.getColumna("cilindraje_veveh").setNombreVisual("Cilindraje");
		tab_vehiculo.getColumna("ide_vecol").setCombo("veh_tipo_color", "ide_vecol", "detalle_vecol", "");
		tab_vehiculo.getColumna("ide_vecol").setOrden(8);
		tab_vehiculo.getColumna("ide_vecol").setNombreVisual("Color");
		tab_vehiculo.getColumna("ide_vecom").setCombo("veh_tipo_combustible", "ide_vecom", "detalle_vecom", "");
		tab_vehiculo.getColumna("ide_vecom").setNombreVisual("Combustible");
		tab_vehiculo.getColumna("ide_vecom").setOrden(9);
		tab_vehiculo.getColumna("ide_vemar").setCombo("veh_tipo_marca", "ide_vemar", "detalle_vemar", "");
		tab_vehiculo.getColumna("ide_vemar").setNombreVisual("Marca");
		tab_vehiculo.getColumna("ide_vemar").setOrden(10);
		tab_vehiculo.getColumna("ide_vemod").setCombo("veh_tipo_modelo", "ide_vemod", "detalle_vemod", "");
		tab_vehiculo.getColumna("ide_vemod").setNombreVisual("Modelo");
		tab_vehiculo.getColumna("ide_vemod").setOrden(11);
		tab_vehiculo.getColumna("anio_fabricacion_veveh");
		tab_vehiculo.getColumna("anio_fabricacion_veveh").setOrden(12);
		tab_vehiculo.getColumna("anio_fabricacion_veveh").setNombreVisual("Año de Fabricación");
		tab_vehiculo.getColumna("fecha_adquisicion_veveh");
		tab_vehiculo.getColumna("fecha_adquisicion_veveh").setOrden(13);
		tab_vehiculo.getColumna("fecha_adquisicion_veveh").setNombreVisual("Fecha de Adquisición");
		tab_vehiculo.getColumna("fecha_matriculacion_veveh");
		tab_vehiculo.getColumna("fecha_matriculacion_veveh").setOrden(14);
		tab_vehiculo.getColumna("fecha_matriculacion_veveh").setNombreVisual("Fecha de matriculación");
		tab_vehiculo.getColumna("fecha_matriculacion_veveh").setMetodoChange("calculaTiempoVigMatricula");
		tab_vehiculo.getColumna("fecha_caducidad_veveh");
		tab_vehiculo.getColumna("fecha_caducidad_veveh").setOrden(15);
		tab_vehiculo.getColumna("fecha_caducidad_veveh").setNombreVisual("Fecha de Vigencia Matriculación");
//		tab_vehiculo.getColumna("ide_vecon").setCombo("veh_conductor", "ide_vecon", "detalle_vecon", "");
		tab_vehiculo.getColumna("ide_vecon").setCombo(ser_transporte.getSqlConductor("true,false"));
		tab_vehiculo.getColumna("ide_vecon").setAutoCompletar();
		tab_vehiculo.getColumna("ide_vecon").setNombreVisual("Conductor");
		tab_vehiculo.getColumna("ide_vecon").setOrden(16);
		tab_vehiculo.getColumna("capacidad_pasajeros_veveh");
		tab_vehiculo.getColumna("capacidad_pasajeros_veveh").setOrden(17);
		tab_vehiculo.getColumna("capacidad_pasajeros_veveh").setNombreVisual("Capacidad de pasajeros");

		tab_vehiculo.getColumna("ide_vetip").setCombo("veh_tipo_vehiculo", "ide_vetip", "detalle_vetip", "");
		tab_vehiculo.getColumna("ide_vetip").setOrden(18);
		tab_vehiculo.getColumna("ide_vetip").setNombreVisual("Tipo de Vehículo");

		tab_vehiculo.getColumna("kilometraje_veveh").setEstilo("font-size:13px;font-weight: bold;text-decoration: underline;color:black");
		tab_vehiculo.getColumna("kilometraje_veveh").setOrden(19);
		tab_vehiculo.getColumna("kilometraje_veveh").setNombreVisual("Kilometraje recorrido");

		tab_vehiculo.getColumna("fecha_ultimo_pago_veveh").setEstilo("font-size:13px;font-weight: bold;color:red");
		tab_vehiculo.getColumna("fecha_ultimo_pago_veveh").setOrden(20);
		tab_vehiculo.getColumna("fecha_ultimo_pago_veveh").setNombreVisual("Fecha de último pago Matric.");

		
		tab_vehiculo.getColumna("lectura_aceite_veveh").setEstilo("font-size:13px;font-weight: bold;color:red");
		tab_vehiculo.getColumna("lectura_aceite_veveh").setOrden(21);
		tab_vehiculo.getColumna("lectura_aceite_veveh").setNombreVisual("Kilometraje de cambio de Aceite");

		tab_vehiculo.getColumna("lectura_abc_veveh").setEstilo("font-size:13px;font-weight: bold;color:red");
		tab_vehiculo.getColumna("lectura_abc_veveh").setOrden(22);
		tab_vehiculo.getColumna("lectura_abc_veveh").setNombreVisual("Kilometraje de ABC realizado");

		tab_vehiculo.getColumna("lectura_llantas_veveh").setEstilo("font-size:13px;font-weight: bold;color:red");
		tab_vehiculo.getColumna("lectura_llantas_veveh").setOrden(23);
		tab_vehiculo.getColumna("lectura_llantas_veveh").setNombreVisual("Kilometraje de cambio de llantas");

		tab_vehiculo.setCondicion("activo_veveh=true");
		
		
		tab_vehiculo.dibujar();
		
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_vehiculo);
		Division div_division = new Division();
		div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
        
      //BOTON AGREGAR VEHIUCLO
    /*  	Boton bot_agregarVehiculo=new Boton();
      	bot_agregarVehiculo.setValue("Agregar Vehículo");
      	bot_agregarVehiculo.setTitle("Permite ingresar un vehículo gestionado en Activos Fijos");
      	bot_agregarVehiculo.setIcon("ui-icon-person");
      	bot_agregarVehiculo.setMetodo("agregarVehiculo");
      	bar_botones.agregarBoton(bot_agregarVehiculo);
		*/
	}
	
	/**
	 * método para calcular la fecha de caducidad de la especie de matricula
	 * @param evt
	 */
	public void calculaTiempoVigMatricula(DateSelectEvent evt){
		tab_vehiculo.modificar(evt);
		
		Date fechaVigencia= new Date();
		String fechaVigMatricula="";
		int anio=2011;
		int valorVigencia;
		valorVigencia=4*365; // 4 años para caducar la especie de matricula del vehiculo 
		

		try {
			fechaVigencia=ser_transporte.calculaFecha(utilitario.DeStringADate(tab_vehiculo.getValor("fecha_matriculacion_veveh")), valorVigencia);
			anio=utilitario.getAnio(utilitario.DeDateAString(fechaVigencia));
			fechaVigMatricula=anio+"-12-31" ;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		tab_vehiculo.setValor("fecha_caducidad_veveh",fechaVigMatricula);
		utilitario.addUpdateTabla(tab_vehiculo, "fecha_caducidad_veveh", "");
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_vehiculo.insertar();
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_vehiculo.guardar();
		guardarPantalla();	
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		
	}

	public Tabla getTab_vehiculo() {
		return tab_vehiculo;
	}

	public void setTab_vehiculo(Tabla tab_vehiculo) {
		this.tab_vehiculo = tab_vehiculo;
	}
	
	

}
