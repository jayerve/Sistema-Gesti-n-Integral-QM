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
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_nomina.ejb.ServicioNomina;
import paq_presupuesto.ejb.ServicioPresupuesto;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;

public class pre_certificacion_futura extends Pantalla{
	private Tabla tab_certificacion_futura=new Tabla();
	private Tabla tab_poa_certificacion_futura=new Tabla();
	private Tabla tab_tablaXls = new Tabla();
	private SeleccionTabla set_poa=new SeleccionTabla();
	private SeleccionTabla set_cert=new SeleccionTabla();
	private Combo com_anio=new Combo();
	private String empleado;
	public static String par_sec_certificacion_fut;
	private static String empleado_responsable;

	///reporte
	private Map p_parametros = new HashMap();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();
	private Map map_parametros = new HashMap();

	// Confirmación 2016-04-06
	private Confirmar con_eliminar = new Confirmar();
	private Confirmar con_guardar=new Confirmar();
	// Confirmación 2016-04-06
  
  
    @EJB
    private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
    @EJB
    private ServicioPresupuesto ser_presupuesto=(ServicioPresupuesto)utilitario.instanciarEJB(ServicioPresupuesto.class);
    @EJB
    private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
    @EJB
    private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
			
	
	public pre_certificacion_futura(){
		// Confirmación 2016-04-06
		con_eliminar.setId("con_eliminar");
		con_eliminar.setMessage("EL REGISTRO SE ELIMINARÁ DE FORMA PERMANENTE. ¿DESEA CONTINUAR?");
		con_eliminar.setTitle("CONFIRMACION ELIMINACIÓN");
        con_eliminar.getBot_aceptar().setMetodo("eliminar");
		agregarComponente(con_eliminar);
		
		con_guardar.setId("con_guardar");
		con_guardar.setMessage("¿Esta Seguro de Generar la Certificación Futura?");
		con_guardar.setTitle("Confirmación de Generación");
		con_guardar.getBot_aceptar().setMetodo("generarCert");
		agregarComponente(con_guardar);
		// Confirmación 2016-04-06
		
		empleado=ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
		par_sec_certificacion_fut =utilitario.getVariable("p_modulo_secuencialcert_futura");
		empleado_responsable=ser_contabilidad.empleadoResponsable(par_sec_certificacion_fut,empleado);
		System.out.println("empleado_responsable"+empleado_responsable);
		if(empleado_responsable==null ||empleado_responsable.isEmpty()){
			utilitario.agregarNotificacionInfo("Mensaje", "No existe usuario responsable para el registro de certificaciones plurianuales");
			return;
		}
		
		///reporte
		rep_reporte.setId("rep_reporte"); //id
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");//ejecuta el metodo al aceptar reporte
		agregarComponente(rep_reporte);//agrega el componente a la pantalla
		bar_botones.agregarReporte();//aparece el boton de reportes en la barra de botones
		self_reporte.setId("self_reporte"); //id
		agregarComponente(self_reporte);
		
		
		///BOTON COMBO
		//empleado=ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
		System.out.println("empleado"+empleado);
		if(empleado==null ||empleado.isEmpty()){
			utilitario.agregarNotificacionInfo("Mensaje", "No existe usuario registrado para el registro de certificaciones futuras");
			return;
		}

		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);

		
		tab_certificacion_futura.setId("tab_certificacion_futura");
		tab_certificacion_futura.setHeader("CERTIFICACION PRESUPUESTARIA FUTURA");
		tab_certificacion_futura.setTabla("pre_certificacion_futura", "ide_prcef", 1);
		tab_certificacion_futura.setCampoOrden("ide_prcef desc");
		tab_certificacion_futura.getColumna("IDE_GEEDP").setCombo(ser_nomina.servicioEmpleadoContrato("true,false"));
		tab_certificacion_futura.getColumna("IDE_GEEDP").setAutoCompletar();	
		tab_certificacion_futura.getColumna("ide_coest").setCombo(ser_contabilidad.getModuloEstados("true", par_sec_certificacion_fut));
		tab_certificacion_futura.getColumna("ide_geare").setCombo("gen_area","ide_geare","detalle_geare","");
		tab_certificacion_futura.getColumna("ide_gtemp").setCombo(ser_nomina.servicioEmpleadosActivos("true,false"));
		tab_certificacion_futura.getColumna("ide_gtemp").setLectura(true);
		tab_certificacion_futura.getColumna("ide_gtemp").setAutoCompletar();
		tab_certificacion_futura.getColumna("ide_prcer").setCombo(ser_presupuesto.getCertificacion("true,false"));
		tab_certificacion_futura.getColumna("ide_prcer").setAutoCompletar();
		tab_certificacion_futura.getColumna("ide_prcer").setLectura(true);
		tab_certificacion_futura.getColumna("activo_prcef").setValorDefecto("true");
		tab_certificacion_futura.getColumna("activo_prcef").setLectura(true);
		tab_certificacion_futura.getColumna("ide_geani").setCombo(ser_contabilidad.getAnio("true,false","true,false"));
		tab_certificacion_futura.setCondicion("ide_geani=-1"); 
		tab_certificacion_futura.getColumna("ide_geani").setVisible(false);
		
		tab_certificacion_futura.getColumna("monto_aprobado_prcef").setEtiqueta();
		tab_certificacion_futura.getColumna("monto_aprobado_prcef").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:black");
		tab_certificacion_futura.getColumna("monto_aprobado_prcef").setValorDefecto("0.00");
		tab_certificacion_futura.setTipoFormulario(true);
		tab_certificacion_futura.getGrid().setColumns(4);
		tab_certificacion_futura.agregarRelacion(tab_poa_certificacion_futura);
		tab_certificacion_futura.dibujar();
		PanelTabla pat_certificacion=new PanelTabla();
		pat_certificacion.setPanelTabla(tab_certificacion_futura);
		
		////// poa certificacion
		tab_poa_certificacion_futura.setId("tab_poa_certificacion_futura");
		tab_poa_certificacion_futura.setHeader("POA CERTIFICACION");
		tab_poa_certificacion_futura.setTabla("pre_poa_certificacion_futura", "ide_prpcf", 2);
		tab_poa_certificacion_futura.getColumna("ide_prpoa").setCombo(ser_presupuesto.getPoaTodos());
		tab_poa_certificacion_futura.getColumna("ide_prpoa").setAutoCompletar();
		tab_poa_certificacion_futura.getColumna("ide_prpoa").setLectura(true);
		tab_poa_certificacion_futura.getColumna("ide_prpoa").setLongitud(90);
		tab_poa_certificacion_futura.getColumna("ide_prfuf").setCombo("pre_fuente_financiamiento","ide_prfuf","detalle_prfuf","");
		tab_poa_certificacion_futura.getColumna("ide_prfuf").setAutoCompletar();
		tab_poa_certificacion_futura.getColumna("ide_prfuf").setLectura(true);		
		tab_poa_certificacion_futura.getColumna("anio_prpcf").setCombo(utilitario.getListaAnios());
		tab_poa_certificacion_futura.getColumna("activo_prpcf").setValorDefecto("true");
		tab_poa_certificacion_futura.getColumna("activo_prpcf").setLectura(true);
		tab_poa_certificacion_futura.getColumna("valor_certificado_prpcf").setMetodoChange("calcular");
		tab_poa_certificacion_futura.getColumna("ide_prpoa").setAncho(50);
		//tab_poa_certificacion_futura.getColumna("saldo_certificacion_prpoc").setEtiqueta();
		//tab_poa_certificacion_futura.getColumna("saldo_certificacion_prpoc").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_poa_certificacion_futura.setColumnaSuma("valor_certificado_prpcf"); 
		tab_poa_certificacion_futura.dibujar();
		PanelTabla pat_poa_certi=new PanelTabla();
		pat_poa_certi.setPanelTabla(tab_poa_certificacion_futura);
		
		
		Division div_divi=new Division();
		div_divi.dividir2(pat_certificacion, pat_poa_certi, "50%", "H");
		agregarComponente(div_divi);
		
		
		/////boton buscar poa
		Boton bot_buscar=new Boton();
		bot_buscar.setIcon("ui-icon-person");
		bot_buscar.setValue("Buscar POA");
		bot_buscar.setMetodo("importarPoa");
		bar_botones.agregarBoton(bot_buscar);

		set_poa.setId("set_poa");
		set_poa.setSeleccionTabla(ser_presupuesto.getPoa("-1","true","true"),"ide_prpoa");
		set_poa.setTitle("Seleccione Poa");
		set_poa.getTab_seleccion().getColumna("codigo_clasificador_prcla").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("descripcion_clasificador_prcla").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("DETALLE_PROGRAMA").setFiltro(true);//pone filtro
		set_poa.getTab_seleccion().getColumna("PROGRAMA").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("DETALLE_PROYECTO").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("PROYECTO").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("DETALLE_PRODUCTO").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("PRODUCTO").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("DETALLE_ACTIVIDAD").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("ACTIVIDAD").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("DETALLE_SUBACTIVIDAD").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("SUBACTIVIDAD").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("CODIGO_SUBACTIVIDAD").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("NUM_RESOLUCION_PRPOA").setFiltro(true);
		set_poa.getBot_aceptar().setMetodo("aceptarPoa");
		agregarComponente(set_poa);
		
		/////boton buscar certificacion
		Boton bot_buscar_cert=new Boton();
		bot_buscar_cert.setIcon("ui-icon-link");
		bot_buscar_cert.setValue("Enlazar Certificación");
		bot_buscar_cert.setMetodo("importarCert");
		bar_botones.agregarBoton(bot_buscar_cert);
		
		set_cert.setId("set_cert");
		set_cert.setSeleccionTabla(ser_presupuesto.getCertificacion("-1","true,false"),"ide_prcer");
		set_cert.setTitle("Seleccione una Certificacion");
		set_cert.getTab_seleccion().getColumna("nro_certificacion_prcer").setFiltroContenido();
		set_cert.getTab_seleccion().getColumna("num_documento_prcer").setFiltroContenido();
		set_cert.getTab_seleccion().getColumna("detalle_prcer").setFiltroContenido();
		set_cert.setRadio();
		set_cert.getBot_aceptar().setMetodo("aceptarCertificacion");
		agregarComponente(set_cert);

		Boton bot_excel=new Boton();
	    bot_excel.setValue("Exportar EXCEL");
	    bot_excel.setIcon("ui-icon-calculator");
	    bot_excel.setAjax(false);
	    bot_excel.setMetodo("exportarExcel");
	    bar_botones.agregarBoton(bot_excel);
	    
	    Boton bot_liquidar=new Boton();
	    bot_liquidar.setIcon("ui-icon-notice");
	    bot_liquidar.setValue("Liquidar");
	    bot_liquidar.setMetodo("liquidar");
		bar_botones.agregarBoton(bot_liquidar);
	}
	
	
	public void seleccionaElAnio(){
		if(com_anio.getValue()!=null){
			tab_certificacion_futura.setCondicion("ide_geani="+com_anio.getValue());
			tab_certificacion_futura.ejecutarSql();
			tab_poa_certificacion_futura.ejecutarValorForanea(tab_certificacion_futura.getValorSeleccionado());
		}
		else{
			utilitario.agregarMensajeInfo("Selecione un año", "");
		}
	}
	
	public void calcular(AjaxBehaviorEvent evt) 
	{
		tab_poa_certificacion_futura.modificar(evt); //Siempre es la primera linea
		calcularCertificacion();
	}
	
	public void calcularCertificacion()
	{
		System.out.println("calcularCertificacion ");
		String valorcert=tab_poa_certificacion_futura.getSumaColumna("valor_certificado_prpcf")+"";

		tab_certificacion_futura.setValor("monto_aprobado_prcef",utilitario.getFormatoNumero(valorcert,2));
		tab_certificacion_futura.modificar(tab_certificacion_futura.getFilaActual());//para que haga el update
		utilitario.addUpdateTabla(tab_certificacion_futura, "monto_aprobado_prcef", "");		
	}
	
	public void exportarExcel()
	{
	      if(com_anio.getValue()==null){
	        utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
	        return;
	      }
	      tab_tablaXls.setSql(ser_presupuesto.getRptCertificacionesFuturas(com_anio.getValue().toString()));
	      tab_tablaXls.ejecutarSql();
	      tab_tablaXls.exportarXLS();
    }
	
	////importar poa
	public void importarPoa(){
		System.out.println(" ingresar al importar "+tab_certificacion_futura.getTotalFilas());
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un año", "");
			return;
		}
		else if(tab_certificacion_futura.isEmpty() || tab_certificacion_futura.getTotalFilas()<=0){
			utilitario.agregarMensajeInfo("No puede buscar un POA", "Debe tener una Certificación Presupuestaria futura guardada..");
			return;
		}

		set_poa.getTab_seleccion().setSql(ser_presupuesto.getPoa(com_anio.getValue().toString(),"true","true"));
		set_poa.getTab_seleccion().ejecutarSql();
		set_poa.dibujar();

	}

	public void aceptarPoa(){
		String str_seleccionados= set_poa.getSeleccionados();

		if (str_seleccionados!=null){
			if(!validar())
				return;
			TablaGenerica tab_poa = ser_presupuesto.getTablaGenericaPoa(str_seleccionados);		
			for(int i=0;i<tab_poa.getTotalFilas();i++){
				TablaGenerica saldo_poa=utilitario.consultar(ser_presupuesto.getSaldoPoa(tab_poa.getValor( i,"ide_prpoa")));
				for(int j=0;j<saldo_poa.getTotalFilas();j++)
				{
					tab_poa_certificacion_futura.insertar();
					tab_poa_certificacion_futura.setValor("ide_prpoa", tab_poa.getValor( i,"ide_prpoa"));
					tab_poa_certificacion_futura.setValor("valor_certificado_prpcf", "0.00");
					tab_poa_certificacion_futura.setValor("ide_prfuf", saldo_poa.getValor(j,"ide_prfuf"));
					//calcularCertificacion();
				}
				
			}
			calcularCertificacion();
			generarCert();
			set_poa.cerrar();
			utilitario.addUpdate("tab_poa_certificacion_futura");
		}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
		}
	
	}
	
	public void importarCert(){
		System.out.println(" ingresar al importar "+tab_certificacion_futura.getTotalFilas());
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un año", "");
			return;
		}
		else if(tab_certificacion_futura.isEmpty() || tab_certificacion_futura.getTotalFilas()<=0){
			utilitario.agregarMensajeInfo("No puede enlazar", "Debe tener una Certificación Presupuestaria futura guardada..");
			return;
		}

		set_cert.getTab_seleccion().setSql(ser_presupuesto.getCertificacion(com_anio.getValue().toString(),"true,false"));
		set_cert.getTab_seleccion().ejecutarSql();
		set_cert.dibujar();

	}
	
	public void aceptarCertificacion()
	{
		String str_ide_prcer= set_cert.getValorSeleccionado();

		if (str_ide_prcer!=null){
			if(!validar())
				return;
			tab_certificacion_futura.setValor("ide_prcer",str_ide_prcer);
			tab_certificacion_futura.modificar(tab_certificacion_futura.getFilaActual());//para que haga el update
			System.out.println("ide_prcer: "+str_ide_prcer);
			tab_certificacion_futura.guardar();
			set_cert.cerrar();
			guardarPantalla();
			utilitario.addUpdate("tab_certificacion_futura");
		}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
		}
	}
	
	public void liquidar()
	{
		tab_certificacion_futura.setValor("ide_coest", "32"); //estado liquidada
		tab_certificacion_futura.modificar(tab_certificacion_futura.getFilaActual());
		tab_certificacion_futura.guardar();
		guardarPantalla();
		utilitario.agregarMensajeInfo("Certificación Liquidada", "Proceso ejecutado correctamente..");
	}

	//reporte
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}
	
	public void aceptarReporte(){
		if(rep_reporte.getReporteSelecionado().equals("Certificacion Futura"));{
			TablaGenerica tab_cuadro_techo=utilitario.consultar(ser_presupuesto.getTecho_futura(com_anio.getValue().toString()));
			System.out.println("getTotalFilas "+tab_cuadro_techo.getTotalFilas());
			if(tab_cuadro_techo.getTotalFilas() != 2)
			{
				utilitario.agregarMensajeInfo("No se puede Imprimir", "No esta completo los techos de las certificaciones futuras...");
				return;
			}
			
			if (rep_reporte.isVisible()){
				
				TablaGenerica tab_empleado=utilitario.consultar(ser_nomina.servicioEmpleadoContratoCodigo("true,false",tab_certificacion_futura.getValor("ide_gtemp")+"")); 
				TablaGenerica tab_usuario =utilitario.consultar("select a.ide_usua,ide_empr,ide_sucu,nom_usua from sis_usuario a, sis_usuario_sucursal b where a.ide_usua = b.ide_usua and a.ide_gtemp ="+tab_certificacion_futura.getValor("ide_gtemp"));
				String elaborado="N/A";
				String pie_elaborado="N/A";
				if(tab_empleado.getTotalFilas()>0)
					pie_elaborado=tab_empleado.getValor("DETALLE_GEDEP");
				
				if(tab_usuario.getTotalFilas()>0)
					elaborado=tab_usuario.getValor("nom_usua");
				
				p_parametros=new HashMap();		
				rep_reporte.cerrar();	
				p_parametros.put("titulo","CERTIFICACION PRESUPUESTARIA PLURIANUAL");
				p_parametros.put("ide_prcef", pckUtilidades.CConversion.CInt(tab_certificacion_futura.getValor("ide_prcef")));
				p_parametros.put("elaborado", elaborado);
				p_parametros.put("pie_elaborado", pie_elaborado);
				p_parametros.put("jefe_presupuesto", utilitario.getVariable("p_nombre_jefe_presupuesto"));
				p_parametros.put("coordinador_finaciero",  utilitario.getVariable("p_nombre_coordinador_fin"));
				p_parametros.put("pie_coordinador_finaciero",  utilitario.getVariable("p_pie_coordinador_fin"));
				p_parametros.put("pie_especialista_pres",  utilitario.getVariable("p_pie_especialista_pres"));
				
				p_parametros.put("n_aprobacion_techo", pckUtilidades.CConversion.CStr(tab_cuadro_techo.getValor(0, "nro_resolucion_prtef")));
				p_parametros.put("monto_aprobacion_gasto_1", pckUtilidades.CConversion.CStr(tab_cuadro_techo.getValor(0, "valor_prtef")));
				p_parametros.put("monto_aprobacion_gasto_2", pckUtilidades.CConversion.CStr(
						utilitario.getFormatoNumero( (pckUtilidades.CConversion.CDbl_2(tab_cuadro_techo.getValor(0, "valor_prtef")) -
						pckUtilidades.CConversion.CDbl_2(tab_cuadro_techo.getValor(0, "valor_certificado")) ),2 )	
								));
				p_parametros.put("monto_aprobacion_inversion_1", pckUtilidades.CConversion.CStr(tab_cuadro_techo.getValor(1, "valor_prtef")));
				p_parametros.put("monto_aprobacion_inversion_2", pckUtilidades.CConversion.CStr(
						utilitario.getFormatoNumero( (pckUtilidades.CConversion.CDbl_2(tab_cuadro_techo.getValor(1, "valor_prtef")) -
						pckUtilidades.CConversion.CDbl_2(tab_cuadro_techo.getValor(1, "valor_certificado"))	),2 )	
								));
	
				self_reporte.setSeleccionFormatoReporte(p_parametros,rep_reporte.getPath());
				self_reporte.dibujar();
			
			}
			else{
				utilitario.agregarMensajeInfo("No se puede continuar", "No ha Seleccionado Ningun Registro");
	
			}
		}
			
	}
		
	public void generarCert()
	{
		if (validar())
		{
			if(tab_certificacion_futura.isFilaInsertada()){
				String id_empleado=ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");	
				if(!empleado.equals(id_empleado)){
					System.out.println("actualizando empleado certificacion futura ide_gtemp generarCert");
					empleado=id_empleado;
				}
				tab_certificacion_futura.setValor("ide_gtemp", empleado );
				utilitario.agregarMensaje("Guardando secuencial certificacion Futura ", "Nro: "+ser_contabilidad.numeroSecuencial(par_sec_certificacion_fut));
				tab_certificacion_futura.setValor("nro_certificacion_prcef",ser_contabilidad.numeroSecuencial(par_sec_certificacion_fut));
				tab_certificacion_futura.modificar(tab_certificacion_futura.getFilaActual());
				ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_sec_certificacion_fut), par_sec_certificacion_fut);
			}
			if(tab_certificacion_futura.guardar()){
	
				if(tab_poa_certificacion_futura.guardar()){
					guardarPantalla();				
				}
			}
		}
		con_guardar.cerrar();
	}
	
	public boolean validar()
	{
		boolean valido=false;
		String[] fecha = tab_certificacion_futura.getValor("fecha_prcef").split("-");		
		String[] fecha_actual=utilitario.getFechaActual().split("-");		
		
		if(fecha[0].equals(fecha_actual[0]) && fecha[1].equals(fecha_actual[1]))
		{
			utilitario.agregarMensajeInfo("IMPORTANTE", "Guardar la certificacion futura con el Boton en forma de Disquete...");
			//tab_tramite.guardar();	
			valido=true;
		}
		else
		{
			System.out.println("fecha_prcer: "+fecha[0]+"-"+fecha[1]);
			System.out.println("fecha_actual: "+fecha_actual[0]+"-"+fecha_actual[1]);
			utilitario.agregarMensajeError("REGISTRO NO EDITABLE", "Certificacion fuera de plazo");
		}
		
		//tab_tramite.guardar();
		
		return valido;
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub

		if(com_anio.getValue()==null){
			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar un año");
			return;
		
		}
		if(tab_certificacion_futura.isFocus()){
			tab_certificacion_futura.insertar();
			tab_certificacion_futura.setValor("ide_coest","2" );
			tab_certificacion_futura.setValor("ide_gtemp", empleado );
			tab_certificacion_futura.setValor("ide_geedp", empleado_responsable );
			tab_certificacion_futura.setValor("ide_geani",com_anio.getValue()+"");
			tab_certificacion_futura.setValor("nro_certificacion_prcef",ser_contabilidad.numeroSecuencial(par_sec_certificacion_fut));
			tab_certificacion_futura.setValor("fecha_prcef", utilitario.getFechaActual());
			utilitario.addUpdate("tab_certificacion_futura");

		}
		else if(tab_poa_certificacion_futura.isFocus()){
			utilitario.agregarMensajeInfo("No puede insertar", "Debe seleccionar del POA");
		}

	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		calcularCertificacion();
	
		con_guardar.dibujar();
		utilitario.addUpdate("con_guardar");
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		
		// Cabecera
		if (con_eliminar.isVisible()) {
			if(!validar())
				return;
			
			// Cabecera
			if (tab_certificacion_futura.isFocus() && (tab_poa_certificacion_futura.getTotalFilas() == 0)) {
				tab_certificacion_futura.eliminar();
				tab_certificacion_futura.guardar();
			}
			
			// Detalle
			if (tab_poa_certificacion_futura.isFocus()) {
				tab_poa_certificacion_futura.eliminar();
				tab_poa_certificacion_futura.guardar();
			}
			
			con_eliminar.cerrar();
			
			utilitario.getTablaisFocus().actualizar();
			
			//calcularCertificacion ();
		} else {
			con_eliminar.dibujar();
		}

		
	}

    public Tabla getTab_certificacion_futura() {
		return tab_certificacion_futura;
	}


	public void setTab_certificacion_futura(Tabla tab_certificacion_futura) {
		this.tab_certificacion_futura = tab_certificacion_futura;
	}

	public Tabla getTab_poa_certificacion_futura() {
		return tab_poa_certificacion_futura;
	}


	public void setTab_poa_certificacion_futura(Tabla tab_poa_certificacion_futura) {
		this.tab_poa_certificacion_futura = tab_poa_certificacion_futura;
	}


	public SeleccionTabla getSet_poa() {
		return set_poa;
	}
	
    public void setSet_poa(SeleccionTabla set_poa) {
		this.set_poa = set_poa;
	}

	public SeleccionTabla getSet_cert() {
		return set_cert;
	}


	public void setSet_cert(SeleccionTabla set_cert) {
		this.set_cert = set_cert;
	}


	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
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
	
	public Confirmar getCon_eliminar() {
		return con_eliminar;
	}

	public void setCon_eliminar(Confirmar con_eliminar) {
		this.con_eliminar = con_eliminar;
	}
	public Tabla getTab_tablaXls() {
		return tab_tablaXls;
	}

	public void setTab_tablaXls(Tabla tab_tablaXls) {
		this.tab_tablaXls = tab_tablaXls;
	}
	


}
