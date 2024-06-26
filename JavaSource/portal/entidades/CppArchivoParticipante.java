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
@Table(name = "cpp_archivo_participante", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CppArchivoParticipante.findAll", query = "SELECT c FROM CppArchivoParticipante c"),
    @NamedQuery(name = "CppArchivoParticipante.findByIdeCparp", query = "SELECT c FROM CppArchivoParticipante c WHERE c.ideCparp = :ideCparp"),
    @NamedQuery(name = "CppArchivoParticipante.findByArchivoCparp", query = "SELECT c FROM CppArchivoParticipante c WHERE c.archivoCparp = :archivoCparp"),
    @NamedQuery(name = "CppArchivoParticipante.findByDetalleCparp", query = "SELECT c FROM CppArchivoParticipante c WHERE c.detalleCparp = :detalleCparp"),
    @NamedQuery(name = "CppArchivoParticipante.findByActivoCparp", query = "SELECT c FROM CppArchivoParticipante c WHERE c.activoCparp = :activoCparp"),
    @NamedQuery(name = "CppArchivoParticipante.findByUsuarioIngre", query = "SELECT c FROM CppArchivoParticipante c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CppArchivoParticipante.findByFechaIngre", query = "SELECT c FROM CppArchivoParticipante c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CppArchivoParticipante.findByHoraIngre", query = "SELECT c FROM CppArchivoParticipante c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CppArchivoParticipante.findByUsuarioActua", query = "SELECT c FROM CppArchivoParticipante c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CppArchivoParticipante.findByFechaActua", query = "SELECT c FROM CppArchivoParticipante c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CppArchivoParticipante.findByHoraActua", query = "SELECT c FROM CppArchivoParticipante c WHERE c.horaActua = :horaActua")})
public class CppArchivoParticipante implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cparp", nullable = false)
    private Integer ideCparp;
    @Size(max = 100)
    @Column(name = "archivo_cparp", length = 100)
    private String archivoCparp;
    @Size(max = 100)
    @Column(name = "detalle_cparp", length = 100)
    private String detalleCparp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_cparp", nullable = false)
    private boolean activoCparp;
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
    @JoinColumn(name = "ide_cppar", referencedColumnName = "ide_cppar")
    @ManyToOne
    private CppParticipantes ideCppar;

    public CppArchivoParticipante() {
    }

    public CppArchivoParticipante(Integer ideCparp) {
        this.ideCparp = ideCparp;
    }

    public CppArchivoParticipante(Integer ideCparp, boolean activoCparp) {
        this.ideCparp = ideCparp;
        this.activoCparp = activoCparp;
    }

    public Integer getIdeCparp() {
        return ideCparp;
    }

    public void setIdeCparp(Integer ideCparp) {
        this.ideCparp = ideCparp;
    }

    public String getArchivoCparp() {
        return archivoCparp;
    }

    public void setArchivoCparp(String archivoCparp) {
        this.archivoCparp = archivoCparp;
    }

    public String getDetalleCparp() {
        return detalleCparp;
    }

    public void setDetalleCparp(String detalleCparp) {
        this.detalleCparp = detalleCparp;
    }

    public boolean getActivoCparp() {
        return activoCparp;
    }

    public void setActivoCparp(boolean activoCparp) {
        this.activoCparp = activoCparp;
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

    public CppParticipantes getIdeCppar() {
        return ideCppar;
    }

    public void setIdeCppar(CppParticipantes ideCppar) {
        this.ideCppar = ideCppar;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCparp != null ? ideCparp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CppArchivoParticipante)) {
            return false;
        }
        CppArchivoParticipante other = (CppArchivoParticipante) object;
        if ((this.ideCparp == null && other.ideCparp != null) || (this.ideCparp != null && !this.ideCparp.equals(other.ideCparp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CppArchivoParticipante[ ideCparp=" + ideCparp + " ]";
    }
    
}
