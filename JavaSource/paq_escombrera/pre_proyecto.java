package paq_escombrera;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import paq_contabilidad.ejb.ServicioContabilidad;
import paq_escombrera.ejb.ServicioEscombrera;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;

public class pre_proyecto extends Pantalla {

	private Tabla tab_proyecto = new Tabla();
	private Tabla tab_detalle = new Tabla();
	private SeleccionTabla set_apus=new SeleccionTabla();

	private Combo com_anio=new Combo();
	private Division div_division = new Division();
	
	///reporte
	private Map p_parametros = new HashMap();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();
	private Map map_parametros = new HashMap();


	@EJB
    private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioEscombrera ser_escombrera = (ServicioEscombrera) utilitario.instanciarEJB(ServicioEscombrera.class);
	@EJB
    private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	@EJB
    private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	
	public pre_proyecto() 
	{
		///reporte
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
		
		Boton bot_buscar=new Boton();
		bot_buscar.setIcon("ui-icon-person");
		bot_buscar.setValue("Importar Rubros");
		bot_buscar.setMetodo("importarRubros");
		bar_botones.agregarBoton(bot_buscar);
			
		tab_proyecto.setId("tab_proyecto");
		tab_proyecto.setHeader("PROYECTOS DE CONSTRUCCION");
		tab_proyecto.setTipoFormulario(true);
		tab_proyecto.getGrid().setColumns(4);
		tab_proyecto.setTabla("apu_proyecto", "ide_appry", 1);
		tab_proyecto.getColumna("ide_geani").setVisible(false);
		tab_proyecto.getColumna("fecha_appry").setValorDefecto(utilitario.getFechaActual());
		tab_proyecto.getColumna("activo_appry").setValorDefecto("true");
		tab_proyecto.getColumna("activo_appry").setLectura(true);
		tab_proyecto.getColumna("IDE_GEEDP").setCombo(ser_nomina.servicioEmpleadoContrato("true,false"));
		tab_proyecto.getColumna("IDE_GEEDP").setAutoCompletar();
		tab_proyecto.getColumna("ide_gtemp").setCombo(ser_nomina.servicioEmpleadosActivos("true,false"));
		tab_proyecto.getColumna("ide_gtemp").setLectura(true);
		tab_proyecto.getColumna("ide_gtemp").setAutoCompletar();
		tab_proyecto.getColumna("total_costo_appry").setEtiqueta();
		tab_proyecto.getColumna("total_costo_appry").setFormatoNumero(2);
		tab_proyecto.getColumna("total_costo_appry").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red;");
		tab_proyecto.setCondicion("ide_geani=-1"); 
		tab_proyecto.agregarRelacion(tab_detalle);
		tab_proyecto.dibujar();
		
		PanelTabla pat_proyecto=new PanelTabla();
		pat_proyecto.setPanelTabla(tab_proyecto);
		
		
		tab_detalle.setId("tab_detalle");
		tab_detalle.setHeader("DETALLE PROYECTO - APUS");
		tab_detalle.setTabla("apu_proyecto_detalle", "ide_apprd", 2);
		tab_detalle.getColumna("ide_apitc").setCombo(ser_escombrera.getAPURubros("true"));
		tab_detalle.getColumna("ide_apitc").setAutoCompletar();			
		tab_detalle.getColumna("cantidad_apprd").setValorDefecto("0");
		tab_detalle.getColumna("activo_apprd").setValorDefecto("true");
		tab_detalle.getColumna("cantidad_apprd").setMetodoChange("calcularDetalle");
		tab_detalle.getColumna("ide_bounm").setCombo("bodt_unidad_medida", "ide_bounm", "detalle_bounm,abreviatura_bounm", "");
		tab_detalle.getColumna("precio_apprd").setEtiqueta();
		tab_detalle.getColumna("precio_apprd").setFormatoNumero(2);
		tab_detalle.getColumna("precio_apprd").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;");
		tab_detalle.getColumna("costo_apprd").setEtiqueta();
		tab_detalle.getColumna("costo_apprd").setFormatoNumero(2);
		tab_detalle.getColumna("costo_apprd").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;");
		tab_detalle.setColumnaSuma("costo_apprd"); 
		
		tab_detalle.dibujar();
		PanelTabla pat_detalle=new PanelTabla();
		pat_detalle.setPanelTabla(tab_detalle);
		
		
		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(pat_proyecto, pat_detalle, "60%", "H");
		agregarComponente(div_division);
		
		
		set_apus.setId("set_apus");
		set_apus.setSeleccionTabla(ser_escombrera.getAPURubrosFULL("true","-1","-1"),"ide_apitc");
		set_apus.setTitle("Seleccione Rubros APUs");
		set_apus.getTab_seleccion().getColumna("capitulo").setFiltroContenido();
		set_apus.getTab_seleccion().getColumna("capitulo").setLongitud(30);
		set_apus.getTab_seleccion().getColumna("sub_capitulo").setFiltroContenido();
		set_apus.getTab_seleccion().getColumna("sub_capitulo").setLongitud(40);
		set_apus.getTab_seleccion().getColumna("ide_bounm").setVisible(false);;
		set_apus.getBot_aceptar().setMetodo("aceptarRubro");
		agregarComponente(set_apus);

	}
	
	public void seleccionaElAnio (){
		if(com_anio.getValue()!=null){
			tab_proyecto.setCondicion("ide_geani="+com_anio.getValue());
			tab_proyecto.ejecutarSql();
			tab_detalle.ejecutarValorForanea(tab_proyecto.getValorSeleccionado());
		}
		else{
			utilitario.agregarMensajeInfo("Selecione un año", "");
		}
	}
	
	public void importarRubros(){
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un año", "");
			return;
		}
		else if(tab_proyecto.isEmpty() || tab_proyecto.getValor("ide_appry")==null){
			utilitario.agregarMensajeInfo("No puede Importar un Rubro", "Debe tener guardado un proyecto antes de continuar...");
			return;
		}

		set_apus.getTab_seleccion().setSql(ser_escombrera.getAPURubrosFULL("true","-1",tab_proyecto.getValor("ide_appry")));
		set_apus.getTab_seleccion().ejecutarSql();
		set_apus.dibujar();

	}
	
	public  void aceptarRubro()
	{
		String str_seleccionados= set_apus.getSeleccionados();
		if (str_seleccionados!=null){
			TablaGenerica tab_rubros = utilitario.consultar(ser_escombrera.getAPURubrosFULL("true",str_seleccionados,"-1"));		
			for(int i=0;i<tab_rubros.getTotalFilas();i++)
			{
				tab_detalle.insertar();
				tab_detalle.setValor("ide_apitc", tab_rubros.getValor( i,"ide_apitc"));
				tab_detalle.setValor("precio_apprd", tab_rubros.getValor( i,"costo_total_apitc"));
				tab_detalle.setValor("ide_bounm", tab_rubros.getValor( i,"ide_bounm"));
				set_apus.cerrar();
			}
			set_apus.cerrar();
			utilitario.addUpdate("tab_detalle");
			
		}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
		}
	
	}
	
	public void calcularDetalle(AjaxBehaviorEvent evt) {
		tab_detalle.modificar(evt); // Siempre es la primera linea
		double dou_cantidad=pckUtilidades.CConversion.CDbl_2(tab_detalle.getValor("cantidad_apprd"));
		double dou_precio=pckUtilidades.CConversion.CDbl_2(tab_detalle.getValor("precio_apprd"));
		double dou_costo=dou_cantidad*dou_precio;
		tab_detalle.setValor("costo_apprd", utilitario.getFormatoNumero(dou_costo, 2));
		tab_detalle.sumarColumnas();
		utilitario.addUpdate("tab_detalle");
		calcular();
	}
	
	public void calcular()
	{
		/*double total_costo=0;
		
		if(tab_proyecto.isEmpty() || tab_proyecto.getValor("ide_appry")==null){
			utilitario.agregarMensajeInfo("No puede Calcular", "Debe tener guardado un proyecto antes de continuar...");
			return;
		}
		
		TablaGenerica tab_rubros = utilitario.consultar(ser_escombrera.getAPURubrosFULL("true,false","select ide_apitc from apu_proyecto_detalle where ide_appry="+tab_proyecto.getValor("ide_appry"),"-1"));	
		
		for(int i=0;i<tab_rubros.getTotalFilas();i++)
		{
			total_costo+= pckUtilidades.CConversion.CDbl_2(tab_rubros.getValor( i,"costo_total_apitc"));
		}
		
		tab_proyecto.setValor("total_costo_appry", utilitario.getFormatoNumero(total_costo, 2));
		*/
		
		double total_costo=pckUtilidades.CConversion.CDbl_2(tab_detalle.getSumaColumna("costo_apprd"));
		
		tab_proyecto.setValor("total_costo_appry", utilitario.getFormatoNumero(total_costo, 2));
		utilitario.addUpdate("tab_proyecto");
		tab_proyecto.modificar(tab_proyecto.getFilaActual());
	}
	
	//reporte
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}
	
	public void aceptarReporte(){
		if(rep_reporte.getReporteSelecionado().equals("Rpt Proyecto"));{
			if (rep_reporte.isVisible()){
				p_parametros=new HashMap();		
				rep_reporte.cerrar();	
				//p_parametros.put("titulo","CERTIFICACION PRESUPUESTARIA");
				p_parametros.put("ide_appry", pckUtilidades.CConversion.CInt(tab_proyecto.getValor("ide_appry")));
				
	
				self_reporte.setSeleccionFormatoReporte(p_parametros,rep_reporte.getPath());
				self_reporte.dibujar();
			
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
			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar un año");
			return;
		
		}
		
		utilitario.getTablaisFocus().insertar();
		if(tab_proyecto.isFocus()){
			String ide_gtempxx=ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
			tab_proyecto.setValor("ide_gtemp",ide_gtempxx );
			tab_proyecto.setValor("ide_geani",com_anio.getValue()+"");
			utilitario.addUpdate("tab_proyecto");
		}
	}

	@Override
	public void guardar() 
	{
		
		if (tab_proyecto.guardar()) {
			tab_detalle.guardar();
			//calcular();
			//tab_proyecto.guardar();
			guardarPantalla();
		}
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}

	public Division getDiv_division() {
		return div_division;
	}

	public void setDiv_division(Division div_division) {
		this.div_division = div_division;
	}

	
	public Tabla getTab_proyecto() {
		return tab_proyecto;
	}

	public void setTab_proyecto(Tabla tab_proyecto) {
		this.tab_proyecto = tab_proyecto;
	}

	public Tabla getTab_detalle() {
		return tab_detalle;
	}

	public void setTab_detalle(Tabla tab_detalle) {
		this.tab_detalle = tab_detalle;
	}

	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}

	
	public SeleccionTabla getSet_apus() {
		return set_apus;
	}

	public void setSet_apus(SeleccionTabla set_apus) {
		this.set_apus = set_apus;
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

	public SeleccionFormatoReporte getSelf_reporte() {
		return self_reporte;
	}

	public void setSelf_reporte(SeleccionFormatoReporte self_reporte) {
		this.self_reporte = self_reporte;
	}

	public Map getMap_parametros() {
		return map_parametros;
	}

	public void setMap_parametros(Map map_parametros) {
		this.map_parametros = map_parametros;
	}

	

}
