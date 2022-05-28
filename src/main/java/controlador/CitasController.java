package controlador;

import modelo.Citas;
import controlador.util.JsfUtil;
import controlador.util.JsfUtil.PersistAction;
import EJB.CitasFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import modelo.Mascotas;
import modelo.Usuarios;

@Named("citasController")
@SessionScoped
public class CitasController implements Serializable {

    @EJB
    private CitasFacade ejbFacade;
    private List<Citas> items = null;
    
    private Citas citaInfo;

    public CitasController() {
        
    }
    
    public Citas getCitaInfo() {
        return this.citaInfo;
    }
    
    public void setCitaInfo(Citas cita) {
        this.citaInfo = cita;
    }
    
    public Citas nuevaCitaInfo() {
        this.citaInfo = new Citas();
        return this.citaInfo;
    }

    private CitasFacade getFacade() {
        return ejbFacade;
    }

    public List<Citas> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
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
    
    public List<Citas> obtenerCitasCliente(Usuarios usuario) {
        
        List<Citas> listaFinal = new ArrayList<>();
        List<Citas> lista = null;
        
        Iterator<Mascotas> itAnimales = usuario.getMascotasList().iterator();
        Mascotas infoAnimales;
        
        while(itAnimales.hasNext()) {
            infoAnimales = itAnimales.next();
            lista = getFacade().citasMascota(infoAnimales);
            
            if (!lista.isEmpty()) {
                listaFinal.addAll(lista);
            }
        }
        
        Collections.sort(listaFinal, (x, y) -> x.getFechaCita().compareTo(y.getFechaCita()));
        return listaFinal;
    }
    
    public void crearNuevaCita() {
        
        if(JsfUtil.fechaValida(this.citaInfo.getFechaCita()) <= 0) {
            
            JsfUtil.addErrorMessage("Fecha introducida es incorrecta");
            this.citaInfo.setFechaCita(null);
            return;
        }
        
        if(this.citaInfo.getDescripcion().length() <= 10) {
            
            JsfUtil.addErrorMessage("Descripción demasiado corta");
            return;
        }
        
        getFacade().create(this.citaInfo); 
        JsfUtil.addSuccessMessage("Cita creada");
        this.items = null;
        this.citaInfo = null;
    }
    
    public void borrarCita(Citas cita) {
        
        getFacade().remove(cita);
        items = null;
        
        JsfUtil.addSuccessMessage("Cita eliminada");
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
