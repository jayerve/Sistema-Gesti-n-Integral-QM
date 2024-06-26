/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_general;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.SelectEvent;

import paq_sistema.aplicacion.Pantalla;

public class pre_area_departamento extends Pantalla {

	private Tabla tab_tabla1 = new Tabla();
	private Tabla tab_tabla2 = new Tabla();
	private Tabla tab_tabla3 = new Tabla();

	//	private Tabla tab_tabla3 = new Tabla();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte sef_reporte = new SeleccionFormatoReporte();
	private SeleccionTabla set_sucursal = new SeleccionTabla();

	public pre_area_departamento() {
		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		bar_botones.agregarReporte();


		tab_tabla1.setId("tab_tabla1");
		tab_tabla1.setTabla("GEN_AREA", "IDE_GEARE", 1);
		tab_tabla1.getColumna("ide_padre_geare").setCombo("GEN_AREA", "IDE_GEARE", "DETALLE_GEARE", "");
		tab_tabla1.getColumna("ACTIVO_GEARE").setCheck();
		tab_tabla1.getColumna("ACTIVO_GEARE").setValorDefecto("true");

		tab_tabla1.onSelect("seleccionarTabla1");
		tab_tabla1.agregarRelacion(tab_tabla2);
		tab_tabla1.dibujar();
		PanelTabla pat_panel1 = new PanelTabla();
		pat_panel1.setMensajeWarn("ÁREAS");
		pat_panel1.setPanelTabla(tab_tabla1);

		tab_tabla2.setId("tab_tabla2");
		tab_tabla2.setTabla("GEN_DEPARTAMENTO", "IDE_GEDEP", 2); //calve primaria compuesta
		tab_tabla2.getColumna("IDE_GEDEP").setEtiqueta();
		tab_tabla2.getColumna("IDE_GEARE").setVisible(false);
		tab_tabla2.getColumna("ACTIVO_GEDEP").setCheck();
		tab_tabla2.getColumna("GEN_IDE_GEDEP").setCombo("GEN_DEPARTAMENTO", "IDE_GEDEP", "DETALLE_GEDEP", "");
		tab_tabla2.getColumna("GEN_IDE_GEDEP").setLongitud(150);
		tab_tabla2.getColumna("GEN_IDE_GEDEP").setAutoCompletar();

		List< Object[]> lis_organico = new ArrayList<Object[]>();
		lis_organico.add(new Object[]{"2", "Área"});		
		lis_organico.add(new Object[]{"4", "Apoyo"});
		lis_organico.add(new Object[]{"3", "Division"});
		lis_organico.add(new Object[]{"1", "Gerencia"});		
		tab_tabla2.getColumna("NIVEL_ORGANICO_GEDEP").setCombo(lis_organico);
		tab_tabla2.getColumna("NIVEL_ORGANICO_GEDEP").setPermitirNullCombo(false);
		//tab_tabla2.getColumna("NIVEL_GEDEP").setRequerida(true);		

		tab_tabla2.setValidarInsertar(true);//Para que solo inserte de una en una        
		tab_tabla2.onSelect("seleccionarTabla2");

		List< Object[]> lis_tipos = new ArrayList<Object[]>();
		lis_tipos.add(new Object[]{"Centro", "Centro"});
		lis_tipos.add(new Object[]{"Derecha", "Derecha"});
		lis_tipos.add(new Object[]{"Izquierda", "Izquierda"});
		lis_tipos.add(new Object[]{"Nodo", "Nodo"});
		tab_tabla2.getColumna("TIPO_GEDEP").setCombo(lis_tipos);
		tab_tabla2.getColumna("TIPO_GEDEP").setPermitirNullCombo(false);
		tab_tabla2.getColumna("TIPO_GEDEP").setMetodoChange("cambioTipo");


		List< Object[]> lis_posicion = new ArrayList<Object[]>();
		lis_posicion.add(new Object[]{"Vertical", "Vertical"});
		lis_posicion.add(new Object[]{"Horizontal", "Horizontal"});
		tab_tabla2.getColumna("POSICION_HIJOS_GEDEP").setCombo(lis_posicion);
		tab_tabla2.getColumna("POSICION_HIJOS_GEDEP").setPermitirNullCombo(false);

		tab_tabla2.dibujar();
		PanelTabla pat_panel2 = new PanelTabla();
		pat_panel2.setMensajeWarn("DEPARTAMENTOS POR AREAS");
		pat_panel2.setPanelTabla(tab_tabla2);


		tab_tabla3.setId("tab_tabla3");
		tab_tabla3.setGenerarPrimaria(false);
		tab_tabla3.setTabla("GEN_DEPARTAMENTO_SUCURSAL", "IDE_SUCU", 3);
		tab_tabla3.getColumna("IDE_GEDEP").setVisible(false);
		tab_tabla3.getColumna("IDE_GEARE").setVisible(false);
		tab_tabla3.getColumna("IDE_GEDEP").setUnico(true);
		tab_tabla3.getColumna("IDE_GEARE").setUnico(true);
		tab_tabla3.getColumna("IDE_SUCU").setUnico(true);
		tab_tabla3.getColumna("IDE_SUCU").setCombo("SIS_SUCURSAL", "IDE_SUCU", "NOM_SUCU","");
		tab_tabla3.getColumna("ACTIVO_GEDES").setCheck();
		tab_tabla3.getColumna("ACTIVO_GEDES").setValorDefecto("true");
		tab_tabla3.getColumna("IDE_GEESO").setVisible(false);

		tab_tabla3.setMostrarcampoSucursal(true);
		tab_tabla3.setRows(10);
		tab_tabla3.setCondicion("IDE_GEARE="+tab_tabla1.getValor("IDE_GEARE")+" and IDE_GEDEP="+tab_tabla2.getValor("IDE_GEDEP"));
		tab_tabla3.dibujar();
		tab_tabla3.getColumna("IDE_SUCU").setExterna(false);
		PanelTabla pat_sucursal = new PanelTabla();
		pat_sucursal.setMensajeWarn("SUCURSAL / AREA / DEPARTAMENTO");
		pat_sucursal.setPanelTabla(tab_tabla3);
		
	/*
				tab_tabla3.setId("tab_tabla3");
				tab_tabla3.setGenerarPrimaria(false);
				tab_tabla3.setTabla("GEN_DEPARTAMENTO_SUCURSAL", "IDE_SUCU,IDE_GEDEP,IDE_GEARE", 3);//clave primaria compuesta
				tab_tabla3.setMostrarcampoSucursal(true);
				tab_tabla3.setValidarInsertar(true);//Para que solo inserte de una en una
				tab_tabla3.getColumna("ide_sucu").setCombo("SIS_SUCURSAL", "IDE_SUCU", "NOM_SUCU", "");
				tab_tabla3.getColumna("ide_gedep").setVisible(false);
				tab_tabla3.getColumna("ide_geare").setVisible(false);
				filtrarSucursales();
				tab_tabla3.dibujar();
				PanelTabla pat_panel3 = new PanelTabla();
				pat_panel3.setMensajeWarn("SUCURSALES POR DEPARTAMENTOS ");
				pat_panel3.setPanelTabla(tab_tabla3);
*/
		//		Division div_vertical = new Division();
		//		div_vertical.dividir2(pat_panel2, pat_panel3, "65%", "V");

		Division div_division = new Division();
		div_division.setId("div_division");
		//div_division.dividir2(pat_panel1, pat_panel2, "50%", "H");
		div_division.dividir3(pat_panel1, pat_panel2,pat_sucursal, "30%", "30%", "H");
		agregarComponente(div_division);


		sef_reporte.setId("sef_reporte");
		agregarComponente(rep_reporte);
		agregarComponente(sef_reporte);
		set_sucursal.setId("set_sucursal");
		set_sucursal.setSeleccionTabla("SIS_SUCURSAL", "IDE_SUCU", "NOM_SUCU");
		set_sucursal.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(set_sucursal);

	}


	@Override
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}
	Map p_parametros= new HashMap();
	public void aceptarReporte(){
		if (rep_reporte.getReporteSelecionado().equals("Detalle Areas y Departamentos")){
			if (rep_reporte.isVisible()){
				p_parametros=new HashMap();				
				rep_reporte.cerrar();				
				p_parametros.put("titulo", " AREAS Y DEPARTAMENTOS ");
				utilitario.addUpdate("rep_reporte,set_sucursal");
				sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());						
				sef_reporte.dibujar();
				utilitario.addUpdate("sef_reporte");
				//
			}}
	}

	public void cambioTipo(AjaxBehaviorEvent evt){
		tab_tabla2.modificar(evt);
		if(tab_tabla2.getValor("TIPO_GEDEP").equalsIgnoreCase("Hijo")){
			tab_tabla2.setValor("NIVEL_ORGANICO_GEDEP", "4");
			utilitario.addUpdateTabla(tab_tabla2,"NIVEL_ORGANICO_GEDEP", "");
		}
	}


	public Reporte getRep_reporte() {
		return rep_reporte;
	}

	public void setRep_reporte(Reporte rep_reporte) {
		this.rep_reporte = rep_reporte;
	}

	public SeleccionFormatoReporte getSef_reporte() {
		return sef_reporte;
	}

	public void setSef_reporte(SeleccionFormatoReporte sef_reporte) {
		this.sef_reporte = sef_reporte;
	}

	/**
	 * Valida que los datos ingrseados en departamentos sean correctos para que se puedan graficar 
	 */
	private String validar(){
		String str_mensaje="";

		if(tab_tabla2.isEmpty()==false && tab_tabla2.getValor("NIVEL_GEDEP").equals("0")){
			TablaGenerica tab_nodos = utilitario.consultar("SELECT * FROM GEN_DEPARTAMENTO WHERE ACTIVO_GEDEP=true AND NIVEL_GEDEP=0 order by NIVEL_GEDEP,TIPO_GEDEP,ORDEN_GEDEP");
			//Valido q solo exista un solo nodo raiz			
			if(!tab_nodos.isEmpty()){
				//Ya existe un nivel raiz no puede grabar otro
				if(tab_tabla2.isFilaInsertada()){
					str_mensaje="Ya existe un nivel 0, solo puede existir un nivel 0";
					return str_mensaje;
				}
			}
			if(tab_tabla2.getValor("NIVEL_GEDEP").equalsIgnoreCase("0")){
				if(tab_tabla2.getValor("TIPO_GEDEP").equalsIgnoreCase("CENTRO")){
					if(tab_tabla2.getValor("GEN_IDE_GEDEP")==null){
					}
					else{
						str_mensaje="El nivel 0 no puede tener valor en el campo padre";	
					}
				}
				else{
					str_mensaje="El nivel 0 debe tener como tipo Centro";            		  
				}
				if(tab_tabla2.getValor("NIVEL_ORGANICO_GEDEP")==null || !tab_tabla2.getValor("NIVEL_ORGANICO_GEDEP").equalsIgnoreCase("1")){
					str_mensaje="El nivel 0 debe tener como nivel organico Gerencia";
				}		
			}
			else if(tab_tabla2.getValor("NIVEL_ORGANICO_GEDEP").equalsIgnoreCase("3") && !(tab_tabla2.getValor("TIPO_GEDEP").equalsIgnoreCase("IZQUIERDA") || tab_tabla2.getValor("TIPO_GEDEP").equalsIgnoreCase("DERECHA"))){ //validaciones apoyo
				str_mensaje="El Nivel Orgánico de Apoyo debe ser de tipo Izquierda o Derecha";

			}
			else if(tab_tabla2.getValor("NIVEL_ORGANICO_GEDEP").equalsIgnoreCase("4") && !tab_tabla2.getValor("TIPO_GEDEP").equalsIgnoreCase("Hijo")){ //validaciones Division				
				str_mensaje="El Nivel Orgánico de División debe ser de tipo Hijo";
			}
			else if(tab_tabla2.getValor("TIPO_GEDEP").equalsIgnoreCase("Hijo")  ){ //validaciones Hijo
				if(tab_tabla2.getValor("NIVEL_ORGANICO_GEDEP").equalsIgnoreCase("4")){
					if(tab_tabla2.getValor("GEN_IDE_GEDEP")==null){
						str_mensaje="Cuando es de tipo Hijo debe tener un valor en el campo padre";
					}
					else{
						if(tab_tabla2.getValorSeleccionado()!=null){
							//Valida que el padre no sea el mismo que esta seleccionado
							if(tab_tabla2.getValor("GEN_IDE_GEDEP").equals(tab_tabla2.getValorSeleccionado())){
								str_mensaje="El valor en el campo padre no puede ser el registro seleccionado";	
							}	
						}

					}

				}
				else{
					str_mensaje="El tipo Hijo debe ser de Nivel Orgánico División";
				}
			}
		}
		return str_mensaje;
	}




	@Override
	public void insertar() {
		if (tab_tabla1.isFocus()) {
			tab_tabla1.insertar();
			tab_tabla2.limpiar();
			//			tab_tabla3.limpiar();
		} else if (tab_tabla2.isFocus()) {
			//Inserta solo si la fila seleccionada de la tabla de areas tiene una clave primaria 
			if (tab_tabla1.isFilaInsertada() == false && tab_tabla1.isEmpty() == false) {
				tab_tabla2.getColumna("IDE_GEARE").setValorDefecto(tab_tabla1.getValor("IDE_GEARE"));
				tab_tabla2.insertar();
			} else {
				utilitario.agregarMensajeInfo("No se puede insertar ", "Debe guardar la tabla de Áreas ");
			}
		}  else if (tab_tabla3.isFocus()) {
			if (tab_tabla2.isFilaInsertada() == false && tab_tabla2.isEmpty() == false) {
			tab_tabla3.insertar();
			tab_tabla3.setValor("IDE_GEARE", tab_tabla1.getValor("IDE_GEARE"));
			tab_tabla3.setValor("IDE_GEDEP", tab_tabla2.getValor("IDE_GEDEP"));
			}else {
				utilitario.agregarMensajeInfo("No se puede insertar ", "Debe guardar la tabla de Departamentos");
			}
		} 
		
		
	}

	/**
	 * Filtra sucursales y departamentos del area seleccionada *
	 */
	private void filtrosTabla1() {
		tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
		filtrarSucursales();
		//		tab_tabla3.ejecutarSql();
	}

	/**
	 * Se ejecuta cuando se selecciona una fila de la tabla1 de Areas
	 *
	 * @param evt
	 */
	public void seleccionarTabla1(SelectEvent evt) {
		tab_tabla1.seleccionarFila(evt);
		filtrosTabla1();
		filtrarDepartamentoSucursal();
	}

	public void filtrarDepartamentoSucursal(){
		//tab_area_dep.seleccionarFila(evt);
		tab_tabla3.setCondicion("IDE_GEARE="+tab_tabla1.getValor("IDE_GEARE")+" and IDE_GEDEP="+tab_tabla2.getValor("IDE_GEDEP"));
		tab_tabla3.ejecutarSql();
	}
	
	
	/**
	 * Se ejecuta cuando un componente de la tabla1 de cualquier fila gana el
	 * foco
	 *
	 * @param evt
	 */
	public void seleccionarTabla1(AjaxBehaviorEvent evt) {
		tab_tabla1.seleccionarFila(evt);
		filtrosTabla1();
	}

	/**
	 * Filtra las sucursales del departamento seleccionado
	 */
	private void filtrosTabla2() {
		filtrarSucursales();
		//		tab_tabla3.ejecutarSql();
	}

	/**
	 * Se ejecuta cuando se selecciona una fila de la tabla2
	 *
	 * @param evt
	 */
	public void seleccionarTabla2(SelectEvent evt) {
		tab_tabla2.seleccionarFila(evt);
		filtrosTabla2();
	}

	/**
	 * Se ejecuta cuando un componente de la tabla2 de cualquier fila gana el
	 * foco
	 *
	 * @param evt
	 */
	public void seleccionarTabla2(AjaxBehaviorEvent evt) {
		tab_tabla2.seleccionarFila(evt);
		filtrosTabla2();
	}

	@Override
	public void guardar() {
		
		
		if (tab_tabla1.isFocus()){
			tab_tabla1.guardar();
			guardarPantalla();
			
		
			
			if (tab_tabla1.getValor("ide_padre_geare")!=null || tab_tabla1.getValor("ide_padre_geare").equals("") ||  !tab_tabla1.getValor("ide_padre_geare").isEmpty()) {
				TablaGenerica tabArea= utilitario.consultar("SELECT IDE_GEARE,DETALLE_GEARE,IDE_PADRE_GEARE,ide_nivel_geare "
						+ "FROM "
						+ "GEN_AREA "
						+ "WHERE ACTIVO_GEARE=TRUE "
						+ "AND IDE_GEARE="+tab_tabla1.getValor("ide_padre_geare")
						+ " ORDER BY IDE_GEARE ASC");
							
				
				if (tabArea.getTotalFilas()>0) {
				if (tabArea.getValor("ide_nivel_geare").equals("0")) {
					
					TablaGenerica tabEstructuraOrganizacionalValidacion= utilitario.consultar("SELECT ide_geeso, pre_ide_geeso, detalle_geeso, activo_geeso, "
							+ "ide_geare, ide_gedep "
							+ "FROM gen_estructura_organizacional  "
							+ "where ide_geare="+tab_tabla1.getValor("IDE_GEARE")
							+ " and ide_gedep is null "
							+ " order by ide_geeso desc "
							+ " limit 1");
					
					if (tabEstructuraOrganizacionalValidacion.getTotalFilas()>0) {
						
						if (!tab_tabla1.getValor("DETALLE_GEARE").equals(tabEstructuraOrganizacionalValidacion.getValor("DETALLE_GEESO"))) {
							utilitario.getConexion().ejecutarSql("update gen_estructura_organizacional set DETALLE_GEESO='"+tab_tabla1.getValor("DETALLE_GEARE")+"' where ide_GEESO=" + tabEstructuraOrganizacionalValidacion.getValor("IDE_GEESO"));
						}
						
					
					
					}else {
						insertarEstructuraOrganizacionalArea(0, tab_tabla1.getValor("DETALLE_GEARE"), true, Integer.parseInt(tab_tabla1.getValorSeleccionado()));				
					
					
					TablaGenerica tabEstructuraOrganizacional= utilitario.consultar("SELECT ide_geeso, pre_ide_geeso, detalle_geeso, activo_geeso, "
							+ "ide_geare, ide_gedep "
							+ "FROM gen_estructura_organizacional  "
							+ "order by ide_geeso desc "
							+ "limit 1");
					
					TablaGenerica tabAnioValidar= utilitario.consultar("select ide_geani,detalle_geani,activo_geani " +
							" from gen_anio where activo_geani in(true)" +
							" and bloqueado_geani in (false) " +
							" order by detalle_geani desc "
							+ "limit 1") ;
					
					insertarEstructuraOrganizacionalVigente(Integer.parseInt(tabAnioValidar.getValor("IDE_GEANI")), true, Integer.parseInt(tabEstructuraOrganizacional.getValor("IDE_GEESO")));
					}
					
				}else {
					TablaGenerica tabEstructura=null;
					
					
					TablaGenerica tabEstructuraOrganizacionalValidacion= utilitario.consultar("SELECT ide_geeso, pre_ide_geeso, detalle_geeso, activo_geeso, "
							+ "ide_geare, ide_gedep "
							+ "FROM gen_estructura_organizacional  "
							+ "where ide_geare="+tab_tabla1.getValor("IDE_GEARE")
							+ " and ide_gedep is null "
							+ " order by ide_geeso desc "
							+ " limit 1");
					
					if (tabEstructuraOrganizacionalValidacion.getTotalFilas()>0) {
						if (!tab_tabla1.getValor("DETALLE_GEARE").equals(tabEstructuraOrganizacionalValidacion.getValor("DETALLE_GEESO"))) {
							utilitario.getConexion().ejecutarSql("update gen_estructura_organizacional set DETALLE_GEESO='"+tab_tabla1.getValor("DETALLE_GEARE")+"' where ide_GEESO=" + tabEstructuraOrganizacionalValidacion.getValor("IDE_GEESO"));
						}
						
					
					
					
					}else {
						
					
		
					
					
					tabEstructura= utilitario.consultar("SELECT ide_geeso, pre_ide_geeso, detalle_geeso, activo_geeso, "
							+ "ide_geare, ide_gedep "
							+ "FROM gen_estructura_organizacional  "
							+ "Where ide_geare="+tab_tabla1.getValor("ide_padre_geare")+" and ide_gedep is null");
					
					insertarEstructuraOrganizacionalArea(Integer.parseInt(tabEstructura.getValor("IDE_GEESO")), tab_tabla1.getValor("DETALLE_GEARE"), true, Integer.parseInt(tab_tabla1.getValorSeleccionado()));		
				
					TablaGenerica tabEstructuraOrganizacional= utilitario.consultar("SELECT ide_geeso, pre_ide_geeso, detalle_geeso, activo_geeso, "
							+ "ide_geare, ide_gedep "
							+ "FROM gen_estructura_organizacional  "
							+ "order by ide_geeso desc "
							+ "limit 1");
					
					TablaGenerica tabAnioValidar= utilitario.consultar("select ide_geani,detalle_geani,activo_geani " +
							" from gen_anio where activo_geani in(true)" +
							" and bloqueado_geani in (false) " +
							" order by detalle_geani desc "
							+ "limit 1") ;
					
					insertarEstructuraOrganizacionalVigente(Integer.parseInt(tabAnioValidar.getValor("IDE_GEANI")), true, Integer.parseInt(tabEstructuraOrganizacional.getValor("IDE_GEESO")));
			
				
					}
				}	
				}
			}

	
		
		
		
		}else if(tab_tabla2.isFocus()){
		tab_tabla2.guardar();
		guardarPantalla();
		TablaGenerica tabEstructura=null;

		
		TablaGenerica tabEstructuraOrganizacionalValidacion= utilitario.consultar("SELECT ide_geeso, pre_ide_geeso, detalle_geeso, activo_geeso, "
				+ "ide_geare, ide_gedep "
				+ "FROM gen_estructura_organizacional  "
				+ "where ide_geare="+tab_tabla2.getValor("IDE_GEARE")
				+ " and ide_gedep="+tab_tabla2.getValor("IDE_GEDEP")
				+ " order by ide_geeso desc "
				+ " limit 1");
		
		if (tabEstructuraOrganizacionalValidacion.getTotalFilas()>0) {
			if (!tab_tabla2.getValor("DETALLE_GEDEP").equals(tabEstructuraOrganizacionalValidacion.getValor("DETALLE_GEESO"))) {
			utilitario.getConexion().ejecutarSql("update gen_estructura_organizacional set DETALLE_GEESO='"+tab_tabla2.getValor("DETALLE_GEDEP")+"' where ide_GEESO=" + tabEstructuraOrganizacionalValidacion.getValor("IDE_GEESO"));
				
			}else {
				
						
			}
			
		}else {
		
		
		
		tabEstructura= utilitario.consultar("SELECT ide_geeso, pre_ide_geeso, detalle_geeso, activo_geeso, "
				+ "ide_geare, ide_gedep "
				+ "FROM gen_estructura_organizacional  "
				+ "Where ide_geare="+tab_tabla2.getValor("ide_geare")+" and ide_gedep is null");
		
insertarEstructuraOrganizacionalDepartamento(Integer.parseInt(tabEstructura.getValor("IDE_GEESO")), tab_tabla2.getValor("DETALLE_GEDEP"), true, 
Integer.parseInt(tab_tabla2.getValor("ide_geare")), Integer.parseInt(tab_tabla2.getValor("ide_gedep")));


TablaGenerica tabEstructuraDepart=null;

tabEstructuraDepart= utilitario.consultar("SELECT ide_geeso, pre_ide_geeso, detalle_geeso, activo_geeso, "
+ "ide_geare, ide_gedep "
+ "FROM gen_estructura_organizacional  "
+ "Where ide_gedep is not null "
+ "order by ide_geeso desc "
+ "limit 1");

TablaGenerica tabAnioValidar= utilitario.consultar("select ide_geani,detalle_geani,activo_geani " +
" from gen_anio where activo_geani in(true)" +
" and bloqueado_geani in (false) " +
" order by detalle_geani desc "
+ "limit 1") ;
insertarEstructuraOrganizacionalVigente(Integer.parseInt(tabAnioValidar.getValor("IDE_GEANI")), true, Integer.parseInt(tabEstructuraDepart.getValor("IDE_GEESO")));		
		}
		
		}
	else if(tab_tabla3.isFocus()){
		tab_tabla3.guardar();
		guardarPantalla();
	

TablaGenerica tabEstructuraDepart=null;

tabEstructuraDepart= utilitario.consultar("SELECT ide_geeso, pre_ide_geeso, detalle_geeso, activo_geeso, "
+ "ide_geare, ide_gedep "
+ "FROM gen_estructura_organizacional  "
+ "Where ide_geare="+tab_tabla3.getValor("IDE_GEARE")+" AND "
+ "IDE_GEDEP="+tab_tabla3.getValor("IDE_GEDEP")
+ " order by ide_geeso desc "
+ " limit 1");



utilitario.getConexion().ejecutarSql("update gen_departamento_sucursal "
		+ "set IDE_GEESO=" +Integer.parseInt(tabEstructuraDepart.getValor("IDE_GEESO"))  + " "
		+ "where ide_geare=" + tab_tabla3.getValor("IDE_GEARE")+" and "
				+ "ide_gedep=" + tab_tabla3.getValor("IDE_GEDEP")+" AND "
				+ "IDE_SUCU="+tab_tabla3.getValor("IDE_SUCU"));
	
	}
	}	
		
		
		
		/*if (tab_tabla1.guardar()) {			
			String str_mensaje=validar();			
			if(!str_mensaje.isEmpty()){
				utilitario.agregarMensajeError("No se puede guardar", str_mensaje);
				return;
			}						
			tab_tabla2.guardar();
		}
		if(guardarPantalla().isEmpty()){
			tab_tabla2.actualizarCombos();
		}
		
		if (tab_tabla1.isFilaInsertada()) {
			
		
	}
			if (tab_tabla2.isFilaInsertada()==true) {
				
			
		}*/
		
	

	@Override
	public void eliminar() {
		

		
		if (tab_tabla1.isFocus()) {
			if (tab_tabla1.eliminar()) {
				//si es que si elimina actualiza la nueva fila seleccionada

				TablaGenerica tabPartida=utilitario.consultar("SELECT ide_gepgc, ide_gegro, ide_gecaf, ide_sucu, ide_gedep, ide_geare,   "
						+ "ide_gepap, ide_gttem, titulo_cargo_gepgc, salario_encargo_gepgc,  "
						+ "fecha_activacion_gepgc, fecha_desactiva_gepgc, motivo_gepgc,  "
						+ " activo_gepgc, usuario_ingre, fecha_ingre, usuario_actua, fecha_actua,  "
						+ "hora_ingre, hora_actua, vacante_gepgc, encargo_gepgc, ide_geeso "
						+ "FROM gen_partida_grupo_cargo "
						+ "where ide_geare="+tab_tabla1.getValor("IDE_GEARE"));
				
				if (tabPartida.getTotalFilas()>0) {
					utilitario.agregarMensaje("No se puede eliminar la fila seleccionada", "El  area ya se encuentra en uso");
					return;
				}

				TablaGenerica tabDepartamento=utilitario.consultar("SELECT ide_gedep,ide_geare from gen_departamento "
						+ "where ide_geare="+tab_tabla1.getValor("IDE_GEARE"));

				if (tabDepartamento.getTotalFilas()>0) {
					utilitario.agregarMensaje("No se puede eliminar la fila seleccionada", "El  area / departamento ya se encuentra en uso");
					
					return;
				}
						
				
				TablaGenerica tabDepartamentoSucursal=utilitario.consultar("SELECT ide_sucu,ide_gedep,ide_geare from gen_departamento_sucursal "
						+ "where ide_geare="+tab_tabla1.getValor("IDE_GEARE"));

				if (tabDepartamentoSucursal.getTotalFilas()>0) {
					utilitario.agregarMensaje("No se puede eliminar la fila seleccionada", "El  area / departamento ya se encuentra en uso");
					
					return;
				}

				
				
				TablaGenerica tabEstructuraOrganizacionalValidacion= utilitario.consultar("SELECT ide_geeso, pre_ide_geeso, detalle_geeso, activo_geeso, "
						+ "ide_geare, ide_gedep "
						+ "FROM gen_estructura_organizacional  "
						+ "where ide_geare="+tab_tabla1.getValor("IDE_GEARE")
						+ " and ide_gedep is null "
						+ " order by ide_geeso desc "
						+ " limit 1");
				
				
	utilitario.getConexion().ejecutarSql("DELETE FROM  gen_estructura_organizacional WHERE IDE_GEARE="+tab_tabla1.getValor("IDE_GEARE")+" "
			+ " AND IDE_GEESO="+tabEstructuraOrganizacionalValidacion.getValor("IDE_GEESO"));
	utilitario.getConexion().ejecutarSql("DELETE FROM  gen_vigente_estructura_organizacional WHERE  IDE_GEESO="+tabEstructuraOrganizacionalValidacion.getValor("IDE_GEESO"));
	
				tab_tabla1.eliminar();
				filtrarSucursales();
				//				tab_tabla3.ejecutarSql();
			}

		} else if (tab_tabla2.isFocus()) {
			if (tab_tabla2.eliminar()) {
				//si es que si elimina actualiza la nueva fila seleccionada
		
				TablaGenerica tabPartida=utilitario.consultar("SELECT ide_gepgc, ide_gegro, ide_gecaf, ide_sucu, ide_gedep, ide_geare,   "
						+ "ide_gepap, ide_gttem, titulo_cargo_gepgc, salario_encargo_gepgc,  "
						+ "fecha_activacion_gepgc, fecha_desactiva_gepgc, motivo_gepgc,  "
						+ " activo_gepgc, usuario_ingre, fecha_ingre, usuario_actua, fecha_actua,  "
						+ "hora_ingre, hora_actua, vacante_gepgc, encargo_gepgc, ide_geeso "
						+ "FROM gen_partida_grupo_cargo "
						+ "where ide_geare="+tab_tabla2.getValor("IDE_GEARE")+" AND IDE_GEDEP="+tab_tabla2.getValor("IDE_GEDEP"));
				
				if (tabPartida.getTotalFilas()>0) {
					utilitario.agregarMensaje("No se puede eliminar la fila seleccionada", "El  area / departamento ya se encuentra en uso");
					
					return;
				}
										
				
				TablaGenerica tabDepartamentoSucursal=utilitario.consultar("SELECT ide_sucu,ide_gedep,ide_geare from gen_departamento_sucursal "
						+ "where ide_gedep="+tab_tabla2.getValor("IDE_GEDEP"));

				if (tabDepartamentoSucursal.getTotalFilas()>0) {
					utilitario.agregarMensaje("No se puede eliminar la fila seleccionada", "El  area / departamento ya se encuentra en uso");
					
					return;
				}
								
				
				TablaGenerica tabEstructuraOrganizacionalValidacion= utilitario.consultar("SELECT ide_geeso, pre_ide_geeso, detalle_geeso, activo_geeso, "
						+ "ide_geare, ide_gedep "
						+ "FROM gen_estructura_organizacional  "
						+ "where ide_geare="+tab_tabla1.getValor("IDE_GEARE")
						+ " and ide_gedep="+tab_tabla1.getValor("IDE_GEDEP")
						+ " order by ide_geeso desc "
						+ " limit 1");
			
				utilitario.getConexion().ejecutarSql("DELETE FROM  gen_estructura_organizacional WHERE IDE_GEARE="+tab_tabla2.getValor("IDE_GEARE")+" AND ide_GEDEP=" + tab_tabla2.getValor("IDE_GEDEP")+" AND IDE_GEESO="+tabEstructuraOrganizacionalValidacion.getValor("IDE_GEESO"));
				utilitario.getConexion().ejecutarSql("DELETE FROM  gen_vigente_estructura_organizacional WHERE  IDE_GEESO="+tabEstructuraOrganizacionalValidacion.getValor("IDE_GEESO"));
			tab_tabla2.eliminar();
				filtrarSucursales();
				
				//				tab_tabla3.ejecutarSql();
			}
		} 
				else if (tab_tabla3.isFocus()) {
					if (tab_tabla3.eliminar()) {
						TablaGenerica tabPartida=utilitario.consultar("SELECT ide_gepgc, ide_gegro, ide_gecaf, ide_sucu, ide_gedep, ide_geare,   "
								+ "ide_gepap, ide_gttem, titulo_cargo_gepgc, salario_encargo_gepgc,  "
								+ "fecha_activacion_gepgc, fecha_desactiva_gepgc, motivo_gepgc,  "
								+ " activo_gepgc, usuario_ingre, fecha_ingre, usuario_actua, fecha_actua,  "
								+ "hora_ingre, hora_actua, vacante_gepgc, encargo_gepgc, ide_geeso "
								+ "FROM gen_partida_grupo_cargo "
								+ "where ide_sucu="+tab_tabla3.getValor("IDE_SUCU")
								+ " and ide_geare="+tab_tabla3.getValor("IDE_GEARE")+" "
								+ "and ide_gedep ="+tab_tabla3.getValor("IDE_GEDEP"));
						
						if (tabPartida.getTotalFilas()>0) {
							utilitario.agregarMensaje("No se puede eliminar la fila seleccionada", "El sucursal / area / departamento ya se encuentra en uso");
							return;
						}

						TablaGenerica tabDepartamentoSucursal=utilitario.consultar("SELECT ide_sucu,ide_gedep,ide_geare from gen_departamento_sucursal "
								+ "where ide_sucu="+tab_tabla3.getValor("IDE_SUCU")
								+ " and ide_geare="+tab_tabla3.getValor("IDE_GEARE")+" "
								+ "and ide_gedep ="+tab_tabla3.getValor("IDE_GEDEP"));
						
						if (tabDepartamentoSucursal.getTotalFilas()>1) {
							utilitario.agregarMensaje("No se puede eliminar la fila seleccionada", "El  area / sucursal ya se encuentra en uso");
							
							return;
						}

						
						
					utilitario.getConexion().ejecutarSql("DELETE FROM  gen_departamento_sucursal WHERE IDE_GEARE="+tab_tabla1.getValor("IDE_GEARE")+" AND ide_GEDEP=" + tab_tabla1.getValor("IDE_GEDEP")+" AND IDE_SUCU="+tab_tabla3.getValor("IDE_SUCU"));
					tab_tabla3.eliminar();
					filtrarSucursales();		
					}
				}

	}

	@Override
	public void inicio() {
		// TODO Auto-generated method stub
		super.inicio();
		if (tab_tabla1.isFocus() || tab_tabla2.isFocus() || tab_tabla3.isFocus()) {
			filtrosTabla2();
		}
	}

	@Override
	public void siguiente() {
		// TODO Auto-generated method stub
		super.siguiente();
		if (tab_tabla1.isFocus() || tab_tabla2.isFocus()) {
			filtrosTabla2();
		}
	}

	@Override
	public void atras() {
		// TODO Auto-generated method stub
		super.atras();
		if (tab_tabla1.isFocus() || tab_tabla2.isFocus()) {
			filtrosTabla2();
		}
	}

	@Override
	public void fin() {
		// TODO Auto-generated method stub
		super.fin();
		if (tab_tabla1.isFocus() || tab_tabla2.isFocus()) {
			filtrosTabla2();
		}
	}

	@Override
	public void actualizar() {
		// TODO Auto-generated method stub
		super.actualizar();
		if (tab_tabla1.isFocus() || tab_tabla2.isFocus()) {
			filtrosTabla2();
		}
	}

	/**
	 * Filtra las sucursales por departamento y área seleccionada
	 */
	private void filtrarSucursales() {
		String str_area = tab_tabla1.getValor("IDE_GEARE");
		if (str_area == null) {
			str_area = "-1";
		}

		String str_departamento = tab_tabla2.getValor("IDE_GEDEP");
		if (str_departamento == null) {
			str_departamento = "-1";
		}

		String str_sucursal = tab_tabla3.getValor("IDE_SUCU");
		if (str_sucursal == null) {
			str_sucursal = "-1";
		}

		tab_tabla3.setCondicion("IDE_SUCU =" + str_sucursal +" AND IDE_GEARE =" + str_area + " AND IDE_GEDEP=" + str_departamento);
		tab_tabla3.ejecutarSql();
		//tab_tabla2.imprimirSql();
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

		public Tabla getTab_tabla3() {
			return tab_tabla3;
		}
	
		public void setTab_tabla3(Tabla tab_tabla3) {
			this.tab_tabla3 = tab_tabla3;
		}

	public SeleccionTabla getSet_sucursal() {
		return set_sucursal;
	}

	public void setSet_sucursal(SeleccionTabla set_sucursal) {
		this.set_sucursal = set_sucursal;
	}

	
	public void insertarEstructuraOrganizacionalArea(
			int pre_ide_geeso,
			String detalle_geeso, 
			boolean activo_geeso,
			int ide_geare
			){
		 String padre="";

			TablaGenerica tab_codigo = utilitario.consultar(servicioCodigoMaximo("gen_estructura_organizacional", "ide_geeso"));
			String codigo=tab_codigo.getValor("codigo");

		 if (pre_ide_geeso==0) {
			
				utilitario.getConexion().ejecutarSql("INSERT INTO gen_estructura_organizacional(" 
						+ "ide_geeso, "
						+ "pre_ide_geeso, "
				  		+ "detalle_geeso, "
				  		+ "activo_geeso,"
				  		+ "ide_geare )" + 
				  		" values( " +codigo + ", "
				  	    + "null, "
				  		+ "'"+detalle_geeso+"', "
				  		+ ""+activo_geeso+", "
				  		+ ""+ide_geare+")"); 
			
				
			}else {
				utilitario.getConexion().ejecutarSql("INSERT INTO gen_estructura_organizacional(" 
						+ "ide_geeso, "
						+ "pre_ide_geeso, "
				  		+ "detalle_geeso, "
				  		+ "activo_geeso,"
				  		+ "ide_geare )" + 
				  		" values( " +codigo + ", "
				  	    + ""+pre_ide_geeso+", "
				  		+ "'"+detalle_geeso+"', "
				  		+ ""+activo_geeso+", "
				  		+ ""+ide_geare+")"); 

			}
			 
	}


	

public void insertarEstructuraOrganizacionalDepartamento(
		int pre_ide_geeso,
		String detalle_geeso, 
		boolean activo_geeso,
		int ide_geare,
		int ide_gedep
		){
	
		TablaGenerica tab_codigo = utilitario.consultar(servicioCodigoMaximo("gen_estructura_organizacional", "ide_geeso"));
		String codigo=tab_codigo.getValor("codigo");
		utilitario.getConexion().ejecutarSql("INSERT INTO gen_estructura_organizacional(" 
					+ "ide_geeso, "
					+ "pre_ide_geeso, "
			  		+ "detalle_geeso, "
			  		+ "activo_geeso,"
			  		+ "ide_geare,"
			  		+ "ide_gedep )" + 
			  		" values( " +codigo + ", "
			  		+ ""+pre_ide_geeso+", "
			  		+ "'"+detalle_geeso+"', "
			  		+ ""+activo_geeso+", "
			  		+ ""+ide_geare+", "
			  		+ ""+ide_gedep+")"); 
	 
}

	


/*public void insertarDepartamento(
		 ide_geare, gen_ide_gedep, detalle_gedep,
		
		String ide_geare, 
		boolean activo_geeso,
		int ide_geare,
		int ide_gedep
		){
	
		TablaGenerica tab_codigo = utilitario.consultar(servicioCodigoMaximo("gen_departamento", "ide_gedep"));
		String codigo=tab_codigo.getValor("codigo");
		utilitario.getConexion().ejecutarSql("INSERT INTO gen_estructura_organizacional(" 
					+ "ide_geeso, "
					+ "pre_ide_geeso, "
			  		+ "detalle_geeso, "
			  		+ "activo_geeso,"
			  		+ "ide_geare,"
			  		+ "ide_gedep )" + 
			  		" values( " +codigo + ", "
			  		+ ""+pre_ide_geeso+", "
			  		+ "'"+detalle_geeso+"', "
			  		+ ""+activo_geeso+", "
			  		+ ""+ide_geare+", "
			  		+ ""+ide_gedep+")"); 
	 
}*/



	public String servicioCodigoMaximo(String tabla,String ide_primario){
		
		String maximo="Select 1 as ide,(case when max("+ide_primario+") is null then 0 else max("+ide_primario+") end) + 1 as codigo from "+tabla;
		return maximo;
	}
 



public void insertarEstructuraOrganizacionalVigente(
		int ide_geani, 
		boolean activo_geveo, 
	    int   ide_geeso
		){
	
		TablaGenerica tab_codigo = utilitario.consultar(servicioCodigoMaximo("gen_vigente_estructura_organizacional", "ide_geeso"));
		String codigo=tab_codigo.getValor("codigo");
		utilitario.getConexion().ejecutarSql("INSERT INTO gen_vigente_estructura_organizacional(" 
					+ "ide_geveo, "
					+ "ide_geani, "
			  		+ "bloqueado_geveo, "
			  		+ "activo_geveo,"
			  		+ "ide_geeso )" + 
			  		" values( " +codigo + ", "
			  		+ ""+ide_geani+", "
			  		+ "false, "
			  		+ ""+activo_geveo+", "
			  		+ ""+ide_geeso+")"); 
	 
}




}
