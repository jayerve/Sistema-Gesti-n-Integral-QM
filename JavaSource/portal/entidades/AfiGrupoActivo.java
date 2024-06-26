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
@Table(name = "afi_grupo_activo", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "AfiGrupoActivo.findAll", query = "SELECT a FROM AfiGrupoActivo a"),
    @NamedQuery(name = "AfiGrupoActivo.findByIdeAfgra", query = "SELECT a FROM AfiGrupoActivo a WHERE a.ideAfgra = :ideAfgra"),
    @NamedQuery(name = "AfiGrupoActivo.findByDetalleAfgra", query = "SELECT a FROM AfiGrupoActivo a WHERE a.detalleAfgra = :detalleAfgra"),
    @NamedQuery(name = "AfiGrupoActivo.findByVidaUtilAfgra", query = "SELECT a FROM AfiGrupoActivo a WHERE a.vidaUtilAfgra = :vidaUtilAfgra"),
    @NamedQuery(name = "AfiGrupoActivo.findByActivoAfgra", query = "SELECT a FROM AfiGrupoActivo a WHERE a.activoAfgra = :activoAfgra"),
    @NamedQuery(name = "AfiGrupoActivo.findByUsuarioIngre", query = "SELECT a FROM AfiGrupoActivo a WHERE a.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "AfiGrupoActivo.findByFechaIngre", query = "SELECT a FROM AfiGrupoActivo a WHERE a.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "AfiGrupoActivo.findByHoraIngre", query = "SELECT a FROM AfiGrupoActivo a WHERE a.horaIngre = :horaIngre"),
    @NamedQuery(name = "AfiGrupoActivo.findByUsuarioActua", query = "SELECT a FROM AfiGrupoActivo a WHERE a.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "AfiGrupoActivo.findByFechaActua", query = "SELECT a FROM AfiGrupoActivo a WHERE a.fechaActua = :fechaActua"),
    @NamedQuery(name = "AfiGrupoActivo.findByHoraActua", query = "SELECT a FROM AfiGrupoActivo a WHERE a.horaActua = :horaActua")})
public class AfiGrupoActivo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_afgra", nullable = false)
    private Long ideAfgra;
    @Size(max = 100)
    @Column(name = "detalle_afgra", length = 100)
    private String detalleAfgra;
    @Column(name = "vida_util_afgra")
    private BigInteger vidaUtilAfgra;
    @Column(name = "activo_afgra")
    private Boolean activoAfgra;
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
    @OneToMany(mappedBy = "ideAfgra")
    private List<AfiNombreActivo> afiNombreActivoList;

    public AfiGrupoActivo() {
    }

    public AfiGrupoActivo(Long ideAfgra) {
        this.ideAfgra = ideAfgra;
    }

    public Long getIdeAfgra() {
        return ideAfgra;
    }

    public void setIdeAfgra(Long ideAfgra) {
        this.ideAfgra = ideAfgra;
    }

    public String getDetalleAfgra() {
        return detalleAfgra;
    }

    public void setDetalleAfgra(String detalleAfgra) {
        this.detalleAfgra = detalleAfgra;
    }

    public BigInteger getVidaUtilAfgra() {
        return vidaUtilAfgra;
    }

    public void setVidaUtilAfgra(BigInteger vidaUtilAfgra) {
        this.vidaUtilAfgra = vidaUtilAfgra;
    }

    public Boolean getActivoAfgra() {
        return activoAfgra;
    }

    public void setActivoAfgra(Boolean activoAfgra) {
        this.activoAfgra = activoAfgra;
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

    public List<AfiNombreActivo> getAfiNombreActivoList() {
        return afiNombreActivoList;
    }

    public void setAfiNombreActivoList(List<AfiNombreActivo> afiNombreActivoList) {
        this.afiNombreActivoList = afiNombreActivoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAfgra != null ? ideAfgra.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AfiGrupoActivo)) {
            return false;
        }
        AfiGrupoActivo other = (AfiGrupoActivo) object;
        if ((this.ideAfgra == null && other.ideAfgra != null) || (this.ideAfgra != null && !this.ideAfgra.equals(other.ideAfgra))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.AfiGrupoActivo[ ideAfgra=" + ideAfgra + " ]";
    }
    
}
