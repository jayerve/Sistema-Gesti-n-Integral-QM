package paq_tesoreria;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJB;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AreaTexto;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import framework.componentes.Texto;
import framework.componentes.VisualizarPDF;
import paq_anticipos.ejb.ServicioAnticipo;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_gestion.ejb.ServicioEmpleado;
import paq_gestion.ejb.ServicioGestion;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;

public class pre_recaudacion_anticipos extends Pantalla{
	
	private Tabla tab_anticipo=new Tabla();
	private Tabla tab_amortizacion=new Tabla();
	private Tabla tab_anticipo_interes=new Tabla();
	private Tabla tab_abono=new Tabla();
	private AutoCompletar aut_empleado = new AutoCompletar();
	private AutoCompletar aut_caja= new AutoCompletar();
	
	private Tabulador tab_tabulador=new Tabulador();
	private Combo com_forma_pago = new Combo();
	private Boton bot_pre_cancelacion=new Boton();

	private Dialogo dia_precancelacion=new Dialogo();
	private Tabla tab_pagos_anticipos=new Tabla();
	private Tabla tab_precancelacion=new Tabla();
	private Etiqueta eti_tot_precancelar=new Etiqueta();
	private Etiqueta eti_tot_adeuda=new Etiqueta();

	private Tabla tab_datos_precancelacion=new Tabla();

	private Texto tex_total_ingresos = new Texto();
	private Texto tex_total_egresos = new Texto();
	private Texto tex_valor_a_recibir = new Texto();
	private Texto txt_documento= new Texto();
	private Texto txt_cliente = new Texto();
	private Texto txt_cheque = new Texto();
	private AreaTexto txt_observacion = new AreaTexto();
	
	public static String par_modulosec_recaudacion;
	String ide_caja="";
	String ide_empleado="";
	String ide_sucursal="";
	String ide_lugar="";
	private double recibido=0;
	private double deuda=0;
	
	private VisualizarPDF vpdf_pago = new VisualizarPDF();
	
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);

	@EJB
	private ServicioGestion ser_gestion = (ServicioGestion) utilitario.instanciarEJB(ServicioGestion.class);

	@EJB
	private ServicioAnticipo ser_anticipo = (ServicioAnticipo) utilitario.instanciarEJB(ServicioAnticipo.class);

	@EJB
	private ServicioEmpleado serv_empleado= (ServicioEmpleado) utilitario.instanciarEJB(ServicioEmpleado.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);

	public pre_recaudacion_anticipos() 
	{
		par_modulosec_recaudacion=utilitario.getVariable("p_modulo_secuencial_recaudacion");
		ide_caja=obtenerCaja();
		ide_empleado=obtenerEmpleado();
		ide_sucursal=obtenerSucursal();
		ide_lugar=obtenerLugarCobro();
		
		bar_botones.limpiar();
		
		// boton limpiar
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");

		// autocompletar empleado
		aut_empleado.setId("aut_empleado");

		String str_sql_emp=ser_gestion.getSqlEmpleadosAutocompletar();
		aut_empleado.setAutoCompletar(str_sql_emp);
		aut_empleado.setMetodoChange("filtrarAnticiposEmpleado");

		Etiqueta eti_colaborador=new Etiqueta("Empleado:");

		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_empleado);
		bar_botones.agregarBoton(bot_limpiar);
		
		// FORMA DE PAGO
    	com_forma_pago.setCombo("select ide_retip,detalle_retip from rec_tipo where activo_retip =true");
    	com_forma_pago.setValue("4");
    	com_forma_pago.setStyle("width: 200px; margin: 0 0 -8px 0;");
    	bar_botones.agregarComponente(new Etiqueta("Forma de Pago:"));
    	bar_botones.agregarComponente(com_forma_pago);

		bot_pre_cancelacion.setValue("Cobrar");
		bot_pre_cancelacion.setMetodo("preCancelarAnticipo");
		bar_botones.agregarBoton(bot_pre_cancelacion);

		//  ANTICIPOS (division 1)
		tab_anticipo.setId("tab_anticipo");
		tab_anticipo.setTabla("NRH_ANTICIPO", "IDE_NRANT", 1);
		tab_anticipo.setTipoFormulario(true);
		tab_anticipo.getColumna("ACTIVO_NRANT").setCheck();
		tab_anticipo.getColumna("ACTIVO_NRANT").setValorDefecto("true");
		tab_anticipo.getColumna("ACTIVO_NRANT").setLectura(true);
		tab_anticipo.getColumna("IDE_GTEMP").setVisible(false);
		tab_anticipo.getColumna("GEN_IDE_GEEDP").setVisible(false);
		tab_anticipo.getColumna("GEN_IDE_GEEDP2").setVisible(false);
		tab_anticipo.getColumna("GEN_IDE_GEEDP3").setVisible(false);
		tab_anticipo.getColumna("IDE_NRMOA").setVisible(false);
		tab_anticipo.getColumna("NRO_ANTICIPO_NRANT").setVisible(false);
		tab_anticipo.getColumna("RESOLUCION_NRANT").setVisible(false);
		tab_anticipo.getColumna("FECHA_RESOLUCION_NRANT").setVisible(false);
		tab_anticipo.getColumna("NRO_MEMO_NRANT").setVisible(false);
		tab_anticipo.getColumna("FECHA_AUTORIZA_NRANT").setVisible(false);
		tab_anticipo.getColumna("RAZON_AUTORIZA_NRANT").setVisible(false);
		tab_anticipo.getColumna("ARCHIVO_MEMO_NRANT").setVisible(false);
		tab_anticipo.getColumna("ANULADO_NRANT").setVisible(false);
		tab_anticipo.getColumna("REAPROBADO_NRANT").setVisible(false);
		tab_anticipo.getColumna("CALIFICADO_NRANT").setVisible(false);
		tab_anticipo.getColumna("ABONO_NRANT").setVisible(false);
		tab_anticipo.getColumna("ANTICIPO_NRANT").setVisible(false);
		tab_anticipo.getColumna("APROBADO_NRANT").setVisible(false);
		tab_anticipo.getColumna("IDE_GEEDP").setVisible(false);
		tab_anticipo.getColumna("NRO_MES_NRANT").setVisible(false);
		tab_anticipo.getColumna("FECHA_SOLICITUD_NRANT").setVisible(false);
		tab_anticipo.getColumna("MONTO_SOLICITADO_NRANT").setVisible(false);
		tab_anticipo.getColumna("FECHA_RRHH_NRANT").setVisible(false);
		
		tab_anticipo.getColumna("GEN_IDE_GEEDP").setCombo("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
				" EMP.APELLIDO_MATERNO_GTEMP || ' ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
				"EMP.SEGUNDO_NOMBRE_GTEMP AS NOMBRES_APELLIDOS, " +
				"SUCU.NOM_SUCU, AREA.DETALLE_GEARE, " +
				"DEPA.DETALLE_GEDEP " +
				"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
				"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
				"LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " +
				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE ");
		
		tab_anticipo.getColumna("ARCHIVO_MEMO_NRANT").setLectura(true);
		tab_anticipo.getColumna("ARCHIVO_MEMO_NRANT").setUpload("archivos");
		
		tab_anticipo.getGrid().setColumns(4);
		tab_anticipo.agregarRelacion(tab_anticipo_interes);

		tab_anticipo.setCondicion("ANTICIPO_NRANT=TRUE and ACTIVO_NRANT=true AND IDE_GTEMP=-1");
		tab_anticipo.setCampoOrden("IDE_NRANT DESC");
		tab_anticipo.setLectura(true);
		tab_anticipo.dibujar();

		// CAJAS
        Etiqueta eti_caja = new Etiqueta("CAJA :");
        eti_caja.setStyle("font-weight: bold;text-aling:left");
       	aut_caja.setId("aut_caja");
       	aut_caja.setStyle("text-aling:left");
       	aut_caja.setAutoCompletar("select ide_tecaj,detalle_tecaj from tes_caja ");
        aut_caja.setValor(ide_caja);
       	aut_caja.setDisabled(true);
       	
    	// DOCUEMNTO PAGO
    	Etiqueta eti_documento = new Etiqueta("NRO DOCUMENTO COBRO:");
    	eti_documento.setStyle("font-weight: bold;text-aling:left");
		txt_documento.setId("txt_documento");
       	txt_documento.setStyle("text-aling:left");	
       	txt_documento.setValue("0");
       	txt_documento.setDisabled(true);
       	
		tex_valor_a_recibir.setId("tex_valor_a_recibir");
		tex_valor_a_recibir.setDisabled(true);
		tex_valor_a_recibir.setStyle("font-size: 14px;color: red;font-weight: bold");
		tex_total_ingresos.setId("tex_total_ingresos");
		tex_total_ingresos.setDisabled(true);
		tex_total_ingresos.setStyle("font-size: 14px;color: black;font-weight: bold");
		tex_total_egresos.setId("tex_total_egresos");
		tex_total_egresos.setDisabled(true);
		tex_total_egresos.setStyle("font-size: 14px;color: black;font-weight: bold");

		Grid gri_valores = new Grid();
		gri_valores.setColumns(2);
		gri_valores.getChildren().add(eti_caja);
		gri_valores.getChildren().add(aut_caja);
		gri_valores.getChildren().add(eti_documento);
		gri_valores.getChildren().add(txt_documento);
		
		gri_valores.getChildren().add(new Etiqueta("Total Valor Solicitado"));
		gri_valores.getChildren().add(tex_total_ingresos);
		gri_valores.getChildren().add(new Etiqueta("Total Valor Pagado"));
		gri_valores.getChildren().add(tex_total_egresos);
		gri_valores.getChildren().add(new Etiqueta("Total Valor Adeuda"));
		gri_valores.getChildren().add(tex_valor_a_recibir);
		
		PanelTabla pat_panel1=new PanelTabla();
		pat_panel1.setPanelTabla(tab_anticipo);
		
		Division div_divisionC=new Division();
		div_divisionC.dividir2(pat_panel1, gri_valores, "70%", "V");

		// DIVISION 2  (TABULADOR - (CAPACIDAD DE PAGO/AMORTIZACION/GARANTE))

		tab_tabulador.setId("tab_tabulador");

		// CAPACIDAD DE PAGO (TABULADOR OPCION 1)		

		tab_datos_precancelacion.setId("tab_datos_precancelacion");
		tab_datos_precancelacion.setIdCompleto("tab_tabulador:tab_datos_precancelacion");
		tab_datos_precancelacion.setSql("select IDE_NRPRE,AMO.NRO_CUOTA_NRAMO,AMO.CUOTA_NRAMO,DETALLE_GEINS,DOC_DEPOSITO_NRPRE,FECHA_DEPOSITO_NRPRE,FECHA_PRECANCELADO_NRPRE,PATH_FOTO_NRPRE from NRH_PRECANCELACION PRE " +
				"INNER JOIN NRH_AMORTIZACION AMO ON AMO.IDE_NRAMO=PRE.IDE_NRAMO " +
				"INNER JOIN GEN_INSTITUCION INS ON INS.IDE_GEINS=PRE.IDE_GEINS " +
				"AND AMO.PRE_CANCELADO_NRAMO=TRUE " +
				"and amo.ide_nrani=-1 ");
		tab_datos_precancelacion.setCampoPrimaria("IDE_NRPRE");
		tab_datos_precancelacion.setNumeroTabla(13);
		tab_datos_precancelacion.getColumna("PATH_FOTO_NRPRE").setVisible(true);
		tab_datos_precancelacion.getColumna("PATH_FOTO_NRPRE").setUpload("nueva_carpeta");
		tab_datos_precancelacion.getColumna("IDE_NRPRE").setVisible(false);
		tab_datos_precancelacion.setColumnaSuma("CUOTA_NRAMO");
		tab_datos_precancelacion.setTipoFormulario(false);
		tab_datos_precancelacion.setLectura(true);
		tab_datos_precancelacion.setMostrarNumeroRegistros(false);
		tab_datos_precancelacion.dibujar();
		
		PanelTabla pat_panel6=new PanelTabla();
		pat_panel6.setPanelTabla(tab_datos_precancelacion);

		// AMORTIZACION (TABULADOR OPCION 2)

		tab_anticipo_interes.setId("tab_anticipo_interes");
		tab_anticipo_interes.setIdCompleto("tab_tabulador:tab_anticipo_interes");
		tab_anticipo_interes.setTabla("NRH_ANTICIPO_INTERES", "IDE_NRANI", 7);
		tab_anticipo_interes.getColumna("ACTIVO_NRANI").setCheck();
		tab_anticipo_interes.getColumna("ACTIVO_NRANI").setValorDefecto("true");
		tab_anticipo_interes.getColumna("PLAZO_NRANI").setNombreVisual("PLAZO (Meses)");
		tab_anticipo_interes.getColumna("AMORTIZACION_NRANI").setValorDefecto(utilitario.getVariable("p_amortizacion_cada"));
		tab_anticipo_interes.getColumna("AMORTIZACION_NRANI").setNombreVisual("AMORTIZACION CADA (Dias)");
		tab_anticipo_interes.getColumna("TASA_INTERES_NRANI").setMetodoChange("calcularTasaEfectiva");
		tab_anticipo_interes.getColumna("TASA_INTERES_NRANI").setValorDefecto(utilitario.getVariable("p_tasa_interes"));
		tab_anticipo_interes.getColumna("TASA_INTERES_NRANI").setVisible(false);
		tab_anticipo_interes.getColumna("AMORTIZACION_NRANI").setMetodoChange("calcularTasaEfectiva");
		tab_anticipo_interes.getColumna("TASA_EFECTIVA_NRANI").setValorDefecto(utilitario.getVariable("p_tasa_interes_efectiva"));
		tab_anticipo_interes.getColumna("TASA_EFECTIVA_NRANI").setLectura(true);
		tab_anticipo_interes.getColumna("TASA_EFECTIVA_NRANI").setVisible(false);
		tab_anticipo_interes.getColumna("AMORTIZACION_NRANI").setLectura(true);
		tab_anticipo_interes.getColumna("PLAZO_NRANI").setLectura(true);
		tab_anticipo_interes.getColumna("MES_GRACIA_NRANI").setValorDefecto("0");
		tab_anticipo_interes.getColumna("MES_GRACIA_NRANI").setVisible(false);
		tab_anticipo_interes.agregarRelacion(tab_amortizacion);
		tab_anticipo_interes.setTipoFormulario(true);
		tab_anticipo_interes.getGrid().setColumns(8);
		tab_anticipo_interes.setMostrarNumeroRegistros(false);
		tab_anticipo_interes.dibujar();

		PanelTabla pat_panel7=new PanelTabla();
		pat_panel7.setPanelTabla(tab_anticipo_interes);

		tab_amortizacion.setId("tab_amortizacion");
		tab_amortizacion.setIdCompleto("tab_tabulador:tab_amortizacion");
		tab_amortizacion.setTabla("NRH_AMORTIZACION", "IDE_NRAMO", 3);
		tab_amortizacion.getColumna("ACTIVO_NRAMO").setCheck();
		tab_amortizacion.getColumna("ACTIVO_NRAMO").setValorDefecto("false");
		tab_amortizacion.getColumna("PRE_CANCELADO_NRAMO").setCheck();
		tab_amortizacion.getColumna("PRE_CANCELADO_NRAMO").setValorDefecto("false");
		tab_amortizacion.getColumna("IDE_NRRUB").setLectura(true);
		tab_amortizacion.getColumna("IDE_NRRUB").setCombo("NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB","");
		tab_amortizacion.getColumna("CAPITAL_NRAMO").setLectura(true);
		tab_amortizacion.getColumna("INTERES_NRAMO").setLectura(true);
		tab_amortizacion.getColumna("CUOTA_NRAMO").setLectura(true);
		tab_amortizacion.getColumna("PRINCIPAL_NRAMO").setLectura(true);
		tab_amortizacion.getColumna("NRO_CUOTA_NRAMO").setLectura(true);
		tab_amortizacion.getColumna("FECHA_CANCELADO_NRAMO").setLectura(true);
		tab_amortizacion.setRecuperarLectura(true);
		tab_amortizacion.setColumnaSuma("CUOTA_NRAMO,PRINCIPAL_NRAMO");
		tab_amortizacion.setCampoOrden("FECHA_VENCIMIENTO_NRAMO ASC");
		tab_amortizacion.dibujar();

		PanelTabla pat_panel3=new PanelTabla();
		pat_panel3.setPanelTabla(tab_amortizacion);

		Grid gri_amor=new Grid();
		gri_amor.setColumns(1);
		gri_amor.setWidth("100%");
		gri_amor.getChildren().add(pat_panel7);
		gri_amor.getChildren().add(pat_panel3);
		
		tab_tabulador.agregarTab("TABLA DE AMORTIZACIÓN", gri_amor);
		tab_tabulador.agregarTab("PRE-CANCELACIONES", pat_panel6);
		
		
		//  DIVISION DE LA PANTALLA
		Division div_division=new Division();
		div_division.dividir2(div_divisionC, tab_tabulador, "30%", "H");
		agregarComponente(div_division);

		// PARA PRE CANCELACION DE ANTICPOS
		tab_precancelacion.setId("tab_precancelacion");
		tab_precancelacion.setTabla("NRH_PRECANCELACION", "IDE_NRPRE", 10);
		tab_precancelacion.getColumna("activo_nrpre").setValorDefecto("true");
		tab_precancelacion.getColumna("ide_geins").setValorDefecto("135");
		tab_precancelacion.getColumna("FECHA_PRECANCELADO_NRPRE").setValorDefecto(utilitario.getFechaActual());
		tab_precancelacion.getColumna("FECHA_DEPOSITO_NRPRE").setValorDefecto(utilitario.getFechaActual());
		//tab_precancelacion.getColumna("PATH_FOTO_NRPRE").setUpload("nueva_carpeta");		
		tab_precancelacion.setCondicion("IDE_NRPRE=-1");
		tab_precancelacion.dibujar();
		tab_precancelacion.setStyle("display:none");
		agregarComponente(tab_precancelacion);
		
		tab_abono.setId("tab_abono");
        tab_abono.setTabla("fac_cobro", "ide_facob", 11);
        tab_abono.setCondicion("ide_fafac=-1");
        tab_abono.setGenerarPrimaria(false);      
        tab_abono.getColumna("fecha_cobro_facob").setValorDefecto(utilitario.getFechaActual());
        tab_abono.getColumna("valor_cobro_interes_facob").setValorDefecto("0.00");
        tab_abono.getColumna("valor_cobro_facob").setValorDefecto("0.00");
        tab_abono.getColumna("valor_cobro_iva_facob").setValorDefecto("0.00");
        tab_abono.getColumna("activo_facob").setValorDefecto("true");
        tab_abono.dibujar();
        tab_abono.setStyle("display:none");        
		agregarComponente(tab_abono);
		

		eti_tot_precancelar.setId("eti_tot_precancelar");
		eti_tot_precancelar.setValue("Total Precancelar: 0.0");
		eti_tot_precancelar.setStyle("font-size:18px;color: green;font-weight: bold");

		eti_tot_adeuda.setId("eti_tot_adeuda");
		eti_tot_adeuda.setValue("Total Adeuda: 0.0");
		eti_tot_adeuda.setStyle("font-size:18px;color: red;font-weight: bold");


		tab_pagos_anticipos.setId("tab_pagos_anticipos");
		tab_pagos_anticipos.setSql("select IDE_NRAMO,NRO_CUOTA_NRAMO," +
				"FECHA_VENCIMIENTO_NRAMO,PRINCIPAL_NRAMO,INTERES_NRAMO,CUOTA_NRAMO,CAPITAL_NRAMO " +
				"from NRH_AMORTIZACION " +
				"WHERE IDE_NRANI IN (SELECT IDE_NRANI FROM NRH_ANTICIPO_INTERES WHERE IDE_NRANT=-1) " +
				"AND IDE_NRRUB IN ("+utilitario.getVariable("p_nrh_rubro_descuento_nomina")+") " +
				"and ACTIVO_NRAMO=FALSE " +
				"ORDER BY FECHA_VENCIMIENTO_NRAMO ASC");
		tab_pagos_anticipos.setCampoPrimaria("IDE_NRAMO");
		tab_pagos_anticipos.setNumeroTabla(12);
		tab_pagos_anticipos.setTipoSeleccion(true);
		tab_pagos_anticipos.onSelectCheck("seleccionaCuotaPrecancelar");
		tab_pagos_anticipos.onUnselectCheck("deSeleccionaCuotaPrecancelar");
		tab_pagos_anticipos.getColumna("CUOTA_NRAMO").setEstilo("font-size: 14px;color: black;font-weight: bold");
		tab_pagos_anticipos.setColumnaSuma("CUOTA_NRAMO");
		tab_pagos_anticipos.dibujar();

		PanelTabla pat_pagos_ant=new PanelTabla();
		pat_pagos_ant.setPanelTabla(tab_pagos_anticipos);

		dia_precancelacion.setId("dia_precancelacion");
		dia_precancelacion.setWidth("50%");
		dia_precancelacion.setHeight("55%");
		
		
		// USUARIO REALIZA PAGO
        Etiqueta eti_usuario = new Etiqueta("NOMBRE REALIZA PAGO :");
        eti_usuario.setStyle("font-size: 15px;font-weight: bold;text-aling:left");
       	txt_cliente.setId("txt_cliente");
       	txt_cliente.setStyle("width:350px;text-aling:left");
       	
       	// DOCUEMNTO CHEQUE
    	Etiqueta eti_cheque = new Etiqueta("NRO CHEQUE/TRASNFERENCIA:");
    	eti_cheque.setStyle("font-size: 15px;font-weight: bold;text-aling:left");
		txt_cheque.setId("txt_cheque");
       	txt_cheque.setStyle("width:350px;text-aling:left");		
       	
       	// OBSERVACIONES
    	Etiqueta eti_observacion = new Etiqueta("OBSERVACIONES:");
    	eti_observacion.setStyle("font-size: 15px;font-weight: bold;text-aling:left");
       	txt_observacion.setId("txt_observacion");
       	txt_observacion.setValue("");
       	txt_observacion.setStyle("width:350px;text-aling:left");	 
       	
		Grid gri_pre_cancelar=new Grid();
		gri_pre_cancelar.setColumns(1);
		
		Grid gri_tabla=new Grid();
		gri_tabla.setColumns(2);
		
		gri_tabla.getChildren().add(eti_usuario);
		gri_tabla.getChildren().add(txt_cliente);
		gri_tabla.getChildren().add(eti_cheque);
		gri_tabla.getChildren().add(txt_cheque);
		gri_tabla.getChildren().add(eti_observacion);
		gri_tabla.getChildren().add(txt_observacion);
		gri_tabla.getChildren().add(eti_tot_adeuda);
		gri_tabla.getChildren().add(eti_tot_precancelar);
		
		gri_pre_cancelar.getChildren().add(gri_tabla);
		gri_pre_cancelar.getChildren().add(pat_pagos_ant);
		
		gri_pre_cancelar.setStyle("width:" + (dia_precancelacion.getAnchoPanel() - 5) + "px;height:" + dia_precancelacion.getAltoPanel() + "px;overflow: auto;display: block;");
		
		dia_precancelacion.setDialogo(gri_pre_cancelar);
		dia_precancelacion.getBot_aceptar().setMetodo("aceptarPrecancelacion");
		dia_precancelacion.setDynamic(false);
		agregarComponente(dia_precancelacion);
		
		//REPORTE
        vpdf_pago.setId("vpdf_pago");
        vpdf_pago.setTitle("Detalle de la Recaudación");
        agregarComponente(vpdf_pago);
	}

	public void verDatosPreCancelacion(){
		tab_datos_precancelacion.setSql("select IDE_NRPRE,AMO.NRO_CUOTA_NRAMO,AMO.CUOTA_NRAMO,DETALLE_GEINS,DOC_DEPOSITO_NRPRE,FECHA_DEPOSITO_NRPRE,FECHA_PRECANCELADO_NRPRE,PATH_FOTO_NRPRE from NRH_PRECANCELACION PRE " +
				"INNER JOIN NRH_AMORTIZACION AMO ON AMO.IDE_NRAMO=PRE.IDE_NRAMO " +
				"INNER JOIN GEN_INSTITUCION INS ON INS.IDE_GEINS=PRE.IDE_GEINS " +
				"AND AMO.PRE_CANCELADO_NRAMO=true " +
				"and amo.ide_nrani="+tab_amortizacion.getValor("IDE_NRANI"));
		tab_datos_precancelacion.ejecutarSql();
		
	}

	public void calcularTotalPrecancelar(){
		double dou_tot_precancelar=0;
		try {

			for (int i = 0; i < tab_pagos_anticipos.getListaFilasSeleccionadas().size(); i++) {
				Object fila[]=tab_pagos_anticipos.getListaFilasSeleccionadas().get(i).getCampos();
				dou_tot_precancelar=pckUtilidades.CConversion.CDbl_2(fila[5]+"")+dou_tot_precancelar; 
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println("tot precancelar "+dou_tot_precancelar);
		recibido=dou_tot_precancelar;
		eti_tot_precancelar.setValue("TOTAL PRECANCELAR: "+utilitario.getFormatoNumero(dou_tot_precancelar));
		utilitario.addUpdate("eti_tot_precancelar");
	}
	public void seleccionaCuotaPrecancelar(SelectEvent evt){
		tab_pagos_anticipos.seleccionarFila(evt);
		calcularTotalPrecancelar();
	}
	public void deSeleccionaCuotaPrecancelar(UnselectEvent evt){
		calcularTotalPrecancelar();
	}

	public boolean validarPagosSeleccionadosPrecancelacion()
	{
		Fila fila=tab_pagos_anticipos.getListaFilasSeleccionadas().get(0);
		TablaGenerica tab_aux=utilitario.consultar("SELECT * FROM NRH_AMORTIZACION " +
				"where IDE_NRANI="+tab_anticipo_interes.getValorSeleccionado()+" " +
				"and ACTIVO_NRAMO = false  " +
				"order by FECHA_VENCIMIENTO_NRAMO ASC");
		int int_indice_tab_aux=0;
		for (int i = 0; i < tab_pagos_anticipos.getListaFilasSeleccionadas().size(); i++) {
			fila=tab_pagos_anticipos.getListaFilasSeleccionadas().get(i);
			String str_fecha_ven_seleccionada=utilitario.getFormatoFecha(utilitario.getFecha(fila.getCampos()[2]+""));
			String str_fecha_ven_tab_aux=tab_aux.getValor(int_indice_tab_aux, "FECHA_VENCIMIENTO_NRAMO");
			str_fecha_ven_tab_aux=utilitario.getFormatoFecha(utilitario.getFecha(str_fecha_ven_tab_aux));
			int_indice_tab_aux=int_indice_tab_aux+1;
			if (!str_fecha_ven_tab_aux.equalsIgnoreCase(str_fecha_ven_seleccionada)){
				utilitario.agregarMensajeError("No se puede precancelar", "El orden seleccionado es incorrecto no se puede saltar un pago");
				return false;
			}
		}
		
		if(validaDocumentoCobro(txt_documento.getValue().toString()))
		{
			System.out.println("Duplicado de Secuencial abonos: " + txt_documento.getValue().toString());
			int secuencial=pckUtilidades.CConversion.CInt(txt_documento.getValue().toString()) + 1;
			System.out.println("Actualizando el Secuencial abonos: " + secuencial);
			txt_documento.setValue(secuencial);
		}
		
		if(validaDocumentoCobro(txt_documento.getValue().toString()))
		{
			utilitario.agregarNotificacionInfo("NO Cobrado", "Secuencial ya utilizado, favor cierre el navegador y vuelva a ingresar... si el problema persiste contactese con el Administrador...");
			return false;
		}
		
		return true;
	}
	
	
	public void aceptarPrecancelacion()
	{
		Locale locale=new Locale("es","ES");
		dia_precancelacion.cerrar();
		if (tab_pagos_anticipos.getListaFilasSeleccionadas().size()>0){
			if (validarPagosSeleccionadosPrecancelacion()){
				Fila fila;

				String str_doc_deposito=txt_documento.getValue().toString();
				String str_observaciones=txt_observacion.getValue().toString();

				for (int i = 0; i < tab_pagos_anticipos.getListaFilasSeleccionadas().size(); i++) 
				{
					fila=tab_pagos_anticipos.getListaFilasSeleccionadas().get(i);
					utilitario.getConexion().agregarSql("update NRH_AMORTIZACION set ACTIVO_NRAMO=TRUE,PRE_CANCELADO_NRAMO=true ,FECHA_CANCELADO_NRAMO=TO_DATE('"+utilitario.getFechaActual()+"', 'yy-mm-dd') " +
							"where IDE_NRAMO ="+fila.getRowKey());

						tab_precancelacion.insertar();
						tab_precancelacion.setValor("IDE_NRAMO", fila.getRowKey());
						tab_precancelacion.setValor("DOC_DEPOSITO_NRPRE", str_doc_deposito);
						//tab_precancelacion.setValor("PATH_FOTO_NRPRE", str_path_foto);
						tab_precancelacion.setValor("OBSERVACIONES_NRPRE", str_observaciones);
						
						tab_abono.insertar();
						tab_abono.setValor("nro_documento_facob",str_doc_deposito);
						tab_abono.setValor("ide_retip",com_forma_pago.getValue().toString());
				        tab_abono.setValor("ide_tecaj",ide_caja);
				        tab_abono.setValor("ide_sucu",ide_sucursal);
				        tab_abono.setValor("ide_gtemp",ide_empleado);
				        tab_abono.setValor("ide_nramo",fila.getRowKey());
				        tab_abono.setValor("documento_cobro",txt_cheque.getValue().toString());
				        tab_abono.setValor("observaciones_facob", str_observaciones);
				        tab_abono.setValor("cliente_pago_facob", pckUtilidades.CConversion.CStr(txt_cliente.getValue()).toString().toUpperCase());
				        tab_abono.setValor("valor_cobro_facob",pckUtilidades.CConversion.CDbl_2(fila.getCampos()[5])+"");	
				}		
				
				if (tab_abono.guardar()){
					tab_precancelacion.guardar();	
				
					guardarPantalla();
					//utilitario.addUpdate("tab_precancelacion,tab_abono");					
					System.out.println("guardaSecuencial abonos: "+par_modulosec_recaudacion);
					ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_modulosec_recaudacion), par_modulosec_recaudacion);
					tab_amortizacion.ejecutarValorForanea(tab_anticipo_interes.getValorSeleccionado());
					actualizarEstadoActivoAnticipo();
					verDatosPreCancelacion();
					utilitario.addUpdate("tab_tabulador:tab_amortizacion,tab_tabulador:tab_datos_precancelacion");
				}
				
				//AQUI ABRE EL REPORTE
	            Map p_parametros = new HashMap();
	            p_parametros.put("titulo", "EMGIRS - EP");
	            p_parametros.put("p_gtemp", pckUtilidades.CConversion.CInt(aut_empleado.getValor()));
	            p_parametros.put("p_cliente_cobro", pckUtilidades.CConversion.CStr(txt_cliente.getValue()).toString().toUpperCase());
	            p_parametros.put("p_nro_comprobante", pckUtilidades.CConversion.CInt(txt_documento.getValue()));
	            p_parametros.put("p_observacion", pckUtilidades.CConversion.CStr(txt_observacion.getValue())); 
	            p_parametros.put("p_ide_tecaj", pckUtilidades.CConversion.CInt(ide_caja));
	            p_parametros.put("p_ide_gtemp", pckUtilidades.CConversion.CInt(ide_empleado));
	            p_parametros.put("p_recibido", recibido);
	            p_parametros.put("p_saldo", (deuda-recibido));

	            p_parametros.put("REPORT_LOCALE", locale);

	            vpdf_pago.setVisualizarPDF("rep_facturacion/rep_recibo_caja_anticipo_tk.jasper", p_parametros);
	            vpdf_pago.dibujar();
	            utilitario.addUpdate("vpdf_pago");
				
	            utilitario.agregarMensaje("Cobrado", "Se cobro con exito, recuerde imprimir su comprobante");
				System.out.println("Cobrado Anticipos empleados.... favor esperar a que el funcionario imprima el comprobante....");	
			}
		}else{
			utilitario.agregarMensajeInfo("No ha seleccionado ningun anticipo para precancelar", "");
		}
	}

	public void actualizarEstadoActivoAnticipo(){

		if (tab_anticipo.getValor("ANULADO_NRANT")==null || tab_anticipo.getValor("ANULADO_NRANT").isEmpty()
				|| tab_anticipo.getValor("ANULADO_NRANT").equalsIgnoreCase("false")
				|| tab_anticipo.getValor("ANULADO_NRANT").equalsIgnoreCase("0")){

			TablaGenerica tab_cuotas=utilitario.consultar("select * from NRH_AMORTIZACION where IDE_NRANI in ( " +
					"select ide_nrani from NRH_ANTICIPO_INTERES where IDE_NRANT="+tab_anticipo.getValorSeleccionado()+")");
			if (tab_cuotas.getTotalFilas()>0){
				TablaGenerica tab_cuotas_pagadas=utilitario.consultar("select * from NRH_AMORTIZACION where IDE_NRANI in ( " +
						"select ide_nrani from NRH_ANTICIPO_INTERES where IDE_NRANT="+tab_anticipo.getValorSeleccionado()+") " +
						"and ACTIVO_NRAMO=TRUE");
				if (tab_cuotas.getTotalFilas()==tab_cuotas_pagadas.getTotalFilas()){
					if (tab_anticipo.getValor("ACTIVO_NRANT")!=null
							&& !tab_anticipo.getValor("ACTIVO_NRANT").isEmpty()
							&& (tab_anticipo.getValor("ACTIVO_NRANT").equalsIgnoreCase("true")
									|| tab_anticipo.getValor("ACTIVO_NRANT").equalsIgnoreCase("true"))){
						utilitario.getConexion().ejecutarSql("update NRH_ANTICIPO set ACTIVO_NRANT=FALSE where IDE_NRANT="+tab_anticipo.getValorSeleccionado());

						String ide_nrant_anterior=tab_anticipo.getValorSeleccionado(); 
						tab_anticipo.setCondicion("ANTICIPO_NRANT=TRUE and ACTIVO_NRANT=true AND IDE_GTEMP="+aut_empleado.getValor());
						tab_anticipo.ejecutarSql();
						tab_anticipo.setFilaActual(ide_nrant_anterior);
					}
				}else{
					String ide_nrant_anterior=tab_anticipo.getValorSeleccionado(); 

					utilitario.getConexion().ejecutarSql("update NRH_ANTICIPO set ACTIVO_NRANT=TRUE where IDE_NRANT="+tab_anticipo.getValorSeleccionado());
					tab_anticipo.setCondicion("ANTICIPO_NRANT=TRUE and ACTIVO_NRANT=true AND IDE_GTEMP="+aut_empleado.getValor());
					tab_anticipo.ejecutarSql();
					tab_anticipo.setFilaActual(ide_nrant_anterior);

				}
			}
		}
	}

	
	public void preCancelarAnticipo(){

		if (tab_anticipo.getTotalFilas()==0){
			utilitario.agregarMensajeInfo("No se puede pre-cancelar","No existe solicitud de anticipo");
			return;
		}
		if (tab_anticipo.isFilaInsertada()){
			utilitario.agregarMensajeInfo("No se puede realizar la pre cancelacion", "Primero debe guardar el registro que esta trabajando");
			return;
		}
		if (tab_anticipo.getValor("ANULADO_NRANT") !=null 
				&& !tab_anticipo.getValor("ANULADO_NRANT").isEmpty()
				&& tab_anticipo.getValor("ANULADO_NRANT").equalsIgnoreCase("true")){
			utilitario.agregarMensajeInfo("No se puede realizar la precancelacion", "El anticipo se encuentra anulado ");
			return;
		}
		if (tab_anticipo.getValor("APROBADO_NRANT") ==null 
				|| tab_anticipo.getValor("APROBADO_NRANT").isEmpty()
				|| tab_anticipo.getValor("APROBADO_NRANT").equalsIgnoreCase("false")){
			utilitario.agregarMensajeInfo("No se puede realizar la precancelacion", "El anticipo aun no se encuentra Aprobado");
			return;
		}

		if (tab_amortizacion.getTotalFilas()==0 ){
			utilitario.agregarMensajeInfo("No se puede precancelar", "No existe tabla de amortizacion");
			return;
		}
		tab_pagos_anticipos.setSql("select IDE_NRAMO,NRO_CUOTA_NRAMO," +
				"FECHA_VENCIMIENTO_NRAMO,PRINCIPAL_NRAMO,INTERES_NRAMO,CUOTA_NRAMO,CAPITAL_NRAMO " +
				"from NRH_AMORTIZACION " +
				"WHERE IDE_NRANI IN (SELECT IDE_NRANI FROM NRH_ANTICIPO_INTERES WHERE IDE_NRANT="+tab_anticipo.getValorSeleccionado()+") " +
				"and ACTIVO_NRAMO=FALSE " +
				"ORDER BY FECHA_VENCIMIENTO_NRAMO ASC");
		tab_pagos_anticipos.ejecutarSql();
		if (tab_pagos_anticipos.getTotalFilas()==0){
			utilitario.agregarMensajeInfo("No se puede precancelar", "No existe pagos por precancelar");
			return;
		}
		
		if(txt_documento.getValue()==null || txt_documento.getValue().toString().length()==0){
			utilitario.agregarMensajeInfo("No se puede cobrar", "No existe un numero de comprobante asignado");
			return;
		}
		
		if(ide_empleado==null || ide_empleado==""){
			utilitario.agregarMensajeInfo("No se puede cobrar", "No existe un recaudador asignado");
			return;
		}
		
		if(pckUtilidades.CConversion.CInt(ide_caja)==0){
			utilitario.agregarMensajeInfo("No se puede cobrar", "No esta autorizado para el cobro, favor contacte al administrador.");
			return;
		}
		deuda=pckUtilidades.CConversion.CDbl_2(tab_pagos_anticipos.getSumaColumna("CUOTA_NRAMO"));
		eti_tot_adeuda.setValue("TOTAL ADEUDA: "+utilitario.getFormatoNumero(deuda));
		eti_tot_precancelar.setValue("TOTAL PRECANCELAR: 0.0");
		tab_precancelacion.limpiar();
		tab_abono.limpiar();

		dia_precancelacion.setTitle("PRECANCELACION DE ANTICIPOS");					
		dia_precancelacion.dibujar();
		 
	}

	
	public void cargarTotales() {
		// USAR PARAMETROS

		tex_total_ingresos.setValue("0");
		tex_total_egresos.setValue("0");
		tex_valor_a_recibir.setValue("0");

		try {
			
			
			TablaGenerica tab_prestamo=utilitario.consultar("SELECT ant.ide_nrant,sum(amor.cuota_nramo) as total_pres  "
					+ "FROM nrh_anticipo ant  "
					+ "left join nrh_anticipo_interes anti on anti.ide_nrant=ant.ide_nrant  "
					+ "left join nrh_amortizacion amor on amor.ide_nrani=anti.ide_nrani  "
					+ "where ant.ide_nrant="+tab_anticipo.getValorSeleccionado()+" and ant.aprobado_nrant=true and activo_nrant=true "
					+ "group by ant.ide_nrant");
			
			
			TablaGenerica tab_ing_egr_ant=utilitario.consultar("SELECT ant.ide_nrant,sum(amor.cuota_nramo) as total_pres  "
					+ "FROM nrh_anticipo ant "
					+ "left join nrh_anticipo_interes anti on anti.ide_nrant=ant.ide_nrant "
					+ "left join nrh_amortizacion amor on amor.ide_nrani=anti.ide_nrani "
					+ "where ant.ide_nrant="+tab_anticipo.getValorSeleccionado()+" and ant.aprobado_nrant=true and activo_nrant=true and amor.fecha_cancelado_nramo is not null " // and amor.ide_nrrol is not null "
					+ "group by ant.ide_nrant");
			
			
			if (tab_prestamo.getTotalFilas()> 0) {
				double dou_tot_ingresos = pckUtilidades.CConversion.CDbl_2(tab_prestamo.getValor("total_pres"));
				tex_total_ingresos.setValue(utilitario.getFormatoNumero(dou_tot_ingresos));
				double dou_val_recibir=0;
				if (tab_ing_egr_ant.getTotalFilas()> 0) {
					dou_val_recibir = pckUtilidades.CConversion.CDbl_2(tab_ing_egr_ant.getValor("total_pres"));
					tex_total_egresos.setValue(utilitario.getFormatoNumero(dou_val_recibir));
					
				}else {
					dou_val_recibir=0;
					tex_total_egresos.setValue(dou_val_recibir);

				}
			
				double dou_tot_egresos = dou_tot_ingresos-dou_val_recibir;
				tex_valor_a_recibir.setValue(utilitario.getFormatoNumero(dou_tot_egresos));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		utilitario.addUpdate("tex_valor_a_recibir");
		utilitario.addUpdate("tex_total_ingresos");
		utilitario.addUpdate("tex_total_egresos");

	}
	
	
	@Override
	public void actualizar() {
		// TODO Auto-generated method stub
		super.actualizar();

		cargarTablasPantalla();
		cargarTotales();
	}

	public String obtenerCaja(){
		String caja="";
		List list_sql1 = utilitario.getConexion().consultar("select ide_tecaj from fac_usuario_lugar where ide_usua ="+utilitario.getVariable("IDE_USUA")+" and activo_fausl=true and recauda_fausl =true limit 1");	
		//System.out.println(" fff "+list_sql1);
		if (!list_sql1.isEmpty() && list_sql1.get(0) != null){
			caja=String.valueOf(list_sql1.get(0));
		}
		return caja;
	}
	
	public String obtenerEmpleado(){
		String empleado="";
		List list_sql1 = utilitario.getConexion().consultar("select ide_gtemp from sis_usuario where ide_usua= "+utilitario.getVariable("IDE_USUA"));	
		//System.out.println(" fff "+list_sql1);
		if (!list_sql1.isEmpty() && list_sql1.get(0) != null){
			empleado=String.valueOf(list_sql1.get(0));
		}
		return empleado;
	}
	
	public String obtenerSucursal(){
		String sucursal="";
		List list_sql1 = utilitario.getConexion().consultar("select ide_sucu from sis_usuario_sucursal where ide_usua= "+utilitario.getVariable("IDE_USUA"));	
		//System.out.println(" fff "+list_sql1);
		if (!list_sql1.isEmpty() && list_sql1.get(0) != null){
			sucursal=String.valueOf(list_sql1.get(0));
		}
		return sucursal;
	}
	
	public String obtenerLugarCobro(){
		String lugar="";
		List list_sql1 = utilitario.getConexion().consultar("select ide_falug from fac_usuario_lugar where ide_usua ="+utilitario.getVariable("IDE_USUA")+" and activo_fausl=true and recauda_fausl =true limit 1");	
		if (!list_sql1.isEmpty() && list_sql1.get(0) != null){
			lugar=String.valueOf(list_sql1.get(0));
		}
		return lugar;
	}
	
	public boolean validaDocumentoCobro(String nro_documento){
		boolean existe=false;
		System.out.println("ValidaDocumentoCobro "+nro_documento);
		
		List list_sql1 = utilitario.getConexion().consultar("select ide_facob FROM fac_cobro fa WHERE nro_documento_facob = "+nro_documento);	
		//System.out.println(" fff "+list_sql1);
		if (!list_sql1.isEmpty() && list_sql1.get(0) != null){
			existe=true;
		}
		return existe;
	}
	
	/**
	 * metodo para el boton Inicio del navegador de paginas, muestra el primer registro de la tabla 
	 * 
	 */
	@Override
	public void inicio() {
		// TODO Auto-generated method stub
		super.inicio();
		cargarTablasPantalla();
		cargarTotales();
	}


	/**
	 * metodo para el boton Fin del navegador de paginas, muestra el ultimo registro de la tabla 
	 * 
	 */
	@Override
	public void fin() {
		// TODO Auto-generated method stub
		super.fin();
		cargarTablasPantalla();
		cargarTotales();
	}


	/**
	 * metodo para el boton Siguiente del navegador de paginas, muestra un registro posterior del registro actual de la tabla 
	 * 
	 */
	@Override
	public void siguiente() {
		// TODO Auto-generated method stub
		super.siguiente();
		cargarTablasPantalla();
		cargarTotales();
	}


	/**
	 * metodo para el boton Atras del navegador de paginas, muestra un registro anterior del registro actual de la tabla 
	 * 
	 */
	@Override
	public void atras() {
		// TODO Auto-generated method stub
		super.atras();
		cargarTablasPantalla();
		cargarTotales();
	}

	public void inicializarColumnasTablaAnticipo(){
		/*if (tab_anticipo.getValor("CALIFICADO_NRANT") == null 
				|| tab_anticipo.getValor("CALIFICADO_NRANT").isEmpty() 
				|| tab_anticipo.getValor("CALIFICADO_NRANT").equalsIgnoreCase("false")){
			tab_anticipo.getColumna("NRO_MES_NRANT").setLectura(false);
			tab_anticipo.getColumna("MONTO_SOLICITADO_NRANT").setLectura(false);
			tab_anticipo.getColumna("FECHA_APROBACION_NRANT").setLectura(false);
			tab_anticipo.getColumna("IDE_NRMOA").setLectura(false);
		}else if (tab_anticipo.getValor("CALIFICADO_NRANT").equals("true")){
			tab_anticipo.getColumna("NRO_MES_NRANT").setLectura(true);
			tab_anticipo.getColumna("MONTO_SOLICITADO_NRANT").setLectura(true);
			tab_anticipo.getColumna("FECHA_APROBACION_NRANT").setLectura(true);
			tab_anticipo.getColumna("IDE_NRMOA").setLectura(true);
		}*/
	}

	public void inicializarColumnasTablaAnticipoInteres(){
		if (tab_anticipo.getValor("CALIFICADO_NRANT") == null 
				|| tab_anticipo.getValor("CALIFICADO_NRANT").isEmpty() 
				|| tab_anticipo.getValor("CALIFICADO_NRANT").equalsIgnoreCase("false")){
			tab_anticipo_interes.getColumna("TASA_INTERES_NRANI").setLectura(false);
			tab_anticipo_interes.getColumna("MES_GRACIA_NRANI").setLectura(false);
			tab_anticipo_interes.getColumna("AMORTIZACION_NRANI").setLectura(false);
			tab_anticipo_interes.getColumna("ACTIVO_NRANI").setLectura(false);
		}else if (tab_anticipo.getValor("CALIFICADO_NRANT").equals("true")){
			tab_anticipo_interes.getColumna("TASA_INTERES_NRANI").setLectura(true);
			tab_anticipo_interes.getColumna("MES_GRACIA_NRANI").setLectura(true);
			tab_anticipo_interes.getColumna("AMORTIZACION_NRANI").setLectura(true);
			tab_anticipo_interes.getColumna("ACTIVO_NRANI").setLectura(true);
		}
	}

	public void cargarTablasPantalla()
	{
		tab_anticipo.setDibujo(false);
		actualizarEstadoActivoAnticipo(); //awbecerra 
		inicializarColumnasTablaAnticipo();
		tab_anticipo.setDibujo(true);

		inicializarColumnasTablaAnticipoInteres();
		tab_anticipo_interes.ejecutarValorForanea(tab_anticipo.getValor("IDE_NRANT"));
		tab_amortizacion.ejecutarValorForanea(tab_anticipo_interes.getValor("IDE_NRANI"));
		tab_amortizacion.getColumna("CUOTA_NRAMO").setTotal(tab_amortizacion.getSumaColumna("CUOTA_NRAMO"));
		tab_amortizacion.getColumna("PRINCIPAL_NRAMO").setTotal(tab_amortizacion.getSumaColumna("PRINCIPAL_NRAMO"));
		verDatosPreCancelacion();
		
		txt_documento.setValue(ser_contabilidad.numeroSecuencial(par_modulosec_recaudacion));
		utilitario.addUpdate("tab_recaudacion,txt_documento");
	}

	
	public void filtrarAnticiposEmpleado(SelectEvent evt){
		aut_empleado.onSelect(evt);
		txt_cliente.setValue(aut_empleado.getValorArreglo(2));
		tab_anticipo.setCondicion("ANTICIPO_NRANT=TRUE and ACTIVO_NRANT=true AND IDE_GTEMP="+aut_empleado.getValor());
		tab_anticipo.ejecutarSql();
		utilitario.addUpdate("txt_cliente");
		cargarTablasPantalla();
		cargarTotales();

	}

	/**
	 * limpia toda la pantalla incluyendo el autocompletar
	 */
	public void limpiar() {
		recibido=0;
		deuda=0;
		tab_anticipo.limpiar();
		tab_anticipo_interes.limpiar();
		tab_amortizacion.limpiar();
		tab_precancelacion.limpiar();
		aut_empleado.limpiar();
		tab_amortizacion.getColumna("CUOTA_NRAMO").setTotal(0);
		tab_amortizacion.getColumna("PRINCIPAL_NRAMO").setTotal(0);
		tex_total_ingresos.setValue("0");
		tex_total_egresos.setValue("0");
		tex_valor_a_recibir.setValue("0");
		utilitario.addUpdate("tex_valor_a_recibir");
		utilitario.addUpdate("tex_total_ingresos");
		utilitario.addUpdate("tex_total_egresos");
		utilitario.addUpdate("aut_empleado");// limpia y refresca el autocompletar
	}

	/* (non-Javadoc)
	 * @see paq_sistema.aplicacion.Pantalla#insertar()
	 * metodo para insertar un registro en cualquier tabla de la pantalla
	 */
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		
		utilitario.agregarMensajeInfo("No se puede insertar", "Unicamente para cobros");
	
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub

		utilitario.agregarMensajeInfo("No se puede guardar", "Use el Boton Cancelar o Abonar");
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.agregarMensajeInfo("No se puede eliminar", "Unicamente para cobros");
	}


	public Tabla getTab_anticipo() {
		return tab_anticipo;
	}

	public void setTab_anticipo(Tabla tab_anticipo) {
		this.tab_anticipo = tab_anticipo;
	}

	public Tabla getTab_amortizacion() {
		return tab_amortizacion;
	}

	public void setTab_amortizacion(Tabla tab_amortizacion) {
		this.tab_amortizacion = tab_amortizacion;
	}

	public AutoCompletar getAut_empleado() {
		return aut_empleado;
	}

	public void setAut_empleado(AutoCompletar aut_empleado) {
		this.aut_empleado = aut_empleado;
	}
	
	public Tabla getTab_anticipo_interes() {
		return tab_anticipo_interes;
	}

	public void setTab_anticipo_interes(Tabla tab_anticipo_interes) {
		this.tab_anticipo_interes = tab_anticipo_interes;
	}

	public Dialogo getDia_precancelacion() {
		return dia_precancelacion;
	}

	public void setDia_precancelacion(Dialogo dia_precancelacion) {
		this.dia_precancelacion = dia_precancelacion;
	}

	public Tabla getTab_pagos_anticipos() {
		return tab_pagos_anticipos;
	}

	public void setTab_pagos_anticipos(Tabla tab_pagos_anticipos) {
		this.tab_pagos_anticipos = tab_pagos_anticipos;
	}

	public Tabla getTab_precancelacion() {
		return tab_precancelacion;
	}

	public void setTab_precancelacion(Tabla tab_precancelacion) {
		this.tab_precancelacion = tab_precancelacion;
	}

	public Tabla getTab_datos_precancelacion() {
		return tab_datos_precancelacion;
	}

	public void setTab_datos_precancelacion(Tabla tab_datos_precancelacion) {
		this.tab_datos_precancelacion = tab_datos_precancelacion;
	}

	public Tabla getTab_abono() {
		return tab_abono;
	}

	public void setTab_abono(Tabla tab_abono) {
		this.tab_abono = tab_abono;
	}

	public AutoCompletar getAut_caja() {
		return aut_caja;
	}

	public void setAut_caja(AutoCompletar aut_caja) {
		this.aut_caja = aut_caja;
	}

	public Combo getCom_forma_pago() {
		return com_forma_pago;
	}

	public void setCom_forma_pago(Combo com_forma_pago) {
		this.com_forma_pago = com_forma_pago;
	}

	
	
	
}

