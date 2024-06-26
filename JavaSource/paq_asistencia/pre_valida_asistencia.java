/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_asistencia;

import javax.ejb.EJB;

import org.primefaces.event.SelectEvent;

import paq_asistencia.ejb.ServicioControl;
import paq_sistema.aplicacion.Pantalla;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.MarcaAgua;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionCalendario;
import framework.componentes.Tabla;
import framework.correo.EnviarCorreo;

/**
 *
 */
public class pre_valida_asistencia extends Pantalla {


	private Tabla tab_tabla = new Tabla();
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
	
	private SeleccionCalendario sec_importar=new SeleccionCalendario();
	
	@EJB
	private ServicioControl ser_control = (ServicioControl) utilitario.instanciarEJB(ServicioControl.class);
	
	private AutoCompletar aut_empleados=new AutoCompletar();

	
	public pre_valida_asistencia() {      
		bar_botones.quitarBotonsNavegacion();
				
		bar_botones.agregarComponente(new Etiqueta("Fecha Inicial :"));
		cal_fecha_inicial.setValue(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		bar_botones.agregarComponente(cal_fecha_inicial);

		bar_botones.agregarComponente(new Etiqueta("Fecha Final :"));
		cal_fecha_final.setFechaActual();
		bar_botones.agregarComponente(cal_fecha_final);

		Boton bot_filtrar = new Boton();
		bot_filtrar.setValue("Actualizar");
		bot_filtrar.setMetodo("actualizarMarcaciones");
		bot_filtrar.setIcon("ui-icon-refresh");
		bar_botones.agregarBoton(bot_filtrar);
		
		bar_botones.agregarSeparador();
		
		aut_empleados.setId("aut_empleados");
		aut_empleados.setAutoCompletar("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end)|| ' ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, " +
				"SUCU.NOM_SUCU, AREA.DETALLE_GEARE, " +
				"DEPA.DETALLE_GEDEP " +
				"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
				"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
				"LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " +
				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE " +
				"WHERE EPAR.ACTIVO_GEEDP=true");
		aut_empleados.setMetodoChange("seleccionoEmpleado");
		
		bar_botones.agregarComponente(aut_empleados);
	
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarBoton(bot_limpiar);
		
		bar_botones.agregarSeparador();		

		Boton bot_importar = new Boton();
		bot_importar.setValue("Importar Marcaciones");
		bot_importar.setMetodo("importar");
		bot_importar.setIcon("ui-icon-clock");
		bar_botones.agregarBoton(bot_importar);
			
		tab_tabla.setId("tab_tabla");				
		tab_tabla.setTabla("ASI_VALIDA_ASISTENCIA", "IDE_ASVAA", 1);
		tab_tabla.setCondicion("TO_CHAR(FECHA_MARCACION_ASVAA,'YYYY-MM-DD') BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"'");
		tab_tabla.getColumna("FECHA_MARCACION_ASVAA").setValorDefecto(utilitario.getFechaActual());
		tab_tabla.getColumna("FECHA_MARCACION_ASVAA").setLectura(true);
		tab_tabla.getColumna("HORA_MARCACION_ASVAA").setLectura(true);
		tab_tabla.getColumna("DIFERENCIA_ASVAA").setLectura(true);		
		tab_tabla.getColumna("NOVEDAD_ASVAA").setCheck();
		tab_tabla.getColumna("ACTIVO_ASVAA").setVisible(false);
		tab_tabla.getColumna("EVENTO_ASVAA").setLectura(true);
		tab_tabla.getColumna("HORA_MARCA_SALIDA_ASVAA").setLectura(true);
		tab_tabla.getColumna("DIFERENCIA_SALIDA_ASVAA").setLectura(true);
		tab_tabla.getColumna("IDE_ASDHE").setVisible(false);
		
		tab_tabla.getColumna("VERIFICADO_ASVAA").setCheck();
		tab_tabla.getColumna("VERIFICADO_ASVAA").setValorDefecto("true");
		
		tab_tabla.getColumna("ACTIVO_ASVAA").setValorDefecto("true");
		tab_tabla.getColumna("IDE_GEEDP").setCombo(aut_empleados.getLista());
		tab_tabla.getColumna("IDE_GEEDP").setAutoCompletar();
		tab_tabla.getColumna("IDE_GEEDP").setLectura(true);
		tab_tabla.getColumna("ide_asnov").setVisible(false);
		tab_tabla.getColumna("IMPORTO_ASVAA").setVisible(false);
		tab_tabla.setEmptyMessage("No se encontraron marcaciones en el rango de fechas seleccionado");
		tab_tabla.setRows(20);
		tab_tabla.dibujar();		
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_tabla);

		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir1(pat_panel);
		agregarComponente(div_division);
		
		sec_importar.setId("sec_importar");
	    agregarComponente(sec_importar);
	    
	    Boton bot_enviar=new Boton();
		bot_enviar.setMetodo("enviarCorreo");
		bot_enviar.setValue("Enviar E-mail");
		bot_enviar.setIcon("ui-icon-mail-closed");
		bar_botones.agregarBoton(bot_enviar);
	}
	
	
	public void enviarCorreo(){
		TablaGenerica tab_gen=utilitario.consultar("SELECT IDE_ASVAA, a.IDE_GEEDP, FECHA_MARCACION_ASVAA, HORA_MARCACION_ASVAA, DIFERENCIA_ASVAA, " +
				"NOVEDAD_ASVAA, ACTIVO_ASVAA,  VERIFICADO_ASVAA, EVENTO_ASVAA, " +
				"IDE_ASNOV, HORA_MARCA_SALIDA_ASVAA, DIFERENCIA_SALIDA_ASVAA ,c.gen_ide_gegro,c.gen_ide_gecaf, " +
				"empleado as empleado_recibe_mensaje,detalle_gtcor,empleado_atrasado " +
				"FROM ASI_VALIDA_ASISTENCIA a, gen_empleados_departamento_par b,gen_grupo_cargo_supervisa c,( " +
				"select ide_geedp,apellido_paterno_gtemp||' '||apellido_materno_gtemp||' '||primer_nombre_gtemp||' '||segundo_nombre_gtemp as empleado, " +
				"detalle_gtcor,ide_gegro,ide_gecaf " +
				"from gth_empleado a,gen_empleados_departamento_par b,gth_correo c " +
				"where a.ide_gtemp = b.ide_gtemp and a.ide_gtemp = c.ide_gtemp " +
				"and notificacion_gtcor=true and activo_gtcor=true ) d, ( " +
				"select ide_geedp,apellido_paterno_gtemp||' '||apellido_materno_gtemp||' '||primer_nombre_gtemp||' '||segundo_nombre_gtemp as empleado_atrasado " +
				"from gth_empleado a,gen_empleados_departamento_par b,gth_correo c " +
				"where a.ide_gtemp = b.ide_gtemp and a.ide_gtemp = c.ide_gtemp " +
				"and notificacion_gtcor=true and activo_gtcor=true ) e " +
				"WHERE TO_CHAR(FECHA_MARCACION_ASVAA,'YYYY-MM-DD') BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"' " +
				"and a.ide_geedp = b.ide_geedp and b.ide_gegro= c.gen_ide_gegro and b.ide_gecaf = c.gen_ide_gecaf " +
				"and c.ide_gegro=d.ide_gegro and c.ide_gecaf= d.ide_gecaf and a.ide_geedp = e.ide_geedp ORDER BY IDE_ASVAA");
	
		EnviarCorreo env_enviar = new EnviarCorreo();
		//Enviar a cada uno de los evaluadores
		String str_asunto="NOVEDAD DE MARCACIONES";
	if(tab_gen.isEmpty()==false){
		
	
		for(int i=0;i<tab_gen.getTotalFilas();i++){
			
			StringBuilder str_mensaje=new StringBuilder();				
			str_mensaje.append("<p style='font-size: 18px;font-weight: bold'>").append("NOVEDAD DE MARCACIONES").append("</p>").append(" \n");
			str_mensaje.append("<p style='font-size: 14px;font-weight: bold'>").append("EMPLEADO :"+tab_gen.getValor(i,"EMPLEADO_ATRASADO")).append("</p>").append(" \n");
			str_mensaje.append("<p style='font-size: 14px;font-weight: bold'>").append("FECHA :"+utilitario.getFormatoFecha(tab_gen.getValor(i,"FECHA_MARCACION_ASVAA"))).append("</p>").append(" \n");
			str_mensaje.append("<p style='font-size: 14px;font-weight: bold'>").append("FECHA :"+utilitario.getFormatoHora(tab_gen.getValor(i,"HORA_MARCACION_ASVAA"))).append("</p>").append(" \n");
			str_mensaje.append("<p>Para su conocimiento, se le informa que el siguiente empleado : <strong>").append(tab_gen.getValor(i,"EMPLEADO_ATRASADO")).append(" </strong> tiene nuna novedad de : "+tab_gen.getValor(i, "EVENTO_ASVAA") +" " );			
						
			String str_mail=tab_gen.getValor(i, "detalle_gtcor");
			System.out.println(str_mail+"  ");
			if(str_mail==null || str_mail.isEmpty()){
				utilitario.agregarMensajeError("El empleado  "+tab_gen.getValor(i, "empleado_recibe_mensaje")+" no tiene configurado un correo para notificaciones", "");
				continue;
			}
			String str_msj= env_enviar.agregarCorreo(str_mail, str_asunto, str_mensaje.toString(), null);
			if(str_msj.isEmpty()==false){
				//Fallo el envio de coorreo
				//str_resultado.append(getFormatoError("No se puede enviar el correo a "+str_mail+" motivo: "+str_msj));
				utilitario.agregarMensajeError("No se pudo enviar a "+str_mail,str_msj);
			}

		}
		String str=env_enviar.enviarTodos();
		if(str.trim().isEmpty()){
			utilitario.agregarMensaje("Se envio correctamente", "");
		}
		else{
			utilitario.agregarMensajeError("No se pudo enviar",str);
		}
		
	}
	else{
		utilitario.agregarMensajeInfo("No existen registros en el rango de fechas actual", "");
	}

	}
	
	
	public void aceptarEliminar(){
		String str_elimino=ser_control.eliminarResumen(sec_importar.getFecha1String(), sec_importar.getFecha2String());
		if(str_elimino.isEmpty()){
			utilitario.agregarMensaje("Se Elimino Correctamente", "");
			tab_tabla.ejecutarSql();	
		}
		sec_importar.cerrar();
				
	}
	
	public void aceptarImportar(){
		int int_total=ser_control.resumirAsistencia(sec_importar.getFecha1String(), sec_importar.getFecha2String());		
		sec_importar.cerrar();
		if(int_total>0){
			utilitario.agregarMensaje("Se Guardo Correctamente", "Se importaron "+int_total+" registros");
			tab_tabla.ejecutarSql();	
		}
		else{
			utilitario.agregarMensajeInfo("No se encontraron marcaciones en el rango de fechas seleccionado", "");
		}
				
	}
	
	public void importar(){
		sec_importar.setTitle("IMPORTAR MARCACIONES");
		sec_importar.setFooter("Recuerde que si ya existen marcaciones importadas en las fechas seleccionadas, se sobreescribiran los registros ");
		sec_importar.setFecha1(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		sec_importar.setFecha2(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		sec_importar.getBot_aceptar().setMetodo("aceptarImportar");
		sec_importar.dibujar();
	}
	
	public void limpiar(){
		aut_empleados.limpiar();
		utilitario.addUpdate("aut_empleados");
		actualizarMarcaciones();
	}

	public void actualizarMarcaciones(){
		if (utilitario.isFechasValidas(cal_fecha_inicial.getFecha(), cal_fecha_final.getFecha())) {
			if(aut_empleados.getValor()!=null){
				tab_tabla.setCondicion("IDE_GEEDP="+aut_empleados.getValor()+" AND TO_CHAR(FECHA_MARCACION_ASVAA,'YYYY-MM-DD') BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"'");	
			}
			else{
				tab_tabla.setCondicion("TO_CHAR(FECHA_MARCACION_ASVAA,'YYYY-MM-DD') BETWEEN '"+cal_fecha_inicial.getFecha()+"' AND '"+cal_fecha_final.getFecha()+"'");
			}
				
			tab_tabla.ejecutarSql();
		}
		else {
			utilitario.agregarMensajeInfo("Rangos de fechas no válidos",
					"");
		}		
	}

	@Override
	public void insertar() {
		tab_tabla.insertar();
	}

	@Override
	public void guardar() {
		tab_tabla.guardar();
		guardarPantalla();
	}

	public void seleccionoEmpleado(SelectEvent evt){
		aut_empleados.onSelect(evt);
		actualizarMarcaciones();	
	}
	
	@Override
	public void eliminar() {
		sec_importar.setTitle("ELIMINAR RESUMEN ASISTENCIA");
		sec_importar.setFooter(null);
		sec_importar.setFecha1(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		sec_importar.setFecha2(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		sec_importar.getBot_aceptar().setMetodo("aceptarEliminar");
		sec_importar.dibujar();
	}

	public Tabla getTab_tabla() {
		return tab_tabla;
	}

	public void setTab_tabla(Tabla tab_tabla) {
		this.tab_tabla = tab_tabla;
	}

	public SeleccionCalendario getSec_importar() {
		return sec_importar;
	}

	public void setSec_importar(SeleccionCalendario sec_importar) {
		this.sec_importar = sec_importar;
	}

	public AutoCompletar getAut_empleados() {
		return aut_empleados;
	}

	public void setAut_empleados(AutoCompletar aut_empleados) {
		this.aut_empleados = aut_empleados;
	}
	
}
