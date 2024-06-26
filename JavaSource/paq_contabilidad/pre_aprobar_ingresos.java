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

public class pre_aprobar_ingresos extends Pantalla{

	private Tabla tab_movimientos_contables=new Tabla();
	private Combo com_anio=new Combo();
	private Combo com_mes = new Combo();
	private Confirmar con_guardar=new Confirmar();
	private Radio rad_aprobado = new Radio();

	private static String p_modulo_anticipos="";
	private static String p_modulo_factruracion="";
	private static String p_modulo_nota_debito="";
	private static String p_modulo_nota_credito="";
	private static String p_sec_ingresos;
	private static String empleado_responsable;
	
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
    private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	
	public pre_aprobar_ingresos() {

		p_modulo_anticipos=utilitario.getVariable("p_modulo_anticipos");
		p_modulo_factruracion=utilitario.getVariable("p_modulo_facturacion");
		p_modulo_nota_debito=utilitario.getVariable("p_modulo_nota_debito");
		p_modulo_nota_credito=utilitario.getVariable("p_modulo_nota_credito");		
		p_sec_ingresos=utilitario.getVariable("p_modulo_ingresos");	
		
		String empleado=ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
		empleado_responsable=ser_contabilidad.empleadoResponsable(p_sec_ingresos,empleado);
		System.out.println("empleado_responsable"+empleado_responsable);
		if(empleado_responsable==null ||empleado_responsable.isEmpty()){
			utilitario.agregarNotificacionInfo("Mensaje", "No existe usuario responsable para la aprobación de ingresos... ACCION NO AUTORIZADA");
			return;
		}
		
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
			
		List listax = new ArrayList();
	    Object filax1[] = {
	       "true", "Aprobado"
	    };
	    Object filax2[] = {
	       "false", "No Aprobado"
	    };
	   
	    listax.add(filax1);
	    listax.add(filax2);
	    rad_aprobado.setId("rad_aprobado");
	    rad_aprobado.setRadio(listax);
	    rad_aprobado.setValue(filax2);
	    rad_aprobado.setMetodoChange("seleccionaOpcion");
	    rad_aprobado.setStyle("display: inline-block; margin: 0 0 -8px 0;");
	    bar_botones.agregarComponente(rad_aprobado);
		
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
		tab_movimientos_contables.setHeader("MOVIMIENTOS CONTABLES POR APROBAR (ingresos)");
		tab_movimientos_contables.setSql(ser_contabilidad.getMovimientosContablesSumaDebeHaber("-1","-1","true,false","-1"));	
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
		tab_movimientos_contables.getColumna("responsable").setLongitud(60);
		tab_movimientos_contables.getColumna("aprobado").setLongitud(60);
		tab_movimientos_contables.setRows(20);
		tab_movimientos_contables.setLectura(true);
		tab_movimientos_contables.setTipoSeleccion(true);
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
		bot_actualizar.setValue("Aprobar Asientos");
		bot_actualizar.setMetodo("aprobar");
		bot_actualizar.setStyle("display: inline-block;");
		bar_botones.agregarBoton(bot_actualizar);			
		
		Boton bot_desaprobar=new Boton();
		bot_desaprobar.setIcon("ui-icon-unlocked");
		bot_desaprobar.setValue("DesAprobar Asientos");
		bot_desaprobar.setMetodo("desaprobar");
		bot_desaprobar.setStyle("display: inline-block;");
		bar_botones.agregarBoton(bot_desaprobar);	
		
		con_guardar.setId("con_guardar");
		//con_guardar.setMessage("¡ESTA SEGURO DE APROBAR LOS ASIENTOS SELECCIONADOS? Recuerde que los mismos deben de contar con un responsable");
		con_guardar.setTitle("CONFIRMACION");
		//con_guardar.getBot_aceptar().setMetodo("aprobar");
		agregarComponente(con_guardar);
	
	}


	public void aprobar()
	{
		if (com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un registro", "Seleccione un año");
			return;	
		}
		else if(com_mes.getValue() != null){
			
			if (!con_guardar.isVisible()){
				if(tab_movimientos_contables.getFilasSeleccionadas().length()<1)
				{
					utilitario.agregarMensajeInfo("Debe seleccionar un registro", "Seleccione un Asiento de Ingreso.!");
					return;	
				}
				con_guardar.setMessage("¡ESTA SEGURO DE APROBAR LOS ASIENTOS SELECCIONADOS? Recuerde que los mismos deben de contar con un responsable");
				con_guardar.getBot_aceptar().setMetodo("aprobar");
				utilitario.addUpdate("con_guardar");
				con_guardar.dibujar();
			}else{
				con_guardar.cerrar();
							
				for(int j=0;j<tab_movimientos_contables.getSeleccionados().length;j++)
				{
					int i = tab_movimientos_contables.getSeleccionados()[j].getIndice();
					
					double diferencia = pckUtilidades.CConversion.CDbl_2(tab_movimientos_contables.getValor(i,"diferencia"));					
					if(diferencia != 0)
					{
						utilitario.agregarNotificacionInfo("Asiento Descuadrado", "No se puede aprobar el asiento: "+tab_movimientos_contables.getValor(i,"detalle_comov"));
						return;
						
					}
				}
				
				ser_contabilidad.aprobarAsientos(com_anio.getValue().toString(), com_mes.getValue().toString(),tab_movimientos_contables.getFilasSeleccionadas(),empleado_responsable);
				tab_movimientos_contables.ejecutarSql();
				utilitario.addUpdate("tab_movimientos_contables");
				utilitario.agregarMensajeInfo("Aprobado", "Asientos Aprobados");	
			}
		}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar un registro", "Seleccione un mes");
			return;		
		}
	}
	
	public void desaprobar()
	{
		if (com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un registro", "Seleccione un año");
			return;	
		}
		else if(com_mes.getValue() != null){
			
			if (!con_guardar.isVisible()){
				if(tab_movimientos_contables.getFilasSeleccionadas().length()<1)
				{
					utilitario.agregarMensajeInfo("Debe seleccionar un registro", "Seleccione un Asiento de Gasto.!");
					return;	
				}
				con_guardar.setMessage("¡ESTA SEGURO DE DESAPROBAR LOS ASIENTOS SELECCIONADOS? Recuerde que debe DesMayorizar antes de realizar esta acción.");
				con_guardar.getBot_aceptar().setMetodo("desaprobar");
				utilitario.addUpdate("con_guardar");
				con_guardar.dibujar();
				
			}else{
				con_guardar.cerrar();
				ser_contabilidad.desAprobarAsientos(com_anio.getValue().toString(), com_mes.getValue().toString(),tab_movimientos_contables.getFilasSeleccionadas());
				tab_movimientos_contables.ejecutarSql();
				utilitario.addUpdate("tab_movimientos_contables");
				utilitario.agregarMensajeInfo("Desaprobación", "Asientos DesAprobados");	
			}
		}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar un registro", "Seleccione un mes");
			return;		
		}
	}
	
	public void seleccionaOpcion (){
		String meses_todos="1,2,3,4,5,6,7,8,9,10,11,12";
		String estado="true,false";
		String condicion=" and (ide_cotim = 1 or coalesce(ide_gemod,0) in ("+p_modulo_anticipos+","+p_modulo_factruracion+","+p_modulo_nota_credito+","+p_modulo_nota_debito+","+p_sec_ingresos+"))";
		if(com_anio.getValue()==null){
			
			tab_movimientos_contables.limpiar();
			utilitario.addUpdate("tab_movimientos_contables");
			return;

		}
		if(com_mes.getValue()!=null){
			meses_todos=com_mes.getValue().toString();
		}
		System.out.println("valor radio "+rad_aprobado.getValue());
		if(rad_aprobado.getValue().equals("true")){
			estado="true";
		}
		if(rad_aprobado.getValue().equals("false")){
			estado="false";
		}
		
		tab_movimientos_contables.setSql(ser_contabilidad.getMovimientosContablesSumaDebeHaberIngresoGasto(com_anio.getValue().toString(),meses_todos,estado,condicion));	
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
