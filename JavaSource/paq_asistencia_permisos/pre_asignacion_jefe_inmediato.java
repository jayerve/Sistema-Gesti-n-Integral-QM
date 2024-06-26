/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_asistencia_permisos;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import paq_gestion.ejb.ServicioEmpleado;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

/**
 *
 * @author DELL-USER
 */
public class pre_asignacion_jefe_inmediato extends Pantalla {
	@EJB
	private ServicioEmpleado ser_empleado=(ServicioEmpleado) utilitario.instanciarEJB(ServicioEmpleado.class);
	@EJB
	private ServicioNomina ser_nomina=(ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	@EJB
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	
    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
	String ide_geedp_activo="";

    public pre_asignacion_jefe_inmediato() {
        TablaGenerica tabEmpDep = ser_seguridad.getEmpledoPartida(utilitario.getVariable("ide_usua"));
        
      

        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("asi_jefe_inmediato", "ide_asjei", 1);
		tab_tabla1.getColumna("IDE_GTEMP").setVisible(true);

        tab_tabla1.getColumna("IDE_GEEDP").setCombo("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, " +
				"AREA.DETALLE_GEARE,CASE WHEN EPAR.ACTIVO_GEEDP=true then 'ACTIVO' ELSE 'INACTIVO' END ACTIVO_GEEDP " +
				//"DEPA.DETALLE_GEDEP " +
				"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
				"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
				"LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " +
				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE ");
		tab_tabla1.getColumna("IDE_GEEDP").setNombreVisual("EMPLEADO");
		tab_tabla1.getColumna("IDE_GEEDP").setAutoCompletar();
		tab_tabla1.getColumna("IDE_GEEDP").setMetodoChange("setEmpleadoArea");
		tab_tabla1.getColumna("IDE_GEEDP").setOrden(1);

		
		
		tab_tabla1.getColumna("ide_geare").setNombreVisual("AREA");
		//tab_tabla1.getColumna("ide_geare").setAutoCompletar();
		tab_tabla1.getColumna("ide_geare").setCombo("SELECT ide_geare, detalle_geare "
		 		+ "FROM gen_area "
		 		+ "WHERE ACTIVO_GEARE=TRUE");
		tab_tabla1.getColumna("ide_geare").setOrden(2);

		///tab_tabla1.getColumna("ide_geare").setMetodoChange("setEmpleadoArea");;
		
		


          
  
		
		
		List listax = new ArrayList();
		Object fila1[] = { "1", "ADMINISTRADOR" };
		Object fila2[] = { "2", "JEFE INMEDIATO" };
		// Object fila8[] = {"2", "ACTIVOS FIJOS"};
		listax.add(fila1);
		listax.add(fila2);
		//tab_tabla1.getColumna("tipo_asjei").setRadio(listax, "2");
		tab_tabla1.getColumna("tipo_asjei").setCombo("SELECT ide_astip, descripcion_astip "
				+ "from asi_tipo_perfil");
		tab_tabla1.getColumna("tipo_asjei").setRequerida(true);
		tab_tabla1.getColumna("tipo_asjei").setOrden(3);

		
		tab_tabla1.getColumna("ide_gtemp_padre_asjei").setNombreVisual("JEFE / COORDINADOR / GERENTE");
		tab_tabla1.getColumna("ide_gtemp_padre_asjei").setCombo("SELECT EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
  				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
  				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
  				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
  				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS " +
  				" FROM GTH_EMPLEADO EMP  ");
		tab_tabla1.getColumna("ide_gtemp_padre_asjei").setNombreVisual("JEFE / COORDINADOR / GERENTE");
		tab_tabla1.getColumna("ide_gtemp_padre_asjei").setAutoCompletar();

		tab_tabla1.getColumna("ide_gtemp_padre_asjei").setOrden(4);

          
		tab_tabla1.getColumna("cargo_padre_asjei").setNombreVisual("CARGO");
		tab_tabla1.getColumna("cargo_padre_asjei").setLongitud(35);
		tab_tabla1.getColumna("cargo_padre_asjei").setOrden(5);
		
		tab_tabla1.getColumna("activo_asjei").setOrden(6);
  		tab_tabla1.getColumna("activo_asjei").setCheck();
  		tab_tabla1.getColumna("activo_asjei").setValorDefecto("true");
  		tab_tabla1.getColumna("activo_asjei").setNombreVisual("ACTIVO");
  		//tab_tabla1.getColumna("activo_asjei").setLectura(true);
  		//ide_geedp_activo=ser_gestion.getIdeContratoActivo(tab_perm
		

		ide_geedp_activo=tabEmpDep.getValor("IDE_GEEDP");
		if (utilitario.getVariable("ide_usua").equals("393")) {
		}else{
		tab_tabla1.setCondicion("IDE_GEEDP IN ("+ide_geedp_activo+")");}
        tab_tabla1.agregarRelacion(tab_tabla2);
        tab_tabla1.setTipoFormulario(true);
        tab_tabla1.getGrid().setColumns(4);
        tab_tabla1.setStyle("width:100%;");

        tab_tabla1.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla1);

        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("asi_empleado_jefe_inmediato", "ide_emjei", 2);
        
        tab_tabla2.getColumna("IDE_GTEMP").setCombo("SELECT EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS " +
				" FROM GTH_EMPLEADO EMP  ");
        tab_tabla2.getColumna("IDE_GTEMP").setNombreVisual("EMPLEADO ASIGNADO");
        tab_tabla2.getColumna("IDE_GTEMP").setAutoCompletar();
		tab_tabla2.getColumna("IDE_GTEMP").setMetodoChange("getEmpleadoAsignado");
		tab_tabla2.getColumna("IDE_GTEMP").setOrden(1);
		

        
        

		
        tab_tabla2.getColumna("activo_emjei").setOrden(18);
        tab_tabla2.getColumna("activo_emjei").setCheck();
        tab_tabla2.getColumna("activo_emjei").setValorDefecto("true");
        tab_tabla2.getColumna("activo_emjei").setNombreVisual("ACTIVO");
  		tab_tabla2.getColumna("activo_emjei").setOrden(6);

        
        //tab_tabla2.getColumna("activo_emjei").setLectura(true);
		//ide_geedp_activo=ser_gestion.getIdeContratoActivo(tab_perm
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pat_panel1, pat_panel2, "30%", "H");
        agregarComponente(div_division);
        
    }

    @Override
    public void insertar() {
    	if (tab_tabla1.isFocus()){
    		tab_tabla1.insertar();
		}else if(tab_tabla2.isFocus()){
			tab_tabla2.insertar();
		}
    }

    @Override
    public void guardar() {
    	
		// TODO Auto-generated method stub
	/*	ser_contabilidad.limpiarAcceso("ASI_PERMISOS_VACACION_HEXT");
		ser_contabilidad.limpiarAcceso("ASI_PERMISO_JUSTIFICACION");
		if (aut_empleado.getValor()!=null){
			System.out.println("ingresa al if de autocompletar: ");
			if (validarSolicitudPermiso()){
				System.out.println("ingresa al if : validarSolicitudPermiso");
				if (tab_permisos.guardar()){
					System.out.println("ingresa al if : tab_permisos");
					if (tab_permiso_justificacion.guardar()) {
						System.out.println("ingresa al if : tab_permiso_justificacion");
						guardarPantalla();	
					}					
				}
			}
		}else{
			utilitario.agregarMensajeInfo("No se puede guardar el Permiso", "Debe seleccionar un Empleado");
		}*/
    	

        if (tab_tabla1.guardar()) {
        
            if (tab_tabla2.guardar()) {
                        }
        	guardarPantalla();
        }

    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
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


	public void setEmpleadoArea() {
	//	tab_tabla1.modificar(evt);
		if (tab_tabla1.getValor("ide_geedp") == null) {
			utilitario.agregarMensajeInfo("Debe Seleccionar un empleado", "No se puede continuar");
			return;
		}
		TablaGenerica  tab_empleado_=utilitario.consultar("SELECT EPAR.IDE_GEEDP,EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, " +
				"AREA.DETALLE_GEARE,AREA.IDE_GEARE  " +
				//"DEPA.DETALLE_GEDEP " +
				"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
				"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
				"LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " +
				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE "
				+ "WHERE EPAR.IDE_GEEDP IN ("+tab_tabla1.getValor("IDE_GEEDP")+")");
		
		tab_tabla1.setValor("IDE_GTEMP",tab_empleado_.getValor("IDE_GTEMP"));
		tab_tabla1.setValor("ide_geare",tab_empleado_.getValor("IDE_GEARE"));
		utilitario.addUpdateTabla(tab_tabla1,"IDE_GTEMP,IDE_GEARE", "");

		//utilitario.addUpdate("IDE_GTEMP");
		// tab_activos_fijos.getColumna("ide_cocac").setAutoCompletar();
	}
	
	
	public void getEmpleadoAsignado() {
		if (tab_tabla2.getValor("IDE_GTEMP") == null) {
			utilitario.agregarMensajeInfo("Debe Seleccionar un empleado", "No se puede continuar");
			return;
		}
		
		StringBuilder str = new StringBuilder();
		TablaGenerica  tab_empleado_asignado=utilitario.consultar("SELECT ide_emjei, ide_asjei, ide_gtemp, activo_emjei "
				+ "FROM asi_empleado_jefe_inmediato  "
				+ "where ide_gtemp="+tab_tabla2.getValor("IDE_GTEMP"));
		
		if (tab_empleado_asignado.getTotalFilas()>0) {
			tab_tabla2.setValor("IDE_GTEMP", "");
			utilitario.addUpdateTabla(tab_tabla2,"IDE_GTEMP", "");
			utilitario.agregarMensajeInfo("El empleado seleccionado ya se encuentra registrado", "Seleccione un nuevo empleado");
			return;
		}
					
	}

}
