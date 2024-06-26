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
@Table(name = "gen_lugar_aplica", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GenLugarAplica.findAll", query = "SELECT g FROM GenLugarAplica g"),
    @NamedQuery(name = "GenLugarAplica.findByIdeGelua", query = "SELECT g FROM GenLugarAplica g WHERE g.ideGelua = :ideGelua"),
    @NamedQuery(name = "GenLugarAplica.findByDetalleGelua", query = "SELECT g FROM GenLugarAplica g WHERE g.detalleGelua = :detalleGelua"),
    @NamedQuery(name = "GenLugarAplica.findByActivoGelua", query = "SELECT g FROM GenLugarAplica g WHERE g.activoGelua = :activoGelua"),
    @NamedQuery(name = "GenLugarAplica.findByUsuarioIngre", query = "SELECT g FROM GenLugarAplica g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenLugarAplica.findByFechaIngre", query = "SELECT g FROM GenLugarAplica g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenLugarAplica.findByUsuarioActua", query = "SELECT g FROM GenLugarAplica g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenLugarAplica.findByFechaActua", query = "SELECT g FROM GenLugarAplica g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenLugarAplica.findByHoraIngre", query = "SELECT g FROM GenLugarAplica g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenLugarAplica.findByHoraActua", query = "SELECT g FROM GenLugarAplica g WHERE g.horaActua = :horaActua")})
public class GenLugarAplica implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gelua", nullable = false)
    private Integer ideGelua;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "detalle_gelua", nullable = false, length = 50)
    private String detalleGelua;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_gelua", nullable = false)
    private boolean activoGelua;
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
    @OneToMany(mappedBy = "ideGelua")
    private List<CntDetalleMovimiento> cntDetalleMovimientoList;
    @OneToMany(mappedBy = "ideGelua")
    private List<NrhRubroAsiento> nrhRubroAsientoList;
    @OneToMany(mappedBy = "ideGelua")
    private List<PreAsociacionPresupuestaria> preAsociacionPresupuestariaList;

    public GenLugarAplica() {
    }

    public GenLugarAplica(Integer ideGelua) {
        this.ideGelua = ideGelua;
    }

    public GenLugarAplica(Integer ideGelua, String detalleGelua, boolean activoGelua) {
        this.ideGelua = ideGelua;
        this.detalleGelua = detalleGelua;
        this.activoGelua = activoGelua;
    }

    public Integer getIdeGelua() {
        return ideGelua;
    }

    public void setIdeGelua(Integer ideGelua) {
        this.ideGelua = ideGelua;
    }

    public String getDetalleGelua() {
        return detalleGelua;
    }

    public void setDetalleGelua(String detalleGelua) {
        this.detalleGelua = detalleGelua;
    }

    public boolean getActivoGelua() {
        return activoGelua;
    }

    public void setActivoGelua(boolean activoGelua) {
        this.activoGelua = activoGelua;
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

    public List<CntDetalleMovimiento> getCntDetalleMovimientoList() {
        return cntDetalleMovimientoList;
    }

    public void setCntDetalleMovimientoList(List<CntDetalleMovimiento> cntDetalleMovimientoList) {
        this.cntDetalleMovimientoList = cntDetalleMovimientoList;
    }

    public List<NrhRubroAsiento> getNrhRubroAsientoList() {
        return nrhRubroAsientoList;
    }

    public void setNrhRubroAsientoList(List<NrhRubroAsiento> nrhRubroAsientoList) {
        this.nrhRubroAsientoList = nrhRubroAsientoList;
    }

    public List<PreAsociacionPresupuestaria> getPreAsociacionPresupuestariaList() {
        return preAsociacionPresupuestariaList;
    }

    public void setPreAsociacionPresupuestariaList(List<PreAsociacionPresupuestaria> preAsociacionPresupuestariaList) {
        this.preAsociacionPresupuestariaList = preAsociacionPresupuestariaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGelua != null ? ideGelua.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenLugarAplica)) {
            return false;
        }
        GenLugarAplica other = (GenLugarAplica) object;
        if ((this.ideGelua == null && other.ideGelua != null) || (this.ideGelua != null && !this.ideGelua.equals(other.ideGelua))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenLugarAplica[ ideGelua=" + ideGelua + " ]";
    }
    
}
