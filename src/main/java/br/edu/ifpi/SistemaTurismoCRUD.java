package br.edu.ifpi;

import br.edu.ifpi.service.TurismoService;
import java.util.Scanner;
import java.io.InputStream;
import java.util.logging.LogManager;

public class SistemaTurismoCRUD {
    
    private static TurismoService turismoService;
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        configurarLogs();
        
        System.out.println("=== SISTEMA DE TURISMO GRAN TURISMO ===");
        System.out.print("Inicializando sistema");
        
        try {
            System.out.print(".");
            turismoService = new TurismoService();
            System.out.print(".");
            System.out.println(" OK!");
            System.out.println("Sistema pronto para uso!\n");
            menuPrincipal();
        } catch (Exception e) {
            System.out.println(" FALHOU!");
            System.err.println("\n⚠ ERRO: Não foi possível iniciar o sistema.");
            
            String mensagem = e.getMessage();
            if (mensagem != null && mensagem.contains("Communications link failure")) {
                System.err.println("Motivo: Não foi possível conectar ao banco de dados.");
                System.err.println("Solução: Verifique se o MySQL está rodando e as credenciais estão corretas.");
            } else if (mensagem != null && mensagem.contains("Access denied")) {
                System.err.println("Motivo: Usuário ou senha incorretos.");
                System.err.println("Solução: Verifique as credenciais no arquivo persistence.xml");
            } else if (mensagem != null && mensagem.contains("Unknown database")) {
                System.err.println("Motivo: Banco de dados 'gran_turismo' não encontrado.");
                System.err.println("Solução: Execute o script 'criar_banco_completo.sql' primeiro.");
            } else {
                System.err.println("Motivo: " + (mensagem != null ? mensagem : "Erro desconhecido"));
            }
            System.err.println("\nPor favor, corrija o problema e tente novamente.");
        } finally {
            if (turismoService != null) {
                turismoService.fecharRecursos();
            }
            if (scanner != null) {
                scanner.close();
            }
        }
    }
    
    private static void configurarLogs() {
        try {
            InputStream configStream = SistemaTurismoCRUD.class
                .getClassLoader()
                .getResourceAsStream("logging.properties");
            if (configStream != null) {
                LogManager.getLogManager().readConfiguration(configStream);
            }
        } catch (Exception e) {
            // Ignora erro de configuração de log
        }
    }
    
    private static void menuPrincipal() {
        while (true) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("GRAN TURISMO - MENU PRINCIPAL");
            System.out.println("=".repeat(50));
            System.out.println("1. Gerenciar Clientes");
            System.out.println("2. Gerenciar Destinos");
            System.out.println("3. Gerenciar Voos");
            System.out.println("4. Gerenciar Hospedagens");
            System.out.println("5. Processar Pagamentos");
            System.out.println("6. Pacotes Turísticos");
            System.out.println("7. Relatórios e Consultas");
            System.out.println("0. Sair");
            System.out.println("=".repeat(50));
            System.out.print("Escolha uma opção: ");
            
            String opcao = scanner.nextLine();
            
            switch (opcao) {
                case "1":
                    turismoService.menuClientes();
                    break;
                case "2":
                    turismoService.menuDestinos();
                    break;
                case "3":
                    turismoService.menuVoos();
                    break;
                case "4":
                    turismoService.menuHospedagens();
                    break;
                case "5":
                    turismoService.menuPagamentos();
                    break;
                case "6":
                    turismoService.menuPacotes();
                    break;
                case "7":
                    menuRelatorios();
                    break;
                case "0":
                    System.out.println("Obrigado por usar o Gran Turismo!");
                    return;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        }
    }
    
    private static void menuRelatorios() {
        while (true) {
            System.out.println("\n=== RELATÓRIOS E CONSULTAS ===");
            System.out.println("1. Total de Clientes Cadastrados");
            System.out.println("2. Total de Destinos por País");
            System.out.println("3. Listar Todos os Voos");
            System.out.println("4. Listar Todas as Hospedagens");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            
            String opcao = scanner.nextLine();
            
            switch (opcao) {
                case "1":
                    turismoService.relatorioTotalClientes();
                    break;
                case "2":
                    turismoService.relatorioDestinosPorPais();
                    break;
                case "3":
                    turismoService.relatorioTodosVoos();
                    break;
                case "4":
                    turismoService.relatorioTodasHospedagens();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }
}
