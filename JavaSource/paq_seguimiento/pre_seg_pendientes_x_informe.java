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

public class pre_seg_pendientes_x_informe extends Pantalla {
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();

    private Tabla tab_permisos_reporte = new Tabla();

    public pre_seg_pendientes_x_informe() {
		bar_botones.getBot_eliminar().setRendered(false);
		bar_botones.getBot_guardar().setRendered(false);
		bar_botones.getBot_insertar().setRendered(false);
		

		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarBoton(bot_limpiar);
    	
    	tab_permisos_reporte.setId("tab_permisos_reporte");
    	
    	
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
    	
    	
    	
    	
    	/*tab_permisos_reporte.setSql("select inf.ide_seinf,inf.numero_seinf,count(rec.ide_serec) as cantidad  "
    			+ "from seg_plan_accion plan "
    			+ "left join seg_informe inf on inf.ide_seinf=plan.ide_seinf "
    			+ "left join seg_recomendacion rec on rec.ide_serec=plan.ide_serec "
    			+ "where activo_sepla=true "
    			+ "and plan.IDE_SEREC IN(SELECT ide_serec "
    			+ "FROM seg_detalle_evaluacion  "
    			+ "where ide_seeva in(4) and ide_seesr not in(2)) "
    			+ "group by inf.ide_seinf,inf.numero_seinf  "
    			+ "order by inf.ide_seinf, inf.fecha_inicio_seinf asc");*/
    	
    	
    	
    	tab_permisos_reporte.setSql("select inf.ide_seinf,inf.numero_seinf,count(rec.ide_serec) as cantidad  "
    			+ "from seg_plan_accion plan "
    			+ "left join seg_informe inf on inf.ide_seinf=plan.ide_seinf "
    			+ "left join seg_recomendacion rec on rec.ide_serec=plan.ide_serec "
    			+ "where activo_sepla=true "
    			+ "and plan.IDE_SEREC IN("+ide_serec+","+ide_serec_fal+") "
    			+ "group by inf.ide_seinf,inf.numero_seinf  "
    			+ "order by inf.ide_seinf, inf.fecha_inicio_seinf asc");

    	
    	tab_permisos_reporte.setCampoPrimaria("numero_seinf");
    	tab_permisos_reporte.getColumna("ide_seinf").setVisible(false);
    	//tab_permisos_reporte.getColumna("ide_seinf").alinearCentro();
    	
    	tab_permisos_reporte.getColumna("numero_seinf").setFiltroContenido();
    	tab_permisos_reporte.getColumna("numero_seinf").setLongitud(40);
    	tab_permisos_reporte.getColumna("numero_seinf").alinearCentro();
    	tab_permisos_reporte.getColumna("numero_seinf").setNombreVisual("INFORME");
    	
    	tab_permisos_reporte.getColumna("cantidad").setFiltroContenido();
    	tab_permisos_reporte.getColumna("cantidad").setLongitud(10);
    	tab_permisos_reporte.getColumna("cantidad").alinearCentro();
    	tab_permisos_reporte.getColumna("cantidad").setNombreVisual("CANTIDAD");
    	tab_permisos_reporte.setColumnaSuma("cantidad");

    	
    	tab_permisos_reporte.setLectura(true);
    	tab_permisos_reporte.setNumeroTabla(1);				
    	tab_permisos_reporte.setRows(40);
   		tab_permisos_reporte.dibujar();
		
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setMensajeWarn("NUMERO_DE_RECOMENDACIONES_PENDIENTES_POR_INFORME");
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
		//tab_permisos_reporte.limpiar();	
		//utilitario.addUpdate("tab_permisos_reporte");// limpia y refresca el autocompletar
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
