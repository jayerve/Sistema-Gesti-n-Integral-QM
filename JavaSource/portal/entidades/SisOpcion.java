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
@Table(name = "sis_opcion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SisOpcion.findAll", query = "SELECT s FROM SisOpcion s"),
    @NamedQuery(name = "SisOpcion.findByIdeOpci", query = "SELECT s FROM SisOpcion s WHERE s.ideOpci = :ideOpci"),
    @NamedQuery(name = "SisOpcion.findByNomOpci", query = "SELECT s FROM SisOpcion s WHERE s.nomOpci = :nomOpci"),
    @NamedQuery(name = "SisOpcion.findByTipoOpci", query = "SELECT s FROM SisOpcion s WHERE s.tipoOpci = :tipoOpci"),
    @NamedQuery(name = "SisOpcion.findByPaqueteOpci", query = "SELECT s FROM SisOpcion s WHERE s.paqueteOpci = :paqueteOpci"),
    @NamedQuery(name = "SisOpcion.findByManualOpci", query = "SELECT s FROM SisOpcion s WHERE s.manualOpci = :manualOpci"),
    @NamedQuery(name = "SisOpcion.findByUsuarioIngre", query = "SELECT s FROM SisOpcion s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SisOpcion.findByFechaIngre", query = "SELECT s FROM SisOpcion s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SisOpcion.findByUsuarioActua", query = "SELECT s FROM SisOpcion s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SisOpcion.findByFechaActua", query = "SELECT s FROM SisOpcion s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SisOpcion.findByHoraIngre", query = "SELECT s FROM SisOpcion s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SisOpcion.findByHoraActua", query = "SELECT s FROM SisOpcion s WHERE s.horaActua = :horaActua"),
    @NamedQuery(name = "SisOpcion.findByAuditoriaOpci", query = "SELECT s FROM SisOpcion s WHERE s.auditoriaOpci = :auditoriaOpci")})
public class SisOpcion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_opci", nullable = false)
    private Integer ideOpci;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nom_opci", nullable = false, length = 50)
    private String nomOpci;
    @Size(max = 55)
    @Column(name = "tipo_opci", length = 55)
    private String tipoOpci;
    @Size(max = 50)
    @Column(name = "paquete_opci", length = 50)
    private String paqueteOpci;
    @Size(max = 60)
    @Column(name = "manual_opci", length = 60)
    private String manualOpci;
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
    @Column(name = "auditoria_opci")
    private Boolean auditoriaOpci;
    @OneToMany(mappedBy = "ideOpci")
    private List<SisReporte> sisReporteList;
    @OneToMany(mappedBy = "ideOpci")
    private List<SisObjetoOpcion> sisObjetoOpcionList;
    @OneToMany(mappedBy = "ideOpci")
    private List<SisAuditoria> sisAuditoriaList;
    @OneToMany(mappedBy = "ideOpci")
    private List<SisPerfilOpcion> sisPerfilOpcionList;
    @OneToMany(mappedBy = "sisIdeOpci")
    private List<SisOpcion> sisOpcionList;
    @JoinColumn(name = "sis_ide_opci", referencedColumnName = "ide_opci")
    @ManyToOne
    private SisOpcion sisIdeOpci;
    @OneToMany(mappedBy = "ideOpci")
    private List<SisTabla> sisTablaList;
    @OneToMany(mappedBy = "ideOpci")
    private List<CrtActividad> crtActividadList;

    public SisOpcion() {
    }

    public SisOpcion(Integer ideOpci) {
        this.ideOpci = ideOpci;
    }

    public SisOpcion(Integer ideOpci, String nomOpci) {
        this.ideOpci = ideOpci;
        this.nomOpci = nomOpci;
    }

    public Integer getIdeOpci() {
        return ideOpci;
    }

    public void setIdeOpci(Integer ideOpci) {
        this.ideOpci = ideOpci;
    }

    public String getNomOpci() {
        return nomOpci;
    }

    public void setNomOpci(String nomOpci) {
        this.nomOpci = nomOpci;
    }

    public String getTipoOpci() {
        return tipoOpci;
    }

    public void setTipoOpci(String tipoOpci) {
        this.tipoOpci = tipoOpci;
    }

    public String getPaqueteOpci() {
        return paqueteOpci;
    }

    public void setPaqueteOpci(String paqueteOpci) {
        this.paqueteOpci = paqueteOpci;
    }

    public String getManualOpci() {
        return manualOpci;
    }

    public void setManualOpci(String manualOpci) {
        this.manualOpci = manualOpci;
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

    public Boolean getAuditoriaOpci() {
        return auditoriaOpci;
    }

    public void setAuditoriaOpci(Boolean auditoriaOpci) {
        this.auditoriaOpci = auditoriaOpci;
    }

    public List<SisReporte> getSisReporteList() {
        return sisReporteList;
    }

    public void setSisReporteList(List<SisReporte> sisReporteList) {
        this.sisReporteList = sisReporteList;
    }

    public List<SisObjetoOpcion> getSisObjetoOpcionList() {
        return sisObjetoOpcionList;
    }

    public void setSisObjetoOpcionList(List<SisObjetoOpcion> sisObjetoOpcionList) {
        this.sisObjetoOpcionList = sisObjetoOpcionList;
    }

    public List<SisAuditoria> getSisAuditoriaList() {
        return sisAuditoriaList;
    }

    public void setSisAuditoriaList(List<SisAuditoria> sisAuditoriaList) {
        this.sisAuditoriaList = sisAuditoriaList;
    }

    public List<SisPerfilOpcion> getSisPerfilOpcionList() {
        return sisPerfilOpcionList;
    }

    public void setSisPerfilOpcionList(List<SisPerfilOpcion> sisPerfilOpcionList) {
        this.sisPerfilOpcionList = sisPerfilOpcionList;
    }

    public List<SisOpcion> getSisOpcionList() {
        return sisOpcionList;
    }

    public void setSisOpcionList(List<SisOpcion> sisOpcionList) {
        this.sisOpcionList = sisOpcionList;
    }

    public SisOpcion getSisIdeOpci() {
        return sisIdeOpci;
    }

    public void setSisIdeOpci(SisOpcion sisIdeOpci) {
        this.sisIdeOpci = sisIdeOpci;
    }

    public List<SisTabla> getSisTablaList() {
        return sisTablaList;
    }

    public void setSisTablaList(List<SisTabla> sisTablaList) {
        this.sisTablaList = sisTablaList;
    }

    public List<CrtActividad> getCrtActividadList() {
        return crtActividadList;
    }

    public void setCrtActividadList(List<CrtActividad> crtActividadList) {
        this.crtActividadList = crtActividadList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideOpci != null ? ideOpci.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisOpcion)) {
            return false;
        }
        SisOpcion other = (SisOpcion) object;
        if ((this.ideOpci == null && other.ideOpci != null) || (this.ideOpci != null && !this.ideOpci.equals(other.ideOpci))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisOpcion[ ideOpci=" + ideOpci + " ]";
    }
    
}
