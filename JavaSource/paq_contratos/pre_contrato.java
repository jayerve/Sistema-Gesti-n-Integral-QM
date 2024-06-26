package paq_contratos;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Imagen;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_contratos.ejb.ServicioContrato;
import paq_gestion.ejb.ServicioGestion;
import paq_nomina.ejb.ServicioNomina;
import paq_precontractual.ejb.ServicioEtapaProcedimiento;
import paq_sistema.aplicacion.Pantalla;

public class pre_contrato extends Pantalla {
	private Tabla tab_contrato =new Tabla();
	private Tabla tab_administrador=new Tabla();
	private Tabla tab_archivo =new Tabla();
	private SeleccionTabla set_empleado_administrador=new SeleccionTabla();
	//private SeleccionTabla set_actualizar_administrador = new SeleccionTabla();
	private Confirmar con_guardar =new Confirmar();
	private Combo com_anio=new Combo();
	
	List lista = new ArrayList();
	private SeleccionTabla set_proveedor= new SeleccionTabla();
	//private SeleccionTabla set_actualizar_proveedor=new SeleccionTabla();
//	private Confirmar con_guardar= new Confirmar();

	
	@EJB
	private ServicioGestion ser_gestion = (ServicioGestion) utilitario.instanciarEJB(ServicioGestion.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	@EJB
	private ServicioContrato ser_contrato = (ServicioContrato) utilitario.instanciarEJB(ServicioContrato.class);
	@EJB
	private ServicioEtapaProcedimiento ser_EtapaProcedimiento = (ServicioEtapaProcedimiento) utilitario.instanciarEJB(ServicioEtapaProcedimiento.class);

	public pre_contrato (){
		
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
	
		Tabulador tab_tabulador = new Tabulador();
		tab_tabulador.setId("tab_tabulador");
		tab_contrato.setId("tab_contrato");
		tab_contrato.setTipoFormulario(true);
		tab_contrato.getGrid().setColumns(4);
		tab_contrato.setHeader("SEGUIMIENTO DE CONTRATOS");
		tab_contrato.setTabla("pre_contrato", "ide_prcon", 1);
		tab_contrato.setCondicion("tipo_int_ext_prcon in (1,2) and ide_prcon=-1");
		tab_contrato.getColumna("ide_prcon").setNombreVisual("CÓDIGO");
		//tab_contrato.getColumna("ide_prcon").setOrden(1);
		tab_contrato.getColumna("activo_prcon");
		tab_contrato.getColumna("activo_prcon").setNombreVisual("ACTIVO");
		//tab_contrato.getColumna("activo_prcon").setOrden(2);
		tab_contrato.getColumna("activo_prcon").setValorDefecto("true");
		
		Object fila1[] = { "0", "Contrato Interno" };
		Object fila2[] = { "1", "Contrato Externo" };
		Object fila3[] = { "2", "Infima Cuantía" };
		Object fila4[] = { "3", "APP_WEB" };

		//lista.add(fila1);
		lista.add(fila2);
		lista.add(fila3);
		//lista.add(fila4);
		tab_contrato.getColumna("tipo_int_ext_prcon").setRadio(lista, "1");
		//tab_contrato.getColumna("tipo_int_ext_prcon").setOrden(3);
		tab_contrato.getColumna("tipo_int_ext_prcon").setValorDefecto("1");
		tab_contrato.getColumna("tipo_int_ext_prcon").setRadioVertical(true);
		tab_contrato.getColumna("tipo_int_ext_prcon").setNombreVisual("Tipo Contrato");
		//tab_contrato.getColumna("ide_prtsc").setCombo("pre_tipo_servicio_contrato", "ide_prtsc", "detalle_prtsc", "");
		//tab_contrato.getColumna("ide_prtsc").setNombreVisual("Tipo de Servicio");
		tab_contrato.getColumna("ide_prtsc").setVisible(false);
		//tab_contrato.getColumna("ide_prtsc").setOrden(4);
		tab_contrato.getColumna("ide_pretp").setCombo(ser_EtapaProcedimiento.getProcedimientos());
		tab_contrato.getColumna("fecha_firma_prcon");
		tab_contrato.getColumna("fecha_firma_prcon").setNombreVisual("Firma de Contrato");
		//tab_contrato.getColumna("fecha_firma_prcon").setOrden(5);
		tab_contrato.getColumna("fecha_firma_prcon").setValorDefecto(utilitario.getFechaActual());
		tab_contrato.getColumna("fecha_inicio_prcon");
		tab_contrato.getColumna("fecha_inicio_prcon").setNombreVisual("Fecha de Inicio");
		//tab_contrato.getColumna("fecha_inicio_prcon").setOrden(6);
		tab_contrato.getColumna("fecha_inicio_prcon").setValorDefecto(utilitario.getFechaActual());
		tab_contrato.getColumna("OBSERVACION_PRCON");
		tab_contrato.getColumna("OBSERVACION_PRCON").setNombreVisual("Objeto del Contrato");
		//tab_contrato.getColumna("OBSERVACION_PRCON").setOrden(7);
		tab_contrato.getColumna("NUMERO_CONTRATO_PRCON");
		tab_contrato.getColumna("NUMERO_CONTRATO_PRCON").setNombreVisual("Número de Contrato");
		//tab_contrato.getColumna("NUMERO_CONTRATO_PRCON").setOrden(8);
		tab_contrato.getColumna("MONTO_ORIGINAL_PRCON");
		tab_contrato.getColumna("MONTO_ORIGINAL_PRCON").setNombreVisual("Monto Original del Contrato");
		//tab_contrato.getColumna("MONTO_ORIGINAL_PRCON").setOrden(9);
		tab_contrato.getColumna("MONTO_PRCON");
		tab_contrato.getColumna("MONTO_PRCON").setNombreVisual("Monto del Contrato");
		//tab_contrato.getColumna("MONTO_PRCON").setOrden(10);
		tab_contrato.getColumna("FECHA_ANTICIPO_PRCON");
		tab_contrato.getColumna("FECHA_ANTICIPO_PRCON").setNombreVisual("Fecha de Anticipo");
		//tab_contrato.getColumna("FECHA_ANTICIPO_PRCON").setOrden(11);
		tab_contrato.getColumna("MONTO_ANTICIPO_PRCON");
		tab_contrato.getColumna("MONTO_ANTICIPO_PRCON").setNombreVisual("Monto del Anticipo");
		//tab_contrato.getColumna("MONTO_ANTICIPO_PRCON").setOrden(12);
		//tab_contrato.getColumna("ide_pretc").setCombo("pre_etapa_contrato", "ide_pretc", "detalle_pretc", "");
		//tab_contrato.getColumna("ide_pretc").setNombreVisual("Etapa de Contrato");
		tab_contrato.getColumna("ide_pretc").setVisible(false);
		//tab_contrato.getColumna("ide_pretc").setOrden(13);
		//tab_contrato.getColumna("IDE_TETAR").setCombo("tes_tarifas", "IDE_TETAR", "detalle_tetar", "");
		//tab_contrato.getColumna("IDE_TETAR").setNombreVisual("tarifa");
		tab_contrato.getColumna("IDE_TETAR").setVisible(false);
		//tab_contrato.getColumna("IDE_TETAR").setOrden(14);
		//tab_contrato.getColumna("CON_IDE_PRCON");
		//tab_contrato.getColumna("CON_IDE_PRCON").setOrden(15);
		tab_contrato.getColumna("CON_IDE_PRCON").setVisible(false);
		//tab_contrato.getColumna("ide_recli").setCombo(ser_contrato.getClientes("1,0"));
		//tab_contrato.getColumna("ide_recli").setAutoCompletar();
		//tab_contrato.getColumna("ide_recli").setLectura(true);
		//tab_contrato.getColumna("ide_recli").setNombreVisual("Cliente");
		tab_contrato.getColumna("ide_recli").setVisible(false);
		//tab_contrato.getColumna("IDE_RECLI").setOrden(16);
		tab_contrato.getColumna("IDE_PERDE");
		//tab_contrato.getColumna("IDE_PERDE").setOrden(17);
		tab_contrato.getColumna("IDE_PERDE").setVisible(false);
		tab_contrato.getColumna("IDE_PERRE");
		//tab_contrato.getColumna("IDE_PERRE").setOrden(18);
		tab_contrato.getColumna("IDE_PERRE").setVisible(false);
		tab_contrato.getColumna("plazo_prcon");
		tab_contrato.getColumna("plazo_prcon").setNombreVisual("Plazo del Contrato");
		tab_contrato.getColumna("plazo_prcon").setVisible(false);
		//tab_contrato.getColumna("plazo_prcon").setOrden(19);
		//tab_contrato.getColumna("ide_prtip").setCombo("pre_tipo_plazo", "ide_prtip", "detalle_prtip", "");
		//tab_contrato.getColumna("ide_prtip").setNombreVisual("Tipo de Plazo");
		tab_contrato.getColumna("ide_prtip").setVisible(false);
		//tab_contrato.getColumna("ide_prtip").setOrden(20);
		tab_contrato.getColumna("fecha_fin_prcon");
		tab_contrato.getColumna("fecha_fin_prcon").setNombreVisual("Fecha fín de Contrato");
		//tab_contrato.getColumna("fecha_fin_prcon").setOrden(21);
		tab_contrato.getColumna("fecha_cierre_prcon");
		tab_contrato.getColumna("fecha_cierre_prcon").setNombreVisual("Fecha Cierre Contrato");
		//tab_contrato.getColumna("fecha_cierre_prcon").setOrden(22);
		tab_contrato.getColumna("fecha_suspension_prcon");
		tab_contrato.getColumna("fecha_suspension_prcon").setNombreVisual("Fec. de Suspención Contrato");
		//tab_contrato.getColumna("fecha_suspension_prcon").setOrden(23);
		tab_contrato.getColumna("motivo_suspension_prcon");
		tab_contrato.getColumna("motivo_suspension_prcon").setNombreVisual("Motivo de Suspención");
		//tab_contrato.getColumna("motivo_suspension_prcon").setOrden(24);
		//tab_contrato.getColumna("num_generador_desecho_prcon");
		//tab_contrato.getColumna("num_generador_desecho_prcon").setNombreVisual("Num. Generador Desecho");
		tab_contrato.getColumna("num_generador_desecho_prcon").setVisible(false);
		//tab_contrato.getColumna("num_generador_desecho_prcon").setOrden(25);
		//tab_contrato.getColumna("ide_reclr").setCombo("rec_cliente_ruta", "ide_reclr", "detalle_reclr", "");
		//tab_contrato.getColumna("ide_reclr").setNombreVisual("Ruta");
		tab_contrato.getColumna("ide_reclr").setVisible(false);
		//tab_contrato.getColumna("ide_reclr").setOrden(26);
		//tab_contrato.getColumna("estimado_desecho_prcon");
		//tab_contrato.getColumna("estimado_desecho_prcon").setNombreVisual("Monto del Contrato");
		tab_contrato.getColumna("estimado_desecho_prcon").setVisible(false);
		tab_contrato.getColumna("ESTIMADO_DESECHO").setVisible(false);
		//tab_contrato.getColumna("estimado_desecho_prcon").setOrden(27);
		tab_contrato.getColumna("ide_coest").setCombo("cont_estado", " ide_coest", "detalle_coest", "");
		//tab_contrato.getColumna("ide_coest").setOrden(28);
		tab_contrato.getColumna("ide_coest").setValorDefecto("31");
		tab_contrato.getColumna("ide_coest").setNombreVisual("Estado");
		tab_contrato.getColumna("nro_contrato_prcon");
		tab_contrato.getColumna("nro_contrato_prcon").setVisible(false);
		//tab_contrato.getColumna("nro_contrato_prcon").setOrden(29);
		//tab_contrato.getColumna("nro_contrato_prcon").setVisible(false);
//		tab_contrato.getColumna("ide_tepro").setCombo("tes_proveedor","ide_tepro","nombre_tepro","");
		tab_contrato.getColumna("ide_tepro").setCombo(ser_contrato.getProveedores());
		tab_contrato.getColumna("ide_tepro").setAutoCompletar();
		tab_contrato.getColumna("ide_tepro").setLectura(false);
		//tab_contrato.getColumna("ide_tepro").setOrden(30);
		tab_contrato.getColumna("ide_tepro").setNombreVisual("Proveedor");
		tab_contrato.getColumna("ide_tepro").setLectura(true);
		tab_contrato.getColumna("ide_prcop").setVisible(false);
		tab_contrato.getColumna("IDE_GEPLF").setVisible(false);
		//tab_contrato.getColumna("fecha_terminacion_prcon").setVisible(false);
		
		tab_contrato.getColumna("contrato_prdoc").setUpload("Contratos");
		
		//----------
		tab_contrato.agregarRelacion(tab_administrador);//agraga relacion para los tabuladores
		tab_contrato.agregarRelacion(tab_archivo);
		tab_contrato.dibujar();
		PanelTabla pat_contratacion=new PanelTabla();
		pat_contratacion.setPanelTabla(tab_contrato);
		
		//tabla administrador_contrato
		tab_administrador.setId("tab_administrador");
		tab_administrador.setHeader("ADMINISTRADOR CONTRATO");
		tab_administrador.setIdCompleto("tab_tabulador:tab_administrador");
		tab_administrador.setTabla("pre_contrato_administrador", "ide_pradc", 2);
		tab_administrador.getColumna("IDE_GTEMP").setVisible(false);
		tab_administrador.setCampoForanea("ide_prcon");
		tab_administrador.getColumna("IDE_GEEDP").setCombo(ser_nomina.servicioEmpleadoContrato("true,false"));
		tab_administrador.getColumna("IDE_GEEDP").setAutoCompletar();
		tab_administrador.getColumna("IDE_GEEDP").setLectura(true);
		tab_administrador.getColumna("IDE_GEEDP").setUnico(true);
		tab_administrador.getColumna("fecha_inicio_pradc").setValorDefecto(utilitario.getFechaActual());
		tab_administrador.getColumna("fecha_fin_pradc").setValorDefecto(utilitario.getFechaActual());
		tab_administrador.getColumna("ide_pradc").setUnico(true);
		tab_administrador.getColumna("activo_pradc").setValorDefecto("true");
		tab_administrador.setCampoForanea("ide_prcon");
		tab_administrador.dibujar();
		PanelTabla pat_administrador =new PanelTabla();
		pat_administrador.setPanelTabla(tab_administrador);
		
		
		tab_archivo.setId("tab_archivo");
		tab_archivo.setHeader("DOCUMENTOS EJECUCION");
		tab_archivo.setIdCompleto("tab_tabulador:tab_archivo");
		//tab_archivo.setTipoFormulario(true);
		tab_archivo.setTabla("pre_contrato_archivo","ide_prcar",3);
		tab_archivo.getColumna("documentoadjunto_prcar").setUpload("EjecucionContrato");
		
		tab_archivo.getColumna("con_ide_prcar").setVisible(false);
		tab_archivo.getColumna("ide_prreq").setCombo("precon_requisito","ide_prreq","descripcion_prreq","");
		tab_archivo.getColumna("ide_prreq").setNombreVisual("Requisito Actividad");
		//tab_archivo.getColumna("ide_prreq").setLectura(true);
		tab_archivo.getColumna("ide_prtip").setCombo("precon_tipologia","ide_prtip","descripcion_prtip","");
		tab_archivo.getColumna("ide_prtip").setValorDefecto("6");
		tab_archivo.getColumna("ide_prtip").setNombreVisual("Tipología");
		tab_archivo.getColumna("presenta_prcar").setCombo(utilitario.getListaSiNo());
		tab_archivo.getColumna("presenta_prcar").setNombreVisual("Subió el Archivo?");
		//tab_archivo.getColumna("presenta_prcar").setLectura(true);
		tab_archivo.getColumna("anexo_prcar").setVisible(false);
		
		
		
		//tab_archivo.getColumna("activo_prcar").setLectura(true);
		tab_archivo.getColumna("activo_prcar").setValorDefecto("true");
		tab_archivo.setCampoOrden("ide_prcar desc");
		tab_archivo.setCampoForanea("ide_prcon");
		//tab_archivo.setCondicion("con_ide_prcar is null");
		tab_archivo.setLectura(true);
		tab_archivo.dibujar();
		PanelTabla pat_archivo= new PanelTabla();
		pat_archivo.setPanelTabla(tab_archivo);
		
		tab_tabulador.agregarTab("ADMINISTRADOR CONTRATO", pat_administrador);//intancia los tabuladores 
		tab_tabulador.agregarTab("EXPEDIENTE",pat_archivo);
		
		//division2
		Division div_division=new Division();
		div_division.dividir2(pat_contratacion,tab_tabulador,"50%","H");
		agregarComponente(div_division);
		
		//Pantalla Dialogo 
		//bara el boton administrador 
		Boton bot_empleado=new Boton();
		bot_empleado.setIcon("ui-icon-person");
		bot_empleado.setValue("Agregar Administrador");
		bot_empleado.setMetodo("importarEmpleado");
		bar_botones.agregarBoton(bot_empleado);
		con_guardar.setId("con_guardar");
		agregarComponente(con_guardar);
		
		set_empleado_administrador.setId("set_empleado_administrador");
		set_empleado_administrador.setSeleccionTabla(ser_nomina.servicioEmpleadoContrato("true"),"ide_geedp");
		set_empleado_administrador.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
		set_empleado_administrador.getTab_seleccion().getColumna("nombres_apellidos").setFiltro(true);
		set_empleado_administrador.setTitle("Seleccione un Empleado");
		set_empleado_administrador.getBot_aceptar().setMetodo("aceptarEmpleado");
		agregarComponente(set_empleado_administrador);
		
		// Boton actualizar administrador 
		/*Boton bot_actualizar=new Boton();
		bot_actualizar.setIcon("ui-icon-person");
		bot_actualizar.setValue("Actualizar Administrador");
		bot_actualizar.setMetodo("actualizarAdministrador");
		bar_botones.agregarBoton(bot_actualizar);
		set_actualizar_administrador.setId("set_actualizar_administrador");
		set_actualizar_administrador.setSeleccionTabla(ser_nomina.servicioEmpleadoContrato("true"),"ide_geedp");
		set_actualizar_administrador.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
		set_actualizar_administrador.getTab_seleccion().getColumna("nombres_apellidos").setFiltro(true);
		set_actualizar_administrador.setRadio();
		set_actualizar_administrador.getBot_aceptar().setMetodo("modificarAdministrador");
		agregarComponente(set_actualizar_administrador);	*/
		
		
		//BOTON AGREGAR PROVEEDOR
		Boton bot_agregarCliente=new Boton();
		bot_agregarCliente.setValue("Agregar Proveedor");
		bot_agregarCliente.setIcon("ui-icon-person");
		bot_agregarCliente.setMetodo("agregarProveedor");
		bar_botones.agregarBoton(bot_agregarCliente);

		//PANTALLA SELECIONAR PROVEEDOR
		set_proveedor.setId("set_proveedor");
		set_proveedor.setTitle("Seleccione un Proveedor");
		set_proveedor.getBot_aceptar().setMetodo("aceptarProveedor");
		set_proveedor.setSeleccionTabla(ser_contrato.getProveedores(),"ide_tepro");
		set_proveedor.getTab_seleccion().getColumna("ruc_tepro").setFiltro(true);
		set_proveedor.getTab_seleccion().getColumna("ruc_tepro").setNombreVisual("Ruc:");
		set_proveedor.getTab_seleccion().getColumna("nombre_tepro").setFiltro(true);
		set_proveedor.getTab_seleccion().getColumna("nombre_tepro").setNombreVisual("Nombre");
		set_proveedor.setRadio();
		set_proveedor.getTab_seleccion().ejecutarSql();
		agregarComponente(set_proveedor);

		//////modificar
		/*Boton bot_modificar=new Boton();
		bot_modificar.setValue("Actualizar Proveedor");
		bot_modificar.setIcon("ui-icon-person");
		bot_modificar.setMetodo("actualizarProveedor");
		bar_botones.agregarBoton(bot_modificar);
		con_guardar.setId("con_guardar");
		agregarComponente(con_guardar);
		
		set_actualizar_proveedor.setId("set_actualizar_proveedor");
		set_actualizar_proveedor.setTitle("Seleccione un Proveedor");
		set_actualizar_proveedor.setSeleccionTabla(ser_contrato.getProveedores(),"ide_tepro");
		set_actualizar_proveedor.getTab_seleccion().getColumna("ruc_tepro").setFiltro(true);
		set_actualizar_proveedor.getTab_seleccion().getColumna("nombre_tepro").setFiltro(true);
		set_actualizar_proveedor.setRadio();
		set_actualizar_proveedor.getBot_aceptar().setMetodo("modificarProveedor");
		agregarComponente(set_actualizar_administrador);
		*/
	}
	
	public void seleccionaElAnio(){   	
		String sqlAnio="tipo_int_ext_prcon in (1,2)";
		if(com_anio.getValue()!=null){
			sqlAnio+=" and extract(year from fecha_firma_prcon)=(SELECT cast(detalle_geani as int) as anio FROM gen_anio where ide_geani="+com_anio.getValue()+") ";				
		}
		else{
			utilitario.agregarMensajeInfo("Selecione un año", "");
			return;
		}

		tab_contrato.setCondicion(sqlAnio);
		tab_contrato.ejecutarSql();
		tab_administrador.ejecutarValorForanea(tab_contrato.getValorSeleccionado());
		tab_archivo.ejecutarValorForanea(tab_contrato.getValorSeleccionado());
	}

	public void agregarProveedor(){
		//Hace aparecer el componente
		try {
			
			System.out.println("el valor tipo "+tab_contrato.getValor("tipo_int_ext_prcon"));
			if(tab_contrato.getValor("tipo_int_ext_prcon")!=null && !tab_contrato.getValor("tipo_int_ext_prcon").isEmpty()){
				
				set_proveedor.getTab_seleccion().setSql(ser_contrato.getProveedores());
				set_proveedor.getTab_seleccion().getColumna("ruc_tepro").setFiltro(true);
				set_proveedor.getTab_seleccion().getColumna("ruc_tepro").setNombreVisual("Ruc");
				set_proveedor.getTab_seleccion().getColumna("nombre_tepro").setFiltro(true);
				set_proveedor.getTab_seleccion().getColumna("nombre_tepro").setNombreVisual("Nombre");
				set_proveedor.setRadio();
				set_proveedor.getTab_seleccion().ejecutarSql();
				set_proveedor.dibujar();	
				
			}else{
				utilitario.agregarMensaje("No se puede registrar un Cliente o Proveedor","");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void aceptarProveedor(){
		String str_seleccionado=set_proveedor.getValorSeleccionado();
		if(str_seleccionado!=null){
			
			tab_contrato.setValor("ide_tepro", str_seleccionado);
			tab_contrato.setValor("ide_recli", null);
			set_proveedor.cerrar();
			utilitario.addUpdate("tab_contrato");			
			tab_contrato.modificar(tab_contrato.getFilaActual());
			guardarPantalla();
		}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar un registro", "");
		}
	}

	/*
	public void actualizarProveedor(){
		//Hace aparecer el componente
		if(tab_contrato.getValor("tipo_int_ext_prcon")!=null && tab_contrato.getValor("nro_establecimiento_recli").isEmpty())
		{
			set_actualizar_proveedor.getTab_seleccion().setSql(ser_contrato.getProveedores());
			set_actualizar_proveedor.getTab_seleccion().getColumna("ruc_tepro").setFiltro(true);			
			set_actualizar_proveedor.getTab_seleccion().getColumna("nombre_tepro").setFiltro(true);
			set_actualizar_proveedor.setRadio();
			set_actualizar_proveedor.getTab_seleccion().ejecutarSql();
			set_actualizar_proveedor.dibujar();	
			
		}else{
			utilitario.agregarMensaje("No se puede actualizar un Cliente o Proveedor","");
		}
	}
	
	public void modificarProveedor(){
		String str_matriz=set_actualizar_proveedor.getValorSeleccionado();

	    tab_contrato.setValor("ide_tepro",(str_matriz));			
	    tab_contrato.modificar(tab_contrato.getFilaActual());
		//utilitario.addUpdate("tab_contrato");			
		con_guardar.setMessage("Esta Seguro de Actualizar el registro");
		con_guardar.setTitle("Confirmación de Actualizar");
		con_guardar.getBot_aceptar().setMetodo("guardarActualilzar");
		con_guardar.dibujar();
		utilitario.addUpdate("con_guardar");
	}
	
	public void guardarActualilzar(){
		tab_contrato.guardar();
		con_guardar.cerrar();
		set_actualizar_administrador.cerrar();
		guardarPantalla();
	}
    */
	/*
	//ADMINISTRADOR	
	public void actualizarAdministrador(){
		if (tab_administrador.getValor("ide_pradc")==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Administrador para actualizar","");
			return;
		}
		set_actualizar_administrador.getTab_seleccion().setSql(ser_nomina.servicioEmpleadoContrato("true"));
		set_actualizar_administrador.getTab_seleccion().ejecutarSql();
		set_actualizar_administrador.dibujar();
			
	}
			
	public void modificarAdministrador(){
		String str_empleadoActualizado=set_actualizar_administrador.getValorSeleccionado();
	   	TablaGenerica tab_empleadoModificadoAdministrador = ser_nomina.ideEmpleadoContrato(str_empleadoActualizado,"true");		
	    tab_administrador.setValor("IDE_GEEDP", tab_empleadoModificadoAdministrador.getValor("IDE_GEEDP"));			
	    tab_administrador.setValor("IDE_GTEMP", tab_empleadoModificadoAdministrador.getValor("IDE_GTEMP"));	
	    tab_administrador.modificar(tab_administrador.getFilaActual());
		utilitario.addUpdate("tab_administrador");	

		con_guardar.setMessage("Esta Seguro de Actualizar el Administrador");
		con_guardar.setTitle("CONFIRMCION DE ACTUALIZAR");
		con_guardar.getBot_aceptar().setMetodo("guardarActualilzarAdministrador");
		con_guardar.dibujar();
		utilitario.addUpdate("con_guardar");


	}
	public void guardarActualilzarAdministrador(){
		System.out.println("Entra a guardar...");
		tab_administrador.guardar();
		con_guardar.cerrar();
		set_actualizar_administrador.cerrar();
		guardarPantalla();
	}
    */
	
	public void importarEmpleado(){
		if (tab_contrato.isEmpty()) {
			utilitario.agregarMensajeInfo("Debe ingresar un registro en el contrato", "");
			return;			
		}
							
		set_empleado_administrador.getTab_seleccion().setSql(ser_nomina.servicioEmpleadoContrato("true"));
		set_empleado_administrador.getTab_seleccion().ejecutarSql();
		set_empleado_administrador.dibujar();
	}
	
	public void aceptarEmpleado(){
		String str_seleccionados=set_empleado_administrador.getSeleccionados();
		if(str_seleccionados!=null){
			//Inserto los empleados seleccionados en la tabla de resposable d econtratacion 
			TablaGenerica tab_empleado_responsable = ser_nomina.ideEmpleadoContrato(str_seleccionados,"true");								
			System.out.println(" tabla generica"+tab_empleado_responsable.getSql());
			
			for(int i=0;i<tab_administrador.getTotalFilas();i++){	
				tab_administrador.setValor(i,"activo_pradc", "false");	
				tab_administrador.modificar(i);//para que haga el update
			}
			
			for(int i=0;i<tab_empleado_responsable.getTotalFilas();i++){
				tab_administrador.insertar();
				tab_administrador.setValor("IDE_GEEDP", tab_empleado_responsable.getValor(i, "IDE_GEEDP"));			
				tab_administrador.setValor("IDE_GTEMP", tab_empleado_responsable.getValor(i, "IDE_GTEMP"));			
			}
			
			set_empleado_administrador.cerrar();
			utilitario.addUpdate("tab_responsable");			
		}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar almenos un registro", "");
		}
	}
		
	
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		
		if (tab_contrato.isFocus()) {
			tab_contrato.insertar();
			//tab_contratacion.setValor("ide_geani", com_anio.getValue()+"");

		}
		else if (tab_administrador.isFocus()) {
			tab_administrador.insertar();

		}
		else if (tab_archivo.isFocus()) {
			tab_archivo.insertar();

		}
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if (tab_contrato.guardar()) {
			
			if (tab_administrador.guardar()) {
				tab_archivo.guardar();		
			}
		}
		guardarPantalla();
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();

	}

	public Tabla getTab_contrato() {
		return tab_contrato;
	}

	public void setTab_contrato(Tabla tab_contrato) {
		this.tab_contrato = tab_contrato;
	}

	public Tabla getTab_administrador() {
		return tab_administrador;
	}

	public void setTab_administrador(Tabla tab_administrador) {
		this.tab_administrador = tab_administrador;
	}

	public Tabla getTab_archivo() {
		return tab_archivo;
	}

	public void setTab_archivo(Tabla tab_archivo) {
		this.tab_archivo = tab_archivo;
	}

	public Confirmar getCon_guardar() {
		return con_guardar;
	}
	public void setCon_guardar(Confirmar con_guardar) {
		this.con_guardar = con_guardar;
	}

	public List getLista() {
		return lista;
	}


	public void setLista(List lista) {
		this.lista = lista;
	}

	public SeleccionTabla getSet_empleado_administrador() {
		return set_empleado_administrador;
	}

	public void setSet_empleado_administrador(
			SeleccionTabla set_empleado_administrador) {
		this.set_empleado_administrador = set_empleado_administrador;
	}

	/*public SeleccionTabla getSet_actualizar_administrador() {
		return set_actualizar_administrador;
	}

	public void setSet_actualizar_administrador(
			SeleccionTabla set_actualizar_administrador) {
		this.set_actualizar_administrador = set_actualizar_administrador;
	}*/

	public SeleccionTabla getSet_proveedor() {
		return set_proveedor;
	}

	public void setSet_proveedor(SeleccionTabla set_proveedor) {
		this.set_proveedor = set_proveedor;
	}

	/*public SeleccionTabla getSet_actualizar_proveedor() {
		return set_actualizar_proveedor;
	}

	public void setSet_actualizar_proveedor(SeleccionTabla set_actualizar_proveedor) {
		this.set_actualizar_proveedor = set_actualizar_proveedor;
	}*/


	
	

}

