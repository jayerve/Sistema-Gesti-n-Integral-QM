
package paq_evaluacion;

import java.math.BigDecimal;
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

import org.apache.poi.hssf.record.formula.TblPtg;
import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import paq_anticipos.ejb.ServicioAnticipo;
import paq_gestion.ejb.ServicioEmpleado;
import paq_gestion.ejb.ServicioGestion;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;
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


public class pre_ficha_evaluacion_evaluadores extends Pantalla {

	private Tabla tab_evaluados=new Tabla();	
	private Tabla tab_actividad_puesto=new Tabla();	
	private Tabla tab_competencia_gestion=new Tabla();
	private Tabla tab_competencia_institucional=new Tabla();
	private Tabla tab_competencia_tecnica=new Tabla();
	private Confirmar con_guardar=new Confirmar();



	@EJB
	private ServicioEmpleado ser_empleado=(ServicioEmpleado) utilitario.instanciarEJB(ServicioEmpleado.class);
	@EJB
	private ServicioNomina ser_nomina=(ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	@EJB
	private ServicioGestion ser_gestion = (ServicioGestion) utilitario.instanciarEJB(ServicioGestion.class);
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();
	private Map p_parametros=new HashMap();

	public pre_ficha_evaluacion_evaluadores() {
		
		con_guardar.setId("con_guardar");
		con_guardar.setMessage("ESTA SEGURO DE CALIFICAR RESULTADOS");
		con_guardar.setTitle("CONFIRMACION DE CALIFICACIÓN");
	
		con_guardar.getBot_aceptar().setMetodo("validarResultados");

		agregarComponente(con_guardar);
		
		bar_botones.getBot_insertar().setRendered(false);
		bar_botones.getBot_eliminar().setRendered(false);
		
		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);

		sef_reporte.setId("sef_reporte");
		agregarComponente(sef_reporte);
		


		Boton bot_validar=new Boton();
		bot_validar.setValue("CALIFICAR");
		bot_validar.setMetodo("validarResultados");
		bar_botones.agregarBoton(bot_validar);
		System.out.println("EVEVA... "+tab_evaluados.getValorSeleccionado());
        System.out.println("IDE_GEEP   "+tab_evaluados.getValor("IDE_GEEDP"));
		System.out.println("PFE SQL tab_evaluados...  "+tab_evaluados.getSql());        
		String ide_gtemp=ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
		System.out.println("PFE SQL ide_gtemp...  "+ide_gtemp);        
		
		if (ide_gtemp==null || ide_gtemp.isEmpty()){
			utilitario.agregarNotificacionInfo("Usted no se encuentra registrado como evaluador", "Contactece con el Administrador");
			return;
		}
		
		
		
		TablaGenerica tab_emple=tab_emple=ser_empleado.getEmpleado(ide_gtemp);

		String str_nom_colaborador="";
		if(tab_emple.getValor("APELLIDO_PATERNO_GTEMP")!=null && !tab_emple.getValor("APELLIDO_PATERNO_GTEMP").isEmpty()){
			str_nom_colaborador+=tab_emple.getValor("APELLIDO_PATERNO_GTEMP")+" ";
			
		
		}
		if(tab_emple.getValor("APELLIDO_MATERNO_GTEMP")!=null && !tab_emple.getValor("APELLIDO_MATERNO_GTEMP").isEmpty()){
			str_nom_colaborador+=tab_emple.getValor("APELLIDO_MATERNO_GTEMP")+" ";		
		}
		if(tab_emple.getValor("PRIMER_NOMBRE_GTEMP")!=null && !tab_emple.getValor("PRIMER_NOMBRE_GTEMP").isEmpty()){
			str_nom_colaborador+=tab_emple.getValor("PRIMER_NOMBRE_GTEMP")+" ";		
		}
		if(tab_emple.getValor("SEGUNDO_NOMBRE_GTEMP")!=null && !tab_emple.getValor("SEGUNDO_NOMBRE_GTEMP").isEmpty()){
			str_nom_colaborador+=tab_emple.getValor("SEGUNDO_NOMBRE_GTEMP")+" ";			
		}
		
		Etiqueta eti_colaborador=new Etiqueta("EVALUADOR:");
		if(tab_emple.getTotalFilas()>0){
			eti_colaborador.setValue("EVALUADOR: "+str_nom_colaborador);
		}else{
			eti_colaborador.setValue("EVALUADOR: ");
		}


		bar_botones.agregarComponente(eti_colaborador);


		tab_evaluados.setId("tab_evaluados");
		tab_evaluados.setNumeroTabla(1);
		tab_evaluados.setSql("select ide_eveva,empleado,a.ide_evdes,a.ide_gtemp," +
				"TO_CHAR(a.fecha_desde_evdes,'yyyy-mm-dd') as fecha_desde_evdes, " +
				"TO_CHAR(a.fecha_hasta_evdes,'yyyy-mm-dd') as fecha_hasta_evdes, " +
				"titulo_profesional_evdes " +
				"from evl_desempenio a,evl_evaluadores b, " +
				"( " +
				"select ide_gtemp,apellido_paterno_gtemp||' '||apellido_materno_gtemp||' '||primer_nombre_gtemp||' '||segundo_nombre_gtemp as empleado " +
				"from gth_empleado " +
				") c " +
				"where activo_evdes=TRUE and activo_eveva=TRUE " +
				"and a.ide_evdes= b.ide_evdes " +
				"and a.ide_gtemp=c.ide_gtemp " +
				"and b.ide_geedp in (select ide_geedp from gen_empleados_departamento_par " +
				"where ide_gtemp in (select ide_gtemp from sis_usuario where ide_usua ="+utilitario.getVariable("ide_usua")+"))");

		System.out.println("PFE SQL tab_evaluados...  "+tab_evaluados.getSql());
		
		tab_evaluados.setCampoPrimaria("ide_eveva");
		tab_evaluados.getColumna("IDE_EVDES").setVisible(false);
		tab_evaluados.getColumna("IDE_GTEMP").setVisible(false);
		tab_evaluados.setLectura(true);
		tab_evaluados.agregarRelacion(tab_actividad_puesto);		
		tab_evaluados.agregarRelacion(tab_competencia_gestion);
		tab_evaluados.agregarRelacion(tab_competencia_institucional);
		tab_evaluados.agregarRelacion(tab_competencia_tecnica);		
		tab_evaluados.dibujar();


		PanelTabla pat_panel1=new PanelTabla();
		pat_panel1.setPanelTabla(tab_evaluados);
		tab_evaluados.setHeader("EVALUADOS");

		Tabulador tab_tabulador=new Tabulador();
		tab_tabulador.setId("tab_tabulador");

		tab_actividad_puesto.setId("tab_actividad_puesto");
		tab_actividad_puesto.setIdCompleto("tab_tabulador:tab_actividad_puesto");
		tab_actividad_puesto.setTabla("EVL_ACTIVIDAD_PUESTO", "IDE_EVACP", 2);
		tab_actividad_puesto.getColumna("ACTIVO_EVACP").setCheck();
		tab_actividad_puesto.getColumna("ACTIVO_EVACP").setValorDefecto("true");
		tab_actividad_puesto.getColumna("ACTIVO_EVACP").setLectura(true);
		tab_actividad_puesto.getColumna("IDE_CMDEC").setCombo("CMP_DETALLE_COMPETENCIA", "IDE_CMDEC", "DETALLE_CMDEC", "");
		tab_actividad_puesto.getColumna("IDE_CMDEC").setAutoCompletar();
		tab_actividad_puesto.getColumna("IDE_EVFAE").setVisible(false);
		tab_actividad_puesto.getColumna("IDE_CMDEC").setLectura(true);		
		tab_actividad_puesto.getColumna("INDICADOR_EVACP").setLectura(true);
		tab_actividad_puesto.getColumna("POR_CUMPLIDO_EVACP").setLectura(true);
		tab_actividad_puesto.getColumna("NIVEL_CUMPLIDO_EVACP").setLectura(true);
		tab_actividad_puesto.getColumna("META_EVACP").setLectura(true);
		tab_actividad_puesto.setColumnaSuma("META_EVACP");
		tab_actividad_puesto.getColumna("CUMPLIDO_EVACP").setMetodoChange("calcularCumplimiento");
//		tab_actividad_puesto.getColumna("ACTIVO_EVACP=1");
		tab_actividad_puesto.dibujar();

		PanelTabla pat_panel2=new PanelTabla();
		pat_panel2.setPanelTabla(tab_actividad_puesto);
		pat_panel2.getMenuTabla().getItem_insertar().setRendered(false);
		pat_panel2.getMenuTabla().getItem_eliminar().setRendered(false);

		tab_competencia_gestion.setId("tab_competencia_gestion");
		tab_competencia_gestion.setIdCompleto("tab_tabulador:tab_competencia_gestion");
		tab_competencia_gestion.setTabla("EVL_OTRA_COMPETENCIA", "IDE_EVOTC", 3);
		tab_competencia_gestion.getColumna("ACTIVO_EVOTC").setCheck();
		tab_competencia_gestion.getColumna("ACTIVO_EVOTC").setValorDefecto("true");
		tab_competencia_gestion.getColumna("ACTIVO_EVOTC").setLectura(true);
		tab_competencia_gestion.getColumna("NIVEL_CUMPLIDO_EVOTC").setLectura(true);
		tab_competencia_gestion.getColumna("IDE_CMDEC").setCombo("CMP_DETALLE_COMPETENCIA", "IDE_CMDEC", "DETALLE_CMDEC", "");
		tab_competencia_gestion.getColumna("IDE_CMDEC").setAutoCompletar();
		tab_competencia_gestion.getColumna("IDE_EVNID").setCombo("EVL_NIVEL_DESARROLLO", "IDE_EVNID", "DETALLE_EVNID", "");
		tab_competencia_gestion.getColumna("IDE_EVNID").setAutoCompletar();
		tab_competencia_gestion.getColumna("IDE_EVREL").setCombo("EVL_RELEVANCIA", "IDE_EVREL", "DETALLE_EVREL", "");
		tab_competencia_gestion.getColumna("IDE_EVREL").setAutoCompletar();
		tab_competencia_gestion.getColumna("IDE_CMDEC").setLectura(true);
		tab_competencia_gestion.getColumna("IDE_EVFAE").setVisible(false);
		tab_competencia_gestion.getColumna("IDE_EVNID").setMetodoChange("filtrarNivelCompetenciaGestion");
		//	tab_competencia_gestion.getColumna("INDICADOR_EVACP").setLectura(true);
		//	tab_competencia_gestion.getColumna("POR_CUMPLIDO_EVACP").setLectura(true);
		//	tab_competencia_gestion.getColumna("NIVEL_CUMPLIDO_EVACP").setLectura(true);
		//	tab_competencia_gestion.getColumna("META_EVACP").setLectura(true);
		tab_competencia_gestion.setCondicion("IDE_EVFAE="+utilitario.getVariable("p_competencias_gestion")+" and ACTIVO_EVOTC=TRUE");
		tab_competencia_gestion.dibujar();


		PanelTabla pat_panel3=new PanelTabla();
		pat_panel3.setPanelTabla(tab_competencia_gestion);
		pat_panel3.getMenuTabla().getItem_insertar().setRendered(false);
		pat_panel3.getMenuTabla().getItem_eliminar().setRendered(false);


		tab_competencia_institucional.setId("tab_competencia_institucional");
		tab_competencia_institucional.setIdCompleto("tab_tabulador:tab_competencia_institucional");
		tab_competencia_institucional.setTabla("EVL_OTRA_COMPETENCIA", "IDE_EVOTC", 4);
		tab_competencia_institucional.getColumna("ACTIVO_EVOTC").setCheck();
		tab_competencia_institucional.getColumna("ACTIVO_EVOTC").setValorDefecto("true");
		tab_competencia_institucional.getColumna("ACTIVO_EVOTC").setLectura(true);
		tab_competencia_institucional.getColumna("NIVEL_CUMPLIDO_EVOTC").setLectura(true);
		tab_competencia_institucional.getColumna("IDE_CMDEC").setCombo("CMP_DETALLE_COMPETENCIA", "IDE_CMDEC", "DETALLE_CMDEC", "");
		tab_competencia_institucional.getColumna("IDE_CMDEC").setAutoCompletar();		
		tab_competencia_institucional.getColumna("IDE_EVNID").setCombo("EVL_NIVEL_DESARROLLO", "IDE_EVNID", "DETALLE_EVNID", "");
		tab_competencia_institucional.getColumna("IDE_EVNID").setAutoCompletar();
		tab_competencia_institucional.getColumna("IDE_EVREL").setCombo("EVL_RELEVANCIA", "IDE_EVREL", "DETALLE_EVREL", "");
		tab_competencia_institucional.getColumna("IDE_EVREL").setAutoCompletar();
		tab_competencia_institucional.getColumna("IDE_CMDEC").setLectura(true);
		tab_competencia_institucional.getColumna("IDE_EVFAE").setVisible(false);
		tab_competencia_institucional.getColumna("IDE_EVNID").setMetodoChange("filtrarNivel");
		/*	tab_competencia_institucional.getColumna("INDICADOR_EVACP").setLectura(true);
		tab_competencia_institucional.getColumna("POR_CUMPLIDO_EVACP").setLectura(true);
		tab_competencia_institucional.getColumna("NIVEL_CUMPLIDO_EVACP").setLectura(true);
		tab_competencia_institucional.getColumna("META_EVACP").setLectura(true);*/
		tab_competencia_institucional.setCondicion("IDE_EVFAE="+utilitario.getVariable("p_competencias_instituciones")+" and ACTIVO_EVOTC=TRUE");		
		tab_competencia_institucional.dibujar();


		PanelTabla pat_panel4=new PanelTabla();
		pat_panel4.setPanelTabla(tab_competencia_institucional);
		pat_panel4.getMenuTabla().getItem_insertar().setRendered(false);
		pat_panel4.getMenuTabla().getItem_eliminar().setRendered(false);


		tab_competencia_tecnica.setId("tab_competencia_tecnica");
		tab_competencia_tecnica.setIdCompleto("tab_tabulador:tab_competencia_tecnica");
		tab_competencia_tecnica.setTabla("EVL_OTRA_COMPETENCIA", "IDE_EVOTC", 5);
		tab_competencia_tecnica.getColumna("ACTIVO_EVOTC").setCheck();
		tab_competencia_tecnica.getColumna("ACTIVO_EVOTC").setValorDefecto("true");
		tab_competencia_tecnica.getColumna("ACTIVO_EVOTC").setLectura(true);
		tab_competencia_tecnica.getColumna("NIVEL_CUMPLIDO_EVOTC").setLectura(true);
		tab_competencia_tecnica.getColumna("IDE_CMDEC").setCombo("CMP_DETALLE_COMPETENCIA", "IDE_CMDEC", "DETALLE_CMDEC", "");
		tab_competencia_tecnica.getColumna("IDE_CMDEC").setAutoCompletar();		
		tab_competencia_tecnica.getColumna("IDE_EVNID").setCombo("EVL_NIVEL_DESARROLLO", "IDE_EVNID", "DETALLE_EVNID", "");
		tab_competencia_tecnica.getColumna("IDE_EVNID").setAutoCompletar();
		tab_competencia_tecnica.getColumna("IDE_EVREL").setCombo("EVL_RELEVANCIA", "IDE_EVREL", "DETALLE_EVREL", "");
		tab_competencia_tecnica.getColumna("IDE_EVREL").setAutoCompletar();
		tab_competencia_tecnica.getColumna("IDE_CMDEC").setLectura(true);
		tab_competencia_tecnica.getColumna("IDE_EVFAE").setVisible(false);
		tab_competencia_tecnica.getColumna("IDE_EVNID").setMetodoChange("filtrarNivelTecnica");
		/*		tab_competencia_tecnica.getColumna("INDICADOR_EVACP").setLectura(true);
		tab_competencia_tecnica.getColumna("POR_CUMPLIDO_EVACP").setLectura(true);
		tab_competencia_tecnica.getColumna("NIVEL_CUMPLIDO_EVACP").setLectura(true);
		tab_competencia_tecnica.getColumna("META_EVACP").setLectura(true);*/
		tab_competencia_tecnica.setCondicion("IDE_EVFAE="+utilitario.getVariable("p_competencias_tecnicas") + " and ACTIVO_EVOTC=TRUE");
		tab_competencia_tecnica.dibujar();

		PanelTabla pat_panel5=new PanelTabla();
		pat_panel5.setPanelTabla(tab_competencia_tecnica);
		pat_panel5.getMenuTabla().getItem_insertar().setRendered(false);
		pat_panel5.getMenuTabla().getItem_eliminar().setRendered(false);


		tab_tabulador.agregarTab("ACTIVIDAD PUESTO", pat_panel2);
		tab_tabulador.agregarTab("COMPETENCIAS GESTIÓN", pat_panel3);
		tab_tabulador.agregarTab("COMPETENCIAS INSTITUCIONALES", pat_panel4);
		tab_tabulador.agregarTab("COMPETENCIAS TECNICAS", pat_panel5);


		//  DIVISION DE LA PANTALLA
		Division div_division=new Division();
		div_division.dividir2(pat_panel1,tab_tabulador,"30%","H");
		agregarComponente(div_division);

	}
	String ide_geedp_activo="";
	public void filtrarCompetencias(AjaxBehaviorEvent evt) {
		tab_evaluados.isFocus();
	}
	
	
	public boolean ValidarCompetencias(String IDE_GEEDP){
		
		
		//System.out.println("BABYBULI"+tab_evaluados.getValorSeleccionado());
		//S//tring IDE_GEEDP=tab_evaluados.getValor("IDE_GEEDP");
		//System.out.println("AJA"+tab_evaluados.getValor("IDE_GEEDP"));
	
		TablaGenerica tab_competencias= utilitario.consultar("SELECT * FROM (SELECT GRU.IDE_EVGRF,FAC.IDE_EVFAE,FAC.DETALLE_EVFAE,EDP.IDE_GEEDP, " +
				"EDP.IDE_GEGRO,GRO.DETALLE_GEGRO,CAF.DETALLE_GECAF,GRU.PESO_POR_EVGRF  FROM " +
				"GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP " +
				"left join GEN_GRUPO_OCUPACIONAL GRO ON GRO.IDE_GEGRO=EDP.IDE_GEGRO " +
				"left join GEN_CARGO_FUNCIONAL CAF ON CAF.IDE_GECAF=EDP.IDE_GECAF " +
				"LEFT JOIN EVL_GRUPO_FACTOR GRU ON GRU.IDE_GEGRO=EDP.IDE_GEGRO " +
				"LEFT JOIN EVL_FACTOR_EVALUACION FAC ON FAC.IDE_EVFAE=GRU.IDE_EVFAE " +
				"WHERE EDP.IDE_GEEDP="+IDE_GEEDP+" " +
				")c " +
				"LEFT JOIN " +
				"((SELECT A.IDE_EVEVA,A.IDE_EVFAE FROM EVL_OTRA_COMPETENCIA A " +
				"GROUP BY  A.IDE_EVEVA,A.IDE_EVFAE " +
				"HAVING A.IDE_EVEVA ="+tab_evaluados.getValorSeleccionado()+" " +
				") " +
				"UNION ( " +
				"SELECT B.IDE_EVEVA,B.IDE_EVFAE FROM  EVL_ACTIVIDAD_PUESTO  B " +
				"GROUP BY B.IDE_EVEVA,B.IDE_EVFAE " +
				"HAVING B.IDE_EVEVA="+tab_evaluados.getValorSeleccionado()+") " +
				"ORDER BY IDE_EVEVA) " +
				"b on b.IDE_EVFAE=c.IDE_EVFAE " +
				"where b.ide_evfae is null"); 
		tab_competencias.imprimirSql();
		String competencia="";
if(tab_competencias.getTotalFilas()>0){
			for (int i = 0; i < tab_competencias.getTotalFilas(); i++)  {
				 competencia+=tab_competencias.getValor(i,"DETALLE_EVFAE")+" , ";
		
			//utilitario.agregarMensajeInfo("Consulte con el Administrador faltan parametros","No se puede realizar la Evaluacion");
				
		}
			 utilitario.agregarMensajeInfo("Consulte con el Administrador faltan parametros",competencia);
			 return true;
			
		}else {
			utilitario.agregarMensajeInfo("CORRECTA ASIGNACION DE COMPETENCIAS","");
		//\\return;
		}
		
	return false;
	}
	
	public void filtrarNivelCompetenciaGestion(SelectEvent evt) {
		tab_competencia_gestion.modificar(evt);
		TablaGenerica tab_institucional=utilitario.consultar("SELECT * FROM EVL_NIVEL_DESARROLLO WHERE IDE_EVNID="+tab_competencia_gestion.getValor("IDE_EVNID"));
		if(tab_institucional.getTotalFilas()>0){
			tab_competencia_gestion.setValor("NIVEL_CUMPLIDO_EVOTC", tab_institucional.getValor("VALOR_EVNID"));
		}else{
			utilitario.agregarMensajeError("No se puede continuar", "No existe un valor para el nivel de cumplimiento");
		}
		utilitario.addUpdate("tab_tabulador:tab_competencia_gestion");
	}
	
	public void validarResultados(){
		
		
	
		if (tab_evaluados.getTotalFilas()==0 )		{
				utilitario.agregarMensajeInfo("No se puede calificar No existen datos","Asignar evaluaciones");
			return;	
			}
		/*	if (tab_actividad_puesto.getTotalFilas()==0 )		{
				utilitario.agregarMensajeInfo("No se puede calificar No existen datos","Asignar Actividades");
			return;	
			}		
			if (tab_competencia_gestion.getTotalFilas()==0 )		{
				utilitario.agregarMensajeInfo("No se puede calificar No existen datos","Asignar Actividades");
			return;	
			}		
			if (tab_competencia_institucional.getTotalFilas()==0 )		{
				utilitario.agregarMensajeInfo("No se puede calificar No existen datos","Asignar Actividades");
			return;	
					
			}
		
			if (tab_competencia_tecnica.getTotalFilas()==0 )		{
				utilitario.agregarMensajeInfo("No se puede calificar No existen datos","Asignar Actividades");
			return;	
					

			}*/
		
	TablaGenerica tab_evaluadores=utilitario.consultar("SELECT IDE_GEEDP,IDE_EVEVA,IDE_EVDES FROM EVL_EVALUADORES WHERE IDE_EVEVA="+tab_evaluados.getValorSeleccionado());
	System.out.println("vasvasd"+tab_evaluadores.getValor("IDE_GEEDP"));
	System.out.println("valorseleccionadp"+tab_evaluadores.getValorSeleccionado());
	
	
	if (tab_evaluados.getTotalFilas()>0) {
		System.out.println("TROLO"+tab_evaluadores.getValor("IDE_GEEDP"));
		if(ValidarCompetencias(tab_evaluadores.getValor("IDE_GEEDP"))==true){
			utilitario.agregarMensajeInfo("No se puede Calificar","Falta Ingresar Competencias De acuerdo al Perfil");
			return;
		}
	}
		
		
		
		for (int i = 0; i < tab_actividad_puesto.getTotalFilas(); i++) {
			if (tab_actividad_puesto.getValor(i,"CUMPLIDO_EVACP")==null || tab_actividad_puesto.getValor(i,"CUMPLIDO_EVACP").isEmpty())		{
				utilitario.agregarMensajeInfo("No se puede Calificar","El % de Cumplimiento no puede ser nulo o vacio");
			return;	
			}	
			
					
		}
		
	

		

		for (int i = 0; i <tab_competencia_gestion.getTotalFilas(); i++) {
			
			
			if (tab_competencia_gestion.getValor(i,"IDE_EVNID")==null || tab_competencia_gestion.getValor(i,"IDE_EVNID").isEmpty() ) {
				
				utilitario.agregarMensajeInfo("No se puede calificar ","Debe ingresar campo Nivel Desarollo");
				return;
				
			}
			if (tab_competencia_gestion.getValor(i,"IDE_EVREL")==null || tab_competencia_gestion.getValor(i,"IDE_EVREL").isEmpty() ) {
				
				utilitario.agregarMensajeInfo("No se puede calificar","Debe ingresar campo Relacion Laboral");
				return;
				
			}
		}	
		

		for (int i = 0; i <tab_competencia_institucional.getTotalFilas(); i++) {
			
			
			if (tab_competencia_institucional.getValor(i,"IDE_EVNID")==null || tab_competencia_institucional.getValor(i,"IDE_EVNID").isEmpty() ) {
				
				utilitario.agregarMensajeInfo("No se puede calificar","Debe ingresar campo Nivel Desarollo");
				return;
				
			}
			if (tab_competencia_institucional.getValor(i,"IDE_EVREL")==null || tab_competencia_institucional.getValor(i,"IDE_EVREL").isEmpty() ) {
				
				utilitario.agregarMensajeInfo("No se puede calificar","Debe ingresar campo Relacion Laboral");
				return;
				
			}
		}	
		
		for (int i = 0; i <tab_competencia_tecnica.getTotalFilas(); i++) {
			
			
			if (tab_competencia_tecnica.getValor(i,"IDE_EVNID")==null || tab_competencia_tecnica.getValor(i,"IDE_EVNID").isEmpty() ) {
				
				utilitario.agregarMensajeInfo("No se puede calificar","Debe ingresar campo Nivel Desarollo");
				return;
				
			}
			if (tab_competencia_tecnica.getValor(i,"IDE_EVREL")==null || tab_competencia_tecnica.getValor(i,"IDE_EVREL").isEmpty() ) {
				
				utilitario.agregarMensajeInfo("No se puede calificar","Debe ingresar campo Relacion Laboral");
				return;
				
			}
		}	
		con_guardar.dibujar();
		if(con_guardar.isVisible()){
			con_guardar.setMessage("ESTA SEGURO DE CALIFICAR RESULTADOS");
			con_guardar.setTitle("CONFIRMACION DE CALIFICACIÓN");
			con_guardar.getBot_aceptar().setMetodo("validar");			
			utilitario.addUpdate("con_guardar");			
		}		
	}
	
	public void filtrarNivelCompetenciaGestion(AjaxBehaviorEvent evt) {
		tab_competencia_institucional.modificar(evt);
		TablaGenerica tab_institucional=utilitario.consultar("SELECT * FROM EVL_NIVEL_DESARROLLO WHERE IDE_EVNID="+tab_competencia_gestion.getValor("IDE_EVNID"));
		if(tab_institucional.getTotalFilas()>0){
			tab_competencia_institucional.setValor("NIVEL_CUMPLIDO_EVOTC", tab_institucional.getValor("VALOR_EVNID"));
		}else{
			utilitario.agregarMensajeError("No se puede continuar", "No existe un valor para el nivel de cumplimiento");
		}
		utilitario.addUpdate("tab_tabulador:tab_competencia_gestion");
	}
	
	
	public void filtrarNivel(SelectEvent evt) {
		tab_competencia_institucional.modificar(evt);
		TablaGenerica tab_institucional=utilitario.consultar("SELECT * FROM EVL_NIVEL_DESARROLLO WHERE IDE_EVNID="+tab_competencia_institucional.getValor("IDE_EVNID"));
		if(tab_institucional.getTotalFilas()>0){
			tab_competencia_institucional.setValor("NIVEL_CUMPLIDO_EVOTC", tab_institucional.getValor("VALOR_EVNID"));
		}else{
			utilitario.agregarMensajeError("No se puede continuar", "No existe un valor para el nivel de cumplimiento");
		}
		utilitario.addUpdate("tab_tabulador:tab_competencia_institucional");
	}
	
	public void filtrarNivel(AjaxBehaviorEvent evt) {
		tab_competencia_institucional.modificar(evt);
		TablaGenerica tab_institucional=utilitario.consultar("SELECT * FROM EVL_NIVEL_DESARROLLO WHERE IDE_EVNID="+tab_competencia_institucional.getValor("IDE_EVNID"));
		if(tab_institucional.getTotalFilas()>0){
			tab_competencia_institucional.setValor("NIVEL_CUMPLIDO_EVOTC", tab_institucional.getValor("VALOR_EVNID"));
		}else{
			utilitario.agregarMensajeError("No se puede continuar", "No existe un valor para el nivel de cumplimiento");
		}
		utilitario.addUpdate("tab_tabulador:tab_competencia_institucional");
	}
	
	
	public void filtrarNivelTecnica(SelectEvent evt) {
		tab_competencia_tecnica.modificar(evt);
		TablaGenerica tab_institucional=utilitario.consultar("SELECT * FROM EVL_NIVEL_DESARROLLO WHERE IDE_EVNID="+tab_competencia_tecnica.getValor("IDE_EVNID"));
		if(tab_institucional.getTotalFilas()>0){
			tab_competencia_tecnica.setValor("NIVEL_CUMPLIDO_EVOTC", tab_institucional.getValor("VALOR_EVNID"));
		}else{
			utilitario.agregarMensajeError("No se puede continuar", "No existe un valor para el nivel de cumplimiento");
		}
		utilitario.addUpdate("tab_tabulador:tab_competencia_tecnica");
	}
	
	public void filtrarNivelTecnica(AjaxBehaviorEvent evt) {
		tab_competencia_tecnica.modificar(evt);
		TablaGenerica tab_institucional=utilitario.consultar("SELECT * FROM EVL_NIVEL_DESARROLLO WHERE IDE_EVNID="+tab_competencia_tecnica.getValor("IDE_EVNID"));
		if(tab_institucional.getTotalFilas()>0){
			tab_competencia_tecnica.setValor("NIVEL_CUMPLIDO_EVOTC", tab_institucional.getValor("VALOR_EVNID"));
		}else{
			utilitario.agregarMensajeError("No se puede continuar", "No existe un valor para el nivel de cumplimiento");
		}
		utilitario.addUpdate("tab_tabulador:tab_competencia_tecnica");
	}

	
	public void calcularCumplimiento(AjaxBehaviorEvent evt){
		tab_actividad_puesto.modificar(evt);
		int meta=pckUtilidades.CConversion.CInt(tab_actividad_puesto.getValor("META_EVACP"));
		int cumplido=pckUtilidades.CConversion.CInt(tab_actividad_puesto.getValor("CUMPLIDO_EVACP"));
		if(cumplido<=meta){			
			int  resp=cumplido*100;
			double total_resp=resp/meta;
			java.math.BigDecimal big_total=new java.math.BigDecimal(total_resp);

			TablaGenerica tab_result = utilitario.consultar("select * from evl_escal_calif_indicador where "+big_total+" between por_ini_eveci and por_fin_eveci");			
			tab_actividad_puesto.setValor(tab_actividad_puesto.getFilaActual(),"POR_CUMPLIDO_EVACP",total_resp+"");
			if(tab_result.getTotalFilas()>0){				
				tab_actividad_puesto.setValor(tab_actividad_puesto.getFilaActual(),"NIVEL_CUMPLIDO_EVACP",tab_result.getValor("nivel_cumple_eveci"));
			}
			utilitario.addUpdate("tab_tabulador:tab_actividad_puesto");			
		}
		else {						
			tab_actividad_puesto.setValor("CUMPLIDO_EVACP", "0");
			tab_actividad_puesto.setValor("POR_CUMPLIDO_EVACP", "0");
			tab_actividad_puesto.setValor("NIVEL_CUMPLIDO_EVACP", "0");
			utilitario.agregarMensajeInfo("No se puede calcular", "Debe ingresar un valor igual o menor");
			utilitario.addUpdate("tab_tabulador:tab_actividad_puesto");///revisar el mensaje no aparec
		}
	}

	/**
	 * limpia toda la pantalla incluyendo el autocompletar
	 */

	public void validar(){
		con_guardar.cerrar();
		if(tab_evaluados.getTotalFilas()>0){			
//			utilitario.getConexion().agregarSql("UPDATE EVL_DESEMPENIO SET ACTIVO_EVDES=0 WHERE IDE_EVDES="+tab_evaluados.getValor("IDE_EVDES"));
			utilitario.getConexion().agregarSqlPantalla("DELETE FROM EVL_RESULTADO WHERE IDE_EVEVA="+tab_evaluados.getValorSeleccionado());
			utilitario.getConexion().agregarSqlPantalla("insert into EVL_RESULTADO (IDE_EVRES,IDE_EVEVA,IDE_EVFAE,PESO_FACTOR_EVRES,RESULTADO_EVRES,ACTIVO_EVRES) " +
					"select row_number () over (order by IDE_EVEVA desc) + (select (case when max(ide_evres) is null then 0 else max(ide_evres) end) as contador from evl_resultado) as IDE_EVRES, " +
					"IDE_EVEVA,IDE_EVFAE,peso_por_evgrf,resultado_guarda,0 as ACTIVO_EVRES " +
					"from ( " +
					"select ide_eveva,ide_evfae,ide_gegro,peso_por_evgrf,detalle_gegro,detalle_evfae,por_peso_eveva,valor_maximo,suma_nivel,cuenta_dividir, " +
					"resultado_division,porcentaje_resultado,resultado_final,(resultado_final*por_peso_eveva) /100 as resultado_guarda " +
					"from ( " +
					"select ide_eveva,ide_evfae,ide_gegro,peso_por_evgrf,detalle_gegro,detalle_evfae,por_peso_eveva,valor_maximo,suma_nivel,cuenta_dividir, " +
					"suma_nivel/cuenta_dividir as resultado_division,((suma_nivel/cuenta_dividir)*100)/valor_maximo as porcentaje_resultado, " +
					"((((suma_nivel/cuenta_dividir)*100)/valor_maximo) * peso_por_evgrf)/100 as resultado_final " +
					"from ( " +
					"select ide_eveva,ide_evfae,(case when sum(nivel_cumplido_evotc) is null then 0 else sum(nivel_cumplido_evotc) end) as suma_nivel,count(ide_eveva) as cuenta_dividir,ide_gegro,peso_por_evgrf, " +
					"detalle_gegro,detalle_evfae,valor_maximo,por_peso_eveva " +
					"from ( " +
					"select b.ide_evotc,b.ide_cmdec,a.ide_eveva,b.ide_evfae,nivel_cumplido_evotc,c.ide_geedp,d.ide_gegro, " +
					"peso_por_evgrf,detalle_gegro,detalle_evfae,valor_maximo,por_peso_eveva " +
					"from evl_evaluadores a, evl_otra_competencia b,evl_desempenio c,gen_empleados_departamento_par d, " +
					"( " +
					"select ide_evgrf,a.ide_gegro,a.ide_evfae,peso_por_evgrf,detalle_gegro,detalle_evfae " +
					"from evl_grupo_factor a,gen_grupo_ocupacional b, evl_factor_evaluacion c " +
					"where a.ide_gegro = b.ide_gegro and a.ide_evfae = c.ide_evfae " +
					") e,(select count(valor_evnid) as valor_maximo from evl_nivel_desarrollo) d " +
					"where a.ide_eveva = b.ide_eveva " +
					"and a.ide_evdes = c.ide_evdes " +
					"and c.ide_geedp = d.ide_geedp " +
					"and d.ide_gegro = e.ide_gegro " +
					"and b.ide_evfae = e.ide_evfae " +
					"and a.ide_eveva="+tab_evaluados.getValor("ide_eveva")+" "  +
					") a " +
					"group by ide_eveva,ide_evfae,ide_gegro,peso_por_evgrf,detalle_gegro,detalle_evfae,valor_maximo,por_peso_eveva " +
					") a " +
					") a " +
					") a " );
			utilitario.getConexion().agregarSqlPantalla("insert into EVL_RESULTADO (IDE_EVRES,IDE_EVEVA,IDE_EVFAE,PESO_FACTOR_EVRES,RESULTADO_EVRES,ACTIVO_EVRES) " +
					"select row_number () over (order by IDE_EVEVA desc) + (select (case when max(ide_evres) is null then 0 else max(ide_evres) end) as contador from evl_resultado) as IDE_EVRES, " +
					"IDE_EVEVA,ide_evfae,peso_por_evgrf,resultado_guardar, 0 as ACTIVO_EVRES " +
					"from ( " +
					"select ide_eveva,ide_evfae,ide_geedp,ide_gegro,peso_por_evgrf,detalle_gegro,detalle_evfae,valor_maximo,por_peso_eveva, " +
					"total_porcentaje,peso_factor, (peso_factor*por_peso_eveva)/100 as resultado_guardar " +
					"from ( " +
					"select ide_eveva,ide_evfae,ide_geedp,ide_gegro,peso_por_evgrf,detalle_gegro,detalle_evfae,valor_maximo,por_peso_eveva, " +
					"suma_porcentajes/cuenta_dividendos as total_porcentaje,((suma_porcentajes/cuenta_dividendos)*peso_por_evgrf)/100 as peso_factor " +
					"from ( " +
					"select ide_eveva,ide_evfae,(case when sum(por_cumplido_evacp) is null then  0 else sum(por_cumplido_evacp) end) as suma_porcentajes,count (ide_eveva) as cuenta_dividendos, " +
					"ide_geedp,ide_gegro,peso_por_evgrf,detalle_gegro,detalle_evfae,valor_maximo,por_peso_eveva " +
					"from ( " +
					"select b.ide_evacp,b.ide_cmdec,a.ide_eveva,b.ide_evfae,por_cumplido_evacp,c.ide_geedp,d.ide_gegro, " +
					"peso_por_evgrf,detalle_gegro,detalle_evfae,valor_maximo,por_peso_eveva " +
					"from evl_evaluadores a, evl_actividad_puesto b,evl_desempenio c,gen_empleados_departamento_par d, " +
					"( " +
					"select ide_evgrf,a.ide_gegro,a.ide_evfae,peso_por_evgrf,detalle_gegro,detalle_evfae " +
					"from evl_grupo_factor a,gen_grupo_ocupacional b, evl_factor_evaluacion c " +
					"where a.ide_gegro = b.ide_gegro and a.ide_evfae = c.ide_evfae " +
					") e,(select count(valor_evnid) as valor_maximo from evl_nivel_desarrollo) d " +
					"where a.ide_eveva = b.ide_eveva " +
					"and a.ide_evdes = c.ide_evdes " +
					"and c.ide_geedp = d.ide_geedp " +
					"and d.ide_gegro = e.ide_gegro " +
					"and b.ide_evfae = e.ide_evfae " +
					"and a.ide_eveva="+tab_evaluados.getValor("ide_eveva")+" " +
					") a " +
					"group by  ide_eveva,ide_evfae,ide_geedp,ide_gegro,peso_por_evgrf,detalle_gegro,detalle_evfae,valor_maximo,por_peso_eveva " +
					") a " +
					") a " +
					") a " );
			utilitario.getConexion().agregarSqlPantalla("update evl_evaluadores set activo_eveva=FALSE where ide_eveva="+tab_evaluados.getValor("ide_eveva")+ ""); 
			utilitario.getConexion().agregarSqlPantalla(" update evl_actividad_puesto set activo_evacp=0 where ide_eveva="+tab_evaluados.getValor("ide_eveva")+" " );
			utilitario.getConexion().agregarSqlPantalla("update evl_otra_competencia set activo_evotc=TRUE where ide_eveva="+tab_evaluados.getValor("ide_eveva")+" ");
			utilitario.getConexion().agregarSqlPantalla("update evl_evaluadores set fecha_evaluacion_eveva=to_date('"+utilitario.getFechaActual()+"','yyyy-mm-dd') where ide_eveva="+tab_evaluados.getValor("ide_eveva")+" ");
			guardarPantalla();
			tab_evaluados.ejecutarSql();
		}
	}


	@Override
	public void insertar() {
		if (tab_actividad_puesto.isFocus()){			
			if (tab_evaluados.getTotalFilas()>0){
				tab_actividad_puesto.insertar();
			}else{
				utilitario.agregarMensajeInfo("No se puede insertar", "Primero debe seleccionar un Evaluador");				
			}					
		}	
		else if (tab_competencia_gestion.isFocus()){			
			if (tab_evaluados.getTotalFilas()>0){
				tab_competencia_gestion.insertar();
			}else{
				utilitario.agregarMensajeInfo("No se puede insertar", "Primero debe seleccionar un Evaluador");				
			}						
		}
		else if (tab_competencia_institucional.isFocus()){			
			if (tab_evaluados.getTotalFilas()>0){
				tab_competencia_institucional.insertar();
			}else{
				utilitario.agregarMensajeInfo("No se puede insertar", "Primero debe seleccionar un Evaluador");				
			}						
		}
		else if (tab_competencia_tecnica.isFocus()){			
			if (tab_evaluados.getTotalFilas()>0){
				tab_competencia_tecnica.insertar();
			}else{
				utilitario.agregarMensajeInfo("No se puede insertar", "Primero debe seleccionar un Evaluador");				
			}						
		}
	}

	@Override
	public void guardar() {										
		if (tab_actividad_puesto.guardar()){
			
			for (int i = 0; i <tab_competencia_gestion.getTotalFilas(); i++) {
				
			
if (tab_competencia_gestion.getValor(i,"IDE_EVNID")==null || tab_competencia_gestion.getValor(i,"IDE_EVNID").isEmpty() ) {
	
	utilitario.agregarMensajeInfo("No se puede insertar","Debe ingresar campo Nivel Desarollo");
	return;
	
}
if (tab_competencia_gestion.getValor(i,"IDE_EVREL")==null || tab_competencia_gestion.getValor(i,"IDE_EVREL").isEmpty() ) {
	
	utilitario.agregarMensajeInfo("No se puede insertar","Debe ingresar campo Relacion Laboral");
	return;
	
}
			}
			if(tab_competencia_gestion.guardar()){
				
				
				for (int i = 0; i <tab_competencia_institucional.getTotalFilas(); i++) {
					
					
					if (tab_competencia_institucional.getValor(i,"IDE_EVNID")==null || tab_competencia_institucional.getValor(i,"IDE_EVNID").isEmpty() ) {
						
						utilitario.agregarMensajeInfo("No se puede insertar","Debe ingresar campo Nivel Desarollo");
						return;
						
					}
					if (tab_competencia_institucional.getValor(i,"IDE_EVREL")==null || tab_competencia_institucional.getValor(i,"IDE_EVREL").isEmpty() ) {
						
						utilitario.agregarMensajeInfo("No se puede guardar ","Debe ingresar campo Relacion Laboral");
						return;
						
					}
				}
				
				
				if(tab_competencia_institucional.guardar()){
				
					
					for (int i = 0; i <tab_competencia_tecnica.getTotalFilas(); i++) {
						
						
						if (tab_competencia_tecnica.getValor(i,"IDE_EVNID")==null || tab_competencia_tecnica.getValor(i,"IDE_EVNID").isEmpty() ) {
							
							utilitario.agregarMensajeInfo("No se puede guardar ","Debe ingresar campo Nivel Desarollo");
							return;
							
						}
						if (tab_competencia_tecnica.getValor(i,"IDE_EVREL")==null || tab_competencia_tecnica.getValor(i,"IDE_EVREL").isEmpty() ) {
							
							utilitario.agregarMensajeInfo("No se puede guardar","Debe ingresar campo Relacion Laboral");
							return;
							
						}
					}	
					
					if (tab_competencia_tecnica.guardar()) {
						guardarPantalla();guardarPantalla();
					}					
				}																		
			}
		}				
	}

	@Override
	public void eliminar() {					
		if(tab_actividad_puesto.isFocus()){
			tab_actividad_puesto.eliminar();
		}			
		else if(tab_competencia_gestion.isFocus()){
			tab_competencia_gestion.eliminar();
		}
		else if(tab_competencia_institucional.isFocus()){
			tab_competencia_institucional.eliminar();
		}
		else if(tab_competencia_tecnica.isFocus()){
			tab_competencia_tecnica.eliminar();
		}
	}

	@Override
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}
	
	@Override
	public void aceptarReporte() {
		if (rep_reporte.getReporteSelecionado().equals("Resultado Evaluacion")){
			if (tab_evaluados.getTotalFilas()>0) {
				if (rep_reporte.isVisible()){
					p_parametros=new HashMap();				
					rep_reporte.cerrar();				
					p_parametros.put("IDE_EVDES",pckUtilidades.CConversion.CInt(tab_evaluados.getValor("IDE_EVDES")));					
					p_parametros.put("titulo", "RESULTADO EVALUACIÓN");
					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());						
					sef_reporte.dibujar();
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun empleado");
			}
		}
	}

	public Tabla getTab_evaluados() {
		return tab_evaluados;
	}


	public void setTab_evaluados(Tabla tab_evaluados) {
		this.tab_evaluados = tab_evaluados;
	}


	public Tabla getTab_actividad_puesto() {
		return tab_actividad_puesto;
	}


	public void setTab_actividad_puesto(Tabla tab_actividad_puesto) {
		this.tab_actividad_puesto = tab_actividad_puesto;
	}


	public Tabla getTab_competencia_gestion() {
		return tab_competencia_gestion;
	}


	public void setTab_competencia_gestion(Tabla tab_competencia_gestion) {
		this.tab_competencia_gestion = tab_competencia_gestion;
	}


	public Tabla getTab_competencia_institucional() {
		return tab_competencia_institucional;
	}


	public void setTab_competencia_institucional(Tabla tab_competencia_institucional) {
		this.tab_competencia_institucional = tab_competencia_institucional;
	}


	public Tabla getTab_competencia_tecnica() {
		return tab_competencia_tecnica;
	}


	public void setTab_competencia_tecnica(Tabla tab_competencia_tecnica) {
		this.tab_competencia_tecnica = tab_competencia_tecnica;
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


	public Confirmar getCon_guardar() {
		return con_guardar;
	}


	public void setCon_guardar(Confirmar con_guardar) {
		this.con_guardar = con_guardar;
	}

}
