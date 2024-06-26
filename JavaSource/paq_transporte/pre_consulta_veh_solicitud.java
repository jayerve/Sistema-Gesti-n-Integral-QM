package paq_transporte;

import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import paq_transporte.ejb.ServicioTransporte;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

public class pre_consulta_veh_solicitud extends Pantalla {
	
	private AutoCompletar aut_vehiculo = new AutoCompletar();
	private Tabla tab_consulta=new Tabla();

	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
	
	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion ) utilitario.instanciarEJB(ServicioFacturacion.class);
	@EJB
	private ServicioNomina ser_empleado = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	@EJB
	private ServicioTransporte ser_transporte = (ServicioTransporte) utilitario.instanciarEJB(ServicioTransporte.class);


	public pre_consulta_veh_solicitud() {
		
		
		bar_botones.limpiar();
		
		aut_vehiculo.setId("aut_vehiculo");     
		aut_vehiculo.setAutoCompletar(ser_transporte.getDatosVehiculos()); 
		aut_vehiculo.setMetodoChange("seleccionoAutocompletar"); //ejecuta el metodo seleccionoAutocompletar
		
		Etiqueta eti_colaborador=new Etiqueta("PLACA DE VEHÍCULO:");
		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_vehiculo);
		
		
		bar_botones.agregarComponente(new Etiqueta("Fecha Inicial :"));
		cal_fecha_inicial.setValue(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		bar_botones.agregarComponente(cal_fecha_inicial);

		bar_botones.agregarComponente(new Etiqueta("Fecha Final :"));
		cal_fecha_final.setValue(utilitario.sumarDiasFecha(utilitario.getDate(), 0));
		bar_botones.agregarComponente(cal_fecha_final);

		Boton bot_actualiza = new Boton();
		bot_actualiza.setId("bot_actualiza");
		bot_actualiza.setTitle("ACTUALIZAR");
		bot_actualiza.setValue("ACTUALIZAR");
		bot_actualiza.setMetodo("consultar");
		bar_botones.agregarBoton(bot_actualiza);
		
		tab_consulta.setId("tab_consulta");
		tab_consulta.setSql(ser_transporte.getSolicitudesVehiculo("2", "",cal_fecha_inicial.getFecha(),cal_fecha_final.getFecha()));
		tab_consulta.setRows(30);
		tab_consulta.setLectura(true);
		tab_consulta.dibujar();

		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_consulta);

		Division div_tabla = new Division();
		div_tabla.dividir1(pat_panel);
		agregarComponente(div_tabla);
		
		// TODO Auto-generated constructor stub

	}

	public void seleccionoAutocompletar(SelectEvent evt){
		aut_vehiculo.onSelect(evt);
		String fecha_inicial=cal_fecha_inicial.getFecha();
		String fecha_final=cal_fecha_final.getFecha();
		
		System.out.println("aut vehiculo "+aut_vehiculo.getValor());
		tab_consulta.setSql(ser_transporte.getSolicitudesVehiculo("1",aut_vehiculo.getValor(),fecha_inicial, fecha_final));
		tab_consulta.ejecutarSql();
		utilitario.addUpdate("aut_factura");
	}
	
	public void consultar(){
		String fecha_inicial=cal_fecha_inicial.getFecha();
		String fecha_final=cal_fecha_final.getFecha();
		
		tab_consulta.setSql(ser_transporte.getSolicitudesVehiculo("2", "", fecha_inicial, fecha_final));
		tab_consulta.ejecutarSql();
		utilitario.addUpdate("tab_consulta");

	}
	
	public void limpiar(){
		aut_vehiculo.limpiar();
		tab_consulta.limpiar();
		utilitario.addUpdate("aut_vehiculo");
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

	public ServicioFacturacion getSer_facturacion() {
		return ser_facturacion;
	}

	public void setSer_facturacion(ServicioFacturacion ser_facturacion) {
		this.ser_facturacion = ser_facturacion;
	}

	public ServicioNomina getSer_empleado() {
		return ser_empleado;
	}

	public void setSer_empleado(ServicioNomina ser_empleado) {
		this.ser_empleado = ser_empleado;
	}

	public ServicioTransporte getSer_transporte() {
		return ser_transporte;
	}

	public void setSer_transporte(ServicioTransporte ser_transporte) {
		this.ser_transporte = ser_transporte;
	}

	public AutoCompletar getAut_vehiculo() {
		return aut_vehiculo;
	}

	public void setAut_vehiculo(AutoCompletar aut_vehiculo) {
		this.aut_vehiculo = aut_vehiculo;
	}

	public Tabla getTab_consulta() {
		return tab_consulta;
	}

	public void setTab_consulta(Tabla tab_consulta) {
		this.tab_consulta = tab_consulta;
	}

	
	
}
