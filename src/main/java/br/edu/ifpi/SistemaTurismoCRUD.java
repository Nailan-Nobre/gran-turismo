package br.edu.ifpi;

import br.edu.ifpi.service.TurismoService;
import br.edu.ifpi.util.Cores;
import java.util.Scanner;
import java.io.InputStream;
import java.util.logging.LogManager;

public class SistemaTurismoCRUD {
    
    private static TurismoService turismoService;
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        configurarLogs();
        
        System.out.println(Cores.separador("=", 50));
        System.out.println(Cores.titulo("   SISTEMA DE TURISMO GRAN TURISMO"));
        System.out.println(Cores.separador("=", 50));
        System.out.print(Cores.info("Inicializando sistema"));
        
        try {
            System.out.print(".");
            turismoService = TurismoService.getInstance();
            System.out.print(".");
            System.out.println(Cores.sucesso(" OK!"));
            System.out.println(Cores.sucesso("Sistema pronto para uso!\n"));
            menuPrincipal();
        } catch (Exception e) {
            System.out.println(Cores.erro(" FALHOU!"));
            System.err.println(Cores.erro("\nERRO: Não foi possível iniciar o sistema."));
            
            String mensagem = e.getMessage();
            if (mensagem != null && mensagem.contains("Communications link failure")) {
                System.err.println(Cores.aviso("Motivo: ") + "Não foi possível conectar ao banco de dados.");
                System.err.println(Cores.info("Solução: ") + "Verifique se o MySQL está rodando e as credenciais estão corretas.");
            } else if (mensagem != null && mensagem.contains("Access denied")) {
                System.err.println(Cores.aviso("Motivo: ") + "Usuário ou senha incorretos.");
                System.err.println(Cores.info("Solução: ") + "Verifique as credenciais no arquivo persistence.xml");
            } else if (mensagem != null && mensagem.contains("Unknown database")) {
                System.err.println(Cores.aviso("Motivo: ") + "Banco de dados 'gran_turismo' não encontrado.");
                System.err.println(Cores.info("Solução: ") + "Execute o script 'criar_banco_completo.sql' primeiro.");
            } else {
                System.err.println(Cores.aviso("Motivo: ") + (mensagem != null ? mensagem : "Erro desconhecido"));
            }
            System.err.println(Cores.info("\nPor favor, corrija o problema e tente novamente."));
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
            System.out.println("\n" + Cores.separador("=", 50));
            System.out.println(Cores.titulo("     GRAN TURISMO - MENU PRINCIPAL"));
            System.out.println(Cores.separador("=", 50));
            System.out.println(Cores.info("[1]") + " Gerenciar Clientes");
            System.out.println(Cores.info("[2]") + " Gerenciar Destinos");
            System.out.println(Cores.info("[3]") + " Gerenciar Voos");
            System.out.println(Cores.info("[4]") + " Gerenciar Hospedagens");
            System.out.println(Cores.info("[5]") + " Processar Pagamentos");
            System.out.println(Cores.info("[6]") + " Pacotes Turísticos");
            System.out.println(Cores.info("[7]") + " Relatórios e Consultas");
            System.out.println(Cores.VERMELHO_BRILHANTE + "[0]" + Cores.RESET + " Sair");
            System.out.println(Cores.separador("=", 50));
            System.out.print(Cores.input("Escolha uma opção: "));
            
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
                    System.out.println(Cores.sucesso("\nObrigado por usar o Gran Turismo!"));
                    return;
                default:
                    System.out.println(Cores.erro("Opção inválida! Tente novamente."));
            }
        }
    }
    
    private static void menuRelatorios() {
        while (true) {
            System.out.println("\n" + Cores.titulo("=== RELATÓRIOS E CONSULTAS ==="));
            System.out.println(Cores.info("[1]") + " Total de Clientes Cadastrados");
            System.out.println(Cores.info("[2]") + " Total de Destinos por País");
            System.out.println(Cores.info("[3]") + " Listar Todos os Voos");
            System.out.println(Cores.info("[4]") + " Listar Todas as Hospedagens");
            System.out.println(Cores.info("[0]") + " Voltar");
            System.out.print(Cores.input("Escolha uma opção: "));
            
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
                    System.out.println(Cores.erro("Opção inválida!"));
            }
        }
    }
}
