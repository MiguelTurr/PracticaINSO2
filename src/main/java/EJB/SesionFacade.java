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
import modelo.Usuarios;

/**
 *
 * @author crest
 */
@Stateless
public class SesionFacade {

    @PersistenceContext(unitName = "VeterinariaPersistencia")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }
    
    public Usuarios comprobarUsuario(String nombre, String clave) {
        List<Usuarios> lista = em.createNamedQuery("Usuarios.checkUsuario").setParameter("usuario", nombre).setParameter("password", clave).getResultList();
        Usuarios usuario = null;
        
        if(lista.isEmpty() == false) {
            usuario = (Usuarios) lista.get(0);
        }
        
        return usuario;
    }
    
    public void actualizarUltimaConexion(Usuarios usuario) {
        
        Usuarios actualizar = (Usuarios) em.createNamedQuery("Usuarios.findByIdUsuario").setParameter("idUsuario", usuario.getIdUsuario()).getResultList().get(0);
        actualizar.setUltimaConexion(new Date());
        
        em.merge(actualizar);
        em.flush();
        em.refresh(actualizar);
    }
    
    public void cambiarPassword(Usuarios usuario, String newPassword) {  
        
        Usuarios actualizar = (Usuarios) em.createNamedQuery("Usuarios.findByIdUsuario").setParameter("idUsuario", usuario.getIdUsuario()).getResultList().get(0);
        actualizar.setPassword(newPassword);

        em.merge(actualizar);
        em.flush();
        em.refresh(actualizar);
    }
}
