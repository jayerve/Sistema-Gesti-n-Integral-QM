package paq_contratos;

import javax.ejb.EJB;

import org.primefaces.event.SelectEvent;

import paq_activos.ejb.ServicioActivos;
import paq_contratos.ejb.ServicioContrato;
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

public class pre_consulta_polizas extends Pantalla {
	
	
	@EJB
	private ServicioActivos ser_activos = (ServicioActivos ) utilitario.instanciarEJB(ServicioActivos.class);

	@EJB
	private ServicioContrato ser_contrato = (ServicioContrato) utilitario.instanciarEJB(ServicioContrato.class);

	private AutoCompletar aut_vehiculo = new AutoCompletar();
	private Tabla tab_consulta=new Tabla();
	private Combo com_rubros = new Combo();

	private SeleccionTabla set_pantalla_sucursal= new SeleccionTabla();
	private SeleccionCalendario sec_rango=new SeleccionCalendario();
	private Check che_todos_emp=new Check();
	private SeleccionTabla set_det_tip_nomina=new SeleccionTabla();
	String str_fecha1="";
	String str_fecha2="";

	public pre_consulta_polizas() {
		
		bar_botones.getBot_eliminar().setRendered(false);
		bar_botones.getBot_guardar().setRendered(false);
		bar_botones.getBot_insertar().setRendered(false);
		
		bar_botones.limpiar();
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		aut_vehiculo.setId("aut_vehiculo");
		aut_vehiculo.setAutoCompletar(ser_contrato.getDatosBanco()); 
		aut_vehiculo.setMetodoChange("seleccionoAutocompletar"); //ejecuta el metodo seleccionoAutocompletar
		
		Etiqueta eti_colaborador=new Etiqueta("BANCO:");
		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_vehiculo);
		bar_botones.agregarBoton(bot_limpiar);
		
		// boton consultar
		Boton bot_consultar = new Boton();
		bot_consultar.setIcon("ui-icon-calculator");
		bot_consultar.setMetodo("consultar");
		bot_consultar.setValue("Consultar por fechas de Vencimiento");
		bot_consultar.setTitle("Consultar");
		bar_botones.agregarBoton(bot_consultar);

		Boton bot_filtrar = new Boton();
		bot_filtrar.setValue("Actualizar Consulta");
		bot_filtrar.setMetodo("seleccionaOpcion");
		bot_filtrar.setIcon("ui-icon-refresh");
		bar_botones.agregarBoton(bot_filtrar);

		tab_consulta.setId("tab_consulta");
		tab_consulta.setSql(ser_contrato.getPolizas("0", "","",""));
		tab_consulta.getColumna("ide_tepol");
		tab_consulta.getColumna("ide_tepol").setNombreVisual("Código");
		tab_consulta.getColumna("numero_poliza_tepol");
		tab_consulta.getColumna("numero_poliza_tepol").setNombreVisual("Número Póliza");
		tab_consulta.getColumna("alerta");
		tab_consulta.getColumna("alerta").setNombreVisual("Alerta");
		tab_consulta.getColumna("alerta").setLongitud(30);
		tab_consulta.setNumeroTabla(1);
		tab_consulta.setRows(20);
		tab_consulta.setLectura(true);
		tab_consulta.dibujar();
		

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
		tab_consulta.setSql(ser_contrato.getPolizas("1",aut_vehiculo.getValor(),"",""));
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
			tab_consulta.setSql(ser_contrato.getPolizas("2", "", str_fecha1, str_fecha2));
			tab_consulta.ejecutarSql();
			utilitario.addUpdate("tab_consulta");

			sec_rango.cerrar();
			
			if (tab_consulta.getTotalFilas()==0){
				utilitario.agregarMensajeInfo("No existen transacciones registradas ", "");
			}
		}
	}

	public void seleccionaOpcion (){
		tab_consulta.limpiar();
		aut_vehiculo.limpiar();
		utilitario.addUpdate("aut_factura");
		tab_consulta.setSql(ser_contrato.getPolizas("0", "","",""));
//		tab_consulta.setSql(ser_cliente.getClientesFacturasContratos("1","0", fecha_inicial, fecha_final, che_fecha_pago.getValue().toString(),che_fecha_contrato.getValue().toString()));
//		tab_cliente_consulta.setSql(ser_cliente.getConsultaClientes("1",aut_factura.getValor(), fecha_inicial, fecha_final, che_fecha_pago.getValue().toString()));
//		tab_cliente_consulta.setSql(ser_cliente.getConsultaClientes("1","0", fecha_inicial, fecha_final, che_fecha_pago.getValue().toString()));
		tab_consulta.ejecutarSql();
		tab_consulta.dibujar();
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

	public ServicioActivos getSer_activos() {
		return ser_activos;
	}

	public void setSer_activos(ServicioActivos ser_activos) {
		this.ser_activos = ser_activos;
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

	public SeleccionTabla getSet_pantalla_sucursal() {
		return set_pantalla_sucursal;
	}

	public void setSet_pantalla_sucursal(SeleccionTabla set_pantalla_sucursal) {
		this.set_pantalla_sucursal = set_pantalla_sucursal;
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

	public String getStr_fecha1() {
		return str_fecha1;
	}

	public void setStr_fecha1(String str_fecha1) {
		this.str_fecha1 = str_fecha1;
	}

	public String getStr_fecha2() {
		return str_fecha2;
	}

	public void setStr_fecha2(String str_fecha2) {
		this.str_fecha2 = str_fecha2;
	}

	public ServicioContrato getSer_contrato() {
		return ser_contrato;
	}

	public void setSer_contrato(ServicioContrato ser_contrato) {
		this.ser_contrato = ser_contrato;
	}


	
}
