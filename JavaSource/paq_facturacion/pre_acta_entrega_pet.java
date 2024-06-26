package paq_facturacion;


import javax.ejb.EJB;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_sistema.aplicacion.Pantalla;

public class pre_acta_entrega_pet extends Pantalla{

	private Tabla tab_acta_entrega_pet=new Tabla();
	private SeleccionTabla set_factura = new SeleccionTabla();
	
	public static String p_modulo_facturacion;

	@EJB
	private ServicioFacturacion ser_Facturacion = (ServicioFacturacion) utilitario.instanciarEJB(ServicioFacturacion.class);
	
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);


	public pre_acta_entrega_pet(){
		

		p_modulo_facturacion=utilitario.getVariable("p_modulo_facturacion");
		
		tab_acta_entrega_pet.setHeader("ACTA ENTREGA RECEPCION PET");
		tab_acta_entrega_pet.setId("tab_acta_entrega_pet");
		tab_acta_entrega_pet.setTabla("fac_acta_entrega_recepcion","ide_acerp", 1);
		tab_acta_entrega_pet.setTipoFormulario(true);
		tab_acta_entrega_pet.getGrid().setColumns(4);
		tab_acta_entrega_pet.getColumna("ide_fafac").setCombo(ser_Facturacion.getCabeceraFactura("2","40"));
		tab_acta_entrega_pet.getColumna("ide_fafac").setAutoCompletar();
		tab_acta_entrega_pet.getColumna("ide_fafac").setLectura(true);
		tab_acta_entrega_pet.getColumna("ide_coest").setCombo(ser_contabilidad.getModuloEstados("true", p_modulo_facturacion));
		tab_acta_entrega_pet.getColumna("ide_coest").setValorDefecto("2");
		tab_acta_entrega_pet.getColumna("ide_recli").setCombo(ser_Facturacion.getClientes("0,1"));
		tab_acta_entrega_pet.getColumna("ide_recli").setAutoCompletar();
		tab_acta_entrega_pet.getColumna("ide_recli").setLectura(true);
		tab_acta_entrega_pet.getColumna("ide_retip").setCombo("rec_tipo", "ide_retip", "detalle_retip", "");
		tab_acta_entrega_pet.getColumna("ide_retip").setValorDefecto(utilitario.getVariable("p_tipo_cobro_factura"));
		tab_acta_entrega_pet.getColumna("ide_retip").setValorDefecto("4");
		tab_acta_entrega_pet.getColumna("fecha_emision_acerp").setValorDefecto(utilitario.getFechaActual());
		tab_acta_entrega_pet.getColumna("cantidad_acerp").setValorDefecto("0");
		tab_acta_entrega_pet.getColumna("peso_bruto_acerp").setValorDefecto("0");
		tab_acta_entrega_pet.getColumna("caracterizacion_acerp").setValorDefecto("0");
		tab_acta_entrega_pet.getColumna("peso_caract_acerp").setValorDefecto("0");
		tab_acta_entrega_pet.getColumna("porctj_exclu_acerp").setValorDefecto("0");
		tab_acta_entrega_pet.getColumna("peso_mat_exclu_acerp").setValorDefecto("0");
		tab_acta_entrega_pet.getColumna("peso_total_acerp").setValorDefecto("0");
		tab_acta_entrega_pet.getColumna("num_botellas_acerp").setValorDefecto("0");
		tab_acta_entrega_pet.getColumna("valor_botella_acerp").setValorDefecto("0");
		tab_acta_entrega_pet.getColumna("precio_total_acerp").setValorDefecto("0");
		
		tab_acta_entrega_pet.getColumna("activo_acerp").setValorDefecto("true");
		
		tab_acta_entrega_pet.dibujar();
		PanelTabla pat_acta_entrega_pet_factura=new PanelTabla();
		pat_acta_entrega_pet_factura.setPanelTabla(tab_acta_entrega_pet);

		agregarComponente(pat_acta_entrega_pet_factura);
		
		Boton bot_factura = new Boton();
		bot_factura.setValue("Agregar Factura");
		bot_factura.setTitle("Factura");
		bot_factura.setIcon("ui-icon-person");
		bot_factura.setMetodo("importarFactura");
		bar_botones.agregarBoton(bot_factura);

		set_factura.setId("set_factura");
		set_factura.setSeleccionTabla(ser_Facturacion.getCabeceraFactura("2","40"),"ide_fafac");
		set_factura.getTab_seleccion().getColumna("secuencial_fafac").setFiltro(true);
		set_factura.getTab_seleccion().getColumna("detalle_bogrm").setFiltro(true);
		set_factura.getBot_aceptar().setMetodo("aceptarFactura");
		set_factura.getTab_seleccion().ejecutarSql();
		set_factura.setRadio();
		agregarComponente(set_factura);
		
	}
	
	public void importarFactura(){
		//set_factura.getTab_seleccion().setSql(ser_Facturacion.getCabeceraFactura("2",aut_factura.getValor()));
		set_factura.getTab_seleccion().ejecutarSql();
		set_factura.dibujar();

	}

	public  void aceptarFactura(){
		String str_seleccionado = set_factura.getValorSeleccionado();
		//System.out.println("entra al str  "+str_seleccionado);
		if (str_seleccionado!=null){
			TablaGenerica tab_aceptarfactura = ser_Facturacion.getTablaGenericaFacturaCabecera(str_seleccionado);		
			//System.out.println(" tabla generica"+tab_aceptarfactura.getSql());
			tab_acta_entrega_pet.insertar();
			tab_acta_entrega_pet.setValor("ide_fafac", tab_aceptarfactura.getValor("ide_fafac"));			
			tab_acta_entrega_pet.setValor("ide_recli", tab_aceptarfactura.getValor("ide_recli"));			
		}
		set_factura.cerrar();
		utilitario.addUpdate("tab_acta_entrega_pet");
	}
	
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_acta_entrega_pet.insertar();

	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_acta_entrega_pet.guardar();
		guardarPantalla();
		

	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_acta_entrega_pet.eliminar();

	}



	public Tabla getTab_acta_entrega_pet() {
		return tab_acta_entrega_pet;
	}



	public void setTab_acta_entrega_pet(Tabla tab_acta_entrega_pet) {
		this.tab_acta_entrega_pet = tab_acta_entrega_pet;
	}



	public SeleccionTabla getSet_factura() {
		return set_factura;
	}



	public void setSet_factura(SeleccionTabla set_factura) {
		this.set_factura = set_factura;
	}



}
