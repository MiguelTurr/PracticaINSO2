/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EJB;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import modelo.Citas;

/**
 *
 * @author crest
 */
@Stateless
public class CitasFacade extends AbstractFacade<Citas> {

    @PersistenceContext(unitName = "VeterinariaPersistencia")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CitasFacade() {
        super(Citas.class);
    }
    
    public List<Citas> citasValidas() {
        return em.createNamedQuery("Citas.citasValidas").setParameter("tiempo", new Date()).getResultList();
    }
    
    public boolean crearCita(Citas cita) {
        
        
        return true;
    }
}
