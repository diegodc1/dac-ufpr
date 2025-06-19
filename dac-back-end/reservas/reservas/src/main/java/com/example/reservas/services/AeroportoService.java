package com.example.reservas.services;

import com.example.reservas.entity.AeroportoEntity;
import com.example.reservas.exceptions.AeroportoNaoEncontradoException;
import com.example.reservas.repositorys.AeroportoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AeroportoService {

    @Autowired
    private AeroportoRepository aeroportoRepository;

    /**
     * Método para buscar aeroporto por código IATA.
     * Caso o aeroporto não seja encontrado, lança uma exceção personalizada.
     *
     * @param codigo Código IATA do aeroporto.
     * @return AeroportoEntity A entidade do aeroporto encontrado.
     */
    @Transactional(readOnly = true)
    public AeroportoEntity buscarPorCodigo(String codigo) {
        // Certifique-se de que o código está em maiúsculo para padronização.
        String codigoFormatado = codigo.toUpperCase();

        // Use findById do repositório para buscar o aeroporto no banco de dados.
        Optional<AeroportoEntity> aeroportoOptional = aeroportoRepository.findById(codigoFormatado);

        // Se o aeroporto não for encontrado, lança uma exceção personalizada.
        return aeroportoOptional.orElseThrow(() ->
                new AeroportoNaoEncontradoException("Aeroporto com código '" + codigo + "' não encontrado"));
    }

    /**
     * Método para salvar ou atualizar um aeroporto.
     * Caso a entidade não tenha ID, o método salvará como uma nova. Caso contrário, atualizará.
     *
     * @param aeroportoEntity A entidade que será salva ou atualizada.
     * @return AeroportoEntity A entidade salva ou atualizada.
     */
    @Transactional
    public AeroportoEntity salvarOuAtualizar(AeroportoEntity aeroportoEntity) {
        // Se a entidade já tem um ID, significa que estamos atualizando.
        // Caso contrário, estamos criando uma nova.
        return aeroportoRepository.save(aeroportoEntity);
    }

    /**
     * Método para excluir um aeroporto pelo código.
     * Se o aeroporto não for encontrado, lança uma exceção personalizada.
     *
     * @param codigo Código IATA do aeroporto a ser excluído.
     */
    @Transactional
    public void excluirPorCodigo(String codigo) {
        // Busca o aeroporto pelo código.
        AeroportoEntity aeroporto = buscarPorCodigo(codigo);

        // Exclui o aeroporto encontrado.
        aeroportoRepository.delete(aeroporto);
    }
}
