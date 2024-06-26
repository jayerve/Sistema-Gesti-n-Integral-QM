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
@Table(name = "gen_tipo_asiento", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GenTipoAsiento.findAll", query = "SELECT g FROM GenTipoAsiento g"),
    @NamedQuery(name = "GenTipoAsiento.findByIdeGetia", query = "SELECT g FROM GenTipoAsiento g WHERE g.ideGetia = :ideGetia"),
    @NamedQuery(name = "GenTipoAsiento.findByDetalleGetia", query = "SELECT g FROM GenTipoAsiento g WHERE g.detalleGetia = :detalleGetia"),
    @NamedQuery(name = "GenTipoAsiento.findByActivoGetia", query = "SELECT g FROM GenTipoAsiento g WHERE g.activoGetia = :activoGetia"),
    @NamedQuery(name = "GenTipoAsiento.findByUsuarioIngre", query = "SELECT g FROM GenTipoAsiento g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenTipoAsiento.findByFechaIngre", query = "SELECT g FROM GenTipoAsiento g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenTipoAsiento.findByUsuarioActua", query = "SELECT g FROM GenTipoAsiento g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenTipoAsiento.findByFechaActua", query = "SELECT g FROM GenTipoAsiento g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenTipoAsiento.findByHoraIngre", query = "SELECT g FROM GenTipoAsiento g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenTipoAsiento.findByHoraActua", query = "SELECT g FROM GenTipoAsiento g WHERE g.horaActua = :horaActua")})
public class GenTipoAsiento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_getia", nullable = false)
    private Integer ideGetia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "detalle_getia", nullable = false, length = 50)
    private String detalleGetia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_getia", nullable = false)
    private boolean activoGetia;
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
    @OneToMany(mappedBy = "ideGetia")
    private List<CntMovimientoContable> cntMovimientoContableList;
    @OneToMany(mappedBy = "ideGetia")
    private List<NrhRubroAsiento> nrhRubroAsientoList;
    @JoinColumn(name = "ide_nrtin", referencedColumnName = "ide_nrtin")
    @ManyToOne
    private NrhTipoNomina ideNrtin;

    public GenTipoAsiento() {
    }

    public GenTipoAsiento(Integer ideGetia) {
        this.ideGetia = ideGetia;
    }

    public GenTipoAsiento(Integer ideGetia, String detalleGetia, boolean activoGetia) {
        this.ideGetia = ideGetia;
        this.detalleGetia = detalleGetia;
        this.activoGetia = activoGetia;
    }

    public Integer getIdeGetia() {
        return ideGetia;
    }

    public void setIdeGetia(Integer ideGetia) {
        this.ideGetia = ideGetia;
    }

    public String getDetalleGetia() {
        return detalleGetia;
    }

    public void setDetalleGetia(String detalleGetia) {
        this.detalleGetia = detalleGetia;
    }

    public boolean getActivoGetia() {
        return activoGetia;
    }

    public void setActivoGetia(boolean activoGetia) {
        this.activoGetia = activoGetia;
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

    public List<CntMovimientoContable> getCntMovimientoContableList() {
        return cntMovimientoContableList;
    }

    public void setCntMovimientoContableList(List<CntMovimientoContable> cntMovimientoContableList) {
        this.cntMovimientoContableList = cntMovimientoContableList;
    }

    public List<NrhRubroAsiento> getNrhRubroAsientoList() {
        return nrhRubroAsientoList;
    }

    public void setNrhRubroAsientoList(List<NrhRubroAsiento> nrhRubroAsientoList) {
        this.nrhRubroAsientoList = nrhRubroAsientoList;
    }

    public NrhTipoNomina getIdeNrtin() {
        return ideNrtin;
    }

    public void setIdeNrtin(NrhTipoNomina ideNrtin) {
        this.ideNrtin = ideNrtin;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGetia != null ? ideGetia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenTipoAsiento)) {
            return false;
        }
        GenTipoAsiento other = (GenTipoAsiento) object;
        if ((this.ideGetia == null && other.ideGetia != null) || (this.ideGetia != null && !this.ideGetia.equals(other.ideGetia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenTipoAsiento[ ideGetia=" + ideGetia + " ]";
    }
    
}
