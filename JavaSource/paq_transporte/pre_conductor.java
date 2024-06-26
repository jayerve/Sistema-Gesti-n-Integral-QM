package paq_transporte;

import java.util.Date;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.DateSelectEvent;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;
import paq_transporte.ejb.ServicioTransporte;

public class pre_conductor extends Pantalla{

	private Tabla tab_conductor=new Tabla();
	@EJB
	private ServicioTransporte ser_transporte = (ServicioTransporte) utilitario.instanciarEJB(ServicioTransporte.class);
	
	public pre_conductor() {
		tab_conductor.setId("tab_conductor");  
		tab_conductor.setTipoFormulario(true);  
		tab_conductor.getGrid().setColumns(4); 
		tab_conductor.setTabla("veh_conductor", "ide_vecon", 1);	
		tab_conductor.getColumna("activo_vecon");
		tab_conductor.getColumna("activo_vecon").setOrden(1);
		tab_conductor.getColumna("activo_vecon").setNombreVisual("ACTIVO");
		tab_conductor.getColumna("activo_vecon").setValorDefecto("true");
//		tab_conductor.getColumna("ide_gtemp").setCombo("gth_empleado", "ide_gtemp", "apellido_paterno_gtemp", "");
		tab_conductor.getColumna("ide_gtemp").setCombo(ser_transporte.getSqlEmpleadosAutocompletar());
		tab_conductor.getColumna("ide_gtemp").setAutoCompletar();
		tab_conductor.getColumna("ide_gtemp").setNombreVisual("Empleado");
		tab_conductor.getColumna("detalle_vecon").setVisible(false);
		
//		tab_conductor.getColumna("ide_veveh").setCombo("veh_vehiculo", "ide_veveh", "placa_veveh", "");
		tab_conductor.getColumna("tipo_licencia_vecon");
		tab_conductor.getColumna("tipo_licencia_vecon").setNombreVisual("Tipo de Licencia");
		tab_conductor.getColumna("puntos_licencia_vecon");
		tab_conductor.getColumna("puntos_licencia_vecon").setNombreVisual("Puntos en Licencia");
		tab_conductor.getColumna("fecha_emitida_vecon");
		tab_conductor.getColumna("fecha_emitida_vecon").setNombreVisual("Fecha de emisión");
		tab_conductor.getColumna("fecha_emitida_vecon").setMetodoChange("calculaTiempoLicencia");
		tab_conductor.getColumna("fecha_vigencia_vecon");
		tab_conductor.getColumna("fecha_vigencia_vecon").setNombreVisual("Fecha de vigencia");
		tab_conductor.dibujar();
		PanelTabla pat_estado=new PanelTabla();
		pat_estado.setPanelTabla(tab_conductor);
		
		agregarComponente(pat_estado);
		
	}
	
	public void calculaTiempoLicencia(DateSelectEvent evt){
		tab_conductor.modificar(evt);
		
		Date fechaVigencia= new Date();
		int valorVigencia;
		valorVigencia=5*365;
		fechaVigencia=ser_transporte.calculaFecha(utilitario.DeStringADate(tab_conductor.getValor("fecha_emitida_vecon")), valorVigencia);
		System.out.println("fecha de vigencia"+fechaVigencia+"");

		tab_conductor.setValor("fecha_vigencia_vecon",utilitario.DeDateAString(fechaVigencia));
			utilitario.addUpdateTabla(tab_conductor, "fecha_vigencia_vecon", "");
		
	}


	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_conductor.insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_conductor.guardar();
		guardarPantalla();		
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_conductor.eliminar();
	}

	public Tabla gettab_conductor() {
		return tab_conductor;
	}

	public void settab_conductor(Tabla tab_conductor) {
		this.tab_conductor = tab_conductor;
	}


}
