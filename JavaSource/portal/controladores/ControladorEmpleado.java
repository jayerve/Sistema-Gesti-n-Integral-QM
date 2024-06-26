/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.controladores;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.UploadedFile;
import framework.aplicacion.TablaGenerica;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Utilitario;
import persistencia.Conexion;
import portal.servicios.ServicioEmpleadoJPA;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.primefaces.model.UploadedFile;


import portal.entidades.GthCargasFamiliares;
import portal.entidades.GthCargo;
import portal.entidades.GthConyuge;
import portal.entidades.GthCorreo;
import portal.entidades.GthCuentaBancariaEmpleado;
import portal.entidades.GthDireccion;
import portal.entidades.GthEmpleado;
import portal.entidades.GthGenero;
import portal.entidades.GthNacionalidad;
import portal.entidades.GthTelefono;
import portal.entidades.GthTipoDocumentoIdentidad;
import portal.entidades.GthTipoParentescoRelacion;
import portal.entidades.GthTipoTelefono;
import portal.entidades.GthUnionLibre;
import portal.entidades.SriDeducibles;
import portal.entidades.SriDeduciblesEmpleado;
import portal.entidades.SriFormulario107;
import portal.entidades.SriImpuestoRenta;
import portal.servicios.ServicioEmpleadoJPA;
import paq_beans.pre_portal;

/**
 *
 * @author DELL-USER
 */
@ManagedBean
@ViewScoped
public class ControladorEmpleado implements Serializable {

    private String strOpcion = "1";
    private Utilitario utilitario = new Utilitario();
    private String strCasado = "1";  //poner variable
    private String strUnionLibre = "2";//poner variable
    private String strSoltero = "0";//poner variable
    private GthEmpleado empleado;
    private GthDireccion direccion;
    private SriImpuestoRenta impuesto_renta;


    
    private SriDeduciblesEmpleado deducibles;
    @EJB
    private ServicioEmpleadoJPA servicioEmpleado;
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);

    //Direccion
    private List listaPaises;
    private List listaProvincias;
    private List listaCiudades;
    private List listaParroquias;
    private String strPais = "0"; //Ecuador por defecto
    private String strProvincia;
    private String strCiudad;
    private String strParroquia;
    ////Telefonos
    private List listaTiposTelefono;
    private List<GthTelefono> listaTelefonos; //lista de telefonos del usuario
    private GthTelefono telefonoNuevo;
    private String telefonoEliminado;
    //Correos
    private GthCorreo correoPersonal;
    private GthCorreo correoInstitucional;
    private SriFormulario107  formulario107;
    private SriFormulario107 sri;
    //Cargas Familiares
    private List<GthCargasFamiliares> listaCargasFamiliares;
    private List listaTipoParentesco;
    private List listaTipoDocumento;
    private List listaGenero;
    private GthCargasFamiliares cargaNueva;
    private String cargaEliminada;
    //Conyugue
    private List listaEstadoCivil;
    private GthConyuge conyugue;
    private GthTelefono conyugueTelefono;
    private GthUnionLibre conyugueUnionLibre;
    private List listaActividadLaboral;
    private List listaNacionalidad;
    private List listaCargos;
    private List listCargasFamiliares;
    private List listEnfermedadCatastrofica;

	
	
    //GASTOS DEDUCIBLES
    private List listaDeducibles;
    private List<SriDeduciblesEmpleado> listaGastosDeducibles; //lista de telefonos del usuario
    private SriDeduciblesEmpleado deduciblesNuevo;
    private String gastoEliminado;
//REPORTE DE GASTOS DEDUCIBLES
    private String strPathReporte;
    private JasperPrint jasperPrint;
    private Map parametros = new HashMap();
	private Connection conn;

 //TOTALES GASTO DEDUCIBLE
	
	private String valorMaxGastosDeducible="";
	private double valoresTablaGastosDeducible=0.0;
	double  valorGastosDeducible=0.00;
 //MODAL ACEPTAR GASTOS
	private boolean booIngresoGasto= false;
	private boolean booIngresoGastoValidacion= false;
	private boolean booIngresoGastoValidacionReporte= false;
	private boolean booIngresoGastos107= false;
	private boolean booIngresoNuevaGastoValidacion= false;
	private int anio=0;
	//Adjunto
	private UploadedFile adjunto;
private String dialogo="";
Double dou_tot_ing_gravados=0.00;
double canastaBasicaFamiliar= Double.parseDouble(utilitario.getVariable("p_canasta_familiar"));
private boolean estadoCarga=false,estadoEnfermedad=false;
private Integer numCarga=0;
private Boolean enfermedad=false;
    @PostConstruct
    public void cargarDatos() {
    	anio=utilitario.getAnio(utilitario.getFechaActual());
        empleado = servicioEmpleado.getEmpleado(utilitario.getVariable("IDE_GTEMP"));
        impuesto_renta = servicioEmpleado.getImpuesto(""+utilitario.getAnio(utilitario.getFechaActual()));

    	//System.out.println( "empleado IDE_GTEMP: "+ utilitario.getVariable("IDE_GTEMP")+"");
        //Direccion
        direccion = servicioEmpleado.getDireccionEmpleado(empleado.getIdeGtemp().toString());
        if (direccion == null) {
            direccion = new GthDireccion();
            direccion.setIdeGtemp(empleado);
        } else {
            try {
            	/* if (direccion.getIdeGedip().getIdeGedip()==0 || direccion.getIdeGedip().getGenIdeGedip().getGenIdeGedip().getGenIdeGedip().getGenIdeGedip().getIdeGedip().toString().isEmpty()) {
        
                 	strParroquia = direccion.getIdeGedip().getIdeGedip() + "";
                     strCiudad = direccion.getIdeGedip().getGenIdeGedip().getIdeGedip() + "";
                     strProvincia = direccion.getIdeGedip().getGenIdeGedip().getGenIdeGedip().getGenIdeGedip().getIdeGedip() + "";    	
                     strPais="0";
         		}else {*/
                strCiudad = direccion.getIdeGedip().getGenIdeGedip().getIdeGedip() + "";
            	listaParroquias = servicioEmpleado.getParroquias(strCiudad);
				if (listaParroquias.size()!=0) {
                strParroquia = direccion.getIdeGedip().getIdeGedip() + "";
                strCiudad = direccion.getIdeGedip().getGenIdeGedip().getIdeGedip() + "";
                strProvincia = direccion.getIdeGedip().getGenIdeGedip().getGenIdeGedip().getGenIdeGedip().getIdeGedip() + "";
	                strPais=direccion.getIdeGedip().getGenIdeGedip().getGenIdeGedip().getGenIdeGedip().getGenIdeGedip().getIdeGedip()+"";
				}else {				
            	strParroquia = direccion.getIdeGedip().getIdeGedip() + "";
                strCiudad = direccion.getIdeGedip().getIdeGedip() + "";
                strProvincia = direccion.getIdeGedip().getGenIdeGedip().getGenIdeGedip().getIdeGedip() + "";
                strPais=direccion.getIdeGedip().getGenIdeGedip().getGenIdeGedip().getGenIdeGedip().getIdeGedip()+"";
                
    			}
               
            } catch (Exception e) {
            }

        }
        //strPais= getDivisionPolitica(direccion.getIdeGedip().getIdeGedip().toString(),1);
     
        
        //   strProvincia= getDivisionPolitica(direccion.getIdeGedip().getIdeGedip().toString(),2);
       // strCiudad= getDivisionPolitica(direccion.getIdeGedip().getIdeGedip().toString(),3);
        //    strProvincia= utilitario.consultar("select ide_ from gen_division_politica where ide_getdp in(10)");
    //    strCiudad= utilitario.consultar("select ide_ from gen_division_politica where ide_getdp in(10)");
        
      
        
        listaPaises = servicioEmpleado.getPaises();
        listaProvincias = servicioEmpleado.getProvincias(strPais);
        listaCiudades = servicioEmpleado.getCiudades(strProvincia);
        listaParroquias = servicioEmpleado.getParroquias(strCiudad);

        //telefonos
        listaTiposTelefono = servicioEmpleado.getTiposTelefono();
        listaTelefonos = servicioEmpleado.getTelefonos(empleado.getIdeGtemp().toString());
        if (listaTelefonos == null) {
            listaTelefonos = new ArrayList<GthTelefono>();
        }

        telefonoNuevo = new GthTelefono();
        telefonoNuevo.setIdeGtemp(empleado);
        telefonoNuevo.setIdeGttit(new GthTipoTelefono());

        //Correos
        correoInstitucional = servicioEmpleado.getCorreoInstitucional(empleado.getIdeGtemp().toString());
        if (correoInstitucional == null) {
            correoInstitucional = new GthCorreo();
            correoInstitucional.setNotificacionGtcor(new Boolean(true));
            correoInstitucional.setIdeGtemp(empleado);
            correoInstitucional.setActivoGtcor(new Boolean(true));
        }
        correoPersonal = servicioEmpleado.getCorreoPersonal(empleado.getIdeGtemp().toString());
        if (correoPersonal == null) {
            correoPersonal = new GthCorreo();
            correoPersonal.setNotificacionGtcor(new Boolean(false));
            correoPersonal.setIdeGtemp(empleado);
            correoPersonal.setActivoGtcor(new Boolean(true));
        }

    
        
        sri = servicioEmpleado.getCampoFormulario(empleado.getIdeGtemp().toString());
        if (sri == null) {
            sri = new SriFormulario107();
            sri.setR307Srfor(new BigDecimal(0));
            sri.setIdeGtemp(empleado);
            sri.setActivoSrfor(new Boolean(true));
            sri.setFechaIngre(utilitario.DeStringADate(utilitario.getFechaActual()));
            sri.setFechaRegistroSrfor(utilitario.DeStringADate(utilitario.getFechaActual()));
            sri.setFechaActua(utilitario.DeStringADate(utilitario.getFechaActual()));
            sri.setR353Srfor(new BigDecimal(0));
            sri.setTipoFormularioSrfor(2);          
            sri.setUsuarioActua(utilitario.getVariable("IDE_USUA"));
            sri.setUsuarioIngre(utilitario.getVariable("IDE_USUA"));
            sri.setIdeSrimr(impuesto_renta);
        }   
        
        
        //Deducibles
        listaDeducibles = servicioEmpleado.getTiposDeducibles();
        listaGastosDeducibles = servicioEmpleado.getDeducibles(empleado.getIdeGtemp().toString());
        if (listaDeducibles == null) {
            listaDeducibles = new ArrayList<SriDeduciblesEmpleado>();
        }

        deduciblesNuevo = new SriDeduciblesEmpleado();
        deduciblesNuevo.setIdeGtemp(empleado);
        deduciblesNuevo.setIdeSrded(new SriDeducibles());

        //Cargas familiares
        listaCargasFamiliares = servicioEmpleado.getCargasFamiliares(empleado.getIdeGtemp().toString());
        if (listaCargasFamiliares == null) {
            listaCargasFamiliares = new ArrayList<GthCargasFamiliares>();
        }
        listaTipoDocumento = servicioEmpleado.getTiposDocumentoIdentidad();
        listaTipoParentesco = servicioEmpleado.getTiposParentesco();
        listaGenero = servicioEmpleado.getGeneros();
        cargaNueva = new GthCargasFamiliares();
        cargaNueva.setIdeGtemp(empleado);
        cargaNueva.setIdeGtgen(new GthGenero());
        cargaNueva.setIdeGttpr(new GthTipoParentescoRelacion());
        cargaNueva.setIdeGttdi(new GthTipoDocumentoIdentidad());

        //conyugue
        listaEstadoCivil = servicioEmpleado.getEstadosCiviles();
        listaActividadLaboral = servicioEmpleado.getActividadesLaborales();
        listaNacionalidad = servicioEmpleado.getNacionalidades();
        listaCargos = servicioEmpleado.getCargos();

        conyugue = servicioEmpleado.getConyuque(empleado.getIdeGtemp().toString());
        if (conyugue == null) {
            conyugue = new GthConyuge();
        }
        conyugue.setIdeGtcar(conyugue.getIdeGtcar() == null ? new GthCargo() : conyugue.getIdeGtcar());
        conyugue.setIdeGtgen(conyugue.getIdeGtgen() == null ? new GthGenero() : conyugue.getIdeGtgen());
        conyugue.setIdeGtnac(conyugue.getIdeGtnac() == null ? new GthNacionalidad() : conyugue.getIdeGtnac());
        conyugue.setIdeGttdi(conyugue.getIdeGttdi() == null ? new GthTipoDocumentoIdentidad() : conyugue.getIdeGttdi());
        conyugue.setIdeGtemp(empleado);
        if (conyugue.getIdeGtcon() != null) {
            conyugueTelefono = servicioEmpleado.getTelefonoConyugue(conyugue.getIdeGtcon().toString());
        }
        if (conyugueTelefono == null) {
            conyugueTelefono = new GthTelefono();
            conyugueTelefono.setIdeGtcon(conyugue);
            conyugueTelefono.setIdeGttit(new GthTipoTelefono());
        }
        conyugueTelefono.setIdeGtcon(conyugue);
        if (conyugue.getIdeGtcon() != null) {
            conyugueUnionLibre = servicioEmpleado.getUnionLibre(conyugue.getIdeGtcon().toString());
        }
        if (conyugueUnionLibre == null) {
            conyugueUnionLibre = new GthUnionLibre();
        }
        conyugueUnionLibre.setIdeGtcon(conyugue);
		strPathReporte = utilitario.getURL() + "/reportes/reporte_gastos_deducibles_" + utilitario.getVariable("IDE_USUA") + ".pdf";
		valorMaxGastosDeducible=getvalorMaximoGastoDeducible();
		valoresTablaGastosDeducible=getvalorTablaGastoDeducible();
		estadoGastoDeducibleValidacion();
		estadoRubrosSriGatosValidacion();
		
		
		
		
	       TablaGenerica tabEmpleado=utilitario.consultar("SELECT EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
	          		+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
	          		+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||   "
	          		+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
	          		+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,EMP.num_cargas_gtemp,"
	          		+ "CASE "
	          		+ "WHEN EMP.enfermedad_catastrofica_gtemp is null THEN 'NO' "
	          		+ "WHEN EMP.enfermedad_catastrofica_gtemp=false THEN 'NO' "
	          		+ "WHEN EMP.enfermedad_catastrofica_gtemp=true THEN 'SI' "
	          		+ "END as enfermedad_catastrofica_gtemp "
	          		//+ "CASE WHEN EMP.enfermedad_catastrofica_gtemp=false THEN 'NO' ELSE 'SI' END as enfermedad_catastrofica_gtemp "
	          		+ "from gth_empleado  EMP "
	          		+ "WHERE EMP.IDE_GTEMP="+utilitario.getVariable("IDE_GTEMP"));
		
	       
	       
	     
	       
	       
	   	String carga="",enfermedad="";
		
		carga=tabEmpleado.getValor("num_cargas_gtemp");
		enfermedad=tabEmpleado.getValor("enfermedad_catastrofica_gtemp");
		estadoCarga=false;estadoEnfermedad=false;
	
		if (tabEmpleado.getValor("enfermedad_catastrofica_gtemp")==null || tabEmpleado.getValor("enfermedad_catastrofica_gtemp").equals("") || tabEmpleado.getValor("enfermedad_catastrofica_gtemp").isEmpty()) {
			estadoEnfermedad=false;
		}else {
			estadoEnfermedad=true;
		}
		
		
		if (tabEmpleado.getValor("num_cargas_gtemp")==null || tabEmpleado.getValor("num_cargas_gtemp").equals("") || tabEmpleado.getValor("num_cargas_gtemp").isEmpty()) {
			estadoCarga=false;
		}else {
			estadoCarga=true;
		}
		
    	listCargasFamiliares = new ArrayList();
    	if (booIngresoGastoValidacion==true) {
    		
    		if (estadoCarga==true) {
    			Object fila1[] = {
    	    			"0", tabEmpleado.getValor("num_cargas_gtemp")
    	    	};
    	    	listCargasFamiliares.add(fila1);

			}else{
    		Object fila1[] = {
	    			"0", "SIN DATOS"
	    	};
        	listCargasFamiliares.add(fila1);

			}
    		
    	}else{
    	Object fila1[] = {
    			"0", "0"
    	};
    	Object fila2[] = {
    			"1", "1"
    	};		
    	Object fila3[] = {
    			"2", "2"
    	};
    	
    	Object fila4[] = {
    			"3", "3"
    			};
    	
    	Object fila5[] = {
    			"4", "4"
    	};
    	
    	Object fila6[] = {
    			"5", "5"
    	};
    	
    	listCargasFamiliares.add(fila1);
    	listCargasFamiliares.add(fila2);
    	listCargasFamiliares.add(fila3);
    	listCargasFamiliares.add(fila4); 
    	listCargasFamiliares.add(fila5);
    	listCargasFamiliares.add(fila6);
    	}
    	    	
    	listEnfermedadCatastrofica = new ArrayList();
    	if (booIngresoGastoValidacion==true) {
    		
    		if (estadoEnfermedad==true) {
    			Object fila8[] = {
    	    			"false", tabEmpleado.getValor("enfermedad_catastrofica_gtemp")
    	    	};
        	 	listEnfermedadCatastrofica.add(fila8); 

    		}else {
    			Object fila8[] = {
    	    			"false", "SIN DATOS"
    	    	};
        	 	listEnfermedadCatastrofica.add(fila8); 

			}
    	
        //	listEnfermedadCatastrofica.add(fila9);
		}else {
	    	Object fila8[] = {
	    			"true", "SI"
	    	};
	    	Object fila9[] = {
	    			"false", "NO"
	    	};		
	    	
	     	listEnfermedadCatastrofica.add(fila8); 
	    	listEnfermedadCatastrofica.add(fila9);
		}
		
		
    }

    public void actualizarEmpleado() {
        cargarDatos();
    }

    public void guardarConyugueUnionLibre(ActionEvent evt) {
          //Valido identificación del conyuque
   
        
        if (empleado.getIdeGtesc() != null) {
            TablaGenerica usuario=utilitario.consultar("select ide_usua,nom_usua,nick_usua from sis_usuario where ide_usua="+utilitario.getVariable("IDE_USUA"));
            empleado.setIdeGtesc(servicioEmpleado.getEstadoCivil(empleado.getIdeGtesc().getIdeGtesc().toString()));
        	empleado.setUsuarioActua(usuario.getValor("NICK_USUA"));
        	empleado.setFechaActua(utilitario.DeStringADate(utilitario.getFechaActual()));
        	empleado.setHoraActua(utilitario.getHora(utilitario.getHoraActual()));
            if (empleado.getIdeGtesc().getIdeGtesc().toString().contains(strUnionLibre)) {
                if (validarDocumentoIdentidad(conyugue.getIdeGttdi().getIdeGttdi().toString(), conyugue.getDocumentoIdentidadGtcon()) == false) {
                    return;
                }
                conyugue.setIdeGttdi(servicioEmpleado.getTipoDocumentoIdentidad(conyugue.getIdeGttdi().getIdeGttdi().toString()));
                conyugue.setIdeGtgen(servicioEmpleado.getGenero(conyugue.getIdeGtgen().getIdeGtgen().toString()));
                conyugue.setIdeGtcar(conyugue.getIdeGtcar().getIdeGtcar() == null ? null : servicioEmpleado.getCargo(conyugue.getIdeGtcar().getIdeGtcar().toString()));
                conyugue.setIdeGtnac(conyugue.getIdeGtnac().getIdeGtnac() == null ? null : servicioEmpleado.getNacionalidad(conyugue.getIdeGtnac().getIdeGtnac().toString()));
                conyugue.setIdeGtnac(conyugue.getIdeGtnac().getIdeGtnac() == null ? null : servicioEmpleado.getNacionalidad(conyugue.getIdeGtnac().getIdeGtnac().toString()));
                conyugue.setActivoGtcon(true);
                conyugueTelefono.setIdeGttit(servicioEmpleado.getTipoTelefono(conyugueTelefono.getIdeGttit().getIdeGttit().toString()));
                String str_mensaje = servicioEmpleado.guardarConyugueUnionLibre(conyugue, conyugueTelefono, conyugueUnionLibre);
                if (str_mensaje.isEmpty()) {
                    utilitario.agregarMensaje("Se guardo correctamente", "");
                    servicioEmpleado.guardarEmpleado(empleado);
                } else {
                    utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
                }
            }
        }
    }

    /**
     * @param ide_gttdi
     * @param documento_identidad_gttdi
     * @return
     *
     * metodo booleano para validar el tipo de documento de identidad cedula y
     * ruc
     */
    private boolean validarDocumentoIdentidad(String ide_gttdi, String documento_identidad) {
        if (ide_gttdi != null && !ide_gttdi.isEmpty()) {
            if (documento_identidad != null && !documento_identidad.isEmpty()) {
                if (ide_gttdi.equals(utilitario.getVariable("p_gth_tipo_documento_cedula"))) {
                    if (!utilitario.validarCedula(documento_identidad)) {
                        utilitario.agregarMensajeInfo("Atencion", "El número de cedula ingresado no es valido");
                        return false;
                    }
                } else if (ide_gttdi.equals(utilitario.getVariable("p_gth_tipo_documento_ruc"))) {
                    if (!utilitario.validarRUC(documento_identidad)) {
                        utilitario.agregarMensajeInfo("Atencion", "El número de RUC ingresado no es valido");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void guardarConyugue(ActionEvent evt) {
    	
    	
    	
        //Valido identificación del conyuque


        if (empleado.getIdeGtesc() != null) {
            empleado.setIdeGtesc(servicioEmpleado.getEstadoCivil(empleado.getIdeGtesc().getIdeGtesc().toString()));
            TablaGenerica usuario=utilitario.consultar("select ide_usua,nom_usua,nick_usua from sis_usuario where ide_usua="+utilitario.getVariable("IDE_USUA"));
        	empleado.setUsuarioActua(usuario.getValor("NICK_USUA"));
        	empleado.setFechaActua(utilitario.DeStringADate(utilitario.getFechaActual()));
        	empleado.setHoraActua(utilitario.getHora(utilitario.getHoraActual()));
            
            if (empleado.getIdeGtesc().getIdeGtesc().toString().contains(strSoltero)) {
                //Soltero
                String str_mensaje = servicioEmpleado.eliminarConyugue(empleado.getIdeGtemp().toString());
                if (str_mensaje.isEmpty()) {
                    empleado.setUsuarioActua(usuario.getValor("nick_usua"));
                    empleado.setFechaActua(utilitario.DeStringADate(utilitario.getFechaActual()));
                    servicioEmpleado.guardarEmpleado(empleado);
                } else {
                    utilitario.agregarMensajeInfo("No puede cambiar su estado civila a Soltero", str_mensaje);
                }
            } else if (empleado.getIdeGtesc().getIdeGtesc().toString().contains(strCasado)) {
                if (validarDocumentoIdentidad(conyugue.getIdeGttdi().getIdeGttdi().toString(), conyugue.getDocumentoIdentidadGtcon()) == false) {
                    return;
                }
                conyugue.setIdeGttdi(servicioEmpleado.getTipoDocumentoIdentidad(conyugue.getIdeGttdi().getIdeGttdi().toString()));
                conyugue.setIdeGtgen(servicioEmpleado.getGenero(conyugue.getIdeGtgen().getIdeGtgen().toString()));
                conyugue.setIdeGtcar(conyugue.getIdeGtcar().getIdeGtcar() == null ? null : servicioEmpleado.getCargo(conyugue.getIdeGtcar().getIdeGtcar().toString()));
                conyugue.setIdeGtnac(conyugue.getIdeGtnac().getIdeGtnac() == null ? null : servicioEmpleado.getNacionalidad(conyugue.getIdeGtnac().getIdeGtnac().toString()));
                conyugueTelefono.setIdeGttit(servicioEmpleado.getTipoTelefono(conyugueTelefono.getIdeGttit().getIdeGttit().toString()));
                conyugue.setActivoGtcon(true);
                String str_mensaje = servicioEmpleado.guardarConyugue(conyugue, conyugueTelefono);
                if (str_mensaje.isEmpty()) {
                    utilitario.agregarMensaje("Se guardo correctamente", "");
                    servicioEmpleado.guardarEmpleado(empleado);
                } else {
                    utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
                }
            } else {
            	
            	
                String str_mensaje = servicioEmpleado.guardarEmpleado(empleado);
                if (str_mensaje.isEmpty()) {
                    utilitario.agregarMensaje("Se guardo correctamente", "");
                } else {
                    utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
                }
            }
        }
    }

    public void guardarDireccion(ActionEvent evt) {
        direccion.setIdeGedip(servicioEmpleado.getDivisionPolitica(strParroquia));        //Asigna la parroquia
        direccion.setNotificacionGtdir(new Boolean(true));
        direccion.setActivoGtdir(true);
        ///direccion.setHoraIngre(utilitario.getHoraActual());
        String str_mensaje = servicioEmpleado.guardarDireccion(direccion);
        if (str_mensaje.isEmpty()) {
            utilitario.agregarMensaje("Se guardo Correctamente", "");
        } else {
            utilitario.agregarMensajeInfo("No se puede guardar", str_mensaje);
        }
    }

    
    public void guardarCargasEnfermedad() {
    	boolean valorInsertar=false;
    	
    	if (enfermedad==false) {
    	    empleado.setEnfermedadCatastroficaGtemp(false); 
    	    valorInsertar=false;
		}else {
    	    empleado.setEnfermedadCatastroficaGtemp(true); 
    	    valorInsertar=true;

		}
           
        //empleado.setNumCargasGtemp(empleado.getNumCargasGtemp());
    	if(numCarga==null || numCarga.equals(""))
    	{
    		utilitario.getConexion().ejecutarSql("update gth_empleado set   num_cargas_gtemp="+0+", enfermedad_catastrofica_gtemp=" + valorInsertar + " where ide_GTEMP=" + utilitario.getVariable("IDE_GTEMP"));

    	}else{
    		utilitario.getConexion().ejecutarSql("update gth_empleado set   num_cargas_gtemp="+numCarga+", enfermedad_catastrofica_gtemp=" + valorInsertar + " where ide_GTEMP=" + utilitario.getVariable("IDE_GTEMP"));

    	}

        
        
       // String str_mensaje = servicioEmpleado.guardarEmpleado(empleado);
       // if (str_mensaje.isEmpty()) {
            utilitario.agregarMensaje("Se guardo Correctamente", "");
        //} else {
         //   utilitario.agregarMensajeInfo("No se puede guardar", str_mensaje);
        //}
    }
    
    
    
    public void guardarCorreo(ActionEvent evt) {
        String str_mensaje = servicioEmpleado.guardarCorreo(correoInstitucional, correoPersonal);
        if (str_mensaje.isEmpty()) {
            utilitario.agregarMensaje("Se guardo Correctamente", "");
        } else {
            utilitario.agregarMensaje("No se pudo guardar", str_mensaje);
        }
    }

    public void guardarFormulario(ActionEvent evt) {
        String str_mensaje = servicioEmpleado.guardarFormulario(sri);
        if (str_mensaje.isEmpty()) {
            utilitario.agregarMensaje("Se guardo Correctamente", "");
            TablaGenerica tab_sri=utilitario.consultar("SELECT ide_srimr, detalle_srimr "
              		+ "FROM sri_impuesto_renta  "
              		+ "WHERE detalle_srimr like '%"+utilitario.getAnio(utilitario.getFechaActual())+"%'");
              
              
        } else {
            utilitario.agregarMensaje("No se pudo guardar", str_mensaje);
        }
    }  
    
    
    public void eliminarTelefono() {
        if (telefonoEliminado != null) {
            //Borro de la base
            String str_mensaje = servicioEmpleado.eliminarTelefono(telefonoEliminado);
            if (str_mensaje.isEmpty()) {
                listaTelefonos = servicioEmpleado.getTelefonos(empleado.getIdeGtemp().toString());
                telefonoEliminado = null;
            } else {
                utilitario.agregarMensajeError("No se puede eliminar el teléfono", str_mensaje);
            }
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar un teléfono ", "");
        }
    }

    
    public void eliminarGastosDeducibles() {
        if (gastoEliminado != null) {
            //Borro de la base
            String str_mensaje = servicioEmpleado.eliminarGasto(gastoEliminado);
            if (str_mensaje.isEmpty()) {
                listaGastosDeducibles = servicioEmpleado.getDeducibles(empleado.getIdeGtemp().toString());
                gastoEliminado = null;
                utilitario.agregarMensaje("Se ha eliminado el  el gasto correctamente",str_mensaje );
            } else {
                utilitario.agregarMensajeError("No se puede eliminar el gasto", str_mensaje);
            }
        } else {
            utilitario.agregarMensajeInfo("No se puede eliminar el gasto", "Debe seleccionar un gasto");
        }
    }
    
    public void refreshGastosDeducibles() {
        
    	valorMaxGastosDeducible=getvalorMaximoGastoDeducible();
		valoresTablaGastosDeducible=getvalorTablaGastoDeducible();
		estadoGastoDeducibleValidacion();
               
    }
    
    
    public void modificarTelefono(RowEditEvent evt) {
        GthTelefono telefonoSeleccionado = (GthTelefono) evt.getObject();
        telefonoSeleccionado.setIdeGttit(servicioEmpleado.getTipoTelefono(telefonoSeleccionado.getIdeGttit().getIdeGttit().toString()));// por si modifica en la tabla                                 
        String str_mensaje = servicioEmpleado.guardarTelefono(telefonoSeleccionado);
        if (!str_mensaje.isEmpty()) {
            utilitario.agregarMensajeError("No se puede modificar el teléfono", str_mensaje);
        }
    }

    
    public void modificarGastoDeducible(RowEditEvent evt) {
        SriDeduciblesEmpleado gastoSeleccionado = (SriDeduciblesEmpleado) evt.getObject();
        //SriDeduciblesEmpleado nuevo=new SriDeduciblesEmpleado();
       // nuevo.setIdeSrdee(gastoSeleccionado.getIdeSrdee().intValue());// por si modifica en la tabla     
     //   gastoSeleccionado.setIdeSrded(gastoSeleccionado.getIdeSrded().getIdeSrded().intValue());// por si modifica en la tabla                                 
       gastoSeleccionado.setIdeSrded(servicioEmpleado.getTipoGastoDeducible(gastoSeleccionado.getIdeSrded().getIdeSrded().toString()));// por si modifica en la tabla                                 
       gastoSeleccionado.setValorDeducibleSrdee(gastoSeleccionado.getValorDeducibleSrdee());// por si modifica en la tabla                                 
       gastoSeleccionado.setIdeGtemp(empleado);
       gastoSeleccionado.setActivoSrdee(true);
       TablaGenerica tab_usuario=utilitario.consultar("select * from sis_usuario where ide_gtemp="+utilitario.getVariable("IDE_GTEMP"));
       gastoSeleccionado.setActivoSrdee(true);
       gastoSeleccionado.setUsuarioActua(tab_usuario.getValor("NICK_USUA"));
       gastoSeleccionado.setFechaActua(utilitario.DeStringADate(utilitario.getFechaActual()));
       gastoSeleccionado.setHoraActua((utilitario.DeStringAHora(utilitario.getHoraActual())));
    
       int validaGasto=validaGastoRepetido(servicioEmpleado.getTipoGastoDeducible(gastoSeleccionado.getIdeSrded().getIdeSrded().toString()).getIdeSrded().toString());
       if (validaGasto>0) {
    	   
    	   TablaGenerica tabGastoRepetido=utilitario.consultar("SELECT ide_srdee, ide_srded, ide_gtemp, "
              		+ "valor_deducible_srdee, observacion_srdee,  "
              		+ "activo_srdee "
              		+ "FROM sri_deducibles_empleado  "
              		+ "where ide_gtemp="+utilitario.getVariable("IDE_GTEMP")+""
              		+ "and ide_srded in(select ide_srded  "
              		+ "FROM sri_deducibles  "
              		+ "WHERE IDE_SRIMR IN(select IDE_SRIMR from sri_impuesto_renta  "
              		+ "where detalle_srimr like '%"+utilitario.getAnio(utilitario.getFechaActual())+"%') "
              		+ "and ide_srded="+servicioEmpleado.getTipoGastoDeducible(gastoSeleccionado.getIdeSrded().getIdeSrded().toString()).getIdeSrded().toString()+" "
              		+ "order by ide_srded)");
           
    	   
    	   if (gastoSeleccionado.getIdeSrdee().toString().equals(tabGastoRepetido.getValor("IDE_SRDEE"))) {
    		}else {
    			utilitario.agregarMensaje("No se puede agregar", "Este gasto ya ha sido ingresado anteriormente");
    			cargarDatos();		
			}
  	}

       
       
       double valor_srdee=gastoSeleccionado.getValorDeducibleSrdee().doubleValue();
       boolean bandValidacion=validarGastosPersonalesDeducibles(valor_srdee,servicioEmpleado.getTipoGastoDeducible(gastoSeleccionado.getIdeSrded().getIdeSrded().toString()).getIdeSrded(),1);
       String str_mensaje="";
       if (bandValidacion==true) {
       	str_mensaje = servicioEmpleado.guardarGastosDeducibles(gastoSeleccionado);	
		}else {
			cargarDatos();
		}
       
        
        if (!str_mensaje.isEmpty()) {
            utilitario.agregarMensajeError("No se puede modificar el gasto", str_mensaje);
            cargarDatos();
        }else {
        	//listaGastosDeducibles = servicioEmpleado.getDeducibles(empleado.getIdeGtemp().toString());
        	//cargarDatos();
        	utilitario.agregarMensaje("Se realizó modificación de gasto con éxito", str_mensaje);
		}
    }

    public void modificarCarga(RowEditEvent evt) {
        GthCargasFamiliares cargaSeleccionada = (GthCargasFamiliares) evt.getObject();
        cargaSeleccionada.setIdeGtgen(servicioEmpleado.getGenero(cargaSeleccionada.getIdeGtgen().getIdeGtgen().toString()));
        cargaSeleccionada.setIdeGttpr(servicioEmpleado.getTipoParentesco(cargaSeleccionada.getIdeGttpr().getIdeGttpr().toString()));
        cargaSeleccionada.setIdeGttdi(servicioEmpleado.getTipoDocumentoIdentidad(cargaSeleccionada.getIdeGttdi().getIdeGttdi().toString()));

        String str_mensaje = servicioEmpleado.guardarCargaFamiliar(cargaSeleccionada);
        if (!str_mensaje.isEmpty()) {
            utilitario.agregarMensajeError("No se puede modificar el teléfono", str_mensaje);
        }
    }

    public void agregarCargaFamiliar(ActionEvent evt) {
          //Valido identificación del conyuque
        if (validarDocumentoIdentidad(cargaNueva.getIdeGttdi().getIdeGttdi().toString(),cargaNueva.getDocumentoIdentidadGtcaf()) == false) {
            return;
        }
        
        cargaNueva.setIdeGtgen(servicioEmpleado.getGenero(cargaNueva.getIdeGtgen().getIdeGtgen().toString()));
        cargaNueva.setIdeGttpr(servicioEmpleado.getTipoParentesco(cargaNueva.getIdeGttpr().getIdeGttpr().toString()));
        cargaNueva.setIdeGttdi(servicioEmpleado.getTipoDocumentoIdentidad(cargaNueva.getIdeGttdi().getIdeGttdi().toString()));
        cargaNueva.setActivoGtcaf(new Boolean(true));
        String str_mensaje = servicioEmpleado.guardarCargaFamiliar(cargaNueva);
        if (str_mensaje.isEmpty()) {
            listaCargasFamiliares.add(cargaNueva);
            cargaNueva = new GthCargasFamiliares();
            cargaNueva.setIdeGtgen(new GthGenero());
            cargaNueva.setIdeGtemp(empleado);
            cargaNueva.setIdeGttpr(new GthTipoParentescoRelacion());
            cargaNueva.setIdeGttdi(new GthTipoDocumentoIdentidad());
        } else {
            utilitario.agregarMensaje("No se puede agregar", str_mensaje);
        }
    }

    public void eliminarCargaFamiliar() {
        if (cargaEliminada != null) {
            //Borro de la base
            String str_mensaje = servicioEmpleado.eliminarCargaFamiliar(cargaEliminada);
            if (str_mensaje.isEmpty()) {
                listaCargasFamiliares = servicioEmpleado.getCargasFamiliares(empleado.getIdeGtemp().toString());
                cargaEliminada = null;
            } else {
                utilitario.agregarMensajeError("No se puede eliminar la carga Familiar", str_mensaje);
            }
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una Carga Familiar ", "");
        }
    }

    public void agregarTelefono(ActionEvent evt) {
        telefonoNuevo.setIdeGttit(servicioEmpleado.getTipoTelefono(telefonoNuevo.getIdeGttit().getIdeGttit().toString()));
        telefonoNuevo.setActivoGttel(new Boolean(true));
        telefonoNuevo.setNotificacionGttel(false);
        String str_mensaje = servicioEmpleado.guardarTelefono(telefonoNuevo);
        if (str_mensaje.isEmpty()) {
            listaTelefonos.add(telefonoNuevo);
            telefonoNuevo = new GthTelefono();
            telefonoNuevo.setIdeGtemp(empleado);
            telefonoNuevo.setIdeGttit(new GthTipoTelefono());
        } else {
            utilitario.agregarMensaje("No se puede agregar", str_mensaje);
        }
    }

    
    public void agregarGastoDeducible(ActionEvent evt) {
        deduciblesNuevo.setIdeSrded(servicioEmpleado.getTipoGastoDeducible(deduciblesNuevo.getIdeSrded().getIdeSrded().toString()));
        deduciblesNuevo.setActivoSrdee(new Boolean(true));
        
        int validaGasto=validaGastoRepetido(servicioEmpleado.getTipoGastoDeducible(deduciblesNuevo.getIdeSrded().getIdeSrded().toString()).getIdeSrded().toString());
        if (validaGasto>0) {
            utilitario.agregarMensaje("No se puede agregar", "Este gasto ya ha sido ingresado anteriormente");
		return;	
		}
        
        double valor_srdee=deduciblesNuevo.getValorDeducibleSrdee().doubleValue();
        boolean bandValidacion=validarGastosPersonalesDeducibles(valor_srdee,servicioEmpleado.getTipoGastoDeducible(deduciblesNuevo.getIdeSrded().getIdeSrded().toString()).getIdeSrded(),0);
        String str_mensaje="";
        if (bandValidacion==true) {
        	str_mensaje = servicioEmpleado.guardarGastosDeducibles(deduciblesNuevo);	
		}else {
			return;
		} 
			
        //String str_mensaje = servicioEmpleado.guardarGastosDeducibles(deduciblesNuevo);
        if (str_mensaje.equals("") || str_mensaje==null) {
            listaDeducibles.add(deduciblesNuevo);
            deduciblesNuevo = new SriDeduciblesEmpleado();
            deduciblesNuevo.setIdeGtemp(empleado);
            deduciblesNuevo.setIdeSrded(new SriDeducibles());
            cargarDatos();
        } else {
            utilitario.agregarMensaje("No se puede agregar", str_mensaje);
        }
    }
    public void filtrarProvincias(AjaxBehaviorEvent evt) {
        if (strPais != null) {
            listaProvincias = servicioEmpleado.getProvincias(strPais);
        } else {
            listaProvincias = null;
        }
        listaCiudades = null;
        listaParroquias = null;
        strCiudad = null;
        strParroquia = null;
    }

    public void filtrarCiudades(AjaxBehaviorEvent evt) {
        if (strProvincia != null) {
            listaCiudades = servicioEmpleado.getCiudades(strProvincia);
        } else {
            listaCiudades = null;
        }
        listaParroquias = null;
        strParroquia = null;
    }

    public void filtrarParroquias(AjaxBehaviorEvent evt) {
        if (strCiudad != null) {
            listaParroquias = servicioEmpleado.getParroquias(strCiudad);
        } else {
            listaParroquias = null;
        }
    }

    public void subirFoto(FileUploadEvent event) {

        try {
        	adjunto=event.getFile();
        	String carpeta="/fotos/"+utilitario.getVariable("IDE_GTEMP")+"/";
            String str_nombre = utilitario.getVariable("NICK") + "_foto" + event.getFile().getFileName().substring(event.getFile().getFileName().lastIndexOf("."), event.getFile().getFileName().length());
            //String str_nombre = utilitario.getVariable("NICK") + utilitario.getFechaActual().replace("-", "") + utilitario.getHoraActual().replace(":", "") + adjunto.getFileName().substring(adjunto.getFileName().lastIndexOf("."), adjunto.getFileName().length());
			//String str_nombre = utilitario.getVariable("IDE_USUA") + utilitario.getFechaActual().replace("-", "") + utilitario.getHoraActual().replace(":", "") + adjunto.getFileName().substring(adjunto.getFileName().lastIndexOf("."), adjunto.getFileName().length());

            str_nombre = str_nombre.toLowerCase();
            String str_path = utilitario.getPropiedad("rutaUpload") + carpeta; 
            		//"/fotos";
            File path = new File(str_path);
            path.mkdirs();//Creo el Directorio
            File result = new File(str_path + "/" + str_nombre);
            ///Para el .war 

            ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
            //str_path = extContext.getRealPath("/upload/fotos");
            str_path = extContext.getRealPath("/upload"+carpeta);
            path = new File(str_path);
            path.mkdirs();//Creo el Directorio
            File result1 = new File(str_path + "/" + str_nombre);

            int BUFFER_SIZE = 6124;
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(result);
                byte[] buffer = new byte[BUFFER_SIZE];
                int bulk;
                InputStream inputStream = adjunto.getInputstream(); 
                //event.getFile().getInputstream();
                while (true) {
                    bulk = inputStream.read(buffer);
                    if (bulk < 0) {
                        break;
                    }
                    fileOutputStream.write(buffer, 0, bulk);
                    fileOutputStream.flush();
                }
                empleado.setPathFotoGtemp("/upload"+carpeta + str_nombre);
                fileOutputStream.close();
                inputStream.close();
            } catch (IOException e) {
                System.out.println("Error subirFoto2:" +e.getMessage());
            }

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(result1);
                byte[] buffer = new byte[BUFFER_SIZE];
                int bulk;
                InputStream inputStream = adjunto.getInputstream(); 
                //event.getFile().getInputstream();
                while (true) {
                    bulk = inputStream.read(buffer);
                    if (bulk < 0) {
                        break;
                    }
                    fileOutputStream.write(buffer, 0, bulk);
                    fileOutputStream.flush();
                }
                fileOutputStream.close();
                inputStream.close();

                //Guardo la foto si se subio correctamente  
                System.out.println("subirFoto 6");
                String str_msj = servicioEmpleado.guardarEmpleado(empleado);
                System.out.println("subirFoto 7");
                if (str_msj.isEmpty()) {
                    utilitario.agregarMensaje("Se Guardo Correctamente", "");
                } else {
                    utilitario.agregarMensajeError("No se pudo Guardar", str_msj);
                }
                //Recargar la pagina para que se cambie la foto
                FacesContext.getCurrentInstance().getExternalContext().redirect("datosEmpleado.jsf");
            } catch (IOException e) {
                System.out.println("Error subirFoto3:" +e.getMessage());
            }
        } catch (Exception ex) {
            System.out.println("Error subirFoto1:" +ex.getMessage());
        }
    }

    

   
   public void reportBuilder() throws JRException {
   	
   	   try {//cat2014pg
              Class.forName("org.postgresql.Driver");
              conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sampu","postgres","cat2014pg");
          } catch (SQLException ex) {
              //Logger.getLogger(IniciarReporte.class.getName()).log(Level.SEVERE, null, ex);
          } catch (ClassNotFoundException ex) {
             // Logger.getLogger(IniciarReporte.class.getName()).log(Level.SEVERE, null, ex);
          }
   	
   	//PARAMETROS DEL FORMULARIO
   	   double VIVIENDA=0.00,EDUCACION=0.00,SALUD=0.00,VESTIMENTA=0.00,ALIMENTACION=0.00,TURISMO=0.00;
   	
    //   strPathReporte = utilitario.getURL()+"/reportes/reporte_"+ utilitario.getVariable("IDE_USUA") + ".pdf";     
       
       TablaGenerica tabGastos = utilitario.consultar("select sum(suma) AS SUMA,ide_gtemp from (SELECT dedu.ide_gtemp, "
       		+ "sum(dedu.valor_deducible_srdee) suma "
       		+ "FROM sri_deducibles_empleado dedu "
       		+ "left join gth_empleado EMP on emp.ide_gtemp=dedu.ide_gtemp "
       		+ "where emp.ide_gtemp="+utilitario.getVariable("IDE_GTEMP")+" and dedu.ide_srded in(select ide_srded  "
       		+ "FROM sri_deducibles  "
       		+ "WHERE IDE_SRIMR IN(select IDE_SRIMR from sri_impuesto_renta "
       		+ "where detalle_srimr like '%"+utilitario.getAnio(utilitario.getFechaActual())+"%' order by ide_srimr desc limit 1))  "
       		+ "group by dedu.ide_gtemp,dedu.valor_deducible_srdee)a "
       		+ "group by ide_gtemp");
       BigDecimal valorGasto=null;
       if (tabGastos.getTotalFilas()>0) {
    	   valorGasto=new BigDecimal(tabGastos.getValor("SUMA"));
       	}else {
       	 valorGasto=new BigDecimal(0.00);
		}
       
     /*  TablaGenerica tabIngresos = utilitario.consultar("SELECT ping.ide_gtemp,pingd.valor_srdpi "
       		+ "FROM sri_proyeccion_ingres   ping  "
       		+ "left join sri_detalle_proyecccion_ingres  pingd   on pingd.ide_srpri= ping.ide_srpri "
       		+ "where ping.ide_srimr  in  (select ide_srimr  from sri_impuesto_renta  where detalle_srimr  like '%"+utilitario.getAnio(utilitario.getFechaActual())+"%') "
       		+ "and ping.ide_gtemp="+utilitario.getVariable("IDE_GTEMP"));
       */
       
       /*TablaGenerica tabDetalleIngresos = utilitario.consultar("SELECT ping.ide_gtemp,sum(pingd.valor_srdpi) as suma  "
       		+ "FROM sri_proyeccion_ingres   ping  "
       		+ "left join sri_detalle_proyecccion_ingres  pingd   on pingd.ide_srpri= ping.ide_srpri  "
       		+ "where ping.ide_srimr  in  (select ide_srimr  from sri_impuesto_renta  where detalle_srimr  like '%"+utilitario.getAnio(utilitario.getFechaActual())+"%') "
       		+ "and ping.ide_gtemp="+utilitario.getVariable("IDE_GTEMP")
       		+ " group by ping.ide_gtemp,pingd.valor_srdpi");
       

       BigDecimal valorIngresos=null;
       if (tabDetalleIngresos.getTotalFilas()>0) {
    	   valorIngresos=new BigDecimal(tabDetalleIngresos.getValor("suma"));
       	}else {
       		valorIngresos=new BigDecimal(0.00);
		}*/
       TablaGenerica tab_srimr=utilitario.consultar("select IDE_SRIMR,detalle_srimr from sri_impuesto_renta "
       		+ "where detalle_srimr like '%"+utilitario.getAnio(utilitario.getFechaActual())+"%' order by IDE_SRIMR desc limit 1");
       
		double dou_tot_ing_gravados=ser_nomina.getTotalIngresosGravados("17", utilitario.getVariable("IDE_GTEMP"));

       

       TablaGenerica  tabGastosIngresados=utilitario.consultar("SELECT dedu.ide_srdee, deducibles.detalle_srded, "
       		+ "dedu.valor_deducible_srdee, deducibles.observaciones_srded "
       		+ "FROM sri_deducibles_empleado dedu "
       		+ "left join gth_empleado EMP on emp.ide_gtemp=dedu.ide_gtemp "
       		+ "left join sri_deducibles deducibles on deducibles.ide_srded=dedu.ide_srded "
       		+ "where emp.ide_gtemp="+utilitario.getVariable("IDE_GTEMP")+" and dedu.ide_srded in(select ide_srded "
       		+ "FROM sri_deducibles "
       		+ "WHERE IDE_SRIMR IN(select IDE_SRIMR from sri_impuesto_renta  "
       		+ "where detalle_srimr like '%"+utilitario.getAnio(utilitario.getFechaActual())+"%' order by IDE_SRIMR desc limit 1)  "
       		+ ")order by deducibles.ide_srded asc");
       
       if (tabGastosIngresados.getTotalFilas()>0) {
		

       
       for (int i = 0; i < tabGastosIngresados.getTotalFilas(); i++) {
		if (tabGastosIngresados.getValor(i,"observaciones_srded").equals("106")) {
			 VIVIENDA=Double.parseDouble(tabGastosIngresados.getValor(i,"valor_deducible_srdee"));
		}
		if (tabGastosIngresados.getValor(i,"observaciones_srded").equals("107")) {
			  EDUCACION=Double.parseDouble(tabGastosIngresados.getValor(i,"valor_deducible_srdee"));
		}
		if (tabGastosIngresados.getValor(i,"observaciones_srded").equals("108")) {
			SALUD=Double.parseDouble(tabGastosIngresados.getValor(i,"valor_deducible_srdee"));
		}
		if (tabGastosIngresados.getValor(i,"observaciones_srded").equals("109")) {
			VESTIMENTA=Double.parseDouble(tabGastosIngresados.getValor(i,"valor_deducible_srdee"));

		}
		if (tabGastosIngresados.getValor(i,"observaciones_srded").equals("110")) {
			ALIMENTACION=Double.parseDouble(tabGastosIngresados.getValor(i,"valor_deducible_srdee"));
		}
		if (tabGastosIngresados.getValor(i,"observaciones_srded").equals("111")) {
			TURISMO=Double.parseDouble(tabGastosIngresados.getValor(i,"valor_deducible_srdee"));
		}
		
	}
   	}else {
   		VIVIENDA=0.00;
   		EDUCACION=0.00;
   		SALUD=0.00;
   		VESTIMENTA=0.00;
   		ALIMENTACION=0.00;
   		TURISMO=0.00;
	}

       
       TablaGenerica tabEmpleado=utilitario.consultar("SELECT EMP.DOCUMENTO_IDENTIDAD_GTEMP,  "
       		+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
       		+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||   "
       		+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||   "
       		+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,EMP.num_cargas_gtemp,EMP.enfermedad_catastrofica_gtemp "
       		+ "from gth_empleado  EMP "
       		+ "WHERE EMP.IDE_GTEMP="+utilitario.getVariable("IDE_GTEMP"));
       
       String documentoIdentificacion="";
       String apellidosNombres="";
       documentoIdentificacion=tabEmpleado.getValor("DOCUMENTO_IDENTIDAD_GTEMP");
       apellidosNombres=tabEmpleado.getValor("NOMBRES_APELLIDOS");
       TablaGenerica tabRuc = utilitario.consultar("select identificacion_empr,nom_empr from sis_empresa limit 1");
       String ruc="";
       ruc=tabRuc.getValor("identificacion_empr");
      
	
       
       int anio=0,mes=0,dia=0;
       
       anio= utilitario.getAnio(utilitario.getFechaActual());
       mes=utilitario.getMes(utilitario.getFechaActual());
       dia=utilitario.getDia(utilitario.getFechaActual());
       parametros.put("ide_gtemp", utilitario.getVariable("IDE_GTEMP"));
       parametros.put("anio",anio);
       parametros.put("mes",mes);
       parametros.put("dia",dia);
       parametros.put("total_gastos",valorGasto);
       parametros.put("total_ingresos",dou_tot_ing_gravados);
       parametros.put("total_ingresos_otros_empleador",0.0);
       
       
       //CALCULO IMPUESTO A LA RENTA PROYECTADA 
       double total_rebaja=0.00,descuento=0.00;
       //PARAMETROS UTILIXADOS PARA 2022
  	/*
	 double multiplicadorFraccionesGravadas = Double.parseDouble(utilitario.getVariable("p_num_veces_canasta_familiar"));
	 double valorMaxGastosDeducibles=canastaBasicaFamiliar*multiplicadorFraccionesGravadas;
		double dou_veces_fraccion_basica_desgravada=ser_nomina.getValorVecesFraccionBasicaDesgravada(tab_srimr.getValor("IDE_SRIMR"));
			double  descuento=0.00;
		
		if ( dou_tot_ing_gravados<dou_veces_fraccion_basica_desgravada) {
			
			if (valorGasto.doubleValue()<valorMaxGastosDeducibles) {
				descuento=valorGasto.doubleValue()*0.20;

			}else {
				descuento=valorMaxGastosDeducibles*0.20;
			}
	
		}else {
			
			if (valorGasto.doubleValue()<valorMaxGastosDeducibles) {
				descuento=valorGasto.doubleValue()*0.10;

			}else {
				descuento=valorMaxGastosDeducibles*0.10;
			}	
		}*/
           
		int numCarga_ = Integer.parseInt(tabEmpleado.getValor("num_cargas_gtemp"));
		boolean enfermedad_ = Boolean.parseBoolean(tabEmpleado.getValor("enfermedad_catastrofica_gtemp"));

		if (enfermedad_==true) {
			if (valorGastosDeducible<valorGasto.doubleValue()) {
				descuento=Double.parseDouble(utilitario.getFormatoNumero((valorGasto.doubleValue()*0.18),2));
			}else {
				descuento=2753.00;

			}
			
				}else {
					if (numCarga_==1) {
						if (valorGastosDeducible>valorGasto.doubleValue()) {
							descuento=Double.parseDouble(utilitario.getFormatoNumero((valorGasto.doubleValue()*0.18),2));
						}else {
							descuento=1239.00;

						}
					}else if (numCarga_==2) {
						if (valorGastosDeducible>valorGasto.doubleValue()) {
							descuento=Double.parseDouble(utilitario.getFormatoNumero((valorGasto.doubleValue()*0.18),2));
						}else {
							descuento=1514.00;

						}
						

					}else if (numCarga_==3) {
						if (valorGastosDeducible>valorGasto.doubleValue()) {
							descuento=Double.parseDouble(utilitario.getFormatoNumero((valorGasto.doubleValue()*0.18),2));
						}else {
							descuento=1927.00;

		}
		
       
					}else if (numCarga_==4) {
						if (valorGastosDeducible>valorGasto.doubleValue()) {
							descuento=Double.parseDouble(utilitario.getFormatoNumero((valorGasto.doubleValue()*0.18),2));
						}else {
							descuento=2340.00;

						}
						

					}else if (numCarga_==5) {
						if (valorGastosDeducible>valorGasto.doubleValue()) {
							descuento=Double.parseDouble(utilitario.getFormatoNumero((valorGasto.doubleValue()*0.18),2));
						}else {
							descuento=2753.00;

						}
					

					}else {
						if (valorGastosDeducible>valorGasto.doubleValue()) {
							descuento=Double.parseDouble(utilitario.getFormatoNumero((valorGasto.doubleValue()*0.18),2));
						}else {
							descuento=964.00;

						}
					

					}

				}
	
		
		
       
		
       
       parametros.put("total_rebaja",descuento);
       parametros.put("ruc",ruc);
       
       parametros.put("documentoIdentificacion",documentoIdentificacion);
       parametros.put("apellidosNombres",apellidosNombres);
       ///Parametros deducibles
       
       parametros.put("VIVIENDA",VIVIENDA);
       parametros.put("EDUCACION",EDUCACION);
       parametros.put("SALUD",SALUD);
       parametros.put("VESTIMENTA",VESTIMENTA);
       parametros.put("ALIMENTACION",ALIMENTACION);
       parametros.put("TURISMO",TURISMO);
   
       parametros.put("firma_contador",utilitario.getVariable("p_nombre_contador"));
	    parametros.put("SUBREPORT_DIR", getURL());
       parametros.put("REPORT_CONNECTION", conn);
       String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/rep_sri/rep_formulario_gastos_deducibles.jasper");
		System.out.println("report  "+report);
		System.out.println("getURL()  "+getURL());
		System.out.println("conn  "+conn.toString());
		System.out.println("parametros  "+parametros.toString());
       jasperPrint = JasperFillManager.fillReport(report, parametros,conn);
	

   }

    
   public void visualizarPermisos() throws JRException,IOException {
		 try
	    {
		//if (getvalorTablaGastoDeducible()<valorGastosDeducible) {
			   
			if(estadoCarga==false){
				   guardarCargasEnfermedad();
				   //estadoGastoDeducibleValidacion();
			}

		   
		
			   
			   
			 
			if (booIngresoGasto==false) {
				 FacesContext fc = FacesContext.getCurrentInstance();
				 	ExternalContext ec = fc.getExternalContext();        
				 	reportBuilder();         
					System.out.println("Reporte Formulario Generado Correctamente");
					JRExporter exporter = new JRPdfExporter();
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
					File fil_reporte = new File(ec.getRealPath("/reportes/reporte_gastos_deducibles_"+ utilitario.getVariable("IDE_USUA") + ".pdf"));
	 			    exporter.setParameter(JRExporterParameter.OUTPUT_FILE, fil_reporte);
					exporter.exportReport();
					utilitario.agregarMensajeInfo("Formulario de Gastos ", "Generado correctamente");
						
			}else{	
				FacesContext fc = FacesContext.getCurrentInstance();
			 	ExternalContext ec = fc.getExternalContext();        
			 	reportBuilder();         
				System.out.println("Reporte Formulario Generado Correctamente");
				JRExporter exporter = new JRPdfExporter();
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				File fil_reporte = new File(ec.getRealPath("/reportes/reporte_gastos_deducibles_"+ utilitario.getVariable("IDE_USUA") + ".pdf"));
 			    exporter.setParameter(JRExporterParameter.OUTPUT_FILE, fil_reporte);
				exporter.exportReport();

				
				TablaGenerica  tabGastosIngresados=utilitario.consultar("SELECT dedu.ide_srdee,dedu.ide_gtemp, "
						+ "dedu.valor_deducible_srdee  "
						+ "FROM sri_deducibles_empleado dedu  "
						+ "left join gth_empleado EMP on emp.ide_gtemp=dedu.ide_gtemp "
						+ "where emp.ide_gtemp="+utilitario.getVariable("IDE_GTEMP")+" and dedu.ide_srded in(select ide_srded  "
						+ "FROM sri_deducibles "
						+ "WHERE IDE_SRIMR IN(select IDE_SRIMR from sri_impuesto_renta "
								+ "where detalle_srimr like '"+utilitario.getAnio(utilitario.getFechaActual())+"%' order by IDE_SRIMR desc limit 1 )) ");
				
	for (int i = 0; i < tabGastosIngresados.getTotalFilas(); i++) {
			utilitario.getConexion().ejecutarSql("update sri_deducibles_empleado "
					+ "set gasto_deducible_generado_srdee=true where ide_srdee=" + tabGastosIngresados.getValor(i,"ide_srdee"));

				}
	utilitario.ejecutarJavaScript("waceptarGasto.hide();");
	cargarDatos();
			}
				
			   

		/*}else {
		utilitario.agregarMensajeInfo("Alerta Gastos deducibles", "Usted sobrepasa el valor máximo es de: "+valorGastosDeducible);	
		utilitario.ejecutarJavaScript("waceptarGasto.hide();");
		cargarDatos();
		}*/
	   
	   
	   
	   
		 				
		    }
		    catch (Exception ex)
		    {
		      System.out.println("error" + ex.getMessage());
		      ex.printStackTrace();
		    }
		 
	}
    
    
    public GthEmpleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(GthEmpleado empleado) {
        this.empleado = empleado;
    }

    public GthDireccion getDireccion() {
        return direccion;
    }

    
    
    public SriDeduciblesEmpleado getDeducibles() {
		return deducibles;
	}

	public void setDeducibles(SriDeduciblesEmpleado deducibles) {
		this.deducibles = deducibles;
	}

    public List getListaPaises() {
        return listaPaises;
    }

    public void setListaPaises(List listaPaises) {
        this.listaPaises = listaPaises;
    }

    public List getListaProvincias() {
        return listaProvincias;
    }

    public void setListaProvincias(List listaProvincias) {
        this.listaProvincias = listaProvincias;
    }

    public List getListaCiudades() {
        return listaCiudades;
    }

    public void setListaCiudades(List listaCiudades) {
        this.listaCiudades = listaCiudades;
    }

    public void setDireccion(GthDireccion direccion) {
        this.direccion = direccion;
    }

    public String getStrPais() {
        return strPais;
    }

    public void setStrPais(String strPais) {
        this.strPais = strPais;
    }

    public String getStrProvincia() {
        return strProvincia;
    }

    public void setStrProvincia(String strProvincia) {
        this.strProvincia = strProvincia;
    }

    public String getStrCiudad() {
        return strCiudad;
    }

    public void setStrCiudad(String strCiudad) {
        this.strCiudad = strCiudad;
    }

    public List getListaParroquias() {
        return listaParroquias;
    }

    public void setListaParroquias(List listaParroquias) {
        this.listaParroquias = listaParroquias;
    }

    public String getStrParroquia() {
        return strParroquia;
    }

    public void setStrParroquia(String strParroquia) {
        this.strParroquia = strParroquia;
    }

    public List getListaTiposTelefono() {
        return listaTiposTelefono;
    }

    public void setListaTiposTelefono(List listaTiposTelefono) {
        this.listaTiposTelefono = listaTiposTelefono;
    }

    public List<GthTelefono> getListaTelefonos() {
        return listaTelefonos;
    }

    public void setListaTelefonos(List<GthTelefono> listaTelefonos) {
        this.listaTelefonos = listaTelefonos;
    }

    public GthTelefono getTelefonoNuevo() {
        return telefonoNuevo;
    }

    public void setTelefonoNuevo(GthTelefono telefonoNuevo) {
        this.telefonoNuevo = telefonoNuevo;
    }




	public SriDeduciblesEmpleado getDeduciblesNuevo() {
		return deduciblesNuevo;
	}

	public void setDeduciblesNuevo(SriDeduciblesEmpleado deduciblesNuevo) {
		this.deduciblesNuevo = deduciblesNuevo;
	}

    public GthCorreo getCorreoPersonal() {
        return correoPersonal;
    }

    public void setCorreoPersonal(GthCorreo correoPersonal) {
        this.correoPersonal = correoPersonal;
    }

    public GthCorreo getCorreoInstitucional() {
        return correoInstitucional;
    }

    public void setCorreoInstitucional(GthCorreo correoInstitucional) {
        this.correoInstitucional = correoInstitucional;
    }

    public String getStrOpcion() {
        return strOpcion;
    }

    public void setStrOpcion(String strOpcion) {
        this.strOpcion = strOpcion;
    }

    public List<GthCargasFamiliares> getListaCargasFamiliares() {
        return listaCargasFamiliares;
    }

    public void setListaCargasFamiliares(List<GthCargasFamiliares> listaCargasFamiliares) {
        this.listaCargasFamiliares = listaCargasFamiliares;
    }

    public List getListaTipoParentesco() {
        return listaTipoParentesco;
    }

    public void setListaTipoParentesco(List listaTipoParentesco) {
        this.listaTipoParentesco = listaTipoParentesco;
    }

    public List getListaTipoDocumento() {
        return listaTipoDocumento;
    }

    public void setListaTipoDocumento(List listaTipoDocumento) {
        this.listaTipoDocumento = listaTipoDocumento;
    }

    public List getListaGenero() {
        return listaGenero;
    }

    public void setListaGenero(List listaGenero) {
        this.listaGenero = listaGenero;
    }

    public GthCargasFamiliares getCargaNueva() {
        return cargaNueva;
    }

    public void setCargaNueva(GthCargasFamiliares cargaNueva) {
        this.cargaNueva = cargaNueva;
    }

    public String getTelefonoEliminado() {
        return telefonoEliminado;
    }

    public void setTelefonoEliminado(String telefonoEliminado) {
        this.telefonoEliminado = telefonoEliminado;
    }

    public String getCargaEliminada() {
        return cargaEliminada;
    }

    public void setCargaEliminada(String cargaEliminada) {
        this.cargaEliminada = cargaEliminada;
    }

    public List getListaEstadoCivil() {
        return listaEstadoCivil;
    }

    public void setListaEstadoCivil(List listaEstadoCivil) {
        this.listaEstadoCivil = listaEstadoCivil;
    }

    public String getStrCasado() {
        return strCasado;
    }

    public void setStrCasado(String strCasado) {
        this.strCasado = strCasado;
    }

    public String getStrUnionLibre() {
        return strUnionLibre;
    }

    public void setStrUnionLibre(String strUnionLibre) {
        this.strUnionLibre = strUnionLibre;
    }

    public GthConyuge getConyugue() {
        return conyugue;
    }

    public void setConyugue(GthConyuge conyugue) {
        this.conyugue = conyugue;
    }

    public List getListaActividadLaboral() {
        return listaActividadLaboral;
    }

    public void setListaActividadLaboral(List listaActividadLaboral) {
        this.listaActividadLaboral = listaActividadLaboral;
    }

    public List getListaNacionalidad() {
        return listaNacionalidad;
    }

    public void setListaNacionalidad(List listaNacionalidad) {
        this.listaNacionalidad = listaNacionalidad;
    }

    public List getListaCargos() {
        return listaCargos;
    }

    public void setListaCargos(List listaCargos) {
        this.listaCargos = listaCargos;
    }

    public List getListaDeducibles() {
		return listaDeducibles;
	}

	public void setListaDeducibles(List listaDeducibles) {
		this.listaDeducibles = listaDeducibles;
	}

    public String getStrSoltero() {
        return strSoltero;
    }

    public void setStrSoltero(String strSoltero) {
        this.strSoltero = strSoltero;
    }

    public GthTelefono getConyugueTelefono() {
        return conyugueTelefono;
    }

    public void setConyugueTelefono(GthTelefono conyugueTelefono) {
        this.conyugueTelefono = conyugueTelefono;
    }

    public GthUnionLibre getConyugueUnionLibre() {
        return conyugueUnionLibre;
    }

    public void setConyugueUnionLibre(GthUnionLibre conyugueUnionLibre) {
        this.conyugueUnionLibre = conyugueUnionLibre;
    }

	public List<SriDeduciblesEmpleado> getListaGastosDeducibles() {
		return listaGastosDeducibles;
	}

	public void setListaGastosDeducibles(
			List<SriDeduciblesEmpleado> listaGastosDeducibles) {
		this.listaGastosDeducibles = listaGastosDeducibles;
	}

	public String getGastoEliminado() {
		return gastoEliminado;
	}

	public void setGastoEliminado(String gastoEliminado) {
		this.gastoEliminado = gastoEliminado;
	}
    
	    
public String getStrPathReporte() {
		return strPathReporte;
	}

	public void setStrPathReporte(String strPathReporte) {
		this.strPathReporte = strPathReporte;
	}

	
public Map getParametros() {
		return parametros;
	}

	public void setParametros(Map parametros) {
		this.parametros = parametros;
	}


	public String getValorMaxGastosDeducible() {
		return valorMaxGastosDeducible;
	}

	public void setValorMaxGastosDeducible(String valorMaxGastosDeducible) {
		this.valorMaxGastosDeducible = valorMaxGastosDeducible;
	}

	public double getValoresTablaGastosDeducible() {
		return valoresTablaGastosDeducible;
	}

	public void setValoresTablaGastosDeducible(double valoresTablaGastosDeducible) {
		this.valoresTablaGastosDeducible = valoresTablaGastosDeducible;
	}

public boolean isBooIngresoGasto() {
		return booIngresoGasto;
	}

	public void setBooIngresoGasto(boolean booIngresoGasto) {
		this.booIngresoGasto = booIngresoGasto;
	}

public boolean isBooIngresoGastoValidacion() {
		return booIngresoGastoValidacion;
	}

	public void setBooIngresoGastoValidacion(boolean booIngresoGastoValidacion) {
		this.booIngresoGastoValidacion = booIngresoGastoValidacion;
	}

	
	

public boolean isBooIngresoGastoValidacionReporte() {
		return booIngresoGastoValidacionReporte;
	}

	public void setBooIngresoGastoValidacionReporte(
			boolean booIngresoGastoValidacionReporte) {
		this.booIngresoGastoValidacionReporte = booIngresoGastoValidacionReporte;
	}

/**
 * Metodo valida que no se sobrespase el valor por concepto de gastos deducibles 
 * @param valorIngresa es el valor ingresado por teclado
 * @param ide_srded es el tipo de gasto seleccionado
 * @param modificar badera para saber si es modificacion o ingreso
 * @return
 */
    public boolean validarGastosPersonalesDeducibles(double valorIngresa, int ide_srded,int modificar){

		 String ide_srimr="";
				 
		 
				 //ser_nomina.getSriImpuestoRenta(utilitario.getFechaActual()).getValor("IDE_SRIMR");
		 
		 ide_srimr= "18";
		TablaGenerica tab_deducibles=ser_nomina.getSriDeducibles(ide_srimr);
		String modificarGasto="";
		if (modificar!=0) {
			modificarGasto="and ide_srded not in ("+ide_srded+")";
		}
		TablaGenerica tab_sri_gastos_deducible=utilitario.consultar("SELECT ide_srdee, ide_srded, ide_gtemp, "
				+ "valor_deducible_srdee, observacion_srdee,activo_srdee  "
				+ "FROM sri_deducibles_empleado where ide_gtemp="+empleado.getIdeGtemp().intValue()+" and ide_srded in(SELECT ide_srded  "
				+ "FROM sri_deducibles  "
				+ "WHERE IDE_SRIMR IN("+ide_srimr+") "+modificarGasto+" )  order by ide_srdee");

	boolean bandIngresoValor=false;
	 double canastaBasicaFamiliar= Double.parseDouble(utilitario.getVariable("p_canasta_familiar"));
	 double multiplicadorFraccionesGravadas = Double.parseDouble(utilitario.getVariable("p_num_veces_canasta_familiar"));
	 double valorMaxGastosDeducibles=canastaBasicaFamiliar*multiplicadorFraccionesGravadas;
	double valor1=0.0;	
		for (int j = 0; j < tab_deducibles.getTotalFilas(); j++) {
			if (ide_srded==Integer.parseInt(tab_deducibles.getValor(j, "IDE_SRDED"))){
				double dou_gasto_ded=0;
				double dou_gasto_ded_max=0; 
				try {
					valor1=valorIngresa;
				
				//System.out.println(" deducible gastos "+dou_gasto_ded);
				} catch (Exception e) {
					// TODO: handle exception
				}
				try {
					dou_gasto_ded_max=Double.parseDouble(tab_deducibles.getValor(j, "valor_maximo"));
					//System.out.println(" deducible gastos maximo "+dou_gasto_ded_max);

				} catch (Exception e) {
					// TODO: handle exception
				}

				//if (valor1>dou_gasto_ded_max){
				if (valor1>valorGastosDeducible){

					utilitario.agregarMensajeInfo("No se puede guardar los gastos deducibles", "El deducible "+tab_deducibles.getValor(j, "detalle_srded")+" sobrepasa el valor maximo deducible "+dou_gasto_ded_max);
					bandIngresoValor=true;
					//return false;
				}else{
					double valor_acumulado_deducible=0.0;
					TablaGenerica tab_sum_sri_gastos_deducible=utilitario.consultar("SELECT ide_gtemp, sum(valor_deducible_srdee) as valor_deducible_srdee "
							+ "FROM sri_deducibles_empleado "
							+ "where ide_gtemp="+empleado.getIdeGtemp().intValue()+" and ide_srded in(SELECT ide_srded  "
							+ "FROM sri_deducibles  "
							+ "WHERE IDE_SRIMR IN("+ide_srimr+") "+modificarGasto+" ) "
									+ "group by ide_gtemp"
							 		+ " order by ide_gtemp");
					if (tab_sum_sri_gastos_deducible.getValor("valor_deducible_srdee")==null || tab_sum_sri_gastos_deducible.getValor("valor_deducible_srdee").toString().equals("")) {
						valor_acumulado_deducible=0.00;
					}else {
						valor_acumulado_deducible=Double.parseDouble(tab_sum_sri_gastos_deducible.getValor("valor_deducible_srdee"));
					}
					
					
					//if ((valor_acumulado_deducible+valor1)>valorMaxGastosDeducibles) {
						if ((valor_acumulado_deducible+valor1)>valorGastosDeducible) {
					
						//utilitario.agregarMensajeInfo("No se puede guardar los gastos deducibles", "El deducible "+tab_deducibles.getValor(j, "detalle_srded")+" sobrepasa el valor maximo permitigo : "+valorGastosDeducible);
						//bandIngresoValor=false;
						//return false;
						
							utilitario.agregarMensajeInfo("Alerta Gastos deducibles", "Usted sobrepasa el valor máximo es de: "+valorGastosDeducible);	
						bandIngresoValor=true;
						return true;
						
						
					}else{				
					bandIngresoValor=true;
					break;}
					//return true;
				}
			}
		}
	
		

double dou_porcentaje_aplica=0;
		try {
			dou_porcentaje_aplica=Double.parseDouble(utilitario.getVariable("p_porcentaje_tot_ing_grab"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		//dou_tot_ing_gravados=ser_nomina.getTotalIngresosGravados(ide_srimr, empleado.getIdeGtemp().toString());
		dou_tot_ing_gravados=ser_nomina.getTotalIngresosGravados("17", empleado.getIdeGtemp().toString());
		double dou_tot_gastos_personales=0.0;
		if (tab_sri_gastos_deducible.getTotalFilas()>0) {
			double valorTemporal=0.0;
			for (int i = 0; i < tab_sri_gastos_deducible.getTotalFilas(); i++) {
				valorTemporal+=Double.parseDouble(tab_sri_gastos_deducible.getValor(i,"VALOR_DEDUCIBLE_SRDEE"));
			}
			
			dou_tot_gastos_personales=valorTemporal+valor1;
		}else {
			dou_tot_gastos_personales=valor1;	
		}
		
		
		double dou_porcentaje_total_ingresos_gravados=(dou_tot_ing_gravados*dou_porcentaje_aplica)/100;
		double dou_veces_fraccion_basica_desgravada=ser_nomina.getValorVecesFraccionBasicaDesgravada(ide_srimr);

		//System.out.println("tot ing gravados "+dou_tot_ing_gravados);
		//System.out.println("tot gastos personales "+dou_tot_gastos_personales);
		//System.out.println("50% tot ing grav "+dou_porcentaje_total_ingresos_gravados);
		//System.out.println("1.3 veces fracion basica desgravada "+dou_veces_fraccion_basica_desgravada);

	//	if (dou_tot_gastos_personales>dou_veces_fraccion_basica_desgravada){
			if (dou_tot_gastos_personales>valorGastosDeducible){
			
			//utilitario.agregarMensajeInfo("No se puede guardar los gastos deducibles", "El total de gastos deducibles "+dou_tot_gastos_personales+" sobrepasa el valor maximo deducible "+valorGastosDeducible);
			
				utilitario.agregarMensajeInfo("Alerta Gastos deducibles", "Usted sobrepasa el valor máximo es de: "+valorGastosDeducible);	
		
			//return false;
		}

		//if (dou_tot_gastos_personales>dou_porcentaje_total_ingresos_gravados){
		//	utilitario.agregarMensajeInfo("No se puede guardar los gastos deducibles", "El total de gastos deducibles "+dou_tot_gastos_personales+" sobrepasa al porcentaje de total ingresos gravados "+dou_porcentaje_total_ingresos_gravados);
		//	return false;
		//}
		return true;

	}

    
  /**
   * Metodo validacion gastoDeducible retorna un valor enteero mayor a cero si existe y 0 para el caso contrario  
   * recibe el tipo de gasto parametro ide_srded
   */
    public int validaGastoRepetido(String ide_srded){
    	
    	   TablaGenerica tabGastoRepetido=utilitario.consultar("SELECT ide_srdee, ide_srded, ide_gtemp, "
           		+ "valor_deducible_srdee, observacion_srdee,  "
           		+ "activo_srdee "
           		+ "FROM sri_deducibles_empleado  "
           		+ "where ide_gtemp="+utilitario.getVariable("IDE_GTEMP")+""
           		+ "and ide_srded in(select ide_srded  "
           		+ "FROM sri_deducibles  "
           		+ "WHERE IDE_SRIMR IN(select IDE_SRIMR from sri_impuesto_renta  "
           		+ "where detalle_srimr like '%"+utilitario.getAnio(utilitario.getFechaActual())+"%' order by ide_srimr desc limit 1) "
           		+ "and ide_srded="+ide_srded+" "
           		+ "order by ide_srded)");
         	return tabGastoRepetido.getTotalFilas();
    }
    

 /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    

   
	  public String getURL()
	  {
	    ExternalContext iecx = FacesContext.getCurrentInstance().getExternalContext();
	    HttpServletRequest request = (HttpServletRequest)iecx.getRequest();
	    String path = request.getRequestURL() + "";
	    path = path.substring(0, path.lastIndexOf("/"));
	    if (path.indexOf("portal") > 0) {
	      path = path.substring(0, path.lastIndexOf("/"));
	    }
	    return path;
	  }
	  public Conexion getConexion()
	  {
	    Conexion conexion = (Conexion)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CONEXION");
	    return conexion;
	  }

	  
	  public String getvalorMaximoGastoDeducible (){

		 //PARAMETROS UTILIXADOS PARA 2022
		 double canastaBasicaFamiliar= Double.parseDouble(utilitario.getVariable("p_canasta_familiar"));
		 double multiplicadorFraccionesGravadas = Double.parseDouble(utilitario.getVariable("p_num_veces_canasta_familiar"));
		 double valorMaxGastosDeducibles=canastaBasicaFamiliar*multiplicadorFraccionesGravadas;
		 double valorDecimoCuarto=0.00;
		 double valorFondoReserva=0.00;
		 double valorDecimoTercer=0.00;
		 double ingresosExcentos=0.00;
			String ide_srimr=ser_nomina.getSriImpuestoRenta(utilitario.getFechaActual()).getValor("IDE_SRIMR");
			TablaGenerica tab_deducibles=ser_nomina.getSriDeducibles(ide_srimr);

			double dou_porcentaje_aplica=0;
			try {
				dou_porcentaje_aplica=Double.parseDouble(utilitario.getVariable("p_porcentaje_tot_ing_grab"));
			} catch (Exception e) {
				// TODO: handle exception
			}

			dou_tot_ing_gravados=0.00;
			dou_tot_ing_gravados=ser_nomina.getTotalIngresosGravados("17", utilitario.getVariable("IDE_GTEMP"));
			double dou_tot_gastos_personales=0.00;
			double dou_porcentaje_total_ingresos_gravados=dou_tot_ing_gravados;
			//double dou_porcentaje_total_ingresos_gravados=(dou_tot_ing_gravados*dou_porcentaje_aplica)/100;
			//Cambio psra p_num_veces_vmgd=2.13
			//24090.30
			double dou_veces_fraccion_basica_desgravada=ser_nomina.getValorVecesFraccionBasicaDesgravada(ide_srimr);

			String valorFinal="";
//			if (dou_porcentaje_total_ingresos_gravados>dou_veces_fraccion_basica_desgravada) {
				//return "Total Ingresos: "+dou_porcentaje_total_ingresos_gravados+" ||  Valor Max:"+valorMaxGastosDeducibles;
			
			
			if (empleado.isEnfermedadCatastroficaGtemp()==true) {
				valorRetornoGastos(10);

			}else {
				if (empleado.getNumCargasGtemp()==null) {
					valorRetornoGastos(0);	

				}else{
				valorRetornoGastos(empleado.getNumCargasGtemp());	}
			}
			return "Total Ingresos: "+dou_porcentaje_total_ingresos_gravados+" ||  Valor Max:"+valorGastosDeducible;

			

			
			
//			}else {
				//return ""+dou_porcentaje_total_ingresos_gravados;
				//return "Total Ingresos: "+dou_porcentaje_total_ingresos_gravados+" ||  Valor Max:"+valorMaxGastosDeducibles;
//			}


			}

		  
	  
	  
	  
	  public Double getvalorTablaGastoDeducible (){
		  BigDecimal valorGasto;
		       TablaGenerica tabGastos = utilitario.consultar("select sum(suma) AS SUMA,ide_gtemp from (SELECT dedu.ide_gtemp, "
		          		+ "sum(dedu.valor_deducible_srdee) suma "
		          		+ "FROM sri_deducibles_empleado dedu "
		          		+ "left join gth_empleado EMP on emp.ide_gtemp=dedu.ide_gtemp "
		          		+ "where emp.ide_gtemp="+utilitario.getVariable("IDE_GTEMP")+" and dedu.ide_srded in(select ide_srded  "
		          		+ "FROM sri_deducibles  "
		          		+ "WHERE IDE_SRIMR IN(select IDE_SRIMR from sri_impuesto_renta "
		          		+ "where detalle_srimr like '%"+utilitario.getAnio(utilitario.getFechaActual())+"%' order by ide_srimr desc limit 1))  "
		          		+ "group by dedu.ide_gtemp,dedu.valor_deducible_srdee)a "
		          		+ "group by ide_gtemp");
		          if (tabGastos.getTotalFilas()>0) {
		        	  valorGasto=new BigDecimal(tabGastos.getValor("SUMA"));
		    			return valorGasto.doubleValue();

				}else {
	    			return 0.00;

				}

		  
	  }


	  public void estadoGasto(){
		  
			TablaGenerica  tabGastosIngresados=utilitario.consultar("SELECT dedu.ide_srdee,dedu.ide_gtemp, "
					+ "dedu.valor_deducible_srdee  "
					+ "FROM sri_deducibles_empleado dedu  "
					+ "left join gth_empleado EMP on emp.ide_gtemp=dedu.ide_gtemp "
					+ "where emp.ide_gtemp="+utilitario.getVariable("IDE_GTEMP")+" and dedu.ide_srded in(select ide_srded  "
					+ "FROM sri_deducibles "
					+ "WHERE IDE_SRIMR IN(select IDE_SRIMR from sri_impuesto_renta "
					+ "where detalle_srimr like '%"+utilitario.getAnio(utilitario.getFechaActual())+"%' order by ide_srimr desc limit 1) ) "
					+ "AND dedu.gasto_deducible_generado_srdee=true");
			
if (tabGastosIngresados.getTotalFilas()==0){
	  this.booIngresoGasto=true;
		
}else {
	this.booIngresoGasto=false;
	utilitario.agregarMensajeInfo("Usted ya ha generado su Formulario de Gastos Deducibles", "Por favor contactese con el Administrador");
    utilitario.ejecutarJavaScript("waceptarGasto.hide();");
	return;
}
		  
		 
	  }
	   public void cancelarGastoDeducible() {
		   booIngresoGasto = false;
	        if (booIngresoGasto == false) {
	            utilitario.ejecutarJavaScript("waceptarGasto.hide();");
	        } else {
	            utilitario.agregarMensajeInfo("Debe generar su formulario de gastos", "Para continuar es necesario que registre sus datos y presione aceptar");
	        }
	    }
	   
	   
		  public void estadoGastoDeducibleValidacion(){
			  String fechalimite="";
				fechalimite=utilitario.getVariable("p_fehca_limite_gastos_personales");
				String fechaComparacion="";
				fechaComparacion=utilitario.getFechaActual();	  
				TablaGenerica  tabGastosIngresados=utilitario.consultar("SELECT dedu.ide_srdee,dedu.ide_gtemp, "
						+ "dedu.valor_deducible_srdee  "
						+ "FROM sri_deducibles_empleado dedu  "
						+ "left join gth_empleado EMP on emp.ide_gtemp=dedu.ide_gtemp "
						+ "where emp.ide_gtemp="+utilitario.getVariable("IDE_GTEMP")+" and dedu.ide_srded in(select ide_srded  "
						+ "FROM sri_deducibles "
						+ "WHERE IDE_SRIMR IN(select IDE_SRIMR from sri_impuesto_renta "
						+ "where detalle_srimr like '%"+utilitario.getAnio(utilitario.getFechaActual())+"%' order by ide_srimr desc limit 1)) "
						+ "AND dedu.gasto_deducible_generado_srdee=true");
	if (tabGastosIngresados.getTotalFilas()==0){
		
		if (fechaComparacion.compareTo(fechalimite)<=0) {
		  this.booIngresoGastoValidacion=false;
		  this.booIngresoGastoValidacionReporte=true;
		}else {
		
				this.booIngresoGastoValidacion=true;
				this.booIngresoGastoValidacionReporte=true;
		}

			
	}else {
		if (fechaComparacion.compareTo(fechalimite)<=0) {
		this.booIngresoGastoValidacion=true;
		this.booIngresoGastoValidacionReporte=false;
		}else {
			this.booIngresoGastoValidacion=true;
			this.booIngresoGastoValidacionReporte=true;
		} 
			
		
		
	}
		
	}
		  

		    
		  
		  public void estadoRubrosSriGatosValidacion(){
			  String fechalimite="";
				fechalimite=utilitario.getVariable("p_fehca_limite_gastos_personales");
				String fechaComparacion="";
				fechaComparacion=utilitario.getFechaActual();	  
				TablaGenerica  tabGastosIngresados=utilitario.consultar("SELECT ide_srfor,r307_srfor,"
						+ "r353_srfor, r361_srfor,  "
						+ "r363_srfor, r365_srfor, r367_srfor, r369_srfor, r371_srfor, r373_srfor,  "
						+ "r381_srfor, r399_srfor, r401_srfor, r403_srfor, r405_srfor, r407_srfor,  "
						+ "r349_srfor, activo_srfor, tipo_formulario_srfor, ide_gtemp, ide_srimr  "
						+ "FROM sri_formulario_107  "
						+ "where ide_gtemp="+utilitario.getVariable("IDE_GTEMP")+" and "
					    + " IDE_SRIMR IN(select IDE_SRIMR from sri_impuesto_renta "
					    + "where detalle_srimr like '%"+utilitario.getAnio(utilitario.getFechaActual())+"%') "
						+ "AND tipo_formulario_srfor=2");
					
				
				if (tabGastosIngresados.getTotalFilas()==0){
		  this.booIngresoGastos107=false;
		}else {
			  this.booIngresoGastos107=true;
		}

				
	}
		  

		    
		  
		  
		  public String getDivisionPolitica(String ide_gedip,int tipo){
			  String divisionPolitica="";
			  
			  TablaGenerica tabGastos =null;
			  if (tipo==1) {
			       tabGastos = utilitario.consultar("select IDE_GEDIP,DETALLE_GEDIP from GEN_DIVISION_POLITICA DP where DP.IDE_GETDP in (10) "
			       		+ "and ide_gedip in("+ide_gedip+")"
			       		+ "order by DP.DETALLE_GEDIP ASC");
			          if (tabGastos.getTotalFilas()>0) {
			        	  divisionPolitica=tabGastos.getValor("IDE_GEDIP");
			    			return divisionPolitica;

					}else {
		    			return "-1";

					}
			}else if (tipo==2) {
			       tabGastos = utilitario.consultar("select a.ide_gedip,a.detalle_gedip as ciudad  "
			       		+ "from  "
			       		+ "(  "
			       		+ "select * from gen_division_politica where ide_getdp=2  "
			       		+ ") a "
			       		+ "left join "
			       		+ "(  "
			       		+ "select a.ide_gedip,a.detalle_gedip as canton, "
			       		+ "b.ide_gedip as codigo_provincia,b.detalle_gedip as provincia, "
			       		+ "b.gen_ide_gedip as codigo_pais "
			       		+ "from (select * from gen_division_politica where ide_getdp=3) a  "
			       		+ "left join (select * from gen_division_politica where ide_getdp=1) b on a.gen_ide_gedip = b.ide_gedip  "
			       		+ ") b on a.gen_ide_gedip = b.ide_gedip   "
			       		+ "left join gen_division_politica c on b.codigo_pais=c.ide_gedip   "
			       		+ "where not c.detalle_gedip is null  "
			       		+ "and a.ide_gedip=57 "
			       		+ "order by provincia,a.detalle_gedip");
			          if (tabGastos.getTotalFilas()>0) {
			        	  divisionPolitica=tabGastos.getValor("IDE_GEDIP");
			    			return divisionPolitica;

					}else {
		    			return "-1";

					}
				
			}else if (tipo==3) {
				 tabGastos = utilitario.consultar("select codigo_pais,c.detalle_gedip ||' ' as pais,codigo_provincia,provincia||' ' as provincia,a.ide_gedip,a.detalle_gedip as ciudad  "
				       		+ "from  "
				       		+ "(  "
				       		+ "select * from gen_division_politica where ide_getdp=2  "
				       		+ ") a "
				       		+ "left join "
				       		+ "(  "
				       		+ "select a.ide_gedip,a.detalle_gedip as canton, "
				       		+ "b.ide_gedip as codigo_provincia,b.detalle_gedip as provincia, "
				       		+ "b.gen_ide_gedip as codigo_pais "
				       		+ "from (select * from gen_division_politica where ide_getdp=3) a  "
				       		+ "left join (select * from gen_division_politica where ide_getdp=1) b on a.gen_ide_gedip = b.ide_gedip  "
				       		+ ") b on a.gen_ide_gedip = b.ide_gedip   "
				       		+ "left join gen_division_politica c on b.codigo_pais=c.ide_gedip   "
				       		+ "where not c.detalle_gedip is null  "
				       		+ "and a.ide_gedip=57 "
				       		+ "order by provincia,a.detalle_gedip");
			          if (tabGastos.getTotalFilas()>0) {
			        	  divisionPolitica=tabGastos.getValor("IDE_GEDIP");
			    			return divisionPolitica;
					}else {
		    			return "-1";

					}
			
			
			}
			  
	return divisionPolitica;
			  
		  }
		  
		public SriFormulario107 getSri() {
			return sri;
		}

		public void setSri(SriFormulario107 sri) {
			this.sri = sri;
		}

		public SriFormulario107 getFormulario107() {
			return formulario107;
		}

		public void setFormulario107(SriFormulario107 formulario107) {
			this.formulario107 = formulario107;
		}
		  
	    public SriImpuestoRenta getImpuesto_renta() {
			return impuesto_renta;
		}

		public void setImpuesto_renta(SriImpuestoRenta impuesto_renta) {
			this.impuesto_renta = impuesto_renta;
		}

		public boolean isBooIngresoGastos107() {
			return booIngresoGastos107;
		}

		public void setBooIngresoGastos107(boolean booIngresoGastos107) {
			this.booIngresoGastos107 = booIngresoGastos107;
		}  
		  
		public UploadedFile getAdjunto() {
			return adjunto;
		}

		public void setAdjunto(UploadedFile adjunto) {
			this.adjunto = adjunto;
		}  
		  
		public boolean isBooIngresoNuevaGastoValidacion() {
			return booIngresoNuevaGastoValidacion;
		}

		public void setBooIngresoNuevaGastoValidacion(
				boolean booIngresoNuevaGastoValidacion) {
			this.booIngresoNuevaGastoValidacion = booIngresoNuevaGastoValidacion;
		}

		public int getAnio() {
			return anio;
		}

		public void setAnio(int anio) {
			this.anio = anio;
		}  
		  
		public String getDialogo() {
			return dialogo;
		}

		public void setDialogo(String dialogo) {
			this.dialogo = dialogo;
		}


		public List getListCargasFamiliares() {
			return listCargasFamiliares;
		}

		public void setListCargasFamiliares(List listCargasFamiliares) {
			this.listCargasFamiliares = listCargasFamiliares;
		}

		public List getListEnfermedadCatastrofica() {
			return listEnfermedadCatastrofica;
		}

		public void setListEnfermedadCatastrofica(List listEnfermedadCatastrofica) {
			this.listEnfermedadCatastrofica = listEnfermedadCatastrofica;
		}


		public void seleccionanNumCargaFamiliar(AjaxBehaviorEvent evt) {

			this.empleado.getNumCargasGtemp();
			//System.out.println("si ingresa");

			// Obtengo el codigo del permiso solicitado
			 numCarga = this.empleado.getNumCargasGtemp().intValue();
			final Boolean enfermedad = empleado.isEnfermedadCatastroficaGtemp();

			if (enfermedad==true) {
				if (getvalorTablaGastoDeducible()<valorGastosDeducible) {
					valorRetornoGastos(10);

				}else {
				utilitario.agregarMensajeInfo("Alerta Gastos deducibles", "Usted sobrepasa el valor máximo es de: "+valorGastosDeducible);	
				}
				
				valorMaxGastosDeducible="Total Ingresos: "+dou_tot_ing_gravados+" ||  Valor Max:"+utilitario.getFormatoNumero(canastaBasicaFamiliar*20.00, 2);
				valorGastosDeducible=Double.parseDouble(utilitario.getFormatoNumero(canastaBasicaFamiliar*20.00, 2));
			}else {
				valorRetornoGastos(numCarga);
				if (getvalorTablaGastoDeducible()<valorGastosDeducible) {
					
				}else {
				utilitario.agregarMensajeInfo("Alerta Gastos deducibles", "Usted sobrepasa el valor máximo es de: "+valorGastosDeducible);	
				}
			if (numCarga==1) {
				//"6882.00";a
				valorMaxGastosDeducible="Total Ingresos: "+dou_tot_ing_gravados+" ||  Valor Max:"+utilitario.getFormatoNumero(canastaBasicaFamiliar*9.00, 2);
				valorGastosDeducible=Double.parseDouble(utilitario.getFormatoNumero(canastaBasicaFamiliar*9.00, 2));
						empleado.setNumCargasGtemp(numCarga);
						
			}else if (numCarga==2) {
				//valorMaxGastosDeducible="8412.00";
				valorMaxGastosDeducible="Total Ingresos: "+dou_tot_ing_gravados+" ||  Valor Max:"+utilitario.getFormatoNumero(canastaBasicaFamiliar*11.00, 2);
				valorGastosDeducible=Double.parseDouble(utilitario.getFormatoNumero(canastaBasicaFamiliar*11.00, 2));
				empleado.setNumCargasGtemp(numCarga);

			}else if (numCarga==3) {
				//valorMaxGastosDeducible="10706.00";
				valorMaxGastosDeducible="Total Ingresos: "+dou_tot_ing_gravados+" ||  Valor Max:"+utilitario.getFormatoNumero(canastaBasicaFamiliar*14.00, 2);
				valorGastosDeducible=Double.parseDouble(utilitario.getFormatoNumero(canastaBasicaFamiliar*14.00, 2));
				empleado.setNumCargasGtemp(numCarga);

			}else if (numCarga==4) {
				 //valorMaxGastosDeducible="13000.00";
					valorMaxGastosDeducible="Total Ingresos: "+dou_tot_ing_gravados+" ||  Valor Max:"+utilitario.getFormatoNumero(canastaBasicaFamiliar*17.00, 2);
					valorGastosDeducible=Double.parseDouble(utilitario.getFormatoNumero(canastaBasicaFamiliar*17.00, 2));
					empleado.setNumCargasGtemp(numCarga);

			}else if (numCarga==5) {
				//valorMaxGastosDeducible="15294.00";
				valorMaxGastosDeducible="Total Ingresos: "+dou_tot_ing_gravados+" ||  Valor Max:"+utilitario.getFormatoNumero(canastaBasicaFamiliar*20.00, 2);
				valorGastosDeducible=Double.parseDouble(utilitario.getFormatoNumero(canastaBasicaFamiliar*20.00, 2));
				empleado.setNumCargasGtemp(numCarga);

			}else {
				//valorMaxGastosDeducible="5353.00";
				valorMaxGastosDeducible="Total Ingresos: "+dou_tot_ing_gravados+" ||  Valor Max:"+utilitario.getFormatoNumero(canastaBasicaFamiliar*7.00, 2);
				valorGastosDeducible=Double.parseDouble(utilitario.getFormatoNumero(canastaBasicaFamiliar*7.00, 2));
				empleado.setNumCargasGtemp(numCarga);

			}
			
			}
			
		}
		
		
		public void seleccionaEnfermedadCatastrofica (AjaxBehaviorEvent evt) {
		 enfermedad = empleado.isEnfermedadCatastroficaGtemp();
			if (enfermedad==true) {
				valorRetornoGastos(10);
				
				
				
		if (getvalorTablaGastoDeducible()<valorGastosDeducible) {
					
				}else {
				utilitario.agregarMensajeInfo("Alerta Gastos deducibles", "Usted sobrepasa el valor máximo es de: "+valorGastosDeducible);	
				}
				
				valorMaxGastosDeducible="Total Ingresos: "+dou_tot_ing_gravados+" ||  Valor Max:"+utilitario.getFormatoNumero(canastaBasicaFamiliar*20.00, 2);
				valorGastosDeducible=Double.parseDouble(utilitario.getFormatoNumero(canastaBasicaFamiliar*20.00, 2));
			}else {
				
				// Obtengo el codigo del permiso solicitado
				numCarga = this.empleado.getNumCargasGtemp().intValue();
				valorRetornoGastos(numCarga);
		if (getvalorTablaGastoDeducible()<valorGastosDeducible) {
					
				}else {
				utilitario.agregarMensajeInfo("Alerta Gastos deducibles", "Usted sobrepasa el valor máximo es de: "+valorGastosDeducible);	
				}
				if (numCarga==1) {
					//"6882.00";a
					valorMaxGastosDeducible="Total Ingresos: "+dou_tot_ing_gravados+" ||  Valor Max:"+utilitario.getFormatoNumero(canastaBasicaFamiliar*9.00, 2);
					valorGastosDeducible=Double.parseDouble(utilitario.getFormatoNumero(canastaBasicaFamiliar*9.00, 2));
							
							
				}else if (numCarga==2) {
					//valorMaxGastosDeducible="8412.00";
					valorMaxGastosDeducible="Total Ingresos: "+dou_tot_ing_gravados+" ||  Valor Max:"+utilitario.getFormatoNumero(canastaBasicaFamiliar*11.00, 2);
					valorGastosDeducible=Double.parseDouble(utilitario.getFormatoNumero(canastaBasicaFamiliar*11.00, 2));
				}else if (numCarga==3) {
					//valorMaxGastosDeducible="10706.00";
					valorMaxGastosDeducible="Total Ingresos: "+dou_tot_ing_gravados+" ||  Valor Max:"+utilitario.getFormatoNumero(canastaBasicaFamiliar*14.00, 2);
					valorGastosDeducible=Double.parseDouble(utilitario.getFormatoNumero(canastaBasicaFamiliar*14.00, 2));
				}else if (numCarga==4) {
					 //valorMaxGastosDeducible="13000.00";
						valorMaxGastosDeducible="Total Ingresos: "+dou_tot_ing_gravados+" ||  Valor Max:"+utilitario.getFormatoNumero(canastaBasicaFamiliar*17.00, 2);
						valorGastosDeducible=Double.parseDouble(utilitario.getFormatoNumero(canastaBasicaFamiliar*17.00, 2));
				}else if (numCarga==5) {
					//valorMaxGastosDeducible="15294.00";
					valorMaxGastosDeducible="Total Ingresos: "+dou_tot_ing_gravados+" ||  Valor Max:"+utilitario.getFormatoNumero(canastaBasicaFamiliar*20.00, 2);
					valorGastosDeducible=Double.parseDouble(utilitario.getFormatoNumero(canastaBasicaFamiliar*20.00, 2));
				}else {
					//valorMaxGastosDeducible="5353.00";
					valorMaxGastosDeducible="Total Ingresos: "+dou_tot_ing_gravados+" ||  Valor Max:"+utilitario.getFormatoNumero(canastaBasicaFamiliar*7.00, 2);
					valorGastosDeducible=Double.parseDouble(utilitario.getFormatoNumero(canastaBasicaFamiliar*7.00, 2));
				}
				
				
			}
			
		}
		
		
		public Double valorRetornoGastos(int carga){
			
			if (carga==10) {
				valorGastosDeducible=Double.parseDouble(utilitario.getFormatoNumero(canastaBasicaFamiliar*20.00, 2));
			}else
			
			if (carga==1) {
				//"6882.00";a
				//valorMaxGastosDeducible="Total Ingresos: "+dou_tot_ing_gravados+" ||  Valor Max:"+utilitario.getFormatoNumero(canastaBasicaFamiliar*9.00, 2);
				valorGastosDeducible=Double.parseDouble(utilitario.getFormatoNumero(canastaBasicaFamiliar*9.00, 2));
						
						
			}else if (carga==2) {
				//valorMaxGastosDeducible="8412.00";
				//valorMaxGastosDeducible="Total Ingresos: "+dou_tot_ing_gravados+" ||  Valor Max:"+utilitario.getFormatoNumero(canastaBasicaFamiliar*11.00, 2);
				valorGastosDeducible=Double.parseDouble(utilitario.getFormatoNumero(canastaBasicaFamiliar*11.00, 2));
			}else if (carga==3) {
				//valorMaxGastosDeducible="10706.00";
				//valorMaxGastosDeducible="Total Ingresos: "+dou_tot_ing_gravados+" ||  Valor Max:"+utilitario.getFormatoNumero(canastaBasicaFamiliar*14.00, 2);
				valorGastosDeducible=Double.parseDouble(utilitario.getFormatoNumero(canastaBasicaFamiliar*14.00, 2));
			}else if (carga==4) {
				 //valorMaxGastosDeducible="13000.00";
					//valorMaxGastosDeducible="Total Ingresos: "+dou_tot_ing_gravados+" ||  Valor Max:"+utilitario.getFormatoNumero(canastaBasicaFamiliar*17.00, 2);
					valorGastosDeducible=Double.parseDouble(utilitario.getFormatoNumero(canastaBasicaFamiliar*17.00, 2));
			}else if (carga==5) {
				//valorMaxGastosDeducible="15294.00";
				//valorMaxGastosDeducible="Total Ingresos: "+dou_tot_ing_gravados+" ||  Valor Max:"+utilitario.getFormatoNumero(canastaBasicaFamiliar*20.00, 2);
				valorGastosDeducible=Double.parseDouble(utilitario.getFormatoNumero(canastaBasicaFamiliar*20.00, 2));
			}else {
				//valorMaxGastosDeducible="5353.00";
			//	valorMaxGastosDeducible="Total Ingresos: "+dou_tot_ing_gravados+" ||  Valor Max:"+utilitario.getFormatoNumero(canastaBasicaFamiliar*7.00, 2);
				valorGastosDeducible=Double.parseDouble(utilitario.getFormatoNumero(canastaBasicaFamiliar*7.00, 2));
			}
			
			
			return valorGastosDeducible;
			
		}
		
		
		  
}
