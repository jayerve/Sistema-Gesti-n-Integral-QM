package paq_transporte;

import javax.ejb.EJB;

import org.primefaces.event.SelectEvent;

import paq_facturacion.ejb.ServicioFacturacion;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import paq_transporte.ejb.ServicioTransporte;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Check;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;

public class pre_consulta_veh_mantenimiento extends Pantalla {
	
	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion ) utilitario.instanciarEJB(ServicioFacturacion.class);
	@EJB
	private ServicioNomina ser_empleado = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	
	@EJB
	private ServicioTransporte ser_transporte = (ServicioTransporte) utilitario.instanciarEJB(ServicioTransporte.class);

	private AutoCompletar aut_vehiculo = new AutoCompletar();
	private Tabla tab_consulta=new Tabla();
	private Combo com_rubros = new Combo();

	
	private SeleccionTabla set_pantalla_sucursal= new SeleccionTabla();
	private SeleccionCalendario sec_rango=new SeleccionCalendario();
	private Check che_todos_emp=new Check();
	private SeleccionTabla set_det_tip_nomina=new SeleccionTabla();
	String str_fecha1="";
	String str_fecha2="";

	public pre_consulta_veh_mantenimiento() {
		
		bar_botones.getBot_eliminar().setRendered(false);
		bar_botones.getBot_guardar().setRendered(false);
		bar_botones.getBot_insertar().setRendered(false);
		
		bar_botones.limpiar();
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		aut_vehiculo.setId("aut_vehiculo");     
		aut_vehiculo.setAutoCompletar(ser_transporte.getDatosVehiculos()); 
		aut_vehiculo.setMetodoChange("seleccionoAutocompletar"); //ejecuta el metodo seleccionoAutocompletar
		
		Etiqueta eti_colaborador=new Etiqueta("PLACA DE VEHÍCULO:");
		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_vehiculo);
		bar_botones.agregarBoton(bot_limpiar);
		
		// boton consultar
		Boton bot_consultar = new Boton();
		bot_consultar.setIcon("ui-icon-calculator");
		bot_consultar.setMetodo("consultar");
		bot_consultar.setValue("Consultar");
		bot_consultar.setTitle("Consultar");
		bar_botones.agregarBoton(bot_consultar);
	
		tab_consulta.setId("tab_consulta");
		tab_consulta.setSql(ser_transporte.getVehiculosMantenimiento("0", "","",""));
		tab_consulta.getColumna("placa_veveh");
		tab_consulta.getColumna("placa_veveh").setLongitud(10);
		tab_consulta.getColumna("placa_veveh").setNombreVisual("Placa");
		tab_consulta.getColumna("detalle_vepav");
		tab_consulta.getColumna("detalle_vepav").setNombreVisual("Tipo de Mantenimiento");
		tab_consulta.getColumna("detalle_vepav").setLongitud(50);
		tab_consulta.getColumna("kilometraje_veveh");
		tab_consulta.getColumna("kilometraje_veveh").setLongitud(25);
		tab_consulta.getColumna("kilometraje_veveh").setNombreVisual("Km. Recorrido");
		tab_consulta.getColumna("lectura_aceite_veveh");
		tab_consulta.getColumna("lectura_aceite_veveh").setLongitud(20);
		tab_consulta.getColumna("lectura_aceite_veveh").setNombreVisual("Últ. Cambio de Aceite Km.");
		tab_consulta.getColumna("lectura_abc_veveh");
		tab_consulta.getColumna("lectura_abc_veveh").setLongitud(20);
		tab_consulta.getColumna("lectura_abc_veveh").setNombreVisual("Últ. Mant. ABC realizado Km.");
		tab_consulta.getColumna("lectura_llantas_veveh");
		tab_consulta.getColumna("lectura_llantas_veveh").setLongitud(20);
		tab_consulta.getColumna("lectura_llantas_veveh").setNombreVisual("Últ. Cambio de llantas Km.");
		tab_consulta.getColumna("detalle_vetip");
		tab_consulta.getColumna("detalle_vetip").setNombreVisual("Tipo Vehículo");
		tab_consulta.getColumna("detalle_vetip").setLongitud(30);
		tab_consulta.getColumna("valor_frecuencia_vepav");
		tab_consulta.getColumna("valor_frecuencia_vepav").setLongitud(30);
		tab_consulta.getColumna("valor_frecuencia_vepav").setNombreVisual("Frecuencia de cambio");
		tab_consulta.getColumna("fecha_ultimo_pago_veveh");
		tab_consulta.getColumna("fecha_ultimo_pago_veveh").setLongitud(30);
		tab_consulta.getColumna("fecha_ultimo_pago_veveh").setNombreVisual("Fecha de pago matricula");
		tab_consulta.getColumna("valor_aceite");
		tab_consulta.getColumna("valor_aceite").setLongitud(25);
		tab_consulta.getColumna("valor_aceite").setNombreVisual("Alerta de Aceite");
		tab_consulta.getColumna("valor_aceite").setVisible(false);
//		tab_consulta.getColumna("valor_aceite").setEstilo(ser_transporte.getAlertaMantenimiento(tab_consulta.getValor("valor_aceite")));
//		tab_consulta.getColumna("valor_aceite").setEstilo(alertaAceite());
		tab_consulta.getColumna("valor_abc");
		tab_consulta.getColumna("valor_abc").setLongitud(25);
		tab_consulta.getColumna("valor_abc").setNombreVisual("Alerta ABC");
//		tab_consulta.getColumna("valor_abc").setEstilo(ser_transporte.getAlertaMantenimiento(tab_consulta.getValor("valor_abc")));
		tab_consulta.getColumna("valor_abc").setVisible(false);
		tab_consulta.getColumna("valor_llantas");
		tab_consulta.getColumna("valor_llantas").setLongitud(25);
		tab_consulta.getColumna("valor_llantas").setNombreVisual("Alerta de LLantas");
		tab_consulta.getColumna("valor_llantas").setVisible(false);
		tab_consulta.getColumna("alerta_aceite");
		tab_consulta.getColumna("alerta_aceite").setLongitud(25);
		tab_consulta.getColumna("alerta_aceite").setNombreVisual("Alerta Cambio de Aciete");
//		tab_consulta.getColumna("alerta_aceite").setEstilo(pintarCampos());
		tab_consulta.getColumna("alerta_abc");
		tab_consulta.getColumna("alerta_abc").setLongitud(25);
		tab_consulta.getColumna("alerta_abc").setNombreVisual("Alerta de A.B.C.");
		tab_consulta.getColumna("alerta_llantas");
		tab_consulta.getColumna("alerta_llantas").setLongitud(25);
		tab_consulta.getColumna("alerta_llantas").setNombreVisual("Alerta de LLantas");
		tab_consulta.setLectura(true);
		//tab_consulta.setTipoSeleccion(true);
		tab_consulta.setNumeroTabla(1);
		
		tab_consulta.dibujar();
		tab_consulta.setRows(20);

		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_consulta);
		
		Division div_tabla = new Division();
		div_tabla.dividir1(pat_panel);
		agregarComponente(div_tabla);
		
		sec_rango.setId("sec_rango");
		sec_rango.setTitle("Seleccione un Rango de Fechas");

		agregarComponente(sec_rango);
		// TODO Auto-generated constructor stub
		
//		pintarCampos();
	}

	public void seleccionoAutocompletar(SelectEvent evt){
		aut_vehiculo.onSelect(evt);
		utilitario.addUpdate("aut_factura");
		System.out.println("aut vehiculo "+aut_vehiculo.getValor());
		tab_consulta.setSql(ser_transporte.getVehiculosMantenimiento("1",aut_vehiculo.getValor(),"",""));
		tab_consulta.ejecutarSql();
		tab_consulta.dibujar();
	}
	
	public void consultar(){
		if (!sec_rango.isVisible()){
			sec_rango.setFecha1(null);
			sec_rango.setFecha2(null);
			str_fecha1="";
			str_fecha2="";
			sec_rango.getBot_aceptar().setMetodo("consultar");
			sec_rango.dibujar();
			utilitario.addUpdate("sec_rango");
		}else{
			if (!sec_rango.isFechasValidas()){
				str_fecha1="";
				str_fecha2="";
				utilitario.agregarMensajeInfo("No se puede consultar", "fechas incorrectas");
				return;
			}
			str_fecha1=sec_rango.getFecha1String();
			str_fecha2=sec_rango.getFecha2String();
			tab_consulta.setSql(ser_transporte.getVehiculosMantenimiento("2", "", str_fecha1, str_fecha2));
			tab_consulta.ejecutarSql();
			utilitario.addUpdate("tab_consulta");

			sec_rango.cerrar();
			
			if (tab_consulta.getTotalFilas()==0){
				utilitario.agregarMensajeInfo("No existen transacciones registradas ", "");
			}
		}
	}

	public String pintarCampos(){
		
		String color="";
		String alertaAceite="";
		String alertaAbc="";
		String alertaLlantas="";
		String alertaFechaPago="";
		
		set_pantalla_sucursal.setId("set_pantalla_sucursal");
		set_pantalla_sucursal.setSeleccionTabla(ser_transporte.getVehiculosMantenimiento("0", "","",""),"");
		set_pantalla_sucursal.getTab_seleccion().getColumna("alerta_aceite");
//		set_pantalla_sucursal.getTab_seleccion().getColumna("alerta_aceite").setEstilo(alertaAceite());
//		set_pantalla_sucursal.getTab_seleccion().getColumna("alerta_abc").setEstilo(alertaAceite());;
//		set_pantalla_sucursal.getTab_seleccion().getColumna("alerta_llantas").setEstilo(alertaAceite());;
		set_pantalla_sucursal.getTab_seleccion().ejecutarSql();
		
		alertaAceite=set_pantalla_sucursal.getTab_seleccion().getStringColumna("alerta_aceite");
		
		for (int i=0; i<alertaAceite.length(); i++ ){
			
			if (alertaAceite!=null){
				color=ser_transporte.getAlertaMantenimiento("alertaAceite");
				tab_consulta.getColumna("alerta_aceite").setEstilo("color");
			}
		}
//		
//		alertaAceite=tab_consulta.getValor("alerta_aceite");
//		System.out.println("la alerta es"+alertaAceite);
//		color=ser_transporte.getAlertaMantenimiento("alertaAceite");
		return color;
//		
	}
	public String alertaAceite(){
		String color="";
		String alertaAceite="";
		String alertaAbc="";
		String alertaLlantas="";
		String alertaFechaPago="";
//		tab_consulta.setValor("alertaAceite", tab_consulta.getValor("valor_aceite"));
		alertaAceite=tab_consulta.getValor("valor_aceite");
		color=ser_transporte.getAlertaMantenimiento("alertaAceite");
		System.out.println("el color es "+ color);
//		 color="font-size:13px;font-weight: bold;text-decoration: underline;color:green";
		return color;
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

	public Combo getCom_rubros() {
		return com_rubros;
	}

	public void setCom_rubros(Combo com_rubros) {
		this.com_rubros = com_rubros;
	}

	public SeleccionCalendario getSec_rango() {
		return sec_rango;
	}

	public void setSec_rango(SeleccionCalendario sec_rango) {
		this.sec_rango = sec_rango;
	}

	public Check getChe_todos_emp() {
		return che_todos_emp;
	}

	public void setChe_todos_emp(Check che_todos_emp) {
		this.che_todos_emp = che_todos_emp;
	}

	public SeleccionTabla getSet_det_tip_nomina() {
		return set_det_tip_nomina;
	}

	public void setSet_det_tip_nomina(SeleccionTabla set_det_tip_nomina) {
		this.set_det_tip_nomina = set_det_tip_nomina;
	}

	public SeleccionTabla getSet_pantalla_sucursal() {
		return set_pantalla_sucursal;
	}

	public void setSet_pantalla_sucursal(SeleccionTabla set_pantalla_sucursal) {
		this.set_pantalla_sucursal = set_pantalla_sucursal;
	}

	
}
