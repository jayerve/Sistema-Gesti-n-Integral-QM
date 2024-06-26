/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author awbecerra
 */
@Entity
@Table(name = "clasificador_presupuestario", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "ClasificadorPresupuestario.findAll", query = "SELECT c FROM ClasificadorPresupuestario c"),
    @NamedQuery(name = "ClasificadorPresupuestario.findByIdeClasifPresupuest", query = "SELECT c FROM ClasificadorPresupuestario c WHERE c.ideClasifPresupuest = :ideClasifPresupuest"),
    @NamedQuery(name = "ClasificadorPresupuestario.findByCodigo", query = "SELECT c FROM ClasificadorPresupuestario c WHERE c.codigo = :codigo"),
    @NamedQuery(name = "ClasificadorPresupuestario.findByNombre", query = "SELECT c FROM ClasificadorPresupuestario c WHERE c.nombre = :nombre")})
public class ClasificadorPresupuestario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ide_clasif_presupuest", nullable = false)
    private Integer ideClasifPresupuest;
    @Size(max = 50)
    @Column(name = "codigo", length = 50)
    private String codigo;
    @Size(max = 400)
    @Column(name = "nombre", length = 400)
    private String nombre;

    public ClasificadorPresupuestario() {
    }

    public ClasificadorPresupuestario(Integer ideClasifPresupuest) {
        this.ideClasifPresupuest = ideClasifPresupuest;
    }

    public Integer getIdeClasifPresupuest() {
        return ideClasifPresupuest;
    }

    public void setIdeClasifPresupuest(Integer ideClasifPresupuest) {
        this.ideClasifPresupuest = ideClasifPresupuest;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideClasifPresupuest != null ? ideClasifPresupuest.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClasificadorPresupuestario)) {
            return false;
        }
        ClasificadorPresupuestario other = (ClasificadorPresupuestario) object;
        if ((this.ideClasifPresupuest == null && other.ideClasifPresupuest != null) || (this.ideClasifPresupuest != null && !this.ideClasifPresupuest.equals(other.ideClasifPresupuest))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.ClasificadorPresupuestario[ ideClasifPresupuest=" + ideClasifPresupuest + " ]";
    }
    
}
