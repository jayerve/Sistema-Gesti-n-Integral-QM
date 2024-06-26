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
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;

public class pre_aplicacion_superavit extends Pantalla{

	private Tabla tab_balance_inicial=new Tabla();
	private Combo com_anio=new Combo();
	private SeleccionTabla set_asiento_contable = new SeleccionTabla();
	private Confirmar con_guardar=new Confirmar();
	private Combo com_nivel_cuenta_inicial = new Combo();
	private Combo com_nivel_cuenta_final = new Combo();
	
	public static String par_tipo_asiento_inicial;
	private SeleccionCalendario sel_calendario=new SeleccionCalendario();

	
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);

	public pre_aplicacion_superavit() {
		par_tipo_asiento_inicial =utilitario.getVariable("p_tipo_asiento_inicial");

		bar_botones.limpiar();
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));	
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
		
		// boton limpiar
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarBoton(bot_limpiar);
		
		tab_balance_inicial.setId("tab_balance_inicial");  
		tab_balance_inicial.setTabla("cont_flujo_financiamiento", "ide_flujo_financiamiento", 1);	
		tab_balance_inicial.setHeader("APLICACION DE SUPERAVIT O DEFICIT");
		tab_balance_inicial.getColumna("con_ide_flujo_financiamiento").setCombo("cont_flujo_financiamiento", "ide_flujo_financiamiento", "des_cuenta", "");
		tab_balance_inicial.getColumna("ide_cuenta").setCombo(ser_contabilidad.getCuentaContable("true,false"));
		tab_balance_inicial.getColumna("ide_cuenta").setAutoCompletar();
		tab_balance_inicial.getColumna("ide_cuenta").setLongitud(200);

		tab_balance_inicial.setLectura(true);
		tab_balance_inicial.dibujar();
		PanelTabla pat_balance_inicial=new PanelTabla();
		pat_balance_inicial.setPanelTabla(tab_balance_inicial);
		Division div1 = new Division();
		div1.dividir1(pat_balance_inicial);
		agregarComponente(div1);
		
		
		Boton bot_actualizar=new Boton();
		bot_actualizar.setIcon("ui-icon-person");
		bot_actualizar.setValue("Calcular SUPERAVIT O FINANCIAMIENTO");
		bot_actualizar.setMetodo("generarCedula");
		bar_botones.agregarBoton(bot_actualizar);	
		
		inicializaCalendario();
		
	}
	public void inicializaCalendario(){
		sel_calendario.setTitle("SELECCION DE FECHAS");
		sel_calendario.setFooter("Seleccione un Rango de fechas");
		sel_calendario.setFecha1(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		sel_calendario.setFecha2(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		sel_calendario.getBot_aceptar().setMetodo("generarCedula");
		agregarComponente(sel_calendario);
	}
	
	public void generarCedula(){
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Año", "");
			return;			

		}
		TablaGenerica tab_valida_anio=utilitario.consultar("select 1 as codigo, 2 as resultado from tes_caja where not extract(year from cast('"+sel_calendario.getFecha1String()+"' as date)) =extract(year from cast('"+sel_calendario.getFecha2String()+"' as date))");
		if(tab_valida_anio.getTotalFilas()>0){
			utilitario.agregarMensajeError("Fechas no Validas", "Favor ingrese fechas que se encuentren dentro de un mismo año");
			return;
		}
		if(sel_calendario.isVisible()){
			sel_calendario.cerrar();

			ser_contabilidad.generarBalComprobacion(com_anio.getValue().toString(), sel_calendario.getFecha1().toString(), sel_calendario.getFecha2().toString(), par_tipo_asiento_inicial);
			TablaGenerica tab_meses = utilitario.consultar("select 1 as codigo,extract(month from cast('"+sel_calendario.getFecha1String()+"' as date)) as mes_inicial, extract(month from cast('"+sel_calendario.getFecha2String()+"' as date)) as mes_final");
			
			String actualiza_tabla="update cont_flujo_financiamiento"
					+" set valor = 0,saldo_inicial=0, saldo_final=0,"
					+" periodo_inicial="+tab_meses.getValor("mes_inicial") +","
					+" periodo_final="+tab_meses.getValor("mes_final");
			utilitario.getConexion().ejecutarSql(actualiza_tabla);
			
			
			String traspasa_valores="update cont_flujo_financiamiento"
					+" set valor = (case when flujo =1 then debe3 when flujo = 2 then haber3 end) ,saldo_inicial= (case when flujo =1 then debe_inicial_cobac when flujo = 2 then haber_inicial_cobac end), saldo_final=(case when flujo =1 then debe_saldo_cobac  when flujo = 2 then haber_saldo_cobac end)"
					+" from ("
					+" select ide_cocac,debe_acumulado_cobac+ debe_periodo_cobac as debe3,haber_acumulado_cobac+haber_periodo_cobac as haber3,debe_inicial_cobac,haber_inicial_cobac,debe_saldo_cobac,haber_saldo_cobac,(case when debe_saldo_cobac = 0 then haber_saldo_cobac else debe_saldo_cobac end) as nuevovalor from cont_balance_comprobacion ) a where ide_cocac=ide_cuenta";
			utilitario.getConexion().ejecutarSql(traspasa_valores);
			
			String calcula_tabla="update cont_flujo_financiamiento"
					+" set valor = val,saldo_inicial=bal_debe,saldo_final=bal_haber"
					+" from ("
					+" select sum(valor) as val,sum(saldo_inicial)as bal_debe,sum(saldo_final) as bal_haber ,con_ide_flujo_financiamiento"
					+" from cont_flujo_financiamiento"
					+" group by con_ide_flujo_financiamiento"
					+" ) a where a.con_ide_flujo_financiamiento = cont_flujo_financiamiento.ide_flujo_financiamiento";
			
			int i=0;
			while (i<=3){
			utilitario.getConexion().ejecutarSql(calcula_tabla);
			 i++;
            }
			
			tab_balance_inicial.ejecutarSql();
			utilitario.addUpdate("tab_balance_inicial");
		}
		else{
			sel_calendario.dibujar();
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
	public SeleccionCalendario getSel_calendario() {
		return sel_calendario;
	}
	public void setSel_calendario(SeleccionCalendario sel_calendario) {
		this.sel_calendario = sel_calendario;
	}

	


}
