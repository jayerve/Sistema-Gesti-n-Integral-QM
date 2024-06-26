package paq_contabilidad;

import javax.ejb.EJB;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Arbol;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_sistema.aplicacion.Pantalla;

public class pre_catalogo_cuenta extends Pantalla{
	
	
	private Tabla tab_tipo_catalogo_cuenta=new Tabla();
	private Tabla tab_vigente=new Tabla();
	private Tabla tab_asociacion_presupuestaria=new Tabla();
	private Division div_division = new Division();
	private Division div_division2 = new Division();
	private Division div_vigente= new Division();

	 	
	private Arbol arb_catalogo_cuenta=new Arbol();
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
		
	public pre_catalogo_cuenta(){
		
					        
		tab_tipo_catalogo_cuenta.setId("tab_tipo_catalogo_cuenta");
		tab_tipo_catalogo_cuenta.setHeader("CATALOGO CUENTAS");
		tab_tipo_catalogo_cuenta.setTipoFormulario(true);  //formulario 
		tab_tipo_catalogo_cuenta.getGrid().setColumns(4); //hacer  columnas 
		tab_tipo_catalogo_cuenta.setTabla("cont_catalogo_cuenta", "ide_cocac", 1);		
	  	tab_tipo_catalogo_cuenta.getColumna("con_ide_cocac2").setCombo("select ide_cocac,cue_codigo_cocac,cue_descripcion_cocac from cont_catalogo_cuenta order by cue_codigo_cocac ");
	  	tab_tipo_catalogo_cuenta.getColumna("con_ide_cocac2").setAutoCompletar();
	  	tab_tipo_catalogo_cuenta.getColumna("ide_cogrc").setCombo("cont_grupo_cuenta","ide_cogrc","detalle_cogrc", "");
	  	tab_tipo_catalogo_cuenta.getColumna("ide_botip").setCombo("bodt_tipo_producto", "ide_botip", "detalle_botip", "");
	  	tab_tipo_catalogo_cuenta.getColumna("nivel_cocac").setCombo(utilitario.getListaGruposNivelCuenta());
	  	tab_tipo_catalogo_cuenta.getColumna("grupo_nivel_cocac").setVisible(false);

		tab_tipo_catalogo_cuenta.getColumna("apertura_cierre_cocac").setRadio(utilitario.getListaAperturaCierre(), "0");
		tab_tipo_catalogo_cuenta.getColumna("apertura_cierre_cocac").setRadioVertical(false);
		tab_tipo_catalogo_cuenta.agregarRelacion(tab_vigente);
		tab_tipo_catalogo_cuenta.setCampoPadre("con_ide_cocac"); //necesarios para el arbol ide recursivo
		tab_tipo_catalogo_cuenta.setCampoNombre("(select coalesce(cue_codigo_cocac,'')||' '|| coalesce(cue_descripcion_cocac,'') as cue_descripcion_cocac from cont_catalogo_cuenta b where b.ide_cocac=cont_catalogo_cuenta.ide_cocac)"); //necesarios para el arbol campo a mostrarse
		tab_tipo_catalogo_cuenta.agregarArbol(arb_catalogo_cuenta);//necesarios para el arbol
		
		// para contruir los radios
		tab_tipo_catalogo_cuenta.getColumna("saldo_cocac").setCombo(utilitario.getListaTipoSaldo());

		tab_tipo_catalogo_cuenta.dibujar();
		PanelTabla pat_tipo_catalogo_cuenta = new PanelTabla();
		pat_tipo_catalogo_cuenta.setPanelTabla(tab_tipo_catalogo_cuenta);
		
		//tabla de años vigentes
		tab_vigente.setId("tab_vigente");
		tab_vigente.setHeader("AÑO VIGENTE");
		tab_vigente.setTabla("cont_vigente", "ide_covig", 2);
		tab_vigente.getColumna("ide_geani").setCombo("gen_anio","ide_geani","detalle_geani","");
		//tab_vigente.getColumna("ide_geani").setUnico(true);
		// ocultar campos de las claves  foraneas
		TablaGenerica  tab_generica=ser_contabilidad.getTablaVigente("cont_vigente");
		for(int i=0;i<tab_generica.getTotalFilas();i++){
			//muestra los ides q quiere mostras.
			if(!tab_generica.getValor(i, "column_name").equals("ide_geani")){	
				tab_vigente.getColumna(tab_generica.getValor(i, "column_name")).setVisible(false);	
			}				
		
   		}
	    						
		tab_vigente.dibujar();
		PanelTabla pat_vigente = new PanelTabla();
	    pat_vigente.setPanelTabla(tab_vigente); 
			 	  	
		//arbol				
		div_vigente =new Division();
		div_vigente.setId("div_vigente");
		div_vigente.dividir2(pat_tipo_catalogo_cuenta,pat_vigente,"50%", "h");
		agregarComponente(div_vigente);
		
		arb_catalogo_cuenta.setId("arb_catalogo_cuenta");
		arb_catalogo_cuenta.dibujar();
		div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(arb_catalogo_cuenta,div_vigente,"25%","v");
		agregarComponente(div_division);
		
		Boton bot_excel=new Boton();
	    bot_excel.setValue("Exportar EXCEL");
	    bot_excel.setIcon("ui-icon-calculator");
	    bot_excel.setAjax(false);
	    bot_excel.setMetodo("exportarExcel");
	    bar_botones.agregarBoton(bot_excel);
       		
	}
	
	public void exportarExcel()
	{
	      Tabla tab_tablaXls = new Tabla();
	      tab_tablaXls.setSql("SELECT ide_cocac, con_ide_cocac, cue_codigo_cocac, cue_descripcion_cocac, sigef_cocac, "
							       +" case when nivel_cocac = 1 then 'TITULO' "
							       +"      when nivel_cocac = 2 then 'GRUPO' "
							       +" when nivel_cocac = 3 then 'SUBGRUPO' "
							       +" when nivel_cocac = 4 then 'CUENTA NIVEL 1' "
							       +" when nivel_cocac = 5 then 'CUENTA NIVEL 2' "
							       +" when nivel_cocac = 6 then 'CUENTA NIVEL 3' "
							       +" when nivel_cocac = 7 then 'CUENTA NIVEL 4' "
							       +" when nivel_cocac = 8 then 'CUENTA NIVEL 5' "
							       +" end as Nivel, "
							
							+" detalle_cogrc, detalle_botip, "
							+" case when saldo_cocac = 1 then 'SALDO DEUDOR' "
							+" when saldo_cocac = 2 then 'SALDO ACREEDOR' "
							+" when saldo_cocac = 3 then 'SALDO DEUDOR - ACREEDOR'	 "
							+" end as tipo_saldo, "
							+" case when apertura_cierre_cocac = 0 then 'N/A' "
							+" when saldo_cocac = 1 then 'APERTURA' "
							+" when saldo_cocac = 2 then 'CIERRE'	 "
							+" when saldo_cocac = 3 then 'APERTURA/CIERRE'	 "
							+"  end as apertura_cierre, "
							+" activo_cocac  "
							+" FROM cont_catalogo_cuenta cc "
							+" left join cont_grupo_cuenta gc on gc.ide_cogrc=cc.ide_cogrc "
							+" left join bodt_tipo_producto tp on tp.ide_botip=cc.ide_botip "
							+" order by cue_codigo_cocac");
	      tab_tablaXls.ejecutarSql();
	      tab_tablaXls.exportarXLS();
    }
	
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().insertar();

		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if(tab_tipo_catalogo_cuenta.guardar()){
           if(tab_vigente.guardar()){
        	   guardarPantalla();
            //actualiza el arbol
            arb_catalogo_cuenta.ejecutarSql();
            utilitario.addUpdate("arb_arbol");
			   }
           }
    				
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
		
		
	}


	public Tabla getTab_vigente() {
		return tab_vigente;
	}
	public void setTab_vigente(Tabla tab_vigente) {
		this.tab_vigente = tab_vigente;
	}
	public Tabla getTab_tipo_catalogo_cuenta() {
		return tab_tipo_catalogo_cuenta;
	}

	public void setTab_tipo_catalogo_cuenta(Tabla tab_tipo_catalogo_cuenta) {
		this.tab_tipo_catalogo_cuenta = tab_tipo_catalogo_cuenta;
	}

	public Arbol getArb_catalogo_cuenta() {
		return arb_catalogo_cuenta;
	}
	public void setArb_catalogo_cuenta(Arbol arb_catalogo_cuenta) {
		this.arb_catalogo_cuenta = arb_catalogo_cuenta;
	}

}
