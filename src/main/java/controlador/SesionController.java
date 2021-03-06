package controlador;

import modelo.Usuarios;
import EJB.SesionFacade;
import controlador.util.JsfUtil;
import java.io.IOException;

import java.io.Serializable;
import java.util.Date;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@Named("sesionController")
@SessionScoped
public class SesionController implements Serializable {

    @EJB
    private SesionFacade ejbFacade;
    private Usuarios logeado = null;
    private boolean estadoLogin = false;
    private String usuario;
    private String password;
    private Date ultimoLogin;
    private String newPassword;

    public SesionController() {
        
    }

    private SesionFacade getFacade() {
        return ejbFacade;
    }
    
    public String getUsuario() {
        return usuario;
    }
    
    public void setUsuario(String nombre) {
        this.usuario = nombre;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String clave) {
        this.password = clave;
    }
    
    public Date getUltimoLogin() {
        return ultimoLogin;
    }
    
    public void setUltimoLogin(Date login) {
        this.ultimoLogin = login;
    }
    
    public Usuarios getLogeado() {
        return this.logeado;
    }
    
    public boolean getEstadoLogin() {
        return this.estadoLogin;
    }
    
    public void setNewPassword(String password) {
        this.newPassword = password;
    }
    
    public String getNewPassword() {
        return this.newPassword;
    }
    
    public void iniciarSesion() throws IOException {
        
        Usuarios usuario = getFacade().comprobarUsuario(this.usuario, this.password);
         
        this.usuario = "";
        this.password = "";
        
        if(usuario == null) {
            JsfUtil.addErrorMessage("Credenciales incorrectas");
            
        } else {
            this.logeado = usuario;
            this.estadoLogin = true;
            this.ultimoLogin = usuario.getUltimaConexion();
            
            getFacade().actualizarUltimaConexion(this.logeado);
            
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect("inicio.xhtml");
        }
    }
    
    public void cerrarSesion() throws IOException {
        
        this.logeado = null;
        this.estadoLogin = false;
        this.newPassword = "";
        
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.invalidate();
            
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect("index.xhtml");
    }
    
    public void actualizarPassword() {
        getFacade().cambiarPassword(this.logeado, this.newPassword);
        JsfUtil.addSuccessMessage("Contrase??a cambiada correctamente");
    }
}
