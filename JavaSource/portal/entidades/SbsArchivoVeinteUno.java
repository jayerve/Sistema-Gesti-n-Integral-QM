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
@Table(name = "sbs_archivo_veinte_uno", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SbsArchivoVeinteUno.findAll", query = "SELECT s FROM SbsArchivoVeinteUno s"),
    @NamedQuery(name = "SbsArchivoVeinteUno.findByIdeSbavu", query = "SELECT s FROM SbsArchivoVeinteUno s WHERE s.ideSbavu = :ideSbavu"),
    @NamedQuery(name = "SbsArchivoVeinteUno.findByPeriodoCargoSbavu", query = "SELECT s FROM SbsArchivoVeinteUno s WHERE s.periodoCargoSbavu = :periodoCargoSbavu"),
    @NamedQuery(name = "SbsArchivoVeinteUno.findByMotivoFinalizacionSbavu", query = "SELECT s FROM SbsArchivoVeinteUno s WHERE s.motivoFinalizacionSbavu = :motivoFinalizacionSbavu"),
    @NamedQuery(name = "SbsArchivoVeinteUno.findByFechaResolOficioSbavu", query = "SELECT s FROM SbsArchivoVeinteUno s WHERE s.fechaResolOficioSbavu = :fechaResolOficioSbavu"),
    @NamedQuery(name = "SbsArchivoVeinteUno.findByNroResolOficioSbavu", query = "SELECT s FROM SbsArchivoVeinteUno s WHERE s.nroResolOficioSbavu = :nroResolOficioSbavu"),
    @NamedQuery(name = "SbsArchivoVeinteUno.findByTituloProfesionalSbavu", query = "SELECT s FROM SbsArchivoVeinteUno s WHERE s.tituloProfesionalSbavu = :tituloProfesionalSbavu"),
    @NamedQuery(name = "SbsArchivoVeinteUno.findByTelefonoOficinaSbavu", query = "SELECT s FROM SbsArchivoVeinteUno s WHERE s.telefonoOficinaSbavu = :telefonoOficinaSbavu"),
    @NamedQuery(name = "SbsArchivoVeinteUno.findByFechaTransaccionSbavu", query = "SELECT s FROM SbsArchivoVeinteUno s WHERE s.fechaTransaccionSbavu = :fechaTransaccionSbavu"),
    @NamedQuery(name = "SbsArchivoVeinteUno.findByDireccionSbavu", query = "SELECT s FROM SbsArchivoVeinteUno s WHERE s.direccionSbavu = :direccionSbavu"),
    @NamedQuery(name = "SbsArchivoVeinteUno.findByTelefonoPersonalSbavu", query = "SELECT s FROM SbsArchivoVeinteUno s WHERE s.telefonoPersonalSbavu = :telefonoPersonalSbavu"),
    @NamedQuery(name = "SbsArchivoVeinteUno.findByEstatusRegistroSbavu", query = "SELECT s FROM SbsArchivoVeinteUno s WHERE s.estatusRegistroSbavu = :estatusRegistroSbavu"),
    @NamedQuery(name = "SbsArchivoVeinteUno.findByActivoSbavu", query = "SELECT s FROM SbsArchivoVeinteUno s WHERE s.activoSbavu = :activoSbavu"),
    @NamedQuery(name = "SbsArchivoVeinteUno.findByUsuarioIngre", query = "SELECT s FROM SbsArchivoVeinteUno s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SbsArchivoVeinteUno.findByFechaIngre", query = "SELECT s FROM SbsArchivoVeinteUno s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SbsArchivoVeinteUno.findByHoraIngre", query = "SELECT s FROM SbsArchivoVeinteUno s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SbsArchivoVeinteUno.findByUsuarioActua", query = "SELECT s FROM SbsArchivoVeinteUno s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SbsArchivoVeinteUno.findByFechaActua", query = "SELECT s FROM SbsArchivoVeinteUno s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SbsArchivoVeinteUno.findByHoraActua", query = "SELECT s FROM SbsArchivoVeinteUno s WHERE s.horaActua = :horaActua")})
public class SbsArchivoVeinteUno implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sbavu", nullable = false)
    private Integer ideSbavu;
    @Column(name = "periodo_cargo_sbavu")
    private Integer periodoCargoSbavu;
    @Size(max = 50)
    @Column(name = "motivo_finalizacion_sbavu", length = 50)
    private String motivoFinalizacionSbavu;
    @Size(max = 50)
    @Column(name = "fecha_resol_oficio_sbavu", length = 50)
    private String fechaResolOficioSbavu;
    @Size(max = 50)
    @Column(name = "nro_resol_oficio_sbavu", length = 50)
    private String nroResolOficioSbavu;
    @Size(max = 50)
    @Column(name = "titulo_profesional_sbavu", length = 50)
    private String tituloProfesionalSbavu;
    @Size(max = 50)
    @Column(name = "telefono_oficina_sbavu", length = 50)
    private String telefonoOficinaSbavu;
    @Column(name = "fecha_transaccion_sbavu")
    @Temporal(TemporalType.DATE)
    private Date fechaTransaccionSbavu;
    @Size(max = 500)
    @Column(name = "direccion_sbavu", length = 500)
    private String direccionSbavu;
    @Size(max = 50)
    @Column(name = "telefono_personal_sbavu", length = 50)
    private String telefonoPersonalSbavu;
    @Size(max = 50)
    @Column(name = "estatus_registro_sbavu", length = 50)
    private String estatusRegistroSbavu;
    @Column(name = "activo_sbavu")
    private Boolean activoSbavu;
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
    @JoinColumn(name = "ide_sbtic", referencedColumnName = "ide_sbtic")
    @ManyToOne
    private SbsTipoCargo ideSbtic;
    @JoinColumn(name = "ide_sbpec", referencedColumnName = "ide_sbpec")
    @ManyToOne
    private SbsPeriodoCatastro ideSbpec;
    @JoinColumn(name = "ide_sbofi", referencedColumnName = "ide_sbofi")
    @ManyToOne
    private SbsOficina ideSbofi;
    @JoinColumn(name = "ide_sbcar", referencedColumnName = "ide_sbcar")
    @ManyToOne
    private SbsCargo ideSbcar;
    @JoinColumn(name = "ide_gttdi", referencedColumnName = "ide_gttdi")
    @ManyToOne
    private GthTipoDocumentoIdentidad ideGttdi;
    @JoinColumn(name = "ide_gtnac", referencedColumnName = "ide_gtnac")
    @ManyToOne
    private GthNacionalidad ideGtnac;
    @JoinColumn(name = "ide_gtgen", referencedColumnName = "ide_gtgen")
    @ManyToOne
    private GthGenero ideGtgen;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;

    public SbsArchivoVeinteUno() {
    }

    public SbsArchivoVeinteUno(Integer ideSbavu) {
        this.ideSbavu = ideSbavu;
    }

    public Integer getIdeSbavu() {
        return ideSbavu;
    }

    public void setIdeSbavu(Integer ideSbavu) {
        this.ideSbavu = ideSbavu;
    }

    public Integer getPeriodoCargoSbavu() {
        return periodoCargoSbavu;
    }

    public void setPeriodoCargoSbavu(Integer periodoCargoSbavu) {
        this.periodoCargoSbavu = periodoCargoSbavu;
    }

    public String getMotivoFinalizacionSbavu() {
        return motivoFinalizacionSbavu;
    }

    public void setMotivoFinalizacionSbavu(String motivoFinalizacionSbavu) {
        this.motivoFinalizacionSbavu = motivoFinalizacionSbavu;
    }

    public String getFechaResolOficioSbavu() {
        return fechaResolOficioSbavu;
    }

    public void setFechaResolOficioSbavu(String fechaResolOficioSbavu) {
        this.fechaResolOficioSbavu = fechaResolOficioSbavu;
    }

    public String getNroResolOficioSbavu() {
        return nroResolOficioSbavu;
    }

    public void setNroResolOficioSbavu(String nroResolOficioSbavu) {
        this.nroResolOficioSbavu = nroResolOficioSbavu;
    }

    public String getTituloProfesionalSbavu() {
        return tituloProfesionalSbavu;
    }

    public void setTituloProfesionalSbavu(String tituloProfesionalSbavu) {
        this.tituloProfesionalSbavu = tituloProfesionalSbavu;
    }

    public String getTelefonoOficinaSbavu() {
        return telefonoOficinaSbavu;
    }

    public void setTelefonoOficinaSbavu(String telefonoOficinaSbavu) {
        this.telefonoOficinaSbavu = telefonoOficinaSbavu;
    }

    public Date getFechaTransaccionSbavu() {
        return fechaTransaccionSbavu;
    }

    public void setFechaTransaccionSbavu(Date fechaTransaccionSbavu) {
        this.fechaTransaccionSbavu = fechaTransaccionSbavu;
    }

    public String getDireccionSbavu() {
        return direccionSbavu;
    }

    public void setDireccionSbavu(String direccionSbavu) {
        this.direccionSbavu = direccionSbavu;
    }

    public String getTelefonoPersonalSbavu() {
        return telefonoPersonalSbavu;
    }

    public void setTelefonoPersonalSbavu(String telefonoPersonalSbavu) {
        this.telefonoPersonalSbavu = telefonoPersonalSbavu;
    }

    public String getEstatusRegistroSbavu() {
        return estatusRegistroSbavu;
    }

    public void setEstatusRegistroSbavu(String estatusRegistroSbavu) {
        this.estatusRegistroSbavu = estatusRegistroSbavu;
    }

    public Boolean getActivoSbavu() {
        return activoSbavu;
    }

    public void setActivoSbavu(Boolean activoSbavu) {
        this.activoSbavu = activoSbavu;
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

    public SbsTipoCargo getIdeSbtic() {
        return ideSbtic;
    }

    public void setIdeSbtic(SbsTipoCargo ideSbtic) {
        this.ideSbtic = ideSbtic;
    }

    public SbsPeriodoCatastro getIdeSbpec() {
        return ideSbpec;
    }

    public void setIdeSbpec(SbsPeriodoCatastro ideSbpec) {
        this.ideSbpec = ideSbpec;
    }

    public SbsOficina getIdeSbofi() {
        return ideSbofi;
    }

    public void setIdeSbofi(SbsOficina ideSbofi) {
        this.ideSbofi = ideSbofi;
    }

    public SbsCargo getIdeSbcar() {
        return ideSbcar;
    }

    public void setIdeSbcar(SbsCargo ideSbcar) {
        this.ideSbcar = ideSbcar;
    }

    public GthTipoDocumentoIdentidad getIdeGttdi() {
        return ideGttdi;
    }

    public void setIdeGttdi(GthTipoDocumentoIdentidad ideGttdi) {
        this.ideGttdi = ideGttdi;
    }

    public GthNacionalidad getIdeGtnac() {
        return ideGtnac;
    }

    public void setIdeGtnac(GthNacionalidad ideGtnac) {
        this.ideGtnac = ideGtnac;
    }

    public GthGenero getIdeGtgen() {
        return ideGtgen;
    }

    public void setIdeGtgen(GthGenero ideGtgen) {
        this.ideGtgen = ideGtgen;
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
        hash += (ideSbavu != null ? ideSbavu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SbsArchivoVeinteUno)) {
            return false;
        }
        SbsArchivoVeinteUno other = (SbsArchivoVeinteUno) object;
        if ((this.ideSbavu == null && other.ideSbavu != null) || (this.ideSbavu != null && !this.ideSbavu.equals(other.ideSbavu))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SbsArchivoVeinteUno[ ideSbavu=" + ideSbavu + " ]";
    }
    
}
