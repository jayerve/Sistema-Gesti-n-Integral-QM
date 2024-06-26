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
import paq_general.ejb.ServicioGeneral;
import paq_presupuesto.ejb.ServicioPresupuesto;
import paq_sistema.aplicacion.Pantalla;

public class pre_cedula_gastos extends Pantalla{

	private ArbolTable arb_tabla_g=new ArbolTable();
	
	private Combo com_anio=new Combo();
	private Dialogo dia_recalcular = new Dialogo();
	private Dialogo dia_busca_nivel = new Dialogo();
	private Confirmar con_guardar=new Confirmar();
	
	private SeleccionTabla sel_certificaciones= new SeleccionTabla();
	private SeleccionTabla sel_compromisos= new SeleccionTabla();
	private SeleccionTabla sel_devengados= new SeleccionTabla();
	private SeleccionCalendario sel_calendario=new SeleccionCalendario();
	
	private Combo com_nivel_cuenta_inicial = new Combo();
	private Combo com_nivel_cuenta_final = new Combo();
	
	public Radio rad_imprimir= new Radio();
	public static String par_tipo_asiento_inicial;
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();
	private Map map_parametros = new HashMap();
	private int tipoRecalculo=1;

	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioPresupuesto ser_presupuesto = (ServicioPresupuesto ) utilitario.instanciarEJB(ServicioPresupuesto.class);
	@EJB
	private ServicioGeneral ser_general = (ServicioGeneral ) utilitario.instanciarEJB(ServicioGeneral.class);
	
	public pre_cedula_gastos() {
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
        Object fila1x[] = {"0", "COSOLIDADO"};
        Object fila2x[] = {"1", "POR FUENTE FINANCIAMIENTO" };
	       
        listax.add(fila1x);
        listax.add(fila2x);
        rad_imprimir.setRadio(listax);

	    dia_recalcular.setId("dia_recalcular");
	    dia_recalcular.setTitle("Ingrese el tipo de recalculo");
       	dia_recalcular.setWidth("25%");
       	dia_recalcular.setHeight("20%");
       	dia_recalcular.setDialogo(rad_imprimir);
       	dia_recalcular.getBot_aceptar().setMetodo("generarCedula");
       	
       	agregarComponente(dia_recalcular);
				
	    arb_tabla_g.setId("arb_tabla_g");
	    
		arb_tabla_g.setArbol("cedula_gasto", "ide_prcla","pre_ide_prcla", "partida");
		arb_tabla_g.setCamposAux("inicial_prcei,reforma_prcei,codificado_prcei,certificado_prcei,comprometido_prcei,devengado_prcei,pagado_prcei,saldo_certificar_prcei,saldo_comprometer_prcei,saldo_devengar_prcei,saldo_pagado_prcei,fecha_inicial_prcei,fecha_final_prcei,fuente");
		arb_tabla_g.setCampoGroupBy("fuente");
		arb_tabla_g.setColumnaSuma("inicial_prcei,reforma_prcei,codificado_prcei,certificado_prcei,comprometido_prcei,devengado_prcei,pagado_prcei,saldo_certificar_prcei,saldo_comprometer_prcei,saldo_devengar_prcei,saldo_pagado_prcei");
		//arb_tabla_g.setCondicion("ide_prcla in (select ide_prcla from pre_clasificador where nivel_prcla between "+com_nivel_cuenta_inicial.getValue()+" and "+com_nivel_cuenta_final.getValue()+" and tipo_prcla = 0 )");
		arb_tabla_g.dibujar();

		sel_certificaciones.setId("sel_certificaciones");
		sel_certificaciones.setSeleccionTabla(ser_presupuesto.getRptCertificaciones(com_anio.getValue().toString()),"ide_prcla");
		sel_certificaciones.setTitle("Ver Certificaciones enlazadas.");
		sel_certificaciones.getBoc_seleccion_inversa().setRendered(false);
		sel_certificaciones.setRadio();
		sel_certificaciones.getTab_seleccion().setColumnaSuma("certificado,comprometido,saldo_por_comprometer");
		sel_certificaciones.getTab_seleccion().getColumna("detalle_prcer").setFiltroContenido();
		sel_certificaciones.getBot_aceptar().setRendered(false);
		agregarComponente(sel_certificaciones);
		
		sel_compromisos.setId("sel_compromisos");
		sel_compromisos.setSeleccionTabla(ser_presupuesto.getRptCompromisos(com_anio.getValue().toString()),"ide_prcla");
		sel_compromisos.setTitle("Ver Compromisos enlazados.");
		sel_compromisos.getBoc_seleccion_inversa().setRendered(false);
		sel_compromisos.setRadio();
		sel_compromisos.getTab_seleccion().setColumnaSuma("comprometido,devengado,saldo_por_devengar");
		sel_compromisos.getTab_seleccion().getColumna("detalle").setFiltroContenido();
		sel_compromisos.getTab_seleccion().getColumna("nro_contrato_proceso_prtra").setFiltroContenido();
		sel_compromisos.getBot_aceptar().setRendered(false);
		agregarComponente(sel_compromisos);
		
		sel_devengados.setId("sel_devengados");
		sel_devengados.setSeleccionTabla(ser_presupuesto.getRptDevengados(com_anio.getValue().toString(),"0",""),"ide_prcla");
		sel_devengados.setTitle("Ver Devengados enlazados.");
		sel_devengados.getBoc_seleccion_inversa().setRendered(false);
		sel_devengados.setRadio();
		sel_devengados.getTab_seleccion().setColumnaSuma("devengado_prmen,pagado_prmen");
		sel_devengados.getTab_seleccion().getColumna("comprobante_egreso_tecpo").setFiltroContenido();
		sel_devengados.getTab_seleccion().getColumna("nombre_proveedor").setFiltroContenido();
		sel_devengados.getBot_aceptar().setRendered(false);
		agregarComponente(sel_devengados);
		
		ItemMenu itm_ver_certificaciones = new ItemMenu();
		itm_ver_certificaciones.setValue("Ver Certificaciones");
		itm_ver_certificaciones.setMetodo("verCertificaciones");
		itm_ver_certificaciones.setIcon("ui-icon-search");
		
		ItemMenu itm_ver_compromisos = new ItemMenu();
		itm_ver_compromisos.setValue("Ver Compromisos");
		itm_ver_compromisos.setMetodo("verCompromisos");
		itm_ver_compromisos.setIcon("ui-icon-search");
		
		ItemMenu itm_ver_devengados = new ItemMenu();
		itm_ver_devengados.setValue("Ver Devengados");
		itm_ver_devengados.setMetodo("verDevengados");
		itm_ver_devengados.setIcon("ui-icon-search");
		
		PanelTabla pat_tabla=new PanelTabla();
		pat_tabla.setPanelArbolTabla(arb_tabla_g);
		pat_tabla.getMenuTabla().getChildren().add(itm_ver_certificaciones);
		pat_tabla.getMenuTabla().getChildren().add(itm_ver_compromisos);
		pat_tabla.getMenuTabla().getChildren().add(itm_ver_devengados);

		Division div1 = new Division();
		div1.dividir1(pat_tabla);
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
		sel_calendario.setFecha1(utilitario.obtenerFechaInicioAnio());
		sel_calendario.setFecha2(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		sel_calendario.getBot_aceptar().setMetodo("generarCedula");
		agregarComponente(sel_calendario);
	}
	
	public void verCertificaciones() {
		sel_certificaciones.getTab_seleccion().setSql(ser_presupuesto.getRptCertificaciones(com_anio.getValue().toString())+ " where ide_prcla="+arb_tabla_g.getValorSeleccionado());
		sel_certificaciones.getTab_seleccion().ejecutarSql();
		sel_certificaciones.dibujar();
	}
	
	public void verCompromisos() {
		sel_compromisos.getTab_seleccion().setSql(ser_presupuesto.getRptCompromisos(com_anio.getValue().toString())+ " where ide_prcla="+arb_tabla_g.getValorSeleccionado());
		//sel_compromisos.getTab_seleccion().imprimirSql();
		sel_compromisos.getTab_seleccion().ejecutarSql();
		sel_compromisos.dibujar();
	}
	
	public void verDevengados() {
		
		//System.out.println("getValoresSeleccionados() "+arb_tabla_g.getValoresSeleccionados());
		String[] valores =  arb_tabla_g.getValoresSeleccionados().split(",");
		
		sel_devengados.getTab_seleccion().setSql(ser_presupuesto.getRptDevengados(com_anio.getValue().toString(),arb_tabla_g.getValorSeleccionado(),valores[valores.length-1]));
		
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
				
				String sql_borra_tabla="delete from pre_cedula_gastos;";
				utilitario.getConexion().ejecutarSql(sql_borra_tabla);
				
				int int_repetir=1;
				TablaGenerica tab_fuente_finaciamiento = utilitario.consultar("select ide_prfuf,ide_geani from pre_fuente_financiamiento_ini where ide_geani="+com_anio.getValue());
				//System.out.println("tab_fuente_finaciamiento");
				tab_fuente_finaciamiento.imprimirSql();
		
				if(tipoRecalculo==1){
					int_repetir=tab_fuente_finaciamiento.getTotalFilas();
				}
		
				for (int i=0;i<int_repetir;i++){
				
					String sql="insert into pre_cedula_gastos (ide_prcei,ide_geani,ide_prcla,inicial_prcei,certificado_prcei,comprometido_prcei,devengado_prcei,pagado_prcei,saldo_comprometer_prcei,"
							+" saldo_devengar_prcei,reforma_prcei,codificado_prcei,saldo_pagado_prcei,ide_prfuf,fecha_inicial_prcei,fecha_final_prcei,saldo_certificar_prcei)"
							+" select row_number() over(order by pc.ide_prcla) + (select (case when  max(ide_prcei) is null then 0 else max(ide_prcei) end) as codigo from pre_cedula_gastos) as codigo,"+com_anio.getValue()+",pc.ide_prcla,0,0,0,0,0,0,0,0,0,0,"
							+  tab_fuente_finaciamiento.getValor(i,"ide_prfuf")+",'"+sel_calendario.getFecha1String()+"','"+sel_calendario.getFecha2String()+"',0"
							+" from pre_clasificador pc where coalesce(activo_prcla,false)=true;";
					
					//System.out.println("imprimir sql insert pre_cedula_gastos "+sql);
					utilitario.getConexion().ejecutarSql(sql);
				}
				
				if(tipoRecalculo==0){ // conciliado
					ejecutaConsolidado();
				}
				
				if(tipoRecalculo==1){ // fuente financiamiento
					ejecutaFuente(); 
				}
				
				String sql_saldo="update pre_cedula_gastos"
				+" set codificado_prcei=inicial_prcei+reforma_prcei,"
				+" saldo_certificar_prcei=(inicial_prcei+reforma_prcei)-certificado_prcei,"
				+" saldo_comprometer_prcei=certificado_prcei-comprometido_prcei,"
				+" saldo_devengar_prcei=comprometido_prcei-devengado_prcei,"
				+" saldo_pagado_prcei=devengado_prcei-pagado_prcei";
				utilitario.getConexion().ejecutarSql(sql_saldo);
				
				String sql_borra_ceros="delete from pre_cedula_gastos where (abs(inicial_prcei)+ abs(reforma_prcei)+abs(codificado_prcei)+ abs(certificado_prcei)+ abs(comprometido_prcei)+ abs(devengado_prcei))=0;";
				utilitario.getConexion().ejecutarSql(sql_borra_ceros);
				
				arb_tabla_g.ejecutarSql();
				utilitario.addUpdate("arb_tabla_g");	
		}
		else
		{
			rad_imprimir.setValue(tipoRecalculo);
			dia_recalcular.dibujar();
		}
	}
	
	public void ejecutaConsolidado(){
		String sql_gasto="update pre_cedula_gastos"
					+" set ide_prfuf=0,inicial_prcei=inicial,"
					+" reforma_prcei=reforma,"
					+" certificado_prcei=certificado,"
					+" comprometido_prcei=comprometido,"
					+" devengado_prcei=devengado,"
					+" pagado_prcei=pagado"
					+"  from ( " + getSqlGasto(true)					
					+ " ) a where pre_cedula_gastos.ide_prcla = a.ide_prcla ";
		
		//System.out.println("imprimir sql ejecutaConsolidado "+sql_gasto);
		utilitario.getConexion().ejecutarSql(sql_gasto);
		
		TablaGenerica tab_nivel=utilitario.consultar("select 1 as codigo, max(nivel_prcla) as maximo from pre_clasificador");
		int int_nivel=pckUtilidades.CConversion.CInt(tab_nivel.getValor("maximo"));
		
		for(int i=0;i <int_nivel;i++){
		
			int valor=int_nivel-i;
			String sql_actualiza_niveles="update pre_cedula_gastos"
						+" set ide_prfuf=0,inicial_prcei=inicial,"
						+" reforma_prcei=reforma,"
						+" certificado_prcei=certificado,"
						+" comprometido_prcei=comprometido,"
						+" devengado_prcei=devengado,"
						+" pagado_prcei=pagado"
						+" from ("
						+" 	select pre_ide_prcla,sum(inicial_prcei) as inicial,sum(reforma_prcei) as reforma,sum(certificado_prcei) as certificado,"
						+" 	sum(comprometido_prcei) as comprometido,sum(devengado_prcei) as devengado,sum(pagado_prcei) as pagado"
						+" 	from pre_cedula_gastos a,pre_clasificador b where a.ide_prcla = b.ide_prcla and nivel_prcla="+valor+" group by pre_ide_prcla"
						+" 	) a where pre_cedula_gastos.ide_prcla = a.pre_ide_prcla ;";
			utilitario.getConexion().ejecutarSql(sql_actualiza_niveles);
		}

	}
	
	public void ejecutaFuente(){
		String sql_gasto="update pre_cedula_gastos"
				+" set inicial_prcei=inicial,"
				+" reforma_prcei=reforma,"
				+" certificado_prcei=certificado,"
				+" comprometido_prcei=comprometido,"
				+" devengado_prcei=devengado,"
				+" pagado_prcei=pagado"
				+"  from ( " + getSqlGasto(false)					
				+ " ) a where pre_cedula_gastos.ide_prcla = a.ide_prcla and pre_cedula_gastos.ide_prfuf=a.ide_prfuf ";
	
		//System.out.println("imprimir sql ejecutaFuente "+sql_gasto);
		utilitario.getConexion().ejecutarSql(sql_gasto);

		TablaGenerica tab_nivel=utilitario.consultar("select 1 as codigo, max(nivel_prcla) as maximo from pre_clasificador");
		int int_nivel=pckUtilidades.CConversion.CInt(tab_nivel.getValor("maximo"));
		
		for(int i=0;i <int_nivel;i++){
		
			int valor=int_nivel-i;
			String sql_actualiza_niveles="update pre_cedula_gastos"
					+" set inicial_prcei=inicial,"
					+" reforma_prcei=reforma,"
					+" certificado_prcei=certificado,"
					+" comprometido_prcei=comprometido,"
					+" devengado_prcei=devengado,"
					+" pagado_prcei=pagado"
					+" from ("
					+" 	select pre_ide_prcla,ide_prfuf,sum(inicial_prcei) as inicial,sum(reforma_prcei) as reforma,sum(certificado_prcei) as certificado,"
					+" 	sum(comprometido_prcei) as comprometido,sum(devengado_prcei) as devengado,sum(pagado_prcei) as pagado"
					+" 	from pre_cedula_gastos a,pre_clasificador b where a.ide_prcla = b.ide_prcla and nivel_prcla="+valor+" group by pre_ide_prcla,ide_prfuf"
					+" 	) a where pre_cedula_gastos.ide_prcla = a.pre_ide_prcla and pre_cedula_gastos.ide_prfuf = a.ide_prfuf ;";
			utilitario.getConexion().ejecutarSql(sql_actualiza_niveles);
		}
		
	}
	
	private String getSqlGasto(boolean consolidado)
	{
		String sql_gasto="";
		if(consolidado)
		{
		    sql_gasto="select b.ide_prcla, "  
				+" sum(presupuesto_inicial_prpoa) as inicial, "
				+" sum(reforma_prpoa) as reforma, "
				+" sum(certificado) as certificado, "
				+" sum(comprometido) as comprometido, "
				+" sum(devengado) as devengado, "
				+" sum(pagado) as pagado "
				
				+" from ( "+ser_general.getEjecucionPresupuestaria(sel_calendario.getFecha1String(),sel_calendario.getFecha2String())
				+" ) a, pre_clasificador b "
				+" where a.codigo_clasificador_prcla=b.codigo_clasificador_prcla and length(a.codigo_clasificador_prcla)>0 "
				+" group by b.ide_prcla ";
		}
		else
		{
			sql_gasto="select b.ide_prcla, c.ide_prfuf, "  
					+" sum(presupuesto_inicial_prpoa) as inicial, "
					+" sum(reforma_prpoa) as reforma, "
					+" sum(certificado) as certificado, "
					+" sum(comprometido) as comprometido, "
					+" sum(devengado) as devengado, "
					+" sum(pagado) as pagado "
					
					+" from ( "+ser_general.getEjecucionPresupuestaria(sel_calendario.getFecha1String(),sel_calendario.getFecha2String())
					+" ) a, pre_clasificador b, pre_fuente_financiamiento c "
					+" where a.codigo_clasificador_prcla=b.codigo_clasificador_prcla and a.fuente=c.detalle_prfuf and length(a.codigo_clasificador_prcla)>0 "
					+" group by b.ide_prcla,c.ide_prfuf ";
		}
		
		return sql_gasto;
	}
	
	public void seleccionaElAnio (){
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Año", "");
			return;			
		}

	}
	
	public void exportarExcel()
	{
	      if(com_anio.getValue()==null){
	        utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
	        return;
	      }
	      Tabla tab_tablaXls = new Tabla();
	      tab_tablaXls.setSql("SELECT fecha_inicial_prcei, fecha_final_prcei,partida, fuente, inicial_prcei, reforma_prcei, "+
	    		  			" codificado_prcei, certificado_prcei, comprometido_prcei, devengado_prcei, "+
    		  				" pagado_prcei, saldo_certificar_prcei, saldo_comprometer_prcei, "+
    		  				" saldo_devengar_prcei, saldo_pagado_prcei"+
	    		  			" FROM cedula_gasto g"+
    		  				" left join pre_clasificador pc on pc.ide_prcla=g.ide_prcla "+
	    		  			" where nivel_prcla=4 "+
	    		  			" order by fuente, partida;");
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
		if(rep_reporte.getReporteSelecionado().equals("Cedula Gasto")){

			if (rep_reporte.isVisible()){
				map_parametros=new HashMap();		
				rep_reporte.cerrar();
				map_parametros.clear();
				
				if(com_anio.getValue()==null){
					utilitario.agregarMensajeInfo("Selecione un Año", "");
					return;			
				}
				com_nivel_cuenta_inicial.setValue("1");
				com_nivel_cuenta_final.setValue("1");
				dia_busca_nivel.dibujar();
			}
			else if (dia_busca_nivel.isVisible()){
				
				map_parametros.put("titulo","CÉDULA DE GASTOS");
				map_parametros.put("tipo",  pckUtilidades.CConversion.CInt(tipoRecalculo));
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
	
	
	/**
	 * limpia toda la pantalla incluyendo el autocompletar
	 */
	public void limpiar() {
		arb_tabla_g.limpiar();
		utilitario.addUpdate("arb_tabla_g");	// limpia y refresca el autocompletar
	}
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		//tab_cedula_gasto.insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		//tab_cedula_gasto.guardar();
		//guardarPantalla();		
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		//tab_cedula_gasto.eliminar();
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

	public SeleccionTabla getSel_certificaciones() {
		return sel_certificaciones;
	}

	public void setSel_certificaciones(SeleccionTabla sel_certificaciones) {
		this.sel_certificaciones = sel_certificaciones;
	}

	public SeleccionTabla getSel_compromisos() {
		return sel_compromisos;
	}

	public void setSel_compromisos(SeleccionTabla sel_compromisos) {
		this.sel_compromisos = sel_compromisos;
	}

	public Dialogo getDia_busca_nivel() {
		return dia_busca_nivel;
	}

	public void setDia_busca_nivel(Dialogo dia_busca_nivel) {
		this.dia_busca_nivel = dia_busca_nivel;
	}

	public SeleccionTabla getSel_devengados() {
		return sel_devengados;
	}

	public void setSel_devengados(SeleccionTabla sel_devengados) {
		this.sel_devengados = sel_devengados;
	}

	
	

}
