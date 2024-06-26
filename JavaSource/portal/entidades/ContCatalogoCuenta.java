/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigInteger;
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
@Table(name = "cont_catalogo_cuenta", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "ContCatalogoCuenta.findAll", query = "SELECT c FROM ContCatalogoCuenta c"),
    @NamedQuery(name = "ContCatalogoCuenta.findByIdeCocac", query = "SELECT c FROM ContCatalogoCuenta c WHERE c.ideCocac = :ideCocac"),
    @NamedQuery(name = "ContCatalogoCuenta.findByCueCodigoCocac", query = "SELECT c FROM ContCatalogoCuenta c WHERE c.cueCodigoCocac = :cueCodigoCocac"),
    @NamedQuery(name = "ContCatalogoCuenta.findByCueDescripcionCocac", query = "SELECT c FROM ContCatalogoCuenta c WHERE c.cueDescripcionCocac = :cueDescripcionCocac"),
    @NamedQuery(name = "ContCatalogoCuenta.findByNivelCocac", query = "SELECT c FROM ContCatalogoCuenta c WHERE c.nivelCocac = :nivelCocac"),
    @NamedQuery(name = "ContCatalogoCuenta.findByGrupoNivelCocac", query = "SELECT c FROM ContCatalogoCuenta c WHERE c.grupoNivelCocac = :grupoNivelCocac"),
    @NamedQuery(name = "ContCatalogoCuenta.findByActivoCocac", query = "SELECT c FROM ContCatalogoCuenta c WHERE c.activoCocac = :activoCocac"),
    @NamedQuery(name = "ContCatalogoCuenta.findByUsuarioIngre", query = "SELECT c FROM ContCatalogoCuenta c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "ContCatalogoCuenta.findByFechaIngre", query = "SELECT c FROM ContCatalogoCuenta c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "ContCatalogoCuenta.findByHoraIngre", query = "SELECT c FROM ContCatalogoCuenta c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "ContCatalogoCuenta.findByUsuarioActua", query = "SELECT c FROM ContCatalogoCuenta c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "ContCatalogoCuenta.findByFechaActua", query = "SELECT c FROM ContCatalogoCuenta c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "ContCatalogoCuenta.findByHoraActua", query = "SELECT c FROM ContCatalogoCuenta c WHERE c.horaActua = :horaActua"),
    @NamedQuery(name = "ContCatalogoCuenta.findBySigefCocac", query = "SELECT c FROM ContCatalogoCuenta c WHERE c.sigefCocac = :sigefCocac"),
    @NamedQuery(name = "ContCatalogoCuenta.findByAperturaCierreCocac", query = "SELECT c FROM ContCatalogoCuenta c WHERE c.aperturaCierreCocac = :aperturaCierreCocac")})
public class ContCatalogoCuenta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cocac", nullable = false)
    private Long ideCocac;
    @Size(max = 50)
    @Column(name = "cue_codigo_cocac", length = 50)
    private String cueCodigoCocac;
    @Size(max = 100)
    @Column(name = "cue_descripcion_cocac", length = 100)
    private String cueDescripcionCocac;
    @Column(name = "nivel_cocac")
    private BigInteger nivelCocac;
    @Size(max = 50)
    @Column(name = "grupo_nivel_cocac", length = 50)
    private String grupoNivelCocac;
    @Column(name = "activo_cocac")
    private Boolean activoCocac;
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
    @Column(name = "sigef_cocac")
    private Boolean sigefCocac;
    @Column(name = "apertura_cierre_cocac")
    private Integer aperturaCierreCocac;
    @OneToMany(mappedBy = "ideCocac")
    private List<GthCuentaAnticipo> gthCuentaAnticipoList;
    @OneToMany(mappedBy = "ideCocac")
    private List<PreAsociacionPresupuestaria> preAsociacionPresupuestariaList;
    @OneToMany(mappedBy = "ideCocac")
    private List<ContVigente> contVigenteList;
    @JoinColumn(name = "ide_cogrc", referencedColumnName = "ide_cogrc")
    @ManyToOne
    private ContGrupoCuenta ideCogrc;
    @OneToMany(mappedBy = "conIdeCocac")
    private List<ContCatalogoCuenta> contCatalogoCuentaList;
    @JoinColumn(name = "con_ide_cocac", referencedColumnName = "ide_cocac")
    @ManyToOne
    private ContCatalogoCuenta conIdeCocac;
    @OneToMany(mappedBy = "conIdeCocac2")
    private List<ContCatalogoCuenta> contCatalogoCuentaList1;
    @JoinColumn(name = "con_ide_cocac2", referencedColumnName = "ide_cocac")
    @ManyToOne
    private ContCatalogoCuenta conIdeCocac2;

    public ContCatalogoCuenta() {
    }

    public ContCatalogoCuenta(Long ideCocac) {
        this.ideCocac = ideCocac;
    }

    public Long getIdeCocac() {
        return ideCocac;
    }

    public void setIdeCocac(Long ideCocac) {
        this.ideCocac = ideCocac;
    }

    public String getCueCodigoCocac() {
        return cueCodigoCocac;
    }

    public void setCueCodigoCocac(String cueCodigoCocac) {
        this.cueCodigoCocac = cueCodigoCocac;
    }

    public String getCueDescripcionCocac() {
        return cueDescripcionCocac;
    }

    public void setCueDescripcionCocac(String cueDescripcionCocac) {
        this.cueDescripcionCocac = cueDescripcionCocac;
    }

    public BigInteger getNivelCocac() {
        return nivelCocac;
    }

    public void setNivelCocac(BigInteger nivelCocac) {
        this.nivelCocac = nivelCocac;
    }

    public String getGrupoNivelCocac() {
        return grupoNivelCocac;
    }

    public void setGrupoNivelCocac(String grupoNivelCocac) {
        this.grupoNivelCocac = grupoNivelCocac;
    }

    public Boolean getActivoCocac() {
        return activoCocac;
    }

    public void setActivoCocac(Boolean activoCocac) {
        this.activoCocac = activoCocac;
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

    public Boolean getSigefCocac() {
        return sigefCocac;
    }

    public void setSigefCocac(Boolean sigefCocac) {
        this.sigefCocac = sigefCocac;
    }

    public Integer getAperturaCierreCocac() {
        return aperturaCierreCocac;
    }

    public void setAperturaCierreCocac(Integer aperturaCierreCocac) {
        this.aperturaCierreCocac = aperturaCierreCocac;
    }

    public List<GthCuentaAnticipo> getGthCuentaAnticipoList() {
        return gthCuentaAnticipoList;
    }

    public void setGthCuentaAnticipoList(List<GthCuentaAnticipo> gthCuentaAnticipoList) {
        this.gthCuentaAnticipoList = gthCuentaAnticipoList;
    }

    public List<PreAsociacionPresupuestaria> getPreAsociacionPresupuestariaList() {
        return preAsociacionPresupuestariaList;
    }

    public void setPreAsociacionPresupuestariaList(List<PreAsociacionPresupuestaria> preAsociacionPresupuestariaList) {
        this.preAsociacionPresupuestariaList = preAsociacionPresupuestariaList;
    }

    public List<ContVigente> getContVigenteList() {
        return contVigenteList;
    }

    public void setContVigenteList(List<ContVigente> contVigenteList) {
        this.contVigenteList = contVigenteList;
    }

    public ContGrupoCuenta getIdeCogrc() {
        return ideCogrc;
    }

    public void setIdeCogrc(ContGrupoCuenta ideCogrc) {
        this.ideCogrc = ideCogrc;
    }

    public List<ContCatalogoCuenta> getContCatalogoCuentaList() {
        return contCatalogoCuentaList;
    }

    public void setContCatalogoCuentaList(List<ContCatalogoCuenta> contCatalogoCuentaList) {
        this.contCatalogoCuentaList = contCatalogoCuentaList;
    }

    public ContCatalogoCuenta getConIdeCocac() {
        return conIdeCocac;
    }

    public void setConIdeCocac(ContCatalogoCuenta conIdeCocac) {
        this.conIdeCocac = conIdeCocac;
    }

    public List<ContCatalogoCuenta> getContCatalogoCuentaList1() {
        return contCatalogoCuentaList1;
    }

    public void setContCatalogoCuentaList1(List<ContCatalogoCuenta> contCatalogoCuentaList1) {
        this.contCatalogoCuentaList1 = contCatalogoCuentaList1;
    }

    public ContCatalogoCuenta getConIdeCocac2() {
        return conIdeCocac2;
    }

    public void setConIdeCocac2(ContCatalogoCuenta conIdeCocac2) {
        this.conIdeCocac2 = conIdeCocac2;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCocac != null ? ideCocac.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContCatalogoCuenta)) {
            return false;
        }
        ContCatalogoCuenta other = (ContCatalogoCuenta) object;
        if ((this.ideCocac == null && other.ideCocac != null) || (this.ideCocac != null && !this.ideCocac.equals(other.ideCocac))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.ContCatalogoCuenta[ ideCocac=" + ideCocac + " ]";
    }
    
}
