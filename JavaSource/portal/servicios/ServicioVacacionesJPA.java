/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.servicios;

import framework.aplicacion.TablaGenerica;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import javax.xml.registry.infomodel.PersonName;

import org.hibernate.validator.util.privilegedactions.GetConstructor;

import paq_contabilidad.ejb.ServicioContabilidad;
import paq_sistema.aplicacion.Utilitario;
import portal.controladores.ControladorVacaciones;
import portal.entidades.AsiMotivo;
import portal.entidades.AsiPermisosVacacionHext;
import portal.entidades.AsiPermisoJustificacion;
import portal.entidades.AsiVacacion;
import portal.entidades.ConReloj;
import portal.entidades.GenAnio;
import portal.entidades.GenMes;
import portal.entidades.GenPeriodo;
import portal.entidades.GenPeriodoPK;
import portal.entidades.GthEmpleado;
import portal.entidades.ConBiometricoMarcaciones;
import portal.entidades.SriImpuestoRenta;
import paq_contabilidad.ejb.*;

/**
 * 
 * @author Juan Ayerve
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@ManagedBean
@ViewScoped
public class ServicioVacacionesJPA {

	// numero de dias al año
	Integer nda = 360;
	/**
	 * numero de dias que el empleado lleva en la empresa
	 */
	Integer nde = 0;
	@EJB
	private ServicioContabilidad ser_contabilidad;


	private Utilitario utilitario = new Utilitario();
	@PersistenceUnit(unitName = "sampuData")
	private EntityManagerFactory fabrica;
	@Resource
	private UserTransaction utx;

	public List<AsiPermisosVacacionHext> getSolicitudesVacaciones(String ideGeedp, String columnNameWhere) {
		EntityManager manejador = fabrica.createEntityManager();
		try {
			StringBuilder strSql = new StringBuilder();
			strSql.append("SELECT a FROM AsiPermisosVacacionHext a WHERE a.").append(columnNameWhere).append(".ideGeedp in(")
            .append(ideGeedp).append(") and a.fechaSolicitudAspvh between "
         			+ " '2018-01-01' and '"+utilitario.getAnio(utilitario.getFechaActual())+"-12-31' order by a.ideAspvh desc");
        	Query q = manejador.createQuery(strSql.toString());
			return q.getResultList();
		} catch (Exception e) {
		} finally {
			manejador.close();
		}
		return null;
	}

	//listaMarcaciones = servicioVacaciones.getSolicitudesVacaciones(str_ide_geedp.toString(), "ideGeedp");

	

	public List<ConBiometricoMarcaciones> getMarcionesTeletrabajo(String ideGeedp) {
		EntityManager manejador = fabrica.createEntityManager();
		try {
			StringBuilder strSql = new StringBuilder();
			//strSql.append("SELECT c FROM ConBiometricoMarcaciones c WHERE c.").append(columnNameWhere).append(".idePersonaCobim in(")
            //.append(ideGeedp).append(") and c.fechaEventoCobim between "
         		
			strSql.append("SELECT c FROM ConBiometricoMarcaciones c WHERE c.idePersonaCobim like '"+ideGeedp+"' and c.fechaEventoCobim >= "           
            + "'"+utilitario.getFechaActual()+"'  "
            //+ "'2021-02-01 06:00:00'  "
            + "and c.ideCorel.ideCorel in("+utilitario.getVariable("p_matriz_marcaciones_biometrico")+") "
            + "order by c.fechaEventoCobim desc");
        	Query q = manejador.createQuery(strSql.toString());
			return q.getResultList();
		} catch (Exception e) {
		} finally {
			manejador.close();
		}
		return null;
	}
	
	
   // @NamedQuery(name = "ConBiometricoMarcaciones.findByIdePersonaCobim", query = "SELECT c FROM ConBiometricoMarcaciones c WHERE c.idePersonaCobim = :idePersonaCobim"),

	
	
	/**
	 * 
	 * @param ideGeedp
	 * @param columnNameWhere
	 *            parametro para ser evaluado
	 * @return
	 */

	public List<AsiPermisosVacacionHext> getSolicitudesVacacionesSolicitadas(String ideGeedp, String columnNameWhere) {
		EntityManager manejador = fabrica.createEntityManager();
		try {
			StringBuilder strSql = new StringBuilder();

			// strSql.append("SELECT a FROM AsiPermisosVacacionHext a WHERE a.").append(columnNameWhere).append(".ideGeedp =")
			// .append(ideGeedp).append(" and a.tipoAspvh=2 order by a.fechaSolicitudAspvh desc");

			strSql.append("SELECT a FROM AsiPermisosVacacionHext a WHERE a.").append(columnNameWhere).append(".ideGeedp in(").append(ideGeedp)
					.append(") and a.fechaSolicitudAspvh between " + " '2018-01-01' and '" + utilitario.getAnio(utilitario.getFechaHoraActual()) + "-12-31' order by a.ideAspvh desc");

			Query q = manejador.createQuery(strSql.toString());
			return q.getResultList();
		} catch (Exception e) {
		} finally {
			// aqui se debe implementar el merge o el persist
			manejador.close();
		}
		return null;
	}

	public String guardarSolicitudVacaciones(AsiPermisosVacacionHext solicitud, AsiPermisoJustificacion solitudjustificacion) {
		EntityManager manejador = fabrica.createEntityManager();
		try {
			utx.begin();
			manejador.joinTransaction();
			solicitud.setIdeAsmot(manejador.find(AsiMotivo.class, solicitud.getIdeAsmot().getIdeAsmot()));
			// solicitud.getGenPeriodo().setGenAnio(manejador.find(GenAnio.class,
			// solicitud.getGenPeriodo().getGenAnio().getIdeGeani()));
			// solicitud.getGenPeriodo().setGenMes(manejador.find(GenMes.class,
			// solicitud.getGenPeriodo().getGenMes().getIdeGemes()));
			// solicitud.getGenPeriodo().setGenPeriodoPK(new GenPeriodoPK(new
			// BigInteger(solicitud.getGenPeriodo().getGenMes().getIdeGemes().toString()),
			// new
			// BigInteger(solicitud.getGenPeriodo().getGenAnio().getIdeGeani().toString())));
			// solicitud.setGenPeriodo(manejador.find(GenPeriodo.class,
			// solicitud.getGenPeriodo().getGenPeriodoPK()));
			// Guarda o modifica solicitud

			if (solicitud.getIdeAspvh() == null) {

				TablaGenerica tab_asi_permisos_vacacion_hext = utilitario.consultar(servicioCodigoMaximo("ASI_PERMISOS_VACACION_HEXT", "IDE_ASPVH"));
				String ideaspvh = tab_asi_permisos_vacacion_hext.getValor("codigo");
				int codigo_ideaspvh = Integer.parseInt(ideaspvh);

				// long ideaspvh =new
				// Long(utilitario.getConexion().getMaximo("ASI_PERMISOS_VACACION_HEXT",
				// "IDE_ASPVH", 1));
				// Integer conertideaspvh= (int) ideaspvh;
				solicitud.setIdeAspvh(codigo_ideaspvh); // maximo de utilitario
				solicitud.setFechaIngre(utilitario.getDate());
				Timestamp timestamp = Timestamp.valueOf(utilitario.getFechaHoraActual());
				solicitud.setHoraIngre(timestamp);

				TablaGenerica tab_asi_permiso_justificacion = utilitario.consultar(servicioCodigoMaximo("ASI_PERMISO_JUSTIFICACION", "IDE_ASPEJ"));
				String ideaspej = tab_asi_permiso_justificacion.getValor("codigo");
				int codigo_ideaspej = Integer.parseInt(ideaspej);
				// Integer conertideaspej= (int) ideaspej;
				solitudjustificacion.setIdeAspej(codigo_ideaspej); // maximo de
																	// utilitario

				// Query query =
				// manejador.createQuery("UPDATE AsiVacacion x SET x.diasPendientesAsvac ="+dias_pendientes+" "+
				// "WHERE ideGtemp=:"+22);
				// query.executeUpdate();
				// AsiVacacion vacacion = manejador.find(AsiVacacion.class, 22);
				// vacacion.setDiasPendientesAsvac(dias_pendientes);

				// subir el archivo

				manejador.persist(solicitud);
				if (solitudjustificacion.getArchivoAspej() != null && !solitudjustificacion.getArchivoAspej().isEmpty()) {
					solitudjustificacion.setIdeAspvh(solicitud);
					manejador.persist(solitudjustificacion);
				}
			} else {
				manejador.merge(solicitud);
				if (solitudjustificacion.getArchivoAspej() != null && !solitudjustificacion.getArchivoAspej().isEmpty()) {
					manejador.merge(solitudjustificacion);
				}
			}
			utx.commit();
		} catch (Exception e) {
			try {
				utx.rollback();
			} catch (Exception e1) {
			}
			return e.getMessage();
		} finally {
			manejador.close();
		}
		return "";
	}

	public List getMotivosVacaciones() {
		
			return utilitario.getConexion().consultar("select IDE_ASMOT,DETALLE_ASMOT,APLICA_VACACIONES_ASMOT, "
					+ "DESCRIPCION_ASMOT,NUM_MAX_DIAS_ASMOT,ES_VACACION_ASMOT, "
					+ "CAMBIO_HORARIO_ASMOT,NUM_MAX_REGISTRO_PERMISO,NUM_MAX_APROBACION_PERMISO "
					+ "from ASI_MOTIVO where ver_motivo_asmot=true "
					+ "and (ver_permiso_tthh=false or ver_permiso_tthh is null) "
					+ "order by DETALLE_ASMOT");
		
	}

	public List getJustificacion() {
		return utilitario.getConexion().consultar("select ide_aspej,ide_aspvh,detalle_aspej,fecha_aspej,archivo_aspej from ASI_PERMISO_JUSTIFICACION order by ide_aspej");
	}

	public List getTipoPermiso() {
		return utilitario.getConexion().consultar("select 1 as ide_tipo, 'Permiso por Horas' as detalle_tipo union select 2 as ide_tipo, 'Vacaciones' as detalle_tipo  union select 4 as ide_tipo, 'Permiso por Dias' as detalle_tipo order by 1 asc");
	}

	public List getJefesInmediatos() {
		String aprobador_permisos=utilitario.getVariable("p_asi_aprobador_jefe_inmediato");
		return utilitario.getConexion().consultar(
				"SELECT EPAR.IDE_GEEDP, " + "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " + "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " + "EMP.PRIMER_NOMBRE_GTEMP || ' ' || "
						+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS " + "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " + "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP "
						+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " + "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " + "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  " + "WHERE EPAR.ACTIVO_GEEDP=TRUE "
							//+ " and epar.ide_gegro in(6) "
						+ " and IDE_GEGRO in ("+aprobador_permisos+") "
						+ "group by EPAR.IDE_GEEDP,EMP.APELLIDO_PATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,EMP.APELLIDO_MATERNO_GTEMP order by APELLIDO_PATERNO_GTEMP ");

		

	}
	
	
	public List getJefesInmediatosSinGerente(){
		
		return utilitario.getConexion().consultar("SELECT EPAR.IDE_GEEDP, " + "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " + "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " + "EMP.PRIMER_NOMBRE_GTEMP || ' ' || "
				+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS " + "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " + "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP "
				+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " + "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " + "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  " + "WHERE EPAR.ACTIVO_GEEDP=TRUE  "
						+ " and epar.ide_gegro not in(6)"
				+ "group by EPAR.IDE_GEEDP,EMP.APELLIDO_PATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,EMP.APELLIDO_MATERNO_GTEMP order by APELLIDO_PATERNO_GTEMP ");
	}

	public List getAnios() {
		return utilitario.getConexion().consultar("select IDE_GEANI,DETALLE_GEANI from GEN_ANIO order by DETALLE_GEANI");
	}

	public List getMeses() {
		return utilitario.getConexion().consultar("select IDE_GEMES,DETALLE_GEMES from GEN_MES order by IDE_GEMES");
	}

	/**
	 * Metodo para obtener al grupo que pertenece el empleado 1: Codigo de
	 * trabajo, 2: Losep, 3:Pasante
	 * 
	 * @param IDE_GEEDP
	 * @return
	 */

	public Integer getGrupoTipoEmpleado(String IDE_GEEDP) {
		Integer ide_gttem = 0;
		TablaGenerica tab_tipo_empleado = utilitario.consultar("select ide_geedp,ide_gttem,ide_gtemp from gen_empleados_departamento_par where IDE_GTEMP=" + IDE_GEEDP + " ORDER BY IDE_GEEDP DESC  LIMIT 1 ");
		ide_gttem = Integer.parseInt(tab_tipo_empleado.getValor("ide_gttem"));
		return ide_gttem;
	}

	/**
	 * Metodo devuelve el numero de dias para el nuevo calculo de dias tomados a
	 * vacacion
	 * 
	 * @param IDE_GEEDP
	 * @return
	 */
	public double getNumeroDiasAjusteEmpleado(String IDE_GEEDP,String ide_asvac) {
		double valorNumeroDiasAjusteEmpleado;
		TablaGenerica tab_periodo = utilitario.consultar("SELECT IDE_ASVAC,FECHA_INGRESO_ASVAC FROM ASI_VACACION WHERE IDE_GTEMP=" + IDE_GEEDP + " AND ACTIVO_ASVAC=true "
				+ "ORDER BY IDE_ASVAC DESC LIMIT 1");
		if (!tab_periodo.isEmpty()) {
			TablaGenerica tabvalorNumeroDiasAjusteEmpleado = utilitario.consultar("select ide_gtemp,sum(nro_dias_ajuste_asvac) as nro_dias_ajuste_asvac " + " "
					+ "from asi_vacacion "
					+ "where IDE_ASVAC in("+ide_asvac+") "
					+ "group by ide_gtemp");
					//+ "" + tab_periodo.getValor("IDE_ASVAC"));
			if (tabvalorNumeroDiasAjusteEmpleado.getValor("nro_dias_ajuste_asvac") == null || tabvalorNumeroDiasAjusteEmpleado.getValor("nro_dias_ajuste_asvac").isEmpty()) {
				valorNumeroDiasAjusteEmpleado = 0.0;
			} else {
				valorNumeroDiasAjusteEmpleado = Double.parseDouble(tabvalorNumeroDiasAjusteEmpleado.getValor("nro_dias_ajuste_asvac"));
			}
			return valorNumeroDiasAjusteEmpleado;
		}

		return 0;

	}

	/**
	 * Metodo devuelve el numero de dias de ajuste para cuando se pasa de 60
	 * 
	 * @param IDE_GEEDP
	 * @return
	 */

	public double nroDiasAjustePeriodo(String IDE_GEEDP,String ide_asvac) {

		double numeroDiasTomadoss = 0.0;
		TablaGenerica tab_periodo = utilitario.consultar("SELECT IDE_ASVAC,FECHA_INGRESO_ASVAC FROM ASI_VACACION WHERE IDE_GTEMP=" + IDE_GEEDP + " AND ACTIVO_ASVAC=true "
				+ "ORDER BY IDE_ASVAC DESC LIMIT 1");
		if (!tab_periodo.isEmpty()) {
			/*TablaGenerica tab_numero_tomados = utilitario.consultar("SELECT IDE_ASVAC,DIA_ACUMULADO " + "FROM ( " + "SELECT IDE_ASVAC,(case when sum(DIA_ACUMULADO_ASDEV) is null then 0 else sum(DIA_ACUMULADO_ASDEV) end)AS DIA_ACUMULADO, "
					+ "(case when SUM(DIA_DESCONTADO_ASDEV) is null then 0 else SUM(DIA_DESCONTADO_ASDEV) end)AS DIA_DESCONTADO " + "FROM ASI_DETALLE_VACACION WHERE ACTIVO_ASDEV = true GROUP BY IDE_ASVAC " + " ) a "
					+ " where IDE_ASVAC in("+ide_asvac+")");
					//+ tab_periodo.getValor("IDE_ASVAC"));*/
			
			
			TablaGenerica tab_numero_tomados = utilitario.consultar("select vac.ide_gtemp,sum(dev.dia_acumulado_asdev) as DIA_ACUMULADO "
			+ "from asi_vacacion vac "
			+ "left join ASI_DETALLE_VACACION dev on dev.ide_asvac=vac.ide_asvac "
			+ "where vac.ide_asvac in("+ide_asvac+") "
			+ "group by vac.ide_gtemp");

			if (tab_numero_tomados.getValor("DIA_ACUMULADO") == null || tab_numero_tomados.getValor("DIA_ACUMULADO").isEmpty()) {
				numeroDiasTomadoss = 0.0;
			} else {
				numeroDiasTomadoss = Double.parseDouble(tab_numero_tomados.getValor("DIA_ACUMULADO"));
			}
			return numeroDiasTomadoss;

		}
		return 0;
	}

	/**
	 * Metodo extrae el numero de dias pendientes para el calculo de dias
	 * tomados hasta el 30 de abril obtenidos del excel
	 * 
	 * @param IDE_GEEDP
	 * @return
	 */

	public double getNumeroDiasPendientesInicial(String IDE_GEEDP,String ide_asvac) {
		double valorIncialDiasTomados;
		TablaGenerica tab_periodo = utilitario.consultar("SELECT IDE_ASVAC,FECHA_INGRESO_ASVAC FROM ASI_VACACION WHERE IDE_GTEMP=" + IDE_GEEDP + " AND ACTIVO_ASVAC=true "
				+ "ORDER BY IDE_ASVAC DESC LIMIT 1");
		if (!tab_periodo.isEmpty()) {
			TablaGenerica tab_valor_InicialDiasTomados = utilitario.consultar("select ide_gtemp,sum(dias_tomados_asvac) as dias_tomados_asvac  " + " "
					+ "from asi_vacacion  "
					+ "where IDE_ASVAC in("+ide_asvac+") "
					+ "group by ide_gtemp");
					//+ "" + tab_periodo.getValor("IDE_ASVAC"));
			if (tab_valor_InicialDiasTomados.getValor("dias_tomados_asvac") == null || tab_valor_InicialDiasTomados.getValor("dias_tomados_asvac").isEmpty()) {
				valorIncialDiasTomados = 0.0;
			} else {
				valorIncialDiasTomados = Double.parseDouble(tab_valor_InicialDiasTomados.getValor("dias_tomados_asvac"));
			}
			return valorIncialDiasTomados;
		}

		return 0;

	}

	/*
	 * Metodo devuelve los dias solicitados con cargo a vacacion de cada
	 * empleado
	 */
	public double getNumeroDiasTomados(String IDE_GEEDP,String ide_asvac) {
		double numeroDiasTomadoss = 0.0;
		TablaGenerica tab_periodo = utilitario.consultar("SELECT IDE_ASVAC,FECHA_INGRESO_ASVAC FROM ASI_VACACION WHERE IDE_GTEMP =" + IDE_GEEDP + " AND ACTIVO_ASVAC=true");
		if (!tab_periodo.isEmpty()) {
			/*TablaGenerica tab_numero_tomados = utilitario.consultar("SELECT IDE_ASVAC,DIA_DESCONTADO " + "FROM ( " + "SELECT IDE_ASVAC,(case when sum(DIA_ACUMULADO_ASDEV) is null then 0 else sum(DIA_ACUMULADO_ASDEV) end)AS DIA_ACUMULADO, "
					+ "(case when SUM(DIA_DESCONTADO_ASDEV) is null then 0 else SUM(DIA_DESCONTADO_ASDEV) end)AS DIA_DESCONTADO " + "FROM ASI_DETALLE_VACACION WHERE ACTIVO_ASDEV = true and fin_semana_asdev=false GROUP BY IDE_ASVAC "
					+ " ) a "
					+ "where IDE_ASVAC in("+ide_asvac+")"); 
					//+ tab_periodo.getValor("IDE_ASVAC"));*/
			TablaGenerica tab_numero_tomados = utilitario.consultar("select vac.ide_gtemp,sum(dev.DIA_DESCONTADO_ASDEV) as DIA_DESCONTADO "
					+ "from asi_vacacion vac "
					+ "left join ASI_DETALLE_VACACION dev on dev.ide_asvac=vac.ide_asvac "
					+ "where vac.ide_asvac in("+ide_asvac+") and dev.fin_semana_asdev=false "
					+ "group by vac.ide_gtemp");

			if (tab_numero_tomados.getValor("DIA_DESCONTADO") == null || tab_numero_tomados.getValor("DIA_DESCONTADO").isEmpty()) {
				numeroDiasTomadoss = 0.0;
			} else {
				numeroDiasTomadoss = Double.parseDouble(tab_numero_tomados.getValor("DIA_DESCONTADO"));
			}
			return numeroDiasTomadoss;

		}
		return 0;

	}

	/*
	 * Metodo devuelve los dias pendientes calculados a partir del excel de cada
	 * empleado
	 */

	public double getNumeroDiasPendientesInicialAjuste(String IDE_GEEDP,String ide_asvac) {
		double numeroDiasTomadoss = 0.0;
		TablaGenerica tab_periodo = utilitario.consultar("SELECT IDE_ASVAC,FECHA_INGRESO_ASVAC FROM ASI_VACACION WHERE IDE_GTEMP =" + IDE_GEEDP + " AND ACTIVO_ASVAC=true "
				+ "ORDER BY IDE_ASVAC DESC LIMIT 1");
		if (!tab_periodo.isEmpty()) {
			
			TablaGenerica tab_numero_tomados = utilitario.consultar("select vac.ide_gtemp,sum(dev.DIA_SOLICITADO_ASDEV) as DIA_SOLICITADO "
			+ "from asi_vacacion vac "
			+ "left join ASI_DETALLE_VACACION dev on dev.ide_asvac=vac.ide_asvac "
			+ "where vac.ide_asvac in("+ide_asvac+") "
			+ "group by vac.ide_gtemp");

			if (tab_numero_tomados.getValor("DIA_SOLICITADO") == null || tab_numero_tomados.getValor("DIA_SOLICITADO").isEmpty()) {
				numeroDiasTomadoss = 0.0;
			} else {
				numeroDiasTomadoss = Double.parseDouble(tab_numero_tomados.getValor("DIA_SOLICITADO"));
			}
			return numeroDiasTomadoss;

		}
		return 0;

	}

	/**
	 * Metodo numero de dias tomados con crago a vacacion a partir de la tabla
	 * asi_permisos_vacacion_hext
	 * 
	 * @param IDE_GEEDP
	 * @return
	 */

	public BigDecimal getNumeroDiasTomadosTablaAsiPermisosVacacionHext(String IDE_GEEDP) {

		List tab_consulta = utilitario.getConexion().consultar(
				"SELECT SUM(NRO_HORAS_ASPVH)/8 as DIAS " + "FROM ASI_PERMISOS_VACACION_HEXT  " + "WHERE ANULADO_ASPVH=FALSE AND REGISTRO_NOVEDAD_ASPVH=TRUE AND APROBADO_ASPVH=TRUE AND APROBADO_TTHH_ASPVH=true "
						+ "AND IDE_GTEMP=(select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP=" + IDE_GEEDP + ") AND ACTIVO_ASPVH=true");

		BigDecimal resultado = (BigDecimal) (tab_consulta.get(0));

		return resultado;

	}

	public List getNumeroDiasTomadosEmpleados() {
		return utilitario.getConexion().consultar(
				"select hext.ide_gtemp, EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " + "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  " + "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||  "
						+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,  " + "ROUND(sum(hext.nro_horas_aspvh),4) as total_horas, "
						+ "ROUND((sum(hext.nro_horas_aspvh)/8),4) as dias_tomados, vac.fecha_ingreso_asvac as Ingreso " + "from asi_permisos_vacacion_hext hext  " + "left join gth_empleado emp on emp.ide_gtemp=hext.ide_gtemp  "
						+ "left join asi_vacacion vac on vac.ide_gtemp=emp.ide_gtemp " + "group by hext.ide_gtemp,NOMBRES_APELLIDOS,vac.fecha_ingreso_asvac " + "order by hext.ide_gtemp asc");

	}

	// CALCULO DE NUMERO DE DIAS

	/**
	 * calcular numero de dias calendario que el empleado lleva en la empresa
	 * 
	 * @param fechaIngresoEmpleado
	 * @return
	 */
	/*public void getNumeroDiasEmpleado() {

		TablaGenerica tab_periodo = utilitario.consultar("SELECT IDE_ASVAC,FECHA_INGRESO_ASVAC FROM ASI_VACACION WHERE IDE_GTEMP in (select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR) AND ACTIVO_ASVAC=true");
		List<Integer> periodos = new ArrayList<Integer>();

		periodos = utilitario.getConexion().consultar("SELECT IDE_ASVAC,FECHA_INGRESO_ASVAC FROM ASI_VACACION WHERE IDE_GTEMP in (select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR) AND ACTIVO_ASVAC=true");

		// TablaGenerica tab_periodo =
		// utilitario.consultar("SELECT IDE_ASVAC,FECHA_INGRESO_ASVAC FROM ASI_VACACION WHERE IDE_GTEMP in (select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP) AND ACTIVO_ASVAC=true");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String dateInString = tab_periodo.getValor("FECHA_INGRESO_ASVAC");
		Date date = null;

		/**
		 * Le asigno a una variable de tipo date la fecha de ingreso
		 */

		/*try {
			date = sdf.parse(dateInString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Creo variable tipo calendario para guardar la fecha de ingreso del
		// empleado
		Calendar fechaIngresoEmpleado = Calendar.getInstance();
		fechaIngresoEmpleado.setTime(date);
		long diferenciaEn_ms = new Date().getTime() - fechaIngresoEmpleado.getTime().getTime();
		long dias = diferenciaEn_ms / (1000 * 60 * 60 * 24);
	}*/
	
	
	   public Integer getNumeroDiasEmpleado(String IDE_GEEDP,String IDE_ASVAC,String fecha_ingreso_asvac,String fecha_finiquito_asvac){
			
		    //	TablaGenerica tab_periodo = utilitario.consultar("select ide_gtemp,fecha_ingreso_asvac,fecha_finiquito_asvac,activo_asvac,ide_asvac from asi_vacacion "
		    //			+ "where ide_gtemp="+IDE_GEEDP+" AND IDE_ASVAC="+IDE_ASVAC+" order by ide_asvac desc limit 1"); 
		       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			//	TablaGenerica tab_partida = getPartida(IDE_GEEDP);

			       
		    	
				
		       String dateInString = "";
		       boolean activo_asvac=false;
			      int sumaValor=0;
		       if (fecha_ingreso_asvac==null || fecha_ingreso_asvac.equals("") || fecha_ingreso_asvac.isEmpty()) {
		    	   return 0;
		       }else {
		    	   dateInString=fecha_ingreso_asvac;
		       }
		       
		      
		       String fecha_finiquito=null;
		       
		       	 int diaFin=0;
		    		 int mesFin=0; // 0 Enero, 11 Diciembre
		    		 int anioFin=0;
			        
		    
		        
		   		Date date = null;
		   		Date dateFechaCalculoFiniquito = null;
		    		/**
		    		 * Le asigno a una variable de tipo date la fecha de ingreso
		    		 */
		    			
		    /*		try {
		    			date = sdf.parse(dateInString);
		    		} catch (ParseException e) {
		    			// TODO Auto-generated catch block
		    			e.printStackTrace();
		    		}
		    		
		    	*/	
		    		
		   			
		       	 try {
					if (fecha_finiquito_asvac==null || fecha_finiquito_asvac.isEmpty()){
					   		try {
					    			date = sdf.parse(dateInString);
					   		       } catch (ParseException e) {
					    			// TODO Auto-generated catch block
					    			e.printStackTrace();
					    		}
			        		
			        		Calendar calendarFin = Calendar.getInstance();
			        		calendarFin.setTime(utilitario.getDate());
			        		 diaFin = calendarFin.get(Calendar.DAY_OF_MONTH);
			        		 mesFin = calendarFin.get(Calendar.MONTH) + 1; // 0 Enero, 11 Diciembre
			        		 anioFin = calendarFin.get(Calendar.YEAR);
					activo_asvac=true;
					
					}else{
					if (activo_asvac==false){
						   fecha_finiquito = fecha_finiquito_asvac;
						   
						  if (utilitario.getDia(fecha_finiquito)==31) {
							   fecha_finiquito=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha_finiquito), -1));
												
						   }
						   
						   if (utilitario.getMes(fecha_finiquito)==2 || utilitario.getMes(fecha_finiquito)==02) {
							
					
						   if (utilitario.getDia(fecha_finiquito)==29) {
							   //fecha_finiquito=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha_finiquito), 1));
												sumaValor=1;
						   }
						   
						   
						   if (utilitario.getDia(fecha_finiquito)==28) {
							//fecha_finiquito=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha_finiquito), 1));
												sumaValor=2;
						   }
						   
							}
		    		try {
		    			date = sdf.parse(dateInString);
								dateFechaCalculoFiniquito = sdf.parse(fecha_finiquito);
						   	
		    		} catch (ParseException e) {
		    			// TODO Auto-generated catch block
		    			//e.printStackTrace();
		    			System.out.println();
		    		}
		    		

							Calendar calendarFin = Calendar.getInstance();
		        			calendarFin.setTime(dateFechaCalculoFiniquito);
		        			 diaFin = calendarFin.get(Calendar.DAY_OF_MONTH);
		            		 mesFin = calendarFin.get(Calendar.MONTH) + 1; // 0 Enero, 11 Diciembre
		            		 anioFin = calendarFin.get(Calendar.YEAR);
						
						
					}else if (activo_asvac==true){
						try {
			    			date = sdf.parse(dateInString);
			   		//		dateFechaCalculoInicial = sdf.parse(dateInStringFechaCalculoInicial);
			    	   	
			   		       } catch (ParseException e) {
			    			// TODO Auto-generated catch block
			    			e.printStackTrace();
			    		}
			       		
			   		
					
					
	        		
	        		Calendar calendarFin = Calendar.getInstance();
	        		calendarFin.setTime(utilitario.getDate());
	        		 diaFin = calendarFin.get(Calendar.DAY_OF_MONTH);
	        		 mesFin = calendarFin.get(Calendar.MONTH) + 1; // 0 Enero, 11 Diciembre
	        		 anioFin = calendarFin.get(Calendar.YEAR);
						
						
					}else {
						utilitario.agregarMensajeError("No contiene Vacaciones", "No se pudo encontrar un registro para el empleado");
					}
					
						
	}

						
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		       		
		
				
		    		
		
		    		Calendar calendarInicio = Calendar.getInstance();
	        		calendarInicio.setTime(date);
	        		int diaInicio = calendarInicio.get(Calendar.DAY_OF_MONTH);
	        		int mesInicio = calendarInicio.get(Calendar.MONTH) + 1; // 0 Enero, 11 Diciembre
	        		int anioInicio = calendarInicio.get(Calendar.YEAR);
		    		
	        		int anios = 0;
	        		int mesesPorAnio = 0;
	        		int diasPorMes = 0;
	        		int diasTipoMes = 30;

	        			if (mesInicio <= mesFin) {
	        				anios = anioFin - anioInicio;
	        				if (diaInicio <= diaFin) {
	        					mesesPorAnio = mesFin - mesInicio;
	        					diasPorMes = diaFin - diaInicio;
	        				} else {
	        					if (mesFin == mesInicio) {
	        						anios = anios - 1;
	        					}
	        					mesesPorAnio = (mesFin - mesInicio - 1 + 12) % 12;
	        					diasPorMes = diasTipoMes - (diaInicio - diaFin);
	        				}
				    	}else{
	        				anios = anioFin - anioInicio - 1;
	        				//System.out.println(anios);
	        				if (diaInicio > diaFin) {
	        					mesesPorAnio = mesFin - mesInicio - 1 + 12;
	        					diasPorMes = diasTipoMes - (diaInicio - diaFin);
	        				} else {
	        					mesesPorAnio = mesFin - mesInicio + 12;
	        					diasPorMes = diaFin - diaInicio;
				    	}
					}
	        		
	               			
	               		//System.out.println("Han transcurrido " + anios + " Años, " + mesesPorAnio + " Meses y " + diasPorMes + " Días.");

	               		//System.out.println("Han transcurrido total dias "+((mesesPorAnio*30)+diasPorMes));		
	               	 

	        	 
	        		int returnValue = -1;
	        				returnValue = anios * 12 + mesesPorAnio;
	               				//System.out.println("Total meses: " + returnValue + " Meses.");

	        				if (activo_asvac==false){
	        					returnValue =(((returnValue*30)+diasPorMes))+1+sumaValor;
	               				}else{
	               					returnValue =(((returnValue*30)+diasPorMes))+1;
	               				}
	               				//System.out.println("Total dias: " + returnValue);
	        				 return returnValue;
		    		 
		    		


		    }
		    
	

	public String getFechaIngresoEmpleado(String IDE_GEEDP) {

		TablaGenerica tab_periodo = utilitario.consultar("SELECT IDE_GTEMP,FECHA_INGRESO_GRUPO_GTEMP FROM GTH_EMPLEADO WHERE IDE_GTEMP in (select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where ACTIVO_GEEDP=TRUE AND IDE_GEEDP=" + IDE_GEEDP + ")");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateInString = tab_periodo.getValor("FECHA_INGRESO_GRUPO_GTEMP");
		return dateInString;
	}

	// CALCULO DE NUMERO DE DIAS

	/**
	 * calcular numero de dias calendario que el empleado lleva en la empresa
	 * 
	 * @param fechaIngresoEmpleado
	 * @return
	 */
	public Integer getNumeroDiasEmpleado(String IDE_GEEDP) {

		// TablaGenerica tab_periodo =
		// utilitario.consultar("SELECT IDE_GTEMP,FECHA_INGRESO_GRUPO_GTEMP FROM GTH_EMPLEADO WHERE IDE_GTEMP in (select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where ACTIVO_GEEDP=TRUE AND IDE_GEEDP="
		// + IDE_GEEDP + ")");
		// String dateInString = "2016-02-02";

		TablaGenerica tab_periodo = utilitario.consultar("select ide_gtemp,fecha_ingreso_asvac,fecha_finiquito_asvac,activo_asvac from asi_vacacion where  "
				+ "ide_gtemp=" + IDE_GEEDP+" and activo_asvac=true");

		
		String fecha_ingreso_continuidad="";
		TablaGenerica tab_vacacion_continuidad = utilitario.consultar("select ide_asvac,fecha_ingreso_asvac,ide_gtemp,nro_dias_ajuste_periodo_asvac,es_continuidad "
				+ "from asi_vacacion where ide_gtemp=" + utilitario.getVariable("IDE_GTEMP")+" and activo_asvac=false "
				+ "order by ide_asvac desc");
		if (tab_vacacion_continuidad.getTotalFilas()>0) {
			for (int i = 0; i < tab_vacacion_continuidad.getTotalFilas(); i++) {
				if ( tab_vacacion_continuidad.getValor(i,"es_continuidad")==null || tab_vacacion_continuidad.getValor(i,"es_continuidad").equals("false")) {
				//SI TIENE CONTINUIDAD
					if (tab_vacacion_continuidad.getTotalFilas()==1) {
					fecha_ingreso_continuidad=tab_periodo.getValor("fecha_ingreso_asvac");
					}else {
						fecha_ingreso_continuidad=tab_periodo.getValor("fecha_ingreso_asvac");

					}
					
					break;
				}else{
				//SI ES LIQUIDACION
					fecha_ingreso_continuidad=tab_vacacion_continuidad.getValor(i,"fecha_ingreso_asvac");
				}
				
			}
		}else {
			fecha_ingreso_continuidad=tab_periodo.getValor("fecha_ingreso_asvac");
		}
		
		
		//String dateInString = pckUtilidades.CConversion.CStr(tab_periodo.getValor("fecha_ingreso_asvac"));
		String dateInString = pckUtilidades.CConversion.CStr(fecha_ingreso_continuidad);
		String fecha_finiquito = pckUtilidades.CConversion.CStr(tab_periodo.getValor("fecha_finiquito_asvac"));

		int diaFin = 0;
		int mesFin = 0; // 0 Enero, 11 Diciembre
		int anioFin = 0;

		Date date = utilitario.DeStringADate(dateInString);
		Date dateFechaCalculoFiniquito = null;
		int returnValue = -1;
		String fecha_finiquito_="";
		Calendar calendarFin = Calendar.getInstance();
		
		if(fecha_finiquito.length()<=4)
		{
			calendarFin.setTime(utilitario.getDate());	
			fecha_finiquito_=utilitario.getFechaActual();
		}
		else
		{
			dateFechaCalculoFiniquito=utilitario.DeStringADate(fecha_finiquito);
			calendarFin.setTime(dateFechaCalculoFiniquito);
			fecha_finiquito_=utilitario.DeDateAString(dateFechaCalculoFiniquito);
		}
		
	      int sumaValor=0;

		  if (utilitario.getDia(fecha_finiquito_)==31) {
			   fecha_finiquito=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha_finiquito_), -1));
								sumaValor=-1;
		   }
		   
		   if (utilitario.getMes(fecha_finiquito_)==2 || utilitario.getMes(fecha_finiquito_)==02) {
			
	
		   if (utilitario.getDia(fecha_finiquito_)==29) {
			   //fecha_finiquito=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha_finiquito), 1));
								sumaValor=1;
		   }
		   
		   
		   if (utilitario.getDia(fecha_finiquito_)==28) {
			//fecha_finiquito=getFechaAsyyyyMMdd(utilitario.sumarDiasFecha(getFechaAsyyyyMMdd(fecha_finiquito), 1));
								sumaValor=2;
		   }
		}
		
		diaFin = calendarFin.get(Calendar.DAY_OF_MONTH);
		mesFin = calendarFin.get(Calendar.MONTH) + 1; // 0 Enero, 11  Diciembre
		anioFin = calendarFin.get(Calendar.YEAR);

		Calendar calendarInicio = Calendar.getInstance();
		calendarInicio.setTime(date);
		int diaInicio = calendarInicio.get(Calendar.DAY_OF_MONTH);
		int mesInicio = calendarInicio.get(Calendar.MONTH) + 1; // 0 Enero, 11  Diciembre
		int anioInicio = calendarInicio.get(Calendar.YEAR);

		int anios = 0;
		int mesesPorAnio = 0;
		int diasPorMes = 0;
		int diasTipoMes = 30;

		if (mesInicio <= mesFin) {
			anios = anioFin - anioInicio;
			if (diaInicio <= diaFin) {
				mesesPorAnio = mesFin - mesInicio;
				diasPorMes = diaFin - diaInicio;
			} else {
				if (mesFin == mesInicio) {
					anios = anios - 1;
				}
				mesesPorAnio = (mesFin - mesInicio - 1 + 12) % 12;
				diasPorMes = diasTipoMes - (diaInicio - diaFin);
			}
		} else {
			anios = anioFin - anioInicio - 1;
			if (diaInicio > diaFin) {
				mesesPorAnio = mesFin - mesInicio - 1 + 12;
				diasPorMes = diasTipoMes - (diaInicio - diaFin);
			} else {
				mesesPorAnio = mesFin - mesInicio + 12;
				diasPorMes = diaFin - diaInicio;
			}
		}
		
		returnValue = anios * 12 + mesesPorAnio;
		returnValue = ((((returnValue * 30) + diasPorMes)) + 1)+sumaValor;
		
		if(returnValue<=0)
			utilitario.agregarMensajeError("No contiene Vacaciones", "No se pudo encontrar un registro para el empleado");

		return returnValue;
	}

	// CALCULO DE NUMERO DE DIAS

	/**
	 * calcula el numero de dias calendario que el empleado lleva en la empresa
	 * desde la fecha ingreso hasta el 30 de abril del 2017
	 * 
	 * @param fechaIngresoEmpleado
	 * @return
	 */
	public Integer getNumeroDiasEmpleadoCalculoInicial(String IDE_GEEDP) {
		// TablaGenerica tab_periodo =
		// utilitario.consultar("SELECT IDE_GTEMP,FECHA_INGRESO_GRUPO_GTEMP FROM GTH_EMPLEADO WHERE IDE_GTEMP in (select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where ACTIVO_GEEDP=TRUE AND IDE_GEEDP="
		// + IDE_GEEDP + ")");
		TablaGenerica tab_periodo = utilitario.consultar("select ide_gtemp,fecha_ingreso_asvac,fecha_finiquito_asvac from asi_vacacion where ide_gtemp=" + IDE_GEEDP+" "
				+ "AND ACTIVO_ASVAC=TRUE ORDER BY IDE_ASVAC DESC LIMIT 1");
		//and nro_dias_ajuste_asvac!=null or nro_dias_ajuste_asvac>0.0
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateInString = tab_periodo.getValor("fecha_ingreso_asvac");
		String dateInStringFechaCalculoInicial = "2017-05-30";

		Date date = null;
		// Fecha calculo de vacaciones hasta el 30 de abril
		Date dateFechaCalculoInicial = null;
		/**
		 * Le asigno a una variable de tipo date la fecha de ingreso
		 */

		try {
			date = sdf.parse(dateInString);
			dateFechaCalculoInicial = sdf.parse(dateInStringFechaCalculoInicial);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Calendar calendarInicio = Calendar.getInstance();
		calendarInicio.setTime(date);
		int diaInicio = calendarInicio.get(Calendar.DAY_OF_MONTH);
		int mesInicio = calendarInicio.get(Calendar.MONTH) + 1; // 0 Enero, 11
																// Diciembre
		int anioInicio = calendarInicio.get(Calendar.YEAR);

		// Fecha fin
		Calendar calendarFin = Calendar.getInstance();
		calendarFin.setTime(dateFechaCalculoInicial);
		int diaFin = calendarFin.get(Calendar.DAY_OF_MONTH);
		int mesFin = calendarFin.get(Calendar.MONTH) + 1; // 0 Enero, 11
															// Diciembre
		int anioFin = calendarFin.get(Calendar.YEAR);

		int anios = 0;
		int mesesPorAnio = 0;
		int diasPorMes = 0;
		int diasTipoMes = 0;
		diasTipoMes = 30;
		// Calculo de días del mes

		if (mesInicio <= mesFin) {
			anios = anioFin - anioInicio;
			if (diaInicio <= diaFin) {
				mesesPorAnio = mesFin - mesInicio;
				diasPorMes = diaFin - diaInicio;
			} else {
				if (mesFin == mesInicio) {
					anios = anios - 1;
				}
				mesesPorAnio = (mesFin - mesInicio - 1 + 12) % 12;
				diasPorMes = diasTipoMes - (diaInicio - diaFin);
			}
		} else {
			anios = anioFin - anioInicio - 1;
			if (diaInicio > diaFin) {
				mesesPorAnio = mesFin - mesInicio - 1 + 12;
				diasPorMes = diasTipoMes - (diaInicio - diaFin);
			} else {
				mesesPorAnio = mesFin - mesInicio + 12;
				diasPorMes = diaFin - diaInicio;
			}
		}
		int returnValue = -1;
		returnValue = anios * 12 + mesesPorAnio;
		returnValue = (((returnValue * 30) + diasPorMes) + 1);
		return returnValue;
	}

	/**
	 * Metodo para obtener los periodos
	 * 
	 * @param IDE_GEEDP
	 * @return
	 */

	public Object getPeriodosVacacionesPrueba(String IDE_GEEDP) {
		TablaGenerica tab_periodo = utilitario.consultar("SELECT IDE_ASVAC,FECHA_INGRESO_ASVAC FROM ASI_VACACION WHERE IDE_GTEMP in (select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP=" + IDE_GEEDP + ") AND ACTIVO_ASVAC=true");
		if (!tab_periodo.isEmpty()) {

			return utilitario
					.getConexion()
					.consultar(
							"SELECT IDE_ASVAC," + "DIA_ACUMULADO," + "NRO_DIAS_ADICIONAL," + "DIA_DESCONTADO,DIA_SOLICITADO,"
									+ " DIA_ACUMULADO+NRO_DIAS_ADICIONAL as NRO_TOTALES_VACACIONES, (DIA_ACUMULADO+NRO_DIAS_ADICIONAL)-(DIA_DESCONTADO+DIA_SOLICITADO) AS DIAS_PENDIENTES  " + " FROM (  "
									+ " SELECT IDE_ASVAC,(case when sum(DIA_ACUMULADO_ASDEV) is null then 0 else sum(DIA_ACUMULADO_ASDEV) end)AS DIA_ACUMULADO,  "
									+ " (case when SUM(DIA_ADICIONAL_ASDEV) is null then 0 else SUM(DIA_ADICIONAL_ASDEV) end) as NRO_DIAS_ADICIONAL,  "
									+ " (case when SUM(DIA_DESCONTADO_ASDEV) is null then 0 else SUM(DIA_DESCONTADO_ASDEV) end)AS DIA_DESCONTADO,  "
									+ " (case when SUM(DIA_SOLICITADO_ASDEV) is null then 0 else SUM(DIA_SOLICITADO_ASDEV) end)AS DIA_SOLICITADO  " + " FROM ASI_DETALLE_VACACION WHERE ACTIVO_ASDEV = true GROUP BY IDE_ASVAC  " + " ) a where IDE_ASVAC="
									+ tab_periodo.getValor("IDE_ASVAC")).get(0);
		}
		return null;
	}

	// PERIODOS DE EMPLEADO DE ACUERDO A LA FECHA DE INGRESO

	public static List<Integer> getPeriodos(Integer nda, Integer nde) {
		List<Integer> peridos = new ArrayList<Integer>();
		Integer indicePeriodo = 1;
		Integer ndeAux = nde;
		do {
			ndeAux = ndeAux - nda;
			peridos.add(indicePeriodo);
			indicePeriodo++;
		} while (ndeAux > 0);
		return peridos;
	}

	// VACACIONES POR PERIODOS
	public static List<Double> getVacacionesXPeriodo(Integer nda, Integer nde, List<Integer> peridos, double numeroDiasVacacionXAnio) {
		List<Double> vaxacionXPeriodo = new ArrayList<Double>();
		Integer ndeAux = nde;
		for (Integer periodo : peridos) {
			if (ndeAux >= nda) {
				ndeAux = nde - (periodo * nda);
				vaxacionXPeriodo.add(numeroDiasVacacionXAnio);
			} else {
				double numdiasUltimoperiodo = (ndeAux * numeroDiasVacacionXAnio) / nda;
				vaxacionXPeriodo.add(numdiasUltimoperiodo);
			}
		}
		return vaxacionXPeriodo;
	}

	// DIAS TOMADOS POR VACACION

	public static List<Double> getVacacionesTomadasXPeriodo(Double numeroDiasTomados, List<Integer> peridos, Double numeroDiasVacacionXAnio) {
		List<Double> vaxacionXPeriodo = new ArrayList<Double>();
		double ndeAux = numeroDiasTomados;
		int valorVacacionesPeriodo=0;
		for (Integer periodo : peridos) {
			if (valorVacacionesPeriodo==peridos.size()) {
				//vaxacionXPeriodo.add(ndeAux);
				if (ndeAux >= 0){
					vaxacionXPeriodo.add(ndeAux);
					}
					else{
						vaxacionXPeriodo.add(0.0);
					}
			}else{
			if (ndeAux >= numeroDiasVacacionXAnio) {
				ndeAux = numeroDiasTomados - (periodo * numeroDiasVacacionXAnio.intValue());
				vaxacionXPeriodo.add(numeroDiasVacacionXAnio);
			} else {
				if (ndeAux >= 0) {
					vaxacionXPeriodo.add(ndeAux);
					ndeAux = numeroDiasTomados - (periodo * numeroDiasVacacionXAnio.intValue());
				} else {
					vaxacionXPeriodo.add(0.0);
				}
			}
		}
		}
		return vaxacionXPeriodo;

	}

	
	

	/**
	 * Metodo que crea la
	 * 
	 * @param vacacionesPeriodo
	 * @param vacacionesTomadas
	 * @return
	 */

	public static List<Double[]> getVacacionesMatriz(List<Double> vacacionesPeriodo, List<Double> vacacionesTomadas) {
		List<Double[]> matriz = new ArrayList<Double[]>();

		Integer dimension = vacacionesPeriodo.size();
		for (Integer i = 0; i < dimension; i++) {
			Double[] obj = new Double[5];
			obj[0] = i.doubleValue() + 1; // periodo
			obj[1] = vacacionesPeriodo.get(i); // dias vacacion x periodo
			// obj[1] = Math.round( obj[1] * 100.0 ) / 100.0;
			obj[2] = vacacionesTomadas.get(i); // dias vacacion tomadas x
												// periodo
			// obj[2] = Math.round( obj[2] * 100.0 ) / 100.0;
			obj[3] = vacacionesPeriodo.get(i) - vacacionesTomadas.get(i); // dias
																			// vacacion
																			// penientes
																			// x
																			// periodo
			// obj[3] = Math.round( obj[3] * 100.0 ) / 100.0;
			obj[4] = vacacionesPeriodo.get(i) + vacacionesPeriodo.get(i);
			// obj[4] = Math.round( obj[4] * 100.0 ) / 100.0;
			matriz.add(obj);
		}
		return matriz;
	}

	
	public static List<Double[]> getVacacionesMatrizExtra_(List<Double> vacacionesPeriodo, List<Double> vacacionesTomadas,int dias_extra) {
		List<Double[]> matriz = new ArrayList<Double[]>();

		Integer dimension = vacacionesPeriodo.size();
		for (Integer i = 0; i < dimension; i++) {
			Double[] obj = new Double[5];
			obj[0] = i.doubleValue() + 1; // periodo
			obj[1] = vacacionesPeriodo.get(i); // dias vacacion x periodo
			// obj[1] = Math.round( obj[1] * 100.0 ) / 100.0;
			obj[2] = Double.parseDouble(getFormatoNumero((vacacionesTomadas.get(i).doubleValue()),2)); // dias vacacion tomadas x
												// periodo
			// obj[2] = Math.round( obj[2] * 100.0 ) / 100.0;
			if ((dimension-1)==i) {
			obj[3] = Double.parseDouble(getFormatoNumero((vacacionesPeriodo.get(i) - vacacionesTomadas.get(i)+dias_extra),2));
			}else{
				obj[3] = Double.parseDouble(getFormatoNumero((vacacionesPeriodo.get(i) - vacacionesTomadas.get(i)),2));
			} // dias
																			// vacacion
																			// penientes
																			// x
																			// periodo
			// obj[3] = Math.round( obj[3] * 100.0 ) / 100.0;
			if ((dimension-1)==i) {
				obj[4] =  Double.parseDouble(getFormatoNumero((vacacionesPeriodo.get(i) + vacacionesPeriodo.get(i)+dias_extra),2));
			}else{
			obj[4] =  Double.parseDouble(getFormatoNumero((vacacionesPeriodo.get(i) + vacacionesPeriodo.get(i)),2));}
			// obj[4] = Math.round( obj[4] * 100.0 ) / 100.0;
			matriz.add(obj);
		}
		return matriz;
	}
	
	
	public  List<Double[]> getVacacionesMatrizExtra(String ide_asvac) {
	
		TablaGenerica tab_periodo = utilitario.consultar("SELECT  ide_asvre, ide_asvac, periodo_asvre, nro_dias_vacacion_asvre, "
				+ "nro_dias_tomados_asvre, nro_dias_pendientes_asvre, activo_asvre, "
				+ "ide_periodo_asvre, base_imponible_asvre, dia_extra_asvre "
				+ "FROM asi_vacacion_resumen_empleado "
				+ "WHERE IDE_ASVAC in("+ide_asvac+")");
		
		
		List<Double[]> matriz = new ArrayList<Double[]>();

		Integer dimension = tab_periodo.getTotalFilas();
		for (Integer i = 0; i < dimension; i++) {
			Double[] obj = new Double[5];
			obj[0] = i.doubleValue() + 1; // periodo
			obj[1] = Double.parseDouble(tab_periodo.getValor(i,"nro_dias_vacacion_asvre")); // dias vacacion x periodo
			// obj[1] = Math.round( obj[1] * 100.0 ) / 100.0;
			obj[2] = Double.parseDouble(tab_periodo.getValor(i,"nro_dias_tomados_asvre")); // dias vacacion tomadas x
												// periodo
			// obj[2] = Math.round( obj[2] * 100.0 ) / 100.0;
			/*if ((dimension-1)==i) {
			obj[3] = Double.parseDouble(getFormatoNumero((vacacionesPeriodo.get(i) - vacacionesTomadas.get(i)+dias_extra),2));
			}else{
				obj[3] = Double.parseDouble(getFormatoNumero((vacacionesPeriodo.get(i) - vacacionesTomadas.get(i)),2));
			} */// dias
																			// vacacion
																			// penientes
																			// x
																			// periodo
			// obj[3] = Math.round( obj[3] * 100.0 ) / 100.0;
			
			obj[3] = Double.parseDouble(tab_periodo.getValor(i,"nro_dias_pendientes_asvre"));
			if (tab_periodo.getValor(i,"dia_extra_asvre")==null) {
				obj[4] =  0.00;
			}else{
				obj[4] =  Double.parseDouble(tab_periodo.getValor(i,"dia_extra_asvre"));
			}

			//if ((dimension-1)==i) {
				//obj[4] =  Double.parseDouble(getFormatoNumero((vacacionesPeriodo.get(i) + vacacionesPeriodo.get(i)+dias_extra),2));
			//}else{
			//obj[4] =  Double.parseDouble(getFormatoNumero((vacacionesPeriodo.get(i) + vacacionesPeriodo.get(i)),2));}
			// obj[4] = Math.round( obj[4] * 100.0 ) / 100.0;
			matriz.add(obj);
		}
		return matriz;
	}
	
	
	/**
	 * 
	 * @param fechaIngresoEmpleado
	 * @return
	 */

	public Double getNumeroDiasPendientes(int nde, int nda, double numeroDiasTomados, double numeroDiasVacacionXAnio) {

		List<Integer> peridos = getPeriodos(nda, nde);
		List<Double> vacacionesPeriodo = getVacacionesXPeriodo(nda, nde, peridos, numeroDiasVacacionXAnio);
		List<Double> vacacionesTomadas = getVacacionesTomadasXPeriodo(numeroDiasTomados, peridos, numeroDiasVacacionXAnio);

		List<Double[]> matriz = getVacacionesMatriz(vacacionesPeriodo, vacacionesTomadas);
		Double diasPendientes = 0.0;
		for (Double[] vacaciones : matriz) {
			diasPendientes = diasPendientes + vacaciones[3];

		}
		return diasPendientes;
	}

	/**
	 * Obtengo el numero de dias
	 * 
	 * @param IDE_GEEDP
	 * @return
	 */

	public BigDecimal getNumeroDiasUltimoPermiso(String IDE_GEEDP) {

		List tab_consulta = utilitario.getConexion().consultar(
				"select ide_gtemp,(sum(nro_horas_aspvh)/8)as dias  " + "from asi_permisos_vacacion_hext  " + "where ide_gtemp=22 and fecha_solicitud_aspvh between '01-01-2017' and '30-12-2017' " + "group by ide_gtemp");

		BigDecimal resultado = (BigDecimal) (tab_consulta.get(0));
		return resultado;
	}

	public String servicioCodigoMaximo(String tabla, String ide_primario) {

		String maximo = "Select 1 as ide,(case when max(" + ide_primario + ") is null then 0 else max(" + ide_primario + ") end) + 1 as codigo from " + tabla;
		return maximo;
	}

	/*
	 * Metodo devuelve los dias solicitados con cargo a vacacion de cada
	 * empleado
	 */
	public double getNumeroDiasTomadosFinSemana(String IDE_GEEDP,String IDE_ASVAC) {
		double numeroDiasTomadoss = 0.0;
		TablaGenerica tab_periodo = utilitario.consultar("SELECT IDE_ASVAC,FECHA_INGRESO_ASVAC FROM ASI_VACACION WHERE IDE_GTEMP =" + IDE_GEEDP + " AND ACTIVO_ASVAC=true AND "
				+ "IDE_ASVAC in("+IDE_ASVAC+")");
		if (!tab_periodo.isEmpty()) {
			/*TablaGenerica tab_numero_tomados = utilitario.consultar("SELECT IDE_ASVAC,DIA_DESCONTADO " + "FROM ( " + "SELECT IDE_ASVAC,(case when sum(DIA_ACUMULADO_ASDEV) is null then 0 else sum(DIA_ACUMULADO_ASDEV) end)AS DIA_ACUMULADO, "
					+ "(case when SUM(DIA_DESCONTADO_ASDEV) is null then 0 else SUM(DIA_DESCONTADO_ASDEV) end)AS DIA_DESCONTADO " + "FROM ASI_DETALLE_VACACION WHERE ACTIVO_ASDEV = true and fin_semana_asdev=true GROUP BY IDE_ASVAC "
					+ " ) a where IDE_ASVAC=" +IDE_ASVAC);*/
			
			TablaGenerica tab_numero_tomados = utilitario.consultar("select vac.ide_gtemp,sum(dev.DIA_DESCONTADO_ASDEV) as DIA_DESCONTADO "
					+ "from asi_vacacion vac "
					+ "left join ASI_DETALLE_VACACION dev on dev.ide_asvac=vac.ide_asvac "
					+ "where vac.ide_asvac in("+IDE_ASVAC+") and  dev.fin_semana_asdev=true "
					+ "group by vac.ide_gtemp");


			if (tab_numero_tomados.getValor("DIA_DESCONTADO") == null || tab_numero_tomados.getValor("DIA_DESCONTADO").isEmpty()) {
				numeroDiasTomadoss = 0.0;
			} else {
				numeroDiasTomadoss = Double.parseDouble(tab_numero_tomados.getValor("DIA_DESCONTADO"));

			}
			return numeroDiasTomadoss;

		}
		return 0;

	}

    private Date getFechaAsyyyyMMdd(String fecha){
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    Date fechaDate = new Date();
	    try {	
	    	fechaDate = df.parse(fecha);
	    	return fechaDate;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return null;

    }
    private String getFechaAsyyyyMMdd(Date fecha){
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    return df.format(fecha);
    }
   
	private static  String getFormatoNumero(Object numero, int numero_decimales) {
		String lstr_formato = "#";
		for (int i = 0; i < numero_decimales; i++) {
			if (i == 0) {
				lstr_formato += ".";
			}
			lstr_formato += "#";
		}
		DecimalFormat formatoNumero;
		DecimalFormatSymbols idfs_simbolos = new DecimalFormatSymbols();
		idfs_simbolos.setDecimalSeparator('.');
		formatoNumero = new DecimalFormat(lstr_formato, idfs_simbolos);
		try {
			double ldob_valor = Double.parseDouble(numero.toString());
			return formatoNumero.format(ldob_valor);
		} catch (Exception ex) {
			return null;
		}
	}
	
	
	public String guardarMarcacionesBiometrico(ConBiometricoMarcaciones solicitud) {
		EntityManager manejador = fabrica.createEntityManager();
		try {
			utx.begin();
			manejador.joinTransaction();
			//solicitud.setIdeCorel(manejador.find(ConReloj.class, 15));
			//if (solicitud.getIdeCobim() == null) {
				//TablaGenerica tab_biometrico = utilitario.consultar(servicioCodigoMaximo("con_biometrico_marcaciones", "IDE_COBIM"));
				//String ideaspvh = tab_biometrico.getValor("codigo");
				//int codigo_ideaspvh = Integer.parseInt(ideaspvh);
				//solicitud.setIdeCobim(codigo_ideaspvh); // maximo de utilitario
//				manejador.persist(solicitud);
				//			} else {
				//manejador.merge(solicitud);
				manejador.persist(solicitud);
			//}
			utx.commit();
		} catch (Exception e) {
			try {
				utx.rollback();
			} catch (Exception e1) {
			}
			return e.getMessage();
		} finally {
			manejador.close();
		}
		return "";
	}
}
