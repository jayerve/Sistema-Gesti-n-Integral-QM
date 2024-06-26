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
@Table(name = "sis_usuario_sucursal", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SisUsuarioSucursal.findAll", query = "SELECT s FROM SisUsuarioSucursal s"),
    @NamedQuery(name = "SisUsuarioSucursal.findByIdeUssu", query = "SELECT s FROM SisUsuarioSucursal s WHERE s.ideUssu = :ideUssu"),
    @NamedQuery(name = "SisUsuarioSucursal.findByIdeSucu", query = "SELECT s FROM SisUsuarioSucursal s WHERE s.ideSucu = :ideSucu"),
    @NamedQuery(name = "SisUsuarioSucursal.findBySisIdeSucu", query = "SELECT s FROM SisUsuarioSucursal s WHERE s.sisIdeSucu = :sisIdeSucu"),
    @NamedQuery(name = "SisUsuarioSucursal.findByUsuarioIngre", query = "SELECT s FROM SisUsuarioSucursal s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SisUsuarioSucursal.findByFechaIngre", query = "SELECT s FROM SisUsuarioSucursal s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SisUsuarioSucursal.findByUsuarioActua", query = "SELECT s FROM SisUsuarioSucursal s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SisUsuarioSucursal.findByFechaActua", query = "SELECT s FROM SisUsuarioSucursal s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SisUsuarioSucursal.findByHoraIngre", query = "SELECT s FROM SisUsuarioSucursal s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SisUsuarioSucursal.findByHoraActua", query = "SELECT s FROM SisUsuarioSucursal s WHERE s.horaActua = :horaActua")})
public class SisUsuarioSucursal implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_ussu", nullable = false)
    private Integer ideUssu;
    @Column(name = "ide_sucu")
    private Integer ideSucu;
    @Column(name = "sis_ide_sucu")
    private Integer sisIdeSucu;
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
    @JoinColumn(name = "ide_usua", referencedColumnName = "ide_usua")
    @ManyToOne
    private SisUsuario ideUsua;

    public SisUsuarioSucursal() {
    }

    public SisUsuarioSucursal(Integer ideUssu) {
        this.ideUssu = ideUssu;
    }

    public Integer getIdeUssu() {
        return ideUssu;
    }

    public void setIdeUssu(Integer ideUssu) {
        this.ideUssu = ideUssu;
    }

    public Integer getIdeSucu() {
        return ideSucu;
    }

    public void setIdeSucu(Integer ideSucu) {
        this.ideSucu = ideSucu;
    }

    public Integer getSisIdeSucu() {
        return sisIdeSucu;
    }

    public void setSisIdeSucu(Integer sisIdeSucu) {
        this.sisIdeSucu = sisIdeSucu;
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

    public SisUsuario getIdeUsua() {
        return ideUsua;
    }

    public void setIdeUsua(SisUsuario ideUsua) {
        this.ideUsua = ideUsua;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideUssu != null ? ideUssu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisUsuarioSucursal)) {
            return false;
        }
        SisUsuarioSucursal other = (SisUsuarioSucursal) object;
        if ((this.ideUssu == null && other.ideUssu != null) || (this.ideUssu != null && !this.ideUssu.equals(other.ideUssu))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisUsuarioSucursal[ ideUssu=" + ideUssu + " ]";
    }
    
}
