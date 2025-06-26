package gm.cuentas.servicio;

import gm.cuentas.modelo.Cuenta;

import java.util.List;

public interface ICuentaServicio {

    Cuenta buscarCuentaPorId(Integer idCuenta);

    List<Cuenta> listarCuentas();

    void guardarCuenta(Cuenta cuenta);

    void eliminarCuenta(Integer idCuenta);
}
