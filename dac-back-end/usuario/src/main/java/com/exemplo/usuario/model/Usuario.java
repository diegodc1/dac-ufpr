package main.java.com.exemplo.usuario.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Usuario {

   

    @Getter @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Setter @Getter
    private String nome;

    @Setter @Getter
    private String email;

    @Setter @Getter
    private String senha;

    @Setter @Getter
    private String perfil;

    //cliente
    @Setter @Getter
    private String cpf;

    @Setter @Getter
    private String telefone;

    @Setter @Getter
    private String cep;

    @Setter @Getter
    private String endereco;

    @Setter @Getter
    private String numero;

    @Setter @Getter
    private String complemento;

    @Setter @Getter
    private String bairro;

    @Setter @Getter
    private String cidade;

    @Setter @Getter
    private String estado;

    //funcionário
    @Setter @Getter
    private String dataNascimento;

    @Setter @Getter
    private String login;

}
