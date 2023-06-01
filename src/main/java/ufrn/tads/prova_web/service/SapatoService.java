package ufrn.tads.prova_web.service;

import org.springframework.stereotype.Service;
import ufrn.tads.prova_web.model.Sapato;
import ufrn.tads.prova_web.repository.SapatoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SapatoService {
    private SapatoRepository repository;

    public SapatoService(SapatoRepository repository) {
        this.repository = repository;
    }

    public void save(Sapato s){
        s.tituloMaiusculo();
        repository.save(s);
    }


    public List<Sapato> findAll(){
        return repository.findAll();
    }

    public List<Sapato> findByDeletedIsNull(){
        return repository.findByDeletedIsNull();
    }


    public Sapato findById(String id){
        Optional<Sapato> s = repository.findById(id);
        return s.orElse(null);
    }

}
