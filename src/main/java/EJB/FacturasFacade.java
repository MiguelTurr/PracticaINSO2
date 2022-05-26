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
import modelo.Facturas;
import modelo.Usuarios;

/**
 *
 * @author crest
 */
@Stateless
public class FacturasFacade extends AbstractFacade<Facturas> {

    @PersistenceContext(unitName = "VeterinariaPersistencia")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FacturasFacade() {
        super(Facturas.class);
    }
    
    public List<Facturas> facturasCliente(Usuarios usuario) {
        
        return em.createNamedQuery("Facturas.facturasCliente").setParameter("usuario", usuario).getResultList();
    }
    
}
