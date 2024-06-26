/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_sistema.parametros;

import framework.aplicacion.Parametro;
import java.util.ArrayList;
import java.util.List;

public class Parametros {

    public List<Parametro> getParametrosSistema() {
        List<Parametro> lis_parametros = new ArrayList<Parametro>();
//////////////////////////////////////////////////////////////////////
        /*
         * SISTEMA MODULO =0
         */
        lis_parametros.add(new Parametro("0", "P_SIS_RESETEO_CLAVE", "Indica cuando se resetea la clave de un usuario", "6", "SIS_ACCION_AUDITORIA", "IDE_ACAU", "DESCRIPCION_ACAU"));
        lis_parametros.add(new Parametro("0", "P_SIS_ACTIVA_USUARIO", "Indica cuando se pone en estado activo a un usuario", "4", "SIS_ACCION_AUDITORIA", "IDE_ACAU", "DESCRIPCION_ACAU"));
        lis_parametros.add(new Parametro("0", "P_SIS_DESACTIVA_USUARIO", "Indica cuando se pone en estado inactivo a un usuario", "3", "SIS_ACCION_AUDITORIA", "IDE_ACAU", "DESCRIPCION_ACAU"));
        lis_parametros.add(new Parametro("0", "P_SIS_INGRESO_USUARIO", "Indica cuando un usuario accede exitosamente al sistema", "0", "SIS_ACCION_AUDITORIA", "IDE_ACAU", "DESCRIPCION_ACAU"));
        lis_parametros.add(new Parametro("0", "P_SIS_FALLO_INGRESO", "Indica cuando no se produce un acceso al sistema por parte de un usuario", "1", "SIS_ACCION_AUDITORIA", "IDE_ACAU", "DESCRIPCION_ACAU"));
        lis_parametros.add(new Parametro("0", "P_SIS_CADUCO_SESSION", "Indica cuando a un usuario se le expira el tiempo de su session", "7", "SIS_ACCION_AUDITORIA", "IDE_ACAU", "DESCRIPCION_ACAU"));
        lis_parametros.add(new Parametro("0", "P_SIS_SALIO_USUARIO", "Indica cuando un usuario sale del sistema", "8", "SIS_ACCION_AUDITORIA", "IDE_ACAU", "DESCRIPCION_ACAU"));
        lis_parametros.add(new Parametro("0", "P_SIS_CAMBIO_CLAVE", "Indica cuando un usuario cambia su clave", "5", "SIS_ACCION_AUDITORIA", "IDE_ACAU", "DESCRIPCION_ACAU"));
        lis_parametros.add(new Parametro("0", "P_SIS_CREAR_USUARIO", "Indica cuando se crea un usuario en el sistema", "9", "SIS_ACCION_AUDITORIA", "IDE_ACAU", "DESCRIPCION_ACAU"));
        lis_parametros.add(new Parametro("0", "P_SIS_BLOQUEA_USUARIO", "Indica cuando se bloqueao a un usuario", "2", "SIS_ACCION_AUDITORIA", "IDE_ACAU", "DESCRIPCION_ACAU"));
        lis_parametros.add(new Parametro("0", "P_SIS_DESBLOQUEA_USUARIO", "Indica cuando se desbloquea a un usuario", "10", "SIS_ACCION_AUDITORIA", "IDE_ACAU", "DESCRIPCION_ACAU"));


        
//////////////////////////////////////////////////////////////////////
       
        /*
         * GENERAL MODULO =1
         */
        
        
        lis_parametros.add(new Parametro("1", "p_tipo_cobro_factura", "Permite definir el tipo de cobro inicial que se mostrará por defecto en la facturación",  "4","rec_tipo", "ide_retip", "detalle_retip"));
        lis_parametros.add(new Parametro("1", "p_gen_terminacion_encargo_posicion", "Permite al momento de hacer una terminacion de subrogacion volver a las caracteristicas del contrato que poseia antes de realizar la subrogacion", "46","GEN_ACCION_EMPLEADO_DEPA", "IDE_GEAED", "DETALLE_GEAED"));
        
        lis_parametros.add(new Parametro("1", "p_gen_status_stand_by", "Indica la categoria estatus stand by", "2","GEN_CATEGORIA_ESTATUS", "IDE_GECAE", "DETALLE_GECAE"));
        lis_parametros.add(new Parametro("1", "p_gen_accion_empl_comision", "Indica la accion de empleado Comision", "51","GEN_ACCION_EMPLEADO_DEPA", "IDE_GEAED", "DETALLE_GEAED"));
        lis_parametros.add(new Parametro("1", "p_gen_tipo_institucion_educativa", "Indica el tipo de institucion Educativa", "4","GEN_TIPO_INSTITUCION", "IDE_GETII", "DETALLE_GETII"));
        lis_parametros.add(new Parametro("1", "p_gen_tipo_institucion_aseguradora", "Indica el tipo de institucion Aseguradoras", "5","GEN_TIPO_INSTITUCION", "IDE_GETII", "DETALLE_GETII"));
        lis_parametros.add(new Parametro("1", "p_gen_tipo_institucion_financiera", "Indica el tipo de institucion Financiera", "2","GEN_TIPO_INSTITUCION", "IDE_GETII", "DETALLE_GETII"));
        lis_parametros.add(new Parametro("1", "p_gen_encargo_posicion", "Permite al momento de hacer una Subrogaciï¿½n desplegar los cargos a ser encargados", "13","GEN_ACCION_MOTIVO_EMPLEADO", "IDE_GEAME", "IDE_GEAME"));
        lis_parametros.add(new Parametro("1", "p_gen_accion_contratacion", "Permite al momento de hacer una contratacion desplegar un dialogo para ingresar el periodo de asistencia ", "1","GEN_ACCION_EMPLEADO_DEPA", "IDE_GEAED", "IDE_GEAED"));
        
        lis_parametros.add(new Parametro("1", "p_gen_lugar_aplica_haber", "Aplica al haber ", "2","GEN_LUGAR_APLICA", "IDE_GELUA", "DETALLE_GELUA"));
        lis_parametros.add(new Parametro("1", "p_gen_lugar_aplica_debe", "aplica al debe ", "1","GEN_LUGAR_APLICA", "IDE_GELUA", "DETALLE_GELUA"));        
        lis_parametros.add(new Parametro("1", "p_gen_instituciones", "Selecciona las instituciones que requiera mostrar (Instituciones Educativas)", "41,43","GEN_INSTITUCION", "IDE_GEINS", "DETALLE_GEINS"));
        lis_parametros.add(new Parametro("1", "p_gen_casas_comerciales", "Selecciona el tipo de institucion casas comerciales  (Casas comerciales )",  "2","GEN_TIPO_INSTITUCION", "IDE_GETII", "DETALLE_GETII"));
        lis_parametros.add(new Parametro("1", "p_gen_instituciones_capacitacion", "Selecciona las instituciones que requiera mostrar (Instituciones Capacitacion)", "41,43"));
        lis_parametros.add(new Parametro("1", "p_gen_instituciones_experiencia_laboral", "Selecciona las instituciones que requiera mostrar (Instituciones Experiencia Laboral)", "41,43"));

        
        lis_parametros.add(new Parametro("1", "p_gen_estado_activo", "Permite conocer tipo de estado (activo)en vacaciones ", "1","gen_estados", "IDE_GEEST", "detalle_geest"));
        lis_parametros.add(new Parametro("1", "p_gen_estado_inactivo", "Permite conocer tipo de estado (inactivo)en vacaciones ", "2","gen_estados", "IDE_GEEST", "detalle_geest"));
        lis_parametros.add(new Parametro("1", "p_gen_actividad_capacitador", "Permite conocer tipo de actividad economica ", "3","GTH_TIPO_ACTIVIDAD_ECONOMICA", "IDE_GTTAE", "DETALLE_GTTAE"));
        lis_parametros.add(new Parametro("1", "p_firma_resp_solicitud_debito_sd", "Permite conocer el cargo de la persona que firma la Solicitud de Anticipo ", "GERENTE ADMINISTRATIVO"));
        lis_parametros.add(new Parametro("1", "p_gen_tipo_institucion_educativa_idiomas", "Indica el tipo de institucion Educativa de Idiomas", "4","GEN_TIPO_INSTITUCION", "IDE_GETII", "DETALLE_GETII"));
        lis_parametros.add(new Parametro("1", "p_gen_responsable_depa_bienestar", "Indica el departamento para poder ver el responsable de la entrevista (Talento Humano)", "135","GEN_DEPARTAMENTO", "IDE_GEDEP", "DETALLE_GEDEP"));
        lis_parametros.add(new Parametro("1", "p_valor_iva", "Indica el valor aplicado del iva", "0.12"));
        lis_parametros.add(new Parametro("1", "p_ambiente_sri", "Indica el ambiente sri, ambiente: 1=pruebas, 2=produccion", "1"));
        lis_parametros.add(new Parametro("1", "p_ip_ws_emgirs", "Indica la direccion ip del servidor del webservices emgirsep", "172.20.120.17"));
        lis_parametros.add(new Parametro("1", "p_director_adminsitrativo", "Indica el titulo y nombre del Director Administrativo", "Ing. Paul Velez"));
        lis_parametros.add(new Parametro("1", "p_jefe_activos_fijos", "Indica el titulo y nombre del jefe de Activos Fijos", "Ing. Evelyn Suarez"));
        lis_parametros.add(new Parametro("1", "p_ip_ws_emgirsCO", "Indica la direccion ip del servidor del webservices NEW Contratos Online", "172.20.120.105:8080"));
        lis_parametros.add(new Parametro("1", "p_web_portal_remates", "Indica la direccion http del portal de remates", "http://remates.funcionjudicial.gob.ec/rematesjudiciales-web/pages/public/portal.jsf"));
        lis_parametros.add(new Parametro("1", "p_web_files_manuales", "Indica la direccion http de los manuales", "https://files.emgirs.gob.ec/s/8kQmMmPxqqXNbkC"));
        
        lis_parametros.add(new Parametro("1", "p_sec_sumilla","Indica el modulo para generar el secuencial de los trámites de sumilla","55","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("1", "p_ip_servidor_erp_tesoreria", "Indica la direccion ip del servidor para el area de tesoreria emgirsep", "172.20.120.23"));
        //lis_parametros.add(new Parametro("1", "p_sec_precontractual","Indica el modulo para generar el secuencial de los procesos de contratación","1","gen_modulo_secuencial","ide_gemos","numero_secuencial_gemos"));
        lis_parametros.add(new Parametro("1", "p_precon_automatico","Indica si la carga documental del flujo de los procesos de contratación es 1: automatico, 0: manual","1"));
        lis_parametros.add(new Parametro("1",  "p_ip_servidor_seguimiento", "Indica la direccion ip del servidor para el area de seguimiento emgirsep", "172.20.120.17"));
        /*
         * GESTION DE TALENTO HUMANO MODULO =2
         */
       
        lis_parametros.add(new Parametro("2", "p_gth_tipo_sindicato", "Indica el tipo de sindicato que pertenece", "1","gth_tipo_sindicato", "ide_gttsi", "detalle_gttsi"));
        lis_parametros.add(new Parametro("2", "p_gth_tipo_contrato_nombramiento", "Indica el tipo de documento de contrato NOMBRAMIENTO", "0","GTH_TIPO_CONTRATO", "IDE_GTTCO", "DETALLE_GTTCO"));
        lis_parametros.add(new Parametro("2", "p_gth_tipo_sangre", "Indica el tipo de sangre del empleado", "11","gth_tipo_sangre", "ide_gttis", "detalle_gttis"));
        lis_parametros.add(new Parametro("2", "p_gth_nacionalidad", "Indica la nacionalidad del empleado", "2","gth_nacionalidad", "ide_gtnac", "detalle_gtnac"));
        lis_parametros.add(new Parametro("2", "p_gth_tipo_documento_cedula", "Indica el tipo de documento de identidad CEDULA", "0","GTH_TIPO_DOCUMENTO_IDENTIDAD", "IDE_GTTDI", "DETALLE_GTTDI"));
        lis_parametros.add(new Parametro("2", "p_gth_tipo_documento_ruc", "Indica el tipo de documento de identidad RUC", "2","GTH_TIPO_DOCUMENTO_IDENTIDAD", "IDE_GTTDI", "DETALLE_GTTDI"));
        lis_parametros.add(new Parametro("2", "p_gth_estado_civil_soltero", "Indica el estado civil soltero", "0","GTH_ESTADO_CIVIL", "IDE_GTESC", "DETALLE_GTESC"));
        lis_parametros.add(new Parametro("2", "p_gth_estado_civil_union_libre", "Indica el estado civil union libre", "2","GTH_ESTADO_CIVIL", "IDE_GTESC", "DETALLE_GTESC"));
        lis_parametros.add(new Parametro("2", "p_gth_tipo_telefono_celular", "Indica el tipo de telefono celular", "0","GTH_TIPO_TELEFONO", "IDE_GTTIT", "DETALLE_GTTIT"));
        lis_parametros.add(new Parametro("2", "p_gth_tipo_telefono_fijo", "Indica el tipo de telefono fijo", "1","GTH_TIPO_TELEFONO", "IDE_GTTIT", "DETALLE_GTTIT"));
        lis_parametros.add(new Parametro("2", "p_gth_tipo_telefono_oficina", "Indica el tipo de telefono de oficina", "3","GTH_TIPO_TELEFONO", "IDE_GTTIT", "DETALLE_GTTIT"));
        lis_parametros.add(new Parametro("2", "p_gth_tipo_empleado_codigo", "Indica el tipo de empleado de codigo de trabajo", "1","GTH_TIPO_EMPLEADO", "IDE_GTTEM", "DETALLE_GTTEM"));
        lis_parametros.add(new Parametro("2", "p_gth_coordinador_tthh", "Indica el nombre del coordinador de talento humano", "Ing. Julio Trujillo"));
        lis_parametros.add(new Parametro("2", "p_gth_analista_tthh", "Indica el nombre del analista de talento humano", "Ing. Paola Loyola"));
        lis_parametros.add(new Parametro("2", "p_gth_administrativo_financiero", "Indica el nombre del gerente administrativo financiero", "Ing. Juan Suarez"));
        lis_parametros.add(new Parametro("2", "p_gth_financiero", "Indica el nombre del coordinador de financiero", "Ing. Juan Suarez"));
        lis_parametros.add(new Parametro("2", "p_gth_financiero_liquidacion", "Indica el nombre del coordinador de financiero", "Econ. Fernando Rodriguez"));

        lis_parametros.add(new Parametro("2", "p_pantalla_generacion_rol", "Indica la pantalla que se visualizará al momento de generar el rol", "/portal/rolesPago.jsf"));

        /*
         * NOMINA =3
         */
        
        
        lis_parametros.add(new Parametro("3", "p_nrh_tipo_garante_empl_biess", "Indica el tipo de garante Empleado del Biess ", "1","NRH_TIPO_GARANTE", "IDE_NRTIG", "DETALLE_NRTIG"));
        
        lis_parametros.add(new Parametro("3", "p_nrh_tipo_nomina_escenario", "Indica el tipo de nomina NORMAL ", "5","NRH_TIPO_NOMINA", "IDE_NRTIN", "DETALLE_NRTIN"));
        lis_parametros.add(new Parametro("3", "p_nrh_tipo_nomina_normal", "Indica el tipo de nomina NORMAL ", "0","NRH_TIPO_NOMINA", "IDE_NRTIN", "DETALLE_NRTIN"));
        lis_parametros.add(new Parametro("3", "p_nrh_tipo_nomina_liquidacion", "Indica el tipo de nomina LIQUIDACION ", "2","NRH_TIPO_NOMINA", "IDE_NRTIN", "DETALLE_NRTIN"));
        lis_parametros.add(new Parametro("3", "p_nrh_tipo_nomina_pago_decimos", "Indica el tipo de nomina PAGO DE DECIMOS ", "4","NRH_TIPO_NOMINA", "IDE_NRTIN", "DETALLE_NRTIN"));
        lis_parametros.add(new Parametro("3", "p_nrh_tipo_nomina_fondos_reserva", "Indica el tipo de nomina PAGO DE FONDOS DE RESERVA ", "4","NRH_TIPO_NOMINA", "IDE_NRTIN", "DETALLE_NRTIN"));
        lis_parametros.add(new Parametro("3", "p_nrh_tipo_nonima_horas_suplementarias", "Indica el tipo de nomina PAGO DE HORAS SUPLEMENTARIAS ", "12","NRH_TIPO_NOMINA", "IDE_NRTIN", "DETALLE_NRTIN"));

        
        lis_parametros.add(new Parametro("3", "p_nrh_tipo_nomina_para_generar_rol", "Indica los tipos de nominas para generar desde rol ", "0,3"));
        lis_parametros.add(new Parametro("3", "p_nrh_tipo_nomina_para_calcular_renta", "Indica los tipos de nominas a los cuales se calcula la renta ", "0"));
        
        
        lis_parametros.add(new Parametro("3", "p_nrh_estado_nomina_cerrada", "Indica la estado de NOMINA CERRADA, en este estado los datos son solo de lectura, no se puede modificar ni eliminar ", "1","NRH_ESTADO_ROL", "IDE_NRESR", "DETALLE_NRESR"));
        lis_parametros.add(new Parametro("3", "p_nrh_estado_pre_nomina", "Indica la estado PRE-NOMINA, en este estado se pueden leer,modificar y eliminar datos de la nomina", "2","NRH_ESTADO_ROL", "IDE_NRESR", "DETALLE_NRESR"));
        lis_parametros.add(new Parametro("3", "p_nrh_estado_nomina_anulada", "Indica la estado de NOMINA ANULADA, en este estado los datos son solo de lectura, y nos indica que una nomina generada se encuentra anulada ", "3","NRH_ESTADO_ROL", "IDE_NRESR", "DETALLE_NRESR"));
        
        lis_parametros.add(new Parametro("3", "p_nrh_forma_calculo_formula", "Indica la forma de calculo TECLADO del Rubro ", "0","NRH_FORMA_CALCULO", "IDE_NRFOC", "DETALLE_NRFOC"));
        lis_parametros.add(new Parametro("3", "p_nrh_forma_calculo_teclado", "Indica la forma de calculo TECLADO del Rubro ", "1","NRH_FORMA_CALCULO", "IDE_NRFOC", "DETALLE_NRFOC"));
        lis_parametros.add(new Parametro("3", "p_nrh_forma_calculo_importado", "Indica la forma de calculo TECLADO del Rubro ", "2","NRH_FORMA_CALCULO", "IDE_NRFOC", "DETALLE_NRFOC"));
        lis_parametros.add(new Parametro("3", "p_nrh_forma_calculo_constante", "Indica la forma de calculo TECLADO del Rubro ", "3","NRH_FORMA_CALCULO", "IDE_NRFOC", "DETALLE_NRFOC"));
        
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_importado", "Indica la forma de calculo IMPORTADO del Rubro ", "2","NRH_FORMA_CALCULO", "IDE_NRFOC", "DETALLE_NRFOC"));

        
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_vacaciones_liquidacion", "Indica el rubro en el cual se va a calcular el valor correspondiente a vacaciones ", "28","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_desc_valores_liquidar", "Indica el descuento de valores por liquidar ", "289","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_dias_ajuste_sueldo", "Indica el numero de dias para calculo de rol en ajuste de sueldo ", "285","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_ajuste_sueldo", "Indica el numero de dias para calculo de rol en ajuste de sueldo ", "287","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_total_ingresos", "Indica el rubro Total Ingresos de un empleado ", "11","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_total_egresos", "Indica el rubro Total Ingresos de un empleado ", "22","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_valor_recibir", "Indica el rubro Total Ingresos de un empleado ", "131","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_subtotal_iess", "Indica el rubro Total Ingresos de un empleado ", "281","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_seguro_social_con_rmu", "Indica el rubro Seguro Social calculado solo con la RMU ", "291","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
//        lis_parametros.add(new Parametro("3", "p_nrh_rubro_sueldo_subroga", "Indica el rubro Suelso subroga ", "27","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
//        lis_parametros.add(new Parametro("3", "p_nrh_rubro_sueldo_subroga_mn", "Indica el rubro sueldo subroga manual ", "177","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        //178
        
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_rmu_cargo_subrogante", "Indica el rubro Total Ingresos de un empleado ", "278","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));        
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_dias_subrogados", "Indica el rubro Total Ingresos de un empleado ", "279","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));        


        lis_parametros.add(new Parametro("3", "p_nrh_rubro_region", "Indica el rubro Region, lo cual nos permite saber a que region pertenece el empleado para pago de decimo 4 ", "285","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_dias_pendientes_vacacion", "Indica el numero de dias pendientes de vacacion ", "68","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_decimo_cuarto_rol", "Indica el rubro Decimo Cuarto Sueldo para el Rol  (Rol) en la fecha de pago configurada para el rubro ", "9","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_decimo_cuarto_rol_p", "Indica el rubro Decimo Cuarto Sueldo para el Rol  (Rol) en la fecha de pago configurada para el rubro ", "283","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));

        lis_parametros.add(new Parametro("3", "p_nrh_rubro_dias_trabajados_d4", "Este rurbro nos indica el numero de dias para el calculo de decimo cuarto ", "65","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_proviciones_d4", "Este rurbro nos indica el numero de dias para el calculo de decimo cuarto ", "121","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_decimo_tercer_rol", "Indica el rubro Decimo Tercer Sueldo para el (Rol) ", "12","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_decimo_tercer_rol_p", "Indica el rubro Decimo Tercer Sueldo para el Rol  (Rol) en la fecha de pago configurada para el rubro ", "334","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_dias_trabajados_d3", "Este rurbro nos indica el numero de dias para el calculo de decimo tercero ", "63","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_salario_basico_unificado_vig", "Este rurbro nos indica el numero de dias para el Salario basico unificado vigente ", "276","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_proviciones_d3", "Este rurbro nos indica el numero de dias para el calculo de decimo cuarto ", "125","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_descuento_decimo_cuarto", "Indica el rubro Decimo Tercer Sueldo para el (Rol) ", "273","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_descuento_decimo_tercer", "Indica el rubro Decimo Tercer Sueldo para el (Rol) ", "195","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_beneficios_guarderia", "Indica el rubro Beneficios Guarderia para el (Rol) ", "144","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));        
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_aportes_personales", "Indica el rubro aporte_personal para el (Rol) nombre del rubro seguro social ", "44","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_impuesto_renta_mensual", "Indica el rubro aporte_personal para el (Rol) ", "41","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));        
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_acumula_fondos_reserva", "Indica el rubro para saber si un empleado acumula o no los fondos de reserva ", "46","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_valor_fondos_reserva", "Indica el rubro con el valor acumulado de los fondos de reserva nomina ", "29","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_valor_provision_fondos_reserva", "Indica el rubro con el valor acumulado de los fondos de reserva iess", "120","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_descuento_nomina", "Indica el rubro Decimo Tercer Sueldo para el (Rol) ", "48","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_acumula_decimos", "Indica el rubro para saber si un empleado acumula los decimos true=acumula, false=no acumula ", "330","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_base_imponible_mes_anterior", "Indica el rubro donde se va a importar el sueldo base del mes anterior ", "329","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_imponible_mes_anterior", "Indica el rubro para del mes anteriro que se toma para el nuevo rubro ", "329","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        
        //Este voy a cambiar
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_fondreser_acum_ante", "Indica el rubro fondos de reserva acumulado a ser importado para la nomina vigente", "136,346","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_fondreser_nomi_ante", "Indica el rubro fondos de reserva pago en nomina a ser importado para la nomina vigente", "29,346","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_fondreser_nomi_ante_liquidacion", "Indica el rubro fondos de reserva pago en nomina a ser importado para la nomina vigente", "29,348","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));

        lis_parametros.add(new Parametro("3", "p_nrh_rubro_fondreser_acum_pago", "Indica el rubro fondos de reserva acumulados con el cual se va a pagar en la nomina vigente", "338","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_fondreser_nomi_pago", "Indica el rubro fondos de reserva en nomina con el cual se va a pagar en la nomina vigente", "339","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
      
        lis_parametros.add(new Parametro("3", "p_nrh_trubro_egreso_informativo", "Indica el tipo de rubro Egreso Informativo (Tipo Rol) ", "2","NRH_TIPO_RUBRO", "IDE_NRTIR", "DETALLE_NRTIR"));
        lis_parametros.add(new Parametro("3", "p_nrh_trubro_ingreso_informativo", "Indica el tipo de rubro Ingreso Informativo (Tipo Rol) ", "3","NRH_TIPO_RUBRO", "IDE_NRTIR", "DETALLE_NRTIR"));
        lis_parametros.add(new Parametro("3", "p_nrh_trubro_informativo", "Indica el tipo de rubro Informativo (Tipo Rol) ", "4","NRH_TIPO_RUBRO", "IDE_NRTIR", "DETALLE_NRTIR"));
        
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_remuneracion_unificada", "Indica el rubro Remuneracion unificada ", "24","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_remuneracion_unificada_honorarios", "Indica el rubro Remuneracion unificada ", "277","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_dias_periodo_nomina", "Indica el rubro Decimo Cuarto Sueldo para el Rol  (Rol) en la fecha de pago configurada para el rubro ", "2","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_dias_trabajados", "Indica los dias trabajados para la generacion del rol ", "274","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_dias_fondos_reserva", "Indica los dias trabajados para la generacion del rol ", "69","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_dias_antiguedad", "Indica los dias trabajados para la generacion del rol ", "60","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("3", "p_gerencia_general_ap", "Indica el nombre de la persona encargada de la Gerencia General para el reporte de acciones de personal", "Eco. Roberto. A. Machuca. C"));
        lis_parametros.add(new Parametro("3", "p_gerencia_administrativa_ap", "Indica el nombre de la persona encargada de la Gerencia Administrativa para el reporte de acciones de personal", "Ing. Marco V. Egas A."));
        
        lis_parametros.add(new Parametro("3", "p_nrh_trubro_egreso", "Indica el tipo de rubro Egreso (Tipo Rol) ", "1","NRH_TIPO_RUBRO", "IDE_NRTIR", "DETALLE_NRTIR"));
        lis_parametros.add(new Parametro("3", "p_nrh_trubro_ingreso", "Indica el tipo de rubro Ingreso (Tipo Rol) ", "0","NRH_TIPO_RUBRO", "IDE_NRTIR", "DETALLE_NRTIR"));
        lis_parametros.add(new Parametro("3", "p_cargo_gerencia_general_ap", "Indica el nombre del Cargo de la Gerencia General para el reporte de acciones de personal", "GERENTE GENERAL"));
        lis_parametros.add(new Parametro("3", "p_cargo_gerencia_administrativa_ap", "Indica el nombre del Cargo de la Gerencia Administrativa para el reporte de acciones de personal", "GERENTE ADMINISTRATIVO"));

        lis_parametros.add(new Parametro("3", "p_liquidacion_elaborado_por", "Indica el nombre de la persona encargada de Elaborar la Liquidacion para el reporte de Liquidacion de Haberes", "Ing. Nelly Fabara"));
        lis_parametros.add(new Parametro("3", "p_liquidacion_revisado_por", "Indica el nombre de la persona encargada de Revizar la Liquidacion para el reporte de Liquidacion de Haberes", "Ing. Maria Fernanda Reyes"));
        lis_parametros.add(new Parametro("3", "p_liquidacion_aprobado_por", "Indica el nombre de la persona encargada de Aprobar la Liquidacion para el reporte de Liquidacion de Haberes", "Ing. Marco Egas"));

        lis_parametros.add(new Parametro("3", "p_nrh_rubro_vacaciones_liquidacion", "Indica el rubro en el cual se va a calcular el valor correspondiente a vacaciones ", "28","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("3", "p_nrh_tipo_garante", "Indica el tipo de garante EMPLEADO DE EMGIRS", "1","nrh_tipo_garante", "ide_nrtig", "detalle_nrtig"));


        lis_parametros.add(new Parametro("3", "p_nrh_rubro_total_proviciones_d3", "Indica el rubro en el cual se va a calcular el valor del decimo tercero ", "367","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_total_proviciones_d4", "Indica el rubro en el cual se va a calcular el valor del decimo cuarto ", "377","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));

        lis_parametros.add(new Parametro("3", "p_nrh_rubro_dias_acumulados", "Indica el rubro en el cual se va a guardar los dias trabajados", "379","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));

        
        lis_parametros.add(new Parametro("3", "p_valor_jubilado_retorna_iess_personal", "Indica el valor asignado por iess personal de un empleado jubilado  ", "0.0945"));
        lis_parametros.add(new Parametro("3", "p_valor_jubilado_retorna_iess_patronal", "Indica el valor asignado por iess patronal de un empleado jubilado ", "0.1015"));

        //Cambio de codigo para iess personal y patronal codigo de trabajo 
        
        lis_parametros.add(new Parametro("3", "p_valor_codigo_iess_personal", "Indica el valor asignado por iess personal de un empleado jubilado  ", "0.1145"));
        lis_parametros.add(new Parametro("3", "p_valor_codigo_iess_patronal", "Indica el valor asignado por iess patronal de un empleado jubilado ", "0.0915"));

        //Rubros actualizar proyeccion de ingresos
        lis_parametros.add(new Parametro("3", "p_nrh_rubros_imp_rent_rol", "Almacena los valores generados por diferentes rubros", "395","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        
        //Proyeccion gastos 
        
        lis_parametros.add(new Parametro("3", "p_valor_rmu", "Indica el rubro rmu ", "288"));
        lis_parametros.add(new Parametro("3", "p_valor_base_imponible", "Indica el rubro base imponible", "337"));
        
        
        //LIQUIDACIONES
        lis_parametros.add(new Parametro("3", "p_nrh_total_decimot_liquidacion", "Indica el rubro total decimo tecero pagado ", "383"));
        lis_parametros.add(new Parametro("3", "p_nrh_total_decimoc_liquidacion", "Indica el rubfro total decimo cuarto pagado ", "385"));
        lis_parametros.add(new Parametro("3", "p_nrh_nro_dias_vacaciones", "Indica el rubro por nro dias vacaciones pendientes ", "387"));
        lis_parametros.add(new Parametro("3", "p_nrh_total_valor_vacaciones_liquidacion", "Indica el valor por vacaciones liquidadas ", "388"));
        //lis_parametros.add(new Parametro("3", "p_nrh_rol_decimo_tercer", "Indica los roles para decimo tercer sueldo ", "71,72,73"));
       // lis_parametros.add(new Parametro("3", "p_nrh_rol_decimo_cuarto", "Indica los roles para decimo tercer sueldo ", "65,66,67,68,69"));
        lis_parametros.add(new Parametro("3", "p_nrh_decimo_tercer_rubros_liquidacion", "Indica los rubros que intervienen en la generacion decimo tercer sueldo ", "125,334"));
        lis_parametros.add(new Parametro("3", "p_nrh_decimo_cuarto_rubros_liquidacion", "Indica los rubros que intervienen en la generacion decimo cuarto sueldo", "121,333"));
        lis_parametros.add(new Parametro("3", "p_nrh_accion_comision_servicios", "Indica la accion por servicio de comisiones no se debe cancelar el rubro de vacaciones gen_accion_motivo", "7"));

        lis_parametros.add(new Parametro("3", "p_nrh_rubro_fondreser_acum_pago_liquidacion", "Indica el rubro fondos de reserva acumulados con el cual se va a pagar en la nomina vigente", "338","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_fondreser_nomi_pago_liquidacion", "Indica el rubro fondos de reserva en nomina con el cual se va a pagar en la nomina vigente", "339","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));


        //AJUSTE LIQUIDACION
        
        lis_parametros.add(new Parametro("3", "p_nrh_dias_exceso_liquidacion", "Indica el valor de ajuste por dias en exceso", "389"));
        lis_parametros.add(new Parametro("3", "p_nrh_ajuste_rmu_liquidacion", "Indica el valor de ajuste por dias en exceso", "390"));
        //Empleados evaluados en anticipo
        lis_parametros.add(new Parametro("3", "p_nrh_empleados_liquidacion", "Indica el valor de ajuste por dias en exceso", "1458,1594,1247"));
        lis_parametros.add(new Parametro("3", "p_nrh_ajuste_dias_exceso", "Indica si contiene o no exceso en rubros generados en liquidacion", "394"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_salario_basico_unificado_anterior", "Este rurbro nos indica el numero de dias para el Salario basico unificado vigente ", "284","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_total_fondos_reserva_liquidacion", "Este rurbro almacena los valores generados por Fondos de Reserva", "395","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_iess_personal_ajuste", "Este rurbro almacena los valores generados por Fondos de Reserva", "396"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_iess_patronal_ajuste", "Este rurbro almacena los valores generados por Fondos de Reserva", "397"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_iece_ajuste", "Este rurbro almacena los valores generados por Fondos de Reserva", "398"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_fondreser_a", "Indica el rubro fondos de reserva pago en nomina a ser importado para la nomina vigente", "347","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_fondreser_c", "Indica el rubro fondos de reserva acumulados con el cual se va a pagar en la nomina vigente", "345","NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("3", "p_valor_variable_total_base_imponible_liquidacion", "Indica el valor generado por base imponible por empleado", "401"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_iess_patronal", "Indica el valor generado en p_nrh_rubro_iess_patronal", "119"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_iece", "Indica el valor generado en p_nrh_rubro_iess_patronal", "122"));

        lis_parametros.add(new Parametro("3", "p_valor_decimot_sin_rol", "Indica el valor generado por decimo cuarto", "411"));
        lis_parametros.add(new Parametro("3", "p_valor_decimoc_sin_rol", "Indica el valor generado por decimotercer", "412"));
        
       
        //Liquidacion Ct
        lis_parametros.add(new Parametro("3", "p_valor_dias_pendientes_liquidar_ct", "Indica el valor generado por base imponible por empleado", "404"));
        lis_parametros.add(new Parametro("3", "p_valor_dias_laborados_liquidar_ct", "Indica el valor generado por base imponible por empleado", "405"));
        lis_parametros.add(new Parametro("3", "p_nrh_total_valor_vacaciones_liquidacionCT", "Indica el valor por vacaciones liquidadas ", "406"));
        lis_parametros.add(new Parametro("3", "p_fecha_inicio_liquidar", "Obtiene todos los empleados desde una fecha inicio  ", "2018-01-01"));
        lis_parametros.add(new Parametro("3", "p_fecha_fin_liquidar", "Obtiene todos los empleados desde una fecha inicio  ", "2019-03-01"));
        //lis_parametros.add(new Parametro("3", "p_dia_trabajado", "Obtiene todos los empleados desde una fecha inicio  ", "2019-03-01"));

        lis_parametros.add(new Parametro("3", "p_valor_vacaciones_negativo", "Obtiene el valor de vacaciones en negativo", "410"));
        lis_parametros.add(new Parametro("3", "p_valor_desahucio", "Obtiene el valor POR DESAHUCIO", "409"));
        
       // lis_parametros.add(new Parametro("3", "p_valor_cancela_rubro", "Obtiene el valor de vacaciones en negativo", "411"));
        
        //ACCIONES DE PERSONAL
        
        lis_parametros.add(new Parametro("3", "p_nrh_acciones_personal", "Este rurbro almacena las acciones de persona", "4,23,37,40,44,7,20,45"));
        lis_parametros.add(new Parametro("3", "p_nrh_rol_fondos_baseimp", "Este rurbro la base imponible generada en el rol", "384"));
        lis_parametros.add(new Parametro("3", "p_nrh_rol_fondos_remun", "Este rurbro la base imponible generada en el rol", "383"));


        //PARAMETROS GENERACIÓN DE ROL 
        
        lis_parametros.add(new Parametro("3", "p_nrh_generar_rol_nomina_normal", "Este rurbro se utiliza para la generación de nomina tipo normal", "0"));
        lis_parametros.add(new Parametro("3", "p_nrh_generar_rol_migracion", "Este rurbro se utiliza para la generación de nomina tipo migracion", "1"));
        lis_parametros.add(new Parametro("3", "p_nrh_generar_rol_pasantias", "Este rurbro se utiliza para la generación de nomina tipo pasantias", "3"));
        lis_parametros.add(new Parametro("3", "p_nrh_generar_rol_liquidacion", "Este rurbro se utiliza para la generación de nomina tipo liquidación", "2"));
        lis_parametros.add(new Parametro("3", "p_nrh_generar_rol_escenarios", "Este rurbro se utiliza para la generación de nomina tipo escenarios", "5"));
        lis_parametros.add(new Parametro("3", "p_nrh_generar_rol_decimo_tercer", "Este rurbro se utiliza para la generación de nomina tipo decimo tercer", "6"));
        lis_parametros.add(new Parametro("3", "p_nrh_generar_rol_decimo_cuarto", "Este rurbro se utiliza para la generación de nomina tipo decimo cuarto", "4"));
        lis_parametros.add(new Parametro("3", "p_nrh_generar_rol_fondos_reserva", "Este rurbro se utiliza para la generación de nomina tipo fondos de reserva", "7"));
        lis_parametros.add(new Parametro("3", "p_nrh_generar_rol_nomina_manual", "Este rurbro se utiliza para la generación de nomina tipo manual", "8"));
        lis_parametros.add(new Parametro("3", "p_nrh_generar_rol_horas_extra", "Este rurbro se utiliza para la generación de nomina tipo horas extra", "9"));
        lis_parametros.add(new Parametro("3", "p_nrh_generar_rol_alimentacion", "Este rurbro se utiliza para la generación de nomina tipo horas extra", "11"));
        
        lis_parametros.add(new Parametro("3", "p_nrh_tipo_nomina_hxa", "Este rurbro se utiliza para la generación de nomina tipo horas extra, suplementarias y alimentacion", "9,12,13"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_ingresos_hxa", "Este rurbro se utiliza para la generación de nomina tipo horas extra, suplementarias y alimentacion", "386"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_egresos_hxa", "Este rurbro se utiliza para la generación de nomina tipo horas extra, suplementarias y alimentacion", "255"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_iess_personal_hxa", "Este rurbro se utiliza para la generación de nomina tipo horas extra, suplementarias y alimentacion", "44"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_base_imponible_hxa", "Este rurbro se utiliza para la generación de nomina tipo horas extra, suplementarias y alimentacion", "386"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_calculo_decimo", "Este rurbro se utiliza para la generación de nomina decimocuarto", "0,9,12,13"));
        lis_parametros.add(new Parametro("3", "p_nrh_reporte_hxa", "Este rurbro almacena el tipo de nomina horas extra, suplemtarias y nocturnas", "13"));
        lis_parametros.add(new Parametro("3", "p_nrh_rubro_etiqueta_rol", "Este rurbro almacena el nombre de la etiqueta", "NÓMINA NORMAL Y H.EXTRA"));

        
        
        /*
         * SRI =4
         */
        lis_parametros.add(new Parametro("4", "p_sri_tipo_moneda_spi", "Indica el codigo de moneda dolar $ para el archivo SPI", "1"));
        lis_parametros.add(new Parametro("4", "p_sri_tipo_pago_spi", "Indica el codigo de tipo de pago via internet para el archivo SPI", "2"));
        lis_parametros.add(new Parametro("4", "p_sri_concepto_spi", "Indica el codigo del concepto para el archivo SPI", "2"));
        lis_parametros.add(new Parametro("4", "p_sri_control_spi", "Indica el numero de control para el archivo SPI", "11217287915534"));
        lis_parametros.add(new Parametro("4", "p_sri_301_sueldos_y_salarios", "Indica el 301 SUELDOS Y SALARIOS para (RUBRO 24) el	SRI", "24","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_sri_303_subrogaciones", "Indica el 303 SOBRESUELDOS, COMISIONES, BONOS Y OTROS INGRESOS GRAVADOS (RUBRO 27) para el SRI", "27","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_sri_305_participacion_utilidades", "Indica el 307 INGRESOS GRAVADOS GENERADOS CON OTROS EMPLEADORES (RUBRO 76) para el SRI", "76","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_sri_307_ing_grav_otro_emp", "Indica el 307 INGRESOS GRAVADOS GENERADOS CON OTROS EMPLEADORES (RUBRO 77) para el SRI", "77","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_sri_311_provisiones_decimo_tercero", "Indica el 311 DECIMO TERCER SUELDO (RUBRO 125)  provisiones para el SRI", "125","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_sri_313_provisiones_decimo_cuarto", "Indica el 313 DECIMO CUARTO SUELDO (RUBRO 121)  provisiones  provisiones para el SRI", "121","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_sri_315_provisiones_fondo_reserva", "Indica el 315 FONDO DE RESERVA (RUBRO 120)  provisiones  provisiones para el SRI", "120","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_sri_317_otros_ing_rel_depencia", "Indica el 317 OTROS INGRESOS EN RELACION DE DEPENDENCIA QUE NO CONSTITUYEN RENTA GRAVADA para el SRI", "283","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_sri_351_aporte_personal", "Indica el 351 (-) APORTE PERSONAL IESS CON ESTE EMPLEADOR para el SRI", "44","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_sri_353_aporte_personal_otros_empl", "Indica el 353 (-) APORTE PERSONAL IESS CON OTROS EMPLEADORES para el SRI", "288","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_sri_381_imp_renta_empleador", "Indica el 381 IMPUESTO A LA RENTA ASUMIDO POR ESTE EMPLEADOR para el SRI", "257","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_sri_403_impuesto_retenido", "Indica el 403 VALOR DEL IMPUESTO RETENIDO Y ASUMIDO POR OTROS EMPL. DURANTE EL PERIODO DECLARADO para el SRI", "258","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_sri_405_impuesto_asumido", "Indica el 405 VALOR DEL IMPUESTO ASUMIDO POR ESTE EMPLEADOR para el SRI", "267","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_sri_407_imp_retenido_trabajador", "Indica el 407 VALOR DEL IMPUESTO RETENIDO AL TRABAJADOR POR ESTE EMPLEADOR para el SRI", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        
        lis_parametros.add(new Parametro("4", "p_redep_salario_ingreso", "Indica el VALOR DEL INGRESO DEL SALARIO", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_salario_egreso", "Indica el VALOR DEL EGRESO DEL SALARIO", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_sobresueldo_ing", "Indica el VALOR DEL INGRESO DEL SOBRESUELDO", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_sobresueldo_egr", "Indica el VALOR DEL EGRESO DEL SOBRESUELDO", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_parti_utilidad_ing", "Indica el VALOR DEL INGRESO DEL PARTICIPACION DE UTILIDAD", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_parti_utilidad_egr", "Indica el VALOR DEL EGRESO DEL PARTICIPACION DE UTILIDAD", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_ingr_gravados_ing", "Indica el VALOR DEL INGRESO GRAVADO", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_ingr_gravados_egr", "Indica el VALOR DEL EGRESO GRAVADO ", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_impuesto_empleador_ing", "Indica el VALOR DEL INGRESO DEL IMPUESTO A LA RENTA POR EL EMPLEADOR", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_impuesto_empleador_egr", "Indica el VALOR DEL EGRESO DEL IMPUESTO A LA RENTA POR EL EMPLEADOR ", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_decimo_tercer_sueldo_ing", "Indica el VALOR DEL INGRESO DEL DECIMO TERCER SUELDO", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_decimo_tercer_sueldo_egr", "Indica el VALOR DEL EGRESO DEL DECIMO TERCER SUELDO", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_decimo_cuarto_sueldo_ing", "Indica el VALOR DEL INGRESO DEL DECIMO CUARTO SUELDO", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_decimo_cuarto_sueldo_egr", "Indica el VALOR DEL EGRESO DEL DECIMO CUARTO SUELDO", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_fondo_reserva_ing", "Indica el VALOR DEL INGRESO DEL FONDO DE RESERVA", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_fondo_reserva_egr", "Indica el VALOR DEL EGRESO DEL FONDO DE RESERVA", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_otros_ingrrelaciondep_ing", "Indica el VALOR DEL INGRESO DEL INGRESO EN RELACION DE DEPENDENCIA", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_otros_ingrrelaciondep_egr", "Indica el VALOR DEL EGRESO DEL INGRESO EN RELACION DE DEPENDENCIA", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_ingresos_gravaempleado_ing", "Indica el VALOR DEL INGRESO DEL EGRESO GRAVADO", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_ingresos_gravaempleado_egr", "Indica el VALOR DEL EGRESO DEL EGRESO GRAVADO", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_aporte_personal_iess_ing", "Indica el VALOR DEL INGRESO DEL APORTE PERSONAL IESS", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_aporte_personal_iess_egr", "Indica el VALOR DEL EGRESO DEL APORTE PERSONAL IESS", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_gas_persona_vivienda_ing", "Indica el VALOR DEL INGRESO DE LOS GASTOS PERSONALES POR VIVIENDA", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_gas_persona_vivienda_egr", "Indica el VALOR DEL EGRESO DE LOS GASTOS PERSONALES POR VIVIENDA", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_gas_persona_salud_ing", "Indica el VALOR DEL GASTO PERSONAL POR SALUD", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_gas_persona_salud_egr", "Indica el VALOR DEL GASTO PERSONAL POR SALUD", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_gas_persona_educa_ing", "Indica el VALOR DEL INGRESO DEL  GASTO PERSONAL POR EDUCACION", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_gas_persona_educa_egr", "Indica el VALOR DEL EGRESO DEL GASTO PERSONAL POR EDUCACION", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_gas_persona_alimenta_ing", "Indica el VALOR DEL INGRESO DEL  GASTO PERSONAL POR ALIMENTACION", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_gas_persona_alimenta_egr", "Indica el VALOR DEL EGRESO DEL GASTO PERSONAL POR ALIMENTACION", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_gas_persona_vestimenta_ing", "Indica el VALOR DEL INGRESO DEL  GASTO PERSONAL POR VESTIMENTA", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_gas_persona_vestimenta_egr", "Indica el VALOR DEL EGRESO DEL GASTO PERSONAL POR VESTIMENTA", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_baseimponible_gravada_ing", "Indica el VALOR DEL INGRESO DE LA BASE IMPONIBLE GRABADA", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_baseimponible_gravada_egr", "Indica el VALOR DEL EGRESO DE LA BASE IMPONIBLE GRABADA", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_impuesto_renta_causado_ing", "Indica el VALOR DEL INGRESO DEL IMPUESTO A LA RENTA GRAVADA", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_impuesto_renta_causado_egr", "Indica el VALOR DEL EGRESO DEL IMPUESTO A LA RENTA GRAVADA", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_impuesto_renta_asumidootr_ing", "Indica el VALOR DEL INGRESO DEL IMPUESTO A LA RENTA GRAVADA", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_impuesto_renta_asumidootr_egr", "Indica el VALOR DEL EGRESO DEL IMPUESTO A LA RENTA GRAVADA", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_impuesto_renta_asumidoest_ing", "Indica el VALOR DEL INGRESO DEL IMPUESTO A LA RENTA GRAVADA", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_impuesto_renta_asumidoest_egr", "Indica el VALOR DEL EGRESO DEL IMPUESTO A LA RENTA GRAVADA", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_impue_retenido_ing", "Indica el VALOR DEL INGRESO DEL IMPUESTO A LA RENTA GRAVADA", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_impue_retenido_egr", "Indica el VALOR DEL EGRESO DEL IMPUESTO A LA RENTA GRAVADA", "42","NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("4", "p_redep_codigo_establecimiento", "Define el codigo del establecimiento", "001"));
        lis_parametros.add(new Parametro("4", "p_redep_pais_tipo_residencia", "Define el numero del tipo de residencia", "943"));
        lis_parametros.add(new Parametro("4", "p_redep_aplica_convenio", "Define el numero del tipo de residencia", "943"));
        lis_parametros.add(new Parametro("4", "p_redep_pais_residencia", "Define el numero del tipo de residencia", "943"));
        lis_parametros.add(new Parametro("4", "p_redep_tipo_identi_discap", "Define el numero del tipo de documento de identidad de los discapcitados", "N"));
        lis_parametros.add(new Parametro("4", "p_redep_nro_docume_discap", "Define el numero del documento de identidad de los discapcitados", "999"));
        lis_parametros.add(new Parametro("4", "p_redep_tipo_sis_salario_neto", "Define el tipo de sistema de salario neto", "1"));
        lis_parametros.add(new Parametro("4", "p_rmu_gastos_deducibles", "Define el tipo de sistema de salario neto", "250.00"));
       
        //Calculo Impuesto  Renta
        
        lis_parametros.add(new Parametro("4", "p_redep_tipo_sis_salario_neto", "Define el tipo de sistema de salario neto", "1"));
        //SECUENCIAL
        lis_parametros.add(new Parametro("4", "p_sec_formulario107","Indica el modulo para generar el secuencial de las actas de bajas","1","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("4", "p_ruc_contador","Indica el numero de ruc del Contador","1714737663001"));
        lis_parametros.add(new Parametro("4", "p_empleados_salida_impuesto","Indica los empleados que no registran impuesto a la renta","2963"));
        

        /*
         * SBS =5
         */
        lis_parametros.add(new Parametro("5", "p_sbs_codigo_identidad", "Indica el codigo de identidad del periodo de catastro", "12345"));
        lis_parametros.add(new Parametro("5", "p_sbs_tipo_archivo_c11", "Indica el tipo de archivo C-11 para la SBS", "1","SBS_TIPO_ARCHIVO","IDE_SBTIA","DETALLE_SBTIA"));
        lis_parametros.add(new Parametro("5", "p_sbs_tipo_archivo_c21", "Indica el tipo de archivo C-21 para la SBS", "2","SBS_TIPO_ARCHIVO","IDE_SBTIA","DETALLE_SBTIA"));
        lis_parametros.add(new Parametro("5", "p_sbs_tipo_archivo_c23", "Indica el tipo de archivo C-23 para la SBS", "3","SBS_TIPO_ARCHIVO","IDE_SBTIA","DETALLE_SBTIA"));
        lis_parametros.add(new Parametro("5", "p_sbs_tipo_archivo_c41", "Indica el tipo de archivo C-41 para la SBS", "4","SBS_TIPO_ARCHIVO","IDE_SBTIA","DETALLE_SBTIA"));
        lis_parametros.add(new Parametro("5", "p_sbs_tipo_archivo_c10", "Indica el tipo de archivo C-10 para la SBS", "5","SBS_TIPO_ARCHIVO","IDE_SBTIA","DETALLE_SBTIA"));

        
        /*
         * EVALUACION = 6
         */
        lis_parametros.add(new Parametro("6", "p_factor_actividad_puesto", "Indica el Factor Evaluacion (Actividades del Puesto)", "4","EVL_FACTOR_EVALUACION","IDE_EVFAE","DETALLE_EVFAE"));
        lis_parametros.add(new Parametro("6", "p_competencias_gestion", "Indica el Factor Evaluacion (Competencias Gestion )", "2","EVL_FACTOR_EVALUACION","IDE_EVFAE","DETALLE_EVFAE"));
        lis_parametros.add(new Parametro("6", "p_competencias_instituciones", "Indica el Factor Evaluacion (Competencias Instituciones)", "3","EVL_FACTOR_EVALUACION","IDE_EVFAE","DETALLE_EVFAE"));
        lis_parametros.add(new Parametro("6", "p_competencias_tecnicas", "Indica el Factor Evaluacion (Competenciias Tecnicas)", "1","EVL_FACTOR_EVALUACION","IDE_EVFAE","DETALLE_EVFAE"));
        
        
        /*
         * SELECCION PERSONAL=7 
         **/
        lis_parametros.add(new Parametro("7", "p_tipo_asunto_evaluacion", "Indica el tipo asunto Evaluacion (Combo como parametros en el IN)", "1","SPR_TIPO_ASUNTO","IDE_SPTIA","DETALLE_SPTIA"));
        lis_parametros.add(new Parametro("7", "p_informacion_academica", "Indica informacion academica (Combo como parametros en el IN)", "1","SPR_FACTOR","IDE_SPFAC","DETALLE_SPFAC"));
        lis_parametros.add(new Parametro("7", "p_experiencia", "Indica la experiencias del personal (Combo como parametros en el IN)", "3","SPR_FACTOR","IDE_SPFAC","DETALLE_SPFAC"));
        lis_parametros.add(new Parametro("7", "p_competencia", "Indica las competencias del personal (Combo como parametros en el IN)", "5","SPR_FACTOR","IDE_SPFAC","DETALLE_SPFAC"));

       
        
        /*
         * SALUD = 10
         */
    //    lis_parametros.add(new Parametro("8", "p_detalle_matriz_riesgos", "Indica el Tipo de Riesgo(Actividades de la Matriz de Riesgo)", "1","SAO_CLASIFICACION_RIESGOS","IDE_SACLR","DETALLE_SACLR"));
        lis_parametros.add(new Parametro("10", "p_riesgos_mecanico", "Indica el Tipo de Riesgo(Actividades de la Matriz de Riesgo(Riesgo Mecanico))", "3","SAO_CLASIFICACION_RIESGOS","IDE_SACLR","DETALLE_SACLR"));
        lis_parametros.add(new Parametro("10", "p_riesgos_fisico", "Indica el Tipo de Riesgo(Actividades de la Matriz de Riesgo(Riesgo Fisico))", "4","SAO_CLASIFICACION_RIESGOS","IDE_SACLR","DETALLE_SACLR"));
        lis_parametros.add(new Parametro("10", "p_riesgos_biologico", "Indica el Tipo de Riesgo(Actividades de la fMatriz de Riesgo(Riesgo Biologico))", "5","SAO_CLASIFICACION_RIESGOS","IDE_SACLR","DETALLE_SACLR"));
        lis_parametros.add(new Parametro("10", "p_riesgos_quimico", "Indica el Tipo de Riesgo(Actividades de la Matriz de Riesgo(Riesgo Quimico))", "6","SAO_CLASIFICACION_RIESGOS","IDE_SACLR","DETALLE_SACLR"));
        lis_parametros.add(new Parametro("10", "p_riesgos_ergonomico", "Indica el Tipo de Riesgo(Actividades de la Matriz de Riesgo(Riesgo Ergonomico))", "7","SAO_CLASIFICACION_RIESGOS","IDE_SACLR","DETALLE_SACLR"));
        lis_parametros.add(new Parametro("10", "p_factor_psicosociales", "Indica el Tipo de Riesgo(Actividades de la Matriz de Riesgo(Factores Psicosociales))", "8","SAO_CLASIFICACION_RIESGOS","IDE_SACLR","DETALLE_SACLR"));
        //89
        
        /*
         * CONTROL DE ASISTENCIA =11
         * */
        lis_parametros.add(new Parametro("11", "p_asi_permiso_dias", "Indica el el numero de horas por permiso", "8"));
        lis_parametros.add(new Parametro("11", "p_motivo_hora_extra", "Indica el motivo de pedir horas extra, se lo utiliza en el portal", "16","ASI_MOTIVO","IDE_ASMOT","DETALLE_ASMOT"));
        lis_parametros.add(new Parametro("11", "p_motivos_horas_extras", "Indica los motivos de horas extras, se utiliza  cargar el combo de motivos", "2,17","ASI_MOTIVO","IDE_ASMOT","DETALLE_ASMOT"));
        lis_parametros.add(new Parametro("11", "p_asi_dias_anio", "Indica numero dias por anio", "360"));
        lis_parametros.add(new Parametro("11", "p_asi_dias_losep", "Indica el numero de dias asignados de vacacion por anio si pertenece a la losep", "30"));
        lis_parametros.add(new Parametro("11", "p_asi_dias_codigo", "Indica el numero de dias asignados de vacacion por anio si pertenece a codigo de trabajo", "15"));
        lis_parametros.add(new Parametro("11", "p_asi_dias_max_vacaciones_losep", "Indica el el numero maximo de dias de vaciones para losep", "60"));
        lis_parametros.add(new Parametro("11", "p_asi_dias_max_vacaciones_codigo_trabajo", "Indica el el numero maximo de dias de vaciones para codigo de trabajo", "45"));
       
        lis_parametros.add(new Parametro("11", "p_asi_dias_max_vacaciones_codigo_trabajo", "Indica el el numero maximo de dias de vaciones para codigo de trabajo", "45"));
        lis_parametros.add(new Parametro("11", "p_asi_mes_hora_extra", "Indica el mes disponible para generación de horas extra", "1,2,3,4,5"));
        lis_parametros.add(new Parametro("11", "p_asi_mes_editar_horario_tthh", "Indica los meses disponibles para la asignación y edición de horarios mensuales del personal", "2,3,4,5,6,7"));
        lis_parametros.add(new Parametro("11", "p_asi_mes_editar_horario_jefe_inmediato", "Indica los meses disponibles para la asignación y edición horarios mensuales del personal", "2,3,4"));
        lis_parametros.add(new Parametro("11", "p_asi_aprobador_jefe_inmediato", "Indica los posibles aprobadores de permisos", "7,6,4,2,8,19"));
        lis_parametros.add(new Parametro("11", "p_modulo_planificacion_horas", "Indica los posibles aprobadores de permisos", "59"));
        lis_parametros.add(new Parametro("11", "p_modulo_anticipos_dias_trabajados", "Indica los dias trabajados", "90"));
        lis_parametros.add(new Parametro("11", "p_gth_analista_tthh_vacaciones", "Indica el nombre del analista de talento humano a cargo de vacaciones", "Psc. Ana Hurtado"));
        lis_parametros.add(new Parametro("11", "p_vacacion_cambio_contrato_empleados", "Indica los empleados a los cuales se les acumula vacaciones", "555"));
        lis_parametros.add(new Parametro("11", "p_anio_asignacion_horario_jefe_inmediato", "Indica los ide_anio", "12"));
        lis_parametros.add(new Parametro("11", "p_anio_asignacion_horario_administrador", "Indica los ide_anio", "11,12"));
        
        
        lis_parametros.add(new Parametro("11", "p_seguimiento_mes", "Indica los meses disponibles para obtener el reporte mensual de seguimiento", "12,1,2"));
        lis_parametros.add(new Parametro("11", "p_seguimiento_anio", "Indica los ide_anio", "15,16"));

        
        //Fecha limite para generacion de ingreso gastos 
        lis_parametros.add(new Parametro("11", "p_fehca_limite_gastos_personales", "Indica la fecha limite para el registro de gastos personales", "2019-01-19"));
        lis_parametros.add(new Parametro("11", "p_dias_limite_ingreso_permiso", "Indica el numero de dias  limite para el ingreso del permiso", "3"));
        lis_parametros.add(new Parametro("11", "p_horas_limite_consulta_medica", "Indica el numero de horas  limite de tiempo del permiso", "3"));
        lis_parametros.add(new Parametro("11", "p_fecha_aprobacion_permisos_resagados", "Indica la fecha inicio de permiso", "2019-06-01"));
        lis_parametros.add(new Parametro("11", "p_empleado_ingresa_permisos", "Indica el ide de la persona habilitada  a ingresar los permisos ", "392","GTH_EMPLEADO","IDE_GTEMP","CONCAT(APELLIDO_PATERNO_GTEMP,APELLIDO_MATERNO_GTEMP,PRIMER_NOMBRE_GTEMP,SEGUNDO_NOMBRE_GTEMP)"));
        lis_parametros.add(new Parametro("11", "p_dias_max_permitidos_vacacion", "Indica el # de dias por vacacion ", "60"));

 
        lis_parametros.add(new Parametro("11", "p_fecha_ini", "Indica fecha_inicio de busqueda de permisos sin  ", "2018-01-01"));
        lis_parametros.add(new Parametro("11", "p_fecha_fin", "Indica fecha_inicio de busqueda de permisos sin  ", "2020-12-31"));
        lis_parametros.add(new Parametro("11", "p_numero_dias_justificativo_hasta", "Dias de busqueda de permisos en reporte marcaciones", "30"));
        lis_parametros.add(new Parametro("11", "p_numero_dias_justificativo_desde", "Dias de busqueda de permisos en reporte marcaciones", "-30"));
        lis_parametros.add(new Parametro("11", "p_dias_pendientes_adicionales", "Dias extra ", "-15.00"));

        lis_parametros.add(new Parametro("11", "p_dias_ingreso_permiso_personal", "Dias para registro de permiso personal ", "1"));

        lis_parametros.add(new Parametro("11", "p_dias_fin_registro_asignacion_mensual", "Dias para registro de permiso personal ", "20"));

        
        //EMPLEADOS CON DIAS EXTRAS A VACACIÓN 
        String a="";
        lis_parametros.add(new Parametro("11", "p_asi_empleados_codigo_trabajo_dia_extra", "Indica los ides de los empleados con dias extra a vacacion ", "73","GTH_EMPLEADO","IDE_GTEMP","CONCAT(APELLIDO_PATERNO_GTEMP,APELLIDO_MATERNO_GTEMP,PRIMER_NOMBRE_GTEMP,SEGUNDO_NOMBRE_GTEMP)"));
        lis_parametros.add(new Parametro("2", "p_especialiista_tthh", "Indica el nombre del especialista de talento humano", "Psi.Carlos Villegas"));
        lis_parametros.add(new Parametro("2", "p_tipo_permisos_covid", "Indica los ides de los tipos de permisos", "2,4,6"));
        lis_parametros.add(new Parametro("11", "p_anio_roles", "Indica los ides de los tipos de permisos", "13,14"));
        //Parametro de tiempo para registro de nueva marcacion
        lis_parametros.add(new Parametro("11", "p_tiempo_limite_marcacion", "Indica el tiempo limite para la siguiente marcacion ", "15"));
        lis_parametros.add(new Parametro("11", "p_matriz_marcaciones_biometrico", "Indica las marcaciones por biometrico que se desplegaran ", "15"));
        //Firmas de Asignacion de Horarios
        lis_parametros.add(new Parametro("11", "p_administrador_horarios_empleados", "Indica el usuario administrador para consulta de horarios ", "2522"));
        lis_parametros.add(new Parametro("11", "p_biometrico_teletrabajo", "Indica el usuario administrador para consulta de horarios ", "15"));
        lis_parametros.add(new Parametro("11", "p_licencia_maternidad", "Indica el el codigo de permiso por licencia de maternidad ", "29"));


        lis_parametros.add(new Parametro("11", "p_hora_inicio_laboral", "Indica hora  inicio de la jornada laboral", "8"));
        lis_parametros.add(new Parametro("11", "p_hora_fin_laboral", "Indica hora fin de la jornada laboral", "17"));

        lis_parametros.add(new Parametro("11", "p_dias_max_permitidos", "Indica hora fin de la jornada laboral", "-15"));
		
        lis_parametros.add(new Parametro("11", "p_dias_max_ingreso_indicadores", "Indica fecha inicio de habilitacion para el registro de indicadores", "-2"));
        lis_parametros.add(new Parametro("11", "p_dias_max_fin_indicadores", "Indica fecha fin de habilitacion para el registro de indicadores", "-4"));
        
        lis_parametros.add(new Parametro("11", "p_permisos_doc_enfermedad", "Tipo permiso por enfermedad", "6"));
        lis_parametros.add(new Parametro("11", "p_encargado_revision_permisos_enfermedad", "Id empleado encargado de revisar permisos por enfermedad", "1203"));
        lis_parametros.add(new Parametro("11", "p_encargado_trabajador_social", "Id empleado encargado de revisar permisos por enfermedad", "1205"));
    
        
        
        lis_parametros.add(new Parametro("11", "p_informe_seguimiento_recomendacion", "Id empleado encargado de revisar permisos por enfermedad", "49,50,51"));
        lis_parametros.add(new Parametro("11", "p_empleado_prueba_auxiliar", "Id empleado auxiliaar_vacaciones", "81"));
        lis_parametros.add(new Parametro("11", "p_notificacion_cssa", "Notificacion CSSA", "ssa@emgirs.gob.ec"));
       
        
		/*
         * GENERAL MODULO = 0
         * */
		lis_parametros.add(new Parametro("0", "p_factor_multiplicador_renta_discapacitados","Indica el factor a multiplicar si el empleado es discapacitado o tercera edad para el calculo de la renta","2"));
		lis_parametros.add(new Parametro("0", "p_num_veces_vmgd","Indica el numero de veces que se aplica a la fraccion basica desgravada para el maximo valor para gastos deducibles ","1.3"));
		lis_parametros.add(new Parametro("0", "p_porcentaje_tot_ing_grab","El porcentaje que se aplica al total de ingresos gravados del contribuyente para la deduccion total por gastos deducibles","50"));
		lis_parametros.add(new Parametro("0", "p_canasta_familiar","Indica el numero de veces que se aplica a la fraccion basica desgravada para el maximo valor para gastos deducibles ","719.65"));
		lis_parametros.add(new Parametro("0", "p_num_veces_canasta_familiar","Indica el numero de veces que se aplica a la fraccion basica desgravada para el maximo valor para gastos deducibles ","7"));

		
		lis_parametros.add(new Parametro("0", "p_CMC_MODULO_MALLA","Lo que vamos a almacenar en el campo cmc_modulo de la tabla mal_cabecera","NO"));
		lis_parametros.add(new Parametro("0", "p_CMC_PROCESO_MALLA","Lo que vamos a almacenar en el campo cmc_proceso de la tabla mal_cabecera","CON"));
		lis_parametros.add(new Parametro("0", "p_CMC_OPERADOR_MALLA","Lo que vamos a almacenar en el campo cmc_operador de la tabla mal_cabecera","CON"));
		lis_parametros.add(new Parametro("0", "p_CMC_ESTADO_MALLA","Lo que vamos a almacenar en el campo cmc_estado de la tabla mal_cabecera","PRO"));
		
		
        lis_parametros.add(new Parametro("0", "p_ubicacion_geografica_core","Para la ubicacion geografica en el core contabilidad","01"));
		
        lis_parametros.add(new Parametro("0", "p_minimo_dias_derecho_fondos","Indica el numero de dias minimo para tener derecho a fondos de reserva (a partir del 13avo mes(390 dias))","390"));
        lis_parametros.add(new Parametro("0", "p_amortizacion_cada","Indica que la amortizacion se la realizara cada 30 dias","30"));
        lis_parametros.add(new Parametro("0", "p_tasa_interes","Indica la tasa de interes anticipo que se la realizara","0"));
        lis_parametros.add(new Parametro("0", "p_tasa_interes_efectiva","Indica la tasa  efectiva anticipo de interes que se la realizara","0"));
        /*
         *  CONVENIOS = 12
         * */
        
        lis_parametros.add(new Parametro("12", "p_modulo_convenio","Indica el modulo que pertenece a convenios","1","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("12", "p_funcionario_convenio","Indica el tipo de persona para convenios = funcionario","3","gen_tipo_persona","ide_getip","detalle_getip"));
        lis_parametros.add(new Parametro("12", "p_particular_convenio","Indica el tipo de persona para convenios = particular","1","gen_tipo_persona","ide_getip","detalle_getip"));
        /*
         *  bodega = 13
         * */
        lis_parametros.add(new Parametro("13", "p_grupo_material","Permite mostras el catalogo de matarieales de cuardo al grupo","1","bodt_tipo_producto","ide_botip","detalle_botip"));
        lis_parametros.add(new Parametro("13", "p_modulo_sec_bod_ingresos","Permite seleccionar el modulo de bodega ingreso de inventarios para generar el numero secuencial en ingresos","17","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("13", "p_modulo_sec_bod_ingresos_existencia","Permite seleccionar el modulo de bodega ingreso de inventarios para generar el numero secuencial en ingresos","17","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("13", "p_modulo_sec_bodega_ingreso_existencia","Permite seleccionar el modulo de bodega ingreso de inventarios para generar el numero secuencial en ingresos","63","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("13", "p_modulo_sec_bodega_egreso_existencia","Permite seleccionar el modulo de bodega ingreso de inventarios para generar el numero secuencial en egresos","64","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("13", "p_modulo_sec_bodega_transferencia_existencia","Permite seleccionar el modulo de bodega ingreso de inventarios para generar el numero secuencial en transferencia","65","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("13", "p_modulo_sec_bodega_transferencia_existencia","Permite seleccionar el modulo de bodega ingreso de inventarios para generar el numero secuencial en transferencia","65","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("13", "p_modulo_bodega_valor_existencias_inc_iva","Determina si el valor de la existencia debe ser tomado del costo unitario o del costo unitario incluido iva.","0","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("13", "p_modulo_sec_bodega_egreso_existencia_por_baja","Determina si el valor de la existencia debe ser tomado del costo unitario o del costo unitario incluido iva.","81","gen_modulo","ide_gemod","detalle_gemod"));

        /*
         *  facturacion = 14
         * */
        lis_parametros.add(new Parametro("14", "p_valor_interes_mora_nd", "Indica  el valor para el calculo de interes de mora en las notas de debito", "9.33"));
        lis_parametros.add(new Parametro("14", "p_dias_calculo_interes_mora_nd", "Indica  el numero de dias vigentes de una factura como pazo para su cancelacion a partir de ese dia se genera una nota de debito", "10"));
        lis_parametros.add(new Parametro("14", "p_fac_nombre_archivo_banco", "Indica el nombre del archivo con el cual se descargara para enviar al banco", "BancoPacifico"));
        lis_parametros.add(new Parametro("14", "p_fac_localidad_archivo_banco", "Indica la localidad para generar el archivo de envio al banco", "5"));
        lis_parametros.add(new Parametro("14", "p_fac_transaccion_archivo_banco", "Indica la transaccion para generar el archivo de envio al banco", "OCP"));
        lis_parametros.add(new Parametro("14", "p_fac_codservicio_archivo_banco", "Indica el codigo de servicio para generar el archivo de envio al banco", "ZG"));
        lis_parametros.add(new Parametro("14", "p_fac_referencia_archivo_banco", "Indica la referencia para generar el archivo de envio al banco", "REFERENCIA"));
        lis_parametros.add(new Parametro("14", "p_fac_formapago_archivo_banco", "Indica la forma de pago de servicio para generar el archivo de envio al banco", "RE"));
        lis_parametros.add(new Parametro("14", "p_fac_moneda_archivo_banco", "Indica la moneda para generar el archivo de envio al banco", "USD"));
        lis_parametros.add(new Parametro("14", "p_modulo_facturacion","Indica el modulo que pertenece a facturacion","14","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("14", "p_notadecredito_anulado","Indica el estado anulado de la nota de credito","14","cont_estado","ide_coest","detalle_coest"));
        lis_parametros.add(new Parametro("14", "p_notadecredito_emitido","Indica el estado emitido de la nota de credito","14","cont_estado","ide_coest","detalle_coest"));
        lis_parametros.add(new Parametro("14", "p_factura_anulado","Indica el estado anulado de la factura","14","cont_estado","ide_coest","detalle_coest"));
        lis_parametros.add(new Parametro("14", "p_factura_emitido","Indica el estado emitido de la factura","14","cont_estado","ide_coest","detalle_coest"));
        lis_parametros.add(new Parametro("14", "p_factura_pagado","Indica el estado pagado de la factura","14","cont_estado","ide_coest","detalle_coest"));
        lis_parametros.add(new Parametro("14", "p_estado_conciliacion_bancaria","Indica el estado conciliacion del archivo bancario","14","cont_estado","ide_coest","detalle_coest"));
        lis_parametros.add(new Parametro("14", "p_estado_nota_debito_factura","Indica el estado que cambia la factura cuando se realiza la nota de debito","14","cont_estado","ide_coest","detalle_coest"));
        lis_parametros.add(new Parametro("14", "p_modulo_nota_debito","Indica el modulo que pertenece a nota de debito","14","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("14", "p_modulo_nota_credito","Indica el modulo que pertenece a nota de credito","14","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("14", "p_retencion_bienes_10","Indica las retenciones del 10% por bienes para el ATS","14","tes_impuesto","ide_teimp","detalle_teimp"));
        lis_parametros.add(new Parametro("14", "p_retencion_servicios_20","Indica las retenciones del 20% por bienes para el ATS","14","tes_impuesto","ide_teimp","detalle_teimp"));
        lis_parametros.add(new Parametro("14", "p_retencion_bienes_30","Indica las retenciones del 30% por bienes para el ATS","14","tes_impuesto","ide_teimp","detalle_teimp"));
        lis_parametros.add(new Parametro("14", "p_retencion_servicios_70","Indica las retenciones del 70% por bienes para el ATS","14","tes_impuesto","ide_teimp","detalle_teimp"));
        lis_parametros.add(new Parametro("14", "p_retencion_iva_50","Indica las retenciones del 50% por iva para el ATS","14","tes_impuesto","ide_teimp","detalle_teimp"));
        lis_parametros.add(new Parametro("14", "p_retencion_iva_100","Indica las retenciones del 100% por bienes para el ATS","14","tes_impuesto","ide_teimp","detalle_teimp"));
        lis_parametros.add(new Parametro("14", "p_autorizacion_retencionsri_ats","Indica el numero de autorizacion de retencion por el sri","144789899"));
        lis_parametros.add(new Parametro("14", "p_factura_abonada","Indica el estado abonada de la factura","30","cont_estado","ide_coest","detalle_coest"));
        lis_parametros.add(new Parametro("14", "p_notadecredito_estados", "Indica los posibles de estados de facturas a listar en las notas de credito", "1,2,24"));

        /*
         *  ADQUISISCIONES = 15
         * */
        
        lis_parametros.add(new Parametro("15", "p_modulo_adquisicion","Indica el modulo que pertenece a adquisiones","9","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("15", "p_estado_modulo_compra","Indica el estado inicial de la solicitud de compra al insertar un nuevo registro","10","cont_estado","ide_coest", "detalle_coest"));
        lis_parametros.add(new Parametro("15", "p_cotizacion_adquisicion","Indica el estado de la cotizacion ","11","cont_estado","ide_coest", "detalle_coest"));
        lis_parametros.add(new Parametro("15", "p_certificacion_adquisicion","Indica el estado de la certificacion de  adquisicion ","12","cont_estado","ide_coest", "detalle_coest"));
        lis_parametros.add(new Parametro("15", "p_solicitud_pago_adquisicion","Indica el estado de la solicitud pago adquisicion  ","13","cont_estado","ide_coest", "detalle_coest"));
        lis_parametros.add(new Parametro("15", "p_solicitud_por_pagar","Indica el estado de la solicitud de compra que esta por pagarse","13","cont_estado","ide_coest", "detalle_coest"));
        lis_parametros.add(new Parametro("15", "p_empleado_adjudicador","Indica el listado de los empleados adjudicadores  ","13","gth_empleado","ide_gtemp", "documento_identidad_gtemp,apellido_paterno_gtemp, apellido_materno_gtemp, primer_nombre_gtemp,segundo_nombre_gtemp"));
        lis_parametros.add(new Parametro("15", "p_empleado_adjudica_infima","Permite filtrar el empleado responsable de adjudicar Compras por infimas Cuantìas (Coordinador Administrativo)","13","gth_empleado","ide_gtemp", "documento_identidad_gtemp,apellido_paterno_gtemp, apellido_materno_gtemp, primer_nombre_gtemp,segundo_nombre_gtemp"));
        lis_parametros.add(new Parametro("15", "p_empleado_adjudica_todos","Permite filtrar el empleado responsable de adjudicar Compras que no sean por infimas Cuantìas (Gerente General)","13","gth_empleado","ide_gtemp", "documento_identidad_gtemp,apellido_paterno_gtemp, apellido_materno_gtemp, primer_nombre_gtemp,segundo_nombre_gtemp"));
        lis_parametros.add(new Parametro("15", "p_tipo_contrata_infima","Define el tipo de contrataciòn que es de tipo infima cuantìa","3","adq_tipo_contratacion","ide_adtic", "detalle_adtic"));
        //lis_parametros.add(new Parametro("15", "p_estado_pendiente_bodega","","3","adq_tipo_contratacion","ide_adtic", "detalle_adtic"));
        //lis_parametros.add(new Parametro("15", "p_estado_recepcion_parcial","","3","adq_tipo_contratacion","ide_adtic", "detalle_adtic"));

 
        /*
         * PRESUPUESTO
        */
        lis_parametros.add(new Parametro("16", "p_modulo_presupuesto","Indica el modulo que pertenece a presupuesto","10","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("16", "p_modulo_tramite","Indica el modulo que pertenece el tramite ","11","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("16", "p_modulo_tramite_alterno","Indica el modulo que pertenece el tramite alterno ","15","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("16", "p_modulo_empleado","Indica el modulo que pertenece el empleado ","8","cont_parametros_general","ide_copag","detalle_copag"));
        lis_parametros.add(new Parametro("16", "p_modulo_no_adjudicado","Indica el modulo que pertenece al no adjudicado ","10","cont_parametros_general","ide_copag","detalle_copag"));
        lis_parametros.add(new Parametro("16", "p_modulo_proveedor","Indica el modulo que pertenece el proveedor ","9","cont_parametros_general","ide_copag","detalle_copag"));
        lis_parametros.add(new Parametro("16", "p_modulo_estado_comprometido","Indica el modulo que pertenece el comprometido ","21","cont_estado","ide_coest","detalle_coest"));
        lis_parametros.add(new Parametro("16", "p_modulo_estado_comprometido","Indica el modulo que pertenece el comprometido ","21","cont_estado","ide_coest","detalle_coest"));
        lis_parametros.add(new Parametro("16", "p_sub_actividad","Indica el nombre sub actividad para ","5","pre_nivel_funcion_programa","ide_prnfp","detalle_prnfp"));
        lis_parametros.add(new Parametro("16", "p_mov_devengado","Indica el tipo de movimiento presupuestario devengado","5","pre_movimiento_presupuestario","ide_prmop","detalle_prmop"));
        lis_parametros.add(new Parametro("16", "p_cuenta_banco","Indica el codigo de cuenta contable de bancos para generar el asiento contable","5","cont_catalogo_cuenta","ide_cocac","cue_codigo_cocac,cue_descripcion_cocac"));
        lis_parametros.add(new Parametro("16", "p_lugar_ejecuta_banco","Indica el si la cuenta cuanta bancos donde se ejcuta debe o haber","2","gen_lugar_aplica","ide_gelua","detalle_gelua"));
        lis_parametros.add(new Parametro("16", "p_cuenta_bancos","Indica el codigo de las cuentas contables de bancos para cuadrar el flujo del efectivo","20,29,4163,4177","cont_catalogo_cuenta","ide_cocac","cue_codigo_cocac,cue_descripcion_cocac"));
        lis_parametros.add(new Parametro("16", "p_lugar_ejecuta_banco","Indica el si la cuenta cuanta bancos donde se ejcuta debe o haber","2","gen_lugar_aplica","ide_gelua","detalle_gelua"));
        lis_parametros.add(new Parametro("16", "p_cuenta_iva","Indica el codigo de cuenta contable de iva para generar el asiento contable","5","cont_catalogo_cuenta","ide_cocac","cue_codigo_cocac,cue_descripcion_cocac"));
        lis_parametros.add(new Parametro("16", "p_lugar_ejecuta_iva","Indica el si la cuenta cuanta iva donde se ejcuta debe o haber","2","gen_lugar_aplica","ide_gelua","detalle_gelua"));
        lis_parametros.add(new Parametro("16", "p_cuenta_iva_cierra","Indica el codigo de cuenta contable de iva para generar el asiento contable","5","cont_catalogo_cuenta","ide_cocac","cue_codigo_cocac,cue_descripcion_cocac"));
        lis_parametros.add(new Parametro("16", "p_lugar_ejecuta_iva_cierra","Indica el si la cuenta cuanta iva donde se ejcuta debe o haber","2","gen_lugar_aplica","ide_gelua","detalle_gelua"));
        lis_parametros.add(new Parametro("16", "p_modulo_secuencialproyecto","Indica el modulo para generar el secuencial de proyecto","21","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("16", "p_modulo_secuencialprograma","Indica el modulo para generar el secuencial de programa","21","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("16", "p_modulo_secuencialproducto","Indica el modulo para generar el secuencial de producto","21","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("16", "p_modulo_secuencialfase","Indica el modulo para generar el secuencial de fase","21","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("16", "p_modulo_secuencialproducto_mc","Indica el modulo para generar el secuencial del producto mi ciudad","21","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("16", "p_modulo_secuencialactividad_mc","Indica el modulo para generar el secuencial de la actividad(fase) mi ciudad","21","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("16", "p_modulo_secuencialsubactiv","Indica el modulo para generar el secuencial de sub actividad","21","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("16", "p_proyecto","Indica el proyecto","1","pre_nivel_funcion_programa","ide_prnfp","detalle_prnfp"));
        lis_parametros.add(new Parametro("16", "p_programa","Indica el programa","2","pre_nivel_funcion_programa","ide_prnfp","detalle_prnfp"));
        lis_parametros.add(new Parametro("16", "p_producto","Indica el producto","3","pre_nivel_funcion_programa","ide_prnfp","detalle_prnfp"));
        lis_parametros.add(new Parametro("16", "p_fase","Indica el la fase","4","pre_nivel_funcion_programa","ide_prnfp","detalle_prnfp"));
        lis_parametros.add(new Parametro("16", "p_producto_mc","Indica el producto mi ciudad","3","pre_nivel_funcion_programa","ide_prnfp","detalle_prnfp"));
        lis_parametros.add(new Parametro("16", "p_actividad_mc","Indica el la fase(actividad) mi ciudad","4","pre_nivel_funcion_programa","ide_prnfp","detalle_prnfp"));
        lis_parametros.add(new Parametro("16", "p_modulo_secuencialcertificacion","Indica el modulo para generar el secuencial de las certificaciones","21","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("16", "p_modulo_secuencialcompromiso","Indica el modulo para generar el secuencial de los compromisos","21","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("16", "p_modulo_secuencialliquidacion","Indica el modulo para generar el secuencial de las liquidaciones","36","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("16", "p_modulo_secuencialliberacionComp","Indica el modulo para generar el secuencial de la liberacion compromiso","36","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("16", "p_modulo_municipio","Indica el codigo de la fuente de financiamiento municipio","1","pre_fuente_financiamiento","ide_prfuf","detalle_prfuf"));
        lis_parametros.add(new Parametro("16", "p_modulo_hospitalario","Indica el codigo de la fuente de financiamiento hospitalario","1","pre_fuente_financiamiento","ide_prfuf","detalle_prfuf"));
        lis_parametros.add(new Parametro("16", "p_modulo_tasas_recoleccion","Indica el codigo de la fuente de financiamiento tasa de recolección","1","pre_fuente_financiamiento","ide_prfuf","detalle_prfuf"));
        lis_parametros.add(new Parametro("16", "p_modulo_ruminahui","Indica el codigo de la fuente de financiamiento ruminahui","1","pre_fuente_financiamiento","ide_prfuf","detalle_prfuf"));
        lis_parametros.add(new Parametro("16", "p_modulo_escombreras","Indica el codigo de la fuente de financiamiento escombreras","1","pre_fuente_financiamiento","ide_prfuf","detalle_prfuf"));
        lis_parametros.add(new Parametro("16", "p_modulo_metro_quito","Indica el codigo de la fuente de financiamiento metro quito","1","pre_fuente_financiamiento","ide_prfuf","detalle_prfuf"));
        lis_parametros.add(new Parametro("16", "p_modulo_convenios","Indica el codigo de la fuente de financiamiento convenios","1","pre_fuente_financiamiento","ide_prfuf","detalle_prfuf"));
        lis_parametros.add(new Parametro("16", "p_modulo_otros_gestores","Indica el codigo de la fuente de financiamiento otros gestores","1","pre_fuente_financiamiento","ide_prfuf","detalle_prfuf"));
        lis_parametros.add(new Parametro("16", "p_modulo_tasas_anticpipos_prov_anteriores","Indica el codigo de la fuente de financiamiento anticipo provicionales anteriores","1","pre_fuente_financiamiento","ide_prfuf","detalle_prfuf"));
        lis_parametros.add(new Parametro("16", "p_modulo_saldos_bancos","Indica el codigo de la fuente de financiamiento saldo bancos","1","pre_fuente_financiamiento","ide_prfuf","detalle_prfuf"));
        lis_parametros.add(new Parametro("16", "p_modulo_comercializacion_rsu","Indica el codigo de la fuente de financiamiento comercializacion rsu","1","pre_fuente_financiamiento","ide_prfuf","detalle_prfuf"));
        lis_parametros.add(new Parametro("16", "p_modulo_tasas_ruminahui_diciembre","Indica el codigo de la fuente de financiamiento tasas ruminahui","1","pre_fuente_financiamiento","ide_prfuf","detalle_prfuf"));
        lis_parametros.add(new Parametro("16", "p_modulo_tasas_recoleccion_diciembre","Indica el codigo de la fuente de financiamiento recoleccion","1","pre_fuente_financiamiento","ide_prfuf","detalle_prfuf"));
        lis_parametros.add(new Parametro("16", "p_modulo_cuentas_por_cobrar_hosp","Indica el codigo de la fuente de financiamiento cxc hospitalarios","1","pre_fuente_financiamiento","ide_prfuf","detalle_prfuf"));
        lis_parametros.add(new Parametro("16", "p_modulo_anticipo_contractual","Indica el codigo de la fuente de financiamiento anticipo contractual","1","pre_fuente_financiamiento","ide_prfuf","detalle_prfuf"));
        lis_parametros.add(new Parametro("16", "p_modulo_secuencialcert_futura","Indica el modulo para generar el secuencial de las certificaciones futuras","55","gen_modulo","ide_gemod","detalle_gemod"));

        /*
         *  Contabilidad = 17
         * */
        lis_parametros.add(new Parametro("17", "p_tipo_asiento_inicial", "Indica  el codigo del tipo de asiento inicial", "4","cont_tipo_asiento","ide_cotia","detalle_cotia"));
        lis_parametros.add(new Parametro("17", "p_cuenta_banco_central", "Indica  el codigo de cuenta del banco central", "17","cont_catalogo_cuenta","ide_cocac","cue_codigo_cocac"));
        lis_parametros.add(new Parametro("17", "p_modulo_contabilidad", "Indica  el moudlo contable", "17","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("17", "p_nombre_contador", "Indica elnombre del Contador general", "Ing. Juan Carlos Flores"));
        lis_parametros.add(new Parametro("17", "p_nombre_coordinador_fin", "Indica el nombre del Coordinador Financiero", "Ing. Juan Flores"));
        lis_parametros.add(new Parametro("17", "p_pie_coordinador_fin", "Indica el nombre del pie de firma del Coordinador Financiero", "COORDINADOR FINANCIERO"));
        lis_parametros.add(new Parametro("17", "p_nombre_jefe_presupuesto", "Indica el nombre del Jefe de Presupuesto", "Ing. Salasar Mishell"));
        lis_parametros.add(new Parametro("17", "p_anio_vigente", "Indica el año vigente","8","gen_anio","ide_geani", "detalle_geani"));
        lis_parametros.add(new Parametro("17", "p_modulo_ingresos","Indica el modulo de Ingresos", "17","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("17", "p_cuenta_saldo_inicial","las cuentas bancos que contemplan los saldos iniciales para el flujo de; efectivo", "1","cont_catalogo_cuenta","ide_cocac","cue_descripcion_cocac"));
        lis_parametros.add(new Parametro("17", "p_nombre_gerente", "Indica el nombre del gerente general", "Ing. Victor CAldas"));
        lis_parametros.add(new Parametro("17", "p_cargo_gerente", "Indica el cargo del gerente general", "Gerente Genreal"));
        lis_parametros.add(new Parametro("17", "p_modulo_anticipos","Indica el modulo que pertenece los anticipos","53","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("17", "p_pie_especialista_pres", "Indica el nombre del pie de firma del Presupuesto", "ESPECIALISTA DE PRESUPUESTO"));
        lis_parametros.add(new Parametro("17", "p_modulo_mayores_emp","Indica el modulo para obtener los empleados responsables","80","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("17", "p_pie_contador", "Indica el nombre de pie de firma del Contador general", "CONTADORA GENERAL"));
        /*
         * TESORERIA=18
         * */
        lis_parametros.add(new Parametro("18", "p_tes_impuesto_renta","Indica el tipo impuesto impuesto a la renta","1","tes_tipo_impuesto","ide_tetii","detalle_tetii"));
        lis_parametros.add(new Parametro("18", "p_tes_impuesto_iva","Indica el tipo impuesto impuesto iva","2","tes_tipo_impuesto","ide_tetii","detalle_tetii"));
        lis_parametros.add(new Parametro("18", "p_tipo_mov_facturacion","Indica el tipo de movimiento a ser utilizado al momento de generar el asiento contable de facturacion","2","cont_tipo_movimiento","ide_cotim","detalle_cotim"));
        lis_parametros.add(new Parametro("18", "p_valor_devenga", "Este campo indica el nombre del campo que se devenga de la tabla tes_comprobante_pago", "valor_pago_tecpo"));
        lis_parametros.add(new Parametro("18", "p_modulo_comprobante_pago", "Indica  el modulo comprobantes de pago", "18","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("18", "p_asiento_anticipo", "Indica  el asiento contable de tipo anticipo", "10","cont_nombre_asiento_contable","ide_conac","detalle_conac"));
        lis_parametros.add(new Parametro("18", "p_asiento_otros_anticipos", "Indica  el asiento contable de tipo anticipo", "10","cont_nombre_asiento_contable","ide_conac","detalle_conac"));
        lis_parametros.add(new Parametro("18", "p_estado_devengado", "Indica  el estado devengado del asiento", "2","cont_estado","ide_coest","detalle_coest"));
        lis_parametros.add(new Parametro("18", "p_tipos_iva", "Indica  los tipos de IVA existentes", "0-0% IVA;0.12-12% IVA;0.14-14% IVA","","",""));

        lis_parametros.add(new Parametro("18", "p_tipo_comprobante", "Indica el tipo de comprobante", "FAC-FACTURA;OTR-OTROS","","",""));
        lis_parametros.add(new Parametro("18", "p_modulo_secuencial_recaudacion","Indica el modulo para generar el secuencial de las recaudaciones","36","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("18", "p_modulo_secuencial_convenios","Indica el modulo para generar el secuencial de los convenios de pago","36","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("18", "p_modulo_secuencial_titulo","Indica el modulo para generar el secuencial de los titulos de credito","36","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("18", "p_modulo_secuencial_polizas","Indica el modulo para generar el secuencial de las polizas","36","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("18", "p_modulo_secuencial_cierre_caja","Indica el modulo para generar el secuencial de los cierres de caja","36","gen_modulo","ide_gemod","detalle_gemod"));
        
        lis_parametros.add(new Parametro("18", "p_tipos_cta_tran", "Indica  las cuentas de transferencia para los pagos SPI", "3972,3973","","",""));
        lis_parametros.add(new Parametro("18", "p_modulo_tesoreria_emp","Indica el modulo para obtener los empleados responsables","79","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("18", "p_nombre_tesorero", "Indica el nombre del Tesorero", "Ing. Jhoanna Robalino"));
        lis_parametros.add(new Parametro("18", "p_pie_tesorero", "Indica el nombre del pie de firma del Tesorero", "TESORERA GENERAL"));
        /*
         * ACTIVOS FIJOS =19
         * */
        lis_parametros.add(new Parametro("19", "p_base_legal_entregarecep","Indica la base legal para imprimir en el acta de Entrega Recepcion de Activos Fijos","1","gen_base_legal","ide_gebal","detalle_gebal"));
        lis_parametros.add(new Parametro("19", "p_base_legal_constafisica","Indica la base legal para imprimir en el acta de Constatacion Fisica","1","gen_base_legal","ide_gebal","detalle_gebal"));
        lis_parametros.add(new Parametro("19", "p_base_legal_cambiocustod","Indica la base legal para imprimir en el acta de Cambio de Custodio","1","gen_base_legal","ide_gebal","detalle_gebal"));
        lis_parametros.add(new Parametro("19", "p_estado_baja_activo","Indica el estado dado de baja del activo o bien","2","afi_actividad","ide_afacd","detalle_afacd"));
        lis_parametros.add(new Parametro("19", "p_modulo_secuencialActaCfisica","Indica el modulo para generar el secuencial de las actas de constatacion fisica","40","gen_modulo","ide_gemod","detalle_gemod"));

        lis_parametros.add(new Parametro("19", "p_sec_acta_entrega_recepcion","Indica el modulo para generar el secuencial de las actas de constatacion fisica","1","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("19", "p_sec_acta_traspaso","Indica el modulo para generar el secuencial de las actas de traspasos","1","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("19", "p_sec_acta_bajas","Indica el modulo para generar el secuencial de las actas de bajas","1","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("19", "p_sec_acta_bajas_externo","Indica el modulo para generar el secuencial de las actas de bajas","1","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("19", "p_sec_acta_comodato_externo","Indica el modulo para generar el secuencial de las actas de comodato","1","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("19", "p_sec_acta_bajas_donacion","Indica el modulo para generar el secuencial de las actas de bajas","1","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("19", "p_sec_acta_prestamo","Indica el modulo para generar el secuencial de las actas de prestamo","1","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("19", "p_sec_acta_devolucion","Indica el modulo para generar el secuencial de las actas de devolucion","1","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("19", "p_sec_acta_constatacion","Indica el modulo para generar el secuencial de las actas de constatacion fisica","1","gen_modulo","ide_gemod","detalle_gemod"));
		lis_parametros.add(new Parametro("19", "p_sec_acta_entrega_recepcion_existencia", "Indica el modulo para generar el secuencial de las actas de entrega recepcion de existencias", "1", "gen_modulo", "ide_gemod", "detalle_gemod"));
		lis_parametros.add(new Parametro("19", "p_sec_acta_baja_existencia", "Indica el modulo para generar el secuencial de las actas de baja de existencias", "1", "gen_modulo", "ide_gemod", "detalle_gemod"));
		lis_parametros.add(new Parametro("19", "p_item_presupuestarios_existencias", "Lista de partidas que deben presentarse en la lista de seleccion de catalogo para existencias todo se debe listar con comas ej. 53.08.11;53.08.04;53.08.02",
				"53.08.11;53.08.04;53.08.02"));
		lis_parametros.add(new Parametro("19", "p_sec_acta_ent_rec_cons","Indica el modulo para generar el secuencial de las actas de entrega recepcion por constatacion fisica","1","gen_modulo","ide_gemod","detalle_gemod"));
		lis_parametros.add(new Parametro("19", "p_sec_acta_bajas_reclasificacion","Indica el modulo para generar el secuencial de las actas de bajas","82","gen_modulo","ide_gemod","detalle_gemod"));
	      
		/*
         * CONTRATOS ONLINE =20
         * */
        lis_parametros.add(new Parametro("20", "p_firma_contrato","Indica el nombre de quien firma el contrato generado en el sistema contratos online","ING. FELIZ URRESTA","","",""));
        lis_parametros.add(new Parametro("20", "p_cargo_firma_contrato", "Indica el cargo de la persona que firma", "Ingeniero"));
        lis_parametros.add(new Parametro("20", "p_modulo_secuencialnotificacion","Indica el modulo para generar el secuencial de las notificaciones","21","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("20", "p_sec_precontractual","Indica el modulo para generar el secuencial de los procesos de contratación","72","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("20", "p_sec_precon_sol","Indica el modulo para generar el secuencial de las solicitudes del proceso de contratación","73","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("20", "p_sec_certificacion_pac","Indica el modulo para generar el secuencial de las certificaciones PAC","74","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("20", "p_sec_certificacion_poa","Indica el modulo para generar el secuencial de las certificaciones POA","75","gen_modulo","ide_gemod","detalle_gemod"));
        lis_parametros.add(new Parametro("20", "p_sec_precon_tecnico","Indica el modulo para generar el secuencial de las designaciones de tecnicos de los procesos de contratación","76","gen_modulo","ide_gemod","detalle_gemod"));
        
        /*
         * Transporte = 21
         */
        lis_parametros.add(new Parametro("21", "p_modulo_sec_solicitud_transporte","Permite seleccionar el modulo de transportepara generar el numero secuencial en solicitudes","78","gen_modulo","ide_gemod","detalle_gemod"));
        
        return lis_parametros;
    }
}
