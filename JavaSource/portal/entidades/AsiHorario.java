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
@Table(name = "asi_horario", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "AsiHorario.findAll", query = "SELECT a FROM AsiHorario a"),
    @NamedQuery(name = "AsiHorario.findByIdeAshor", query = "SELECT a FROM AsiHorario a WHERE a.ideAshor = :ideAshor"),
    @NamedQuery(name = "AsiHorario.findByIdeSucu", query = "SELECT a FROM AsiHorario a WHERE a.ideSucu = :ideSucu"),
    @NamedQuery(name = "AsiHorario.findByActivoAshor", query = "SELECT a FROM AsiHorario a WHERE a.activoAshor = :activoAshor"),
    @NamedQuery(name = "AsiHorario.findByUsuarioIngre", query = "SELECT a FROM AsiHorario a WHERE a.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "AsiHorario.findByFechaIngre", query = "SELECT a FROM AsiHorario a WHERE a.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "AsiHorario.findByUsuarioActua", query = "SELECT a FROM AsiHorario a WHERE a.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "AsiHorario.findByFechaActua", query = "SELECT a FROM AsiHorario a WHERE a.fechaActua = :fechaActua"),
    @NamedQuery(name = "AsiHorario.findByHoraIngre", query = "SELECT a FROM AsiHorario a WHERE a.horaIngre = :horaIngre"),
    @NamedQuery(name = "AsiHorario.findByHoraActua", query = "SELECT a FROM AsiHorario a WHERE a.horaActua = :horaActua"),
    @NamedQuery(name = "AsiHorario.findByHoraInicialAshor", query = "SELECT a FROM AsiHorario a WHERE a.horaInicialAshor = :horaInicialAshor"),
    @NamedQuery(name = "AsiHorario.findByHoraFinalAshor", query = "SELECT a FROM AsiHorario a WHERE a.horaFinalAshor = :horaFinalAshor")})
public class AsiHorario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_ashor", nullable = false)
    private Integer ideAshor;
    @Column(name = "ide_sucu")
    private Integer ideSucu;
    @Column(name = "activo_ashor")
    private Boolean activoAshor;
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
    @Column(name = "hora_inicial_ashor")
    @Temporal(TemporalType.TIME)
    private Date horaInicialAshor;
    @Column(name = "hora_final_ashor")
    @Temporal(TemporalType.TIME)
    private Date horaFinalAshor;
    @OneToMany(mappedBy = "ideAshor")
    private List<AsiDiaHorario> asiDiaHorarioList;
    @OneToMany(mappedBy = "ideAshor")
    private List<AsiTurnos> asiTurnosList;
    @JoinColumn(name = "ide_asgri", referencedColumnName = "ide_asgri")
    @ManyToOne
    private AsiGrupoIntervalo ideAsgri;

    public AsiHorario() {
    }

    public AsiHorario(Integer ideAshor) {
        this.ideAshor = ideAshor;
    }

    public Integer getIdeAshor() {
        return ideAshor;
    }

    public void setIdeAshor(Integer ideAshor) {
        this.ideAshor = ideAshor;
    }

    public Integer getIdeSucu() {
        return ideSucu;
    }

    public void setIdeSucu(Integer ideSucu) {
        this.ideSucu = ideSucu;
    }

    public Boolean getActivoAshor() {
        return activoAshor;
    }

    public void setActivoAshor(Boolean activoAshor) {
        this.activoAshor = activoAshor;
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

    public Date getHoraInicialAshor() {
        return horaInicialAshor;
    }

    public void setHoraInicialAshor(Date horaInicialAshor) {
        this.horaInicialAshor = horaInicialAshor;
    }

    public Date getHoraFinalAshor() {
        return horaFinalAshor;
    }

    public void setHoraFinalAshor(Date horaFinalAshor) {
        this.horaFinalAshor = horaFinalAshor;
    }

    public List<AsiDiaHorario> getAsiDiaHorarioList() {
        return asiDiaHorarioList;
    }

    public void setAsiDiaHorarioList(List<AsiDiaHorario> asiDiaHorarioList) {
        this.asiDiaHorarioList = asiDiaHorarioList;
    }

    public List<AsiTurnos> getAsiTurnosList() {
        return asiTurnosList;
    }

    public void setAsiTurnosList(List<AsiTurnos> asiTurnosList) {
        this.asiTurnosList = asiTurnosList;
    }

    public AsiGrupoIntervalo getIdeAsgri() {
        return ideAsgri;
    }

    public void setIdeAsgri(AsiGrupoIntervalo ideAsgri) {
        this.ideAsgri = ideAsgri;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAshor != null ? ideAshor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AsiHorario)) {
            return false;
        }
        AsiHorario other = (AsiHorario) object;
        if ((this.ideAshor == null && other.ideAshor != null) || (this.ideAshor != null && !this.ideAshor.equals(other.ideAshor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.AsiHorario[ ideAshor=" + ideAshor + " ]";
    }
    
}
