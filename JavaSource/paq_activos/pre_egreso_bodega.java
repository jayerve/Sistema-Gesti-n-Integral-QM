package paq_activos;

import javax.ejb.EJB;

import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.BotonesCombo;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.ItemMenu;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_sistema.aplicacion.Pantalla;

/*
 * La presente clase permite hacer la recepcion de los activos fijos egresados de bodega, para que el material 
 * pueda cumplir con las condiciones para ser recibidas por bodega el tipo de ingreso a bodega del material
 * debe ser tipo activo fijo ( BBDD tabla: bodt_bodega campo: tipo_ingreso_bobod=2) es asi tambien que en la BBDD
 * en la tabla bodt_egreso y bodt_concepto_egreso el campo activo debe poseer el valor de true, ya que
 * si tiene el valor false indica que el material ya fue ingresado a activos fijos.
 */
public class pre_egreso_bodega extends Pantalla{
	private Tabla tab_egreso= new Tabla();
	private SeleccionTabla set_egreso = new SeleccionTabla();
	private int int_maximo_detalle=-1;
	private Texto tex_maximo=new Texto();
	private Combo com_anio=new Combo();
	private Confirmar con_guardar=new Confirmar();
	




	@EJB
	private ServicioBodega ser_bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	
	
	
	public pre_egreso_bodega(){
		
		bar_botones.limpiar();
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
		
		Boton bot_egreso = new Boton();
		bot_egreso.setValue("Buscar Egresos de Bodega");
		bot_egreso.setTitle("EGRESOS DE BODEGA");
		bot_egreso.setIcon("ui-icon-person");
		bot_egreso.setMetodo("importarEgreso");
		bar_botones.agregarBoton(bot_egreso);
		
		Boton bot_recibir_activo=new Boton();
		bot_recibir_activo.setIcon("ui-icon-person");
		bot_recibir_activo.setValue("Recibir Activo");
		bot_recibir_activo.setMetodo("recibirActivo");
		bar_botones.agregarBoton(bot_recibir_activo);

		BotonesCombo boc_seleccion_inversa = new BotonesCombo();
		ItemMenu itm_todas = new ItemMenu();
		ItemMenu itm_niguna = new ItemMenu();

		boc_seleccion_inversa.setValue("Selección Inversa");
		boc_seleccion_inversa.setIcon("ui-icon-circle-check");
		boc_seleccion_inversa.setMetodo("seleccinarInversa");
		boc_seleccion_inversa.setUpdate("tab_egreso");
		itm_todas.setValue("Seleccionar Todo");
		itm_todas.setIcon("ui-icon-check");
		itm_todas.setMetodo("seleccionarTodas");
		itm_todas.setUpdate("tab_egreso");
		boc_seleccion_inversa.agregarBoton(itm_todas);
		itm_niguna.setValue("Seleccionar Ninguna");
		itm_niguna.setIcon("ui-icon-minus");
		itm_niguna.setMetodo( "seleccionarNinguna");
		itm_niguna.setUpdate("tab_egreso");
		boc_seleccion_inversa.agregarBoton(itm_niguna);
		
		//////egreso
		tab_egreso.setId("tab_egreso");
		tab_egreso.setSql(ser_bodega.getMaterialesPorEgreso("-1", "true,false"));
		tab_egreso.setNumeroTabla(1);
		tab_egreso.setCampoPrimaria("ide_boegr");
		tab_egreso.getColumna("codigo_bomat").setNombreVisual("CODIGO DEL MATERIAL");
		tab_egreso.getColumna("descripcion_bobod").setNombreVisual("DETALLE DE INGRESO BODEGA");
		tab_egreso.getColumna("marca_bobod").setNombreVisual("MARCA");
		tab_egreso.getColumna("modelo_bobod").setNombreVisual("MODELO");
		tab_egreso.getColumna("serie_bobod").setNombreVisual("SERIE");
		tab_egreso.getColumna("color_bobod").setNombreVisual("COLOR");
		tab_egreso.getColumna("detalle_bomat").setNombreVisual("NOMBRE DEL MATERIAL");
		tab_egreso.getColumna("fecha_egreso_boegr").setNombreVisual("FECHA DE EGRESO");
		tab_egreso.getColumna("cantidad_egreso_boegr").setNombreVisual("CANTIDAD DE EGRESO");
		tab_egreso.getColumna("documento_egreso_boegr").setNombreVisual("DOCUMENTO DE EGRESO");
		tab_egreso.getColumna("fecha_compra_bobod").setNombreVisual("FECHA COMPRA");
		tab_egreso.getColumna("num_factura_bobod").setNombreVisual("NUMERO DE FACTURA");
		tab_egreso.getColumna("nombre_tepro").setNombreVisual("PROVEEDOR");
		tab_egreso.getColumna("ide_bocoe").setVisible(false);
		tab_egreso.getColumna("ide_tepro").setVisible(false);

		tab_egreso.setLectura(true);
		tab_egreso.setTipoSeleccion(true);
		tab_egreso.dibujar();
		PanelTabla pat_panel=new PanelTabla();
		pat_panel.getChildren().add(boc_seleccion_inversa);
		pat_panel.setPanelTabla(tab_egreso);
		pat_panel.getMenuTabla().getItem_formato().setDisabled(true);

		Division div_division=new Division();
		div_division.dividir1(pat_panel);
		agregarComponente(div_division);
		
		
		
		set_egreso.setId("set_egreso");
		set_egreso.setSeleccionTabla(ser_bodega.getEgresoBodegaActivos("-1"),"ide_bocoe");
		set_egreso.getTab_seleccion().getColumna("numero_egreso_bocoe").setNombreVisual("Nro. EGRESO BODEGA");
		set_egreso.getTab_seleccion().getColumna("fecha_egreso_bocoe").setNombreVisual("FECHA EGRESO");
		set_egreso.getTab_seleccion().getColumna("uso_bocoe").setNombreVisual("USO");
		set_egreso.getTab_seleccion().getColumna("numero_egreso_bocoe").setFiltro(true);
		set_egreso.getTab_seleccion().getColumna("fecha_egreso_bocoe").setFiltro(true);
		set_egreso.getTab_seleccion().getColumna("uso_bocoe").setFiltro(true);
		set_egreso.getTab_seleccion().getColumna("uso_bocoe").setLongitud(100);
		

		set_egreso.getBot_aceptar().setMetodo("aceptarEgreso");
		set_egreso.getTab_seleccion().ejecutarSql();
		set_egreso.setRadio();
		agregarComponente(set_egreso);
		
		con_guardar.setId("con_guardar");
		con_guardar.setMessage("ESTA SEGURO DE INGRESAR LOS MATERIALES SELECCIONADOS AL INVENTARIO DE ACTIVOS FIJOS");
		con_guardar.setTitle("CONFIRMACION DE INGRESO");

		agregarComponente(con_guardar);


			
	}
	///////EGRESO BODEGA
	public void importarEgreso (){
		if(com_anio.getValue()!=null){
			set_egreso.getTab_seleccion().setSql(ser_bodega.getEgresoBodegaActivos(com_anio.getValue().toString()));
			set_egreso.getTab_seleccion().ejecutarSql();
			set_egreso.dibujar();
		}
		else{
			utilitario.agregarMensajeInfo("Selecione un año", "");

		}
	}
	
	public void recibirActivo(){
		
		if(tab_egreso.getTotalFilas()==0){
			utilitario.agregarMensaje("No existen Registros", "Envie a buscar un egreso de bodega");
			return;
		}
		if (tab_egreso.getListaFilasSeleccionadas().size()==0){
			utilitario.agregarMensajeInfo("Seleccione un registro", "Debe seleccionar al menos un registro");
			return;
		}
		String str_seleccionados=tab_egreso.getFilasSeleccionadas();
		System.out.println(" probando el str_Selccionado "+str_seleccionados);
		if(str_seleccionados!=null || !str_seleccionados.isEmpty()){
			
			if (!con_guardar.isVisible()){
				// dibuja dialogo de confirmacion de recepcion de activjvos fijos
				con_guardar.setMessage("ESTA SEGURO DE INGRESAR LOS MATERIALES SELECCIONADOS AL INVENTARIO DE ACTIVOS FIJOS");
				con_guardar.setTitle("CONFIRMACION DE INGRESO");
				con_guardar.getBot_aceptar().setMetodo("recibirActivo");
				con_guardar.dibujar();
				utilitario.addUpdate("con_guardar");
			}else{
				con_guardar.cerrar();
			
			TablaGenerica material_egreso=utilitario.consultar(ser_bodega.getMaterialesEgresoCodigo(str_seleccionados, "true,false"));
			utilitario.getConexion().ejecutarSql("DELETE from SIS_BLOQUEO where upper(TABLA_BLOQ) like 'afi_activo'");
			TablaGenerica valor=utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("afi_activo", "ide_afact"));
			ide_inicial=Long.parseLong(valor.getValor("codigo"));
			for(int i=0;i<material_egreso.getTotalFilas();i++){
				System.out.println(" entwr e afor 0"+i);
				utilitario.getConexion().ejecutarSql("insert into afi_activo (ide_afact,marca_afact,serie_afact,modelo_afact,color_afact,cantidad_afact,valor_unitario_afact,egreso_bodega_afact,"
						+" fecha_alta_afact,valor_neto_afact,valor_compra_afact,activo_afact,secuencial_afact,ide_boegr)"
						+" values ("+ide_inicial+",'"+material_egreso.getValor(i, "marca_bobod")+"','"+material_egreso.getValor(i, "serie_bobod")+"','"+material_egreso.getValor(i, "modelo_bobod")+"','"+material_egreso.getValor(i, "color_bobod")
						+"',"+material_egreso.getValor(i, "cantidad_egreso_boegr")+","+material_egreso.getValor(i, "valor_unitario_bobod")+",'"+material_egreso.getValor(i, "documento_egreso_boegr")+"','"+material_egreso.getValor(i, "fecha_compra_bobod")
						+"',"+material_egreso.getValor(i, "valor_unitario_bobod")+","+material_egreso.getValor(i, "valor_total_bobod")+",true,"+material_egreso.getValor(i, "cantidad_egreso_boegr")+","+material_egreso.getValor(i, "ide_boegr")+");");		
				utilitario.getConexion().ejecutarSql("update bodt_egreso set activo_boegr=false where ide_boegr="+material_egreso.getValor(i, "ide_boegr"));
				// revisa si existe detalle de materiales por reciobir si no existe actualiza la cabecera del egreso con estado false
				TablaGenerica revisar_materiales =utilitario.consultar(" select * from bodt_egreso where ide_bocoe in (select ide_bocoe from bodt_egreso where ide_boegr="+material_egreso.getValor(i, "ide_boegr")+") and activo_boegr=true");
				if(revisar_materiales.getTotalFilas()>0){
				}
				else{
					utilitario.getConexion().ejecutarSql("update bodt_concepto_egreso set activo_bocoe=false where ide_bocoe in (select ide_bocoe from bodt_egreso where ide_boegr="+material_egreso.getValor(i, "ide_boegr")+") ");
				}
				ide_inicial++;
			}
			tab_egreso.ejecutarSql();
			utilitario.addUpdate("tab_egreso");
			}
		}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
			return;		
		}
		

	}
	
	
////aceptar
	
	public void aceptarEgreso() {
		String str_seleccionados=set_egreso.getValorSeleccionado();
		tab_egreso.setSql(ser_bodega.getMaterialesPorEgreso(str_seleccionados, "true"));
		tab_egreso.ejecutarSql();
		tab_egreso.imprimirSql();
		set_egreso.cerrar();
	}
	


	
	
	
	
long ide_inicial=0;
public void seleccionarTodas() {
	tab_egreso.setSeleccionados(null);
    Fila seleccionados[] = new Fila[tab_egreso.getTotalFilas()];
    for (int i = 0; i < tab_egreso.getFilas().size(); i++) {
        seleccionados[i] = tab_egreso.getFilas().get(i);
    }
    tab_egreso.setSeleccionados(seleccionados);
}

/**DFJ**/
public void seleccinarInversa() {
        if (tab_egreso.getSeleccionados() == null) {
            seleccionarTodas();
        } else if (tab_egreso.getSeleccionados().length == tab_egreso.getTotalFilas()) {
            seleccionarNinguna();
        } else {
            Fila seleccionados[] = new Fila[tab_egreso.getTotalFilas() - tab_egreso.getSeleccionados().length];
            int cont = 0;
            for (int i = 0; i < tab_egreso.getFilas().size(); i++) {
                boolean boo_selecionado = false;
                for (int j = 0; j < tab_egreso.getSeleccionados().length; j++) {
                    if (tab_egreso.getSeleccionados()[j].equals(tab_egreso.getFilas().get(i))) {
                        boo_selecionado = true;
                        break;
                    }
                }
                if (boo_selecionado == false) {
                    seleccionados[cont] = tab_egreso.getFilas().get(i);
                    cont++;
                }
            }
            tab_egreso.setSeleccionados(seleccionados);
        }
    }

/**DFJ**/
public void seleccionarNinguna() {
	tab_egreso.setSeleccionados(null);
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
	public Tabla getTab_egreso() {
		return tab_egreso;
	}
	public void setTab_egreso(Tabla tab_egreso) {
		this.tab_egreso = tab_egreso;
	}
	public SeleccionTabla getSet_egreso() {
		return set_egreso;
	}
	public void setSet_egreso(SeleccionTabla set_egreso) {
		this.set_egreso = set_egreso;
	}
	public Confirmar getCon_guardar() {
		return con_guardar;
	}
	public void setCon_guardar(Confirmar con_guardar) {
		this.con_guardar = con_guardar;
	}

	

}
