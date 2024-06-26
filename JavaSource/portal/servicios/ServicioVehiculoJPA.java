/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.servicios;

import framework.aplicacion.TablaGenerica;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import javax.xml.registry.infomodel.PersonName;

import org.hibernate.validator.util.privilegedactions.GetConstructor;



import paq_sistema.aplicacion.Utilitario;
import portal.entidades.VehSolicitud;
import portal.entidades.VehSolicitudArchivo;
import portal.entidades.VehSolicitudOcupante;
import portal.entidades.VehSolicitudRuta;

/**
 * 
 * @author Alex Becerra
 */

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@ManagedBean
@ViewScoped
public class ServicioVehiculoJPA {

	private Utilitario utilitario = new Utilitario();
	@PersistenceUnit(unitName = "sampuData")
	private EntityManagerFactory fabrica;
	@Resource
	private UserTransaction utx;

	private Integer totalSegundosHoraEntrada = 0;
	private Integer totalSegundosHoraSalida = 612000;

	
	public String guardarSolicitudVehiculo(VehSolicitud solicitud,
			VehSolicitudRuta solicitudRuta, List<VehSolicitudOcupante> listaOcupantes, VehSolicitudArchivo solicitudJustificacion) {

		EntityManager manejador = fabrica.createEntityManager();
		try {
			utx.begin();
			manejador.joinTransaction();
			// solicitud.setIdeAsmot(manejador.find(AsiMotivo.class,
			// solicitud.getIdeAsmot().getIdeAsmot()));
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

			if (solicitud.getIdeVesol() == null) {

				TablaGenerica tab_veh_solicitud = utilitario
						.consultar(servicioCodigoMaximo("VEH_SOLICITUD",
								"IDE_VESOL"));
				String ideaspvh = tab_veh_solicitud.getValor("codigo");
				long codigo_ideaspvh = Long.parseLong(ideaspvh);
				solicitud.setIdeVesol(codigo_ideaspvh); // maximo de utilitario

				// String gen_ide_geedp
				// =tab_veh_solicitud.getValor("gen_ide_geedp");
				// int codigoGenIdeGeedp=Integer.parseInt(gen_ide_geedp);
				// solicitud.setGenIdeGeedp(genIdeGeedp);
				// IdeVesol(codigo_ideaspvh); //maximo de utilitario

				TablaGenerica tab_veh_solicitud_ruta = utilitario
						.consultar(servicioCodigoMaximo("VEH_SOLICITUD_RUTA",
								"IDE_VESRU"));
				String ideaspej = tab_veh_solicitud_ruta.getValor("codigo");
				long codigo_ideaspej = Long.parseLong(ideaspej);
				solicitudRuta.setIdeVesru(codigo_ideaspej);
				// Integer conertideaspej= (int) ideaspej;

				solicitudRuta.setIdeVesol(solicitud);
				solicitudRuta.setIdeVesru(codigo_ideaspej);
				
				TablaGenerica tab_veh_solicitud_archivo = utilitario
						.consultar(servicioCodigoMaximo("veh_solicitud_archivo",
								"ide_vesoa"));
				String ide_vesoa = tab_veh_solicitud_archivo.getValor("codigo");
				int codigo_vesoa = pckUtilidades.CConversion.CInt(ide_vesoa);
				solicitudJustificacion.setIdeVesoa(codigo_vesoa);
				solicitudJustificacion.setIdeVesol(solicitud);
				
				long ide_vesoc=new Long(utilitario.getConexion().getMaximo("veh_solicitud_ocupante", "ide_vesoc", 1));	
			
				manejador.persist(solicitud);
				manejador.persist(solicitudRuta);
				manejador.persist(solicitudJustificacion);
				
				for(VehSolicitudOcupante vehSolicitudOcu :listaOcupantes) {		
					vehSolicitudOcu.setIdeVesoc(pckUtilidades.CConversion.CInt(ide_vesoc+""));
					vehSolicitudOcu.setIdeVesol(solicitud);
					manejador.persist(vehSolicitudOcu);  
					ide_vesoc++;
			    }
				
			}
			utx.commit();
		} catch (Exception e) {
			e.printStackTrace();
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

	public List getGeneraSolicitud(String IDE_GTEMP) {
		return utilitario
				.getConexion()
				.consultar(
						"SELECT EPAR.IDE_GEEDP, "
								+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  "
								+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || "
								+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' || "
								+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS "
								+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR "
								+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP "
								+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
								+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP "
								+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
								+ "WHERE EPAR.ACTIVO_GEEDP=TRUE AND EMP.IDE_GTEMP="
								+ IDE_GTEMP
								+ " "
								+ "group by EPAR.IDE_GEEDP,EMP.APELLIDO_PATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,EMP.APELLIDO_MATERNO_GTEMP order by APELLIDO_PATERNO_GTEMP ");
	}

	public List getNombreSolicitud() {
		return utilitario
				.getConexion()
				.consultar(
						"SELECT EPAR.IDE_GTEMP, "
								+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  "
								+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || "
								+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' || "
								+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS "
								+ "FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR "
								+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP "
								+ "LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU "
								+ "LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP "
								+ "LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE  "
								+ "WHERE EPAR.ACTIVO_GEEDP=TRUE and emp.activo_gtemp=true "
								+ "group by EPAR.IDE_GTEMP,EMP.APELLIDO_PATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP,EMP.APELLIDO_MATERNO_GTEMP order by APELLIDO_PATERNO_GTEMP ");
	}

	public List getDepartamentos(String IDE_GTEMP) {
		return utilitario
				.getConexion()
				.consultar(
						"select par.ide_gtemp,depar.detalle_gedep from gen_empleados_departamento_par par  "
								+ "left join gen_departamento depar on depar.ide_gedep=par.ide_gedep "
								+ "left join gen_area area on area.ide_geare=par.ide_geare "
								+ "where par.activo_geedp and par.ide_gtemp=+"
								+ IDE_GTEMP
								+ " "
								+ "group by  par.ide_gtemp,depar.detalle_gedep "
								+ "order by  par.ide_gtemp");
	}

	// ----------------
	public List<VehSolicitud> getSolicitudesVehiculos(String ideGtemp,
			String columnNameWhere) {
		EntityManager manejador = fabrica.createEntityManager();
		try {
			StringBuilder strSql = new StringBuilder();
			strSql.append("SELECT v FROM VehSolicitud v WHERE v.")
					.append(columnNameWhere).append(".ideGtemp =")
					.append(ideGtemp).append(" order by v.ideVesol desc");
			// System.out.println("srSql "+strSql.toString());
			// Query q = manejador.createQuery(strSql.toString());
			Query q = manejador
					.createQuery("Select v from VehSolicitud v where v.ideGtemp.ideGtemp = "
							+ ideGtemp + "order by a.ideVesol desc");

			return q.getResultList();
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			manejador.close();
		}
		return null;
	}

	// ----------
	public List<VehSolicitud> getSolicitudesVacaciones(String ideGtemp,String columnNameWhere) {
		//System.out.println("getSolicitudesVacaciones el ide"+ideGtemp+ "la clomun"+columnNameWhere);
		EntityManager manejador = fabrica.createEntityManager();

		try {
			StringBuilder strSql = new StringBuilder();
			strSql.append("SELECT v FROM VehSolicitud v WHERE v.")
					.append(columnNameWhere).append(".ideGtemp =")
					.append(ideGtemp).append(" order by v.ideVesol asc");
			System.out.println("getSolicitudesVacaciones srSql " + strSql.toString());
			Query q = manejador.createQuery(strSql.toString());

			return q.getResultList();
		} catch (Exception e) {
			System.out.println("Error getSolicitudesVacaciones "+e.getMessage());
		} finally {
			manejador.close();
		}
		return null;
	}

	/**
	 * 
	 * @param ideGeedp
	 * @param columnNameWhere
	 *            parametro para ser evaluado
	 * @return
	 */

	public List<VehSolicitud> getSolicitudesVacacionesSolicitadas(
			String ideGtemp, String columnNameWhere) {
		EntityManager manejador = fabrica.createEntityManager();
		try {
			StringBuilder strSql = new StringBuilder();

			strSql.append("SELECT a FROM VehSolicitud a WHERE a.")
					.append(columnNameWhere).append(".ideGtemp =")
					.append(ideGtemp).append(" order by a.ideVesol desc");
			// strSql.append("select a from veh_solicitud a where ide_gtemp = "+ideGtemp);

			Query q = manejador.createQuery(strSql.toString());
			return q.getResultList();
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			// aqui se debe implementar el merge o el persist
			manejador.close();
		}
		return null;
	}

	public List getRutaVehiculo() {
		return utilitario
				.getConexion()
				.consultar(
						"select ide_verut,detalle_verut from veh_ruta  where activo_verut=true order by detalle_verut asc");
	}

	public List getTipoVehiculo() {
		return utilitario
				.getConexion()
				.consultar(
						"select ide_vetip,detalle_vetip from veh_tipo_vehiculo  where activo_vetip=true order by detalle_vetip asc");
	}

	public List getEstadoSolicitud() {
		return utilitario
				.getConexion()
				.consultar(
						"select ide_vetes, detalle_vetes from veh_solicitud_estado  where activo_vetes=true order by ide_vetes ");
	}
	
	public List getSucursales() {
		return utilitario
				.getConexion()
				.consultar("SELECT IDE_SUCU,NOM_SUCU FROM SIS_SUCURSAL where IDE_SUCU in (1,10) order by NOM_SUCU ");
	}

	public String servicioCodigoMaximo(String tabla, String ide_primario) {

		String maximo = "Select 1 as ide, max(coalesce(" + ide_primario + ",0)) + 1 as codigo from " + tabla;
		return maximo;
	}

	public double diferenciaHoras(Date horaInicial, Date horaFinal){
		
		double dou_diferencia= utilitario.getDiferenciaHoras(
				utilitario.getHora(utilitario.DeDateAString(horaInicial)),
				utilitario.getHora(utilitario.DeDateAString(horaFinal))); 
				
		return dou_diferencia;
	}
	
	
	public double calcularHoras(Date horaInicial, Date horaFinal) {

		double valor = 0;

		try {
			if (horaFinal == null || horaFinal == null)

				return 0;

			Calendar fechaInicial = Calendar.getInstance();
			fechaInicial.setTime(horaInicial);

			Calendar fechaFinal = Calendar.getInstance();
			fechaFinal.setTime(horaFinal);

			String str_hora_inicial = pckUtilidades.Utilitario.padLeft(fechaInicial.getTime().getHours()+"", 2) + ":"
					+ pckUtilidades.Utilitario.padLeft(fechaInicial.getTime().getMinutes()+"", 2)  + ":00";
			
			String str_hora_final = pckUtilidades.Utilitario.padLeft(fechaFinal.getTime().getHours()+"", 2) + ":"
					+ pckUtilidades.Utilitario.padLeft(fechaFinal.getTime().getMinutes()+"", 2) + ":00";

			//System.out.println("strhora inicial prueba " + str_hora_inicial);
			//System.out.println("strhora final prueba " + str_hora_final);

			Date hora_inicial = utilitario.getHora(utilitario
					.getFormatoHora(str_hora_inicial));
			Date hora_final = utilitario.getHora(utilitario
					.getFormatoHora(str_hora_final));

			if (!utilitario.isHoraMayor(str_hora_inicial, str_hora_final)) {

				int total_segundos_hora_inicial = (hora_inicial.getHours() * 3600)
						+ (hora_inicial.getMinutes() * 60)
						+ hora_inicial.getSeconds();
				int total_segundos_hora_final = (hora_final.getHours() * 3600)
						+ (hora_final.getMinutes() * 60)
						+ hora_final.getSeconds();

				if (total_segundos_hora_inicial < totalSegundosHoraEntrada) {
					total_segundos_hora_inicial = totalSegundosHoraEntrada;
				}
				if (total_segundos_hora_final > totalSegundosHoraSalida) {
					total_segundos_hora_final = totalSegundosHoraSalida;
				}

//				System.out.println("hora inicial "
//						+ total_segundos_hora_inicial);
//				System.out.println("hora final " + total_segundos_hora_final);
				int total_diferencia_segundo = total_segundos_hora_final
						- total_segundos_hora_inicial;

				int total_horas = total_diferencia_segundo / 3600;
				int nuevo_valor = total_diferencia_segundo
						- (total_horas * 3600);
				int total_minutos = nuevo_valor / 60;
				int total_segundos = nuevo_valor - (total_minutos * 60);

				double total_diferencia_segundos = ((total_horas * 3600)
						+ (total_minutos * 60) + total_segundos);
				double total_diferencia_horas = total_diferencia_segundos / 3600;
//				if (total_diferencia_horas > 8)
//					total_diferencia_horas = 8;
//				System.out.println("Horas: " + total_diferencia_horas);
				
				valor= total_diferencia_horas;
				
			} else
				utilitario.agregarMensajeInfo("Horas no válidas", "No se pudo calcular el número de horas ");

		} catch (Exception e) {
			System.out.println("Error calcularHoras servicio: "+ e.getMessage());
			
			utilitario.agregarMensajeInfo("Horas no válidas", "No se pudo calcular el número de horas " + e.getMessage());

		}

		return valor;
	}
	
//	public List<VehSolicitud> getSolicitudes (String ideGtemp){
//		
//		return utilitario.getConexion().consultar("select ide_vesol as ideVesol, fecha_solicitud_vesol as fechaSolicitudVesol, "
//				+"motivo_vesol as motivoVesol, fecha_salida_vesol as fechaSalidaVesol, "
//				+"tiempo_solicitado_vesol as tiempoSolicitadoVesol, ide_vetes as ideVetes "
//				+"from veh_solicitud veh "
//				+"where activo_vesol=true and ide_gtemp = "+ideGtemp+ "order by ide_vesol; ");
//		
//	}
	
	
		public List<VehSolicitud> getSolicitudes (String ideGtemp){
		
			EntityManager manejador = fabrica.createEntityManager();
			
	        try {
	            Query q = manejador.createQuery("SELECT v FROM VehSolicitud v WHERE v.ideGtemp.ideGtemp =" + ideGtemp);
	            return q.getResultList();
	        } catch (Exception e) {
	        } finally {
	            manejador.close();
	        }
	        return null;
		}

}
