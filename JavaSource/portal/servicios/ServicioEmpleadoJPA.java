/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.servicios;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EnterpriseBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.inject.New;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import framework.aplicacion.TablaGenerica;
import paq_sistema.aplicacion.Utilitario;
import portal.entidades.GenDivisionPolitica;
import portal.entidades.GenEmpleadosDepartamentoPar;
import portal.entidades.GthCargasFamiliares;
import portal.entidades.GthCargo;
import portal.entidades.GthConyuge;
import portal.entidades.GthCorreo;
import portal.entidades.GthDireccion;
import portal.entidades.GthEmpleado;
import portal.entidades.GthEstadoCivil;
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

/**
 *
 * @author DELL-USER
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class ServicioEmpleadoJPA {

    private Utilitario utilitario = new Utilitario();
    @PersistenceUnit(unitName="sampuData")
    private EntityManagerFactory fabrica;
    @Resource
    private UserTransaction utx;

    public GthEmpleado getEmpleado(String ideGtemp) {
        EntityManager manejador = fabrica.createEntityManager();
        try {
            Query q = manejador.createQuery("SELECT g FROM GthEmpleado g WHERE g.ideGtemp=" + ideGtemp);
            return (GthEmpleado) q.getSingleResult();
        } catch (Exception e) {
            System.out.println("ERROR getEmpleado : " + e.getMessage());
        } finally {
            manejador.close();
        }
        return null;
    }

    
    public SriImpuestoRenta getImpuesto(String detalleSrimr) {
        EntityManager manejador = fabrica.createEntityManager();
		String sql="SELECT s FROM SriImpuestoRenta s WHERE s.detalleSrimr like'" + detalleSrimr+"'";
        try {
            Query q = manejador.createQuery(sql);
            return (SriImpuestoRenta) q.getSingleResult();
        } catch (Exception e) {
            System.out.println("ERROR getImpuesto : " + e.getMessage());
			System.out.println("SQL: "+sql);
        } finally {
            manejador.close();
        }
        return null;
    }
    
    public List<GthTelefono> getTelefonos(String ideGtemp) {
        EntityManager manejador = fabrica.createEntityManager();
        try {
            Query q = manejador.createQuery("SELECT g FROM GthTelefono g WHERE g.ideGtemp.ideGtemp =" + ideGtemp);
            return q.getResultList();
        } catch (Exception e) {
        } finally {
            manejador.close();
        }
        return null;
    }

    public List getTiposTelefono() {

        return utilitario.getConexion().consultar("select IDE_GTTIT,DETALLE_GTTIT from GTH_TIPO_TELEFONO");
    }

  
    /**
     * Metodo devuelve los gastos ingresados por el empleado
     * @param ideGtemp
     * @return
     */
    public List<SriDeduciblesEmpleado> getDeducibles(String ideGtemp) {
        EntityManager manejador = fabrica.createEntityManager();
        StringBuilder str_ide= new StringBuilder();
        TablaGenerica tabGasto=utilitario.consultar("SELECT ide_srded,ide_srimr  "
        		+ "FROM sri_deducibles "
        		+ "WHERE IDE_SRIMR IN(select IDE_SRIMR from sri_impuesto_renta  "
        		+ "where detalle_srimr like '%"+utilitario.getAnio(utilitario.getFechaActual())+"%' order by ide_srimr desc limit 1) "
        		+ "order by ide_srded");
        int contador=0;
        for (int i = 0; i < tabGasto.getTotalFilas(); i++) {
			str_ide.append(tabGasto.getValor(i,"IDE_SRDED"));
			contador++;
			if (tabGasto.getTotalFilas()==1) {
				
			}else if (contador==tabGasto.getTotalFilas()) {
				
			}else {
				str_ide.append(",");
			}
		}
        
        try {
            Query q = manejador.createQuery("SELECT s FROM SriDeduciblesEmpleado s WHERE s.ideGtemp.ideGtemp =" + ideGtemp + " "
            		+ "AND s.ideSrded.ideSrded in("+str_ide.toString()+") and s.activoSrdee=true");
            return q.getResultList();
        } catch (Exception e) {
        } finally {
            manejador.close();
        }
        return null;
    }
    
    /**
     * Metodo devuelve todos los tipos de gasto deducible existentes
     * @return
     */
    public List getTiposDeducibles() {
    	return utilitario.getConexion().consultar("SELECT ide_srded, detalle_srded  "
    			+ "FROM sri_deducibles  "
    			+ "WHERE IDE_SRIMR IN "
    			+ "(SELECT IDE_SRIMR FROM SRI_IMPUESTO_RENTA WHERE DETALLE_SRIMR LIKE '%"+utilitario.getAnio(utilitario.getFechaActual())+"%' order by ide_srimr desc limit 1)");

    }

    
    public List getPaises() {
        return utilitario.getConexion().consultar("select IDE_GEDIP,DETALLE_GEDIP from GEN_DIVISION_POLITICA DP where DP.IDE_GETDP in (1) order by DP.DETALLE_GEDIP ASC ");
    }

    public List getProvincias(String idePais) {
        //Pner variable en el  0 y 1
        return utilitario.getConexion().consultar("select dp.IDE_GEDIP,dp.DETALLE_GEDIP as provincia from "
        		+ "GEN_DIVISION_POLITICA DP "
        		+ "INNER JOIN GEN_DIVISION_POLITICA DP1 ON DP1.IDE_GEDIP=DP.GEN_IDE_GEDIP "
        		+ "where DP.IDE_GETDP in (2) and DP1.IDE_GEDIP=" + idePais + " order by DP.DETALLE_GEDIP ASC");
    }

    public List getCiudades(String ideProvincia) {
        //Pner variable en el  3
        return utilitario.getConexion().consultar("select DP1.IDE_GEDIP, "
                + "DP1.DETALLE_GEDIP AS CIUDAD_VIVE "
                + "from GEN_DIVISION_POLITICA DP "
                + "INNER JOIN GEN_TIPO_DIVISION_POLITICA TDP ON TDP.IDE_GETDP=DP.IDE_GETDP "
                + "INNER JOIN GEN_DIVISION_POLITICA DP1 ON DP1.IDE_GEDIP=DP.GEN_IDE_GEDIP "
                + "inner join GEN_DIVISION_POLITICA DP2 ON DP2.IDE_GEDIP=DP1.GEN_IDE_GEDIP "
                + "INNER join GEN_DIVISION_POLITICA DP3 ON DP3.IDE_GEDIP=DP2.GEN_IDE_GEDIP "
                + "INNER join GEN_DIVISION_POLITICA DP4 ON DP4.IDE_GEDIP=DP3.GEN_IDE_GEDIP "
                + "AND TDP.NIVEL_GETDP IN (5) where DP3.IDE_GEDIP=" + ideProvincia + " group by DP1.IDE_GEDIP, DP1.DETALLE_GEDIP "
                + "order by DP1.DETALLE_GEDIP ASC");
    }

    public List getParroquias(String ideCiudad) {
        /*//Pner variable en el  3        
        */return utilitario.getConexion().consultar("select DP.IDE_GEDIP, DP.DETALLE_GEDIP AS PARROQUIA_VIVE from GEN_DIVISION_POLITICA DP "
        		+ "INNER JOIN GEN_TIPO_DIVISION_POLITICA TDP ON TDP.IDE_GETDP=DP.IDE_GETDP "
        		+ "INNER JOIN GEN_DIVISION_POLITICA DP1 ON DP1.IDE_GEDIP=DP.GEN_IDE_GEDIP "
        		+ "inner join GEN_DIVISION_POLITICA DP2 ON DP2.IDE_GEDIP=DP1.GEN_IDE_GEDIP "
        		+ "INNER join GEN_DIVISION_POLITICA DP3 ON DP3.IDE_GEDIP=DP2.GEN_IDE_GEDIP "
        		+ "INNER join GEN_DIVISION_POLITICA DP4 ON DP4.IDE_GEDIP=DP3.GEN_IDE_GEDIP 				"
        		+ "AND TDP.NIVEL_GETDP IN (5) WHERE DP1.IDE_GEDIP =" + ideCiudad + " order by DP.DETALLE_GEDIP ASC");
    	

    }

    public GthCorreo getCorreoInstitucional(String ideGtemp) {
        EntityManager manejador = fabrica.createEntityManager();
        try {
            Query q = manejador.createQuery("SELECT g FROM GthCorreo g WHERE g.ideGtemp.ideGtemp =" + ideGtemp + " and g.notificacionGtcor=1");
            if (q.getResultList().size() > 0) {
                return (GthCorreo) q.getResultList().get(0);
            }
            return (GthCorreo) q.getSingleResult();
        } catch (Exception e) {
        } finally {
            manejador.close();
        }
        return null;
    }
    public SriFormulario107 getCampoFormulario(String ideGtemp) {
        EntityManager manejador = fabrica.createEntityManager();
        try {
            Query q = manejador.createQuery("SELECT s FROM SriFormulario107 s WHERE s.ideGtemp.ideGtemp =" + ideGtemp + " and s.tipoFormularioSrfor=2");
            if (q.getResultList().size() > 0) {
                return (SriFormulario107) q.getResultList().get(0);
            }
            return (SriFormulario107) q.getSingleResult();
        } catch (Exception e) {
        } finally {
            manejador.close();
        }
        return null;
    }


    public GthCorreo getCorreoPersonal(String ideGtemp) {
        EntityManager manejador = fabrica.createEntityManager();
        try {
        	//Query q = manejador.createQuery("SELECT g FROM GthCorreo g WHERE g.ideGtemp.ideGtemp =" + ideGtemp + " and s.tipoFormularioSrfor=0");
        	Query q = manejador.createQuery("SELECT g FROM GthCorreo g WHERE  g.ideGtemp.ideGtemp =" + ideGtemp + " and g.notificacionGtcor =false");
            if (q.getResultList().size() > 0) {
                return (GthCorreo) q.getResultList().get(0);
            }
            return (GthCorreo) q.getSingleResult();
        } catch (Exception e) {
        } finally {
            manejador.close();
        }
        return null;
    }

    
    public GthConyuge getConyuque(String ideGtemp) {
        EntityManager manejador = fabrica.createEntityManager();
        try {
            Query q = manejador.createQuery("SELECT g FROM GthConyuge g WHERE g.ideGtemp.ideGtemp =" + ideGtemp);
            return (GthConyuge) q.getSingleResult();
        } catch (Exception e) {
        } finally {
            manejador.close();
        }
        return null;
    }

    public GthDireccion getDireccionEmpleado(String ideGtemp) {
        EntityManager manejador = fabrica.createEntityManager();
        try {
            Query q = manejador.createQuery("SELECT g FROM GthDireccion g WHERE g.ideGtemp.ideGtemp =" + ideGtemp);
            if (q.getResultList().size() > 0) {
                return (GthDireccion) q.getResultList().get(0);
            }
            return (GthDireccion) q.getSingleResult();
        } catch (Exception e) {
        } finally {
            manejador.close();
        }
        return null;
    }

    public GenDivisionPolitica getDivisionPolitica(String ideGedip) {
        EntityManager manejador = fabrica.createEntityManager();
        try {
            Query q = manejador.createNamedQuery("GenDivisionPolitica.findByIdeGedip");
            q.setParameter("ideGedip", new BigDecimal(ideGedip));
            return (GenDivisionPolitica) q.getSingleResult();
        } catch (Exception e) {
        } finally {
            manejador.close();
        }
        return null;
    }

    public GthTipoTelefono getTipoTelefono(String ideGttit) {
        EntityManager manejador = fabrica.createEntityManager();
        try {
            Query q = manejador.createNamedQuery("GthTipoTelefono.findByIdeGttit");
            q.setParameter("ideGttit", new BigDecimal(ideGttit));
            return (GthTipoTelefono) q.getSingleResult();
        } catch (Exception e) {
        } finally {
            manejador.close();
        }
        return null;
    }

    public SriDeducibles getTipoGastoDeducible(String ideSrded) {
        EntityManager manejador = fabrica.createEntityManager();
        try {
            Query q = manejador.createNamedQuery("SriDeducibles.findByIdeSrded");
            int ide_srded=Integer.parseInt(ideSrded);
            q.setParameter("ideSrded",new Integer(ide_srded));

            return (SriDeducibles) q.getSingleResult();
        } catch (Exception e) {
        } finally {
            manejador.close();
        }
        return null;
    }
    
    public GthTipoParentescoRelacion getTipoParentesco(String ideGttpr) {
        EntityManager manejador = fabrica.createEntityManager();
        try {
            Query q = manejador.createNamedQuery("GthTipoParentescoRelacion.findByIdeGttpr");
            q.setParameter("ideGttpr", new BigDecimal(ideGttpr));
            return (GthTipoParentescoRelacion) q.getSingleResult();
        } catch (Exception e) {
        } finally {
            manejador.close();
        }
        return null;
    }

    public GthTipoDocumentoIdentidad getTipoDocumentoIdentidad(String ideGttdi) {
        EntityManager manejador = fabrica.createEntityManager();
        try {
            Query q = manejador.createNamedQuery("GthTipoDocumentoIdentidad.findByIdeGttdi");
            q.setParameter("ideGttdi", new BigDecimal(ideGttdi));
            return (GthTipoDocumentoIdentidad) q.getSingleResult();
        } catch (Exception e) {
        } finally {
            manejador.close();
        }
        return null;
    }

    public GthGenero getGenero(String ideGtgen) {
        EntityManager manejador = fabrica.createEntityManager();
        try {
            Query q = manejador.createNamedQuery("GthGenero.findByIdeGtgen");
            q.setParameter("ideGtgen", new BigDecimal(ideGtgen));
            return (GthGenero) q.getSingleResult();
        } catch (Exception e) {
        } finally {
            manejador.close();
        }
        return null;
    }

    public GthEstadoCivil getEstadoCivil(String ideGtesc) {
        EntityManager manejador = fabrica.createEntityManager();
        try {
            Query q = manejador.createNamedQuery("GthEstadoCivil.findByIdeGtesc");
            q.setParameter("ideGtesc", new BigDecimal(ideGtesc));
            return (GthEstadoCivil) q.getSingleResult();
        } catch (Exception e) {
        } finally {
            manejador.close();
        }
        return null;
    }

    public GthCargo getCargo(String ideGtcar) {
        EntityManager manejador = fabrica.createEntityManager();
        try {
            Query q = manejador.createNamedQuery("GthCargo.findByIdeGtcar");
            q.setParameter("ideGtcar", new BigDecimal(ideGtcar));
            return (GthCargo) q.getSingleResult();
        } catch (Exception e) {
        } finally {
            manejador.close();
        }
        return null;
    }

    public GthNacionalidad getNacionalidad(String ideGtnac) {
        EntityManager manejador = fabrica.createEntityManager();
        try {
            Query q = manejador.createNamedQuery("GthNacionalidad.findByIdeGtnac");
            q.setParameter("ideGtnac", new BigDecimal(ideGtnac));
            return (GthNacionalidad) q.getSingleResult();
        } catch (Exception e) {
        } finally {
            manejador.close();
        }
        return null;
    }

    public String guardarTelefono(GthTelefono telefono) {
        EntityManager manejador = fabrica.createEntityManager();
        try {
            utx.begin();
            TablaGenerica usuario=utilitario.consultar("select ide_usua,nom_usua,nick_usua from sis_usuario where ide_usua="+utilitario.getVariable("IDE_USUA"));
            if (telefono.getIdeGttel() == null) {
                //asignar maximo
            	long ideaspvh=new Long(utilitario.getConexion().getMaximo("GTH_TELEFONO", "IDE_GTTEL", 1));
            	System.out.println("guardar telefono "+ideaspvh);
            	Integer conertideaspvh = (int) ideaspvh;
                telefono.setIdeGttel(conertideaspvh); //maximo de utilitario
                telefono.setUsuarioIngre(usuario.getValor("nick_usua"));
                telefono.setFechaIngre(utilitario.DeStringADate(utilitario.getFechaActual()));
                telefono.setHoraIngre(utilitario.getHora(utilitario.getHoraActual()));
                telefono.setActivoGttel(true);
                System.out.println("telefono "+telefono);
                manejador.persist(telefono);
            } else {
                telefono.setHoraActua(utilitario.getHora(utilitario.getHoraActual()));
                telefono.setUsuarioActua(usuario.getValor("nick_usua"));
                telefono.setFechaActua(utilitario.DeStringADate(utilitario.getFechaActual()));
                manejador.merge(telefono);
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

    
    
    public String guardarGastosDeducibles(SriDeduciblesEmpleado deducibles) {
        EntityManager manejador = fabrica.createEntityManager();
        try {
            utx.begin();
            if (deducibles.getIdeSrdee() == null) {
                //asignar maximoide_srdee
            	long ideaspvh=new Long(utilitario.getConexion().getMaximo("SRI_DEDUCIBLES_EMPLEADO", "IDE_SRDEE", 1));
            	System.out.println("guardar DEDUCIBLES "+ideaspvh);
            	Integer conertideaspvh = (int) (long) ideaspvh;	 // (int) ideaspvh;
            	deducibles.setIdeSrdee(conertideaspvh); //maximo de utilitario
                System.out.println("DEDUCIBLES "+deducibles);
                TablaGenerica tab_usuario=utilitario.consultar("select * from sis_usuario where ide_gtemp="+utilitario.getVariable("IDE_GTEMP"));
                deducibles.setActivoSrdee(true);
                deducibles.setUsuarioIngre(tab_usuario.getValor("NICK_USUA"));
                deducibles.setFechaIngre(utilitario.DeStringADate(utilitario.getFechaActual()));
                deducibles.setHoraIngre((utilitario.DeStringAHora(utilitario.getHoraActual())));
                manejador.persist(deducibles);
            } else {

                manejador.merge(deducibles);
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
    
    
    

    public String eliminarTelefono(String telefono) {
        EntityManager manejador = fabrica.createEntityManager();
        try {
            utx.begin();
            GthTelefono borraTelefono = manejador.find(GthTelefono.class, new Integer(telefono));
            manejador.remove(borraTelefono);
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

    
    public String eliminarGasto(String gastoDeducible) {
        EntityManager manejador = fabrica.createEntityManager();
        try {
            utx.begin();
            SriDeduciblesEmpleado borraGastoDeducible = manejador.find(SriDeduciblesEmpleado.class, new Integer(gastoDeducible));
            manejador.remove(borraGastoDeducible);
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

    
    public GenEmpleadosDepartamentoPar getEmpleadoDepartamentoPartida(String ideGtemp) {
        EntityManager manejador = fabrica.createEntityManager();
        try {
            Query q = manejador.createQuery("SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.ideGtemp.ideGtemp =" + ideGtemp + " and g.activoGeedp=true");
            return (GenEmpleadosDepartamentoPar) q.getSingleResult();
        } catch (Exception e) {
        } finally {
            manejador.close();
        }
        return null;
    }

    public String eliminarCargaFamiliar(String carga) {
        EntityManager manejador = fabrica.createEntityManager();
        try {
            utx.begin();
            GthCargasFamiliares borraCarga = manejador.find(GthCargasFamiliares.class, new BigDecimal(carga));
            manejador.remove(borraCarga);
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

    public String guardarDireccion(GthDireccion direccion) {
        EntityManager manejador = fabrica.createEntityManager();
        TablaGenerica usuario=utilitario.consultar("select ide_usua,nom_usua,nick_usua from sis_usuario where ide_usua="+utilitario.getVariable("IDE_USUA"));
        try {
            utx.begin();
            manejador.joinTransaction();
            //Guarda o modifica direccion            
            if (direccion.getIdeGtdir() == null) {
            	long idegtdir=new Long(utilitario.getConexion().getMaximo("GTH_DIRECCION", "IDE_GTDIR", 1));
          	  Integer conertideaspvh= (int) idegtdir;
                direccion.setIdeGtdir(conertideaspvh); //maximo de utilitario
                direccion.setUsuarioIngre(usuario.getValor("nick_usua"));
                direccion.setFechaIngre(utilitario.DeStringADate(utilitario.getFechaActual()));
                direccion.setHoraIngre(utilitario.getHora(utilitario.getHoraActual()));
                manejador.persist(direccion);
            } else {
            	 direccion.setUsuarioActua(usuario.getValor("nick_usua"));
                 direccion.setFechaActua(utilitario.DeStringADate(utilitario.getFechaActual()));
                 direccion.setHoraActua(utilitario.getHora(utilitario.getHoraActual()));
                manejador.merge(direccion);
               
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

    public String guardarEmpleado(GthEmpleado empleado) {
        EntityManager manejador = fabrica.createEntityManager();
        try {
            utx.begin();
            manejador.joinTransaction();
            manejador.merge(empleado);
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

    public String guardarCorreo(GthCorreo correoInstitucional, GthCorreo correoPersonal) {
        EntityManager manejador = fabrica.createEntityManager();
        try {
            utx.begin();
            manejador.joinTransaction();
            TablaGenerica usuario=utilitario.consultar("select ide_usua,nom_usua,nick_usua from sis_usuario where ide_usua="+utilitario.getVariable("IDE_USUA"));

            //Guarda Correos
            if (correoInstitucional.getIdeGtcor() == null) {
            	 long idegtcor=new Long(utilitario.getConexion().getMaximo("GTH_CORREO", "IDE_GTCOR", 1));
                   	  Integer conertideaspvh= (int) idegtcor;
                correoInstitucional.setIdeGtcor(conertideaspvh); //maximo de utilitario
                correoInstitucional.setUsuarioIngre(usuario.getValor("nick_usua"));
                correoInstitucional.setFechaIngre(utilitario.DeStringADate(utilitario.getFechaActual()));
                correoInstitucional.setHoraIngre(utilitario.getHora(utilitario.getHoraActual()));
                correoInstitucional.setNotificacionGtcor(true);
                correoInstitucional.setActivoGtcor(true);
                manejador.persist(correoInstitucional);
               
            } else {
            	TablaGenerica correo=utilitario.consultar("SELECT ide_gtcor, ide_gtemp, ide_geben, detalle_gtcor, activo_gtcor, "
            			+ "notificacion_gtcor  "
            			+ "FROM gth_correo  "
            			+ "where ide_gtemp="+utilitario.getVariable("IDE_GTEMP")+"  and notificacion_gtcor=true");
            	if (correoInstitucional.getDetalleGtcor().equals(correo.getValor("detalle_gtcor"))) {
					
				}else{
                correoInstitucional.setUsuarioActua(usuario.getValor("nick_usua"));
                correoInstitucional.setFechaActua(utilitario.DeStringADate(utilitario.getFechaActual()));
                correoInstitucional.setHoraActua(utilitario.getHora(utilitario.getHoraActual()));
                manejador.merge(correoInstitucional);}
            }

            if (correoPersonal.getIdeGtcor() == null) {
                //si hay valor en el correo guardo
                if (correoPersonal.getDetalleGtcor() != null && !correoPersonal.getDetalleGtcor().isEmpty()) {
                	 long idegtcor=new Long(utilitario.getConexion().getMaximo("GTH_CORREO", "IDE_GTCOR", 1));
                      Integer conertideaspvh= (int) idegtcor;
                    correoPersonal.setIdeGtcor(conertideaspvh); //maximo de utilitario
                    correoPersonal.setActivoGtcor(false);
                    correoPersonal.setNotificacionGtcor(false);
                    correoPersonal.setUsuarioIngre(usuario.getValor("nick_usua"));
                    correoPersonal.setFechaIngre(utilitario.DeStringADate(utilitario.getFechaActual()));
                    correoPersonal.setHoraIngre(utilitario.getHora(utilitario.getHoraActual()));
                    manejador.persist(correoPersonal);
                }
            } else {
                //si hay valor en el correo modifico
                if (correoPersonal.getDetalleGtcor() != null && !correoPersonal.getDetalleGtcor().isEmpty()) {
                	TablaGenerica correo=utilitario.consultar("SELECT ide_gtcor, ide_gtemp, ide_geben, detalle_gtcor, activo_gtcor, "
                			+ "notificacion_gtcor  "
                			+ "FROM gth_correo  "
                			+ "where ide_gtemp="+utilitario.getVariable("IDE_GTEMP")+"  and notificacion_gtcor=false");
                	if (correoPersonal.getDetalleGtcor().equals(correo.getValor("detalle_gtcor"))) {
    					
    				}else{
                	
                	correoPersonal.setUsuarioActua(usuario.getValor("nick_usua"));
                    correoPersonal.setFechaActua(utilitario.DeStringADate(utilitario.getFechaActual()));
                    correoPersonal.setHoraActua(utilitario.getHora(utilitario.getHoraActual()));
                    correoPersonal.setActivoGtcor(false);
                    correoPersonal.setNotificacionGtcor(false);
                    manejador.merge(correoPersonal);}
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

    
    
    public String guardarFormulario(SriFormulario107 formulario107) {
        EntityManager manejador = fabrica.createEntityManager();
        try { 
            utx.begin();
            manejador.joinTransaction();
            //Guarda Formularios
            if (formulario107.getIdeSrfor() == null) {
            	 long idesrfor=new Long(utilitario.getConexion().getMaximo("sri_formulario_107", "ide_srfor", 1));
                   	  Integer conertideaspvh= (int) idesrfor;
                formulario107.setIdeSrfor(conertideaspvh); //maximo de utilitario
                manejador.persist(formulario107);
               
            } else {
                manejador.merge(formulario107);
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
    
    
    
    
    
    
    
    public String guardarCargaFamiliar(GthCargasFamiliares carga) {
        EntityManager manejador = fabrica.createEntityManager();
        try {
            utx.begin();
            manejador.joinTransaction();
            //Guarda o modifica 
            TablaGenerica usuario=utilitario.consultar("select ide_usua,nom_usua,nick_usua from sis_usuario where ide_usua="+utilitario.getVariable("IDE_USUA"));
            if (carga.getIdeGtcaf() == null) {
            	long idegtcaf=new Long(utilitario.getConexion().getMaximo("GTH_CARGAS_FAMILIARES", "IDE_GTCAF", 1));
             	  Integer conertideaspvh= (int) idegtcaf;
                carga.setIdeGtcaf(conertideaspvh);
                carga.setUsuarioIngre(usuario.getValor("NICK_USUA"));
                carga.setFechaIngre(utilitario.DeStringADate(utilitario.getFechaActual()));
                carga.setHoraIngre(utilitario.getHora(utilitario.getHoraActual()));

                manejador.persist(carga);
            } else {
                carga.setUsuarioActua(usuario.getValor("NICK_USUA"));
                carga.setFechaActua(utilitario.DeStringADate(utilitario.getFechaActual()));
                carga.setHoraActua(utilitario.getHora(utilitario.getHoraActual()));
                manejador.merge(carga);
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

    public String guardarConyugue(GthConyuge conyugue, GthTelefono telefono) {
        EntityManager manejador = fabrica.createEntityManager();
        TablaGenerica usuario=utilitario.consultar("select ide_usua,nom_usua,nick_usua from sis_usuario where ide_usua="+utilitario.getVariable("IDE_USUA"));
        try {
            utx.begin();
            manejador.joinTransaction();
            //Guarda o modifica 
            if (conyugue.getIdeGtcon() == null) {
            	long idegtcon=new Long(utilitario.getConexion().getMaximo("GTH_CONYUGE", "IDE_GTCON", 1));
           	  Integer conertideaspvh= (int) idegtcon;
                conyugue.setIdeGtcon(conertideaspvh);
                conyugue.setUsuarioIngre(usuario.getValor("NICK_USUA"));
                conyugue.setFechaIngre(utilitario.DeStringADate(utilitario.getFechaActual()));
                conyugue.setHoraIngre(utilitario.getHora(utilitario.getHoraActual()));        
                manejador.persist(conyugue);
                telefono.setIdeGtcon(conyugue);
                long idegttel=new Long(utilitario.getConexion().getMaximo("GTH_TELEFONO", "IDE_GTTEL", 1));
             	  Integer conertideidegttel= (int) idegttel;
                telefono.setIdeGttel(conertideidegttel);
                telefono.setUsuarioIngre(usuario.getValor("NICK_USUA"));
                telefono.setFechaIngre(utilitario.DeStringADate(utilitario.getFechaActual()));
                telefono.setHoraIngre(utilitario.getHora(utilitario.getHoraActual()));
                manejador.persist(telefono);
            } else {
                conyugue.setUsuarioActua(usuario.getValor("NICK_USUA"));
                conyugue.setFechaActua(utilitario.DeStringADate(utilitario.getFechaActual()));
                conyugue.setHoraActua(utilitario.getHora(utilitario.getHoraActual()));
                manejador.merge(conyugue);
                //Si el telefono es nuevo
                if (telefono.getIdeGttel() == null) {
                    telefono.setIdeGtcon(conyugue);
                    long idegttel=new Long(utilitario.getConexion().getMaximo("GTH_TELEFONO", "IDE_GTTEL", 1));
               	  Integer conertideidegttel= (int) idegttel;
                    telefono.setIdeGttel(conertideidegttel);
                    telefono.setUsuarioIngre(usuario.getValor("NICK_USUA"));
                    telefono.setFechaIngre(utilitario.DeStringADate(utilitario.getFechaActual()));
                    telefono.setHoraIngre(utilitario.getHora(utilitario.getHoraActual()));
                    manejador.persist(telefono);
                } else {
                    telefono.setUsuarioActua(usuario.getValor("NICK_USUA"));
                    telefono.setFechaActua(utilitario.DeStringADate(utilitario.getFechaActual()));
                    telefono.setHoraActua(utilitario.getHora(utilitario.getHoraActual()));
                    manejador.merge(telefono);
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

    public String guardarConyugueUnionLibre(GthConyuge conyugue, GthTelefono telefono, GthUnionLibre union) {
        EntityManager manejador = fabrica.createEntityManager();
        try {
            utx.begin();
            manejador.joinTransaction();
            TablaGenerica usuario=utilitario.consultar("select ide_usua,nom_usua,nick_usua from sis_usuario where ide_usua="+utilitario.getVariable("IDE_USUA"));

            //Guarda o modifica 
            if (conyugue.getIdeGtcon() == null) {
            	long idegtcon=new Long(utilitario.getConexion().getMaximo("GTH_CONYUGE", "IDE_GTCON", 1));
             	  Integer conertideaspvh= (int) idegtcon;
                conyugue.setIdeGtcon(conertideaspvh);
                conyugue.setUsuarioIngre(usuario.getValor("NICK_USUA"));
                conyugue.setFechaIngre(utilitario.DeStringADate(utilitario.getFechaActual()));
                conyugue.setHoraIngre(utilitario.getHora(utilitario.getHoraActual()));
                manejador.persist(conyugue);
                telefono.setIdeGtcon(conyugue);
                telefono.setUsuarioIngre(usuario.getValor("NICK_USUA"));
                telefono.setFechaIngre(utilitario.DeStringADate(utilitario.getFechaActual()));
                telefono.setHoraIngre(utilitario.getHora(utilitario.getHoraActual()));
                long idegttel=new Long(utilitario.getConexion().getMaximo("GTH_TELEFONO", "IDE_GTTEL", 1));
           	  Integer conertidegttel= (int) idegttel;
               telefono.setIdeGttel(conertidegttel);
                manejador.persist(telefono);
                long idegtunl=new Long(utilitario.getConexion().getMaximo("GTH_UNION_LIBRE", "IDE_GTUNL", 1));
             	  Integer conertidegtunl= (int) idegtunl;
                union.setIdeGtunl(conertidegtunl);
                union.setIdeGtcon(conyugue);
                union.setUsuarioIngre(usuario.getValor("NICK_USUA"));
                union.setFechaIngre(utilitario.DeStringADate(utilitario.getFechaActual()));
                union.setHoraIngre(utilitario.getHora(utilitario.getHoraActual()));
                manejador.persist(union);
            } else {
                conyugue.setUsuarioActua(usuario.getValor("NICK_USUA"));
                conyugue.setFechaActua(utilitario.DeStringADate(utilitario.getFechaActual()));
                conyugue.setHoraActua(utilitario.getHora(utilitario.getHoraActual()));
                manejador.merge(conyugue);
                //Si el telefono es nuevo
                if (telefono.getIdeGttel() == null) {
                    telefono.setIdeGtcon(conyugue);
                    long idegttel=new Long(utilitario.getConexion().getMaximo("GTH_TELEFONO", "IDE_GTTEL", 1));
               	  Integer conertidegttel= (int) idegttel;
                    telefono.setIdeGttel(conertidegttel);
                    telefono.setUsuarioIngre(usuario.getValor("NICK_USUA"));
                    telefono.setFechaIngre(utilitario.DeStringADate(utilitario.getFechaActual()));
                    telefono.setHoraIngre(utilitario.getHora(utilitario.getHoraActual()));
                    manejador.persist(telefono);
                } else {
                    telefono.setUsuarioActua(usuario.getValor("NICK_USUA"));
                    telefono.setFechaActua(utilitario.DeStringADate(utilitario.getFechaActual()));
                    telefono.setHoraActua(utilitario.getHora(utilitario.getHoraActual()));
                    manejador.merge(telefono);
                }
                //Union libre
                if (union.getIdeGtunl() == null) {
                	 long idegttel=new Long(utilitario.getConexion().getMaximo("GTH_UNION_LIBRE", "IDE_GTUNL", 1));
                  	  Integer conertidegttel= (int) idegttel;
                    union.setIdeGtunl(conertidegttel);
                    union.setIdeGtcon(conyugue);
                    union.setUsuarioIngre(usuario.getValor("NICK_USUA"));
                    union.setFechaIngre(utilitario.DeStringADate(utilitario.getFechaActual()));
                    union.setHoraIngre(utilitario.getHora(utilitario.getHoraActual()));
                    manejador.persist(union);
                } else {
                    manejador.merge(union);
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

    public List<GthCargasFamiliares> getCargasFamiliares(String ideGtemp) {
        EntityManager manejador = fabrica.createEntityManager();
        try {
            Query q = manejador.createQuery("SELECT g FROM GthCargasFamiliares g WHERE g.ideGtemp.ideGtemp =" + ideGtemp);
            return q.getResultList();
        } catch (Exception e) {
        } finally {
            manejador.close();
        }
        return null;
    }

    public GthUnionLibre getUnionLibre(String ideGtcon) {
        EntityManager manejador = fabrica.createEntityManager();
        try {
            Query q = manejador.createQuery("SELECT g FROM GthUnionLibre g WHERE g.ideGtcon.ideGtcon =" + ideGtcon);
            return (GthUnionLibre) q.getSingleResult();
        } catch (Exception e) {
        } finally {
            manejador.close();
        }
        return null;
    }

    public String eliminarConyugue(String ideGtemp) {
        EntityManager manejador = fabrica.createEntityManager();
        try {
            GthConyuge conygue = getConyuque(ideGtemp);
            if (conygue != null) {
                utx.begin();
                manejador.joinTransaction();
                //Borra telefono de conyugue
                List<GthTelefono> telefonos = getListaTelefonoConyugue(conygue.getIdeGtcon().toString());
                if (telefonos != null && !telefonos.isEmpty()) {
                    for (GthTelefono telefonoActual : telefonos) {
                        manejador.remove(telefonoActual);
                    }
                }
                //Borra datos de union
                GthUnionLibre union = getUnionLibre(conygue.getIdeGtcon().toString());
                if (union != null) {
                    manejador.remove(union);
                }
                manejador.remove(conygue);
                utx.commit();
            }

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

    public GthTelefono getTelefonoConyugue(String ideGtcon) {
        EntityManager manejador = fabrica.createEntityManager();
        try {
            Query q = manejador.createQuery("SELECT g FROM GthTelefono g WHERE g.ideGtcon.ideGtcon =" + ideGtcon);
            return (GthTelefono) q.getSingleResult();
        } catch (Exception e) {
        } finally {
            manejador.close();
        }
        return null;
    }

    public List<GthTelefono> getListaTelefonoConyugue(String ideGtcon) {
        EntityManager manejador = fabrica.createEntityManager();
        try {
            Query q = manejador.createQuery("SELECT g FROM GthTelefono g WHERE g.ideGtcon.ideGtcon =" + ideGtcon);
            return q.getResultList();
        } catch (Exception e) {
        } finally {
            manejador.close();
        }
        return null;
    }

    public List getGeneros() {
        return utilitario.getConexion().consultar("select IDE_GTGEN,DETALLE_GTGEN from GTH_GENERO  order by DETALLE_GTGEN");
    }

    public List getTiposParentesco() {
       // return utilitario.getConexion().consultar("select IDE_GTTPR,DETALLE_GTTPR from GTH_TIPO_PARENTESCO_RELACION where DEPENDIENTE_GTTPR=true order by DETALLE_GTTPR");
    	 return utilitario.getConexion().consultar("select IDE_GTTPR,DETALLE_GTTPR from GTH_TIPO_PARENTESCO_RELACION  order by DETALLE_GTTPR");
    }

    public List getTiposDocumentoIdentidad() {
        return utilitario.getConexion().consultar("select IDE_GTTDI,DETALLE_GTTDI from GTH_TIPO_DOCUMENTO_IDENTIDAD  order by DETALLE_GTTDI");
    }

    public List getEstadosCiviles() {
        return utilitario.getConexion().consultar("select IDE_GTESC,DETALLE_GTESC from GTH_ESTADO_CIVIL  order by DETALLE_GTESC");
    }

    public List getCargos() {
        return utilitario.getConexion().consultar("select IDE_GTCAR,DETALLE_GTCAR from GTH_CARGO  order by DETALLE_GTCAR");
    }

    public List getNacionalidades() {
        return utilitario.getConexion().consultar("select IDE_GTNAC,DETALLE_GTNAC from GTH_NACIONALIDAD order by DETALLE_GTNAC");
    }

    public List getActividadesLaborales() {
        return utilitario.getConexion().consultar("select IDE_GTACL,DETALLE_GTACL from GTH_ACTIVIDAD_LABORAL order by DETALLE_GTACL");
    }
    
        
    
}
