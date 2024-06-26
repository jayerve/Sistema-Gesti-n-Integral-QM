package paq_juridico;


import javax.ejb.EJB;

import org.primefaces.event.SelectEvent;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_sistema.aplicacion.Pantalla;


public class pre_cliente_contacto extends Pantalla{
	
	private Tabla tab_clientes=new Tabla();
	private Tabla tab_email=new Tabla();
	//private Tabla tab_documento=new Tabla();
	private Tabla tab_direccion=new Tabla();
	private Tabla tab_telefono=new Tabla();
	private Tabla tab_establecimiento = new Tabla();
	private AutoCompletar aut_cliente=new AutoCompletar();

	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion ) utilitario.instanciarEJB(ServicioFacturacion.class);


	public pre_cliente_contacto(){

		Boton bot_limpiar=new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		aut_cliente.setId("aut_cliente");
		aut_cliente.setAutoCompletar(ser_facturacion.getClientesMatrices("0"));
		aut_cliente.setFiltroContenido(true);
		aut_cliente.setMetodoChange("seleccionoAutocompletar"); //ejecuta el metodo seleccionoAutocompletar

		Etiqueta eti_colaborador=new Etiqueta("CLIENTE:");
		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_cliente);
		bar_botones.agregarBoton(bot_limpiar);
		
		Tabulador tab_tabulador = new Tabulador();
		tab_tabulador.setId("tab_tabulador");
		
		tab_clientes.setId("tab_clientes");
		tab_clientes.setTipoFormulario(true);  //formulario 
		tab_clientes.getGrid().setColumns(6); //hacer  columnas 
		tab_clientes.setSql(ser_facturacion.getClientesMatrices("-1"));
		//tab_clientes.setClaveCompuesta("ide_recli");
		tab_clientes.setLectura(true);
		tab_clientes.dibujar();
		
		PanelTabla pat_clientes=new PanelTabla ();
		pat_clientes.setPanelTabla(tab_clientes);
				
		tab_telefono.setId("tab_telefono");
		tab_telefono.setIdCompleto("tab_tabulador:tab_telefono");
		tab_telefono.setTabla("rec_cliente_telefono", "ide_reclt", 2);
		tab_telefono.getColumna("ide_reclt").setOrden(1);
		tab_telefono.getColumna("ide_retco").setOrden(2);
		tab_telefono.getColumna("ide_retco").setCombo("rec_tipo_contacto", "ide_retco", "detalle_retco", "activo_retco=true");
		tab_telefono.getColumna("nombre_contacto_reclt").setOrden(3);
		tab_telefono.getColumna("cargo_contacto_reclt").setOrden(4);	
		tab_telefono.getColumna("ide_reteo").setOrden(5);
		tab_telefono.getColumna("ide_reteo").setCombo("rec_telefono_operadora", "ide_reteo", "detalle_reteo", "");
		tab_telefono.getColumna("telefono_reclt").setOrden(6);
		tab_telefono.getColumna("email_reclt").setOrden(7);
		tab_telefono.getColumna("notificacion_reclt").setOrden(8);
		tab_telefono.getColumna("domicilio_reclt").setOrden(9);
		tab_telefono.getColumna("activo_reclt").setOrden(10);		
		tab_telefono.getColumna("activo_reclt").setValorDefecto("true");	
		tab_telefono.getColumna("ide_recli").setVisible(false);	
		
		tab_telefono.setCondicion("ide_recli=-1");
		tab_telefono.setTipoFormulario(true);
		tab_telefono.getGrid().setColumns(4);		
		tab_telefono.dibujar();
		
		PanelTabla pat_contactos=new PanelTabla();
		pat_contactos.setPanelTabla(tab_telefono);
		
		tab_email.setId("tab_email");
		tab_email.setIdCompleto("tab_tabulador:tab_email");
		tab_email.setTabla("rec_cliente_email","ide_recle",3);
		tab_email.setCondicion("ide_recli=-1");
		tab_email.getColumna("ide_recli").setVisible(false);
		tab_email.getColumna("notificacion_recle").setValorDefecto("true");
		tab_email.getColumna("activo_recle").setValorDefecto("true");
		//tab_email.setTipoFormulario(true);
		//tab_email.getGrid().setColumns(4);	
		//tab_email.setLectura(true);
		tab_email.dibujar();
		
		PanelTabla pat_email = new PanelTabla();
		pat_email.setPanelTabla(tab_email);
		
		tab_direccion.setId("tab_direccion");
		tab_direccion.setIdCompleto("tab_tabulador:tab_direccion");
		tab_direccion.setTabla("rec_cliente_direccion","ide_recld",4);
		tab_direccion.setCondicion("ide_recli=-1");
		tab_direccion.getColumna("ide_recli").setVisible(false);
		tab_direccion.getColumna("direccion_recld").setNombreVisual("DIRECCIÓN COMPLETA");
		tab_direccion.getColumna("notificacion_recld").setValorDefecto("true");
		tab_direccion.getColumna("activo_recld").setValorDefecto("true");
		tab_direccion.dibujar();
		
		PanelTabla pat_direccion = new PanelTabla();
		pat_direccion.setPanelTabla(tab_direccion);
		
		detalleEstablecimiento();
		PanelTabla pat_establecimiento = new PanelTabla();
		pat_establecimiento.setPanelTabla(tab_establecimiento);
		
		tab_tabulador.agregarTab("CONTACTOS", pat_contactos);
		tab_tabulador.agregarTab("DIRECCION ESTABLECIMIENTO", pat_direccion);
		tab_tabulador.agregarTab("EMAIL", pat_email);
		tab_tabulador.agregarTab("ESTABLECIMIENTOS", pat_establecimiento);
		
		Division div_division=new Division();
		div_division.setId("div_division");
		div_division.dividir2(pat_clientes,tab_tabulador,"50%","H");
		agregarComponente(div_division);

		utilitario.agregarNotificacionInfo("AVISO","Se recomienda actualizar los establecimientos que se encuentren activos (true) o del mas reciente...");
		
	}


	public void limpiar(){
		aut_cliente.limpiar();
		tab_clientes.limpiar();
		tab_email.limpiar();
		tab_telefono.limpiar();
		tab_direccion.limpiar();
		tab_establecimiento.limpiar();
		utilitario.addUpdate("aut_cliente,tab_clientes,tab_direccion,tab_email,tab_telefono,tab_establecimiento");
	}

	//METDO AUTOCOMPLETAR
	public void seleccionoAutocompletar(SelectEvent evt){
		//Cuando selecciona una opcion del autocompletar siempre debe hacerse el onSelect(evt)
		aut_cliente.onSelect(evt);
		tab_clientes.setSql(ser_facturacion.getClientesMatrices(aut_cliente.getValor()));
		tab_clientes.ejecutarSql();

		tab_direccion.setCondicion("ide_recli="+aut_cliente.getValor());
		tab_direccion.ejecutarSql();
		tab_email.setCondicion("ide_recli="+aut_cliente.getValor());
		tab_email.ejecutarSql();
		tab_telefono.setCondicion("ide_recli="+aut_cliente.getValor());
		tab_telefono.ejecutarSql();
		tab_establecimiento.setCondicion("ide_recli="+aut_cliente.getValor());
		tab_establecimiento.ejecutarSql();
		
		utilitario.addUpdate("tab_clientes,tab_direccion,tab_email,tab_telefono,tab_establecimiento");
	}

	private void detalleEstablecimiento() 
	{
		tab_establecimiento.setId("tab_establecimiento");
		tab_establecimiento.setIdCompleto("tab_tabulador:tab_establecimiento");
		//tab_establecimiento.setTipoFormulario(true);
		//tab_establecimiento.getGrid().setColumns(4);
		tab_establecimiento.setTabla("rec_clientes_establecimiento", "ide_reest", 5);
		//tab_establecimiento.setCampoForanea("ide_recli");
		tab_establecimiento.getColumna("ide_reest").setNombreVisual("CODIGO");
		tab_establecimiento.getColumna("ide_reest").setVisible(true);
		tab_establecimiento.getColumna("activo_reest").setNombreVisual("ACTIVO");
		tab_establecimiento.getColumna("activo_reest").setValorDefecto("true");
		tab_establecimiento.getColumna("activo_reest").setVisible(true);

		tab_establecimiento.getColumna("establecimiento_operativo_reest").setNombreVisual("ESTABLECIMIENTO OPERATIVO");
		tab_establecimiento.getColumna("establecimiento_operativo_reest").setVisible(true);
		tab_establecimiento.getColumna("establecimiento_operativo_reest").setLongitud(75);
		tab_establecimiento.getColumna("nro_establecimiento_reest").setNombreVisual("Nro. ESTABLECIMIENTO");
		tab_establecimiento.getColumna("nro_establecimiento_reest").setVisible(true);
		tab_establecimiento.getColumna("codigo_zona_reest").setNombreVisual("CÓDIGO DE ZONA");
		tab_establecimiento.getColumna("codigo_zona_reest").setVisible(true);
		tab_establecimiento.getColumna("direccion_reest").setNombreVisual("DIRECCIÓN DE ESTABLECIMIENTO");
		tab_establecimiento.getColumna("direccion_reest").setVisible(true);
		tab_establecimiento.getColumna("direccion_operativa_reest").setNombreVisual("DIRECCIÓN OPERATIVA");
		tab_establecimiento.getColumna("referencia_reest").setNombreVisual("REFERENCIA");
		tab_establecimiento.getColumna("referencia_reest").setVisible(true);
		tab_establecimiento.getColumna("email_reest").setNombreVisual("E-MAIL");
		tab_establecimiento.getColumna("email_reest").setVisible(true);
		tab_establecimiento.getColumna("telefono_reest").setNombreVisual("TELÉFONO");
		tab_establecimiento.getColumna("telefono_reest").setVisible(true);

		tab_establecimiento.getColumna("ide_recli").setVisible(false);
		tab_establecimiento.getColumna("ide_retia").setCombo("rec_tipo_asistencia", "ide_retia", "detalle_retia", "");
		tab_establecimiento.getColumna("ide_retia").setVisible(true);
		tab_establecimiento.getColumna("ide_retia").setNombreVisual("TIPO DE FRECUENCIA");
		tab_establecimiento.getColumna("ide_reclr").setCombo("rec_cliente_ruta", "ide_reclr", "detalle_reclr", "");
		tab_establecimiento.getColumna("ide_reclr").setVisible(true);
		tab_establecimiento.getColumna("ide_reclr").setNombreVisual("RUTA");
		
		tab_establecimiento.getColumna("codigo_zona_reest").setVisible(false);
		tab_establecimiento.getColumna("coordx_reest").setVisible(false);
		tab_establecimiento.getColumna("coordy_reest").setVisible(false);
		tab_establecimiento.getColumna("direccion_operativa_reest").setVisible(false);
		tab_establecimiento.getColumna("telefono_reest").setVisible(false);
		//tab_establecimiento.getColumna("email_reest").setVisible(false);

		tab_establecimiento.setCondicion("ide_recli=-1");
		tab_establecimiento.setCampoOrden("nro_establecimiento_reest");
		tab_establecimiento.setLectura(true);
		tab_establecimiento.dibujar();
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if(aut_cliente.getValor()!=null){
			if(tab_telefono.isFocus()){
				tab_telefono.getColumna("ide_recli").setValorDefecto(aut_cliente.getValor());
				tab_telefono.insertar();
			}
			else if(tab_direccion.isFocus()){
				tab_direccion.getColumna("ide_recli").setValorDefecto(aut_cliente.getValor());
				tab_direccion.insertar();
			}
			else if(tab_email.isFocus()){
				tab_email.getColumna("ide_recli").setValorDefecto(aut_cliente.getValor());
				tab_email.insertar();
			}
		}

	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_telefono.guardar();
		tab_direccion.guardar();
		tab_email.guardar();
		guardarPantalla();
			
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}


	public Tabla getTab_telefono() {
		return tab_telefono;
	}


	public void setTab_telefono(Tabla tab_telefono) {
		this.tab_telefono = tab_telefono;
	}

	

	public Tabla getTab_clientes() {
		return tab_clientes;
	}


	public void setTab_clientes(Tabla tab_clientes) {
		this.tab_clientes = tab_clientes;
	}


	public Tabla getTab_email() {
		return tab_email;
	}


	public void setTab_email(Tabla tab_email) {
		this.tab_email = tab_email;
	}


	public AutoCompletar getAut_cliente() {
		return aut_cliente;
	}

	public void setAut_cliente(AutoCompletar aut_cliente) {
		this.aut_cliente = aut_cliente;
	}


	public Tabla getTab_establecimiento() {
		return tab_establecimiento;
	}


	public void setTab_establecimiento(Tabla tab_establecimiento) {
		this.tab_establecimiento = tab_establecimiento;
	}


	public Tabla getTab_direccion() {
		return tab_direccion;
	}


	public void setTab_direccion(Tabla tab_direccion) {
		this.tab_direccion = tab_direccion;
	}


}
