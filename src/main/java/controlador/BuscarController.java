package controlador;

import modelo.Usuarios;
import EJB.BuscarFacade;
import controlador.util.JsfUtil;
import java.io.IOException;

import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import modelo.Mascotas;

@Named("buscarController")
@SessionScoped
public class BuscarController implements Serializable {

    @EJB
    private BuscarFacade ejbFacade;
    private String textoBuscarCliente;
    private String textoBuscarMascota;
    private List<Usuarios> buscarClientes = null;
    private List<Mascotas> buscarMascotas = null;
    
   private Usuarios verPerfilCliente;
   private Mascotas verPerfilMascota;

    public BuscarController() {
        
    }

    private BuscarFacade getFacade() {
        return ejbFacade;
    }
    
    public void setTextoBuscarCliente(String texto) {
        this.textoBuscarCliente = texto;
    }
    
    public String getTextoBuscarCliente() {
        return this.textoBuscarCliente;
    }
    
    public void setTextoBuscarMascota(String texto) {
        this.textoBuscarMascota = texto;
    }
    
    public String getTextoBuscarMascota() {
        return this.textoBuscarMascota;
    }
    
    public List<Mascotas> getBuscarMascotas() {
        return this.buscarMascotas;
    }
    
    public List<Usuarios> getBuscarClientes() {
        return this.buscarClientes;
    }
    
    public Usuarios getVerPerfilCliente() {
        return this.verPerfilCliente;
    }
    
    public Mascotas getVerPerfilMascota() {
        return this.verPerfilMascota;
    }
    
    public void setVerPerfilCliente(Usuarios perfil) {
        this.verPerfilCliente = perfil;
    }
    public void setVerPerfilMascota(Mascotas perfil) {
        this.verPerfilMascota = perfil;
    }
    
    public void buscarClienteNombre() {
        buscarClientes = getFacade().buscarClienteNombre(this.textoBuscarCliente);
        
        if(buscarClientes.isEmpty() == true) {
            this.textoBuscarCliente = "";
            JsfUtil.addErrorMessage("No se ha encontrado ningún resultado");
        }
    }
    
    public void verPerfil(Usuarios usuario) throws IOException {
        System.out.println("Ver perfil de cliente:" +usuario.getNombre());
        
        this.verPerfilCliente = usuario;
        
        // Ir al perfil cliente
            
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect("perfilCliente.xhtml");
    }
    
    public void buscarMascotaNombre() {
        buscarMascotas = getFacade().buscarMascotaNombre(this.textoBuscarMascota);
        
        if(buscarMascotas.isEmpty() == true) {
            this.textoBuscarMascota = "";
            JsfUtil.addErrorMessage("No se ha encontrado ningún resultado");
        }
    }
    
    public void verPerfil(Mascotas mascota) throws IOException {
        System.out.println("Ver perfil de mascota :" +mascota.getNombre());
        
        // Ir al perfil cliente
            
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect("perfilMascota.xhtml");
    }
}
