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
@Table(name = "sao_ubicacion_sucursal", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SaoUbicacionSucursal.findAll", query = "SELECT s FROM SaoUbicacionSucursal s"),
    @NamedQuery(name = "SaoUbicacionSucursal.findByIdeSaubs", query = "SELECT s FROM SaoUbicacionSucursal s WHERE s.ideSaubs = :ideSaubs"),
    @NamedQuery(name = "SaoUbicacionSucursal.findByIdeSucu", query = "SELECT s FROM SaoUbicacionSucursal s WHERE s.ideSucu = :ideSucu"),
    @NamedQuery(name = "SaoUbicacionSucursal.findByActivoSaubs", query = "SELECT s FROM SaoUbicacionSucursal s WHERE s.activoSaubs = :activoSaubs"),
    @NamedQuery(name = "SaoUbicacionSucursal.findByUsuarioIngre", query = "SELECT s FROM SaoUbicacionSucursal s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SaoUbicacionSucursal.findByFechaIngre", query = "SELECT s FROM SaoUbicacionSucursal s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SaoUbicacionSucursal.findByHoraIngre", query = "SELECT s FROM SaoUbicacionSucursal s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SaoUbicacionSucursal.findByUsuarioActua", query = "SELECT s FROM SaoUbicacionSucursal s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SaoUbicacionSucursal.findByFechaActua", query = "SELECT s FROM SaoUbicacionSucursal s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SaoUbicacionSucursal.findByHoraActua", query = "SELECT s FROM SaoUbicacionSucursal s WHERE s.horaActua = :horaActua")})
public class SaoUbicacionSucursal implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_saubs", nullable = false)
    private Integer ideSaubs;
    @Column(name = "ide_sucu")
    private Integer ideSucu;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_saubs", nullable = false)
    private boolean activoSaubs;
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
    @OneToMany(mappedBy = "ideSaubs")
    private List<SaoCustodio> saoCustodioList;
    @JoinColumn(name = "ide_saubi", referencedColumnName = "ide_saubi")
    @ManyToOne
    private SaoUbicacion ideSaubi;

    public SaoUbicacionSucursal() {
    }

    public SaoUbicacionSucursal(Integer ideSaubs) {
        this.ideSaubs = ideSaubs;
    }

    public SaoUbicacionSucursal(Integer ideSaubs, boolean activoSaubs) {
        this.ideSaubs = ideSaubs;
        this.activoSaubs = activoSaubs;
    }

    public Integer getIdeSaubs() {
        return ideSaubs;
    }

    public void setIdeSaubs(Integer ideSaubs) {
        this.ideSaubs = ideSaubs;
    }

    public Integer getIdeSucu() {
        return ideSucu;
    }

    public void setIdeSucu(Integer ideSucu) {
        this.ideSucu = ideSucu;
    }

    public boolean getActivoSaubs() {
        return activoSaubs;
    }

    public void setActivoSaubs(boolean activoSaubs) {
        this.activoSaubs = activoSaubs;
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

    public List<SaoCustodio> getSaoCustodioList() {
        return saoCustodioList;
    }

    public void setSaoCustodioList(List<SaoCustodio> saoCustodioList) {
        this.saoCustodioList = saoCustodioList;
    }

    public SaoUbicacion getIdeSaubi() {
        return ideSaubi;
    }

    public void setIdeSaubi(SaoUbicacion ideSaubi) {
        this.ideSaubi = ideSaubi;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSaubs != null ? ideSaubs.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaoUbicacionSucursal)) {
            return false;
        }
        SaoUbicacionSucursal other = (SaoUbicacionSucursal) object;
        if ((this.ideSaubs == null && other.ideSaubs != null) || (this.ideSaubs != null && !this.ideSaubs.equals(other.ideSaubs))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SaoUbicacionSucursal[ ideSaubs=" + ideSaubs + " ]";
    }
    
}
