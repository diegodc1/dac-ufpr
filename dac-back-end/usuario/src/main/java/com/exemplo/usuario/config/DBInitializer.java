
package main.java.com.exemplo.usuario.usuario.config;

import br.net.web.usuario.usuario.model.Voos;
import br.net.web.usuario.usuario.model.Usuario;
import br.net.web.usuario.usuario.database.VoosRepositoryRepository;
import br.net.web.usurio.usuario.database.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DBInitializer {

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepository) {
        return args -> {
            // Clientes
            usuarioRepository.save(new Usuario(0, "Ana Paula", "anapaula@gmail.com", "jgwZFC7mE0Lh+LCab8y89YZ9sVQkREdO03rRG9COsGI=", // s:6515
             "CLIENTE", "123.456.789-00", "41 98765-4321", "80010-000", "Rua das Flores", "123", "Apto 12", "Centro", "Curitiba", "PR", null, null));
           

             // Funcionario
            Usuario funcionario = new Usuario();
            funcionario.setNome(" Andre Silva");
            funcionario.setEmail("andre@topus.com");
            funcionario.setPerfil("FUNCIONARIO");
            funcionario.setSenha("jgwZFC7mE0Lh+LCab8y89YZ9sVQkREdO03rRG9COsGI=");
            funcionario.setDataNascimento("1995-05-01");
            
            // Administrador
            Usuario admin = new Usuario();
            admin.setEmail("admin@topus.com");
            admin.setSenha("jgwZFC7mE0Lh+LCab8y89YZ9sVQkREdO03rRG9COsGI="); // s/:6515
            admin.setPerfil("ADMIN");

            // Salvando os dados no banco
            usuarioRepository.save(funcionario);
    
            usuarioRepository.save(admin);

        };
    }
        @Bean
    CommandLineRunner initVoosDatabase(VoosRepository voosRepository) {
        return args -> {
            // Voos Cadastrados
            voosRepository.save(new voo(1, "606040", true));
            categoriaRepository.save(new voo(2, "203010", true));
            

        };
    }
}
