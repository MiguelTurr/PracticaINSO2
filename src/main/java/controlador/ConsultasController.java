package controlador;

import modelo.Consultas;
import controlador.util.JsfUtil;
import controlador.util.JsfUtil.PersistAction;
import EJB.ConsultasFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import modelo.Mascotas;
import modelo.Usuarios;
import org.primefaces.event.RateEvent;

@Named("consultasController")
@SessionScoped
public class ConsultasController implements Serializable {

    @EJB
    private ConsultasFacade ejbFacade;
    private List<Consultas> items = null;
    private Consultas selected;
    
    private String consultaInfo;
    private Consultas rateConsulta;

    public ConsultasController() {
    }

    public Consultas getSelected() {
        return selected;
    }

    public void setSelected(Consultas selected) {
        this.selected = selected;
    }
    
    public String getConsultaInfo() {
        return this.consultaInfo;
    }
    
    public void setConsultaInfo(String descripcion) {
        this.consultaInfo = descripcion;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ConsultasFacade getFacade() {
        return ejbFacade;
    }

    public Consultas prepareCreate() {
        selected = new Consultas();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ConsultasCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ConsultasUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ConsultasDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Consultas> getItems() {
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

    public Consultas getConsultas(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Consultas> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Consultas> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }
    
    public List<Consultas> ordernarConsultas(Mascotas mascota) {
 
        List<Consultas> lista = getFacade().obtenerConsultasMascota(mascota);
        return lista;
    }
    
    public void descripcionConsultaLength(FacesContext context, UIComponent comp, Object value){

        String mno = (String) value;

        if(mno.length() < 15) {
            ((UIInput) comp).setValid(false);
            JsfUtil.addErrorMessage("Esa celda necesita más de 15 caracteres");
        }
    }
    
    
    public void crearNuevaConsulta(Mascotas mascota, Usuarios empleado) {
        Consultas consulta = new Consultas();
        
        consulta.setFechaConsulta(new Date());
        consulta.setDescripcion(this.consultaInfo);
        consulta.setIdMascota(mascota);
        consulta.setIdEmpleado(empleado);
        
        getFacade().create(consulta); 
        mascota.getConsultasList().add(consulta);
        JsfUtil.addSuccessMessage("Se ha creado una nueva consulta");
    }
    
    public void valorarConsulta(RateEvent<Integer> rateEvent) {
        
        rateConsulta.setValoracion(rateEvent.getRating());
        getFacade().edit(rateConsulta);
        JsfUtil.addSuccessMessage("Has valorado la consulta");
    }
    
    public void valorarConsulta(Consultas consulta) {
        rateConsulta = consulta;
    }
    
    public int obtenerValoracionEmpleado(Usuarios empleado) {
        int valoracion = 0;
        int total = 0;
        List<Consultas> lista = getFacade().findAll();
        Consultas comparar;
        
        for(int i = 0; i < lista.size(); i++) {
            comparar = lista.get(i);
            
            if(comparar.getIdEmpleado().equals(empleado)) {
                valoracion += comparar.getValoracion();
                total ++;
            }
        }
        
        if(total > 1) {
            valoracion = valoracion / total;
        }
        
        return valoracion;
    }

    @FacesConverter(forClass = Consultas.class)
    public static class ConsultasControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ConsultasController controller = (ConsultasController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "consultasController");
            return controller.getConsultas(getKey(value));
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
            if (object instanceof Consultas) {
                Consultas o = (Consultas) object;
                return getStringKey(o.getIdConsulta());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Consultas.class.getName()});
                return null;
            }
        }

    }

}
