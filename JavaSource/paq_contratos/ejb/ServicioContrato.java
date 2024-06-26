package paq_contratos.ejb;

import javax.ejb.Stateless;

import paq_sistema.aplicacion.Utilitario;

/**
 * Session Bean implementation class ServicioTransporte
 */
@Stateless
public class ServicioContrato {

	public ServicioContrato() {
		// TODO Auto-generated constructor stub
	}

	public String getClientes(String matrizSucursal ){
	    String tab_cliente="select a.ide_recli, ruc_comercial_recli,razon_social_recli, " +
	            "  nombre_comercial_recli " +
	            "  from rec_clientes a " +
	            //" where a.activo_recli = true " +//and coalesce(MATRIZ_SUCURSAL_RECLI,0) in ("+matrizSucursal+")" +
	            " ORDER BY  nombre_comercial_recli";
	    return tab_cliente;
	}
	

	public String getProveedores(){
	    String tab_cliente="select ide_tepro, ruc_tepro, nombre_tepro " +
	            "  from tes_proveedor  " +
	            " where activo_tepro = 'true'and nombre_tepro is not null " +
	            " ORDER BY  nombre_tepro";
	    return tab_cliente;
	}

	
	public String getContratos(){
	    String tab_contrato="select ide_prcon, numero_contrato_prcon, observacion_prcon " +  
	    					"from pre_contrato "+
							"where activo_prcon='true' "+
							"and tipo_int_ext_prcon <>0 order by numero_contrato_prcon ; ";	
	    return tab_contrato;
	}
	
	public String getContratosExt(){
	    String tab_contrato="select ide_prcon, numero_contrato_prcon, observacion_prcon " +  
	    					"from pre_contrato "+
							"where activo_prcon=true "+
							"and tipo_int_ext_prcon in (1,2) order by numero_contrato_prcon ; ";	
	    return tab_contrato;
	}
	
	public String ContratosExt(){
	    String tab_contrato="SELECT con.ide_prcon, "
				+" detalle_coest,"
				+" fecha_firma_prcon,"
				+" numero_contrato_prcon,"
				+" observacion_prcon,"
				+" monto_prcon,"
				+" DOCUMENTO_IDENTIDAD_GTEMP ||' '|| NOMBRES_APELLIDOS as administrador,"
				+" DETALLE_GEDEP,"
				+" ruc_tepro ||' '|| nombre_tepro as proveedor"
				
				+" FROM public.pre_contrato con"
				+" left join cont_estado est on est.ide_coest=con.ide_coest"
				+" left join pre_contrato_administrador ad on ad.ide_prcon=con.ide_prcon"
				+" left join (SELECT EPAR.IDE_GEEDP, EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
				+" 		EMP.APELLIDO_PATERNO_GTEMP || ' ' || (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end)  || ' ' ||EMP.PRIMER_NOMBRE_GTEMP || ' ' || (case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end)" 
				+" 		 AS NOMBRES_APELLIDOS, DEPA.DETALLE_GEDEP FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR "
				+" 		 LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU" 
				+" 		 LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE) emp on emp.IDE_GEEDP=ad.IDE_GEEDP"
				
				+" left join tes_proveedor pro on pro.ide_tepro=con.ide_tepro"
				
				+" where tipo_int_ext_prcon in (1,2) order by fecha_firma_prcon desc; ";	
	    return tab_contrato;
	}

	public String getNumRegistros( ){
	    String tab_numero="select count(ide_predo) as num from pre_control_documento where activo_predo='true' ";
	    return tab_numero;
	}

	
	public String getDatosBanco() {
		String tab_datos = "select cast(ide_geins as bigint),detalle_geins "
				+ "from gen_institucion where activo_geins = true and gen_ide_geins=1 "
				+ "order by detalle_geins";
		return tab_datos;
	}

	
	public String getPolizas(String grupos, String banco,
			String fecha_inicial, String fecha_final) {

		String tab_polizas = /*"SELECT pol.ide_tepol, pol.numero_poliza_tepol, pol.valor_tepol, vigencia_desde_tepol, vigencia_hasta_tepol, " 
				+" bco.detalle_geins, e.detalle_coest, tip.detalle_tetip,	con.numero_contrato_prcon, con.observacion_prcon, con.fecha_inicio_prcon, con.ruc_tepro, con.nombre_tepro, "  
				+"CASE WHEN pol.vigencia_hasta_tepol < current_date THEN 'VENCIDO'	ELSE 'VIGENTE' END as alerta "
				+"FROM tes_poliza pol "
				+"LEFT JOIN gen_institucion bco ON bco.ide_geins= pol.ide_geins "
				+"left join cont_estado e ON e.ide_coest=pol.ide_coest "
				+"left join tes_tipo_poliza tip ON tip.ide_tetip=pol.ide_tetip "
				+"left join  "
				+"( select ide_prcon, numero_contrato_prcon, observacion_prcon, fecha_inicio_prcon, ruc_tepro, nombre_tepro, "
				+"p.ide_recli, ruc_comercial_recli, nombre_comercial_recli  "
				+"from pre_contrato p "
				+"left join rec_clientes c ON c.ide_recli=p.ide_recli "
				+"left join tes_proveedor t ON t.ide_tepro=p.ide_tepro "
				+"where activo_prcon = true )con ON con.ide_prcon=pol.ide_prcon "
				+"where pol.activo_tepol=true and bco.gen_ide_geins=1	and bco.activo_geins=true and e.activo_coest = true "
				+"and tip.activo_tetip=true " ;*/
				
				" SELECT pol.ide_tepol, secuencial_tepol as secuencial, pol.numero_poliza_tepol,tip.detalle_tetip as tipo, con.observacion_prcon as Referencia_contrato, af.nombre_tepro as afianzado, bco.detalle_geins as emisor, be.detalle_geins as beneficiario," 
				+" pol.fecha_emision_tepol as fecha_emision, pol.valor_tepol as monto, con.fecha_inicio_prcon as fecha_inicio_contrato, con.fecha_fin_prcon as fecha_fin_contrato,"
				+" vigencia_desde_tepol, vigencia_hasta_tepol, e.detalle_coest as estado_poliza, con.numero_contrato_prcon, con.nombre_tepro, renovaciones,"
				+" fecha_desde_terep as fecha_desde_renovacion,fecha_hasta_terep as fecha_hasta_renovacion,valor_terep as valor_renovacion, "
				+" CASE WHEN (case when renovaciones>0 then fecha_hasta_terep else pol.vigencia_hasta_tepol end) < current_date THEN 'VENCIDO' ELSE 'VIGENTE' END as alerta  "
				+" FROM tes_poliza pol "
				+" LEFT JOIN gen_institucion bco ON bco.ide_geins= pol.ide_geins and bco.ide_getii in (1,14)"
				+" left join cont_estado e ON e.ide_coest=pol.ide_coest "
				+" left join tes_tipo_poliza tip ON tip.ide_tetip=pol.ide_tetip "
				+" left join tes_proveedor af ON af.ide_tepro=pol.afianzado_tepol"
				+" left join gen_institucion be ON be.ide_geins=pol.beneficiario_tepol and be.ide_getii in (13)"

				+" left join  ( select ide_prcon, numero_contrato_prcon, observacion_prcon, fecha_inicio_prcon,fecha_fin_prcon, ruc_tepro, nombre_tepro, p.ide_recli, ruc_comercial_recli, nombre_comercial_recli  "
				+" 		from pre_contrato p "
				+" 		left join rec_clientes c ON c.ide_recli=p.ide_recli "
				+" 		left join tes_proveedor t ON t.ide_tepro=p.ide_tepro "
				+" 		where activo_prcon = true )con ON con.ide_prcon=pol.ide_prcon "
				+" left join (SELECT ide_tepol, count(*) as renovaciones FROM tes_renovacion_poliza group by ide_tepol) rn on rn.ide_tepol=pol.ide_tepol"
				
				+" left join (SELECT ide_tepol, fecha_desde_terep,fecha_hasta_terep,valor_terep, detalle_coest FROM tes_renovacion_poliza rp "
				+"           join cont_estado e on e.ide_coest=rp.ide_coest where activo_terep=true ) rn2 on rn2.ide_tepol=pol.ide_tepol "
						
				+" where pol.activo_tepol=true "
				+" and bco.activo_geins=true and e.activo_coest = true and tip.activo_tetip=true ";
						
		
			//	--and bco.ide_geins=1
			//	--and vigencia_hasta_tepol between '2017-08-01' and '2017-08-31'
				if (grupos.equals("1")) {
					tab_polizas += " and bco.ide_geins ='" + banco + "'";
				}
				if (grupos.equals("2")) {
					tab_polizas += " and vigencia_hasta_tepol  between '"
							+ fecha_inicial + "' and '" + fecha_final + "' ";
				}
				
		System.out.println("tab_polizas "+tab_polizas);
		return tab_polizas;
	}

	
	public String getDatosContratos(String grupos, String tipo,
			String fecha_inicial, String fecha_final) {

		String tab_contratos = "SELECT ide_prcon, fecha_inicio_prcon, fecha_fin_prcon, numero_contrato_prcon, observacion_prcon, e.detalle_coest, ts.detalle_prtsc,et.detalle_pretc, "
				+"CASE when tipo_int_ext_prcon =0 THEN 'INTERNO' WHEN tipo_int_ext_prcon =1 THEN 'EXTERNO' ELSE 'INFIMA CUANTIA' END AS tipo_contrato, "
				+"monto_prcon, plazo_prcon, detalle_prtip, "
				+"ruc_comercial_recli, nombre_comercial_recli, ruc_tepro, nombre_tepro "
				+"from pre_contrato con "
				+"left join cont_estado e ON e.ide_coest=con.ide_coest "
				+"left join pre_tipo_servicio_contrato ts ON ts.ide_prtsc=con.ide_prtsc "
				+"left join pre_etapa_contrato et ON et.ide_pretc=con.ide_pretc "
				+"left join rec_clientes c ON c.ide_recli=con.ide_recli "
				+"left join tes_proveedor t ON t.ide_tepro=con.ide_tepro "
				+"left join pre_tipo_plazo tp ON tp.ide_prtip=con.ide_prtip "
				+"where con.activo_prcon= 'true' ";
//				and tipo_int_ext_prcon =1 
//				and fecha_inicio_prcon between '2017-01-01' and '2017-12-31' " ;
				if (grupos.equals("1")) {
					tab_contratos += " and con.ide_tepro ='" + tipo + "'";
					tab_contratos += " or con.ide_recli ='" + tipo + "'";
				}
				if (grupos.equals("2")) {
					tab_contratos += " and fecha_inicio_prcon  between '"
							+ fecha_inicial + "' and '" + fecha_final + "' ";
				}
		return tab_contratos;
	}


	public String getClientesDatosBasicos(){
		    String tab_cliente="select a.ide_recli, ruc_comercial_recli,razon_social_recli from rec_clientes a " 
								+"where coalesce(nro_establecimiento_recli,0) in (1) and activo_recli='true' and ruc_comercial_recli is not null "
								+"UNION "
								+"select ide_tepro, ruc_tepro,nombre_tepro from tes_proveedor "
								+"where activo_tepro='true' "
								+"ORDER BY  RAZON_SOCIAL_recli";
		    return tab_cliente;
		}
	
	public String getContratosControlPrevio(){
	    String tab_contrato="select ide_prcon, numero_contrato_prcon, observacion_prcon " +  
	    					"from pre_contrato "+
							"where activo_prcon='true' "+
							"and tipo_int_ext_prcon <>0 order by numero_contrato_prcon ; ";	
	    return tab_contrato;
	}

	
	public String getDatosControlPrevio(String grupos, String tipo,
			String fecha_inicial, String fecha_final) {

		String tab_contratos = " select   "
				+"cp.ide_precp, CASE WHEN tipo_control_precp=0 THEN 'ANTICIPO' WHEN tipo_control_precp=1 THEN 'AVANCE' ELSE 'CIERRE' END AS tipo_control,  "
				+"cp.detalle_precp, cp.fecha_registro_precp, con.ide_prcon, con.numero_contrato_prcon, con.observacion_prcon, cpd.detalle_predo, cpd.detalle_prtcd, cpd.detalle_prcpd " 
				+"from pre_control_previo cp "
				+"left join pre_contrato con ON con.ide_prcon=cp.ide_prcon "
				+"left join (select p.ide_precp, p.ide_prcpd,d.detalle_predo, cd.detalle_prtcd, p.detalle_prcpd "    
				+"from pre_control_pre_doc p "
				+"left join pre_control_documento d ON d.ide_predo= p.ide_predo "
				+"left join pre_tipo_control_documento cd ON cd.ide_prtcd=p.ide_prtcd "
				+"where p.activo_prcpd = 'true' )cpd ON cpd.ide_precp=cp.ide_precp " 
				+"where cp.activo_precp = 'true' ";
//				+"--and con.ide_prcon= 3345 "
//				+"--and cp.fecha_registro_precp between '2017-08-01' and '2017-08-31' "
//				+"order by cp.fecha_registro_precp";
				if (grupos.equals("1")) {
					tab_contratos += " and con.ide_prcon ='" + tipo + "'";
				}
				if (grupos.equals("2")) {
					tab_contratos += " and cp.fecha_registro_precp  between '"
							+ fecha_inicial + "' and '" + fecha_final + "' ";
				}
		return tab_contratos;
	}

	public String getPAC(){
	    String tab_pac="select ide_prpac, cpc_prpac, descripcion_prpac " +  
	    					"from pre_pac "+
							"where activo_prpac=true; ";	
	    return tab_pac;
	}
	
	public String getProcesos(){
	    String tab_proceso="select ide_prcop, codigo_proceso_prcop, objeto_contratacion_prcop " +  
	    					"from pre_contratacion_publica "+
							"where activo_prcop=true; ";	
	    return tab_proceso;
	}

}
