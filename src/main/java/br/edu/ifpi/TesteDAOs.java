package br.edu.ifpi;

import br.edu.ifpi.Model.*;
import br.edu.ifpi.dao.*;

/**
 * Classe para testar os DAOs
 */
public class TesteDAOs {
    
    public static void main(String[] args) {
        System.out.println("=== Testando DAOs ===\n");
        
        // Criar instâncias dos DAOs
        ClienteDAO clienteDAO = new ClienteDAO();
        DestinoDAO destinoDAO = new DestinoDAO();
        VooDAO vooDAO = new VooDAO();
        HospedagemDAO hospedagemDAO = new HospedagemDAO();
        
        try {
            // 1. Testando Cliente
            System.out.println("1. Testando ClienteDAO...");
            Cliente cliente = new Cliente("João Silva", "joao@email.com", "12345678901");
            clienteDAO.salvar(cliente);
            System.out.println("✅ Cliente salvo: " + cliente.getNome());
            
            // Buscar cliente por CPF
            Cliente clienteEncontrado = clienteDAO.buscarPorCpf("12345678901");
            if (clienteEncontrado != null) {
                System.out.println("✅ Cliente encontrado por CPF: " + clienteEncontrado.getNome());
            }
            
            // 2. Testando Destino
            System.out.println("\n2. Testando DestinoDAO...");
            Destino destino1 = new Destino("Paris", "França");
            Destino destino2 = new Destino("Rio de Janeiro", "Brasil");
            destinoDAO.salvar(destino1);
            destinoDAO.salvar(destino2);
            System.out.println("✅ Destinos salvos: " + destino1.getDescricao() + " e " + destino2.getDescricao());
            
            // 3. Testando Voo
            System.out.println("\n3. Testando VooDAO...");
            Voo voo1 = new Voo("LATAM", "São Paulo", "Paris", 2500.00);
            Voo voo2 = new Voo("GOL", "São Paulo", "Rio de Janeiro", 350.00);
            vooDAO.salvar(voo1);
            vooDAO.salvar(voo2);
            System.out.println("✅ Voos salvos: " + voo1.getDescricaoServico() + " e " + voo2.getDescricaoServico());
            
            // 4. Testando Hospedagem
            System.out.println("\n4. Testando HospedagemDAO...");
            Hospedagem hospedagem1 = new Hospedagem("Hotel Paris Luxo", 7, 300.00);
            Hospedagem hospedagem2 = new Hospedagem("Copacabana Palace", 5, 800.00);
            hospedagemDAO.salvar(hospedagem1);
            hospedagemDAO.salvar(hospedagem2);
            System.out.println("✅ Hospedagens salvas: " + hospedagem1.getDescricaoServico() + " e " + hospedagem2.getDescricaoServico());
            
            // 5. Testando consultas
            System.out.println("\n5. Testando consultas...");
            System.out.println("📋 Total de clientes: " + clienteDAO.listarTodos().size());
            System.out.println("📋 Total de destinos: " + destinoDAO.listarTodos().size());
            System.out.println("📋 Total de voos: " + vooDAO.listarTodos().size());
            System.out.println("📋 Total de hospedagens: " + hospedagemDAO.listarTodos().size());
            
            // Consultas específicas
            System.out.println("\n6. Consultas específicas...");
            System.out.println("✈️  Voos para Paris: " + vooDAO.buscarPorDestino("Paris").size());
            System.out.println("🏨 Hospedagens com preço até R$ 500: " + hospedagemDAO.buscarPorPrecoMaximo(500.00).size());
            System.out.println("🌍 Destinos no Brasil: " + destinoDAO.buscarPorPais("Brasil").size());
            
            System.out.println("\n🎉 Todos os testes executados com sucesso!");
            
        } catch (Exception e) {
            System.err.println("❌ Erro durante os testes: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Fechar EntityManagerFactory
            GenericDAOImpl.fecharEntityManagerFactory();
        }
    }
}
