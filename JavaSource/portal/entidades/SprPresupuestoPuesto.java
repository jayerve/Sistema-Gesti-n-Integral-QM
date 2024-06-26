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
@Table(name = "spr_presupuesto_puesto", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SprPresupuestoPuesto.findAll", query = "SELECT s FROM SprPresupuestoPuesto s"),
    @NamedQuery(name = "SprPresupuestoPuesto.findByIdeSpprp", query = "SELECT s FROM SprPresupuestoPuesto s WHERE s.ideSpprp = :ideSpprp"),
    @NamedQuery(name = "SprPresupuestoPuesto.findByCargoParaSpprp", query = "SELECT s FROM SprPresupuestoPuesto s WHERE s.cargoParaSpprp = :cargoParaSpprp"),
    @NamedQuery(name = "SprPresupuestoPuesto.findByCargoDeSpprp", query = "SELECT s FROM SprPresupuestoPuesto s WHERE s.cargoDeSpprp = :cargoDeSpprp"),
    @NamedQuery(name = "SprPresupuestoPuesto.findByNroDocumentoSpprp", query = "SELECT s FROM SprPresupuestoPuesto s WHERE s.nroDocumentoSpprp = :nroDocumentoSpprp"),
    @NamedQuery(name = "SprPresupuestoPuesto.findByFechaSpprp", query = "SELECT s FROM SprPresupuestoPuesto s WHERE s.fechaSpprp = :fechaSpprp"),
    @NamedQuery(name = "SprPresupuestoPuesto.findByDetalleSpprp", query = "SELECT s FROM SprPresupuestoPuesto s WHERE s.detalleSpprp = :detalleSpprp"),
    @NamedQuery(name = "SprPresupuestoPuesto.findByAprobadoSpprp", query = "SELECT s FROM SprPresupuestoPuesto s WHERE s.aprobadoSpprp = :aprobadoSpprp"),
    @NamedQuery(name = "SprPresupuestoPuesto.findByActivoSpprp", query = "SELECT s FROM SprPresupuestoPuesto s WHERE s.activoSpprp = :activoSpprp"),
    @NamedQuery(name = "SprPresupuestoPuesto.findByUsuarioIngre", query = "SELECT s FROM SprPresupuestoPuesto s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SprPresupuestoPuesto.findByFechaIngre", query = "SELECT s FROM SprPresupuestoPuesto s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SprPresupuestoPuesto.findByHoraIngre", query = "SELECT s FROM SprPresupuestoPuesto s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SprPresupuestoPuesto.findByUsuarioActua", query = "SELECT s FROM SprPresupuestoPuesto s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SprPresupuestoPuesto.findByFechaActua", query = "SELECT s FROM SprPresupuestoPuesto s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SprPresupuestoPuesto.findByHoraActua", query = "SELECT s FROM SprPresupuestoPuesto s WHERE s.horaActua = :horaActua")})
public class SprPresupuestoPuesto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_spprp", nullable = false)
    private Integer ideSpprp;
    @Size(max = 50)
    @Column(name = "cargo_para_spprp", length = 50)
    private String cargoParaSpprp;
    @Size(max = 50)
    @Column(name = "cargo_de_spprp", length = 50)
    private String cargoDeSpprp;
    @Size(max = 50)
    @Column(name = "nro_documento_spprp", length = 50)
    private String nroDocumentoSpprp;
    @Column(name = "fecha_spprp")
    @Temporal(TemporalType.DATE)
    private Date fechaSpprp;
    @Size(max = 4000)
    @Column(name = "detalle_spprp", length = 4000)
    private String detalleSpprp;
    @Column(name = "aprobado_spprp")
    private Integer aprobadoSpprp;
    @Column(name = "activo_spprp")
    private Boolean activoSpprp;
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
    @OneToMany(mappedBy = "ideSpprp")
    private List<SprPresupuestoArchivo> sprPresupuestoArchivoList;
    @OneToMany(mappedBy = "ideSpprp")
    private List<SprCargoPresupuesto> sprCargoPresupuestoList;
    @OneToMany(mappedBy = "ideSpprp")
    private List<SprPresupuestoBeneficiario> sprPresupuestoBeneficiarioList;

    public SprPresupuestoPuesto() {
    }

    public SprPresupuestoPuesto(Integer ideSpprp) {
        this.ideSpprp = ideSpprp;
    }

    public Integer getIdeSpprp() {
        return ideSpprp;
    }

    public void setIdeSpprp(Integer ideSpprp) {
        this.ideSpprp = ideSpprp;
    }

    public String getCargoParaSpprp() {
        return cargoParaSpprp;
    }

    public void setCargoParaSpprp(String cargoParaSpprp) {
        this.cargoParaSpprp = cargoParaSpprp;
    }

    public String getCargoDeSpprp() {
        return cargoDeSpprp;
    }

    public void setCargoDeSpprp(String cargoDeSpprp) {
        this.cargoDeSpprp = cargoDeSpprp;
    }

    public String getNroDocumentoSpprp() {
        return nroDocumentoSpprp;
    }

    public void setNroDocumentoSpprp(String nroDocumentoSpprp) {
        this.nroDocumentoSpprp = nroDocumentoSpprp;
    }

    public Date getFechaSpprp() {
        return fechaSpprp;
    }

    public void setFechaSpprp(Date fechaSpprp) {
        this.fechaSpprp = fechaSpprp;
    }

    public String getDetalleSpprp() {
        return detalleSpprp;
    }

    public void setDetalleSpprp(String detalleSpprp) {
        this.detalleSpprp = detalleSpprp;
    }

    public Integer getAprobadoSpprp() {
        return aprobadoSpprp;
    }

    public void setAprobadoSpprp(Integer aprobadoSpprp) {
        this.aprobadoSpprp = aprobadoSpprp;
    }

    public Boolean getActivoSpprp() {
        return activoSpprp;
    }

    public void setActivoSpprp(Boolean activoSpprp) {
        this.activoSpprp = activoSpprp;
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

    public List<SprPresupuestoArchivo> getSprPresupuestoArchivoList() {
        return sprPresupuestoArchivoList;
    }

    public void setSprPresupuestoArchivoList(List<SprPresupuestoArchivo> sprPresupuestoArchivoList) {
        this.sprPresupuestoArchivoList = sprPresupuestoArchivoList;
    }

    public List<SprCargoPresupuesto> getSprCargoPresupuestoList() {
        return sprCargoPresupuestoList;
    }

    public void setSprCargoPresupuestoList(List<SprCargoPresupuesto> sprCargoPresupuestoList) {
        this.sprCargoPresupuestoList = sprCargoPresupuestoList;
    }

    public List<SprPresupuestoBeneficiario> getSprPresupuestoBeneficiarioList() {
        return sprPresupuestoBeneficiarioList;
    }

    public void setSprPresupuestoBeneficiarioList(List<SprPresupuestoBeneficiario> sprPresupuestoBeneficiarioList) {
        this.sprPresupuestoBeneficiarioList = sprPresupuestoBeneficiarioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSpprp != null ? ideSpprp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SprPresupuestoPuesto)) {
            return false;
        }
        SprPresupuestoPuesto other = (SprPresupuestoPuesto) object;
        if ((this.ideSpprp == null && other.ideSpprp != null) || (this.ideSpprp != null && !this.ideSpprp.equals(other.ideSpprp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SprPresupuestoPuesto[ ideSpprp=" + ideSpprp + " ]";
    }
    
}
