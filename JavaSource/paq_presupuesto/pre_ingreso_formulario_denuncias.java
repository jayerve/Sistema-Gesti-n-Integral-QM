package paq_presupuesto;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Imagen;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_nomina.ejb.ServicioNomina;
import paq_precontractual.ejb.ServicioGeneralAdmPrecon;
import paq_presupuesto.ejb.ServicioPresupuesto;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;

public class pre_ingreso_formulario_denuncias extends Pantalla{
	
	private Tabla tab_pac=new Tabla();
	private Tabla tab_partida=new Tabla();
	private Tabla tab_responsable=new Tabla();
	private Tabla tab_archivo=new Tabla();
	private Tabla tab_anio=new Tabla();
	
	private Combo com_anio=new Combo();
	private SeleccionTabla set_poa=new SeleccionTabla();
	private SeleccionTabla set_empleado=new SeleccionTabla();
	private SeleccionTabla set_actualizar = new SeleccionTabla();
	
	private Confirmar con_guardar =new Confirmar();
	
	private Map p_parametros = new HashMap();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();
	private Map map_parametros = new HashMap();
	
	public static String par_sec_certificacion_pac;
	private String empleado;
	private static String ide_geare;
	
	@EJB
	private ServicioPresupuesto ser_presupuesto=(ServicioPresupuesto) utilitario.instanciarEJB(ServicioPresupuesto.class);
	@EJB
    private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	@EJB
    private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	@EJB
	private ServicioGeneralAdmPrecon ser_generalAdm = (ServicioGeneralAdmPrecon ) utilitario.instanciarEJB(ServicioGeneralAdmPrecon.class);

	public pre_ingreso_formulario_denuncias(){
		
		
		rep_reporte.setId("rep_reporte"); //id
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");//ejecuta el metodo al aceptar reporte
		agregarComponente(rep_reporte);//agrega el componente a la pantalla
		bar_botones.agregarReporte();//aparece el boton de reportes en la barra de botones
		self_reporte.setId("self_reporte"); //id
		agregarComponente(self_reporte);
		
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione el Año:"));
		bar_botones.agregarComponente(com_anio);	
		
		empleado=ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
		System.out.println("empleado: "+empleado);
		String departamento_actual=ser_generalAdm.servicioDepartamento("true", empleado);
		System.out.println("departamento_actual: "+departamento_actual);
		TablaGenerica tab_departamento= utilitario.consultar(departamento_actual);
		ide_geare=tab_departamento.getValor("IDE_GEARE");
		par_sec_certificacion_pac =utilitario.getVariable("p_sec_certificacion_pac");
	
		Tabulador tab_tabulador = new Tabulador();
        tab_tabulador.setId("tab_tabulador");
        
        tab_pac.setId("tab_pac");
        tab_pac.setHeader("BANDEJA FORMULARIOS DE DENUNCIA");
        tab_pac.setTabla("pre_pac","ide_prpac",1);
        tab_pac.getColumna("ide_copec").setCombo("cont_periodo_cuatrimestre","ide_copec","detalle_copec","");
        tab_pac.getColumna("ide_cotio").setCombo("cont_tipo_compra","ide_cotio","detalle_cotio","");
        tab_pac.getColumna("ide_coest").setCombo("cont_estado", "ide_coest","detalle_coest","");
        tab_pac.getColumna("ide_bounm").setCombo("bodt_unidad_medida","ide_bounm","detalle_bounm","");
        tab_pac.getColumna("tipo_prod_prpac").setCombo(utilitario.getListaTipoProducto());	
        tab_pac.getColumna("tregimen_prpac").setCombo(utilitario.getListaTipoRegimen());	
        tab_pac.getColumna("ide_adtic").setCombo("adq_tipo_contratacion","ide_adtic", "detalle_adtic","");
        tab_pac.getColumna("ide_adtic").setRequerida(true);
        tab_pac.getColumna("ide_bounm").setValorDefecto("1");
        tab_pac.getColumna("tipo_prod_prpac").setValorDefecto("1");
        tab_pac.getColumna("tregimen_prpac").setValorDefecto("1");
        tab_pac.getColumna("publicado_prpac").setValorDefecto("true");
        tab_pac.getColumna("activo_prpac").setValorDefecto("true");
        tab_pac.getColumna("cantidad_prpac").setValorDefecto("0");
        tab_pac.getColumna("valor_unitario_prpac").setValorDefecto("0");
        tab_pac.getColumna("valor_total_prpac").setValorDefecto("0");
        tab_pac.getColumna("nro_orden_prpac").setValorDefecto("0");
        
        tab_pac.getColumna("valor_total_prpac").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
        tab_pac.getColumna("valor_total_prpac").setEtiqueta();
        tab_pac.getColumna("cantidad_prpac").setMetodoChange("calcular");
        tab_pac.getColumna("valor_unitario_prpac").setMetodoChange("calcular");
        
        tab_pac.getColumna("ide_geare").setCombo("gen_area","ide_geare","detalle_geare","");
        tab_pac.getColumna("fecha_inicio_prpac").setValorDefecto(utilitario.getFechaActual());
		
        tab_pac.setTipoFormulario(true);  //formulario 
	    tab_pac.getGrid().setColumns(4); //hacer  columnas 
        tab_pac.getColumna("ide_geani").setVisible(false);
        tab_pac.setCondicion("ide_geani=-1");
        tab_pac.agregarRelacion(tab_partida);//agraga relacion para los tabuladores
        tab_pac.agregarRelacion(tab_responsable);
        tab_pac.agregarRelacion(tab_archivo);
        tab_pac.dibujar();
        
		PanelTabla pat_pac=new PanelTabla();
		pat_pac.setPanelTabla(tab_pac);
			
		//tabla 2
		tab_partida.setId("tab_partida");
		tab_partida.setHeader("PARTIDA DE CONTRATACION (PAC)");
		tab_partida.setIdCompleto("tab_tabulador:tab_partida");
		tab_partida.setTabla("pre_partida_pac","ide_prpap",2);
		tab_partida.getColumna("ide_prcla").setAncho(50);
		tab_partida.getColumna("ide_prcla").setCombo("select ide_prcla,codigo_clasificador_prcla,descripcion_clasificador_prcla from pre_clasificador order by codigo_clasificador_prcla");
		tab_partida.getColumna("ide_prcla").setAutoCompletar();
		tab_partida.getColumna("ide_prcla").setLectura(true);
        
		tab_partida.getColumna("ide_prpoa").setAncho(50);
		tab_partida.getColumna("ide_prpoa").setCombo(ser_presupuesto.getPoaTodos());
		tab_partida.getColumna("ide_prpoa").setAutoCompletar();
		tab_partida.getColumna("ide_prpoa").setLectura(true);
		//tab_partida.getColumna("valor_certificado_prpap").setLectura(true);
		tab_partida.getColumna("anio_prpap").setCombo(utilitario.getListaAnios());
		tab_partida.getColumna("certificado_poa_prpap").setLectura(true);
		
		tab_partida.getColumna("valor_prpap").setMetodoChange("cambiaValor");
		//tab_partida.getColumna("ide_prcla").setUnico(true);
		//tab_partida.getColumna("ide_prpac").setUnico(true);
		tab_partida.getColumna("activo_prpap").setValorDefecto("true");
		tab_partida.getColumna("portal_prpap").setValorDefecto("true");
		tab_partida.getColumna("valor_prpap").setValorDefecto("0");
		tab_partida.setColumnaSuma("valor_prpap");
		tab_partida.dibujar();
		
		PanelTabla pat_partida=new PanelTabla();
		pat_partida.setPanelTabla(tab_partida);
		
		
		//RESPONSABLE PAC
		tab_responsable.setId("tab_responsable");
		tab_responsable.setHeader("RESPONSABLES FORMULARIO DE DENUNCIAS");
		tab_responsable.setIdCompleto("tab_tabulador:tab_responsable");
		tab_responsable.setTabla("pre_responsable_contratacion","ide_prrec",4);
		tab_responsable.getColumna("ide_prcop").setVisible(false);
		tab_responsable.getColumna("IDE_GTEMP").setVisible(false);
		//tab_responsable.setCampoForanea("ide_prpac");
		tab_responsable.getColumna("IDE_GEEDP").setCombo(ser_nomina.servicioEmpleadoContrato("true,false"));
		tab_responsable.getColumna("IDE_GEEDP").setAutoCompletar();
		tab_responsable.getColumna("IDE_GEEDP").setLectura(true);
		//tab_responsable.getColumna("IDE_GEEDP").setUnico(true);
		//tab_responsable.getColumna("ide_prrec").setUnico(true);
		//tab_responsable.setCampoForanea("ide_prcop");
		tab_responsable.setCondicion("ide_prcop is null");
		tab_responsable.getColumna("activo_prrec").setValorDefecto("true");
		//tab_responsable.setLectura(true);
		tab_responsable.dibujar();
		PanelTabla pat_panel2 = new PanelTabla();
		pat_panel2.setPanelTabla(tab_responsable);
		
		/*Imagen fondo= new Imagen(); 
        fondo.setStyle("text-aling:center;position:absolute;top:100px;left:590px;");
        fondo.setValue("imagenes/logo.png");
        pat_partida.setWidth("100%");
        pat_partida.getChildren().add(fondo);*/
		
		 //tabla 3
	    tab_archivo.setId("tab_archivo");
	    tab_archivo.setHeader("ARCHIVO");
        tab_archivo.setIdCompleto("tab_tabulador:tab_archivo");
        tab_archivo.setTipoFormulario(true);
        tab_archivo.getGrid().setColumns(4);
        tab_archivo.setTabla("pre_archivo","ide_prarc",3);
        tab_archivo.getColumna("foto_prarc").setUpload("fotos");
        tab_archivo.dibujar();
        PanelTabla pat_archivo= new PanelTabla();
        pat_archivo.setPanelTabla(tab_archivo);
        
        Imagen fondo1= new Imagen(); // imagen de fondo
        fondo1.setStyle("text-aling:center;position:absolute;top:100px;left:490px;");
        fondo1.setValue("imagenes/logo.png");
        pat_archivo.setWidth("100%");
        pat_archivo.getChildren().add(fondo1);

       //tab_tabulador.agregarTab("PARTIDA",pat_partida);//tabuladores 
        tab_tabulador.agregarTab("RESPONSABLE",pat_panel2); 
        tab_tabulador.agregarTab("ARCHIVO",pat_archivo);
        
		Division div_division=new Division();
		div_division.dividir2(pat_pac,tab_tabulador, "50%", "h");
		agregarComponente(div_division);

	    //boton buscar poa
		Boton bot_buscar=new Boton();
		bot_buscar.setIcon("ui-icon-person");
		bot_buscar.setValue("Buscar POA");
		bot_buscar.setMetodo("importarPoa");
		//bar_botones.agregarBoton(bot_buscar);

		set_poa.setId("set_poa");
		set_poa.setSeleccionTabla(ser_presupuesto.getPoa("-1","true","true",0,false),"ide_prpoa");
		set_poa.setTitle("Seleccione Poa");
		set_poa.getTab_seleccion().getColumna("codigo_clasificador_prcla").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("descripcion_clasificador_prcla").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("DETALLE_PROGRAMA").setFiltroContenido();//pone filtro
		set_poa.getTab_seleccion().getColumna("PROGRAMA").setFiltroContenido();
		set_poa.getTab_seleccion().getColumna("DETALLE_PROYECTO").setFiltroContenido();
		set_poa.getTab_seleccion().getColumna("PROYECTO").setFiltroContenido();
		set_poa.getTab_seleccion().getColumna("DETALLE_PRODUCTO").setFiltroContenido();
		set_poa.getTab_seleccion().getColumna("PRODUCTO").setFiltroContenido();
		set_poa.getTab_seleccion().getColumna("DETALLE_ACTIVIDAD").setFiltroContenido();
		set_poa.getTab_seleccion().getColumna("ACTIVIDAD").setFiltroContenido();
		set_poa.getTab_seleccion().getColumna("DETALLE_SUBACTIVIDAD").setFiltroContenido();
		set_poa.getTab_seleccion().getColumna("SUBACTIVIDAD").setFiltroContenido();
		set_poa.getTab_seleccion().getColumna("CODIGO_SUBACTIVIDAD").setFiltroContenido();
		set_poa.getTab_seleccion().getColumna("NUM_RESOLUCION_PRPOA").setFiltroContenido();
		set_poa.setRadio();
		set_poa.getBot_aceptar().setMetodo("aceptarPoa");
		set_poa.setFooter("Lineas del POA en base al Area registrada en la Linea del PAC guardada...");
		agregarComponente(set_poa);
		
		//ojo con el actualizar los totales // 
		
		
		//bara el boton empleado 
		Boton bot_empleado=new Boton();
		bot_empleado.setIcon("ui-icon-person");
		bot_empleado.setValue("Agregar Responsable");
		bot_empleado.setMetodo("importarEmpleado");
		bar_botones.agregarBoton(bot_empleado);
		con_guardar.setId("con_guardar");
		agregarComponente(con_guardar);
				
		set_empleado.setId("set_empleado");
		set_empleado.setSeleccionTabla(ser_nomina.servicioEmpleadoContrato("true"),"ide_geedp");
		set_empleado.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("nombres_apellidos").setFiltro(true);
		set_empleado.setTitle("Seleccione un Empleado");
		set_empleado.getBot_aceptar().setMetodo("aceptarEmpleado");
		agregarComponente(set_empleado);
	
		// Boton Actualizar Representante
		/*Boton bot_actualizar=new Boton();
		bot_actualizar.setIcon("ui-icon-person");
		bot_actualizar.setValue("Actualizar Responsable");
		bot_actualizar.setMetodo("actualizarResponsable");
		bar_botones.agregarBoton(bot_actualizar);*/
		
		set_actualizar.setId("set_actualizar");
		set_actualizar.setSeleccionTabla(ser_nomina.servicioEmpleadoContrato("true"),"ide_geedp");
		set_actualizar.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
		set_actualizar.getTab_seleccion().getColumna("nombres_apellidos").setFiltro(true);
		set_actualizar.setRadio();
		set_actualizar.getBot_aceptar().setMetodo("modificarResponsable");
		agregarComponente(set_actualizar);	
	}

	public void cambiaValor(AjaxBehaviorEvent evt){
		tab_partida.modificar(evt);
		tab_partida.setColumnaSuma("valor_prpap");
		utilitario.addUpdate("tab_tabulador:tab_partida");
	}
	
	public  void aceptarPoa(){
		String str_seleccionados= set_poa.getValorSeleccionado();

		if (str_seleccionados!=null){
			TablaGenerica tab_poa = ser_presupuesto.getTablaGenericaPoa(str_seleccionados);		
			TablaGenerica taAni = utilitario.consultar("SELECT ide_geani,detalle_geani FROM gen_anio WHERE ide_geani=" + com_anio.getValue().toString());
			
			String anio=taAni.getValor("detalle_geani");
			tab_partida.insertar();
			tab_partida.setValor("ide_prpoa",tab_poa.getValor("ide_prpoa"));
			tab_partida.setValor("ide_prcla",tab_poa.getValor("ide_prcla"));
			//tab_partida.setValor("valor_prpap",tab_poa.getValor("presupuesto_codificado_prpoa"));
			tab_partida.setValor("valor_prpap",tab_poa.getValor("saldo"));
			tab_partida.setValor("anio_prpap",anio);
			
			set_poa.cerrar();
			utilitario.addUpdate("tab_pac");
		}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
		}
	
	}

    //importar poa
	public void importarPoa(){
		//System.out.println(" ingresar al importar");
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un año", "");
			return;
		}
		
		if(tab_pac.getTotalFilas()<1)
		{
			utilitario.agregarMensajeInfo("Debe guardar un registro de PAC antes de continuar...", "");
			return;
		}

		if (!verificarSiSePuedeEditar()) {
			return;
		}

		set_poa.getTab_seleccion().setSql(ser_presupuesto.getPoa(com_anio.getValue().toString(),"true","true", pckUtilidades.CConversion.CInt(tab_pac.getValor("ide_geare")), false ));
		set_poa.getTab_seleccion().ejecutarSql();
		set_poa.dibujar();

	}
	
	public void calcular(AjaxBehaviorEvent evt) {
		tab_pac.modificar(evt); //Siempre es la primera linea
		
		double dou_cantidad=pckUtilidades.CConversion.CDbl(tab_pac.getValor("cantidad_prpac"));
		double dou_valor_unitario=pckUtilidades.CConversion.CDbl(tab_pac.getValor("valor_unitario_prpac"));
		
		tab_pac.setValor("valor_total_prpac",utilitario.getFormatoNumero((dou_cantidad*dou_valor_unitario),2));
		utilitario.addUpdate("tab_pac");
	}
	
	public void importarEmpleado(){
		
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un año", "");
			return;
		}
		
		if (tab_pac.isEmpty()) {
			utilitario.agregarMensajeInfo("Debe ingresar un registro en el PAC", "");
			return;
		}
							
		if (!verificarSiSePuedeEditar()) {
			return;
		}
							
		set_empleado.getTab_seleccion().setSql(ser_nomina.servicioEmpleadoContrato("true"));
		set_empleado.getTab_seleccion().ejecutarSql();
		set_empleado.dibujar();
	}
			

	public void aceptarEmpleado(){
		String str_seleccionados=set_empleado.getSeleccionados();
		if(str_seleccionados!=null){
			
			for(int i=0;i<tab_responsable.getTotalFilas();i++){	
				tab_responsable.setValor(i,"activo_prrec", "false");	
				tab_responsable.modificar(i);//para que haga el update
			}
			
			//Inserto los empleados seleccionados en la tabla de resposable d econtratacion 
			TablaGenerica tab_empleado_responsable = ser_nomina.ideEmpleadoContrato(str_seleccionados,"true");	
			tab_empleado_responsable.imprimirSql();
						
			for(int i=0;i<tab_empleado_responsable.getTotalFilas();i++){
				tab_responsable.insertar();
				tab_responsable.setValor("IDE_GEEDP", tab_empleado_responsable.getValor(i, "IDE_GEEDP"));			
				tab_responsable.setValor("IDE_GTEMP", tab_empleado_responsable.getValor(i, "IDE_GTEMP"));			
			}
			tab_pac.guardar();
			tab_responsable.guardar();
			guardarPantalla();
			set_empleado.cerrar();
			utilitario.addUpdate("tab_responsable");			
		}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar almenos un registro", "");
		}
	}
	
	//reporte
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		rep_reporte.dibujar();
	}
		
	public void aceptarReporte(){
		if(rep_reporte.getReporteSelecionado().equals("CERTIFICACION PAC"))
		{			
			if (rep_reporte.isVisible()){
				rep_reporte.cerrar();	
				
				TablaGenerica tab_anio =utilitario.consultar("SELECT ide_geani, detalle_geani FROM gen_anio where ide_geani="+com_anio.getValue());				
				String anio=tab_anio.getValor("detalle_geani");
				String secuencial_doc="CERT-"+ser_contabilidad.numeroSecuencial(par_sec_certificacion_pac)+"-"+anio;
				utilitario.agregarMensaje("Guardando secuencial ", "Nro: "+secuencial_doc);
				ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_sec_certificacion_pac), par_sec_certificacion_pac);
				
				p_parametros=new HashMap();		
				p_parametros.put("ide_prpac",pckUtilidades.CConversion.CInt(tab_pac.getValor("ide_prpac")));
				p_parametros.put("nro_documento",secuencial_doc);
				self_reporte.setSeleccionFormatoReporte(p_parametros,rep_reporte.getPath());
				self_reporte.dibujar();				
			}
			else{
				utilitario.agregarMensajeInfo("No se puede continuar", "No ha Seleccionado Ningun Registro");	
			}		
		}
	}

	public boolean verificarSiSePuedeEditar() {
		String ide_prpac = tab_pac.getValor("ide_prpac");
		if (ide_prpac == null || ide_prpac == "") {
			// El PAC no ha sido guardada aún
			return true;
		}

		TablaGenerica tab_precon_precontractual =utilitario.consultar("SELECT ide_prpre, descripcion_prpre,fecha_prpre,responsable_actual_prpre FROM precon_precontractual where ide_prpac="+ide_prpac);		
		if (tab_precon_precontractual.getTotalFilas() > 0) {
			// Existen ya el proceso en ejecición
			utilitario.agregarMensajeError("No puede editar el Proceso de Contratación", 
					"Este proceso ya fue iniciado el: "+tab_precon_precontractual.getValor( "fecha_prpre")
					+" Responsable: "+tab_precon_precontractual.getValor( "responsable_actual_prpre"));
			return false;
		}
		// No existe proceso iniciado, se puede modificar
		return true;
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if (com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("No se puede insertar", "Debe Seleccionar un Anio");
			return;
		}
		if(tab_pac.isFocus()){
			//tab_pac.insertar();
			//tab_pac.setValor("ide_geani",com_anio.getValue()+"");
			
			tab_pac.insertar();
			tab_pac.setValor("ide_coest","2");
			tab_pac.setValor("ide_geani",com_anio.getValue()+"");
			tab_pac.setValor("ide_geare",pckUtilidades.CConversion.CInt(ide_geare)+"");
			utilitario.agregarMensajeInfo("Nota", "Debe Seleccionar la opcion Buscar POA para agregar detalles.");
			//return;
		}
		else if(tab_partida.isFocus()){
			tab_partida.insertar();
		}	
		else if(tab_archivo.isFocus()){
			tab_archivo.insertar();
				
		}else if (tab_responsable.isFocus()) {
			tab_responsable.insertar();

		}
			
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if (!verificarSiSePuedeEditar()) {
			return;
		}
		
		if( tab_pac.guardar()){
			if(!pckUtilidades.CConversion.CBol(tab_partida.getValor("certificado_poa_prpap")))
				tab_partida.guardar();
			tab_responsable.guardar();
			tab_archivo.guardar();
   		}
     
		guardarPantalla();
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		if (!verificarSiSePuedeEditar()) {
			return;
		}
		if(tab_pac.isFocus()){
			tab_pac.eliminar();
			}
		else if(tab_partida.isFocus()){
			if(!pckUtilidades.CConversion.CBol(tab_partida.getValor("certificado_poa_prpap")))
				tab_partida.eliminar();
		}
		else if(tab_archivo.isFocus()){
			tab_archivo.eliminar();
		}
		else if(tab_responsable.isFocus()){
			tab_responsable.eliminar();
		}
		
	}
	
	public void seleccionaElAnio(){
		if(com_anio.getValue()!=null){
			//if(empleado.equals("22") || empleado.equals("530") || empleado.equals("1155") || empleado.equals("636") || empleado.equals("508") || empleado.equals("269"))
				tab_pac.setCondicion("ide_geani="+com_anio.getValue());
			//else
			//	tab_pac.setCondicion("ide_geani="+com_anio.getValue()+" and ide_geare="+pckUtilidades.CConversion.CInt(ide_geare));
			
			tab_pac.ejecutarSql();
			tab_partida.ejecutarValorForanea(tab_pac.getValorSeleccionado());
			tab_archivo.ejecutarValorForanea(tab_pac.getValorSeleccionado());
			tab_responsable.ejecutarValorForanea(tab_pac.getValorSeleccionado());
		}
		else {
			tab_pac.setCondicion("ide_geani=-1");
			tab_pac.ejecutarSql();
			tab_anio.ejecutarValorForanea(tab_pac.getValorSeleccionado());
			tab_archivo.ejecutarValorForanea(tab_pac.getValorSeleccionado());
			tab_responsable.ejecutarValorForanea(tab_pac.getValorSeleccionado());
		}
	}

	public Tabla getTab_pac() {
		return tab_pac;
	}

	public void setTab_pac(Tabla tab_pac) {
		this.tab_pac = tab_pac;
	}

	public Tabla getTab_partida() {
		return tab_partida;
	}

	public void setTab_partida(Tabla tab_partida) {
		this.tab_partida = tab_partida;
	}

	public Tabla getTab_archivo() {
		return tab_archivo;
	}

	public void setTab_archivo(Tabla tab_archivo) {
		this.tab_archivo = tab_archivo;
	}
	public Tabla getTab_anio() {
		return tab_anio;
	}
	public void setTab_anio(Tabla tab_anio) {
		this.tab_anio = tab_anio;
	}
	public Combo getCom_anio() {
		return com_anio;
	}
	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}

	public SeleccionTabla getSet_poa() {
		return set_poa;
	}

	public void setSet_poa(SeleccionTabla set_poa) {
		this.set_poa = set_poa;
	}

	public Tabla getTab_responsable() {
		return tab_responsable;
	}

	public void setTab_responsable(Tabla tab_responsable) {
		this.tab_responsable = tab_responsable;
	}

	public SeleccionTabla getSet_empleado() {
		return set_empleado;
	}

	public void setSet_empleado(SeleccionTabla set_empleado) {
		this.set_empleado = set_empleado;
	}

	public SeleccionTabla getSet_actualizar() {
		return set_actualizar;
	}

	public void setSet_actualizar(SeleccionTabla set_actualizar) {
		this.set_actualizar = set_actualizar;
	}

	public Confirmar getCon_guardar() {
		return con_guardar;
	}

	public void setCon_guardar(Confirmar con_guardar) {
		this.con_guardar = con_guardar;
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

	

}
