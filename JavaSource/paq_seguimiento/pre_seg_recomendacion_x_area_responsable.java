/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_seguimiento;

import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

/**
 *
 * @author DELL-USER
 */

public class pre_seg_recomendacion_x_area_responsable extends Pantalla {
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();

    private Tabla tab_permisos_reporte = new Tabla();

    public pre_seg_recomendacion_x_area_responsable() {
		bar_botones.getBot_eliminar().setRendered(false);
		bar_botones.getBot_guardar().setRendered(false);
		bar_botones.getBot_insertar().setRendered(false);
		bar_botones.getBot_atras().setRendered(false);
		bar_botones.getBot_inicio().setRendered(false);
		bar_botones.getBot_siguiente().setRendered(false);
		bar_botones.getBot_fin().setRendered(false);

		Etiqueta eti_colaborador=new Etiqueta("Generar Historico:");
		bar_botones.agregarComponente(eti_colaborador);
		Boton bot_generar_historico=new Boton();
		bot_generar_historico.setValue("HISTORICO FECHA");
		//bot_aprobar_plan.setMetodo("reprogramarPlan");
		bot_generar_historico.setMetodo("registrarHistorico");
		bar_botones.agregarBoton(bot_generar_historico);
		
   	
		TablaGenerica tab_recomendaciones_eva = utilitario.consultar("SELECT ide_serec,ide_seeva "
    			+ "FROM seg_detalle_evaluacion  "
    			+ "where ide_seeva in(4) and ide_seesr not in(2)");
    	String ide_serec="",ide_serec_fal="";
		if (tab_recomendaciones_eva.getTotalFilas()>0) {
			for (int i = 0; i < tab_recomendaciones_eva.getTotalFilas(); i++) {
				if (tab_recomendaciones_eva.getTotalFilas()==1) {
					ide_serec=tab_recomendaciones_eva.getValor(i,"ide_serec");
				}else if((tab_recomendaciones_eva.getTotalFilas()-1)==i){
					ide_serec+=tab_recomendaciones_eva.getValor(i,"ide_serec");
				}else{
				ide_serec+=tab_recomendaciones_eva.getValor(i,"ide_serec")+",";
				}
			}
		}
		
		
    	TablaGenerica tab_recomendaciones_fal =utilitario.consultar("select ide_serec,ide_seinf from seg_recomendacion where ide_seinf in("+utilitario.getVariable("p_informe_seguimiento_recomendacion")+")");
    	if (tab_recomendaciones_fal.getTotalFilas()>0) {
			for (int i = 0; i < tab_recomendaciones_fal.getTotalFilas(); i++) {
				if (tab_recomendaciones_fal.getTotalFilas()==1) {
					ide_serec_fal=tab_recomendaciones_fal.getValor(i,"ide_serec");
				}else if((tab_recomendaciones_fal.getTotalFilas()-1)==i){
					ide_serec_fal+=tab_recomendaciones_fal.getValor(i,"ide_serec");
				}else{
					ide_serec_fal+=tab_recomendaciones_fal.getValor(i,"ide_serec")+",";
				}
			}
		}
    	
    	
    	
   	
    	tab_permisos_reporte.setId("tab_permisos_reporte");
    	
    	tab_permisos_reporte.setSql("select inf.numero_seinf,reco.numero_serec,reco.asunto_serec, emp.descripcion_seemp,emp.sigla_seemp,est.descripcion_seesp,plan.observacion_sepla   "
    			+ "from seg_plan_accion  plan  "
    			+ "left join seg_informe inf on inf.ide_seinf=plan.ide_seinf   "
    			+ "left join seg_recomendacion reco on reco.ide_serec=plan.ide_serec   "
    			+ "left join seg_asignacion asi on asi.ide_seinf=plan.ide_seinf and asi.ide_serec=reco.ide_serec  "
    			+ "left join seg_estado_plan_accion est on est.ide_seesp=plan.ide_seesp   "
    			+ "left join seg_empresa emp on emp.ide_seemp=plan.ide_seemp  "
    			+ "where plan.activo_sepla=true  AND plan.IDE_SEREC IN("+ide_serec+","+ide_serec_fal+") "
    			+ "group by plan.ide_sepla,inf.numero_seinf,reco.numero_serec, reco.asunto_serec, emp.descripcion_seemp  ,emp.sigla_seemp,est.descripcion_seesp,plan.observacion_sepla   "
    			+ "order by inf.numero_seinf asc,reco.numero_serec asc");
    	
    	
    	
    	
  /*  	tab_permisos_reporte.setSql("select inf.numero_seinf,reco.numero_serec,reco.asunto_serec, emp.descripcion_seemp,emp.sigla_seemp,est.descripcion_seesp,plan.observacion_sepla   "
    			+ "from seg_plan_accion  plan  "
    			+ "left join seg_informe inf on inf.ide_seinf=plan.ide_seinf   "
    			+ "left join seg_recomendacion reco on reco.ide_serec=plan.ide_serec   "
    			+ "left join seg_asignacion asi on asi.ide_seinf=plan.ide_seinf and asi.ide_serec=reco.ide_serec  "
    			+ "left join seg_estado_plan_accion est on est.ide_seesp=plan.ide_seesp   "
    			+ "left join seg_empresa emp on emp.ide_seemp=plan.ide_seemp  "
    			+ "where plan.activo_sepla=true  AND plan.IDE_SEREC IN(SELECT ide_serec "
    			+ "FROM seg_detalle_evaluacion  "
    			+ "where ide_seeva in(4) and ide_seesr not in(2) ) "
    			+ "group by plan.ide_sepla,inf.numero_seinf,reco.numero_serec, reco.asunto_serec, emp.descripcion_seemp  ,emp.sigla_seemp,est.descripcion_seesp,plan.observacion_sepla   "
    			+ "order by inf.numero_seinf asc,reco.numero_serec asc");
    	*/
    	
    	tab_permisos_reporte.setCampoPrimaria("numero_seinf");
    	
    	tab_permisos_reporte.getColumna("numero_seinf").setFiltroContenido();
    	tab_permisos_reporte.getColumna("numero_seinf").setLongitud(40);
    	tab_permisos_reporte.getColumna("numero_seinf").alinearCentro();
    	tab_permisos_reporte.getColumna("numero_seinf").setNombreVisual("NO. DE INFORME");
    	
    	
    	tab_permisos_reporte.getColumna("numero_serec").setFiltroContenido();
    	tab_permisos_reporte.getColumna("numero_serec").setLongitud(10);
    	tab_permisos_reporte.getColumna("numero_serec").alinearCentro();
    	tab_permisos_reporte.getColumna("numero_serec").setNombreVisual("NO.REC");
    	
    	   	
    	tab_permisos_reporte.getColumna("asunto_serec").setFiltroContenido();
    	tab_permisos_reporte.getColumna("asunto_serec").setLongitud(200);
    	tab_permisos_reporte.getColumna("asunto_serec").alinearCentro();
    	tab_permisos_reporte.getColumna("asunto_serec").setNombreVisual("ASUNTO RECOMENDACION");
    	
    	   	
    	tab_permisos_reporte.getColumna("descripcion_seemp").setFiltroContenido();
    	tab_permisos_reporte.getColumna("descripcion_seemp").setLongitud(100);
    	tab_permisos_reporte.getColumna("descripcion_seemp").alinearCentro();
    	tab_permisos_reporte.getColumna("descripcion_seemp").setNombreVisual("AREA RESPONSABLE");

    	tab_permisos_reporte.getColumna("sigla_seemp").setFiltroContenido();
    	tab_permisos_reporte.getColumna("sigla_seemp").setLongitud(10);
    	tab_permisos_reporte.getColumna("sigla_seemp").alinearCentro();
    	tab_permisos_reporte.getColumna("sigla_seemp").setNombreVisual("GERENCIA");
    	tab_permisos_reporte.getColumna("sigla_seemp").setVisible(false);;
    	
    	
    	tab_permisos_reporte.getColumna("descripcion_seesp").setFiltroContenido();
    	tab_permisos_reporte.getColumna("descripcion_seesp").setLongitud(80);
    	tab_permisos_reporte.getColumna("descripcion_seesp").alinearCentro();
    	tab_permisos_reporte.getColumna("descripcion_seesp").setNombreVisual("ESTADO");


    	tab_permisos_reporte.getColumna("observacion_sepla").setFiltroContenido();
    	tab_permisos_reporte.getColumna("observacion_sepla").setLongitud(100);
    	tab_permisos_reporte.getColumna("observacion_sepla").alinearCentro();
    	tab_permisos_reporte.getColumna("observacion_sepla").setNombreVisual("OBSERVACION");
    	
    	tab_permisos_reporte.setLectura(true);
    	tab_permisos_reporte.setNumeroTabla(1);				
    	tab_permisos_reporte.setRows(40);
    	tab_permisos_reporte.setCampoOrden("numero_seinf");
   		tab_permisos_reporte.dibujar();
		
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setMensajeWarn("RESUMEN_POR_AREA_RESPONSABLE");
		pat_panel.setPanelTabla(tab_permisos_reporte);
		
	
		Division div_division = new Division();		
		div_division.dividir1(pat_panel);
		agregarComponente(div_division);
		
    	

    }





	public Tabla getTab_permisos_reporte() {
		return tab_permisos_reporte;
	}





	public void setTab_permisos_reporte(Tabla tab_permisos_reporte) {
		this.tab_permisos_reporte = tab_permisos_reporte;
	}





	public void limpiar() {
		tab_permisos_reporte.limpiar();	
		utilitario.addUpdate("tab_permisos_reporte");// limpia y refresca el autocompletar
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

}
