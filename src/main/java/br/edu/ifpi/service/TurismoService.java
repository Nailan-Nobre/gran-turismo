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
                default: System.out.println("❌ Opção inválida!");
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
                System.out.println("❌ Erro: CPF já está cadastrado!");
                return;
            }
            
            Cliente cliente = new Cliente(nome, email, cpf);
            clienteDAO.salvar(cliente);
            System.out.println("✅ Cliente cadastrado com sucesso! ID: " + cliente.getId());
        } catch (Exception e) {
            System.out.println("❌ Erro ao cadastrar cliente: " + e.getMessage());
        }
    }
    
    private void listarClientes() {
        System.out.println("\n--- Lista de Clientes ---");
        try {
            List<Cliente> clientes = clienteDAO.listarTodos();
            if (clientes.isEmpty()) {
                System.out.println("📋 Nenhum cliente cadastrado.");
            } else {
                for (Cliente cliente : clientes) {
                    System.out.println("ID: " + cliente.getId() + 
                                     " | Nome: " + cliente.getNome() + 
                                     " | Email: " + cliente.getEmail() + 
                                     " | CPF: " + cliente.getCpf());
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Erro ao listar clientes: " + e.getMessage());
        }
    }
    
    private void buscarClientePorCpf() {
        System.out.print("Digite o CPF: ");
        String cpf = scanner.nextLine();
        
        try {
            Cliente cliente = clienteDAO.buscarPorCpf(cpf);
            if (cliente != null) {
                System.out.println("✅ Cliente encontrado:");
                System.out.println("ID: " + cliente.getId() + 
                                 " | Nome: " + cliente.getNome() + 
                                 " | Email: " + cliente.getEmail() + 
                                 " | CPF: " + cliente.getCpf());
            } else {
                System.out.println("❌ Cliente não encontrado.");
            }
        } catch (Exception e) {
            System.out.println("❌ Erro ao buscar cliente: " + e.getMessage());
        }
    }
    
    private void buscarClientePorNome() {
        System.out.print("Digite o nome (ou parte): ");
        String nome = scanner.nextLine();
        
        try {
            List<Cliente> clientes = clienteDAO.buscarPorNome(nome);
            if (clientes.isEmpty()) {
                System.out.println("❌ Nenhum cliente encontrado.");
            } else {
                System.out.println("✅ Clientes encontrados:");
                for (Cliente cliente : clientes) {
                    System.out.println("ID: " + cliente.getId() + 
                                     " | Nome: " + cliente.getNome() + 
                                     " | Email: " + cliente.getEmail() + 
                                     " | CPF: " + cliente.getCpf());
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Erro ao buscar clientes: " + e.getMessage());
        }
    }
    
    private void atualizarCliente() {
        System.out.print("Digite o ID do cliente: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            Cliente cliente = clienteDAO.buscarPorId(id);
            
            if (cliente == null) {
                System.out.println("❌ Cliente não encontrado.");
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
            System.out.println("✅ Cliente atualizado com sucesso!");
            
        } catch (NumberFormatException e) {
            System.out.println("❌ ID inválido!");
        } catch (Exception e) {
            System.out.println("❌ Erro ao atualizar cliente: " + e.getMessage());
        }
    }
    
    private void removerCliente() {
        System.out.print("Digite o ID do cliente: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            Cliente cliente = clienteDAO.buscarPorId(id);
            
            if (cliente == null) {
                System.out.println("❌ Cliente não encontrado.");
                return;
            }
            
            System.out.println("Cliente: " + cliente.getNome() + " - " + cliente.getEmail());
            System.out.print("Confirma a remoção? (s/n): ");
            String confirmacao = scanner.nextLine();
            
            if (confirmacao.equalsIgnoreCase("s") || confirmacao.equalsIgnoreCase("sim")) {
                clienteDAO.remover(cliente);
                System.out.println("✅ Cliente removido com sucesso!");
            } else {
                System.out.println("❌ Remoção cancelada.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("❌ ID inválido!");
        } catch (Exception e) {
            System.out.println("❌ Erro ao remover cliente: " + e.getMessage());
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
                default: System.out.println("❌ Opção inválida!");
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
            System.out.println("✅ Destino cadastrado com sucesso! ID: " + destino.getId());
        } catch (Exception e) {
            System.out.println("❌ Erro ao cadastrar destino: " + e.getMessage());
        }
    }
    
    private void listarDestinos() {
        System.out.println("\n--- Lista de Destinos ---");
        try {
            List<Destino> destinos = destinoDAO.listarTodos();
            if (destinos.isEmpty()) {
                System.out.println("📋 Nenhum destino cadastrado.");
            } else {
                for (Destino destino : destinos) {
                    System.out.println("ID: " + destino.getId() + 
                                     " | " + destino.getDescricao());
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Erro ao listar destinos: " + e.getMessage());
        }
    }
    
    private void buscarDestinosPorPais() {
        System.out.print("Digite o país: ");
        String pais = scanner.nextLine();
        
        try {
            List<Destino> destinos = destinoDAO.buscarPorPais(pais);
            if (destinos.isEmpty()) {
                System.out.println("❌ Nenhum destino encontrado para este país.");
            } else {
                System.out.println("✅ Destinos encontrados:");
                for (Destino destino : destinos) {
                    System.out.println("ID: " + destino.getId() + 
                                     " | " + destino.getDescricao());
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Erro ao buscar destinos: " + e.getMessage());
        }
    }
    
    private void buscarDestinosPorNome() {
        System.out.print("Digite o nome da cidade: ");
        String nome = scanner.nextLine();
        
        try {
            List<Destino> destinos = destinoDAO.buscarPorNome(nome);
            if (destinos.isEmpty()) {
                System.out.println("❌ Nenhum destino encontrado.");
            } else {
                System.out.println("✅ Destinos encontrados:");
                for (Destino destino : destinos) {
                    System.out.println("ID: " + destino.getId() + 
                                     " | " + destino.getDescricao());
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Erro ao buscar destinos: " + e.getMessage());
        }
    }
    
    private void atualizarDestino() {
        System.out.print("Digite o ID do destino: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            Destino destino = destinoDAO.buscarPorId(id);
            
            if (destino == null) {
                System.out.println("❌ Destino não encontrado.");
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
            System.out.println("✅ Destino atualizado com sucesso!");
            
        } catch (NumberFormatException e) {
            System.out.println("❌ ID inválido!");
        } catch (Exception e) {
            System.out.println("❌ Erro ao atualizar destino: " + e.getMessage());
        }
    }
    
    private void removerDestino() {
        System.out.print("Digite o ID do destino: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            Destino destino = destinoDAO.buscarPorId(id);
            
            if (destino == null) {
                System.out.println("❌ Destino não encontrado.");
                return;
            }
            
            System.out.println("Destino: " + destino.getDescricao());
            System.out.print("Confirma a remoção? (s/n): ");
            String confirmacao = scanner.nextLine();
            
            if (confirmacao.equalsIgnoreCase("s") || confirmacao.equalsIgnoreCase("sim")) {
                destinoDAO.remover(destino);
                System.out.println("✅ Destino removido com sucesso!");
            } else {
                System.out.println("❌ Remoção cancelada.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("❌ ID inválido!");
        } catch (Exception e) {
            System.out.println("❌ Erro ao remover destino: " + e.getMessage());
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
