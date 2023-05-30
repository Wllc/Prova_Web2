package ufrn.tads.prova_web;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import ufrn.tads.prova_web.model.Sapato;
import ufrn.tads.prova_web.model.Usuario;
import ufrn.tads.prova_web.repository.UsuarioRepository;
import ufrn.tads.prova_web.repository.SapatoRepository;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class ProvaWebApplication{

    public static void main(String[] args) {
        SpringApplication.run(ProvaWebApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(SapatoRepository sapatoRepository, UsuarioRepository usuarioRepository, PasswordEncoder encoder){
        return args -> {
//            List<Sapato> sapatos = Stream.of(
//                    new Sapato(1L,null, "sapato1.jpg","Tênis Nike Downshifter 12", "R$ 399,99",42, "Lançamento"),
//                    new Sapato(2L,null, "sapato2.jpg","Tênis Nike Renew Ride 3", "R$ 246,99",42, "Exclusivo")
//            ).collect(Collectors.toList());
//            sapatoRepository.saveAll(sapatos);

            List<Usuario> users = Stream.of(
                    new Usuario(1, "João", "123.456.789-10", "wallace", encoder.encode("wallace"), true),
                    new Usuario(2, "Maria", "444.456.789-10", "user", encoder.encode("user"), false),
                    new Usuario(3, "Pedro", "555.456.789-10", "user1", encoder.encode("user1"), false)
            ).collect(Collectors.toList());
            usuarioRepository.saveAll(users);
        };
    }

}
