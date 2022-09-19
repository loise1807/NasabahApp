package springboot.pactindo.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import springboot.pactindo.app.model.Nasabah;

public interface NasabahRepository extends JpaRepository <Nasabah, Long> {

    Nasabah findByNik(String nik);
    Nasabah findByNoRekening(String noRekening);

}
