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
@Table(name = "spr_referencias", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SprReferencias.findAll", query = "SELECT s FROM SprReferencias s"),
    @NamedQuery(name = "SprReferencias.findByIdeSpref", query = "SELECT s FROM SprReferencias s WHERE s.ideSpref = :ideSpref"),
    @NamedQuery(name = "SprReferencias.findByApellidosNombresSpref", query = "SELECT s FROM SprReferencias s WHERE s.apellidosNombresSpref = :apellidosNombresSpref"),
    @NamedQuery(name = "SprReferencias.findByEmailSpref", query = "SELECT s FROM SprReferencias s WHERE s.emailSpref = :emailSpref"),
    @NamedQuery(name = "SprReferencias.findByTelefonosSpref", query = "SELECT s FROM SprReferencias s WHERE s.telefonosSpref = :telefonosSpref"),
    @NamedQuery(name = "SprReferencias.findByActivoSpref", query = "SELECT s FROM SprReferencias s WHERE s.activoSpref = :activoSpref"),
    @NamedQuery(name = "SprReferencias.findByUsuarioIngre", query = "SELECT s FROM SprReferencias s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SprReferencias.findByFechaIngre", query = "SELECT s FROM SprReferencias s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SprReferencias.findByHoraIngre", query = "SELECT s FROM SprReferencias s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SprReferencias.findByUsuarioActua", query = "SELECT s FROM SprReferencias s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SprReferencias.findByFechaActua", query = "SELECT s FROM SprReferencias s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SprReferencias.findByHoraActua", query = "SELECT s FROM SprReferencias s WHERE s.horaActua = :horaActua")})
public class SprReferencias implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_spref", nullable = false)
    private Integer ideSpref;
    @Size(max = 100)
    @Column(name = "apellidos_nombres_spref", length = 100)
    private String apellidosNombresSpref;
    @Size(max = 100)
    @Column(name = "email_spref", length = 100)
    private String emailSpref;
    @Size(max = 50)
    @Column(name = "telefonos_spref", length = 50)
    private String telefonosSpref;
    @Column(name = "activo_spref")
    private Boolean activoSpref;
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
    @JoinColumn(name = "ide_sptir", referencedColumnName = "ide_sptir")
    @ManyToOne
    private SprTipoReferencia ideSptir;
    @JoinColumn(name = "ide_spbap", referencedColumnName = "ide_spbap")
    @ManyToOne
    private SprBasePostulante ideSpbap;

    public SprReferencias() {
    }

    public SprReferencias(Integer ideSpref) {
        this.ideSpref = ideSpref;
    }

    public Integer getIdeSpref() {
        return ideSpref;
    }

    public void setIdeSpref(Integer ideSpref) {
        this.ideSpref = ideSpref;
    }

    public String getApellidosNombresSpref() {
        return apellidosNombresSpref;
    }

    public void setApellidosNombresSpref(String apellidosNombresSpref) {
        this.apellidosNombresSpref = apellidosNombresSpref;
    }

    public String getEmailSpref() {
        return emailSpref;
    }

    public void setEmailSpref(String emailSpref) {
        this.emailSpref = emailSpref;
    }

    public String getTelefonosSpref() {
        return telefonosSpref;
    }

    public void setTelefonosSpref(String telefonosSpref) {
        this.telefonosSpref = telefonosSpref;
    }

    public Boolean getActivoSpref() {
        return activoSpref;
    }

    public void setActivoSpref(Boolean activoSpref) {
        this.activoSpref = activoSpref;
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

    public SprTipoReferencia getIdeSptir() {
        return ideSptir;
    }

    public void setIdeSptir(SprTipoReferencia ideSptir) {
        this.ideSptir = ideSptir;
    }

    public SprBasePostulante getIdeSpbap() {
        return ideSpbap;
    }

    public void setIdeSpbap(SprBasePostulante ideSpbap) {
        this.ideSpbap = ideSpbap;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSpref != null ? ideSpref.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SprReferencias)) {
            return false;
        }
        SprReferencias other = (SprReferencias) object;
        if ((this.ideSpref == null && other.ideSpref != null) || (this.ideSpref != null && !this.ideSpref.equals(other.ideSpref))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SprReferencias[ ideSpref=" + ideSpref + " ]";
    }
    
}
