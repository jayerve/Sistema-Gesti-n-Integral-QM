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
@Table(name = "spr_solicitud_aprobacion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SprSolicitudAprobacion.findAll", query = "SELECT s FROM SprSolicitudAprobacion s"),
    @NamedQuery(name = "SprSolicitudAprobacion.findByIdeSpsoa", query = "SELECT s FROM SprSolicitudAprobacion s WHERE s.ideSpsoa = :ideSpsoa"),
    @NamedQuery(name = "SprSolicitudAprobacion.findByNroDocumentoSpsoa", query = "SELECT s FROM SprSolicitudAprobacion s WHERE s.nroDocumentoSpsoa = :nroDocumentoSpsoa"),
    @NamedQuery(name = "SprSolicitudAprobacion.findByFechaSpsoa", query = "SELECT s FROM SprSolicitudAprobacion s WHERE s.fechaSpsoa = :fechaSpsoa"),
    @NamedQuery(name = "SprSolicitudAprobacion.findByDetalleSpsoa", query = "SELECT s FROM SprSolicitudAprobacion s WHERE s.detalleSpsoa = :detalleSpsoa"),
    @NamedQuery(name = "SprSolicitudAprobacion.findByCargoParaSpsoa", query = "SELECT s FROM SprSolicitudAprobacion s WHERE s.cargoParaSpsoa = :cargoParaSpsoa"),
    @NamedQuery(name = "SprSolicitudAprobacion.findByCargoDeSpsoa", query = "SELECT s FROM SprSolicitudAprobacion s WHERE s.cargoDeSpsoa = :cargoDeSpsoa"),
    @NamedQuery(name = "SprSolicitudAprobacion.findByAprobadoSpsoa", query = "SELECT s FROM SprSolicitudAprobacion s WHERE s.aprobadoSpsoa = :aprobadoSpsoa"),
    @NamedQuery(name = "SprSolicitudAprobacion.findByActivoSpsoa", query = "SELECT s FROM SprSolicitudAprobacion s WHERE s.activoSpsoa = :activoSpsoa"),
    @NamedQuery(name = "SprSolicitudAprobacion.findByUsuarioIngre", query = "SELECT s FROM SprSolicitudAprobacion s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SprSolicitudAprobacion.findByFechaIngre", query = "SELECT s FROM SprSolicitudAprobacion s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SprSolicitudAprobacion.findByHoraIngre", query = "SELECT s FROM SprSolicitudAprobacion s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SprSolicitudAprobacion.findByUsuarioActua", query = "SELECT s FROM SprSolicitudAprobacion s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SprSolicitudAprobacion.findByFechaActua", query = "SELECT s FROM SprSolicitudAprobacion s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SprSolicitudAprobacion.findByHoraActua", query = "SELECT s FROM SprSolicitudAprobacion s WHERE s.horaActua = :horaActua")})
public class SprSolicitudAprobacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_spsoa", nullable = false)
    private Integer ideSpsoa;
    @Size(max = 50)
    @Column(name = "nro_documento_spsoa", length = 50)
    private String nroDocumentoSpsoa;
    @Column(name = "fecha_spsoa")
    @Temporal(TemporalType.DATE)
    private Date fechaSpsoa;
    @Size(max = 4000)
    @Column(name = "detalle_spsoa", length = 4000)
    private String detalleSpsoa;
    @Size(max = 50)
    @Column(name = "cargo_para_spsoa", length = 50)
    private String cargoParaSpsoa;
    @Size(max = 50)
    @Column(name = "cargo_de_spsoa", length = 50)
    private String cargoDeSpsoa;
    @Column(name = "aprobado_spsoa")
    private Integer aprobadoSpsoa;
    @Column(name = "activo_spsoa")
    private Boolean activoSpsoa;
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
    @JoinColumn(name = "ide_spsop", referencedColumnName = "ide_spsop")
    @ManyToOne
    private SprSolicitudPuesto ideSpsop;
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
    @OneToMany(mappedBy = "ideSpsoa")
    private List<SprPostulante> sprPostulanteList;

    public SprSolicitudAprobacion() {
    }

    public SprSolicitudAprobacion(Integer ideSpsoa) {
        this.ideSpsoa = ideSpsoa;
    }

    public Integer getIdeSpsoa() {
        return ideSpsoa;
    }

    public void setIdeSpsoa(Integer ideSpsoa) {
        this.ideSpsoa = ideSpsoa;
    }

    public String getNroDocumentoSpsoa() {
        return nroDocumentoSpsoa;
    }

    public void setNroDocumentoSpsoa(String nroDocumentoSpsoa) {
        this.nroDocumentoSpsoa = nroDocumentoSpsoa;
    }

    public Date getFechaSpsoa() {
        return fechaSpsoa;
    }

    public void setFechaSpsoa(Date fechaSpsoa) {
        this.fechaSpsoa = fechaSpsoa;
    }

    public String getDetalleSpsoa() {
        return detalleSpsoa;
    }

    public void setDetalleSpsoa(String detalleSpsoa) {
        this.detalleSpsoa = detalleSpsoa;
    }

    public String getCargoParaSpsoa() {
        return cargoParaSpsoa;
    }

    public void setCargoParaSpsoa(String cargoParaSpsoa) {
        this.cargoParaSpsoa = cargoParaSpsoa;
    }

    public String getCargoDeSpsoa() {
        return cargoDeSpsoa;
    }

    public void setCargoDeSpsoa(String cargoDeSpsoa) {
        this.cargoDeSpsoa = cargoDeSpsoa;
    }

    public Integer getAprobadoSpsoa() {
        return aprobadoSpsoa;
    }

    public void setAprobadoSpsoa(Integer aprobadoSpsoa) {
        this.aprobadoSpsoa = aprobadoSpsoa;
    }

    public Boolean getActivoSpsoa() {
        return activoSpsoa;
    }

    public void setActivoSpsoa(Boolean activoSpsoa) {
        this.activoSpsoa = activoSpsoa;
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

    public SprSolicitudPuesto getIdeSpsop() {
        return ideSpsop;
    }

    public void setIdeSpsop(SprSolicitudPuesto ideSpsop) {
        this.ideSpsop = ideSpsop;
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

    public List<SprPostulante> getSprPostulanteList() {
        return sprPostulanteList;
    }

    public void setSprPostulanteList(List<SprPostulante> sprPostulanteList) {
        this.sprPostulanteList = sprPostulanteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSpsoa != null ? ideSpsoa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SprSolicitudAprobacion)) {
            return false;
        }
        SprSolicitudAprobacion other = (SprSolicitudAprobacion) object;
        if ((this.ideSpsoa == null && other.ideSpsoa != null) || (this.ideSpsoa != null && !this.ideSpsoa.equals(other.ideSpsoa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SprSolicitudAprobacion[ ideSpsoa=" + ideSpsoa + " ]";
    }
    
}
