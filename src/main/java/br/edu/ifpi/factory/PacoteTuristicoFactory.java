package br.edu.ifpi.factory;

import br.edu.ifpi.Model.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Factory Method para criação de Pacotes Turísticos e Reservas.
 * Facilita a construção de objetos complexos do sistema.
 */
public class PacoteTuristicoFactory {
    
    /**
     * Builder para criar pacotes turísticos de forma fluente
     */
    public static class PacoteBuilder {
        private Destino destino;
        private List<ServicoContratavel> servicos;
        
        public PacoteBuilder() {
            this.servicos = new ArrayList<>();
        }
        
        /**
         * Define o destino do pacote
         */
        public PacoteBuilder comDestino(Destino destino) {
            if (destino == null) {
                throw new IllegalArgumentException("Destino não pode ser nulo");
            }
            this.destino = destino;
            return this;
        }
        
        /**
         * Define o destino do pacote criando um novo
         */
        public PacoteBuilder comDestino(String nomeDestino, String pais) {
            this.destino = EntidadeFactory.criarDestino(nomeDestino, pais);
            return this;
        }
        
        /**
         * Adiciona um serviço ao pacote
         */
        public PacoteBuilder adicionarServico(ServicoContratavel servico) {
            if (servico == null) {
                throw new IllegalArgumentException("Serviço não pode ser nulo");
            }
            this.servicos.add(servico);
            return this;
        }
        
        /**
         * Adiciona um voo ao pacote
         */
        public PacoteBuilder adicionarVoo(String companhia, String origem, String destino, double preco) {
            Voo voo = ServicoContratavelFactory.criarVoo(companhia, origem, destino, preco);
            this.servicos.add(voo);
            return this;
        }
        
        /**
         * Adiciona uma hospedagem ao pacote
         */
        public PacoteBuilder adicionarHospedagem(String hotel, int diarias, double precoPorNoite) {
            Hospedagem hospedagem = ServicoContratavelFactory.criarHospedagem(hotel, diarias, precoPorNoite);
            this.servicos.add(hospedagem);
            return this;
        }
        
        /**
         * Adiciona um passeio ao pacote
         */
        public PacoteBuilder adicionarPasseio(String nome, double preco) {
            Passeio passeio = ServicoContratavelFactory.criarPasseio(nome, preco);
            this.servicos.add(passeio);
            return this;
        }
        
        /**
         * Adiciona múltiplos serviços ao pacote
         */
        public PacoteBuilder adicionarServicos(List<ServicoContratavel> servicos) {
            if (servicos == null) {
                throw new IllegalArgumentException("Lista de serviços não pode ser nula");
            }
            this.servicos.addAll(servicos);
            return this;
        }
        
        /**
         * Constrói o pacote turístico
         */
        public PacoteTuristico construir() {
            if (destino == null) {
                throw new IllegalStateException("Destino é obrigatório para criar um pacote turístico");
            }
            if (servicos.isEmpty()) {
                throw new IllegalStateException("Pacote turístico deve ter pelo menos um serviço");
            }
            
            return new PacoteTuristico(destino, new ArrayList<>(servicos));
        }
    }
    
    /**
     * Cria um novo builder para pacote turístico
     */
    public static PacoteBuilder novoPacote() {
        return new PacoteBuilder();
    }
    
    /**
     * Cria um pacote turístico simples
     */
    public static PacoteTuristico criarPacote(Destino destino, List<ServicoContratavel> servicos) {
        if (destino == null) {
            throw new IllegalArgumentException("Destino não pode ser nulo");
        }
        if (servicos == null || servicos.isEmpty()) {
            throw new IllegalArgumentException("Pacote deve ter pelo menos um serviço");
        }
        
        return new PacoteTuristico(destino, servicos);
    }
    
    /**
     * Cria uma reserva completa
     */
    public static Reserva criarReserva(Cliente cliente, PacoteTuristico pacote, MetodoPagamento metodoPagamento) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não pode ser nulo");
        }
        if (pacote == null) {
            throw new IllegalArgumentException("Pacote não pode ser nulo");
        }
        if (metodoPagamento == null) {
            throw new IllegalArgumentException("Método de pagamento não pode ser nulo");
        }
        
        return new Reserva(cliente, pacote, metodoPagamento);
    }
    
    /**
     * Builder para criar reservas de forma fluente
     */
    public static class ReservaBuilder {
        private Cliente cliente;
        private PacoteTuristico pacote;
        private MetodoPagamento metodoPagamento;
        
        public ReservaBuilder comCliente(Cliente cliente) {
            this.cliente = cliente;
            return this;
        }
        
        public ReservaBuilder comCliente(String nome, String email, String cpf) {
            this.cliente = EntidadeFactory.criarCliente(nome, email, cpf);
            return this;
        }
        
        public ReservaBuilder comPacote(PacoteTuristico pacote) {
            this.pacote = pacote;
            return this;
        }
        
        public ReservaBuilder comPagamentoCartao(String numeroCartao, String titular) {
            this.metodoPagamento = MetodoPagamentoFactory.criarCartaoCredito(numeroCartao, titular);
            return this;
        }
        
        public ReservaBuilder comPagamentoPix(String chavePix) {
            this.metodoPagamento = MetodoPagamentoFactory.criarTransferenciaPix(chavePix);
            return this;
        }
        
        public ReservaBuilder comMetodoPagamento(MetodoPagamento metodoPagamento) {
            this.metodoPagamento = metodoPagamento;
            return this;
        }
        
        public Reserva construir() {
            return criarReserva(cliente, pacote, metodoPagamento);
        }
    }
    
    /**
     * Cria um novo builder para reserva
     */
    public static ReservaBuilder novaReserva() {
        return new ReservaBuilder();
    }
}
