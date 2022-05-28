package controlador;

import modelo.Facturas;
import controlador.util.JsfUtil;
import controlador.util.JsfUtil.PersistAction;
import EJB.FacturasFacade;

import java.io.Serializable;
import java.text.DecimalFormat;
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
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import modelo.Infofacturas;
import modelo.Usuarios;

@Named("facturasController")
@SessionScoped
public class FacturasController implements Serializable {

    @EJB
    private FacturasFacade ejbFacade;
    private List<Facturas> items = null;
    private Facturas selected;
    
    private Facturas facturaInfo;
    private List<Facturas> facturasDelMes;
    private boolean facturasCliente = false;
    private double gananciasMes;

    public FacturasController() {
    }

    public Facturas getSelected() {
        return selected;
    }

    public void setSelected(Facturas selected) {
        this.selected = selected;
    }
    
    public boolean getFacturasCliente() {
        return this.facturasCliente;
    }
    
    public void setFacturasCliente(boolean tiene) {
        this.facturasCliente = tiene;
    }
    
    public List<Facturas> getFacturasDelMes() {
        return this.facturasDelMes;
    }
    
    public void setFacturaInfo(Facturas factura) {
        this.facturaInfo = factura;
    }
    
    public Facturas getFacturaInfo() {
        return this.facturaInfo;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private FacturasFacade getFacade() {
        return ejbFacade;
    }

    public Facturas prepareCreate() {
        selected = new Facturas();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("FacturasCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("FacturasUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("FacturasDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Facturas> getItems() {
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

    public Facturas getFacturas(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Facturas> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Facturas> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }
    
    public int itemsTotalFactura(Facturas factura) {
        int elementos = 0;
        
        List<Infofacturas> lista = factura.getInfofacturasList();
        Iterator<Infofacturas> it = lista.iterator();
        Infofacturas info;
        
        while(it.hasNext()) {
            info = it.next();
            elementos += info.getCantidad();
        }
        return elementos;
    }
    
    public String costeTotalFactura(Facturas factura) {
        double coste = 0.0;
        
        List<Infofacturas> lista = factura.getInfofacturasList();
        Iterator<Infofacturas> it = lista.iterator();
        Infofacturas info;
        
        while(it.hasNext()) {
            info = it.next();
            coste += info.getPrecio() * info.getCantidad();
        }
        
        if(Double.compare(coste, 0.0) == 0) {
            return "0,00€";
            
        } else {
            DecimalFormat numberFormat = new DecimalFormat("#.00");
            return numberFormat.format(coste)+"€";
        } 
    }
    
    public List<Facturas> obtenerFacturas(Usuarios usuario) {
       
        List<Facturas> lista = getFacade().facturasCliente(usuario); 
        
        this.facturasCliente = !lista.isEmpty();
        
        return lista;
    }
    
    public void descargarFactura(Facturas factura) {
        System.out.println("Quieres descargar una factura: "+factura);
    }
    
    public List<Facturas> facturasDelMes() {
        
        List<Facturas> listaFinal = new ArrayList<>();
        List<Facturas> lista = getItems();

        if(lista != null) {

            Iterator<Facturas> itFacturas = lista.iterator();
            Facturas infoFacturas;

            Date fecha = new Date();

            while (itFacturas.hasNext()) {
                infoFacturas = itFacturas.next();

                if (infoFacturas.getFechaFactura().getMonth() == fecha.getMonth()
                        && infoFacturas.getFechaFactura().getYear() == fecha.getYear()
                        && !infoFacturas.getInfofacturasList().isEmpty()) {

                    listaFinal.add(infoFacturas);
                }
            }
        }        
        
        this.facturasDelMes = listaFinal;
        System.out.println("Facturas totales: " + listaFinal.size());
        
        return listaFinal;
    }

    @FacesConverter(forClass = Facturas.class)
    public static class FacturasControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            FacturasController controller = (FacturasController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "facturasController");
            return controller.getFacturas(getKey(value));
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
            if (object instanceof Facturas) {
                Facturas o = (Facturas) object;
                return getStringKey(o.getIdFactura());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Facturas.class.getName()});
                return null;
            }
        }

    }

}
