/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author awbecerra
 */
@Entity
@Table(name = "poa_2015", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "Poa2015.findAll", query = "SELECT p FROM Poa2015 p"),
    @NamedQuery(name = "Poa2015.findByIdePoa", query = "SELECT p FROM Poa2015 p WHERE p.idePoa = :idePoa"),
    @NamedQuery(name = "Poa2015.findByPrograma", query = "SELECT p FROM Poa2015 p WHERE p.programa = :programa"),
    @NamedQuery(name = "Poa2015.findByProyecto", query = "SELECT p FROM Poa2015 p WHERE p.proyecto = :proyecto"),
    @NamedQuery(name = "Poa2015.findByPresupuesto2015", query = "SELECT p FROM Poa2015 p WHERE p.presupuesto2015 = :presupuesto2015"),
    @NamedQuery(name = "Poa2015.findByMetasDelProyecto", query = "SELECT p FROM Poa2015 p WHERE p.metasDelProyecto = :metasDelProyecto"),
    @NamedQuery(name = "Poa2015.findByProductoObra", query = "SELECT p FROM Poa2015 p WHERE p.productoObra = :productoObra"),
    @NamedQuery(name = "Poa2015.findByFase", query = "SELECT p FROM Poa2015 p WHERE p.fase = :fase"),
    @NamedQuery(name = "Poa2015.findBySubActividad", query = "SELECT p FROM Poa2015 p WHERE p.subActividad = :subActividad"),
    @NamedQuery(name = "Poa2015.findByFechaInicio", query = "SELECT p FROM Poa2015 p WHERE p.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "Poa2015.findByFechaEntregaContraPublica", query = "SELECT p FROM Poa2015 p WHERE p.fechaEntregaContraPublica = :fechaEntregaContraPublica"),
    @NamedQuery(name = "Poa2015.findByFechaFin", query = "SELECT p FROM Poa2015 p WHERE p.fechaFin = :fechaFin"),
    @NamedQuery(name = "Poa2015.findByPartidaPresupustaria", query = "SELECT p FROM Poa2015 p WHERE p.partidaPresupustaria = :partidaPresupustaria"),
    @NamedQuery(name = "Poa2015.findByDetallePartida", query = "SELECT p FROM Poa2015 p WHERE p.detallePartida = :detallePartida"),
    @NamedQuery(name = "Poa2015.findByMunicipio", query = "SELECT p FROM Poa2015 p WHERE p.municipio = :municipio"),
    @NamedQuery(name = "Poa2015.findByHospitalarios", query = "SELECT p FROM Poa2015 p WHERE p.hospitalarios = :hospitalarios"),
    @NamedQuery(name = "Poa2015.findByTasaRecoleccion", query = "SELECT p FROM Poa2015 p WHERE p.tasaRecoleccion = :tasaRecoleccion"),
    @NamedQuery(name = "Poa2015.findByRumninahui", query = "SELECT p FROM Poa2015 p WHERE p.rumninahui = :rumninahui"),
    @NamedQuery(name = "Poa2015.findByEscombreras", query = "SELECT p FROM Poa2015 p WHERE p.escombreras = :escombreras"),
    @NamedQuery(name = "Poa2015.findByMetroQuito", query = "SELECT p FROM Poa2015 p WHERE p.metroQuito = :metroQuito"),
    @NamedQuery(name = "Poa2015.findByConvenios", query = "SELECT p FROM Poa2015 p WHERE p.convenios = :convenios"),
    @NamedQuery(name = "Poa2015.findByOtrosGestores", query = "SELECT p FROM Poa2015 p WHERE p.otrosGestores = :otrosGestores"),
    @NamedQuery(name = "Poa2015.findByTotal", query = "SELECT p FROM Poa2015 p WHERE p.total = :total"),
    @NamedQuery(name = "Poa2015.findByCodigo", query = "SELECT p FROM Poa2015 p WHERE p.codigo = :codigo")})
public class Poa2015 implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ide_poa", nullable = false)
    private Integer idePoa;
    @Size(max = 300)
    @Column(name = "programa", length = 300)
    private String programa;
    @Size(max = 500)
    @Column(name = "proyecto", length = 500)
    private String proyecto;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "presupuesto_2015", precision = 20, scale = 4)
    private BigDecimal presupuesto2015;
    @Size(max = 500)
    @Column(name = "metas_del_proyecto", length = 500)
    private String metasDelProyecto;
    @Size(max = 500)
    @Column(name = "producto_obra", length = 500)
    private String productoObra;
    @Size(max = 500)
    @Column(name = "fase", length = 500)
    private String fase;
    @Size(max = 500)
    @Column(name = "sub_actividad", length = 500)
    private String subActividad;
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Column(name = "fecha_entrega_contra_publica")
    @Temporal(TemporalType.DATE)
    private Date fechaEntregaContraPublica;
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @Column(name = "partida_presupustaria")
    private BigInteger partidaPresupustaria;
    @Size(max = 500)
    @Column(name = "detalle_partida", length = 500)
    private String detallePartida;
    @Column(name = "municipio", precision = 20, scale = 4)
    private BigDecimal municipio;
    @Column(name = "hospitalarios", precision = 20, scale = 4)
    private BigDecimal hospitalarios;
    @Column(name = "tasa_recoleccion", precision = 20, scale = 4)
    private BigDecimal tasaRecoleccion;
    @Column(name = "rumninahui", precision = 20, scale = 4)
    private BigDecimal rumninahui;
    @Column(name = "escombreras", precision = 20, scale = 4)
    private BigDecimal escombreras;
    @Column(name = "metro_quito", precision = 20, scale = 4)
    private BigDecimal metroQuito;
    @Column(name = "convenios", precision = 20, scale = 4)
    private BigDecimal convenios;
    @Column(name = "otros_gestores", precision = 20, scale = 4)
    private BigDecimal otrosGestores;
    @Column(name = "total", precision = 20, scale = 4)
    private BigDecimal total;
    @Column(name = "codigo")
    private BigInteger codigo;

    public Poa2015() {
    }

    public Poa2015(Integer idePoa) {
        this.idePoa = idePoa;
    }

    public Integer getIdePoa() {
        return idePoa;
    }

    public void setIdePoa(Integer idePoa) {
        this.idePoa = idePoa;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }

    public BigDecimal getPresupuesto2015() {
        return presupuesto2015;
    }

    public void setPresupuesto2015(BigDecimal presupuesto2015) {
        this.presupuesto2015 = presupuesto2015;
    }

    public String getMetasDelProyecto() {
        return metasDelProyecto;
    }

    public void setMetasDelProyecto(String metasDelProyecto) {
        this.metasDelProyecto = metasDelProyecto;
    }

    public String getProductoObra() {
        return productoObra;
    }

    public void setProductoObra(String productoObra) {
        this.productoObra = productoObra;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }

    public String getSubActividad() {
        return subActividad;
    }

    public void setSubActividad(String subActividad) {
        this.subActividad = subActividad;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaEntregaContraPublica() {
        return fechaEntregaContraPublica;
    }

    public void setFechaEntregaContraPublica(Date fechaEntregaContraPublica) {
        this.fechaEntregaContraPublica = fechaEntregaContraPublica;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public BigInteger getPartidaPresupustaria() {
        return partidaPresupustaria;
    }

    public void setPartidaPresupustaria(BigInteger partidaPresupustaria) {
        this.partidaPresupustaria = partidaPresupustaria;
    }

    public String getDetallePartida() {
        return detallePartida;
    }

    public void setDetallePartida(String detallePartida) {
        this.detallePartida = detallePartida;
    }

    public BigDecimal getMunicipio() {
        return municipio;
    }

    public void setMunicipio(BigDecimal municipio) {
        this.municipio = municipio;
    }

    public BigDecimal getHospitalarios() {
        return hospitalarios;
    }

    public void setHospitalarios(BigDecimal hospitalarios) {
        this.hospitalarios = hospitalarios;
    }

    public BigDecimal getTasaRecoleccion() {
        return tasaRecoleccion;
    }

    public void setTasaRecoleccion(BigDecimal tasaRecoleccion) {
        this.tasaRecoleccion = tasaRecoleccion;
    }

    public BigDecimal getRumninahui() {
        return rumninahui;
    }

    public void setRumninahui(BigDecimal rumninahui) {
        this.rumninahui = rumninahui;
    }

    public BigDecimal getEscombreras() {
        return escombreras;
    }

    public void setEscombreras(BigDecimal escombreras) {
        this.escombreras = escombreras;
    }

    public BigDecimal getMetroQuito() {
        return metroQuito;
    }

    public void setMetroQuito(BigDecimal metroQuito) {
        this.metroQuito = metroQuito;
    }

    public BigDecimal getConvenios() {
        return convenios;
    }

    public void setConvenios(BigDecimal convenios) {
        this.convenios = convenios;
    }

    public BigDecimal getOtrosGestores() {
        return otrosGestores;
    }

    public void setOtrosGestores(BigDecimal otrosGestores) {
        this.otrosGestores = otrosGestores;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigInteger getCodigo() {
        return codigo;
    }

    public void setCodigo(BigInteger codigo) {
        this.codigo = codigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePoa != null ? idePoa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Poa2015)) {
            return false;
        }
        Poa2015 other = (Poa2015) object;
        if ((this.idePoa == null && other.idePoa != null) || (this.idePoa != null && !this.idePoa.equals(other.idePoa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.Poa2015[ idePoa=" + idePoa + " ]";
    }
    
}
