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
@Table(name = "sis_usuario", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SisUsuario.findAll", query = "SELECT s FROM SisUsuario s"),
    @NamedQuery(name = "SisUsuario.findByIdeUsua", query = "SELECT s FROM SisUsuario s WHERE s.ideUsua = :ideUsua"),
    @NamedQuery(name = "SisUsuario.findByNomUsua", query = "SELECT s FROM SisUsuario s WHERE s.nomUsua = :nomUsua"),
    @NamedQuery(name = "SisUsuario.findByNickUsua", query = "SELECT s FROM SisUsuario s WHERE s.nickUsua = :nickUsua"),
    @NamedQuery(name = "SisUsuario.findByMailUsua", query = "SELECT s FROM SisUsuario s WHERE s.mailUsua = :mailUsua"),
    @NamedQuery(name = "SisUsuario.findByFechaRegUsua", query = "SELECT s FROM SisUsuario s WHERE s.fechaRegUsua = :fechaRegUsua"),
    @NamedQuery(name = "SisUsuario.findByFechaCaducUsua", query = "SELECT s FROM SisUsuario s WHERE s.fechaCaducUsua = :fechaCaducUsua"),
    @NamedQuery(name = "SisUsuario.findByActivoUsua", query = "SELECT s FROM SisUsuario s WHERE s.activoUsua = :activoUsua"),
    @NamedQuery(name = "SisUsuario.findByTemaUsua", query = "SELECT s FROM SisUsuario s WHERE s.temaUsua = :temaUsua"),
    @NamedQuery(name = "SisUsuario.findByUsuarioIngre", query = "SELECT s FROM SisUsuario s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SisUsuario.findByFechaIngre", query = "SELECT s FROM SisUsuario s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SisUsuario.findByUsuarioActua", query = "SELECT s FROM SisUsuario s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SisUsuario.findByFechaActua", query = "SELECT s FROM SisUsuario s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SisUsuario.findByBloqueadoUsua", query = "SELECT s FROM SisUsuario s WHERE s.bloqueadoUsua = :bloqueadoUsua"),
    @NamedQuery(name = "SisUsuario.findByCambiaClaveUsua", query = "SELECT s FROM SisUsuario s WHERE s.cambiaClaveUsua = :cambiaClaveUsua"),
    @NamedQuery(name = "SisUsuario.findByHoraActua", query = "SELECT s FROM SisUsuario s WHERE s.horaActua = :horaActua"),
    @NamedQuery(name = "SisUsuario.findByHoraIngre", query = "SELECT s FROM SisUsuario s WHERE s.horaIngre = :horaIngre")})
public class SisUsuario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_usua", nullable = false)
    private Integer ideUsua;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "nom_usua", nullable = false, length = 80)
    private String nomUsua;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "nick_usua", nullable = false, length = 40)
    private String nickUsua;
    @Size(max = 100)
    @Column(name = "mail_usua", length = 100)
    private String mailUsua;
    @Column(name = "fecha_reg_usua")
    @Temporal(TemporalType.DATE)
    private Date fechaRegUsua;
    @Column(name = "fecha_caduc_usua")
    @Temporal(TemporalType.DATE)
    private Date fechaCaducUsua;
    @Column(name = "activo_usua")
    private Boolean activoUsua;
    @Size(max = 40)
    @Column(name = "tema_usua", length = 40)
    private String temaUsua;
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
    @Column(name = "bloqueado_usua")
    private Boolean bloqueadoUsua;
    @Column(name = "cambia_clave_usua")
    private Boolean cambiaClaveUsua;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideUsua")
    private List<CntMovimientoContable> cntMovimientoContableList;
    @OneToMany(mappedBy = "ideUsua")
    private List<SisAuditoria> sisAuditoriaList;
    @OneToMany(mappedBy = "ideUsua")
    private List<AsiNovedad> asiNovedadList;
    @JoinColumn(name = "ide_perf", referencedColumnName = "ide_perf")
    @ManyToOne
    private SisPerfil idePerf;
    @JoinColumn(name = "ide_empr", referencedColumnName = "ide_empr")
    @ManyToOne
    private SisEmpresa ideEmpr;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @OneToMany(mappedBy = "ideUsua")
    private List<SisUsuarioClave> sisUsuarioClaveList;
    @OneToMany(mappedBy = "ideUsua")
    private List<SaoEvaluacionPosiciograma> saoEvaluacionPosiciogramaList;
    @OneToMany(mappedBy = "ideUsua")
    private List<SisUsuarioSucursal> sisUsuarioSucursalList;
    @OneToMany(mappedBy = "ideUsua")
    private List<FacUsuarioLugar> facUsuarioLugarList;
    @OneToMany(mappedBy = "ideUsua")
    private List<SaoFichaMedica> saoFichaMedicaList;
    @OneToMany(mappedBy = "ideUsua")
    private List<NrhRol> nrhRolList;
    @OneToMany(mappedBy = "ideUsua")
    private List<SisHistorialClaves> sisHistorialClavesList;
    @OneToMany(mappedBy = "ideUsua")
    private List<SisBloqueo> sisBloqueoList;
    @OneToMany(mappedBy = "ideUsua")
    private List<CrcUsuarioCalendario> crcUsuarioCalendarioList;

    public SisUsuario() {
    }

    public SisUsuario(Integer ideUsua) {
        this.ideUsua = ideUsua;
    }

    public SisUsuario(Integer ideUsua, String nomUsua, String nickUsua) {
        this.ideUsua = ideUsua;
        this.nomUsua = nomUsua;
        this.nickUsua = nickUsua;
    }

    public Integer getIdeUsua() {
        return ideUsua;
    }

    public void setIdeUsua(Integer ideUsua) {
        this.ideUsua = ideUsua;
    }

    public String getNomUsua() {
        return nomUsua;
    }

    public void setNomUsua(String nomUsua) {
        this.nomUsua = nomUsua;
    }

    public String getNickUsua() {
        return nickUsua;
    }

    public void setNickUsua(String nickUsua) {
        this.nickUsua = nickUsua;
    }

    public String getMailUsua() {
        return mailUsua;
    }

    public void setMailUsua(String mailUsua) {
        this.mailUsua = mailUsua;
    }

    public Date getFechaRegUsua() {
        return fechaRegUsua;
    }

    public void setFechaRegUsua(Date fechaRegUsua) {
        this.fechaRegUsua = fechaRegUsua;
    }

    public Date getFechaCaducUsua() {
        return fechaCaducUsua;
    }

    public void setFechaCaducUsua(Date fechaCaducUsua) {
        this.fechaCaducUsua = fechaCaducUsua;
    }

    public Boolean getActivoUsua() {
        return activoUsua;
    }

    public void setActivoUsua(Boolean activoUsua) {
        this.activoUsua = activoUsua;
    }

    public String getTemaUsua() {
        return temaUsua;
    }

    public void setTemaUsua(String temaUsua) {
        this.temaUsua = temaUsua;
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

    public Boolean getBloqueadoUsua() {
        return bloqueadoUsua;
    }

    public void setBloqueadoUsua(Boolean bloqueadoUsua) {
        this.bloqueadoUsua = bloqueadoUsua;
    }

    public Boolean getCambiaClaveUsua() {
        return cambiaClaveUsua;
    }

    public void setCambiaClaveUsua(Boolean cambiaClaveUsua) {
        this.cambiaClaveUsua = cambiaClaveUsua;
    }

    public Date getHoraActua() {
        return horaActua;
    }

    public void setHoraActua(Date horaActua) {
        this.horaActua = horaActua;
    }

    public Date getHoraIngre() {
        return horaIngre;
    }

    public void setHoraIngre(Date horaIngre) {
        this.horaIngre = horaIngre;
    }

    public List<CntMovimientoContable> getCntMovimientoContableList() {
        return cntMovimientoContableList;
    }

    public void setCntMovimientoContableList(List<CntMovimientoContable> cntMovimientoContableList) {
        this.cntMovimientoContableList = cntMovimientoContableList;
    }

    public List<SisAuditoria> getSisAuditoriaList() {
        return sisAuditoriaList;
    }

    public void setSisAuditoriaList(List<SisAuditoria> sisAuditoriaList) {
        this.sisAuditoriaList = sisAuditoriaList;
    }

    public List<AsiNovedad> getAsiNovedadList() {
        return asiNovedadList;
    }

    public void setAsiNovedadList(List<AsiNovedad> asiNovedadList) {
        this.asiNovedadList = asiNovedadList;
    }

    public SisPerfil getIdePerf() {
        return idePerf;
    }

    public void setIdePerf(SisPerfil idePerf) {
        this.idePerf = idePerf;
    }

    public SisEmpresa getIdeEmpr() {
        return ideEmpr;
    }

    public void setIdeEmpr(SisEmpresa ideEmpr) {
        this.ideEmpr = ideEmpr;
    }

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    public List<SisUsuarioClave> getSisUsuarioClaveList() {
        return sisUsuarioClaveList;
    }

    public void setSisUsuarioClaveList(List<SisUsuarioClave> sisUsuarioClaveList) {
        this.sisUsuarioClaveList = sisUsuarioClaveList;
    }

    public List<SaoEvaluacionPosiciograma> getSaoEvaluacionPosiciogramaList() {
        return saoEvaluacionPosiciogramaList;
    }

    public void setSaoEvaluacionPosiciogramaList(List<SaoEvaluacionPosiciograma> saoEvaluacionPosiciogramaList) {
        this.saoEvaluacionPosiciogramaList = saoEvaluacionPosiciogramaList;
    }

    public List<SisUsuarioSucursal> getSisUsuarioSucursalList() {
        return sisUsuarioSucursalList;
    }

    public void setSisUsuarioSucursalList(List<SisUsuarioSucursal> sisUsuarioSucursalList) {
        this.sisUsuarioSucursalList = sisUsuarioSucursalList;
    }

    public List<FacUsuarioLugar> getFacUsuarioLugarList() {
        return facUsuarioLugarList;
    }

    public void setFacUsuarioLugarList(List<FacUsuarioLugar> facUsuarioLugarList) {
        this.facUsuarioLugarList = facUsuarioLugarList;
    }

    public List<SaoFichaMedica> getSaoFichaMedicaList() {
        return saoFichaMedicaList;
    }

    public void setSaoFichaMedicaList(List<SaoFichaMedica> saoFichaMedicaList) {
        this.saoFichaMedicaList = saoFichaMedicaList;
    }

    public List<NrhRol> getNrhRolList() {
        return nrhRolList;
    }

    public void setNrhRolList(List<NrhRol> nrhRolList) {
        this.nrhRolList = nrhRolList;
    }

    public List<SisHistorialClaves> getSisHistorialClavesList() {
        return sisHistorialClavesList;
    }

    public void setSisHistorialClavesList(List<SisHistorialClaves> sisHistorialClavesList) {
        this.sisHistorialClavesList = sisHistorialClavesList;
    }

    public List<SisBloqueo> getSisBloqueoList() {
        return sisBloqueoList;
    }

    public void setSisBloqueoList(List<SisBloqueo> sisBloqueoList) {
        this.sisBloqueoList = sisBloqueoList;
    }

    public List<CrcUsuarioCalendario> getCrcUsuarioCalendarioList() {
        return crcUsuarioCalendarioList;
    }

    public void setCrcUsuarioCalendarioList(List<CrcUsuarioCalendario> crcUsuarioCalendarioList) {
        this.crcUsuarioCalendarioList = crcUsuarioCalendarioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideUsua != null ? ideUsua.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisUsuario)) {
            return false;
        }
        SisUsuario other = (SisUsuario) object;
        if ((this.ideUsua == null && other.ideUsua != null) || (this.ideUsua != null && !this.ideUsua.equals(other.ideUsua))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisUsuario[ ideUsua=" + ideUsua + " ]";
    }
    
}
