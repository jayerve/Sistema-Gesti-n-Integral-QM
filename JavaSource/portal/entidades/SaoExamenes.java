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
@Table(name = "sao_examenes", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SaoExamenes.findAll", query = "SELECT s FROM SaoExamenes s"),
    @NamedQuery(name = "SaoExamenes.findByIdeSaexa", query = "SELECT s FROM SaoExamenes s WHERE s.ideSaexa = :ideSaexa"),
    @NamedQuery(name = "SaoExamenes.findByDetalleSaexa", query = "SELECT s FROM SaoExamenes s WHERE s.detalleSaexa = :detalleSaexa"),
    @NamedQuery(name = "SaoExamenes.findByActivoSaexa", query = "SELECT s FROM SaoExamenes s WHERE s.activoSaexa = :activoSaexa"),
    @NamedQuery(name = "SaoExamenes.findByUsuarioIngre", query = "SELECT s FROM SaoExamenes s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SaoExamenes.findByFechaIngre", query = "SELECT s FROM SaoExamenes s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SaoExamenes.findByUsuarioActua", query = "SELECT s FROM SaoExamenes s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SaoExamenes.findByFechaActua", query = "SELECT s FROM SaoExamenes s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SaoExamenes.findByHoraIngre", query = "SELECT s FROM SaoExamenes s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SaoExamenes.findByHoraActua", query = "SELECT s FROM SaoExamenes s WHERE s.horaActua = :horaActua")})
public class SaoExamenes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_saexa", nullable = false)
    private Integer ideSaexa;
    @Size(max = 100)
    @Column(name = "detalle_saexa", length = 100)
    private String detalleSaexa;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_saexa", nullable = false)
    private boolean activoSaexa;
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
    @OneToMany(mappedBy = "ideSaexa")
    private List<SaoFichaExamenes> saoFichaExamenesList;

    public SaoExamenes() {
    }

    public SaoExamenes(Integer ideSaexa) {
        this.ideSaexa = ideSaexa;
    }

    public SaoExamenes(Integer ideSaexa, boolean activoSaexa) {
        this.ideSaexa = ideSaexa;
        this.activoSaexa = activoSaexa;
    }

    public Integer getIdeSaexa() {
        return ideSaexa;
    }

    public void setIdeSaexa(Integer ideSaexa) {
        this.ideSaexa = ideSaexa;
    }

    public String getDetalleSaexa() {
        return detalleSaexa;
    }

    public void setDetalleSaexa(String detalleSaexa) {
        this.detalleSaexa = detalleSaexa;
    }

    public boolean getActivoSaexa() {
        return activoSaexa;
    }

    public void setActivoSaexa(boolean activoSaexa) {
        this.activoSaexa = activoSaexa;
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

    public List<SaoFichaExamenes> getSaoFichaExamenesList() {
        return saoFichaExamenesList;
    }

    public void setSaoFichaExamenesList(List<SaoFichaExamenes> saoFichaExamenesList) {
        this.saoFichaExamenesList = saoFichaExamenesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSaexa != null ? ideSaexa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaoExamenes)) {
            return false;
        }
        SaoExamenes other = (SaoExamenes) object;
        if ((this.ideSaexa == null && other.ideSaexa != null) || (this.ideSaexa != null && !this.ideSaexa.equals(other.ideSaexa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SaoExamenes[ ideSaexa=" + ideSaexa + " ]";
    }
    
}
