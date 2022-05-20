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
import modelo.Roles;
import modelo.Usuarios;

/**
 *
 * @author crest
 */

@Stateless
public class BuscarFacade {

    @PersistenceContext(unitName = "VeterinariaPersistencia")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }
    
    public List<Usuarios> buscarClienteNombre(String nombre) {
        
        Roles cliente = (Roles) em.createNamedQuery("Roles.findByIdRol").setParameter("idRol", 3).getResultList().get(0);
        return em.createNamedQuery("Usuarios.buscarCliente").setParameter("usuario", "%"+nombre+"%").setParameter("rol", cliente).getResultList();
    }
}
