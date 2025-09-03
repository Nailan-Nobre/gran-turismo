package br.edu.ifpi;

import br.edu.ifpi.service.TurismoService;
import java.util.Scanner;

/**
 * Aplicação principal do sistema de turismo com operações CRUD completas
 */
public class SistemaTurismoCRUD {
    
    private static TurismoService turismoService = new TurismoService();
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("🌍 === SISTEMA DE TURISMO GRAN TURISMO === 🌍");
        System.out.println("Sistema completo com operações CRUD");
        
        try {
            menuPrincipal();
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        } finally {
            turismoService.fecharRecursos();
            if (scanner != null) {
                scanner.close();
            }
        }
    }
    
    private static void menuPrincipal() {
        while (true) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("🌍 GRAN TURISMO - MENU PRINCIPAL");
            System.out.println("=".repeat(50));
            System.out.println("1. 👤 Gerenciar Clientes");
            System.out.println("2. 🌍 Gerenciar Destinos");
            System.out.println("3. ✈️  Gerenciar Voos");
            System.out.println("4. 🏨 Gerenciar Hospedagens");
            System.out.println("5. 📊 Relatórios e Consultas");
            System.out.println("6. 🎯 Sistema Original (Demo)");
            System.out.println("0. 🚪 Sair");
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
                    menuVoos();
                    break;
                case "4":
                    menuHospedagens();
                    break;
                case "5":
                    menuRelatorios();
                    break;
                case "6":
                    sistemaOriginal();
                    break;
                case "0":
                    System.out.println("👋 Obrigado por usar o Gran Turismo!");
                    return;
                default:
                    System.out.println("❌ Opção inválida! Tente novamente.");
            }
        }
    }
    
    private static void menuVoos() {
        System.out.println("\n=== GERENCIAR VOOS ===");
        System.out.println("✈️  Funcionalidade em desenvolvimento...");
        System.out.println("📝 Em breve: Cadastrar, listar, buscar e gerenciar voos");
        System.out.print("Pressione ENTER para continuar...");
        scanner.nextLine();
    }
    
    private static void menuHospedagens() {
        System.out.println("\n=== GERENCIAR HOSPEDAGENS ===");
        System.out.println("🏨 Funcionalidade em desenvolvimento...");
        System.out.println("📝 Em breve: Cadastrar, listar, buscar e gerenciar hospedagens");
        System.out.print("Pressione ENTER para continuar...");
        scanner.nextLine();
    }
    
    private static void menuRelatorios() {
        System.out.println("\n=== RELATÓRIOS E CONSULTAS ===");
        System.out.println("📊 Funcionalidade em desenvolvimento...");
        System.out.println("📝 Em breve: Relatórios de clientes, destinos mais procurados, etc.");
        System.out.print("Pressione ENTER para continuar...");
        scanner.nextLine();
    }
    
    private static void sistemaOriginal() {
        System.out.println("\n=== SISTEMA ORIGINAL (DEMO) ===");
        System.out.println("🎯 Esta é uma demonstração do sistema original...");
        System.out.println("📝 Funcionalidade mantida para referência");
        System.out.print("Pressione ENTER para continuar...");
        scanner.nextLine();
        
        // Aqui você pode chamar o sistema original se quiser
        // br.edu.ifpi.Model.Main.main(new String[]{});
    }
}
