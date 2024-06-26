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
@Table(name = "sis_sucursal", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SisSucursal.findAll", query = "SELECT s FROM SisSucursal s"),
    @NamedQuery(name = "SisSucursal.findByIdeSucu", query = "SELECT s FROM SisSucursal s WHERE s.ideSucu = :ideSucu"),
    @NamedQuery(name = "SisSucursal.findByNomSucu", query = "SELECT s FROM SisSucursal s WHERE s.nomSucu = :nomSucu"),
    @NamedQuery(name = "SisSucursal.findByTelefonosSucu", query = "SELECT s FROM SisSucursal s WHERE s.telefonosSucu = :telefonosSucu"),
    @NamedQuery(name = "SisSucursal.findByDireccionSucu", query = "SELECT s FROM SisSucursal s WHERE s.direccionSucu = :direccionSucu"),
    @NamedQuery(name = "SisSucursal.findByContactoSucu", query = "SELECT s FROM SisSucursal s WHERE s.contactoSucu = :contactoSucu"),
    @NamedQuery(name = "SisSucursal.findByActivoSucu", query = "SELECT s FROM SisSucursal s WHERE s.activoSucu = :activoSucu"),
    @NamedQuery(name = "SisSucursal.findByUsuarioIngre", query = "SELECT s FROM SisSucursal s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SisSucursal.findByFechaIngre", query = "SELECT s FROM SisSucursal s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SisSucursal.findByHoraIngre", query = "SELECT s FROM SisSucursal s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SisSucursal.findByUsuarioActua", query = "SELECT s FROM SisSucursal s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SisSucursal.findByFechaActua", query = "SELECT s FROM SisSucursal s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SisSucursal.findByHoraActua", query = "SELECT s FROM SisSucursal s WHERE s.horaActua = :horaActua")})
public class SisSucursal implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sucu", nullable = false)
    private Long ideSucu;
    @Size(max = 100)
    @Column(name = "nom_sucu", length = 100)
    private String nomSucu;
    @Size(max = 100)
    @Column(name = "telefonos_sucu", length = 100)
    private String telefonosSucu;
    @Size(max = 250)
    @Column(name = "direccion_sucu", length = 250)
    private String direccionSucu;
    @Size(max = 100)
    @Column(name = "contacto_sucu", length = 100)
    private String contactoSucu;
    @Column(name = "activo_sucu")
    private Boolean activoSucu;
    @Size(max = 50)
    @Column(name = "usuario_ingre", length = 50)
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @Size(max = 50)
    @Column(name = "usuario_actua", length = 50)
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @JoinColumn(name = "ide_empr", referencedColumnName = "ide_empr")
    @ManyToOne
    private SisEmpresa ideEmpr;
    @JoinColumn(name = "ide_sbofi", referencedColumnName = "ide_sbofi")
    @ManyToOne
    private SbsOficina ideSbofi;
    @JoinColumn(name = "ide_gedip", referencedColumnName = "ide_gedip")
    @ManyToOne
    private GenDivisionPolitica ideGedip;

    public SisSucursal() {
    }

    public SisSucursal(Long ideSucu) {
        this.ideSucu = ideSucu;
    }

    public Long getIdeSucu() {
        return ideSucu;
    }

    public void setIdeSucu(Long ideSucu) {
        this.ideSucu = ideSucu;
    }

    public String getNomSucu() {
        return nomSucu;
    }

    public void setNomSucu(String nomSucu) {
        this.nomSucu = nomSucu;
    }

    public String getTelefonosSucu() {
        return telefonosSucu;
    }

    public void setTelefonosSucu(String telefonosSucu) {
        this.telefonosSucu = telefonosSucu;
    }

    public String getDireccionSucu() {
        return direccionSucu;
    }

    public void setDireccionSucu(String direccionSucu) {
        this.direccionSucu = direccionSucu;
    }

    public String getContactoSucu() {
        return contactoSucu;
    }

    public void setContactoSucu(String contactoSucu) {
        this.contactoSucu = contactoSucu;
    }

    public Boolean getActivoSucu() {
        return activoSucu;
    }

    public void setActivoSucu(Boolean activoSucu) {
        this.activoSucu = activoSucu;
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

    public SisEmpresa getIdeEmpr() {
        return ideEmpr;
    }

    public void setIdeEmpr(SisEmpresa ideEmpr) {
        this.ideEmpr = ideEmpr;
    }

    public SbsOficina getIdeSbofi() {
        return ideSbofi;
    }

    public void setIdeSbofi(SbsOficina ideSbofi) {
        this.ideSbofi = ideSbofi;
    }

    public GenDivisionPolitica getIdeGedip() {
        return ideGedip;
    }

    public void setIdeGedip(GenDivisionPolitica ideGedip) {
        this.ideGedip = ideGedip;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSucu != null ? ideSucu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisSucursal)) {
            return false;
        }
        SisSucursal other = (SisSucursal) object;
        if ((this.ideSucu == null && other.ideSucu != null) || (this.ideSucu != null && !this.ideSucu.equals(other.ideSucu))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisSucursal[ ideSucu=" + ideSucu + " ]";
    }
    
}
