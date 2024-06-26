/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "gth_tipo_parentesco_relacion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthTipoParentescoRelacion.findAll", query = "SELECT g FROM GthTipoParentescoRelacion g"),
    @NamedQuery(name = "GthTipoParentescoRelacion.findByIdeGttpr", query = "SELECT g FROM GthTipoParentescoRelacion g WHERE g.ideGttpr = :ideGttpr"),
    @NamedQuery(name = "GthTipoParentescoRelacion.findByDetalleGttpr", query = "SELECT g FROM GthTipoParentescoRelacion g WHERE g.detalleGttpr = :detalleGttpr"),
    @NamedQuery(name = "GthTipoParentescoRelacion.findByCodigoSbsGttpr", query = "SELECT g FROM GthTipoParentescoRelacion g WHERE g.codigoSbsGttpr = :codigoSbsGttpr"),
    @NamedQuery(name = "GthTipoParentescoRelacion.findByActivoGttpr", query = "SELECT g FROM GthTipoParentescoRelacion g WHERE g.activoGttpr = :activoGttpr"),
    @NamedQuery(name = "GthTipoParentescoRelacion.findByUsuarioIngre", query = "SELECT g FROM GthTipoParentescoRelacion g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthTipoParentescoRelacion.findByFechaIngre", query = "SELECT g FROM GthTipoParentescoRelacion g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthTipoParentescoRelacion.findByUsuarioActua", query = "SELECT g FROM GthTipoParentescoRelacion g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthTipoParentescoRelacion.findByFechaActua", query = "SELECT g FROM GthTipoParentescoRelacion g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthTipoParentescoRelacion.findByHoraIngre", query = "SELECT g FROM GthTipoParentescoRelacion g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthTipoParentescoRelacion.findByHoraActua", query = "SELECT g FROM GthTipoParentescoRelacion g WHERE g.horaActua = :horaActua"),
    @NamedQuery(name = "GthTipoParentescoRelacion.findByDependienteGttpr", query = "SELECT g FROM GthTipoParentescoRelacion g WHERE g.dependienteGttpr = :dependienteGttpr")})
public class GthTipoParentescoRelacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gttpr", nullable = false)
    private Integer ideGttpr;
    @Size(max = 50)
    @Column(name = "detalle_gttpr", length = 50)
    private String detalleGttpr;
    @Size(max = 50)
    @Column(name = "codigo_sbs_gttpr", length = 50)
    private String codigoSbsGttpr;
    @Column(name = "activo_gttpr")
    private Boolean activoGttpr;
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
    @Column(name = "dependiente_gttpr")
    private Boolean dependienteGttpr;
    // @OneToMany(mappedBy = "ideGttpr")
    // private List<SbsArchivoVeinteTres> sbsArchivoVeinteTresList;
    @OneToMany(mappedBy = "ideGttpr")
    private List<GthBeneficiarioSeguro> gthBeneficiarioSeguroList;
    @OneToMany(mappedBy = "ideGttpr")
    private List<GthFamiliar> gthFamiliarList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGttpr")
    private List<GthCargasFamiliares> gthCargasFamiliaresList;
    @OneToMany(mappedBy = "ideGttpr")
    private List<GthPersonaEmergencia> gthPersonaEmergenciaList;
    @OneToMany(mappedBy = "ideGttpr")
    private List<NrhRetencionJudicial> nrhRetencionJudicialList;

    public GthTipoParentescoRelacion() {
    }

    public GthTipoParentescoRelacion(Integer ideGttpr) {
        this.ideGttpr = ideGttpr;
    }

    public Integer getIdeGttpr() {
        return ideGttpr;
    }

    public void setIdeGttpr(Integer ideGttpr) {
        this.ideGttpr = ideGttpr;
    }

    public String getDetalleGttpr() {
        return detalleGttpr;
    }

    public void setDetalleGttpr(String detalleGttpr) {
        this.detalleGttpr = detalleGttpr;
    }

    public String getCodigoSbsGttpr() {
        return codigoSbsGttpr;
    }

    public void setCodigoSbsGttpr(String codigoSbsGttpr) {
        this.codigoSbsGttpr = codigoSbsGttpr;
    }

    public Boolean getActivoGttpr() {
        return activoGttpr;
    }

    public void setActivoGttpr(Boolean activoGttpr) {
        this.activoGttpr = activoGttpr;
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

    public Boolean getDependienteGttpr() {
        return dependienteGttpr;
    }

    public void setDependienteGttpr(Boolean dependienteGttpr) {
        this.dependienteGttpr = dependienteGttpr;
    }

   /* public List<SbsArchivoVeinteTres> getSbsArchivoVeinteTresList() {
        return sbsArchivoVeinteTresList;
    }

    public void setSbsArchivoVeinteTresList(List<SbsArchivoVeinteTres> sbsArchivoVeinteTresList) {
        this.sbsArchivoVeinteTresList = sbsArchivoVeinteTresList;
    }*/

    public List<GthBeneficiarioSeguro> getGthBeneficiarioSeguroList() {
        return gthBeneficiarioSeguroList;
    }

    public void setGthBeneficiarioSeguroList(List<GthBeneficiarioSeguro> gthBeneficiarioSeguroList) {
        this.gthBeneficiarioSeguroList = gthBeneficiarioSeguroList;
    }

    public List<GthFamiliar> getGthFamiliarList() {
        return gthFamiliarList;
    }

    public void setGthFamiliarList(List<GthFamiliar> gthFamiliarList) {
        this.gthFamiliarList = gthFamiliarList;
    }

    public List<GthCargasFamiliares> getGthCargasFamiliaresList() {
        return gthCargasFamiliaresList;
    }

    public void setGthCargasFamiliaresList(List<GthCargasFamiliares> gthCargasFamiliaresList) {
        this.gthCargasFamiliaresList = gthCargasFamiliaresList;
    }

    public List<GthPersonaEmergencia> getGthPersonaEmergenciaList() {
        return gthPersonaEmergenciaList;
    }

    public void setGthPersonaEmergenciaList(List<GthPersonaEmergencia> gthPersonaEmergenciaList) {
        this.gthPersonaEmergenciaList = gthPersonaEmergenciaList;
    }

    public List<NrhRetencionJudicial> getNrhRetencionJudicialList() {
        return nrhRetencionJudicialList;
    }

    public void setNrhRetencionJudicialList(List<NrhRetencionJudicial> nrhRetencionJudicialList) {
        this.nrhRetencionJudicialList = nrhRetencionJudicialList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGttpr != null ? ideGttpr.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTipoParentescoRelacion)) {
            return false;
        }
        GthTipoParentescoRelacion other = (GthTipoParentescoRelacion) object;
        if ((this.ideGttpr == null && other.ideGttpr != null) || (this.ideGttpr != null && !this.ideGttpr.equals(other.ideGttpr))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTipoParentescoRelacion[ ideGttpr=" + ideGttpr + " ]";
    }
    
}
