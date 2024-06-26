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
@Table(name = "sri_configuracion_general", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SriConfiguracionGeneral.findAll", query = "SELECT s FROM SriConfiguracionGeneral s"),
    @NamedQuery(name = "SriConfiguracionGeneral.findByIdeSrcog", query = "SELECT s FROM SriConfiguracionGeneral s WHERE s.ideSrcog = :ideSrcog"),
    @NamedQuery(name = "SriConfiguracionGeneral.findByUsuarioIngre", query = "SELECT s FROM SriConfiguracionGeneral s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SriConfiguracionGeneral.findByFechaIngre", query = "SELECT s FROM SriConfiguracionGeneral s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SriConfiguracionGeneral.findByHoraIngre", query = "SELECT s FROM SriConfiguracionGeneral s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SriConfiguracionGeneral.findByUsuarioActua", query = "SELECT s FROM SriConfiguracionGeneral s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SriConfiguracionGeneral.findByFechaActua", query = "SELECT s FROM SriConfiguracionGeneral s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SriConfiguracionGeneral.findByHoraActua", query = "SELECT s FROM SriConfiguracionGeneral s WHERE s.horaActua = :horaActua"),
    @NamedQuery(name = "SriConfiguracionGeneral.findByFechaInicioRepreSrcog", query = "SELECT s FROM SriConfiguracionGeneral s WHERE s.fechaInicioRepreSrcog = :fechaInicioRepreSrcog"),
    @NamedQuery(name = "SriConfiguracionGeneral.findByFechaFinRepreSrcog", query = "SELECT s FROM SriConfiguracionGeneral s WHERE s.fechaFinRepreSrcog = :fechaFinRepreSrcog"),
    @NamedQuery(name = "SriConfiguracionGeneral.findByActivoRepreSrcog", query = "SELECT s FROM SriConfiguracionGeneral s WHERE s.activoRepreSrcog = :activoRepreSrcog"),
    @NamedQuery(name = "SriConfiguracionGeneral.findByRazonSocialSrcog", query = "SELECT s FROM SriConfiguracionGeneral s WHERE s.razonSocialSrcog = :razonSocialSrcog"),
    @NamedQuery(name = "SriConfiguracionGeneral.findByDocumentoRepreSrcog", query = "SELECT s FROM SriConfiguracionGeneral s WHERE s.documentoRepreSrcog = :documentoRepreSrcog"),
    @NamedQuery(name = "SriConfiguracionGeneral.findByPrimerNombreContaSrcog", query = "SELECT s FROM SriConfiguracionGeneral s WHERE s.primerNombreContaSrcog = :primerNombreContaSrcog"),
    @NamedQuery(name = "SriConfiguracionGeneral.findBySegundoNombreContaSrcog", query = "SELECT s FROM SriConfiguracionGeneral s WHERE s.segundoNombreContaSrcog = :segundoNombreContaSrcog"),
    @NamedQuery(name = "SriConfiguracionGeneral.findByApellidoPaternoContaSrcog", query = "SELECT s FROM SriConfiguracionGeneral s WHERE s.apellidoPaternoContaSrcog = :apellidoPaternoContaSrcog"),
    @NamedQuery(name = "SriConfiguracionGeneral.findByApellidoMaternoContaSrcog", query = "SELECT s FROM SriConfiguracionGeneral s WHERE s.apellidoMaternoContaSrcog = :apellidoMaternoContaSrcog"),
    @NamedQuery(name = "SriConfiguracionGeneral.findByFechaInicioContaSrcog", query = "SELECT s FROM SriConfiguracionGeneral s WHERE s.fechaInicioContaSrcog = :fechaInicioContaSrcog"),
    @NamedQuery(name = "SriConfiguracionGeneral.findByFechaFinContaSrcog", query = "SELECT s FROM SriConfiguracionGeneral s WHERE s.fechaFinContaSrcog = :fechaFinContaSrcog"),
    @NamedQuery(name = "SriConfiguracionGeneral.findByActivoContaSrcog", query = "SELECT s FROM SriConfiguracionGeneral s WHERE s.activoContaSrcog = :activoContaSrcog"),
    @NamedQuery(name = "SriConfiguracionGeneral.findByDocumentoContaSrcog", query = "SELECT s FROM SriConfiguracionGeneral s WHERE s.documentoContaSrcog = :documentoContaSrcog"),
    @NamedQuery(name = "SriConfiguracionGeneral.findByPathFirmaRepreSrcog", query = "SELECT s FROM SriConfiguracionGeneral s WHERE s.pathFirmaRepreSrcog = :pathFirmaRepreSrcog"),
    @NamedQuery(name = "SriConfiguracionGeneral.findByPathFirmaContaSrcog", query = "SELECT s FROM SriConfiguracionGeneral s WHERE s.pathFirmaContaSrcog = :pathFirmaContaSrcog")})
public class SriConfiguracionGeneral implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_srcog", nullable = false)
    private Integer ideSrcog;
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
    @Column(name = "fecha_inicio_repre_srcog")
    @Temporal(TemporalType.DATE)
    private Date fechaInicioRepreSrcog;
    @Column(name = "fecha_fin_repre_srcog")
    @Temporal(TemporalType.DATE)
    private Date fechaFinRepreSrcog;
    @Column(name = "activo_repre_srcog")
    private Boolean activoRepreSrcog;
    @Size(max = 100)
    @Column(name = "razon_social_srcog", length = 100)
    private String razonSocialSrcog;
    @Size(max = 15)
    @Column(name = "documento_repre_srcog", length = 15)
    private String documentoRepreSrcog;
    @Size(max = 20)
    @Column(name = "primer_nombre_conta_srcog", length = 20)
    private String primerNombreContaSrcog;
    @Size(max = 20)
    @Column(name = "segundo_nombre_conta_srcog", length = 20)
    private String segundoNombreContaSrcog;
    @Size(max = 20)
    @Column(name = "apellido_paterno_conta_srcog", length = 20)
    private String apellidoPaternoContaSrcog;
    @Size(max = 20)
    @Column(name = "apellido_materno_conta_srcog", length = 20)
    private String apellidoMaternoContaSrcog;
    @Column(name = "fecha_inicio_conta_srcog")
    @Temporal(TemporalType.DATE)
    private Date fechaInicioContaSrcog;
    @Column(name = "fecha_fin_conta_srcog")
    @Temporal(TemporalType.DATE)
    private Date fechaFinContaSrcog;
    @Column(name = "activo_conta_srcog")
    private Boolean activoContaSrcog;
    @Size(max = 15)
    @Column(name = "documento_conta_srcog", length = 15)
    private String documentoContaSrcog;
    @Size(max = 100)
    @Column(name = "path_firma_repre_srcog", length = 100)
    private String pathFirmaRepreSrcog;
    @Size(max = 100)
    @Column(name = "path_firma_conta_srcog", length = 100)
    private String pathFirmaContaSrcog;
    @JoinColumn(name = "ide_geani", referencedColumnName = "ide_geani")
    @ManyToOne
    private GenAnio ideGeani;

    public SriConfiguracionGeneral() {
    }

    public SriConfiguracionGeneral(Integer ideSrcog) {
        this.ideSrcog = ideSrcog;
    }

    public Integer getIdeSrcog() {
        return ideSrcog;
    }

    public void setIdeSrcog(Integer ideSrcog) {
        this.ideSrcog = ideSrcog;
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

    public Date getFechaInicioRepreSrcog() {
        return fechaInicioRepreSrcog;
    }

    public void setFechaInicioRepreSrcog(Date fechaInicioRepreSrcog) {
        this.fechaInicioRepreSrcog = fechaInicioRepreSrcog;
    }

    public Date getFechaFinRepreSrcog() {
        return fechaFinRepreSrcog;
    }

    public void setFechaFinRepreSrcog(Date fechaFinRepreSrcog) {
        this.fechaFinRepreSrcog = fechaFinRepreSrcog;
    }

    public Boolean getActivoRepreSrcog() {
        return activoRepreSrcog;
    }

    public void setActivoRepreSrcog(Boolean activoRepreSrcog) {
        this.activoRepreSrcog = activoRepreSrcog;
    }

    public String getRazonSocialSrcog() {
        return razonSocialSrcog;
    }

    public void setRazonSocialSrcog(String razonSocialSrcog) {
        this.razonSocialSrcog = razonSocialSrcog;
    }

    public String getDocumentoRepreSrcog() {
        return documentoRepreSrcog;
    }

    public void setDocumentoRepreSrcog(String documentoRepreSrcog) {
        this.documentoRepreSrcog = documentoRepreSrcog;
    }

    public String getPrimerNombreContaSrcog() {
        return primerNombreContaSrcog;
    }

    public void setPrimerNombreContaSrcog(String primerNombreContaSrcog) {
        this.primerNombreContaSrcog = primerNombreContaSrcog;
    }

    public String getSegundoNombreContaSrcog() {
        return segundoNombreContaSrcog;
    }

    public void setSegundoNombreContaSrcog(String segundoNombreContaSrcog) {
        this.segundoNombreContaSrcog = segundoNombreContaSrcog;
    }

    public String getApellidoPaternoContaSrcog() {
        return apellidoPaternoContaSrcog;
    }

    public void setApellidoPaternoContaSrcog(String apellidoPaternoContaSrcog) {
        this.apellidoPaternoContaSrcog = apellidoPaternoContaSrcog;
    }

    public String getApellidoMaternoContaSrcog() {
        return apellidoMaternoContaSrcog;
    }

    public void setApellidoMaternoContaSrcog(String apellidoMaternoContaSrcog) {
        this.apellidoMaternoContaSrcog = apellidoMaternoContaSrcog;
    }

    public Date getFechaInicioContaSrcog() {
        return fechaInicioContaSrcog;
    }

    public void setFechaInicioContaSrcog(Date fechaInicioContaSrcog) {
        this.fechaInicioContaSrcog = fechaInicioContaSrcog;
    }

    public Date getFechaFinContaSrcog() {
        return fechaFinContaSrcog;
    }

    public void setFechaFinContaSrcog(Date fechaFinContaSrcog) {
        this.fechaFinContaSrcog = fechaFinContaSrcog;
    }

    public Boolean getActivoContaSrcog() {
        return activoContaSrcog;
    }

    public void setActivoContaSrcog(Boolean activoContaSrcog) {
        this.activoContaSrcog = activoContaSrcog;
    }

    public String getDocumentoContaSrcog() {
        return documentoContaSrcog;
    }

    public void setDocumentoContaSrcog(String documentoContaSrcog) {
        this.documentoContaSrcog = documentoContaSrcog;
    }

    public String getPathFirmaRepreSrcog() {
        return pathFirmaRepreSrcog;
    }

    public void setPathFirmaRepreSrcog(String pathFirmaRepreSrcog) {
        this.pathFirmaRepreSrcog = pathFirmaRepreSrcog;
    }

    public String getPathFirmaContaSrcog() {
        return pathFirmaContaSrcog;
    }

    public void setPathFirmaContaSrcog(String pathFirmaContaSrcog) {
        this.pathFirmaContaSrcog = pathFirmaContaSrcog;
    }

    public GenAnio getIdeGeani() {
        return ideGeani;
    }

    public void setIdeGeani(GenAnio ideGeani) {
        this.ideGeani = ideGeani;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSrcog != null ? ideSrcog.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SriConfiguracionGeneral)) {
            return false;
        }
        SriConfiguracionGeneral other = (SriConfiguracionGeneral) object;
        if ((this.ideSrcog == null && other.ideSrcog != null) || (this.ideSrcog != null && !this.ideSrcog.equals(other.ideSrcog))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SriConfiguracionGeneral[ ideSrcog=" + ideSrcog + " ]";
    }

}
