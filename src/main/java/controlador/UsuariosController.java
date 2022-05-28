package controlador;

import modelo.Usuarios;
import controlador.util.JsfUtil;
import EJB.UsuariosFacade;
import java.io.IOException;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import modelo.Roles;

@Named("usuariosController")
@SessionScoped
public class UsuariosController implements Serializable {

    @EJB
    private UsuariosFacade ejbFacade;
    private List<Usuarios> items = null;
    
    private Usuarios usuarioInfo;
    private Usuarios usuarioCreado = null;
    
    public Usuarios nuevoUsuarioInfo() throws IOException {
        this.usuarioInfo = new Usuarios();
        
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect("crearCliente.xhtml");
        
        return this.usuarioInfo;
    }
    
    public Usuarios nuevoUsuarioInfoAdmin() {
        this.usuarioInfo = new Usuarios();
        return this.usuarioInfo;
    }
    
    public Usuarios editarUsuarioInfo(Usuarios user) {
        this.usuarioInfo = user;
        return this.usuarioInfo;
    }
    
    public void setUsuarioInfo(Usuarios user) {
        this.usuarioInfo = user;
    }
    
    public Usuarios getUsuarioInfo() {
        return this.usuarioInfo;
    }

    public UsuariosController() {
    }
  
    public void setUsuarioCreado(Usuarios user) {
        this.usuarioCreado = user;
    }
  
    public Usuarios getUsuarioCreado() {
        return this.usuarioCreado;
    }

    private UsuariosFacade getFacade() {
        return ejbFacade;
    }

    public List<Usuarios> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
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
    
    public void sinNumerosAndSize(FacesContext context, UIComponent comp, Object value) {

        String mno = (String) value;

        if(mno.length() < 3) {
            ((UIInput) comp).setValid(false);
            JsfUtil.addErrorMessage("Esa celda necesita más de 3 caracteres");
            
        } else if (mno.matches(".*[0-9].*")) {
            ((UIInput) comp).setValid(false);
            JsfUtil.addErrorMessage("Esa celda no puede contener números");
        }
    }
    
    public void sinNumeros(FacesContext context, UIComponent comp, Object value) {

        String mno = (String) value;

        if (mno.matches(".*[0-9].*")) {
            ((UIInput) comp).setValid(false);
            JsfUtil.addErrorMessage("Esa celda no puede contener números");
        }
    }
    
    public void sizeString(FacesContext context, UIComponent comp, Object value){

        String mno = (String) value;

        if(mno.length() < 3) {
            ((UIInput) comp).setValid(false);
            JsfUtil.addErrorMessage("Esa celda necesita más de 3 caracteres");
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
    
    public void comprobarTelefono(FacesContext context, UIComponent comp, Object value) {
        
        String mno = (String) value;
        
        if(!mno.matches("[0-9]{9}")) {
            ((UIInput) comp).setValid(false);
            JsfUtil.addErrorMessage("El número de teléfono no es correcto");
        }
    }
    
    public void comprobarFecha(FacesContext context, UIComponent comp, Object value) {
        Date fecha = (Date) value;
        Date fechaValida = new Date(104, 1, 1);
        
        if(fechaValida.compareTo(fecha) < 0) {
            ((UIInput) comp).setValid(false);
            JsfUtil.addErrorMessage("Debe tener más de 18 años");
        }
    }
    
    public void crearUsuario(Roles rol) throws IOException {
        
        this.usuarioInfo.setCreadoEn(new Date());
        this.usuarioInfo.setUltimaConexion(new Date());
        this.usuarioInfo.setIdRol(rol);
        
        getFacade().create(this.usuarioInfo);
        JsfUtil.addSuccessMessage("Se ha creado el nuevo cliente");

        this.usuarioCreado = this.usuarioInfo;
        
        this.items = null;
        this.usuarioInfo = new Usuarios();
        this.usuarioInfo.setIdRol(this.usuarioCreado.getIdRol());
    }
    
    public void crearUsuarioAdmin() throws IOException {
        
        this.usuarioInfo.setCreadoEn(new Date());
        this.usuarioInfo.setUltimaConexion(new Date());
        
        getFacade().create(this.usuarioInfo);
        JsfUtil.addSuccessMessage("Se ha creado un nuevo usuario");
        
        this.items = null;
        this.usuarioInfo = null;
    }
    
    public void editarUsuario() {
        getFacade().edit(this.usuarioInfo);
        JsfUtil.addSuccessMessage("Has editado al usuario");
        
        this.usuarioInfo = null;
    }
    
    public void borrarCliente(Usuarios usuario) throws IOException {
        getFacade().remove(usuario);
        items = null;
        
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect("inicio.xhtml");
    }
    
    public void borrarUsuario(Usuarios usuario, Usuarios admin) throws IOException {
        if(usuario.equals(admin)) {
            JsfUtil.addErrorMessage("No puedes borrar ese usuario");
            return;
        }
        
        getFacade().remove(usuario);
        items = null;
        
        JsfUtil.addSuccessMessage("Usuario eliminado");
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
