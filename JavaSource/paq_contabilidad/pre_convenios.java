/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_contabilidad;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;

import paq_contabilidad.ejb.ServicioContabilidad;
import paq_general.ejb.ServicioGeneral;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Arbol;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;


public class pre_convenios extends Pantalla {

	private Tabla tab_tabla1 = new Tabla();
	private Tabla tab_tabla2 = new Tabla();
	private Tabla tab_tabla3 = new Tabla();
	private Arbol arb_arbol = new Arbol();
	private Dialogo dia_responsable=new Dialogo();
	public static String par_modulo_convenio;
	public static String par_funcionario_convenio;
	public static String par_particular_convenio;

	private SeleccionTabla set_tipo_persona = new SeleccionTabla();
	private SeleccionTabla set_empleado= new SeleccionTabla();
	private SeleccionTabla set_tipo_convenio= new SeleccionTabla();
	private SeleccionTabla set_persona= new SeleccionTabla();
	private SeleccionTabla set_empleado_nuevo= new SeleccionTabla();
	private SeleccionTabla  set_institucion= new SeleccionTabla();
	private SeleccionTabla sel_tab_particular = new SeleccionTabla();
	private SeleccionTabla sel_tab_funcionario = new SeleccionTabla();
	private Dialogo dia_estados = new Dialogo();
	private Map p_parametros = new HashMap();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();
	private SeleccionTabla set_estados = new SeleccionTabla();
	@EJB
	private ServicioGeneral ser_general = (ServicioGeneral ) utilitario.instanciarEJB(ServicioGeneral.class);
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);

	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);

	public pre_convenios() {

		rep_reporte.setId("rep_reporte"); //id
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");//ejecuta el metodo al aceptar reporte
		agregarComponente(rep_reporte);//agrega el componente a la pantalla
		bar_botones.agregarReporte();//aparece el boton de reportes en la barra de botones
		self_reporte.setId("self_reporte"); //id
		agregarComponente(self_reporte); //agrego el componente a la pantall

		Tabulador tab_tabulador = new Tabulador();
		tab_tabulador.setId("tab_tabulador");

		tab_tabla1.setId("tab_tabla1");
		tab_tabla1.setTabla("cont_convenio", "ide_cocon", 1);
		tab_tabla1.setTipoFormulario(true);  //formulario 
		tab_tabla1.getGrid().setColumns(4); //hacer  columnas 
		tab_tabla1.setCampoPadre("con_ide_cocon");
		tab_tabla1.setCampoNombre("detalle_contrato_cocon");
		tab_tabla1.getColumna("ide_cotie").setCombo("cont_tipo_convenio","ide_cotie","detalle_cotie","");
		tab_tabla1.getColumna("ide_geins").setCombo("gen_institucion","ide_geins","detalle_geins","");
		tab_tabla1.getColumna("ide_coest").setCombo("cont_estado","ide_coest","detalle_coest","");
		tab_tabla1.agregarRelacion(tab_tabla2);
		tab_tabla1.agregarRelacion(tab_tabla3);
		tab_tabla1.agregarArbol(arb_arbol);
		tab_tabla1.dibujar();
		PanelTabla pat_panel1 = new PanelTabla();
		pat_panel1.setPanelTabla(tab_tabla1);

		arb_arbol.setId("arb_arbol");
		arb_arbol.dibujar();

		tab_tabla2.setId("tab_tabla2");
		tab_tabla2.setIdCompleto("tab_tabulador:tab_tabla2");
		tab_tabla2.setTipoFormulario(true);  //formulario
		tab_tabla2.getGrid().setColumns(4);
		tab_tabla2.setTabla("cont_responsable_convenio", "ide_corec", 2);
		tab_tabla2.getColumna("ide_cocon").setCombo("cont_convenio", "ide_cocon", "detalle_contrato_cocon", "");
		tab_tabla2.getColumna("ide_getip").setCombo("gen_tipo_persona", "ide_getip", "detalle_getip", "");
		tab_tabla2.getColumna("ide_gecaf").setCombo("gen_cargo_funcional", "ide_gecaf", "detalle_gecaf", "");
		//tab_tabla2.getColumna("ide_getip").setCombo(ser_general.getTipoPersona("true",par_modulo_convenio));
		tab_tabla2.getColumna("IDE_GTEMP").setVisible(false);
		tab_tabla2.getColumna("IDE_GEEDP").setCombo(ser_nomina.servicioEmpleadoContrato("true,false"));
		tab_tabla2.getColumna("IDE_GEEDP").setAutoCompletar();
		tab_tabla2.getColumna("IDE_GEEDP").setLectura(true);
		tab_tabla2.getColumna("IDE_GEEDP").setUnico(true);
		tab_tabla2.getColumna("ide_corec").setUnico(true);
		tab_tabla2.getColumna("apellido_paterno_corec").setAutoCompletar();
		
		//tab_tabla2.setCampoForanea("ide_cocon");

		//getTipoPersona("true",par_modulo_convenio),"ide_getip");
		tab_tabla2.dibujar();
		PanelTabla pat_panel2 = new PanelTabla();
		pat_panel2.setPanelTabla(tab_tabla2);

		tab_tabla3.setId("tab_tabla3");
		tab_tabla3.setIdCompleto("tab_tabulador:tab_tabla3");
		tab_tabla3.setTipoFormulario(true);;  //formulario 
		tab_tabla3.getGrid().setColumns(4);
		tab_tabla3.setTabla("cont_archivo_convenio", "ide_coarc", 3);
		tab_tabla3.getColumna("foto_coarc").setUpload("tabla3");
		//tab_tabla3.getColumna("foto_coarc").setValorDefecto("Cargar Archivo");
		tab_tabla3.dibujar();
		PanelTabla pat_panel3 = new PanelTabla();
		pat_panel3.setPanelTabla(tab_tabla3);







		tab_tabulador.agregarTab("RESPONSABLE FIRMA CONVENIO",pat_panel2);
		tab_tabulador.agregarTab("ARCHIVO ANEXO CONVENIO", pat_panel3);


		Division div3 = new Division(); //UNE OPCION Y DIV 2
		div3.dividir2(pat_panel1, tab_tabulador, "60%", "H");
		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(arb_arbol, div3, "21%", "V");  //arbol y div3
		agregarComponente(div_division);
		/*
		Boton bot_agregar = new Boton();
		bot_agregar.setValue("Agregar Tipo Persona");
		bot_agregar.setMetodo("importarTipoPersona");
		bar_botones.agregarBoton(bot_agregar);
		 */
		par_modulo_convenio=utilitario.getVariable("p_modulo_convenio");
		par_funcionario_convenio=utilitario.getVariable("p_funcionario_convenio");
		par_particular_convenio=utilitario.getVariable("p_particular_convenio");



		inicializarSetEstados();
		inicializaSetTipopersona();  
		inicializaSetEmpleado();
		inicializarSetTipoConvenio();
		inicializarSetInstitucion();
		inicializaInsertaSetTipopersona();
		inicializaInsertaSetEmpeado();



	}

	public void inicializaSetEmpleado(){
		set_empleado.setId("set_empleado");
		set_empleado.setTitle("Seleccione un Empleado");
		set_empleado.setSeleccionTabla(ser_nomina.servicioEmpleadoContrato("true,false"),"ide_geedp");
		set_empleado.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("nombres_apellidos").setFiltro(true);
		set_empleado.setRadio();
		set_empleado.getTab_seleccion().ejecutarSql();
		set_empleado.getBot_aceptar().setMetodo("aceptarEmpleado");
		agregarComponente(set_empleado);

	}
	public void insertarRegistro(){

	}
	public void inicializaSetTipopersona(){

		set_tipo_persona.setId("set_tipo_persona");
		set_tipo_persona.setTitle("Seleccione un Tipo de Persona");
		set_tipo_persona.setSeleccionTabla(ser_general.getTipoPersona("true",par_modulo_convenio),"ide_getip");
		set_tipo_persona.setRadio();
		set_tipo_persona.getTab_seleccion().ejecutarSql();
		set_tipo_persona.getBot_aceptar().setMetodo("aceptarTipoPersona");        
		agregarComponente(set_tipo_persona);   
	}
	///////inserat d0cumento nuevo

	public void inicializaInsertaSetTipopersona(){

		set_persona.setId("set_persona");
		set_persona.setTitle("Seleccione un Tipo de Persona");
		set_persona.setSeleccionTabla(ser_general.getTipoPersona("true",par_modulo_convenio),"ide_getip");
		set_persona.setRadio();
		set_persona.getTab_seleccion().ejecutarSql();
		set_persona.getBot_aceptar().setMetodo("aceptarPersona");        
		agregarComponente(set_persona); 

	}

	public void inicializaInsertaSetEmpeado(){
		set_empleado_nuevo.setId("set_empleado_nuevo");
		set_empleado_nuevo.setTitle("Seleccione un Empleado");
		set_empleado_nuevo.setSeleccionTabla(ser_nomina.servicioEmpleadoContrato("true,false"),"ide_geedp");
		set_empleado_nuevo.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
		set_empleado_nuevo.getTab_seleccion().getColumna("nombres_apellidos").setFiltro(true);
		set_empleado_nuevo.setRadio();
		set_empleado_nuevo.getTab_seleccion().ejecutarSql();
		set_empleado_nuevo.getBot_aceptar().setMetodo("aceptarPersona");
		agregarComponente(set_empleado_nuevo);

	}

	public void aceptarPersona(){
		if(set_persona.isVisible()){
		tab_tabla2.insertar();	
		String str_seleccionado=set_persona.getValorSeleccionado();
		if(str_seleccionado.equals(par_funcionario_convenio)){
			tab_tabla2.setValor("ide_getip", str_seleccionado); ///para que se me despliegue el valor que selecciono en el campo 
			System.out.println(" tipo persona"+str_seleccionado);
			set_persona.cerrar();
			set_empleado_nuevo.dibujar();
			System.out.println("salo del tipo persona");
		}
		
		}else if (set_empleado_nuevo.isVisible()){
			if (set_empleado_nuevo.getValorSeleccionado()!=null){
			  String str_seleccionado_emnuevo=set_empleado_nuevo.getValorSeleccionado();
				System.out.println("entro al empleado"+set_empleado_nuevo.getValorSeleccionado());
				TablaGenerica tab_empleado_responsable = ser_nomina.ideEmpleadoContrato(str_seleccionado_emnuevo,"true");	
				
				tab_tabla2.setValor("IDE_GEEDP", tab_empleado_responsable.getValor("IDE_GEEDP"));			
				tab_tabla2.setValor("IDE_GTEMP", tab_empleado_responsable.getValor("IDE_GTEMP"));
				tab_tabla2.setValor("APELLIDO_PATERNO_corec", tab_empleado_responsable.getValor("APELLIDO_PATERNO_GTEMP"));
				utilitario.addUpdate("tab_tabla2");	
				set_empleado_nuevo.cerrar();
			}
			
		}

		
	}


	public void inicializarSetEstados(){

		set_estados.setId("set_estados");
		set_estados.setTitle("SELECCIONE EL/LOS ESTADOS");
		set_estados.setSeleccionTabla(ser_contabilidad.getModuloEstados("false",par_modulo_convenio), "ide_coest");
		set_estados.getTab_seleccion().ejecutarSql();
		set_estados.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(set_estados);  

	} 

	public void inicializarSetTipoConvenio(){

		set_tipo_convenio.setId("set_tipo_convenio");
		set_tipo_convenio.setTitle("SELECCIONE EL TIPO DE CONVENIO");
		set_tipo_convenio.setSeleccionTabla(ser_contabilidad.getTipoConvenio("true,false",par_modulo_convenio),"ide_cotie");
		set_tipo_convenio.getTab_seleccion().ejecutarSql();
		set_tipo_convenio.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(set_tipo_convenio);

	}

	public void inicializarSetInstitucion(){
		set_institucion.setId("set_institucion");
		set_institucion.setTitle("SELECCIONE UNA INSTITUCION");
		set_institucion.setSeleccionTabla(ser_contabilidad.getInstitucion("true,false",par_modulo_convenio),"ide_geins");
		set_institucion.getTab_seleccion().ejecutarSql();
		set_institucion.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(set_institucion);

	}

	public void reporteSetEstados(){
		set_estados.setSeleccionTabla(ser_contabilidad.getModuloEstados("true,false",par_modulo_convenio), "ide_coest");
		set_estados.getTab_seleccion().ejecutarSql();
		set_estados.getBot_aceptar().setMetodo("aceptarReporte");
		set_estados.dibujar();
	}

	public void reporteSetTipoConvenio(){

		set_tipo_convenio.setSeleccionTabla(ser_contabilidad.getTipoConvenio("true,false",par_modulo_convenio),"ide_cotie");
		set_tipo_convenio.getTab_seleccion().ejecutarSql();
		set_tipo_convenio.getBot_aceptar().setMetodo("aceptarReporte");
		set_tipo_convenio.dibujar();

	}

	public void reporteSetInstitucion(){

		set_institucion.setSeleccionTabla(ser_contabilidad.getInstitucion("true,false",par_modulo_convenio),"ide_geins");
		set_institucion.getTab_seleccion().getColumna("DETALLE_GEINS").setFiltro(true);
		set_institucion.getTab_seleccion().ejecutarSql();
		set_institucion.getBot_aceptar().setMetodo("aceptarReporte");
		set_institucion.dibujar();


	}
	public void importarTipoPersona(){

		set_tipo_persona.getTab_seleccion().setSql(ser_general.getTipoPersona("true",par_modulo_convenio));
		set_tipo_persona.getTab_seleccion().ejecutarSql();
		set_tipo_persona.dibujar();

	}
	public  void aceptarEstados(){
		String str_seleccionados=set_estados.getSeleccionados();
		if(str_seleccionados!=null){
			//Inserto los empleados seleccionados en la tabla de participantes 
			TablaGenerica tab_generica= ser_contabilidad.getTablaModuloEstados("true",str_seleccionados);		   		
			System.out.println(" tabla generica"+tab_generica.getSql());
			for(int i=0;i<tab_generica.getTotalFilas();i++){
				tab_tabla1.insertar();
				tab_tabla1.setValor("ide_coest", tab_generica.getValor(i, "ide_coest"));			

			}

			set_estados.cerrar();
			utilitario.addUpdate("tab_tabla1");	
		}
	}
	/*public  void aceptarTipoPersona(){
		String str_seleccionado=set_tipo_persona.getValorSeleccionado();
		System.out.println("ACTIVOS :"+set_tipo_persona.getValorSeleccionado());
		if(str_seleccionado.equals(par_funcionario_convenio)){
System.out.println("if del aceptar persona"+str_seleccionado.toString());
			tab_tabla2.setValor("ide_getip", str_seleccionado); ///para que se me despliegue el valor que selecciono en el campo 
			tab_tabla2.modificar(tab_tabla2.getFilaActual());//para que haga el update
			TablaGenerica tab_empleado = ser_nomina.ideEmpleadoContrato(str_seleccionado);	
			set_empleado.setSeleccionTabla(ser_nomina.servicioEmpleadoContrato("true,false"),"ide_geedp");
			set_empleado.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
			set_empleado.getTab_seleccion().getColumna("nombres_apellidos").setFiltro(true);
			set_empleado.getTab_seleccion().ejecutarSql();
			tab_tabla2.insertar();
			tab_tabla2.guardar();
			utilitario.addUpdate("tab_tabla2");
			set_tipo_persona.cerrar();			
			set_empleado.dibujar();

			System.out.println("salir del if tipo persona"+tab_empleado.getValorSeleccionado());


		}else if( str_seleccionado.equals(par_particular_convenio)){

			TablaGenerica tab_empleado = ser_nomina.ideEmpleadoContrato(str_seleccionado);				System.out.println("ACTIVOS :"+set_tipo_persona.getValorSeleccionado());
			set_empleado.setSeleccionTabla(ser_nomina.servicioEmpleadoContrato("true,false"),"ide_geedp");
			set_empleado.getTab_seleccion().ejecutarSql();
			set_empleado.cerrar();
			utilitario.addUpdate("tab_tabla2");
			inicializaSetEmpleado();
			utilitario.agregarMensajeInfo("Hola particular","particular");

		}else{

			utilitario.agregarMensajeInfo("No se puede continuar", "No ha Seleccionado Ningun Registro");
			return;
		}
	}
	 */
	public void aceptarEmpleado(){
		String str_seleccionado=set_empleado.getValorSeleccionado();
		if(str_seleccionado!=null){
			//Inserto los empleados seleccionados en la tabla de resposable d econtratacion 
			TablaGenerica tab_empleado_responsable = ser_nomina.ideEmpleadoContrato(str_seleccionado,"true");		

			System.out.println(" tabla generica"+tab_empleado_responsable.getSql());
			//for(int i=0;i<tab_empleado_responsable.getTotalFilas();i++){
			//tab_tabla2.insertar();
			tab_tabla2.setValor("IDE_GEEDP", tab_empleado_responsable.getValor("IDE_GEEDP"));			
			tab_tabla2.setValor("IDE_GTEMP", tab_empleado_responsable.getValor("IDE_GTEMP"));
			tab_tabla2.modificar(tab_tabla2.getFilaActual());//para que haga el update
			tab_tabla2.guardar();

			utilitario.addUpdate("tab_tabla2");	

		}
		set_empleado.cerrar();
	}
	//else{
	///	utilitario.agregarMensajeInfo("Debe seleccionar almenos un registro", "");
	//	}
	//	}


	public  void aceptarTipoConvenio(){
		String str_seleccionados=set_tipo_convenio.getSeleccionados();
		if(str_seleccionados!=null){
			//Inserto los empleados seleccionados en la tabla de participantes 
			TablaGenerica tab_generica= ser_contabilidad.getTablaTipoConvenio("true",str_seleccionados);		   		
			System.out.println(" tabla generica"+tab_generica.getSql());

			for(int i=0;i<tab_generica.getTotalFilas();i++){
				tab_tabla1.insertar();
				tab_tabla1.setValor("ide_cotie", tab_generica.getValor(i, "ide_cotie"));			

			}

			set_tipo_convenio.cerrar();
			utilitario.addUpdate("tab_tabla1");	
		}
	}

	public  void aceptarInstitucion(){
		String str_seleccionados=set_institucion.getSeleccionados();
		if(str_seleccionados!=null){
			//Inserto los empleados seleccionados en la tabla de participantes 
			TablaGenerica tab_generica= ser_contabilidad.getTablaTipoConvenio("true",str_seleccionados);		   		
			System.out.println(" tabla generica"+tab_generica.getSql());

			for(int i=0;i<tab_generica.getTotalFilas();i++){
				tab_tabla1.insertar();
				tab_tabla1.setValor("ide_geins", tab_generica.getValor(i, "ide_geins"));			

			}

			set_institucion.cerrar();
			utilitario.addUpdate("tab_tabla1");	
		}
	}

	@Override
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}
	@Override
	public void aceptarReporte(){
		//Pregunta que reporte selecciono el usuario
		if (rep_reporte.getReporteSelecionado().equals("Convenios")){
			if (rep_reporte.isVisible()){
				p_parametros=new HashMap();		
				rep_reporte.cerrar();
				p_parametros.clear();
				reporteSetEstados();
			}
			else if (set_estados.isVisible()){
				if(set_estados.getListaSeleccionados().size()>0){	
					p_parametros.put("ide_coest",pckUtilidades.CConversion.CInt(set_estados.getSeleccionados()));
					set_estados.cerrar();
					reporteSetTipoConvenio();
				}

			}
			else if (set_tipo_convenio.isVisible()){
				if (set_tipo_convenio.getListaSeleccionados().size()> 0 ){
					p_parametros.put("ide_cotie",pckUtilidades.CConversion.CInt(set_tipo_convenio.getSeleccionados()));
					set_tipo_convenio.cerrar();
					reporteSetInstitucion();
				}
			}
			else if(set_institucion.isVisible()){
				if(set_institucion.getListaSeleccionados().size()>0){	
					p_parametros.put("titulo", "Convenios Vigentes");
					p_parametros.put("ide_geins",pckUtilidades.CConversion.CInt(set_institucion.getSeleccionados()));
					self_reporte.setSeleccionFormatoReporte(p_parametros,rep_reporte.getPath());
					set_institucion.cerrar();
					self_reporte.dibujar();
				}					

			}
			else {
				utilitario.agregarMensajeInfo("No se puede continuar", "No ha Seleccionado Ningun Registro");

			}
		}
	}





	@Override
	public void guardar() {
		if (tab_tabla1.guardar()) {
			if (tab_tabla2.guardar()) {
				if (tab_tabla3.guardar()) {

				}
			}
		}
		guardarPantalla();
	}

	@Override
	public void eliminar() {
		utilitario.getTablaisFocus().eliminar();
	}
	@Override
	public void insertar() {
		if(tab_tabla1.isFocus()){
			tab_tabla1.insertar();
		}
		else if (tab_tabla2.isFocus()){

			set_persona.getTab_seleccion().setSql(ser_general.getTipoPersona("true",par_modulo_convenio));
			set_persona.getTab_seleccion().ejecutarSql();
			set_persona.dibujar();

		}
		else if (tab_tabla2.isFocus()){

			set_persona.getTab_seleccion().setSql(ser_general.getTipoPersona("true",par_modulo_convenio));
			set_persona.getTab_seleccion().ejecutarSql();
			set_persona.dibujar();

		}
		else if (tab_tabla3.isFocus()){
			tab_tabla3.insertar();
		}

	}

	public Arbol getArb_arbol() {
		return arb_arbol;
	}

	public void setArb_arbol(Arbol arb_arbol) {
		this.arb_arbol = arb_arbol;
	}

	public Tabla getTab_tabla1() {
		return tab_tabla1;
	}

	public void setTab_tabla1(Tabla tab_tabla1) {
		this.tab_tabla1 = tab_tabla1;
	}

	public Tabla getTab_tabla2() {
		return tab_tabla2;
	}

	public void setTab_tabla2(Tabla tab_tabla2) {
		this.tab_tabla2 = tab_tabla2;
	}

	public Tabla getTab_tabla3() {
		return tab_tabla3;
	}

	public void setTab_tabla3(Tabla tab_tabla3) {
		this.tab_tabla3 = tab_tabla3;
	}

	public SeleccionTabla getSet_tipo_persona() {
		return set_tipo_persona;
	}

	public void setSet_tipo_persona(SeleccionTabla set_tipo_persona) {
		this.set_tipo_persona = set_tipo_persona;
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

	public SeleccionTabla getSet_estados() {
		return set_estados;
	}

	public void setSet_estados(SeleccionTabla set_estados) {
		this.set_estados = set_estados;
	}

	public SeleccionTabla getSet_tipo_convenio() {
		return set_tipo_convenio;
	}

	public void setSet_tipo_convenio(SeleccionTabla set_tipo_convenio) {
		this.set_tipo_convenio = set_tipo_convenio;
	}

	public SeleccionTabla getSet_institucion() {
		return set_institucion;
	}

	public void setSet_institucion(SeleccionTabla set_institucion) {
		this.set_institucion = set_institucion;
	}

	public SeleccionTabla getSet_empleado() {
		return set_empleado;
	}

	public void setSet_empleado(SeleccionTabla set_empleado) {
		this.set_empleado = set_empleado;
	}

	public SeleccionTabla getSet_persona() {
		return set_persona;
	}

	public void setSet_persona(SeleccionTabla set_persona) {
		this.set_persona = set_persona;
	}

	public SeleccionTabla getSet_empleado_nuevo() {
		return set_empleado_nuevo;
	}

	public void setSet_empleado_nuevo(SeleccionTabla set_empleado_nuevo) {
		this.set_empleado_nuevo = set_empleado_nuevo;
	}



}
