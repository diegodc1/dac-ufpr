import { Injectable } from '@angular/core';

// Definição da estrutura da reserva
interface Reserva {
  codigo: string;
  dataHora: string;
  aeroportoOrigem: string;
  aeroportoDestino: string;
  valorGasto: number;
  milhasGastadas: number;
  estado: 'CRIADA' | 'CHECK-IN' | 'CANCELADA';
}

interface Transacao {
  dataHora: string;
  valorEmReais: number;
  milhas: number;
  descricao: string;
  tipo: 'ENTRADA' | 'SAÍDA';
}

@Injectable({
  providedIn: 'root',
})
export class ReservaService {
  private saldoMilhas: number = 10000;  // Saldo inicial de milhas
  private reservas: Reserva[] = [];     // Lista de reservas
  private transacoes: Transacao[] = []; // Lista de transações de milhas

  constructor() {}

  // Retorna o saldo de milhas atual
  getSaldoMilhas(): number {
    return this.saldoMilhas;
  }

  // Retorna todas as reservas
  getReservas(): Reserva[] {
    return this.reservas;
  }

  // Retorna uma reserva pelo código
  getReservaByCodigo(codigo: string): Reserva | undefined {
    return this.reservas.find((reserva) => reserva.codigo === codigo);
  }

  // Cria uma nova reserva
  criarReserva(dataHora: string, aeroportoOrigem: string, aeroportoDestino: string, valorGasto: number, milhasGastadas: number): Reserva {
    const codigo = this.gerarCodigoReserva();
    const novaReserva: Reserva = {
      codigo,
      dataHora,
      aeroportoOrigem,
      aeroportoDestino,
      valorGasto,
      milhasGastadas,
      estado: 'CRIADA',
    };

    this.reservas.push(novaReserva);
    this.atualizarMilhas(-milhasGastadas); // Deduz as milhas do saldo

    return novaReserva;
  }

  // Gera um código de reserva único
  private gerarCodigoReserva(): string {
    const letras = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
    const numeros = '0123456789';
    let codigo = '';
    for (let i = 0; i < 3; i++) {
      codigo += letras.charAt(Math.floor(Math.random() * letras.length));
    }
    for (let i = 0; i < 3; i++) {
      codigo += numeros.charAt(Math.floor(Math.random() * numeros.length));
    }
    return codigo;
  }

  // Atualiza o saldo de milhas (para compra, cancelamento, etc.)
  private atualizarMilhas(milhas: number): void {
    this.saldoMilhas += milhas;
    // Registra a transação de milhas
    const transacao: Transacao = {
      dataHora: new Date().toLocaleString(),
      valorEmReais: 0, // O valor em reais é 0, pois estamos manipulando apenas as milhas
      milhas: milhas,
      descricao: milhas > 0 ? 'ENTRADA DE MILHAS' : 'SAÍDA DE MILHAS',
      tipo: milhas > 0 ? 'ENTRADA' : 'SAÍDA',
    };
    this.transacoes.push(transacao);
  }

  // Compra de milhas
  comprarMilhas(valorEmReais: number): void {
    if (valorEmReais <= 0) {
      console.error('O valor para a compra de milhas deve ser maior que zero.');
      return; // Evita compras inválidas
    }
    const milhasCompradas = Math.floor(valorEmReais / 5); // 1 milha para cada R$5
    this.atualizarMilhas(milhasCompradas);

    const transacao: Transacao = {
      dataHora: new Date().toLocaleString(),
      valorEmReais,
      milhas: milhasCompradas,
      descricao: 'COMPRA DE MILHAS',
      tipo: 'ENTRADA',
    };

    this.transacoes.push(transacao);
  }

  // Cancelar uma reserva
  cancelarReserva(reserva: Reserva): void {
    if (reserva.estado === 'CRIADA' || reserva.estado === 'CHECK-IN') {
      reserva.estado = 'CANCELADA';
      this.atualizarMilhas(reserva.milhasGastadas); // Retorna as milhas ao saldo
      const transacao: Transacao = {
        dataHora: new Date().toLocaleString(),
        valorEmReais: 0,
        milhas: reserva.milhasGastadas,
        descricao: `CANCELAMENTO DE RESERVA - ${reserva.aeroportoOrigem} -> ${reserva.aeroportoDestino}`,
        tipo: 'ENTRADA',
      };
      this.transacoes.push(transacao);
    } else {
      console.log('Reserva não pode ser cancelada, pois o estado não é válido.');
      // Adicione lógica para tratar reservas já canceladas ou concluídas
    }
  }

  // Ver o extrato de transações (compra de milhas, cancelamento, etc.)
  getExtratoTransacoes(): Transacao[] {
    return this.transacoes;
  }
}
