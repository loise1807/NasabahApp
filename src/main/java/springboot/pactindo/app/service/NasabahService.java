package springboot.pactindo.app.service;

import springboot.pactindo.app.dto.SaldoDTO;
import springboot.pactindo.app.dto.StatusDTO;
import springboot.pactindo.app.model.Nasabah;

import java.util.List;

public interface NasabahService {

    Nasabah create(Nasabah nasabah);
    Nasabah update(Long id, Nasabah nasabah);
    boolean delete(Long id);
    Nasabah findById(Long id);
    List<Nasabah> findAll();
    Nasabah findByNik(String nik);
    Nasabah findByNoRekening(String noRekening);

    Nasabah setorTunai(Long id, SaldoDTO saldoDTO);
    Nasabah tarikTunai(Long id, SaldoDTO saldoDTO);

    Nasabah approvedRegister(Long id, StatusDTO statusDTO);
    Nasabah rejectedRegister(Long id, StatusDTO statusDTO);

}
