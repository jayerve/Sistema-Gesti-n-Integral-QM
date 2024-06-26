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
@Table(name = "spr_solicitud_puesto", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SprSolicitudPuesto.findAll", query = "SELECT s FROM SprSolicitudPuesto s"),
    @NamedQuery(name = "SprSolicitudPuesto.findByIdeSpsop", query = "SELECT s FROM SprSolicitudPuesto s WHERE s.ideSpsop = :ideSpsop"),
    @NamedQuery(name = "SprSolicitudPuesto.findByNroDocumentoSpsop", query = "SELECT s FROM SprSolicitudPuesto s WHERE s.nroDocumentoSpsop = :nroDocumentoSpsop"),
    @NamedQuery(name = "SprSolicitudPuesto.findByFechaSpsop", query = "SELECT s FROM SprSolicitudPuesto s WHERE s.fechaSpsop = :fechaSpsop"),
    @NamedQuery(name = "SprSolicitudPuesto.findByDetalleSpsop", query = "SELECT s FROM SprSolicitudPuesto s WHERE s.detalleSpsop = :detalleSpsop"),
    @NamedQuery(name = "SprSolicitudPuesto.findByCargoParaSpsop", query = "SELECT s FROM SprSolicitudPuesto s WHERE s.cargoParaSpsop = :cargoParaSpsop"),
    @NamedQuery(name = "SprSolicitudPuesto.findByCargoDeSpsop", query = "SELECT s FROM SprSolicitudPuesto s WHERE s.cargoDeSpsop = :cargoDeSpsop"),
    @NamedQuery(name = "SprSolicitudPuesto.findByRevizadoSpsop", query = "SELECT s FROM SprSolicitudPuesto s WHERE s.revizadoSpsop = :revizadoSpsop"),
    @NamedQuery(name = "SprSolicitudPuesto.findByActivoSpsop", query = "SELECT s FROM SprSolicitudPuesto s WHERE s.activoSpsop = :activoSpsop"),
    @NamedQuery(name = "SprSolicitudPuesto.findByUsuarioIngre", query = "SELECT s FROM SprSolicitudPuesto s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SprSolicitudPuesto.findByFechaIngre", query = "SELECT s FROM SprSolicitudPuesto s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SprSolicitudPuesto.findByHoraIngre", query = "SELECT s FROM SprSolicitudPuesto s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SprSolicitudPuesto.findByUsuarioActua", query = "SELECT s FROM SprSolicitudPuesto s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SprSolicitudPuesto.findByFechaActua", query = "SELECT s FROM SprSolicitudPuesto s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SprSolicitudPuesto.findByHoraActua", query = "SELECT s FROM SprSolicitudPuesto s WHERE s.horaActua = :horaActua")})
public class SprSolicitudPuesto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_spsop", nullable = false)
    private Integer ideSpsop;
    @Size(max = 50)
    @Column(name = "nro_documento_spsop", length = 50)
    private String nroDocumentoSpsop;
    @Column(name = "fecha_spsop")
    @Temporal(TemporalType.DATE)
    private Date fechaSpsop;
    @Size(max = 4000)
    @Column(name = "detalle_spsop", length = 4000)
    private String detalleSpsop;
    @Size(max = 50)
    @Column(name = "cargo_para_spsop", length = 50)
    private String cargoParaSpsop;
    @Size(max = 50)
    @Column(name = "cargo_de_spsop", length = 50)
    private String cargoDeSpsop;
    @Column(name = "revizado_spsop")
    private Integer revizadoSpsop;
    @Column(name = "activo_spsop")
    private Boolean activoSpsop;
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
    @OneToMany(mappedBy = "ideSpsop")
    private List<SprPresupuestoPuesto> sprPresupuestoPuestoList;
    @OneToMany(mappedBy = "ideSpsop")
    private List<SprDetalleSolicitudPuesto> sprDetalleSolicitudPuestoList;
    @OneToMany(mappedBy = "ideSpsop")
    private List<SprResponsableCalificacion> sprResponsableCalificacionList;
    @OneToMany(mappedBy = "ideSpsop")
    private List<SprSolicitudAprobacion> sprSolicitudAprobacionList;
    @OneToMany(mappedBy = "sprIdeSpsop")
    private List<SprSolicitudPuesto> sprSolicitudPuestoList;
    @JoinColumn(name = "spr_ide_spsop", referencedColumnName = "ide_spsop")
    @ManyToOne
    private SprSolicitudPuesto sprIdeSpsop;
    @JoinColumn(name = "ide_spasu", referencedColumnName = "ide_spasu")
    @ManyToOne
    private SprAsunto ideSpasu;
    @JoinColumn(name = "ide_gttdc", referencedColumnName = "ide_gttdc")
    @ManyToOne
    private GthTipoDocumento ideGttdc;
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar ideGeedp;
    @JoinColumn(name = "gen_ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar genIdeGeedp;

    public SprSolicitudPuesto() {
    }

    public SprSolicitudPuesto(Integer ideSpsop) {
        this.ideSpsop = ideSpsop;
    }

    public Integer getIdeSpsop() {
        return ideSpsop;
    }

    public void setIdeSpsop(Integer ideSpsop) {
        this.ideSpsop = ideSpsop;
    }

    public String getNroDocumentoSpsop() {
        return nroDocumentoSpsop;
    }

    public void setNroDocumentoSpsop(String nroDocumentoSpsop) {
        this.nroDocumentoSpsop = nroDocumentoSpsop;
    }

    public Date getFechaSpsop() {
        return fechaSpsop;
    }

    public void setFechaSpsop(Date fechaSpsop) {
        this.fechaSpsop = fechaSpsop;
    }

    public String getDetalleSpsop() {
        return detalleSpsop;
    }

    public void setDetalleSpsop(String detalleSpsop) {
        this.detalleSpsop = detalleSpsop;
    }

    public String getCargoParaSpsop() {
        return cargoParaSpsop;
    }

    public void setCargoParaSpsop(String cargoParaSpsop) {
        this.cargoParaSpsop = cargoParaSpsop;
    }

    public String getCargoDeSpsop() {
        return cargoDeSpsop;
    }

    public void setCargoDeSpsop(String cargoDeSpsop) {
        this.cargoDeSpsop = cargoDeSpsop;
    }

    public Integer getRevizadoSpsop() {
        return revizadoSpsop;
    }

    public void setRevizadoSpsop(Integer revizadoSpsop) {
        this.revizadoSpsop = revizadoSpsop;
    }

    public Boolean getActivoSpsop() {
        return activoSpsop;
    }

    public void setActivoSpsop(Boolean activoSpsop) {
        this.activoSpsop = activoSpsop;
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

    public List<SprPresupuestoPuesto> getSprPresupuestoPuestoList() {
        return sprPresupuestoPuestoList;
    }

    public void setSprPresupuestoPuestoList(List<SprPresupuestoPuesto> sprPresupuestoPuestoList) {
        this.sprPresupuestoPuestoList = sprPresupuestoPuestoList;
    }

    public List<SprDetalleSolicitudPuesto> getSprDetalleSolicitudPuestoList() {
        return sprDetalleSolicitudPuestoList;
    }

    public void setSprDetalleSolicitudPuestoList(List<SprDetalleSolicitudPuesto> sprDetalleSolicitudPuestoList) {
        this.sprDetalleSolicitudPuestoList = sprDetalleSolicitudPuestoList;
    }

    public List<SprResponsableCalificacion> getSprResponsableCalificacionList() {
        return sprResponsableCalificacionList;
    }

    public void setSprResponsableCalificacionList(List<SprResponsableCalificacion> sprResponsableCalificacionList) {
        this.sprResponsableCalificacionList = sprResponsableCalificacionList;
    }

    public List<SprSolicitudAprobacion> getSprSolicitudAprobacionList() {
        return sprSolicitudAprobacionList;
    }

    public void setSprSolicitudAprobacionList(List<SprSolicitudAprobacion> sprSolicitudAprobacionList) {
        this.sprSolicitudAprobacionList = sprSolicitudAprobacionList;
    }

    public List<SprSolicitudPuesto> getSprSolicitudPuestoList() {
        return sprSolicitudPuestoList;
    }

    public void setSprSolicitudPuestoList(List<SprSolicitudPuesto> sprSolicitudPuestoList) {
        this.sprSolicitudPuestoList = sprSolicitudPuestoList;
    }

    public SprSolicitudPuesto getSprIdeSpsop() {
        return sprIdeSpsop;
    }

    public void setSprIdeSpsop(SprSolicitudPuesto sprIdeSpsop) {
        this.sprIdeSpsop = sprIdeSpsop;
    }

    public SprAsunto getIdeSpasu() {
        return ideSpasu;
    }

    public void setIdeSpasu(SprAsunto ideSpasu) {
        this.ideSpasu = ideSpasu;
    }

    public GthTipoDocumento getIdeGttdc() {
        return ideGttdc;
    }

    public void setIdeGttdc(GthTipoDocumento ideGttdc) {
        this.ideGttdc = ideGttdc;
    }

    public GenEmpleadosDepartamentoPar getIdeGeedp() {
        return ideGeedp;
    }

    public void setIdeGeedp(GenEmpleadosDepartamentoPar ideGeedp) {
        this.ideGeedp = ideGeedp;
    }

    public GenEmpleadosDepartamentoPar getGenIdeGeedp() {
        return genIdeGeedp;
    }

    public void setGenIdeGeedp(GenEmpleadosDepartamentoPar genIdeGeedp) {
        this.genIdeGeedp = genIdeGeedp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSpsop != null ? ideSpsop.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SprSolicitudPuesto)) {
            return false;
        }
        SprSolicitudPuesto other = (SprSolicitudPuesto) object;
        if ((this.ideSpsop == null && other.ideSpsop != null) || (this.ideSpsop != null && !this.ideSpsop.equals(other.ideSpsop))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SprSolicitudPuesto[ ideSpsop=" + ideSpsop + " ]";
    }
    
}
