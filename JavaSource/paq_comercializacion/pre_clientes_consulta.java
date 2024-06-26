
package paq_comercializacion;

import javax.ejb.EJB;

import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_comercializacion.ejb.ServicioClientes;
import paq_sistema.aplicacion.Pantalla;

public class pre_clientes_consulta extends Pantalla{
	
	
	private Tabla tab_cliente_consulta = new Tabla();

	@EJB
	private ServicioClientes ser_cliente = (ServicioClientes) utilitario.instanciarEJB(ServicioClientes.class);
	
	
	public pre_clientes_consulta(){
		
		bar_botones.limpiar();
		
		Boton bot_filtrar = new Boton();
		bot_filtrar.setValue("Actualizar Consulta");
		bot_filtrar.setMetodo("seleccionaOpcion");
		bot_filtrar.setIcon("ui-icon-refresh");
		bar_botones.agregarBoton(bot_filtrar);
		
		tab_cliente_consulta.setId("tab_cliente_consulta");
		tab_cliente_consulta.setSql(ser_cliente.getClientesAvCorp());
		tab_cliente_consulta.getColumna("nro_contrato").setFiltroContenido();
		tab_cliente_consulta.getColumna("nro_contrato").setLongitud(30);
		tab_cliente_consulta.getColumna("ruc_comercial_factura").setFiltroContenido();
		tab_cliente_consulta.getColumna("nombre_comercial_factura").setFiltroContenido();
		tab_cliente_consulta.setLectura(true);
		tab_cliente_consulta.setRows(20);
		tab_cliente_consulta.dibujar();
		
		

		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_cliente_consulta);
		
		Division div_tabla = new Division();
		div_tabla.dividir1(pat_panel);
		agregarComponente(div_tabla);
		
		utilitario.agregarNotificacionInfo("Importante.","Cualquier cambio no se olvide de sincronizar los clientes al sistema de RUTAS, para que AVCORP los pueda ver.");
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

	public void seleccionaOpcion (){
		tab_cliente_consulta.limpiar();
		tab_cliente_consulta.ejecutarSql();
		tab_cliente_consulta.dibujar();
		utilitario.addUpdate("tab_cliente_consulta");
		utilitario.agregarNotificacionInfo("Importante.","Cualquier cambio no se olvide de sincronizar los clientes al sistema de RUTAS, para que AVCORP los pueda ver.");
	}

	public Tabla getTab_cliente_consulta() {
		return tab_cliente_consulta;
	}

	public void setTab_cliente_consulta(Tabla tab_cliente_consulta) {
		this.tab_cliente_consulta = tab_cliente_consulta;
	}


}