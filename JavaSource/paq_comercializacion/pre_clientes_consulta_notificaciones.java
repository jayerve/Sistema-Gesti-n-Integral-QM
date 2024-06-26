
package paq_comercializacion;

import javax.ejb.EJB;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_comercializacion.ejb.ServicioClientes;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_sistema.aplicacion.Pantalla;

public class pre_clientes_consulta_notificaciones extends Pantalla{
	
	private Tabla tab_cliente_notificaciones = new Tabla();
	private AutoCompletar aut_cliente = new AutoCompletar();
	private Combo com_gestores_c=new Combo();

	@EJB
	private ServicioClientes ser_cliente = (ServicioClientes) utilitario.instanciarEJB(ServicioClientes.class);
	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion ) utilitario.instanciarEJB(ServicioFacturacion.class);
	
	public pre_clientes_consulta_notificaciones(){
		
		bar_botones.limpiar();
		
		com_gestores_c.setId("com_gestores_c");
		com_gestores_c.setCombo("SELECT ide_regec,nombre_regec FROM rec_gestor_comercial");
		com_gestores_c.setMetodo("seleccionaOpcion");
		com_gestores_c.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Gestor:"));
		bar_botones.agregarComponente(com_gestores_c);
		
		Boton bot_filtrar = new Boton();
		bot_filtrar.setValue("Actualizar Consulta");
		bot_filtrar.setMetodo("seleccionaOpcion");
		bot_filtrar.setIcon("ui-icon-refresh");

		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");	
		
		aut_cliente.setId("aut_cliente");
		aut_cliente.setAutoCompletar(ser_facturacion.getDatosBasicosClientes("0,1"));
		aut_cliente.setMetodoChange("seleccionaOpcion");
		Etiqueta eti_colaborador=new Etiqueta("CLIENTE:");
		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_cliente);
		bar_botones.agregarBoton(bot_filtrar);
		bar_botones.agregarBoton(bot_limpiar);
		
		tab_cliente_notificaciones.setId("tab_cliente_notificaciones");
		tab_cliente_notificaciones.setSql(ser_cliente.getClientesNotificados("-1","0"));
		tab_cliente_notificaciones.getColumna("nombre_regec").setFiltroContenido();
		tab_cliente_notificaciones.getColumna("ruc_comercial_recli").setFiltroContenido();

		tab_cliente_notificaciones.setLectura(true);
		tab_cliente_notificaciones.setRows(20);
		tab_cliente_notificaciones.dibujar();

		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_cliente_notificaciones);
		
		Division div_tabla = new Division();
		div_tabla.dividir1(pat_panel);
		agregarComponente(div_tabla);
		
		//utilitario.agregarNotificacionInfo("Importante.","Cualquier cambio no se olvide de sincronizar los clientes al sistema de RUTAS, para que AVCORP los pueda ver.");
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
	
	public void limpiar()
	{
		aut_cliente.limpiar();
		//com_gestores_c.limpiar();
		tab_cliente_notificaciones.setSql(ser_cliente.getClientesNotificados("-1","0"));
		tab_cliente_notificaciones.ejecutarSql();
		utilitario.addUpdate("tab_cliente_notificaciones,aut_cliente");
	}

	public void seleccionaOpcion (){
		tab_cliente_notificaciones.limpiar();
		tab_cliente_notificaciones.setSql(ser_cliente.getClientesNotificados(pckUtilidades.CConversion.CStr(com_gestores_c.getValue()),pckUtilidades.CConversion.CStr(aut_cliente.getValor())));
		tab_cliente_notificaciones.ejecutarSql();
		tab_cliente_notificaciones.dibujar();
		utilitario.addUpdate("tab_cliente_notificaciones");
		//utilitario.agregarNotificacionInfo("Importante.","Cualquier cambio no se olvide de sincronizar los clientes al sistema de RUTAS, para que AVCORP los pueda ver.");
	}

	public Tabla getTab_cliente_notificaciones() {
		return tab_cliente_notificaciones;
	}

	public void setTab_cliente_notificaciones(Tabla tab_cliente_notificaciones) {
		this.tab_cliente_notificaciones = tab_cliente_notificaciones;
	}

	public AutoCompletar getAut_cliente() {
		return aut_cliente;
	}

	public void setAut_cliente(AutoCompletar aut_cliente) {
		this.aut_cliente = aut_cliente;
	}

	public Combo getCom_gestores_c() {
		return com_gestores_c;
	}

	public void setCom_gestores_c(Combo com_gestores_c) {
		this.com_gestores_c = com_gestores_c;
	}




}