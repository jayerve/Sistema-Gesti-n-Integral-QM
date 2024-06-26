/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_asistencia;

import java.util.HashMap;
import java.util.Map;


import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;

/**
 *
 * @author DELL-USER
 */
public class pre_marcaciones extends Pantalla {


	private Tabla tab_tabla = new Tabla();
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();
	private Map p_parametros=new HashMap();
	private SeleccionCalendario sel_cal = new SeleccionCalendario();
	private SeleccionTabla set_empleado=new SeleccionTabla();
	String FECHA_INICIO="",FECHA_FIN="",empleadoDoc="",tarjeta_marcacion_gtemp="",tipo_perfil="";
	TablaGenerica tabJefeInmediato=null;
	//sadsad
	

	public pre_marcaciones() {      
		
		
		 TablaGenerica tab_usua=utilitario.consultar("select * from SIS_USUARIO USU "
		 		+ "LEFT JOIN GTH_EMPLEADO EMP ON USU.IDE_GTEMP=EMP.IDE_GTEMP "
		 		+ "where USU.IDE_USUA="+utilitario.getVariable("ide_usua"));
		 
		 empleadoDoc=tab_usua.getValor("ide_gtemp");
		 tarjeta_marcacion_gtemp=tab_usua.getValor("tarjeta_marcacion_gtemp");
		
		 
		 
		 
		 tabJefeInmediato=utilitario.consultar("SELECT asjei.ide_asjei, asjei.ide_gtemp, asjei.ide_geedp, asjei.tipo_asjei, asjei.ide_geare, asjei.activo_asjei,area.detalle_geare,ide_gtemp_padre_asjei,cargo_padre_asjei  "
	    			+ "FROM asi_jefe_inmediato  asjei "
	    			+ "left join gen_area area on area.ide_geare=asjei.ide_geare "
	    			+ "where ide_gtemp="+empleadoDoc);
	    	
	    	if (tabJefeInmediato.getValor("tipo_asjei")==null || tabJefeInmediato.getValor("tipo_asjei").equals("") || tabJefeInmediato.getValor("tipo_asjei").isEmpty()) {
	    		tipo_perfil="2";
	    	}else {
	    		tipo_perfil=tabJefeInmediato.getValor("tipo_asjei");
	                
			
	    	}
		 
		 
	    if(tipo_perfil.equals("1")){
		bar_botones.limpiar();
		bar_botones.agregarComponente(new Etiqueta("Fecha Inicial :"));
		cal_fecha_inicial.setFechaActual();
		bar_botones.agregarComponente(cal_fecha_inicial);

		bar_botones.agregarComponente(new Etiqueta("Fecha Final :"));
		cal_fecha_final.setFechaActual();
		bar_botones.agregarComponente(cal_fecha_final);

		Boton bot_filtrar = new Boton();
		bot_filtrar.setValue("Actualizar");
		bot_filtrar.setMetodo("actualizarMarcaciones");
		bot_filtrar.setIcon("ui-icon-refresh");
		bar_botones.agregarBoton(bot_filtrar);

		sel_cal.setId("sel_cal");
		sel_cal.setMultiple(true);
		sel_cal.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(sel_cal);

		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);

		sef_reporte.setId("sef_reporte");
		agregarComponente(sef_reporte);
		bar_botones.agregarReporte();
	    }		

		set_empleado.setId("set_empleado");
		/*set_empleado.setSeleccionTabla("SELECT "
				+ "EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, APELLIDO_PATERNO_GTEMP || ' ' || (case when APELLIDO_MATERNO_GTEMP is null then '' else APELLIDO_MATERNO_GTEMP end) || ' ' || PRIMER_NOMBRE_GTEMP || ' ' || (case when SEGUNDO_NOMBRE_GTEMP is null then '' else SEGUNDO_NOMBRE_GTEMP end)  "
				+ "as NOMBRES  "
				+ "FROM CON_BIOMETRICO_MARCACIONES BIO  "
				+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.TARJETA_MARCACION_GTEMP=trim(BIO.IDE_PERSONA_COBIM)  "
				+ "WHERE TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') BETWEEN '2021-03-01' AND '2021-03-31'  "
				+ "AND BIO.ide_corel in("+utilitario.getVariable("p_biometrico_teletrabajo")+") "
				+ "GROUP BY EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP,APELLIDO_PATERNO_GTEMP,APELLIDO_MATERNO_GTEMP,PRIMER_NOMBRE_GTEMP,SEGUNDO_NOMBRE_GTEMP  "
				+ "ORDER BY  APELLIDO_PATERNO_GTEMP,APELLIDO_MATERNO_GTEMP,PRIMER_NOMBRE_GTEMP,SEGUNDO_NOMBRE_GTEMP", "IDE_GTEMP");
		set_empleado.getBot_aceptar().setMetodo("aceptarReporte");
		set_empleado.setTitle("SELECCION DE EMPLEADOS");
		set_empleado.getTab_seleccion().getColumna("NOMBRES").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);		*/
		agregarComponente(set_empleado);
		
		 
		tab_tabla.setId("tab_tabla");
		tab_tabla.setCampoPrimaria("IDE_COBIM");		
		String diaInicio=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(utilitario.getFechaActual()),-15));
		String sql="";
				sql="SELECT BIO.IDE_COBIM,BIO.EVENTO_RELOJ_COBIM,IDE_PERSONA_COBIM as CODIGO_MARCACION,"
				+ "(SELECT DOCUMENTO_IDENTIDAD_GTEMP FROM GTH_EMPLEADO WHERE TARJETA_MARCACION_GTEMP=trim('"+tarjeta_marcacion_gtemp+"')) AS DOCUMENTO_IDENTIDAD_GTEMP,"
				+ "(SELECT APELLIDO_PATERNO_GTEMP || ' ' || (case when APELLIDO_MATERNO_GTEMP is null then '' else APELLIDO_MATERNO_GTEMP end) || ' ' || "
				+ "PRIMER_NOMBRE_GTEMP || ' ' || (case when SEGUNDO_NOMBRE_GTEMP is null then '' else SEGUNDO_NOMBRE_GTEMP end) "
				+ "FROM GTH_EMPLEADO WHERE TARJETA_MARCACION_GTEMP=trim('"+tarjeta_marcacion_gtemp+"')) as NOMBRES,"
				+ "DETALLE_COREL AS LUGAR_MARCACION,"
				+ "TO_CHAR(FECHA_EVENTO_COBIM,'YYYY-MM-DD') as FECHAM,TO_CHAR(FECHA_EVENTO_COBIM,'hh24:mi:ss') AS HORAM "
				+ "FROM CON_BIOMETRICO_MARCACIONES BIO INNER JOIN CON_RELOJ RELOJ ON BIO.IDE_COREL=RELOJ.IDE_COREL  WHERE TO_CHAR(FECHA_EVENTO_COBIM,'YYYY-MM-DD') ";
				if (tipo_perfil.equals("1")) {
					sql+="BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"'   ";
				}else {
					sql+=" >= '"+diaInicio+"'  and BIO.ide_persona_cobim=trim('"+tarjeta_marcacion_gtemp+"')  ";
				}
				
				//+ "BETWEEN '"+utilitario.getFechaActual()+"' AND '"+utilitario.getFechaActual()+"' and BIO.ide_persona_cobim='"+tarjeta_marcacion_gtemp+"'  "
				sql+="order by NOMBRES asc,FECHA_EVENTO_COBIM desc,TO_CHAR(FECHA_EVENTO_COBIM,'hh24:mi:ss') desc";
				tab_tabla.setSql(sql);
		
		//+ "BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"' and BIO.ide_persona_cobim='"+tarjeta_marcacion_gtemp+"'  "

		//WHERE IDE_GTEMP="+empleadoDoc+" ,
		

		
		
		//tab_tabla.getColumna("EVENTO_RELOJ_COBIM").setFiltro(true);
		//tab_tabla.getColumna("IDE_PERSONA_COBIM").setFiltro(true);
		tab_tabla.getColumna("IDE_COBIM").setNombreVisual("CODIGO");
		 if(tipo_perfil.equals("1")){
		tab_tabla.getColumna("EVENTO_RELOJ_COBIM").setNombreVisual("EVENTO");
		tab_tabla.getColumna("EVENTO_RELOJ_COBIM").setLongitud(3);
		tab_tabla.getColumna("EVENTO_RELOJ_COBIM").setVisible(true);
		 }else {
		tab_tabla.getColumna("EVENTO_RELOJ_COBIM").setVisible(false);
		}
		tab_tabla.getColumna("CODIGO_MARCACION").setNombreVisual("COD.MARCACION");
		tab_tabla.getColumna("CODIGO_MARCACION").setLongitud(25);

		tab_tabla.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setNombreVisual("DOC.IDENTIDAD");
		tab_tabla.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setLongitud(25);
		tab_tabla.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);

		tab_tabla.getColumna("LUGAR_MARCACION").setNombreVisual("LUGAR");
		tab_tabla.getColumna("LUGAR_MARCACION").setLongitud(25);
		tab_tabla.getColumna("LUGAR_MARCACION").setFiltro(true);
		
		
		tab_tabla.getColumna("FECHAM").setNombreVisual("FECHA");
		tab_tabla.getColumna("FECHAM").setLongitud(12);
		tab_tabla.getColumna("FECHAM").setFiltro(true);
		
		tab_tabla.getColumna("HORAM").setNombreVisual("HORA");
		tab_tabla.getColumna("HORAM").setLongitud(12);
		tab_tabla.getColumna("HORAM").setFiltro(true);
		

		//tab_tabla.getColumna("NOMBRES").setFiltro(true);
		//tab_tabla.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
		//tab_tabla.getColumna("LUGAR_MARCACION").setFiltro(true);
		
		
		tab_tabla.getColumna("NOMBRES").setFiltro(true);
		//tab_tabla.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
		//tab_tabla.getColumna("LUGAR_MARCACION").setFiltro(true);
		
		
		
		
		tab_tabla.setNumeroTabla(1);				
		tab_tabla.setLectura(true);
		tab_tabla.setRows(20);
		tab_tabla.dibujar();		
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_tabla);

		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir1(pat_panel);
		agregarComponente(div_division);
	}

	public void actualizarMarcaciones(){
		if (cal_fecha_inicial.getValue() != null && cal_fecha_final.getValue() != null) {
		//	tab_tabla.setSql("SELECT BIO.IDE_COBIM,BIO.EVENTO_RELOJ_COBIM,IDE_PERSONA_COBIM,(SELECT APELLIDO_PATERNO_GTEMP || ' ' || (case when APELLIDO_MATERNO_GTEMP is null then '' else APELLIDO_MATERNO_GTEMP end) || ' ' || PRIMER_NOMBRE_GTEMP || ' ' || (case when SEGUNDO_NOMBRE_GTEMP is null then '' else SEGUNDO_NOMBRE_GTEMP end) FROM GTH_EMPLEADO WHERE TARJETA_MARCACION_GTEMP=trim(IDE_PERSONA_COBIM)) as NOMBRES,DETALLE_COREL,TO_CHAR(FECHA_EVENTO_COBIM,'YYYY-MM-DD') as FECHAM,TO_CHAR(FECHA_EVENTO_COBIM,'hh24:mi:ss') AS HORAM FROM CON_BIOMETRICO_MARCACIONES BIO INNER JOIN CON_RELOJ RELOJ ON BIO.IDE_COREL=RELOJ.IDE_COREL  WHERE TO_CHAR(FECHA_EVENTO_COBIM,'YYYY-MM-DD') BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"'");	
		//	tab_tabla.ejecutarSql();
			
			String diaInicio=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(utilitario.getFechaActual()),-1));
			cal_fecha_inicial.getFecha();
			cal_fecha_final.getFecha();
		/*	tab_tabla.setSql("SELECT BIO.IDE_COBIM,BIO.EVENTO_RELOJ_COBIM,IDE_PERSONA_COBIM as CODIGO_MARCACION,"
					+ "(SELECT DOCUMENTO_IDENTIDAD_GTEMP FROM GTH_EMPLEADO )  AS DOCUMENTO_IDENTIDAD_GTEMP,"
					+ "(SELECT APELLIDO_PATERNO_GTEMP || ' ' || (case when APELLIDO_MATERNO_GTEMP is null then '' else APELLIDO_MATERNO_GTEMP end) || ' ' || PRIMER_NOMBRE_GTEMP || ' ' || "
					+ "(case when SEGUNDO_NOMBRE_GTEMP is null then '' else SEGUNDO_NOMBRE_GTEMP end) "
					+ "FROM GTH_EMPLEADO "
					+ ") as NOMBRES,"
//					+ "(SELECT DOCUMENTO_IDENTIDAD_GTEMP FROM GTH_EMPLEADO WHERE TARJETA_MARCACION_GTEMP=trim(IDE_PERSONA_COBIM)) as CEDULA,"
   				    + "DETALLE_COREL AS LUGAR_MARCACION,"
   					//+ "(SELECT DOCUMENTO_IDENTIDAD_GTEMP FROM GTH_EMPLEADO WHERE TARJETA_MARCACION_GTEMP=trim(IDE_PERSONA_COBIM)),"
   				    + "TO_CHAR(FECHA_EVENTO_COBIM,'YYYY-MM-DD') as FECHAM,TO_CHAR(FECHA_EVENTO_COBIM,'hh24:mi:ss') AS HORAM FROM CON_BIOMETRICO_MARCACIONES BIO "
   				    + "INNER JOIN CON_RELOJ RELOJ ON BIO.IDE_COREL=RELOJ.IDE_COREL  "
   				    + "WHERE TO_CHAR(FECHA_EVENTO_COBIM,'YYYY-MM-DD') BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"' "
   				    + "order by NOMBRES asc,FECHA_EVENTO_COBIM asc");*/
			
			tab_tabla.setSql("SELECT BIO.IDE_COBIM,BIO.EVENTO_RELOJ_COBIM,IDE_PERSONA_COBIM as CODIGO_MARCACION,"
					+ "(SELECT DOCUMENTO_IDENTIDAD_GTEMP FROM GTH_EMPLEADO WHERE TARJETA_MARCACION_GTEMP=trim(IDE_PERSONA_COBIM)) AS DOCUMENTO_IDENTIDAD_GTEMP,"
					+ "(SELECT APELLIDO_PATERNO_GTEMP || ' ' || (case when APELLIDO_MATERNO_GTEMP is null then '' else APELLIDO_MATERNO_GTEMP end) || ' ' || PRIMER_NOMBRE_GTEMP || ' ' || (case when SEGUNDO_NOMBRE_GTEMP is null then '' else SEGUNDO_NOMBRE_GTEMP end) FROM GTH_EMPLEADO WHERE TARJETA_MARCACION_GTEMP=trim(IDE_PERSONA_COBIM)) as NOMBRES,"
//					+ "(SELECT DOCUMENTO_IDENTIDAD_GTEMP FROM GTH_EMPLEADO WHERE TARJETA_MARCACION_GTEMP=trim(IDE_PERSONA_COBIM)) as CEDULA,"
   				    + "DETALLE_COREL AS LUGAR_MARCACION,"
   					//+ "(SELECT DOCUMENTO_IDENTIDAD_GTEMP FROM GTH_EMPLEADO WHERE TARJETA_MARCACION_GTEMP=trim(IDE_PERSONA_COBIM)),"
   				    + "TO_CHAR(FECHA_EVENTO_COBIM,'YYYY-MM-DD') as FECHAM,TO_CHAR(FECHA_EVENTO_COBIM,'hh24:mi:ss') AS HORAM FROM CON_BIOMETRICO_MARCACIONES BIO INNER JOIN CON_RELOJ RELOJ ON BIO.IDE_COREL=RELOJ.IDE_COREL  "
   				    + "WHERE TO_CHAR(FECHA_EVENTO_COBIM,'YYYY-MM-DD') BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"' "
   				    + "order by NOMBRES asc,FECHA_EVENTO_COBIM desc");
			tab_tabla.ejecutarSql();
			
				
		}
		else {
			utilitario.agregarMensajeInfo("Filtros no validos","Debe ingresar los fitros de rangos de fechas");
		}		
	}

	@Override
	public void insertar() {
		tab_tabla.insertar();
	}

	@Override
	public void guardar() {
		tab_tabla.guardar();
		guardarPantalla();
	}

	@Override
	public void eliminar() {
		tab_tabla.eliminar();
	}

	public Tabla getTab_tabla() {
		return tab_tabla;
	}

	public void setTab_tabla(Tabla tab_tabla) {
		this.tab_tabla = tab_tabla;
	}

	@Override
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();

	}

	@Override
	public void aceptarReporte() {
		// TODO Auto-generated method stub
		if (rep_reporte.getReporteSelecionado().equals("Reporte de Marcaciones por Teletrabajo")){
				if (rep_reporte.isVisible()){
					p_parametros=new HashMap();				
					rep_reporte.cerrar();		
					sel_cal.dibujar();
					//set_empleado.setSql(null);
					//utilitario.addUpdate("set_empleado");
				}else if(sel_cal.isVisible()){
					if(sel_cal.isFechasValidas()){
						FECHA_FIN=sel_cal.getFecha2String();
						FECHA_INICIO=sel_cal.getFecha1String();
						p_parametros.put("FECHA_INICIO",sel_cal.getFecha1String());
						p_parametros.put("FECHA_FIN",sel_cal.getFecha2String());
						System.out.println("fecha 1:"+sel_cal.getFecha1String());
						System.out.println("fecha 2:"+sel_cal.getFecha2String());
						set_empleado.setId("set_empleado");
						set_empleado.setSeleccionTabla("SELECT "
								+ "EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, APELLIDO_PATERNO_GTEMP || ' ' || (case when APELLIDO_MATERNO_GTEMP is null then '' else APELLIDO_MATERNO_GTEMP end) || ' ' || PRIMER_NOMBRE_GTEMP || ' ' || (case when SEGUNDO_NOMBRE_GTEMP is null then '' else SEGUNDO_NOMBRE_GTEMP end)  "
								+ "as NOMBRES  "
								+ "FROM CON_BIOMETRICO_MARCACIONES BIO  "
								+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.TARJETA_MARCACION_GTEMP=trim(BIO.IDE_PERSONA_COBIM)  "
								+ "WHERE TO_CHAR(BIO.FECHA_EVENTO_COBIM,'YYYY-MM-DD') BETWEEN '"+FECHA_INICIO+"' AND '"+FECHA_FIN+"'  "
								+ "AND BIO.ide_corel in("+utilitario.getVariable("p_biometrico_teletrabajo")+") "
								+ "GROUP BY EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP,APELLIDO_PATERNO_GTEMP,APELLIDO_MATERNO_GTEMP,PRIMER_NOMBRE_GTEMP,SEGUNDO_NOMBRE_GTEMP  "
								+ "ORDER BY  APELLIDO_PATERNO_GTEMP,APELLIDO_MATERNO_GTEMP,PRIMER_NOMBRE_GTEMP,SEGUNDO_NOMBRE_GTEMP", "IDE_GTEMP");
						set_empleado.getBot_aceptar().setMetodo("aceptarReporte");
						set_empleado.setTitle("SELECCION DE EMPLEADOS");
						set_empleado.getTab_seleccion().getColumna("NOMBRES").setFiltro(true);
						set_empleado.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);	
						sel_cal.cerrar();
						set_empleado.redibujar();
		    		   	//sel_empleado_editar.redibujar();
		    		   //	utilitario.addUpdate("sel_empleado_editar");
					}else{
						utilitario.agregarMensajeInfo("ERROR AL GENERAR REPORTE","Las fechas no son validas");
					}
			
				}else if(set_empleado.isVisible()){
					if(set_empleado.getSeleccionados()!=null && !set_empleado.getSeleccionados().isEmpty()){
						
						TablaGenerica tab_empleado=utilitario.consultar("SELECT "
								+ "EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP  "
								+ "FROM GTH_EMPLEADO emp  "
								+ "where emp.ide_gtemp IN("+set_empleado.getSeleccionados()+")");
					
						TablaGenerica tab_ide_geedp = null;
						StringBuilder sql = new StringBuilder();
						for (int i = 0; i < tab_empleado.getTotalFilas(); i++) {
							tab_ide_geedp = utilitario.consultar("select ide_geedp,ide_gtemp from gen_empleados_departamento_par "
									+ "where ide_gtemp="+tab_empleado.getValor(i,"ide_gtemp")+" order by ide_geedp desc "
									+ "limit 1");
							if (tab_ide_geedp.getTotalFilas()>0) {
								
								if (i==(tab_empleado.getTotalFilas()-1)) {
									sql.append(tab_ide_geedp.getValor("IDE_GEEDP"));

								}else{
								sql.append(tab_ide_geedp.getValor("IDE_GEEDP"));
								sql.append(",");
								}
							}
						}
			
						
						System.out.println(""+set_empleado.getSeleccionados());
						p_parametros.put("IDE_PERSONA_COBIM", sql);
						p_parametros.put("IDE_COREL", utilitario.getVariable("p_biometrico_teletrabajo"));
	    				p_parametros.put("titulo", " REPORTE DE MARCACIONES POR TELETRABAJO");
						System.out.println("path "+rep_reporte.getPath());
						sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());						
						set_empleado.cerrar();
						sef_reporte.dibujar();

					}
					else {
						utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Empleado");
					}
				}
		}
	}

	public Reporte getRep_reporte() {
		return rep_reporte;
	}

	public void setRep_reporte(Reporte rep_reporte) {
		this.rep_reporte = rep_reporte;
	}

	public SeleccionFormatoReporte getSef_reporte() {
		return sef_reporte;
	}

	public void setSef_reporte(SeleccionFormatoReporte sef_reporte) {
		this.sef_reporte = sef_reporte;
	}

	public Map getP_parametros() {
		return p_parametros;
	}

	public void setP_parametros(Map p_parametros) {
		this.p_parametros = p_parametros;
	}

	public SeleccionCalendario getSel_cal() {
		return sel_cal;
	}

	public void setSel_cal(SeleccionCalendario sel_cal) {
		this.sel_cal = sel_cal;
	}

	public SeleccionTabla getSet_empleado() {
		return set_empleado;
	}

	public void setSet_empleado(SeleccionTabla set_empleado) {
		this.set_empleado = set_empleado;
	}

	public String getFECHA_INICIO() {
		return FECHA_INICIO;
	}

	public void setFECHA_INICIO(String fECHA_INICIO) {
		FECHA_INICIO = fECHA_INICIO;
	}

	public String getFECHA_FIN() {
		return FECHA_FIN;
	}

	public void setFECHA_FIN(String fECHA_FIN) {
		FECHA_FIN = fECHA_FIN;
	}
	
	
	
}
