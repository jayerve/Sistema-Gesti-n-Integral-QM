package paq_salud;

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
import paq_contabilidad.ejb.ServicioContabilidad;
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

 
public class pre_ficha_medica_preocu extends Pantalla {

	private Tabla tab_ficha_medica=new Tabla();
	private Tabla tab_receta_medica=new Tabla();
	private Tabla tab_ficha_diagnostico=new Tabla();
	private Tabla tab_ficha_examenes=new Tabla();
	private Tabla tab_ficha_motivo_consulta=new Tabla();
	private Tabla tab_ficha_anamnesis=new Tabla();
	private Tabla tab_codigo_sie=new Tabla();
	

	private AutoCompletar aut_empleado = new AutoCompletar();
	private Confirmar con_guardar=new Confirmar();
	@EJB
	private ServicioEmpleado ser_empleado=(ServicioEmpleado) utilitario.instanciarEJB(ServicioEmpleado.class);
	@EJB
	private ServicioNomina ser_nomina=(ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);

	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();
	private Map p_parametros=new HashMap();	
	@EJB
	private ServicioGestion ser_gestion = (ServicioGestion) utilitario.instanciarEJB(ServicioGestion.class);

	public pre_ficha_medica_preocu() {

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

		

		//  FICHA MEDICA (division 1)

		tab_ficha_medica.setId("tab_ficha_medica");
		tab_ficha_medica.setTabla("SAO_FICHA_MEDICA", "IDE_SAFIM", 1);
		tab_ficha_medica.getColumna("ACTIVO_SAFIM").setCheck();
		tab_ficha_medica.getColumna("ACTIVO_SAFIM").setValorDefecto("true");
		tab_ficha_medica.getColumna("IDE_SAESP").setCombo("SAO_ESPECIALIDAD", "IDE_SAESP", "DETALLE_SAESP", "");
		tab_ficha_medica.getColumna("IDE_SATIC").setCombo("SAO_TIPO_CONSULTA", "IDE_SATIC", "DETALLE_SATIC", "");
		tab_ficha_medica.getColumna("IDE_USUA").setCombo("SIS_USUARIO", "IDE_USUA", "NOM_USUA", "");
		tab_ficha_medica.getColumna("ide_saapp").setCombo("sao_actitud_preocupacional", "ide_saapp", "detalle_saapp", "");
		tab_ficha_medica.getColumna("ide_coest").setCombo(ser_contabilidad.getModuloEstados("true", "13"));
		tab_ficha_medica.getColumna("ide_coest").setVisible(false);
		tab_ficha_medica.getColumna("IDE_USUA").setAutoCompletar();
		tab_ficha_medica.getColumna("IDE_USUA").setLectura(true);
		tab_ficha_medica.getColumna("IDE_USUA").setValorDefecto(utilitario.getVariable("ide_usua"));
		tab_ficha_medica.getColumna("mes_edad_safim").setVisible(false);
		tab_ficha_medica.getColumna("TALLA_SAFIM").setMascara("9.99");
		tab_ficha_medica.getColumna("TALLA_SAFIM").setMetodoChange("calcularMasaCorporal");
		tab_ficha_medica.getColumna("PESO_SAFIM").setMetodoChange("calcularMasaCorporal");
		tab_ficha_medica.getColumna("TEMPERATURA_SAFIM").setMascara("99");
		tab_ficha_medica.getColumna("PULSO_SAFIM").setMascara("99");
		tab_ficha_medica.getColumna("FRE_CARDIACA_SAFIM").setMascara("99");
		tab_ficha_medica.getColumna("FRE_RESPIRATORIA_SAFIM").setMascara("99");
		tab_ficha_medica.getColumna("IMC_SAFIM").setFormatoNumero(2);
		tab_ficha_medica.getColumna("IMC_SAFIM").setEtiqueta();
		tab_ficha_medica.getColumna("SUPERFICIE_CORPORAL_SAFIM").setFormatoNumero(2);
		tab_ficha_medica.getColumna("SUPERFICIE_CORPORAL_SAFIM").setEtiqueta();
		tab_ficha_medica.getColumna("IDE_SUCU").setLectura(true);
		tab_ficha_medica.getColumna("IDE_GTEMP").setVisible(false);
		tab_ficha_medica.getColumna("IDE_GEEDP").setVisible(false);		
		tab_ficha_medica.getColumna("ide_gtesc").setCombo("gth_estado_civil", "ide_gtesc", "detalle_gtesc", "");
		tab_ficha_medica.getColumna("ide_saapp").setVisible(true);
		tab_ficha_medica.getColumna("ide_gtgen").setCombo("gth_genero", "ide_gtgen", "detalle_gtgen", "");
		tab_ficha_medica.getColumna("tipo_empleado_safim").setVisible(false);
		tab_ficha_medica.getColumna("tipo_empleado_safim").setValorDefecto("1");
		tab_ficha_medica.getColumna("PRESUNTIVO_SAFIM").setCheck();
		tab_ficha_medica.getColumna("PRESUNTIVO_SAFIM").setValorDefecto("false");
		tab_ficha_medica.getColumna("DEFINITIVO_SAFIM").setCheck();
		tab_ficha_medica.getColumna("DEFINITIVO_SAFIM").setValorDefecto("false");
		tab_ficha_medica.getColumna("INTERCONSULTA_SAFIM").setCheck();
		tab_ficha_medica.getColumna("INTERCONSULTA_SAFIM").setValorDefecto("false");
		tab_ficha_medica.getColumna("PRIMERA_SAFIM").setCheck();
		tab_ficha_medica.getColumna("PRIMERA_SAFIM").setValorDefecto("false");
		tab_ficha_medica.getColumna("SUBSECUENTE_SAFIM").setCheck();
		tab_ficha_medica.getColumna("SUBSECUENTE_SAFIM").setValorDefecto("false");
		tab_ficha_medica.getColumna("IDE_SATIC").setRequerida(true);
		tab_ficha_medica.getColumna("PESO_SAFIM").setRequerida(true);
		tab_ficha_medica.getColumna("TEMPERATURA_SAFIM").setRequerida(true);
		tab_ficha_medica.getColumna("FRE_RESPIRATORIA_SAFIM").setRequerida(true);
		tab_ficha_medica.getColumna("SUBSECUENTE_SAFIM ").setRequerida(true);
		tab_ficha_medica.getColumna("TALLA_SAFIM").setRequerida(true);
		tab_ficha_medica.getColumna("PULSO_SAFIM").setVisible(false);
		tab_ficha_medica.getColumna("FRE_CARDIACA_SAFIM").setRequerida(true);		
		tab_ficha_medica.getColumna("IDE_SAESP").setRequerida(true);
		tab_ficha_medica.getColumna("IMC_SAFIM").setRequerida(true);
		tab_ficha_medica.getColumna("SUPERFICIE_CORPORAL_SAFIM").setRequerida(true);
		tab_ficha_medica.getColumna("FECHA_CONSULTA_SAFIM").setRequerida(true);		
		tab_ficha_medica.getColumna("nombre_safim").setRequerida(true);		
		tab_ficha_medica.getColumna("fecha_nacimiento_safim").setRequerida(true);		
		tab_ficha_medica.getColumna("apellidos_safim").setRequerida(true);		
		tab_ficha_medica.getColumna("OBSERVACIONES_SAFIM").setRequerida(true);
		tab_ficha_medica.getColumna("fecha_desde_cert_safim").setMetodoChange("calcularDias");
		tab_ficha_medica.getColumna("fecha_hasta_cert_safim").setMetodoChange("calcularDias");
		tab_ficha_medica.getColumna("nro_dia_cert_safim").setEtiqueta();
		tab_ficha_medica.getGrid().setColumns(6);
		tab_ficha_medica.setTipoFormulario(true);	
		tab_ficha_medica.agregarRelacion(tab_receta_medica);
		tab_ficha_medica.agregarRelacion(tab_ficha_diagnostico);
		tab_ficha_medica.agregarRelacion(tab_ficha_examenes);
		tab_ficha_medica.agregarRelacion(tab_ficha_motivo_consulta);
		tab_ficha_medica.agregarRelacion(tab_ficha_anamnesis);	
		tab_ficha_medica.agregarRelacion(tab_codigo_sie);
		tab_ficha_medica.setCondicion("tipo_empleado_safim = 1");		
		tab_ficha_medica.dibujar();

		PanelTabla pat_panel1=new PanelTabla();
		pat_panel1.setPanelTabla(tab_ficha_medica);
		tab_ficha_medica.setHeader("FICHA MEDICA EMPLEADO");

		Tabulador tab_tabulador=new Tabulador();
		tab_tabulador.setId("tab_tabulador");

		//  RECETA MEDICA (TABULADOR 1)
		tab_receta_medica.setId("tab_receta_medica");
		tab_receta_medica.setIdCompleto("tab_tabulador:tab_receta_medica");
		tab_receta_medica.setTabla("SAO_RECETA_MEDICA", "IDE_SAREM", 2);
		tab_receta_medica.getColumna("ACTIVO_SAREM").setCheck();
		tab_receta_medica.getColumna("ACTIVO_SAREM").setValorDefecto("true");
		tab_receta_medica.getColumna("IDE_SAMED").setCombo("SAO_MEDICACION", "IDE_SAMED", "DETALLE_SAMED", "");
		tab_receta_medica.getColumna("IDE_SAMED").setAutoCompletar();
		tab_receta_medica.getColumna("IDE_SAMED").setVisible(false);

		tab_receta_medica.getColumna("CANTIDAD_SAREM").setRequerida(true);
		tab_receta_medica.getColumna("INDICACION_SAREM").setRequerida(true);

		tab_receta_medica.dibujar();

		PanelTabla pat_panel2=new PanelTabla();
		pat_panel2.setPanelTabla(tab_receta_medica);

		// FICHA DIAGNOSTICO (TABULADOR 2)

		tab_ficha_diagnostico.setId("tab_ficha_diagnostico");
		tab_ficha_diagnostico.setIdCompleto("tab_tabulador:tab_ficha_diagnostico");
		tab_ficha_diagnostico.setTabla("SAO_FICHA_DIAGNOSTICO", "IDE_SAFID", 3);
		tab_ficha_diagnostico.getColumna("ACTIVO_SAFID").setCheck();
		tab_ficha_diagnostico.getColumna("ACTIVO_SAFID").setValorDefecto("true");
		tab_ficha_diagnostico.getColumna("IDE_SARED").setCombo("SAO_REGISTRO_DIAGNOSTICO", "IDE_SARED", "DETALLE_SARED", "");
		tab_ficha_diagnostico.getColumna("IDE_SARED").setVisible(false);
		tab_ficha_diagnostico.dibujar();

		PanelTabla pat_panel3=new PanelTabla();
		pat_panel3.setPanelTabla(tab_ficha_diagnostico);

		//  RECETA EXAMENES (TABULADOR 3)

		tab_ficha_examenes.setId("tab_ficha_examenes");
		tab_ficha_examenes.setIdCompleto("tab_tabulador:tab_ficha_examenes");
		tab_ficha_examenes.setTabla("SAO_FICHA_EXAMENES", "IDE_SAFIE", 4);
		tab_ficha_examenes.getColumna("ACTIVO_SAFIE").setCheck();
		tab_ficha_examenes.getColumna("ACTIVO_SAFIE").setValorDefecto("true");
		tab_ficha_examenes.getColumna("IDE_SAEXA").setCombo("SAO_EXAMENES", "IDE_SAEXA", "DETALLE_SAEXA", "");
		tab_ficha_examenes.getColumna("IDE_SAEXA").setAutoCompletar();
		tab_ficha_examenes.getColumna("IDE_SAEXA").setVisible(false);
		tab_ficha_examenes.dibujar();

		PanelTabla pat_panel4=new PanelTabla();
		pat_panel4.setPanelTabla(tab_ficha_examenes);

		//  RECETA MOTIVO CONSULTA (TABULADOR 4)

		tab_ficha_motivo_consulta.setId("tab_ficha_motivo_consulta");
		tab_ficha_motivo_consulta.setIdCompleto("tab_tabulador:tab_ficha_motivo_consulta");
		tab_ficha_motivo_consulta.setTabla("SAO_FICHA_MOTIVO_CONSULTA", "IDE_SAFMC", 5);
		tab_ficha_motivo_consulta.getColumna("ACTIVO_SAFMC").setCheck();
		tab_ficha_motivo_consulta.getColumna("ACTIVO_SAFMC").setValorDefecto("true");
		tab_ficha_motivo_consulta.getColumna("IDE_SAMOC").setCombo("SAO_MOTIVO_CONSULTA", "IDE_SAMOC", "DETALLE_SAMOC", "");
		tab_ficha_motivo_consulta.getColumna("IDE_SAMOC").setAutoCompletar();
		tab_ficha_motivo_consulta.getColumna("IDE_SAMOC").setVisible(false);
		tab_ficha_motivo_consulta.dibujar();

		PanelTabla pat_panel5=new PanelTabla();
		pat_panel5.setPanelTabla(tab_ficha_motivo_consulta);

		//  RECETA ANAMNESIS (TABULADOR 5)

		tab_ficha_anamnesis.setId("tab_ficha_anamnesis");
		tab_ficha_anamnesis.setIdCompleto("tab_tabulador:tab_ficha_anamnesis");
		tab_ficha_anamnesis.setTabla("SAO_FICHA_ANAMNESIS", "IDE_SAFIA", 6);
		tab_ficha_anamnesis.getColumna("ACTIVO_SAFIA").setCheck();
		tab_ficha_anamnesis.getColumna("ACTIVO_SAFIA").setValorDefecto("true");
		tab_ficha_anamnesis.getColumna("IDE_SAANA").setCombo("SAO_ANAMNESIS", "IDE_SAANA", "DETALLE_SAANA", "");
		tab_ficha_anamnesis.getColumna("DETALLE_SAFIA").setRequerida(true);
		tab_ficha_anamnesis.dibujar();

		PanelTabla pat_panel6=new PanelTabla();
		pat_panel6.setPanelTabla(tab_ficha_anamnesis);
		
	   //  RECETA ANAMNESIS (TABULADOR 6)

			tab_codigo_sie.setId("tab_codigo_sie");
			tab_codigo_sie.setIdCompleto("tab_tabulador:tab_codigo_sie");
			tab_codigo_sie.setTabla("sao_ficha_sie10", "ide_safis", 7);
			tab_codigo_sie.getColumna("ide_sacos").setCombo("sao_codigo_sie10", "ide_sacos", "detalle_sacos", "");
			tab_codigo_sie.getColumna("ACTIVO_SAFIS").setValorDefecto("true");

			tab_codigo_sie.dibujar();

			PanelTabla pat_panel7=new PanelTabla();
			pat_panel7.setPanelTabla(tab_codigo_sie);

		tab_tabulador.agregarTab("RECETA MEDICA", pat_panel2);
		tab_tabulador.agregarTab("DIAGNOSTICO", pat_panel3);
		tab_tabulador.agregarTab("EXAMENES", pat_panel4);
		tab_tabulador.agregarTab("MOTIVO CONSULTA", pat_panel5);
		tab_tabulador.agregarTab("ANAMNESIS", pat_panel6);
		tab_tabulador.agregarTab("FICHA SIE 10", pat_panel7);


		//  DIVISION DE LA PANTALLA
		Division div_division=new Division();
		div_division.dividir2(pat_panel1,tab_tabulador,"60%","H");
		agregarComponente(div_division);

		// confirmacion para guardar datos
		//con_guardar.setId("con_guardar");
	//	agregarComponente(con_guardar);

	}

	/*String ide_geedp_activo="";
	public void filtrarAnticiposEmpleado(SelectEvent evt){
		aut_empleado.onSelect(evt);
		ide_geedp_activo=ser_gestion.getIdeContratoActivo(aut_empleado.getValor());
		tab_ficha_medica.setCondicion("IDE_GTEMP="+aut_empleado.getValor());
		tab_ficha_medica.ejecutarSql();
		tab_receta_medica.ejecutarValorForanea(tab_ficha_medica.getValorSeleccionado());
		tab_ficha_diagnostico.ejecutarValorForanea(tab_ficha_medica.getValorSeleccionado());
		tab_ficha_examenes.ejecutarValorForanea(tab_ficha_medica.getValorSeleccionado());
		tab_ficha_motivo_consulta.ejecutarValorForanea(tab_ficha_medica.getValorSeleccionado());
		tab_ficha_anamnesis.ejecutarValorForanea(tab_ficha_medica.getValorSeleccionado());
	}

	/**
	 * limpia toda la pantalla incluyendo el autocompletar
	 */
	/*public void limpiar() {
		tab_ficha_medica.limpiar();
		tab_receta_medica.limpiar();
		tab_ficha_diagnostico.limpiar();
		tab_ficha_examenes.limpiar();
		tab_ficha_motivo_consulta.limpiar();
		tab_ficha_anamnesis.limpiar();
		tab_codigo_sie.limpiar();
		
	/*	ide_geedp_activo="";
		aut_empleado.limpiar();
		aut_empleado.limpiar();
		utilitario.addUpdate("aut_empleado");// limpia y refresca el autocompletar
	}*/



	@Override
	public void insertar() {
		utilitario.getTablaisFocus().insertar();

	}
		// TODO Auto-generated method stub
		/*if (tab_ficha_medica.isFocus()){
			
				
					tab_ficha_medica.insertar();
			
		}
		else if (tab_receta_medica.isFocus()){
			if (aut_empleado.getValor()!=null){
				if (tab_ficha_medica.getTotalFilas()>0){
					tab_receta_medica.insertar();
				}else{
					utilitario.agregarMensajeInfo("No se puede insertar", "Primero debe insertar un Ficha Medica");				
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar el Empleado");
			}			
		}
		else if (tab_ficha_diagnostico.isFocus()){
			if (aut_empleado.getValor()!=null){
				if (tab_ficha_medica.getTotalFilas()>0){
					tab_ficha_diagnostico.insertar();
				}else{
					utilitario.agregarMensajeInfo("No se puede insertar", "Primero debe insertar un Ficha Medica");				
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar el Empleado");
			}			
		}	
		else if (tab_ficha_examenes.isFocus()){
			if (aut_empleado.getValor()!=null){
				if (tab_ficha_medica.getTotalFilas()>0){
					tab_ficha_examenes.insertar();
				}else{
					utilitario.agregarMensajeInfo("No se puede insertar", "Primero debe insertar un Ficha Medica");				
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar el Empleado");
			}			
		}

		else if (tab_ficha_motivo_consulta.isFocus()){
			if (aut_empleado.getValor()!=null){
				if (tab_ficha_medica.getTotalFilas()>0){
					tab_ficha_motivo_consulta.insertar();
				}else{
					utilitario.agregarMensajeInfo("No se puede insertar", "Primero debe insertar un Ficha Medica");				
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar el Empleado");
			}			
		}
		else if (tab_ficha_anamnesis.isFocus()){
			if (aut_empleado.getValor()!=null){
				if (tab_ficha_medica.getTotalFilas()>0){
					tab_ficha_anamnesis.insertar();
				}else{
					utilitario.agregarMensajeInfo("No se puede insertar", "Primero debe insertar un Ficha Medica");				
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar el Empleado");
			}			
		}
		else if (tab_codigo_sie.isFocus()){
			tab_codigo_sie.insertar();
		}
	}*/

	@Override
	public void guardar() {		
		
				if (tab_ficha_medica.guardar()){	
						if(tab_receta_medica.guardar()){					
								if (tab_ficha_diagnostico.guardar()){
										if(tab_ficha_examenes.guardar()){
												if (tab_ficha_motivo_consulta.guardar()) {
														if (tab_ficha_anamnesis.guardar()) {
															if(tab_codigo_sie.guardar()){
															guardarPantalla();
															}
														}	
													}
												}
											}							
						
					}
				}
		
	}

	@Override
	public void eliminar() {
		//if (aut_empleado.getValor()!=null){
			utilitario.getTablaisFocus().eliminar();
//			if (tab_ficha_medica.isFocus()){	
//				tab_ficha_medica.eliminar();							
//			}else if(tab_receta_medica.isFocus()){
//				tab_receta_medica.eliminar();
//			}
//			else if(tab_ficha_diagnostico.isFocus()){
//				tab_ficha_diagnostico.eliminar();
//			}
//			else if(tab_receta_medica.isFocus()){
//				tab_receta_medica.eliminar();
//			}
//			else if(tab_ficha_examenes.isFocus()){
//				tab_ficha_examenes.eliminar();
//			}
//			else if(tab_ficha_anamnesis.isFocus()){
//				tab_ficha_anamnesis.eliminar();
//			}
		}
		//else{
		//	utilitario.agregarMensajeInfo("No se puede guardar el Anticipo", "Debe seleccionar un Empleado");
		//}
	

	public AutoCompletar getAut_empleado() {
		return aut_empleado;
	}

	public void setAut_empleado(AutoCompletar aut_empleado) {
		this.aut_empleado = aut_empleado;
	}

	public Confirmar getCon_guardar() {
		return con_guardar;
	}

	public void setCon_guardar(Confirmar con_guardar) {
		this.con_guardar = con_guardar;
	}

	public Tabla gettab_ficha_medica() {
		return tab_ficha_medica;
	}

	public void settab_ficha_medica(Tabla tab_ficha_medica) {
		this.tab_ficha_medica = tab_ficha_medica;
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

	public Tabla getTab_ficha_medica() {
		return tab_ficha_medica;
	}

	public void setTab_ficha_medica(Tabla tab_ficha_medica) {
		this.tab_ficha_medica = tab_ficha_medica;
	}

	public Tabla getTab_receta_medica() {
		return tab_receta_medica;
	}

	public void setTab_receta_medica(Tabla tab_receta_medica) {
		this.tab_receta_medica = tab_receta_medica;
	}

	public Tabla getTab_ficha_diagnostico() {
		return tab_ficha_diagnostico;
	}

	public void setTab_ficha_diagnostico(Tabla tab_ficha_diagnostico) {
		this.tab_ficha_diagnostico = tab_ficha_diagnostico;
	}

	public Tabla getTab_ficha_examenes() {
		return tab_ficha_examenes;
	}

	public void setTab_ficha_examenes(Tabla tab_ficha_examenes) {
		this.tab_ficha_examenes = tab_ficha_examenes;
	}

	public Tabla getTab_ficha_motivo_consulta() {
		return tab_ficha_motivo_consulta;
	}

	public void setTab_ficha_motivo_consulta(Tabla tab_ficha_motivo_consulta) {
		this.tab_ficha_motivo_consulta = tab_ficha_motivo_consulta;
	}

	public Tabla getTab_ficha_anamnesis() {
		return tab_ficha_anamnesis;
	}

	public void setTab_ficha_anamnesis(Tabla tab_ficha_anamnesis) {
		this.tab_ficha_anamnesis = tab_ficha_anamnesis;
	}

	@Override
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}

	@Override
	public void aceptarReporte() {	
		if (rep_reporte.getReporteSelecionado().equals("Ficha Medica Empleado")){		
			
				if (tab_ficha_medica.getTotalFilas()>0) {			
					if (rep_reporte.isVisible()){
						p_parametros=new HashMap();		
						rep_reporte.cerrar();		
						p_parametros.put("ide_safim",pckUtilidades.CConversion.CInt(tab_ficha_medica.getValor("IDE_SAFIM")));
						p_parametros.put("titulo", "DETALLE FICHA MEDICA POR EMPLEADO");
						sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());				
						sef_reporte.dibujar();
						sef_reporte.getGri_cuerpo().getChildren().get(0).getChildren().remove(4);
						sef_reporte.getGri_cuerpo().getChildren().get(0).getChildren().remove(3);						
					}
				}else{
					utilitario.agregarMensajeInfo("No se puede continuar", "No contiene Ficha Medica");
				}
			
			
		}
		else if (rep_reporte.getReporteSelecionado().equals("Receta Medica")){		
				if (tab_ficha_medica.getTotalFilas()>0) {					
						if (rep_reporte.isVisible()){
							p_parametros=new HashMap();		
							rep_reporte.cerrar();		
							p_parametros.put("ide_safim",pckUtilidades.CConversion.CInt(tab_ficha_medica.getValor("IDE_SAFIM")));
							p_parametros.put("titulo", "DETALLE FICHA MEDICA POR EMPLEADO");
							sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());				
							sef_reporte.dibujar();
							
						}									
				}else{
					utilitario.agregarMensajeInfo("No se puede continuar", "No contiene Ficha Medica");
				}
			
		}else 	if (rep_reporte.getReporteSelecionado().equals("Certificado Medico")){		
				if (tab_ficha_medica.getTotalFilas()>0) {			
					if (rep_reporte.isVisible()){
						p_parametros=new HashMap();		
						rep_reporte.cerrar();		
						p_parametros.put("ide_safim",pckUtilidades.CConversion.CInt(tab_ficha_medica.getValor("IDE_SAFIM")));
						p_parametros.put("titulo", "CERTIFICADO MEDICO");
						sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());				
						sef_reporte.dibujar();
						
					}
				}else{
					utilitario.agregarMensajeInfo("No se puede continuar", "No contiene Certificado Medico");
				}
			}
			
		}
	
	public void calcularMasaCorporal(AjaxBehaviorEvent evt){
		tab_ficha_medica.modificar(evt);
		double dou_imc=0;
		double dou_talla2=0;
		if(tab_ficha_medica.getValor("TALLA_SAFIM")!=null && !tab_ficha_medica.getValor("TALLA_SAFIM").isEmpty()){
			if(tab_ficha_medica.getValor("PESO_SAFIM")!=null && !tab_ficha_medica.getValor("PESO_SAFIM").isEmpty()){
				double dou_talla=Double.parseDouble(tab_ficha_medica.getValor("TALLA_SAFIM"));
				double dou_peso=Double.parseDouble(tab_ficha_medica.getValor("PESO_SAFIM"));
				if(dou_peso>0 && dou_talla>0){
										
					dou_talla2=dou_talla*dou_talla;
					
					dou_imc=dou_peso/dou_talla2;
					
					tab_ficha_medica.setValor("IMC_SAFIM",dou_imc+"");
					utilitario.addUpdateTabla(tab_ficha_medica, "IMC_SAFIM", "");
					calcularSuperficieCorporal(dou_talla, dou_peso);
				}else{
					utilitario.agregarMensajeInfo("No se puede calcular el IMC y la Superficie Corporal", "Debe ingresar la valores mayores a cero");
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede calcular el IMC y la Superficie Corporal", "Debe ingresar la Peso del Empleado");
			}				
		}else{
			utilitario.agregarMensajeInfo("No se puede calcular el IMC y la Superficie Corporal", "Debe ingresar la Talla del Empleado");
		}
	}

	public void calcularSuperficieCorporal(double dou_talla_corp, double dou_peso_corp){		
		double dou_superficie_corp=0;
		double dou_talla_cm=0;		
		dou_talla_cm=dou_talla_corp*100;
		dou_superficie_corp=((0.7184)*(Math.pow(dou_talla_cm,0.725))*(Math.pow(dou_peso_corp,0.425)))/100;
		tab_ficha_medica.setValor("SUPERFICIE_CORPORAL_SAFIM", dou_superficie_corp+"");
		utilitario.addUpdateTabla(tab_ficha_medica, "SUPERFICIE_CORPORAL_SAFIM", "");				
	}

	public boolean validarFichaMedica(){
		if(tab_ficha_medica.isFilaInsertada()){
			if(tab_ficha_medica.getValor("IDE_SATIC")==null || tab_ficha_medica.getValor("IDE_SATIC").isEmpty()){
				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar el Tipo de Consulta ");
				return false;
			}
			if(tab_ficha_medica.getValor("PESO_SAFIM")==null || tab_ficha_medica.getValor("PESO_SAFIM").isEmpty()){
				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar el Peso ");
				return false;
			}
			if(tab_ficha_medica.getValor("TEMPERATURA_SAFIM")==null || tab_ficha_medica.getValor("TEMPERATURA_SAFIM").isEmpty()){
				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar la Temperatura ");
				return false;
			}
			if(tab_ficha_medica.getValor("FRE_RESPIRATORIA_SAFIM")==null || tab_ficha_medica.getValor("FRE_RESPIRATORIA_SAFIM").isEmpty()){
				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar  la Frecuencia Respiratoria ");
				return false;
			}
		
			if(tab_ficha_medica.getValor("TALLA_SAFIM")==null || tab_ficha_medica.getValor("TALLA_SAFIM").isEmpty()){
				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar la Talla ");
				return false;
			}
			if(tab_ficha_medica.getValor("PULSO_SAFIM")==null || tab_ficha_medica.getValor("PULSO_SAFIM").isEmpty()){
				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar el Pulso ");
				return false;
			}
			if(tab_ficha_medica.getValor("FRE_CARDIACA_SAFIM")==null || tab_ficha_medica.getValor("FRE_CARDIACA_SAFIM").isEmpty()){
				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar la Frecuencia Cardiaca ");
				return false;
			}
			if(tab_ficha_medica.getValor("ACTIVO_SAFIM")==null || tab_ficha_medica.getValor("ACTIVO_SAFIM").isEmpty()){
				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar el Estado Activo");
				return false;
			}
			if(tab_ficha_medica.getValor("IDE_SAESP")==null || tab_ficha_medica.getValor("IDE_SAESP").isEmpty()){
				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar el Tipo de Especialidad");
				return false;
			}
			if(tab_ficha_medica.getValor("IMC_SAFIM")==null || tab_ficha_medica.getValor("IMC_SAFIM").isEmpty()){
				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar el IMC ");
				return false;
			}
			if(tab_ficha_medica.getValor("SUPERFICIE_CORPORAL_SAFIM")==null || tab_ficha_medica.getValor("SUPERFICIE_CORPORAL_SAFIM").isEmpty()){
				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar la Superficie Corporal ");
				return false;
			}
			if(tab_ficha_medica.getValor("FECHA_CONSULTA_SAFIM")==null || tab_ficha_medica.getValor("FECHA_CONSULTA_SAFIM").isEmpty()){
				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar la fecha de Consulta ");
				return false;
			}
			if(tab_ficha_medica.getValor("OBSERVACIONES_SAFIM")==null || tab_ficha_medica.getValor("OBSERVACIONES_SAFIM").isEmpty()){
				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar la Observacion");
				return false;
			}
			if(tab_ficha_medica.getValor("SUBSECUENTE_SAFIM")==null || tab_ficha_medica.getValor("SUBSECUENTE_SAFIM").isEmpty()){
				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar el Subsecuente");
				return false;
			}

		}
		return true;
	}

	public boolean validarRecetaMedica(){
		if(tab_receta_medica.isFilaInsertada()){
			if(tab_receta_medica.getValor("IDE_SAMED")==null || tab_receta_medica.getValor("IDE_SAMED").isEmpty()){
				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar La Medicación ");
				return false;
			}
			if(tab_receta_medica.getValor("CANTIDAD_SAREM")==null || tab_receta_medica.getValor("CANTIDAD_SAREM").isEmpty()){
				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar la Cantidad ");
				return false;
			}
			if(tab_receta_medica.getValor("INDICACION_SAREM")==null || tab_receta_medica.getValor("INDICACION_SAREM").isEmpty()){
				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar la Indicación ");
				return false;
			}
		}
		return true;
	}
	public boolean validarDiagnostico(){
		if(tab_ficha_diagnostico.isFilaInsertada()){
			if(tab_ficha_diagnostico.isFilaInsertada()){
				if(tab_ficha_diagnostico.getValor("IDE_SARED")==null || tab_ficha_diagnostico.getValor("IDE_SARED").isEmpty()){
					utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar el Registro del Diagnostico ");
					return false;
				}
			}
		}
		return true;		
	}
	public boolean validarExamenes(){
		if(tab_ficha_examenes.isFilaInsertada()){
			if(tab_ficha_examenes.getValor("IDE_SAEXA")==null || tab_ficha_examenes.getValor("IDE_SAEXA").isEmpty()){
				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar los Examenes ");
				return false;
			}
		}
		return true;		
	}
	public boolean validarMotivoConsulta(){
		if(tab_ficha_motivo_consulta.isFilaInsertada()){
			if(tab_ficha_motivo_consulta.getValor("IDE_SAMOC")==null || tab_ficha_motivo_consulta.getValor("IDE_SAMOC").isEmpty()){
				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar El Motivo de la Consulta ");
				return false;
			}	
		}
		return true;		
	}

	public boolean validarAnamnesis(){
		if(tab_ficha_anamnesis.isFilaInsertada()){
			if(tab_ficha_anamnesis.getValor("IDE_SAANA")==null || tab_ficha_anamnesis.getValor("IDE_SAANA").isEmpty()){
				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar la Anamnesis ");
				return false;
			}
			if(tab_ficha_anamnesis.getValor("DETALLE_SAFIA")==null || tab_ficha_anamnesis.getValor("DETALLE_SAFIA").isEmpty()){
				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar El Detalle de la Anamnesis ");
				return false;
			}
		}
		return true;		
	}

/*	public void edad(){
		int a=pckUtilidades.CConversion.CInt((tab_ficha_medica.getColumna("ANIO_EDAD_SAFIM").toString()));
		int  b=	utilitario.getAnio(utilitario.getFechaActual());
		int anios_totales=a-b;
		int mes=pckUtilidades.CConversion.CInt((tab_ficha_medica.getColumna("MES_EDAD_SAFIM").toString()));
		int  mes_actual=utilitario.getMes(utilitario.getFechaActual());
		int meses_totales=mes-mes_actual;
		
	}*/
	
	public void calcularDias(DateSelectEvent evt){
		tab_ficha_medica.modificar(evt);
		if((tab_ficha_medica.getValor("fecha_desde_cert_safim")==null || tab_ficha_medica.getValor("fecha_desde_cert_safim").isEmpty()) 
				|| (tab_ficha_medica.getValor("fecha_hasta_cert_safim")==null || tab_ficha_medica.getValor("fecha_hasta_cert_safim").isEmpty())){
			return;
		}
		if (utilitario.isFechaMenor(utilitario.getFecha(tab_ficha_medica.getValor("FECHA_HASTA_CERT_SAFIM")), utilitario.getFecha(tab_ficha_medica.getValor("FECHA_DESDE_CERT_SAFIM")))){
			utilitario.agregarMensajeInfo("No se puede guardar", "La fecha hasta no puede ser menor que la fecha desde");
			return ;
		}
		//if(aut_empleado.getValor()!=null){
			try {
				int nro_dias=0;
				if (utilitario.isFechaMenor(utilitario.getFecha(tab_ficha_medica.getValor("fecha_hasta_cert_safim")), utilitario.getFecha(tab_ficha_medica.getValor("fecha_desde_cert_safim")))){
					utilitario.agregarMensajeError("No se puede calcular el numero de dias", "La fecha hasta no puede ser menor que la fecha desde");
				}else{						
					nro_dias=utilitario.getDiferenciasDeFechas(utilitario.getFecha(tab_ficha_medica.getValor("fecha_desde_cert_safim")), utilitario.getFecha(tab_ficha_medica.getValor("fecha_hasta_cert_safim")));
					tab_ficha_medica.setValor(tab_ficha_medica.getFilaActual(),"nro_dia_cert_safim",(nro_dias+1)+"");
					utilitario.addUpdateTabla(tab_ficha_medica, "nro_dia_cert_safim", "");
				}	
			} catch (Exception e) {
				utilitario.agregarMensajeInfo("No se puede calcular el numero de dias", "La fecha hasta no puede ser menor que la fecha desde");
			}					
		}
	//else{
			//utilitario.agregarMensajeInfo("No se puede calcular los dias de vacación", "");
		//}	
//	}

public void calcularDiasVacacion(SelectEvent evt){
	tab_ficha_medica.modificar(evt);
		//if(aut_empleado.getValor()!=null){				
			if (utilitario.isFechaMenor(utilitario.getFecha(tab_ficha_medica.getValor("fecha_hasta_cert_safim")), utilitario.getFecha(tab_ficha_medica.getValor("fecha_desde_cert_safim")))){
				utilitario.agregarMensajeInfo("No se puede calcular el numero de dias", "La fecha hasta no puede ser menor que la fecha desde");		
			}else{				
				int nro_dias=0;		
				nro_dias=utilitario.getDiferenciasDeFechas(utilitario.getFecha(tab_ficha_medica.getValor("fecha_desde_cert_safim")), utilitario.getFecha(tab_ficha_medica.getValor("fecha_hasta_cert_safim")));
				tab_ficha_medica.setValor(tab_ficha_medica.getFilaActual(),"nro_dia_cert_safim",(nro_dias+1)+"");
				utilitario.addUpdateTabla(tab_ficha_medica, "nro_dia_cert_safim", "");
				System.out.println("numero de dias: "+(nro_dias+1));
			}					
		}
/*else{
			utilitario.agregarMensajeInfo("No se puede calcular los dias de vacación", "");
		}	
	}	*/
	
	/*public String getEdadCompleta(String fecha_nac,boolean en_letras){
		if (fecha_nac!=null && !fecha_nac.isEmpty()) {
			String fecha_actual=utilitario.getFechaActual();
			int mes_nac=0,dia_nac=0,anio_nac=0;
			int mes_actual=0,dia_actual=0,anio_actual=0;
			int mes_edad=0,dia_edad=0,anio_edad=0;

			anio_nac=utilitario.getAnio(utilitario.getFormatoFecha(utilitario.getFecha(fecha_nac)));
			mes_nac=utilitario.getMes(utilitario.getFormatoFecha(utilitario.getFecha(fecha_nac)));
			dia_nac=utilitario.getDia(utilitario.getFormatoFecha(utilitario.getFecha(fecha_nac)));

			anio_actual=utilitario.getAnio(fecha_actual);
			mes_actual=utilitario.getMes(fecha_actual);
			dia_actual=utilitario.getDia(fecha_actual);

			if (anio_nac<=anio_actual) {
				anio_edad=anio_actual-anio_nac;
				if (mes_nac>mes_actual) {
					anio_edad=anio_edad-1;
					mes_edad=12-mes_nac+mes_actual;
				}
				if (mes_nac==mes_actual) {
					if (dia_nac>dia_actual) {
						anio_edad=anio_edad-1;
					}
					mes_edad=mes_actual-mes_nac;
				}
				if (mes_nac<mes_actual) {
					mes_edad=mes_actual-mes_nac;
				}
				if (dia_nac>dia_actual) {
					dia_edad=30-dia_nac+dia_actual;
				}else {
					dia_edad=dia_actual-dia_nac;
				}
				//return ""+anio_edad+""+mes_edad+""+dia_edad;
				if (en_letras){
					return anio_edad+" anos "+mes_edad+" meses "+dia_edad+" dias";	
				}else{
					return anio_edad+"-"+mes_edad+"-"+dia_edad;
				}
			}
		}
		return null;
	}
	
	public String getEdadSoloAnios(String str_anio_nacimiento ){
		String edad=getEdadCompleta(str_anio_nacimiento, false);		
		return edad.substring(0,edad.indexOf("-"));
	}
	public String getEdadSoloMeses(String str_anio_nacimiento ){
		String edad=getEdadCompleta(str_anio_nacimiento, false);
		edad=edad.substring(edad.indexOf("-")+1,edad.length());		
		return edad.substring(0,edad.indexOf("-"));
	}

	public void calcularEdadEmpleado(){
		TablaGenerica tab_edad_empleado=utilitario.consultar("SELECT * FROM gth_empleado  where ide_gtemp="+aut_empleado.getValor());
		if (tab_edad_empleado.getTotalFilas()>0) {
			String str_anio_nacimiento="";
			if (tab_edad_empleado.getValor("FECHA_NACIMIENTO_GTEMP")!=null && !tab_edad_empleado.getValor("FECHA_NACIMIENTO_GTEMP").isEmpty()) {
				str_anio_nacimiento=tab_edad_empleado.getValor("FECHA_NACIMIENTO_GTEMP");				
				tab_ficha_medica.modificar(tab_ficha_medica.getFilaActual());
				tab_ficha_medica.setValor("ANIO_EDAD_SAFIM", getEdadSoloAnios(str_anio_nacimiento));
				tab_ficha_medica.setValor("MES_EDAD_SAFIM", getEdadSoloMeses(str_anio_nacimiento));
				utilitario.addUpdateTabla(tab_ficha_medica, "ANIO_EDAD_SAFIM,MES_EDAD_SAFIM", "");
			}			
		}
	}*/

	public Tabla getTab_codigo_sie() {
		return tab_codigo_sie;
	}

	public void setTab_codigo_sie(Tabla tab_codigo_sie) {
		this.tab_codigo_sie = tab_codigo_sie;
	}

	public Map getP_parametros() {
		return p_parametros;
	}

	public void setP_parametros(Map p_parametros) {
		this.p_parametros = p_parametros;
	}
	
	
	
	
}
