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
@Table(name = "bodt_concepto_egreso", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "BodtConceptoEgreso.findAll", query = "SELECT b FROM BodtConceptoEgreso b"),
    @NamedQuery(name = "BodtConceptoEgreso.findByIdeBocoe", query = "SELECT b FROM BodtConceptoEgreso b WHERE b.ideBocoe = :ideBocoe"),
    @NamedQuery(name = "BodtConceptoEgreso.findByIdeBobod", query = "SELECT b FROM BodtConceptoEgreso b WHERE b.ideBobod = :ideBobod"),
    @NamedQuery(name = "BodtConceptoEgreso.findByParticularBocoe", query = "SELECT b FROM BodtConceptoEgreso b WHERE b.particularBocoe = :particularBocoe"),
    @NamedQuery(name = "BodtConceptoEgreso.findByUsoBocoe", query = "SELECT b FROM BodtConceptoEgreso b WHERE b.usoBocoe = :usoBocoe"),
    @NamedQuery(name = "BodtConceptoEgreso.findByNumeroEgresoBocoe", query = "SELECT b FROM BodtConceptoEgreso b WHERE b.numeroEgresoBocoe = :numeroEgresoBocoe"),
    @NamedQuery(name = "BodtConceptoEgreso.findByUbicacionBocoe", query = "SELECT b FROM BodtConceptoEgreso b WHERE b.ubicacionBocoe = :ubicacionBocoe"),
    @NamedQuery(name = "BodtConceptoEgreso.findByActivoBocoe", query = "SELECT b FROM BodtConceptoEgreso b WHERE b.activoBocoe = :activoBocoe"),
    @NamedQuery(name = "BodtConceptoEgreso.findByUsuarioIngre", query = "SELECT b FROM BodtConceptoEgreso b WHERE b.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "BodtConceptoEgreso.findByFechaIngre", query = "SELECT b FROM BodtConceptoEgreso b WHERE b.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "BodtConceptoEgreso.findByHoraIngre", query = "SELECT b FROM BodtConceptoEgreso b WHERE b.horaIngre = :horaIngre"),
    @NamedQuery(name = "BodtConceptoEgreso.findByUsuarioActua", query = "SELECT b FROM BodtConceptoEgreso b WHERE b.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "BodtConceptoEgreso.findByFechaActua", query = "SELECT b FROM BodtConceptoEgreso b WHERE b.fechaActua = :fechaActua"),
    @NamedQuery(name = "BodtConceptoEgreso.findByHoraActua", query = "SELECT b FROM BodtConceptoEgreso b WHERE b.horaActua = :horaActua")})
public class BodtConceptoEgreso implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_bocoe", nullable = false)
    private Long ideBocoe;
    @Column(name = "ide_bobod")
    private BigInteger ideBobod;
    @Size(max = 250)
    @Column(name = "particular_bocoe", length = 250)
    private String particularBocoe;
    @Size(max = 2147483647)
    @Column(name = "uso_bocoe", length = 2147483647)
    private String usoBocoe;
    @Column(name = "numero_egreso_bocoe")
    private BigInteger numeroEgresoBocoe;
    @Size(max = 100)
    @Column(name = "ubicacion_bocoe", length = 100)
    private String ubicacionBocoe;
    @Column(name = "activo_bocoe")
    private Boolean activoBocoe;
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
    @OneToMany(mappedBy = "ideBocoe")
    private List<BodtEgreso> bodtEgresoList;
    @JoinColumn(name = "ide_getip", referencedColumnName = "ide_getip")
    @ManyToOne
    private GenTipoPersona ideGetip;
    @JoinColumn(name = "gen_ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar genIdeGeedp;
    @JoinColumn(name = "gen_ide_geedp2", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar genIdeGeedp2;
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar ideGeedp;
    @JoinColumn(name = "ide_geare", referencedColumnName = "ide_geare")
    @ManyToOne
    private GenArea ideGeare;
    @JoinColumn(name = "ide_geani", referencedColumnName = "ide_geani")
    @ManyToOne
    private GenAnio ideGeani;
    @JoinColumn(name = "ide_bodes", referencedColumnName = "ide_bodes")
    @ManyToOne
    private BodtDestino ideBodes;

    public BodtConceptoEgreso() {
    }

    public BodtConceptoEgreso(Long ideBocoe) {
        this.ideBocoe = ideBocoe;
    }

    public Long getIdeBocoe() {
        return ideBocoe;
    }

    public void setIdeBocoe(Long ideBocoe) {
        this.ideBocoe = ideBocoe;
    }

    public BigInteger getIdeBobod() {
        return ideBobod;
    }

    public void setIdeBobod(BigInteger ideBobod) {
        this.ideBobod = ideBobod;
    }

    public String getParticularBocoe() {
        return particularBocoe;
    }

    public void setParticularBocoe(String particularBocoe) {
        this.particularBocoe = particularBocoe;
    }

    public String getUsoBocoe() {
        return usoBocoe;
    }

    public void setUsoBocoe(String usoBocoe) {
        this.usoBocoe = usoBocoe;
    }

    public BigInteger getNumeroEgresoBocoe() {
        return numeroEgresoBocoe;
    }

    public void setNumeroEgresoBocoe(BigInteger numeroEgresoBocoe) {
        this.numeroEgresoBocoe = numeroEgresoBocoe;
    }

    public String getUbicacionBocoe() {
        return ubicacionBocoe;
    }

    public void setUbicacionBocoe(String ubicacionBocoe) {
        this.ubicacionBocoe = ubicacionBocoe;
    }

    public Boolean getActivoBocoe() {
        return activoBocoe;
    }

    public void setActivoBocoe(Boolean activoBocoe) {
        this.activoBocoe = activoBocoe;
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

    public List<BodtEgreso> getBodtEgresoList() {
        return bodtEgresoList;
    }

    public void setBodtEgresoList(List<BodtEgreso> bodtEgresoList) {
        this.bodtEgresoList = bodtEgresoList;
    }

    public GenTipoPersona getIdeGetip() {
        return ideGetip;
    }

    public void setIdeGetip(GenTipoPersona ideGetip) {
        this.ideGetip = ideGetip;
    }

    public GenEmpleadosDepartamentoPar getGenIdeGeedp() {
        return genIdeGeedp;
    }

    public void setGenIdeGeedp(GenEmpleadosDepartamentoPar genIdeGeedp) {
        this.genIdeGeedp = genIdeGeedp;
    }

    public GenEmpleadosDepartamentoPar getGenIdeGeedp2() {
        return genIdeGeedp2;
    }

    public void setGenIdeGeedp2(GenEmpleadosDepartamentoPar genIdeGeedp2) {
        this.genIdeGeedp2 = genIdeGeedp2;
    }

    public GenEmpleadosDepartamentoPar getIdeGeedp() {
        return ideGeedp;
    }

    public void setIdeGeedp(GenEmpleadosDepartamentoPar ideGeedp) {
        this.ideGeedp = ideGeedp;
    }

    public GenArea getIdeGeare() {
        return ideGeare;
    }

    public void setIdeGeare(GenArea ideGeare) {
        this.ideGeare = ideGeare;
    }

    public GenAnio getIdeGeani() {
        return ideGeani;
    }

    public void setIdeGeani(GenAnio ideGeani) {
        this.ideGeani = ideGeani;
    }

    public BodtDestino getIdeBodes() {
        return ideBodes;
    }

    public void setIdeBodes(BodtDestino ideBodes) {
        this.ideBodes = ideBodes;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideBocoe != null ? ideBocoe.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BodtConceptoEgreso)) {
            return false;
        }
        BodtConceptoEgreso other = (BodtConceptoEgreso) object;
        if ((this.ideBocoe == null && other.ideBocoe != null) || (this.ideBocoe != null && !this.ideBocoe.equals(other.ideBocoe))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.BodtConceptoEgreso[ ideBocoe=" + ideBocoe + " ]";
    }
    
}
