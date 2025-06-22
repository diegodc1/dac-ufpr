package com.aerolinha.utils;

import org.springframework.stereotype.Component;

@Component
public class ValidadorCPF {
    private static final int TAMANHO_CPF = 11;
    private static final int POSICAO_PRIMEIRO_VERIFICADOR = 9;
    private static final int POSICAO_SEGUNDO_VERIFICADOR = 10;

    public boolean cpfValido(String numeroCpf) {
        if (numeroCpf == null || numeroCpf.length() != TAMANHO_CPF) {
            return false;
        }

        int[] digitos = extrairDigitos(numeroCpf);

        if (todosDigitosIguais(digitos)) {
            return false;
        }

        int primeiroVerificador = calcularDigitoVerificador(digitos, POSICAO_PRIMEIRO_VERIFICADOR);
        if (primeiroVerificador != digitos[POSICAO_PRIMEIRO_VERIFICADOR]) {
            return false;
        }

        int segundoVerificador = calcularDigitoVerificador(digitos, POSICAO_SEGUNDO_VERIFICADOR);
        return segundoVerificador == digitos[POSICAO_SEGUNDO_VERIFICADOR];
    }

    private int[] extrairDigitos(String cpf) {
        int[] digitos = new int[TAMANHO_CPF];
        for (int i = 0; i < TAMANHO_CPF; i++) {
            digitos[i] = Character.getNumericValue(cpf.charAt(i));
        }
        return digitos;
    }

    private boolean todosDigitosIguais(int[] digitos) {
        int primeiroDigito = digitos[0];
        for (int i = 1; i < digitos.length; i++) {
            if (digitos[i] != primeiroDigito) {
                return false;
            }
        }
        return true;
    }

    private int calcularDigitoVerificador(int[] digitos, int posicao) {
        int soma = 0;
        int peso = posicao + 1;

        for (int i = 0; i < posicao; i++) {
            soma += digitos[i] * (peso - i);
        }

        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }
}
