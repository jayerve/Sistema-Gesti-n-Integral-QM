
package paq_comercializacion;

import javax.ejb.EJB;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_comercializacion.ejb.ServicioClientes;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;

public class pre_clientes_gestores extends Pantalla{
	
	private Tabla tab_gestores = new Tabla();
	private Tabla tab_clientes_g = new Tabla();
	
	private SeleccionTabla set_pantallacliente = new SeleccionTabla();

	@EJB
	private ServicioNomina ser_empleado = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion) utilitario.instanciarEJB(ServicioFacturacion.class);
	@EJB
	private ServicioClientes ser_cliente = (ServicioClientes) utilitario.instanciarEJB(ServicioClientes.class);
	
	
	public pre_clientes_gestores()
	{

		// BOTON AGREGAR CLIENTE
		Boton bot_agregarCliente = new Boton();
		bot_agregarCliente.setValue("Asignar Clientes");
		bot_agregarCliente.setIcon("ui-icon-person");
		bot_agregarCliente.setMetodo("agregarCliente");
		bar_botones.agregarBoton(bot_agregarCliente);
		
		// PANTALLA SELECIONAR CLIENTE
		set_pantallacliente.setId("set_pantallacliente");
		set_pantallacliente.setTitle("SELECCIONE CLIENTES");
		set_pantallacliente.getBot_aceptar().setMetodo("aceptarCliente");
		set_pantallacliente.setSeleccionTabla(ser_facturacion.getClientesActivos("0,1"), "ide_recli");
		set_pantallacliente.getTab_seleccion().getColumna("ruc_comercial_recli").setFiltro(true);
		set_pantallacliente.getTab_seleccion().getColumna("razon_social_recli").setFiltroContenido();
		set_pantallacliente.getTab_seleccion().getColumna("ruc_comercial_recli").setLongitud(25);
		set_pantallacliente.getTab_seleccion().getColumna("razon_social_recli").setLongitud(40);
		set_pantallacliente.getTab_seleccion().getColumna("numero_contrato").setLongitud(10);
		set_pantallacliente.getTab_seleccion().getColumna("establecimiento_operativo_recli").setLongitud(20);
		set_pantallacliente.getTab_seleccion().getColumna("telefono_factura_recli").setLongitud(20);
		set_pantallacliente.getTab_seleccion().getColumna("email_recle").setLongitud(20);
		set_pantallacliente.getTab_seleccion().ejecutarSql();
		agregarComponente(set_pantallacliente);
		
		tab_gestores.setId("tab_gestores");
		tab_gestores.setTabla("rec_gestor_comercial","ide_regec",1);
		tab_gestores.setTipoFormulario(true);
//		tab_gestores.getGrid().setColumns(4);
		tab_gestores.getColumna("ide_gtemp").setCombo(ser_empleado.servicioEmpleadosActivos("true,false"));
		tab_gestores.getColumna("ide_gtemp").setAutoCompletar();
		tab_gestores.getColumna("ide_corr").setCombo("select ide_corr,correo_corr from sis_correo where activo_corr=true");
		tab_gestores.getColumna("ide_corr").setAutoCompletar();
		tab_gestores.getColumna("activo_regec").setValorDefecto("true");
		//tab_gestores.setRows(20);
		tab_gestores.dibujar();
		
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_gestores);
		
		
		tab_clientes_g.setId("tab_clientes_g");
		tab_clientes_g.setSql(ser_cliente.getClientesGestores("-1"));

		tab_clientes_g.setRows(20);
		tab_clientes_g.setLectura(true);
		tab_clientes_g.dibujar();
		//tab_clientes_g.setLectura(false);
		
		PanelTabla pat_panel2 = new PanelTabla();
		pat_panel2.setPanelTabla(tab_clientes_g);
		
		Division div_tabla = new Division();
		div_tabla.dividir2(pat_panel,pat_panel2,"40%","H");
		agregarComponente(div_tabla);
		
		verClientes();
		
	}

	public void agregarCliente() {

		if (tab_gestores.getTotalFilas()>0) {	
			if (tab_gestores.getValor("ide_regec") != null) {
				set_pantallacliente.getTab_seleccion().setSql(ser_facturacion.getClientesActivos("0,1"));
				set_pantallacliente.getTab_seleccion().ejecutarSql();
				set_pantallacliente.dibujar();		
			}
		}
		else
			utilitario.agregarMensaje("Seleccione un GESTOR", "");
	}

	public void aceptarCliente() {

		String str_seleccionados = set_pantallacliente.getSeleccionados();
		System.out.println("Entrar al aceptar" + str_seleccionados);
		if (str_seleccionados != null) {
			
			String str_update_gestor="update rec_clientes set ide_regec="+tab_gestores.getValor("ide_regec")+" where ide_recli in ("+str_seleccionados+");";
			utilitario.getConexion().ejecutarSql(str_update_gestor);
			System.out.println("actualiza gestor: "+str_update_gestor);
			verClientes();
			set_pantallacliente.cerrar();
			utilitario.addUpdate("tab_gestores,tab_clientes_g");
		} else {
			utilitario.agregarMensajeInfo("Debe seleccionar almenos un registro", "");
		}
	}
	
	private void verClientes()
	{
		tab_clientes_g.setSql(ser_cliente.getClientesGestores(pckUtilidades.CConversion.CInt(tab_gestores.getValor("ide_regec"))+""));
		//tab_clientes_g.setCondicion("ide_regec="+pckUtilidades.CConversion.CInt(tab_gestores.getValor("ide_regec")));
		//tab_clientes_g.imprimirSql();
		tab_clientes_g.ejecutarSql();
	}
	
	@Override
	public void inicio() {
		// TODO Auto-generated method stub
		super.inicio();
		verClientes();
	}

	@Override
	public void siguiente() {
		// TODO Auto-generated method stub
		super.siguiente();
		verClientes();
	}

	@Override
	public void fin() {
		// TODO Auto-generated method stub
		super.fin();
		verClientes();
	}

	@Override
	public void atras() {
		// TODO Auto-generated method stub
		super.atras();
		verClientes();
	}
	
	@Override
	public void actualizar(){
		super.actualizar();
		verClientes();
	}
	
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if(tab_gestores.isFocus()){
			tab_gestores.insertar();
			verClientes();
		}
		else
		{
			utilitario.agregarMensajeInfo("No puede insertar", "Debe seleccionar un Gestor Comercial");
		}

	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if (tab_gestores.guardar()) {
			tab_clientes_g.guardar();
			guardarPantalla();
			verClientes();
		}
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub

	}

	public Tabla getTab_gestores() {
		return tab_gestores;
	}

	public void setTab_gestores(Tabla tab_gestores) {
		this.tab_gestores = tab_gestores;
	}

	public Tabla getTab_clientes_g() {
		return tab_clientes_g;
	}

	public void setTab_clientes_g(Tabla tab_clientes_g) {
		this.tab_clientes_g = tab_clientes_g;
	}

	public SeleccionTabla getSet_pantallacliente() {
		return set_pantallacliente;
	}

	public void setSet_pantallacliente(SeleccionTabla set_pantallacliente) {
		this.set_pantallacliente = set_pantallacliente;
	}



}