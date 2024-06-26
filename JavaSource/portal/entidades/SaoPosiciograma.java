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
@Table(name = "sao_posiciograma", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SaoPosiciograma.findAll", query = "SELECT s FROM SaoPosiciograma s"),
    @NamedQuery(name = "SaoPosiciograma.findByIdeSapos", query = "SELECT s FROM SaoPosiciograma s WHERE s.ideSapos = :ideSapos"),
    @NamedQuery(name = "SaoPosiciograma.findByDetalleSapos", query = "SELECT s FROM SaoPosiciograma s WHERE s.detalleSapos = :detalleSapos"),
    @NamedQuery(name = "SaoPosiciograma.findByActivoSapos", query = "SELECT s FROM SaoPosiciograma s WHERE s.activoSapos = :activoSapos"),
    @NamedQuery(name = "SaoPosiciograma.findByUsuarioIngre", query = "SELECT s FROM SaoPosiciograma s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SaoPosiciograma.findByFechaIngre", query = "SELECT s FROM SaoPosiciograma s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SaoPosiciograma.findByUsuarioActua", query = "SELECT s FROM SaoPosiciograma s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SaoPosiciograma.findByFechaActua", query = "SELECT s FROM SaoPosiciograma s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SaoPosiciograma.findByHoraIngre", query = "SELECT s FROM SaoPosiciograma s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SaoPosiciograma.findByHoraActua", query = "SELECT s FROM SaoPosiciograma s WHERE s.horaActua = :horaActua")})
public class SaoPosiciograma implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sapos", nullable = false)
    private Integer ideSapos;
    @Size(max = 100)
    @Column(name = "detalle_sapos", length = 100)
    private String detalleSapos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_sapos", nullable = false)
    private boolean activoSapos;
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
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @OneToMany(mappedBy = "ideSapos")
    private List<SaoDetalleEvaluacion> saoDetalleEvaluacionList;

    public SaoPosiciograma() {
    }

    public SaoPosiciograma(Integer ideSapos) {
        this.ideSapos = ideSapos;
    }

    public SaoPosiciograma(Integer ideSapos, boolean activoSapos) {
        this.ideSapos = ideSapos;
        this.activoSapos = activoSapos;
    }

    public Integer getIdeSapos() {
        return ideSapos;
    }

    public void setIdeSapos(Integer ideSapos) {
        this.ideSapos = ideSapos;
    }

    public String getDetalleSapos() {
        return detalleSapos;
    }

    public void setDetalleSapos(String detalleSapos) {
        this.detalleSapos = detalleSapos;
    }

    public boolean getActivoSapos() {
        return activoSapos;
    }

    public void setActivoSapos(boolean activoSapos) {
        this.activoSapos = activoSapos;
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

    public List<SaoDetalleEvaluacion> getSaoDetalleEvaluacionList() {
        return saoDetalleEvaluacionList;
    }

    public void setSaoDetalleEvaluacionList(List<SaoDetalleEvaluacion> saoDetalleEvaluacionList) {
        this.saoDetalleEvaluacionList = saoDetalleEvaluacionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSapos != null ? ideSapos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaoPosiciograma)) {
            return false;
        }
        SaoPosiciograma other = (SaoPosiciograma) object;
        if ((this.ideSapos == null && other.ideSapos != null) || (this.ideSapos != null && !this.ideSapos.equals(other.ideSapos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SaoPosiciograma[ ideSapos=" + ideSapos + " ]";
    }
    
}
