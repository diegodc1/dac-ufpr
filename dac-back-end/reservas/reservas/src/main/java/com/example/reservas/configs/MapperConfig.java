package com.example.reservas.configs;

import com.example.reservas.entity.AeroportoEntity;
import com.example.reservas.entity.EstadoReservaEntity;
import com.example.reservas.entity.ReservaEntity;
import com.example.reservas.model.ReservaModel;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        // Mapeamento ReservaEntity -> ReservaModel
        mapper.addMappings(new PropertyMap<ReservaEntity, ReservaModel>() {
            @Override
            protected void configure() {
                map().setEstado(source.getEstado().getDescricao());
                map().setAeroportoOrigemCodigo(source.getAeroportoOrigem().getCodigo());
                map().setAeroportoDestinoCodigo(source.getAeroportoDestino().getCodigo());
            }
        });

        // Converter para criar o EstadoReservaEntity a partir do estado (String)
        Converter<String, EstadoReservaEntity> estadoConverter = new Converter<String, EstadoReservaEntity>() {
            @Override
            public EstadoReservaEntity convert(MappingContext<String, EstadoReservaEntity> context) {
                if (context.getSource() == null) return null;
                EstadoReservaEntity estado = new EstadoReservaEntity();
                estado.setDescricao(context.getSource());
                // IMPORTANTE: preencher o ID do estado se possível, ou buscar no DB no Service
                return estado;
            }
        };

        // Converter para criar AeroportoEntity a partir do código (String)
        Converter<String, AeroportoEntity> aeroportoConverter = new Converter<String, AeroportoEntity>() {
            @Override
            public AeroportoEntity convert(MappingContext<String, AeroportoEntity> context) {
                if (context.getSource() == null) return null;
                AeroportoEntity aeroporto = new AeroportoEntity();
                aeroporto.setCodigo(context.getSource());
                // IMPORTANTE: buscar dados completos no service para campos adicionais
                return aeroporto;
            }
        };

        // Mapeamento ReservaModel -> ReservaEntity
        mapper.addMappings(new PropertyMap<ReservaModel, ReservaEntity>() {
            @Override
            protected void configure() {
                using(estadoConverter).map(source.getEstado(), destination.getEstado());
                using(aeroportoConverter).map(source.getAeroportoOrigemCodigo(), destination.getAeroportoOrigem());
                using(aeroportoConverter).map(source.getAeroportoDestinoCodigo(), destination.getAeroportoDestino());
            }
        });

        return mapper;
    }
}
