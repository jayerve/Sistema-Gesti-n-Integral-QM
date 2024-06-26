/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
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
@Table(name = "gen_perido_rol", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GenPeridoRol.findAll", query = "SELECT g FROM GenPeridoRol g"),
    @NamedQuery(name = "GenPeridoRol.findByIdeGepro", query = "SELECT g FROM GenPeridoRol g WHERE g.ideGepro = :ideGepro"),
    @NamedQuery(name = "GenPeridoRol.findByFechaInicialGepro", query = "SELECT g FROM GenPeridoRol g WHERE g.fechaInicialGepro = :fechaInicialGepro"),
    @NamedQuery(name = "GenPeridoRol.findByFechaFinalGepro", query = "SELECT g FROM GenPeridoRol g WHERE g.fechaFinalGepro = :fechaFinalGepro"),
    @NamedQuery(name = "GenPeridoRol.findByDetallePeriodoGepro", query = "SELECT g FROM GenPeridoRol g WHERE g.detallePeriodoGepro = :detallePeriodoGepro"),
    @NamedQuery(name = "GenPeridoRol.findByUsuarioIngre", query = "SELECT g FROM GenPeridoRol g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenPeridoRol.findByFechaIngre", query = "SELECT g FROM GenPeridoRol g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenPeridoRol.findByUsuarioActua", query = "SELECT g FROM GenPeridoRol g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenPeridoRol.findByFechaActua", query = "SELECT g FROM GenPeridoRol g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenPeridoRol.findByHoraIngre", query = "SELECT g FROM GenPeridoRol g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenPeridoRol.findByHoraActua", query = "SELECT g FROM GenPeridoRol g WHERE g.horaActua = :horaActua"),
    @NamedQuery(name = "GenPeridoRol.findByActivoGepro", query = "SELECT g FROM GenPeridoRol g WHERE g.activoGepro = :activoGepro")})
public class GenPeridoRol implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gepro", nullable = false)
    private Integer ideGepro;
    @Column(name = "fecha_inicial_gepro")
    @Temporal(TemporalType.DATE)
    private Date fechaInicialGepro;
    @Column(name = "fecha_final_gepro")
    @Temporal(TemporalType.DATE)
    private Date fechaFinalGepro;
    @Size(max = 100)
    @Column(name = "detalle_periodo_gepro", length = 100)
    private String detallePeriodoGepro;
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
    @Column(name = "activo_gepro")
    private Boolean activoGepro;
    @JoinColumn(name = "ide_nrtit", referencedColumnName = "ide_nrtit")
    @ManyToOne
    private NrhTipoRol ideNrtit;
    @JoinColumns({
        @JoinColumn(name = "ide_gemes", referencedColumnName = "ide_gemes"),
        @JoinColumn(name = "ide_geani", referencedColumnName = "ide_geani")})
    @ManyToOne
    private GenPeriodo genPeriodo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGepro")
    private List<NrhRol> nrhRolList;

    public GenPeridoRol() {
    }

    public GenPeridoRol(Integer ideGepro) {
        this.ideGepro = ideGepro;
    }

    public Integer getIdeGepro() {
        return ideGepro;
    }

    public void setIdeGepro(Integer ideGepro) {
        this.ideGepro = ideGepro;
    }

    public Date getFechaInicialGepro() {
        return fechaInicialGepro;
    }

    public void setFechaInicialGepro(Date fechaInicialGepro) {
        this.fechaInicialGepro = fechaInicialGepro;
    }

    public Date getFechaFinalGepro() {
        return fechaFinalGepro;
    }

    public void setFechaFinalGepro(Date fechaFinalGepro) {
        this.fechaFinalGepro = fechaFinalGepro;
    }

    public String getDetallePeriodoGepro() {
        return detallePeriodoGepro;
    }

    public void setDetallePeriodoGepro(String detallePeriodoGepro) {
        this.detallePeriodoGepro = detallePeriodoGepro;
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

    public Boolean getActivoGepro() {
        return activoGepro;
    }

    public void setActivoGepro(Boolean activoGepro) {
        this.activoGepro = activoGepro;
    }

    public NrhTipoRol getIdeNrtit() {
        return ideNrtit;
    }

    public void setIdeNrtit(NrhTipoRol ideNrtit) {
        this.ideNrtit = ideNrtit;
    }

    public GenPeriodo getGenPeriodo() {
        return genPeriodo;
    }

    public void setGenPeriodo(GenPeriodo genPeriodo) {
        this.genPeriodo = genPeriodo;
    }

    public List<NrhRol> getNrhRolList() {
        return nrhRolList;
    }

    public void setNrhRolList(List<NrhRol> nrhRolList) {
        this.nrhRolList = nrhRolList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGepro != null ? ideGepro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenPeridoRol)) {
            return false;
        }
        GenPeridoRol other = (GenPeridoRol) object;
        if ((this.ideGepro == null && other.ideGepro != null) || (this.ideGepro != null && !this.ideGepro.equals(other.ideGepro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenPeridoRol[ ideGepro=" + ideGepro + " ]";
    }
    
}
