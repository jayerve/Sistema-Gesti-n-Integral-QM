package paq_bodega;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;

public class pre_inventario extends Pantalla{

	private Tabla tab_inventario=new Tabla();
	private Combo com_anio=new Combo();
	private SeleccionTabla set_material=new SeleccionTabla();
	private SeleccionTabla set_actualizamaterial=new SeleccionTabla();
	private SeleccionTabla set_reporte_inventario=new SeleccionTabla();
	private SeleccionTabla set_buscar = new SeleccionTabla();
	private  Tabla tab_tabla2 = new Tabla();
	private Confirmar con_guardar=new Confirmar();
	public static String par_grupo_material;
	private Map p_parametros = new HashMap();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();


	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioBodega ser_Bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);
	public pre_inventario() {

		rep_reporte.setId("rep_reporte"); //id
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");//ejecuta el metodo al aceptar reporte
		agregarComponente(rep_reporte);//agrega el componente a la pantalla
		bar_botones.agregarReporte();//aparece el boton de reportes en la barra de botones
		self_reporte.setId("self_reporte"); //id
		agregarComponente(self_reporte);
		
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
		par_grupo_material=utilitario.getVariable("p_grupo_material");


		tab_inventario.setId("tab_inventario");  
		tab_inventario.setTabla("bodt_inventario","ide_boinv", 1);
		tab_inventario.getColumna("ide_bomat").setUnico(true);
		tab_inventario.getColumna("ide_geani").setUnico(true);
		tab_inventario.getColumna("ide_geani").setVisible(false);
		tab_inventario.getColumna("gen_ide_geani").setVisible(false);
		tab_inventario.getColumna("IDE_GEARE").setVisible(false);
		tab_inventario.getColumna("ingreso_material_boinv").setValorDefecto("0.0");
		tab_inventario.getColumna("egreso_material_boinv").setValorDefecto("0.0");
		tab_inventario.getColumna("existencia_inicial_boinv").setValorDefecto("0.0");
		tab_inventario.getColumna("costo_anterior_boinv").setValorDefecto("0.0");
		tab_inventario.getColumna("costo_actual_boinv").setValorDefecto("0.0");
		tab_inventario.getColumna("costo_inicial_boinv").setValorDefecto("0.0");
		tab_inventario.getColumna("activo_boinv").setValorDefecto("true");

		tab_inventario.getColumna("ingreso_material_boinv").setMetodoChange("validarIngreso");
		tab_inventario.getColumna("egreso_material_boinv").setMetodoChange("validarIngreso");
		tab_inventario.getColumna("existencia_inicial_boinv").setMetodoChange("validarIngreso");	

		tab_inventario.setCondicion("ide_geani=-1"); 
		tab_inventario.getColumna("activo_boinv").setLectura(true);
		tab_inventario.getColumna("ide_bomat").setCombo(ser_Bodega.getInventario("1","true,false",""));
		tab_inventario.getColumna("ide_bomat").setAutoCompletar();
		tab_inventario.getColumna("ide_bomat").setLectura(true);
		//	tab_inventario.getColumna("ide_bomat").setCombo(ser_Bodega.getMaterialBodega("-1"));
		tab_inventario.getColumna("gen_ide_geani").setCombo(ser_contabilidad.getAnio("true,false","true,false"));
		tab_inventario.getColumna("IDE_GEARE").setCombo("gen_area","ide_geare","detalle_geare","");
		tab_inventario.dibujar();

		PanelTabla pat_inventario = new PanelTabla ();
		pat_inventario.setPanelTabla(tab_inventario);

		Division div_division=new Division();
		div_division.dividir1(pat_inventario);
		agregarComponente(div_division);

		Boton bot_material = new Boton();
		bot_material.setValue("Agregar Material");
		bot_material.setTitle("MATERIAL");
		bot_material.setIcon("ui-icon-person");
		bot_material.setMetodo("importarMaterial");
		bar_botones.agregarBoton(bot_material);

		
		Boton bot_buscar = new Boton();
		bot_buscar.setValue("Buscar Inventario");
		bot_buscar.setTitle("BUSCAR");
		bot_buscar.setIcon("ui-icon-person");
		bot_buscar.setMetodo("buscarMaterial");
		bar_botones.agregarBoton(bot_buscar);
		
		set_material.setId("set_material");
		set_material.setSeleccionTabla(ser_Bodega.getInventario("0","true",par_grupo_material),"ide_bomat");
		set_material.getTab_seleccion().getColumna("codigo_bomat").setFiltro(true);
		set_material.getTab_seleccion().getColumna("detalle_bomat").setFiltro(true);
		set_material.getTab_seleccion().getColumna("detalle_bogrm").setFiltro(true);
		set_material.getBot_aceptar().setMetodo("aceptarMaterial");
		set_material.getTab_seleccion().ejecutarSql();
		agregarComponente(set_material);

		bar_botones.agregarBoton(bot_material);
		con_guardar.setId("con_guardar");
		agregarComponente(con_guardar);

		Boton bot_actualizar=new Boton();
		bot_actualizar.setIcon("ui-icon-person");
		bot_actualizar.setValue("Actualizar Material");
		bot_actualizar.setMetodo("actualizarMaterial");
		bar_botones.agregarBoton(bot_actualizar);	
		
		 Boton bot_excel=new Boton();
	     bot_excel.setValue("Exportar EXCEL");
	     bot_excel.setIcon("ui-icon-calculator");
	     bot_excel.setAjax(false);
	     bot_excel.setMetodo("exportarExcel");
	     bar_botones.agregarBoton(bot_excel);

		set_actualizamaterial.setId("set_actualizamaterial");
		set_actualizamaterial.setSeleccionTabla(ser_Bodega.getInventario("0","true",par_grupo_material),"ide_bomat");
		set_actualizamaterial.getTab_seleccion().getColumna("codigo_bomat").setFiltro(true);
		set_actualizamaterial.getTab_seleccion().getColumna("detalle_bomat").setFiltro(true);
		set_actualizamaterial.getTab_seleccion().getColumna("detalle_bogrm").setFiltro(true);
		set_actualizamaterial.getBot_aceptar().setMetodo("modificarMaterial");
		set_actualizamaterial.setRadio();
		agregarComponente(set_actualizamaterial);
		
		
		///////// imprimir kardex
		set_reporte_inventario.setId("set_reporte_inventario");
		set_reporte_inventario.setSeleccionTabla(ser_Bodega.getDatosInventarioAnio("-1"),"ide_boinv");
		set_reporte_inventario.getTab_seleccion().getColumna("codigo_bomat").setFiltro(true);
		set_reporte_inventario.getTab_seleccion().getColumna("detalle_bomat").setFiltro(true);
		set_reporte_inventario.getTab_seleccion().getColumna("codigo_bomat").setNombreVisual("CODIGO MATERIAL");
		set_reporte_inventario.getTab_seleccion().getColumna("detalle_bomat").setNombreVisual("NOMBRE MATERIAL");
		set_reporte_inventario.getTab_seleccion().getColumna("ide_geani").setVisible(false);
		set_reporte_inventario.getTab_seleccion().getColumna("ingreso_material_boinv").setVisible(false);
		set_reporte_inventario.getTab_seleccion().getColumna("egreso_material_boinv").setVisible(false);
		set_reporte_inventario.getTab_seleccion().getColumna("existencia_inicial_boinv").setVisible(false);
		set_reporte_inventario.getTab_seleccion().getColumna("costo_anterior_boinv").setVisible(false);
		set_reporte_inventario.getTab_seleccion().getColumna("costo_actual_boinv").setVisible(false);
		set_reporte_inventario.getTab_seleccion().getColumna("fecha_ingr_articulo_boinv").setVisible(false);
		set_reporte_inventario.getTab_seleccion().getColumna("costo_inicial_boinv").setVisible(false);
		set_reporte_inventario.getTab_seleccion().getColumna("ide_bomat").setVisible(false);
	
		set_reporte_inventario.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(set_reporte_inventario);
		
		/// INSTANCIO TABLA PARA BUSCAR
		set_buscar.setId("set_buscar");
		set_buscar.setSeleccionTabla(ser_Bodega.getInventario("0","true",par_grupo_material),"ide_bomat");
		set_buscar.getTab_seleccion().getColumna("codigo_bomat").setFiltro(true);
		set_buscar.getTab_seleccion().getColumna("detalle_bomat").setFiltro(true);
		set_buscar.getTab_seleccion().getColumna("detalle_bogrm").setFiltro(true);
		set_buscar.getBot_aceptar().setMetodo("filtraMaterial");
		set_buscar.getTab_seleccion().ejecutarSql();
		set_buscar.setRadio();
		agregarComponente(set_buscar);
	}
	public void buscarMaterial(){
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}

		set_buscar.getTab_seleccion().setSql(ser_Bodega.getInventario("0","true",par_grupo_material));
		set_buscar.getTab_seleccion().ejecutarSql();
		set_buscar.dibujar();
	}
	public void filtraMaterial(){
		tab_inventario.setCondicion("ide_bomat="+set_buscar.getValorSeleccionado());
		tab_inventario.ejecutarSql();
		set_buscar.cerrar();
		utilitario.addUpdate("tab_inventario");
	}
	public void imprimirKardex(){
		if (tab_inventario.getValor("ide_geani")==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un año ","");
			return;

		}
		set_reporte_inventario.getTab_seleccion().setSql(ser_Bodega.getDatosInventarioAnio(com_anio.getValue().toString()));
		set_reporte_inventario.getTab_seleccion().ejecutarSql();
		set_reporte_inventario.dibujar();
	}

	public void exportarExcel(){
      if(com_anio.getValue()==null){
        utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
        return;
      }
      tab_tabla2.setSql(ser_Bodega.getDatosInventarioAnio(com_anio.getValue().toString()));
      tab_tabla2.ejecutarSql();
      if (tab_tabla2.getTotalFilas()>0){
    	  reportes.exportarInventarioXLS(tab_tabla2,"inventario_saldos_material.xls","1","1");
      }
    }
	
	
	public void actualizarMaterial(){
		if (tab_inventario.getValor("ide_geani")==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un año ","");
			return;

		}
		set_actualizamaterial.getTab_seleccion().setSql(ser_Bodega.getInventario("0","true",par_grupo_material));
		set_actualizamaterial.getTab_seleccion().ejecutarSql();
		set_actualizamaterial.dibujar();	
	}	

	public void modificarMaterial(){

		String str_materialActualizado=set_actualizamaterial.getValorSeleccionado();

		TablaGenerica tab_materialModificado = ser_Bodega.getTablaInventario(str_materialActualizado);
		tab_inventario.setValor("IDE_BOMAT", tab_materialModificado.getValor("IDE_BOMAT"));			
		tab_inventario.modificar(tab_inventario.getFilaActual());
		utilitario.addUpdate("tab_inventario");	

		con_guardar.setMessage("Esta Seguro de Actualizar el Material");
		con_guardar.setTitle("CONFIRMACION ");
		con_guardar.getBot_aceptar().setMetodo("guardarActualilzarMaterial");
		con_guardar.dibujar();
		utilitario.addUpdate("con_guardar");

	}
	public void guardarActualilzarMaterial(){
		tab_inventario.guardar();
		con_guardar.cerrar();
		set_actualizamaterial.cerrar();
		guardarPantalla();

	}
	public void importarMaterial(){
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}

		set_material.getTab_seleccion().setSql(ser_Bodega.getInventario("0","true",par_grupo_material));
		set_material.getTab_seleccion().ejecutarSql();
		set_material.dibujar();

	}

	public  void aceptarMaterial(){
		String str_seleccionados = set_material.getSeleccionados();
		System.out.println("entra al str  "+str_seleccionados);

		if (str_seleccionados!=null){
			tab_inventario.insertar();
			tab_inventario.setValor("ide_bomat",str_seleccionados);
			tab_inventario.setValor("ide_geani", com_anio.getValue()+"");

		}
		set_material.cerrar();
		utilitario.addUpdate("tab_inventario");
	}

	public void seleccionaElAnio (){
		if(com_anio.getValue()!=null){

			tab_inventario.setCondicion("ide_geani="+com_anio.getValue());
			tab_inventario.ejecutarSql();
			//tab_mes.ejecutarValorForanea(tab_poa.getValorSeleccionado());

		}
		else{
			tab_inventario.setCondicion("ide_geani=-1");
			tab_inventario.ejecutarSql();
		}
	}
public void validarIngreso(AjaxBehaviorEvent evt){
	tab_inventario.modificar(evt); //Siempre es la primera linea

	double ingreso_material=0;
	double egreso_material=0;
	double existencia_inicial=0;
	double costo_inicial=0;
	double costo_anterior=0;
	double costo_actual=0;
	
	try {
	ingreso_material=Double.parseDouble(tab_inventario.getValor("ingreso_material_boinv"));
	} catch (Exception e) {
	}
	try {
	egreso_material = Double.parseDouble(tab_inventario.getValor("egreso_material_boinv"));
	} catch (Exception e) {
	}
	try {
	existencia_inicial = Double.parseDouble(tab_inventario.getValor("existencia_inicial_boinv"));
	} catch (Exception e) {
	}
	try {
	costo_anterior = Double.parseDouble(tab_inventario.getValor("costo_anterior_boinv"));
	} catch (Exception e) {
	}
	try {
	costo_actual = Double.parseDouble(tab_inventario.getValor("costo_actual_boinv"));
	} catch (Exception e) {
	}
	try {
	costo_inicial = Double.parseDouble(tab_inventario.getValor("costo_inicial_boinv"));
	} catch (Exception e) {
	}
	double total_ingreso=existencia_inicial+ingreso_material;
	if(egreso_material > total_ingreso)
	{
		tab_inventario.setValor("egreso_material_boinv", "0");
		utilitario.addUpdate("tab_inventario");
		utilitario.agregarMensajeError("Error Ingreso", "La cantidad de Egreso de Material supera el Total existente del material");
       return;
	}	
	
}

public void abrirListaReportes() {
	// TODO Auto-generated method stub
	rep_reporte.dibujar();
}
public void aceptarReporte(){
	if(rep_reporte.getReporteSelecionado().equals("Kardex Inventarios"));{
		if (rep_reporte.isVisible()){
			
			p_parametros=new HashMap();		
			rep_reporte.cerrar();
			p_parametros.clear();
			imprimirKardex();
			
		}
		else if(set_reporte_inventario.isVisible()) {
			
			if(set_reporte_inventario.getListaSeleccionados().size()>0){
				ser_Bodega.matrizKardexInventarios(set_reporte_inventario.getSeleccionados());
				
				p_parametros.put("pide_boinv",set_reporte_inventario.getSeleccionados());
				p_parametros.put("titulo","TARJETA KARDEX DE INVENTARIOS");
				self_reporte.setSeleccionFormatoReporte(p_parametros,rep_reporte.getPath());
			    self_reporte.dibujar();
				set_reporte_inventario.cerrar();

			}		
		}
		else{
			utilitario.agregarMensajeInfo("No se puede continuar", "No ha Seleccionado Ningun Registro");

		}

}
}
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		if(tab_inventario.isFocus()) {
			utilitario.agregarMensaje("No se puede insertar", "Debe Agregar Material");
		}
		else{
			tab_inventario.isFocus(); 
			tab_inventario.insertar();
			tab_inventario.setValor("ide_geani", com_anio.getValue()+"");


		} 

	}





	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		
				tab_inventario.guardar();
		guardarPantalla();

	}



	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_inventario.eliminar();

	}



	public Tabla getTab_inventario() {
		return tab_inventario;
	}



	public void setTab_inventario(Tabla tab_inventario) {
		this.tab_inventario = tab_inventario;
	}

	public Combo getCom_anio() {
		return com_anio;
	}

	public SeleccionTabla getSet_reporte_inventario() {
		return set_reporte_inventario;
	}
	public void setSet_reporte_inventario(SeleccionTabla set_reporte_inventario) {
		this.set_reporte_inventario = set_reporte_inventario;
	}
	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}

	public SeleccionTabla getSet_material() {
		return set_material;
	}

	public void setSet_material(SeleccionTabla set_material) {
		this.set_material = set_material;
	}
	public SeleccionTabla getSet_actualizamaterial() {
		return set_actualizamaterial;
	}
	public void setSet_actualizamaterial(SeleccionTabla set_actualizamaterial) {
		this.set_actualizamaterial = set_actualizamaterial;
	}
	public Confirmar getCon_guardar() {
		return con_guardar;
	}
	public void setCon_guardar(Confirmar con_guardar) {
		this.con_guardar = con_guardar;
	}
	public static String getPar_grupo_material() {
		return par_grupo_material;
	}
	public static void setPar_grupo_material(String par_grupo_material) {
		pre_inventario.par_grupo_material = par_grupo_material;
	}
	public SeleccionFormatoReporte getSelf_reporte() {
		return self_reporte;
	}
	public void setSelf_reporte(SeleccionFormatoReporte self_reporte) {
		this.self_reporte = self_reporte;
	}
	public Map getP_parametros() {
		return p_parametros;
	}
	public void setP_parametros(Map p_parametros) {
		this.p_parametros = p_parametros;
	}
	public Reporte getRep_reporte() {
		return rep_reporte;
	}
	public void setRep_reporte(Reporte rep_reporte) {
		this.rep_reporte = rep_reporte;
	}
	public SeleccionTabla getSet_buscar() {
		return set_buscar;
	}
	public void setSet_buscar(SeleccionTabla set_buscar) {
		this.set_buscar = set_buscar;
	} 

}
