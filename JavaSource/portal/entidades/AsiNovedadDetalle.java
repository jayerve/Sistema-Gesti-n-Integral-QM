/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author awbecerra
 */
@Entity
@Table(name = "asi_novedad_detalle", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "AsiNovedadDetalle.findAll", query = "SELECT a FROM AsiNovedadDetalle a"),
    @NamedQuery(name = "AsiNovedadDetalle.findByIdeAsnod", query = "SELECT a FROM AsiNovedadDetalle a WHERE a.ideAsnod = :ideAsnod"),
    @NamedQuery(name = "AsiNovedadDetalle.findByFechaAsnod", query = "SELECT a FROM AsiNovedadDetalle a WHERE a.fechaAsnod = :fechaAsnod"),
    @NamedQuery(name = "AsiNovedadDetalle.findByNroHorasAsnod", query = "SELECT a FROM AsiNovedadDetalle a WHERE a.nroHorasAsnod = :nroHorasAsnod"),
    @NamedQuery(name = "AsiNovedadDetalle.findByNroHorasAprobadoAsnod", query = "SELECT a FROM AsiNovedadDetalle a WHERE a.nroHorasAprobadoAsnod = :nroHorasAprobadoAsnod"),
    @NamedQuery(name = "AsiNovedadDetalle.findByAprobadoAsnod", query = "SELECT a FROM AsiNovedadDetalle a WHERE a.aprobadoAsnod = :aprobadoAsnod"),
    @NamedQuery(name = "AsiNovedadDetalle.findByNominaAsnod", query = "SELECT a FROM AsiNovedadDetalle a WHERE a.nominaAsnod = :nominaAsnod"),
    @NamedQuery(name = "AsiNovedadDetalle.findByVacacionesAsnod", query = "SELECT a FROM AsiNovedadDetalle a WHERE a.vacacionesAsnod = :vacacionesAsnod"),
    @NamedQuery(name = "AsiNovedadDetalle.findByObservacionAsnod", query = "SELECT a FROM AsiNovedadDetalle a WHERE a.observacionAsnod = :observacionAsnod"),
    @NamedQuery(name = "AsiNovedadDetalle.findByActivoAsnod", query = "SELECT a FROM AsiNovedadDetalle a WHERE a.activoAsnod = :activoAsnod"),
    @NamedQuery(name = "AsiNovedadDetalle.findByUsuarioIngre", query = "SELECT a FROM AsiNovedadDetalle a WHERE a.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "AsiNovedadDetalle.findByFechaIngre", query = "SELECT a FROM AsiNovedadDetalle a WHERE a.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "AsiNovedadDetalle.findByUsuarioActua", query = "SELECT a FROM AsiNovedadDetalle a WHERE a.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "AsiNovedadDetalle.findByFechaActua", query = "SELECT a FROM AsiNovedadDetalle a WHERE a.fechaActua = :fechaActua"),
    @NamedQuery(name = "AsiNovedadDetalle.findByIdeAsvaa", query = "SELECT a FROM AsiNovedadDetalle a WHERE a.ideAsvaa = :ideAsvaa"),
    @NamedQuery(name = "AsiNovedadDetalle.findByHoraInicioAsnod", query = "SELECT a FROM AsiNovedadDetalle a WHERE a.horaInicioAsnod = :horaInicioAsnod"),
    @NamedQuery(name = "AsiNovedadDetalle.findByHoraFinAsnod", query = "SELECT a FROM AsiNovedadDetalle a WHERE a.horaFinAsnod = :horaFinAsnod"),
    @NamedQuery(name = "AsiNovedadDetalle.findByHoraIngre", query = "SELECT a FROM AsiNovedadDetalle a WHERE a.horaIngre = :horaIngre"),
    @NamedQuery(name = "AsiNovedadDetalle.findByHoraActua", query = "SELECT a FROM AsiNovedadDetalle a WHERE a.horaActua = :horaActua")})
public class AsiNovedadDetalle implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_asnod", nullable = false)
    private Integer ideAsnod;
    @Column(name = "fecha_asnod")
    @Temporal(TemporalType.DATE)
    private Date fechaAsnod;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "nro_horas_asnod", nullable = false, precision = 12, scale = 3)
    private BigDecimal nroHorasAsnod;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nro_horas_aprobado_asnod", nullable = false, precision = 12, scale = 3)
    private BigDecimal nroHorasAprobadoAsnod;
    @Basic(optional = false)
    @NotNull
    @Column(name = "aprobado_asnod", nullable = false)
    private int aprobadoAsnod;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nomina_asnod", nullable = false)
    private int nominaAsnod;
    @Column(name = "vacaciones_asnod")
    private Integer vacacionesAsnod;
    @Size(max = 4000)
    @Column(name = "observacion_asnod", length = 4000)
    private String observacionAsnod;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_asnod", nullable = false)
    private boolean activoAsnod;
    @Size(max = 50)
    @Column(name = "usuario_ingre", length = 50)
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Size(max = 50)
    @Column(name = "usuario_actua", length = 50)
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "ide_asvaa")
    private Integer ideAsvaa;
    @Column(name = "hora_inicio_asnod")
    @Temporal(TemporalType.TIME)
    private Date horaInicioAsnod;
    @Column(name = "hora_fin_asnod")
    @Temporal(TemporalType.TIME)
    private Date horaFinAsnod;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_aspvh", referencedColumnName = "ide_aspvh")
    @ManyToOne
    private AsiPermisosVacacionHext ideAspvh;
    @JoinColumn(name = "ide_asnov", referencedColumnName = "ide_asnov")
    @ManyToOne
    private AsiNovedad ideAsnov;
    @JoinColumn(name = "ide_asmot", referencedColumnName = "ide_asmot")
    @ManyToOne
    private AsiMotivo ideAsmot;
    @JoinColumn(name = "ide_asdhe", referencedColumnName = "ide_asdhe")
    @ManyToOne
    private AsiDetalleHorasExtras ideAsdhe;
    @OneToMany(mappedBy = "ideAsnod")
    private List<AsiNovedadJustificacion> asiNovedadJustificacionList;
    @OneToMany(mappedBy = "ideAsnod")
    private List<AsiDetalleVacacion> asiDetalleVacacionList;

    public AsiNovedadDetalle() {
    }

    public AsiNovedadDetalle(Integer ideAsnod) {
        this.ideAsnod = ideAsnod;
    }

    public AsiNovedadDetalle(Integer ideAsnod, BigDecimal nroHorasAsnod, BigDecimal nroHorasAprobadoAsnod, int aprobadoAsnod, int nominaAsnod, boolean activoAsnod) {
        this.ideAsnod = ideAsnod;
        this.nroHorasAsnod = nroHorasAsnod;
        this.nroHorasAprobadoAsnod = nroHorasAprobadoAsnod;
        this.aprobadoAsnod = aprobadoAsnod;
        this.nominaAsnod = nominaAsnod;
        this.activoAsnod = activoAsnod;
    }

    public Integer getIdeAsnod() {
        return ideAsnod;
    }

    public void setIdeAsnod(Integer ideAsnod) {
        this.ideAsnod = ideAsnod;
    }

    public Date getFechaAsnod() {
        return fechaAsnod;
    }

    public void setFechaAsnod(Date fechaAsnod) {
        this.fechaAsnod = fechaAsnod;
    }

    public BigDecimal getNroHorasAsnod() {
        return nroHorasAsnod;
    }

    public void setNroHorasAsnod(BigDecimal nroHorasAsnod) {
        this.nroHorasAsnod = nroHorasAsnod;
    }

    public BigDecimal getNroHorasAprobadoAsnod() {
        return nroHorasAprobadoAsnod;
    }

    public void setNroHorasAprobadoAsnod(BigDecimal nroHorasAprobadoAsnod) {
        this.nroHorasAprobadoAsnod = nroHorasAprobadoAsnod;
    }

    public int getAprobadoAsnod() {
        return aprobadoAsnod;
    }

    public void setAprobadoAsnod(int aprobadoAsnod) {
        this.aprobadoAsnod = aprobadoAsnod;
    }

    public int getNominaAsnod() {
        return nominaAsnod;
    }

    public void setNominaAsnod(int nominaAsnod) {
        this.nominaAsnod = nominaAsnod;
    }

    public Integer getVacacionesAsnod() {
        return vacacionesAsnod;
    }

    public void setVacacionesAsnod(Integer vacacionesAsnod) {
        this.vacacionesAsnod = vacacionesAsnod;
    }

    public String getObservacionAsnod() {
        return observacionAsnod;
    }

    public void setObservacionAsnod(String observacionAsnod) {
        this.observacionAsnod = observacionAsnod;
    }

    public boolean getActivoAsnod() {
        return activoAsnod;
    }

    public void setActivoAsnod(boolean activoAsnod) {
        this.activoAsnod = activoAsnod;
    }

    public String getUsuarioIngre() {
        return usuarioIngre;
    }

    public void setUsuarioIngre(String usuarioIngre) {
        this.usuarioIngre = usuarioIngre;
    }

    public Date getFechaIngre() {
        return fechaIngre;
    }

    public void setFechaIngre(Date fechaIngre) {
        this.fechaIngre = fechaIngre;
    }

    public String getUsuarioActua() {
        return usuarioActua;
    }

    public void setUsuarioActua(String usuarioActua) {
        this.usuarioActua = usuarioActua;
    }

    public Date getFechaActua() {
        return fechaActua;
    }

    public void setFechaActua(Date fechaActua) {
        this.fechaActua = fechaActua;
    }

    public Integer getIdeAsvaa() {
        return ideAsvaa;
    }

    public void setIdeAsvaa(Integer ideAsvaa) {
        this.ideAsvaa = ideAsvaa;
    }

    public Date getHoraInicioAsnod() {
        return horaInicioAsnod;
    }

    public void setHoraInicioAsnod(Date horaInicioAsnod) {
        this.horaInicioAsnod = horaInicioAsnod;
    }

    public Date getHoraFinAsnod() {
        return horaFinAsnod;
    }

    public void setHoraFinAsnod(Date horaFinAsnod) {
        this.horaFinAsnod = horaFinAsnod;
    }

    public Date getHoraIngre() {
        return horaIngre;
    }

    public void setHoraIngre(Date horaIngre) {
        this.horaIngre = horaIngre;
    }

    public Date getHoraActua() {
        return horaActua;
    }

    public void setHoraActua(Date horaActua) {
        this.horaActua = horaActua;
    }

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    public AsiPermisosVacacionHext getIdeAspvh() {
        return ideAspvh;
    }

    public void setIdeAspvh(AsiPermisosVacacionHext ideAspvh) {
        this.ideAspvh = ideAspvh;
    }

    public AsiNovedad getIdeAsnov() {
        return ideAsnov;
    }

    public void setIdeAsnov(AsiNovedad ideAsnov) {
        this.ideAsnov = ideAsnov;
    }

    public AsiMotivo getIdeAsmot() {
        return ideAsmot;
    }

    public void setIdeAsmot(AsiMotivo ideAsmot) {
        this.ideAsmot = ideAsmot;
    }

    public AsiDetalleHorasExtras getIdeAsdhe() {
        return ideAsdhe;
    }

    public void setIdeAsdhe(AsiDetalleHorasExtras ideAsdhe) {
        this.ideAsdhe = ideAsdhe;
    }

    public List<AsiNovedadJustificacion> getAsiNovedadJustificacionList() {
        return asiNovedadJustificacionList;
    }

    public void setAsiNovedadJustificacionList(List<AsiNovedadJustificacion> asiNovedadJustificacionList) {
        this.asiNovedadJustificacionList = asiNovedadJustificacionList;
    }

    public List<AsiDetalleVacacion> getAsiDetalleVacacionList() {
        return asiDetalleVacacionList;
    }

    public void setAsiDetalleVacacionList(List<AsiDetalleVacacion> asiDetalleVacacionList) {
        this.asiDetalleVacacionList = asiDetalleVacacionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAsnod != null ? ideAsnod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AsiNovedadDetalle)) {
            return false;
        }
        AsiNovedadDetalle other = (AsiNovedadDetalle) object;
        if ((this.ideAsnod == null && other.ideAsnod != null) || (this.ideAsnod != null && !this.ideAsnod.equals(other.ideAsnod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.AsiNovedadDetalle[ ideAsnod=" + ideAsnod + " ]";
    }
    
}
