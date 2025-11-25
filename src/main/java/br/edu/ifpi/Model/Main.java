package br.edu.ifpi.Model;

import br.edu.ifpi.dao.*;
import br.edu.ifpi.factory.*;
import br.edu.ifpi.util.Validador;
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
        
        System.out.println("=".repeat(60));
        System.out.println("BEM-VINDO AO SISTEMA GRAN TURISMO");
        System.out.println("=".repeat(60));
        System.out.print("\nConectando ao banco de dados");
        
        try {
            System.out.print(".");
            inicializarDAOs();
            System.out.print(".");
            System.out.println(" OK!");
            System.out.println("Sistema pronto!\n");
            
            Cliente cliente = autenticarCliente();
            if (cliente == null) {
                System.out.println("Encerrando sistema...");
                return;
            }
            
            // Verifica se cliente já tem método de pagamento configurado
            MetodoPagamento pagamento;
            if (cliente.temPagamentoConfigurado()) {
                pagamento = cliente.recuperarMetodoPagamento();
                System.out.println("✓ Método de pagamento carregado: " + pagamento.obterDescricao());
            } else {
                pagamento = configurarPagamento(cliente);
            }
            
            menuPrincipal(cliente, pagamento);
            
        } catch (Exception e) {
            System.out.println(" FALHOU!");
            System.err.println("\n⚠ ERRO: Não foi possível iniciar o sistema.");
            
            String mensagem = e.getMessage();
            if (mensagem != null && mensagem.contains("Communications link failure")) {
                System.err.println("Motivo: Não foi possível conectar ao banco de dados.");
                System.err.println("Solução: Verifique se o MySQL está rodando.");
            } else if (mensagem != null && mensagem.contains("Access denied")) {
                System.err.println("Motivo: Usuário ou senha incorretos.");
                System.err.println("Solução: Verifique as credenciais no arquivo persistence.xml");
            } else {
                System.err.println("Motivo: " + (mensagem != null ? mensagem : "Erro desconhecido"));
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
            System.out.println("\n=== AUTENTICAÇÃO ===");
            System.out.println("[1] Fazer Login");
            System.out.println("[2] Criar Nova Conta");
            System.out.println("[0] Sair");
            System.out.print("Escolha: ");
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
                System.out.println("Opção inválida!");
            }
        }
    }
    
    private static Cliente realizarLogin() {
        System.out.println("\n=== LOGIN ===");
        
        System.out.print("CPF: ");
        String cpf = scanner.nextLine().trim();
        
        if (!Validador.validarCPF(cpf)) {
            System.out.println("ERRO: CPF inválido.");
            return null;
        }
        cpf = Validador.formatarCPF(cpf);
        
        System.out.print("Senha: ");
        String senha = scanner.nextLine().trim();
        
        try {
            Cliente cliente = clienteDAO.fazerLogin(cpf, senha);
            if (cliente != null) {
                System.out.println("\n✓ Login realizado com sucesso!");
                System.out.println("Bem-vindo(a), " + cliente.getNome() + "!");
                return cliente;
            } else {
                System.out.println("ERRO: CPF ou senha incorretos.");
                return null;
            }
        } catch (Exception e) {
            System.out.println("ERRO ao fazer login: " + e.getMessage());
            return null;
        }
    }
    
    private static Cliente realizarCadastroCliente() {
        Cliente cliente = null;
        
        while (cliente == null) {
            System.out.println("\n=== CADASTRO DE NOVA CONTA ===");
            
            System.out.print("Nome completo: ");
            String nome = scanner.nextLine().trim();
            if (nome.isEmpty()) {
                System.out.println("ERRO: Nome não pode ser vazio.");
                continue;
            }
            
            String email = "";
            while (email.isEmpty()) {
                System.out.print("Email: ");
                email = scanner.nextLine().trim();
                if (!Validador.validarEmail(email)) {
                    System.out.println("ERRO: Email inválido. Use o formato: exemplo@dominio.com");
                    email = "";
                }
            }
            
            String telefone = "";
            while (telefone.isEmpty()) {
                System.out.print("Telefone (com DDD): ");
                telefone = scanner.nextLine().trim();
                if (!Validador.validarTelefone(telefone)) {
                    System.out.println("ERRO: Telefone inválido. Use o formato: (99) 99999-9999");
                    telefone = "";
                }
            }
            telefone = Validador.formatarTelefone(telefone);
            
            String cpf = "";
            while (cpf.isEmpty()) {
                System.out.print("CPF: ");
                cpf = scanner.nextLine().trim();
                if (!Validador.validarCPF(cpf)) {
                    System.out.println("ERRO: CPF inválido. Digite apenas números (11 dígitos).");
                    cpf = "";
                }
            }
            cpf = Validador.formatarCPF(cpf);
            
            // Verificar se CPF já existe
            try {
                if (clienteDAO.existeCpf(cpf)) {
                    System.out.println("ERRO: CPF já cadastrado. Use a opção de login.");
                    return null;
                }
            } catch (Exception e) {
                System.out.println("ERRO ao verificar CPF: " + e.getMessage());
                return null;
            }
            
            String senha = "";
            while (senha.isEmpty()) {
                System.out.print("Senha (mínimo 6 caracteres): ");
                senha = scanner.nextLine().trim();
                if (senha.length() < 6) {
                    System.out.println("ERRO: Senha deve ter no mínimo 6 caracteres.");
                    senha = "";
                    continue;
                }
                
                System.out.print("Confirme a senha: ");
                String confirmaSenha = scanner.nextLine().trim();
                if (!senha.equals(confirmaSenha)) {
                    System.out.println("ERRO: As senhas não coincidem.");
                    senha = "";
                }
            }
            
            try {
                cliente = EntidadeFactory.criarCliente(nome, email, cpf, senha);
                cliente.setTelefone(telefone);
                clienteDAO.salvar(cliente);
                System.out.println("\n✓ Conta criada com sucesso!");
                System.out.println("Bem-vindo(a), " + cliente.getNome() + "!");
            } catch (Exception e) {
                System.out.println("ERRO ao cadastrar: " + e.getMessage());
                cliente = null;
            }
        }
        
        return cliente;
    }
    
    private static MetodoPagamento configurarPagamento(Cliente cliente) {
        MetodoPagamento pagamento = null;
        
        while (pagamento == null) {
            System.out.println("\n=== CONFIGURAR FORMA DE PAGAMENTO ===");
            System.out.println("[1] Chave PIX");
            System.out.println("[2] Cartão de Crédito");
            System.out.print("Escolha: ");
            String opcao = scanner.nextLine().trim();

            try {
                if (opcao.equals("1")) {
                    System.out.print("Banco: ");
                    String banco = scanner.nextLine().trim();
                    
                    System.out.print("Agência: ");
                    String agencia = scanner.nextLine().trim();
                    
                    System.out.print("Conta: ");
                    String conta = scanner.nextLine().trim();
                    
                    System.out.print("Chave PIX (CPF, email ou telefone): ");
                    String chavePix = scanner.nextLine().trim();
                    
                    if (banco.isEmpty() || agencia.isEmpty() || conta.isEmpty() || chavePix.isEmpty()) {
                        System.out.println("ERRO: Todos os campos são obrigatórios.");
                        continue;
                    }
                    
                    pagamento = MetodoPagamentoFactory.criarTransferenciaBancaria(
                        banco, agencia, conta, chavePix
                    );
                    System.out.println("✓ Forma de pagamento PIX configurada!");
                    
                } else if (opcao.equals("2")) {
                    String numeroCartao = "";
                    while (numeroCartao.isEmpty()) {
                        System.out.print("Número do cartão (13-19 dígitos): ");
                        numeroCartao = scanner.nextLine().trim();
                        if (!Validador.validarNumeroCartao(numeroCartao)) {
                            System.out.println("ERRO: Número de cartão inválido.");
                            numeroCartao = "";
                        }
                    }
                    
                    System.out.print("Nome do titular: ");
                    String titular = scanner.nextLine().trim();
                    if (titular.isEmpty()) {
                        System.out.println("ERRO: Nome do titular não pode ser vazio.");
                        continue;
                    }
                    
                    String validade = "";
                    while (validade.isEmpty()) {
                        System.out.print("Validade (MM/AA): ");
                        validade = scanner.nextLine().trim();
                        if (!Validador.validarValidade(validade)) {
                            System.out.println("ERRO: Validade inválida ou cartão vencido. Use MM/AA.");
                            validade = "";
                        }
                    }
                    
                    String cvv = "";
                    while (cvv.isEmpty()) {
                        System.out.print("CVV (3-4 dígitos): ");
                        cvv = scanner.nextLine().trim();
                        if (!Validador.validarCVV(cvv)) {
                            System.out.println("ERRO: CVV inválido.");
                            cvv = "";
                        }
                    }
                    
                    pagamento = MetodoPagamentoFactory.criarCartaoCredito(
                        numeroCartao, titular, validade, cvv
                    );
                    System.out.println("✓ Cartão de crédito configurado!");
                    
                } else {
                    System.out.println("ERRO: Opção inválida.");
                }
            } catch (Exception e) {
                System.out.println("ERRO: " + e.getMessage());
                pagamento = null;
            }
        }
        
        // Salva o método de pagamento no cliente
        try {
            cliente.salvarMetodoPagamento(pagamento);
            clienteDAO.atualizar(cliente);
            System.out.println("✓ Método de pagamento salvo com sucesso!");
        } catch (Exception e) {
            System.out.println("⚠ Aviso: Não foi possível salvar o método de pagamento: " + e.getMessage());
        }
        
        return pagamento;
    }
    
    private static void menuPrincipal(Cliente cliente, MetodoPagamento pagamento) {
        Reserva reserva = null;
        PacoteTuristico pacote = null;
        
        while (true) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("MENU PRINCIPAL");
            System.out.println("=".repeat(60));
            System.out.println("[1] Minhas Informações");
            System.out.println("[2] Buscar Destinos Disponíveis");
            System.out.println("[3] Buscar Voos Disponíveis");
            System.out.println("[4] Buscar Hospedagens Disponíveis");
            System.out.println("[5] Criar Pacote Turístico");
            System.out.println("[8] Alterar Método de Pagamento");
            
            if (pacote != null && reserva == null) {
                System.out.println("[6] Confirmar Reserva");
            }
            if (reserva != null) {
                System.out.println("[7] Ver Minha Reserva");
            }
            
            System.out.println("[0] Sair");
            System.out.println("=".repeat(60));
            System.out.print("Escolha: ");
            
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
                        System.out.print("\nDeseja criar a reserva agora? (S/N): ");
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
                        System.out.println("Você já possui uma reserva confirmada.");
                    } else {
                        System.out.println("ERRO: Crie um pacote turístico primeiro.");
                    }
                    break;
                    
                case "7":
                    if (reserva != null) {
                        exibirReserva(reserva);
                    } else {
                        System.out.println("Você não possui reservas.");
                    }
                    break;
                
                case "8":
                    MetodoPagamento novoPagamento = configurarPagamento(cliente);
                    if (novoPagamento != null) {
                        pagamento = novoPagamento;
                    }
                    break;
                    
                case "0":
                    System.out.println("\nObrigado por usar o Gran Turismo!");
                    System.out.println("Até logo!");
                    return;
                    
                default:
                    System.out.println("ERRO: Opção inválida.");
            }
        }
    }
    
    private static void exibirInformacoesCliente(Cliente cliente, MetodoPagamento pagamento, 
                                                  PacoteTuristico pacote, Reserva reserva) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("MINHAS INFORMAÇÕES");
        System.out.println("=".repeat(60));
        System.out.println("Nome: " + cliente.getNome());
        System.out.println("Email: " + cliente.getEmail());
        System.out.println("CPF: " + cliente.getCpf());
        System.out.println("Telefone: " + cliente.getTelefone());
        System.out.println("Forma de pagamento: " + pagamento.obterDescricao());
        
        if (pacote != null) {
            System.out.println("\n--- PACOTE ATUAL ---");
            System.out.println("Destino: " + pacote.getDestino().getNome() + " - " + 
                             pacote.getDestino().getPais());
            System.out.println("Valor total: R$ " + String.format("%.2f", pacote.calcularValorTotal()));
        }
        
        if (reserva != null) {
            System.out.println("\nStatus: RESERVA CONFIRMADA");
        }
        System.out.println("=".repeat(60));
    }
    
    private static void buscarDestinos() {
        try {
            System.out.println("\n=== DESTINOS DISPONÍVEIS ===");
            List<Destino> destinos = destinoDAO.listarTodos();
            
            if (destinos.isEmpty()) {
                System.out.println("Nenhum destino cadastrado no momento.");
                return;
            }
            
            System.out.println("\nTotal de destinos: " + destinos.size());
            System.out.println("-".repeat(60));
            
            for (Destino d : destinos) {
                System.out.println("ID: " + d.getId());
                System.out.println("Destino: " + d.getNome() + " - " + d.getPais());
                System.out.println("Descrição: " + d.getDescricao());
                System.out.println("-".repeat(60));
            }
            
        } catch (Exception e) {
            System.out.println("ERRO ao buscar destinos: " + e.getMessage());
        }
    }
    
    private static void buscarVoos() {
        try {
            System.out.println("\n=== VOOS DISPONÍVEIS ===");
            List<Voo> voos = vooDAO.listarTodos();
            
            if (voos.isEmpty()) {
                System.out.println("Nenhum voo cadastrado no momento.");
                return;
            }
            
            System.out.println("\nTotal de voos: " + voos.size());
            System.out.println("-".repeat(60));
            
            for (Voo v : voos) {
                System.out.println("ID: " + v.getId());
                System.out.println("Companhia: " + v.getCompanhiaAerea());
                System.out.println("Rota: " + v.getOrigem() + " → " + v.getDestinoVoo());
                System.out.println("Preço: R$ " + String.format("%.2f", v.getPreco()));
                System.out.println("-".repeat(60));
            }
            
        } catch (Exception e) {
            System.out.println("ERRO ao buscar voos: " + e.getMessage());
        }
    }
    
    private static void buscarHospedagens() {
        try {
            System.out.println("\n=== HOSPEDAGENS DISPONÍVEIS ===");
            List<Hospedagem> hospedagens = hospedagemDAO.listarTodos();
            
            if (hospedagens.isEmpty()) {
                System.out.println("Nenhuma hospedagem cadastrada no momento.");
                return;
            }
            
            System.out.println("\nTotal de hospedagens: " + hospedagens.size());
            System.out.println("-".repeat(60));
            
            for (Hospedagem h : hospedagens) {
                System.out.println("ID: " + h.getId());
                System.out.println("Hotel: " + h.getNomeHotel());
                System.out.println("Diárias: " + h.getDiarias());
                System.out.println("Preço por noite: R$ " + String.format("%.2f", h.getPrecoPorNoite()));
                double total = h.getPreco();
                System.out.println("Total: R$ " + String.format("%.2f", total));
                System.out.println("-".repeat(60));
            }
            
        } catch (Exception e) {
            System.out.println("ERRO ao buscar hospedagens: " + e.getMessage());
        }
    }
    
    private static PacoteTuristico criarPacoteTuristico() {
        try {
            System.out.println("\n=== CRIAR PACOTE TURÍSTICO ===");
            
            System.out.print("ID do Destino: ");
            Long destinoId = Long.parseLong(scanner.nextLine().trim());
            Destino destino = destinoDAO.buscarPorId(destinoId);
            
            if (destino == null) {
                System.out.println("ERRO: Destino não encontrado.");
                return null;
            }
            
            System.out.print("ID do Voo: ");
            Long vooId = Long.parseLong(scanner.nextLine().trim());
            Voo voo = vooDAO.buscarPorId(vooId);
            
            if (voo == null) {
                System.out.println("ERRO: Voo não encontrado.");
                return null;
            }
            
            System.out.print("ID da Hospedagem: ");
            Long hospedagemId = Long.parseLong(scanner.nextLine().trim());
            Hospedagem hospedagem = hospedagemDAO.buscarPorId(hospedagemId);
            
            if (hospedagem == null) {
                System.out.println("ERRO: Hospedagem não encontrada.");
                return null;
            }
            
            PacoteTuristico pacote = PacoteTuristicoFactory.builder()
                .comDestino(destino)
                .comVoo(voo)
                .comHospedagem(hospedagem)
                .build();
            
            System.out.println("\n✓ Pacote criado com sucesso!");
            System.out.println("Destino: " + destino.getNome() + " - " + destino.getPais());
            System.out.println("Voo: " + voo.getCompanhiaAerea() + " (" + voo.getOrigem() + 
                             " → " + voo.getDestinoVoo() + ")");
            System.out.println("Hospedagem: " + hospedagem.getNomeHotel() + " - " + 
                             hospedagem.getDiarias() + " diárias");
            System.out.println("Valor total: R$ " + String.format("%.2f", pacote.calcularValorTotal()));
            
            return pacote;
            
        } catch (NumberFormatException e) {
            System.out.println("ERRO: ID inválido. Digite apenas números.");
            return null;
        } catch (Exception e) {
            System.out.println("ERRO ao criar pacote: " + e.getMessage());
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
            
            System.out.println("\n=== CONFIRMAÇÃO DE RESERVA ===");
            System.out.println("Valor total: R$ " + String.format("%.2f", valor));
            System.out.println("Forma de pagamento: " + pagamento.obterDescricao());
            System.out.print("\nConfirmar reserva? (S/N): ");
            
            String confirmacao = scanner.nextLine().trim();
            
            if (confirmacao.equalsIgnoreCase("S") || confirmacao.equalsIgnoreCase("SIM")) {
                boolean sucesso = pagamento.processarPagamento(valor);
                
                if (sucesso) {
                    System.out.println("\n✓ RESERVA CONFIRMADA COM SUCESSO!");
                    System.out.println("Código da reserva: " + reserva.getCodigoReserva());
                    return reserva;
                } else {
                    System.out.println("ERRO: Falha ao processar pagamento.");
                    return null;
                }
            } else {
                System.out.println("Reserva cancelada.");
                return null;
            }
            
        } catch (Exception e) {
            System.out.println("ERRO ao criar reserva: " + e.getMessage());
            return null;
        }
    }
    
    private static void exibirReserva(Reserva reserva) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("MINHA RESERVA");
        System.out.println("=".repeat(60));
        System.out.println("Código: " + reserva.getCodigoReserva());
        System.out.println("Cliente: " + reserva.getCliente().getNome());
        System.out.println("\n" + reserva.getResumoReserva());
        System.out.println("\nValor total: R$ " + String.format("%.2f", reserva.calcularValorTotal()));
        System.out.println("Forma de pagamento: " + reserva.getMetodoPagamento().obterDescricao());
        System.out.println("=".repeat(60));
    }
    
    private static void fecharRecursos() {
        if (scanner != null) {
            scanner.close();
        }
        GenericDAOImpl.fecharEntityManagerFactory();
    }
}
