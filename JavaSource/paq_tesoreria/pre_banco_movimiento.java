/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_tesoreria;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.SelectEvent;

import paq_sistema.aplicacion.Pantalla;
import paq_tesoreria.ejb.ServicioTesoreria;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;

public class pre_banco_movimiento extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private AutoCompletar aut_movimniento =new AutoCompletar();
    private SeleccionTabla seltab_valida_contable = new SeleccionTabla();
    private SeleccionTabla seltab_valida_transito = new SeleccionTabla(); 
    private SeleccionTabla seltab_consulta_contable = new SeleccionTabla();
    private SeleccionTabla seltab_consulta_transito = new SeleccionTabla(); 
    
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
	
	public static String par_cuentas_contables_conciliacion;
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();
	private Map map_parametros = new HashMap();
	
    @EJB
    private ServicioTesoreria ser_Tesoreria = (ServicioTesoreria) utilitario.instanciarEJB(ServicioTesoreria.class);

    public pre_banco_movimiento() {
    	par_cuentas_contables_conciliacion=utilitario.getVariable("p_cuentas_conciliar");

		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);
		
		bar_botones.agregarReporte();
		
		sel_rep.setId("sel_rep");
		agregarComponente(sel_rep);    	
    	
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
    	aut_movimniento.setId("aut_movimniento");
    	aut_movimniento.setStyle("width: 200px;");
    	aut_movimniento.setAutoCompletar("select a.ide_tebac,a.nro_cuenta_tebac,b.detalle_teban,detalle_gttcb  " +
    			"from tes_banco_cuenta a " +
    			"left join tes_banco b on b.ide_teban=a.ide_teban " +
    			"left join gth_tipo_cuenta_bancaria c on c.ide_gttcb=a.ide_gttcb " +
    			"order by a.ide_tebac desc");
    	aut_movimniento.setMetodoChange("Carga");
    	
    	bar_botones.agregarComponente(new Etiqueta("Seleccione: "));
    	bar_botones.agregarComponente(aut_movimniento);
    	bar_botones.agregarBoton(bot_limpiar);
    	
    	Boton bot_valida_transito = new Boton();
    	bot_valida_transito.setId("bot_valida_transito");
    	bot_valida_transito.setValue("VERIFICACION TRANSITO");
    	bot_valida_transito.setMetodo("aceptarTransito");
    	bar_botones.agregarBoton(bot_valida_transito);
    	
    	Boton bot_consulta_transito = new Boton();
    	bot_consulta_transito.setId("bot_consulta_transito");
    	bot_consulta_transito.setValue("CONSULTA TRANSITO");
    	bot_consulta_transito.setMetodo("aceptarConsultaTransito");
    	bar_botones.agregarBoton(bot_consulta_transito);
    	
    	Boton bot_valida_contable = new Boton();
    	bot_valida_contable.setId("bot_valida_contable");
    	bot_valida_contable.setValue("VERIFICACION CONTABLE");
    	bot_valida_contable.setMetodo("aceptarContable");
    	bar_botones.agregarBoton(bot_valida_contable);
    	
    	Boton bot_consulta_contable = new Boton();
    	bot_consulta_contable.setId("bot_consulta_contable");
    	bot_consulta_contable.setValue("CONSULTA CONTABLE");
    	bot_consulta_contable.setMetodo("aceptarConsultaContable");
    	bar_botones.agregarBoton(bot_consulta_contable);

    	
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("tes_banco_anio", "ide_tebaa",1);
        tab_tabla1.getColumna("ide_geani").setCombo("GEN_ANIO", "ide_geani", "detalle_geani", "");
        tab_tabla1.getColumna("ide_geani").setMetodoChange("getSaldoInicial");
        
        tab_tabla1.getColumna("ide_cocac").setCombo("select ide_cocac,cue_codigo_cocac,cue_descripcion_cocac  " + "from cont_catalogo_cuenta " + "where activo_cocac=true");
        tab_tabla1.getColumna("ide_cocac").setAutoCompletar();
        tab_tabla1.getColumna("ACTIVO_tebaa").setValorDefecto("true"); 
		tab_tabla1.setCondicion("ide_tebaa=-1");
        tab_tabla1.agregarRelacion(tab_tabla2);
        tab_tabla1.dibujar();
        
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla1);
        
        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("tes_banco_movimiento", "ide_tebam",2);
        tab_tabla2.getColumna("ide_gemes").setCombo("gen_mes", "ide_gemes", "detalle_gemes", "");
        tab_tabla2.getColumna("ide_gemes").setMetodoChange("getSaldo");
        tab_tabla2.getColumna("mov_debe_tebam").setValorDefecto("0.00"); 
        tab_tabla2.getColumna("mov_haber_tebam").setValorDefecto("0.00");
        tab_tabla2.getColumna("saldo_tebam").setValorDefecto("0.00");
        tab_tabla2.getColumna("saldo_inicial_tebam").setValorDefecto("0.00");
        tab_tabla2.getColumna("saldo_final_tebam").setValorDefecto("0.00");
        tab_tabla2.getColumna("saldo_contable_tebam").setValorDefecto("0.00");
        tab_tabla2.getColumna("valor_transito_tebam").setValorDefecto("0.00");
        tab_tabla2.getColumna("fecha_concialia_tebam").setValorDefecto(utilitario.getFechaActual());
        tab_tabla2.getColumna("verificado_contable_tebam").setValorDefecto("false"); 
        tab_tabla2.getColumna("verificado_transito_tebam").setValorDefecto("false"); 
        tab_tabla2.getColumna("activo_tebam").setValorDefecto("true"); 
       
        tab_tabla2.dibujar();
        
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pat_panel1, pat_panel2, "20%", "H");
        agregarComponente(div_division);
        
        iniciaSelTabContables();
        iniciaSelTabTransito();
        iniciaConsultaSelTabContables();
        iniciaConsultaSelTabTransito();
    }
    
    public void iniciaSelTabContables(){
        seltab_valida_contable.setId("seltab_valida_contable");
        seltab_valida_contable.setSeleccionTabla(ser_Tesoreria.getSqlConciliacionBancaria("1", "1","1900-01-01","1900-01-01","0","-1"),"ide_codem");
        seltab_valida_contable.getTab_seleccion().getColumna("nro_comprobante_comov").setFiltro(true);
        seltab_valida_contable.getTab_seleccion().getColumna("detalle_comov").setFiltro(true);
        seltab_valida_contable.getTab_seleccion().getColumna("ide_comov").setFiltro(true);
        
        seltab_valida_contable.setTitle("SELECCION ASIENTOS A SER CONCILIADOS");        
        seltab_valida_contable.getBot_aceptar().setMetodo("aprobarContable");
        agregarComponente(seltab_valida_contable);
        
    }
    public void iniciaSelTabTransito(){
        seltab_valida_transito.setId("seltab_valida_transito");
        seltab_valida_transito.setSeleccionTabla(ser_Tesoreria.getSqlConciliacionBancaria("1", "1","1900-01-01","1900-01-01","0","-1"),"ide_codem");
        seltab_valida_transito.getTab_seleccion().getColumna("ide_comov").setFiltro(true);
        seltab_valida_transito.getTab_seleccion().getColumna("nro_comprobante_comov").setFiltro(true);
        seltab_valida_transito.getTab_seleccion().getColumna("detalle_comov").setFiltro(true);
        seltab_valida_transito.setTitle("SELECCION ASIENTOS QUE SE ENCUENTRAN EN TRANSITO");        
        seltab_valida_transito.getBot_aceptar().setMetodo("aprobarTransito");
        //set_solicitud.setRadio();
        agregarComponente(seltab_valida_transito);
        
    }
    public void iniciaConsultaSelTabContables(){
        seltab_consulta_contable.setId("seltab_consulta_contable");
        seltab_consulta_contable.setSeleccionTabla(ser_Tesoreria.sqlConciliado("1", "-1"),"ide_codem");
        seltab_consulta_contable.getTab_seleccion().getColumna("nro_comprobante_comov").setFiltro(true);
        seltab_consulta_contable.getTab_seleccion().getColumna("detalle_comov").setFiltro(true);
        seltab_consulta_contable.getTab_seleccion().getColumna("ide_comov").setFiltro(true);
        //seltab_consulta_contable.getTab_seleccion().getSumaColumna("debe_codem,haber_codem");
        //seltab_consulta_contable.set     
        seltab_consulta_contable.setTitle("SELECCION ASIENTOS QUE DESEE REVERTIR");  
        seltab_consulta_contable.getBot_aceptar().setMetodo("desAprobarContable");
        
        agregarComponente(seltab_consulta_contable);
    }
    
    public void iniciaConsultaSelTabTransito(){
        seltab_consulta_transito.setId("seltab_consulta_transito");
        seltab_consulta_transito.setSeleccionTabla(ser_Tesoreria.sqlConciliado("2", "-1"),"ide_codem");
        seltab_consulta_transito.getTab_seleccion().getColumna("nro_comprobante_comov").setFiltro(true);
        seltab_consulta_transito.getTab_seleccion().getColumna("detalle_comov").setFiltro(true);
        seltab_consulta_transito.getTab_seleccion().getColumna("ide_comov").setFiltro(true);

        //seltab_consulta_transito.getBot_aceptar().setRendered(false);
        seltab_consulta_transito.setTitle("SELECCION ASIENTOS EN TRANSITO QUE DESEE REVERTIR"); 
        seltab_consulta_transito.getBot_aceptar().setMetodo("desAprobarTransito");
        agregarComponente(seltab_consulta_transito);
    }   
    public void aceptarConsultaContable(){
    	//System.out.println(    "valor nuemro de de fila"	+tab_tabla2.getFilaSeleccionada().getRowKey());

    	if(tab_tabla2.getTotalFilas()>0){
        	seltab_consulta_contable.getTab_seleccion().setSql(ser_Tesoreria.sqlConciliado("1", tab_tabla2.getFilaSeleccionada().getRowKey()+""));
            //seltab_consulta_contable.getTab_seleccion().getSumaColumna("debe_codem,haber_codem");

        	seltab_consulta_contable.getTab_seleccion().ejecutarSql();
        	seltab_consulta_contable.dibujar();
    	}
    	else{
    		utilitario.agregarMensajeInfo("No existen filas en el detalle de la cuenta Bancaria", "Inserte o seleccione una fila en el detalle");

    	}

     }
    public void aceptarConsultaTransito(){
    	if(tab_tabla2.getTotalFilas()>0){
        	seltab_consulta_transito.getTab_seleccion().setSql(ser_Tesoreria.sqlConciliado("2", tab_tabla2.getFilaSeleccionada().getRowKey()+""));
        	seltab_consulta_transito.getTab_seleccion().ejecutarSql();
        	seltab_consulta_transito.dibujar();
    	}
    	else{
    		utilitario.agregarMensajeInfo("No existen filas en el detalle de la cuenta Bancaria", "Inserte o seleccione una fila en el detalle");

    	}

     }    
    
    public void aceptarContable(){
    	
    	//System.out.println(    "valor indicccce"	+tab_tabla1.getFilaSeleccionada().getRowKey());
    	//System.out.println(    "valor nuemro de de fila"	+tab_tabla1.getFilaActual());
    	//System.out.println(    "valor nuemro de de fila"	+tab_tabla1.getValor(tab_tabla1.getFilaActual(),"ide_geani"));
    	if(tab_tabla2.getTotalFilas()>0){
 
    		if(tab_tabla2.getValor(tab_tabla2.getFilaActual(), "verificado_transito_tebam").equals("true")){
	        	seltab_valida_contable.getTab_seleccion().setSql(ser_Tesoreria.getSqlConciliacionBancaria(    	tab_tabla1.getValor(tab_tabla1.getFilaActual(), "ide_cocac"), "2",tab_tabla2.getValor(tab_tabla2.getFilaActual(), "fecha_inicial_tebam"),tab_tabla2.getValor(tab_tabla2.getFilaActual(), "fecha_final_tebam"),"0","-1"));
	        	seltab_valida_contable.getTab_seleccion().ejecutarSql();
	        	seltab_valida_contable.dibujar();
    		}
    		else{
        		utilitario.agregarMensajeInfo("Contabilizar","Debe Verificar Transito.");

    		}
    	}
    	else{
    		utilitario.agregarMensajeInfo("No existen filas en el detalle de la cuenta Bancaria", "Inserte o seleccione una fila en el detalle");

    	}

     }
    
    public void aceptarTransito(){
     	System.out.println("entra a metodo impostar solicitud");
    	if(tab_tabla2.getTotalFilas()>0){
    		
    		System.out.println(" verificado "+tab_tabla2.getValor(tab_tabla2.getFilaActual(), "verificado_contable_tebam"));
    		
    		//if(tab_tabla2.getValor(tab_tabla2.getFilaActual(), "verificado_contable_tebam").equals("true")){
	        	seltab_valida_transito.getTab_seleccion().setSql(ser_Tesoreria.getSqlConciliacionBancaria(tab_tabla1.getValor(tab_tabla1.getFilaActual(), "ide_cocac"), "1",tab_tabla2.getValor(tab_tabla2.getFilaActual(), "fecha_inicial_tebam"),tab_tabla2.getValor(tab_tabla2.getFilaActual(), "fecha_final_tebam"),"0","-1"));
	        	seltab_valida_transito.getTab_seleccion().ejecutarSql();
	        	seltab_valida_transito.dibujar();
    		//}
    		//else{
        	//	utilitario.agregarMensajeInfo("Contabilizar","Debe Verificar Contable y Conciliar Primero las Cuentas Contables");
    		//}
   		
    		
    	}
    	else{
    		utilitario.agregarMensajeInfo("No existen filas en el detalle de la cuenta Bancaria", "Inserte o seleccione una fila en el detalle");
     	
    	}
     }
    
    public void aprobarContable(){
		String str_seleccionados = seltab_valida_contable.getSeleccionados();
		if (str_seleccionados != null) {
			
			String consultaseleccionados =ser_Tesoreria.getSqlConciliacionBancaria( tab_tabla1.getValor(tab_tabla1.getFilaActual(), "ide_cocac"), "2",tab_tabla2.getValor(tab_tabla2.getFilaActual(), "fecha_inicial_tebam"),tab_tabla2.getValor(tab_tabla2.getFilaActual(), "fecha_final_tebam"),"1",str_seleccionados);
		    String sql="select 1 as codigo,sum(debe_codem) as debe,sum(haber_codem) as haber,saldo_cocac,"
		    		   +" (case when saldo_cocac =1 then sum(debe_codem) - sum(haber_codem) when saldo_cocac=2 then sum(haber_codem) -sum(debe_codem) end ) as saldo from (";
				sql+=consultaseleccionados;
				sql+=" ) a group by saldo_cocac";
			TablaGenerica tab_saldo = utilitario.consultar(sql);
			
			String sql_update_movimiento_banco ="update tes_banco_movimiento set saldo_contable_tebam ="+tab_saldo.getValor("saldo")+",verificado_contable_tebam =true where ide_tebam="+tab_tabla2.getValor(tab_tabla2.getFilaActual(), "ide_tebam");
			
			System.out.println(" sql_update_movimiento_banco "+ sql_update_movimiento_banco);
			
			utilitario.getConexion().ejecutarSql(sql_update_movimiento_banco);
			
			TablaGenerica tab_movimiento=utilitario.consultar(consultaseleccionados);
			
			for(int i=0; i<tab_movimiento.getTotalFilas();i++){
				String sql_update_detalle_movimiento="update cont_detalle_movimiento set conciliado_banco_codem=1, fecha_conciliado_banco_codem='"+tab_tabla2.getValor(tab_tabla2.getFilaActual(), "fecha_concialia_tebam")+"', ide_tebam="+tab_tabla2.getValor(tab_tabla2.getFilaActual(), "ide_tebam")+" where ide_codem ="+tab_movimiento.getValor(i, "ide_codem");
				
				System.out.println(" sql_update_detalle_movimiento "+ sql_update_detalle_movimiento);
				
				utilitario.getConexion().ejecutarSql(sql_update_detalle_movimiento);
			}
			

			seltab_valida_contable.cerrar();
			utilitario.agregarMensaje("Conciliado Exitosamente", "");
			//utilitario.addUpdate("tab_tabla1");
			//utilitario.addUpdate("tab_tabla2");
			tab_tabla2.ejecutarSql();
			//utilitario.addUpdateTabla(tab_tabla2, "saldo_contable_tebam,verificado_contable_tebam", "");
			
		} else {
			utilitario.agregarMensajeInfo("Seleccione un Registro","Seleccione un registro para continuar");
		}
    }
    
    public void aprobarTransito(){
    	String str_seleccionados = seltab_valida_transito.getSeleccionados();
		if (str_seleccionados != null) {
			
			String consultaseleccionados =ser_Tesoreria.getSqlConciliacionBancaria(tab_tabla1.getValor(tab_tabla1.getFilaActual(), "ide_cocac"), "1",tab_tabla2.getValor(tab_tabla2.getFilaActual(), "fecha_inicial_tebam"),tab_tabla2.getValor(tab_tabla2.getFilaActual(), "fecha_final_tebam"),"1",str_seleccionados);
		
			 String sql="select 1 as codigo,sum(debe_codem) as debe,sum(haber_codem) as haber,saldo_cocac,"
		    		   +" (case when saldo_cocac =1 then sum(debe_codem) - sum(haber_codem) when saldo_cocac=2 then sum(haber_codem) -sum(debe_codem) end ) as saldo from (";
				sql+=consultaseleccionados;
				sql+=" ) a group by saldo_cocac";
			TablaGenerica tab_saldo = utilitario.consultar(sql);
			
			String sql_update_movimiento_banco ="update tes_banco_movimiento set valor_transito_tebam = "+tab_saldo.getValor("saldo")+", verificado_transito_tebam=true,mov_debe_tebam ="+tab_saldo.getValor("debe")+",mov_haber_tebam ="+tab_saldo.getValor("haber")+"  where ide_tebam="+tab_tabla2.getValor(tab_tabla2.getFilaActual(), "ide_tebam");
			utilitario.getConexion().ejecutarSql(sql_update_movimiento_banco);
			
			TablaGenerica tab_movimiento=utilitario.consultar(consultaseleccionados);
			
			for(int i=0; i<tab_movimiento.getTotalFilas();i++){
				String sql_update_detalle_movimiento="update cont_detalle_movimiento set conciliado_banco_codem=2, fecha_conciliado_banco_codem='"+tab_tabla2.getValor(tab_tabla2.getFilaActual(), "fecha_concialia_tebam")+"', ide_tebam="+tab_tabla2.getValor(tab_tabla2.getFilaActual(), "ide_tebam")+" where ide_codem ="+tab_movimiento.getValor(i, "ide_codem");
				utilitario.getConexion().ejecutarSql(sql_update_detalle_movimiento);
			}
		
			seltab_valida_transito.cerrar();
			utilitario.agregarMensaje("Ejecutado Cuentas en Transito Exitosamente", "");

			//utilitario.addUpdateTabla(tab_tabla2, "valor_transito_tebam,verificado_transito_tebam", "");
			tab_tabla2.ejecutarSql();
			
		} else {
			utilitario.agregarMensajeInfo("Seleccione un Registro","Seleccione un registro para continuar");
		}
    }
	
    public void desAprobarContable(){
		String str_seleccionados = seltab_consulta_contable.getSeleccionados();
		if (str_seleccionados != null) {
			
			String consultaseleccionados =ser_Tesoreria.getSqlRevConciliacionBancaria(tab_tabla1.getValor(tab_tabla1.getFilaActual(), "ide_cocac"), "2",tab_tabla2.getValor(tab_tabla2.getFilaActual(), "fecha_inicial_tebam"),tab_tabla2.getValor(tab_tabla2.getFilaActual(), "fecha_final_tebam"),"1",str_seleccionados);
		    String sql="select 1 as codigo,sum(debe_codem) as debe,sum(haber_codem) as haber,saldo_cocac,"
		    		   +" (case when saldo_cocac =1 then sum(debe_codem) - sum(haber_codem) when saldo_cocac=2 then sum(haber_codem) -sum(debe_codem) end ) as saldo from (";
				sql+=consultaseleccionados;
				sql+=" ) a group by saldo_cocac";
			TablaGenerica tab_saldo = utilitario.consultar(sql);
			
			String sql_update_movimiento_banco ="update tes_banco_movimiento set saldo_contable_tebam = saldo_contable_tebam-"+tab_saldo.getValor("saldo")+",verificado_contable_tebam =true where ide_tebam="+tab_tabla2.getValor(tab_tabla2.getFilaActual(), "ide_tebam");
			utilitario.getConexion().ejecutarSql(sql_update_movimiento_banco);
			
			TablaGenerica tab_movimiento=utilitario.consultar(consultaseleccionados);
			
			for(int i=0; i<tab_movimiento.getTotalFilas();i++){
				String sql_update_detalle_movimiento="update cont_detalle_movimiento set conciliado_banco_codem=null, fecha_conciliado_banco_codem=null , ide_tebam=null where ide_codem ="+tab_movimiento.getValor(i, "ide_codem");
				utilitario.getConexion().ejecutarSql(sql_update_detalle_movimiento);
			}
			
			seltab_consulta_contable.cerrar();
			utilitario.agregarMensaje("DesConciliado Exitosamente", "");
			//utilitario.addUpdate("tab_tabla1");
			//utilitario.addUpdate("tab_tabla2");
			tab_tabla2.ejecutarSql();
			
		} else {
			utilitario.agregarMensajeInfo("Seleccione un Registro","Seleccione un registro para continuar");
		}
    }
    
    public void desAprobarTransito(){
    	String str_seleccionados = seltab_consulta_transito.getSeleccionados();
		if (str_seleccionados != null) {
			
			String consultaseleccionados =ser_Tesoreria.getSqlRevConciliacionBancaria(tab_tabla1.getValor(tab_tabla1.getFilaActual(), "ide_cocac"), "1",tab_tabla2.getValor(tab_tabla2.getFilaActual(), "fecha_inicial_tebam"),tab_tabla2.getValor(tab_tabla2.getFilaActual(), "fecha_final_tebam"),"1",str_seleccionados);
		
			 String sql="select 1 as codigo,sum(debe_codem) as debe,sum(haber_codem) as haber,saldo_cocac,"
		    		   +" (case when saldo_cocac =1 then sum(debe_codem) - sum(haber_codem) when saldo_cocac=2 then sum(haber_codem) -sum(debe_codem) end ) as saldo from (";
				sql+=consultaseleccionados;
				sql+=" ) a group by saldo_cocac";
			TablaGenerica tab_saldo = utilitario.consultar(sql);
			
			String sql_update_movimiento_banco ="update tes_banco_movimiento set "
					+ " valor_transito_tebam = valor_transito_tebam - "+tab_saldo.getValor("saldo")+", "
					+ " verificado_transito_tebam=true, mov_debe_tebam=mov_debe_tebam-debe ,mov_haber_tebam=mov_haber_tebam-haber  where ide_tebam="+tab_tabla2.getValor(tab_tabla2.getFilaActual(), "ide_tebam");
			utilitario.getConexion().ejecutarSql(sql_update_movimiento_banco);
			
			TablaGenerica tab_movimiento=utilitario.consultar(consultaseleccionados);
			
			for(int i=0; i<tab_movimiento.getTotalFilas();i++){
				String sql_update_detalle_movimiento="update cont_detalle_movimiento set conciliado_banco_codem=null, fecha_conciliado_banco_codem=null, ide_tebam=null where ide_codem ="+tab_movimiento.getValor(i, "ide_codem");
				utilitario.getConexion().ejecutarSql(sql_update_detalle_movimiento);
			}
		
			seltab_consulta_transito.cerrar();
			utilitario.agregarMensaje("Ejecutado Cuentas en Transito Exitosamente", "");
			//utilitario.addUpdate("tab_tabla1");
			//utilitario.addUpdate("tab_tabla2");
			tab_tabla2.ejecutarSql();
			
		} else {
			utilitario.agregarMensajeInfo("Seleccione un Registro","Seleccione un registro para continuar");
		}
    }
    
    
    
    public void getSaldoInicial(AjaxBehaviorEvent evt){
    	tab_tabla1.modificar(evt);
    	
    	String sql = ser_Tesoreria.getSqlConciliacionBancariaSaldo(tab_tabla1.getValor(tab_tabla1.getFilaActual(), "ide_cocac"), "1",tab_tabla1.getValor(tab_tabla1.getFilaActual(), "ide_geani"),"1");
    	TablaGenerica tab_saldo = utilitario.consultar(sql);
    	
    	double saldo_ini=0;
    	
    	if(tab_saldo.getTotalFilas()>0){
    		saldo_ini=pckUtilidades.CConversion.CDbl_2(tab_saldo.getValor("saldo"));
    	}

    	tab_tabla1.setValor("saldo_inicial_tebaa", saldo_ini+"");
		utilitario.addUpdateTabla(tab_tabla1, "saldo_inicial_tebaa", "");
	}
    
    public void getSaldo(AjaxBehaviorEvent evt){
    	tab_tabla2.modificar(evt);
    	
    	String sql = ser_Tesoreria.getSqlConciliacionBancariaSaldo(tab_tabla1.getValor(tab_tabla1.getFilaActual(), "ide_cocac"), tab_tabla2.getValor(tab_tabla2.getFilaActual(), "ide_gemes"),tab_tabla1.getValor(tab_tabla1.getFilaActual(), "ide_geani"),"0");
    	TablaGenerica tab_saldo = utilitario.consultar(sql);
    	
    	double saldo_mes=0;
    	
    	if(tab_saldo.getTotalFilas()>0){
    		saldo_mes=pckUtilidades.CConversion.CDbl_2(tab_saldo.getValor("saldo"));
    	}

    	tab_tabla2.setValor("saldo_tebam", saldo_mes+"");
		utilitario.addUpdateTabla(tab_tabla2, "saldo_tebam", "");
	}
    
    public void limpiar(){
		aut_movimniento.limpiar();
		tab_tabla1.limpiar();
		tab_tabla2.limpiar();
		utilitario.addUpdate("aut_movimniento");
	}
    public void Carga(SelectEvent evt){
    	System.out.println("aut_movimiento "+aut_movimniento.getValor());
    	aut_movimniento.onSelect(evt);
    		tab_tabla1.setCondicion("IDE_TEBAC="+aut_movimniento.getValor());
        	tab_tabla1.ejecutarSql();
    
    }

    @Override
    public void insertar() {
    	
		if(aut_movimniento.getValor()==null){
			utilitario.agregarMensaje("No se puede insertar", "Debe Elegir una cuenta");
			return;

		}
		if(tab_tabla1.isFocus()){
			tab_tabla1.insertar();
			tab_tabla1.setValor("ide_tebac",aut_movimniento.getValor()+"");
			}
		else if(tab_tabla2.isFocus()){
			tab_tabla2.insertar();
		}
    	
    	
    }

    @Override
    public void guardar() {

        if (tab_tabla1.guardar()) {
            if (tab_tabla2.guardar()) {
                guardarPantalla();
            }
        }
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }
	@Override
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}
	@Override
	public void aceptarReporte() {
		// TODO Auto-generated method stub
		if(rep_reporte.getReporteSelecionado().equals("Conciliación Bancaria")){
			if (rep_reporte.isVisible()){
				rep_reporte.cerrar();	
				TablaGenerica tab_usuario =utilitario.consultar("select a.ide_usua,ide_empr,ide_sucu,nom_usua from sis_usuario a, sis_usuario_sucursal b where a.ide_usua = b.ide_usua and a.ide_usua ="+utilitario.getVariable("ide_usua"));
				
				map_parametros.put("titulo","CONCILIACION BANCARIA");
				map_parametros.put("contador_general",  utilitario.getVariable("p_nombre_contador"));		
				map_parametros.put("elaborado_por", tab_usuario.getValor("nom_usua"));
				map_parametros.put("ide_prcer",pckUtilidades.CConversion.CInt(tab_tabla2.getFilaSeleccionada().getRowKey()));
				sel_rep.setSeleccionFormatoReporte(map_parametros,rep_reporte.getPath());
				sel_rep.dibujar();
				}
		} 
	}
    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public Tabla getTab_tabla2() {
        return tab_tabla2;
    }

    public void setTab_tabla2(Tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
    }

	public AutoCompletar getAut_movimniento() {
		return aut_movimniento;
	}

	public void setAut_movimniento(AutoCompletar aut_movimniento) {
		this.aut_movimniento = aut_movimniento;
	}

	public SeleccionTabla getSeltab_valida_contable() {
		return seltab_valida_contable;
	}

	public void setSeltab_valida_contable(SeleccionTabla seltab_valida_contable) {
		this.seltab_valida_contable = seltab_valida_contable;
	}

	public SeleccionTabla getSeltab_valida_transito() {
		return seltab_valida_transito;
	}

	public void setSeltab_valida_transito(SeleccionTabla seltab_valida_transito) {
		this.seltab_valida_transito = seltab_valida_transito;
	}

	public SeleccionTabla getSeltab_consulta_contable() {
		return seltab_consulta_contable;
	}

	public void setSeltab_consulta_contable(SeleccionTabla seltab_consulta_contable) {
		this.seltab_consulta_contable = seltab_consulta_contable;
	}

	public SeleccionTabla getSeltab_consulta_transito() {
		return seltab_consulta_transito;
	}

	public void setSeltab_consulta_transito(SeleccionTabla seltab_consulta_transito) {
		this.seltab_consulta_transito = seltab_consulta_transito;
	}


	public Calendario getCal_fecha_inicial() {
		return cal_fecha_inicial;
	}

	public void setCal_fecha_inicial(Calendario cal_fecha_inicial) {
		this.cal_fecha_inicial = cal_fecha_inicial;
	}

	public Calendario getCal_fecha_final() {
		return cal_fecha_final;
	}

	public void setCal_fecha_final(Calendario cal_fecha_final) {
		this.cal_fecha_final = cal_fecha_final;
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
    
    
}
