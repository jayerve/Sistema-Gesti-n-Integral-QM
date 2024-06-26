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
@Table(name = "sis_accion_auditoria", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SisAccionAuditoria.findAll", query = "SELECT s FROM SisAccionAuditoria s"),
    @NamedQuery(name = "SisAccionAuditoria.findByIdeAcau", query = "SELECT s FROM SisAccionAuditoria s WHERE s.ideAcau = :ideAcau"),
    @NamedQuery(name = "SisAccionAuditoria.findByNomAcau", query = "SELECT s FROM SisAccionAuditoria s WHERE s.nomAcau = :nomAcau"),
    @NamedQuery(name = "SisAccionAuditoria.findByDescripcionAcau", query = "SELECT s FROM SisAccionAuditoria s WHERE s.descripcionAcau = :descripcionAcau"),
    @NamedQuery(name = "SisAccionAuditoria.findByUsuarioIngre", query = "SELECT s FROM SisAccionAuditoria s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SisAccionAuditoria.findByFechaIngre", query = "SELECT s FROM SisAccionAuditoria s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SisAccionAuditoria.findByHoraIngre", query = "SELECT s FROM SisAccionAuditoria s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SisAccionAuditoria.findByUsuarioActua", query = "SELECT s FROM SisAccionAuditoria s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SisAccionAuditoria.findByFechaActua", query = "SELECT s FROM SisAccionAuditoria s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SisAccionAuditoria.findByHoraActua", query = "SELECT s FROM SisAccionAuditoria s WHERE s.horaActua = :horaActua")})
public class SisAccionAuditoria implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_acau", nullable = false)
    private Integer ideAcau;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nom_acau", nullable = false, length = 50)
    private String nomAcau;
    @Size(max = 190)
    @Column(name = "descripcion_acau", length = 190)
    private String descripcionAcau;
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

    public SisAccionAuditoria() {
    }

    public SisAccionAuditoria(Integer ideAcau) {
        this.ideAcau = ideAcau;
    }

    public SisAccionAuditoria(Integer ideAcau, String nomAcau) {
        this.ideAcau = ideAcau;
        this.nomAcau = nomAcau;
    }

    public Integer getIdeAcau() {
        return ideAcau;
    }

    public void setIdeAcau(Integer ideAcau) {
        this.ideAcau = ideAcau;
    }

    public String getNomAcau() {
        return nomAcau;
    }

    public void setNomAcau(String nomAcau) {
        this.nomAcau = nomAcau;
    }

    public String getDescripcionAcau() {
        return descripcionAcau;
    }

    public void setDescripcionAcau(String descripcionAcau) {
        this.descripcionAcau = descripcionAcau;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAcau != null ? ideAcau.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisAccionAuditoria)) {
            return false;
        }
        SisAccionAuditoria other = (SisAccionAuditoria) object;
        if ((this.ideAcau == null && other.ideAcau != null) || (this.ideAcau != null && !this.ideAcau.equals(other.ideAcau))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisAccionAuditoria[ ideAcau=" + ideAcau + " ]";
    }
    
}
