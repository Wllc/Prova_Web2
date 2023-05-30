package ufrn.tads.prova_web.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ufrn.tads.prova_web.erros.ApiErros;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Sapato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Date deleted;
    String imageUri;
    @NotBlank(message = ApiErros.ERRO_TITULO)
    String titulo;
    @NotBlank(message = ApiErros.ERRO_PRECO)
    String preco;
    @NotNull(message = ApiErros.ERRO_TAMANHO)
    Integer tamanho;
    @NotBlank(message = ApiErros.ERRO_DISPONIBILIDADE)
    String disponibilidade;

    public void tituloMaiusculo(){
        this.titulo.toUpperCase();
    }
}
