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
    }

    public void agregarCuenta(){
        //Imprimimos la información del formularioo
        logger.info("Cuenta a guardar: {}", this.cuentaSeleccionada);

        //Si el id del objeto cuentaSeleccionada es nulo,
        // simplemente llamamos al método guardar de nuestro servicio y lo pasamos como parámetro
        if (this.cuentaSeleccionada.getIdCuenta()==null){
            cuentaServicio.guardarCuenta(cuentaSeleccionada);
            //Agregamos la nueva cuenta a nuestra lista para que se actulice la vista sin tener que consultar la base de datos
            cuentas.add(cuentaSeleccionada);
            //Reiniciamos el objeto cuenta vinculado al formulario
            this.cuentaSeleccionada = new Cuenta();
            //Enviamos un mensaje con FacesContext
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Cuenta agregada",
                            "Se ha agregado con éxito la nueva cuenta"));

            //Ocultamos la ventana modal
            PrimeFaces.current().executeScript("PF('ventanaModalCuenta').hide()");

            //Recargamos la tabla
            PrimeFaces.current().ajax().update("cuentas-form:mensajes", "cuentas-form:cuentas-tabla");
        }
    }

}
