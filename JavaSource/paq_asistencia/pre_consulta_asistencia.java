package paq_asistencia;

import javax.ejb.EJB;

import org.primefaces.event.SelectEvent;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Check;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionCalendario;
import framework.componentes.Tabla;
import paq_asistencia.ejb.ServicioAsistencia;
import paq_gestion.ejb.ServicioGestion;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;

public class pre_consulta_asistencia extends Pantalla{

	@EJB

	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);

	@EJB
	private ServicioGestion ser_gestion = (ServicioGestion) utilitario.instanciarEJB(ServicioGestion.class);
	@EJB
	private ServicioAsistencia ser_asistencia = (ServicioAsistencia) utilitario.instanciarEJB(ServicioAsistencia.class);

	private AutoCompletar aut_empleado = new AutoCompletar();
	private Tabla tab_consulta=new Tabla();
	private Combo com_rubros = new Combo();

	private SeleccionCalendario sec_rango=new SeleccionCalendario();
	private Check che_todos_emp=new Check();

	public pre_consulta_asistencia(){
		bar_botones.getBot_eliminar().setRendered(false);
		bar_botones.getBot_guardar().setRendered(false);
		bar_botones.getBot_insertar().setRendered(false);

		// boton limpiar
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarBoton(bot_limpiar);

		// boton consultar
		Boton bot_consultar = new Boton();
		bot_consultar.setIcon("ui-icon-calculator");
		bot_consultar.setMetodo("consultar");
		bot_consultar.setValue("Consultar");
		bot_consultar.setTitle("Consultar");
		bar_botones.agregarBoton(bot_consultar);

		tab_consulta.setId("tab_consulta");
		tab_consulta.setSql(ser_asistencia.getConsultaAsistencia("1900-01-01", "1900-01-01"));
		tab_consulta.getColumna("ide_asmot").setVisible(false);
		tab_consulta.getColumna("ide_gtemp").setVisible(false);
		tab_consulta.getColumna("ide_geedp").setVisible(false);
		tab_consulta.getColumna("tipo_aspvh").setVisible(false);
		tab_consulta.getColumna("aprobado_tthh_aspvh").setVisible(false);

		tab_consulta.setCampoPrimaria("ide_aspvh");
		tab_consulta.setLectura(true);
		tab_consulta.setNumeroTabla(1);
		tab_consulta.dibujar();
		tab_consulta.setRows(20);

		PanelTabla pat_tab=new PanelTabla();
		pat_tab.setPanelTabla(tab_consulta);

		Division div1=new Division();
		div1.dividir1(pat_tab);


		agregarComponente(div1);

		sec_rango.setId("sec_rango");
		sec_rango.setTitle("Seleccione un Rango de Fechas");

		agregarComponente(sec_rango);

	}

	public void limpiar(){
		tab_consulta.setSql(ser_asistencia.getConsultaAsistencia("1900-01-01", "1900-01-01"));
		tab_consulta.ejecutarSql();		
	}

	String str_fecha1="";
	String str_fecha2="";
	
	public void consultar(){
		


		if (!sec_rango.isVisible()){
			sec_rango.setFecha1(null);
			sec_rango.setFecha2(null);
			str_fecha1="";
			str_fecha2="";
			sec_rango.getBot_aceptar().setMetodo("consultar");
			sec_rango.dibujar();
			utilitario.addUpdate("sec_rango");
		}else{

			if (!sec_rango.isFechasValidas()){
				str_fecha1="";
				str_fecha2="";
				utilitario.agregarMensajeInfo("No se puede consultar", "fechas incorrectas");
				return;
			}



			System.out.println("che "+che_todos_emp.getValue());

			str_fecha1=sec_rango.getFecha1String();
			str_fecha2=sec_rango.getFecha2String();
			
				tab_consulta.setSql(ser_asistencia.getConsultaAsistencia(str_fecha1, str_fecha2));
			
			tab_consulta.ejecutarSql();
			utilitario.addUpdate("tab_consulta");


			sec_rango.cerrar();
			
			if (tab_consulta.getTotalFilas()==0){
				utilitario.agregarMensajeInfo("No existen transacciones registradas ", "");
			}
		}
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

	public AutoCompletar getAut_empleado() {
		return aut_empleado;
	}

	public void setAut_empleado(AutoCompletar aut_empleado) {
		this.aut_empleado = aut_empleado;
	}

	public Tabla getTab_consulta() {
		return tab_consulta;
	}

	public void setTab_consulta(Tabla tab_consulta) {
		this.tab_consulta = tab_consulta;
	}


	public SeleccionCalendario getSec_rango() {
		return sec_rango;
	}


	public void setSec_rango(SeleccionCalendario sec_rango) {
		this.sec_rango = sec_rango;
	}



}
