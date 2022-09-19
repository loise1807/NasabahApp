package springboot.pactindo.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springboot.pactindo.app.dto.SaldoDTO;
import springboot.pactindo.app.dto.StatusDTO;
import springboot.pactindo.app.model.Nasabah;
import springboot.pactindo.app.repository.NasabahRepository;

import java.util.List;
import java.util.Optional;

@Service
public class NasabahServiceImpl implements NasabahService {

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    NasabahRepository nasabahRepository;

    @Override
    public Nasabah create(Nasabah nasabah) {
        nasabah.setStatus("PENDING");
        final Nasabah result = nasabahRepository.save(nasabah);
        return result;
    }

    @Override
    public Nasabah update(Long id, Nasabah nasabah) {
        Nasabah result = findById(id);
        if(result != null){
            result.setNik(nasabah.getNik());
            result.setNama(nasabah.getNama());
            result.setDomisili(nasabah.getDomisili());
            result.setNoRekening(nasabah.getNoRekening());
            result.setNoTelp(nasabah.getNoTelp());
            result.setSaldo(nasabah.getSaldo());
            result.setStatus(nasabah.getStatus());
            nasabahRepository.save(result);

            return result;
        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        final Nasabah result = findById(id);
        if(result != null){
            nasabahRepository.delete(result);
            return true;
        }
        return false;
    }

    @Override
    public Nasabah findById(Long id) {
        Optional<Nasabah> result = nasabahRepository.findById(id);
        return result.orElse(null);
    }

    @Override
    public List<Nasabah> findAll() {
        return nasabahRepository.findAll();
    }

    @Override
    public Nasabah findByNik(String nik) {
        return nasabahRepository.findByNik(nik);
    }

    @Override
    public Nasabah findByNoRekening(String noRekening) {
        return nasabahRepository.findByNoRekening(noRekening);
    }

    @Override
    public Nasabah setorTunai(Long id, SaldoDTO saldoDTO) {
        Nasabah result = findById(id);
        Long total = result.getSaldo()+ saldoDTO.getTunai();
        result.setSaldo(total);
        nasabahRepository.save(result);

        return result;
    }

    @Override
    public Nasabah tarikTunai(Long id, SaldoDTO saldoDTO) {
        Nasabah result = findById(id);
        Long total = result.getSaldo()-saldoDTO.getTunai();
        result.setSaldo(total);
        nasabahRepository.save(result);

        return result;
    }

    @Override
    public Nasabah approvedRegister(Long id, StatusDTO statusDTO) {
        Nasabah result = findById(id);

        if(result != null){
            result.setNoRekening(randomDecimalString(10));
            result.setSaldo(0L);
            result.setStatus(statusDTO.getNewStatus());
            nasabahRepository.save(result);

            return result;
        }
        return null;
    }

    @Override
    public Nasabah rejectedRegister(Long id, StatusDTO statusDTO) {
        Nasabah result = findById(id);

        if(result != null){
            result.setStatus(statusDTO.getNewStatus());
            nasabahRepository.save(result);

            return result;
        }
        return null;
    }

    static char digits[] = {'0','1','2','3','4','5','6','7','8','9'};

    public static char randomDecimalDigit() {
        return digits[(int)Math.floor(Math.random() * 10)];
    }

    public static String randomDecimalString(int ndigits) {
        StringBuilder result = new StringBuilder();
        for(int i=0; i<ndigits; i++) {
            result.append(randomDecimalDigit());
        }
        return result.toString();
    }

}
