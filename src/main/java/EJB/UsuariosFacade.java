/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EJB;

import java.util.Date;
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
public class UsuariosFacade extends AbstractFacade<Usuarios> {

    @PersistenceContext(unitName = "VeterinariaPersistencia")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuariosFacade() {
        super(Usuarios.class);
    }
    
    public Usuarios buscarID() {
        return (Usuarios) em.createNamedQuery("Usuarios.findByIdUsuario").setParameter("idUsuario", 1).getResultList().get(0);
    }
    
    public void crearNuevoCliente(Usuarios usuario) {
        
        Roles rol = (Roles) em.createNamedQuery("Roles.findByIdRol").setParameter("idRol", 3).getResultList().get(0);
        usuario.setIdRol(rol);
        
        em.persist(usuario);
    }
}
