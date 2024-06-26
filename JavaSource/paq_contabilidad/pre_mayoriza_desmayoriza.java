package paq_contabilidad;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;



import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.BotonesCombo;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.ItemMenu;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;

public class pre_mayoriza_desmayoriza extends Pantalla{

	private Tabla tab_movimientos_contables=new Tabla();
	private Combo com_anio=new Combo();
	private SeleccionTabla set_asiento_contable = new SeleccionTabla();
	private Confirmar con_guardar=new Confirmar();
	private Radio rad_mayoriza_desmayoriza= new Radio();
	private Combo com_mes = new Combo();

	public static String par_tipo_asiento_inicial;
	private static String empleado_responsable;

	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
    private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);

	public pre_mayoriza_desmayoriza() {
		// Este parametro contiene el el tipo siento inicial de apertura
		par_tipo_asiento_inicial =utilitario.getVariable("p_tipo_asiento_inicial"); 
		
		empleado_responsable=ser_contabilidad.empleadoResponsable(utilitario.getVariable("p_modulo_mayores_emp"),ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp"));
		System.out.println("empleado_responsable"+empleado_responsable);	
		
		bar_botones.limpiar();
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));		
		com_anio.setMetodo("seleccionaOpcion");
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
		
		com_mes.setCombo(ser_contabilidad.getMes("true,false"));		
		com_mes.setMetodo("seleccionaOpcion");
		com_mes.setStyle("width: 100px; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Mes:"));
		bar_botones.agregarComponente(com_mes);
		
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarBoton(bot_limpiar);
		
		List lista = new ArrayList();
		Object fila1[] = {
		    "true", "ASIENTOS MAYORIZADOS"
		};
		Object fila2[] = {
		    "false", "ASIENTOS NO MAYORIZADOS"
		};
		   
		lista.add(fila1);
		lista.add(fila2);
		rad_mayoriza_desmayoriza.setId("rad_mayoriza_desmayoriza");
		rad_mayoriza_desmayoriza.setRadio(lista);
		rad_mayoriza_desmayoriza.setValue(fila2);
		rad_mayoriza_desmayoriza.setMetodoChange("seleccionaOpcion");
		rad_mayoriza_desmayoriza.setStyle("display: inline-block; margin: 0 0 -8px 0;");
		bar_botones.agregarComponente(rad_mayoriza_desmayoriza);
		// boton limpiar
		
		
		
		BotonesCombo boc_seleccion_inversa = new BotonesCombo();
		ItemMenu itm_todas = new ItemMenu();
		ItemMenu itm_niguna = new ItemMenu();

		boc_seleccion_inversa.setValue("Selección Inversa");
		boc_seleccion_inversa.setIcon("ui-icon-circle-check");
		boc_seleccion_inversa.setMetodo("seleccinarInversa");
		boc_seleccion_inversa.setUpdate("tab_movimientos_contables");
		itm_todas.setValue("Seleccionar Todo");
		itm_todas.setIcon("ui-icon-check");
		itm_todas.setMetodo("seleccionarTodas");
		itm_todas.setUpdate("tab_movimientos_contables");
		boc_seleccion_inversa.agregarBoton(itm_todas);
		itm_niguna.setValue("Seleccionar Ninguna");
		itm_niguna.setIcon("ui-icon-minus");
		itm_niguna.setMetodo( "seleccionarNinguna");
		itm_niguna.setUpdate("tab_movimientos_contables");
		boc_seleccion_inversa.agregarBoton(itm_niguna);
		
		
		tab_movimientos_contables.setId("tab_movimientos_contables");  
		tab_movimientos_contables.setHeader("MOVIMIENTOS CONTABLES PARA MAYORIZAR O DESMAYORIZAR");
		tab_movimientos_contables.setSql(ser_contabilidad.getMovimientosContablesSumaDebeHaber("-1","-1","false","-1"));	
		tab_movimientos_contables.setNumeroTabla(1);
		tab_movimientos_contables.setCampoPrimaria("ide_comov");
		tab_movimientos_contables.getColumna("ide_comov").setNombreVisual("CODIGO");
		tab_movimientos_contables.getColumna("nro_comprobante_comov").setNombreVisual("NRO. COMPROBANTE");
		tab_movimientos_contables.getColumna("mov_fecha_comov").setNombreVisual("FECHA ASIENTO");
		tab_movimientos_contables.getColumna("detalle_comov").setNombreVisual("DETALLE ASIENTO");
		tab_movimientos_contables.getColumna("debe").setNombreVisual("DEBE");
		tab_movimientos_contables.getColumna("haber").setNombreVisual("HABER");
		tab_movimientos_contables.getColumna("diferencia").setNombreVisual("DIFERENCIA");
		tab_movimientos_contables.getColumna("ide_comov").setFiltro(true);
		tab_movimientos_contables.getColumna("nro_comprobante_comov").setFiltro(true);
		tab_movimientos_contables.getColumna("mov_fecha_comov").setFiltro(true);
		tab_movimientos_contables.getColumna("detalle_comov").setFiltro(true);
		tab_movimientos_contables.getColumna("debe").setFiltro(true);
		tab_movimientos_contables.getColumna("haber").setFiltro(true);
		tab_movimientos_contables.getColumna("diferencia").setFiltro(true);
		
		tab_movimientos_contables.getColumna("detalle_asiento").setVisible(false);
		tab_movimientos_contables.setRows(20);
		tab_movimientos_contables.setLectura(true);

		tab_movimientos_contables.dibujar();
		PanelTabla pat_balance_inicial=new PanelTabla();
		pat_balance_inicial.getChildren().add(boc_seleccion_inversa);
		pat_balance_inicial.setPanelTabla(tab_movimientos_contables);
		pat_balance_inicial.getMenuTabla().getItem_formato().setDisabled(true);

		Division div1 = new Division();
		div1.dividir1(pat_balance_inicial);
		agregarComponente(div1);
		
		//Boton actualizar		
		Boton bot_actualizar=new Boton();
		bot_actualizar.setIcon("ui-icon-person");
		bot_actualizar.setValue("Mayorizar");
		bot_actualizar.setMetodo("generarBalanceComprobacion");
		bot_actualizar.setStyle("display: inline-block;");
		bar_botones.agregarBoton(bot_actualizar);				
		con_guardar.setId("con_guardar");
		con_guardar.setMessage("ESTA SEGURO DE MAYORIZAR Y GENERAR EL BALANCE INICIAL");
		con_guardar.setTitle("CONFIRMACION DE CALCULO");
		agregarComponente(con_guardar);

		Boton bot_desmayoriza=new Boton();
		bot_desmayoriza.setIcon("ui-icon-person");
		bot_desmayoriza.setValue("Desmayorizar");
		bot_desmayoriza.setMetodo("desmayoriza");
		bot_desmayoriza.setStyle("display: inline-block;");
		bar_botones.agregarBoton(bot_desmayoriza);		
		
		if(empleado_responsable==null ||empleado_responsable.isEmpty()){
			utilitario.agregarNotificacionInfo("Mensaje", "No existe usuario responsable para mayorizar/desmayorizar");
			bot_actualizar.setDisabled(true);
			bot_desmayoriza.setDisabled(true);
		}
	}

	public void desmayoriza(){
		if (com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un registro", "Seleccione un año");
			return;	
		}
			else if(com_mes.getValue() != null){
				
				ser_contabilidad.desmayorizaAsientos(com_anio.getValue().toString(), com_mes.getValue().toString());
				utilitario.agregarMensajeInfo("Desmayorizado", "Asientos desmayorizados");

			}
			else{
				utilitario.agregarMensajeInfo("Debe seleccionar un registro", "Seleccione un mes");
				return;		
			}
	}
	public void generarBalanceComprobacion(){
	
	if (com_anio.getValue()==null){
		utilitario.agregarMensajeInfo("Debe seleccionar un registro", "Seleccione un año");
		return;	
	}
		else if(com_mes.getValue() != null){
			
			if (!con_guardar.isVisible()){
				con_guardar.setMessage("¿ESTA SEGURO DE MAYORIZAR Y GENERAR EL BALANCE DE COMPROBACION? Recuerde que los asientos deben ser aprobados.");
				con_guardar.setTitle("CONFIRMACION DE CALCULO");
				con_guardar.getBot_aceptar().setMetodo("generarBalanceComprobacion");
				con_guardar.dibujar();
				utilitario.addUpdate("con_guardar");
			}else{
				con_guardar.cerrar();
				ser_contabilidad.generarBalanceComprobacion(com_anio.getValue().toString(), com_mes.getValue().toString(), par_tipo_asiento_inicial);
				tab_movimientos_contables.ejecutarSql();
				utilitario.addUpdate("tab_movimientos_contables");
				utilitario.agregarMensajeInfo("Se Mayorizo", "Se mayorizo y genero el balance de comprobaciòn");	
			}
		}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar un registro", "Seleccione un mes");
			return;		
		}
	}
	public void seleccionaOpcion (){
		String meses_todos="1,2,3,4,5,6,7,8,9,10,11,12";
		//String estado="true,false";
		String estado="false";
		if(com_anio.getValue()==null){
			
			tab_movimientos_contables.limpiar();
			utilitario.addUpdate("tab_movimientos_contables");
			return;

		}
		if(com_mes.getValue()!=null){
			meses_todos=com_mes.getValue().toString();
		}
		System.out.println("valor radio "+rad_mayoriza_desmayoriza.getValue());
		if(rad_mayoriza_desmayoriza.getValue().equals("true")){
			estado="true";
		}
		if(rad_mayoriza_desmayoriza.getValue().equals("false")){
			estado="false";
		}
		
			tab_movimientos_contables.setSql(ser_contabilidad.getMovimientosContablesSumaDebeHaber(com_anio.getValue().toString(),meses_todos,estado,par_tipo_asiento_inicial));	
			tab_movimientos_contables.ejecutarSql();
		
		utilitario.addUpdate("tab_movimientos_contables");
	}
	
	/**
	 * limpia toda la pantalla incluyendo el autocompletar
	 */
	public void limpiar() {
		tab_movimientos_contables.limpiar();	
		utilitario.addUpdate("tab_movimientos_contables");// limpia y refresca el autocompletar
	}
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_movimientos_contables.insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_movimientos_contables.guardar();
		guardarPantalla();		
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_movimientos_contables.eliminar();
	}

	public void seleccionarTodas() {
		tab_movimientos_contables.setSeleccionados(null);
        Fila seleccionados[] = new Fila[tab_movimientos_contables.getTotalFilas()];
        for (int i = 0; i < tab_movimientos_contables.getFilas().size(); i++) {
            seleccionados[i] = tab_movimientos_contables.getFilas().get(i);
        }
        tab_movimientos_contables.setSeleccionados(seleccionados);
}

/**DFJ**/
public void seleccinarInversa() {
        if (tab_movimientos_contables.getSeleccionados() == null) {
            seleccionarTodas();
        } else if (tab_movimientos_contables.getSeleccionados().length == tab_movimientos_contables.getTotalFilas()) {
            seleccionarNinguna();
        } else {
            Fila seleccionados[] = new Fila[tab_movimientos_contables.getTotalFilas() - tab_movimientos_contables.getSeleccionados().length];
            int cont = 0;
            for (int i = 0; i < tab_movimientos_contables.getFilas().size(); i++) {
                boolean boo_selecionado = false;
                for (int j = 0; j < tab_movimientos_contables.getSeleccionados().length; j++) {
                    if (tab_movimientos_contables.getSeleccionados()[j].equals(tab_movimientos_contables.getFilas().get(i))) {
                        boo_selecionado = true;
                        break;
                    }
                }
                if (boo_selecionado == false) {
                    seleccionados[cont] = tab_movimientos_contables.getFilas().get(i);
                    cont++;
                }
            }
            tab_movimientos_contables.setSeleccionados(seleccionados);
        }
    }

/**DFJ**/
public void seleccionarNinguna() {
	tab_movimientos_contables.setSeleccionados(null);
    }

	public Tabla getTab_movimientos_contables() {
		return tab_movimientos_contables;
	}
	public void setTab_movimientos_contables(Tabla tab_movimientos_contables) {
		this.tab_movimientos_contables = tab_movimientos_contables;
	}
	public SeleccionTabla getSet_asiento_contable() {
		return set_asiento_contable;
	}
	public void setSet_asiento_contable(SeleccionTabla set_asiento_contable) {
		this.set_asiento_contable = set_asiento_contable;
	}
	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}
	
	public Combo getCom_mes() {
		return com_mes;
	}

	public void setCom_mes(Combo com_mes) {
		this.com_mes = com_mes;
	}

	public Confirmar getCon_guardar() {
		return con_guardar;
	}
	public void setCon_guardar(Confirmar con_guardar) {
		this.con_guardar = con_guardar;
	}

	


}
