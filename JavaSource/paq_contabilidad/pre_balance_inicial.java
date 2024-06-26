package paq_contabilidad;

import javax.ejb.EJB;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;

public class pre_balance_inicial extends Pantalla{

	private Tabla tab_balance_inicial=new Tabla();
	private Combo com_anio=new Combo();
	private SeleccionTabla set_asiento_contable = new SeleccionTabla();
	private Confirmar con_guardar=new Confirmar();
	private Combo com_nivel_cuenta_inicial = new Combo();
	private Combo com_nivel_cuenta_final = new Combo();
	
	public static String par_tipo_asiento_inicial;

	
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);

	public pre_balance_inicial() {
		par_tipo_asiento_inicial =utilitario.getVariable("p_tipo_asiento_inicial");

		bar_botones.limpiar();
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));		
		com_anio.setMetodo("seleccionaElAnio");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
		
		com_nivel_cuenta_inicial.setCombo(utilitario.getListaGruposNivelCuenta());		
		com_nivel_cuenta_inicial.setValue("1");
		com_nivel_cuenta_inicial.setMetodo("seleccionaElAnio");
		com_nivel_cuenta_inicial.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione el Nivel de Cuenta Inicial"));
		bar_botones.agregarComponente(com_nivel_cuenta_inicial);
		
		com_nivel_cuenta_final.setCombo(utilitario.getListaGruposNivelCuenta());		
		com_nivel_cuenta_final.setValue("1");
		com_nivel_cuenta_final.setMetodo("seleccionaElAnio");
		com_nivel_cuenta_final.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione el Nivel de Cuenta Final"));
		bar_botones.agregarComponente(com_nivel_cuenta_final);

		// boton limpiar
				Boton bot_limpiar = new Boton();
				bot_limpiar.setIcon("ui-icon-cancel");
				bot_limpiar.setMetodo("limpiar");
				bar_botones.agregarBoton(bot_limpiar);
				
		tab_balance_inicial.setId("tab_balance_inicial");  
		tab_balance_inicial.setTabla("cont_balance_inicial", "ide_cobai", 1);	
		tab_balance_inicial.setHeader("BALANCE INICIAL");
		tab_balance_inicial.setCondicion("ide_geani=-1");
		tab_balance_inicial.getColumna("ide_geani").setCombo(ser_contabilidad.getAnio("true,false", "true,false"));
		tab_balance_inicial.getColumna("ide_geani").setAutoCompletar();
		tab_balance_inicial.getColumna("ide_cocac").setCombo(ser_contabilidad.servicioCatalogoCuentaCombo());
		tab_balance_inicial.getColumna("ide_cocac").setAutoCompletar();
		tab_balance_inicial.getColumna("ide_cocac").setFiltroContenido();
		tab_balance_inicial.getColumna("ide_cocac").setLongitud(200);

		tab_balance_inicial.getColumna("valor_descripcion_cobai").setFiltroContenido();

		tab_balance_inicial.setRows(20);
		tab_balance_inicial.setLectura(true);
		tab_balance_inicial.dibujar();
		PanelTabla pat_balance_inicial=new PanelTabla();
		pat_balance_inicial.setPanelTabla(tab_balance_inicial);
		Division div1 = new Division();
		div1.dividir1(pat_balance_inicial);
		agregarComponente(div1);
		
		//Boton actualizar		
				Boton bot_actualizar=new Boton();
				bot_actualizar.setIcon("ui-icon-person");
				bot_actualizar.setValue("Generar Balance Inicial");
				bot_actualizar.setMetodo("generarBalanceIncial");
				bar_botones.agregarBoton(bot_actualizar);	
				
				set_asiento_contable.setId("set_asiento_contable");
				set_asiento_contable.setSeleccionTabla(ser_contabilidad.getMovimientosContables("-1","-1","-1"),"ide_comov");
				set_asiento_contable.getBot_aceptar().setMetodo("calcularBalanceInicial");
				agregarComponente(set_asiento_contable);
				
				con_guardar.setId("con_guardar");
				con_guardar.setMessage("ESTA SEGURO DE GENERAR EL BALANCE INICIAL");
				con_guardar.setTitle("CONFIRMACION DE CALCULO");

				agregarComponente(con_guardar);
		
	}
	public void generarBalanceIncial(){
		if (com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un año","");
			return;

		}
		set_asiento_contable.getTab_seleccion().setSql(ser_contabilidad.getMovimientosContables(com_anio.getValue().toString(),par_tipo_asiento_inicial,"6"));
		set_asiento_contable.getTab_seleccion().ejecutarSql();
		set_asiento_contable.dibujar();	
	}
	public void calcularBalanceInicial(){
		String str_seleccionados=set_asiento_contable.getSeleccionados();
		System.out.println(" probando el str_Selccionado "+str_seleccionados);
		if(str_seleccionados!=null || !str_seleccionados.isEmpty()){
			
			if (!con_guardar.isVisible()){
				// dibuja dialogo de confirmacion de recepcion de activjvos fijos
				con_guardar.setMessage("ESTA SEGURO DE GENERAR EL BALANCE INICIAL");
				con_guardar.setTitle("CONFIRMACION DE CALCULO");
				con_guardar.getBot_aceptar().setMetodo("calcularBalanceInicial");
				con_guardar.dibujar();
				utilitario.addUpdate("con_guardar");
			}else{
				set_asiento_contable.cerrar();
				con_guardar.cerrar();
				ser_contabilidad.generarBalanceInicial(com_anio.getValue().toString(), str_seleccionados);
				}
			tab_balance_inicial.ejecutarSql();
			utilitario.addUpdate("tab_balance_inicial");
			}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
			return;		
		}
	}
	public void seleccionaElAnio (){
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Año", "");
			return;			

		}
		if(com_nivel_cuenta_inicial.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione el Nivel de Cuenta Inicial", "");
			return;			

		}
		if(com_nivel_cuenta_final.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione el Nivel de Cuenta Final", "");
			return;			

		}
		
			
			tab_balance_inicial.setCondicion("ide_geani="+com_anio.getValue()+" and ide_cocac in (select ide_cocac from cont_catalogo_cuenta where nivel_cocac between "+com_nivel_cuenta_inicial.getValue()+" and "+com_nivel_cuenta_final.getValue()+")");
			tab_balance_inicial.ejecutarSql();
			utilitario.addUpdate("tab_balance_inicial");
		
	}
	/**
	 * limpia toda la pantalla incluyendo el autocompletar
	 */
	public void limpiar() {
		tab_balance_inicial.limpiar();	
		utilitario.addUpdate("tab_balance_inicial");// limpia y refresca el autocompletar
	}
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_balance_inicial.insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_balance_inicial.guardar();
		guardarPantalla();		
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_balance_inicial.eliminar();
	}

	public Tabla getTab_balance_inicial() {
		return tab_balance_inicial;
	}

	public void setTab_balance_inicial(Tabla tab_balance_inicial) {
		this.tab_balance_inicial = tab_balance_inicial;
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
	public SeleccionTabla getSet_asiento_contable() {
		return set_asiento_contable;
	}
	public void setSet_asiento_contable(SeleccionTabla set_asiento_contable) {
		this.set_asiento_contable = set_asiento_contable;
	}
	public Confirmar getCon_guardar() {
		return con_guardar;
	}
	public void setCon_guardar(Confirmar con_guardar) {
		this.con_guardar = con_guardar;
	}

	


}
