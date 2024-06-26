/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_biometrico;

import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.DateSelectEvent;

import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;

/**
 *
 * @author DELL-USER
 */
public class pre_novedad extends Pantalla {

    private Tabla tab_novedad = new Tabla();
    private Combo com_tipo_hora= new Combo();
	private Texto tex_turno;

    public pre_novedad() {


    	
    	bar_botones.agregarComponente(new Etiqueta("INGRESO DE FERIADOS/NOVEDADES"));
    	
    	tab_novedad.setId("tab_novedad");
    	tab_novedad.setTabla("ASI_NOVEDAD", "IDE_ASNOV", 1);
    	tab_novedad.getColumna("IDE_ASNOV").setNombreVisual("CÓDIGO");
    	tab_novedad.getColumna("IDE_ASNOV").setOrden(1);
    	tab_novedad.getColumna("IDE_ASNOV").alinearCentro();
    	tab_novedad.getColumna("IDE_ASNOV").setLectura(false);


    	tab_novedad.getColumna("IDE_USUA").setValorDefecto(utilitario.getVariable("IDE_USUA"));
    	tab_novedad.getColumna("IDE_USUA").setCombo("SIS_USUARIO","IDE_USUA","NOM_USUA","");
    	tab_novedad.getColumna("IDE_USUA").setAutoCompletar();
    	tab_novedad.getColumna("IDE_USUA").setLectura(true);
    	tab_novedad.getColumna("IDE_USUA").setOrden(2);
    	tab_novedad.getColumna("IDE_USUA").alinearCentro();
    	tab_novedad.getColumna("IDE_USUA").setNombreVisual("USUARIO");
    	
    	
    	tab_novedad.getColumna("FECHA_ASNOV").setValorDefecto(utilitario.getFechaActual());
    	tab_novedad.getColumna("FECHA_ASNOV").setNombreVisual("FECHA_NOVEDAD");
    	tab_novedad.getColumna("FECHA_ASNOV").setLectura(true);
    	tab_novedad.getColumna("FECHA_ASNOV").setOrden(3);
    	tab_novedad.getColumna("FECHA_ASNOV").alinearCentro();
    	tab_novedad.getColumna("FECHA_ASNOV").setVisible(false);



    	tab_novedad.getColumna("FECHA_INICIO_ASNOV").setOrden(4);

    	tab_novedad.getColumna("FECHA_INICIO_ASNOV").setNombreVisual("FECHA INICIO IMPORTA");


    	tab_novedad.getColumna("FECHA_FIN_ASNOV").setOrden(5);

    	tab_novedad.getColumna("FECHA_FIN_ASNOV").setNombreVisual("FECHA FIN IMPORTA");

    	
    	tab_novedad.getColumna("OBSERVACION_ASNOV").setOrden(6);
    	tab_novedad.getColumna("OBSERVACION_ASNOV").setNombreVisual("OBSERVACIÓN");

    	tab_novedad.getColumna("ACTIVO_ASNOV").setCheck();
    	tab_novedad.getColumna("ACTIVO_ASNOV").setNombreVisual("ACTIVO");
    	tab_novedad.getColumna("ACTIVO_ASNOV").setOrden(8);
    	tab_novedad.getColumna("ACTIVO_ASNOV").setLectura(true);
    	
    	tab_novedad.getColumna("importacion_asnov").setCheck();
    	tab_novedad.getColumna("importacion_asnov").setNombreVisual("IMPORTACIÓN");
    	tab_novedad.getColumna("importacion_asnov").setOrden(10);
    	tab_novedad.getColumna("importacion_asnov").setValorDefecto(""+false);
    	tab_novedad.getColumna("importacion_asnov").setVisible(false);
    	
    	
     	tab_novedad.getColumna("dia_feriado_asnov").setNombreVisual("DIA_FESTIVO");
    	tab_novedad.getColumna("dia_feriado_asnov").setOrden(9);
     	tab_novedad.getColumna("dia_feriado_asnov").setLectura(true);

    	


    	tab_novedad.getColumna("activo_asnov").setVisible(false);

    	tab_novedad.getColumna("cambio_asnov").setNombreVisual("C.TURNO");
     	tab_novedad.getColumna("cambio_asnov").setLectura(true);
     	tab_novedad.getColumna("cambio_asnov").setVisible(false);

    	//tab_novedad.getColumna("cambio_asnov").setLectura(true);

    	tab_novedad.getColumna("ide_astur").setCombo("SELECT IDE_ASTUR,NOM_ASTUR,DESCRIPCION_ASTUR FROM ASI_TURNOS WHERE "
    			+ "turno_matriz_astur=false  ORDER BY NOM_ASTUR DESC");
    	tab_novedad.getColumna("ide_astur").setNombreVisual("TURNO");
    	tab_novedad.getColumna("ide_astur").setOrden(7);
    	tab_novedad.getColumna("ide_astur").setVisible(false);
    	
    	tab_novedad.getColumna("dia_feriado_asnov").setValorDefecto("true");
    	tab_novedad.getColumna("dia_feriado_asnov").setVisible(false);
    	
    	tab_novedad.getColumna("importacion_asnov").setValorDefecto("false");
    	tab_novedad.getColumna("importacion_asnov").setVisible(false);

    	tab_novedad.getColumna("cambio_asnov").setValorDefecto("false");
    	tab_novedad.setHeader("REGISTRO DIAS FESTIVOS Y DISPOSICIONES");
    	//tab_novedad.setCondicion("importacion_asnov!=true and importacion_asnov!=false");
    	tab_novedad.setCondicion("dia_feriado_asnov=true");
    	
    	tab_novedad.setRows(5);
    	tab_novedad.dibujar();
    	PanelTabla pat_panel = new PanelTabla();
    	pat_panel.setPanelTabla(tab_novedad);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
        
  
        
		

    }

    @Override
    public void insertar() {
    	
    //	TablaGenerica tab_novedadImportar = utilitario.consultar("select IDE_ASNOV,FECHA_INICIO_ASNOV,FECHA_FIN_ASNOV from asi_novedad and dia_feriado_asnov=true "
    //			+ "AND FECHA_INICIO_ASNOV="+tab_novedad.getValor("FECHA_INICIO_ASNOV"));
    	
    	//if (tab_novedadImportar.getTotalFilas()>0) {
       

          
      		  tab_novedad.insertar();


    }

    @Override
    public void guardar() {
    	
  //  	tab_novedad.setValor("FECHA_FIN_ASNOV",tab_novedad.getValor("FECHA_INICIO_ASNOV"));

    
          
    	
    		/*TablaGenerica tab_novedadImportar = utilitario.consultar("select IDE_ASNOV,FECHA_INICIO_ASNOV,FECHA_FIN_ASNOV from asi_novedad where dia_feriado_asnov=true "
        			+ "AND FECHA_INICIO_ASNOV='"+tab_novedad.getValor("FECHA_INICIO_ASNOV")+"' ");
        	
        	if (tab_novedadImportar.getTotalFilas()>0) {
        		utilitario.agregarMensajeError("Fecha ya se encuentra ingresada ", "Registre una nueva fecha");
        		return;
        	}else {*/
        		  if (tab_novedad.guardar()) {
	            guardarPantalla();
			}
         }

    @Override
    public void eliminar() {
        tab_novedad.eliminar();
    }

	public Tabla getTab_novedad() {
		return tab_novedad;
	}

	public void setTab_novedad(Tabla tab_novedad) {
		this.tab_novedad = tab_novedad;
	}

	
	
	
	
		
	
	
	 public void cambioTipoHora(){
	    	//if(com_tipo_hora.getValue()!=null){
		    	if(com_tipo_hora.getValue().toString().equals("true")){
		    		tab_novedad.limpiar();
				   tab_novedad.setCondicion("cambio_asnov=true");
	    		   	}
	    	else{
	    		tab_novedad.limpiar();
	    	    tab_novedad.setCondicion("dia_feriado_asnov=true");   

	   }//else {
		
//	}
	    	
	    	  	tab_novedad.ejecutarSql();   
	    	  	tab_novedad.actualizar();
	    		utilitario.addUpdate("tab_novedad,com_tipo_hora");
	    	
}
	 
	 
	 
	 private void cambiarEstadoNick(){
			if(tab_novedad.isFilaInsertada()){
				//si la fila es insertada activo el cuadro de texto
				tex_turno.setDisabled(false);
			}
			else{
				tex_turno.setDisabled(true);
			}
		}

	@Override
	public void inicio() {
		// TODO Auto-generated method stub
		super.inicio();
	}

	@Override
	public void fin() {
		// TODO Auto-generated method stub
		super.fin();
	}

	@Override
	public void siguiente() {
		// TODO Auto-generated method stub
		super.siguiente();
	}

	@Override
	public void atras() {
		// TODO Auto-generated method stub
		super.atras();
	}

	@Override
	public void buscar() {
		// TODO Auto-generated method stub
		super.buscar();
	}

	

	
	 
}