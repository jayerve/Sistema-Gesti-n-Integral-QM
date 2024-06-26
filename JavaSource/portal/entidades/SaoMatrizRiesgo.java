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
@Table(name = "sao_matriz_riesgo", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SaoMatrizRiesgo.findAll", query = "SELECT s FROM SaoMatrizRiesgo s"),
    @NamedQuery(name = "SaoMatrizRiesgo.findByIdeSamar", query = "SELECT s FROM SaoMatrizRiesgo s WHERE s.ideSamar = :ideSamar"),
    @NamedQuery(name = "SaoMatrizRiesgo.findByFechaEvalSamar", query = "SELECT s FROM SaoMatrizRiesgo s WHERE s.fechaEvalSamar = :fechaEvalSamar"),
    @NamedQuery(name = "SaoMatrizRiesgo.findByResponsableSamar", query = "SELECT s FROM SaoMatrizRiesgo s WHERE s.responsableSamar = :responsableSamar"),
    @NamedQuery(name = "SaoMatrizRiesgo.findByActivoSamar", query = "SELECT s FROM SaoMatrizRiesgo s WHERE s.activoSamar = :activoSamar"),
    @NamedQuery(name = "SaoMatrizRiesgo.findByUsuarioIngre", query = "SELECT s FROM SaoMatrizRiesgo s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SaoMatrizRiesgo.findByFechaIngre", query = "SELECT s FROM SaoMatrizRiesgo s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SaoMatrizRiesgo.findByHoraIngre", query = "SELECT s FROM SaoMatrizRiesgo s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SaoMatrizRiesgo.findByUsuarioActua", query = "SELECT s FROM SaoMatrizRiesgo s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SaoMatrizRiesgo.findByFechaActua", query = "SELECT s FROM SaoMatrizRiesgo s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SaoMatrizRiesgo.findByHoraActua", query = "SELECT s FROM SaoMatrizRiesgo s WHERE s.horaActua = :horaActua")})
public class SaoMatrizRiesgo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_samar", nullable = false)
    private Integer ideSamar;
    @Column(name = "fecha_eval_samar")
    @Temporal(TemporalType.DATE)
    private Date fechaEvalSamar;
    @Size(max = 100)
    @Column(name = "responsable_samar", length = 100)
    private String responsableSamar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_samar", nullable = false)
    private boolean activoSamar;
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
    @OneToMany(mappedBy = "ideSamar")
    private List<SaoDetalleMatrizRiesgo> saoDetalleMatrizRiesgoList;
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar ideGeedp;
    @JoinColumn(name = "gen_ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar genIdeGeedp;
    @JoinColumns({
        @JoinColumn(name = "ide_sucu", referencedColumnName = "ide_sucu"),
        @JoinColumn(name = "ide_gedep", referencedColumnName = "ide_gedep"),
        @JoinColumn(name = "ide_geare", referencedColumnName = "ide_geare")})
    @ManyToOne
    private GenDepartamentoSucursal genDepartamentoSucursal;
    @JoinColumn(name = "ide_geben", referencedColumnName = "ide_geben")
    @ManyToOne
    private GenBeneficiario ideGeben;

    public SaoMatrizRiesgo() {
    }

    public SaoMatrizRiesgo(Integer ideSamar) {
        this.ideSamar = ideSamar;
    }

    public SaoMatrizRiesgo(Integer ideSamar, boolean activoSamar) {
        this.ideSamar = ideSamar;
        this.activoSamar = activoSamar;
    }

    public Integer getIdeSamar() {
        return ideSamar;
    }

    public void setIdeSamar(Integer ideSamar) {
        this.ideSamar = ideSamar;
    }

    public Date getFechaEvalSamar() {
        return fechaEvalSamar;
    }

    public void setFechaEvalSamar(Date fechaEvalSamar) {
        this.fechaEvalSamar = fechaEvalSamar;
    }

    public String getResponsableSamar() {
        return responsableSamar;
    }

    public void setResponsableSamar(String responsableSamar) {
        this.responsableSamar = responsableSamar;
    }

    public boolean getActivoSamar() {
        return activoSamar;
    }

    public void setActivoSamar(boolean activoSamar) {
        this.activoSamar = activoSamar;
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

    public List<SaoDetalleMatrizRiesgo> getSaoDetalleMatrizRiesgoList() {
        return saoDetalleMatrizRiesgoList;
    }

    public void setSaoDetalleMatrizRiesgoList(List<SaoDetalleMatrizRiesgo> saoDetalleMatrizRiesgoList) {
        this.saoDetalleMatrizRiesgoList = saoDetalleMatrizRiesgoList;
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

    public GenDepartamentoSucursal getGenDepartamentoSucursal() {
        return genDepartamentoSucursal;
    }

    public void setGenDepartamentoSucursal(GenDepartamentoSucursal genDepartamentoSucursal) {
        this.genDepartamentoSucursal = genDepartamentoSucursal;
    }

    public GenBeneficiario getIdeGeben() {
        return ideGeben;
    }

    public void setIdeGeben(GenBeneficiario ideGeben) {
        this.ideGeben = ideGeben;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSamar != null ? ideSamar.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaoMatrizRiesgo)) {
            return false;
        }
        SaoMatrizRiesgo other = (SaoMatrizRiesgo) object;
        if ((this.ideSamar == null && other.ideSamar != null) || (this.ideSamar != null && !this.ideSamar.equals(other.ideSamar))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SaoMatrizRiesgo[ ideSamar=" + ideSamar + " ]";
    }
    
}
