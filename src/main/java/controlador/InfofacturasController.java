package controlador;

import modelo.Infofacturas;
import controlador.util.JsfUtil;
import controlador.util.JsfUtil.PersistAction;
import EJB.InfofacturasFacade;

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
import modelo.Facturas;

@Named("infofacturasController")
@SessionScoped
public class InfofacturasController implements Serializable {

    @EJB
    private InfofacturasFacade ejbFacade;
    private List<Infofacturas> items = null;
    private Infofacturas selected;
    private Infofacturas infoFacturasInfo;

    public InfofacturasController() {
    }

    public Infofacturas getSelected() {
        return selected;
    }

    public void setSelected(Infofacturas selected) {
        this.selected = selected;
    }
    
    public void setInfoFacturasInfo(Infofacturas info) {
        this.infoFacturasInfo = info;
    }
    
    public Infofacturas getInfoFacturasInfo() {
        return this.infoFacturasInfo;
    }
    
    public void nuevaInfoFacturasInfo() {
        this.infoFacturasInfo = new Infofacturas();
    }
    
    public void crearNuevaInfofactura(Facturas factura) {
        
        int cantidadElegida = this.infoFacturasInfo.getCantidad();
        
        if(cantidadElegida <= 0) {
            JsfUtil.addErrorMessage("Esa cantidad no es válida");
            return;
        }
        
        int cantidad = this.infoFacturasInfo.getIdProducto().getCantidad();
        
        if(cantidad == -1) {
            this.infoFacturasInfo.setCantidad(1);
        }
        
        if(cantidad != -1 && cantidadElegida > cantidad) {
            JsfUtil.addErrorMessage("No hay tanta cantidad de ese producto");
            return;
        }
        
        // Descuento
        
        float descuento = this.infoFacturasInfo.getIdProducto().getDescuento();
        float precioFinal = this.infoFacturasInfo.getIdProducto().getPrecio();
        
        if(descuento != 0.0) {
            precioFinal = precioFinal * (descuento / 100);
        }
        
        //
        
        this.infoFacturasInfo.setPrecio(precioFinal);
        this.infoFacturasInfo.setIdFactura(factura);
        
        //
        
        factura.getInfofacturasList().add(this.infoFacturasInfo);
        getFacade().create(this.infoFacturasInfo);
        
        JsfUtil.addSuccessMessage("Añadido producto a la factura");
        
        //
        
        items = null;
        this.infoFacturasInfo = new Infofacturas();
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private InfofacturasFacade getFacade() {
        return ejbFacade;
    }

    public Infofacturas prepareCreate() {
        selected = new Infofacturas();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("InfofacturasCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("InfofacturasUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("InfofacturasDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Infofacturas> getItems() {
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

    public Infofacturas getInfofacturas(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Infofacturas> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Infofacturas> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Infofacturas.class)
    public static class InfofacturasControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            InfofacturasController controller = (InfofacturasController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "infofacturasController");
            return controller.getInfofacturas(getKey(value));
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
            if (object instanceof Infofacturas) {
                Infofacturas o = (Infofacturas) object;
                return getStringKey(o.getIdInfo());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Infofacturas.class.getName()});
                return null;
            }
        }

    }

}
