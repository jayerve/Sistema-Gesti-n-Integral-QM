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
public class GthHobiePK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gthob", nullable = false)
    private int ideGthob;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gtemp", nullable = false)
    private int ideGtemp;

    public GthHobiePK() {
    }

    public GthHobiePK(int ideGthob, int ideGtemp) {
        this.ideGthob = ideGthob;
        this.ideGtemp = ideGtemp;
    }

    public int getIdeGthob() {
        return ideGthob;
    }

    public void setIdeGthob(int ideGthob) {
        this.ideGthob = ideGthob;
    }

    public int getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(int ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) ideGthob;
        hash += (int) ideGtemp;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthHobiePK)) {
            return false;
        }
        GthHobiePK other = (GthHobiePK) object;
        if (this.ideGthob != other.ideGthob) {
            return false;
        }
        if (this.ideGtemp != other.ideGtemp) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthHobiePK[ ideGthob=" + ideGthob + ", ideGtemp=" + ideGtemp + " ]";
    }
    
}
