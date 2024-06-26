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
@Table(name = "sbs_oficina", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SbsOficina.findAll", query = "SELECT s FROM SbsOficina s"),
    @NamedQuery(name = "SbsOficina.findByIdeSbofi", query = "SELECT s FROM SbsOficina s WHERE s.ideSbofi = :ideSbofi"),
    @NamedQuery(name = "SbsOficina.findByDetalleSbofi", query = "SELECT s FROM SbsOficina s WHERE s.detalleSbofi = :detalleSbofi"),
    @NamedQuery(name = "SbsOficina.findByCodigoSbsSbofi", query = "SELECT s FROM SbsOficina s WHERE s.codigoSbsSbofi = :codigoSbsSbofi"),
    @NamedQuery(name = "SbsOficina.findByActivoSbofi", query = "SELECT s FROM SbsOficina s WHERE s.activoSbofi = :activoSbofi"),
    @NamedQuery(name = "SbsOficina.findByUsuarioIngre", query = "SELECT s FROM SbsOficina s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SbsOficina.findByFechaIngre", query = "SELECT s FROM SbsOficina s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SbsOficina.findByHoraIngre", query = "SELECT s FROM SbsOficina s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SbsOficina.findByUsuarioActua", query = "SELECT s FROM SbsOficina s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SbsOficina.findByFechaActua", query = "SELECT s FROM SbsOficina s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SbsOficina.findByHoraActua", query = "SELECT s FROM SbsOficina s WHERE s.horaActua = :horaActua")})
public class SbsOficina implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sbofi", nullable = false)
    private Integer ideSbofi;
    @Size(max = 50)
    @Column(name = "detalle_sbofi", length = 50)
    private String detalleSbofi;
    @Size(max = 50)
    @Column(name = "codigo_sbs_sbofi", length = 50)
    private String codigoSbsSbofi;
    @Column(name = "activo_sbofi")
    private Boolean activoSbofi;
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
    @OneToMany(mappedBy = "ideSbofi")
    private List<SbsArchivoOnce> sbsArchivoOnceList;
    @OneToMany(mappedBy = "ideSbofi")
    private List<SbsArchivoVeinteUno> sbsArchivoVeinteUnoList;
    @OneToMany(mappedBy = "ideSbofi")
    private List<SbsArchivoEdiez> sbsArchivoEdiezList;
    @JoinColumn(name = "ide_sbtio", referencedColumnName = "ide_sbtio")
    @ManyToOne
    private SbsTipoOficina ideSbtio;
    @OneToMany(mappedBy = "ideSbofi")
    private List<SisSucursal> sisSucursalList;

    public SbsOficina() {
    }

    public SbsOficina(Integer ideSbofi) {
        this.ideSbofi = ideSbofi;
    }

    public Integer getIdeSbofi() {
        return ideSbofi;
    }

    public void setIdeSbofi(Integer ideSbofi) {
        this.ideSbofi = ideSbofi;
    }

    public String getDetalleSbofi() {
        return detalleSbofi;
    }

    public void setDetalleSbofi(String detalleSbofi) {
        this.detalleSbofi = detalleSbofi;
    }

    public String getCodigoSbsSbofi() {
        return codigoSbsSbofi;
    }

    public void setCodigoSbsSbofi(String codigoSbsSbofi) {
        this.codigoSbsSbofi = codigoSbsSbofi;
    }

    public Boolean getActivoSbofi() {
        return activoSbofi;
    }

    public void setActivoSbofi(Boolean activoSbofi) {
        this.activoSbofi = activoSbofi;
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

    public List<SbsArchivoOnce> getSbsArchivoOnceList() {
        return sbsArchivoOnceList;
    }

    public void setSbsArchivoOnceList(List<SbsArchivoOnce> sbsArchivoOnceList) {
        this.sbsArchivoOnceList = sbsArchivoOnceList;
    }

    public List<SbsArchivoVeinteUno> getSbsArchivoVeinteUnoList() {
        return sbsArchivoVeinteUnoList;
    }

    public void setSbsArchivoVeinteUnoList(List<SbsArchivoVeinteUno> sbsArchivoVeinteUnoList) {
        this.sbsArchivoVeinteUnoList = sbsArchivoVeinteUnoList;
    }

    public List<SbsArchivoEdiez> getSbsArchivoEdiezList() {
        return sbsArchivoEdiezList;
    }

    public void setSbsArchivoEdiezList(List<SbsArchivoEdiez> sbsArchivoEdiezList) {
        this.sbsArchivoEdiezList = sbsArchivoEdiezList;
    }

    public SbsTipoOficina getIdeSbtio() {
        return ideSbtio;
    }

    public void setIdeSbtio(SbsTipoOficina ideSbtio) {
        this.ideSbtio = ideSbtio;
    }

    public List<SisSucursal> getSisSucursalList() {
        return sisSucursalList;
    }

    public void setSisSucursalList(List<SisSucursal> sisSucursalList) {
        this.sisSucursalList = sisSucursalList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSbofi != null ? ideSbofi.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SbsOficina)) {
            return false;
        }
        SbsOficina other = (SbsOficina) object;
        if ((this.ideSbofi == null && other.ideSbofi != null) || (this.ideSbofi != null && !this.ideSbofi.equals(other.ideSbofi))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SbsOficina[ ideSbofi=" + ideSbofi + " ]";
    }
    
}
