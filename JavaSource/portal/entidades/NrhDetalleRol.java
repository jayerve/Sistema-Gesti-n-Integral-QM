/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "nrh_detalle_rol", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "NrhDetalleRol.findAll", query = "SELECT n FROM NrhDetalleRol n"),
    @NamedQuery(name = "NrhDetalleRol.findByIdeNrdro", query = "SELECT n FROM NrhDetalleRol n WHERE n.ideNrdro = :ideNrdro"),
    @NamedQuery(name = "NrhDetalleRol.findByValorNrdro", query = "SELECT n FROM NrhDetalleRol n WHERE n.valorNrdro = :valorNrdro"),
    @NamedQuery(name = "NrhDetalleRol.findByOrdenCalculoNrdro", query = "SELECT n FROM NrhDetalleRol n WHERE n.ordenCalculoNrdro = :ordenCalculoNrdro"),
    @NamedQuery(name = "NrhDetalleRol.findByUsuarioIngre", query = "SELECT n FROM NrhDetalleRol n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhDetalleRol.findByFechaIngre", query = "SELECT n FROM NrhDetalleRol n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhDetalleRol.findByUsuarioActua", query = "SELECT n FROM NrhDetalleRol n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhDetalleRol.findByFechaActua", query = "SELECT n FROM NrhDetalleRol n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhDetalleRol.findByHoraIngre", query = "SELECT n FROM NrhDetalleRol n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhDetalleRol.findByHoraActua", query = "SELECT n FROM NrhDetalleRol n WHERE n.horaActua = :horaActua")})
public class NrhDetalleRol implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_nrdro", nullable = false)
    private Integer ideNrdro;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "valor_nrdro", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorNrdro;
    @Column(name = "orden_calculo_nrdro")
    private Integer ordenCalculoNrdro;
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
    @JoinColumn(name = "ide_nrrol", referencedColumnName = "ide_nrrol", nullable = false)
    @ManyToOne(optional = false)
    private NrhRol ideNrrol;
    @JoinColumn(name = "ide_nrder", referencedColumnName = "ide_nrder", nullable = false)
    @ManyToOne(optional = false)
    private NrhDetalleRubro ideNrder;
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp", nullable = false)
    @ManyToOne(optional = false)
    private GenEmpleadosDepartamentoPar ideGeedp;

    public NrhDetalleRol() {
    }

    public NrhDetalleRol(Integer ideNrdro) {
        this.ideNrdro = ideNrdro;
    }

    public NrhDetalleRol(Integer ideNrdro, BigDecimal valorNrdro) {
        this.ideNrdro = ideNrdro;
        this.valorNrdro = valorNrdro;
    }

    public Integer getIdeNrdro() {
        return ideNrdro;
    }

    public void setIdeNrdro(Integer ideNrdro) {
        this.ideNrdro = ideNrdro;
    }

    public BigDecimal getValorNrdro() {
        return valorNrdro;
    }

    public void setValorNrdro(BigDecimal valorNrdro) {
        this.valorNrdro = valorNrdro;
    }

    public Integer getOrdenCalculoNrdro() {
        return ordenCalculoNrdro;
    }

    public void setOrdenCalculoNrdro(Integer ordenCalculoNrdro) {
        this.ordenCalculoNrdro = ordenCalculoNrdro;
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

    public NrhRol getIdeNrrol() {
        return ideNrrol;
    }

    public void setIdeNrrol(NrhRol ideNrrol) {
        this.ideNrrol = ideNrrol;
    }

    public NrhDetalleRubro getIdeNrder() {
        return ideNrder;
    }

    public void setIdeNrder(NrhDetalleRubro ideNrder) {
        this.ideNrder = ideNrder;
    }

    public GenEmpleadosDepartamentoPar getIdeGeedp() {
        return ideGeedp;
    }

    public void setIdeGeedp(GenEmpleadosDepartamentoPar ideGeedp) {
        this.ideGeedp = ideGeedp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNrdro != null ? ideNrdro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhDetalleRol)) {
            return false;
        }
        NrhDetalleRol other = (NrhDetalleRol) object;
        if ((this.ideNrdro == null && other.ideNrdro != null) || (this.ideNrdro != null && !this.ideNrdro.equals(other.ideNrdro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhDetalleRol[ ideNrdro=" + ideNrdro + " ]";
    }
    
}
