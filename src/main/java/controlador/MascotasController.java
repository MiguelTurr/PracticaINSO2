package controlador;

import modelo.Mascotas;
import controlador.util.JsfUtil;
import controlador.util.JsfUtil.PersistAction;
import EJB.MascotasFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import modelo.Animales;
import modelo.Usuarios;

@Named("mascotasController")
@SessionScoped
public class MascotasController implements Serializable {

    @EJB
    private MascotasFacade ejbFacade;
    private List<Mascotas> items = null;
    private Mascotas selected;
    
    private Animales mascotaTipo;
    private String mascotaNombre;

    public MascotasController() {
    }

    public Mascotas getSelected() {
        return selected;
    }

    public void setSelected(Mascotas selected) {
        this.selected = selected;
    }
    
    public Animales getMascotaTipo() {
        return this.mascotaTipo;
    }
    
    public void setMascotaTipo(Animales tipo) {
        this.mascotaTipo = tipo;
    }
    
    public String getMascotaNombre() {
        return this.mascotaNombre;
    }
    
    public void setMascotaNombre(String nombre) {
        this.mascotaNombre = nombre;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private MascotasFacade getFacade() {
        return ejbFacade;
    }

    public Mascotas prepareCreate() {
        selected = new Mascotas();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("MascotasCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("MascotasUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("MascotasDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Mascotas> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Mascotas getMascotas(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Mascotas> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Mascotas> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }
    
    public void crearNuevaMascota(Usuarios usuario) {
        System.out.println("Quiero crear una mascota: "+ mascotaTipo.getTipoAnimal());
        System.out.println("de: "+ usuario.getNombre());
    }
    
    public void borrarMascota(Mascotas mascota) {
        System.out.println("Quiero borrar una mascota: "+mascota.getNombre());
    }

    @FacesConverter(forClass = Mascotas.class)
    public static class MascotasControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MascotasController controller = (MascotasController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mascotasController");
            return controller.getMascotas(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Mascotas) {
                Mascotas o = (Mascotas) object;
                return getStringKey(o.getIdMascota());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Mascotas.class.getName()});
                return null;
            }
        }

    }

}
