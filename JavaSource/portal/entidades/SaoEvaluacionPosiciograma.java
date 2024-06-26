/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
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
@Table(name = "sao_evaluacion_posiciograma", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SaoEvaluacionPosiciograma.findAll", query = "SELECT s FROM SaoEvaluacionPosiciograma s"),
    @NamedQuery(name = "SaoEvaluacionPosiciograma.findByIdeSaevp", query = "SELECT s FROM SaoEvaluacionPosiciograma s WHERE s.ideSaevp = :ideSaevp"),
    @NamedQuery(name = "SaoEvaluacionPosiciograma.findByDetalleSaevp", query = "SELECT s FROM SaoEvaluacionPosiciograma s WHERE s.detalleSaevp = :detalleSaevp"),
    @NamedQuery(name = "SaoEvaluacionPosiciograma.findByFechaEvaluacionSaevp", query = "SELECT s FROM SaoEvaluacionPosiciograma s WHERE s.fechaEvaluacionSaevp = :fechaEvaluacionSaevp"),
    @NamedQuery(name = "SaoEvaluacionPosiciograma.findByActivoSaevp", query = "SELECT s FROM SaoEvaluacionPosiciograma s WHERE s.activoSaevp = :activoSaevp"),
    @NamedQuery(name = "SaoEvaluacionPosiciograma.findByUsuarioIngre", query = "SELECT s FROM SaoEvaluacionPosiciograma s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SaoEvaluacionPosiciograma.findByFechaIngre", query = "SELECT s FROM SaoEvaluacionPosiciograma s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SaoEvaluacionPosiciograma.findByHoraIngre", query = "SELECT s FROM SaoEvaluacionPosiciograma s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SaoEvaluacionPosiciograma.findByUsuarioActua", query = "SELECT s FROM SaoEvaluacionPosiciograma s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SaoEvaluacionPosiciograma.findByFechaActua", query = "SELECT s FROM SaoEvaluacionPosiciograma s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SaoEvaluacionPosiciograma.findByHoraActua", query = "SELECT s FROM SaoEvaluacionPosiciograma s WHERE s.horaActua = :horaActua")})
public class SaoEvaluacionPosiciograma implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_saevp", nullable = false)
    private Integer ideSaevp;
    @Size(max = 1000)
    @Column(name = "detalle_saevp", length = 1000)
    private String detalleSaevp;
    @Column(name = "fecha_evaluacion_saevp")
    @Temporal(TemporalType.DATE)
    private Date fechaEvaluacionSaevp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_saevp", nullable = false)
    private boolean activoSaevp;
    @Size(max = 50)
    @Column(name = "usuario_ingre", length = 50)
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.DATE)
    private Date horaIngre;
    @Size(max = 50)
    @Column(name = "usuario_actua", length = 50)
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.DATE)
    private Date horaActua;
    @OneToMany(mappedBy = "ideSaevp")
    private List<SaoDetalleEvaluacion> saoDetalleEvaluacionList;
    @JoinColumn(name = "ide_usua", referencedColumnName = "ide_usua")
    @ManyToOne
    private SisUsuario ideUsua;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar ideGeedp;

    public SaoEvaluacionPosiciograma() {
    }

    public SaoEvaluacionPosiciograma(Integer ideSaevp) {
        this.ideSaevp = ideSaevp;
    }

    public SaoEvaluacionPosiciograma(Integer ideSaevp, boolean activoSaevp) {
        this.ideSaevp = ideSaevp;
        this.activoSaevp = activoSaevp;
    }

    public Integer getIdeSaevp() {
        return ideSaevp;
    }

    public void setIdeSaevp(Integer ideSaevp) {
        this.ideSaevp = ideSaevp;
    }

    public String getDetalleSaevp() {
        return detalleSaevp;
    }

    public void setDetalleSaevp(String detalleSaevp) {
        this.detalleSaevp = detalleSaevp;
    }

    public Date getFechaEvaluacionSaevp() {
        return fechaEvaluacionSaevp;
    }

    public void setFechaEvaluacionSaevp(Date fechaEvaluacionSaevp) {
        this.fechaEvaluacionSaevp = fechaEvaluacionSaevp;
    }

    public boolean getActivoSaevp() {
        return activoSaevp;
    }

    public void setActivoSaevp(boolean activoSaevp) {
        this.activoSaevp = activoSaevp;
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

    public Date getHoraIngre() {
        return horaIngre;
    }

    public void setHoraIngre(Date horaIngre) {
        this.horaIngre = horaIngre;
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

    public Date getHoraActua() {
        return horaActua;
    }

    public void setHoraActua(Date horaActua) {
        this.horaActua = horaActua;
    }

    public List<SaoDetalleEvaluacion> getSaoDetalleEvaluacionList() {
        return saoDetalleEvaluacionList;
    }

    public void setSaoDetalleEvaluacionList(List<SaoDetalleEvaluacion> saoDetalleEvaluacionList) {
        this.saoDetalleEvaluacionList = saoDetalleEvaluacionList;
    }

    public SisUsuario getIdeUsua() {
        return ideUsua;
    }

    public void setIdeUsua(SisUsuario ideUsua) {
        this.ideUsua = ideUsua;
    }

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    public GenEmpleadosDepartamentoPar getIdeGeedp() {
        return ideGeedp;
    }

    public void setIdeGeedp(GenEmpleadosDepartamentoPar ideGeedp) {
        this.ideGeedp = ideGeedp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSaevp != null ? ideSaevp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaoEvaluacionPosiciograma)) {
            return false;
        }
        SaoEvaluacionPosiciograma other = (SaoEvaluacionPosiciograma) object;
        if ((this.ideSaevp == null && other.ideSaevp != null) || (this.ideSaevp != null && !this.ideSaevp.equals(other.ideSaevp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SaoEvaluacionPosiciograma[ ideSaevp=" + ideSaevp + " ]";
    }
    
}
