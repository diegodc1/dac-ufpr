import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Data
public class ClienteResponseDTO {

    @NotNull
    private Long codigo;

    @NotNull
    @Size(min = 11, max = 11)
    private String cpf;

    @Email
    private String email;

    @NotBlank
    private String nome;

    @Pattern(regexp = "\\d{10,11}")
    private String telefone;

    @PositiveOrZero
    private Integer saldoMilhas;
}
