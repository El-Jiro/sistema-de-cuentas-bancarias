package gm.cuentas.controlador;

import gm.cuentas.modelo.Cuenta;
import gm.cuentas.servicio.CuentaServicio;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@ViewScoped
public class IndexControlador {

    //Inyectamos una instancia de la clase de servicio
    @Autowired
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private CuentaServicio cuentaServicio;

    //Creamos una lista para guardar los objetos de tipo cuenta obtenidos de la base de datos
    @Setter(AccessLevel.NONE)
    private List<Cuenta> cuentas;
    //Creamos un objeto logger para mandar información a la consola;
    private static final Logger logger = LoggerFactory.getLogger(IndexControlador.class);
    //Creamos un salto de línea
    private static final String nl = System.lineSeparator();

    /*
    * Creamos un método inicial y le añadimos la anotación PostConstruct, es decir que se invocará
    * automáticamente después de que se haya construido la instancia correspondiente a esta clase*/

    @PostConstruct
    public void init(){
        recuperarDatos();
    }

    public void recuperarDatos(){
        cuentas = cuentaServicio.listarCuentas();
        logger.info(nl);
        cuentas.forEach((cuenta -> logger.info(cuenta.toString())));
    }

}
