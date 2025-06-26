package gm.cuentas.modelo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TipoCuenta {
    AHORRO("Ahorro"),
    CREDITO("Crédito"),
    INVERSION("Inversión");

    final String displayName;
}
