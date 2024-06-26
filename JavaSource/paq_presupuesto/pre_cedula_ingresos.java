package paq_presupuesto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;



import framework.aplicacion.TablaGenerica;
import framework.componentes.ArbolTable;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.ItemMenu;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_nomina.ejb.ServicioNomina;
import paq_presupuesto.ejb.ServicioPresupuesto;
import paq_sistema.aplicacion.Pantalla;

public class pre_cedula_ingresos extends Pantalla{

	private ArbolTable arb_tabla_g=new ArbolTable();
	private Combo com_anio=new Combo();

	private Confirmar con_guardar=new Confirmar();
	private Combo com_nivel_cuenta_inicial = new Combo();
	private Combo com_nivel_cuenta_final = new Combo();
	private SeleccionTabla sel_devengados= new SeleccionTabla();
	private Radio rad_imprimir= new Radio();
	private Dialogo dia_recalcular = new Dialogo();
	private Dialogo dia_busca_nivel = new Dialogo();
	
	public static String par_tipo_asiento_inicial;
	private SeleccionCalendario sel_calendario=new SeleccionCalendario();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();
	private Map map_parametros = new HashMap();
	private int tipoRecalculo=1;

	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioPresupuesto ser_presupuesto = (ServicioPresupuesto ) utilitario.instanciarEJB(ServicioPresupuesto.class);

	public pre_cedula_ingresos() {
		par_tipo_asiento_inicial =utilitario.getVariable("p_tipo_asiento_inicial");

		bar_botones.limpiar();
		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);		
		bar_botones.agregarReporte();		
		sel_rep.setId("sel_rep");
		agregarComponente(sel_rep);
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));		
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setValue(utilitario.getVariable("p_anio_vigente"));
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		//com_anio.setValue("13");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
		
		com_nivel_cuenta_inicial.setCombo(utilitario.getListaGruposNivelPresupuesto());		
		com_nivel_cuenta_final.setCombo(utilitario.getListaGruposNivelPresupuesto());		

		// Dialogo para ingresar niveles
       	Grid gri_niveles = new Grid();
       	gri_niveles.setColumns(2);
       	gri_niveles.getChildren().add(new Etiqueta("Seleccione el Nivel de Cuenta Inicial"));
       	gri_niveles.getChildren().add(com_nivel_cuenta_inicial);
       	gri_niveles.getChildren().add(new Etiqueta("Seleccione el Nivel de Cuenta Final"));
       	gri_niveles.getChildren().add(com_nivel_cuenta_final);
		
		dia_busca_nivel.setId("dia_busca_nivel");
		dia_busca_nivel.setTitle("Ingrese los niveles para el reporte");
		dia_busca_nivel.setWidth("25%");
		dia_busca_nivel.setHeight("20%");
		dia_busca_nivel.setDialogo(gri_niveles);
		dia_busca_nivel.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(dia_busca_nivel);

		// boton limpiar
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarBoton(bot_limpiar);
				

		List listax = new ArrayList();
	       Object fila1x[] = {
	           "0", "COSOLIDADO"
	       };
	       Object fila2x[] = {
	           "1", "POR FUENTE FINANCIAMIENTO"
	       };
	       
        listax.add(fila1x);
        listax.add(fila2x);
        rad_imprimir.setId("rad_imprimir");
        rad_imprimir.setRadio(listax);
        rad_imprimir.setValue(fila2x);
	    
        dia_recalcular.setId("dia_recalcular");
	    dia_recalcular.setTitle("Ingrese el tipo de recalculo");
       	dia_recalcular.setWidth("25%");
       	dia_recalcular.setHeight("20%");
       	dia_recalcular.setDialogo(rad_imprimir);
       	dia_recalcular.getBot_aceptar().setMetodo("generarCedula");
       	
       	agregarComponente(dia_recalcular);
       	
       	arb_tabla_g.setId("arb_tabla_g");
		arb_tabla_g.setArbol("cedula_ingreso", "ide_prcla","pre_ide_prcla", "partida");
		arb_tabla_g.setCamposAux("inicial_prcing, reformado_prcing, codificado_prcing, devengado_prcing, cobrado_prcing, cobrado_efectivo_prcing, fecha_inicial_prcing, fecha_final_prcing");
		arb_tabla_g.setCampoGroupBy("fuente");
		arb_tabla_g.setColumnaSuma("inicial_prcing, reformado_prcing, codificado_prcing, devengado_prcing, cobrado_prcing, cobrado_efectivo_prcing");
		arb_tabla_g.dibujar();
       	
		
		sel_devengados.setId("sel_devengados");
		sel_devengados.setSeleccionTabla(ser_presupuesto.getRptDevengadosIngreso(com_anio.getValue().toString(),"0"),"ide_prcla");
		sel_devengados.setTitle("Ver Devengados enlazados.");
		sel_devengados.getBoc_seleccion_inversa().setRendered(false);
		sel_devengados.setRadio();
		sel_devengados.getTab_seleccion().setColumnaSuma("devengado_prmen,cobrado_prmen");
		sel_devengados.getTab_seleccion().getColumna("descripcion_movimiento").setFiltroContenido();
		sel_devengados.getTab_seleccion().getColumna("codigo_movimiento").setFiltroContenido();
		sel_devengados.getBot_aceptar().setRendered(false);
		agregarComponente(sel_devengados);
		
		ItemMenu itm_ver_devengados = new ItemMenu();
		itm_ver_devengados.setValue("Ver Devengados");
		itm_ver_devengados.setMetodo("verDevengados");
		itm_ver_devengados.setIcon("ui-icon-search");
		
		PanelTabla pat_balance_inicial=new PanelTabla();
		pat_balance_inicial.setPanelArbolTabla(arb_tabla_g);
		pat_balance_inicial.getMenuTabla().getChildren().add(itm_ver_devengados);
		Division div1 = new Division();
		div1.dividir1(pat_balance_inicial);
		agregarComponente(div1);
		
		Boton bot_actualizar=new Boton();
		bot_actualizar.setIcon("ui-icon-person");
		bot_actualizar.setValue("Recalcular Cedula Presupuestaria");
		bot_actualizar.setMetodo("generarCedula");
		bar_botones.agregarBoton(bot_actualizar);	
		
		Boton bot_excel=new Boton();
	    bot_excel.setValue("Exportar EXCEL");
	    bot_excel.setIcon("ui-icon-calculator");
	    bot_excel.setAjax(false);
	    bot_excel.setMetodo("exportarExcel");
	    bar_botones.agregarBoton(bot_excel);
	    
		inicializaCalendario();
		
		
		
	}
	
	public void inicializaCalendario(){
		sel_calendario.setTitle("SELECCION DE FECHAS");
		sel_calendario.setFooter("Seleccione un Rango de fechas");
		//sel_calendario.setFecha1(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		sel_calendario.setFecha1(utilitario.obtenerFechaInicioAnio());
		sel_calendario.setFecha2(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		sel_calendario.getBot_aceptar().setMetodo("generarCedula");
		agregarComponente(sel_calendario);
	}
	
	public void verDevengados() {
		
		if(pckUtilidades.CConversion.CInt(arb_tabla_g.getValorSeleccionado())<1)
		{
			utilitario.agregarMensajeInfo("Sin devengados", "Favor seleccione un registro correcto...");
			return;
		}
		
		sel_devengados.getTab_seleccion().setSql(ser_presupuesto.getRptDevengadosIngreso(com_anio.getValue().toString(),arb_tabla_g.getValorSeleccionado().toString()));
		sel_devengados.getTab_seleccion().imprimirSql();
		sel_devengados.getTab_seleccion().ejecutarSql();
		sel_devengados.dibujar();
	}
	
	public void generarCedula(){
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Año", "");
			return;			

		}
		if(dia_recalcular.isVisible()){
			if(rad_imprimir.getValue()==null){
				utilitario.agregarMensajeInfo("Selecione un Tipo", "");
				return;			
			}
			tipoRecalculo=pckUtilidades.CConversion.CInt(rad_imprimir.getValue());
			dia_recalcular.cerrar();
			sel_calendario.dibujar();
		}
		else if(sel_calendario.isVisible()){
			sel_calendario.cerrar();
			
			String sql_borra_tabla="delete from pre_cedula_ingreso;";
			utilitario.getConexion().ejecutarSql(sql_borra_tabla);
			
			int int_repetir=1;
			TablaGenerica tab_fuente_finaciamiento = utilitario.consultar("select ide_prfuf,ide_geani from pre_fuente_financiamiento_ini where ide_geani="+com_anio.getValue());
	//		tab_fuente_finaciamiento.imprimirSql();
	
			if(tipoRecalculo==1){
				int_repetir=tab_fuente_finaciamiento.getTotalFilas();
			}
	
			for (int i=0;i<int_repetir;i++){
			
				String sql="insert into pre_cedula_ingreso (ide_prcing,ide_geani,ide_prcla,inicial_prcing,reformado_prcing,codificado_prcing,devengado_prcing,cobrado_prcing,saldo_devengar_prcing,"
						+" saldo_cobrar_prcing,fecha_inicial_prcing,fecha_final_prcing,ide_prfuf,cobrado_efectivo_prcing)"
						+" select row_number() over(order by ide_prcla) + (select (case when max(ide_prcing) is null then 0 else max(ide_prcing) end) as codigo from pre_cedula_ingreso) as codigo,"+com_anio.getValue()+","
						+" ide_prcla,0,0,0,0,0,0,0,'"+sel_calendario.getFecha1String()+"','"+sel_calendario.getFecha2String()+"',"+tab_fuente_finaciamiento.getValor(i,"ide_prfuf")+",0"
						+" from pre_clasificador"
						+" where tipo_prcla =1;";
				//System.out.println("imprimir sql insert "+sql);
				utilitario.getConexion().ejecutarSql(sql);
			
			}
			
			
			if(tipoRecalculo==0){ // conciliado
				ejecutaConsolidado();
			}
			
			if(tipoRecalculo==1){ // fuente financiamiento
				ejecutaFuente();
			}
			
			String sql_borra_ceros="delete from pre_cedula_ingreso where (abs(inicial_prcing)+ abs(reformado_prcing)+abs(codificado_prcing)+ abs(devengado_prcing)+ abs(cobrado_prcing))=0;";
			utilitario.getConexion().ejecutarSql(sql_borra_ceros);
			
			arb_tabla_g.ejecutarSql();
			utilitario.addUpdate("arb_tabla_g");
			
		}
		else {
			rad_imprimir.setValue(tipoRecalculo);
			dia_recalcular.dibujar();
		}
	}

	public void ejecutaConsolidado(){
		String sql_inicial="update pre_cedula_ingreso"  
				+" set inicial_prcing = inicial from ("
				+" 		select ide_prcla,sum(valor_inicial_pranu) as inicial from ("
				+" 				select a.ide_prcla,valor_inicial_pranu from pre_anual a where not a.ide_prcla is null and ide_geani = "+com_anio.getValue()
				+" 				) a group by ide_prcla ) a where a.ide_prcla=pre_cedula_ingreso.ide_prcla ";
		
		String sql_reformado="update pre_cedula_ingreso"
				+" set reformado_prcing = reforma from ("
				+" 	select ide_prcla,sum(reforma) as reforma from  ("
				+" 		select a.ide_prcla,(val_reforma_d_prrem - val_reforma_h_prrem) as reforma from pre_anual a, pre_reforma_mes b"
				+" 		where not a.ide_prcla is null and a.ide_pranu = b.ide_pranu and ide_geani = "+com_anio.getValue()+" and fecha_reforma_prrem between '"+sel_calendario.getFecha1String()+"' and '"+sel_calendario.getFecha2String()+"'"
				+" 		) a group by ide_prcla"
				+" 		) a  where a.ide_prcla=pre_cedula_ingreso.ide_prcla ";
		
		String sql_devengado="update pre_cedula_ingreso"
				+" set devengado_prcing = devengado from ("
				+" 		select ide_prcla,sum(devengado) as devengado from  ("
				+" 				select a.ide_prcla,devengado_prmen as devengado from pre_anual a, pre_mensual b"
				+" 				where not a.ide_prcla is null and a.ide_pranu = b.ide_pranu and ide_geani = "+com_anio.getValue()+" and fecha_ejecucion_prmen between '"+sel_calendario.getFecha1String()+"' and '"+sel_calendario.getFecha2String()+"'"
				+" 				) a group by ide_prcla"
				+" 		) a  where a.ide_prcla=pre_cedula_ingreso.ide_prcla ";
		
		String sql_cobrado="update pre_cedula_ingreso"
				+" set cobrado_prcing = cobrado from ("
				+" 		select ide_prcla,sum(cobrado) as cobrado from  ("
				+" 				select a.ide_prcla,cobrado_prmen as cobrado from pre_anual a, pre_mensual b"
				+" 				where not a.ide_prcla is null and a.ide_pranu = b.ide_pranu and ide_geani = "+com_anio.getValue()+" and fecha_ejecucion_prmen between '"+sel_calendario.getFecha1String()+"' and '"+sel_calendario.getFecha2String()+"'"
				+" 				) a group by ide_prcla"
				+" 		) a  where a.ide_prcla=pre_cedula_ingreso.ide_prcla";
		
		utilitario.getConexion().ejecutarSql(sql_inicial);
		utilitario.getConexion().ejecutarSql(sql_reformado);
		utilitario.getConexion().ejecutarSql(sql_devengado);
		utilitario.getConexion().ejecutarSql(sql_cobrado);
		
		String sql_autogestion="update pre_cedula_ingreso set devengado_prcing=(inicial_prcing + reformado_prcing), cobrado_prcing=(inicial_prcing + reformado_prcing) where ide_prcla=677 and fecha_final_prcing between '2020-07-01' and '2021-12-31'"; // Segun Ticket #466100 Enviado el: lunes, 14 de febrero de 2022 8:50 - Maritza Vanessa Zarcos Cabanilla
		utilitario.getConexion().ejecutarSql(sql_autogestion); //Segun Ticket #339096 31/7/20 10:53 Eso automáticamente el momento que pasa a nuestro presupuesto y al ser fondos propios no necesita de un registro individual para ir devengado
		
		TablaGenerica tab_nivel=utilitario.consultar("select 1 as codigo, max(nivel_prcla) as maximo from pre_clasificador");
		int int_nivel=pckUtilidades.CConversion.CInt(tab_nivel.getValor("maximo"));
		
		for(int i=0;i <int_nivel;i++){
		
		int valor=int_nivel-i;
		String sql_actualiza_niveles="update pre_cedula_ingreso"
				+" set ide_prfuf=0,inicial_prcing=inicial,"
				+" reformado_prcing= reforma,"
				+" devengado_prcing=devengado,"
				+" cobrado_prcing= cobrado,"
				+" cobrado_efectivo_prcing=  cobrado_efectivo"
				+" from("
					+" 	select pre_ide_prcla,sum(inicial_prcing) as inicial,sum(reformado_prcing) as reforma,sum(devengado_prcing) as devengado,"
					+"  sum(cobrado_prcing) as cobrado,sum(cobrado_efectivo_prcing) as cobrado_efectivo"
					+" 	from pre_cedula_ingreso a,pre_clasificador b where a.ide_prcla = b.ide_prcla and nivel_prcla="+valor+" group by pre_ide_prcla"
					+" 	) a where pre_cedula_ingreso.ide_prcla = a.pre_ide_prcla; ";
		utilitario.getConexion().ejecutarSql(sql_actualiza_niveles);
		}
		String sql_saldo="update pre_cedula_ingreso"
				+" set ide_prfuf=0,codificado_prcing=inicial_prcing + reformado_prcing,"
				+" saldo_devengar_prcing = (inicial_prcing + reformado_prcing) - devengado_prcing,"
				+" saldo_cobrar_prcing = devengado_prcing - cobrado_prcing";
		
		utilitario.getConexion().ejecutarSql(sql_saldo);		
	}
	
	public void ejecutaFuente(){  
		String sql_inicial="update pre_cedula_ingreso"  
				+" set inicial_prcing = inicial from ("
				+" 		select ide_prcla,ide_prfuf,sum(valor_inicial_pranu) as inicial from ("
				+" 				select a.ide_prcla,a.ide_prfuf,valor_inicial_pranu from pre_anual a where not a.ide_prcla is null and ide_geani = "+com_anio.getValue()
				+" 				) a group by ide_prcla,ide_prfuf ) a where a.ide_prcla=pre_cedula_ingreso.ide_prcla and  a.ide_prfuf = pre_cedula_ingreso.ide_prfuf";
		
		String sql_reformado="update pre_cedula_ingreso"
				+" set reformado_prcing = reforma from ("
				+" 	select ide_prcla,ide_prfuf,sum(reforma) as reforma from  ("
				+" 		select a.ide_prcla,a.ide_prfuf,(val_reforma_d_prrem - val_reforma_h_prrem) as reforma from pre_anual a, pre_reforma_mes b"
				+" 		where not a.ide_prcla is null and a.ide_pranu = b.ide_pranu and ide_geani = "+com_anio.getValue()+" and fecha_reforma_prrem between '"+sel_calendario.getFecha1String()+"' and '"+sel_calendario.getFecha2String()+"'"
				+" 		) a group by ide_prcla,ide_prfuf"
				+" 		) a  where a.ide_prcla=pre_cedula_ingreso.ide_prcla and  a.ide_prfuf = pre_cedula_ingreso.ide_prfuf";
		
		String sql_devengado="update pre_cedula_ingreso"
				+" set devengado_prcing = devengado from ("
				+" 		select ide_prcla,ide_prfuf,sum(devengado) as devengado from  ("
				+" 				select a.ide_prcla,a.ide_prfuf,devengado_prmen as devengado from pre_anual a, pre_mensual b"
				+" 				where not a.ide_prcla is null and a.ide_pranu = b.ide_pranu and ide_geani = "+com_anio.getValue()+" and fecha_ejecucion_prmen between '"+sel_calendario.getFecha1String()+"' and '"+sel_calendario.getFecha2String()+"'"
				+" 				) a group by ide_prcla,ide_prfuf"
				+" 		) a  where a.ide_prcla=pre_cedula_ingreso.ide_prcla and  a.ide_prfuf = pre_cedula_ingreso.ide_prfuf";
		
		String sql_cobrado="update pre_cedula_ingreso"
				+" set cobrado_prcing = cobrado from ("
				+" 		select ide_prcla,ide_prfuf,sum(cobrado) as cobrado from  ("
				+" 				select a.ide_prcla,a.ide_prfuf,cobrado_prmen as cobrado from pre_anual a, pre_mensual b"
				+" 				where not a.ide_prcla is null and a.ide_pranu = b.ide_pranu and ide_geani = "+com_anio.getValue()+" and fecha_ejecucion_prmen between '"+sel_calendario.getFecha1String()+"' and '"+sel_calendario.getFecha2String()+"'"
				+" 				) a group by ide_prcla,ide_prfuf"
				+" 		) a  where a.ide_prcla=pre_cedula_ingreso.ide_prcla and  a.ide_prfuf = pre_cedula_ingreso.ide_prfuf";
		
		utilitario.getConexion().ejecutarSql(sql_inicial);
		utilitario.getConexion().ejecutarSql(sql_reformado);
		utilitario.getConexion().ejecutarSql(sql_devengado);
		utilitario.getConexion().ejecutarSql(sql_cobrado);
		
		String sql_autogestion="update pre_cedula_ingreso set devengado_prcing=(inicial_prcing + reformado_prcing), cobrado_prcing=(inicial_prcing + reformado_prcing) where ide_prcla=677 and fecha_final_prcing between '2020-07-01' and '2021-12-31'"; // Segun Ticket #466100 Enviado el: lunes, 14 de febrero de 2022 8:50 - Maritza Vanessa Zarcos Cabanilla
		utilitario.getConexion().ejecutarSql(sql_autogestion); //Segun Ticket #339096 31/7/20 10:53
		
		TablaGenerica tab_nivel=utilitario.consultar("select 1 as codigo, max(nivel_prcla) as maximo from pre_clasificador");
		int int_nivel=pckUtilidades.CConversion.CInt(tab_nivel.getValor("maximo"));
		
		for(int i=0;i <int_nivel;i++){
		
		int valor=int_nivel-i;
		String sql_actualiza_niveles="update pre_cedula_ingreso"
				+" set inicial_prcing=inicial,"
				+" reformado_prcing= reforma,"
				+" devengado_prcing=devengado,"
				+" cobrado_prcing= cobrado,"
				+" cobrado_efectivo_prcing=  cobrado_efectivo"
				+" from("
					+" 	select pre_ide_prcla,ide_prfuf,sum(inicial_prcing) as inicial,sum(reformado_prcing) as reforma,sum(devengado_prcing) as devengado,"
					+"  sum(cobrado_prcing) as cobrado,sum(cobrado_efectivo_prcing) as cobrado_efectivo"
					+" 	from pre_cedula_ingreso a,pre_clasificador b where a.ide_prcla = b.ide_prcla and nivel_prcla="+valor+" group by pre_ide_prcla,ide_prfuf"
					+" 	) a where pre_cedula_ingreso.ide_prcla = a.pre_ide_prcla and pre_cedula_ingreso.ide_prfuf = a.ide_prfuf; ";
		utilitario.getConexion().ejecutarSql(sql_actualiza_niveles);
		}
		String sql_saldo="update pre_cedula_ingreso"
				+" set codificado_prcing=inicial_prcing + reformado_prcing,"
				+" saldo_devengar_prcing = (inicial_prcing + reformado_prcing) - devengado_prcing,"
				+" saldo_cobrar_prcing = devengado_prcing - cobrado_prcing";
		
		utilitario.getConexion().ejecutarSql(sql_saldo);
	}
	
	public void exportarExcel()
	{
	      if(com_anio.getValue()==null){
	        utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
	        return;
	      }
	      Tabla tab_tablaXls = new Tabla();
	      tab_tablaXls.setSql("SELECT fecha_inicial_prcing, fecha_final_prcing, partida, fuente, inicial_prcing, reformado_prcing, "+
	    		  				" codificado_prcing, devengado_prcing, cobrado_prcing, cobrado_efectivo_prcing "+
								" FROM cedula_ingreso i "+
								" left join pre_clasificador pc on pc.ide_prcla=i.ide_prcla "+
								" where nivel_prcla=4 "+
								" order by fuente, partida ;");
	      tab_tablaXls.ejecutarSql();
	      tab_tablaXls.exportarXLS();
    }
	
	@Override
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}
	
	@Override
	public void aceptarReporte() {
		// TODO Auto-generated method stub
		if(rep_reporte.getReporteSelecionado().equals("Cedula Ingreso")){
			
			if (rep_reporte.isVisible()){
				map_parametros=new HashMap();		
				rep_reporte.cerrar();
				map_parametros.clear();
				
				if(com_anio.getValue()==null){
					utilitario.agregarMensajeInfo("Selecione un Año", "");
					return;			
				}
				com_nivel_cuenta_inicial.setValue("4");
				com_nivel_cuenta_final.setValue("4");
				dia_busca_nivel.dibujar();
			}
			else if (dia_busca_nivel.isVisible()){
				
				map_parametros.put("titulo","CÉDULA DE INGRESOS");				
				map_parametros.put("ide_geani",  pckUtilidades.CConversion.CInt(com_anio.getValue()+""));
				map_parametros.put("nivel_inicial", pckUtilidades.CConversion.CInt(com_nivel_cuenta_inicial.getValue()+""));
				map_parametros.put("nivel_final",  pckUtilidades.CConversion.CInt(com_nivel_cuenta_final.getValue()+""));
				map_parametros.put("jefe_presupuesto", utilitario.getVariable("p_nombre_jefe_presupuesto"));
				map_parametros.put("coordinador_finaciero",  utilitario.getVariable("p_nombre_coordinador_fin"));
				map_parametros.put("pie_coordinador_finaciero",  utilitario.getVariable("p_pie_coordinador_fin"));
				map_parametros.put("pie_especialista_pres",  utilitario.getVariable("p_pie_especialista_pres"));

				dia_busca_nivel.cerrar();	
				sel_rep.setSeleccionFormatoReporte(map_parametros,rep_reporte.getPath());
				sel_rep.dibujar();
			}
		} 
	}
	
	public void seleccionaElAnio (){
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Año", "");
			return;			

		}
		
		
	}
	/**
	 * limpia toda la pantalla incluyendo el autocompletar
	 */
	
	public void limpiar() {
		arb_tabla_g.limpiar();
		utilitario.addUpdate("arb_tabla_g");// limpia y refresca el autocompletar
	}
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		//tab_cedula_ingreso.insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		//tab_cedula_ingreso.guardar();
		//guardarPantalla();		
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		//tab_cedula_ingreso.eliminar();
	}

	
	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}
	
	public Combo getCom_nivel_cuenta_inicial() {
		return com_nivel_cuenta_inicial;
	}
	public void setCom_nivel_cuenta_inicial(Combo com_nivel_cuenta_inicial) {
		this.com_nivel_cuenta_inicial = com_nivel_cuenta_inicial;
	}
	public Combo getCom_nivel_cuenta_final() {
		return com_nivel_cuenta_final;
	}
	public void setCom_nivel_cuenta_final(Combo com_nivel_cuenta_final) {
		this.com_nivel_cuenta_final = com_nivel_cuenta_final;
	}

	public Confirmar getCon_guardar() {
		return con_guardar;
	}
	public void setCon_guardar(Confirmar con_guardar) {
		this.con_guardar = con_guardar;
	}
	public SeleccionCalendario getSel_calendario() {
		return sel_calendario;
	}
	public void setSel_calendario(SeleccionCalendario sel_calendario) {
		this.sel_calendario = sel_calendario;
	}

	public Reporte getRep_reporte() {
		return rep_reporte;
	}

	public void setRep_reporte(Reporte rep_reporte) {
		this.rep_reporte = rep_reporte;
	}

	public SeleccionFormatoReporte getSel_rep() {
		return sel_rep;
	}

	public void setSel_rep(SeleccionFormatoReporte sel_rep) {
		this.sel_rep = sel_rep;
	}

	public SeleccionTabla getSel_devengados() {
		return sel_devengados;
	}

	public void setSel_devengados(SeleccionTabla sel_devengados) {
		this.sel_devengados = sel_devengados;
	}

	public Dialogo getDia_recalcular() {
		return dia_recalcular;
	}

	public void setDia_recalcular(Dialogo dia_recalcular) {
		this.dia_recalcular = dia_recalcular;
	}

	public Radio getRad_imprimir() {
		return rad_imprimir;
	}

	public void setRad_imprimir(Radio rad_imprimir) {
		this.rad_imprimir = rad_imprimir;
	}

	public ArbolTable getArb_tabla_g() {
		return arb_tabla_g;
	}

	public void setArb_tabla_g(ArbolTable arb_tabla_g) {
		this.arb_tabla_g = arb_tabla_g;
	}


}
