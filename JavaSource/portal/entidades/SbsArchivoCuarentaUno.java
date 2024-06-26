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
@Table(name = "sbs_archivo_cuarenta_uno", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SbsArchivoCuarentaUno.findAll", query = "SELECT s FROM SbsArchivoCuarentaUno s"),
    @NamedQuery(name = "SbsArchivoCuarentaUno.findByIdeSbacu", query = "SELECT s FROM SbsArchivoCuarentaUno s WHERE s.ideSbacu = :ideSbacu"),
    @NamedQuery(name = "SbsArchivoCuarentaUno.findByTipoAuditorSbacu", query = "SELECT s FROM SbsArchivoCuarentaUno s WHERE s.tipoAuditorSbacu = :tipoAuditorSbacu"),
    @NamedQuery(name = "SbsArchivoCuarentaUno.findByFechaInicioSbacu", query = "SELECT s FROM SbsArchivoCuarentaUno s WHERE s.fechaInicioSbacu = :fechaInicioSbacu"),
    @NamedQuery(name = "SbsArchivoCuarentaUno.findByFechaFinSbacu", query = "SELECT s FROM SbsArchivoCuarentaUno s WHERE s.fechaFinSbacu = :fechaFinSbacu"),
    @NamedQuery(name = "SbsArchivoCuarentaUno.findByMotivoFinaliza", query = "SELECT s FROM SbsArchivoCuarentaUno s WHERE s.motivoFinaliza = :motivoFinaliza"),
    @NamedQuery(name = "SbsArchivoCuarentaUno.findByFechaResolSbacu", query = "SELECT s FROM SbsArchivoCuarentaUno s WHERE s.fechaResolSbacu = :fechaResolSbacu"),
    @NamedQuery(name = "SbsArchivoCuarentaUno.findByNumResolSbacu", query = "SELECT s FROM SbsArchivoCuarentaUno s WHERE s.numResolSbacu = :numResolSbacu"),
    @NamedQuery(name = "SbsArchivoCuarentaUno.findByEstatusSbacu", query = "SELECT s FROM SbsArchivoCuarentaUno s WHERE s.estatusSbacu = :estatusSbacu"),
    @NamedQuery(name = "SbsArchivoCuarentaUno.findByActivoSbacu", query = "SELECT s FROM SbsArchivoCuarentaUno s WHERE s.activoSbacu = :activoSbacu"),
    @NamedQuery(name = "SbsArchivoCuarentaUno.findByUsuarioIngre", query = "SELECT s FROM SbsArchivoCuarentaUno s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SbsArchivoCuarentaUno.findByFechaIngre", query = "SELECT s FROM SbsArchivoCuarentaUno s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SbsArchivoCuarentaUno.findByHoraIngre", query = "SELECT s FROM SbsArchivoCuarentaUno s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SbsArchivoCuarentaUno.findByUsuarioActua", query = "SELECT s FROM SbsArchivoCuarentaUno s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SbsArchivoCuarentaUno.findByFechaActua", query = "SELECT s FROM SbsArchivoCuarentaUno s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SbsArchivoCuarentaUno.findByHoraActua", query = "SELECT s FROM SbsArchivoCuarentaUno s WHERE s.horaActua = :horaActua")})
public class SbsArchivoCuarentaUno implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sbacu", nullable = false)
    private Integer ideSbacu;
    @Size(max = 50)
    @Column(name = "tipo_auditor_sbacu", length = 50)
    private String tipoAuditorSbacu;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio_sbacu", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaInicioSbacu;
    @Column(name = "fecha_fin_sbacu")
    @Temporal(TemporalType.DATE)
    private Date fechaFinSbacu;
    @Size(max = 100)
    @Column(name = "motivo_finaliza", length = 100)
    private String motivoFinaliza;
    @Column(name = "fecha_resol_sbacu")
    @Temporal(TemporalType.DATE)
    private Date fechaResolSbacu;
    @Size(max = 50)
    @Column(name = "num_resol_sbacu", length = 50)
    private String numResolSbacu;
    @Size(max = 50)
    @Column(name = "estatus_sbacu", length = 50)
    private String estatusSbacu;
    @Column(name = "activo_sbacu")
    private Boolean activoSbacu;
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
    @JoinColumn(name = "ide_sbpec", referencedColumnName = "ide_sbpec")
    @ManyToOne
    private SbsPeriodoCatastro ideSbpec;
    @JoinColumn(name = "ide_gttdi", referencedColumnName = "ide_gttdi")
    @ManyToOne
    private GthTipoDocumentoIdentidad ideGttdi;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;

    public SbsArchivoCuarentaUno() {
    }

    public SbsArchivoCuarentaUno(Integer ideSbacu) {
        this.ideSbacu = ideSbacu;
    }

    public SbsArchivoCuarentaUno(Integer ideSbacu, Date fechaInicioSbacu) {
        this.ideSbacu = ideSbacu;
        this.fechaInicioSbacu = fechaInicioSbacu;
    }

    public Integer getIdeSbacu() {
        return ideSbacu;
    }

    public void setIdeSbacu(Integer ideSbacu) {
        this.ideSbacu = ideSbacu;
    }

    public String getTipoAuditorSbacu() {
        return tipoAuditorSbacu;
    }

    public void setTipoAuditorSbacu(String tipoAuditorSbacu) {
        this.tipoAuditorSbacu = tipoAuditorSbacu;
    }

    public Date getFechaInicioSbacu() {
        return fechaInicioSbacu;
    }

    public void setFechaInicioSbacu(Date fechaInicioSbacu) {
        this.fechaInicioSbacu = fechaInicioSbacu;
    }

    public Date getFechaFinSbacu() {
        return fechaFinSbacu;
    }

    public void setFechaFinSbacu(Date fechaFinSbacu) {
        this.fechaFinSbacu = fechaFinSbacu;
    }

    public String getMotivoFinaliza() {
        return motivoFinaliza;
    }

    public void setMotivoFinaliza(String motivoFinaliza) {
        this.motivoFinaliza = motivoFinaliza;
    }

    public Date getFechaResolSbacu() {
        return fechaResolSbacu;
    }

    public void setFechaResolSbacu(Date fechaResolSbacu) {
        this.fechaResolSbacu = fechaResolSbacu;
    }

    public String getNumResolSbacu() {
        return numResolSbacu;
    }

    public void setNumResolSbacu(String numResolSbacu) {
        this.numResolSbacu = numResolSbacu;
    }

    public String getEstatusSbacu() {
        return estatusSbacu;
    }

    public void setEstatusSbacu(String estatusSbacu) {
        this.estatusSbacu = estatusSbacu;
    }

    public Boolean getActivoSbacu() {
        return activoSbacu;
    }

    public void setActivoSbacu(Boolean activoSbacu) {
        this.activoSbacu = activoSbacu;
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

    public SbsPeriodoCatastro getIdeSbpec() {
        return ideSbpec;
    }

    public void setIdeSbpec(SbsPeriodoCatastro ideSbpec) {
        this.ideSbpec = ideSbpec;
    }

    public GthTipoDocumentoIdentidad getIdeGttdi() {
        return ideGttdi;
    }

    public void setIdeGttdi(GthTipoDocumentoIdentidad ideGttdi) {
        this.ideGttdi = ideGttdi;
    }

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSbacu != null ? ideSbacu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SbsArchivoCuarentaUno)) {
            return false;
        }
        SbsArchivoCuarentaUno other = (SbsArchivoCuarentaUno) object;
        if ((this.ideSbacu == null && other.ideSbacu != null) || (this.ideSbacu != null && !this.ideSbacu.equals(other.ideSbacu))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SbsArchivoCuarentaUno[ ideSbacu=" + ideSbacu + " ]";
    }
    
}
