package controlador;

import modelo.Citas;
import controlador.util.JsfUtil;
import controlador.util.JsfUtil.PersistAction;
import EJB.CitasFacade;

import java.io.Serializable;
import java.util.Date;
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
import modelo.Mascotas;

@Named("citasController")
@SessionScoped
public class CitasController implements Serializable {

    @EJB
    private CitasFacade ejbFacade;
    private List<Citas> items = null;
    private Citas selected;
    
    private Date citaFecha;
    private String citaDesc;
    private Mascotas citaMascota;

    public CitasController() {
        
    }

    public Citas getSelected() {
        return selected;
    }

    public void setSelected(Citas selected) {
        this.selected = selected;
    }
    
    public Date getCitaFecha() {
        return citaFecha;
    }
    
    public void setCitaFecha(Date date) {
        this.citaFecha = date;
    }
    
    public String getCitaDesc() {
        return citaDesc;
    }
    
    public void setCitaDesc(String desc) {
        this.citaDesc = desc;
    }
    
    public Mascotas getCitaMascota() {
        return citaMascota;
    }
    
    public void setCitaMascota(Mascotas mascota) {
        this.citaMascota = mascota;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private CitasFacade getFacade() {
        return ejbFacade;
    }

    public Citas prepareCreate() {
        selected = new Citas();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CitasCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("CitasUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CitasDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Citas> getItems() {
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

    public Citas getCitas(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Citas> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Citas> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }
    
    public List<Citas> obtenerCitasValidas() {
        
        List<Citas> lista = getFacade().citasValidas();
        return lista;
    }
    
    public List<Citas> obtenerCitasMascota(Mascotas mascota) {
 
        List<Citas> lista = getFacade().citasMascota(mascota);
        return lista;
    }
    
    public void crearNuevaCita() {
        
        if(JsfUtil.fechaValida(citaFecha) <= 0) {
            
            JsfUtil.addErrorMessage("Fecha introducida es incorrecta");
            this.citaFecha = null;
            return;
        }
        
        if(this.citaDesc.length() <= 10) {
            
            JsfUtil.addErrorMessage("Descripción demasiado corta");
            this.citaDesc = "";
            return;
        }

        Citas cita = new Citas();
        cita.setFechaCita(this.citaFecha);
        cita.setDescripcion(this.citaDesc);
        cita.setIdMascota(this.citaMascota);

        getFacade().crearNuevaCita(cita); 
    }

    @FacesConverter(forClass = Citas.class)
    public static class CitasControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CitasController controller = (CitasController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "citasController");
            return controller.getCitas(getKey(value));
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
            if (object instanceof Citas) {
                Citas o = (Citas) object;
                return getStringKey(o.getIdCita());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Citas.class.getName()});
                return null;
            }
        }

    }

}
