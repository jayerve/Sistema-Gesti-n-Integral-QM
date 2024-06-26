package paq_evaluacion;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import paq_anticipos.ejb.ServicioAnticipo;
import paq_gestion.ejb.ServicioEmpleado;
import paq_gestion.ejb.ServicioGestion;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AreaTexto;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.ItemMenu;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import framework.componentes.Texto;


public class pre_resultado_evaluacion extends Pantalla {

	private Tabla tab_desempenio=new Tabla();
	private Tabla tab_evaluadores=new Tabla();
	private Tabla tab_resultado=new Tabla();

    
	private AutoCompletar aut_empleado = new AutoCompletar();	
	@EJB
	private ServicioEmpleado ser_empleado=(ServicioEmpleado) utilitario.instanciarEJB(ServicioEmpleado.class);
	@EJB
	private ServicioNomina ser_nomina=(ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();
	private Map p_parametros=new HashMap();	
	@EJB
	private ServicioGestion ser_gestion = (ServicioGestion) utilitario.instanciarEJB(ServicioGestion.class);

	public pre_resultado_evaluacion() {

		// boton limpiar
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarReporte();
		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);

		sef_reporte.setId("sef_reporte");
		agregarComponente(sef_reporte);

		// autocompletar empleado
		aut_empleado.setId("aut_empleado");
		String str_sql_emp=ser_gestion.getSqlEmpleadosAutocompletar();
		aut_empleado.setAutoCompletar(str_sql_emp);
		aut_empleado.setMetodoChange("filtrarAnticiposEmpleado");

		Etiqueta eti_colaborador=new Etiqueta("Empleado:");
		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_empleado);
		bar_botones.agregarBoton(bot_limpiar);
		bar_botones.getBot_insertar().setRendered(false);
		bar_botones.getBot_guardar().setRendered(false);
		bar_botones.getBot_eliminar().setRendered(false);


		tab_desempenio.setId("tab_desempenio");
		tab_desempenio.setTabla("EVL_DESEMPENIO", "IDE_EVDES", 1);
		tab_desempenio.getColumna("ACTIVO_EVDES").setCheck();
		tab_desempenio.getColumna("ACTIVO_EVDES").setValorDefecto("true");
		tab_desempenio.getColumna("IDE_GEEDP").setVisible(false);
		tab_desempenio.getColumna("IDE_GTEMP").setVisible(false);
		tab_desempenio.agregarRelacion(tab_evaluadores);		
		tab_desempenio.setCondicion("IDE_GTEMP=-1");
		tab_desempenio.setLectura(true);
		tab_desempenio.dibujar();

		PanelTabla pat_panel1=new PanelTabla();
		pat_panel1.setPanelTabla(tab_desempenio);

		tab_evaluadores.setId("tab_evaluadores");		
		tab_evaluadores.setTabla("EVL_EVALUADORES", "IDE_EVEVA", 2);
		tab_evaluadores.getColumna("ACTIVO_EVEVA").setCheck();
		tab_evaluadores.getColumna("ACTIVO_EVEVA").setValorDefecto("true");
		tab_evaluadores.getColumna("IDE_GEEDP").setCombo("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
				" EMP.APELLIDO_MATERNO_GTEMP || ' ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
				"EMP.SEGUNDO_NOMBRE_GTEMP AS NOMBRES_APELLIDOS, " +
				"SUCU.NOM_SUCU, AREA.DETALLE_GEARE, " +
				"DEPA.DETALLE_GEDEP " +
				"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
				"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
				"LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " +
				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE " +
				"WHERE EPAR.ACTIVO_GEEDP=true");
		tab_evaluadores.getColumna("IDE_GEEDP").setAutoCompletar();
		tab_evaluadores.setColumnaSuma("POR_PESO_EVEVA");
		tab_evaluadores.getColumna("POR_PESO_EVEVA").setRequerida(true);
		tab_evaluadores.getColumna("POR_PESO_EVEVA").setMetodoChange("validarCero");		
		tab_evaluadores.agregarRelacion(tab_resultado);
		tab_evaluadores.setLectura(true);
		tab_evaluadores.getColumna("IDE_GEEDP").setLongitud(200);
		tab_evaluadores.dibujar();

		PanelTabla pat_panel2=new PanelTabla();
		pat_panel2.setPanelTabla(tab_evaluadores);

		tab_resultado.setId("tab_resultado");		
		tab_resultado.setTabla("EVL_RESULTADO", "IDE_EVRES", 3);
		tab_resultado.getColumna("ACTIVO_EVRES").setCheck();
		tab_resultado.getColumna("ACTIVO_EVRES").setValorDefecto("true");
		tab_resultado.getColumna("IDE_EVFAE").setCombo("EVL_FACTOR_EVALUACION", "IDE_EVFAE", "DETALLE_EVFAE", "");
		tab_resultado.getColumna("IDE_EVFAE").setAutoCompletar();
		tab_resultado.getColumna("IDE_EVFAE").setLongitud(200);
		tab_resultado.setColumnaSuma("PESO_FACTOR_EVRES");
	//	tab_evaluadores.getColumna("resultado_evres").setMetodoChange("validarCeroResultado");
		tab_resultado.setLectura(true);
		tab_resultado.dibujar();

		PanelTabla pat_panel3=new PanelTabla();
		pat_panel3.setPanelTabla(tab_resultado);

		//  DIVISION DE LA PANTALLA
		Division div_division=new Division();
		div_division.dividir3(pat_panel1,pat_panel2,pat_panel3,"30%","30%","H");
		agregarComponente(div_division);

	}

	String ide_geedp_activo="";
	public void filtrarAnticiposEmpleado(SelectEvent evt){
		aut_empleado.onSelect(evt);
		ide_geedp_activo=ser_gestion.getIdeContratoActivo(aut_empleado.getValor());
		tab_desempenio.setCondicion("IDE_GTEMP="+aut_empleado.getValor());
		tab_desempenio.ejecutarSql();
		tab_evaluadores.ejecutarValorForanea(tab_desempenio.getValorSeleccionado());
		tab_resultado.ejecutarValorForanea(tab_evaluadores.getValorSeleccionado());	
	}

	public void validarCero(AjaxBehaviorEvent evt){
		tab_evaluadores.modificar(evt);
		if(tab_evaluadores.getValor("POR_PESO_EVEVA")!=null){
			double dou_valor=0;
			try {
				dou_valor=Double.parseDouble(tab_evaluadores.getValor("POR_PESO_EVEVA"));

			} catch (Exception e) {
				// TODO: handle exception
			}	
			if(dou_valor<=0){
				tab_evaluadores.setValor("POR_PESO_EVEVA", "");
				utilitario.addUpdateTabla(tab_evaluadores, "POR_PESO_EVEVA", "");
				utilitario.agregarMensajeInfo("El campo PESO EVALUADOR no puede menor o igual a 0", "");
			}
			else{
				tab_evaluadores.sumarColumnas();
				utilitario.addUpdate("tab_evaluadores");
			}
		}
	}
	
	
	
	public void validarCeroResultado(AjaxBehaviorEvent evt){
		tab_resultado.modificar(evt);
		if(tab_resultado.getValor("resultado_evres")!=null){
			double dou_valor=0;
			try {
				dou_valor=Double.parseDouble(tab_resultado.getValor("resultado_evres"));

			} catch (Exception e) {
				// TODO: handle exception
			}	
			if(dou_valor<=0){
				tab_resultado.setValor("resultado_evres", "");
				utilitario.addUpdateTabla(tab_resultado, "resultado_evres", "");
				utilitario.agregarMensajeInfo("El campo RESULTADO EVALUACION no puede menor o igual a 0", "");
			}
			else{
				tab_resultado.sumarColumnas();
				utilitario.addUpdate("tab_resultado");
			}
		}
	}


	/**
	 * limpia toda la pantalla incluyendo el autocompletar
	 */
	public void limpiar() {
		tab_desempenio.limpiar();
		tab_evaluadores.limpiar();
		tab_resultado.limpiar();
		ide_geedp_activo="";
		aut_empleado.limpiar();
		aut_empleado.limpiar();
		utilitario.addUpdate("aut_empleado");// limpia y refresca el autocompletar
		tab_evaluadores.sumarColumnas();
		tab_resultado.sumarColumnas();
	}



	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if (tab_desempenio.isFocus()){
			if (aut_empleado.getValor()!=null){
				if (ide_geedp_activo!=null && !ide_geedp_activo.isEmpty()){		
					tab_desempenio.insertar();
					tab_desempenio.setValor("IDE_GEEDP",ide_geedp_activo);
					tab_desempenio.setValor("IDE_GTEMP", aut_empleado.getValor());
					TablaGenerica tab_titulo_profesional=utilitario.consultar("SELECT a.IDE_GTEMP,b.IDE_GTEDE,(case when b.DETALLE_GTTTP is null then 'SIN TITULO' else b.DETALLE_GTTTP end) as titulo " +
							"FROM GTH_EMPLEADO a " +
							"left join " +
							"(SELECT a.IDE_GTEDE,a.IDE_GTEMP,b.DETALLE_GTTTP FROM GTH_EDUCACION_EMPLEADO a, GTH_TIPO_TITULO_PROFESIONAL b " +
							"where a.IDE_GTTTP=b.IDE_GTTTP and b.ACTIVO_GTTTP=1 and rownum =1) b on a.IDE_GTEMP=b.IDE_GTEMP where a.IDE_GTEMP="+aut_empleado.getValor());
					tab_desempenio.setValor("TITULO_PROFESIONAL_EVDES", tab_titulo_profesional.getValor("titulo"));
				}else{
					utilitario.agregarMensajeInfo("No se puede insertar", "El contrato del empleado no esta activo");
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar el Empleado");
			}
		}
		else if (tab_evaluadores.isFocus()){
			if (aut_empleado.getValor()!=null){
				if (tab_desempenio.getTotalFilas()>0){
					tab_evaluadores.insertar();
				}else{
					utilitario.agregarMensajeInfo("No se puede insertar", "Primero debe insertar un Desempeño");				
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar el Empleado");
			}			
		}
		else if (tab_resultado.isFocus()){
			if (aut_empleado.getValor()!=null){
				if (tab_evaluadores.getTotalFilas()>0){
					tab_resultado.insertar();					
				}else{
					utilitario.agregarMensajeInfo("No se puede insertar", "Primero debe insertar un Evaluador");				
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar el Empleado");
			}			
		}	
	}

	@Override
	public void guardar() {		
		if (aut_empleado.getValor()!=null){
			if(tab_evaluadores.getSumaColumna("POR_PESO_EVEVA")!=100.00){
				utilitario.agregarMensajeError("La suma de las columna del peso del evaluador debe ser igual a 100", "");
				return;
			}
			if (tab_desempenio.guardar()){				
				if(tab_evaluadores.guardar()){						
					if (tab_resultado.guardar()){													
						guardarPantalla();									
						tab_evaluadores.sumarColumnas();					
					}
				}
			}
		}
		else{
			utilitario.agregarMensajeInfo("No se puede guardar ", "Debe seleccionar un Empleado");
		}
	}



	@Override
	public void eliminar() {
		if (aut_empleado.getValor()!=null){			
			if (tab_desempenio.isFocus()){	
				tab_desempenio.eliminar();							
			}else if(tab_evaluadores.isFocus()){
				tab_evaluadores.eliminar();
				tab_evaluadores.sumarColumnas();
			}
			else if(tab_resultado.isFocus()){
				tab_resultado.eliminar();
			}
			else if(tab_evaluadores.isFocus()){
				tab_evaluadores.eliminar();
			}		
		}else{
			utilitario.agregarMensajeInfo("No se puede guardar el Anticipo", "Debe seleccionar un Empleado");
		}
	}

	public AutoCompletar getAut_empleado() {
		return aut_empleado;
	}

	public void setAut_empleado(AutoCompletar aut_empleado) {
		this.aut_empleado = aut_empleado;
	}

	public Tabla gettab_desempenio() {
		return tab_desempenio;
	}

	public void settab_desempenio(Tabla tab_desempenio) {
		this.tab_desempenio = tab_desempenio;
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

	public Tabla gettab_evaluadores() {
		return tab_evaluadores;
	}

	public void settab_evaluadores(Tabla tab_evaluadores) {
		this.tab_evaluadores = tab_evaluadores;
	}

	public Tabla getTab_resultado() {
		return tab_resultado;
	}

	public void setTab_resultado(Tabla tab_resultado) {
		this.tab_resultado = tab_resultado;
	}

	public Tabla getTab_desempenio() {
		return tab_desempenio;
	}

	public void setTab_desempenio(Tabla tab_desempenio) {
		this.tab_desempenio = tab_desempenio;
	}

	public Tabla getTab_evaluadores() {
		return tab_evaluadores;
	}

	public void setTab_evaluadores(Tabla tab_evaluadores) {
		this.tab_evaluadores = tab_evaluadores;
	}
	

	@Override
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}

	@Override
	public void aceptarReporte() {
		if (rep_reporte.getReporteSelecionado().equals("Resultado Evaluacion")){
			if (tab_desempenio.getTotalFilas()>0) {
				if (rep_reporte.isVisible()){
					p_parametros=new HashMap();				
					rep_reporte.cerrar();				
					p_parametros.put("ide_evdes",pckUtilidades.CConversion.CInt(tab_desempenio.getValor("IDE_EVDES")));					
					p_parametros.put("titulo", "RESULTADO EVALUACIÓN");
					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());						
					sef_reporte.dibujar();
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun empleado");
			}
		}
	}
}
