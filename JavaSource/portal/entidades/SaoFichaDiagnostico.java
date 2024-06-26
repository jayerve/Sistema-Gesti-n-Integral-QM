/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "sao_ficha_diagnostico", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SaoFichaDiagnostico.findAll", query = "SELECT s FROM SaoFichaDiagnostico s"),
    @NamedQuery(name = "SaoFichaDiagnostico.findByIdeSafid", query = "SELECT s FROM SaoFichaDiagnostico s WHERE s.ideSafid = :ideSafid"),
    @NamedQuery(name = "SaoFichaDiagnostico.findByActivoSafid", query = "SELECT s FROM SaoFichaDiagnostico s WHERE s.activoSafid = :activoSafid"),
    @NamedQuery(name = "SaoFichaDiagnostico.findByUsuarioIngre", query = "SELECT s FROM SaoFichaDiagnostico s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SaoFichaDiagnostico.findByFechaIngre", query = "SELECT s FROM SaoFichaDiagnostico s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SaoFichaDiagnostico.findByUsuarioActua", query = "SELECT s FROM SaoFichaDiagnostico s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SaoFichaDiagnostico.findByFechaActua", query = "SELECT s FROM SaoFichaDiagnostico s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SaoFichaDiagnostico.findByHoraIngre", query = "SELECT s FROM SaoFichaDiagnostico s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SaoFichaDiagnostico.findByHoraActua", query = "SELECT s FROM SaoFichaDiagnostico s WHERE s.horaActua = :horaActua")})
public class SaoFichaDiagnostico implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_safid", nullable = false)
    private Integer ideSafid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_safid", nullable = false)
    private boolean activoSafid;
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
    @JoinColumn(name = "ide_sared", referencedColumnName = "ide_sared")
    @ManyToOne
    private SaoRegistroDiagnostico ideSared;
    @JoinColumn(name = "ide_safim", referencedColumnName = "ide_safim")
    @ManyToOne
    private SaoFichaMedica ideSafim;

    public SaoFichaDiagnostico() {
    }

    public SaoFichaDiagnostico(Integer ideSafid) {
        this.ideSafid = ideSafid;
    }

    public SaoFichaDiagnostico(Integer ideSafid, boolean activoSafid) {
        this.ideSafid = ideSafid;
        this.activoSafid = activoSafid;
    }

    public Integer getIdeSafid() {
        return ideSafid;
    }

    public void setIdeSafid(Integer ideSafid) {
        this.ideSafid = ideSafid;
    }

    public boolean getActivoSafid() {
        return activoSafid;
    }

    public void setActivoSafid(boolean activoSafid) {
        this.activoSafid = activoSafid;
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

    public SaoRegistroDiagnostico getIdeSared() {
        return ideSared;
    }

    public void setIdeSared(SaoRegistroDiagnostico ideSared) {
        this.ideSared = ideSared;
    }

    public SaoFichaMedica getIdeSafim() {
        return ideSafim;
    }

    public void setIdeSafim(SaoFichaMedica ideSafim) {
        this.ideSafim = ideSafim;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSafid != null ? ideSafid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaoFichaDiagnostico)) {
            return false;
        }
        SaoFichaDiagnostico other = (SaoFichaDiagnostico) object;
        if ((this.ideSafid == null && other.ideSafid != null) || (this.ideSafid != null && !this.ideSafid.equals(other.ideSafid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SaoFichaDiagnostico[ ideSafid=" + ideSafid + " ]";
    }
    
}
