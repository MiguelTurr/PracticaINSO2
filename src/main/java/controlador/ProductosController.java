package controlador;

import modelo.Productos;
import controlador.util.JsfUtil;
import controlador.util.JsfUtil.PersistAction;
import EJB.ProductosFacade;

import java.io.Serializable;
import java.util.ArrayList;
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

@Named("productosController")
@SessionScoped
public class ProductosController implements Serializable {

    @EJB
    private ProductosFacade ejbFacade;
    private List<Productos> items = null;
    
    private Productos productoInfo;

    public ProductosController() {
    }
    
    public void setProductoInfo(Productos producto) {
        this.productoInfo = producto;
    }
    
    public Productos getProductoInfo() {
        return this.productoInfo;
    }

    public Productos nuevoProductoInfo() {
        this.productoInfo = new Productos();
        return this.productoInfo;
    }
    
    public Productos editarProductoInfo(Productos edit) {
        this.productoInfo = edit;
        return this.productoInfo;
    }

    private ProductosFacade getFacade() {
        return ejbFacade;
    }

    public List<Productos> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public Productos getProductos(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Productos> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Productos> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }
    
    public void comprobarNombreProducto(FacesContext context, UIComponent comp, Object value){

        String mno = (String) value;

        if(mno.length() < 3) {
            ((UIInput) comp).setValid(false);
            JsfUtil.addErrorMessage("Esa celda necesita más de 3 caracteres");
        }
    }
    
    public void comprobarCantidadProducto(FacesContext context, UIComponent comp, Object value){

        int valor = (int) value;
        if(valor < -1) {
            ((UIInput) comp).setValid(false);
            JsfUtil.addErrorMessage("No puedes poner ese stock");
        }
    }
    
    public void comprobarPrecioProducto(FacesContext context, UIComponent comp, Object value){

        float valor = (float) value;

        if(valor <= 0.0) {
            ((UIInput) comp).setValid(false);
            JsfUtil.addErrorMessage("No puedes poner ese precio");
        }
    }
    
    public void comprobarDescuentoProducto(FacesContext context, UIComponent comp, Object value){

        float valor = (float) value;

        if(valor < 0.0) {
            ((UIInput) comp).setValid(false);
            JsfUtil.addErrorMessage("No puedes poner ese precio");
        }
    }
    
    public void borrarProducto(Productos producto) {
        
        if(producto.getCantidad() > 0) {
            JsfUtil.addErrorMessage("Ese producto tiene stock en la tienda");
            return;
        }
        
        getFacade().remove(producto);
        items = null;
        
        JsfUtil.addSuccessMessage("Producto eliminado");
    }
    
    public void crearProducto() {
        
        this.productoInfo.setDescuento(0.0f);
        
        getFacade().create(this.productoInfo);
        this.items = null;
        this.productoInfo = null;
        
        JsfUtil.addSuccessMessage("Producto creado");
    }
    
    public void editarProducto() {
        
        getFacade().edit(this.productoInfo);
        JsfUtil.addSuccessMessage("Producto editado");
        
        this.productoInfo = null;
    }
    
    public void actualizarStock(Productos producto, int cantidad) {
        
        int stock = producto.getCantidad();
        
        if(stock != -1) {
            producto.setCantidad(stock - cantidad);
            getFacade().edit(producto);
            this.items = null;
        }
    }
    
    public List<Productos> getProductosDisponibles() {
        List<Productos> lista = getFacade().findAll();
        List<Productos> listaFinal = new ArrayList<>();
        Productos prod;
        Iterator<Productos> it = lista.iterator();
        
        while(it.hasNext()) {
            prod = it.next();
            if(prod.getCantidad() != 0) {
                listaFinal.add(prod);
            }
        }
        
        return listaFinal;
    }

    @FacesConverter(forClass = Productos.class)
    public static class ProductosControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProductosController controller = (ProductosController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "productosController");
            return controller.getProductos(getKey(value));
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
            if (object instanceof Productos) {
                Productos o = (Productos) object;
                return getStringKey(o.getIdProducto());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Productos.class.getName()});
                return null;
            }
        }

    }

}
