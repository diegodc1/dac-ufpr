package com.example.reservas.services;

import com.example.reservas.entity.AeroportoEntity;
import com.example.reservas.exceptions.AeroportoNaoEncontradoException;
import com.example.reservas.repositorys.AeroportoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AeroportoService {

    @Autowired
    private AeroportoRepository aeroportoRepository;

    public AeroportoEntity buscarPorCodigo(String codigo) {
        return aeroportoRepository.findById(codigo.toUpperCase())
                .orElseThrow(() -> new AeroportoNaoEncontradoException("Aeroporto com código '" + codigo + "' não encontrado"));
    }
}
