/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author awbecerra
 */
@Embeddable
public class GenPeriodoPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gemes", nullable = false)
    private int ideGemes;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_geani", nullable = false)
    private int ideGeani;

    public GenPeriodoPK() {
    }

    public GenPeriodoPK(int ideGemes, int ideGeani) {
        this.ideGemes = ideGemes;
        this.ideGeani = ideGeani;
    }

    public int getIdeGemes() {
        return ideGemes;
    }

    public void setIdeGemes(int ideGemes) {
        this.ideGemes = ideGemes;
    }

    public int getIdeGeani() {
        return ideGeani;
    }

    public void setIdeGeani(int ideGeani) {
        this.ideGeani = ideGeani;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) ideGemes;
        hash += (int) ideGeani;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenPeriodoPK)) {
            return false;
        }
        GenPeriodoPK other = (GenPeriodoPK) object;
        if (this.ideGemes != other.ideGemes) {
            return false;
        }
        if (this.ideGeani != other.ideGeani) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenPeriodoPK[ ideGemes=" + ideGemes + ", ideGeani=" + ideGeani + " ]";
    }
    
}
