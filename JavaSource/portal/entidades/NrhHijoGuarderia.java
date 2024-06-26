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
@Table(name = "nrh_hijo_guarderia", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "NrhHijoGuarderia.findAll", query = "SELECT n FROM NrhHijoGuarderia n"),
    @NamedQuery(name = "NrhHijoGuarderia.findByIdeNrhig", query = "SELECT n FROM NrhHijoGuarderia n WHERE n.ideNrhig = :ideNrhig"),
    @NamedQuery(name = "NrhHijoGuarderia.findByFechaBeneficioNrhig", query = "SELECT n FROM NrhHijoGuarderia n WHERE n.fechaBeneficioNrhig = :fechaBeneficioNrhig"),
    @NamedQuery(name = "NrhHijoGuarderia.findByActivoNrhig", query = "SELECT n FROM NrhHijoGuarderia n WHERE n.activoNrhig = :activoNrhig"),
    @NamedQuery(name = "NrhHijoGuarderia.findByUsuarioIngre", query = "SELECT n FROM NrhHijoGuarderia n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhHijoGuarderia.findByFechaIngre", query = "SELECT n FROM NrhHijoGuarderia n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhHijoGuarderia.findByUsuarioActua", query = "SELECT n FROM NrhHijoGuarderia n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhHijoGuarderia.findByFechaActua", query = "SELECT n FROM NrhHijoGuarderia n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhHijoGuarderia.findByHoraIngre", query = "SELECT n FROM NrhHijoGuarderia n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhHijoGuarderia.findByHoraActua", query = "SELECT n FROM NrhHijoGuarderia n WHERE n.horaActua = :horaActua")})
public class NrhHijoGuarderia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_nrhig", nullable = false)
    private Integer ideNrhig;
    @Column(name = "fecha_beneficio_nrhig")
    @Temporal(TemporalType.DATE)
    private Date fechaBeneficioNrhig;
    @Column(name = "activo_nrhig")
    private Boolean activoNrhig;
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
    @JoinColumn(name = "ide_nrbee", referencedColumnName = "ide_nrbee")
    @ManyToOne
    private NrhBeneficioEmpleado ideNrbee;
    @JoinColumn(name = "ide_gtcaf", referencedColumnName = "ide_gtcaf")
    @ManyToOne
    private GthCargasFamiliares ideGtcaf;
    @OneToMany(mappedBy = "ideNrhig")
    private List<NrhDetalleFacturaGuarderia> nrhDetalleFacturaGuarderiaList;

    public NrhHijoGuarderia() {
    }

    public NrhHijoGuarderia(Integer ideNrhig) {
        this.ideNrhig = ideNrhig;
    }

    public Integer getIdeNrhig() {
        return ideNrhig;
    }

    public void setIdeNrhig(Integer ideNrhig) {
        this.ideNrhig = ideNrhig;
    }

    public Date getFechaBeneficioNrhig() {
        return fechaBeneficioNrhig;
    }

    public void setFechaBeneficioNrhig(Date fechaBeneficioNrhig) {
        this.fechaBeneficioNrhig = fechaBeneficioNrhig;
    }

    public Boolean getActivoNrhig() {
        return activoNrhig;
    }

    public void setActivoNrhig(Boolean activoNrhig) {
        this.activoNrhig = activoNrhig;
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

    public NrhBeneficioEmpleado getIdeNrbee() {
        return ideNrbee;
    }

    public void setIdeNrbee(NrhBeneficioEmpleado ideNrbee) {
        this.ideNrbee = ideNrbee;
    }

    public GthCargasFamiliares getIdeGtcaf() {
        return ideGtcaf;
    }

    public void setIdeGtcaf(GthCargasFamiliares ideGtcaf) {
        this.ideGtcaf = ideGtcaf;
    }

    public List<NrhDetalleFacturaGuarderia> getNrhDetalleFacturaGuarderiaList() {
        return nrhDetalleFacturaGuarderiaList;
    }

    public void setNrhDetalleFacturaGuarderiaList(List<NrhDetalleFacturaGuarderia> nrhDetalleFacturaGuarderiaList) {
        this.nrhDetalleFacturaGuarderiaList = nrhDetalleFacturaGuarderiaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNrhig != null ? ideNrhig.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhHijoGuarderia)) {
            return false;
        }
        NrhHijoGuarderia other = (NrhHijoGuarderia) object;
        if ((this.ideNrhig == null && other.ideNrhig != null) || (this.ideNrhig != null && !this.ideNrhig.equals(other.ideNrhig))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhHijoGuarderia[ ideNrhig=" + ideNrhig + " ]";
    }
    
}
