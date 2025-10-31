package br.edu.ifpi.service;

import br.edu.ifpi.Model.*;
import br.edu.ifpi.dao.*;
import java.util.List;
import java.util.Scanner;

/**
 * Serviço principal para gerenciar operações CRUD do sistema de turismo
 */
public class TurismoService {
    
    private ClienteDAO clienteDAO;
    private DestinoDAO destinoDAO;
    private VooDAO vooDAO;
    private HospedagemDAO hospedagemDAO;
    private Scanner scanner;
    
    public TurismoService() {
        this.clienteDAO = new ClienteDAO();
        this.destinoDAO = new DestinoDAO();
        this.vooDAO = new VooDAO();
        this.hospedagemDAO = new HospedagemDAO();
        this.scanner = new Scanner(System.in);
    }
    
    // ==================== CLIENTES ====================
    
    public void menuClientes() {
        while (true) {
            System.out.println("\n=== GERENCIAR CLIENTES ===");
            System.out.println("1. Cadastrar Cliente");
            System.out.println("2. Listar Clientes");
            System.out.println("3. Buscar Cliente por CPF");
            System.out.println("4. Buscar Cliente por Nome");
            System.out.println("5. Atualizar Cliente");
            System.out.println("6. Remover Cliente");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            
            String opcao = scanner.nextLine();
            
            switch (opcao) {
                case "1": cadastrarCliente(); break;
                case "2": listarClientes(); break;
                case "3": buscarClientePorCpf(); break;
                case "4": buscarClientePorNome(); break;
                case "5": atualizarCliente(); break;
                case "6": removerCliente(); break;
                case "0": return;
                default: System.out.println("Opção inválida!");
            }
        }
    }
    
    private void cadastrarCliente() {
        System.out.println("\n--- Cadastrar Cliente ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();
        
        try {
            // Verificar se CPF já existe
            if (clienteDAO.existeCpf(cpf)) {
                System.out.println("ERRO: CPF já está cadastrado!");
                return;
            }
            
            Cliente cliente = new Cliente(nome, email, cpf);
            clienteDAO.salvar(cliente);
            System.out.println("Cliente cadastrado com sucesso! ID: " + cliente.getId());
        } catch (Exception e) {
            System.out.println("ERRO ao cadastrar cliente: " + e.getMessage());
        }
    }
    
    private void listarClientes() {
        System.out.println("\n--- Lista de Clientes ---");
        try {
            List<Cliente> clientes = clienteDAO.listarTodos();
            if (clientes.isEmpty()) {
                System.out.println("Nenhum cliente cadastrado.");
            } else {
                for (Cliente cliente : clientes) {
                    System.out.println("ID: " + cliente.getId() + 
                                     " | Nome: " + cliente.getNome() + 
                                     " | Email: " + cliente.getEmail() + 
                                     " | CPF: " + cliente.getCpf());
                }
            }
        } catch (Exception e) {
            System.out.println("ERRO ao listar clientes: " + e.getMessage());
        }
    }
    
    private void buscarClientePorCpf() {
        System.out.print("Digite o CPF: ");
        String cpf = scanner.nextLine();
        
        try {
            Cliente cliente = clienteDAO.buscarPorCpf(cpf);
            if (cliente != null) {
                System.out.println("Cliente encontrado:");
                System.out.println("ID: " + cliente.getId() + 
                                 " | Nome: " + cliente.getNome() + 
                                 " | Email: " + cliente.getEmail() + 
                                 " | CPF: " + cliente.getCpf());
            } else {
                System.out.println("Cliente não encontrado.");
            }
        } catch (Exception e) {
            System.out.println("ERRO ao buscar cliente: " + e.getMessage());
        }
    }
    
    private void buscarClientePorNome() {
        System.out.print("Digite o nome (ou parte): ");
        String nome = scanner.nextLine();
        
        try {
            List<Cliente> clientes = clienteDAO.buscarPorNome(nome);
            if (clientes.isEmpty()) {
                System.out.println("Nenhum cliente encontrado.");
            } else {
                System.out.println("Clientes encontrados:");
                for (Cliente cliente : clientes) {
                    System.out.println("ID: " + cliente.getId() + 
                                     " | Nome: " + cliente.getNome() + 
                                     " | Email: " + cliente.getEmail() + 
                                     " | CPF: " + cliente.getCpf());
                }
            }
        } catch (Exception e) {
            System.out.println("ERRO ao buscar clientes: " + e.getMessage());
        }
    }
    
    private void atualizarCliente() {
        System.out.print("Digite o ID do cliente: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            Cliente cliente = clienteDAO.buscarPorId(id);
            
            if (cliente == null) {
                System.out.println("Cliente não encontrado.");
                return;
            }
            
            System.out.println("Cliente atual: " + cliente.getNome() + " - " + cliente.getEmail());
            System.out.print("Novo nome (ou ENTER para manter): ");
            String novoNome = scanner.nextLine();
            if (!novoNome.trim().isEmpty()) {
                cliente.setNome(novoNome);
            }
            
            System.out.print("Novo email (ou ENTER para manter): ");
            String novoEmail = scanner.nextLine();
            if (!novoEmail.trim().isEmpty()) {
                cliente.setEmail(novoEmail);
            }
            
            clienteDAO.atualizar(cliente);
            System.out.println("Cliente atualizado com sucesso!");
            
        } catch (NumberFormatException e) {
            System.out.println("ID inválido!");
        } catch (Exception e) {
            System.out.println("ERRO ao atualizar cliente: " + e.getMessage());
        }
    }
    
    private void removerCliente() {
        System.out.print("Digite o ID do cliente: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            Cliente cliente = clienteDAO.buscarPorId(id);
            
            if (cliente == null) {
                System.out.println("Cliente não encontrado.");
                return;
            }
            
            System.out.println("Cliente: " + cliente.getNome() + " - " + cliente.getEmail());
            System.out.print("Confirma a remoção? (s/n): ");
            String confirmacao = scanner.nextLine();
            
            if (confirmacao.equalsIgnoreCase("s") || confirmacao.equalsIgnoreCase("sim")) {
                clienteDAO.remover(cliente);
                System.out.println("Cliente removido com sucesso!");
            } else {
                System.out.println("Remoção cancelada.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("ID inválido!");
        } catch (Exception e) {
            System.out.println("ERRO ao remover cliente: " + e.getMessage());
        }
    }
    
    // ==================== DESTINOS ====================
    
    public void menuDestinos() {
        while (true) {
            System.out.println("\n=== GERENCIAR DESTINOS ===");
            System.out.println("1. Cadastrar Destino");
            System.out.println("2. Listar Destinos");
            System.out.println("3. Buscar por País");
            System.out.println("4. Buscar por Nome");
            System.out.println("5. Atualizar Destino");
            System.out.println("6. Remover Destino");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            
            String opcao = scanner.nextLine();
            
            switch (opcao) {
                case "1": cadastrarDestino(); break;
                case "2": listarDestinos(); break;
                case "3": buscarDestinosPorPais(); break;
                case "4": buscarDestinosPorNome(); break;
                case "5": atualizarDestino(); break;
                case "6": removerDestino(); break;
                case "0": return;
                default: System.out.println("Opção inválida!");
            }
        }
    }
    
    private void cadastrarDestino() {
        System.out.println("\n--- Cadastrar Destino ---");
        System.out.print("Nome da cidade: ");
        String nome = scanner.nextLine();
        
        System.out.print("País: ");
        String pais = scanner.nextLine();
        
        try {
            Destino destino = new Destino(nome, pais);
            destinoDAO.salvar(destino);
            System.out.println("Destino cadastrado com sucesso! ID: " + destino.getId());
        } catch (Exception e) {
            System.out.println("ERRO ao cadastrar destino: " + e.getMessage());
        }
    }
    
    private void listarDestinos() {
        System.out.println("\n--- Lista de Destinos ---");
        try {
            List<Destino> destinos = destinoDAO.listarTodos();
            if (destinos.isEmpty()) {
                System.out.println("Nenhum destino cadastrado.");
            } else {
                for (Destino destino : destinos) {
                    System.out.println("ID: " + destino.getId() + 
                                     " | " + destino.getDescricao());
                }
            }
        } catch (Exception e) {
            System.out.println("ERRO ao listar destinos: " + e.getMessage());
        }
    }
    
    private void buscarDestinosPorPais() {
        System.out.print("Digite o país: ");
        String pais = scanner.nextLine();
        
        try {
            List<Destino> destinos = destinoDAO.buscarPorPais(pais);
            if (destinos.isEmpty()) {
                System.out.println("Nenhum destino encontrado para este país.");
            } else {
                System.out.println("Destinos encontrados:");
                for (Destino destino : destinos) {
                    System.out.println("ID: " + destino.getId() + 
                                     " | " + destino.getDescricao());
                }
            }
        } catch (Exception e) {
            System.out.println("ERRO ao buscar destinos: " + e.getMessage());
        }
    }
    
    private void buscarDestinosPorNome() {
        System.out.print("Digite o nome da cidade: ");
        String nome = scanner.nextLine();
        
        try {
            List<Destino> destinos = destinoDAO.buscarPorNome(nome);
            if (destinos.isEmpty()) {
                System.out.println("Nenhum destino encontrado.");
            } else {
                System.out.println("Destinos encontrados:");
                for (Destino destino : destinos) {
                    System.out.println("ID: " + destino.getId() + 
                                     " | " + destino.getDescricao());
                }
            }
        } catch (Exception e) {
            System.out.println("ERRO ao buscar destinos: " + e.getMessage());
        }
    }
    
    private void atualizarDestino() {
        System.out.print("Digite o ID do destino: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            Destino destino = destinoDAO.buscarPorId(id);
            
            if (destino == null) {
                System.out.println("Destino não encontrado.");
                return;
            }
            
            System.out.println("Destino atual: " + destino.getDescricao());
            System.out.print("Novo nome (ou ENTER para manter): ");
            String novoNome = scanner.nextLine();
            if (!novoNome.trim().isEmpty()) {
                destino.setNome(novoNome);
            }
            
            System.out.print("Novo país (ou ENTER para manter): ");
            String novoPais = scanner.nextLine();
            if (!novoPais.trim().isEmpty()) {
                destino.setPais(novoPais);
            }
            
            destinoDAO.atualizar(destino);
            System.out.println("Destino atualizado com sucesso!");
            
        } catch (NumberFormatException e) {
            System.out.println("ID inválido!");
        } catch (Exception e) {
            System.out.println("ERRO ao atualizar destino: " + e.getMessage());
        }
    }
    
    private void removerDestino() {
        System.out.print("Digite o ID do destino: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            Destino destino = destinoDAO.buscarPorId(id);
            
            if (destino == null) {
                System.out.println("Destino não encontrado.");
                return;
            }
            
            System.out.println("Destino: " + destino.getDescricao());
            System.out.print("Confirma a remoção? (s/n): ");
            String confirmacao = scanner.nextLine();
            
            if (confirmacao.equalsIgnoreCase("s") || confirmacao.equalsIgnoreCase("sim")) {
                destinoDAO.remover(destino);
                System.out.println("Destino removido com sucesso!");
            } else {
                System.out.println("Remoção cancelada.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("ID inválido!");
        } catch (Exception e) {
            System.out.println("ERRO ao remover destino: " + e.getMessage());
        }
    }
    
    // ==================== VOOS ====================
    
    public void menuVoos() {
        while (true) {
            System.out.println("\n=== GERENCIAR VOOS ===");
            System.out.println("1. Cadastrar Voo");
            System.out.println("2. Listar Voos");
            System.out.println("3. Buscar Voo por ID");
            System.out.println("4. Remover Voo");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            
            String opcao = scanner.nextLine();
            
            switch (opcao) {
                case "1": cadastrarVoo(); break;
                case "2": listarVoos(); break;
                case "3": buscarVooPorId(); break;
                case "4": removerVoo(); break;
                case "0": return;
                default: System.out.println("Opção inválida!");
            }
        }
    }
    
    private void cadastrarVoo() {
        System.out.println("\n--- Cadastrar Voo ---");
        System.out.print("Companhia Aérea: ");
        String companhia = scanner.nextLine();
        
        System.out.print("Origem: ");
        String origem = scanner.nextLine();
        
        System.out.print("Destino: ");
        String destino = scanner.nextLine();
        
        System.out.print("Preço da Passagem: ");
        try {
            double preco = Double.parseDouble(scanner.nextLine());
            
            Voo voo = new Voo(companhia, origem, destino, preco);
            vooDAO.salvar(voo);
            System.out.println("Voo cadastrado com sucesso! ID: " + voo.getId());
        } catch (NumberFormatException e) {
            System.out.println("Preço inválido!");
        } catch (Exception e) {
            System.out.println("ERRO ao cadastrar voo: " + e.getMessage());
        }
    }
    
    private void listarVoos() {
        System.out.println("\n--- Lista de Voos ---");
        try {
            List<Voo> voos = vooDAO.listarTodos();
            if (voos.isEmpty()) {
                System.out.println("Nenhum voo cadastrado.");
            } else {
                for (Voo voo : voos) {
                    System.out.println("ID: " + voo.getId() + 
                                     " | " + voo.getCompanhiaAerea() +
                                     " | " + voo.getOrigem() + " -> " + voo.getDestinoVoo() +
                                     " | R$ " + String.format("%.2f", voo.getPrecoPassagem()));
                }
            }
        } catch (Exception e) {
            System.out.println("ERRO ao listar voos: " + e.getMessage());
        }
    }
    
    private void buscarVooPorId() {
        System.out.print("Digite o ID do voo: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            Voo voo = vooDAO.buscarPorId(id);
            
            if (voo != null) {
                System.out.println("Voo encontrado:");
                System.out.println("ID: " + voo.getId() + 
                                 " | " + voo.getCompanhiaAerea() +
                                 " | " + voo.getOrigem() + " -> " + voo.getDestinoVoo() +
                                 " | R$ " + String.format("%.2f", voo.getPrecoPassagem()));
            } else {
                System.out.println("Voo não encontrado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido!");
        } catch (Exception e) {
            System.out.println("ERRO ao buscar voo: " + e.getMessage());
        }
    }
    
    private void removerVoo() {
        System.out.print("Digite o ID do voo: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            Voo voo = vooDAO.buscarPorId(id);
            
            if (voo == null) {
                System.out.println("Voo não encontrado.");
                return;
            }
            
            System.out.println("Voo: " + voo.getCompanhiaAerea() + " - " + voo.getOrigem() + " -> " + voo.getDestinoVoo());
            System.out.print("Confirma a remoção? (s/n): ");
            String confirmacao = scanner.nextLine();
            
            if (confirmacao.equalsIgnoreCase("s") || confirmacao.equalsIgnoreCase("sim")) {
                vooDAO.remover(voo);
                System.out.println("Voo removido com sucesso!");
            } else {
                System.out.println("Remoção cancelada.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("ID inválido!");
        } catch (Exception e) {
            System.out.println("ERRO ao remover voo: " + e.getMessage());
        }
    }
    
    // ==================== HOSPEDAGENS ====================
    
    public void menuHospedagens() {
        while (true) {
            System.out.println("\n=== GERENCIAR HOSPEDAGENS ===");
            System.out.println("1. Cadastrar Hospedagem");
            System.out.println("2. Listar Hospedagens");
            System.out.println("3. Buscar Hospedagem por ID");
            System.out.println("4. Remover Hospedagem");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            
            String opcao = scanner.nextLine();
            
            switch (opcao) {
                case "1": cadastrarHospedagem(); break;
                case "2": listarHospedagens(); break;
                case "3": buscarHospedagemPorId(); break;
                case "4": removerHospedagem(); break;
                case "0": return;
                default: System.out.println("Opção inválida!");
            }
        }
    }
    
    private void cadastrarHospedagem() {
        System.out.println("\n--- Cadastrar Hospedagem ---");
        System.out.print("Nome do Hotel: ");
        String nomeHotel = scanner.nextLine();
        
        System.out.print("Número de Diárias: ");
        try {
            int diarias = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Preço por Noite: ");
            double precoPorNoite = Double.parseDouble(scanner.nextLine());
            
            Hospedagem hospedagem = new Hospedagem(nomeHotel, diarias, precoPorNoite);
            hospedagemDAO.salvar(hospedagem);
            System.out.println("Hospedagem cadastrada com sucesso! ID: " + hospedagem.getId());
        } catch (NumberFormatException e) {
            System.out.println("Valores inválidos!");
        } catch (Exception e) {
            System.out.println("ERRO ao cadastrar hospedagem: " + e.getMessage());
        }
    }
    
    private void listarHospedagens() {
        System.out.println("\n--- Lista de Hospedagens ---");
        try {
            List<Hospedagem> hospedagens = hospedagemDAO.listarTodos();
            if (hospedagens.isEmpty()) {
                System.out.println("Nenhuma hospedagem cadastrada.");
            } else {
                for (Hospedagem hosp : hospedagens) {
                    double custoTotal = hosp.getPrecoPorNoite() * hosp.getDiarias();
                    System.out.println("ID: " + hosp.getId() + 
                                     " | " + hosp.getNomeHotel() +
                                     " | " + hosp.getDiarias() + " diárias" +
                                     " | R$ " + String.format("%.2f", hosp.getPrecoPorNoite()) + "/noite" +
                                     " | Total: R$ " + String.format("%.2f", custoTotal));
                }
            }
        } catch (Exception e) {
            System.out.println("ERRO ao listar hospedagens: " + e.getMessage());
        }
    }
    
    private void buscarHospedagemPorId() {
        System.out.print("Digite o ID da hospedagem: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            Hospedagem hosp = hospedagemDAO.buscarPorId(id);
            
            if (hosp != null) {
                double custoTotal = hosp.getPrecoPorNoite() * hosp.getDiarias();
                System.out.println("Hospedagem encontrada:");
                System.out.println("ID: " + hosp.getId() + 
                                 " | " + hosp.getNomeHotel() +
                                 " | " + hosp.getDiarias() + " diárias" +
                                 " | R$ " + String.format("%.2f", hosp.getPrecoPorNoite()) + "/noite" +
                                 " | Total: R$ " + String.format("%.2f", custoTotal));
            } else {
                System.out.println("Hospedagem não encontrada.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido!");
        } catch (Exception e) {
            System.out.println("ERRO ao buscar hospedagem: " + e.getMessage());
        }
    }
    
    private void removerHospedagem() {
        System.out.print("Digite o ID da hospedagem: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            Hospedagem hosp = hospedagemDAO.buscarPorId(id);
            
            if (hosp == null) {
                System.out.println("Hospedagem não encontrada.");
                return;
            }
            
            System.out.println("Hospedagem: " + hosp.getNomeHotel() + " - " + hosp.getDiarias() + " diárias");
            System.out.print("Confirma a remoção? (s/n): ");
            String confirmacao = scanner.nextLine();
            
            if (confirmacao.equalsIgnoreCase("s") || confirmacao.equalsIgnoreCase("sim")) {
                hospedagemDAO.remover(hosp);
                System.out.println("Hospedagem removida com sucesso!");
            } else {
                System.out.println("Remoção cancelada.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("ID inválido!");
        } catch (Exception e) {
            System.out.println("ERRO ao remover hospedagem: " + e.getMessage());
        }
    }
    
    // ==================== RELATÓRIOS ====================
    
    public void relatorioTotalClientes() {
        try {
            List<Cliente> clientes = clienteDAO.listarTodos();
            System.out.println("\n=== TOTAL DE CLIENTES CADASTRADOS ===");
            System.out.println("Total: " + clientes.size() + " cliente(s)");
        } catch (Exception e) {
            System.out.println("ERRO ao gerar relatório: " + e.getMessage());
        }
    }
    
    public void relatorioDestinosPorPais() {
        try {
            List<Destino> destinos = destinoDAO.listarTodos();
            System.out.println("\n=== DESTINOS POR PAÍS ===");
            
            // Agrupar destinos por país manualmente
            java.util.Map<String, java.util.List<String>> destinosPorPais = new java.util.HashMap<>();
            for (Destino dest : destinos) {
                destinosPorPais.computeIfAbsent(dest.getPais(), k -> new java.util.ArrayList<>()).add(dest.getNome());
            }
            
            if (destinosPorPais.isEmpty()) {
                System.out.println("Nenhum destino cadastrado.");
            } else {
                for (java.util.Map.Entry<String, java.util.List<String>> entry : destinosPorPais.entrySet()) {
                    System.out.println("\n" + entry.getKey() + " (" + entry.getValue().size() + " destino(s)):");
                    for (String cidade : entry.getValue()) {
                        System.out.println("  - " + cidade);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("ERRO ao gerar relatório: " + e.getMessage());
        }
    }
    
    public void relatorioTodosVoos() {
        try {
            List<Voo> voos = vooDAO.listarTodos();
            System.out.println("\n=== RELATÓRIO DE TODOS OS VOOS ===");
            
            if (voos.isEmpty()) {
                System.out.println("Nenhum voo cadastrado.");
            } else {
                System.out.println("Total de voos: " + voos.size());
                double somaPrecos = 0;
                for (Voo voo : voos) {
                    somaPrecos += voo.getPrecoPassagem();
                    System.out.println("- " + voo.getCompanhiaAerea() + ": " + 
                                     voo.getOrigem() + " -> " + voo.getDestinoVoo() +
                                     " - R$ " + String.format("%.2f", voo.getPrecoPassagem()));
                }
                System.out.println("\nPreço médio: R$ " + String.format("%.2f", somaPrecos / voos.size()));
            }
        } catch (Exception e) {
            System.out.println("ERRO ao gerar relatório: " + e.getMessage());
        }
    }
    
    public void relatorioTodasHospedagens() {
        try {
            List<Hospedagem> hospedagens = hospedagemDAO.listarTodos();
            System.out.println("\n=== RELATÓRIO DE TODAS AS HOSPEDAGENS ===");
            
            if (hospedagens.isEmpty()) {
                System.out.println("Nenhuma hospedagem cadastrada.");
            } else {
                System.out.println("Total de hospedagens: " + hospedagens.size());
                double somaCustos = 0;
                for (Hospedagem hosp : hospedagens) {
                    double custoTotal = hosp.getPrecoPorNoite() * hosp.getDiarias();
                    somaCustos += custoTotal;
                    System.out.println("- " + hosp.getNomeHotel() + ": " +
                                     hosp.getDiarias() + " diárias - " +
                                     "R$ " + String.format("%.2f", custoTotal) + " total");
                }
                System.out.println("\nCusto médio: R$ " + String.format("%.2f", somaCustos / hospedagens.size()));
            }
        } catch (Exception e) {
            System.out.println("ERRO ao gerar relatório: " + e.getMessage());
        }
    }
    
    // ==================== UTILITÁRIOS ====================
    
    public void fecharRecursos() {
        if (scanner != null) {
            scanner.close();
        }
        GenericDAOImpl.fecharEntityManagerFactory();
    }
}
