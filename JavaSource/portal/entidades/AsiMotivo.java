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
@Table(name = "asi_motivo", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "AsiMotivo.findAll", query = "SELECT a FROM AsiMotivo a"),
    @NamedQuery(name = "AsiMotivo.findByIdeAsmot", query = "SELECT a FROM AsiMotivo a WHERE a.ideAsmot = :ideAsmot"),
    @NamedQuery(name = "AsiMotivo.findByDetalleAsmot", query = "SELECT a FROM AsiMotivo a WHERE a.detalleAsmot = :detalleAsmot"),
    @NamedQuery(name = "AsiMotivo.findByRemuneradoAsmot", query = "SELECT a FROM AsiMotivo a WHERE a.remuneradoAsmot = :remuneradoAsmot"),
    @NamedQuery(name = "AsiMotivo.findByArchivoAsmot", query = "SELECT a FROM AsiMotivo a WHERE a.archivoAsmot = :archivoAsmot"),
    @NamedQuery(name = "AsiMotivo.findByActivoAsmot", query = "SELECT a FROM AsiMotivo a WHERE a.activoAsmot = :activoAsmot"),
    @NamedQuery(name = "AsiMotivo.findByUsuarioIngre", query = "SELECT a FROM AsiMotivo a WHERE a.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "AsiMotivo.findByFechaIngre", query = "SELECT a FROM AsiMotivo a WHERE a.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "AsiMotivo.findByUsuarioActua", query = "SELECT a FROM AsiMotivo a WHERE a.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "AsiMotivo.findByFechaActua", query = "SELECT a FROM AsiMotivo a WHERE a.fechaActua = :fechaActua"),
    @NamedQuery(name = "AsiMotivo.findByHoraIngre", query = "SELECT a FROM AsiMotivo a WHERE a.horaIngre = :horaIngre"),
    @NamedQuery(name = "AsiMotivo.findByHoraActua", query = "SELECT a FROM AsiMotivo a WHERE a.horaActua = :horaActua"),
    @NamedQuery(name = "AsiMotivo.findByAplicaVacacionesAsmot", query = "SELECT a FROM AsiMotivo a WHERE a.aplicaVacacionesAsmot = :aplicaVacacionesAsmot")})
public class AsiMotivo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_asmot", nullable = false)
    private Integer ideAsmot;
    @Size(max = 50)
    @Column(name = "detalle_asmot", length = 50)
    private String detalleAsmot;
    @Column(name = "remunerado_asmot")
    private Integer remuneradoAsmot;
    @Column(name = "archivo_asmot")
    private Integer archivoAsmot;
   
    @Column(name = "activo_asmot")
    private Boolean activoAsmot;
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
    @Column(name = "aplica_vacaciones_asmot")
    private Boolean aplicaVacacionesAsmot;
    @OneToMany(mappedBy = "ideAsmot")
    private List<AsiPermisosVacacionHext> asiPermisosVacacionHextList;
    @OneToMany(mappedBy = "ideAsmot")
    private List<AsiNovedadDetalle> asiNovedadDetalleList;
    @OneToMany(mappedBy = "ideAsmot")
    private List<AsiDetalleHorasExtras> asiDetalleHorasExtrasList;
    @JoinColumn(name = "ide_nrrub", referencedColumnName = "ide_nrrub")
    @ManyToOne
    private NrhRubro ideNrrub;
    @JoinColumn(name = "ide_getpr", referencedColumnName = "ide_getpr")
    @ManyToOne
    private GenTipoPeriodo ideGetpr;
    @JoinColumn(name = "ide_asgrm", referencedColumnName = "ide_asgrm")
    @ManyToOne
    private AsiGrupoMotivo ideAsgrm;

    public AsiMotivo() {
    }

    public AsiMotivo(Integer ideAsmot) {
        this.ideAsmot = ideAsmot;
    }

    public Integer getIdeAsmot() {
        return ideAsmot;
    }

    public void setIdeAsmot(Integer ideAsmot) {
        this.ideAsmot = ideAsmot;
    }

    public String getDetalleAsmot() {
        return detalleAsmot;
    }

    public void setDetalleAsmot(String detalleAsmot) {
        this.detalleAsmot = detalleAsmot;
    }

    public Integer getRemuneradoAsmot() {
        return remuneradoAsmot;
    }

    public void setRemuneradoAsmot(Integer remuneradoAsmot) {
        this.remuneradoAsmot = remuneradoAsmot;
    }

    public Integer getArchivoAsmot() {
        return archivoAsmot;
    }

    public void setArchivoAsmot(Integer archivoAsmot) {
        this.archivoAsmot = archivoAsmot;
    }



    public Boolean getActivoAsmot() {
        return activoAsmot;
    }

    public void setActivoAsmot(Boolean activoAsmot) {
        this.activoAsmot = activoAsmot;
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

    public Boolean getAplicaVacacionesAsmot() {
        return aplicaVacacionesAsmot;
    }

    public void setAplicaVacacionesAsmot(Boolean aplicaVacacionesAsmot) {
        this.aplicaVacacionesAsmot = aplicaVacacionesAsmot;
    }

    public List<AsiPermisosVacacionHext> getAsiPermisosVacacionHextList() {
        return asiPermisosVacacionHextList;
    }

    public void setAsiPermisosVacacionHextList(List<AsiPermisosVacacionHext> asiPermisosVacacionHextList) {
        this.asiPermisosVacacionHextList = asiPermisosVacacionHextList;
    }

    public List<AsiNovedadDetalle> getAsiNovedadDetalleList() {
        return asiNovedadDetalleList;
    }

    public void setAsiNovedadDetalleList(List<AsiNovedadDetalle> asiNovedadDetalleList) {
        this.asiNovedadDetalleList = asiNovedadDetalleList;
    }

    public List<AsiDetalleHorasExtras> getAsiDetalleHorasExtrasList() {
        return asiDetalleHorasExtrasList;
    }

    public void setAsiDetalleHorasExtrasList(List<AsiDetalleHorasExtras> asiDetalleHorasExtrasList) {
        this.asiDetalleHorasExtrasList = asiDetalleHorasExtrasList;
    }

    public NrhRubro getIdeNrrub() {
        return ideNrrub;
    }

    public void setIdeNrrub(NrhRubro ideNrrub) {
        this.ideNrrub = ideNrrub;
    }

    public GenTipoPeriodo getIdeGetpr() {
        return ideGetpr;
    }

    public void setIdeGetpr(GenTipoPeriodo ideGetpr) {
        this.ideGetpr = ideGetpr;
    }

    public AsiGrupoMotivo getIdeAsgrm() {
        return ideAsgrm;
    }

    public void setIdeAsgrm(AsiGrupoMotivo ideAsgrm) {
        this.ideAsgrm = ideAsgrm;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAsmot != null ? ideAsmot.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AsiMotivo)) {
            return false;
        }
        AsiMotivo other = (AsiMotivo) object;
        if ((this.ideAsmot == null && other.ideAsmot != null) || (this.ideAsmot != null && !this.ideAsmot.equals(other.ideAsmot))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.AsiMotivo[ ideAsmot=" + ideAsmot + " ]";
    }
    
}
