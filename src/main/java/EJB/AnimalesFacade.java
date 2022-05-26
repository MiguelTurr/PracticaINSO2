/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EJB;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import modelo.Animales;

/**
 *
 * @author crest
 */
@Stateless
public class AnimalesFacade extends AbstractFacade<Animales> {

    @PersistenceContext(unitName = "VeterinariaPersistencia")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AnimalesFacade() {
        super(Animales.class);
    }
    
}
