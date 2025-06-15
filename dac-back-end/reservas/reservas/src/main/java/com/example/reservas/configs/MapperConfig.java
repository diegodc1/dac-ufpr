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

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        // Mapeamento ReservaEntity -> ReservaModel
        mapper.addMappings(new PropertyMap<ReservaEntity, ReservaModel>() {
            @Override
            protected void configure() {
                // getDescricao() retorna o objeto EstadoReservaEntity
                map().setEstado(source.getDescricao().getDescricao());
                map().setAeroportoOrigemCodigo(source.getAeroportoOrigem().getCodigo());
                map().setAeroportoDestinoCodigo(source.getAeroportoDestino().getCodigo());
            }
        });

        // Converter para criar o EstadoReservaEntity a partir da descrição (String)
        Converter<String, EstadoReservaEntity> estadoConverter = new Converter<String, EstadoReservaEntity>() {
            @Override
            public EstadoReservaEntity convert(MappingContext<String, EstadoReservaEntity> context) {
                if (context.getSource() == null) return null;
                EstadoReservaEntity estado = new EstadoReservaEntity();
                estado.setDescricao(context.getSource());
                // Você pode setar o ID aqui se ele for conhecido
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
                return aeroporto;
            }
        };

        // Mapeamento ReservaModel -> ReservaEntity
        mapper.addMappings(new PropertyMap<ReservaModel, ReservaEntity>() {
            @Override
            protected void configure() {
                using(estadoConverter).map(source.getEstado(), destination.getDescricao());
                using(aeroportoConverter).map(source.getAeroportoOrigemCodigo(), destination.getAeroportoOrigem());
                using(aeroportoConverter).map(source.getAeroportoDestinoCodigo(), destination.getAeroportoDestino());
            }
        });

        return mapper;
    }
}
