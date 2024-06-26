/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_indicador;

import javax.ejb.EJB;

import paq_contabilidad.ejb.ServicioContabilidad;
import paq_sistema.aplicacion.Pantalla;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;


public class pre_ind_balance_score extends Pantalla {
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	
    private Tabla tab_balance_score = new Tabla();
	private Combo com_anio=new Combo();
	private Combo com_area=new Combo();
	String area="",anio="";
    public pre_ind_balance_score() {
    	
   	
    	com_area.setCombo("select ide_geare,abreviatura_geare  || ' ' || detalle_geare  from gen_area where activo_geare=true");
    	com_area.setMetodo("seleccionaElArea");
    	com_area.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Area:"));
		bar_botones.agregarComponente(com_area);
    	
		
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Anio:"));
		bar_botones.agregarComponente(com_anio);
		
		
    	
		
		
    	tab_balance_score.setId("tab_balance_score");
    	tab_balance_score.setNumeroTabla(1);
    	
    	tab_balance_score.setTabla("ind_balance_score_card_parametro", "ide_inbap", 1);
    	tab_balance_score.getColumna("ide_inbap").setNombreVisual("CODIGO");
    	tab_balance_score.getColumna("ide_inbap").setOrden(1);
    	   	
    	tab_balance_score.getColumna("ide_inper").setCombo("SELECT ide_inper, detalle_inper  "
    			+ "FROM ind_perspectiva  "
    			+ "WHERE activo_inper=true ");
    	tab_balance_score.getColumna("ide_inper").setNombreVisual("PERSPECTIVA");
    	tab_balance_score.getColumna("ide_inper").setRequerida(true);
    	tab_balance_score.getColumna("ide_inper").setOrden(2);

    	
    	
    	tab_balance_score.getColumna("ide_geare").setCombo("SELECT ide_geare, detalle_geare  "
    			+ "FROM gen_area  "
    			+ "WHERE activo_geare=true ");
    	tab_balance_score.getColumna("ide_geare").setAutoCompletar(); 	

    	tab_balance_score.getColumna("ide_geare").setRequerida(true);
    	tab_balance_score.getColumna("ide_geare").setLectura(true);
    	tab_balance_score.getColumna("ide_geare").setVisible(false);

    	
    	
     	tab_balance_score.getColumna("ide_inobj").setCombo("SELECT ide_inobj, detalle_inobj  "
    			+ "FROM ind_objetivo  "
    			+ "WHERE activo_inobj=true ");
    	tab_balance_score.getColumna("ide_inobj").setNombreVisual("OBJETIVO");
    	tab_balance_score.getColumna("ide_inobj").setRequerida(true);
    	tab_balance_score.getColumna("ide_inobj").setOrden(3);

    	
     	
     	
     	tab_balance_score.getColumna("ide_inesa").setCombo("SELECT ide_inesa, detalle_inesa  "
    			+ "FROM ind_estrategias_actuales  "
    			+ "WHERE activo_inesa=true ");
    	tab_balance_score.getColumna("ide_inesa").setNombreVisual("ESTRATEGIAS ACTUALES");
    	tab_balance_score.getColumna("ide_inesa").setRequerida(true);
    	tab_balance_score.getColumna("ide_inesa").setOrden(4);

    
    	
       	tab_balance_score.getColumna("ide_gecaf").setCombo("SELECT ide_gecaf, detalle_gecaf  "
    			+ "FROM gen_cargo_funcional  "
    			+ "WHERE ide_gecaf>69 order by detalle_gecaf asc ");
    	tab_balance_score.getColumna("ide_gecaf").setNombreVisual("CARGO");
    	tab_balance_score.getColumna("ide_gecaf").setRequerida(true);
    	tab_balance_score.getColumna("ide_gecaf").setOrden(5);

    	
       	
    	tab_balance_score.getColumna("ide_inind").setCombo("SELECT ide_inind, detalle_inind  "
    			+ "FROM ind_indicador  "
    			+ "WHERE activo_inind=true ");
    	tab_balance_score.getColumna("ide_inind").setNombreVisual("INDICADORES");
    	tab_balance_score.getColumna("ide_inind").setRequerida(true);
    	tab_balance_score.getColumna("ide_inind").setOrden(6);

    	
    	   	
    	tab_balance_score.getColumna("ide_inmec").setCombo("SELECT ide_inmec, detalle_inmec  "
    			+ "FROM ind_metodo_calculo  "
    			+ "WHERE activo_inmec=true ");
    	
    	tab_balance_score.getColumna("ide_inmec").setNombreVisual("METODO DE CALCULO");
    	tab_balance_score.getColumna("ide_inmec").setRequerida(true);
    	tab_balance_score.getColumna("ide_inmec").setOrden(7);

    	

    	tab_balance_score.getColumna("ide_inobi").setCombo("SELECT ide_inobi, detalle_inobi  "
    			+ "FROM ind_objetivo_indicador  "
    			+ "WHERE activo_inobi=true ");
    	tab_balance_score.getColumna("ide_inobi").setNombreVisual("OBJETIVO DEL  INDICADOR");
    	tab_balance_score.getColumna("ide_inobi").setAutoCompletar();
    	tab_balance_score.getColumna("ide_inobi").setRequerida(true);
    	tab_balance_score.getColumna("ide_inobi").setOrden(8);

    	

    	tab_balance_score.getColumna("recursos_inbap").setNombreVisual("RECURSOS");
    	tab_balance_score.getColumna("recursos_inbap").setOrden(9);
    	
       	tab_balance_score.getColumna("meta_inbap").setNombreVisual("META");
    	tab_balance_score.getColumna("meta_inbap").setOrden(10);
     	

    	tab_balance_score.getColumna("ide_infre").setCombo("SELECT ide_infre, detalle_infre  "
    			+ "FROM ind_frecuencia  "
    			+ "WHERE activo_infre=true ");
    	
      	tab_balance_score.getColumna("ide_infre").setNombreVisual("FRECUENCIA");
    	tab_balance_score.getColumna("ide_infre").setRequerida(true);
    	tab_balance_score.getColumna("ide_infre").setOrden(11);

     	
    	tab_balance_score.getColumna("ide_inmed").setCombo("SELECT ide_inmed, detalle_inmed  "
    			+ "FROM ind_medida  "
    			+ "WHERE activo_inmed=true ");
    	
      	tab_balance_score.getColumna("ide_inmed").setNombreVisual("MEDIDA");
    	tab_balance_score.getColumna("ide_inmed").setRequerida(true);
    	tab_balance_score.getColumna("ide_inmed").setOrden(12);

    	  	
    	tab_balance_score.getColumna("ide_geani").setCombo("SELECT ide_geani, detalle_geani  "
    			+ "FROM gen_anio  ");
    	tab_balance_score.getColumna("ide_geani").setAutoCompletar();
    	tab_balance_score.getColumna("ide_geani").setLectura(true);
    	tab_balance_score.getColumna("ide_geani").setNombreVisual("ANIO");
    	tab_balance_score.getColumna("ide_geani").setOrden(13);

    	

    	tab_balance_score.getColumna("ind_inrel").setCombo("SELECT ind_inrel, detalle_inrel  "
    			+ "FROM ind_relevancia  "
    			+ "WHERE activo_inrel=true ");
    	
      	tab_balance_score.getColumna("ind_inrel").setNombreVisual("RELEVANCIA");
    	tab_balance_score.getColumna("ind_inrel").setRequerida(true);
    	tab_balance_score.getColumna("ind_inrel").setOrden(14);

    	
    	tab_balance_score.getColumna("activo_inbap").setCheck();
    	tab_balance_score.getColumna("activo_inbap").setValorDefecto("TRUE");
    	tab_balance_score.getColumna("activo_inbap").setNombreVisual("ACTIVO");
    	tab_balance_score.getColumna("activo_inbap").setOrden(15);

    	tab_balance_score.getColumna("planificado_inbap").setNombreVisual("REFERENCIA");
    	tab_balance_score.getColumna("planificado_inbap").setOrden(16);
    	tab_balance_score.getColumna("planificado_inbap").setVisible(false);
    	
    	
    	tab_balance_score.getColumna("ind_inrec").setCombo("SELECT ind_inrec, detalle_inrec  "
    			+ "FROM ind_recursos  "
    			+ "WHERE activo_inrec=true ");
    	
      	tab_balance_score.getColumna("ind_inrec").setNombreVisual("RECURSOS");
    	tab_balance_score.getColumna("ind_inrec").setRequerida(false);
    	tab_balance_score.getColumna("ind_inrec").setOrden(17);
    	tab_balance_score.getColumna("ind_inrec").setVisible(false);

    
    	tab_balance_score.getColumna("ind_indes").setCombo("SELECT ind_indes, detalle_indes  "
    			+ "FROM ind_desarrolla  "
    			+ "WHERE activo_indes=true ");
    	
      	tab_balance_score.getColumna("ind_indes").setNombreVisual("DESARROLLA");
    	tab_balance_score.getColumna("ind_indes").setRequerida(false);
    	tab_balance_score.getColumna("ind_indes").setVisible(false);
    	tab_balance_score.getColumna("ind_indes").setOrden(18);
    	


    	tab_balance_score.setCondicion("ide_geani=-1");
    	tab_balance_score.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_balance_score);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
    }

    @Override
    public void insertar() {
    	if(com_anio.getValue()==null || com_area.getValue()==null){
			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar parametros validos");
	    	tab_balance_score.setCondicion("ide_geani=-1");
			return;
			
			}else {
		    	tab_balance_score.insertar();
		    	tab_balance_score.setValor("ide_geani",""+com_anio.getValue());
		    	tab_balance_score.setValor("ide_geare",""+com_area.getValue());
			}
    	
    	
    }

    @Override
    public void guardar() {
        if (tab_balance_score.guardar()) {
            guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
    	tab_balance_score.eliminar();
    }



	public void seleccionaElAnio (){
		if(com_anio.getValue()!=null){
			anio=com_anio.getValue().toString();
			if(com_area.getValue()!=null){
				tab_balance_score.setCondicion("IDE_GEANI="+com_anio.getValue()+" and ide_geare="+com_area.getValue());
				tab_balance_score.ejecutarSql();
			}else {
				utilitario.agregarMensaje("Debe seleccionar parametros validos", "Escoga area ");
		    	tab_balance_score.setCondicion("ide_geani=-1");
				return;		
			}
			
			
	
		}else{
			utilitario.agregarMensaje("Debe seleccionar parametros validos", "Escoga anio ");
	    	tab_balance_score.setCondicion("ide_geani=-1");
			return;			
		}
			
			
	}
	
	
	
	public void seleccionaElArea (){
		if(com_area.getValue()!=null){
			area=com_area.getValue().toString();
			if(com_anio.getValue()!=null){
				tab_balance_score.setCondicion("IDE_GEANI="+com_anio.getValue()+" and ide_geare="+com_area.getValue());
				tab_balance_score.ejecutarSql();
			}else {
				utilitario.agregarMensaje("Debe seleccionar parametros validos", "Escoga anio ");
		    	tab_balance_score.setCondicion("ide_geani=-1");
				return;		
			}
	
		}else{
			utilitario.agregarMensaje("Debe seleccionar parametros validos", "Escoga area ");
	    	tab_balance_score.setCondicion("ide_geani=-1");
			return;			
		}
			
			
	}

	public Tabla getTab_balance_score() {
		return tab_balance_score;
	}

	public void setTab_balance_score(Tabla tab_balance_score) {
		this.tab_balance_score = tab_balance_score;
	}

	
	
	
}
