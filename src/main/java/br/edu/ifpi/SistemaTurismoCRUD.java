package br.edu.ifpi;

import br.edu.ifpi.service.TurismoService;
import java.util.Scanner;

/**
 * Aplicação principal do sistema de turismo com operações CRUD completas
 */
public class SistemaTurismoCRUD {
    
    private static TurismoService turismoService;
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("=== SISTEMA DE TURISMO GRAN TURISMO ===");
        System.out.println("Sistema completo com operações CRUD");
        
        try {
            // Inicializa o serviço apenas quando necessário
            turismoService = new TurismoService();
            menuPrincipal();
        } catch (Exception e) {
            System.err.println("ERRO: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (turismoService != null) {
                turismoService.fecharRecursos();
            }
            if (scanner != null) {
                scanner.close();
            }
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
            System.out.println("5. Relatórios e Consultas");
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
