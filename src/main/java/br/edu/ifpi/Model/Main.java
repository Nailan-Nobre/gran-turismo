package br.edu.ifpi.Model;

import br.edu.ifpi.dao.*;
import br.edu.ifpi.factory.*;
import br.edu.ifpi.util.Validador;
import br.edu.ifpi.util.Cores;
import java.util.Scanner;
import java.util.List;
import java.io.InputStream;
import java.util.logging.LogManager;

public class Main {
    
    private static Scanner scanner = new Scanner(System.in);
    private static ClienteDAO clienteDAO;
    private static DestinoDAO destinoDAO;
    private static VooDAO vooDAO;
    private static HospedagemDAO hospedagemDAO;
    
    public static void main(String[] args) {
        configurarLogs();
        
        System.out.println(Cores.separador("=", 60));
        System.out.println(Cores.titulo("        BEM-VINDO AO SISTEMA GRAN TURISMO"));
        System.out.println(Cores.separador("=", 60));
        System.out.print(Cores.info("\nConectando ao banco de dados"));
        
        try {
            System.out.print(".");
            inicializarDAOs();
            System.out.print(".");
            System.out.println(Cores.sucesso(" OK!"));
            System.out.println(Cores.sucesso("Sistema pronto!\n"));
            
            Cliente cliente = autenticarCliente();
            if (cliente == null) {
                System.out.println(Cores.info("Encerrando sistema..."));
                return;
            }
            
            // Verifica se cliente já tem método de pagamento configurado
            MetodoPagamento pagamento;
            if (cliente.temPagamentoConfigurado()) {
                pagamento = cliente.recuperarMetodoPagamento();
                System.out.println(Cores.sucesso("Método de pagamento carregado: ") + Cores.destaque(pagamento.obterDescricao()));
            } else {
                pagamento = configurarPagamento(cliente);
            }
            
            menuPrincipal(cliente, pagamento);
            
        } catch (Exception e) {
            System.out.println(Cores.erro(" FALHOU!"));
            System.err.println(Cores.erro("\nERRO: Não foi possível iniciar o sistema."));
            
            String mensagem = e.getMessage();
            if (mensagem != null && mensagem.contains("Communications link failure")) {
                System.err.println(Cores.aviso("Motivo: ") + "Não foi possível conectar ao banco de dados.");
                System.err.println(Cores.info("Solução: ") + "Verifique se o MySQL está rodando.");
            } else if (mensagem != null && mensagem.contains("Access denied")) {
                System.err.println(Cores.aviso("Motivo: ") + "Usuário ou senha incorretos.");
                System.err.println(Cores.info("Solução: ") + "Verifique as credenciais no arquivo persistence.xml");
            } else {
                System.err.println(Cores.aviso("Motivo: ") + (mensagem != null ? mensagem : "Erro desconhecido"));
            }
        } finally {
            fecharRecursos();
        }
    }
    
    private static void configurarLogs() {
        try {
            InputStream configStream = Main.class
                .getClassLoader()
                .getResourceAsStream("logging.properties");
            if (configStream != null) {
                LogManager.getLogManager().readConfiguration(configStream);
            }
        } catch (Exception e) {
            // Ignora erro de configuração de log
        }
    }
    
    private static void inicializarDAOs() {
        clienteDAO = DAOFactory.getClienteDAO();
        destinoDAO = DAOFactory.getDestinoDAO();
        vooDAO = DAOFactory.getVooDAO();
        hospedagemDAO = DAOFactory.getHospedagemDAO();
    }
    
    private static Cliente autenticarCliente() {
        while (true) {
            System.out.println("\n" + Cores.titulo("=== AUTENTICAÇÃO ==="));
            System.out.println(Cores.info("[1]") + " Fazer Login");
            System.out.println(Cores.info("[2]") + " Criar Nova Conta");
            System.out.println(Cores.info("[0]") + " Sair");
            System.out.print(Cores.input("Escolha: "));
            String opcao = scanner.nextLine().trim();
            
            if (opcao.equals("1")) {
                Cliente cliente = realizarLogin();
                if (cliente != null) {
                    return cliente;
                }
            } else if (opcao.equals("2")) {
                Cliente cliente = realizarCadastroCliente();
                if (cliente != null) {
                    return cliente;
                }
            } else if (opcao.equals("0")) {
                return null;
            } else {
                System.out.println(Cores.erro("Opção inválida!"));
            }
        }
    }
    
    private static Cliente realizarLogin() {
        System.out.println("\n" + Cores.titulo("=== LOGIN ==="));
        
        System.out.print(Cores.input("CPF: "));
        String cpf = scanner.nextLine().trim();
        
        if (!Validador.validarCPF(cpf)) {
            System.out.println(Cores.erro("ERRO: CPF inválido."));
            return null;
        }
        cpf = Validador.formatarCPF(cpf);
        
        System.out.print(Cores.input("Senha: "));
        String senha = scanner.nextLine().trim();
        
        try {
            Cliente cliente = clienteDAO.fazerLogin(cpf, senha);
            if (cliente != null) {
                System.out.println(Cores.sucesso("\nLogin realizado com sucesso!"));
                System.out.println(Cores.destaque("Bem-vindo(a), ") + Cores.CIANO_NEGRITO + cliente.getNome() + Cores.RESET + Cores.destaque("!"));
                return cliente;
            } else {
                System.out.println(Cores.erro("ERRO: CPF ou senha incorretos."));
                return null;
            }
        } catch (Exception e) {
            System.out.println(Cores.erro("ERRO ao fazer login: ") + e.getMessage());
            return null;
        }
    }
    
    private static Cliente realizarCadastroCliente() {
        Cliente cliente = null;
        
        while (cliente == null) {
            System.out.println("\n" + Cores.titulo("=== CADASTRO DE NOVA CONTA ==="));
            
            System.out.print(Cores.input("Nome completo: "));
            String nome = scanner.nextLine().trim();
            if (nome.isEmpty()) {
                System.out.println(Cores.erro("ERRO: Nome não pode ser vazio."));
                continue;
            }
            
            String email = "";
            while (email.isEmpty()) {
                System.out.print(Cores.input("Email: "));
                email = scanner.nextLine().trim();
                if (!Validador.validarEmail(email)) {
                    System.out.println(Cores.erro("ERRO: Email inválido. Use o formato: exemplo@dominio.com"));
                    email = "";
                }
            }
            
            String telefone = "";
            while (telefone.isEmpty()) {
                System.out.print(Cores.input("Telefone (com DDD): "));
                telefone = scanner.nextLine().trim();
                if (!Validador.validarTelefone(telefone)) {
                    System.out.println(Cores.erro("ERRO: Telefone inválido. Use o formato: (99) 99999-9999"));
                    telefone = "";
                }
            }
            telefone = Validador.formatarTelefone(telefone);
            
            String cpf = "";
            while (cpf.isEmpty()) {
                System.out.print(Cores.input("CPF: "));
                cpf = scanner.nextLine().trim();
                if (!Validador.validarCPF(cpf)) {
                    System.out.println(Cores.erro("ERRO: CPF inválido. Digite apenas números (11 dígitos)."));
                    cpf = "";
                }
            }
            cpf = Validador.formatarCPF(cpf);
            
            // Verificar se CPF já existe
            try {
                if (clienteDAO.existeCpf(cpf)) {
                    System.out.println(Cores.erro("ERRO: CPF já cadastrado. Use a opção de login."));
                    return null;
                }
            } catch (Exception e) {
                System.out.println(Cores.erro("ERRO ao verificar CPF: ") + e.getMessage());
                return null;
            }
            
            String senha = "";
            while (senha.isEmpty()) {
                System.out.print(Cores.input("Senha (mínimo 6 caracteres): "));
                senha = scanner.nextLine().trim();
                if (senha.length() < 6) {
                    System.out.println(Cores.erro("ERRO: Senha deve ter no mínimo 6 caracteres."));
                    senha = "";
                    continue;
                }
                
                System.out.print(Cores.input("Confirme a senha: "));
                String confirmaSenha = scanner.nextLine().trim();
                if (!senha.equals(confirmaSenha)) {
                    System.out.println(Cores.erro("ERRO: As senhas não coincidem."));
                    senha = "";
                }
            }
            
            try {
                cliente = EntidadeFactory.criarCliente(nome, email, cpf, senha);
                cliente.setTelefone(telefone);
                clienteDAO.salvar(cliente);
                System.out.println(Cores.sucesso("\nConta criada com sucesso!"));
                System.out.println(Cores.destaque("Bem-vindo(a), ") + Cores.CIANO_NEGRITO + cliente.getNome() + Cores.RESET + Cores.destaque("!"));
            } catch (Exception e) {
                System.out.println(Cores.erro("ERRO ao cadastrar: ") + e.getMessage());
                cliente = null;
            }
        }
        
        return cliente;
    }
    
    private static MetodoPagamento configurarPagamento(Cliente cliente) {
        MetodoPagamento pagamento = null;
        
        while (pagamento == null) {
            System.out.println("\n" + Cores.titulo("=== CONFIGURAR FORMA DE PAGAMENTO ==="));
            System.out.println(Cores.info("[1]") + " Chave PIX");
            System.out.println(Cores.info("[2]") + " Cartão de Crédito");
            System.out.print(Cores.input("Escolha: "));
            String opcao = scanner.nextLine().trim();

            try {
                if (opcao.equals("1")) {
                    System.out.print(Cores.input("Banco: "));
                    String banco = scanner.nextLine().trim();
                    
                    System.out.print(Cores.input("Agência: "));
                    String agencia = scanner.nextLine().trim();
                    
                    System.out.print(Cores.input("Conta: "));
                    String conta = scanner.nextLine().trim();
                    
                    System.out.print(Cores.input("Chave PIX (CPF, email ou telefone): "));
                    String chavePix = scanner.nextLine().trim();
                    
                    if (banco.isEmpty() || agencia.isEmpty() || conta.isEmpty() || chavePix.isEmpty()) {
                        System.out.println(Cores.erro("ERRO: Todos os campos são obrigatórios."));
                        continue;
                    }
                    
                    pagamento = MetodoPagamentoFactory.criarTransferenciaBancaria(
                        banco, agencia, conta, chavePix
                    );
                    System.out.println(Cores.sucesso("Forma de pagamento PIX configurada!"));
                    
                } else if (opcao.equals("2")) {
                    String numeroCartao = "";
                    while (numeroCartao.isEmpty()) {
                        System.out.print(Cores.input("Número do cartão (13-19 dígitos): "));
                        numeroCartao = scanner.nextLine().trim();
                        if (!Validador.validarNumeroCartao(numeroCartao)) {
                            System.out.println(Cores.erro("ERRO: Número de cartão inválido."));
                            numeroCartao = "";
                        }
                    }
                    
                    System.out.print(Cores.input("Nome do titular: "));
                    String titular = scanner.nextLine().trim();
                    if (titular.isEmpty()) {
                        System.out.println(Cores.erro("ERRO: Nome do titular não pode ser vazio."));
                        continue;
                    }
                    
                    String validade = "";
                    while (validade.isEmpty()) {
                        System.out.print(Cores.input("Validade (MM/AA): "));
                        validade = scanner.nextLine().trim();
                        if (!Validador.validarValidade(validade)) {
                            System.out.println(Cores.erro("ERRO: Validade inválida ou cartão vencido. Use MM/AA."));
                            validade = "";
                        }
                    }
                    
                    String cvv = "";
                    while (cvv.isEmpty()) {
                        System.out.print(Cores.input("CVV (3-4 dígitos): "));
                        cvv = scanner.nextLine().trim();
                        if (!Validador.validarCVV(cvv)) {
                            System.out.println(Cores.erro("ERRO: CVV inválido."));
                            cvv = "";
                        }
                    }
                    
                    pagamento = MetodoPagamentoFactory.criarCartaoCredito(
                        numeroCartao, titular, validade, cvv
                    );
                    System.out.println(Cores.sucesso("Cartão de crédito configurado!"));
                    
                } else {
                    System.out.println(Cores.erro("ERRO: Opção inválida."));
                }
            } catch (Exception e) {
                System.out.println(Cores.erro("ERRO: ") + e.getMessage());
                pagamento = null;
            }
        }
        
        // Salva o método de pagamento no cliente
        try {
            cliente.salvarMetodoPagamento(pagamento);
            clienteDAO.atualizar(cliente);
            System.out.println(Cores.sucesso("Método de pagamento salvo com sucesso!"));
        } catch (Exception e) {
            System.out.println(Cores.aviso("Aviso: Não foi possível salvar o método de pagamento: ") + e.getMessage());
        }
        
        return pagamento;
    }
    
    private static void menuPrincipal(Cliente cliente, MetodoPagamento pagamento) {
        Reserva reserva = null;
        PacoteTuristico pacote = null;
        
        while (true) {
            System.out.println("\n" + Cores.separador("=", 60));
            System.out.println(Cores.titulo("                    MENU PRINCIPAL"));
            System.out.println(Cores.separador("=", 60));
            System.out.println(Cores.info("[1]") + " Minhas Informações");
            System.out.println(Cores.info("[2]") + " Buscar Destinos Disponíveis");
            System.out.println(Cores.info("[3]") + " Buscar Voos Disponíveis");
            System.out.println(Cores.info("[4]") + " Buscar Hospedagens Disponíveis");
            System.out.println(Cores.info("[5]") + " Criar Pacote Turístico");
            System.out.println(Cores.info("[8]") + " Alterar Método de Pagamento");
            
            if (pacote != null && reserva == null) {
                System.out.println(Cores.VERDE_BRILHANTE + "[6]" + Cores.RESET + " Confirmar Reserva");
            }
            if (reserva != null) {
                System.out.println(Cores.VERDE_BRILHANTE + "[7]" + Cores.RESET + " Ver Minha Reserva");
            }
            
            System.out.println(Cores.VERMELHO_BRILHANTE + "[0]" + Cores.RESET + " Sair");
            System.out.println(Cores.separador("=", 60));
            System.out.print(Cores.input("Escolha: "));
            
            String opcao = scanner.nextLine().trim();
            
            switch (opcao) {
                case "1":
                    exibirInformacoesCliente(cliente, pagamento, pacote, reserva);
                    break;
                    
                case "2":
                    buscarDestinos();
                    break;
                    
                case "3":
                    buscarVoos();
                    break;
                    
                case "4":
                    buscarHospedagens();
                    break;
                    
                case "5":
                    pacote = criarPacoteTuristico();
                    if (pacote != null) {
                        System.out.print(Cores.input("\nDeseja criar a reserva agora? (S/N): "));
                        String resposta = scanner.nextLine().trim();
                        if (resposta.equalsIgnoreCase("S") || resposta.equalsIgnoreCase("SIM")) {
                            reserva = criarReserva(cliente, pacote, pagamento);
                        }
                    }
                    break;
                    
                case "6":
                    if (pacote != null && reserva == null) {
                        reserva = criarReserva(cliente, pacote, pagamento);
                    } else if (reserva != null) {
                        System.out.println(Cores.aviso("Você já possui uma reserva confirmada."));
                    } else {
                        System.out.println(Cores.erro("ERRO: Crie um pacote turístico primeiro."));
                    }
                    break;
                    
                case "7":
                    if (reserva != null) {
                        exibirReserva(reserva);
                    } else {
                        System.out.println(Cores.aviso("Você não possui reservas."));
                    }
                    break;
                
                case "8":
                    MetodoPagamento novoPagamento = configurarPagamento(cliente);
                    if (novoPagamento != null) {
                        pagamento = novoPagamento;
                    }
                    break;
                    
                case "0":
                    System.out.println(Cores.sucesso("\nObrigado por usar o Gran Turismo!"));
                    System.out.println(Cores.info("Até logo!"));
                    return;
                    
                default:
                    System.out.println(Cores.erro("ERRO: Opção inválida."));
            }
        }
    }
    
    private static void exibirInformacoesCliente(Cliente cliente, MetodoPagamento pagamento, 
                                                  PacoteTuristico pacote, Reserva reserva) {
        System.out.println("\n" + Cores.separador("=", 60));
        System.out.println(Cores.titulo("MINHAS INFORMAÇÕES"));
        System.out.println(Cores.separador("=", 60));
        System.out.println(Cores.info("Nome: ") + Cores.BRANCO_BRILHANTE + cliente.getNome() + Cores.RESET);
        System.out.println(Cores.info("Email: ") + Cores.BRANCO_BRILHANTE + cliente.getEmail() + Cores.RESET);
        System.out.println(Cores.info("CPF: ") + Cores.BRANCO_BRILHANTE + cliente.getCpf() + Cores.RESET);
        System.out.println(Cores.info("Telefone: ") + Cores.BRANCO_BRILHANTE + cliente.getTelefone() + Cores.RESET);
        System.out.println(Cores.info("Forma de pagamento: ") + Cores.ROXO_BRILHANTE + pagamento.obterDescricao() + Cores.RESET);
        
        if (pacote != null) {
            System.out.println("\n" + Cores.destaque("--- PACOTE ATUAL ---"));
            System.out.println(Cores.info("Destino: ") + Cores.CIANO_BRILHANTE + pacote.getDestino().getNome() + " - " + 
                             pacote.getDestino().getPais() + Cores.RESET);
            System.out.println(Cores.info("Valor total: ") + Cores.valor("R$ " + String.format("%.2f", pacote.calcularValorTotal())));
        }
        
        if (reserva != null) {
            System.out.println("\n" + Cores.sucesso("Status: RESERVA CONFIRMADA"));
        }
        System.out.println(Cores.separador("=", 60));
    }
    
    private static void buscarDestinos() {
        try {
            System.out.println("\n" + Cores.titulo("=== DESTINOS DISPONÍVEIS ==="));
            List<Destino> destinos = destinoDAO.listarTodos();
            
            if (destinos.isEmpty()) {
                System.out.println(Cores.aviso("Nenhum destino cadastrado no momento."));
                return;
            }
            
            System.out.println(Cores.info("\nTotal de destinos: ") + Cores.BRANCO_BRILHANTE + destinos.size() + Cores.RESET);
            System.out.println(Cores.separador("-", 60));
            
            for (Destino d : destinos) {
                System.out.println(Cores.info("ID: ") + Cores.AMARELO_BRILHANTE + d.getId() + Cores.RESET);
                System.out.println(Cores.info("Destino: ") + Cores.CIANO_NEGRITO + d.getNome() + " - " + d.getPais() + Cores.RESET);
                System.out.println(Cores.info("Descrição: ") + d.getDescricao());
                System.out.println(Cores.separador("-", 60));
            }
            
        } catch (Exception e) {
            System.out.println(Cores.erro("ERRO ao buscar destinos: ") + e.getMessage());
        }
    }
    
    private static void buscarVoos() {
        try {
            System.out.println("\n" + Cores.titulo("=== VOOS DISPONÍVEIS ==="));
            List<Voo> voos = vooDAO.listarTodos();
            
            if (voos.isEmpty()) {
                System.out.println(Cores.aviso("Nenhum voo cadastrado no momento."));
                return;
            }
            
            System.out.println(Cores.info("\nTotal de voos: ") + Cores.BRANCO_BRILHANTE + voos.size() + Cores.RESET);
            System.out.println(Cores.separador("-", 60));
            
            for (Voo v : voos) {
                System.out.println(Cores.info("ID: ") + Cores.AMARELO_BRILHANTE + v.getId() + Cores.RESET);
                System.out.println(Cores.info("Companhia: ") + Cores.AZUL_BRILHANTE + v.getCompanhiaAerea() + Cores.RESET);
                System.out.println(Cores.info("Rota: ") + Cores.CIANO_BRILHANTE + v.getOrigem() + " → " + v.getDestinoVoo() + Cores.RESET);
                System.out.println(Cores.info("Preço: ") + Cores.valor("R$ " + String.format("%.2f", v.getPreco())));
                System.out.println(Cores.separador("-", 60));
            }
            
        } catch (Exception e) {
            System.out.println(Cores.erro("ERRO ao buscar voos: ") + e.getMessage());
        }
    }
    
    private static void buscarHospedagens() {
        try {
            System.out.println("\n" + Cores.titulo("=== HOSPEDAGENS DISPONÍVEIS ==="));
            List<Hospedagem> hospedagens = hospedagemDAO.listarTodos();
            
            if (hospedagens.isEmpty()) {
                System.out.println(Cores.aviso("Nenhuma hospedagem cadastrada no momento."));
                return;
            }
            
            System.out.println(Cores.info("\nTotal de hospedagens: ") + Cores.BRANCO_BRILHANTE + hospedagens.size() + Cores.RESET);
            System.out.println(Cores.separador("-", 60));
            
            for (Hospedagem h : hospedagens) {
                System.out.println(Cores.info("ID: ") + Cores.AMARELO_BRILHANTE + h.getId() + Cores.RESET);
                System.out.println(Cores.info("Hotel: ") + Cores.ROXO_BRILHANTE + h.getNomeHotel() + Cores.RESET);
                System.out.println(Cores.info("Diárias: ") + Cores.BRANCO_BRILHANTE + h.getDiarias() + Cores.RESET);
                System.out.println(Cores.info("Preço por noite: ") + Cores.valor("R$ " + String.format("%.2f", h.getPrecoPorNoite())));
                double total = h.getPreco();
                System.out.println(Cores.info("Total: ") + Cores.valor("R$ " + String.format("%.2f", total)));
                System.out.println(Cores.separador("-", 60));
            }
            
        } catch (Exception e) {
            System.out.println(Cores.erro("ERRO ao buscar hospedagens: ") + e.getMessage());
        }
    }
    
    private static PacoteTuristico criarPacoteTuristico() {
        try {
            System.out.println("\n" + Cores.titulo("=== CRIAR PACOTE TURÍSTICO ==="));
            
            System.out.print(Cores.input("ID do Destino: "));
            Long destinoId = Long.parseLong(scanner.nextLine().trim());
            Destino destino = destinoDAO.buscarPorId(destinoId);
            
            if (destino == null) {
                System.out.println(Cores.erro("ERRO: Destino não encontrado."));
                return null;
            }
            
            System.out.print(Cores.input("ID do Voo: "));
            Long vooId = Long.parseLong(scanner.nextLine().trim());
            Voo voo = vooDAO.buscarPorId(vooId);
            
            if (voo == null) {
                System.out.println(Cores.erro("ERRO: Voo não encontrado."));
                return null;
            }
            
            System.out.print(Cores.input("ID da Hospedagem: "));
            Long hospedagemId = Long.parseLong(scanner.nextLine().trim());
            Hospedagem hospedagem = hospedagemDAO.buscarPorId(hospedagemId);
            
            if (hospedagem == null) {
                System.out.println(Cores.erro("ERRO: Hospedagem não encontrada."));
                return null;
            }
            
            PacoteTuristico pacote = PacoteTuristicoFactory.builder()
                .comDestino(destino)
                .comVoo(voo)
                .comHospedagem(hospedagem)
                .build();
            
            System.out.println(Cores.sucesso("\nPacote criado com sucesso!"));
            System.out.println(Cores.info("Destino: ") + Cores.CIANO_BRILHANTE + destino.getNome() + " - " + destino.getPais() + Cores.RESET);
            System.out.println(Cores.info("Voo: ") + Cores.AZUL_BRILHANTE + voo.getCompanhiaAerea() + Cores.RESET + " (" + voo.getOrigem() + 
                             " → " + voo.getDestinoVoo() + ")");
            System.out.println(Cores.info("Hospedagem: ") + Cores.ROXO_BRILHANTE + hospedagem.getNomeHotel() + Cores.RESET + " - " + 
                             hospedagem.getDiarias() + " diárias");
            System.out.println(Cores.info("Valor total: ") + Cores.valor("R$ " + String.format("%.2f", pacote.calcularValorTotal())));
            
            return pacote;
            
        } catch (NumberFormatException e) {
            System.out.println(Cores.erro("ERRO: ID inválido. Digite apenas números."));
            return null;
        } catch (Exception e) {
            System.out.println(Cores.erro("ERRO ao criar pacote: ") + e.getMessage());
            return null;
        }
    }
    
    private static Reserva criarReserva(Cliente cliente, PacoteTuristico pacote, 
                                       MetodoPagamento pagamento) {
        try {
            Reserva reserva = PacoteTuristicoFactory.builder()
                .comCliente(cliente)
                .comDestino(pacote.getDestino())
                .comVoo((Voo) pacote.getServicos().get(0))
                .comHospedagem((Hospedagem) pacote.getServicos().get(1))
                .comFormaPagamento(pagamento)
                .buildReserva();
            
            double valor = reserva.calcularValorTotal();
            
            System.out.println("\n" + Cores.titulo("=== CONFIRMAÇÃO DE RESERVA ==="));
            System.out.println(Cores.info("Valor total: ") + Cores.valor("R$ " + String.format("%.2f", valor)));
            System.out.println(Cores.info("Forma de pagamento: ") + Cores.ROXO_BRILHANTE + pagamento.obterDescricao() + Cores.RESET);
            System.out.print(Cores.input("\nConfirmar reserva? (S/N): "));
            
            String confirmacao = scanner.nextLine().trim();
            
            if (confirmacao.equalsIgnoreCase("S") || confirmacao.equalsIgnoreCase("SIM")) {
                boolean sucesso = pagamento.processarPagamento(valor);
                
                if (sucesso) {
                    System.out.println(Cores.VERDE_NEGRITO + "\n" + "=".repeat(50) + Cores.RESET);
                    System.out.println(Cores.sucesso("      RESERVA CONFIRMADA COM SUCESSO!"));
                    System.out.println(Cores.VERDE_NEGRITO + "=".repeat(50) + Cores.RESET);
                    System.out.println(Cores.info("Código da reserva: ") + Cores.AMARELO_NEGRITO + reserva.getCodigoReserva() + Cores.RESET);
                    return reserva;
                } else {
                    System.out.println(Cores.erro("ERRO: Falha ao processar pagamento."));
                    return null;
                }
            } else {
                System.out.println(Cores.aviso("Reserva cancelada."));
                return null;
            }
            
        } catch (Exception e) {
            System.out.println(Cores.erro("ERRO ao criar reserva: ") + e.getMessage());
            return null;
        }
    }
    
    private static void exibirReserva(Reserva reserva) {
        System.out.println("\n" + Cores.separador("=", 60));
        System.out.println(Cores.titulo("MINHA RESERVA"));
        System.out.println(Cores.separador("=", 60));
        System.out.println(Cores.info("Código: ") + Cores.AMARELO_NEGRITO + reserva.getCodigoReserva() + Cores.RESET);
        System.out.println(Cores.info("Cliente: ") + Cores.CIANO_BRILHANTE + reserva.getCliente().getNome() + Cores.RESET);
        System.out.println("\n" + reserva.getResumoReserva());
        System.out.println(Cores.info("\nValor total: ") + Cores.valor("R$ " + String.format("%.2f", reserva.calcularValorTotal())));
        System.out.println(Cores.info("Forma de pagamento: ") + Cores.ROXO_BRILHANTE + reserva.getMetodoPagamento().obterDescricao() + Cores.RESET);
        System.out.println(Cores.separador("=", 60));
    }
    
    private static void fecharRecursos() {
        if (scanner != null) {
            scanner.close();
        }
        GenericDAOImpl.fecharEntityManagerFactory();
    }
}
