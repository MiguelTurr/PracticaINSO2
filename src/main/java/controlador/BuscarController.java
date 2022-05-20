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
    
    public List<Usuarios> getBuscarClientes() {
        return this.buscarClientes;
    }
    
    public void buscarClienteNombre() {
        buscarClientes = getFacade().buscarClienteNombre(this.textoBuscarCliente);
        
        if(buscarClientes.isEmpty() == true) {
            this.textoBuscarCliente = "";
            JsfUtil.addErrorMessage("No se ha encontrado ningún resultado");
        }
    }
    
    public void verPerfil(Usuarios usuario) {
        System.out.println("Ver perfil de :" +usuario.getNombre());
    }
    
    public void buscarMascotaNombre() {
        System.out.println("Quiero buscar una mascota: "+this.textoBuscarMascota);
        //buscarClientes = getFacade().buscarClienteNombre(this.textoBuscarCliente);
    }
}
