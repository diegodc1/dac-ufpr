export interface Reserva {
    codigo: string;                     
    estado: 'RESERVADO' | 'CHECK-IN' | 'CANCELADO'; 
    aeroportoOrigem: string;           
    aeroportoDestino: string;          
  
    dataHora: string;                 
    duracaoHoras: number;             
    duracaoMinutos: number;           
  
    valorGasto: number;                   
    milhasGastadas: number;                   
  
    nomePassageiro?: string;          
    assento?: string;                
    emailUsuario?: string;            
  
    dataCriacao?: string;             
    ultimaAtualizacao?: string;       
 }
  