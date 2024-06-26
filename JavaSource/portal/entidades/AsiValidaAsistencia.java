/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "asi_valida_asistencia", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "AsiValidaAsistencia.findAll", query = "SELECT a FROM AsiValidaAsistencia a"),
    @NamedQuery(name = "AsiValidaAsistencia.findByIdeAsvaa", query = "SELECT a FROM AsiValidaAsistencia a WHERE a.ideAsvaa = :ideAsvaa"),
    @NamedQuery(name = "AsiValidaAsistencia.findByFechaMarcacionAsvaa", query = "SELECT a FROM AsiValidaAsistencia a WHERE a.fechaMarcacionAsvaa = :fechaMarcacionAsvaa"),
    @NamedQuery(name = "AsiValidaAsistencia.findByDiferenciaSalidaAsvaa", query = "SELECT a FROM AsiValidaAsistencia a WHERE a.diferenciaSalidaAsvaa = :diferenciaSalidaAsvaa"),
    @NamedQuery(name = "AsiValidaAsistencia.findByDiferenciaAsvaa", query = "SELECT a FROM AsiValidaAsistencia a WHERE a.diferenciaAsvaa = :diferenciaAsvaa"),
    @NamedQuery(name = "AsiValidaAsistencia.findByEventoAsvaa", query = "SELECT a FROM AsiValidaAsistencia a WHERE a.eventoAsvaa = :eventoAsvaa"),
    @NamedQuery(name = "AsiValidaAsistencia.findByImportoAsvaa", query = "SELECT a FROM AsiValidaAsistencia a WHERE a.importoAsvaa = :importoAsvaa"),
    @NamedQuery(name = "AsiValidaAsistencia.findByActivoAsvaa", query = "SELECT a FROM AsiValidaAsistencia a WHERE a.activoAsvaa = :activoAsvaa"),
    @NamedQuery(name = "AsiValidaAsistencia.findByUsuarioIngre", query = "SELECT a FROM AsiValidaAsistencia a WHERE a.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "AsiValidaAsistencia.findByFechaIngre", query = "SELECT a FROM AsiValidaAsistencia a WHERE a.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "AsiValidaAsistencia.findByUsuarioActua", query = "SELECT a FROM AsiValidaAsistencia a WHERE a.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "AsiValidaAsistencia.findByFechaActua", query = "SELECT a FROM AsiValidaAsistencia a WHERE a.fechaActua = :fechaActua"),
    @NamedQuery(name = "AsiValidaAsistencia.findByHoraIngre", query = "SELECT a FROM AsiValidaAsistencia a WHERE a.horaIngre = :horaIngre"),
    @NamedQuery(name = "AsiValidaAsistencia.findByHoraActua", query = "SELECT a FROM AsiValidaAsistencia a WHERE a.horaActua = :horaActua"),
    @NamedQuery(name = "AsiValidaAsistencia.findByHoraMarcacionAsvaa", query = "SELECT a FROM AsiValidaAsistencia a WHERE a.horaMarcacionAsvaa = :horaMarcacionAsvaa"),
    @NamedQuery(name = "AsiValidaAsistencia.findByHoraMarcaSalidaAsvaa", query = "SELECT a FROM AsiValidaAsistencia a WHERE a.horaMarcaSalidaAsvaa = :horaMarcaSalidaAsvaa"),
    @NamedQuery(name = "AsiValidaAsistencia.findByNovedadAsvaa", query = "SELECT a FROM AsiValidaAsistencia a WHERE a.novedadAsvaa = :novedadAsvaa"),
    @NamedQuery(name = "AsiValidaAsistencia.findByVerificadoAsvaa", query = "SELECT a FROM AsiValidaAsistencia a WHERE a.verificadoAsvaa = :verificadoAsvaa")})
public class AsiValidaAsistencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_asvaa", nullable = false)
    private Integer ideAsvaa;
    @Column(name = "fecha_marcacion_asvaa")
    @Temporal(TemporalType.DATE)
    private Date fechaMarcacionAsvaa;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "diferencia_salida_asvaa", precision = 12, scale = 2)
    private BigDecimal diferenciaSalidaAsvaa;
    @Column(name = "diferencia_asvaa", precision = 12, scale = 2)
    private BigDecimal diferenciaAsvaa;
    @Size(max = 100)
    @Column(name = "evento_asvaa", length = 100)
    private String eventoAsvaa;
    @Column(name = "importo_asvaa")
    private Integer importoAsvaa;
    @Column(name = "activo_asvaa")
    private Boolean activoAsvaa;
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
    @Column(name = "hora_marcacion_asvaa")
    @Temporal(TemporalType.TIME)
    private Date horaMarcacionAsvaa;
    @Column(name = "hora_marca_salida_asvaa")
    @Temporal(TemporalType.TIME)
    private Date horaMarcaSalidaAsvaa;
    @Column(name = "novedad_asvaa")
    private Boolean novedadAsvaa;
    @Column(name = "verificado_asvaa")
    private Boolean verificadoAsvaa;
    @OneToMany(mappedBy = "ideAsvaa")
    private List<AsiValidaAsistenciaJustifi> asiValidaAsistenciaJustifiList;
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar ideGeedp;
    @JoinColumn(name = "ide_asnov", referencedColumnName = "ide_asnov")
    @ManyToOne
    private AsiNovedad ideAsnov;
    @JoinColumn(name = "ide_asdhe", referencedColumnName = "ide_asdhe")
    @ManyToOne
    private AsiDetalleHorasExtras ideAsdhe;

    public AsiValidaAsistencia() {
    }

    public AsiValidaAsistencia(Integer ideAsvaa) {
        this.ideAsvaa = ideAsvaa;
    }

    public Integer getIdeAsvaa() {
        return ideAsvaa;
    }

    public void setIdeAsvaa(Integer ideAsvaa) {
        this.ideAsvaa = ideAsvaa;
    }

    public Date getFechaMarcacionAsvaa() {
        return fechaMarcacionAsvaa;
    }

    public void setFechaMarcacionAsvaa(Date fechaMarcacionAsvaa) {
        this.fechaMarcacionAsvaa = fechaMarcacionAsvaa;
    }

    public BigDecimal getDiferenciaSalidaAsvaa() {
        return diferenciaSalidaAsvaa;
    }

    public void setDiferenciaSalidaAsvaa(BigDecimal diferenciaSalidaAsvaa) {
        this.diferenciaSalidaAsvaa = diferenciaSalidaAsvaa;
    }

    public BigDecimal getDiferenciaAsvaa() {
        return diferenciaAsvaa;
    }

    public void setDiferenciaAsvaa(BigDecimal diferenciaAsvaa) {
        this.diferenciaAsvaa = diferenciaAsvaa;
    }

    public String getEventoAsvaa() {
        return eventoAsvaa;
    }

    public void setEventoAsvaa(String eventoAsvaa) {
        this.eventoAsvaa = eventoAsvaa;
    }

    public Integer getImportoAsvaa() {
        return importoAsvaa;
    }

    public void setImportoAsvaa(Integer importoAsvaa) {
        this.importoAsvaa = importoAsvaa;
    }

    public Boolean getActivoAsvaa() {
        return activoAsvaa;
    }

    public void setActivoAsvaa(Boolean activoAsvaa) {
        this.activoAsvaa = activoAsvaa;
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

    public Date getHoraMarcacionAsvaa() {
        return horaMarcacionAsvaa;
    }

    public void setHoraMarcacionAsvaa(Date horaMarcacionAsvaa) {
        this.horaMarcacionAsvaa = horaMarcacionAsvaa;
    }

    public Date getHoraMarcaSalidaAsvaa() {
        return horaMarcaSalidaAsvaa;
    }

    public void setHoraMarcaSalidaAsvaa(Date horaMarcaSalidaAsvaa) {
        this.horaMarcaSalidaAsvaa = horaMarcaSalidaAsvaa;
    }

    public Boolean getNovedadAsvaa() {
        return novedadAsvaa;
    }

    public void setNovedadAsvaa(Boolean novedadAsvaa) {
        this.novedadAsvaa = novedadAsvaa;
    }

    public Boolean getVerificadoAsvaa() {
        return verificadoAsvaa;
    }

    public void setVerificadoAsvaa(Boolean verificadoAsvaa) {
        this.verificadoAsvaa = verificadoAsvaa;
    }

    public List<AsiValidaAsistenciaJustifi> getAsiValidaAsistenciaJustifiList() {
        return asiValidaAsistenciaJustifiList;
    }

    public void setAsiValidaAsistenciaJustifiList(List<AsiValidaAsistenciaJustifi> asiValidaAsistenciaJustifiList) {
        this.asiValidaAsistenciaJustifiList = asiValidaAsistenciaJustifiList;
    }

    public GenEmpleadosDepartamentoPar getIdeGeedp() {
        return ideGeedp;
    }

    public void setIdeGeedp(GenEmpleadosDepartamentoPar ideGeedp) {
        this.ideGeedp = ideGeedp;
    }

    public AsiNovedad getIdeAsnov() {
        return ideAsnov;
    }

    public void setIdeAsnov(AsiNovedad ideAsnov) {
        this.ideAsnov = ideAsnov;
    }

    public AsiDetalleHorasExtras getIdeAsdhe() {
        return ideAsdhe;
    }

    public void setIdeAsdhe(AsiDetalleHorasExtras ideAsdhe) {
        this.ideAsdhe = ideAsdhe;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAsvaa != null ? ideAsvaa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AsiValidaAsistencia)) {
            return false;
        }
        AsiValidaAsistencia other = (AsiValidaAsistencia) object;
        if ((this.ideAsvaa == null && other.ideAsvaa != null) || (this.ideAsvaa != null && !this.ideAsvaa.equals(other.ideAsvaa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.AsiValidaAsistencia[ ideAsvaa=" + ideAsvaa + " ]";
    }
    
}
