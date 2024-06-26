package paq_presupuesto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import paq_contabilidad.ejb.ServicioContabilidad;
import paq_general.ejb.ServicioGeneral;
import paq_presupuesto.ejb.ServicioPresupuesto;
import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Check;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import paq_presupuesto.pre_contratacion;
public class pre_poa_reforma_fuente extends Pantalla {
	
	private Tabla tab_poa_reforma_fuenta =new Tabla();
	private SeleccionTabla set_reforma_fuente = new SeleccionTabla();
	private Combo com_anio=new Combo();
	private Combo com_fuente_financiamiento=new Combo();
	private Combo com_area=new Combo();
	private Etiqueta eti_valor_total = new Etiqueta();
	private Dialogo dia_datos_reforma=new Dialogo();
	private Texto txt_num_resolucion_guarda=new Texto();
	private Calendario cal_fecha_reforma=new Calendario();
	
	private Radio rad_imprimir= new Radio();
	//private Check che_aprobado=new Check();
	private Etiqueta eti_mensajesaldos = new Etiqueta();
    private int ide_prfuf=-1;
    private String ide_geare="-1";
    private SeleccionTabla sel_resolucion= new SeleccionTabla();

	@EJB
	private ServicioPresupuesto ser_presupuesto=(ServicioPresupuesto)utilitario.instanciarEJB(ServicioPresupuesto.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioGeneral ser_general = (ServicioGeneral ) utilitario.instanciarEJB(ServicioGeneral.class);

	public pre_poa_reforma_fuente() {
		
		bar_botones.getBot_insertar().setRendered(false);
		bar_botones.getBot_eliminar().setRendered(false);
		bar_botones.getBot_atras().setRendered(false);
		bar_botones.getBot_fin().setRendered(false);
		bar_botones.getBot_inicio().setRendered(false);
		bar_botones.getBot_siguiente().setRendered(false);
		
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);

		com_fuente_financiamiento.setId("com_fuente_financiamiento");
		com_fuente_financiamiento.setCombo("select ide_prfuf,detalle_prfuf from  pre_fuente_financiamiento where activo_prfuf=true order by detalle_prfuf");
		com_fuente_financiamiento.setStyle("width: 200px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Fuente Financiamiento"));
		bar_botones.agregarComponente(com_fuente_financiamiento);
		
		com_area.setId("com_area");
		com_area.setCombo("select ide_geare,detalle_geare from  gen_area order by detalle_geare");
		com_area.setStyle("width: 200px; margin: 0 0 -8px 0;");
		com_area.setMetodo("cambiaArea");
		bar_botones.agregarComponente(new Etiqueta("Area (Opcional)"));
		bar_botones.agregarComponente(com_area);
		
		Boton bot_buscar = new Boton();
		bot_buscar.setIcon("ui-icon-person");
		bot_buscar.setValue("Buscar POA");
		bot_buscar.setMetodo("mostrarSetReformaFuente");
		bar_botones.agregarBoton(bot_buscar); 
		
		List lista = new ArrayList();
	    Object fila1[] = {
	       "0", "Reforma Techo/Piso"
	    };
	    Object fila2[] = {
	       "1", "Reforma por Partida"
	    };
	   
	    lista.add(fila1);
	    lista.add(fila2);
	    rad_imprimir.setId("rad_imprimir");
	    rad_imprimir.setRadio(lista);
	    rad_imprimir.setValue(fila2);
	    rad_imprimir.setVertical();

		Boton bot_buscar_resol = new Boton();
		bot_buscar_resol.setIcon("ui-icon-person");
		bot_buscar_resol.setValue("Buscar Reformas");
		bot_buscar_resol.setMetodo("mostrarDialogoBusca");
		bar_botones.agregarBoton(bot_buscar_resol); 
		
		Boton bot_sincronizar = new Boton();
		bot_sincronizar.setIcon("ui-icon-unlocked");
		bot_sincronizar.setValue("Desbloquear POA");
		bot_sincronizar.setMetodo("sincronizarSaldos");
		bar_botones.agregarBoton(bot_sincronizar); 

		Grid gri_formulario = new Grid();
    	gri_formulario.setColumns(4);
    	
    	gri_formulario.getChildren().add(rad_imprimir);

    	Etiqueta eti_total = new Etiqueta("TOTAL REFORMADO:");
    	eti_total.setStyle("font-size: 15px;font-weight: bold;text-aling:left");
       	gri_formulario.getChildren().add(eti_total);
       	
       	eti_valor_total.setId("eti_valor_total");
       	eti_valor_total.setValue("0.00");
    	eti_valor_total.setStyle("font-size: 17px;color: green;font-weight: bold;text-aling:left");
       	gri_formulario.getChildren().add(eti_valor_total);
       	

       	eti_mensajesaldos.setId("eti_mensajesaldos");
       	eti_mensajesaldos.setStyle("font-size: 18px;color: red;font-weight: bold");

       	gri_formulario.getChildren().add(eti_mensajesaldos);
       	
       	//Grid para el dialogo
       	Grid gri_guarda_reforma = new Grid();
       	gri_guarda_reforma.setColumns(2);
       	
       	Etiqueta eti_resolcion_guarda = new Etiqueta("Nro. Resolución: ");
       	gri_guarda_reforma.getChildren().add(eti_resolcion_guarda);
       	txt_num_resolucion_guarda.setId("txt_num_resolucion_guarda");
       	txt_num_resolucion_guarda.setSize(30);
       	gri_guarda_reforma.getChildren().add(txt_num_resolucion_guarda);
       	
       	Etiqueta eti_fecha_guarda = new Etiqueta("Fecha Reforma");
       	gri_guarda_reforma.getChildren().add(eti_fecha_guarda);
       	cal_fecha_reforma.setId("cal_fecha_reforma");
       	cal_fecha_reforma.setFechaActual();
       	gri_guarda_reforma.getChildren().add(cal_fecha_reforma);       	  	
       	
       	dia_datos_reforma.setId("dia_datos_reforma");
       	dia_datos_reforma.setTitle("Ingrese los Siguientes Datos para Realizar la Reforma");
       	dia_datos_reforma.setWidth("25%");
       	dia_datos_reforma.setHeight("20%");
       	dia_datos_reforma.setDialogo(gri_guarda_reforma);
       	dia_datos_reforma.setFooter("Estos Datos no podrán ser modificados posteriormente..");
       	dia_datos_reforma.getBot_aceptar().setMetodo("guardar");

		tab_poa_reforma_fuenta.setId("tab_poa_reforma_fuenta");
		tab_poa_reforma_fuenta.setTabla("pre_poa_reforma_fuente", "ide_prprf", 1);
		tab_poa_reforma_fuenta.getColumna("ide_prpoa").setCombo(ser_presupuesto.getPoaNombre("select ide_geani from gen_anio"));
		tab_poa_reforma_fuenta.setCondicion("ide_prprf=-1 and ide_geani=-1");
		tab_poa_reforma_fuenta.getColumna("ide_prpoa").setAutoCompletar();
		tab_poa_reforma_fuenta.getColumna("ide_prpoa").setLectura(true);
		//tab_poa_reforma_fuenta.getColumna("ide_prpoa").setFiltroContenido();
		tab_poa_reforma_fuenta.getColumna("ide_prfuf").setCombo("pre_fuente_financiamiento","ide_prfuf","detalle_prfuf","");
		tab_poa_reforma_fuenta.getColumna("ide_prfuf").setAutoCompletar();
		tab_poa_reforma_fuenta.getColumna("ide_prfuf").setLectura(true);
		tab_poa_reforma_fuenta.getColumna("saldo_actual_prprf").setLectura(true);
		tab_poa_reforma_fuenta.getColumna("activo_prprf").setLectura(true);
		tab_poa_reforma_fuenta.getColumna("resolucion_prprf").setLectura(true);
		tab_poa_reforma_fuenta.getColumna("resolucion_prprf").setValorDefecto("TEMPORAL");
		tab_poa_reforma_fuenta.getColumna("fecha_prprf").setLectura(true);
		tab_poa_reforma_fuenta.getColumna("aprobado_prprf").setLectura(true);
		tab_poa_reforma_fuenta.getColumna("ide_geani").setVisible(false);
		tab_poa_reforma_fuenta.getColumna("valor_reformado_prprf").setMetodoChange("calcularReforma");
		tab_poa_reforma_fuenta.getColumna("saldo_actual_prprf").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_poa_reforma_fuenta.getColumna("ide_prprf").setOrden(1);
		tab_poa_reforma_fuenta.getColumna("ide_prfuf").setOrden(2);
		tab_poa_reforma_fuenta.getColumna("saldo_actual_prprf").setOrden(3);
		tab_poa_reforma_fuenta.getColumna("ide_prfuf").setLongitud_control(30);
		tab_poa_reforma_fuenta.getColumna("valor_reformado_prprf").setLongitud_control(30);
		tab_poa_reforma_fuenta.getColumna("valor_reformado_prprf").setLongitud(30);
		
		//Auxiliares
		tab_poa_reforma_fuenta.getColumna("codigo_poa").setLectura(true);
		
		tab_poa_reforma_fuenta.getColumna("partida_poa").setCombo(ser_presupuesto.getPartidaNombre("select ide_geani from gen_anio"));
		tab_poa_reforma_fuenta.getColumna("partida_poa").setAutoCompletar();
		tab_poa_reforma_fuenta.getColumna("partida_poa").setLectura(true);
		
		tab_poa_reforma_fuenta.getColumna("area_poa").setCombo(ser_presupuesto.getAreaNombre("select ide_geani from gen_anio"));
		tab_poa_reforma_fuenta.getColumna("area_poa").setAutoCompletar();
		tab_poa_reforma_fuenta.getColumna("area_poa").setLectura(true);
		
		tab_poa_reforma_fuenta.setSortBy("ide_prpoa");


		tab_poa_reforma_fuenta.dibujar();
		PanelTabla pat_reforma = new PanelTabla();
		pat_reforma.setHeader(gri_formulario);
		pat_reforma.setPanelTabla(tab_poa_reforma_fuenta);
		
		Division div_poa_reforma = new Division();
		div_poa_reforma.setId("div_poa_reforma");
		div_poa_reforma.dividir1(pat_reforma);
		
		agregarComponente(div_poa_reforma);
		
		agregarComponente(dia_datos_reforma);

		
		inicializaSetPoaReformar();
		
		utilitario.getConexion().ejecutarSql("delete from pre_poa_reforma_fuente where aprobado_prprf = false and resolucion_prprf is null and valor_reformado_prprf=0;");
		inicializarSelResolucion();
		
	}
	
	public void sincronizarSaldos()
	{

		TablaGenerica tab_poa = ser_presupuesto.getTablaGenericaPoaXAnio(com_anio.getValue()+"");	
		//tab_poa.imprimirSql();
		for(int i=0;i<tab_poa.getTotalFilas();i++){
			TablaGenerica saldo_poa=utilitario.consultar(ser_presupuesto.getSaldoPoa(tab_poa.getValor( i,"ide_prpoa")));
			//saldo_poa.imprimirSql();
			for(int j=0;j<saldo_poa.getTotalFilas();j++){
				utilitario.getConexion().ejecutarSql("update pre_poa_fuente_ejecucion set valor_certificado_prpfe="+saldo_poa.getValor(j,"valor_certificado_prpoc")
		                       +" where ide_prfuf = "+saldo_poa.getValor(j,"ide_prfuf")+" and ide_prpoa="+saldo_poa.getValor(j,"ide_prpoa"));
				utilitario.getConexion().ejecutarSql("update pre_poa set valor_certificado_prpoa=(select sum(coalesce(valor_certificado_prpfe,0)) from pre_poa_fuente_ejecucion where ide_prpoa="+saldo_poa.getValor(j,"ide_prpoa")+") where ide_prpoa="+saldo_poa.getValor(j,"ide_prpoa"));
			}
			
		}
		System.out.println("sincronizarSaldos ok.");
		utilitario.agregarMensaje("Mensaje", "POA Desbloqueado...");
	}
	
	public void inicializaSetPoaReformar(){
		set_reforma_fuente.setId("set_reforma_fuente");
		set_reforma_fuente.setTitle("Seleccione una Linea del POA para la Reforma");
		set_reforma_fuente.setSeleccionTabla(ser_presupuesto.getPoaSaldosFuenteFinanciamiento("-1","-1","0","1","-1"),"codigo");
		set_reforma_fuente.getTab_seleccion().getColumna("detalle_prfuf").setNombreVisual("Fuente Financiamiento");
		set_reforma_fuente.getTab_seleccion().getColumna("valor_asignado").setNombreVisual("Asignación Inicial F.F.");
		set_reforma_fuente.getTab_seleccion().getColumna("codigo_clasificador_prcla").setNombreVisual("Partida Presupuestaria");
		set_reforma_fuente.getTab_seleccion().getColumna("codigo_subactividad").setNombreVisual("Codigo Sub-Actividad");
		set_reforma_fuente.getTab_seleccion().getColumna("detalle_subactividad").setNombreVisual("Sub-Actividad");
		set_reforma_fuente.getTab_seleccion().getColumna("num_resolucion_prpoa").setNombreVisual("Nro. Resolución");
		set_reforma_fuente.getTab_seleccion().getColumna("detalle_proyecto").setNombreVisual("PROYECTO");
		set_reforma_fuente.getTab_seleccion().getColumna("detalle_geare").setNombreVisual("AREA/COORDINACION");
		set_reforma_fuente.getTab_seleccion().getColumna("presupuesto_inicial_prpoa").setNombreVisual("INICIAL");
		set_reforma_fuente.getTab_seleccion().getColumna("presupuesto_codificado_prpoa").setNombreVisual("CODIFICADO");
		set_reforma_fuente.getTab_seleccion().getColumna("valor_saldo_fuente").setNombreVisual("SALDO FUENTE");
		
		set_reforma_fuente.getTab_seleccion().getColumna("ide_prpoa").setFiltro(true);
		set_reforma_fuente.getTab_seleccion().getColumna("detalle_prfuf").setFiltroContenido();
		set_reforma_fuente.getTab_seleccion().getColumna("codigo_clasificador_prcla").setFiltroContenido();
		//set_reforma_fuente.getTab_seleccion().getColumna("codigo_subactividad").setFiltroContenido();
		set_reforma_fuente.getTab_seleccion().getColumna("detalle_subactividad").setFiltroContenido();
		set_reforma_fuente.getTab_seleccion().getColumna("num_resolucion_prpoa").setFiltroContenido();
		set_reforma_fuente.getTab_seleccion().getColumna("detalle_proyecto").setFiltroContenido();
		set_reforma_fuente.getTab_seleccion().getColumna("detalle_geare").setFiltroContenido();
		set_reforma_fuente.getTab_seleccion().getColumna("descripcion_clasificador_prcla").setFiltroContenido();
		set_reforma_fuente.getTab_seleccion().getColumna("detalle_programa").setFiltroContenido();
		set_reforma_fuente.getTab_seleccion().getColumna("detalle_producto").setFiltroContenido();
		set_reforma_fuente.getTab_seleccion().getColumna("detalle_actividad").setFiltroContenido();
		
		set_reforma_fuente.getTab_seleccion().getColumna("ide_prpoa").setNombreVisual("Codigo POA");
		//set_reforma_fuente.getTab_seleccion().getColumna("ide_prpoa").setVisible(false);
		set_reforma_fuente.getTab_seleccion().getColumna("codigo_subactividad").setVisible(false);
		set_reforma_fuente.getTab_seleccion().getColumna("num_resolucion_prpoa").setVisible(false);
		set_reforma_fuente.getTab_seleccion().getColumna("ide_prfuf").setVisible(false);
		set_reforma_fuente.getTab_seleccion().getColumna("valor_asignado").setVisible(false);
		set_reforma_fuente.getTab_seleccion().getColumna("valor_reformado").setVisible(false);
		set_reforma_fuente.getTab_seleccion().getColumna("valor_saldo_fuente").setVisible(false);
		set_reforma_fuente.getTab_seleccion().getColumna("fecha_inicio_prpoa").setVisible(false);
		set_reforma_fuente.getTab_seleccion().getColumna("fecha_fin_prpoa").setVisible(false);
		//set_reforma_fuente.getTab_seleccion().getColumna("presupuesto_inicial_prpoa").setVisible(false);
		//set_reforma_fuente.getTab_seleccion().getColumna("presupuesto_codificado_prpoa").setVisible(false);
		set_reforma_fuente.getTab_seleccion().getColumna("reforma_prpoa").setVisible(false);
		set_reforma_fuente.getTab_seleccion().getColumna("detalle_geani").setVisible(false);
		set_reforma_fuente.getTab_seleccion().getColumna("detalle_prfuf").setVisible(false);
		set_reforma_fuente.getTab_seleccion().setSortBy("ide_prpoa");
		
		//set_reforma_fuente.setRadio();
		set_reforma_fuente.getTab_seleccion().ejecutarSql();
		set_reforma_fuente.getBot_aceptar().setMetodo("aceptarPoa");

		agregarComponente(set_reforma_fuente);

	}
	
	public void cargaFuente()
	{
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un año", com_anio.getValue()+"");
			return;
		}
		
		tab_poa_reforma_fuenta.setCondicion("aprobado_prprf = false and resolucion_prprf is null and ide_geani=" +com_anio.getValue() +" and ide_prfuf="+com_fuente_financiamiento.getValue());
		tab_poa_reforma_fuenta.ejecutarSql();
	}
	
	public void cambiaArea()
	{
		ide_geare="-1";
		try
		{
			ide_geare=pckUtilidades.CConversion.CInt(com_area.getValue())+"";
		}
		catch(Exception e)
		{}
	}

	public void inicializarSelResolucion(){
		//dialogo para reporte
		sel_resolucion.setId("sel_resolucion");
		sel_resolucion.setSeleccionTabla("select distinct resolucion_prprf as codigo, detalle_geani,resolucion_prprf,aprobado_prprf "+
				"  from pre_poa_reforma_fuente rf "+
				"  join gen_anio a on a.ide_geani=rf.ide_geani "+
				"  where aprobado_prprf=false "+
				" order by 2 desc, 3 desc","codigo");
		sel_resolucion.setRadio();
		sel_resolucion.getTab_seleccion().ejecutarSql();
		sel_resolucion.getTab_seleccion().getColumna("resolucion_prprf").setFiltro(true);
		sel_resolucion.getBot_aceptar().setMetodo("buscaResolucion");
		agregarComponente(sel_resolucion);
	
	}
	
	public void calcularReforma(AjaxBehaviorEvent evt) {
		//System.out.println(" entre actualizar ");
		
		tab_poa_reforma_fuenta.modificar(evt); //Siempre es la primera linea
		eti_mensajesaldos.setValue("");
		utilitario.addUpdate("eti_mensajesaldos");
		double dou_valor_reforma=0;
		double dou_valor_saldo=0;
		double dou_valor_reforma_individual=0;
		double dou_valor_certificado=0;
		double total_ejecutado=0;
		double dou_valor_total_reformado=0;
		String mensaje="0";
		
		
		int i = tab_poa_reforma_fuenta.getFilaActual();
		
		TablaGenerica tab_poa_certificado= utilitario.consultar("select ide_prpfe,valor_certificado_prpfe from pre_poa_fuente_ejecucion where ide_prfuf = "+tab_poa_reforma_fuenta.getValor(i,"ide_prfuf")+" and ide_prpoa="+tab_poa_reforma_fuenta.getValor(i,"ide_prpoa"));
			if(tab_poa_certificado.getTotalFilas()>0){
				dou_valor_certificado=pckUtilidades.CConversion.CDbl_2(tab_poa_certificado.getValor("valor_certificado_prpfe"));
				
				BigDecimal big_valor_certificado=new BigDecimal(dou_valor_certificado);
				big_valor_certificado=big_valor_certificado.setScale(2, RoundingMode.HALF_UP);
				
				dou_valor_certificado=pckUtilidades.CConversion.CDbl_2(big_valor_certificado);
				
			}
			//Obtenemos el valor del saldo
			try {
				dou_valor_saldo =pckUtilidades.CConversion.CDbl_2(tab_poa_reforma_fuenta.getValor(i,"saldo_actual_prprf"));
				
				BigDecimal big_valor_saldo=new BigDecimal(dou_valor_saldo);
				big_valor_saldo=big_valor_saldo.setScale(2, RoundingMode.HALF_UP);
				
				dou_valor_saldo=pckUtilidades.CConversion.CDbl_2(big_valor_saldo);
				
			} catch (Exception e) {
				System.out.println("Error ingreso valor saldo_actual_prprf "+e);
			}
			
			try {
				//Obtenemos el valor de la cantidad
				dou_valor_reforma +=pckUtilidades.CConversion.CDbl_2(tab_poa_reforma_fuenta.getValor(i,"valor_reformado_prprf"));
				
				
				BigDecimal big_valor_reforma=new BigDecimal(dou_valor_reforma);
				big_valor_reforma=big_valor_reforma.setScale(2, RoundingMode.HALF_UP);
				
				dou_valor_reforma=pckUtilidades.CConversion.CDbl_2(big_valor_reforma);
				
			} catch (Exception e) {
				System.out.println("Error ingreso valor reformado "+e);
			}
		
			total_ejecutado =dou_valor_saldo-dou_valor_certificado;
			BigDecimal big_total_ejecutado=new BigDecimal(total_ejecutado);
			big_total_ejecutado=big_total_ejecutado.setScale(2, RoundingMode.HALF_UP);
			
			total_ejecutado=pckUtilidades.CConversion.CDbl_2(big_total_ejecutado);
			//System.out.println("valores ejcutados "+total_ejecutado+" dou_valor_saldo "+dou_valor_saldo+" dou_valor_certificado "+dou_valor_certificado);
			double valor_ingresado =pckUtilidades.CConversion.CDbl_2(tab_poa_reforma_fuenta.getValor(i,"valor_reformado_prprf")); //*(-1);
			
			BigDecimal big_valor_ingresado=new BigDecimal(valor_ingresado);
			big_valor_ingresado=big_valor_ingresado.setScale(2, RoundingMode.HALF_UP);
			
			valor_ingresado=pckUtilidades.CConversion.CDbl_2(big_valor_ingresado);
			
			double valor_ingreso_positivo=pckUtilidades.CConversion.CDbl_2(tab_poa_reforma_fuenta.getValor(i,"valor_reformado_prprf"));
			//System.out.println("valor_ingreso_positivo "+valor_ingreso_positivo+" valor_ingresado " +valor_ingresado);

			double ejcutado_inicial=0;
			double ejecucion=0;
			double asi_inicial=0;
						
			//System.out.println("total_ejecutado "+total_ejecutado+" valor_ingresado " +valor_ingresado);
			
			if(valor_ingresado<0){
				valor_ingresado=valor_ingresado*(-1);
				
					if(valor_ingresado>total_ejecutado){
						//System.out.println("intrego ver saldo insuficiente "+total_ejecutado+"   xxx  "+valor_ingresado);
						//utilitario.agregarMensajeError("Saldo Insuficiente", "No se puede ejecutar la reforma solo dispone de un saldo incluido el valor certificado de: "+total_ejecutado);
						eti_mensajesaldos.setValue("<img src='imagenes/im_eliminar.png' /> No se puede ejecutar la reforma solo dispone un saldo de: "+total_ejecutado);

						tab_poa_reforma_fuenta.setValor(i, "valor_reformado_prprf", "0");
						
						utilitario.agregarNotificacionInfo("Bloqueo de POA", "Favor intente desbloquear los saldos del POA para continuar... Si el problema persiste comuniquese con el Administrador.");
						//eti_valor_total.setValue(utilitario.getFormatoNumero("0", 3));
						utilitario.addUpdateTabla(tab_poa_reforma_fuenta, "valor_reformado_prprf", "eti_mensajesaldos");
						//return;
				
					}
			
			}
       		TablaGenerica tabla1= utilitario.consultar(ser_presupuesto.getInicialFuenteFinanciamiento(tab_poa_reforma_fuenta.getValor(i,"ide_prfuf"), com_anio.getValue().toString()));
       		if(tabla1.getTotalFilas()>0){
       			asi_inicial=pckUtilidades.CConversion.CDbl_2(tabla1.getValor("valor"));
       			
       			BigDecimal big_asi_inicial=new BigDecimal(asi_inicial);
    			big_asi_inicial=big_asi_inicial.setScale(2, RoundingMode.HALF_UP);
    			
    			asi_inicial=pckUtilidades.CConversion.CDbl_2(big_asi_inicial);
    			
    			TablaGenerica tabla2=utilitario.consultar(ser_presupuesto.getEjecutaFuenteFinanciamiento(tab_poa_reforma_fuenta.getValor(i, "ide_prfuf"), com_anio.getValue().toString(),null));
    			if(tabla2.getTotalFilas()>0){
    				ejcutado_inicial=pckUtilidades.CConversion.CDbl_2(tabla2.getValor("valor"));
    				BigDecimal big_ejcutado_inicial=new BigDecimal(ejcutado_inicial);
        			big_ejcutado_inicial=big_ejcutado_inicial.setScale(2, RoundingMode.HALF_UP);
    				
        			ejcutado_inicial=pckUtilidades.CConversion.CDbl_2(big_ejcutado_inicial);
        			
    				ejecucion=dou_valor_reforma+ejcutado_inicial;
    				BigDecimal big_ejecucion=new BigDecimal(ejecucion);
        			big_ejecucion=big_ejecucion.setScale(2, RoundingMode.HALF_UP);

    				ejecucion=pckUtilidades.CConversion.CDbl_2(big_ejecucion);
    			//	System.out.println("ejcutado_inicial x "+ejcutado_inicial+" ejecucion x "+ejecucion);

    			}
       		}
			//System.out.println("asi_inicial "+asi_inicial+" ejecucion x "+ejecucion);
			
       		//if(asi_inicial<ejecucion){
				//System.out.println("intrego ver saldo insuficiente "+asi_inicial+"   xxx  "+ejecucion);

       			//utilitario.agregarMensajeError("Supera Techo Inicial", "No se puede ejecutar la reforma la fuente de financiamiento posee un Techo Máximo de: "+asi_inicial);
				//tab_poa_reforma_fuenta.setValor(i, "valor_reformado_prprf", "0");
				//utilitario.addUpdate("tab_poa_reforma_fuenta");
				//break;
       		//}
		
       		calcular_total();
	}
	
	private void calcular_total()
	{
		double dou_valor_total_reformado=0;
       	// El siguiente for me permite sumar toda la columna de las reformas	
		 for(int j=0;j<tab_poa_reforma_fuenta.getTotalFilas();j++){
       		try {
				//Obtenemos el valor de la cantidad
				dou_valor_total_reformado +=pckUtilidades.CConversion.CDbl_2(tab_poa_reforma_fuenta.getValor(j,"valor_reformado_prprf"));
				
				
				BigDecimal big_valor_reforma=new BigDecimal(dou_valor_total_reformado);
				big_valor_reforma=big_valor_reforma.setScale(2, RoundingMode.HALF_UP);
				
				dou_valor_total_reformado=pckUtilidades.CConversion.CDbl_2(big_valor_reforma);
				
			} catch (Exception e) {
				System.out.println("Error ingreso valor reformado "+e);
			}
		 }
		//System.out.println(" actaulizco el valor "+dou_valor_reforma);
		eti_valor_total.setValue(utilitario.getFormatoNumero(dou_valor_total_reformado, 3));
		utilitario.addUpdate("eti_valor_total");
	}
	
	public void mostrarDialogoBusca(){
		
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un año", "");
			return;
		}
		if(com_fuente_financiamiento.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar una fuente de financiamiento", "");
			return;
		}
		
		sel_resolucion.dibujar();
	}
	public void buscaResolucion()
	{
		txt_num_resolucion_guarda.setValue("");
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un año", "");
			return;
		}
		if(sel_resolucion.getValorSeleccionado()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar una reforma no aprobada para continuar...", "");
			return;
		}
		//tab_poa_reforma_fuenta.setCondicion("resolucion_prprf='"+txt_num_oficio.getValue()+"' and ide_geani=" +com_anio.getValue() );
		tab_poa_reforma_fuenta.setCondicion("resolucion_prprf='"+pckUtilidades.CConversion.CStr(sel_resolucion.getValorSeleccionado())+"' and ide_geani=" +com_anio.getValue()+" and ide_prfuf="+com_fuente_financiamiento.getValue() );
		tab_poa_reforma_fuenta.ejecutarSql();
		
		com_fuente_financiamiento.setValue(tab_poa_reforma_fuenta.getValor("ide_prfuf"));
		ide_prfuf=pckUtilidades.CConversion.CInt(com_fuente_financiamiento.getValue());
		
		txt_num_resolucion_guarda.setValue(pckUtilidades.CConversion.CStr(sel_resolucion.getValorSeleccionado()));
		utilitario.addUpdate("com_fuente_financiamiento,txt_num_resolucion_guarda");
		calcular_total();
		sel_resolucion.cerrar();
	}
	
	public void aceptarPoa(){
		String str_seleccionado=set_reforma_fuente.getSeleccionados();
		//System.out.println("seleccionados "+str_seleccionado);
		if(pckUtilidades.CConversion.CStr(str_seleccionado).length()>0)
		{
			TablaGenerica tab_busca_poa=utilitario.consultar(ser_presupuesto.getPoaSaldosFuenteFinanciamiento(com_anio.getValue().toString(),com_fuente_financiamiento.getValue().toString(),"1",str_seleccionado,ide_geare));
			for(int i=0;i<tab_busca_poa.getTotalFilas();i++){
				//if(tab_busca_poa.getValor(i,"codigo").equals(str_seleccionado)){
				if(!existePoaReforma(tab_busca_poa.getValor(i, "ide_prpoa"),tab_busca_poa.getValor(i, "ide_prfuf")))
				{
					tab_poa_reforma_fuenta.insertar();
					tab_poa_reforma_fuenta.setValor("ide_prpoa", tab_busca_poa.getValor(i, "ide_prpoa"));
					tab_poa_reforma_fuenta.setValor("ide_prfuf", tab_busca_poa.getValor(i, "ide_prfuf"));
					tab_poa_reforma_fuenta.setValor("ide_geani",com_anio.getValue()+"");
					tab_poa_reforma_fuenta.setValor("valor_reformado_prprf", "0");
					tab_poa_reforma_fuenta.setValor("activo_prprf", "true");
					tab_poa_reforma_fuenta.setValor("aprobado_prprf", "false");
					tab_poa_reforma_fuenta.setValor("saldo_actual_prprf", tab_busca_poa.getValor(i, "valor_saldo_fuente"));
					////auxiliares
					tab_poa_reforma_fuenta.setValor("codigo_poa", tab_busca_poa.getValor(i, "ide_prpoa"));
					tab_poa_reforma_fuenta.setValor("partida_poa", tab_busca_poa.getValor(i, "ide_prpoa"));
					tab_poa_reforma_fuenta.setValor("area_poa", tab_busca_poa.getValor(i, "ide_prpoa"));
				}
				else
					utilitario.agregarMensajeInfo("Sub-Actividad POA", "La Sub-Actividad del POA con codigo: "+tab_busca_poa.getValor(i, "ide_prpoa")+" Ya fue agregado anteriormente a esta reforma...");
				//}
			}
			tab_poa_reforma_fuenta.guardar();
			guardarPantalla();
			set_reforma_fuente.cerrar();
			utilitario.addUpdate("tab_poa_reforma_fuenta");	
		}
		else
			utilitario.agregarMensajeInfo("Buscar Sub-Actividad POA", "Seleccione una linea del POA para realizar la reforma...");

	}
	
	private boolean existePoaReforma(String ide_prpoa, String ide_prfuf)
	{
		boolean respuesta=false;
		for(int i=0;i<tab_poa_reforma_fuenta.getTotalFilas();i++)
		{
			if(pckUtilidades.CConversion.CStr(tab_poa_reforma_fuenta.getValor(i,"ide_prpoa")).equals(ide_prpoa) 
					&& pckUtilidades.CConversion.CStr(tab_poa_reforma_fuenta.getValor(i,"ide_prfuf")).equals(ide_prfuf))
			//pckUtilidades.CConversion.CDbl_2(tab_recaudacion.getValor(j,"valor"))
			{
				respuesta=true;
				break;
			}	

		}
		return respuesta;
	}
	
	public void mostrarSetReformaFuente(){
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un año", "");
			return;
		}
		else if(com_fuente_financiamiento.getValue()==null){
			utilitario.agregarMensajeInfo("Fuente de Financiamiento", "Seleccione una fuente de Financiamiento para realizar la reforma");
			return;
		}
		else 
		{
			//if(tab_poa_reforma_fuenta.getFilas().size()<1)
			if(ide_prfuf != pckUtilidades.CConversion.CInt(com_fuente_financiamiento.getValue()))
			{
				tab_poa_reforma_fuenta.setCondicion("ide_prprf=-1 and ide_geani=-1");	
				tab_poa_reforma_fuenta.ejecutarSql();
				eti_valor_total.setValue("0.00");
				utilitario.addUpdate("tab_poa_reforma_fuenta,eti_valor_total");
				utilitario.getConexion().ejecutarSql("delete from pre_poa_reforma_fuente where aprobado_prprf = false and resolucion_prprf is null;");
			}
			
			ide_prfuf=pckUtilidades.CConversion.CInt(com_fuente_financiamiento.getValue());
			
			set_reforma_fuente.getTab_seleccion().setSql(ser_presupuesto.getPoaSaldosFuenteFinanciamiento(com_anio.getValue().toString(),com_fuente_financiamiento.getValue().toString(),"0","-1",ide_geare));
			set_reforma_fuente.getTab_seleccion().ejecutarSql();
			set_reforma_fuente.dibujar();
		}
	}
	public void borrarContadores(String resolucion){
		String sql_emilinar_contadores="delete from sis_bloqueo where tabla_bloq ilike 'pre_poa_reforma';"
				+" delete from pre_poa_reforma where resolucion_prpor='"+resolucion+"';";
		utilitario.getConexion().ejecutarSql(sql_emilinar_contadores);
		
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_poa_reforma_fuenta.insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		eti_mensajesaldos.setValue("");
		utilitario.addUpdate("eti_mensajesaldos");
		
		if(dia_datos_reforma.isVisible()){
		   // Inserto validando reforma por partida
			if(rad_imprimir.getValue().equals("1") && eti_valor_total.getValue().equals("0")) {
				tab_poa_reforma_fuenta.guardar();
				guardarPantalla();
				borrarContadores(txt_num_resolucion_guarda.getValue().toString());
				
				for(int i=0;i<tab_poa_reforma_fuenta.getTotalFilas();i++){
					
					String sql_actualiza="update pre_poa_reforma_fuente set fecha_prprf='"+cal_fecha_reforma.getFecha()+"',resolucion_prprf='"+txt_num_resolucion_guarda.getValue().toString()+"',aprobado_prprf='false' where ide_prprf="+tab_poa_reforma_fuenta.getValor(i, "ide_prprf");
					utilitario.getConexion().ejecutarSql(sql_actualiza);
					//actualizarSaldosReforma(tab_poa_reforma_fuenta.getValor(i, "ide_prpoa"), tab_poa_reforma_fuenta.getValor(i, "valor_reformado_prprf"), txt_num_resolucion_guarda.getValue().toString(), cal_fecha_reforma.getFecha(), tab_poa_reforma_fuenta.getValor(i, "saldo_actual_prprf"),che_aprobado.getValue().toString());
					/*Esto comento de lo ultimo que funcionaba porque la aprobacion se la realiza desde una interfaz aprobacion de resoluciones POA
					ser_presupuesto.trigActualizaReformaFuente(tab_poa_reforma_fuenta.getValor(i, "ide_prpoa"));
					ser_presupuesto.trigActualizaReforma(tab_poa_reforma_fuenta.getValor(i, "ide_prpoa"));
					*/
				}
				utilitario.getConexion().ejecutarSql("delete from pre_poa_reforma_fuente where fecha_ingre='"+utilitario.getFechaActual()+"' and aprobado_prprf = false and valor_reformado_prprf=0 and ide_geani=" +com_anio.getValue());
				tab_poa_reforma_fuenta.setCondicion("resolucion_prprf='"+txt_num_resolucion_guarda.getValue().toString()+"' and ide_geani=" +com_anio.getValue());
				tab_poa_reforma_fuenta.ejecutarSql();
			}
		   // Inserto validando reforma por techos
			else if(rad_imprimir.getValue().equals("0")){
				
				tab_poa_reforma_fuenta.guardar();
				guardarPantalla();
				borrarContadores(txt_num_resolucion_guarda.getValue().toString());
				
				for(int i=0;i<tab_poa_reforma_fuenta.getTotalFilas();i++){
					String sql_actualiza="update pre_poa_reforma_fuente set fecha_prprf='"+cal_fecha_reforma.getFecha()+"',resolucion_prprf='"+txt_num_resolucion_guarda.getValue().toString()+"',aprobado_prprf='false' where ide_prprf="+tab_poa_reforma_fuenta.getValor(i, "ide_prprf");
					utilitario.getConexion().ejecutarSql(sql_actualiza);
					//actualizarSaldosReforma(tab_poa_reforma_fuenta.getValor(i, "ide_prpoa"), tab_poa_reforma_fuenta.getValor(i, "valor_reformado_prprf"), txt_num_resolucion_guarda.getValue().toString(), cal_fecha_reforma.getFecha(), tab_poa_reforma_fuenta.getValor(i, "saldo_actual_prprf"),che_aprobado.getValue().toString());

					/*Esto comento de lo ultimo que funcionaba porque la aprobacion se la realiza desde una interfaz aprobacion de resoluciones POA
					//triguers actualiza reformas.
					ser_presupuesto.trigActualizaReformaFuente(tab_poa_reforma_fuenta.getValor(i, "ide_prpoa"));
					ser_presupuesto.trigActualizaReforma(tab_poa_reforma_fuenta.getValor(i, "ide_prpoa"));
					*/
				}
				tab_poa_reforma_fuenta.setCondicion("resolucion_prprf='"+txt_num_resolucion_guarda.getValue().toString()+"' and ide_geani=" +com_anio.getValue());
				tab_poa_reforma_fuenta.setCampoOrden("ide_prprf desc");
				tab_poa_reforma_fuenta.ejecutarSql();
			}
			else {
				utilitario.agregarMensajeError("No se puede ejecutar Reforma", "La Reforma por Partida debe Generar un Valor 0.00");
				return;
				
			}
			dia_datos_reforma.cerrar();
		}
		else{
			dia_datos_reforma.dibujar();
		}
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		
	}

	public Tabla getTab_poa_reforma_fuenta() {
		return tab_poa_reforma_fuenta;
	}

	public void setTab_poa_reforma_fuenta(Tabla tab_poa_reforma_fuenta) {
		this.tab_poa_reforma_fuenta = tab_poa_reforma_fuenta;
	}

	public SeleccionTabla getSet_reforma_fuente() {
		return set_reforma_fuente;
	}

	public void setSet_reforma_fuente(SeleccionTabla set_reforma_fuente) {
		this.set_reforma_fuente = set_reforma_fuente;
	}
	public Dialogo getDia_datos_reforma() {
		return dia_datos_reforma;
	}
	public void setDia_datos_reforma(Dialogo dia_datos_reforma) {
		this.dia_datos_reforma = dia_datos_reforma;
	}

	public SeleccionTabla getSel_resolucion() {
		return sel_resolucion;
	}
	public void setSel_resolucion(SeleccionTabla sel_resolucion) {
		this.sel_resolucion = sel_resolucion;
	}



}
