package paq_precontractual.ejb;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import paq_sistema.aplicacion.Utilitario;
import framework.aplicacion.TablaGenerica;

@Stateless
public class ServicioPrecontractual {
	
	@EJB
	private ServicioEtapaProcedimiento ser_EtapaProcedimiento;
	
	public ServicioPrecontractual(){
		
	}
	private Utilitario utilitario=new Utilitario();
	
	/**
	 * Metodo que devuelve el numero secuencial, por modulo
	 * @param modulo recibe el modulo al cual pertenece el secuencial
	 * @return String nro secuencial por modulo
	 */
	public String numeroSecuencial(String modulo){
		String resultado="1";
		TablaGenerica valor_resultado=utilitario.consultar("select ide_gemod,numero_secuencial_gemos from gen_modulo_secuencial where ide_gemod="+modulo);
		Integer nuevo_valor=pckUtilidades.CConversion.CInt(valor_resultado.getValor("numero_secuencial_gemos"))+1;
		resultado =	nuevo_valor+"";
		//System.out.println("SECUENCIA--->>"+resultado);
		return resultado;
	}
	
	public String guardaSecuencial(String secuencial_vigente,String modulo){
		String mensaje="Actualizado Secuencial";
		double nuevo_valor=Double.parseDouble(secuencial_vigente);
		utilitario.getConexion().ejecutarSql("update gen_modulo_secuencial set numero_secuencial_gemos="+nuevo_valor+" where ide_gemod="+modulo);
		//System.out.println("update gen_modulo_secuencial set numero_secuencial_gemos="+nuevo_valor+" where ide_gemod="+modulo);
		return mensaje;
	}
	public String cargarInformacionUsuarioEtapaAEnviar(Integer ide_prpro,Integer ide_etapa){
		String info_usuario_enviar_etapa="select ide_geedp,ide_gtemp from gen_empleados_departamento_par where ide_geedp=(select ide_geedp from precon_eta_proce_empl_dep where activo_prepe=true and ide_pretp = (select ide_pretp from precon_etapa_procedimiento where ide_preta ="+ide_etapa+" and ide_prpro="+ide_prpro+"));";
		return info_usuario_enviar_etapa;
	}
	
	public String cargarDatosUsuarioEtapaAEnviar(Integer ide_prpro,Integer ide_etapa){
		String datos_usuario_enviar_etapa="select ide_geedp,DOCUMENTO_IDENTIDAD_GTEMP,NOMBRES_APELLIDOS,detalle_geare, DETALLE_GEDEP from (SELECT EPAR.IDE_GEEDP, EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
                         +" coalesce(EMP.PRIMER_NOMBRE_GTEMP,'') || ' ' || coalesce(EMP.SEGUNDO_NOMBRE_GTEMP,'') || ' ' || coalesce(EMP.APELLIDO_PATERNO_GTEMP,'') || ' ' || coalesce(EMP.APELLIDO_MATERNO_GTEMP,'') "
                         +" AS NOMBRES_APELLIDOS, DEPA.DETALLE_GEDEP, EPAR.activo_geedp, detalle_geare FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR "
                         +" LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
                         +" LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE) para " 
                         +" where ide_geedp=(select ide_geedp from precon_eta_proce_empl_dep where activo_prepe=true and ide_pretp = (select ide_pretp from precon_etapa_procedimiento where ide_preta ="+ide_etapa+" and ide_prpro="+ide_prpro+"));";
		return datos_usuario_enviar_etapa;
	}
	
	public String responsablePAC(Integer ide_prpac){
		String info_responsablePAC="SELECT pac.ide_prpac, descripcion_prpac, justificacion_prpac, descripcion_prpac, NOMBRES_APELLIDOS,detalle_geare, DETALLE_GEDEP, rpac.ide_gtemp,rpac.IDE_GEEDP "
				+" FROM pre_pac pac "
                +" left join pre_responsable_contratacion rpac on rpac.ide_prpac=pac.ide_prpac "
                +" left join (SELECT EPAR.IDE_GEEDP, EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
                +"      coalesce(EMP.PRIMER_NOMBRE_GTEMP,'') || ' ' || coalesce(EMP.SEGUNDO_NOMBRE_GTEMP,'') || ' ' || coalesce(EMP.APELLIDO_PATERNO_GTEMP,'') || ' ' || coalesce(EMP.APELLIDO_MATERNO_GTEMP,'') "
                +"     AS NOMBRES_APELLIDOS, DEPA.DETALLE_GEDEP, EPAR.activo_geedp, detalle_geare FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR "
                +"     LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
                +"     LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE) resp on resp.IDE_GEEDP=rpac.IDE_GEEDP "
				+" where rpac.activo_prrec=true and pac.ide_prpac="+ ide_prpac;
		return info_responsablePAC;
	}
	
	public String getProceso (){
		String proceso = "select ide_prpre, extract(year from fecha_prpre) as anio,descripcion_prpre,estado_proceso_prpre  from precon_precontractual where activo_prpre=true order by anio desc,descripcion_prpre;";
		return proceso;
	}
	public String getReportePrecontractualEstado (Integer ide_pretp){
		String proceso = "select ide_prpre, descripcion_prpre from precon_precontractual where estado_proceso_prpre not in( 'REGISTRADO') and ide_pretp="+ide_pretp+" and activo_prpre=true order by descripcion_prpre";
		return proceso;
	}
	public String getReportePrecontractual (Integer ide_prpre){
		
		String proceso = "select ide_prpre, descripcion_prpre from precon_precontractual where ide_prpre="+ide_prpre+" and activo_prpre=true order by descripcion_prpre";
		return proceso;
	}
	public String getProcesoParaleloUnion(Integer ide_usuario_actual){
		String proceso = "select ide_prpre,descripcion_prpre from precon_precontractual where ide_usuario_iniciop_prpre="+ide_usuario_actual+" and se_encuentra_catalogoe_prpre='NO' and ide_actual_preta=9 and ide_preta=10 and estado_proceso_prpre='EN PROCESO' and activo_prpre=true;";
		return proceso;
	}
	public String getProcesoParaleloUnionUpdate(Integer ide_prpre_actual){
		String proceso = "update precon_precontractual set se_encuentra_catalogoe_prpre='NO' where ide_prpre="+ide_prpre_actual+";";
		return proceso;
	}
	public String getProcesoParaleloUpdateEstadoFalse(Integer ide_prpre_seleccionado){
		String proceso = "update precon_precontractual set activo_prpre=false where ide_prpre="+ide_prpre_seleccionado+";";
		return proceso;
	}
	
	public String getProcesoExpediente(){
		String proceso = "select ide_prpre,descripcion_prpre,descripcion as procedimiento,extract(year from fecha_prpre) as anio,detalle_geare as area from precon_precontractual pc " 
                + " left join ("+ser_EtapaProcedimiento.getProcedimientos()+") pro on pro.ide_pretp=pc.ide_pretp left join gen_area ar on ar.ide_geare=pc.ide_geare ";
		return proceso;
	}
	
}
