package gm.cuentas.servicio;

import gm.cuentas.modelo.Cuenta;
import gm.cuentas.repositorio.CuentaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CuentaServicio implements ICuentaServicio {

    @Autowired
    private CuentaRepositorio cuentaRepositorio;

    @Override
    public Cuenta buscarCuentaPorId(Integer idCuenta) {
        var cuenta = cuentaRepositorio.findById(idCuenta).orElse(null);
        return cuenta;
    }

    @Override
    public List<Cuenta> listarCuentas() {
       List<Cuenta> cuentas = cuentaRepositorio.findAll();
       return cuentas;
    }

    @Override
    public void guardarCuenta(Cuenta cuenta) {
        cuentaRepositorio.save(cuenta);
    }

    @Override
    public void eliminarCuenta(Integer idCuenta) {
        cuentaRepositorio.deleteById(idCuenta);
    }
}
