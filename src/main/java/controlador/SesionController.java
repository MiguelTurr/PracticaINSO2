package controlador;

import modelo.Usuarios;
import EJB.SesionFacade;
import controlador.util.JsfUtil;
import java.io.IOException;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named("sesionController")
@SessionScoped
public class SesionController implements Serializable {

    @EJB
    private SesionFacade ejbFacade;
    private Usuarios logeado = null;
    private boolean estadoLogin = false;
    private String usuario;
    private String password;

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
    
    public Usuarios getLogeado() {
        return this.logeado;
    }
    
    public boolean getEstadoLogin() {
        return this.estadoLogin;
    }
    
    public void iniciarSesion() throws IOException {
        
        Usuarios usuario = getFacade().comprobarUsuario(this.usuario, this.password);
        
        if(usuario == null) {
            
            this.usuario = "";
            this.password = "";
            
            JsfUtil.addErrorMessage("Credenciales incorrectas");
            
        } else {
            System.out.println(usuario.getNombre());
            
            this.logeado = usuario;
            this.estadoLogin = true;
            
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect("inicio.xhtml");
        }
    }
    
    public void cerrarSesion() throws IOException {
        this.logeado = null;
        this.estadoLogin = false;
    }
}
