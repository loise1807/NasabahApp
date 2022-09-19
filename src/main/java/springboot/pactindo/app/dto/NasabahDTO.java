package springboot.pactindo.app.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
public class NasabahDTO{
    private Long id;

    private String nik;

    private String  nama;

    private String  domisili;

    private String noTelp;

    private String noRekening;

    private Long saldo;

    private String  status;
}
