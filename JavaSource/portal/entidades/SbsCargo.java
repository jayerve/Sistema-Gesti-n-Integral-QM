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
@Table(name = "sbs_cargo", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SbsCargo.findAll", query = "SELECT s FROM SbsCargo s"),
    @NamedQuery(name = "SbsCargo.findByIdeSbcar", query = "SELECT s FROM SbsCargo s WHERE s.ideSbcar = :ideSbcar"),
    @NamedQuery(name = "SbsCargo.findByDetalleSbcar", query = "SELECT s FROM SbsCargo s WHERE s.detalleSbcar = :detalleSbcar"),
    @NamedQuery(name = "SbsCargo.findByCodigoSbs", query = "SELECT s FROM SbsCargo s WHERE s.codigoSbs = :codigoSbs"),
    @NamedQuery(name = "SbsCargo.findByActivoSbcar", query = "SELECT s FROM SbsCargo s WHERE s.activoSbcar = :activoSbcar"),
    @NamedQuery(name = "SbsCargo.findByUsuarioIngre", query = "SELECT s FROM SbsCargo s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SbsCargo.findByFechaIngre", query = "SELECT s FROM SbsCargo s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SbsCargo.findByHoraIngre", query = "SELECT s FROM SbsCargo s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SbsCargo.findByUsuarioActua", query = "SELECT s FROM SbsCargo s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SbsCargo.findByFechaActua", query = "SELECT s FROM SbsCargo s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SbsCargo.findByHoraActua", query = "SELECT s FROM SbsCargo s WHERE s.horaActua = :horaActua")})
public class SbsCargo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sbcar", nullable = false)
    private Integer ideSbcar;
    @Size(max = 1000)
    @Column(name = "detalle_sbcar", length = 1000)
    private String detalleSbcar;
    @Size(max = 50)
    @Column(name = "codigo_sbs", length = 50)
    private String codigoSbs;
    @Column(name = "activo_sbcar")
    private Boolean activoSbcar;
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
    @OneToMany(mappedBy = "ideSbcar")
    private List<SbsArchivoVeinteUno> sbsArchivoVeinteUnoList;
    @OneToMany(mappedBy = "ideSbcar")
    private List<GenCargoFuncional> genCargoFuncionalList;
    @OneToMany(mappedBy = "ideSbcar")
    private List<SbsArchivoEdiez> sbsArchivoEdiezList;
    @JoinColumn(name = "ide_sbtic", referencedColumnName = "ide_sbtic")
    @ManyToOne
    private SbsTipoCargo ideSbtic;
    @OneToMany(mappedBy = "sbsIdeSbcar")
    private List<SbsCargo> sbsCargoList;
    @JoinColumn(name = "sbs_ide_sbcar", referencedColumnName = "ide_sbcar")
    @ManyToOne
    private SbsCargo sbsIdeSbcar;

    public SbsCargo() {
    }

    public SbsCargo(Integer ideSbcar) {
        this.ideSbcar = ideSbcar;
    }

    public Integer getIdeSbcar() {
        return ideSbcar;
    }

    public void setIdeSbcar(Integer ideSbcar) {
        this.ideSbcar = ideSbcar;
    }

    public String getDetalleSbcar() {
        return detalleSbcar;
    }

    public void setDetalleSbcar(String detalleSbcar) {
        this.detalleSbcar = detalleSbcar;
    }

    public String getCodigoSbs() {
        return codigoSbs;
    }

    public void setCodigoSbs(String codigoSbs) {
        this.codigoSbs = codigoSbs;
    }

    public Boolean getActivoSbcar() {
        return activoSbcar;
    }

    public void setActivoSbcar(Boolean activoSbcar) {
        this.activoSbcar = activoSbcar;
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

    public List<SbsArchivoVeinteUno> getSbsArchivoVeinteUnoList() {
        return sbsArchivoVeinteUnoList;
    }

    public void setSbsArchivoVeinteUnoList(List<SbsArchivoVeinteUno> sbsArchivoVeinteUnoList) {
        this.sbsArchivoVeinteUnoList = sbsArchivoVeinteUnoList;
    }

    public List<GenCargoFuncional> getGenCargoFuncionalList() {
        return genCargoFuncionalList;
    }

    public void setGenCargoFuncionalList(List<GenCargoFuncional> genCargoFuncionalList) {
        this.genCargoFuncionalList = genCargoFuncionalList;
    }

    public List<SbsArchivoEdiez> getSbsArchivoEdiezList() {
        return sbsArchivoEdiezList;
    }

    public void setSbsArchivoEdiezList(List<SbsArchivoEdiez> sbsArchivoEdiezList) {
        this.sbsArchivoEdiezList = sbsArchivoEdiezList;
    }

    public SbsTipoCargo getIdeSbtic() {
        return ideSbtic;
    }

    public void setIdeSbtic(SbsTipoCargo ideSbtic) {
        this.ideSbtic = ideSbtic;
    }

    public List<SbsCargo> getSbsCargoList() {
        return sbsCargoList;
    }

    public void setSbsCargoList(List<SbsCargo> sbsCargoList) {
        this.sbsCargoList = sbsCargoList;
    }

    public SbsCargo getSbsIdeSbcar() {
        return sbsIdeSbcar;
    }

    public void setSbsIdeSbcar(SbsCargo sbsIdeSbcar) {
        this.sbsIdeSbcar = sbsIdeSbcar;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSbcar != null ? ideSbcar.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SbsCargo)) {
            return false;
        }
        SbsCargo other = (SbsCargo) object;
        if ((this.ideSbcar == null && other.ideSbcar != null) || (this.ideSbcar != null && !this.ideSbcar.equals(other.ideSbcar))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SbsCargo[ ideSbcar=" + ideSbcar + " ]";
    }
    
}
