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
@Table(name = "spr_base_postulante", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SprBasePostulante.findAll", query = "SELECT s FROM SprBasePostulante s"),
    @NamedQuery(name = "SprBasePostulante.findByIdeSpbap", query = "SELECT s FROM SprBasePostulante s WHERE s.ideSpbap = :ideSpbap"),
    @NamedQuery(name = "SprBasePostulante.findByApellidoNombreSpbap", query = "SELECT s FROM SprBasePostulante s WHERE s.apellidoNombreSpbap = :apellidoNombreSpbap"),
    @NamedQuery(name = "SprBasePostulante.findByDocumentoIdentidadSpbap", query = "SELECT s FROM SprBasePostulante s WHERE s.documentoIdentidadSpbap = :documentoIdentidadSpbap"),
    @NamedQuery(name = "SprBasePostulante.findByTelefonoConvenSpbap", query = "SELECT s FROM SprBasePostulante s WHERE s.telefonoConvenSpbap = :telefonoConvenSpbap"),
    @NamedQuery(name = "SprBasePostulante.findByTelefonoCelularSpbap", query = "SELECT s FROM SprBasePostulante s WHERE s.telefonoCelularSpbap = :telefonoCelularSpbap"),
    @NamedQuery(name = "SprBasePostulante.findByDireccionSpbap", query = "SELECT s FROM SprBasePostulante s WHERE s.direccionSpbap = :direccionSpbap"),
    @NamedQuery(name = "SprBasePostulante.findByCorreoSpbap", query = "SELECT s FROM SprBasePostulante s WHERE s.correoSpbap = :correoSpbap"),
    @NamedQuery(name = "SprBasePostulante.findByFechaIngresoSpbap", query = "SELECT s FROM SprBasePostulante s WHERE s.fechaIngresoSpbap = :fechaIngresoSpbap"),
    @NamedQuery(name = "SprBasePostulante.findByFechaNacimientoSpbap", query = "SELECT s FROM SprBasePostulante s WHERE s.fechaNacimientoSpbap = :fechaNacimientoSpbap"),
    @NamedQuery(name = "SprBasePostulante.findByPathFotoSpbap", query = "SELECT s FROM SprBasePostulante s WHERE s.pathFotoSpbap = :pathFotoSpbap"),
    @NamedQuery(name = "SprBasePostulante.findByActivoSpbap", query = "SELECT s FROM SprBasePostulante s WHERE s.activoSpbap = :activoSpbap"),
    @NamedQuery(name = "SprBasePostulante.findByUsuarioIngre", query = "SELECT s FROM SprBasePostulante s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SprBasePostulante.findByFechaIngre", query = "SELECT s FROM SprBasePostulante s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SprBasePostulante.findByHoraIngre", query = "SELECT s FROM SprBasePostulante s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SprBasePostulante.findByUsuarioActua", query = "SELECT s FROM SprBasePostulante s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SprBasePostulante.findByFechaActua", query = "SELECT s FROM SprBasePostulante s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SprBasePostulante.findByHoraActua", query = "SELECT s FROM SprBasePostulante s WHERE s.horaActua = :horaActua")})
public class SprBasePostulante implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_spbap", nullable = false)
    private Integer ideSpbap;
    @Size(max = 100)
    @Column(name = "apellido_nombre_spbap", length = 100)
    private String apellidoNombreSpbap;
    @Size(max = 15)
    @Column(name = "documento_identidad_spbap", length = 15)
    private String documentoIdentidadSpbap;
    @Size(max = 20)
    @Column(name = "telefono_conven_spbap", length = 20)
    private String telefonoConvenSpbap;
    @Size(max = 20)
    @Column(name = "telefono_celular_spbap", length = 20)
    private String telefonoCelularSpbap;
    @Size(max = 500)
    @Column(name = "direccion_spbap", length = 500)
    private String direccionSpbap;
    @Size(max = 100)
    @Column(name = "correo_spbap", length = 100)
    private String correoSpbap;
    @Column(name = "fecha_ingreso_spbap")
    @Temporal(TemporalType.DATE)
    private Date fechaIngresoSpbap;
    @Column(name = "fecha_nacimiento_spbap")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimientoSpbap;
    @Size(max = 100)
    @Column(name = "path_foto_spbap", length = 100)
    private String pathFotoSpbap;
    @Column(name = "activo_spbap")
    private Boolean activoSpbap;
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
    @OneToMany(mappedBy = "ideSpbap")
    private List<SprArchivoPostulante> sprArchivoPostulanteList;
    @OneToMany(mappedBy = "ideSpbap")
    private List<SprCargoAplica> sprCargoAplicaList;
    @OneToMany(mappedBy = "ideSpbap")
    private List<SprInformatica> sprInformaticaList;
    @OneToMany(mappedBy = "ideSpbap")
    private List<SprExperienciaLaboral> sprExperienciaLaboralList;
    @OneToMany(mappedBy = "ideSpbap")
    private List<SprPostulante> sprPostulanteList;
    @OneToMany(mappedBy = "ideSpbap")
    private List<SprEstudios> sprEstudiosList;
    @JoinColumn(name = "ide_gttdi", referencedColumnName = "ide_gttdi")
    @ManyToOne
    private GthTipoDocumentoIdentidad ideGttdi;
    @JoinColumn(name = "ide_gtnac", referencedColumnName = "ide_gtnac")
    @ManyToOne
    private GthNacionalidad ideGtnac;
    @JoinColumn(name = "ide_gtgen", referencedColumnName = "ide_gtgen")
    @ManyToOne
    private GthGenero ideGtgen;
    @OneToMany(mappedBy = "ideSpbap")
    private List<SprIdioma> sprIdiomaList;
    @OneToMany(mappedBy = "ideSpbap")
    private List<SprCapacitacion> sprCapacitacionList;
    @OneToMany(mappedBy = "ideSpbap")
    private List<SprReferencias> sprReferenciasList;

    public SprBasePostulante() {
    }

    public SprBasePostulante(Integer ideSpbap) {
        this.ideSpbap = ideSpbap;
    }

    public Integer getIdeSpbap() {
        return ideSpbap;
    }

    public void setIdeSpbap(Integer ideSpbap) {
        this.ideSpbap = ideSpbap;
    }

    public String getApellidoNombreSpbap() {
        return apellidoNombreSpbap;
    }

    public void setApellidoNombreSpbap(String apellidoNombreSpbap) {
        this.apellidoNombreSpbap = apellidoNombreSpbap;
    }

    public String getDocumentoIdentidadSpbap() {
        return documentoIdentidadSpbap;
    }

    public void setDocumentoIdentidadSpbap(String documentoIdentidadSpbap) {
        this.documentoIdentidadSpbap = documentoIdentidadSpbap;
    }

    public String getTelefonoConvenSpbap() {
        return telefonoConvenSpbap;
    }

    public void setTelefonoConvenSpbap(String telefonoConvenSpbap) {
        this.telefonoConvenSpbap = telefonoConvenSpbap;
    }

    public String getTelefonoCelularSpbap() {
        return telefonoCelularSpbap;
    }

    public void setTelefonoCelularSpbap(String telefonoCelularSpbap) {
        this.telefonoCelularSpbap = telefonoCelularSpbap;
    }

    public String getDireccionSpbap() {
        return direccionSpbap;
    }

    public void setDireccionSpbap(String direccionSpbap) {
        this.direccionSpbap = direccionSpbap;
    }

    public String getCorreoSpbap() {
        return correoSpbap;
    }

    public void setCorreoSpbap(String correoSpbap) {
        this.correoSpbap = correoSpbap;
    }

    public Date getFechaIngresoSpbap() {
        return fechaIngresoSpbap;
    }

    public void setFechaIngresoSpbap(Date fechaIngresoSpbap) {
        this.fechaIngresoSpbap = fechaIngresoSpbap;
    }

    public Date getFechaNacimientoSpbap() {
        return fechaNacimientoSpbap;
    }

    public void setFechaNacimientoSpbap(Date fechaNacimientoSpbap) {
        this.fechaNacimientoSpbap = fechaNacimientoSpbap;
    }

    public String getPathFotoSpbap() {
        return pathFotoSpbap;
    }

    public void setPathFotoSpbap(String pathFotoSpbap) {
        this.pathFotoSpbap = pathFotoSpbap;
    }

    public Boolean getActivoSpbap() {
        return activoSpbap;
    }

    public void setActivoSpbap(Boolean activoSpbap) {
        this.activoSpbap = activoSpbap;
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

    public List<SprArchivoPostulante> getSprArchivoPostulanteList() {
        return sprArchivoPostulanteList;
    }

    public void setSprArchivoPostulanteList(List<SprArchivoPostulante> sprArchivoPostulanteList) {
        this.sprArchivoPostulanteList = sprArchivoPostulanteList;
    }

    public List<SprCargoAplica> getSprCargoAplicaList() {
        return sprCargoAplicaList;
    }

    public void setSprCargoAplicaList(List<SprCargoAplica> sprCargoAplicaList) {
        this.sprCargoAplicaList = sprCargoAplicaList;
    }

    public List<SprInformatica> getSprInformaticaList() {
        return sprInformaticaList;
    }

    public void setSprInformaticaList(List<SprInformatica> sprInformaticaList) {
        this.sprInformaticaList = sprInformaticaList;
    }

    public List<SprExperienciaLaboral> getSprExperienciaLaboralList() {
        return sprExperienciaLaboralList;
    }

    public void setSprExperienciaLaboralList(List<SprExperienciaLaboral> sprExperienciaLaboralList) {
        this.sprExperienciaLaboralList = sprExperienciaLaboralList;
    }

    public List<SprPostulante> getSprPostulanteList() {
        return sprPostulanteList;
    }

    public void setSprPostulanteList(List<SprPostulante> sprPostulanteList) {
        this.sprPostulanteList = sprPostulanteList;
    }

    public List<SprEstudios> getSprEstudiosList() {
        return sprEstudiosList;
    }

    public void setSprEstudiosList(List<SprEstudios> sprEstudiosList) {
        this.sprEstudiosList = sprEstudiosList;
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

    public List<SprIdioma> getSprIdiomaList() {
        return sprIdiomaList;
    }

    public void setSprIdiomaList(List<SprIdioma> sprIdiomaList) {
        this.sprIdiomaList = sprIdiomaList;
    }

    public List<SprCapacitacion> getSprCapacitacionList() {
        return sprCapacitacionList;
    }

    public void setSprCapacitacionList(List<SprCapacitacion> sprCapacitacionList) {
        this.sprCapacitacionList = sprCapacitacionList;
    }

    public List<SprReferencias> getSprReferenciasList() {
        return sprReferenciasList;
    }

    public void setSprReferenciasList(List<SprReferencias> sprReferenciasList) {
        this.sprReferenciasList = sprReferenciasList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSpbap != null ? ideSpbap.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SprBasePostulante)) {
            return false;
        }
        SprBasePostulante other = (SprBasePostulante) object;
        if ((this.ideSpbap == null && other.ideSpbap != null) || (this.ideSpbap != null && !this.ideSpbap.equals(other.ideSpbap))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SprBasePostulante[ ideSpbap=" + ideSpbap + " ]";
    }
    
}
