package gm.cuentas.modelo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCuenta;

    private String nombre;
    @Enumerated(EnumType.STRING)
    private TipoCuenta tipoCuenta;
    private Double saldo;
}
