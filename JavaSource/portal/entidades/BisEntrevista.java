/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
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
@Table(name = "bis_entrevista", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "BisEntrevista.findAll", query = "SELECT b FROM BisEntrevista b"),
    @NamedQuery(name = "BisEntrevista.findByIdeBient", query = "SELECT b FROM BisEntrevista b WHERE b.ideBient = :ideBient"),
    @NamedQuery(name = "BisEntrevista.findByFechaEntrevistaBient", query = "SELECT b FROM BisEntrevista b WHERE b.fechaEntrevistaBient = :fechaEntrevistaBient"),
    @NamedQuery(name = "BisEntrevista.findByObservacionesBient", query = "SELECT b FROM BisEntrevista b WHERE b.observacionesBient = :observacionesBient"),
    @NamedQuery(name = "BisEntrevista.findByActivoBient", query = "SELECT b FROM BisEntrevista b WHERE b.activoBient = :activoBient"),
    @NamedQuery(name = "BisEntrevista.findByUsuarioIngre", query = "SELECT b FROM BisEntrevista b WHERE b.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "BisEntrevista.findByFechaIngre", query = "SELECT b FROM BisEntrevista b WHERE b.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "BisEntrevista.findByHoraIngre", query = "SELECT b FROM BisEntrevista b WHERE b.horaIngre = :horaIngre"),
    @NamedQuery(name = "BisEntrevista.findByUsuarioActua", query = "SELECT b FROM BisEntrevista b WHERE b.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "BisEntrevista.findByFechaActua", query = "SELECT b FROM BisEntrevista b WHERE b.fechaActua = :fechaActua"),
    @NamedQuery(name = "BisEntrevista.findByHoraActua", query = "SELECT b FROM BisEntrevista b WHERE b.horaActua = :horaActua")})
public class BisEntrevista implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_bient", nullable = false)
    private Integer ideBient;
    @Column(name = "fecha_entrevista_bient")
    @Temporal(TemporalType.DATE)
    private Date fechaEntrevistaBient;
    @Size(max = 1000)
    @Column(name = "observaciones_bient", length = 1000)
    private String observacionesBient;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_bient", nullable = false)
    private boolean activoBient;
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
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar ideGeedp;
    @JoinColumn(name = "gen_ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar genIdeGeedp;
    @JoinColumn(name = "ide_biple", referencedColumnName = "ide_biple")
    @ManyToOne
    private BisPlanEntrevista ideBiple;
    @JoinColumn(name = "ide_bimoe", referencedColumnName = "ide_bimoe")
    @ManyToOne
    private BisMotivoEntrevista ideBimoe;

    public BisEntrevista() {
    }

    public BisEntrevista(Integer ideBient) {
        this.ideBient = ideBient;
    }

    public BisEntrevista(Integer ideBient, boolean activoBient) {
        this.ideBient = ideBient;
        this.activoBient = activoBient;
    }

    public Integer getIdeBient() {
        return ideBient;
    }

    public void setIdeBient(Integer ideBient) {
        this.ideBient = ideBient;
    }

    public Date getFechaEntrevistaBient() {
        return fechaEntrevistaBient;
    }

    public void setFechaEntrevistaBient(Date fechaEntrevistaBient) {
        this.fechaEntrevistaBient = fechaEntrevistaBient;
    }

    public String getObservacionesBient() {
        return observacionesBient;
    }

    public void setObservacionesBient(String observacionesBient) {
        this.observacionesBient = observacionesBient;
    }

    public boolean getActivoBient() {
        return activoBient;
    }

    public void setActivoBient(boolean activoBient) {
        this.activoBient = activoBient;
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

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
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

    public BisPlanEntrevista getIdeBiple() {
        return ideBiple;
    }

    public void setIdeBiple(BisPlanEntrevista ideBiple) {
        this.ideBiple = ideBiple;
    }

    public BisMotivoEntrevista getIdeBimoe() {
        return ideBimoe;
    }

    public void setIdeBimoe(BisMotivoEntrevista ideBimoe) {
        this.ideBimoe = ideBimoe;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideBient != null ? ideBient.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BisEntrevista)) {
            return false;
        }
        BisEntrevista other = (BisEntrevista) object;
        if ((this.ideBient == null && other.ideBient != null) || (this.ideBient != null && !this.ideBient.equals(other.ideBient))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.BisEntrevista[ ideBient=" + ideBient + " ]";
    }
    
}
