package controlador;

import modelo.Usuarios;
import controlador.util.JsfUtil;
import controlador.util.JsfUtil.PersistAction;
import EJB.UsuariosFacade;
import java.io.IOException;

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
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("usuariosController")
@SessionScoped
public class UsuariosController implements Serializable {

    @EJB
    private UsuariosFacade ejbFacade;
    private List<Usuarios> items = null;
    private Usuarios selected;
    
    private String clienteNombre;
    private String clienteApellido1;
    private String clienteApellido2;
    private String clienteDni;
    private String clienteTelefono;
    private Date clienteFecha;
    private String clienteUsuario;
    private String clientePassword;
    
    private Usuarios usuarioCreado = null;

    public UsuariosController() {
    }
    
    public String getClienteNombre() {
        return this.clienteNombre;
    }
    public String getClienteApellido1() {
        return this.clienteApellido1;
    }
    public String getClienteApellido2() {
        return this.clienteApellido2;
    }
    public String getClienteDni() {
        return this.clienteDni;
    }
    public String getClienteTelefono() {
        return this.clienteTelefono;
    }
    public Date getClienteFecha() {
        return this.clienteFecha;
    }
    public String getClienteUsuario() {
        return this.clienteUsuario;
    }
    public String getClientePassword() {
        return this.clientePassword;
    }
    public Usuarios getUsuarioCreado() {
        return this.usuarioCreado;
    }
    
    public void setClienteNombre(String nombre) {
        this.clienteNombre = nombre;
    }
    public void setClienteApellido1(String apellido) {
        this.clienteApellido1 = apellido;
    }
    public void setClienteApellido2(String apellido) {
        this.clienteApellido2 = apellido;
    }
    public void setClienteDni(String dni) {
        this.clienteDni = dni;
    }
    public void setClienteTelefono(String telefono) {
        this.clienteTelefono = telefono;
    }
    public void setClienteFecha(Date fecha) {
        this.clienteFecha = fecha;
    }
    public void setClienteUsuario(String user) {
        this.clienteUsuario = user;
    }
    public void setClientePassword(String password) {
        this.clientePassword = password;
    }
    public void setUsuarioCreado(Usuarios user) {
        this.usuarioCreado = user;
    }

    public Usuarios getSelected() {
        return selected;
    }

    public void setSelected(Usuarios selected) {
        this.selected = selected;
    }
  
    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private UsuariosFacade getFacade() {
        return ejbFacade;
    }

    public Usuarios prepareCreate() {
        selected = new Usuarios();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("UsuariosCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("UsuariosUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("UsuariosDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Usuarios> getItems() {
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

    public Usuarios getUsuarios(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Usuarios> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Usuarios> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }
    
    public void sinNumeros(FacesContext context, UIComponent comp, Object value) {

        String mno = (String) value;

        if (mno.matches(".*[0-9].*")) {
            ((UIInput) comp).setValid(false);
            JsfUtil.addErrorMessage("Esa celda no puede contener números");
        }
    }
    
    public void comprobarDNI(FacesContext context, UIComponent comp, Object value) {
        
        String DIGITO_CONTROL = "TRWAGMYFPDXBNJZSQVHLCKE";
        String mno = (String) value;
        
        if(!mno.matches("[0-9]{8}[A-Z]")
        || mno.charAt(8) != DIGITO_CONTROL.charAt(Integer.parseInt(mno.substring(0, 8)) % 23)) {
            ((UIInput) comp).setValid(false);
            JsfUtil.addErrorMessage("El DNI no es correcto");
        }
    }
    
    public void crearCliente() throws IOException {
 
        Usuarios nuevoUsuario = new Usuarios();

        //
        
        nuevoUsuario.setNombre(clienteNombre);
        nuevoUsuario.setApellido1(clienteApellido1);
        nuevoUsuario.setApellido2(clienteApellido2);
        nuevoUsuario.setDni(clienteDni);
        nuevoUsuario.setTelefono(clienteTelefono);
        nuevoUsuario.setFechaNacimiento(clienteFecha);
        nuevoUsuario.setCreadoEn(new Date());
        nuevoUsuario.setUltimaConexion(new Date());

        // Creación Usuario
        nuevoUsuario.setUsuario(clienteUsuario);
        nuevoUsuario.setPassword(clientePassword);

        //
        getFacade().crearNuevoCliente(nuevoUsuario);
        JsfUtil.addSuccessMessage("Se ha creado el nuevo cliente");

        this.usuarioCreado = nuevoUsuario;
        
        // Reset
        
        clienteNombre = "";
        clienteApellido1 = "";
        clienteApellido2 = "";
        clienteDni = "";
        clienteTelefono = "";
        clienteFecha = null;
        clienteUsuario = "";
        clientePassword = "";
    }
    
    public void borrarCliente(Usuarios usuario) throws IOException {
        getFacade().remove(usuario);
        
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect("inicio.xhtml");
    }

    @FacesConverter(forClass = Usuarios.class)
    public static class UsuariosControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UsuariosController controller = (UsuariosController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "usuariosController");
            return controller.getUsuarios(getKey(value));
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
            if (object instanceof Usuarios) {
                Usuarios o = (Usuarios) object;
                return getStringKey(o.getIdUsuario());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Usuarios.class.getName()});
                return null;
            }
        }

    }

}
