package springboot.pactindo.app.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springboot.pactindo.app.dto.NasabahDTO;
import springboot.pactindo.app.dto.SaldoDTO;
import springboot.pactindo.app.dto.StatusDTO;
import springboot.pactindo.app.model.Nasabah;
import springboot.pactindo.app.repository.NasabahRepository;
import springboot.pactindo.app.service.NasabahService;

import java.util.*;

@RestController
@RequestMapping("/nasabah")
public class NasabahController {

    @Autowired
    private NasabahRepository nasabahRepository;

    @Autowired
    private NasabahService nasabahService;


    ModelMapper modelMapper = new ModelMapper();


    private Nasabah mapToEntity(NasabahDTO nasabahDTO){
        return modelMapper.map(nasabahDTO,Nasabah.class);
    }

    private  NasabahDTO mapToDTO(Nasabah nasabah){
        return modelMapper.map(nasabah,NasabahDTO.class);
    }

    @PostMapping("/create")
    public Map<String, Object> createNasabah(
            @RequestBody NasabahDTO nasabahDTO
    ){
        Map<String,Object> mapResult = new HashMap<>();
        Nasabah nasabah = mapToEntity(nasabahDTO);

        final Nasabah result = nasabahService.create(nasabah);

        mapResult.put("message","Create Success");
        mapResult.put("data",result);

        return mapResult;
    }

    @GetMapping("/all")
    public Map<String,Object> getAllNasabah(){
        Map<String,Object> mapResult = new HashMap<>();

        List<NasabahDTO> listNasabah = new ArrayList<>();

        for(Nasabah nasabah:nasabahService.findAll()){
            NasabahDTO nasabahDTO = mapToDTO(nasabah);
            listNasabah.add(nasabahDTO);
        }

        String message;
        if(listNasabah.isEmpty()){
            message = "Data Not Found";
        }else{
            message = "Data All Found";
        }

        mapResult.put("message",message);
        mapResult.put("data",listNasabah);
        mapResult.put("Count",listNasabah.size());

        return mapResult;


    }

    @PutMapping("/edit/{nasabahId}")
    public Map<String , Object> updateNasabah(
            @PathVariable Long nasabahId,
            @RequestBody NasabahDTO nasabahDTO
    ){
        Map<String,Object> mapResult = new HashMap<>();
        Nasabah nasabah = nasabahService.findById(nasabahId);
        Nasabah result = nasabahService.update(nasabah.getId(),mapToEntity(nasabahDTO));

        mapResult.put("message","Success Update");
        mapResult.put("data",result);

        return mapResult;
    }

    @DeleteMapping("/delete/{nasabahId}")
    public Map<String,Object> deleteNasabah(
            @PathVariable Long nasabahId
    ){
        Map<String,Object> mapResult = new HashMap<>();
//        Nasabah nasabah = nasabahService.findById(nasabahId);
        nasabahService.delete(nasabahId);

        mapResult.put("Message","Success Delete");

        return mapResult;
    }

    @GetMapping("/ceksaldo")
    public Map<String,Object> getNasabah(
            @RequestParam String noRekening
    ){
        Map<String,Object> mapResult = new HashMap<>();
        Nasabah nasabah = nasabahService.findByNoRekening(noRekening);

        Map<String,String> result = new HashMap<>();
        result.put("Nomor Rekening",nasabah.getNoRekening());
        result.put("Saldo",Long.toString(nasabah.getSaldo()));

        mapResult.put("message","Success Check");
        mapResult.put("data",result);

        return mapResult;
    }

    @PutMapping("/setortunai")
    public Map<String,Object> setorTunai(
            @RequestParam String noRekening,
            @RequestBody SaldoDTO saldoDTO
    ){
        Map<String,Object> mapResult = new HashMap<>();
        Nasabah nasabah = nasabahService.findByNoRekening(noRekening);
        Long saldoBefore = nasabah.getSaldo();
        if(saldoDTO.getTunai()>0){
            mapResult.put("message","Success Add Money");
            mapResult.put("Saldo Sebelum",saldoBefore);
            nasabah = nasabahService.setorTunai(nasabah.getId(), saldoDTO);
            mapResult.put("Saldo Sesudah",nasabah.getSaldo());
        }else {
            mapResult.put("message","Failed Add Money");
            mapResult.put("Saldo Anda",nasabah.getSaldo());
        }

        return mapResult;

    }

    @PutMapping("/tariktunai")
    public Map<String,Object> tarikTunai(
            @RequestParam String noRekening,
            @RequestBody SaldoDTO saldoDTO
    ){
        Map<String,Object> mapResult = new HashMap<>();
        Nasabah nasabah = nasabahService.findByNoRekening(noRekening);
        Long saldoBefore = nasabah.getSaldo();
        if(saldoDTO.getTunai()>0 && saldoDTO.getTunai() % 50000 == 0){
            mapResult.put("message","Success Get Money");
            mapResult.put("Saldo Sebelum",saldoBefore);
            nasabah = nasabahService.tarikTunai(nasabah.getId(), saldoDTO);
            mapResult.put("Saldo Sesudah",nasabah.getSaldo());
        }else if(saldoDTO.getTunai()>0 && saldoDTO.getTunai() % 50000 != 0){
            mapResult.put("message","Failed Get Money");
            mapResult.put("Detail","Uang yang ditarik harus kelipatan Rp. 50000");
        }else{
            mapResult.put("message","Failed Get Money");
            mapResult.put("Saldo Anda",nasabah.getSaldo());
        }

        return mapResult;

    }

    @PutMapping("/approved")
    public Map<String,Object> approved(
            @RequestParam String nik
    ){
        Map<String,Object> mapResult = new HashMap<>();
        Nasabah nasabah = nasabahService.findByNik(nik);
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setNewStatus("APPROVED");

        if(Objects.equals(nasabah.getStatus(), "PENDING")){
            mapResult.put("message","Success Approved");
            nasabah = nasabahService.approvedRegister(nasabah.getId(), statusDTO);
            mapResult.put("data",nasabah);
        }else{
            mapResult.put("message","Failed Approved");
        }

        return mapResult;
    }

    @PutMapping("/rejected")
    public Map<String,Object> rejected(
            @RequestParam String nik
    ){
        Map<String,Object> mapResult = new HashMap<>();
        Nasabah nasabah = nasabahService.findByNik(nik);
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setNewStatus("REJECTED");

        if(Objects.equals(nasabah.getStatus(), "PENDING")
                && !Objects.equals(nasabah.getStatus(), "APPROVED")){
            mapResult.put("message","Success Rejected");
            nasabah = nasabahService.rejectedRegister(nasabah.getId(), statusDTO);
            mapResult.put("data",nasabah);
        }else{
            mapResult.put("message","Failed Rejected");
        }

        return mapResult;
    }

//    @PostMapping("/create")
//    public NasabahDTO create(
//            @RequestBody NasabahDTO request
//    ){
//        final Nasabah nasabah = nasabahService.mapToEntity(request);
//        final Nasabah result = nasabahService.create(nasabah);
//        return nasabahService.mapToDTO(result);
//    }

//    @PutMapping("/update/{id_nasabah}")
//    public NasabahDTO update(
//            @PathVariable Long id_nasabah,
//            @RequestBody NasabahDTO request
//    ){
//        final Nasabah nasabah = nasabahService.mapToEntity(request);
//        final Nasabah result = nasabahService.update(id_nasabah, nasabah);
//        return nasabahService.mapToDTO(result);
//    }
//
//    @GetMapping("/all")
//    public List<NasabahDTO> findAll(){
//        return nasabahService.findAll().stream().map(nasabah -> nasabahService.mapToDTO(nasabah))
//                .collect(Collectors.toList());
//    }
//
//    @DeleteMapping("/delete/{id_nasabah}")
//    public Boolean delete(
//            @PathVariable Long id_nasabah
//    ){
//        return nasabahService.delete(id_nasabah);
//    }

}
