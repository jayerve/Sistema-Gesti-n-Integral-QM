package paq_bodega;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_gestion.ejb.ServicioGestion;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;

public class pre_egreso_material extends Pantalla {
	private Tabla  tab_concepto_egreso=new Tabla();
	private Tabla  tab_egreso=new Tabla();
	private Combo com_anio=new Combo();
	private Etiqueta eti_total= new Etiqueta();

	private SeleccionTabla set_inventario=new SeleccionTabla();
	private SeleccionTabla set_inventario_saldo = new SeleccionTabla();

	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	@EJB
	private ServicioGestion ser_gestion = (ServicioGestion) utilitario.instanciarEJB(ServicioGestion.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioBodega ser_Bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);


	public pre_egreso_material(){

		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));		
		com_anio.setMetodo("seleccionaElAnio");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);

		tab_concepto_egreso.setId("tab_concepto_egreso");
		tab_concepto_egreso.setHeader("EGRESO DE MATERIALES");
		tab_concepto_egreso.setTabla("bodt_concepto_egreso","ide_bocoe", 1);
		tab_concepto_egreso.getColumna("ide_geani").setVisible(false);
		tab_concepto_egreso.getColumna("ide_adsoc").setVisible(false);
		tab_concepto_egreso.setCondicion("ide_geani = -1");
		tab_concepto_egreso.getColumna("IDE_GETIP").setCombo("gen_tipo_persona","ide_getip","detalle_getip","");
		tab_concepto_egreso.getColumna("IDE_GEARE").setCombo("gen_area","ide_geare", "detalle_geare", "");
		tab_concepto_egreso.getColumna("IDE_BODES").setCombo("bodt_destino","ide_bodes","detalle_bodes", "");
		tab_concepto_egreso.getColumna("IDE_GEEDP").setCombo(ser_nomina.servicioEmpleadoContrato("true,false"));
		tab_concepto_egreso.getColumna("gen_ide_geedp").setCombo(ser_nomina.servicioEmpleadoContrato("true,false"));
		tab_concepto_egreso.getColumna("gen_ide_geedp2").setCombo(ser_nomina.servicioEmpleadoContrato("true,false"));
		tab_concepto_egreso.getColumna("IDE_GEEDP").setAutoCompletar();
		tab_concepto_egreso.getColumna("gen_ide_geedp").setAutoCompletar();
		tab_concepto_egreso.getColumna("gen_ide_geedp2").setAutoCompletar();
		tab_concepto_egreso.agregarRelacion(tab_egreso);
		tab_concepto_egreso.setTipoFormulario(true);
		tab_concepto_egreso.getGrid().setColumns(4);	
		tab_concepto_egreso.dibujar();
		PanelTabla pat_concepto_egreso=new PanelTabla();
		pat_concepto_egreso.setPanelTabla(tab_concepto_egreso);


		tab_egreso.setId("tab_egreso");
		tab_egreso.setHeader("DETALLE EGRESO DE MATERIALES");
		tab_egreso.setTabla("bodt_egreso", "ide_boegr", 2);		
		tab_egreso.getColumna("ide_boinv").setCombo(ser_Bodega.getInventarioMaterial());
		tab_egreso.getColumna("ide_boinv").setLectura(true);
		tab_egreso.getColumna("cantidad_egreso_boegr").setMetodoChange("validarIngreso");
		tab_egreso.getColumna("ide_bobod").setVisible(false);
		tab_egreso.getColumna("costo_egreso_boegr").setVisible(false);
		tab_egreso.getColumna("total_egreso_boegr").setVisible(false);
		tab_egreso.getColumna("existencias_boegr").setVisible(false);
		tab_egreso.getColumna("costo_anterior_boegr").setVisible(false);
		tab_egreso.getColumna("fecha_ingreso_articulo_boegr").setVisible(false);
		tab_egreso.getColumna("fecha_egreso_boegr").setLectura(true);
		tab_egreso.getColumna("documento_egreso_boegr").setLectura(true);
		tab_egreso.getColumna("activo_boegr").setLectura(true);
		tab_egreso.dibujar();
		PanelTabla pat_egreso=new PanelTabla();
		pat_egreso.setPanelTabla(tab_egreso);
		


		Division div_division=new Division();
		div_division.dividir2(pat_concepto_egreso,pat_egreso, "50%", "h");
		agregarComponente(div_division);

		Boton bot_inventario = new Boton();
		bot_inventario.setValue("Buscar Inventario");
		bot_inventario.setTitle("INVENTARIO");
		bot_inventario.setIcon("ui-icon-person");
		bot_inventario.setMetodo("importarInventario");
		bar_botones.agregarBoton(bot_inventario);
		
		set_inventario.setId("set_inventario");
		set_inventario.setSeleccionTabla(ser_contabilidad.getInventario("1","1",""),"ide_boinv");
		set_inventario.getTab_seleccion().getColumna("codigo_bomat").setFiltro(true);
		set_inventario.getTab_seleccion().getColumna("detalle_bomat").setFiltro(true);
		set_inventario.getBot_aceptar().setMetodo("aceptarInventario");
		set_inventario.setRadio();
		set_inventario.getTab_seleccion().ejecutarSql();
		agregarComponente(set_inventario);
		
		
		
		///Agregamos boton saldos inventario

		
		Boton bot_inventario_saldo = new Boton();
		bot_inventario_saldo.setValue("Consultar Saldo Inventario");
		bot_inventario_saldo.setTitle("SALDOS INVENTARIO");
		bot_inventario_saldo.setIcon("ui-icon-person");
		bot_inventario_saldo.setMetodo("importarSaldo");
		bar_botones.agregarBoton(bot_inventario_saldo);
		set_inventario_saldo.setId("set_inventario_saldo");
		set_inventario_saldo.setSeleccionTabla(ser_Bodega.getDatosInventarioAnio("-1"),"ide_boinv");
		set_inventario_saldo.getTab_seleccion().getColumna("existencia_actual").setNombreVisual("SALDO MATERIAL");
		set_inventario_saldo.getTab_seleccion().getColumna("existencia_actual").setEstilo("font-size: 14px;color: red;font-weight: bold");
		set_inventario_saldo.getTab_seleccion().getColumna("codigo_bomat").setFiltro(true);
		set_inventario_saldo.getTab_seleccion().getColumna("detalle_bomat").setFiltro(true);
		set_inventario_saldo.getTab_seleccion().getColumna("codigo_bomat").setNombreVisual("CODIGO MATERIAL");
		set_inventario_saldo.getTab_seleccion().getColumna("detalle_bomat").setNombreVisual("NOMBRE MATERIAL");
		set_inventario_saldo.getTab_seleccion().getColumna("ide_geani").setVisible(false);
		set_inventario_saldo.getTab_seleccion().getColumna("ingreso_material_boinv").setVisible(false);
		set_inventario_saldo.getTab_seleccion().getColumna("egreso_material_boinv").setVisible(false);
		set_inventario_saldo.getTab_seleccion().getColumna("existencia_inicial_boinv").setVisible(false);
		set_inventario_saldo.getTab_seleccion().getColumna("costo_anterior_boinv").setVisible(false);
		set_inventario_saldo.getTab_seleccion().getColumna("costo_actual_boinv").setVisible(false);
		set_inventario_saldo.getTab_seleccion().getColumna("fecha_ingr_articulo_boinv").setVisible(false);
		set_inventario_saldo.getTab_seleccion().getColumna("costo_inicial_boinv").setVisible(false);
		set_inventario_saldo.getTab_seleccion().getColumna("ide_bomat").setVisible(false);
		set_inventario_saldo.getBot_aceptar().setRendered(false);
		set_inventario_saldo.getTab_seleccion().ejecutarSql();
		agregarComponente(set_inventario_saldo);
		
	}

	public void seleccionaElAnio (){
		if(com_anio.getValue()!=null){
			tab_concepto_egreso.setCondicion("ide_geani="+com_anio.getValue()+" and ide_adsoc is null");
			tab_concepto_egreso.ejecutarSql();
			tab_concepto_egreso.imprimirSql();
			tab_egreso.ejecutarValorForanea(tab_concepto_egreso.getValorSeleccionado());


		}
		else{
			tab_egreso.ejecutarSql();
			tab_egreso.ejecutarValorForanea(tab_concepto_egreso.getValorSeleccionado());
			utilitario.agregarMensajeInfo("Selecione un Año", "");

		}
	}

	public void importarSaldo(){
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		
		set_inventario_saldo.getTab_seleccion().setSql(ser_Bodega.getDatosInventarioAnio(com_anio.getValue().toString()));
		set_inventario_saldo.getTab_seleccion().ejecutarSql();
		set_inventario_saldo.dibujar();

	}
	public  void aceptarInventario(){
		String str_seleccionado = set_inventario.getValorSeleccionado();
	
		if (str_seleccionado!=null){
			tab_egreso.insertar();
			tab_egreso.setValor("ide_boinv",str_seleccionado);
			tab_egreso.setValor("fecha_egreso_boegr", tab_concepto_egreso.getValor("fecha_egreso_bocoe"));
			tab_egreso.setValor("documento_egreso_boegr", tab_concepto_egreso.getValor("numero_egreso_bocoe"));			
			tab_egreso.setValor("activo_boegr", "true");
			
			
		}
		set_inventario.cerrar();
		utilitario.addUpdate("tab_egreso");
	}

	public void importarInventario(){
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		else if(tab_concepto_egreso.isEmpty()){
			utilitario.agregarMensajeInfo("Debe insertar un registro", "");
			return;
		} 
		set_inventario.getTab_seleccion().setSql(ser_contabilidad.getInventario(com_anio.getValue().toString(),"1",""));
		set_inventario.getTab_seleccion().ejecutarSql();
		set_inventario.dibujar();

	}
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if(com_anio.getValue()==null){
			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar un año");
			return;

		}
		if (tab_concepto_egreso.isFocus()) {
			tab_concepto_egreso.insertar();
			tab_concepto_egreso.setValor("ide_geani", com_anio.getValue()+"");

		}

		else if (tab_egreso.isFocus()) {
			tab_egreso.insertar();
			//tab_egreso.setValor("ide_boinv", com_anio.getValue()+"");

		}	
	}

	public void validarIngreso(AjaxBehaviorEvent evt){
		tab_egreso.modificar(evt); //Siempre es la primera linea

		double ingreso_material=0;
		double existencia_inicial=0;

		TablaGenerica existencia_material =utilitario.consultar(ser_Bodega.getDatosInventarioPrincipal(tab_egreso.getValor("ide_boinv")));
		
		try {
		ingreso_material=Double.parseDouble(tab_egreso.getValor("cantidad_egreso_boegr"));
		} catch (Exception e) {
		}
		
		try {
		existencia_inicial = Double.parseDouble(existencia_material.getValor("existencia_actual"));
		} catch (Exception e) {
		}
		
		if(ingreso_material > existencia_inicial)
		{
			tab_egreso.setValor("cantidad_egreso_boegr", "0");
			utilitario.addUpdate("tab_egreso");
			utilitario.agregarNotificacionInfo("Error Ingreso", "La cantidad de Egreso de Material supera el Total existente del material:      "+existencia_inicial);
	       return;
		}	
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if (tab_concepto_egreso.guardar()){
			tab_egreso.guardar();
		}
		guardarPantalla();

	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		if(tab_concepto_egreso.isFocus()){
			tab_concepto_egreso.eliminar();
		}
		else if(tab_egreso.isFocus()){
			tab_egreso.eliminar();
		}	

	}
	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}

	public SeleccionTabla getSet_inventario_saldo() {
		return set_inventario_saldo;
	}

	public void setSet_inventario_saldo(SeleccionTabla set_inventario_saldo) {
		this.set_inventario_saldo = set_inventario_saldo;
	}

	public SeleccionTabla getSet_inventario() {
		return set_inventario;
	}

	public void setSet_inventario(SeleccionTabla set_inventario) {
		this.set_inventario = set_inventario;
	}

	public Tabla getTab_concepto_egreso() {
		return tab_concepto_egreso;
	}

	public void setTab_concepto_egreso(Tabla tab_concepto_egreso) {
		this.tab_concepto_egreso = tab_concepto_egreso;
	}

	public Tabla getTab_egreso() {
		return tab_egreso;
	}

	public void setTab_egreso(Tabla tab_egreso) {
		this.tab_egreso = tab_egreso;
	}


}
