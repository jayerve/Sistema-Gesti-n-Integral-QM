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
@Table(name = "gen_modulo", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GenModulo.findAll", query = "SELECT g FROM GenModulo g"),
    @NamedQuery(name = "GenModulo.findByIdeGemod", query = "SELECT g FROM GenModulo g WHERE g.ideGemod = :ideGemod"),
    @NamedQuery(name = "GenModulo.findByDetalleGemod", query = "SELECT g FROM GenModulo g WHERE g.detalleGemod = :detalleGemod"),
    @NamedQuery(name = "GenModulo.findByActivoGemod", query = "SELECT g FROM GenModulo g WHERE g.activoGemod = :activoGemod"),
    @NamedQuery(name = "GenModulo.findByUsuarioIngre", query = "SELECT g FROM GenModulo g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenModulo.findByFechaIngre", query = "SELECT g FROM GenModulo g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenModulo.findByHoraIngre", query = "SELECT g FROM GenModulo g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenModulo.findByUsuarioActua", query = "SELECT g FROM GenModulo g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenModulo.findByFechaActua", query = "SELECT g FROM GenModulo g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenModulo.findByHoraActua", query = "SELECT g FROM GenModulo g WHERE g.horaActua = :horaActua")})
public class GenModulo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gemod", nullable = false)
    private Long ideGemod;
    @Size(max = 100)
    @Column(name = "detalle_gemod", length = 100)
    private String detalleGemod;
    @Column(name = "activo_gemod")
    private Boolean activoGemod;
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
    @OneToMany(mappedBy = "ideGemod")
    private List<GenModuloHabilitado> genModuloHabilitadoList;
    @OneToMany(mappedBy = "ideGemod")
    private List<GenTipoPersonaModulo> genTipoPersonaModuloList;
    @OneToMany(mappedBy = "ideGemod")
    private List<GenModuloSecuencial> genModuloSecuencialList;
    @OneToMany(mappedBy = "ideGemod")
    private List<ContParametroModulo> contParametroModuloList;
    @OneToMany(mappedBy = "ideGemod")
    private List<GenModuloEstado> genModuloEstadoList;
    @OneToMany(mappedBy = "genIdeGemod")
    private List<GenModulo> genModuloList;
    @JoinColumn(name = "gen_ide_gemod", referencedColumnName = "ide_gemod")
    @ManyToOne
    private GenModulo genIdeGemod;
    @OneToMany(mappedBy = "ideGemod")
    private List<GenModuloDocumento> genModuloDocumentoList;

    public GenModulo() {
    }

    public GenModulo(Long ideGemod) {
        this.ideGemod = ideGemod;
    }

    public Long getIdeGemod() {
        return ideGemod;
    }

    public void setIdeGemod(Long ideGemod) {
        this.ideGemod = ideGemod;
    }

    public String getDetalleGemod() {
        return detalleGemod;
    }

    public void setDetalleGemod(String detalleGemod) {
        this.detalleGemod = detalleGemod;
    }

    public Boolean getActivoGemod() {
        return activoGemod;
    }

    public void setActivoGemod(Boolean activoGemod) {
        this.activoGemod = activoGemod;
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

    public List<GenModuloHabilitado> getGenModuloHabilitadoList() {
        return genModuloHabilitadoList;
    }

    public void setGenModuloHabilitadoList(List<GenModuloHabilitado> genModuloHabilitadoList) {
        this.genModuloHabilitadoList = genModuloHabilitadoList;
    }

    public List<GenTipoPersonaModulo> getGenTipoPersonaModuloList() {
        return genTipoPersonaModuloList;
    }

    public void setGenTipoPersonaModuloList(List<GenTipoPersonaModulo> genTipoPersonaModuloList) {
        this.genTipoPersonaModuloList = genTipoPersonaModuloList;
    }

    public List<GenModuloSecuencial> getGenModuloSecuencialList() {
        return genModuloSecuencialList;
    }

    public void setGenModuloSecuencialList(List<GenModuloSecuencial> genModuloSecuencialList) {
        this.genModuloSecuencialList = genModuloSecuencialList;
    }

    public List<ContParametroModulo> getContParametroModuloList() {
        return contParametroModuloList;
    }

    public void setContParametroModuloList(List<ContParametroModulo> contParametroModuloList) {
        this.contParametroModuloList = contParametroModuloList;
    }

    public List<GenModuloEstado> getGenModuloEstadoList() {
        return genModuloEstadoList;
    }

    public void setGenModuloEstadoList(List<GenModuloEstado> genModuloEstadoList) {
        this.genModuloEstadoList = genModuloEstadoList;
    }

    public List<GenModulo> getGenModuloList() {
        return genModuloList;
    }

    public void setGenModuloList(List<GenModulo> genModuloList) {
        this.genModuloList = genModuloList;
    }

    public GenModulo getGenIdeGemod() {
        return genIdeGemod;
    }

    public void setGenIdeGemod(GenModulo genIdeGemod) {
        this.genIdeGemod = genIdeGemod;
    }

    public List<GenModuloDocumento> getGenModuloDocumentoList() {
        return genModuloDocumentoList;
    }

    public void setGenModuloDocumentoList(List<GenModuloDocumento> genModuloDocumentoList) {
        this.genModuloDocumentoList = genModuloDocumentoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGemod != null ? ideGemod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenModulo)) {
            return false;
        }
        GenModulo other = (GenModulo) object;
        if ((this.ideGemod == null && other.ideGemod != null) || (this.ideGemod != null && !this.ideGemod.equals(other.ideGemod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenModulo[ ideGemod=" + ideGemod + " ]";
    }
    
}
