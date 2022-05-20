/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EJB;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import modelo.Consultas;
import modelo.Mascotas;

/**
 *
 * @author crest
 */
@Stateless
public class ConsultasFacade extends AbstractFacade<Consultas> {

    @PersistenceContext(unitName = "VeterinariaPersistencia")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConsultasFacade() {
        super(Consultas.class);
    }
    
    public List<Consultas> obtenerConsultasMascota(Mascotas mascota) {
        
        return em.createNamedQuery("Consultas.buscarConsultaAnimal").setParameter("idMascota", mascota).getResultList();
    }
    
}
