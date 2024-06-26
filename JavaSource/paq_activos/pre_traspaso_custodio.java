package paq_activos;

import javax.ejb.EJB;

import org.primefaces.event.SelectEvent;

import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.ItemMenu;
import framework.componentes.BotonesCombo;
import paq_activos.ejb.ServicioActivos;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_gestion.ejb.ServicioGestion;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;

public class pre_traspaso_custodio extends Pantalla {
	private Tabla tab_traspaso = new Tabla();
	private Tabla tab_custodio= new Tabla();
	private Dialogo dia_traspaso_custodio=new Dialogo();
	private Tabla tab_tarspaso_Custodio=new Tabla();

	private AutoCompletar aut_empleado = new AutoCompletar();
	private SeleccionTabla set_custodio=new SeleccionTabla();
	private SeleccionTabla set_tabla=new SeleccionTabla();
	
	@EJB
	private ServicioGestion ser_gestion = (ServicioGestion) utilitario.instanciarEJB(ServicioGestion.class);
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	@EJB
	private ServicioActivos ser_activos=(ServicioActivos) utilitario.instanciarEJB(ServicioActivos.class);
	@EJB
	private ServicioContabilidad ser_Contabilidad= (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class); 
	
	public pre_traspaso_custodio(){
		bar_botones.getBot_insertar().setRendered(false);
		// boton limpiar
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");

		// autocompletar empleado
		aut_empleado.setId("aut_empleado");
		String str_sql_emp=ser_gestion.getSqlEmpleadosAutocompletar();
		aut_empleado.setAutoCompletar(str_sql_emp);
		aut_empleado.setMetodoChange("filtrarCustodio");

		Etiqueta eti_colaborador=new Etiqueta("CUSTODIO ACTUAL:");


		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_empleado);
		bar_botones.agregarBoton(bot_limpiar);
		
		
      /**DFJ**/
    BotonesCombo boc_seleccion_inversa = new BotonesCombo();
    ItemMenu itm_todas = new ItemMenu();
    ItemMenu itm_niguna = new ItemMenu();


    boc_seleccion_inversa.setValue("Selección Inversa");
        boc_seleccion_inversa.setIcon("ui-icon-circle-check");
        boc_seleccion_inversa.setMetodo("seleccinarInversa");
        boc_seleccion_inversa.setUpdate("tab_traspaso");
        itm_todas.setValue("Seleccionar Todo");
        itm_todas.setIcon("ui-icon-check");
        itm_todas.setMetodo("seleccionarTodas");
        itm_todas.setUpdate("tab_traspaso");
        boc_seleccion_inversa.agregarBoton(itm_todas);
        itm_niguna.setValue("Seleccionar Ninguna");
        itm_niguna.setIcon("ui-icon-minus");
        itm_niguna.setMetodo( "seleccionarNinguna");
        itm_niguna.setUpdate("tab_traspaso");
        boc_seleccion_inversa.agregarBoton(itm_niguna);


      
      
		tab_traspaso.setId("tab_traspaso");
		tab_traspaso.setSql("select detalle_afact,b.ide_afcus,serie_afact,modelo_afact,marca_afact,cod_barra_afcus,numero_acta_afcus," +
				 "fecha_entrega_afcus,apellido_paterno_gtemp,apellido_materno_gtemp,primer_nombre_gtemp,segundo_nombre_gtemp " +
				 "from afi_activo a,afi_custodio b, gen_empleados_departamento_par c, gth_empleado d where a.ide_afact=b.ide_afact " + 
				 " and b.ide_geedp=c.ide_geedp and c.ide_geedp=-1 and c.ide_gtemp=d.ide_gtemp order by fecha_entrega_afcus desc");
		tab_traspaso.setNumeroTabla(1);
		tab_traspaso.setCampoPrimaria("ide_afcus");
		
	tab_traspaso.setLectura(true);
	tab_traspaso.setTipoSeleccion(true);
	
	tab_traspaso.dibujar();
	tab_traspaso.imprimirSql();
	PanelTabla pat_panel=new PanelTabla();
   pat_panel.getChildren().add(boc_seleccion_inversa);

	pat_panel.setPanelTabla(tab_traspaso);
	Division div_division=new Division();
	div_division.dividir1(pat_panel);
	agregarComponente(div_division);
	
/////custodio
		Boton bot_custodio=new Boton();
		bot_custodio.setIcon("ui-icon-person");
		bot_custodio.setValue("Traspaso Custodio");
		bot_custodio.setMetodo("abrirDialogoCustodio");
		bar_botones.agregarBoton(bot_custodio);
	
	////selecion tabla
	dia_traspaso_custodio.setId("dia_traspaso_custodio");
	dia_traspaso_custodio.setTitle("TRASPASO CUSTODIO");
	dia_traspaso_custodio.setWidth("45%");
	dia_traspaso_custodio.setHeight("45%");
	Grid gri_cuerpo=new Grid();
	tab_tarspaso_Custodio.setId("tab_tarspaso_Custodio");
	tab_tarspaso_Custodio.setTabla("afi_custodio", "ide_afcus",10);
	tab_tarspaso_Custodio.setTipoFormulario(true);
	tab_tarspaso_Custodio.setCondicion("ide_afcus=-1");//para que aparesca vacia
	tab_tarspaso_Custodio.getGrid().setColumns(2);
	//oculto todos los campos
	tab_tarspaso_Custodio.getColumna("ide_afcus").setVisible(false);
	tab_tarspaso_Custodio.getColumna("ide_afact").setVisible(false);
	tab_tarspaso_Custodio.getColumna("gen_ide_geedp").setVisible(false);
	tab_tarspaso_Custodio.getColumna("detalle_afcus").setVisible(false);
	tab_tarspaso_Custodio.getColumna("cod_barra_afcus").setVisible(false);
	tab_tarspaso_Custodio.getColumna("nro_secuencial_afcus").setVisible(false);
	tab_tarspaso_Custodio.getColumna("activo_afcus").setVisible(false);
	tab_tarspaso_Custodio.getColumna("ide_geedp").setVisible(true);
	tab_tarspaso_Custodio.getColumna("ide_geedp").setNombreVisual("CUSTODIO NUEVO");
	tab_tarspaso_Custodio.getColumna("ide_geedp").setCombo(ser_nomina.servicioEmpleadoContrato("true,false"));
	tab_tarspaso_Custodio.getColumna("ide_geedp").setAutoCompletar();
	tab_tarspaso_Custodio.getColumna("fecha_entrega_afcus").setVisible(true);
	tab_tarspaso_Custodio.getColumna("fecha_entrega_afcus").setNombreVisual("FECHA ENTREGA");
	tab_tarspaso_Custodio.getColumna("fecha_entrega_afcus").setValorDefecto(utilitario.getFechaActual());
	tab_tarspaso_Custodio.getColumna("fecha_descargo_afcus").setVisible(true);
	tab_tarspaso_Custodio.getColumna("fecha_descargo_afcus").setNombreVisual("FECHA DESCARGA");
	tab_tarspaso_Custodio.getColumna("fecha_descargo_afcus").setValorDefecto(utilitario.getFechaActual());
	tab_tarspaso_Custodio.getColumna("numero_acta_afcus").setVisible(true);
	tab_tarspaso_Custodio.getColumna("numero_acta_afcus").setNombreVisual("NUMERO ACTA");
	//tab_tarspaso_Custodio.getColumna("numero_acta_afcus").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
	//tab_tarspaso_Custodio.getColumna("numero_acta_afcus").setValorDefecto("0");
	tab_tarspaso_Custodio.getColumna("razon_descargo_afcus").setVisible(true);
	tab_tarspaso_Custodio.getColumna("razon_descargo_afcus").setNombreVisual("RAZON DESCARGA");
	tab_tarspaso_Custodio.dibujar();

	gri_cuerpo.getChildren().add(tab_tarspaso_Custodio);
	
	dia_traspaso_custodio.getBot_aceptar().setMetodo("aceptarDialogoCustodio");

	dia_traspaso_custodio.setDialogo(gri_cuerpo);
	agregarComponente(dia_traspaso_custodio);

	
	}
	long ide_inicial=0;
	public void  aceptarDialogoCustodio(){
		String str_seleccionados=tab_traspaso.getFilasSeleccionadas();
		if(str_seleccionados.length()>0)
		{
			TablaGenerica tab_consulta_custodio= ser_activos.getTablaGenericaConsultaCustodio(str_seleccionados);
			//utilitario.getConexion().ejecutarSql("DELETE from SIS_BLOQUEO where upper(TABLA_BLOQ) like 'afi_custodio'");
			ide_inicial=utilitario.getConexion().getMaximo("afi_custodio", "ide_afcus", 1);
			for(int i=0;i<tab_consulta_custodio.getTotalFilas();i++){
				
						utilitario.getConexion().ejecutarSql("update afi_custodio set fecha_descargo_afcus= '"+tab_tarspaso_Custodio.getValor("fecha_descargo_afcus")+"' ,"
						+" razon_descargo_afcus= '"+tab_tarspaso_Custodio.getValor("razon_descargo_afcus")+"' ,"
						+" activo_afcus=false where ide_afcus="+tab_consulta_custodio.getValor(i, "ide_afcus"));
						
						ser_Contabilidad.limpiarAcceso("afi_custodio");
						utilitario.getConexion().ejecutarSql("insert into afi_custodio (ide_afcus,ide_afact,ide_geedp,detalle_afcus,fecha_entrega_afcus,numero_acta_afcus,cod_barra_afcus,nro_secuencial_afcus,activo_afcus,gen_ide_geedp)"
						+" values ( "+ide_inicial+","+tab_consulta_custodio.getValor(i, "ide_afact")+", "+tab_tarspaso_Custodio.getValor("ide_geedp")+",'"+tab_consulta_custodio.getValor(i, "detalle_afcus")+"','"
						+tab_tarspaso_Custodio.getValor("fecha_entrega_afcus")+"','"+tab_tarspaso_Custodio.getValor("numero_acta_afcus")+"','"+tab_consulta_custodio.getValor(i, "cod_barra_afcus")+"',"+tab_consulta_custodio.getValor(i, "nro_secuencial_afcus")
						+",true,"+tab_consulta_custodio.getValor(i,"ide_geedp")+" )");
				
				ide_inicial++;
			}
			dia_traspaso_custodio.cerrar();
			tab_traspaso.setSql("select detalle_afact,b.ide_afcus,serie_afact,modelo_afact,marca_afact,cod_barra_afcus,numero_acta_afcus," +
					 "fecha_entrega_afcus,apellido_paterno_gtemp,apellido_materno_gtemp,primer_nombre_gtemp,segundo_nombre_gtemp " +
					 "from afi_activo a,afi_custodio b, gen_empleados_departamento_par c, gth_empleado d where a.ide_afact=b.ide_afact " + 
					 " and b.ide_geedp=c.ide_geedp and c.ide_geedp="+aut_empleado.getValor()+" and c.ide_gtemp=d.ide_gtemp and activo_afcus=true order by fecha_entrega_afcus desc");
	
			tab_traspaso.ejecutarSql();
			utilitario.addUpdate("tab_traspaso");
			utilitario.agregarMensaje("Guardado", "Cambio de custodio realizado con exito");
		}
		else
		{
			utilitario.agregarMensajeInfo("Error", "Seleccione al menos un item.");
		}
	}
	public void abrirDialogoCustodio(){
		//Hace aparecer el componente
		if(aut_empleado.getValor()!=null){
			tab_tarspaso_Custodio.limpiar();
			tab_tarspaso_Custodio.insertar();
			//tab_direccion.limpiar();
		//	tab_direccion.insertar();
			dia_traspaso_custodio.dibujar();
		}
		else{
			utilitario.agregarMensaje("Inserte un Custodio", "");
		}

	}

/**DFJ**/
public void seleccionarTodas() {
        tab_traspaso.setSeleccionados(null);
        Fila seleccionados[] = new Fila[tab_traspaso.getTotalFilas()];
        for (int i = 0; i < tab_traspaso.getFilas().size(); i++) {
            seleccionados[i] = tab_traspaso.getFilas().get(i);
        }
        tab_traspaso.setSeleccionados(seleccionados);
}

/**DFJ**/
public void seleccinarInversa() {
        if (tab_traspaso.getSeleccionados() == null) {
            seleccionarTodas();
        } else if (tab_traspaso.getSeleccionados().length == tab_traspaso.getTotalFilas()) {
            seleccionarNinguna();
        } else {
            Fila seleccionados[] = new Fila[tab_traspaso.getTotalFilas() - tab_traspaso.getSeleccionados().length];
            int cont = 0;
            for (int i = 0; i < tab_traspaso.getFilas().size(); i++) {
                boolean boo_selecionado = false;
                for (int j = 0; j < tab_traspaso.getSeleccionados().length; j++) {
                    if (tab_traspaso.getSeleccionados()[j].equals(tab_traspaso.getFilas().get(i))) {
                        boo_selecionado = true;
                        break;
                    }
                }
                if (boo_selecionado == false) {
                    seleccionados[cont] = tab_traspaso.getFilas().get(i);
                    cont++;
                }
            }
            tab_traspaso.setSeleccionados(seleccionados);
        }
    }

/**DFJ**/
public void seleccionarNinguna() {
        tab_traspaso.setSeleccionados(null);
    }




	public void filtrarCustodio(SelectEvent evt){
		tab_traspaso.setSql("select detalle_afact,b.ide_afcus,serie_afact,modelo_afact,marca_afact,cod_barra_afcus,numero_acta_afcus," +
				 "fecha_entrega_afcus,apellido_paterno_gtemp,apellido_materno_gtemp,primer_nombre_gtemp,segundo_nombre_gtemp " +
				 "from afi_activo a,afi_custodio b, gen_empleados_departamento_par c, gth_empleado d where a.ide_afact=b.ide_afact " + 
				 " and b.ide_geedp=c.ide_geedp and c.ide_gtemp="+aut_empleado.getValor()+" and c.ide_gtemp=d.ide_gtemp and activo_afcus=true order by fecha_entrega_afcus desc");
        tab_traspaso.imprimirSql();
		tab_traspaso.ejecutarSql();
		utilitario.addUpdate("tab_traspaso");

		

	}
	/**
	 * limpia toda la pantalla incluyendo el autocompletar
	 */
	public void limpiar() {
		aut_empleado.limpiar();
		utilitario.addUpdate("aut_empleado");// limpia y refresca el autocompletar


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

	public Tabla getTab_traspaso() {
		return tab_traspaso;
	}

	public void setTab_traspaso(Tabla tab_traspaso) {
		this.tab_traspaso = tab_traspaso;
	}

	public AutoCompletar getAut_empleado() {
		return aut_empleado;
	}

	public void setAut_empleado(AutoCompletar aut_empleado) {
		this.aut_empleado = aut_empleado;
	}

	public SeleccionTabla getSet_custodio() {
		return set_custodio;
	}

	public void setSet_custodio(SeleccionTabla set_custodio) {
		this.set_custodio = set_custodio;
	}

	public Tabla getTab_custodio() {
		return tab_custodio;
	}

	public void setTab_custodio(Tabla tab_custodio) {
		this.tab_custodio = tab_custodio;
	}

	public Dialogo getDia_traspaso_custodio() {
		return dia_traspaso_custodio;
	}

	public void setDia_traspaso_custodio(Dialogo dia_traspaso_custodio) {
		this.dia_traspaso_custodio = dia_traspaso_custodio;
	}

	public Tabla getTab_tarspaso_Custodio() {
		return tab_tarspaso_Custodio;
	}

	public void setTab_tarspaso_Custodio(Tabla tab_tarspaso_Custodio) {
		this.tab_tarspaso_Custodio = tab_tarspaso_Custodio;
	}

}
