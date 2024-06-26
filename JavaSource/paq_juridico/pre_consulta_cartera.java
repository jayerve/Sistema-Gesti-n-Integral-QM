package paq_juridico;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;

import org.primefaces.event.SelectEvent;

import framework.aplicacion.Fila;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.BotonesCombo;
import framework.componentes.Calendario;
import framework.componentes.Check;
import framework.componentes.Confirmar;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.ItemMenu;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import framework.componentes.VisualizarPDF;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_juridico.ejb.ServicioJuridico;
import paq_sistema.aplicacion.Pantalla;
import paq_tesoreria.ejb.ServicioTesoreria;

public class pre_consulta_cartera extends Pantalla{
	
	private Tabla tab_recaudacion = new Tabla();
	private AutoCompletar aut_cliente = new AutoCompletar();
	private Check che_todos_emp=new Check();

	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion ) utilitario.instanciarEJB(ServicioFacturacion.class);
	@EJB
	private ServicioJuridico ser_juridico = (ServicioJuridico ) utilitario.instanciarEJB(ServicioJuridico.class);

	public pre_consulta_cartera(){

		bar_botones.limpiar();
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		
		aut_cliente.setId("aut_cliente");
		aut_cliente.setAutoCompletar(ser_facturacion.getDatosBasicosClientes("0,1"));
		aut_cliente.setMetodoChange("seleccionoAutocompletar");
		Etiqueta eti_colaborador=new Etiqueta("CLIENTE:");
		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_cliente);
		bar_botones.agregarBoton(bot_limpiar);
		
		che_todos_emp.setId("che_todos_emp");
		Etiqueta eti_todos_emp=new Etiqueta("Todos los Clientes");
		bar_botones.agregarComponente(eti_todos_emp);
		bar_botones.agregarComponente(che_todos_emp);
		
		
		Boton bot_actualiza = new Boton();
		bot_actualiza.setId("bot_actualiza");
		bot_actualiza.setTitle("ACTUALIZAR");
		bot_actualiza.setValue("ACTUALIZAR");
		bot_actualiza.setMetodo("cargaCliente");
		bar_botones.agregarBoton(bot_actualiza);

		tab_recaudacion.setId("tab_recaudacion");
		tab_recaudacion.setSql(ser_juridico.getCarteraClientes("-1", "0"));
		tab_recaudacion.getColumna("ide_fafac").setVisible(false);
		tab_recaudacion.getColumna("ide_recli").setVisible(false);
		tab_recaudacion.getColumna("grupo").setVisible(false);
		tab_recaudacion.getColumna("ruc_comercial_recli").setLongitud(30);
		tab_recaudacion.getColumna("secuencial_fafac").setLongitud(30);
		tab_recaudacion.setColumnaSuma("total_fafac,interes");
		tab_recaudacion.setRows(25);
		tab_recaudacion.setLectura(true);
		tab_recaudacion.dibujar();
		
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_recaudacion);
		
		Division div_recaudacion = new Division();
		div_recaudacion.setId("div_recaudacion");
		div_recaudacion.dividir1(pat_panel);
		
		agregarComponente(div_recaudacion);
		

	}
	
	public void seleccionoAutocompletar(SelectEvent evt){
		//Cuando selecciona una opcion del autocompletar siempre debe hacerse el onSelect(evt)
		aut_cliente.onSelect(evt);
		che_todos_emp.setValue("false");
		utilitario.addUpdate("che_todos_emp");
	}
	
	public void cargaCliente(){
		
		if(che_todos_emp.getValue().toString().equalsIgnoreCase("true")){
			aut_cliente.limpiar();
			tab_recaudacion.limpiar();
			tab_recaudacion.setSql(ser_juridico.getCarteraClientes("-1", "1"));
			tab_recaudacion.getColumna("ruc_comercial_recli").setVisible(true);
			tab_recaudacion.getColumna("razon_social_recli").setVisible(true);
			tab_recaudacion.getColumna("detalle_bogrm").setVisible(false);
			tab_recaudacion.getColumna("secuencial_fafac").setVisible(false);
			tab_recaudacion.getColumna("fecha_vencimiento_fafac").setVisible(false);
			tab_recaudacion.getColumna("establecimiento").setVisible(false);
			
			tab_recaudacion.dibujar();
			tab_recaudacion.ejecutarSql();
			utilitario.addUpdate("tab_recaudacion,aut_cliente");
		}
		else{
			tab_recaudacion.limpiar();
			tab_recaudacion.setSql(ser_juridico.getCarteraClientes(aut_cliente.getValor(), "0"));
			tab_recaudacion.getColumna("ruc_comercial_recli").setVisible(false);
			tab_recaudacion.getColumna("razon_social_recli").setVisible(false);
			tab_recaudacion.getColumna("detalle_bogrm").setVisible(true);
			tab_recaudacion.getColumna("secuencial_fafac").setVisible(true);
			tab_recaudacion.getColumna("fecha_vencimiento_fafac").setVisible(true);
			tab_recaudacion.getColumna("establecimiento").setVisible(true);
			
			tab_recaudacion.dibujar();
			tab_recaudacion.ejecutarSql();
			utilitario.addUpdate("tab_recaudacion");
		}
	}
	
	public void limpiar(){
		aut_cliente.limpiar();
		tab_recaudacion.limpiar();
		
		tab_recaudacion.setSql(ser_juridico.getCarteraClientes("-1", "0"));
		tab_recaudacion.ejecutarSql();

		utilitario.addUpdate("tab_recaudacion,aut_cliente");
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

	public Tabla getTab_recaudacion() {
		return tab_recaudacion;
	}

	public void setTab_recaudacion(Tabla tab_recaudacion) {
		this.tab_recaudacion = tab_recaudacion;
	}

	public AutoCompletar getAut_cliente() {
		return aut_cliente;
	}

	public void setAut_cliente(AutoCompletar aut_cliente) {
		this.aut_cliente = aut_cliente;
	}

	

}
