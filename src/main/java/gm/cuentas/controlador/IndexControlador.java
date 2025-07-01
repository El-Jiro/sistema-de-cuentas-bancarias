package gm.cuentas.controlador;

import gm.cuentas.modelo.Cuenta;
import gm.cuentas.modelo.TipoCuenta;
import gm.cuentas.servicio.CuentaServicio;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import lombok.*;
import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@ViewScoped
public class IndexControlador {

    //Inyectamos una instancia de la clase de servicio
    @Autowired
    private CuentaServicio cuentaServicio;

    //Creamos una lista para guardar los objetos de tipo cuenta obtenidos de la base de datos
    @Setter(AccessLevel.NONE)
    @Getter
    private List<Cuenta> cuentas;
    //Creamos un objeto de tipo cuenta sin inicializar
    @Getter
    @Setter
    private Cuenta cuentaSeleccionada;
    @Getter
    private List<TipoCuenta> opciones;
    //Creamos un objeto logger para mandar información a la consola;
    private static final Logger logger = LoggerFactory.getLogger(IndexControlador.class);
    //Creamos un salto de línea
    private static final String nl = System.lineSeparator();

    /*
    * Creamos un método inicial y le añadimos la anotación PostConstruct, es decir que se invocará
    * automáticamente después de que se haya construido la instancia correspondiente a esta clase*/

    @PostConstruct
    public void init(){
        cuentaSeleccionada = new Cuenta();
        opciones = Arrays.asList(TipoCuenta.values());
        recuperarDatos();
    }

    public void recuperarDatos(){
        cuentas = cuentaServicio.listarCuentas();
        logger.info(nl);
        cuentas.forEach((cuenta -> logger.info(cuenta.toString())));
        logger.info(nl);
    }

    public void guardarCuenta(){
        //Imprimimos la información del formulario
        logger.info("Cuenta a guardar: {}", cuentaSeleccionada.toString() + nl);

        //----------------------------------CASO DE AGREGAR------------------------------------
        //Si el id del objeto cuentaSeleccionada es nulo,
        // simplemente llamamos al método guardar de nuestro servicio y lo pasamos como parámetro
        if (this.cuentaSeleccionada.getIdCuenta() == null){
            cuentaServicio.guardarCuenta(cuentaSeleccionada);
            //Agregamos la nueva cuenta a nuestra lista para que se actulice la vista sin tener que consultar la base de datos
            cuentas.add(cuentaSeleccionada);
            //Reiniciamos el objeto cuenta vinculado al formulario
            this.cuentaSeleccionada = new Cuenta();
            notificarYActualizar("Cuenta agregada", "La nueva cuenta se ha agregado con éxito",
                    "PF('ventanaModalCuenta').hide()");
        } else {
            //----------------CASO DE ACTUALIZAR------------------
            //Simplemente guardamos el objeto de nuestro formulario sobreescribiendo la información
            this.cuentaServicio.guardarCuenta(this.cuentaSeleccionada);

            //Guardamos el id en una variable
            Integer idCuenta = this.cuentaSeleccionada.getIdCuenta();

            //Reiniciamos el formulario
            this.cuentaSeleccionada = new Cuenta();

            //Notificamos al usuario y actualizamos la vista
            notificarYActualizar("Cuenta modificada", "Se ha actualizado correctamente la información de la cuenta con el id: " + idCuenta,
                    "PF('ventanaModalCuenta').hide()");


        }

    }

    public void eliminarCuenta(){
        //Obtenemos el id del objeto enviado en el formulario
        Integer idCuenta = this.cuentaSeleccionada.getIdCuenta();
        //lo imprimimos
        logger.info("ID de la cuenta a eliminar: {}", idCuenta);
        //Eliminamos el registro de la base de datos
        this.cuentaServicio.eliminarCuenta(idCuenta);
        //Eliminamos el objeto correspondiente de nuestra Lista
        Cuenta cuentaEliminada = this.buscarCuentaPorId(idCuenta);
        logger.info("Objeto a eliminar de la lista: {}", cuentaEliminada);
        this.cuentas.remove(cuentaEliminada);
        //Reiniciamos el id del formulario
        this.cuentaSeleccionada.setIdCuenta(null);

        notificarYActualizar("Cuenta Eliminada", "Se ha eliminado con éxito la cuenta con el id: "
                + idCuenta, "PF('eliminarCuentaVentana').hide()");
    }

    //Creamos un método para buscar el objeto cuenta en la lista mediante su id
    private Cuenta buscarCuentaPorId(int id){
        Cuenta cuenta = this.cuentas.stream().filter(c->c.getIdCuenta().
                        equals(id)).findFirst().orElse(null);

        return cuenta;
    }

    private void notificarYActualizar(String asuntoMensaje, String cuerpoMensaje, String pfScript){
        //Enviamos un mensaje con FacesContext
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(asuntoMensaje, cuerpoMensaje));
        //Ocultamos la ventana modal
        PrimeFaces.current().executeScript(pfScript);
        //Recargamos la tabla
        PrimeFaces.current().ajax().update("cuentas-form:mensajes", "cuentas-form:cuentas-tabla");
    }
}
