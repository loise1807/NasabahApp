package springboot.pactindo.app.model;

import lombok.NoArgsConstructor;
import lombok.Data;

import javax.persistence.*;


@Data
@NoArgsConstructor
@Entity
@Table(name = "NASABAH", schema = "public")
public class Nasabah {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator_nasabah_id_seq")
    @SequenceGenerator(name = "generator_nasabah_id_seq", sequenceName = "nasabah_id_seq",schema = "public"
    ,allocationSize = 1)
    private Long id;

    @Column(unique = true, name="nik")
    private String nik;

    @Column(name="nama")
    private String  nama;

    @Column(name="domisili")
    private String  domisili;

    @Column(unique = true, name="nomor_telp")
    private String noTelp;

    @Column(unique = true, name="nomor_rekening")
    private String noRekening;

    @Column(name="saldo")
    private Long saldo;

    @Column(name="status")
    private String  status;


}
