package paq_contabilidad;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;

public class pre_balance_comprobacion extends Pantalla{

	private Tabla tab_balance_inicial=new Tabla();
	private Combo com_anio=new Combo();
	private SeleccionTabla set_asiento_contable = new SeleccionTabla();
	private Confirmar con_guardar=new Confirmar();
	private Combo com_nivel_cuenta_inicial = new Combo();
	private Combo com_nivel_cuenta_final = new Combo();
	
	public static String par_tipo_asiento_inicial;
	private SeleccionCalendario sel_calendario=new SeleccionCalendario();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();
	private Map map_parametros = new HashMap();
	
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);

	public pre_balance_comprobacion() {
		par_tipo_asiento_inicial =utilitario.getVariable("p_tipo_asiento_inicial");

		bar_botones.limpiar();
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));	
		com_anio.setValue(utilitario.getVariable("p_anio_vigente"));
		com_anio.setStyle("width: 100px; margin: 0 0 -8px 0;");
		com_anio.setMetodo("seleccionaElAnio");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
		
		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);
		
		bar_botones.agregarReporte();
		
		sel_rep.setId("sel_rep");
		agregarComponente(sel_rep);	
		
		com_nivel_cuenta_inicial.setCombo(utilitario.getListaGruposNivelCuenta());		
		com_nivel_cuenta_inicial.setStyle("width: 100px; margin: 0 0 -8px 0;");
		com_nivel_cuenta_inicial.setValue("1");
		com_nivel_cuenta_inicial.setMetodo("seleccionaElAnio");
		bar_botones.agregarComponente(new Etiqueta("Seleccione el Nivel de Cuenta Inicial"));
		bar_botones.agregarComponente(com_nivel_cuenta_inicial);
		
		com_nivel_cuenta_final.setCombo(utilitario.getListaGruposNivelCuenta());	
		com_nivel_cuenta_final.setStyle("width: 100px; margin: 0 0 -8px 0;");
		com_nivel_cuenta_final.setValue("1");
		com_nivel_cuenta_final.setMetodo("seleccionaElAnio");
		bar_botones.agregarComponente(new Etiqueta("Seleccione el Nivel de Cuenta Final"));
		bar_botones.agregarComponente(com_nivel_cuenta_final);

		// boton limpiar
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarBoton(bot_limpiar);
				
		tab_balance_inicial.setId("tab_balance_inicial");  
		tab_balance_inicial.setTabla("cont_balance_comprobacion", "ide_cobac", 1);	
		tab_balance_inicial.setHeader("BALANCE DE COMPROBACION");
		tab_balance_inicial.setCondicion("ide_geani=-1");
		tab_balance_inicial.getColumna("ide_geani").setCombo(ser_contabilidad.getAnio("true,false", "true,false"));
		tab_balance_inicial.getColumna("ide_geani").setAutoCompletar();
		tab_balance_inicial.getColumna("ide_cocac").setCombo(ser_contabilidad.servicioCatalogoCuentaCombo());
		tab_balance_inicial.getColumna("ide_cocac").setAutoCompletar();
		tab_balance_inicial.getColumna("ide_cocac").setFiltroContenido();
		tab_balance_inicial.getColumna("ide_cocac").setLongitud(200);

		tab_balance_inicial.setRows(20);
		tab_balance_inicial.setLectura(true);
		tab_balance_inicial.dibujar();
		PanelTabla pat_balance_inicial=new PanelTabla();
		pat_balance_inicial.setPanelTabla(tab_balance_inicial);
		Division div1 = new Division();
		div1.dividir1(pat_balance_inicial);
		agregarComponente(div1);
		
		
		Boton bot_actualizar=new Boton();
		bot_actualizar.setIcon("ui-icon-person");
		bot_actualizar.setValue("Recalcular");
		bot_actualizar.setMetodo("generarCedula");
		bar_botones.agregarBoton(bot_actualizar);	
		
		Boton bot_generar = new Boton();
		bot_generar.setValue("Generar Archivo");
		bot_generar.setMetodo("generar_archivo");
		bot_generar.setAjax(false);
		bar_botones.agregarBoton(bot_generar);
		
		Boton bot_generar_bde = new Boton();
		bot_generar_bde.setValue("Generar Archivo bde");
		bot_generar_bde.setMetodo("generar_archivo_bde");
		bot_generar_bde.setAjax(false);
		bar_botones.agregarBoton(bot_generar_bde);
		
		Boton bot_mov=new Boton();
		bot_mov.setIcon("ui-icon-person");
		bot_mov.setValue("Movimientos Conflictivos");
		bot_mov.setMetodo("movimientosConHijos");
		bar_botones.agregarBoton(bot_mov);	
		
		inicializaCalendario();
		inicializaAsientosConflictivos();
	}
	
	public void inicializaAsientosConflictivos(){
		set_asiento_contable.setId("set_asiento_contable");
		set_asiento_contable.setTitle("Asientos Contables con Problemas");
		set_asiento_contable.setSeleccionTabla(ser_contabilidad.getMayorAnalitico("1900-01-01", "1900-01-01", "-1","-1"), "ide_comov");		
		set_asiento_contable.setFooter("ASIENTOS QUE POSEEN PROBLEMAS DE NIVEL DE CUENTAS..!");
		set_asiento_contable.setRadio();
		set_asiento_contable.getTab_seleccion().ejecutarSql();
		set_asiento_contable.getBot_aceptar().setMetodo("movimientosConHijos");
		agregarComponente(set_asiento_contable);
	}
	
	public void inicializaCalendario(){
		sel_calendario.setTitle("SELECCION DE FECHAS");
		sel_calendario.setFooter("Seleccione un Rango de fechas");
		sel_calendario.setFecha1(utilitario.obtenerFechaInicioAnio());
		sel_calendario.setFecha2(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		sel_calendario.getBot_aceptar().setMetodo("generarCedula");
		agregarComponente(sel_calendario);
	}
	
	public void movimientosConHijos(){
		
		if(set_asiento_contable.isVisible()){
			set_asiento_contable.cerrar();
		}
		else
		{
			TablaGenerica tab_movimientos= utilitario.consultar("SELECT distinct cat.ide_cocac as codigo,cat.ide_cocac FROM cont_movimiento mov"
																+" join cont_detalle_movimiento dmov on dmov.ide_comov=mov.ide_comov"
																+" join cont_catalogo_cuenta cat on cat.ide_cocac = dmov.ide_cocac"
																+" join cont_catalogo_cuenta catH on catH.con_ide_cocac=cat.ide_cocac "
																+" where activo_comov=true and mov_fecha_comov between '"+sel_calendario.getFecha1String()+"' and '"+sel_calendario.getFecha2String()+"'");
			tab_movimientos.imprimirSql();
			StringBuilder sel_ide_cocac=new StringBuilder();
			for (int i=0; i < tab_movimientos.getTotalFilas();i++){
				
				if(sel_ide_cocac.toString().isEmpty()==false){
					sel_ide_cocac.append(",");
                }
				sel_ide_cocac.append(tab_movimientos.getValor(i,"ide_cocac"));
			}
			
			if(tab_movimientos.getTotalFilas()<1)
				sel_ide_cocac.append("0");
			
			System.out.println("sel_ide_cocac: "+sel_ide_cocac);
			
			set_asiento_contable.getTab_seleccion().setSql(ser_contabilidad.getMayorAnalitico(sel_calendario.getFecha1String(), sel_calendario.getFecha2String(), sel_ide_cocac.toString(),com_anio.getValue()+""));
			set_asiento_contable.getTab_seleccion().ejecutarSql();
			set_asiento_contable.dibujar();			
		}
	}
	
	public void generarCedula(){
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Año", "");
			return;			

		}
		
		if(sel_calendario.isVisible()){
			
			TablaGenerica tab_valida_anio=utilitario.consultar("select 1 as codigo, 2 as resultado from tes_caja where not extract(year from cast('"+sel_calendario.getFecha1String()+"' as date)) =extract(year from cast('"+sel_calendario.getFecha2String()+"' as date))");
			if(tab_valida_anio.getTotalFilas()>0){
				utilitario.agregarMensajeError("Fechas no Validas", "Favor ingrese fechas que se encuentren dentro de un mismo año");
				return;
			}
			
			sel_calendario.cerrar();
			ser_contabilidad.generarBalComprobacion(com_anio.getValue().toString(), sel_calendario.getFecha1String(), sel_calendario.getFecha2String(), par_tipo_asiento_inicial);
			//tab_balance_inicial.setCondicion("ide_geani="+com_anio.getValue()+" and ide_cocac in (select ide_cocac from cont_catalogo_cuenta where nivel_cocac between "+com_nivel_cuenta_inicial.getValue()+" and "+com_nivel_cuenta_final.getValue()+")");
			tab_balance_inicial.setCondicion("ide_geani="+com_anio.getValue()+" and ide_cocac in (select ide_cocac from cont_catalogo_cuenta where nivel_cocac between "+com_nivel_cuenta_inicial.getValue()+" and "+com_nivel_cuenta_final.getValue()+")"+
					                         " and (abs(debe_inicial_cobac) + abs(haber_inicial_cobac) + abs(debe_acumulado_cobac) + abs(haber_acumulado_cobac) + abs(debe_periodo_cobac) + abs(haber_periodo_cobac) + abs(debe_saldo_cobac) + abs(haber_saldo_cobac))>0");
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
		
		tab_balance_inicial.setCondicion("ide_geani="+com_anio.getValue()+" and ide_cocac in (select ide_cocac from cont_catalogo_cuenta where nivel_cocac between "+com_nivel_cuenta_inicial.getValue()+" and "+com_nivel_cuenta_final.getValue()+")"+
		" and (abs(debe_inicial_cobac) + abs(haber_inicial_cobac) + abs(debe_acumulado_cobac) + abs(haber_acumulado_cobac) + abs(debe_periodo_cobac) + abs(haber_periodo_cobac) + abs(debe_saldo_cobac) + abs(haber_saldo_cobac))>0");
		tab_balance_inicial.ejecutarSql();
		utilitario.addUpdate("tab_balance_inicial");
		
	}
	
	public void generar_archivo(){
		
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
		
		if(!com_nivel_cuenta_inicial.getValue().toString().equals("4") ){
			utilitario.agregarMensajeInfo("No se puede generar el Archivo", "Seleccione el Nivel 1 en cuenta inicial.");
			return;
		}
		
		if(!com_nivel_cuenta_final.getValue().toString().equals("5")){
			utilitario.agregarMensajeInfo("No se puede generar el Archivo", "Seleccione el Nivel 2 en cuenta final.");
			return;
		}
		
		TablaGenerica tab_balance_comp=ser_contabilidad.getTablaBalanceComprobacionArchivoPlano(com_anio.getValue().toString(), com_nivel_cuenta_inicial.getValue().toString(), com_nivel_cuenta_final.getValue().toString());
		
		if(tab_balance_comp.isEmpty()){
			utilitario.agregarMensajeInfo("No se puede generar el Archivo", "No existen registros para la generación del archivo");
			return;
		}
		
		String fecha_final=tab_balance_comp.getValor(0, "fecha_final_cobac");
		
		try {
			ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
			String fileName= "BALANCE_COMPROBACION_"+utilitario.getNombreMes(utilitario.getMes(fecha_final))+utilitario.getAnio(fecha_final);
			String path= extContext.getRealPath("/");	
			String[] cuentaContable;
			
			File archivotxt=new File(path+fileName.concat(".txt"));
			BufferedWriter escribir=new BufferedWriter(new FileWriter(archivotxt)); 
			
			for(int i=0;i<tab_balance_comp.getTotalFilas();i++){

				StringBuilder str_detalle=new StringBuilder();
				//str_detalle.append(utilitario.getFormatoNumero(tab_balance_comp.getValor(i, "periodo"),1)).append("|");
				str_detalle.append(pckUtilidades.Utilitario.padLeft(utilitario.getFormatoNumero(tab_balance_comp.getValor(i, "periodo"),1),2)).append("|");
				
				cuentaContable=tab_balance_comp.getValor(i, "cuenta").split(Pattern.quote("."));

				str_detalle.append(cuentaContable[0]).append("|");
				str_detalle.append(cuentaContable[1]).append("|");
				str_detalle.append(cuentaContable[2]).append("|");
				
				str_detalle.append(tab_balance_comp.getValor(i, "debe_inicial_cobac")).append("|");
				str_detalle.append(tab_balance_comp.getValor(i, "haber_inicial_cobac")).append("|");
				str_detalle.append(tab_balance_comp.getValor(i, "debe_periodo_cobac")).append("|");
				str_detalle.append(tab_balance_comp.getValor(i, "haber_periodo_cobac")).append("|");
				str_detalle.append(tab_balance_comp.getValor(i, "debe_acumulado_cobac")).append("|");
				str_detalle.append(tab_balance_comp.getValor(i, "haber_acumulado_cobac")).append("|");
				str_detalle.append(tab_balance_comp.getValor(i, "debe_saldo_cobac")).append("|");
				str_detalle.append(tab_balance_comp.getValor(i, "haber_saldo_cobac"));

				String str_spi_encr="";
				try {
					 str_spi_encr=str_detalle.toString();
					str_spi_encr=str_spi_encr.replaceAll("Á", "A");
					str_spi_encr=str_spi_encr.replaceAll("É", "E");
					str_spi_encr=str_spi_encr.replaceAll("Í", "I");
					str_spi_encr=str_spi_encr.replaceAll("Ó", "O");
					str_spi_encr=str_spi_encr.replaceAll("Ú", "U");
					str_spi_encr=str_spi_encr.replaceAll("á", "a");
					str_spi_encr=str_spi_encr.replaceAll("é", "e");
					str_spi_encr=str_spi_encr.replaceAll("í", "i");
					str_spi_encr=str_spi_encr.replaceAll("ó", "o");
					str_spi_encr=str_spi_encr.replaceAll("ú", "u");
					str_spi_encr=str_spi_encr.replaceAll("Ñ", "N");
					str_spi_encr=str_spi_encr.replaceAll("ñ", "n");
				} catch (Exception e) {
					// TODO: handle exception
				}				

				escribir.write(str_spi_encr.toString());	
				escribir.newLine();
			}
			escribir.close();
			
			utilitario.crearArchivo(new File[]{archivotxt},fileName.concat(".txt"));	
			//utilitario.crearArchivo(archivotxt.getPath());

		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("generar_archivo error: "+ e);
		}	
		
	}
	
	public void generar_archivo_bde(){
		
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
		
		if(!com_nivel_cuenta_inicial.getValue().toString().equals("4") ){
			utilitario.agregarMensajeInfo("No se puede generar el Archivo bde", "Seleccione el Nivel 1 en cuenta inicial.");
			return;
		}
		
		if(!com_nivel_cuenta_final.getValue().toString().equals("5")){
			utilitario.agregarMensajeInfo("No se puede generar el Archivo bde", "Seleccione el Nivel 2 en cuenta final.");
			return;
		}
		
		TablaGenerica tab_balance_comp=ser_contabilidad.getTablaBalanceComprobacionArchivoPlano(com_anio.getValue().toString(), com_nivel_cuenta_inicial.getValue().toString(), com_nivel_cuenta_final.getValue().toString());
		
		if(tab_balance_comp.isEmpty()){
			utilitario.agregarMensajeInfo("No se puede generar el Archivo bde", "No existen registros para la generación del archivo");
			return;
		}
		
		String fecha_final=tab_balance_comp.getValor(0, "fecha_final_cobac");
		
		try {
			ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
			String fileName= "BALANCE_COMPROBACION_bde_"+utilitario.getNombreMes(utilitario.getMes(fecha_final))+utilitario.getAnio(fecha_final);
			String path= extContext.getRealPath("/");	
			String[] cuentaContable;
			
			File archivotxt=new File(path+fileName.concat(".txt"));
			BufferedWriter escribir=new BufferedWriter(new FileWriter(archivotxt)); 
			
			for(int i=0;i<tab_balance_comp.getTotalFilas();i++){

				StringBuilder str_detalle=new StringBuilder();
				str_detalle.append("04930000").append("\t");
				
				str_detalle.append(utilitario.getFormatoNumero(tab_balance_comp.getValor(i, "periodo"),1)).append("\t");

				cuentaContable=tab_balance_comp.getValor(i, "cuenta").split(Pattern.quote("."));

				str_detalle.append(cuentaContable[0]);
				str_detalle.append(cuentaContable[1]);
				
				if (!cuentaContable[2].equals("00"))
					str_detalle.append(cuentaContable[2]);
					
				str_detalle.append("\t");
				str_detalle.append(tab_balance_comp.getValor(i, "descripcion_cuenta")).append("\t");
				
				str_detalle.append(tab_balance_comp.getValor(i, "debe_inicial_cobac")).append("\t");
				str_detalle.append(tab_balance_comp.getValor(i, "haber_inicial_cobac")).append("\t");
				str_detalle.append(tab_balance_comp.getValor(i, "debe_acumulado_cobac")).append("\t");
				str_detalle.append(tab_balance_comp.getValor(i, "haber_acumulado_cobac")).append("\t");
				str_detalle.append(tab_balance_comp.getValor(i, "debe_saldo_cobac")).append("\t");
				str_detalle.append(tab_balance_comp.getValor(i, "haber_saldo_cobac"));

				String str_spi_encr="";
				try {
					 str_spi_encr=str_detalle.toString();
					str_spi_encr=str_spi_encr.replaceAll("Á", "A");
					str_spi_encr=str_spi_encr.replaceAll("É", "E");
					str_spi_encr=str_spi_encr.replaceAll("Í", "I");
					str_spi_encr=str_spi_encr.replaceAll("Ó", "O");
					str_spi_encr=str_spi_encr.replaceAll("Ú", "U");
					str_spi_encr=str_spi_encr.replaceAll("á", "a");
					str_spi_encr=str_spi_encr.replaceAll("é", "e");
					str_spi_encr=str_spi_encr.replaceAll("í", "i");
					str_spi_encr=str_spi_encr.replaceAll("ó", "o");
					str_spi_encr=str_spi_encr.replaceAll("ú", "u");
					str_spi_encr=str_spi_encr.replaceAll("Ñ", "N");
					str_spi_encr=str_spi_encr.replaceAll("ñ", "n");
				} catch (Exception e) {
					// TODO: handle exception
				}				

				escribir.write(str_spi_encr.toString());	
				escribir.newLine();
			}
			escribir.close();
			
			utilitario.crearArchivo(new File[]{archivotxt},fileName.concat(".txt"));	
			//utilitario.crearArchivo(archivotxt.getPath());

		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("generar_archivo bde error: "+ e);
		}	
		
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
	@Override
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}
	
	@Override
	public void aceptarReporte() {
		// TODO Auto-generated method stub
		if(rep_reporte.getReporteSelecionado().equals("Balance Comprobacion")){
			
			if (rep_reporte.isVisible()){
				rep_reporte.cerrar();	
				map_parametros.put("titulo","BALANCE DE COMPROBACION");
				map_parametros.put("contador_general",  utilitario.getVariable("p_nombre_contador"));				
				map_parametros.put("coordinador_finaciero",  utilitario.getVariable("p_nombre_coordinador_fin"));
				map_parametros.put("gerente",  utilitario.getVariable("p_nombre_gerente"));				
				map_parametros.put("cargo_gerente",  utilitario.getVariable("p_cargo_gerente"));
				map_parametros.put("ide_geani",  pckUtilidades.CConversion.CInt(com_anio.getValue()+""));
				map_parametros.put("nivel_inicial", pckUtilidades.CConversion.CInt(com_nivel_cuenta_inicial.getValue()+""));
				map_parametros.put("nivel_final",  pckUtilidades.CConversion.CInt(com_nivel_cuenta_final.getValue()+""));
				map_parametros.put("pie_coordinador_finaciero",  utilitario.getVariable("p_pie_coordinador_fin"));	

				sel_rep.setSeleccionFormatoReporte(map_parametros,rep_reporte.getPath());
				sel_rep.dibujar();
				}
		} 
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
