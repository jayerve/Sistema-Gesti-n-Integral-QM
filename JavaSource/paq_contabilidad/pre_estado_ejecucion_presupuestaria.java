/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_contabilidad;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJB;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

import org.primefaces.component.column.Column;
import org.primefaces.component.columngroup.ColumnGroup;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.row.Row;
import org.primefaces.component.subtable.SubTable;

import paq_presupuesto.ejb.ServicioPresupuesto;
import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Check;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;


public class pre_estado_ejecucion_presupuestaria extends Pantalla {

	private List<Object> lis_datos_ejc = new ArrayList<Object>();
	private List lis_totales_consolidado=new ArrayList();
	
	private Division div_division = new Division();
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
	private DataTable tabla= new DataTable();
	private Grid gri=new Grid();
	
	///reporte
	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte self_reporte=new SeleccionFormatoReporte();
	private Map p_parametros=new HashMap();

	private Check che_detallado=new Check();
	private Check che_soloValores=new Check();
	private Check che_grupos=new Check();
	private boolean repDetallado=false;
	private boolean soloValores=false;
	private boolean grupos=false;

	
	@EJB
	private ServicioPresupuesto ser_presupuesto = (ServicioPresupuesto ) utilitario.instanciarEJB(ServicioPresupuesto.class);

	public pre_estado_ejecucion_presupuestaria() {        

		bar_botones.limpiar();
		
		///reporte
		rep_reporte.setId("rep_reporte"); //id
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");//ejecuta el metodo al aceptar reporte
		agregarComponente(rep_reporte);//agrega el componente a la pantalla
		bar_botones.agregarReporte();//aparece el boton de reportes en la barra de botones
		self_reporte.setId("self_reporte"); //id
		agregarComponente(self_reporte);
		
		bar_botones.agregarComponente(new Etiqueta("Fecha Inicio :"));
		cal_fecha_inicial.setValue(utilitario.obtenerFechaInicioAnio());
		bar_botones.agregarComponente(cal_fecha_inicial);
				
		bar_botones.agregarComponente(new Etiqueta("Fecha Corte :"));
		cal_fecha_final.setValue(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		bar_botones.agregarComponente(cal_fecha_final);
		
		che_detallado.setId("che_detallado");
		che_detallado.setMetodoChange("cambia");
		Etiqueta eti_detallado=new Etiqueta("Detallado");
		bar_botones.agregarComponente(eti_detallado);
		bar_botones.agregarComponente(che_detallado);
		
		che_grupos.setId("che_grupos");
		che_grupos.setMetodoChange("grupos");
		Etiqueta eti_grupos=new Etiqueta("Grupos");
		bar_botones.agregarComponente(eti_grupos);
		bar_botones.agregarComponente(che_grupos);
		
		che_soloValores.setId("che_soloValores");
		che_soloValores.setMetodoChange("soloValores");
		Etiqueta eti_soloValores=new Etiqueta("Solo Valores");
		bar_botones.agregarComponente(eti_soloValores);
		bar_botones.agregarComponente(che_soloValores);
		
		Boton bot_actualiza = new Boton();
		bot_actualiza.setId("bot_actualiza");
		bot_actualiza.setTitle("ACTUALIZAR");
		bot_actualiza.setValue("ACTUALIZAR");
		bot_actualiza.setMetodo("cargaEjecucion");
		bar_botones.agregarBoton(bot_actualiza);
		
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setTitle("Limpiar");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarBoton(bot_limpiar);
		
		cargarTabla(cal_fecha_inicial.getFecha(), cal_fecha_inicial.getFecha());
		div_division.setId("div_division");
		agregarComponente(div_division);

	}
	
	public void cargaEjecucion(){
		String fecha_inicial=cal_fecha_inicial.getFecha();
		String fecha_final=cal_fecha_final.getFecha();
		cargarTabla(fecha_inicial, fecha_final);
		
		utilitario.addUpdate("div_division");
	}

	
	public void limpiar(){
		cargarTabla(cal_fecha_inicial.getFecha(), cal_fecha_inicial.getFecha());
		utilitario.addUpdate("div_division");
	}

	public void cargarTabla(String fecha_inicial, String fecha_final){

		lis_datos_ejc.clear();
		lis_totales_consolidado.clear();
		llenarTabla(fecha_inicial, fecha_final);
		dibujarTabla(fecha_inicial, fecha_final);
	}
	
	//reporte
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}
	
	public void aceptarReporte(){

		Locale locale=new Locale("es","ES");
		
		if(rep_reporte.getReporteSelecionado().equals("Estado Ejecucion Presupuestaria"));{
			if (rep_reporte.isVisible()){
				p_parametros=new HashMap();	
				TablaGenerica tab_anio =utilitario.consultar("select ide_geani as cod, ide_geani from gen_anio where detalle_geani = cast(extract(year from DATE '"+cal_fecha_inicial.getFecha()+"' ) as varchar)");
				rep_reporte.cerrar();	
				p_parametros.put("titulo","ESTADO DE EJECUCION PRESUPUESTARIA");
				p_parametros.put("fecha_inicial1", cal_fecha_inicial.getFecha());
				p_parametros.put("fecha_final1", cal_fecha_final.getFecha());
				p_parametros.put("contador_general",  utilitario.getVariable("p_nombre_contador"));				
				p_parametros.put("coordinador_finaciero",  utilitario.getVariable("p_nombre_coordinador_fin"));
				p_parametros.put("gerente",  utilitario.getVariable("p_nombre_gerente"));				
				p_parametros.put("cargo_gerente",  utilitario.getVariable("p_cargo_gerente"));
				p_parametros.put("ide_geani", pckUtilidades.CConversion.CInt(tab_anio.getValor("ide_geani")));
				p_parametros.put("REPORT_LOCALE", locale);
	
				self_reporte.setSeleccionFormatoReporte(p_parametros,rep_reporte.getPath());
				self_reporte.dibujar();
			
			}
			else{
				utilitario.agregarMensajeInfo("No se puede continuar", "No ha Seleccionado Ningun Registro");
	
			}
		}
			
	}
	
	public void grupos()
	{
		if(che_grupos.getValue().toString().equalsIgnoreCase("true")){
			grupos=true;
		}
		else{
			grupos=false;
		}
	}
	
	public void soloValores()
	{
		if(che_soloValores.getValue().toString().equalsIgnoreCase("true")){
			soloValores=true;
		}
		else{
			soloValores=false;
		}
	}
	
	public void cambia()
	{
		if(che_detallado.getValue().toString().equalsIgnoreCase("true")){
			repDetallado=true;
		}
		else{
			repDetallado=false;
		}
	}
		

	public void llenarTabla(String fecha_inicial,String fecha_final){

			TablaGenerica tab_rep_global=utilitario.consultar("select ide_prcla,codigo_clasificador_prcla,descripcion_clasificador_prcla,tipo_prcla from pre_clasificador where nivel_prcla=1 order by 1;"); 
			lis_datos_ejc = new ArrayList<Object>();
			List<Object> lisq = new ArrayList<Object>();
			TablaGenerica tab_nivel2 = new TablaGenerica();
			Object [] obj_columnas=new Object[8]; 
			double [] totales=new double[5];
			double [] totales_consolidado=new double[5];
			double codificado=0;
			double devengado_contable=0;
			double devengado_pres=0;
			double diferencia=0;
			double diferencia_cont_pres=0;
			
			//System.out.println("sql rep_global "+tab_rep_global.getSql());

			if (tab_rep_global.getTotalFilas()>0){

				for (int i = 0; i < 5; i++) {
					totales_consolidado[i]=0;
				}

				for (int i = 0; i < tab_rep_global.getTotalFilas(); i++) {
					lisq = new ArrayList<Object>();
					
					if(repDetallado){
						tab_nivel2=utilitario.consultar(getSqlClasificadorDet(fecha_inicial,fecha_final,tab_rep_global.getValor(i, "tipo_prcla"),tab_rep_global.getValor(i, "ide_prcla")));
					}
					else
						tab_nivel2=utilitario.consultar(getSqlClasificador(fecha_inicial,fecha_final,tab_rep_global.getValor(i, "tipo_prcla"),tab_rep_global.getValor(i, "ide_prcla")));
					
					for (int x = 0; x < 4; x++) {
						totales[x]=0;
					}
					
					for (int j = 0; j < tab_nivel2.getTotalFilas(); j++) {
						
						codificado=pckUtilidades.CConversion.CDbl_2(tab_nivel2.getValor(j, "codificado"));
						devengado_contable=pckUtilidades.CConversion.CDbl_2(tab_nivel2.getValor(j, "devengado_contable"));
						devengado_pres=pckUtilidades.CConversion.CDbl_2(tab_nivel2.getValor(j, "devengado_pres"));
						diferencia=pckUtilidades.CConversion.CDbl_2(tab_nivel2.getValor(j, "diferencia"));
						diferencia_cont_pres=Math.abs(pckUtilidades.Utilitario.Redondear(devengado_contable-devengado_pres));
						
						obj_columnas=new Object[8];
						obj_columnas[0]=tab_nivel2.getValor(j, "grupo");
						obj_columnas[1]=tab_nivel2.getValor(j, "concepto");
						obj_columnas[2]=codificado;
						obj_columnas[3]=devengado_contable;
						obj_columnas[4]=devengado_pres;
						obj_columnas[5]=diferencia;
						obj_columnas[6] = diferencia_cont_pres;
						obj_columnas[7]=tab_nivel2.getValor(j, "cuenta_contable");
						
						totales[0]=totales[0]+codificado;
						totales[1]=totales[1]+devengado_contable;
						totales[2]=totales[2]+devengado_pres;
						totales[3]=totales[3]+diferencia;
						totales[4]=totales[4]+diferencia_cont_pres;
						
						//if(pckUtilidades.CConversion.CInt(tab_rep_global.getValor(i, "tipo_prcla"))==1){
							totales_consolidado[0]=totales_consolidado[0]+codificado;
							totales_consolidado[1]=totales_consolidado[1]+devengado_contable;
							totales_consolidado[2]=totales_consolidado[2]+devengado_pres;
							totales_consolidado[3]=totales_consolidado[3]+diferencia;
							totales_consolidado[4]=totales_consolidado[4]+diferencia_cont_pres;
						/*}
						else
						{
							totales_consolidado[0]=totales_consolidado[0]-pckUtilidades.CConversion.CDbl_2(tab_nivel2.getValor(j, "codificado"));
							totales_consolidado[1]=totales_consolidado[1]-pckUtilidades.CConversion.CDbl_2(tab_nivel2.getValor(j, "devengado_contable"));
							totales_consolidado[2]=totales_consolidado[2]-pckUtilidades.CConversion.CDbl_2(tab_nivel2.getValor(j, "devengado_pres"));
							totales_consolidado[3]=totales_consolidado[3]-pckUtilidades.CConversion.CDbl_2(tab_nivel2.getValor(j, "diferencia"));
						}*/
						lisq.add(obj_columnas);
					}
			
					Object [] obj=new Object[2+totales.length]; 
					obj[0]=tab_rep_global.getValor(i, "descripcion_clasificador_prcla");
					obj[1]=lisq;
					for (int j = 0; j < totales.length; j++) {
						BigDecimal big_valor=new BigDecimal(pckUtilidades.CConversion.CDbl_2(totales[j]+""));
						big_valor=big_valor.setScale(2, RoundingMode.HALF_UP);
						obj[j+2]=""+big_valor;
					}
					lis_datos_ejc.add(obj);
				}
				
				lis_totales_consolidado=new ArrayList();
				for (int j = 0; j < totales_consolidado.length; j++) {
					BigDecimal big_valor=new BigDecimal(pckUtilidades.CConversion.CDbl_2(totales_consolidado[j]+""));
					big_valor=big_valor.setScale(2, RoundingMode.HALF_UP);
					lis_totales_consolidado.add(""+big_valor);
				}

			}
	}

	
	public void dibujarTabla(String fecha_inicial,String fecha_final){

		tabla=new DataTable();
		tabla.setId("tabla");
		tabla.setResizableColumns(true);
		tabla.setStyle("font-size:13px");
		tabla.setVar("suc");
		tabla.setValueExpression("value", crearValueExpression("pre_index.clase.lis_datos_ejc"));

		ColumnGroup columnGroup=new ColumnGroup();
		columnGroup.setType("header");
		tabla.getChildren().add(columnGroup);

		Row r1=new Row();
		columnGroup.getChildren().add(r1);

		Column c1=new Column();
		c1.setHeaderText("GRUPOS");
		c1.setWidth(120);
		c1.setRowspan(3);
		r1.getChildren().add(c1);

		Column c2=new Column();
		c2.setHeaderText("CONCEPTO");
		c2.setWidth(60);
		c2.setRowspan(3);
		r1.getChildren().add(c2);

		Column c3=new Column();
		c3.setHeaderText("CODIFICADO");
		c3.setRowspan(3);
		c3.setWidth(200);
		c3.setResizable(true);
		r1.getChildren().add(c3);

		Column c4=new Column();
		c4.setHeaderText("DEVENGADO_CONT");
		c4.setWidth(100);
		c4.setRowspan(3);
		r1.getChildren().add(c4);

		Column c5=new Column();
		c5.setHeaderText("DEVENGADO_PRES");
		c5.setWidth(100);
		c5.setRowspan(3);
		r1.getChildren().add(c5);
		
		Column c51=new Column();
		c51.setHeaderText("DIFERENCIA");
		c51.setWidth(100);
		c51.setRowspan(3);
		r1.getChildren().add(c51);

		Column c511=new Column();
		c511.setHeaderText("DIF_CONT-PRES");
		c511.setWidth(100);
		c511.setRowspan(3);
		r1.getChildren().add(c511);
		
		Row r2=new Row();	
		columnGroup.getChildren().add(r2);
		Column c6=new Column();
		c6.setHeaderText("CUENTAS CONTABLES");
		r2.getChildren().add(c6);

		Row r3=new Row();	
		columnGroup.getChildren().add(r3);
		
		SubTable subtable= new SubTable();
		subtable.setVar("emp");
		subtable.setValueExpression("value", crearValueExpression("suc[1]"));
		tabla.getChildren().add(subtable);

		Etiqueta eti_sucursal=new Etiqueta();
		eti_sucursal.setValueExpression("value", "suc[0]");
		eti_sucursal.setStyle("font-size:14px");
		subtable.getFacets().put("header", eti_sucursal);


		for(int i=0;i<8;i++){
			Column c8=new Column();
			Etiqueta eti=new Etiqueta();

			eti.setValueExpression("value", "emp["+i+"]");
			if (i>1){
				c8.setStyle("text-align:center;font-size:11px;font-weight:bold");
			}else if (i==1){
				c8.setStyle("text-align:left;font-size:9px");
			}else {
				c8.setStyle("text-align:center;font-size:9px");
			}
			c8.setResizable(true);

			c8.getChildren().add(eti);
			subtable.getChildren().add(c8);
		}


		ColumnGroup columnGroupTotales=new ColumnGroup();
		columnGroupTotales.setType("footer");
		subtable.getChildren().add(columnGroupTotales);

		Row r4=new Row();
		columnGroupTotales.getChildren().add(r4);

		Column c9=new Column(); 
		c9.setColspan(2);
		c9.setFooterText("TOTAL: ");
		c9.setStyle("text-align:right;font-size:14px;padding-right:10px");
		r4.getChildren().add(c9);
		for (int i = 0; i < 5; i++) {
			Column c10=new Column();
			c10.setValueExpression("footerText", crearValueExpression("suc["+(i+2)+"]"));
			c10.setStyle("text-align:right;font-size:14px;padding-right:10px");			
			r4.getChildren().add(c10);
		}

		ColumnGroup columnGroupTotalesC=new ColumnGroup();
		columnGroupTotalesC.setType("footer");
		tabla.getChildren().add(columnGroupTotalesC);

		Row r5=new Row();
		columnGroupTotalesC.getChildren().add(r5);

		Column c11=new Column(); 
		c11.setColspan(2);
		c11.setFooterText("SUPERAVIT O DEFICIT PRESUPUESTARIO: ");
		c11.setStyle("text-align:right;font-size:14px;padding-right:10px");
		r5.getChildren().add(c11);

		for (int i = 0; i < 5; i++) {
			Column c12=new Column();
			c12.setValueExpression("footerText", crearValueExpression("pre_index.clase.lis_totales_consolidado["+i+"]"));				
			c12.setStyle("text-align:right;font-size:14px;padding-right:10px");
			r5.getChildren().add(c12);
		}


		tabla.setSelectionMode("single");

		Grid gri_cabecera=new Grid();
		gri_cabecera.setWidth("100%");

		Etiqueta eti_titulo=new Etiqueta();
		eti_titulo.setEstiloCabecera("font-size: 13px;color: black;font-weight: bold;text-align: center");
		eti_titulo.setValue("ESTADO DE EJECUCION PRESUPUESTARIA ");

		Etiqueta eti_periodo=new Etiqueta();
		eti_periodo.setEstiloCabecera("font-size: 13px;color: black;font-weight: bold;text-align: center");
		eti_periodo.setValue("Desde "+fecha_inicial+" Hasta "+fecha_final);

		gri_cabecera.getChildren().add(eti_titulo);
		gri_cabecera.getChildren().add(eti_periodo);

		gri=new Grid();
		gri.setWidth("100%");
		gri.setStyle("display:block;height:100%");
		gri.getChildren().add(tabla);
		gri.setHeader(gri_cabecera);

		div_division.getChildren().clear();
		div_division.dividir1(gri);

	}
	

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	public List<Object> getLis_datos_ejc() {
		return lis_datos_ejc;
	}

	public void setLis_datos_ejc(List<Object> lis_datos_ejc) {
		this.lis_datos_ejc = lis_datos_ejc;
	}

	public List getLis_totales_consolidado() {
		return lis_totales_consolidado;
	}

	public void setLis_totales_consolidado(List lis_totales_consolidado) {
		this.lis_totales_consolidado = lis_totales_consolidado;
	}

	public Calendario getCal_fecha_final() {
		return cal_fecha_final;
	}

	public void setCal_fecha_final(Calendario cal_fecha_final) {
		this.cal_fecha_final = cal_fecha_final;
	}

	public DataTable getTabla() {
		return tabla;
	}

	public void setTabla(DataTable tabla) {
		this.tabla = tabla;
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

	public Map getP_parametros() {
		return p_parametros;
	}

	public void setP_parametros(Map p_parametros) {
		this.p_parametros = p_parametros;
	}

	public Check getChe_detallado() {
		return che_detallado;
	}

	public void setChe_detallado(Check che_detallado) {
		this.che_detallado = che_detallado;
	}

	private ValueExpression crearValueExpression(String valueExpression) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		return facesContext.getApplication().getExpressionFactory().createValueExpression(
				facesContext.getELContext(), "#{" + valueExpression + "}", Object.class);
	}

	public String getSqlClasificador(String fecha_inicial,String fecha_final,String ingresoGasto, String ide_prcla)
	{
		String sql="select codigo,grupo,concepto,codificado,devengado_contable,devengado_pres,codificado-devengado_pres as diferencia,cuenta_contable from ( "
			+" select ide_prasp as codigo, codigo_clasificador_prcla as grupo, descripcion_clasificador_prcla as concepto,"
            //+" (case when clp.tipo_prcla=1 then coalesce(panui.codificado_anual,0) else coalesce(panug.codificado_anual,0) end) as codificado,"
            +" (case when clp.tipo_prcla=1 then coalesce(panui.codificado_anual,0) else coalesce(panug.codificado_poa,0) end) as codificado,"
            +" (case when asp.ide_gelua=1 then coalesce(dmov.debe,0) else coalesce(dmov.haber,0) end) as devengado_contable,"
            +" (case when clp.tipo_prcla=1 then coalesce(pmesi.devengado,0) else coalesce(pmesg.devengado,0) end) as devengado_pres, 0 as diferencia, cue_codigo_cocac as cuenta_contable"
            +" from pre_asociacion_presupuestaria asp"
            +" left join pre_clasificador clp on clp.ide_prcla=asp.ide_prcla"
            +" left join cont_catalogo_cuenta catc on catc.ide_cocac=asp.ide_cocac"
			+" left join (select ide_cocac, sum(debe_codem) as debe, sum(haber_codem) as haber from cont_detalle_movimiento det "
			+" 			  left join cont_movimiento mov on mov.ide_comov=det.ide_comov"
			+" 			  where mov_fecha_comov between '"+fecha_inicial+"' and '"+fecha_final+"' and mov.ide_cotia not in (2,4) "
			+"			  group by ide_cocac) dmov on dmov.ide_cocac=catc.ide_cocac "// --nivel 2
			+" left join (select pcl3.pre_ide_prcla, sum(devengado_prmen) as devengado from pre_mensual pmes"
			+"            left join pre_anual panio on panio.ide_pranu=pmes.ide_pranu and panio.ide_prcla is null"
			+" 	   		  left join pre_programa pprog on pprog.ide_prpro=panio.ide_prpro "
			+" 	   		  left join pre_clasificador pcl4 on pcl4.ide_prcla=pprog.ide_prcla"
			+" 	   		  left join pre_clasificador pcl3 on pcl3.ide_prcla=pcl4.pre_ide_prcla"
			+"			  where fecha_ejecucion_prmen between '"+fecha_inicial+"' and '"+fecha_final+"'"
			+"            group by pcl3.pre_ide_prcla) pmesg on pmesg.pre_ide_prcla=clp.ide_prcla "// --gastos
			+" left join (select pcl3.pre_ide_prcla, sum(devengado_prmen) as devengado from pre_mensual pmes"
			+"            left join pre_anual panio on panio.ide_pranu=pmes.ide_pranu"
			+" 	          left join pre_clasificador pcl4 on pcl4.ide_prcla=panio.ide_prcla"
			+" 	          left join pre_clasificador pcl3 on pcl3.ide_prcla=pcl4.pre_ide_prcla"
			+"			  where fecha_ejecucion_prmen between '"+fecha_inicial+"' and '"+fecha_final+"' and panio.ide_geani=(select ide_geani from gen_anio where detalle_geani = cast(extract(year from DATE '"+fecha_inicial+"' ) as varchar)) " 
			+"            group by pcl3.pre_ide_prcla) pmesi on pmesi.pre_ide_prcla=clp.ide_prcla "//  --ingresos
			
			
			/*+" left join (select pcl3.pre_ide_prcla, sum(valor_codificado_pranu) as codificado_anual"
            +"        from pre_anual panio"
            +"        left join pre_clasificador pcl4 on pcl4.ide_prcla=panio.ide_prcla"
            +"        left join pre_clasificador pcl3 on pcl3.ide_prcla=pcl4.pre_ide_prcla"
            +"        where panio.ide_geani = (select ide_geani from gen_anio where detalle_geani = cast(extract(year from DATE '"+fecha_inicial+"' ) as varchar)) "
            +"        group by pcl3.pre_ide_prcla) panui on panui.pre_ide_prcla=clp.ide_prcla"*///  --ingresos
            
			+" left join (select pcl3.pre_ide_prcla, sum(coalesce(valor_inicial_pranu,0)+coalesce(reforma,0)) as codificado_anual"
            +"        from pre_anual panio"
            +"        left join pre_clasificador pcl4 on pcl4.ide_prcla=panio.ide_prcla"
            +"        left join pre_clasificador pcl3 on pcl3.ide_prcla=pcl4.pre_ide_prcla"
            +"        left join (select ide_pranu, sum(coalesce(val_reforma_d_prrem,0) - coalesce(val_reforma_h_prrem,0)) as reforma from pre_reforma_mes "
            +"                   where fecha_reforma_prrem between '"+fecha_inicial+"' and '"+fecha_final+"' "
            +"                   group by ide_pranu "
            +"                  ) ref on ref.ide_pranu=panio.ide_pranu "
            +"        where panio.ide_geani = (select ide_geani from gen_anio where detalle_geani = cast(extract(year from DATE '"+fecha_inicial+"' ) as varchar)) "
            +"        group by pcl3.pre_ide_prcla) panui on panui.pre_ide_prcla=clp.ide_prcla" ///  --ingresos 2
            
//            +" left join (select pcl3.pre_ide_prcla, sum(valor_codificado_pranu) as codificado_anual"
//            +"          from pre_anual panio"
//            +"          left join pre_programa pprog on pprog.ide_prpro=panio.ide_prpro"
//            +"          left join pre_clasificador pcl4 on pcl4.ide_prcla=pprog.ide_prcla"
//            +"          left join pre_clasificador pcl3 on pcl3.ide_prcla=pcl4.pre_ide_prcla"
//            +"          group by pcl3.pre_ide_prcla) panug on panug.pre_ide_prcla=clp.ide_prcla"// --gastos
            
            //+" left join (select pcl3.pre_ide_prcla, sum(presupuesto_codificado_prpoa) as codificado_poa"
            +" left join (select pcl3.pre_ide_prcla, sum(presupuesto_inicial_prpoa + coalesce(reforma,0)) as codificado_poa"
            +"        from pre_poa poa"
            +"        left join pre_clasificador pcl4 on pcl4.ide_prcla=poa.ide_prcla"
            +"        left join pre_clasificador pcl3 on pcl3.ide_prcla=pcl4.pre_ide_prcla"
            +"        left join (select sum(valor_reformado_prprf) as reforma, ide_prpoa from pre_poa_reforma_fuente  rf   " 
			+"                  where fecha_prprf between '"+fecha_inicial+"' and  '"+fecha_final+"' group by ide_prpoa) rf on rf.ide_prpoa = poa.ide_prpoa  " 
            +"        where poa.ide_geani = (select ide_geani from gen_anio where detalle_geani = cast(extract(year from DATE '"+fecha_inicial+"' ) as varchar))  " 
            +"        group by pcl3.pre_ide_prcla) panug on panug.pre_ide_prcla=clp.ide_prcla" // --gastos por codificado poa
                    
			+" where clp.tipo_prcla= " +ingresoGasto //--parametro 0 egreso 1 ingreso"
			+" and asp.ide_gelua = (case when clp.tipo_prcla=0 then 2 else 1 end) "// --2 haber 1 debe
			+" and clp.pre_ide_prcla= "+ide_prcla //--parametro 695 gastos corrientes 1 ingresos corrientes
			+" and asp.ide_prmop=5"
			+" and clp.nivel_prcla=2 and asp.activo_prasp=true) a ";
		if(soloValores)
			sql+=" where (abs(codificado)+abs(devengado_contable)+abs(devengado_pres))>0 ";
		
		sql+= " order by grupo;";
		
		System.out.println("getSqlClasificador-Ejecucion "+sql);
		return sql;
	}

	public String getSqlClasificadorDet(String fecha_inicial,String fecha_final,String ingresoGasto, String ide_prcla)
	{
		String sql="select distinct grupo,concepto,codificado,devengado_contable,devengado_pres,codificado-devengado_pres as diferencia, cuenta_contable from ( ";
			if(!grupos){
				System.out.println("consulta getSqlClasificadorDet: !grupos ");
				
					sql+= "select asp.ide_prasp as codigo, pcl4.codigo_clasificador_prcla as grupo, pcl4.descripcion_clasificador_prcla as concepto, "
					//+ "(case when clp2.tipo_prcla=1 then coalesce(panui.codificado_anual,0) else coalesce(panug.codificado_anual,0) end) as codificado, "
					+ "(case when clp2.tipo_prcla=1 then coalesce(panui.codificado_anual,0) else coalesce(panug.codificado_poa,0) end) as codificado, "
					+" (case when asp.ide_gelua=1 then coalesce(dmov.debe,0) else coalesce(dmov.haber,0) end) as devengado_contable, "
					+" (case when clp2.tipo_prcla=1 then coalesce(pmesi.devengado,0) else coalesce(pmesg.devengado,0) end) as devengado_pres, "
					+" catc2.cue_codigo_cocac as cuenta_contable "
					+" from pre_asociacion_presupuestaria asp "
					+" left join pre_clasificador pcl4 on pcl4.ide_prcla=asp.ide_prcla "
					+" left join pre_clasificador pcl3 on pcl3.ide_prcla=pcl4.pre_ide_prcla"
					+" left join pre_clasificador clp2 on clp2.ide_prcla=pcl3.pre_ide_prcla "
					+" left join cont_catalogo_cuenta catc2 on catc2.ide_cocac=asp.ide_cocac"
					+" left join (select ide_cocac, sum(debe_codem) as debe, sum(haber_codem) as haber from cont_detalle_movimiento det "
					+" 			  left join cont_movimiento mov on mov.ide_comov=det.ide_comov"
					+" 			  where mov_fecha_comov between '"+fecha_inicial+"' and '"+fecha_final+"' and mov.ide_cotia not in (2,4) "
					+"			  group by ide_cocac ) dmov on dmov.ide_cocac=catc2.ide_cocac"// --nivel 2
					
					+" left join (select pprog.ide_prcla,ide_cocac, sum(devengado_prmen) as devengado from pre_mensual pmes"
					+"            left join pre_anual panio on panio.ide_pranu=pmes.ide_pranu and panio.ide_prcla is null"
					+" 			  left join pre_programa pprog on pprog.ide_prpro=panio.ide_prpro "
					+"		      left join cont_detalle_movimiento det on det.ide_codem=pmes.ide_codem "
					+"			  where ide_cocac is not null and pmes.ide_prfuf is not null and pmes.ide_comov is not null and pmes.ide_prtra is not null "
					+"            and pmes.ide_tecpo is not null and coalesce(pmes.activo_prmen,false)=true and pmes.fecha_ejecucion_prmen between '"+fecha_inicial+"' and '"+fecha_final+"'"
					+"            group by pprog.ide_prcla,ide_cocac) pmesg on pmesg.ide_prcla=pcl4.ide_prcla and pmesg.ide_cocac=catc2.ide_cocac"//  --gastos
					
					+" left join (select panio.ide_prcla,ide_cocac, sum(devengado_prmen) as devengado from pre_mensual pmes"
					+"            left join pre_anual panio on panio.ide_pranu=pmes.ide_pranu "
					+"			  left join cont_detalle_movimiento det on det.ide_codem=pmes.ide_codem "
					+"			  where ide_cocac is not null and pmes.ide_prfuf is not null and pmes.ide_comov is not null "
					+"            and coalesce(pmes.activo_prmen,false)=true and fecha_ejecucion_prmen between '"+fecha_inicial+"' and '"+fecha_final+"' and panio.ide_geani=(select ide_geani from gen_anio where detalle_geani = cast(extract(year from DATE '"+fecha_inicial+"' ) as varchar)) "
					+"            group by panio.ide_prcla,ide_cocac) pmesi on pmesi.ide_prcla=pcl4.ide_prcla and pmesi.ide_cocac=catc2.ide_cocac"//  --ingresos
					+" left join (select panio.ide_prcla, sum(valor_codificado_pranu) as codificado_anual"
	                +"              from pre_anual panio "
	                +"       where panio.ide_geani = (select ide_geani from gen_anio where detalle_geani = cast(extract(year from DATE '"+fecha_inicial+"' ) as varchar)) "
	                +" group by panio.ide_prcla) panui on panui.ide_prcla=asp.ide_prcla "
//					+" left join (select pprog.ide_prcla, sum(valor_codificado_pranu) as codificado_anual"
//					+"		from pre_anual panio"
//					+"		left join pre_programa pprog on pprog.ide_prpro=panio.ide_prpro"
//	                +"		group by pprog.ide_prcla) panug on panug.ide_prcla=asp.ide_prcla"

					//+" left join (select poa.ide_prcla, sum(presupuesto_codificado_prpoa) as codificado_poa"
					+" left join (select poa.ide_prcla, sum(presupuesto_inicial_prpoa + coalesce(reforma,0)) as codificado_poa"
					+"		from pre_poa poa "
					+"        left join (select sum(valor_reformado_prprf) as reforma, ide_prpoa from pre_poa_reforma_fuente  rf   " 
					+"                  where fecha_prprf between '"+fecha_inicial+"' and  '"+fecha_final+"' group by ide_prpoa) rf on rf.ide_prpoa = poa.ide_prpoa  " 
					+"      where poa.ide_geani = (select ide_geani from gen_anio where detalle_geani = cast(extract(year from DATE '"+fecha_inicial+"' ) as varchar))  " 
					+" group by poa.ide_prcla) panug on panug.ide_prcla=asp.ide_prcla"
	
				+" where clp2.tipo_prcla= " +ingresoGasto //--parametro 0 egreso 1 ingreso"
				//+" and asp.ide_gelua = (case when clp2.tipo_prcla=0 then 2 else 1 end) "// --2 haber 1 debe
				+" and clp2.pre_ide_prcla= "+ide_prcla //--parametro 695 gastos corrientes 1 ingresos corrientes
				+" and asp.ide_prmop=5"
				+" and pcl4.nivel_prcla=4 and asp.activo_prasp=true ";
			}
			else
			{
				sql+= "select distinct pcl4.codigo_clasificador_prcla as grupo, pcl4.descripcion_clasificador_prcla as concepto,  "
						+" 'N/A' as cuenta_contable ,"
						//+" (case when clp2.tipo_prcla=1 then coalesce(panui.codificado_anual,0) else coalesce(panug.codificado_anual,0) end) as codificado,  "
						+" (case when clp2.tipo_prcla=1 then coalesce(panui.codificado_anual,0) else coalesce(panug.codificado_poa,0) end) as codificado,  "
						+" sum((case when asp.ide_gelua=1 then coalesce(dmov.debe,0) else coalesce(dmov.haber,0) end)) as devengado_contable,  "
						+" sum((case when clp2.tipo_prcla=1 then coalesce(pmesi.devengado,0) else coalesce(pmesg.devengado,0) end)) as devengado_pres"
						
						+" from pre_asociacion_presupuestaria asp  "
						+" left join pre_clasificador pcl4 on pcl4.ide_prcla=asp.ide_prcla  "
						+" left join pre_clasificador pcl3 on pcl3.ide_prcla=pcl4.pre_ide_prcla "
						+" left join pre_clasificador clp2 on clp2.ide_prcla=pcl3.pre_ide_prcla  "
						+" left join cont_catalogo_cuenta catc2 on catc2.ide_cocac=asp.ide_cocac "
						+" left join (select ide_cocac, sum(debe_codem) as debe, sum(haber_codem) as haber from cont_detalle_movimiento det  "
						+"   left join cont_movimiento mov on mov.ide_comov=det.ide_comov "
						+"   where mov_fecha_comov between '" +fecha_inicial +"' and '" +fecha_final +"'  and mov.ide_cotia not in (2,4)  "
						+"  group by ide_cocac) dmov on dmov.ide_cocac=catc2.ide_cocac  "
						
						+" left join (select pprog.ide_prcla,ide_cocac, sum(devengado_prmen) as devengado from pre_mensual pmes "
						+"      left join pre_anual panio on panio.ide_pranu=pmes.ide_pranu and panio.ide_prcla is null "
						+"      left join pre_programa pprog on pprog.ide_prpro=panio.ide_prpro  "
						+"      left join cont_detalle_movimiento det on det.ide_codem=pmes.ide_codem  "
						+"      where ide_cocac is not null and fecha_ejecucion_prmen between '" +fecha_inicial +"' and '" +fecha_final +"'  "
						+"      group by pprog.ide_prcla,ide_cocac) pmesg on pmesg.ide_prcla=pcl4.ide_prcla and pmesg.ide_cocac=catc2.ide_cocac   "
						
						+" left join (select panio.ide_prcla,ide_cocac, sum(devengado_prmen) as devengado from pre_mensual pmes "
						+"      left join pre_anual panio on panio.ide_pranu=pmes.ide_pranu  "
						+"      left join cont_detalle_movimiento det on det.ide_codem=pmes.ide_codem  "
						+"     where ide_cocac is not null and fecha_ejecucion_prmen between '" +fecha_inicial +"' and '" +fecha_final +"' and panio.ide_geani=(select ide_geani from gen_anio where detalle_geani = cast(extract(year from DATE '"+fecha_inicial+"' ) as varchar)) "
						+"      group by panio.ide_prcla,ide_cocac) pmesi on pmesi.ide_prcla=pcl4.ide_prcla and pmesi.ide_cocac=catc2.ide_cocac  "
						
						+" left join (select panio.ide_prcla, sum(valor_codificado_pranu) as codificado_anual "
						+"      from pre_anual panio "
						+"      where panio.ide_geani = (select ide_geani from gen_anio where detalle_geani = cast(extract(year from DATE '"+fecha_inicial+"' ) as varchar)) "
						+" group by panio.ide_prcla) panui on panui.ide_prcla=asp.ide_prcla  "
						
//						+" left join (select pprog.ide_prcla, sum(valor_codificado_pranu) as codificado_anual "
//						+"     from pre_anual panio "
//						+"     left join pre_programa pprog on pprog.ide_prpro=panio.ide_prpro "
//						+"    group by pprog.ide_prcla) panug on panug.ide_prcla=asp.ide_prcla "
						
						//+" left join (select poa.ide_prcla, sum(presupuesto_codificado_prpoa) as codificado_poa"
						+" left join (select poa.ide_prcla, sum(presupuesto_inicial_prpoa + coalesce(reforma,0)) as codificado_poa"
						+"		from pre_poa poa "
						+"        left join (select sum(valor_reformado_prprf) as reforma, ide_prpoa from pre_poa_reforma_fuente  rf   " 
						+"                  where fecha_prprf between '"+fecha_inicial+"' and  '"+fecha_final+"' group by ide_prpoa) rf on rf.ide_prpoa = poa.ide_prpoa  " 
						+"      where poa.ide_geani = (select ide_geani from gen_anio where detalle_geani = cast(extract(year from DATE '"+fecha_inicial+"' ) as varchar))  " 
						+" group by poa.ide_prcla) panug on panug.ide_prcla=asp.ide_prcla"
					
						+" where clp2.tipo_prcla=" +ingresoGasto
						+"		and clp2.pre_ide_prcla="+ide_prcla 
						+"		and asp.ide_prmop=5 "
						+"		and pcl4.nivel_prcla=4 and asp.activo_prasp=true  "
						+"		group by grupo,concepto,codificado";
			}
			
			sql+=" ) a ";
			if(soloValores) 
				sql+=" where (abs(codificado)+abs(devengado_contable)+abs(devengado_pres))>0 ";
			
			sql+= " order by grupo;";
			
			System.out.println("consulta getSqlClasificadorDet: "+ sql);
		
		return sql;
	}

}
