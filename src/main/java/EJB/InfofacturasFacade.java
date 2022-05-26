/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EJB;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import modelo.Infofacturas;

/**
 *
 * @author crest
 */
@Stateless
public class InfofacturasFacade extends AbstractFacade<Infofacturas> {

    @PersistenceContext(unitName = "VeterinariaPersistencia")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public InfofacturasFacade() {
        super(Infofacturas.class);
    }
    
}
