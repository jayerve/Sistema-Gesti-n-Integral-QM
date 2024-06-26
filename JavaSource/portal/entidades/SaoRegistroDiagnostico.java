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
@Table(name = "sao_registro_diagnostico", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SaoRegistroDiagnostico.findAll", query = "SELECT s FROM SaoRegistroDiagnostico s"),
    @NamedQuery(name = "SaoRegistroDiagnostico.findByIdeSared", query = "SELECT s FROM SaoRegistroDiagnostico s WHERE s.ideSared = :ideSared"),
    @NamedQuery(name = "SaoRegistroDiagnostico.findByDetalleSared", query = "SELECT s FROM SaoRegistroDiagnostico s WHERE s.detalleSared = :detalleSared"),
    @NamedQuery(name = "SaoRegistroDiagnostico.findByActivoSared", query = "SELECT s FROM SaoRegistroDiagnostico s WHERE s.activoSared = :activoSared"),
    @NamedQuery(name = "SaoRegistroDiagnostico.findByUsuarioIngre", query = "SELECT s FROM SaoRegistroDiagnostico s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SaoRegistroDiagnostico.findByFechaIngre", query = "SELECT s FROM SaoRegistroDiagnostico s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SaoRegistroDiagnostico.findByUsuarioActua", query = "SELECT s FROM SaoRegistroDiagnostico s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SaoRegistroDiagnostico.findByFechaActua", query = "SELECT s FROM SaoRegistroDiagnostico s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SaoRegistroDiagnostico.findByHoraActua", query = "SELECT s FROM SaoRegistroDiagnostico s WHERE s.horaActua = :horaActua"),
    @NamedQuery(name = "SaoRegistroDiagnostico.findByHoraIngre", query = "SELECT s FROM SaoRegistroDiagnostico s WHERE s.horaIngre = :horaIngre")})
public class SaoRegistroDiagnostico implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sared", nullable = false)
    private Integer ideSared;
    @Size(max = 1000)
    @Column(name = "detalle_sared", length = 1000)
    private String detalleSared;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_sared", nullable = false)
    private boolean activoSared;
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
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @OneToMany(mappedBy = "ideSared")
    private List<SaoFichaDiagnostico> saoFichaDiagnosticoList;

    public SaoRegistroDiagnostico() {
    }

    public SaoRegistroDiagnostico(Integer ideSared) {
        this.ideSared = ideSared;
    }

    public SaoRegistroDiagnostico(Integer ideSared, boolean activoSared) {
        this.ideSared = ideSared;
        this.activoSared = activoSared;
    }

    public Integer getIdeSared() {
        return ideSared;
    }

    public void setIdeSared(Integer ideSared) {
        this.ideSared = ideSared;
    }

    public String getDetalleSared() {
        return detalleSared;
    }

    public void setDetalleSared(String detalleSared) {
        this.detalleSared = detalleSared;
    }

    public boolean getActivoSared() {
        return activoSared;
    }

    public void setActivoSared(boolean activoSared) {
        this.activoSared = activoSared;
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

    public Date getHoraActua() {
        return horaActua;
    }

    public void setHoraActua(Date horaActua) {
        this.horaActua = horaActua;
    }

    public Date getHoraIngre() {
        return horaIngre;
    }

    public void setHoraIngre(Date horaIngre) {
        this.horaIngre = horaIngre;
    }

    public List<SaoFichaDiagnostico> getSaoFichaDiagnosticoList() {
        return saoFichaDiagnosticoList;
    }

    public void setSaoFichaDiagnosticoList(List<SaoFichaDiagnostico> saoFichaDiagnosticoList) {
        this.saoFichaDiagnosticoList = saoFichaDiagnosticoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSared != null ? ideSared.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaoRegistroDiagnostico)) {
            return false;
        }
        SaoRegistroDiagnostico other = (SaoRegistroDiagnostico) object;
        if ((this.ideSared == null && other.ideSared != null) || (this.ideSared != null && !this.ideSared.equals(other.ideSared))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SaoRegistroDiagnostico[ ideSared=" + ideSared + " ]";
    }
    
}
