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
@Table(name = "gen_tipo_persona", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GenTipoPersona.findAll", query = "SELECT g FROM GenTipoPersona g"),
    @NamedQuery(name = "GenTipoPersona.findByIdeGetip", query = "SELECT g FROM GenTipoPersona g WHERE g.ideGetip = :ideGetip"),
    @NamedQuery(name = "GenTipoPersona.findByDetalleGetip", query = "SELECT g FROM GenTipoPersona g WHERE g.detalleGetip = :detalleGetip"),
    @NamedQuery(name = "GenTipoPersona.findByActivoGetip", query = "SELECT g FROM GenTipoPersona g WHERE g.activoGetip = :activoGetip"),
    @NamedQuery(name = "GenTipoPersona.findByUsuarioIngre", query = "SELECT g FROM GenTipoPersona g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenTipoPersona.findByFechaIngre", query = "SELECT g FROM GenTipoPersona g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenTipoPersona.findByHoraIngre", query = "SELECT g FROM GenTipoPersona g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenTipoPersona.findByUsuarioActua", query = "SELECT g FROM GenTipoPersona g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenTipoPersona.findByFechaActua", query = "SELECT g FROM GenTipoPersona g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenTipoPersona.findByHoraActua", query = "SELECT g FROM GenTipoPersona g WHERE g.horaActua = :horaActua")})
public class GenTipoPersona implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_getip", nullable = false)
    private Long ideGetip;
    @Size(max = 50)
    @Column(name = "detalle_getip", length = 50)
    private String detalleGetip;
    @Column(name = "activo_getip")
    private Boolean activoGetip;
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
    @OneToMany(mappedBy = "ideGetip")
    private List<PreComparecienteContrato> preComparecienteContratoList;
    @OneToMany(mappedBy = "ideGetip")
    private List<GenTipoPersonaModulo> genTipoPersonaModuloList;
    @OneToMany(mappedBy = "ideGetip")
    private List<BodtConceptoEgreso> bodtConceptoEgresoList;
    @OneToMany(mappedBy = "ideGetip")
    private List<ContResponsableConvenio> contResponsableConvenioList;

    public GenTipoPersona() {
    }

    public GenTipoPersona(Long ideGetip) {
        this.ideGetip = ideGetip;
    }

    public Long getIdeGetip() {
        return ideGetip;
    }

    public void setIdeGetip(Long ideGetip) {
        this.ideGetip = ideGetip;
    }

    public String getDetalleGetip() {
        return detalleGetip;
    }

    public void setDetalleGetip(String detalleGetip) {
        this.detalleGetip = detalleGetip;
    }

    public Boolean getActivoGetip() {
        return activoGetip;
    }

    public void setActivoGetip(Boolean activoGetip) {
        this.activoGetip = activoGetip;
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

    public List<PreComparecienteContrato> getPreComparecienteContratoList() {
        return preComparecienteContratoList;
    }

    public void setPreComparecienteContratoList(List<PreComparecienteContrato> preComparecienteContratoList) {
        this.preComparecienteContratoList = preComparecienteContratoList;
    }

    public List<GenTipoPersonaModulo> getGenTipoPersonaModuloList() {
        return genTipoPersonaModuloList;
    }

    public void setGenTipoPersonaModuloList(List<GenTipoPersonaModulo> genTipoPersonaModuloList) {
        this.genTipoPersonaModuloList = genTipoPersonaModuloList;
    }

    public List<BodtConceptoEgreso> getBodtConceptoEgresoList() {
        return bodtConceptoEgresoList;
    }

    public void setBodtConceptoEgresoList(List<BodtConceptoEgreso> bodtConceptoEgresoList) {
        this.bodtConceptoEgresoList = bodtConceptoEgresoList;
    }

    public List<ContResponsableConvenio> getContResponsableConvenioList() {
        return contResponsableConvenioList;
    }

    public void setContResponsableConvenioList(List<ContResponsableConvenio> contResponsableConvenioList) {
        this.contResponsableConvenioList = contResponsableConvenioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGetip != null ? ideGetip.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenTipoPersona)) {
            return false;
        }
        GenTipoPersona other = (GenTipoPersona) object;
        if ((this.ideGetip == null && other.ideGetip != null) || (this.ideGetip != null && !this.ideGetip.equals(other.ideGetip))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenTipoPersona[ ideGetip=" + ideGetip + " ]";
    }
    
}
